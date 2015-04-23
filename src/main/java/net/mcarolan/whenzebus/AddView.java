package net.mcarolan.whenzebus;

import java.util.Set;

import com.google.common.collect.Sets;

import net.mcarolan.whenzebus.api.Client;
import net.mcarolan.whenzebus.api.Response;
import net.mcarolan.whenzebus.api.UnknownBusStop;
import net.mcarolan.whenzebus.api.field.Fields;
import net.mcarolan.whenzebus.api.value.StopCode1;
import net.mcarolan.whenzebus.api.value.StopPointName;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddView extends ActionBarActivity {
	
	private static final String TAG = "AddView";

	private Button addButton;
	private EditText smsCode;
	private TextView addErrorMessage;
	
	private final WhenZeBusDAL dal = new WhenZeBusDAL(this);
	private final Client client = new Client("http://countdown.api.tfl.gov.uk");
	
	private class LookupBusInformationResult {
		private final Throwable error;
		private final Set<Response> responses;
		private final boolean isSuccess;
		
		public LookupBusInformationResult(Throwable error,
				Set<Response> responses, boolean isSuccess) {
			this.error = error;
			this.responses = responses;
			this.isSuccess = isSuccess;
		}
		
	}
	
	private class LookupBusInformation extends AsyncTask<Void, Void, LookupBusInformationResult> {
		
		private StopCode1 stopCode1;

		@Override
		protected LookupBusInformationResult doInBackground(Void... params) {
			try {
				stopCode1 = new StopCode1(smsCode.getText().toString());
				final Set<Response> result = client.getResponses(stopCode1, true, Client.BUS_INFORMATION_FIELDS);
				return new LookupBusInformationResult(null, result, true);
			}
			catch (Exception e) {
				return new LookupBusInformationResult(e, null, false);
			}
		}

		@Override
		protected void onPostExecute(LookupBusInformationResult result) {
			if (result.isSuccess) {
				if (result.responses.size() != 1) {
					AddView.this.showError(getResources().getString(R.string.adddialog_invalid_response));
					AddView.this.enableAddButton();
				}
				else {
					final Response first = result.responses.iterator().next();
					Log.i(TAG, "Response =  " + first.toString());
					dal.addBusStop(BusStop.fromResponse(first));
					AddView.this.finish();
				}
			}
			else {
				Log.e(TAG, "Could not look up bus information", result.error);
				if (result.error instanceof UnknownBusStop) {
					AddView.this.showError(getResources().getString(R.string.adddialog_bad_smscode));
					AddView.this.enableAddButton();
					
				}
				else {
					AddView.this.showError(getResources().getString(R.string.adddialog_could_not_communicate));
					AddView.this.enableAddButton();
				}
			}
		}
		
	}
	
	private void showError(String message) {
		addErrorMessage.setText(message);
		addErrorMessage.setVisibility(View.VISIBLE);
	}
	
	private void enableAddButton() {
		addButton.setEnabled(true);
		addButton.setText(getResources().getString(R.string.add_button));
	}
	
	private void disableAddButton() {
		addButton.setEnabled(false);
		addButton.setText(getResources().getString(R.string.add_button_pressed));
	}
	
	private View.OnClickListener onAddClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (dal.countBusStopsWith(new StopCode1(smsCode.getText().toString())) > 0) {
				addErrorMessage.setText(getResources().getString(R.string.add_already_present));
				addErrorMessage.setVisibility(View.VISIBLE);
			}
			else {
				AddView.this.disableAddButton();
				new LookupBusInformation().execute();
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.addContainer, new PlaceholderFragment())
                    .commit();
        }
	}

	public class PlaceholderFragment extends Fragment {
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add, container, false);
			addButton = (Button) rootView.findViewById(R.id.addButton);
			addButton.setOnClickListener(onAddClick);
			smsCode = (EditText) rootView.findViewById(R.id.smsCode);
			addErrorMessage = (TextView) rootView.findViewById(R.id.addErrorMessage);
			return rootView;
		}
		
	}
	
}

package net.mcarolan.whenzebus;

import java.util.Set;

import com.google.common.collect.Lists;
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

	private static final Client client = new Client("http://countdown.api.tfl.gov.uk");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.addContainer, new AddFragment())
                    .commit();
        }
	}

	public static class AddFragment extends Fragment {
		
		private class LookupBusInformation extends AsyncTask<Void, Void, ClientResult> {
		
			private StopCode1 stopCode1;

			@Override
			protected ClientResult doInBackground(Void... params) {
				try {
					stopCode1 = new StopCode1(getSmsCode().getText().toString());
					final Set<Response> result = client.getResponses(stopCode1, true, Client.BUS_INFORMATION_FIELDS);
					return new ClientResult(true, result, null);
				}
				catch (Exception e) {
					return new ClientResult(false, Sets.<Response>newHashSet(), e);
				}
			}

			@Override
			protected void onPostExecute(ClientResult result) {
				if (result.isSuccess()) {
					if (result.getResponses().size() != 1) {
						showError(getResources().getString(R.string.adddialog_invalid_response));
						enableAddButton();
					}
					else {
						final WhenZeBusDAL dal = new WhenZeBusDAL(getActivity());
						final Response first = result.getResponses().iterator().next();
						Log.i(TAG, "Response =  " + first.toString());
						dal.addBusStop(BusStop.fromResponse(first));
						getActivity().finish();
					}
				}
				else {
					Log.e(TAG, "Could not look up bus information", result.error);
					if (result.getError() instanceof UnknownBusStop) {
						showError(getResources().getString(R.string.adddialog_bad_smscode));
						enableAddButton();
						
					}
					else {
						showError(getResources().getString(R.string.adddialog_could_not_communicate));
						enableAddButton();
					}
				}
			}
		
		}

		private View.OnClickListener onAddClick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final WhenZeBusDAL dal = new WhenZeBusDAL(AddFragment.this.getActivity());
				if (dal.countBusStopsWith(new StopCode1(AddFragment.this.getSmsCode().getText().toString())) > 0) {
					AddFragment.this.showError(getResources().getString(R.string.add_already_present));
				} else {
					AddFragment.this.disableAddButton();
					new LookupBusInformation().execute();
				}
			}
		};

		private void showError(String message) {
			getErrorMessage().setText(message);
			getErrorMessage().setVisibility(View.VISIBLE);
		}
	
		private void enableAddButton() {
			getAddButton().setEnabled(true);
			getAddButton().setText(getResources().getString(R.string.add_button));
		}
	
		private void disableAddButton() {
			getAddButton().setEnabled(false);
			getAddButton().setText(getResources().getString(R.string.add_button_pressed));
		}

		private Button getAddButton() {
			return (Button) getActivity().findViewById(R.id.addButton);
		}

		private EditText getSmsCode() {
			return (EditText) getActivity().findViewById(R.id.smsCode);
		}

		private TextView getErrorMessage() {
			return (TextView) getActivity().findViewById(R.id.addErrorMessage);
		}
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_add, container, false);
			final Button addButton = (Button) rootView.findViewById(R.id.addButton);
			addButton.setOnClickListener(onAddClick);
			return rootView;
		}
		
	}
	
}

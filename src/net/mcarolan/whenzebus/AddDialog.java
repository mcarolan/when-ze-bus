package net.mcarolan.whenzebus;

import java.util.Set;

import com.google.common.collect.Sets;

import net.mcarolan.whenzebus.api.Client;
import net.mcarolan.whenzebus.api.Response;
import net.mcarolan.whenzebus.api.StopCode1;
import net.mcarolan.whenzebus.api.StopPointName;
import net.mcarolan.whenzebus.api.UnknownBusStop;
import net.mcarolan.whenzebus.api.field.Fields;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddDialog extends DialogFragment {
	
	private final Activity context;
	private final WhenZeBusDAL dal;
	private final Client client;
	
	private static final String TAG = "AddDialog";
	
	public AddDialog(Activity context, WhenZeBusDAL whereZeBusDAL, Client client) {
		this.context = context;
		this.dal = whereZeBusDAL;
		this.client = client;
	}
	
	private class OkClick implements Button.OnClickListener {
		
		private final EditText smsCode;
		private final TextView errorTextView;
		private final Dialog dialog;
		
		public OkClick(EditText smsCode, TextView errorTextView, Dialog dialog) {
			this.smsCode = smsCode;
			this.errorTextView = errorTextView;
			this.dialog = dialog;
		}
		
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
					final Set<Response> result = client.getResponses(stopCode1, true, Sets.newHashSet(Fields.StopPointName));
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
						errorTextView.setText(getResources().getString(R.string.adddialog_invalid_response));
						errorTextView.setVisibility(View.VISIBLE);
					}
					else {
						final Response first = result.responses.iterator().next();
						final String stopPointName = Fields.StopPointName.extract(first);
						Log.i(TAG, "StopPointName " + stopPointName);
						dal.addBusStop(stopCode1,  new StopPointName(stopPointName));
						dialog.dismiss();
					}
				}
				else {
					Log.e(TAG, "Could not look up bus information", result.error);
					if (result.error instanceof UnknownBusStop) {
						errorTextView.setText(getResources().getString(R.string.adddialog_bad_smscode));
						errorTextView.setVisibility(View.VISIBLE);
					}
					else {
						errorTextView.setText(getResources().getString(R.string.adddialog_could_not_communicate));
						errorTextView.setVisibility(View.VISIBLE);
					}
				}
			}
			
		}

		@Override
		public void onClick(View v) {
			new LookupBusInformation().execute();
		}
		
	}
	
	private class CancelClick implements Button.OnClickListener {
		
		private final Dialog dialog;
		

		public CancelClick(Dialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
		
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	final LayoutInflater layoutInflater = context.getLayoutInflater();
    	final View view = layoutInflater.inflate(R.layout.bus_stop_entry, null);
    	
    	final EditText smsCode = (EditText) view.findViewById(R.id.smscode);
    	final TextView errorTextView = (TextView) view.findViewById(R.id.unknownBusStop);
    	final Button okButton = (Button) view.findViewById(R.id.okButton);
    	final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
    	builder.setView(view);
    	
    	final Dialog dialog = builder.create();
    	
    	okButton.setOnClickListener(new OkClick(smsCode, errorTextView, dialog));
    	cancelButton.setOnClickListener(new CancelClick(dialog));
            
    	return dialog;
	}
}
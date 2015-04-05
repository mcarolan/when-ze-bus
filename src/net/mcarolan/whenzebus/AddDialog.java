package net.mcarolan.whenzebus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddDialog extends DialogFragment {
	
	private final Activity context;
	private final WhenZeBusDAL dal;
	
	public AddDialog(Activity context, WhenZeBusDAL whereZeBusDAL) {
		this.context = context;
		this.dal = whereZeBusDAL;
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	final LayoutInflater layoutInflater = context.getLayoutInflater();
    	final View view = layoutInflater.inflate(R.layout.bus_stop_entry, null);
    	final EditText smsCode = (EditText) view.findViewById(R.id.smscode);
    	
    	builder.setView(view)
    		   .setPositiveButton("Add", new DialogInterface.OnClickListener() {
    			   @Override
                   public void onClick(DialogInterface dialog, int id) {
    				   
                   }

    		   }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    			   @Override
                   public void onClick(DialogInterface dialog, int id) {
    				   
                   }

    		   });
    	
    	return builder.create();
	}
	
}
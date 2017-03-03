package br.gbizotto.customcamera.camera2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Gabriela on 02/03/2017.
 */

public class ErrorDialog extends DialogFragment {

    public static final String FRAGMENT_DIALOG = "dialog";

    private static final String ARG_MESSAGE = "message";

    public static ErrorDialog newInstance(String message) {
        ErrorDialog dialog = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        return new AlertDialog.Builder(activity)
                .setMessage(getArguments().getString(ARG_MESSAGE))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                })
                .create();
    }
}

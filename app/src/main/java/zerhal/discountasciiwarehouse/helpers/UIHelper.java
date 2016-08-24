package zerhal.discountasciiwarehouse.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import zerhal.discountasciiwarehouse.R;

/**
 * Created by sameer on 16-08-23.
 */
public class UIHelper {
    public static void showAlertDialogForError(Exception e, Context context){
        new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage(e.getMessage())
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

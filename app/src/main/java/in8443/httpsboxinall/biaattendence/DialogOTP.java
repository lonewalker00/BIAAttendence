package in8443.httpsboxinall.biaattendence;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogOTP extends AppCompatDialogFragment {

    EditText et_otp;
    DialogOTPListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        super.setCancelable(false);
        View view = inflater.inflate(R.layout.dialog_otp, null);
        builder.setView(view).setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        et_otp = (EditText)view.findViewById(R.id.otp);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = et_otp.getText().toString();
                if ( otp.trim().length()==0 )
                    et_otp.setError("Field is Empty !");
                else if(otp.length()!=6)
                    et_otp.setError("Enter valid 6 digits OTP");
                else {
                    listener.applyTexts(otp);
                    alertDialog.dismiss();
                }
            }
        });
        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogOTPListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Must implement listener");

        }
    }

    public interface DialogOTPListener{
        void applyTexts(String otp);
    }


}
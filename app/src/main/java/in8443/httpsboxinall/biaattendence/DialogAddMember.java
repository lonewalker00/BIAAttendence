package in8443.httpsboxinall.biaattendence;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.sip.SipAudioCall;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.zip.Inflater;

public class DialogAddMember extends AppCompatDialogFragment {

    EditText et_name, et_user_password;
    DialogAddMemberListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        super.setCancelable(false);
        View view = inflater.inflate(R.layout.dialog_add_member, null);
        builder.setView(view).setTitle("Add Member").setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        et_name = (EditText)view.findViewById(R.id.add_username);
        et_user_password = (EditText)view.findViewById(R.id.add_password);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_name.getText().toString();
                String pass = et_user_password.getText().toString();
                if ( name.trim().length()==0 )
                    et_name.setError("Field is Empty !");
                if ( pass.trim().length()==0 )
                    et_user_password.setError("Field is Empty !");
                else if(pass.length()!=4)
                    et_user_password.setError("Enter 4 digits password !");
                else {
                    listener.applyTexts(name, pass);
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
            listener = (DialogAddMemberListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Must implement listener");

        }
    }

    public interface DialogAddMemberListener{
        void applyTexts(String name, String password);
    }

}
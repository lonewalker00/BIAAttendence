package in8443.httpsboxinall.biaattendence;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DialogConfirmation extends AppCompatDialogFragment {

    TextView tv_name, tv_timeIn, tv_timeOut;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        super.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirmation, null);
        Bundle b = getArguments();
        String name = b.getString("name");
        String timein = b.getString("timein");
        String timeout = b.getString("timeout");

        tv_name = (TextView)view.findViewById(R.id.textView4);
        tv_timeIn = (TextView)view.findViewById(R.id.textView5);
        tv_timeOut = (TextView)view.findViewById(R.id.textView6);

        if (timeout.equals("")) {
            builder.setView(view).setTitle("Time in Marked").setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
        } else {
            builder.setView(view).setTitle("Time out Marked").setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
        }
        tv_name.setText(name);
        tv_timeIn.setText("Time in:    "+timein);
        if (timeout.equals("")) tv_timeOut.setText(timeout);
        else tv_timeOut.setText("Time out: "+timeout);
        //title, name, timein, timeout, marginTop
        //ALTER TABLE timings ADD CONSTRAINT id FOREIGN KEY (id) REFERENCES attendence (id) ON DELETE CASCADE;
        return builder.create();
    }
}
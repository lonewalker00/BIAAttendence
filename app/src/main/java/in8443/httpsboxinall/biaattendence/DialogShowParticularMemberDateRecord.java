package in8443.httpsboxinall.biaattendence;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DialogShowParticularMemberDateRecord extends AppCompatDialogFragment {

    ListView listView;
    String memberName, memberId, dateFrom, dateTo;
    String totalDates [], presentDates [], timeIn[], timeOut[];
    int imageId[];

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        super.setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_particular_member_date_record, null);

        Bundle b = getArguments();
        memberName = b.getString("name");
        memberId = b.getString("id");
        dateFrom = b.getString("DateFrom");
        dateTo = b.getString("DateTo");
        totalDates = b.getStringArray("totalDates");
        presentDates = b.getStringArray("presentDates");
        timeIn = b.getStringArray("timeIn");
        timeOut = b.getStringArray("timeOut");
        imageId = b.getIntArray("image");

        /*Toast.makeText(getContext(), dateFrom+dateTo+Arrays.toString(totalDates)+"\n"+Arrays.toString(presentDates)+"\n"+
                Arrays.toString(timeIn)+"\n"+Arrays.toString(timeOut),
                Toast.LENGTH_LONG).show();*/

        listView = (ListView)view.findViewById(R.id.listView3);

        /*tv_name.setText(tv_name.getText().toString()+"\n"+Arrays.toString(totalDates)+"\n"+Arrays.toString(presentDates)+
                "\n"+Arrays.toString(timeIn)+"\n"+Arrays.toString(timeOut));*/
        //Toast.makeText(getContext(), "jhcjcj", Toast.LENGTH_LONG).show();

        AdapterMemberAttendenceRecord adapter = new AdapterMemberAttendenceRecord(getContext(),
                R.layout.item_member_attendence_record, totalDates, timeIn, timeOut, imageId);
        listView.setAdapter(adapter);

        builder.setView(view).setTitle(/*Arrays.toString(totalDates)+"\n"+Arrays.toString(presentDates)+ "\n"+
                Arrays.toString(timeIn)+"\n"+Arrays.toString(timeOut)+*/memberId+".\t\t"+memberName).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }

}
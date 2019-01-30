package in8443.httpsboxinall.biaattendence;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterMemberAttendenceRecord extends ArrayAdapter<String> {
    String totalDates [], timeIn[], timeOut[];
    int imageId[];
    Context context;

    public AdapterMemberAttendenceRecord(@NonNull Context context, int resource, String [] totalDates, String [] timeIn, String [] timeOut,
                                   int [] imageId) {
        super(context,resource,totalDates);
        this.context = context;
        this.totalDates = totalDates;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.imageId = imageId;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        convertView= LayoutInflater.from(context).inflate(R.layout.item_member_attendence_record,parent,false );

        TextView tv1,tv2,tv3;
        ImageView imageView;
        tv1= (TextView) convertView.findViewById(R.id.mem_dates);
        tv2= (TextView) convertView.findViewById(R.id.mem_timein);
        tv3= (TextView) convertView.findViewById(R.id.mem_timeout);
        imageView = (ImageView)convertView.findViewById(R.id.attendence_status);

        tv1.setText(totalDates[position]);
        tv2.setText(timeIn[position]);
        tv3.setText(timeOut[position]);
        imageView.setImageResource(imageId[position]);

        return convertView;

    }
}
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

public class AdapterTodayPresentList extends ArrayAdapter<String> {
    String [] name;
    String [] date;
    String [] timeIn;
    String [] timeOut;
    int [] imageId;
    Context context;

    public AdapterTodayPresentList(@NonNull Context context, int resource, String [] name, String [] timeIn, String [] timeOut,
                                   int [] imageId) {
        super(context,resource,name);
        this.context = context;
        this.name = name;
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


        convertView= LayoutInflater.from(context).inflate(R.layout.item_list_today_attendence,parent,false );

        TextView tv1,tv2,tv3;
        ImageView imageView;
        tv1= (TextView) convertView.findViewById(R.id.name);
        tv2= (TextView) convertView.findViewById(R.id.timein);
        tv3= (TextView) convertView.findViewById(R.id.timeout);
        imageView = (ImageView)convertView.findViewById(R.id.imageView2);
        if (timeIn[position].equals("null")){
            timeIn[position] = "    - -  ";
        }
        if (timeOut[position].equals("null")){
            timeOut[position] = "\t- -";
        }

        tv1.setText(name[position]);
        tv2.setText("Time in:  "+timeIn[position]);
        tv3.setText("Time out:  "+timeOut[position]);
        imageView.setImageResource(imageId[position]);

        return convertView;

    }
}
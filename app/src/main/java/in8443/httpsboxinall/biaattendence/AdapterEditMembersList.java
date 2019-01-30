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

public class AdapterEditMembersList extends ArrayAdapter<String> {
    String [] name;
    String [] idno;
    Context context;

    public AdapterEditMembersList(@NonNull Context context, int resource, String [] name, String [] idno) {
        super(context,resource,name);
        this.context = context;
        this.name = name;
        this.idno = idno;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.item_list_edit_members,parent,false );

        TextView tv1;
        tv1= (TextView) convertView.findViewById(R.id.member_name);
        tv1.setText(idno[position]+".\t"+name[position]);
        return convertView;
    }
}
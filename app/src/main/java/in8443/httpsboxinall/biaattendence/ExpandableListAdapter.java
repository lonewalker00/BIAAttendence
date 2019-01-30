package in8443.httpsboxinall.biaattendence;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends android.widget.BaseExpandableListAdapter {

    Context context;
    List<String> listDataHeader;
    HashMap<String, List<String>> listHashMap;
    TextView groupItemName, childItemName;
    ColorStateList oldColors;
    int paint;
    int flag = 0;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap ){
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1); // i = groupitem i1 = chi;d item
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if (view==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group_item, null);
        }
        groupItemName = (TextView)view.findViewById(R.id.group_item_name);
        groupItemName.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String childText = (String)getChild(i, i1);
        if (view==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_child_items, null);
        }
        childItemName = (TextView)view.findViewById(R.id.child_item_name);
        childItemName.setText(childText);
        childItemName.setTextSize(18);
        if (flag==0) {
            oldColors = childItemName.getTextColors();
            paint = childItemName.getPaintFlags();
        }
        flag++;
        childItemName.setTextColor(oldColors);
        childItemName.setPaintFlags(paint);
        if (i1==4) { //if (listHashMap.get(listDataHeader.get(i)).size()-1==i1)
            childItemName.setTextSize(13);
            childItemName.setTextColor(Color.BLUE);
            childItemName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
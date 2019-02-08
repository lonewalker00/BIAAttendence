package in8443.httpsboxinall.biaattendence;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;

public class MainActivity5_customAttendenceRecord extends AppCompatActivity {

    Button btn_from, btn_to;
    ImageButton btn_img_change, btn_img_confirm;
    TextView tv_from, tv_to;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listHashMap;
    DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;
    Calendar calendar = null;
    int year, month, day, y, m, d, yfrom, mfrom, dfrom, yto, mto, dto;
    ProgressDialog progressDialog;
   /* Connection con = null;
    String sqlURL = null;  //"jdbc:mysql://192.168.43.132:3306/bia";
    String sqlUser = null; //"root";
    String sqlPass = null; //"";*/
    String name [], id [], todayAttendence [], presentAttendence [];
    String totalDates [], presentDates [], timeIn[], timeOut[];
    int imgaeId[];
    Bundle b;
    String minDate;
    long millis;
    Boolean isSuccess1 = true;
    Boolean isSuccess2 = true;
    Boolean isSuccess3 = true;
    Boolean isSuccess4 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity5_custom_attendence_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for back button// for hide keyboard on startup
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("View by Date");
        /*getSupportActionBar().setLogo(R.drawable.main_logo_5);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        sqlURL = getResources().getString(R.string.url);
        sqlUser = getResources().getString(R.string.user);
        sqlPass = getResources().getString(R.string.pass);*/
        btn_from = (Button)findViewById(R.id.button8);
        btn_to = (Button)findViewById(R.id.button9);
        btn_img_change = (ImageButton)findViewById(R.id.imageButton);
        btn_img_confirm = (ImageButton)findViewById(R.id.imageButton2);
        tv_from = (TextView)findViewById(R.id.textView7);
        tv_to = (TextView)findViewById(R.id.textView8);
        progressDialog = new ProgressDialog(this);
        expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);

        if (!isOnline()){
            Toast.makeText(MainActivity5_customAttendenceRecord.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
        } else {
            view_members_list();
            getMinDate_in_fromDatePicker();
        }

        long date1 = System.currentTimeMillis();
        long date2 = System.currentTimeMillis();
        long date3 = System.currentTimeMillis();
        y = Integer.parseInt(new SimpleDateFormat("yyyy").format(date1));
        m = Integer.parseInt(new SimpleDateFormat("MM").format(date2));
        d = Integer.parseInt(new SimpleDateFormat("dd").format(date3));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance(Locale.ENGLISH);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        btn_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity5_customAttendenceRecord.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener1, year+y, month+m-1, day+d);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-86400421);
                datePickerDialog.getDatePicker().setMinDate(millis); // method is made at the end.
                // convert miliseconds to date use this:--  https://codechi.com/dev-tools/date-to-millisecond-calculators/
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                tv_from.setText(day+"-"+month+"-"+year);
                yfrom = year;       mfrom = month;  dfrom = day;
            }
        };
        btn_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String frm = tv_from.getText().toString();
                if (frm.equals("Select date")){
                    Toast.makeText(MainActivity5_customAttendenceRecord.this, "Please select from date", Toast.LENGTH_LONG).show();
                } else {
                    String temp = yfrom+"-"+mfrom+"-"+dfrom;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    try {c.setTime(sdf.parse(temp));} catch (ParseException e) {e.printStackTrace();}
                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity5_customAttendenceRecord.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener2, year + y, month + m - 1, day + d);
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime()/*Long.parseLong("1547231460000")*/);
                    // convert miliseconds to date use this:--  https://codechi.com/dev-tools/date-to-millisecond-calculators/
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
            }
        });
        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                tv_to.setText(day+"-"+month+"-"+year);
                yto = year;        mto = month;           dto = day;
            }
        };
        btn_img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = tv_from.getText().toString();
                String to = tv_to.getText().toString();
                tv_from.setText(to);
                tv_to.setText(from);
                int temp;
                temp = yto;    yto = yfrom;     yfrom = temp;
                temp = mto;    mto = mfrom;     mfrom = temp;
                temp = dto;    dto = dfrom;     dfrom = temp;

            }
        });
        btn_img_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()){
                    Toast.makeText(MainActivity5_customAttendenceRecord.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                } else {
                    String from = tv_from.getText().toString();
                    String to = tv_to.getText().toString();
                    Boolean check = true;
                    if (from.equals("Select date") || to.equals("Select date")) {
                        Toast.makeText(MainActivity5_customAttendenceRecord.this, "Please select date", Toast.LENGTH_LONG).show();
                    } else {
                        if (yto >= yfrom) {
                            if (mto >= mfrom) {
                                if (dto >= dfrom) {
                                    String f = yfrom + "-" + mfrom + "-" + dfrom;
                                    String t = yto + "-" + mto + "-" + dto;
                                    view_members_list_byDate(t, f);
                                } else check = false;
                            } else check = false;
                        } else check = false;
                    }
                    if (!check) {
                        Toast.makeText(MainActivity5_customAttendenceRecord.this, "Date should be Interchanged",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView,
                                        View view, int groupPosition, int childPosition, long id1) {
                if (childPosition==4) {
                    Boolean check = true;
                    if (!isOnline()){
                        Toast.makeText(MainActivity5_customAttendenceRecord.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                    } else {
                        if (yto >= yfrom) {
                            if (mto >= mfrom) {
                                if (dto >= dfrom) {
                                    String from = tv_from.getText().toString();
                                    String to = tv_to.getText().toString();
                                    b = new Bundle();
                                    b.putString("name", name[groupPosition]);
                                    b.putString("id", "" + id[groupPosition]);
                                    b.putString("DateFrom", yfrom + "-" + mfrom + "-" + dfrom);
                                    b.putString("DateTo", yto + "-" + mto + "-" + dto);
                                    if (from.equals("Select date") && to.equals("Select date")) {
                                        String dt = "" + new Date(System.currentTimeMillis());
                                        show_member_details("" + id[groupPosition], "created_at", dt);
                                    } else if (from.equals("Select date") && !to.equals("Select date")) {
                                        Toast.makeText(MainActivity5_customAttendenceRecord.this, "Please select dates", Toast.LENGTH_LONG).show();
                                    } else if (!from.equals("Select date") && to.equals("Select date")) {
                                        Toast.makeText(MainActivity5_customAttendenceRecord.this, "Please select dates", Toast.LENGTH_LONG).show();
                                    } else {
                                        show_member_details("" + id[groupPosition], yfrom + "-" + mfrom + "-" + dfrom, yto + "-" + mto + "-" + dto);
                                        /*Toast.makeText(MainActivity5_customAttendenceRecord.this,
                                                listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition) + "is selected from " +
                                                        id[groupPosition], Toast.LENGTH_LONG).show();*/
                                    }
                                } else check = false;
                            } else check = false;
                        } else check = false;
                        if (!check)
                            Toast.makeText(MainActivity5_customAttendenceRecord.this, "Date should be Interchanged", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
    }


    public void show_member_details(final String id, final String dateFrom, final String dateTo){
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/show_particular_member_details.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    totalDates = new String[jsonArray.length()];
                    presentDates = new String[jsonArray.length()];
                    timeIn = new String[jsonArray.length()];
                    timeOut = new String[jsonArray.length()];
                    imgaeId = new int[jsonArray.length()];
                    for (int i=0; i<jsonArray.length(); i++){
                        totalDates[i] = jsonArray.getJSONObject(i).getString("totalDates");
                        presentDates[i] = jsonArray.getJSONObject(i).getString("presentDates");
                        timeIn[i] = jsonArray.getJSONObject(i).getString("timeIn");
                        timeOut[i] = jsonArray.getJSONObject(i).getString("timeOut");
                    }
                    isSuccess1 = true;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isSuccess1 = false;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("from", dateFrom);
                map.put("to", dateTo);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity5_customAttendenceRecord.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                if (isSuccess1) {
                    for (int i = 0; i < totalDates.length; i++) {
                        if (!presentDates[i].equals("null")) {
                            imgaeId[i] = R.drawable.logo_2;
                            timeIn[i] = "Time in:  " + timeIn[i];
                            if (timeOut[i].equals("null")) timeOut[i] = "Time out:  " + "Not done";
                            else timeOut[i] = "Time out:  " + timeOut[i];
                        } else {
                            timeIn[i] = "";
                            timeOut[i] = "";
                            imgaeId[i] = R.drawable.logo_4;
                        }
                    }
                    b.putStringArray("totalDates", totalDates);
                    b.putStringArray("presentDates", presentDates);
                    b.putStringArray("timeIn", timeIn);
                    b.putStringArray("timeOut", timeOut);
                    b.putIntArray("image", imgaeId);
                    DialogShowParticularMemberDateRecord dialog = new DialogShowParticularMemberDateRecord();
                    dialog.setArguments(b);
                    dialog.show(getSupportFragmentManager(), "Dialog Member Record");
                } else Toast.makeText(MainActivity5_customAttendenceRecord.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
            }
        }, 4000);
    }


    public void view_members_list_byDate(final String to, final String from){
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/view_members_list_byDate.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        id[i] = jsonArray.getJSONObject(i).getString("id");
                        name[i] = jsonArray.getJSONObject(i).getString("name");
                        todayAttendence[i] = jsonArray.getJSONObject(i).getString("totalAttendence");
                        presentAttendence[i] = jsonArray.getJSONObject(i).getString("presentAttendence");
                    }
                    isSuccess2 = true;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isSuccess2 = false;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("from", from);
                map.put("to", to);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity5_customAttendenceRecord.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess2) {
                    init();
                    expandableListAdapter = new ExpandableListAdapter(MainActivity5_customAttendenceRecord.this,
                            listDataHeader, listHashMap);
                    expandableListView.setAdapter(expandableListAdapter);
                /*Toast.makeText(MainActivity5_customAttendenceRecord.this, Arrays.toString(id)+"\n"+ Arrays.toString(name)+"\n"+
                        Arrays.toString(todayAttendence)+"\n"+Arrays.toString(presentAttendence), Toast.LENGTH_LONG).show();*/
                } else Toast.makeText(MainActivity5_customAttendenceRecord.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        }, 4000);
    }


    public void view_members_list(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/view_members_list.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    id = new String[jsonArray.length()];
                    name = new String[jsonArray.length()];
                    todayAttendence = new String[jsonArray.length()];
                    presentAttendence = new String[jsonArray.length()];
                    for (int i=0; i<jsonArray.length(); i++){
                        id[i] = jsonArray.getJSONObject(i).getString("id");
                        name[i] = jsonArray.getJSONObject(i).getString("name");
                        todayAttendence[i] = jsonArray.getJSONObject(i).getString("totalAttendence");
                        presentAttendence[i] = jsonArray.getJSONObject(i).getString("presentAttendence");
                    }
                    isSuccess3 = true;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isSuccess3 = false;
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity5_customAttendenceRecord.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess3) {
                    init();
                    expandableListAdapter = new ExpandableListAdapter(MainActivity5_customAttendenceRecord.this,
                            listDataHeader, listHashMap);
                    expandableListView.setAdapter(expandableListAdapter);
                /*Toast.makeText(MainActivity5_customAttendenceRecord.this, Arrays.toString(id)+"\n"+ Arrays.toString(name)+"\n"+
                        Arrays.toString(todayAttendence)+"\n"+Arrays.toString(presentAttendence), Toast.LENGTH_LONG).show();*/
                } else Toast.makeText(MainActivity5_customAttendenceRecord.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        }, 4000);
    }


    public void init() {
        listDataHeader = new ArrayList<>();
        listHashMap = new HashMap<>();
        for (int i=0; i<name.length; i++){
            listDataHeader.add(id[i]+".\t\t"+name[i]);
            int AA = Integer.parseInt(todayAttendence[i]) - Integer.parseInt(presentAttendence[i]);
            double PER = (Double.parseDouble(presentAttendence[i]) / Double.parseDouble(todayAttendence[i])) * 100.0;
            List<String> p = new ArrayList<>();
            p.add("Total Attendence: \t"+todayAttendence[i]);
            p.add("Present:             \t\t\t\t"+presentAttendence[i]);
            p.add("Absent:               \t\t\t\t"+AA);
            p.add("Percentage:         \t\t\t"+String.format("%.2f", PER)+"%");
            p.add("Show full details");
            listHashMap.put(listDataHeader.get(i), p);
        }
    }


    public void getMinDate_in_fromDatePicker(){
        String url = "https://boxinall.in/BIAOfficeAttendence/getMinDate_in_fromDatePicker.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    minDate = jsonArray.getJSONObject(0).getString("minDate");
                    isSuccess4 = true;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isSuccess4 = false;
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity5_customAttendenceRecord.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess4) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdf.parse(minDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    millis = date.getTime();
                    //Toast.makeText(MainActivity5_customAttendenceRecord.this, minDate+"\n"+date+"\n"+millis, Toast.LENGTH_LONG).show();
                } else Toast.makeText(MainActivity5_customAttendenceRecord.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
            }
        }, 3700);
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable())
            return false;
        return true;
    }


}

 /*public class BackgroundTask extends AsyncTask<String, String, String> {
        Statement st;
        ResultSet rs;
        Boolean isSuccess = true;
        int countTotalEmployee = 0;

        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        public String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
                if (con == null) {
                    isSuccess = false;
                } else {
                    st = con.createStatement();
                    String query1 = "SELECT id, name FROM attendence ORDER BY id;";
                    rs = st.executeQuery(query1);
                    while (rs.next()){
                        countTotalEmployee++;
                    }

                    name = new String[countTotalEmployee];
                    id = new int[countTotalEmployee];
                    TA = new String[countTotalEmployee];
                    PA = new String[countTotalEmployee];

                    rs = st.executeQuery(query1);
                    for (int i=0; rs.next(); i++){
                        id[i] = rs.getInt("id");
                        name[i] = rs.getString("name");
                    }

                    String query2 = "SELECT DATEDIFF(CURRENT_TIMESTAMP,date(Created_at)) AS totalAttendence " +
                            "FROM attendence ORDER BY id;";
                    rs = st.executeQuery(query2);
                    for (int i=0; rs.next(); i++){
                        TA[i] = rs.getString("totalAttendence");
                        TA[i] = ""+(Integer.parseInt(TA[i]) + 1);
                    }

                    for (int i=0; i<id.length; i++) {
                        String query3 = "SELECT COUNT(*) AS presentAttendence FROM timings WHERE id = "+id[i]+" GROUP BY id;";
                        rs = st.executeQuery(query3);
                        if (rs.next()){
                            PA[i] = rs.getString("presentAttendence");
                        } else {
                            PA[i] = "0";
                        }
                    }

                    rs.close();
                    st.close();
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                isSuccess = false;
            } catch (Exception ex) {
                ex.printStackTrace();
                isSuccess = false;
            } finally {
                try{
                    if(st!=null)
                        st.close();
                }catch(SQLException se2){}
                try{
                    if(con!=null)
                        con.close();
                }catch(SQLException se){
                    se.printStackTrace();
                    isSuccess = false;
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String s) {
            progressDialog.hide();
            Toast.makeText(MainActivity5_customAttendenceRecord.this, Arrays.toString(id)+"\n"+ Arrays.toString(name)+"\n"+
                        Arrays.toString(todayAttendence)+"\n"+Arrays.toString(presentAttendence), Toast.LENGTH_LONG).show();
            if (isSuccess) {
                init();
                expandableListAdapter = new ExpandableListAdapter(MainActivity5_customAttendenceRecord.this,
                        listDataHeader, listHashMap);
                expandableListView.setAdapter(expandableListAdapter);
            } else {
                Toast.makeText(MainActivity5_customAttendenceRecord.this, "Some error occurred !!", Toast.LENGTH_LONG).show();
            }
        }
    }*/

 /*public class BackgroundTask2 extends AsyncTask<String, String, String> {

        Statement st;
        ResultSet rs;
        Boolean isSuccess = true;

        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        public String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
                if (con == null) {

                } else {
                    st = con.createStatement();
                    for (int i=0; i<id.length; i++){
                        Boolean z = false;
                        String query1 = "SELECT DATEDIFF('"+strings[0]+"','"+strings[1]+"') AS totalAttendence FROM attendence "
                                + "WHERE DATE('"+strings[1]+"') >= DATE(created_at) AND id = "+id[i]+";";
                        String query2 = "SELECT DATEDIFF('"+strings[0]+"',DATE(created_at)) AS totalAttendence FROM attendence " +
                                "WHERE id = "+id[i]+" AND DATE('"+strings[0]+"') >= DATE(created_at);";
                        rs = st.executeQuery(query1);
                        if (rs.next()){
                            todayAttendence[i] = rs.getString("totalAttendence");
                            z = true;
                        } else {
                            rs = st.executeQuery(query2);
                            if (rs.next()){
                                todayAttendence[i] = rs.getString("totalAttendence");
                                z = true;
                            }
                        }
                        if (z)
                            todayAttendence[i] = ""+(Integer.parseInt(todayAttendence[i]) + 1);
                        else
                            todayAttendence[i] = "0";
                    }

                    for (int i=0; i<id.length; i++) {
                        String query3 = "SELECT COUNT(*) AS presentAttendence FROM timings WHERE id = "+id[i]+" AND " +
                                "Date BETWEEN '"+strings[1]+"' AND '"+strings[0]+"' GROUP BY id";
                        rs = st.executeQuery(query3);
                        if (rs.next()){
                            presentAttendence[i] = rs.getString("presentAttendence");
                        } else {
                            presentAttendence[i] = "0";
                        }
                    }
                    rs.close();
                    st.close();
                    con.close();
                }
            }catch (SQLException se) {
                se.printStackTrace();
                isSuccess = false;
            } catch (Exception ex) {
                ex.printStackTrace();
                isSuccess = false;
            } finally {
                try{
                    if(st!=null)
                        st.close();
                }catch(SQLException se2){}
                try{
                    if(con!=null)
                        con.close();
                }catch(SQLException se){
                    se.printStackTrace();
                    isSuccess = false;
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String s) {
            progressDialog.hide();
            *//*Toast.makeText(MainActivity5_customAttendenceRecord.this, Arrays.toString(TA)+"\n"+ Arrays.toString(PA),
                    Toast.LENGTH_LONG).show();*//*
            if (isSuccess) {
                init();
                expandableListAdapter = new ExpandableListAdapter(MainActivity5_customAttendenceRecord.this,
                        listDataHeader, listHashMap);
                expandableListView.setAdapter(expandableListAdapter);
            } else {
                Toast.makeText(MainActivity5_customAttendenceRecord.this, "Some error occured !!", Toast.LENGTH_LONG).show();
            }
        }
    }
*/

    /*public class BackgroundTask3 extends AsyncTask<String, String, String> {

        Statement st;
        ResultSet rs;
        Boolean isSuccess = true;
        int countDates = 1;

        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        public String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
                if (con == null) {

                } else {
                    st = con.createStatement();
                    String query1;
                    if (strings[1].equals("created_at")) {
                        query1 = "SELECT DATE(" + strings[1] + ") AS dates FROM attendence WHERE id = " + strings[0] + ";";
                        // SELECT DATE(DATE(created_at)+1) AS dates FROM attendence where id = 7
                        // AND DATE(DATE(created_at)+1) < CURRENT_DATE
                    } else {
                        query1 = "SELECT DATE('" + strings[1] + "') AS dates FROM attendence WHERE id = " + strings[0] + ";";
                    }
                    rs = st.executeQuery(query1);
                    if (rs.next()){
                        String dt = rs.getString("dates");// Start date
                        String dt1 = dt;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sdf.parse(dt));

                        while (!dt.equals(strings[2])){
                            c.add(Calendar.DATE, 1);  // number of days to add
                            dt = sdf.format(c.getTime());
                            countDates++;
                        }

                        totalDates = new String[countDates];
                        presentDates = new String[countDates];
                        timeIn = new String[countDates];
                        timeOut = new String[countDates];
                        imgaeId = new int[countDates];

                        c.setTime(sdf.parse(dt1));
                        for (int i=0; i<countDates; i++){
                            totalDates[i] = dt1;
                            c.add(Calendar.DATE, 1);  // number of days to add
                            dt1 = sdf.format(c.getTime());
                        }
                    }

                    String query2;
                    if (strings[1].equals("created_at")) {
                        query2 = "SELECT Date, Time_in, Time_out FROM timings WHERE id = " + strings[0] + ";";
                    } else {
                        query2 = "SELECT Date, Time_in, Time_out FROM timings WHERE id = "+strings[0]+
                                " AND DATE BETWEEN '"+strings[1]+"' AND '"+strings[2]+"';";
                    }
                    rs = st.executeQuery(query2);
                    for (int i=0; rs.next(); i++){
                        presentDates[i] = rs.getString("Date");
                    }
                    rs = st.executeQuery(query2);
                    for (int i=0; i<countDates; i++){
                        Boolean flag = true;
                        for (int j=0; j<presentDates.length; j++){
                            if (totalDates[i].equals(presentDates[j])){
                                flag = false;
                                if (rs.next()){
                                    timeIn[i] = "Time in:  "+rs.getString("Time_in");
                                    String temp = rs.getString("Time_out");
                                    imgaeId[i] = R.drawable.logo_2;
                                    if (temp==null)
                                        timeOut[i] = "Time out:  "+"Not done";
                                    else
                                        timeOut[i] = "Time out:  "+temp;
                                }
                                break;
                            }
                        }
                        if (flag){
                            timeIn[i] = "";
                            timeOut[i] = "";
                            imgaeId[i] = R.drawable.logo_4;
                        }
                    }

                    rs.close();
                    st.close();
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                isSuccess = false;
            } catch (Exception ex) {
                ex.printStackTrace();
                isSuccess = false;
            } finally {
                try{
                    if(st!=null)
                        st.close();
                }catch(SQLException se2){}
                try{
                    if(con!=null)
                        con.close();
                }catch(SQLException se){
                    se.printStackTrace();
                    isSuccess = false;
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String s) {
            Toast.makeText(MainActivity5_customAttendenceRecord.this, Arrays.toString(totalDates)+"\n"+
                            Arrays.toString(presentDates)+"\n"+ Arrays.toString(timeIn)+"\n"+Arrays.toString(timeOut),
                    Toast.LENGTH_LONG).show();
            if (isSuccess) {
                b.putStringArray("totalDates", totalDates);
                b.putStringArray("presentDates", presentDates);
                b.putStringArray("timeIn", timeIn);
                b.putStringArray("timeOut", timeOut);
                b.putIntArray("image", imgaeId);
                DialogShowParticularMemberDateRecord dialog = new DialogShowParticularMemberDateRecord();
                dialog.setArguments(b);
                dialog.show(getSupportFragmentManager(), "Dialog Member Record");
            } else {
                Toast.makeText(MainActivity5_customAttendenceRecord.this, "Some error occured !!", Toast.LENGTH_LONG).show();
            }
            progressDialog.hide();
        }
    }*/



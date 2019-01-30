package in8443.httpsboxinall.biaattendence;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class MainActivity2_toadyAttendence extends AppCompatActivity {

    ListView listView;
    ProgressDialog progressDialog;
    int countTotalEmployee = 0;
    String[] name;
    String[] date;
    String[] timeIn;
    String[] timeOut;
    int[] imageId;
    Boolean isSuccess = true;
    /*Connection con = null;
    String sqlURL = null;
    String sqlUser = null;
    String sqlPass = null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_toady_attendence);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // for hide keyboard on startup
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Today's Attendence");
        getSupportActionBar().setSubtitle("Currently in office");
        /*getSupportActionBar().setLogo(R.drawable.main_logo_5);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        sqlURL = getResources().getString(R.string.url);
        sqlUser = getResources().getString(R.string.user);
        sqlPass = getResources().getString(R.string.pass);*/
        progressDialog = new ProgressDialog(MainActivity2_toadyAttendence.this);
        listView = (ListView) findViewById(R.id.listView);
        if (!isOnline()){
            Toast.makeText(MainActivity2_toadyAttendence.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
        } else background();
    }

    public void background() {
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/today_attendence.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    countTotalEmployee = jsonArray.length();
                    name = new String[countTotalEmployee];
                    date = new String[countTotalEmployee];
                    timeIn = new String[countTotalEmployee];
                    timeOut = new String[countTotalEmployee];
                    imageId = new int[countTotalEmployee];
                    for (int i = 0; i < countTotalEmployee; i++) {
                        name[i] = jsonArray.getJSONObject(i).getString("name");
                        date[i] = jsonArray.getJSONObject(i).getString("date");
                        timeIn[i] = jsonArray.getJSONObject(i).getString("timeIn");
                        timeOut[i] = jsonArray.getJSONObject(i).getString("timeOut");
                        if (timeIn[i].equals("null") && timeOut[i].equals("null"))
                            imageId[i] = R.drawable.logo_4;
                        else
                            imageId[i] = R.drawable.logo_3;
                        if (timeOut[i].equals("null") && !timeIn[i].equals("null"))
                            imageId[i] = R.drawable.logo_2;
                    }
                    isSuccess = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isSuccess = false;
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2_toadyAttendence.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    AdapterTodayPresentList myAdapter = new AdapterTodayPresentList(MainActivity2_toadyAttendence.this,
                            R.layout.item_list_today_attendence, name, timeIn, timeOut, imageId);
                    listView.setAdapter(myAdapter);
                /*Toast.makeText(MainActivity2_toadyAttendence.this, "\n"+ Arrays.toString(name) +"\n"+ Arrays.toString(date) +"\n"+
                    Arrays.toString(timeIn) +"\n"+ Arrays.toString(timeOut), Toast.LENGTH_LONG).show();*/
                } else
                    Toast.makeText(MainActivity2_toadyAttendence.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        }, 3000);
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

        Boolean isSuccess = true;
        String z = "";
        Statement st;
        ResultSet rs;

        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        public String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
                if (con == null) {
                    z = "No Internet Connection";
                } else {
                    st = con.createStatement();
                    String query1 = "SELECT Name FROM attendence ORDER BY id";
                    rs = st.executeQuery(query1);
                    while (rs.next()){
                        countTotalEmployee++;
                    }
                    name = new String[countTotalEmployee];
                    date = new String[countTotalEmployee];
                    timeIn = new String[countTotalEmployee];
                    timeOut = new String[countTotalEmployee];
                    rs = st.executeQuery(query1);
                    for(int i=0; rs.next(); i++) {
                        name[i] = rs.getString("Name");
                    }
                    String query2 = "SELECT Name, Date, Time_in, Time_out FROM attendence a, timings t " +
                            "WHERE Date = CURRENT_DATE AND a.id = t.id";
                    rs = st.executeQuery(query2);
                    for(int i=0; rs.next(); i++) {
                        String name1 = rs.getString("Name");
                        for (int j=0; j<countTotalEmployee; j++) {
                            if (name1.equals(name[j])) {
                                date[j] = rs.getString("Date");
                                timeIn[j] = rs.getString("Time_in");
                                timeOut[j] = rs.getString("Time_out");
                            }
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
            return z;
        }

        @Override
        public void onPostExecute(String s) {
            if (isSuccess) {
                imageId = new int[countTotalEmployee];
                for (int i = 0; i < countTotalEmployee; i++) {
                    if ((timeIn[i] == null && timeOut[i] == null)) {
                        imageId[i] = R.drawable.logo_4;
                    } else {
                        imageId[i] = R.drawable.logo_3;
                    }
                    if (timeOut[i] == null && timeIn[i] != null) {
                        imageId[i] = R.drawable.logo_2;
                    }
                }
                AdapterTodayPresentList myAdapter = new AdapterTodayPresentList(MainActivity2_toadyAttendence.this,
                        R.layout.item_list_today_attendence, name, timeIn, timeOut, imageId);
                listView.setAdapter(myAdapter);
            } else {
                Toast.makeText(MainActivity2_toadyAttendence.this, "Some error occured !!", Toast.LENGTH_LONG).show();
            }
            *//*Toast.makeText(MainActivity2_toadyAttendence.this, z +"\n"+ Arrays.toString(name) +"\n"+ Arrays.toString(date) +"\n"+
                    Arrays.toString(timeIn) +"\n"+ Arrays.toString(timeOut), Toast.LENGTH_LONG).show();*//*
            progressDialog.hide();
        }
    }*/


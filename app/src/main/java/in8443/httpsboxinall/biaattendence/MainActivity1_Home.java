package in8443.httpsboxinall.biaattendence;

import java.text.SimpleDateFormat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import android.content.Intent;

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
import java.util.HashMap;
import java.util.Map;


public class MainActivity1_Home extends AppCompatActivity {

    TextView tv_todayDate, tv_time;
    EditText et_user_pass;
    Button showAttendence;
    ImageButton markAttendence;
    String date, time, TodayDate, TodayTime;
    Dialog dialog;
    ProgressDialog progressDialog;
    /* Connection con = null;
     String sqlURL = null;
     // 136.243.67.94:3306/boxinall_what2fixrecords......192.168.43.132:3306/bia....boxinall.in:8443/boxinall_what2fixrecords
     String sqlUser = null;//"ritish";
     String sqlPass = null;//"6@24eknT";*/
    Boolean isSuccess = true;
    Boolean isRecordFound = false;
    Boolean isInserted = false;
    Boolean isUpdated = false;
    Boolean isRepeated = false;
    String member_id, member_name, member_timeIn, member_timeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity1__home);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // for hide keyboard on startup
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Mark Attendence");
        /*getSupportActionBar().setSubtitle("\tMark Attendence");
        getSupportActionBar().setLogo(R.drawable.main_logo_5);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        sqlURL = getResources().getString(R.string.url);
        sqlUser = getResources().getString(R.string.user);
        sqlPass = getResources().getString(R.string.pass);*/
        tv_todayDate = (TextView) findViewById(R.id.textView);
        tv_time = (TextView) findViewById(R.id.textView2);
        et_user_pass = (EditText) findViewById(R.id.editText);
        markAttendence = (ImageButton) findViewById(R.id.imageButton3);
        showAttendence = (Button) findViewById(R.id.button);
        dialog = new Dialog(MainActivity1_Home.this);
        progressDialog = new ProgressDialog(MainActivity1_Home.this);

        et_user_pass.setFocusableInTouchMode(false);
        et_user_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_user_pass.setFocusableInTouchMode(true);
            }
        });
        date = tv_todayDate.getText().toString();
        time = tv_time.getText().toString();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date1 = System.currentTimeMillis();
                                long date2 = System.currentTimeMillis();
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
                                TodayDate = simpleDateFormat1.format(date1);
                                TodayTime = simpleDateFormat2.format(date2);
                                tv_todayDate.setText(date + TodayDate);
                                tv_time.setText(time + TodayTime);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        markAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = et_user_pass.getText().toString();
                if (pass.trim().length() == 0)
                    et_user_pass.setError("Field can't Empty !!");
                else if (pass.length() != 4)
                    et_user_pass.setError("Enter 4 digits password !!");
                else if (!isOnline()){
                    Toast.makeText(MainActivity1_Home.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                } else {
                    background(pass);
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // hide keyboard
                }
            }
        });
        showAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity1_Home.this, MainActivity2_toadyAttendence.class));
            }
        });

    }

    public void background(final String password) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/mark_attendence.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.getJSONObject(0).getString("isRecordFound").equals("Y"))
                        isRecordFound = true;
                    else isRecordFound = false;
                    if (jsonArray.getJSONObject(1).getString("isInserted").equals("Y"))
                        isInserted = true;
                    else isInserted = false;
                    if (jsonArray.getJSONObject(2).getString("isUpdated").equals("Y"))
                        isUpdated = true;
                    else isUpdated = false;
                    if (jsonArray.getJSONObject(3).getString("isRepeated").equals("Y"))
                        isRepeated = true;
                    else isRepeated = false;
                    member_id = jsonArray.getJSONObject(4).getString("id");
                    member_name = jsonArray.getJSONObject(5).getString("name");
                    member_timeIn = jsonArray.getJSONObject(6).getString("timeIn");
                    member_timeOut = jsonArray.getJSONObject(7).getString("timeOut");
                    isSuccess = true;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isSuccess = false;
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("password", password);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity1_Home.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    if (!isRecordFound)
                        Toast.makeText(MainActivity1_Home.this, "No such record found !!", Toast.LENGTH_LONG).show();
                    else if (isRepeated)
                        Toast.makeText(MainActivity1_Home.this, "Your Today's attendence is already marked!!", Toast.LENGTH_LONG).show();
                    else if (isInserted || isUpdated) {
                        Bundle b = new Bundle();
                        b.putString("name", member_name);
                        b.putString("timein", member_timeIn);
                        b.putString("timeout", member_timeOut);
                        DialogConfirmation dialogConfirmation = new DialogConfirmation();
                        dialogConfirmation.setArguments(b);
                        dialogConfirmation.show(getSupportFragmentManager(), "Confirmation Dialog");
                    }
                } else Toast.makeText(MainActivity1_Home.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
                et_user_pass.setText("");
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
    /*public class BackgroundTask extends AsyncTask<String, String, String>{

        Boolean isConnection = false;
        Boolean isRecordFound = false;
        Boolean isInserted = false;
        Boolean isUpdated = false;
        Boolean isRepeated = false;
        Boolean isSuccess = true;
        Statement st;
        ResultSet rs;
        int id;
        String name;
        String timeIn;

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
                    isConnection = false;
                } else {
                    isConnection = true;
                    st = con.createStatement();
                    String query1 = "SELECT id, name FROM attendence WHERE password = '" + strings[0] + "';";
                    rs = st.executeQuery(query1);
                    if (rs.next()) {
                        id = rs.getInt("id");
                        name = rs.getString("name");
                        isRecordFound = true;
                    } else {
                        isRecordFound = false;
                    }


                    if (isRecordFound) {
                        Boolean restrict = true;
                        String date = strings[1];
                        String query2 = "SELECT * FROM timings WHERE id = " + id + " AND Date = CURRENT_DATE;";
                        rs = st.executeQuery(query2);
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String date2 = rs.getString("Date");
                            String timeIn = rs.getString("Time_in");
                            String timeOut = rs.getString("Time_out");
                            if (id != 0 && date2 != null && timeIn != null && timeOut != null) {
                                restrict = false;
                            }
                        }
                        if (restrict) {
                            Boolean temp = true;
                            String query3 = "SELECT * FROM timings WHERE id = " + id;
                            rs = st.executeQuery(query3);
                            while (rs.next()) {
                                String date2 = rs.getString("Date");
                                if (date2.equals(date)) {
                                    timeIn = rs.getString("Time_in");
                                    temp = false;
                                    break;
                                }
                            }
                            if (temp) {
                                String query4 = "INSERT INTO timings (id, Date, Time_in) VALUES(" + id + ", CURRENT_DATE,CURRENT_TIME);";
                                st.executeUpdate(query4);
                                isInserted = true;
                            } else {
                                String query4 = "UPDATE timings SET Time_out = CURRENT_TIME where id = " + id + ";";
                                st.executeUpdate(query4);
                                isUpdated = true;
                            }
                        } else {
                            isRepeated = true;
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
            if (isSuccess) {
                if (!isConnection)
                    Toast.makeText(MainActivity1_Home.this, "Check your Internet Connection !!", Toast.LENGTH_LONG).show();
                else if (!isRecordFound)
                    Toast.makeText(MainActivity1_Home.this, "No such record found !!", Toast.LENGTH_LONG).show();
                else if (!isInserted && !isUpdated && !isRepeated)
                    Toast.makeText(MainActivity1_Home.this, "Issue occurred, Time in can't set!!", Toast.LENGTH_LONG).show();
                else if (!isUpdated && !isInserted && !isRepeated)
                    Toast.makeText(MainActivity1_Home.this, "Issue occurred, Time out can't set!!", Toast.LENGTH_LONG).show();
                else if (isRepeated)
                    Toast.makeText(MainActivity1_Home.this, "Your Today's attendence is already marked!!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
                if (isConnection && isRecordFound && (isInserted || isUpdated) && !isRepeated) {
                    Bundle b = new Bundle();
                    if (isInserted) {
                        b.putString("name", name);
                        b.putString("timein", "Time in:     " + TodayTime);
                        b.putString("timeout", "");
                    }
                    if (isUpdated) {
                        b.putString("name", name);
                        b.putString("timein", "Time in:     " + timeIn);
                        b.putString("timeout", "Time out:  " + TodayTime);
                    }
                    DialogConfirmation dialogConfirmation = new DialogConfirmation();
                    dialogConfirmation.setArguments(b);
                    dialogConfirmation.show(getSupportFragmentManager(), "Confirmation Dialog");
                }
            } else {
                Toast.makeText(MainActivity1_Home.this, "Some Error Occurred!!", Toast.LENGTH_LONG).show();
            }
            et_user_pass.setText("");
        }

    }*/


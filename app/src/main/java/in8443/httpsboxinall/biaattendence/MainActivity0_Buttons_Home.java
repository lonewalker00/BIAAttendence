package in8443.httpsboxinall.biaattendence;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.Arrays;
import java.util.Calendar;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity0_Buttons_Home extends AppCompatActivity {

    Button markAttendence, attendenceRecord, editMembers;
    /*ProgressDialog progressDialog;
    String url, user, pass;
    Connection con = null;
    Statement st = null;
    ResultSet rs;
    String z = "";
    String re, i, n;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity0__buttons__home);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("\tBIA Softech");
        getSupportActionBar().setSubtitle("\tAttendence App");
        getSupportActionBar().setLogo(R.drawable.main_logo_5);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        markAttendence = (Button) findViewById(R.id.button2);
        attendenceRecord = (Button) findViewById(R.id.button3);
        editMembers = (Button) findViewById(R.id.button4);
        /*progressDialog = new ProgressDialog(this);
        url = getResources().getString(R.string.url);
        user = getResources().getString(R.string.user);
        pass = getResources().getString(R.string.pass);
        */
        markAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity0_Buttons_Home.this, MainActivity1_Home.class));
            }
        });
        attendenceRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) startActivity(new Intent(MainActivity0_Buttons_Home.this, MainActivity5_customAttendenceRecord.class));
                else Toast.makeText(MainActivity0_Buttons_Home.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
            }
        });
        editMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity0_Buttons_Home.this, MainActivity3_Edit_Members.class));
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable())
            return false;
        return true;
    }


}
    /*public void background(){
        progressDialog.setMessage("Loading...");
        progressDialog.show();
                String url = "https://boxinall.in/BIAOfficeAttendence/mark_attendence_fetch_id.php";
                StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            i = jsonObject1.getString("id");
                            re = jsonObject1.getString("status");
                            n = jsonObject1.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {error.printStackTrace();}
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", "4567");
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity0_Buttons_Home.this);
                requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity0_Buttons_Home.this, i+" "+n+" "+re, Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        }, 500);
    }*/
    /*public class BackgroundTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(url, user, pass);
                if (con == null) {
                    z = "No internet";
                } else {
                    st = con.createStatement();
                    String query1 = "SELECT * FROM attendence;";
                    rs = st.executeQuery(query1);
                    while (rs.next()) {
                        z = z + rs.getString("name");
                    }
                    rs.close();
                    st.close();
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
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
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity0_Buttons_Home.this, z, Toast.LENGTH_LONG).show();
            progressDialog.hide();
        }
    }
*/

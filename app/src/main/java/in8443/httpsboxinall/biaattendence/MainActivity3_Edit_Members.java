package in8443.httpsboxinall.biaattendence;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;

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

public class MainActivity3_Edit_Members extends Activity {

    RelativeLayout relativeLayout;
    EditText ET_USER_PASS;
    TextView FORGET_PASS;
    Button login;
    ProgressDialog progressDialog;
    Boolean isSuccess = false;
    Boolean check = true;
    /*Connection con = null;
    String sqlURL = null;
    String sqlUser = null;
    String sqlPass = null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3__edit__members);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // for hide keyboard on startup
        /*this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sqlURL = getResources().getString(R.string.url);
        sqlUser = getResources().getString(R.string.user);
        sqlPass = getResources().getString(R.string.pass);*/
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_1);
        ET_USER_PASS = (EditText) findViewById(R.id.editText2);
        login = (Button) findViewById(R.id.button5);
        FORGET_PASS = (TextView) findViewById(R.id.textView3);
        progressDialog = new ProgressDialog(MainActivity3_Edit_Members.this);
        if (!isOnline()) {
            Snackbar.make(relativeLayout, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    String pass = ET_USER_PASS.getText().toString();
                    if (pass.trim().length() == 0)
                        ET_USER_PASS.setError("Enter valid Password");
                     else
                        background(pass);
                } else
                    Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        FORGET_PASS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity3_Edit_Members.this, MainActivity4_ChangePasswordActivity.class);
                startActivity(i);
            }
        });
    }

    public void background(final String password){
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/edit_member_login.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.getJSONObject(0).getString("success").equals("Y")) isSuccess = true;
                    else isSuccess = false;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                check = false;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("password", password);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3_Edit_Members.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (check) {
                    if (isSuccess) {
                        Toast.makeText(MainActivity3_Edit_Members.this, "Welcome !!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity3_Edit_Members.this, MainActivity3_Edit_Members_List.class));
                    } else {
                        Toast.makeText(MainActivity3_Edit_Members.this, "Wrong Password. Try Again !!", Toast.LENGTH_LONG).show();
                    }
                } else Toast.makeText(MainActivity3_Edit_Members.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
                ET_USER_PASS.setText("");
            }
        }, 2500);
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

    Boolean isSuccess = true;
    String z = "";
    Statement st;
    ResultSet rs;

    @Override
    public void onPreExecute() {
        progressDialog.setMessage("Logging in...");
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
                String query = "SELECT * FROM editmembers WHERE password = '" + strings[0] + "';";
                rs = st.executeQuery(query);
                if (rs.next()) {
                    z = "Welcome";
                    isSuccess = true;
                } else {
                    z = "Wrong Password !!";
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
        Toast.makeText(MainActivity3_Edit_Members.this, z, Toast.LENGTH_LONG).show();
        progressDialog.hide();
        if (isSuccess) {
            startActivity(new Intent(MainActivity3_Edit_Members.this, MainActivity3_Edit_Members_List.class));
            ET_USER_PASS.setText("");
        } else {
            Toast.makeText(MainActivity3_Edit_Members.this, "Some error Occurred !!", Toast.LENGTH_LONG).show();
        }
    }
}*/


package in8443.httpsboxinall.biaattendence;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity4_ChangePasswordActivity extends AppCompatActivity implements DialogOTP.DialogOTPListener{

    EditText et_email, et_new_pass, et_confirm_pass;
    Button btn_save_changes, btn_cancel;
    ProgressDialog progressDialog;
    Boolean isSuccess = true;
    Boolean isUpdated = false;
    int randomNumber;
    Session session = null;
    Properties properties = null;
    String email, new_password;
    /*Connection con = null;
    String sqlURL = null;
    String sqlUser = null;
    String sqlPass = null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity4__change_password);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change Password");
       /* getSupportActionBar().setLogo(R.drawable.main_logo_5);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        sqlURL = getResources().getString(R.string.url);
        sqlUser = getResources().getString(R.string.user);
        sqlPass = getResources().getString(R.string.pass);*/
        et_email = (EditText) findViewById(R.id.editText4);
        et_new_pass = (EditText) findViewById(R.id.editText5);
        et_confirm_pass = (EditText) findViewById(R.id.editText6);
        btn_save_changes = (Button) findViewById(R.id.button6);
        btn_cancel = (Button) findViewById(R.id.button7);
        progressDialog = new ProgressDialog(this);

        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString();
                String new_pass = et_new_pass.getText().toString();
                String confirm_pass = et_confirm_pass.getText().toString();
                if (email.trim().length() == 0 || new_pass.trim().length() == 0 || confirm_pass.trim().length() == 0) {
                    if (email.trim().length() == 0)
                        et_email.setError("Field can't Empty");
                    if (new_pass.trim().length() == 0)
                        et_new_pass.setError("Field can't Empty");
                    if (confirm_pass.trim().length() == 0)
                        et_confirm_pass.setError("Field can't Empty");
                }
                else if (!email.contains("@")) et_email.setError("Enter valid Email id");
                else if (new_pass.length() < 4) et_new_pass.setError("Password is too short");
                else if (!new_pass.equals(confirm_pass)) {
                    et_new_pass.setError("Password do not match");
                    et_confirm_pass.setError("Password do not match");
                    Toast.makeText(MainActivity4_ChangePasswordActivity.this, "Password do not match", Toast.LENGTH_LONG).show();
                } else if (!isOnline()){
                    Toast.makeText(MainActivity4_ChangePasswordActivity.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                } else if (email.equals("sid15597@gmail.com") || email.equals("shivamritish@gmail.com") || email.equals("varshneyrit@gmail.com")) {
                    new_password = confirm_pass;
                    randomNumber = 100000 + new Random().nextInt(900000);
                    properties = new Properties();
                    properties.put("mail.smtp.host", "smtp.gmail.com");
                    properties.put("mail.smtp.socketFactory.port", "465");
                    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.port", "465");
                    session = Session.getDefaultInstance(properties,new Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication(){
                            return new PasswordAuthentication("bia.attendence@gmail.com", "bia@1234");
                        }
                    });
                    session.setDebug(true);
                    new sendMail().execute();
                } else {
                    Toast.makeText(MainActivity4_ChangePasswordActivity.this, "Sorry! You don't have privilage!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class sendMail extends AsyncTask<String, String, String>{
        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Sending OTP...");
            progressDialog.show();
        }
        @Override
        public String doInBackground(String... strings) {
            try {
                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress("bia.attendence@gmail.com"));
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                mimeMessage.setSubject("OTP to reset password");
                mimeMessage.setText(randomNumber+"\nEnter this OTP in Attendence app to change your password.");
                Transport.send(mimeMessage);
            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException ex) {
                ex.printStackTrace();
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            return null;
        }
        @Override
        public void onPostExecute(String s) {
            progressDialog.hide();
            DialogOTP dialogOTP = new DialogOTP();
            dialogOTP.show(getSupportFragmentManager(), "OTP Dialog");
        }
    }

    @Override
    public void applyTexts(String otp) {
        if (otp.equals(""+randomNumber)){
            change_password(new_password);
        } else {
            Toast.makeText(MainActivity4_ChangePasswordActivity.this, "Wrong OTP!!", Toast.LENGTH_LONG).show();
        }
    }

    public void change_password(final String newPassword) {
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/change_login_password.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    if (jsonObject1.getString("isUpdated").equals("Y")) isUpdated = true;
                    else isUpdated = false;
                    isSuccess = true;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isSuccess = false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("password", newPassword);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity4_ChangePasswordActivity.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                if (isSuccess) {
                    if (isUpdated) {
                        Toast.makeText(MainActivity4_ChangePasswordActivity.this, "Updated Successfully!!", Toast.LENGTH_LONG).show();
                        finish();
                    } else
                        Toast.makeText(MainActivity4_ChangePasswordActivity.this, "Can't Update. Error occurred!!", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(MainActivity4_ChangePasswordActivity.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
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

        Boolean isSuccess = false;
        String z = "";
        Statement st;
        ResultSet rs;
        int id;
        String name;

        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Updating...");
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
                    String query1 = "SELECT id, password FROM attendence WHERE password = '" + strings[0] + "';";
                    rs = st.executeQuery(query1);
                    if (rs.next()) {
                        id = rs.getInt("id");
                        String pss = rs.getString("password");
                        if (pss.equals("4567") || pss.equals("1234")){
                            isSuccess = true;
                        } else {
                            z = "Sorry! You don't have privilage";
                            isSuccess = false;
                        }
                    } else {
                        z = "User password doesn't found !!";
                        isSuccess = false;
                    }
                    if (isSuccess){
                        String query2 = "SELECT name FROM attendence WHERE id = "+id+";";
                        rs = st.executeQuery(query2);
                        if (rs.next()){
                            name = rs.getString("name");

                        }
                        String query3 = "UPDATE editmembers SET password = '"+strings[1]+"';";
                        String query4 = "UPDATE editmembers SET change_by = '"+name+"';";
                        st.executeUpdate(query3);
                        st.executeUpdate(query4);
                        z = "Updated Successfully";
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
            Toast.makeText(MainActivity4_ChangePasswordActivity.this, z, Toast.LENGTH_LONG).show();
            progressDialog.hide();
            if (isSuccess)
                finish();
            else
                Toast.makeText(MainActivity4_ChangePasswordActivity.this, "Some error occured !!", Toast.LENGTH_LONG).show();
        }
    }*/
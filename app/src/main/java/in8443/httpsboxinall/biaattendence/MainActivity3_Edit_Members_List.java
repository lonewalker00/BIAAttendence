package in8443.httpsboxinall.biaattendence;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity3_Edit_Members_List extends AppCompatActivity implements DialogAddMember.DialogAddMemberListener {

    ListView listView;
    FloatingActionButton addMember;
    ProgressDialog progressDialog;
    AdapterEditMembersList myAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String [] name;
    String [] idno;
    Boolean isInserted  = false;
    Boolean isDeleted = false;
    Boolean isTodayAttendenceMarked = false;
    Boolean isTodayAttendenceDeleted = false;
    Boolean isSuccess1 = true;
    Boolean isSuccess2 = true;
    Boolean isSuccess3 = true;
    Boolean isSuccess4 = true;
    /*Connection con = null;
    String sqlURL = null;//IPv4 Address. . . . . . . . . . . : 192.168.225.24....192.168.43.132
    String sqlUser = null;
    String sqlPass = null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity3__edit__members__list);
        getSupportActionBar().setTitle("Member's List");
        /*sqlURL = getResources().getString(R.string.url);
        sqlUser = getResources().getString(R.string.user);
        sqlPass = getResources().getString(R.string.pass);*/
        progressDialog = new ProgressDialog(MainActivity3_Edit_Members_List.this);
        listView = (ListView)findViewById(R.id.listView2);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        addMember = (FloatingActionButton)findViewById(R.id.fab);

        if (!isOnline()){
            Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
        } else fetch_member_list();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isOnline()){
                    Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                } else fetch_member_list();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity3_Edit_Members_List.this);
                alert.setMessage("Choose your action ?").setCancelable(false)
                        .setPositiveButton("Delete  Today's  Attendence", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isOnline()){
                                    Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                                } else delete_today_attendence(""+idno[position]);
                            }
                        })
                        .setNegativeButton("Delete  member", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isOnline()){
                                    Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                                } else delete_member(""+idno[position]);
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                AlertDialog close = alert.create();
                close.show();
            }
        });
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddMember dialogAddMember = new DialogAddMember();
                dialogAddMember.show(getSupportFragmentManager(), "addMember Dialog");
            }
        });
    }

    public void fetch_member_list(){
        swipeRefreshLayout.setRefreshing(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/edit_members_list.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    name = new String[jsonArray.length()];
                    idno = new String[jsonArray.length()];
                    for (int i=0; i<jsonArray.length(); i++){
                        idno[i] = jsonArray.getJSONObject(i).getString("id");
                        name[i] = jsonArray.getJSONObject(i).getString("name");
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
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3_Edit_Members_List.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess1) {
                    myAdapter = new AdapterEditMembersList(MainActivity3_Edit_Members_List.this, R.layout.item_list_edit_members, name, idno);
                    listView.setAdapter(myAdapter);
                } else Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.hide();
            }
        }, 3000);

    }

    @Override
    public void applyTexts(String name, String password) {
        if (!isOnline()){
            Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
        } else add_member(name, password);
    }

    public void delete_member(final String member_position){
        progressDialog.setMessage("Deleting");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/delete_member.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.getJSONObject(0).getString("isDeleted").equals("Y")) isDeleted = true;
                    else isDeleted = false;
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
                map.put("id", member_position);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3_Edit_Members_List.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess2) {
                    if (isDeleted) {
                        Toast.makeText(MainActivity3_Edit_Members_List.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                        fetch_member_list();
                    } else
                        Toast.makeText(MainActivity3_Edit_Members_List.this, "Can't delete. Issue occurred !!", Toast.LENGTH_LONG).show();
                } else Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        }, 3000);

    }

    public void add_member(final String name, final String password){
        progressDialog.setMessage("Adding");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/add_member.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.getJSONObject(0).getString("isInserted").equals("Y")) isInserted = true;
                    else isInserted = false;
                    isSuccess3 = true;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isSuccess3 = false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("password", password);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3_Edit_Members_List.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess3) {
                    if (isInserted) {
                        Toast.makeText(MainActivity3_Edit_Members_List.this, "Added Successfully", Toast.LENGTH_LONG).show();
                        fetch_member_list();
                    } else
                        Toast.makeText(MainActivity3_Edit_Members_List.this, "Can't add. Issue occurred !!", Toast.LENGTH_LONG).show();
                } else Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        }, 2500);
    }

    public void delete_today_attendence(final String member_position){
        progressDialog.setMessage("Deleting");
        progressDialog.show();
        String url = "https://boxinall.in/BIAOfficeAttendence/delete_today_attendence.php";
        StringRequest stringRequest = new StringRequest(1, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.getJSONObject(0).getString("isAttendenceMarked").equals("Y")) isTodayAttendenceMarked = true;
                    else isTodayAttendenceMarked = false;
                    if (jsonArray.getJSONObject(0).getString("isDeleted").equals("Y")) isTodayAttendenceDeleted = true;
                    else isTodayAttendenceMarked = false;
                    isSuccess4 = true;
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                isSuccess4 = false;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", member_position);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity3_Edit_Members_List.this);
        requestQueue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess4) {
                    if (isTodayAttendenceMarked){
                        if (isTodayAttendenceDeleted) {
                            Toast.makeText(MainActivity3_Edit_Members_List.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                        } else Toast.makeText(MainActivity3_Edit_Members_List.this, "Can't delete. Issue occurred !!", Toast.LENGTH_LONG).show();
                    } else Toast.makeText(MainActivity3_Edit_Members_List.this, "Today's Attendence isn't marked !!", Toast.LENGTH_LONG).show();
                } else Toast.makeText(MainActivity3_Edit_Members_List.this, "Check your internet connection !!", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        }, 3000);
        //Toast.makeText(MainActivity3_Edit_Members_List.this, "Deleted Today's Attendence", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Do you want to logout ?").setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog close = alert.create();
        close.setTitle("Logout");
        close.show();
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
        int countTotalEmployee = 0;
        Statement st;
        ResultSet rs;

        @Override
        public void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
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
                    String query1 = "SELECT id, name FROM attendence ORDER BY id";
                    rs = st.executeQuery(query1);
                    while (rs.next()){
                        countTotalEmployee++;
                    }
                    name = new String[countTotalEmployee];
                    idno = new int[countTotalEmployee];
                    rs = st.executeQuery(query1);
                    for(int i=0; rs.next(); i++) {
                        name[i] = rs.getString("Name");
                        idno[i] = rs.getInt("id");
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
                myAdapter = new AdapterEditMembersList(MainActivity3_Edit_Members_List.this, R.layout.item_list_edit_members, name, idno);
                listView.setAdapter(myAdapter);
            } else {
                Toast.makeText(MainActivity3_Edit_Members_List.this, "Some error occurred !!", Toast.LENGTH_LONG).show();
            }
            swipeRefreshLayout.setRefreshing(false);
            progressDialog.hide();
        }
    }*/

    /*public class BackgroundTask2 extends AsyncTask<String, String, String> {

        Boolean isSuccess = false;
        Statement st;
        ResultSet rs;

        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Adding");
            progressDialog.show();
        }

        @Override
        public String doInBackground(String... strings) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
                if (con == null) {
                    Toast.makeText(MainActivity3_Edit_Members_List.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    st = con.createStatement();
                    String query1 = "INSERT INTO attendence (name, password) VALUES ('"+strings[0]+"', "+strings[1]+");";
                    String query2 = "CALL xyz();";
                    if (st.executeUpdate(query1)==1)
                        isSuccess = true;
                    st.executeUpdate(query2);
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
                    isSuccess = false;
                    se.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String s) {
            if (isSuccess)
                Toast.makeText(MainActivity3_Edit_Members_List.this, "Added Successfully", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(MainActivity3_Edit_Members_List.this, "Can't Added. Issuses occured !!", Toast.LENGTH_LONG).show();
            progressDialog.hide();
            fetch_member_list();
        }
    }*/

/*public class BackgroundTask3 extends AsyncTask<String, String, String> {

        int position;
        Boolean isSuccess = false;
        Statement st;
        ResultSet rs;

        @Override
        public void onPreExecute() {
            progressDialog.setMessage("Deleting...");
            progressDialog.show();
        }

        @Override
        public String doInBackground(String... strings) {
            position = Integer.parseInt(strings[0]);
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(sqlURL, sqlUser, sqlPass);
                if (con == null) {
                    Toast.makeText(MainActivity3_Edit_Members_List.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                } else {
                    st = con.createStatement();
                    String query1 = "DELETE FROM attendence WHERE id = "+position+";";
                    String query2 = "CALL xyz();";
                    if (st.executeUpdate(query1)==1)
                        isSuccess = true;
                    st.executeUpdate(query2);
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
                    isSuccess = false;
                    se.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String s) {
            if (isSuccess)
                Toast.makeText(MainActivity3_Edit_Members_List.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(MainActivity3_Edit_Members_List.this, "Can't Delete. Issuses occured !!", Toast.LENGTH_LONG).show();
            progressDialog.hide();
            fetch_member_list();
        }
    }*/


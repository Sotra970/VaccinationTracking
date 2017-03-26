package classes;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ebada.vaccinationtracking.HomeActiviy;
import com.example.ebada.vaccinationtracking.R;
import com.example.ebada.vaccinationtracking.ShoWnotificationsActivity;
import com.example.ebada.vaccinationtracking.ShowKids;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import classes.Kids;
import classes.SessionMangement;
import classes.Vaccination;
import classes.VolleyConnection;

/**
 * Created by ebada on 14/03/2017.
 */

public class newReminder extends IntentService {
    ArrayList<Vaccination> vac;
    ArrayList<Kids> kids;
    String id ;
    Calendar calendar = Calendar.getInstance();
    SessionMangement sessionMangement;
    int determineValue;
    public newReminder() {
        super("name for service");
        kids = new ArrayList<>();
        determineValue = -1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sessionMangement = new SessionMangement(this);
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (sessionMangement.CheckLogin() == false)
        {}
        else {
            vac = intent.getParcelableArrayListExtra("paraam");
            id = intent.getStringExtra("id");
            Kids(new KidVolleyCallback() {
                @Override
                public void onSuccess(String r) {
                    if (r.equals("Empty") == true) {
                    } else {
                        Kids kid;
                        kids = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(r);
                            JSONObject jsonObject;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                kid = new Kids();
                                kid.setID(jsonObject.getString("id"));
                                kid.setName(jsonObject.getString("name"));
                                kid.setDate(jsonObject.getString("birth_date"));
                                kid.setGender(jsonObject.getString("gender"));
                                kid.setParentID(jsonObject.getString("parent_id"));
                                kids.add(kid);
                            }
                            if (vac == null || kids == null) {
                            } else {
                                for (int i = 0; i < kids.size(); i++) {
                                    checkAge(vac, calcdate(ConvertFromStringTodate(kids.get(i).getDate())),kids.get(i).getName(), getBaseContext());

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    void checkAge(ArrayList<Vaccination> vaccinations, long age,String name,Context context)
    {
        for (int i = 0 ;i < vaccinations.size() ; i++)
        {
            if (vaccinations.get(i).getNum_month() == age)
            {
                if (determineValue == 1) {
                    noificateMe(vaccinations.get(i), "You have a new Notification", "Your Kid should have his vaccination today ", name, context, i);
                    determineValue = -1;
                }
                else if (determineValue == 0) {
                    noificateMe(vaccinations.get(i), "You have a new Notification", "Your Kid should have his vaccination tomorrow ", name, context, i);
                    determineValue = -1;
                }
                else if (determineValue == 2) {
                    noificateMe(vaccinations.get(i), "You have a new Notification", "Your Kid should had his vaccination yesterday ", name, context, i);
                    determineValue = -1;
                }
                else
                {}

            }
            else if (vaccinations.get(i).getNum_month() != age)
            {

            }
        }
    }
    Calendar ConvertFromStringTodate(String datee) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");
        java.util.Date d1 = null;
        Calendar cal;
        d1 = format.parse(datee);
        cal = Calendar.getInstance();
        cal.setTime(d1);
        cal.toString();
        return cal;
    }

    private long calcdate(Calendar kidDate)
    {
        calendar.get(Calendar.YEAR);
        calendar.get(Calendar.MONTH);
        calendar.get(Calendar.DAY_OF_MONTH);
        int monthsBetween = 0;
        int dateDiff = calendar.get(Calendar.DAY_OF_MONTH)-kidDate.get(Calendar.DAY_OF_MONTH);

        if(dateDiff<0) {
            int borrrow = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (calendar.get(Calendar.DAY_OF_MONTH)+borrrow)-kidDate.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if(dateDiff>0) {
                monthsBetween++;
            }
        }

        monthsBetween += calendar.get(Calendar.MONTH)-kidDate.get(Calendar.MONTH);
        monthsBetween  += (calendar.get(Calendar.YEAR)-kidDate.get(Calendar.YEAR))*12;
        int day;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day == kidDate.get(Calendar.DAY_OF_MONTH))
        {
            // the vaccination day
            determineValue = 1;
        }
        else if (((kidDate.get(Calendar.DAY_OF_MONTH))-(day)) == 1)
        {
            //the day before vaccination day
            determineValue = 0;
        }
        else if (((kidDate.get(Calendar.DAY_OF_MONTH)-(day))) == -1)
        {
            //the day after vaccination day
            determineValue = 2;
        }
        else
        {determineValue = -1;}
        return  monthsBetween;
    }

    public void Kids(final KidVolleyCallback collback)
    {
        String URL = "http://experttk.com/vaccinations/kids.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                collback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > param = new HashMap<String, String>();
                param.put("parent_id",id);
                return param;
            }
        };
        VolleyConnection.getsInsteance().getmRequestQueue().add(stringRequest);
    }

    public interface KidVolleyCallback{

        void onSuccess(String r);
    }
    private void noificateMe(Vaccination vaccination, String title, String text, String name, Context context, int i)
    {

        NotificationManager notification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent inn = new Intent(context,ShoWnotificationsActivity.class);
        inn.putExtra("noti",vaccination);
        inn.putExtra("namee",name);
        inn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending = PendingIntent.getActivity(this,i,inn,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pending)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);
        notification.notify(i,builder.build());
    }
}

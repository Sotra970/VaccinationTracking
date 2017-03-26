package classes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.PeriodicSync;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.BitmapCompat;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ebada.vaccinationtracking.HomeActiviy;
import com.example.ebada.vaccinationtracking.R;
import com.example.ebada.vaccinationtracking.ShoWnotificationsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ebada on 07/03/2017.
 */

public class Reminder extends BroadcastReceiver
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Kids> kidses;
    Kids kid;
    ArrayList<Vaccination> vacs;
    Calendar calendar = Calendar.getInstance();
    private static final String PREF_KEY = "dataaa";
    private static final String PREF_NAME = "file";
    private int Private_mode = 0;
    public Reminder(){}
    public Reminder(Context context)
    {
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Private_mode);
        editor = sharedPreferences.edit();

    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        vacs = intent.getParcelableArrayListExtra("paraam");
        if (kidses == null || vacs == null)
        {}
        else {
            for (int i = 0; i < kidses.size(); i++) {
                try {
                    checkAge(vacs, calcdate(calendar, ConvertFromStringTodate(kidses.get(i).getDate())), context);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    void checkAge(ArrayList<Vaccination> vaccinations , long age,Context context)
    {
        for (int i = 0 ;i < vaccinations.size() ; i++)
        {
            if (vaccinations.get(i).getNum_month() == age)
            {
                NotificationManager notification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Intent inn = new Intent(context,ShoWnotificationsActivity.class);
                inn.putExtra("noti",vaccinations.get(i));
                inn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pending = PendingIntent.getActivity(context,i,inn,PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentIntent(pending)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("you have a new notification")
                        .setContentText("a new Vaccination")
                        .setAutoCancel(true);
                notification.notify(i,builder.build());
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

    long calcdate(Calendar currentDate,Calendar kidDate)
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
        return monthsBetween;
    }


    private ArrayList<Kids> getKIDES ()
    {
        ArrayList<Kids> K ;
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PREF_KEY,"");
        Type type = new TypeToken<ArrayList<Kids>>() {}.getType();
        K = gson.fromJson(json,type);
        return K;
    }
    public void addTO (Kids k)
    {
        kidses = getKIDES();
        if (kidses == null)
        {
            kidses = new ArrayList<>();
            kidses.add(k);
            Gson gson = new Gson();
            String json = gson.toJson(kidses);
            editor.putString(PREF_KEY, json);
            editor.commit();
        }
        else
        {
            kidses.add(k);
            Gson gson = new Gson();
            String json = gson.toJson(kidses);
            editor.putString(PREF_KEY, json);
            editor.commit();
        }
    }
    public void clear ()
    {
        editor.clear();
        editor.commit();
    }

//    public int NotificationID (){
//          AtomicInteger c = new AtomicInteger(0);
//            return c.incrementAndGet();
//
//    }
}

package classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by ebada on 02/03/2017.
 */

public class SessionMangement {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int Private_mode = 0;
    private static final String PREF_NAME = "UserLoggedInpref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String key_NAME = "FIRSTNAME";
    public static final String key_EMAIL = "EMAIL";
    public static final String key_PASSWORD = "password";
    public static final String  key_PHONE= "phone";
    public static final String  Key_CITY = "city";
    public static final String  key_ADDRESS= "address";
    public static final String  key_GENDER= "gender";
    public static final String key_ID = "id";
    public SessionMangement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Private_mode);
        editor = pref.edit();
    }
    public void createLoginSession(String name,String email,String password,String phone,String city,String address,String gender,String ID){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(key_NAME,name);
        editor.putString(key_EMAIL,email);
        editor.putString(key_PASSWORD,password);
        editor.putString(key_PHONE,phone);
        editor.putString(Key_CITY,city);
        editor.putString(key_ADDRESS,address);
        editor.putString(key_GENDER,gender);
        editor.putString(key_ID,ID);
        editor.commit();
    }
    public boolean IsLogedIn()
    {
        return pref.getBoolean(IS_LOGIN,false);
    }

    public boolean CheckLogin ()
    {
        if (!this.IsLogedIn())
            return false;
        else
            return true;
    }

    public String getName ()
    {return pref.getString(key_NAME,"");}

    public String getEmail ()
    {return pref.getString(key_EMAIL,"");}
    public String getPassword ()
    {return pref.getString(key_PASSWORD,"");}
    public String getphone ()
    {return pref.getString(key_PHONE,"");}
    public String getCity()
    {return  pref.getString(Key_CITY,"");}
    public String getAddress ()
    {return pref.getString(key_ADDRESS,"");}
    public String getID ()
    {return pref.getString(key_ID,"");}
    public String getGender()
    {return pref.getString(key_GENDER,"");}
    public void logoutUser()
    {
        editor.clear();
        editor.commit();
    }
}

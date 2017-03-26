package classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ebada on 09/03/2017.
 */

public class CheckAge
{
    SharedPreferences prefVaccinations,prefKids;
    SharedPreferences.Editor editorVaccinations , editorKids;
    Context _context;
    int Private_mode = 0;
    private static final String PREF_NAME_VACCINTIONS = "vaccinations";
    private static final String PREF_NAME_KIDS = "kids";
    public static final String key_kid = "KID";
    public static final String key_vaccination = "VACCINATIONS";
    ArrayList<Vaccination> vaccinations;
    ArrayList<Kids> kidses;
    public  CheckAge (Context context)
    {
        this._context = context;
        prefVaccinations = _context.getSharedPreferences(PREF_NAME_VACCINTIONS, Private_mode);
        prefKids = _context.getSharedPreferences(PREF_NAME_KIDS,Private_mode);
        editorVaccinations = prefVaccinations.edit();
        editorKids = prefKids.edit();
        vaccinations = new ArrayList<>();
        kidses = new ArrayList<>();
    }

    public void addToVaccinations(Vaccination vaccination)
    {
        if (checkV(vaccination) == true)
        {

        }
        else {
            this.vaccinations.add(vaccination);
            Gson gson = new Gson();
            String json = gson.toJson(vaccinations);
            editorVaccinations.putString(key_vaccination, json);
            editorVaccinations.commit();
        }
    }

    public void addToKides(ArrayList<Kids> kids)
    {

            Gson gson = new Gson();
            String json = gson.toJson(kids);
            editorKids.putString(key_kid, json);
            editorKids.commit();

    }

    public ArrayList<Vaccination> getVaccinations ()
    {
        ArrayList<Vaccination> v ;
        Gson gson = new Gson();
        String json = prefVaccinations.getString(key_vaccination,"");
        Type type = new TypeToken<ArrayList<Vaccination>>() {}.getType();
        v = gson.fromJson(json,type);
        return v;
    }
    public ArrayList<Kids> getKidses ()
    {
        ArrayList<Kids> K ;
        Gson gson = new Gson();
        String json = prefKids.getString(key_kid,"");
        Type type = new TypeToken<ArrayList<Kids>>() {}.getType();
        K = gson.fromJson(json,type);
        return K;
    }
    private boolean checkV (Vaccination vaccination)
    {
        int check = 0;
        ArrayList<Vaccination> v ;
        v = new ArrayList<>();
        Gson gson = new Gson();
        String json = prefVaccinations.getString(key_vaccination,"");
        Type type = new TypeToken<ArrayList<Vaccination>>() {}.getType();
        v = gson.fromJson(json,type);
        if (v == null)
        {return false;}
        else {
            for (int i = 0; i < v.size(); i++) {
                if (v.get(i).getId() == vaccination.getId()) {
                    check = 1;
                    break;
                }
            }
        }
        if (check == 1)
            return true;
        else
            return false;
    }

//    private boolean checkK (Kids kids)
//    {
//        int check = 0;
//        ArrayList<Kids> K ;
//        Gson gson = new Gson();
//        String json = prefKids.getString(key_kid,"");
//        Type type = new TypeToken<ArrayList<Kids>>() {}.getType();
//        K = gson.fromJson(json,type);
//            for (int i = 0; i < K.size(); i++) {
//                if (K.get(i).getID() == kids.getID()) {
//                    check = 1;
//                    break;
//                }
//            }
//        if (check == 1)
//            return true;
//        else
//            return false;
//    }

    public void clear ()
    {
        editorVaccinations.clear();
        editorKids.clear();
        editorKids.commit();
        editorVaccinations.commit();
    }
}

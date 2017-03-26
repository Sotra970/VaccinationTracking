package classes;

import android.content.Context;

/**
 * Created by ebada on 01/03/2017.
 */

public class Validation {
    public Validation(Context context)
    {
    }
    public boolean EmailValidtaion (String mail)
    {
        if (!mail.matches(("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
        {
            return false;
        }
        else
            return true;
    }

    public boolean PassWordValidation(String pass)
    {
        if (!(pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")))
            return false;
        else
            return true;
    }

}

package classes;

import android.app.Application;
import android.content.Context;

/**
 * Created by ebada on 01/03/2017.
 */

public class MyApplication extends Application{
    private static MyApplication sInsatnce;

    @Override
    public void onCreate() {
        super.onCreate();
        sInsatnce = this;
    }
    public static MyApplication getsInsatnce()
    {
        return sInsatnce;
    }
    public static Context getAppConetext()
    {
        return sInsatnce.getApplicationContext();
    }
}

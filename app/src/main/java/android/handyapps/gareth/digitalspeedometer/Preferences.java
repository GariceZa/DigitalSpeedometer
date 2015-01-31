package android.handyapps.gareth.digitalspeedometer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Gareth on 2015-01-28.
 */
public class Preferences {

    Context context;

    public Preferences(Context cont){

        context = cont;
    }

    // returns the users speed unit preference
    protected double getSpeedUnit(){

        SharedPreferences get = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.parseDouble(get.getString("unit", "3.6"));
    }

}

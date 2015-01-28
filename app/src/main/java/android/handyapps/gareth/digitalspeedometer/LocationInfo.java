package android.handyapps.gareth.digitalspeedometer;

import android.content.Context;
import android.location.Location;

/**
 * Created by Gareth on 2015-01-26.
 */
public class LocationInfo {

    private int speed = 0;
    private Location location;
    private Context context;

    public LocationInfo(Location loc,Context con){
        location = loc;
        context = con;
    }

    // Returns a speed value based on the speed unit preference
    protected int getSpeed(){

        Preferences prefs = new Preferences(context);
        speed = (int)(location.getSpeed() * prefs.getSpeedUnit());
        return  speed;
    }
}

package android.handyapps.gareth.digitalspeedometer;

import android.content.Context;
import android.location.Location;

/**
 * Created by Gareth on 2015-01-26.
 */
public class LocationInfo {

    private Location location;
    private Context context;

    public LocationInfo(Location loc,Context con){

        location = loc;
        context = con;
    }

    // Returns a speed value based on the speed unit preference
    protected int getSpeed(){

        Preferences prefs = new Preferences(context);
        return (int)(location.getSpeed() * prefs.getSpeedUnit());
    }

    // Returns the accuracy of the location data
    protected int getAccuracy(){

        return (int)location.getAccuracy();
    }
}

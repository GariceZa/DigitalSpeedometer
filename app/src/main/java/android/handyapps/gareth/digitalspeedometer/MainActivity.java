package android.handyapps.gareth.digitalspeedometer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private TextView speed;
    private LocationManager locMan;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // If the devices location services are disabled
        if(!locationServiceEnabled()){
            // Notify user
            locationServiceDisabledAlert();
        }
        else {
            // Start retrieving location updates
            setLocationUpdates();
            // Set the text view to the digital font
            setTypeFace();
        }
    }

    // Determines if location services are enabled
    private boolean locationServiceEnabled() {

        LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // Displays an alert dialog allowing the user to turn location services on
    private void locationServiceDisabledAlert(){

        AlertDialog.Builder locationServiceAlert = new AlertDialog.Builder(this,R.style.DialogTheme);
        locationServiceAlert.setMessage(R.string.enable_location_service)
                .setCancelable(false)
                .setPositiveButton(R.string.enable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //opens the location service settings
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        // closes the activity
                        finish();
                    }
                });
        locationServiceAlert.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // destroys the application
                finish();
            }
        });
        AlertDialog alert = locationServiceAlert.create();
        alert.show();
    }

    // Sets the text to the digital typeface stored in the assets folder
    private void setTypeFace(){

        Typeface digitalFont = Typeface.createFromAsset(getAssets(),"fonts/digital.ttf");
        speed = (TextView)findViewById(R.id.tvSpeed);
        speed.setTypeface(digitalFont);
    }

    // Starts location updates
    private void setLocationUpdates(){

        locMan      = (LocationManager)getSystemService(LOCATION_SERVICE);
        provider    = LocationManager.GPS_PROVIDER;

        locMan.requestLocationUpdates(provider,1000,0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Executes an async task to update the speed
        new RetrieveSpeed().execute(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("--onStatusChanged", "Status Changed " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("--onProviderEnabled","Provider Enabled " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("--onProviderDisabled","Provider Disabled " + provider);
    }

    private class RetrieveSpeed extends AsyncTask<Location,Void,Integer> {


        @Override
        protected Integer doInBackground(Location... params) {
            // Instantiates a new instance of the LocationInfo class
            LocationInfo locationInfo = new LocationInfo(params[0],getApplicationContext());
            Log.i("---LOCATION---",params[0].toString());
            Log.i("---SPEED---", String.valueOf(locationInfo.getSpeed()));
            // Returns the current speed
            return locationInfo.getSpeed();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // Sets the text view to the current speed
            speed.setText(String.valueOf(integer));
        }
    }
}

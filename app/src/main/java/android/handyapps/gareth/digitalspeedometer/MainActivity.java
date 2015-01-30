package android.handyapps.gareth.digitalspeedometer;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private TextView speed,speedUnit;
    private LocationManager locMan;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // define the color of the action bar
        setActionBarColor();

        // sets the preference default values the first time the app runs
        PreferenceManager.setDefaultValues(this,R.xml.settings,false);

        // If the devices location services are disabled
        if(!locationServiceEnabled()){
            // Notify user
            locationServiceDisabledAlert();
        }
        else {
            // Set the text view to the digital font
            setTypeFace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop retrieving location updates
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start retrieving location updates
        startLocationUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this,ShowUserPreferences.class));
        }
        return true;
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

    // Sets the color of the actionbar
    private void setActionBarColor(){

        ActionBar actionbar = getSupportActionBar();
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_background)));
    }

    // Determines if location services are enabled
    private boolean locationServiceEnabled() {

        LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // Displays an alert dialog allowing the user to turn location services on
    private void locationServiceDisabledAlert(){
        //https://github.com/afollestad/material-dialogs
        new MaterialDialog.Builder(this)
                .title(R.string.enable_location_service_title)
                .content(R.string.enable_location_service)
                .positiveText(R.string.enable)
                .negativeText(R.string.exit)
                .cancelable(false)
                .iconRes(R.drawable.ic_alert_white_36dp)
                .theme(Theme.DARK)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //opens the location service settings
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        // closes the speedometer application
                        finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        // closes the application
                        finish();
                    }
                })
                .show();
    }

    // Sets the text to the digital typeface stored in the assets folder
    private void setTypeFace(){

        Typeface digitalFont = Typeface.createFromAsset(getAssets(),"fonts/digital.ttf");
        speed = (TextView)findViewById(R.id.tvSpeed);
        speed.setTypeface(digitalFont);

        speedUnit = (TextView)findViewById(R.id.tvSpeedUnit);
        speedUnit.setTypeface(digitalFont);
    }

    // Sets the unit value to display
    private void setUnit(){

        Preferences preferences = new Preferences(MainActivity.this);

        if(preferences.getSpeedUnit() == 3.6){

            speedUnit.setText(R.string.kph);
        }
        else if(preferences.getSpeedUnit() == 2.23694){
            speedUnit.setText(R.string.mph);
        }
        else{
            speedUnit.setText(R.string.knots);
        }
    }

    // Starts location updates
    private void startLocationUpdates(){

        locMan      = (LocationManager)getSystemService(LOCATION_SERVICE);
        provider    = LocationManager.GPS_PROVIDER;

        locMan.requestLocationUpdates(provider,1000,0, this);
    }

    private void stopLocationUpdates(){

        locMan.removeUpdates(this);
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

            // Sets the speed unit text view
            setUnit();
        }
    }
}

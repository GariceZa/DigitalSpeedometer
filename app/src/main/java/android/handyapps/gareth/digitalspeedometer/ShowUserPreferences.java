package android.handyapps.gareth.digitalspeedometer;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by Gareth on 2015-01-28.
 */
public class ShowUserPreferences extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new UserPreferencesFragment())
                .commit();

        // Adds the home left facing arrow (<-) to the action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    // Called when the user selects the home arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // If the user clicks the back/home arrow
        switch (item.getItemId()){
            case android.R.id.home:
                finish(); // Close the preference screen
        }

        return super.onOptionsItemSelected(item);
    }
}

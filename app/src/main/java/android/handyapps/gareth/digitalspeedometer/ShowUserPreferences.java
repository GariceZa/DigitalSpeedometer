package android.handyapps.gareth.digitalspeedometer;

import android.app.Activity;
import android.os.Bundle;

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
    }
}

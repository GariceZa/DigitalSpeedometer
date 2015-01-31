package android.handyapps.gareth.digitalspeedometer;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Gareth on 2015-01-28.
 */
public class UserPreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}

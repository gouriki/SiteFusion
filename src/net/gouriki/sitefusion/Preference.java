package net.gouriki.sitefusion;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preference extends PreferenceActivity {
	@SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB)
            addPreferencesFromResource(R.xml.preference);
		setTitle("SiteFusion");
    }
}

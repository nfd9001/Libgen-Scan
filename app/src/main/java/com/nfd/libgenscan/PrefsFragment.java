package com.nfd.libgenscan;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-18.
 */
public class PrefsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.prefs, rootKey);
    }
}

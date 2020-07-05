package com.nfd.libgenscan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class PrefsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.d("LGS", "Creating pref fragment.");
        setPreferencesFromResource(R.xml.prefs, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getKey().equals("chooseprovider")) {
            Log.d("LGS", "starting providerchoice");
            startActivity(new Intent(getContext(), ProviderChoiceActivity.class));
        }
        Log.d("LGS", "Some preference clicked: " + preference.getKey());
        return super.onPreferenceTreeClick(preference);
    }


}

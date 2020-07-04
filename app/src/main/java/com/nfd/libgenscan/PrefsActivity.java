package com.nfd.libgenscan;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2019-08-18.
 */
public class PrefsActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LGS", "Expanded fragment");
        setContentView(R.layout.activity_prefs);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.prefs, new PrefsFragment())
                .commit();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Options");
    }
}

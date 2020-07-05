package com.nfd.libgenscan;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import data.AppDatabase;
import data.provider.Provider;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

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

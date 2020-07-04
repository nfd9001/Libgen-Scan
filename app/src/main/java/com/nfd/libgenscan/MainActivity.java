package com.nfd.libgenscan;

import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import data.AppDatabase;
import data.book.BookRef;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import me.dm7.barcodescanner.zbar.*;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static List<BarcodeFormat> allowedFormats = Arrays.asList(BarcodeFormat.ISBN10, BarcodeFormat.ISBN13);


    //TODO: annotate s.t. < 23 are accepted
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);

        if (!haveCameraPermission()) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    private boolean haveCameraPermission() {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // This is because the dialog was cancelled when we recreated the activity.
        if (permissions.length == 0 || grantResults.length == 0)
            return;

        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mScannerView.startCamera();
            } else {
                finish();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
        mScannerView = null;
    }
    private boolean isAllowed(BarcodeFormat b) {
        return allowedFormats.contains(b);
    }
    //intended to fire URL opener intents from other classes
    public void fire(Intent i) {
        startActivity(i);
    }

    @Override
    public void handleResult(Result rawResult) {
        if (!isAllowed(rawResult.getBarcodeFormat())){
            Toast.makeText(getApplicationContext(), getString(R.string.badformat),
                    Toast.LENGTH_SHORT).show();
            mScannerView.resumeCameraPreview(this);
        }
        final BookRef b = new BookRef(rawResult.getContents(), false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean a = sp.getBoolean("autosearch", true);
        if (sp.getBoolean("history", true)){
            if (a){b.opened = true;}
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance(getApplicationContext()).bookRefDao().insertAll(b);
                }
            }).start();
        }
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(R.id.main_opt):
                Intent i = new Intent(getApplicationContext(), PrefsActivity.class);
                startActivity(i);
                return true;
            case (R.id.main_hist):
                //TODO fill
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.nfd.libgenscan;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import data.AppDatabase;
import data.DataHelpers;
import data.book.BookRef;
import data.provider.Provider;
import data.provider.ProviderDao;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MainActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final List<BarcodeFormat> allowedFormats = Arrays.asList(BarcodeFormat.ISBN10, BarcodeFormat.ISBN13);

    Provider currentProvider;

    //TODO: annotate s.t. < 23 are accepted
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);

        if (!haveCameraPermission()) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
        AppDatabase db = AppDatabase.getInstance(this);
        ProviderDao providerDao = db.providerDao();

        fetchSelectedProvider();
        if (currentProvider == null) {
            DataHelpers.addDefaultProviders(providerDao);
            fetchSelectedProvider();
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
        fetchSelectedProvider();
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

    void fetchSelectedProvider() {
        try {
            FutureTask<List<Provider>> t = new FutureTask<>(
                    new Callable<List<Provider>>() {
                        @Override
                        public List<Provider> call() throws Exception {
                            Log.i("LGS", "Trying to set selected provider.");
                            return AppDatabase
                                    .getInstance(getApplicationContext())
                                    .providerDao()
                                    .findSelected();
                        }
                    });
            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.execute(t);

            currentProvider = t.get(200, TimeUnit.MILLISECONDS).get(0);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            Log.e("LGS", "Failed to fetch selected provider with a " + e.getClass().getCanonicalName());
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.cantSetProvider),
                    Toast.LENGTH_LONG).show();
            Log.i("LGS", "Got an empty list of providers back.");
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        if (!isAllowed(rawResult.getBarcodeFormat())) {
            Toast.makeText(getApplicationContext(), getString(R.string.badformat),
                    Toast.LENGTH_SHORT).show();
            mScannerView.resumeCameraPreview(this);
        }
        final BookRef b = new BookRef(rawResult.getContents(), false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean a = sp.getBoolean("autosearch", true);
        boolean h = sp.getBoolean("history", true);
        if (!a && !h) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_history_no_search),
                    Toast.LENGTH_LONG).show();
        }
        if (h) {
            if (a) {
                b.opened = true;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppDatabase.getInstance(getApplicationContext()).bookRefDao().insertAll(b);
                }
            }).start();
            Toast.makeText(getApplicationContext(), getString(R.string.gotBook),
                    Toast.LENGTH_SHORT).show();
        }
        if (a) {
            if (currentProvider == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.cantSetProvider),
                        Toast.LENGTH_LONG).show();
            } else {
                b.searchBook(this, currentProvider);
            }
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
        switch (item.getItemId()) {
            case (R.id.main_opt):
                Intent i = new Intent(getApplicationContext(), PrefsActivity.class);
                startActivity(i);
                return true;
            case (R.id.main_hist):
                Intent j = new Intent(getApplicationContext(), HistoryActivity.class);
                if (currentProvider == null) {
                    Log.i("LGS", "No provider to pass to History");
                } else {
                    j.putExtra("currentProvider", currentProvider);
                }
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

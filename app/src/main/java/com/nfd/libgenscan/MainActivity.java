package com.nfd.libgenscan;

import android.net.Uri;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import me.dm7.barcodescanner.zbar.*;
import android.content.Intent;


public class MainActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        if(rawResult.getBarcodeFormat().getName().toLowerCase().equals("isbn10") ||
                rawResult.getBarcodeFormat().getName().toLowerCase().equals("isbn13")){
            //send intent to browser
            String uri = "http://libgen.io/search.php?req=" + rawResult.getContents() +
                    "&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);


        } else {
            //try again
            Toast.makeText(getApplicationContext(),
                    "Barcode does not appear to be a valid ISBN, try again", Toast.LENGTH_SHORT).show();
            mScannerView.resumeCameraPreview(this);
        }
    }
}

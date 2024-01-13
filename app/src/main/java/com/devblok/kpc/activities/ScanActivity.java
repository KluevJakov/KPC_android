package com.devblok.kpc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.devblok.kpc.R;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            requestCameraPermission();
        }

        //setContentView(R.layout.activity_scan);
    }

    private void startScanner() {
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void handleResult(Result result) {
        Intent intent = new Intent(this, AnimalActivity.class);
        intent.putExtra("animalId", result.getText());
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            requestCameraPermission();
        }
    }


}

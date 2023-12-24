package com.devblok.kpc.activities;

import static com.devblok.kpc.tools.WebConstants.backendUrlBase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.devblok.kpc.R;
import com.devblok.kpc.tools.ActivityTools;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadActivity extends AppCompatActivity {

    private PDFView pdfView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Bundle b = getIntent().getExtras();

        TextView titleRead = findViewById(R.id.titleRead);
        titleRead.setText(b.getString("title"));
        TextView authorRead = findViewById(R.id.authorRead);
        authorRead.setText(b.getString("author"));

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            ActivityTools.setupBackLastFragment(intent, R.id.main);
            ActivityTools.closeAllConnections();
            startActivity(intent);
        });

        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);

        String pdfUrl = backendUrlBase + b.getString("file");
        new LoadPdfTask().execute(pdfUrl);
    }

    private class LoadPdfTask extends AsyncTask<String, Integer, byte[]> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected byte[] doInBackground(String... params) {
            String pdfUrl = params[0];

            try {
                URL url = new URL(pdfUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                int fileLength = urlConnection.getContentLength();

                try (InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    int totalBytesRead = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        totalBytesRead += bytesRead;
                        publishProgress((int) ((totalBytesRead * 100) / fileLength));
                    }

                    return outputStream.toByteArray();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(byte[] pdfBytes) {
            if (pdfBytes != null) {
                pdfView.fromBytes(pdfBytes).load();
            }
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}

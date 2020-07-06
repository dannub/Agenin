package com.agenin.id.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.agenin.id.R;

public class FullScreenActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        webView = findViewById(R.id.full_video);
        String link = getIntent().getStringExtra("link");
        webView.loadUrl(link);
        finish();
    }
}
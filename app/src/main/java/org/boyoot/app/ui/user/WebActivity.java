package org.boyoot.app.ui.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.boyoot.app.R;
import org.boyoot.app.databinding.ActivityWebBinding;
import org.boyoot.app.ui.contact.ContactActivity;

public class WebActivity extends AppCompatActivity  {

    ActivityWebBinding binding;
    private static final String url ="https://docs.google.com/forms/d/e/1FAIpQLScL61l-7pq061wx2M_cFL2AsAmaQYwA3SBn5f6cn4dPZpSMwQ/viewform?hl=ar";


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_web);

        WebSettings settings = binding.requestWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        binding.requestWebView.loadUrl(url);
        binding.requestWebView.setWebViewClient(new MyClient());

        binding.contactUsButton.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://wa.me/966592008428");
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(Intent.createChooser(sendIntent,"Choose App"));
        });

    }


    class MyClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            binding.webProgressBar.setVisibility(View.GONE);
        }
    }

}

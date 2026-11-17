package com.tameemkhan.finora;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.*;

public class MainActivity extends Activity {
    private static final String HOME = "https://finora-app.netlify.app";
    private WebView web;
    private ValueCallback<Uri[]> fileCb;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.parseColor("#0A0A12"));
        getWindow().setNavigationBarColor(Color.parseColor("#0A0A12"));
        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#0A0A12"));
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        CookieManager.getInstance().setAcceptThirdPartyCookies(web, true);
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
                String scheme = r.getUrl().getScheme();
                if ("http".equals(scheme) || "https".equals(scheme)) return false;
                try { startActivity(new Intent(Intent.ACTION_VIEW, r.getUrl())); } catch (Exception e) { }
                return true;
            }
            @Override public void onReceivedError(WebView v, WebResourceRequest r, WebResourceError e) {
                if (r.isForMainFrame()) v.loadDataWithBaseURL(null,
                    "<html><body style='margin:0;height:100vh;display:flex;align-items:center;justify-content:center;background:#0A0A12;color:#CBD5E1;font-family:sans-serif;text-align:center;padding:24px;box-sizing:border-box'>"
                    + "<div><h2 style='color:#fff'>No internet connection</h2><p>Finora needs internet to load.</p>"
                    + "<a href='" + HOME + "' style='display:inline-block;margin-top:12px;padding:12px 22px;border-radius:10px;background:#8B5CF6;color:#fff;text-decoration:none;font-weight:600'>Try again</a></div></body></html>",
                    "text/html", "utf-8", null);
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> cb, FileChooserParams p) {
                if (fileCb != null) fileCb.onReceiveValue(null);
                fileCb = cb;
                try { startActivityForResult(p.createIntent(), 42); } catch (Exception e) { fileCb = null; return false; }
                return true;
            }
        });
        if (b != null) web.restoreState(b); else web.loadUrl(HOME);
    }

    @Override protected void onActivityResult(int req, int res, Intent data) {
        if (req == 42 && fileCb != null) { fileCb.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(res, data)); fileCb = null; }
        else super.onActivityResult(req, res, data);
    }
    @Override protected void onSaveInstanceState(Bundle o) { super.onSaveInstanceState(o); web.saveState(o); }
    @Override public void onBackPressed() { if (web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}

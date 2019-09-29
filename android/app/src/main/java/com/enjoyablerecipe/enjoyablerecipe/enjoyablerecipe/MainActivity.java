package com.enjoyablerecipe.enjoyablerecipe.enjoyablerecipe;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private ValueCallback<Uri[]> filePathCallback = null;
    private Uri imageCaptureTitle = null;

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

            if (MainActivity.this.filePathCallback != null) {
                MainActivity.this.filePathCallback.onReceiveValue(null);
            }
            MainActivity.this.filePathCallback = filePathCallback;
            Intent imageCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String title = System.currentTimeMillis() + ".jpg";
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, title);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            MainActivity.this.imageCaptureTitle = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            imageCapture.putExtra(MediaStore.EXTRA_OUTPUT, MainActivity.this.imageCaptureTitle);
            Intent getContent = new Intent(Intent.ACTION_GET_CONTENT);
            getContent.addCategory(Intent.CATEGORY_OPENABLE);
            getContent.setType("image/*");
            Intent intent = Intent.createChooser(imageCapture, "");
            intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { getContent });
            startActivityForResult(intent, 1);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.enjoyablerecipe.com/");
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ((WebView) findViewById(R.id.webView)).goBack();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (filePathCallback != null) {
                    Uri[] receiveValue = null;
                    if (resultCode == RESULT_OK) {
                        if (imageCaptureTitle != null) {
                            receiveValue = new Uri[] {
                                    imageCaptureTitle,
                            };
                        } else if (data != null) {
                            receiveValue = new Uri[] {
                                    data.getData(),
                            };
                        }
                    }
                    filePathCallback.onReceiveValue(receiveValue);
                    filePathCallback = null;
                    imageCaptureTitle = null;
                    return;
                } else {
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

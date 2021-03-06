package com.moris.tavda;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ActivityWebview extends AppCompatActivity {

    private WebView wbNews;

    private Elements elements;
    private FloatingActionButton floatingActionButton_share;
    String param_str;
    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        FloatingActionButton floatingActionButton=findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        floatingActionButton_share = findViewById(R.id.fab_share);

        Parse parse = new Parse();
        Bundle extr = getIntent().getExtras();
        if (extr != null) param_str = extr.getString("INTENT_EXTRA_URL");
        showProgressDialog();
        parse.execute(param_str);
        setWebView();

    }

     class Parse extends AsyncTask<String, Void, String> {
        // TODO: 8/14/2018 Прогрес бар
        @Override
        protected String doInBackground(String... params) {
            Document document;
            try {
                document = Jsoup.connect(params[0]).get();
                elements = document.select(".node.story");
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
            return document.html();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (elements != null) {
                String unencodedHtml = "<html lang=\"ru\"><head></head><body><style>\n" +
                        "img {\n" +
                        "    max-width: 100%;\n" +
                        "    width:auto;\n" +
                        "    height: auto;\n" +
                        "}\n" +" iframe {\n" +
                        "    position: absolute;\n" +
                        "    top: 0;\n" +
                        "    left: 0;\n" +
                        "    width: 100%;\n" +
                        "}"+
                        "</style>\n<div>"+elements.toString()+"</div>" + "<table align=\"center\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\"><tbody><tr><td>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</td><td style=\"text-align: center;\"><p>Материалы с официального сайта Тавдинского городского округа</p><p><a href=\"http://www.adm-tavda.ru/\">www.adm-tavda.ru</a></p></td></tr></tbody></table></body><html>";
//            String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                    Base64.NO_PADDING);
                wbNews.loadDataWithBaseURL("http://www.adm-tavda.ru", unencodedHtml, "text/html; charset=utf-8", "base64", "http://www.adm-tavda.ru");
                floatingActionButton_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Отправлено из мобильного приложения Живая Тавда: " + param_str);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Отправлено из мобильного приложения Живая Тавда");
//                    sendIntent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "ssss@where.com" });
//                    sendIntent.putExtra(Intent.EXTRA_CONTENT_ANNOTATIONS, "EXTRA_CONTENT_ANNOTATIONS");
//                    sendIntent.putExtra(Intent.EXTRA_SPLIT_NAME,"dddddd");
                        sendIntent.putExtra(Intent.EXTRA_HTML_TEXT, "<html><body><h1>Отправлено из мобильного приложения Живая Тавда.</h1></html></body>");
                        sendIntent.setType("text/plan");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_name)));
                    }
                });
            } else {
                wbNews.loadDataWithBaseURL("http://www.adm-tavda.ru", "<H3>Источник недоступен, проверьте связь</H3>", "text/html; charset=utf-8", "base64", "http://www.adm-tavda.ru");
            }
            hideProgressDialog();
        }
    }


    private void setWebView() {
        wbNews = findViewById(R.id.webview);
        WebSettings wbset = wbNews.getSettings();
        wbset.setJavaScriptEnabled(true);
        // FIXME: 8/12/2018 вырести в настройки
//        wbset.setBuiltInZoomControls(true);
//        wbset.setDisplayZoomControls(false);
        wbset.setDefaultTextEncodingName("utf-8");
        wbNews.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                wbNews.setVisibility(view.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wbNews.setVisibility(view.VISIBLE);
            }
        });
//        newsLink = getIntent().getExtras().getString("INTENT_EXTRA_URL");
//        wbNews.loadUrl(newsLink);
//        String unencodedHtml ="<html><body>"+"</body></html>";
//        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
//                Base64.NO_PADDING);
//        wbNews.loadData(encodedHtml, "text/html", "base64");

    }
}
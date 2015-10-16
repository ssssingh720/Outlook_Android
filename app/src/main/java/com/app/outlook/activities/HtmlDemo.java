package com.app.outlook.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.webkit.WebView;

import com.app.outlook.R;
import com.app.outlook.Utils.MyWebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by srajendrakumar on 15/10/15.
 */
public class HtmlDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);


        String data = "<p><strong><img class=\\\"alignleft wp-image-74 size-medium\\\" src=\\\"http://outlook.jumpcatch.com/wp-content/uploads/2015/10/outlook-hindi-magazine1-215x300.jpg\\\" alt=\\\"outlook-hindi-magazine1\\\" width=\\\"215\\\" height=\\\"300\\\" />Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry&#8217;s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</p>\\n";
        String data2 = "<p><strong><img src=\"http://www.w3schools.com/images/w3schools_green.jpg\" alt=\"W3Schools.com\" style=\"width:104px;height:142px;\">\n" +
                "Lorem Ipsum</strong> is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry&#8217;s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</p>";
        String data1 = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<img src=\"http://www.w3schools.com/images/w3schools_green.jpg\" alt=\"W3Schools.com\" style=\"width:104px;height:142px;\">\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        data = stripHtml(data);
        WebView webview = (WebView)this.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadData(data, "text/html", "UTF-8");

//        webview.getSettings().setUseWideViewPort(true);
//        webview.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");

//        String summary = "<html><head><title>Title of the document</title></head><body><h1><a href=\"hrupin://second_activity\">LINK to second activity</a></h1><h1><a href=\"http://www.google.com/\">Link to GOOGLE.COM</a></h1></body></html>";
//        webview.loadData(data, "text/html", null);
//        webview.setWebViewClient(new MyWebViewClient(this));
    }
    public String stripHtml(String html) {
//        return Html.fromHtml(html).toString();
        return html.replace("\\","");
    }

}

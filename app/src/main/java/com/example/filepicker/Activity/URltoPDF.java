package com.example.filepicker.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.filepicker.AllImage.imageFolder;
import com.example.filepicker.R;

import java.io.File;

public class URltoPDF extends AppCompatActivity {

    private WebView myWebView;
    EditText txt;
    LinearLayout     Ok;
    private String storeFilename;
    File storeFilepathNname;
    imageFolder storeFilePath;
    String url;
    ImageButton back_btn;
    RelativeLayout mainLayout;
    LottieAnimationView animation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlto_pdf);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray));

        }

        myWebView = findViewById(R.id.myWebView);
        txt = findViewById(R.id.txt);
        Ok = findViewById(R.id.Ok);
        back_btn=findViewById(R.id.back_btn);
        animation_view = findViewById(R.id.animation_view);
        mainLayout=findViewById(R.id.mainLayout);

        Toast.makeText(this, "please enter url and press ok", Toast.LENGTH_SHORT).show();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //add webview client to handle event of loading
        myWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                //if page loaded successfully then show print button
//                findViewById(R.id.fab).setVisibility(View.VISIBLE);
            }
        });

        //prepare your html content which will be show in webview
        String htmlDocument = "<html><body>" +
                "<h1>Webview Print Test </h1>" +
                "<h2>I am Webview</h2>" +
                "<p> By PointOfAndroid</p>" +
                "<p> This is some sample content.</p>" +
                "<p> By PointOfAndroid</p>" +
                "<p> This is some sample content.</p>" +
                "<p> By PointOfAndroid</p>" +
                "<p> This is some sample content.</p>" +
                "" +
                "" +
                "" + "Put your content here" +
                "" +
                "" +
                "</body></html>";

        //load your html to webview
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = txt.getText().toString();
                if (url == "") {
                    Toast.makeText(getApplicationContext(), "Enter url", Toast.LENGTH_SHORT).show();
                } else {

//                    myWebView.loadUrl("https://www.google.com");

                    if(url.equals("")){
                        Toast.makeText(URltoPDF.this, "please Enter url", Toast.LENGTH_SHORT).show();
                    }else{
                        dialog(v);
                    }



                }
            }
        });


    }

    //create a function to create the print job
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = getString(R.string.app_name) + " Print Test";

        //open print dialog
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }

    //perform click pdf creation operation on click of print button click
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void printPDF(View view) {
        createWebPrintJob(myWebView);
    }


    public void dialog(View v) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure You wanted to make decision");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {


                        new CountDownTimer(10000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                myWebView.loadUrl(url);
                                animation_view.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.GONE);


                            }

                            public void onFinish() {
                                animation_view.setVisibility(View.GONE);
                                printPDF(v);
                            }

                        }.start();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("callResume","call...");

        mainLayout.setVisibility(View.VISIBLE);

//        Ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                url = txt.getText().toString();
//                if (url == "") {
//                    Toast.makeText(getApplicationContext(), "Enter url", Toast.LENGTH_SHORT).show();
//                } else {
//
////                    myWebView.loadUrl("https://www.google.com");
//
//                    if(url.equals("")){
//                        Toast.makeText(URltoPDF.this, "please Enter url", Toast.LENGTH_SHORT).show();
//                    }else{
//                        dialog(v);
//                    }
//
//
//
//                }
//            }
//        });
    }
}
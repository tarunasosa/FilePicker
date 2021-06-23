package com.example.filepicker.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.filepicker.AllImage.ImageDisplay;
import com.example.filepicker.AllImage.imageFolder;
import com.example.filepicker.R;
import com.example.filepicker.ViewPDF;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class URltoPDF extends AppCompatActivity {

    private WebView myWebView;
    EditText txt;
    Button Ok;
    private String storeFilename;
    File storeFilepathNname;
    imageFolder storeFilePath;
    String url;
    LottieAnimationView animation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urlto_pdf);
        myWebView = findViewById(R.id.myWebView);
        txt = findViewById(R.id.txt);
        Ok = findViewById(R.id.Ok);
        animation_view = findViewById(R.id.animation_view);

        Toast.makeText(this, "please enter url and press ok", Toast.LENGTH_SHORT).show();

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

                    dialog(v);

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

    public void createPDF() {


        Document doc = new Document();
        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        storeFilepathNname = null;
        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");
        Log.e("filepath", "" + storeFilepathNname);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(storeFilepathNname));
            doc.open();
            String mText = "showData.getText().toString()";
            doc.addAuthor("Text To Pdf");
            for (int i = 0; i < ImageDisplay.selectedImage.size(); i++) {
                Image image = Image.getInstance(ImageDisplay.selectedImage.get(i));

                float documentWidth = doc.getPageSize().getWidth()
                        - doc.leftMargin() - doc.rightMargin();
                float documentHeight = doc.getPageSize().getHeight()
                        - doc.topMargin() - doc.bottomMargin();
                image.scaleToFit(documentWidth, documentHeight);

//                Log.e("Document - Image  = Height", document.getPageSize().getHeight()+" - "+image.getScaledHeight());

                float leftMargin = doc.getPageSize().getWidth() - image.getScaledWidth();
                float lMargin = leftMargin / 2;

                float topMargin = doc.getPageSize().getHeight() - image.getScaledHeight();
                float tMargin = topMargin / 2;

                image.setAbsolutePosition(lMargin, tMargin);

                image.setAlignment(Image.ALIGN_CENTER);
                doc.add(image);
                doc.newPage();


            }

            doc.close();


        } catch (Exception e) {
            Toast.makeText(URltoPDF.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
//        try {
//            downloadAndCombinePDFs();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
                                txt.setVisibility(View.GONE);
                                Ok.setVisibility(View.GONE);

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

}
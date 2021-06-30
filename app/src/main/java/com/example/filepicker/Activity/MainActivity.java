package com.example.filepicker.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


import com.aspose.words.Document;
import com.example.filepicker.AllImage.MainActivity_gallery;

import com.example.filepicker.R;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;


import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class
MainActivity extends AppCompatActivity {

    private int FILE_ZIP_CODE = 001;
    private int FILE_TXT_CODE = 002;
    private int FILE_DOCX_CODE = 003;
    ArrayList<String> pickImage;
    ImageButton back_btn;


    private static final int PICK_PDF_FILE = 2;

    ImageButton pickimage, picktxtFile, pickdocFile, urlTopdf, zipTopdf;
    public static ArrayList<String> pdfpathAry;
    Uri document;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray));
        }

        pdfpathAry = new ArrayList<>();
        pickimage = findViewById(R.id.pickimage);
        picktxtFile = findViewById(R.id.picktxtFile);
        pickdocFile = findViewById(R.id.pickdocFile);
        urlTopdf = findViewById(R.id.urlTopdf);
        zipTopdf = findViewById(R.id.zipTopdf);
        back_btn = findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        zipTopdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ShowAllZipFile.class);
                startActivity(i);

            }
        });

        urlTopdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), URltoPDF.class);
                startActivity(i);


            }
        });

        pickimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity_gallery.class);
                startActivity(i);


            }
        });
        picktxtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ShowAllTextFile.class);
                startActivity(i);
            }
        });
        pickdocFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent i=new Intent(getApplicationContext(), ShowAllDocFile.class);
//                startActivity(i);


                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // mime types for MS Word documents
                String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                // start activiy
                startActivityForResult(intent, PICK_PDF_FILE);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                document = intent.getData();
                Log.d("path2", "" + document);


                Intent i = new Intent(getApplicationContext(), ViewPDF.class);
                i.putExtra("getpdfpath", String.valueOf(document));
                i.putExtra("from", "docx");
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
//                Toast.makeText(getApplicationContext(), "" + document, Toast.LENGTH_SHORT).show();
                // open the selected document into an Input stream

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pdfpathAry.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pdfpathAry.clear();

    }
}
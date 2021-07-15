package com.example.filepicker.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.filepicker.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.psdev.licensesdialog.LicensesDialog;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;


public class Home_Activity extends AppCompatActivity {
    ImageButton createPDF, PDFhistory, mergePDF;
    ImageButton info_btn;


    public static ArrayList<String> mergepdflist = new ArrayList<>();


    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int MULTIPLE_PERMISSIONS = 10;
    String checkbtnClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createPDF = findViewById(R.id.createPDF);
        PDFhistory = findViewById(R.id.PDFhistory);
        mergePDF = findViewById(R.id.mergePDF);


        info_btn = findViewById(R.id.info_btn);

        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LicensesDialog.Builder(Home_Activity.this)

                        .setNotices(R.raw.notices)
                        .build()
                        .show();
            }
        });


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray));
        }

        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbtnClick = "createPDF";


                if ((ActivityCompat.checkSelfPermission(Home_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();

                    return;

                } else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }

            }
        });
        PDFhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbtnClick = "PDFhistory";

                if ((ActivityCompat.checkSelfPermission(Home_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();

                    return;

                } else {
                    Intent i = new Intent(getApplicationContext(), Show_pdf_history.class);
                    startActivity(i);
                }

            }
        });
        mergePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbtnClick = "mergePDF";

                if ((ActivityCompat.checkSelfPermission(Home_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();

                    return;

                } else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/pdf");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, 100);
                }


            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (null != data) { // checking empty selection
                if (null != data.getClipData()) { // checking multiple selection or not
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        String uri = data.getClipData().getItemAt(i).getUri().toString();
                        mergepdflist.add(uri);
                    }

                    Intent i = new Intent(getApplicationContext(), ViewPDF.class);
                    i.putExtra("from", "mergepdf");
                    startActivity(i);


                } else {
                    Uri uri = data.getData();
                }
            }
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkbtnClick.equals("createPDF")) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    } else if (checkbtnClick.equals("PDFhistory")) {
                        Intent i = new Intent(getApplicationContext(), Show_pdf_history.class);
                        startActivity(i);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("application/pdf");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, 100);
                    }


                } else {
                    Toast.makeText(this, "no permissions granted", Toast.LENGTH_SHORT).show();
//                    if ((ActivityCompat.checkSelfPermission(Home_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//                        checkPermissions();
//
//                        return;
//
//                    }
                    // no permissions granted.
                }

            }

        }
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);

            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
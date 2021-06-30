package com.example.filepicker.Activity;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

import de.psdev.licensesdialog.LicensesDialog;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;


public class Home_Activity extends AppCompatActivity {
    ImageButton createPDF, PDFhistory, mergePDF;
    boolean hasStoragePermission;
    ImageButton info_btn;
    final int READ_WRITE_HOME = 1001;
    public static final int merge_REQUEST_CODE = 400;


    public static ArrayList<String> mergepdflist;

    private static final int REQUEST_CODE = 101;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int MULTIPLE_PERMISSIONS = 10;


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
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);

                int result;
                List<String> listPermissionsNeeded = new ArrayList<>();
                for (String p : permissions) {
                    result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(p);

                    }

                }
                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(Home_Activity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                    return;
                } else {
                    if (permission()) {
                        if (SDK_INT >= Build.VERSION_CODES.R) {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                        }

                    } else {
                        RequestPermission_Dialog();
                    }
                }

            }
        });
        PDFhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);

                int result;
                List<String> listPermissionsNeeded = new ArrayList<>();
                for (String p : permissions) {
                    result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(p);

                    }

                }
                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(Home_Activity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                    return;
                } else {
                    if (permission()) {
                        if (SDK_INT >= Build.VERSION_CODES.R) {
                            Intent i = new Intent(getApplicationContext(), Show_pdf_history.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getApplicationContext(), Show_pdf_history.class);
                            startActivity(i);
                        }

                    } else {
                        RequestPermission_Dialog();
                    }
                }

            }
        });
        mergePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);

                int result;
                List<String> listPermissionsNeeded = new ArrayList<>();
                for (String p : permissions) {
                    result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(p);

                    }

                }
                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(Home_Activity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
                    return;
                } else {
                    if (permission()) {
                        if (SDK_INT >= Build.VERSION_CODES.R) {
                            Intent i = new Intent(getApplicationContext(), ShowAllPdfForMerge.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getApplicationContext(), ShowAllPdfForMerge.class);
                            startActivity(i);
                        }

                    } else {
                        RequestPermission_Dialog();
                    }
                }

            }
        });
    }





    public void RequestPermission_Dialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                startActivityForResult(intent, 2000);
            } catch (Exception e) {
                Intent obj = new Intent();
                obj.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(obj, 2000);
            }
        } else {
            ActivityCompat.requestPermissions(Home_Activity.this, new String[]{ READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    if (storage && read) {

                    }
                } else {
                    Toast.makeText(Home_Activity.this, "Please allow permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }


    public boolean permission() {
        if (SDK_INT >= Build.VERSION_CODES.R) { // R is Android 11
            return Environment.isExternalStorageManager();
        } else {

            int read = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

            return  read == PackageManager.PERMISSION_GRANTED;
        }
    }

}
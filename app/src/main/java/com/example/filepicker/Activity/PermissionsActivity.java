package com.example.filepicker.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.filepicker.R;


public class PermissionsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 10;
Button createPDF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_home);


        createPDF = findViewById(R.id.createPDF);

        //Proceed button that goes to mainactivity only if the desired permissions are given
        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PermissionsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED ) {
                    // call this to finish the current activity
                    setWritePermission();
                }
                else{
                    Toast.makeText(PermissionsActivity.this,"Contacts permission required",Toast.LENGTH_LONG).show();
                    setReadPermission();

                }
            }
        });
    }

    private void gotoActivity() {
        Intent intent = new Intent(PermissionsActivity.this, ShowAllPdfForMerge.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setWritePermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionsActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Toast.makeText(PermissionsActivity.this,"Read contacts",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(PermissionsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_CONTACTS);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(PermissionsActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_CONTACTS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    private void setReadPermission() {
        if (ContextCompat.checkSelfPermission(PermissionsActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionsActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //Toast.makeText(PermissionsActivity.this,"Read contacts",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(PermissionsActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(PermissionsActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Contacts permission granted", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        else   if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_CONTACTS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Write permission granted", Toast.LENGTH_SHORT).show();
                gotoActivity();

            } else {
                Toast.makeText(this, "Write permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

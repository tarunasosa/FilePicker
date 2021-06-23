package com.example.filepicker.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.filepicker.MainActivity;
import com.example.filepicker.R;
import com.example.filepicker.ViewPDF;

import java.io.File;
import java.util.ArrayList;


public class Home_Activity extends AppCompatActivity {
    ImageButton createPDF, PDFhistory, mergePDF;
    boolean hasStoragePermission;
    final int READ_WRITE_HOME = 1001;
    public static final int merge_REQUEST_CODE = 400;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,


    };
    String[] permissions1 = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,


    };
public  static   ArrayList<String> mergepdflist;

    public static final int MULTIPLE_PERMISSIONS = 10;
    public static final int MULTIPLE_PERMISSIONS1 = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        createPDF = findViewById(R.id.createPDF);
        PDFhistory = findViewById(R.id.PDFhistory);
        mergePDF = findViewById(R.id.mergePDF);

        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });
        PDFhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), Show_pdf_history.class);
                startActivity(i);
            }
        });
        mergePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), ShowAllPdfForMerge.class);
                startActivity(i);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        if (requestCode == merge_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                mergepdflist = new ArrayList<>();
                int count = intent.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                for (int i = 0; i < count; i++) {
                    mergepdflist.add(getPath(getApplicationContext(), intent.getClipData().getItemAt(i).getUri()));
                    Log.d("intentArray", "" + getPath(getApplicationContext(), intent.getClipData().getItemAt(i).getUri()));

                }

//               Uri data=intent.getData();
//
//               Toast.makeText(this, ""+getPath(getApplicationContext(),data), Toast.LENGTH_SHORT).show();
//
//
//               Log.d("getpath",""+getPath(getApplicationContext(),data));


                Intent i = new Intent(getApplicationContext(), ViewPDF.class);
                i.putExtra("getpdfpath", "path");
                i.putExtra("from", "mergepdf");
//               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                Uri contentUri = null;
                try {
                    String fileName = getFilePath(context, uri);
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                    }
                    String id = DocumentsContract.getDocumentId(uri);
                    if (id.startsWith("raw:")) {
                        id = id.replaceFirst("raw:", "");
                        File file = new File(id);
                        if (file.exists()) return id;
                    }
                    contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (contentUri != null) {
                    return getDataColumn(context, contentUri, null, null);
                } else {
                    return null;
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return uri.getPath();
    }

    public String getFilePath(Context context, Uri uri) {
        Cursor cursor = null;
        String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        try {
            if (uri == null) return null;
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
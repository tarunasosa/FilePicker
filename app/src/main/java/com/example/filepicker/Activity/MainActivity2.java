package com.example.filepicker.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.filepicker.R;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
Button btn1;
File mergepdfpath;
    File storeFilePath;
    ArrayList<String> pathAry=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn1=findViewById(R.id.btn1);

        File file1 = getExternalFilesDir("PDFtoANYFormat");
        String relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        storeFilePath = new File(relativePath);
        if (!storeFilePath.exists()) {
            storeFilePath.mkdirs();
            Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, 100);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100) {
            if(null != data) { // checking empty selection
                if(null != data.getClipData()) { // checking multiple selection or not
                    for(int i = 0; i < data.getClipData().getItemCount(); i++) {
                        String uri = data.getClipData().getItemAt(i).getUri().toString();
                        pathAry.add(uri);
                    }
                    try {
                        downloadAndCombinePDFs();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Uri uri = data.getData();
                }
            }
        }
    }

    private void downloadAndCombinePDFs() throws IOException {
        PDFMergerUtility ut = new PDFMergerUtility();

        for (int i = 0; i < pathAry.size(); i++) {

            final InputStream in=getContentResolver().openInputStream(Uri.parse(pathAry.get(i)));
            ut.addSource(in);
        }



        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        mergepdfpath = null;
        mergepdfpath = new File(storeFilePath.getPath() + File.separator +
                "/" + filename + ".pdf");
        Log.d("mergecall","cal..");

        final FileOutputStream fileOutputStream = new FileOutputStream(mergepdfpath);
//        final InputStream in=getContentResolver().openInputStream(Uri.parse(mergepdfpath));
        try {
            ut.setDestinationStream(fileOutputStream);
            ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
            Toast.makeText(this, "Done...", Toast.LENGTH_SHORT).show();

        } finally {
            fileOutputStream.close();
        }

        Log.d("data", "" + mergepdfpath);
//        SharedPreferences.Editor editor = getSharedPreferences("mypref", MODE_PRIVATE).edit();
//        editor.putString("path", mergepdfpath.toString());
//        editor.apply();

//        viewPDF.fromFile(mergepdfpath).show();


    }
}
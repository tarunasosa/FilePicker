package com.example.filepicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.filepicker.Activity.Home_Activity;
import com.example.filepicker.Activity.ShowAllDocFile;
import com.example.filepicker.Activity.ShowAllPdfForMerge;
import com.example.filepicker.Activity.ShowAllTextFile;
import com.example.filepicker.Activity.ZipToPdf;
import com.example.filepicker.Adapter.MergePDFAdapter;
import com.example.filepicker.AllImage.ImageDisplay;
import com.example.filepicker.AllImage.MainActivity_gallery;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pdfview.PDFView;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewPDF extends AppCompatActivity {
    PDFView viewPDF;
    String getpdfpath, from;

    private int FILE_CODE = 001;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    ArrayList<String> pickImage;
    File pdfpath;
    SharedPreferences pref;
    File mergepdfpath;
    ArrayList<String> pdfpathAry;
    File storeFilePath;
    String storeFilename, txtFileData, path;
    File storeFilepathNname;
    TextView edit_txt, add_text;
    public static final int txt_REQUEST_CODE = 100;
    public static final int docx_REQUEST_CODE = 200;
    public static final int zip_REQUEST_CODE = 300;
    ProgressBar progressBar;
    com.google.android.material.floatingactionbutton.FloatingActionButton fab_add, fab_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);
        Log.d("Activityyy", "create");
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        pdfpathAry = MainActivity.pdfpathAry;
        viewPDF = findViewById(R.id.viewPDF);
        edit_txt = findViewById(R.id.edit_txt);
        add_text = findViewById(R.id.add_text);

        fab_add = findViewById(R.id.add);
        fab_edit = findViewById(R.id.Edit);


        File file1 = getExternalFilesDir("PDFtoANYFormat");
        String relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        storeFilePath = new File(relativePath);
        if (!storeFilePath.exists()) {
            storeFilePath.mkdirs();
            Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
        }


        getpdfpath = getIntent().getStringExtra("getpdfpath");
        from = getIntent().getStringExtra("from");


        Log.d("getpdfpath", "" + getpdfpath);
        Log.d("from", "" + from);
//        pdfpathAry.add(getpdfpath);

        if (from.equals("txt")) {


            ReadTextFromTextFile(getpdfpath);
            TextToPdf();
            pdfpathAry.add(String.valueOf(storeFilepathNname));

            if (pdfpathAry.size() > 1) {
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        try {
                            downloadAndCombinePDFs();
                            storeFilepathNname = mergepdfpath;
                            viewPDF.fromFile(mergepdfpath.toString()).show();
                            progressBar.setVisibility(View.GONE);

                            deleteExitFile();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }.start();

            } else {
                viewPDF.fromFile(storeFilepathNname).show();
                progressBar.setVisibility(View.GONE);
            }

            Log.d("pdfpathAry", "" + pdfpathAry.size());
        }
        else if (from.equals("edited")) {
            storeFilepathNname = new File(getpdfpath);
            viewPDF.fromFile(getpdfpath).show();
            pdfpathAry.add(String.valueOf(storeFilepathNname));

        }
        else if (from.equals("docx")) {
            fab_edit.setVisibility(View.GONE);
            edit_txt.setVisibility(View.GONE);
            Log.d("getpdfpath", "" + getpdfpath);
            DocxtoPdf();
            pdfpathAry.add(String.valueOf(storeFilepathNname));

            Log.d("arrrr", "" + pdfpathAry);

            if (pdfpathAry.size() > 1) {
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        try {
                            downloadAndCombinePDFs();
                            storeFilepathNname = mergepdfpath;
                            Log.d("arrrr1", "" + storeFilepathNname);
                            viewPDF.fromFile(mergepdfpath.toString()).show();
                            progressBar.setVisibility(View.GONE);

                            deleteExitFile();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }.start();

            } else {
                Log.d("arrrr2", "" + storeFilepathNname);
                viewPDF.fromFile(storeFilepathNname).show();
                progressBar.setVisibility(View.GONE);
            }

        }
        else if (from.equals("image")) {
            fab_edit.setVisibility(View.GONE);
            edit_txt.setVisibility(View.GONE);

            Toast.makeText(this, "" + ImageDisplay.selectedImage.size(), Toast.LENGTH_SHORT).show();
            createPDF();
            pdfpathAry.add(String.valueOf(storeFilepathNname));

            if (pdfpathAry.size() > 1) {
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        try {
                            downloadAndCombinePDFs();
                            storeFilepathNname = mergepdfpath;
                            viewPDF.fromFile(mergepdfpath.toString()).show();
                            progressBar.setVisibility(View.GONE);

                            deleteExitFile();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }.start();

            } else {
                viewPDF.fromFile(storeFilepathNname).show();
                progressBar.setVisibility(View.GONE);
            }

            Log.d("array", "" + pdfpathAry);
        }
        else if (from.equals("mergepdf")) {
            fab_edit.setVisibility(View.GONE);
            fab_add.setVisibility(View.GONE);
            edit_txt.setVisibility(View.GONE);
            add_text.setVisibility(View.GONE);

            pdfpathAry= ShowAllPdfForMerge.selectedImage;
            Toast.makeText(this, ""+pdfpathAry.toString(), Toast.LENGTH_SHORT).show();
            Log.d("jndjmn ",""+pdfpathAry.toString());


            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    try {
                        downloadAndCombinePDFs();
                        storeFilepathNname = mergepdfpath;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    viewPDF.fromFile(storeFilepathNname).show();
                    progressBar.setVisibility(View.GONE);

                }

            }.start();


        }


        Addbtnprocess();
        Editbtnprocess();


        Log.d("data", "" + getpdfpath);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.pdfpathAry.clear();
        finish();
    }

    public void createPDF() {


        Document doc = new Document();
        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        storeFilepathNname = null;
        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");
        Log.e("filepath", "" + pdfpath);
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
            Toast.makeText(ViewPDF.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
//        try {
//            downloadAndCombinePDFs();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Activityyy", "resume");


    }

    private void downloadAndCombinePDFs() throws IOException {
        PDFMergerUtility ut = new PDFMergerUtility();

        for (int i = 0; i < pdfpathAry.size(); i++) {

            ut.addSource(new File(pdfpathAry.get(i)));

        }


        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        mergepdfpath = null;
        mergepdfpath = new File(storeFilePath.getPath() + File.separator +
                "/" + filename + ".pdf");

        final FileOutputStream fileOutputStream = new FileOutputStream(mergepdfpath);
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

        viewPDF.fromFile(mergepdfpath).show();


    }

    public void ReadTextFromTextFile(String txtpath) {
        File file = new File(txtpath);
        Log.d("dddddd", "" + txtpath);

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while (true) {

                if (!((line = br.readLine()) != null)) break;

                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            Toast.makeText(this, "file not fromated", Toast.LENGTH_SHORT).show();
            //You'll need to add proper error handling here
        }

//Find the view by its id

//Set the text
        txtFileData = text.toString();
    }

    public void TextToPdf() {


        Document doc = new Document();
        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");
        Log.e("filepath", "" + storeFilepathNname);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(storeFilepathNname));
            doc.open();
//            String mText = txtData.getText().toString();
            doc.addAuthor("Text To Pdf");
            doc.add(new Paragraph(txtFileData));
            doc.close();
            Toast.makeText(ViewPDF.this, storeFilename + ".pdf\nis saved to\n" + storeFilepathNname, Toast.LENGTH_SHORT).show();
//            Log.d("path", "" + file.getPath());
        } catch (Exception e) {

            Toast.makeText(ViewPDF.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        Log.d("data", "" + storeFilepathNname.toString());
    }

    public void Addbtnprocess() {

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((from.equals("txt")) || (from.equals("edited"))) {

                    Intent i=new Intent(getApplicationContext(), ShowAllTextFile.class);
                    startActivity(i);
                    finish();

                } else if (from.equals("docx")) {
                    Intent i=new Intent(getApplicationContext(), ShowAllDocFile.class);
                    startActivity(i);
                    finish();

                }
                if (from.equals("image")) {

                    Intent i = new Intent(getApplicationContext(), MainActivity_gallery.class);
                    startActivity(i);
                    finish();
                }


            }
        });


    }

    public void Editbtnprocess() {

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfpathAry.clear();
                Intent i = new Intent(getApplicationContext(), EditPDF.class);
                i.putExtra("editPDFpath", storeFilepathNname.toString());
                startActivity(i);
                finish();

            }
        });

    }

    public void DocxtoPdf() {

        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");

        try (InputStream inputStream =
                     new FileInputStream(getpdfpath)) {

            com.aspose.words.Document doc = new com.aspose.words.Document(inputStream);
            // save DOCX as PDF
            Log.d("name", "" + storeFilepathNname);
            doc.save(String.valueOf(storeFilepathNname));
            // show PDF file location in toast as well as treeview (optional)
            Toast.makeText(ViewPDF.this, "File saved in: " + storeFilepathNname, Toast.LENGTH_LONG).show();

            // view converted PDF
//            viewPDFFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(ViewPDF.this, "File not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ViewPDF.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ViewPDF.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteExitFile() {
        for (int i = 0; i < pdfpathAry.size(); i++) {
            File fdelete = new File(pdfpathAry.get(i));
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                    Log.d("from", "delete");
                } else {
                    Toast.makeText(this, "not delete", Toast.LENGTH_SHORT).show();
                    Log.d("from", "not delete");
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == txt_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                Uri data = intent.getData();

                Toast.makeText(this, "" + getPath(getApplicationContext(), data), Toast.LENGTH_SHORT).show();

                Log.d("getpath", "" + getPath(getApplicationContext(), data));

                path = getPath(getApplicationContext(), data);
                Intent i = new Intent(getApplicationContext(), ViewPDF.class);
                i.putExtra("getpdfpath", path);
                i.putExtra("from", "txt");
//               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();


            }

        } else if (requestCode == docx_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                Uri data = intent.getData();

                Toast.makeText(this, "" + getPath(getApplicationContext(), data), Toast.LENGTH_SHORT).show();
                Log.d("getpath", "" + getPath(getApplicationContext(), data));

                path = getPath(getApplicationContext(), data);
                Intent i = new Intent(getApplicationContext(), ViewPDF.class);
                i.putExtra("getpdfpath", path);
                i.putExtra("from", "docx");
//               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }


        } else if (requestCode == zip_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                Uri data = intent.getData();

                Toast.makeText(this, "" + getPath(getApplicationContext(), data), Toast.LENGTH_SHORT).show();


                Log.d("getpath", "" + getPath(getApplicationContext(), data));

                path = getPath(getApplicationContext(), data);
                Intent i = new Intent(getApplicationContext(), ZipToPdf.class);
                i.putExtra("getpdfpath", path);
                i.putExtra("from", "zip");
//               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
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
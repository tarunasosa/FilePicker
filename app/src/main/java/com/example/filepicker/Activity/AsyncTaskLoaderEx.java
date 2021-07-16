package com.example.filepicker.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.aspose.words.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncTaskLoaderEx extends AsyncTaskLoader<String> {
    private static final AtomicInteger sCurrentUniqueId = new AtomicInteger(0);
    String document;
    Context context;
    private String mData;
    private String storeFilename;
    private File storeFilePath;
     File storeFilepathNname;

    public AsyncTaskLoaderEx(final Context context, String document) {
        super(context);
        this.context = context;
        this.document = document;


        onContentChanged();
    }

    public static int getNewUniqueLoaderId() {
        return sCurrentUniqueId.getAndIncrement();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        //this part should be removed from support library 27.1.0 :
        //else if (hasResult)
//            deliverResult(mData);
    }

    @Override
    public void deliverResult(final String data) {
        mData = data;
        super.deliverResult(storeFilepathNname.toString());

    }
    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        onReleaseResources(mData);
        mData = null;


    }

    protected void onReleaseResources(String data) {
        //nothing to do.
    }

    public String getResult() {
        return mData;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public String loadInBackground() {
        DocxtoPdf();

//        Log.d("path2", "" + document);
//        Intent i = new Intent(context, ViewPDF.class);
//        i.putExtra("getpdfpath", storeFilepathNname.toString());
//        i.putExtra("from", "docx");
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);



        return "";
    }

    public void DocxtoPdf() {

        File file1 = context.getExternalFilesDir("PDFtoANYFormat");
        String relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        storeFilePath = new File(relativePath);
        if (!storeFilePath.exists()) {
            storeFilePath.mkdirs();
            Toast.makeText(context, "create", Toast.LENGTH_SHORT).show();
        }

        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");




        try (InputStream inputStream =
                     context.getContentResolver().openInputStream(Uri.parse(document))) {

            com.aspose.words.Document doc = new com.aspose.words.Document(inputStream);

            // save DOCX as PDF
            Log.d("name", "" + storeFilepathNname);
            doc.save(String.valueOf(storeFilepathNname));
            // show PDF file location in toast as well as treeview (optional)

            // view converted PDF
//            viewPDFFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
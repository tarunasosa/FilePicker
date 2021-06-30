package com.example.filepicker.Activity;

import android.content.Context;
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
    private String mData;

    String storeFilename;
    File storeFilepathNname;
    File storeFilePath;
    String getpdfpath;
    Context context;

    public static int getNewUniqueLoaderId() {
        return sCurrentUniqueId.getAndIncrement();
    }

    public AsyncTaskLoaderEx(final Context context, File storeFilePath, String getpdfpath) {
        super(context);
        this.context=context;
        this.storeFilePath = storeFilePath;
        this.getpdfpath = getpdfpath;

        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        //this part should be removed from support library 27.1.0 :
        //else if (hasResult)
        //    deliverResult(mData);
    }

    @Override
    public void deliverResult(final String data) {
        mData = data;

        super.deliverResult(String.valueOf(storeFilepathNname));
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
        Log.d("datapath", "" + storeFilepathNname);
        return String.valueOf(storeFilepathNname);
    }


    public String DocxtoPdf() {

        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");

        Log.d("dataa:"," "+storeFilename+" "+storeFilepathNname+" "+getpdfpath);

        try (InputStream inputStream =
                     new FileInputStream(getpdfpath)) {

           Document doc = new Document(inputStream);

            Log.d("name", "" + storeFilepathNname);
            doc.save(String.valueOf(storeFilepathNname));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("catch:","FileNotFoundException");
            Toast.makeText(context, "File not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("catch:","IOException");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("catch:","Exception");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return String.valueOf(storeFilepathNname);
    }
}
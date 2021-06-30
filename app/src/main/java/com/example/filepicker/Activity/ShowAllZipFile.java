package com.example.filepicker.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.filepicker.Adapter.DOCXAdapter;
import com.example.filepicker.Adapter.PDFhistoryAdapter;
import com.example.filepicker.Adapter.ZipAdapter;
import com.example.filepicker.Model.DocModel;
import com.example.filepicker.Model.ZipModel;
import com.example.filepicker.R;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ShowAllZipFile extends AppCompatActivity {
    ArrayList<ZipModel> arrData;
    ListView list_doc;
    Context context;
    ZipAdapter madapter;
    ImageButton back_btn, info_zip;
    TextView array_state_txt;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_zip_file);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray));
        }

        list_doc = findViewById(R.id.list_doc);
        back_btn = findViewById(R.id.back_btn);
        info_zip = findViewById(R.id.info_zip);
        context = this;
        array_state_txt = findViewById(R.id.array_state_txt);
        new LoadApplications().execute();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        info_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               zipinfoDialog();
            }
        });


    }

    public void getAllzipFile() {
        arrData = new ArrayList<>();
        Iterator<File> fileIterator = FileUtils.iterateFiles(
                Environment.getExternalStorageDirectory(),
                FileFilterUtils.suffixFileFilter("zip"),
                TrueFileFilter.INSTANCE);
        while (fileIterator.hasNext()) {
            File fileINeed = fileIterator.next();

            if (fileINeed != null) {
                ZipModel zipModel = new ZipModel();
                zipModel.setName(fileINeed.getName());
                Log.d("APKname:", "" + fileINeed.getName());
                zipModel.setFolder(fileINeed.getAbsolutePath());

                File file = new File(fileINeed.getAbsolutePath());

                Date date = new Date((fileINeed.lastModified()));
                String dateFormat = DateFormat.getDateInstance().format(date);

                long file_size = fileINeed.length();

//                audioModel.setDate(dateFormat);
//                audioModel.setSize(String.valueOf(file_size));

                arrData.add(zipModel);
                Log.d("apksize", "" + fileINeed.length());
            }
        }
        Log.d("apksize", "" + arrData.size());

    }

    class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(context, null, "Loading zip file...");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAllzipFile();
            if (arrData.size() > 0) {
                madapter = new ZipAdapter(getApplicationContext(), arrData);
                Log.e("xdnjb", "" + arrData.size());

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (arrData.size() > 0) {
                list_doc.setAdapter(madapter);

            } else {
                array_state_txt.setVisibility(View.VISIBLE);
            }
            progress.dismiss();
            super.onPostExecute(aVoid);
        }

    }

    public void zipinfoDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("This Zip to Pdf conversion only convert txt file or image file convert to pdf");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        alertDialog.dismiss();
                    }
                });



        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
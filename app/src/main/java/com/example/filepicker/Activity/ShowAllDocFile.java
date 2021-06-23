package com.example.filepicker.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filepicker.Adapter.DOCXAdapter;
import com.example.filepicker.Model.DocModel;
import com.example.filepicker.R;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ShowAllDocFile extends AppCompatActivity {
ArrayList<DocModel> arrData;
ListView list_doc;
Context context;
DOCXAdapter madapter;
TextView array_state_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_doc_file);
        context=this;
        list_doc=findViewById(R.id.list_doc);
        array_state_txt=findViewById(R.id.array_state_txt);


        new LoadApplications().execute();



    }
    public void getAlldocFile() {
        arrData = new ArrayList<>();
        Iterator<File> fileIterator = FileUtils.iterateFiles(
                Environment.getExternalStorageDirectory(),
                FileFilterUtils.suffixFileFilter("docx"),
                TrueFileFilter.INSTANCE);
        while (fileIterator.hasNext()) {
            File fileINeed = fileIterator.next();

            if (fileINeed != null) {
                DocModel docModel = new DocModel();
                docModel.setName(fileINeed.getName());
                Log.d("APKname:", "" + fileINeed.getName());
                docModel.setFolder(fileINeed.getAbsolutePath());

                File file = new File(fileINeed.getAbsolutePath());

                Date date = new Date((fileINeed.lastModified()));
                String dateFormat = DateFormat.getDateInstance().format(date);

                long file_size = fileINeed.length();

//                audioModel.setDate(dateFormat);
//                audioModel.setSize(String.valueOf(file_size));

                arrData.add(docModel);
                Log.d("apksize", "" + fileINeed.length());
            }
        }
        Log.d("apksize", "" + arrData.size());

    }
    class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(context, null, "Loading docx file...");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAlldocFile();
            if (arrData.size() > 0) {
                madapter = new DOCXAdapter(getApplicationContext(), arrData);
                Log.e("xdnjb", "" + arrData.size());

            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (arrData.size() > 0) {
                list_doc.setAdapter(madapter);

            }
 else
            {
                array_state_txt.setVisibility(View.VISIBLE);
            }
            progress.dismiss();
            super.onPostExecute(aVoid);
        }

    }

}
package com.example.filepicker.Activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.filepicker.Adapter.DOCXAdapter;
import com.example.filepicker.Adapter.txtAdapter;
import com.example.filepicker.Model.DocModel;
import com.example.filepicker.Model.txtModel;
import com.example.filepicker.R;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ShowAllTextFile extends AppCompatActivity {
ArrayList<txtModel> arrtxtData;
ListView list_txt;
Context context;
txtAdapter madapter;
TextView array_state_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_txt_file);
        context=this;
        list_txt=findViewById(R.id.list_txt);

        array_state_txt=findViewById(R.id.array_state_txt);

        new LoadApplications().execute();


    }
    public void getAlltxtFile() {
        arrtxtData = new ArrayList<>();
        Iterator<File> fileIterator = FileUtils.iterateFiles(
                Environment.getExternalStorageDirectory(),
                FileFilterUtils.suffixFileFilter("txt"),
                TrueFileFilter.INSTANCE);
        while (fileIterator.hasNext()) {
            File fileINeed = fileIterator.next();

            if (fileINeed != null) {
                txtModel txtModel = new txtModel();
                txtModel.setName(fileINeed.getName());
                Log.d("APKname:", "" + fileINeed.getName());
                txtModel.setFolder(fileINeed.getAbsolutePath());

                File file = new File(fileINeed.getAbsolutePath());

                Date date = new Date((fileINeed.lastModified()));
                String dateFormat = DateFormat.getDateInstance().format(date);

                long file_size = fileINeed.length();

//                audioModel.setDate(dateFormat);
//                audioModel.setSize(String.valueOf(file_size));

                arrtxtData.add(txtModel);
                Log.d("apksize", "" + fileINeed.length());
            }
        }
        Log.d("apksize", "" + arrtxtData.size());

    }
    class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(context, null, "Loading txt file...");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAlltxtFile();
            if (arrtxtData.size() > 0) {
                madapter = new txtAdapter(getApplicationContext(), arrtxtData);
                Log.e("xdnjb", "" + arrtxtData.size());

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (arrtxtData.size() > 0) {
                list_txt.setAdapter(madapter);



            } else {
                array_state_txt.setVisibility(View.VISIBLE);
            }
            progress.dismiss();
            super.onPostExecute(aVoid);
        }

    }

}
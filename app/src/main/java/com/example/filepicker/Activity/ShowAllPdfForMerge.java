package com.example.filepicker.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.filepicker.Adapter.MergePDFAdapter;
import com.example.filepicker.Model.MergePdfModel;
import com.example.filepicker.R;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ShowAllPdfForMerge extends AppCompatActivity {
    ArrayList<MergePdfModel> arrData = new ArrayList<>();
    ListView list_doc;
    Context context;
    TextView array_state_txt;
    private ArrayList<File> fileList = new ArrayList<File>();
    File dir;
    MergePDFAdapter madapter;
    public static ArrayList<String> selectedImage;
    public static ImageButton mergeOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_pdf_merge_file);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray));
        }

        context = this;
        list_doc = findViewById(R.id.list_doc);
        mergeOK = findViewById(R.id.mergeOK);
        array_state_txt = findViewById(R.id.array_state_txt);


        new LoadApplications().execute();
//        if(arrData.size()==0){
//            Toast.makeText(context, "No Data Available...", Toast.LENGTH_SHORT).show();
//        }

        ShowAllPdfForMerge.mergeOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImage = new ArrayList<>();
                for (int i = 0; i < MergePDFAdapter.al_pdf_merge.size(); i++) {

                    if (MergePDFAdapter.al_pdf_merge.get(i).isIschecked() == true) {
                        Log.d("getOriginal", "" + MergePDFAdapter.al_pdf_merge.get(i).isFolder());
                        selectedImage.add(MergePDFAdapter.al_pdf_merge.get(i).isFolder());
                    }
                }
                Log.d("arrsize", "" + selectedImage.size());
                if (selectedImage.size() >= 2) {

                    Intent i = new Intent(getApplicationContext(), ViewPDF.class);
//                i.putExtra("getpdfpath",path);
                    i.putExtra("from", "mergepdf");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(context, "Plese Select two pdf..", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void walkdir(File dir) {
        String pdfPattern = ".pdf";

        File listFile[] = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        Log.d("pdfdata", "" + listFile[i].getName());

                    }
                }
            }
        }
    }

    public void getAllPdfMergeFile() {
        Iterator<File> fileIterator = FileUtils.iterateFiles(
                Environment.getExternalStorageDirectory(),
                FileFilterUtils.suffixFileFilter("pdf"),
                TrueFileFilter.INSTANCE);
        while (fileIterator.hasNext()) {
            File fileINeed = fileIterator.next();

            if (fileINeed != null) {
                MergePdfModel mergePdfModel = new MergePdfModel();
                mergePdfModel.setName(fileINeed.getName());
                Log.d("APKname:", "" + fileINeed.getName());
                mergePdfModel.setFolder(fileINeed.getAbsolutePath());

                File file = new File(fileINeed.getAbsolutePath());

                Date date = new Date((fileINeed.lastModified()));
                String dateFormat = DateFormat.getDateInstance().format(date);

                long file_size = fileINeed.length();


                arrData.add(mergePdfModel);
                Log.d("apksize", "" + fileINeed.length());
            }
        }
        Log.d("apksize", "" + arrData.size());

    }

    class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(context, null, "Loading pdf file...");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getAllPdfMergeFile();

            if (arrData.size() > 0) {
                madapter = new MergePDFAdapter(getApplicationContext(), arrData);
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

}
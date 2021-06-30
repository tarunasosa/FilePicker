package com.example.filepicker.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filepicker.Adapter.DOCXAdapter;
import com.example.filepicker.Adapter.PDFhistoryAdapter;
import com.example.filepicker.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Show_pdf_history extends AppCompatActivity implements DataShareInterface {
    Context context;
    ListView lv_pdf;
    PDFhistoryAdapter madapter;
    File dir;
    TextView array_state_txt;
    String relativePath;
    public static ArrayList<File> fileList = new ArrayList<File>();
    File storeFilePath;
    AlertDialog dialog;
    EditText editrename;
    ImageButton back_btn;
    LinearLayout cancelLayout, OkLayout;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf_history);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray));
        }

        context = this;
        lv_pdf = (ListView) findViewById(R.id.lv_pdf);
        back_btn = findViewById(R.id.back_btn);
        array_state_txt = findViewById(R.id.array_state_txt);

        File file1 = getExternalFilesDir("PDFtoANYFormat");
        relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        storeFilePath = new File(relativePath);
        if (!storeFilePath.exists()) {
            storeFilePath.mkdirs();
            Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
        }

        new LoadApplications().execute();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public ArrayList<File> getfile(File dir) {
        fileList.clear();
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);

                } else {

                    boolean booleanpdf = false;
                    if (listFile[i].getName().endsWith(".pdf")) {

                        for (int j = 0; j < fileList.size(); j++) {
                            if (fileList.get(j).getPath().equals(listFile[i].getName())) {
                                booleanpdf = true;
                            } else {

                            }
                        }

                        if (booleanpdf) {
                            booleanpdf = false;
                        } else {
                            fileList.add(listFile[i]);

                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void onSetValues(String path) {
        File file1 = new File(path);
        Log.d("getsharepath", "" + file1.getAbsolutePath());
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("application/pdf");
        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this, this.getPackageName() + ".provider", file1));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Intent.createChooser(intent, "Share"));

    }

    @Override
    public void onSetRename(String data, int renamepos) {
        Rename_DialogBox(data, renamepos);

    }

    @Override
    public void onDeleteDialog(int deletepos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Are you sure You wanted to delete file");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        String root = Environment.getExternalStorageDirectory().toString();
                        // File file = new File(root + "/storage/emulated/0/showPDF");
                        File file = new File(PDFhistoryAdapter.al_pdf_history.get(deletepos).getAbsolutePath());
                        file.delete();
                        PDFhistoryAdapter.al_pdf_history.remove(file);
                        new LoadApplications().execute();
                        if (file.exists()) {
                            try {
                                file.getCanonicalFile().delete();
                                PDFhistoryAdapter.al_pdf_history.remove(file);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (file.exists()) {
                                context.deleteFile(file.getName());
                                PDFhistoryAdapter.al_pdf_history.remove(file);
                                new LoadApplications().execute();
                            }
                        }
                        Toast.makeText(context, "deleted" + file, Toast.LENGTH_SHORT).show();

                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
            dir = new File(relativePath);
            getfile(dir);
            if (fileList.size() > 0) {

                setAdaper();


            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("size", "" + fileList.size());
            if (fileList.size() > 0) {
                lv_pdf.setAdapter(madapter);


            } else {
                array_state_txt.setVisibility(View.VISIBLE);
            }
            progress.dismiss();
            super.onPostExecute(aVoid);
        }

    }

    private void setAdaper() {
        madapter = new PDFhistoryAdapter(getApplicationContext(), fileList, this);
        Log.e("xdnjb", "" + fileList.size());
    }

    public void Rename_DialogBox(String data, int renamepos) {


        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        // set the custom layout
        final View customLayout
                = this.getLayoutInflater()
                .inflate(
                        R.layout.rename_dialog,
                        null);

//         cancel_btn=findViewById(R.id.cancel_btn);
//         done_btn=findViewById(R.id.done_btn);
//         cancel_btn.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//             }
//         });
        editrename
                = customLayout
                .findViewById(
                        R.id.rename_edittx);
        cancelLayout
                = customLayout
                .findViewById(
                        R.id.cancelLayout);
        OkLayout
                = customLayout
                .findViewById(
                        R.id.OkLayout);

//        builder
//                .setPositiveButton(
//                        "OK",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(
//                                    DialogInterface dialog,
//                                    int which) {
//
//                                strEditAppName = editappname.getText().toString();
//                                SharedPreferences.Editor editor = getSharedPreferences("Short_cutApp", MODE_PRIVATE).edit();
//                                editor.putString("getEditedLabel", strEditAppName);
//                                editor.apply();
//                                appnametext.setText(strEditAppName);
//                                appnametextbelowicon.setText(strEditAppName);
//                            }
//                        });
        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        OkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editrename.getText().toString().equals("")) {
                    Toast.makeText(context, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else {
                    rename(data, renamepos, editrename.getText().toString());
                    dialog.dismiss();
                }

            }
        });
//        editappname.setText(applicationInfo2.loadLabel(getPackageManager()));
        customLayout.setMinimumWidth(100);
        builder.setView(customLayout);
        dialog
                = builder.create();
        dialog.show();

    }

    public void rename(String data, int renamepos, String filename) {
        for (int i = 0; i < PDFhistoryAdapter.al_pdf_history.size(); i++) {
            if (PDFhistoryAdapter.al_pdf_history.get(i).getName().equals(filename)) {
                data = "same";
            }
        }
        Log.d("data", "" + data);
        if (data.equals("same")) {
            Toast.makeText(context, "Name Already Exits", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, " not same..", Toast.LENGTH_SHORT).show();

            Log.d("renamepath", "" + PDFhistoryAdapter.al_pdf_history.get(renamepos).getPath());
            Log.d("renameName", "" + PDFhistoryAdapter.al_pdf_history.get(renamepos).getName());
            String Absolutepath = PDFhistoryAdapter.al_pdf_history.get(renamepos).getPath();
            String renamepath = Absolutepath.replace(PDFhistoryAdapter.al_pdf_history.get(renamepos).getName(), "");
            String name = PDFhistoryAdapter.al_pdf_history.get(renamepos).getName();
            Log.d("renameNewpath", "" + renamepath);


            File from = new File(renamepath, name);
            File to = new File(renamepath, filename + ".pdf");
            from.renameTo(to);


//            File file1 = context.getExternalFilesDir("PDFtoANYFormat");
//            relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
//            storeFilePath = new File(relativePath);
//            if (!storeFilePath.exists()) {
//                storeFilePath.mkdirs();
//                Toast.makeText(context, "create", Toast.LENGTH_SHORT).show();
//            }
//
//            dir = new File(relativePath);


//            PDFhistoryAdapter.al_pdf_history = getfile(dir);
            new LoadApplications().execute();

        }
    }

}
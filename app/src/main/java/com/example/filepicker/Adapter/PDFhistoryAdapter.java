package com.example.filepicker.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.filepicker.Activity.DataShareInterface;
import com.example.filepicker.Activity.ViewAllPDF;
import com.example.filepicker.Activity.ZipToPdf;
import com.example.filepicker.Model.txtModel;
import com.example.filepicker.R;
import com.example.filepicker.ViewPDF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class PDFhistoryAdapter extends ArrayAdapter<File> {


    public static ArrayList<File> fileList = new ArrayList<File>();
    String relativePath;
    File storeFilePath, dir;
    String data;
    int renamepos;
    Context context;
    ViewHolder viewHolder;
    public static ArrayList<File> al_pdf_history;
    DataShareInterface dataShareInterface;


    public PDFhistoryAdapter(Context context, ArrayList<File> al_pdf_history, DataShareInterface dataShareInterface) {
        super(context, R.layout.adapter_pdf_history, al_pdf_history);
        this.context = context;
        this.al_pdf_history = al_pdf_history;
        this.dataShareInterface = dataShareInterface;

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_pdf_history.size() > 0) {
            return al_pdf_history.size();
        } else {
            return 1;
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {


        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_pdf_history, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.btndeletepdf = (ImageView) view.findViewById(R.id.btndeletepdf);
            viewHolder.relative_main = view.findViewById(R.id.relative_main);
            viewHolder.btnsharepdf = view.findViewById(R.id.btnsharepdf);
            viewHolder.name1 = view.findViewById(R.id.name1);
            viewHolder.renamebtn = view.findViewById(R.id.renamebtn);
            viewHolder.path = view.findViewById(R.id.path);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.btndeletepdf.setTag(position);
            viewHolder.btndeletepdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    String root = Environment.getExternalStorageDirectory().toString();
                    // File file = new File(root + "/storage/emulated/0/showPDF");
                    File file = new File(al_pdf_history.get(pos).getAbsolutePath());
                    file.delete();
                    al_pdf_history.remove(file);
                    notifyDataSetChanged();
                    if (file.exists()) {
                        try {
                            file.getCanonicalFile().delete();
                            al_pdf_history.remove(file);
                            notifyDataSetChanged();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (file.exists()) {
                            context.deleteFile(file.getName());
                            al_pdf_history.remove(file);
                            notifyDataSetChanged();
                        }
                    }
                    Toast.makeText(context, "deleted" + file, Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.relative_main.setTag(position);
            viewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos1 = (int) v.getTag();
                    String txt = "from_pdf";
                    Intent i = new Intent(getContext(), ViewAllPDF.class);
                    i.putExtra("getpdfpath", al_pdf_history.get(position).getAbsolutePath());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
            viewHolder.btnsharepdf.setTag(position);
            viewHolder.btnsharepdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos_share = (int) v.getTag();


                    dataShareInterface.onSetValues(al_pdf_history.get(pos_share).getAbsolutePath());


                }
            });
            viewHolder.renamebtn.setTag(position);
            viewHolder.renamebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     renamepos = (int) v.getTag();
                     data = "Not same";


                   dataShareInterface.onSetRename(data,renamepos);


                }
            });
        }

        Log.d("name", "" + al_pdf_history.get(position).getName());

        String data = al_pdf_history.get(position).getName();

        viewHolder.name1.setText(data);

        viewHolder.path.setText(al_pdf_history.get(position).getAbsolutePath());

        return view;

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
        return fileList;
    }

    public class ViewHolder {

        TextView name1, path;
        ImageView btndeletepdf, btnsharepdf, renamebtn;
        RelativeLayout relative_main;

    }

//    public void Rename_DialogBox() {
//
//
//        AlertDialog.Builder builder
//                = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
//        // set the custom layout
//        final View customLayout
//                = context.getLayoutInflater()
//                .inflate(
//                        R.layout.rename_dialog,
//                        null);
//
////         cancel_btn=findViewById(R.id.cancel_btn);
////         done_btn=findViewById(R.id.done_btn);
////         cancel_btn.setOnClickListener(new View.OnClickListener() {
////             @Override
////             public void onClick(View v) {
////             }
////         });
//        editrename
//                = customLayout
//                .findViewById(
//                        R.id.rename_edittx);
//        cancelLayout
//                = customLayout
//                .findViewById(
//                        R.id.cancelLayout);
//        OkLayout
//                = customLayout
//                .findViewById(
//                        R.id.OkLayout);
//
////        builder
////                .setPositiveButton(
////                        "OK",
////                        new DialogInterface.OnClickListener() {
////
////                            @Override
////                            public void onClick(
////                                    DialogInterface dialog,
////                                    int which) {
////
////                                strEditAppName = editappname.getText().toString();
////                                SharedPreferences.Editor editor = getSharedPreferences("Short_cutApp", MODE_PRIVATE).edit();
////                                editor.putString("getEditedLabel", strEditAppName);
////                                editor.apply();
////                                appnametext.setText(strEditAppName);
////                                appnametextbelowicon.setText(strEditAppName);
////                            }
////                        });
//        cancelLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//            }
//        });
//        OkLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(editrename.getText().toString().equals("")){
//                    Toast.makeText(context, "Please Enter Name", Toast.LENGTH_SHORT).show();
//                }else {
//                    rename(data, renamepos, editrename.getText().toString());
//                    dialog.dismiss();
//                }
//
//            }
//        });
////        editappname.setText(applicationInfo2.loadLabel(getPackageManager()));
//        customLayout.setMinimumWidth(100);
//        builder.setView(customLayout);
//        dialog
//                = builder.create();
//        dialog.show();
//
//    }
//    public void rename(String data,int renamepos,String filename){
//        for (int i = 0; i < al_pdf_history.size(); i++) {
//            if (al_pdf_history.get(i).getName().equals(filename)) {
//                data = "same";
//            }
//        }
//        Log.d("data", "" + data);
//        if (data.equals("same")) {
//            Toast.makeText(context, "Name Already Exits", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, " not same..", Toast.LENGTH_SHORT).show();
//
//            Log.d("renamepath", "" + al_pdf_history.get(renamepos).getPath());
//            Log.d("renameName", "" + al_pdf_history.get(renamepos).getName());
//            String Absolutepath = al_pdf_history.get(renamepos).getPath();
//            String renamepath = Absolutepath.replace(al_pdf_history.get(renamepos).getName(), "");
//            String name = al_pdf_history.get(renamepos).getName();
//            Log.d("renameNewpath", "" + renamepath);
//
//
//            File from = new File(renamepath, name);
//            File to = new File(renamepath, filename);
//            from.renameTo(to);
//
//
//            File file1 = context.getExternalFilesDir("PDFtoANYFormat");
//            relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
//            storeFilePath = new File(relativePath);
//            if (!storeFilePath.exists()) {
//                storeFilePath.mkdirs();
//                Toast.makeText(context, "create", Toast.LENGTH_SHORT).show();
//            }
//
//            dir = new File(relativePath);
//
//
//            al_pdf_history = getfile(dir);
//
//            notifyDataSetChanged();
//        }
//    }

}

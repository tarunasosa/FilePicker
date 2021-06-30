package com.example.filepicker.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.filepicker.Activity.DataShareInterface;
import com.example.filepicker.Activity.ViewAllPDF;
import com.example.filepicker.R;

import java.io.File;
import java.util.ArrayList;


public class PDFhistoryAdapter extends ArrayAdapter<File> {


    public static ArrayList<File> fileList = new ArrayList<File>();
    String relativePath;
    File storeFilePath, dir;
    String data;
    int renamepos;
    AlertDialog alertDialog;
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
            viewHolder.relative_main = view.findViewById(R.id.pdf_relative_main);
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
                    dataShareInterface.onDeleteDialog(pos);


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
        viewHolder.path.setSelected(true);

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




}

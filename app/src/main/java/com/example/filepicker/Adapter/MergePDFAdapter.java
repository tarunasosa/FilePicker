package com.example.filepicker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filepicker.Activity.ShowAllPdfForMerge;
import com.example.filepicker.Activity.ShowAllZipFile;
import com.example.filepicker.AllImage.picture_Adapter;
import com.example.filepicker.Model.DocModel;
import com.example.filepicker.Model.MergePdfModel;
import com.example.filepicker.R;
import com.example.filepicker.ViewPDF;

import java.util.ArrayList;


public class MergePDFAdapter extends ArrayAdapter<MergePdfModel> {


    Context context;
    ViewHolder viewHolder;
    public  static  ArrayList<MergePdfModel> al_pdf_merge;
    String path;


    public MergePDFAdapter(Context context, ArrayList<MergePdfModel> al_pdf_merge) {
        super(context, R.layout.adapter_merge_pdf, al_pdf_merge);
        this.context = context;
        this.al_pdf_merge = al_pdf_merge;

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_pdf_merge.size() > 0) {
            return al_pdf_merge.size();
        } else {
            return 1;
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final MergePdfModel pu = al_pdf_merge.get(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_merge_pdf, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mergepdfname = (TextView) view.findViewById(R.id.mergepdfname);
            viewHolder.relative_main = view.findViewById(R.id.relative_main);
            viewHolder.checkmerge = view.findViewById(R.id.checkmerge);

            view.setTag(viewHolder);
        }
//        String path=pu.isFolder();

        viewHolder.mergepdfname.setText(al_pdf_merge.get(position).getName());
        Log.d("name", "" + al_pdf_merge.get(position).getName());
        viewHolder.checkmerge.setTag(position);

        viewHolder.checkmerge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int currentPos= (int) buttonView.getTag();
                 isChecked = false;

                if (al_pdf_merge.get(currentPos).isIschecked() == false) {
                    isChecked = true;
                    path = al_pdf_merge.get(currentPos).isFolder();
                }
                Log.d("response ", currentPos + " " + isChecked);
                al_pdf_merge.get(currentPos).setIschecked(isChecked);

//                   String uri = pictureList.get(currentPos).getImageUri();
//                    Log.d("current ",""+uri);
                path = al_pdf_merge.get(currentPos).isFolder();
                Log.d("current ", "" + path);
                String pathh = "" + path;

//                imagArray.add(path);
//                Log.d("imageArray ", "" + imagArray);
            }
        });




//        viewHolder.relative_main.setTag(position);
//        viewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos= (int) v.getTag();
////
//                Intent i=new Intent(getContext(), ViewPDF.class);
//                i.putExtra("getpdfpath",path);
//                i.putExtra("from","docx");
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);
//                Toast.makeText(context, ""+path, Toast.LENGTH_SHORT).show();
//
//            }
//        });


        return view;

    }

    public class ViewHolder {

        TextView mergepdfname;
        RelativeLayout relative_main;
        CheckBox checkmerge;

    }

}

package com.example.filepicker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filepicker.Model.DocModel;
import com.example.filepicker.R;
import com.example.filepicker.Activity.ViewPDF;

import java.util.ArrayList;


public class DOCXAdapter extends ArrayAdapter<DocModel> {


    Context context;
    ViewHolder viewHolder;
    ArrayList<DocModel> al_doc;

    public DOCXAdapter(Context context, ArrayList<DocModel> al_doc) {
        super(context, R.layout.adapter_docx, al_doc);
        this.context = context;
        this.al_doc = al_doc;

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_doc.size() > 0) {
            return al_doc.size();
        } else {
            return 1;
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final DocModel pu = al_doc.get(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_docx, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.docname = (TextView) view.findViewById(R.id.docname);
            viewHolder.relative_main=view.findViewById(R.id.relative_main);

            view.setTag(viewHolder);
        }

        viewHolder.docname.setText(al_doc.get(position).getName());
        Log.d("name",""+al_doc.get(position).getName());

        String path=pu.isFolder();
        viewHolder.relative_main.setTag(position);
        viewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
//
                Intent i=new Intent(getContext(), ViewPDF.class);
                i.putExtra("getpdfpath",path);
                i.putExtra("from","docx");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                Toast.makeText(context, ""+path, Toast.LENGTH_SHORT).show();

            }
        });


        return view;

    }

    public class ViewHolder {

        TextView docname;
        RelativeLayout relative_main;

    }

}

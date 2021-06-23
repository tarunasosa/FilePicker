package com.example.filepicker.Adapter;

import android.app.Activity;
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
import com.example.filepicker.Model.txtModel;
import com.example.filepicker.R;
import com.example.filepicker.ViewPDF;

import java.util.ArrayList;


public class txtAdapter extends ArrayAdapter<txtModel> {


    Context context;
    ViewHolder viewHolder;
    ArrayList<txtModel> al_txt;

    public txtAdapter(Context context, ArrayList<txtModel> al_txt) {
        super(context, R.layout.adapter_txt, al_txt);
        this.context = context;
        this.al_txt = al_txt;

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_txt.size() > 0) {
            return al_txt.size();
        } else {
            return 1;
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final txtModel pu = al_txt.get(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_txt, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtname = (TextView) view.findViewById(R.id.txtname);
            viewHolder.relative_main=view.findViewById(R.id.relative_main);

            view.setTag(viewHolder);
        }

        viewHolder.txtname.setText(pu.getName());

        String path=pu.isFolder();
        Log.d("name",""+al_txt.get(position).getName());
        viewHolder.relative_main.setTag(position);
        viewHolder.relative_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
//
                Intent i=new Intent(getContext(), ViewPDF.class);
                i.putExtra("getpdfpath",path);
                i.putExtra("from","txt");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
//                ((Activity)context).finish();

//                Toast.makeText(context, ""+path, Toast.LENGTH_SHORT).show();

            }
        });



        return view;

    }

    public class ViewHolder {

        TextView txtname;
        RelativeLayout relative_main;

    }

}

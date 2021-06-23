package com.example.filepicker.AllImage;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filepicker.R;


/**
 *Author CodeBoy722
 *
 * picture_Adapter's ViewHolder
 */

public class PicHolder extends RecyclerView.ViewHolder{

    public static ImageView picture,select_img;
    public static RelativeLayout picture_frame;
    public static CheckBox select_image;


    PicHolder(@NonNull View itemView) {
        super(itemView);

        picture = itemView.findViewById(R.id.image);
        picture_frame = itemView.findViewById(R.id.picture_frame);
        select_image = itemView.findViewById(R.id.select_image);
    }
}

package com.example.filepicker.AllImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.filepicker.R;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

/**
 * Author CodeBoy722
 * <p>
 * A RecyclerView Adapter class that's populates a RecyclerView with images from
 * a folder on the device external storage
 */
public class picture_Adapter extends RecyclerView.Adapter<PicHolder> {

    public static ArrayList<pictureFacer> pictureList;
    private Context pictureContx;
    private final itemClickListener picListerner;
    private int lastSelectedPosition = -1;
    private String path;
    ArrayList<String> imagArray=new ArrayList<>();

    /**
     * @param pictureList  ArrayList of pictureFacer objects
     * @param pictureContx The Activities Context
     * @param picListerner An interface for listening to clicks on the RecyclerView's items
     */
    public picture_Adapter(ArrayList<pictureFacer> pictureList, Context pictureContx, itemClickListener picListerner) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = picListerner;
    }

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.pic_holder_item, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {

        final pictureFacer image = pictureList.get(position);

        if (image.getPicturePath().contains(".gif")) {
            holder.picture_frame.setVisibility(View.GONE);
        } else {
            holder.picture_frame.setVisibility(View.VISIBLE);
            Glide.with(pictureContx)
                    .load(image.getPicturePath())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.picture);



        }


        setTransitionName(holder.picture, String.valueOf(position) + "_image");
        holder.picture_frame.setTag(position);
        Log.d("data:", "" + holder.picture);

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // picListerner.onPicClicked(holder,position, pictureList);
            }
        });
        holder.picture_frame.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int pos = (int) v.getTag();
                if (pos == position) {
                    holder.select_img.setVisibility(View.VISIBLE);
                } else {
                    holder.select_img.setVisibility(View.GONE);
                }
//                if(lastSelectedPosition > 0) {
//                    pictureList.get(lastSelectedPosition).setSelected(false);
//                }
//
//                pictureFacer.setSelected(pictureFacer.isSelected());
//                Toast.makeText(pictureContx, "click", Toast.LENGTH_SHORT).show();
//                holder.select_img.setVisibility(View.VISIBLE);
//                //holder.picture_frame.setBackgroundColor(pictureFacer.isSelected() ? Color.BLACK : Color.BLACK);
//
//                // store last selected item position
//
//                lastSelectedPosition = pos;
                return true;
            }
        });
        holder.select_image.setTag(position);
        holder.select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();
                boolean isChecked = false;

                if (pictureList.get(currentPos).isIschecked() == false) {
                    isChecked = true;
                    path = pictureList.get(currentPos).getPicturePath();
                }
                Log.d("response ", currentPos + " " + isChecked);
                pictureList.get(currentPos).setIschecked(isChecked);

//                   String uri = pictureList.get(currentPos).getImageUri();
//                    Log.d("current ",""+uri);
                path = pictureList.get(currentPos).getPicturePath();
                Log.d("current ", "" + path);
                String pathh = "" + path;

                imagArray.add(path);
                Log.d("imageArray ", "" + imagArray);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}

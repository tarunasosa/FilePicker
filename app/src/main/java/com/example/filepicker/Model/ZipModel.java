package com.example.filepicker.Model;

/**
 * Created by Kashif on 4/7/2018.
 */

public class ZipModel {

    private String name;
    private String folder;
    private  String date,size;

    public ZipModel(){
        this.name = name;
        this.folder = folder;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String isFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


}

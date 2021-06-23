package com.example.filepicker.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.filepicker.R;
import com.pdfview.PDFView;

public class ViewAllPDF extends AppCompatActivity {
PDFView viewAllPDF;
     String getpdfpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_pdf);

        viewAllPDF=findViewById(R.id.viewAllPDF);

        getpdfpath = getIntent().getStringExtra("getpdfpath");
        viewAllPDF.fromFile(getpdfpath).show();
    }
}
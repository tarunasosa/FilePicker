package com.example.filepicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class EditPDF extends AppCompatActivity {
    EditText edit_text;
    String editPDFpath;
    Button savebtn;
     String Filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pdf);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        edit_text=findViewById(R.id.edit_text);

        savebtn=findViewById(R.id.savebtn);
        editPDFpath=getIntent().getStringExtra("editPDFpath");

        extractPDF();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextToPdf();
            }
        });

    }

    private void extractPDF() {
        try {
            // creating a string for
            // storing our extracted text.
            String extractedText = "";

            // creating a variable for pdf reader
            // and passing our PDF file in it.
            PdfReader reader = new PdfReader(editPDFpath);

            // below line is for getting number
            // of pages of PDF file.
            int n = reader.getNumberOfPages();

            // running a for loop to get the data from PDF
            // we are storing that data inside our string.
            for (int i = 0; i < n; i++) {
                extractedText = extractedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
                // to extract the PDF content from the different pages
            }

            // after extracting all the data we are
            // setting that string value to our text view.
            edit_text.setText(extractedText);

            // below line is used for closing reader.
            reader.close();
        } catch (Exception e) {
            // for handling error while extracting the text file.
            edit_text.setText("Error found is : \n" + e);
        }
    }
    public void EditTextToPdf() {

        File file1 = getExternalFilesDir("PDFtoANYFormat");
        String relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        File file = new File(relativePath);
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
        }

        Document doc = new Document();
//        filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
//        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
//
//        filepath = new File(file.getPath() + File.separator +
//                "/" + filename + ".pdf");
        File name=new File(editPDFpath);
        Filename=name.getName();
        Log.d("name",""+name.getName());
        Log.e("filepath", "" + editPDFpath);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(editPDFpath));
            doc.open();
            String mText = edit_text.getText().toString();
            doc.addAuthor("Text To Pdf");
            doc.add(new Paragraph(mText));
            doc.close();
            Toast.makeText(EditPDF.this, "Edit "+Filename + ".pdf\nand saved to\n" + editPDFpath, Toast.LENGTH_SHORT).show();
            Log.d("path", "" + file.getPath());
        } catch (Exception e) {

            Toast.makeText(EditPDF.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

}
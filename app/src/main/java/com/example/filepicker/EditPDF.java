package com.example.filepicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

        edit_text = findViewById(R.id.edit_text);

        savebtn = findViewById(R.id.savebtn);
        editPDFpath = getIntent().getStringExtra("editPDFpath");

        extractPDF();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextToPdf();


                new CountDownTimer(2000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        Intent i = new Intent(getApplicationContext(), ViewPDF.class);
                        i.putExtra("getpdfpath", editPDFpath);
                        Log.d("pathShow",""+editPDFpath);
                        i.putExtra("from", "edited");
                        startActivity(i);
                        finish();
                    }

                }.start();

//
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


        Document doc = new Document();
//        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
//        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
//
//        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
//                "/" + storeFilename + ".pdf");
//        Log.e("filepath", "" + storeFilepathNname);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(editPDFpath));
            doc.open();
//            String mText = txtData.getText().toString();
            doc.addAuthor("Text To Pdf");
            doc.add(new Paragraph(edit_text.getText().toString()));
            doc.close();
            Toast.makeText(EditPDF.this, editPDFpath + ".pdf\nis saved to\n" , Toast.LENGTH_SHORT).show();
//            Log.d("path", "" + file.getPath());
            Log.d("pathShow",""+editPDFpath);
        } catch (Exception e) {
            Toast.makeText(EditPDF.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        Log.d("data", "" + editPDFpath);
    }

}
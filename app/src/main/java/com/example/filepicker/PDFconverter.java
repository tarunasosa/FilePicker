package com.example.filepicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pdfview.PDFView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PDFconverter extends AppCompatActivity {
    String pdfpath;
    TextView txtData;
    PDFView pdfView;
    String filename;
    File filepath;
    Button editbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfconverter);
        pdfpath = getIntent().getStringExtra("pdfpath");
        txtData = findViewById(R.id.txtData);
        pdfView = findViewById(R.id.pdfView);
        editbtn=findViewById(R.id.editbtn);
        ReadTextFromTextFile();
        TextToPdf();

        File data = new File(String.valueOf(filepath));

        pdfView.fromFile(data).show();

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),EditPDF.class);
                i.putExtra("editPDFpath",String.valueOf(filepath));
                startActivity(i);
            }
        });



    }

    public void ReadTextFromTextFile() {
        File file = new File(pdfpath);

//Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while (true) {

                if (!((line = br.readLine()) != null)) break;

                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }

//Find the view by its id

//Set the text
        txtData.setText(text.toString());
    }

    public void TextToPdf() {

        File file1 = getExternalFilesDir("PDFtoANYFormat");
        String relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        File file = new File(relativePath);
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
        }

        Document doc = new Document();
        filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        filepath = new File(file.getPath() + File.separator +
                "/" + filename + ".pdf");
        Log.e("filepath", "" + filepath);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filepath));
            doc.open();
            String mText = txtData.getText().toString();
            doc.addAuthor("Text To Pdf");
            doc.add(new Paragraph(mText));
            doc.close();
            Toast.makeText(PDFconverter.this, filename + ".pdf\nis saved to\n" + filepath, Toast.LENGTH_SHORT).show();
            Log.d("path", "" + file.getPath());
        } catch (Exception e) {
            Toast.makeText(PDFconverter.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


}
package com.example.filepicker.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.filepicker.R;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pdfview.PDFView;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipToPdf extends AppCompatActivity {

    RadioButton rbmerge, rbNmerge;
    AlertDialog dialog;
    SharedPreferences.Editor editor;
    RadioGroup modeGroup1;
    PDFView pdfView;

    String radio_StateMode1;
    File filepath, getzipfilepath;
    ArrayList<String> pdfpath;
String viewpdfpath;
    String filename;
    String getzippath;
    String Dialg;
    File file;
    ArrayList<String> deleteCreatedPDF;
    ArrayList<String> deleteCreatedziptofile;
    String txtFileData;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_to_pdf);
        pdfView = findViewById(R.id.pdfView);
        progressBar=findViewById(R.id.spin_kit);
        editor = getSharedPreferences("mypref", MODE_PRIVATE).edit();

        deleteCreatedPDF = new ArrayList<>();
        deleteCreatedziptofile = new ArrayList<>();

        File file1 = getExternalFilesDir("PDFtoANYFormat");
        String relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        file = new File(relativePath);
        if (!file.exists()) {
            file.mkdirs();
//            Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();

        }

        setDialog();


    }

    public void setDialog() {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.show_dialog,
                        null);
        rbmerge = customLayout.findViewById(R.id.merge);
        rbNmerge = customLayout.findViewById(R.id.Nmerge);
        modeGroup1 = customLayout.findViewById(R.id.modeGroup1);


        getradio();
        setradio();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Dialg = "dismiss";

                init();

            }
        });

        customLayout.setMinimumWidth(100);
        builder.setView(customLayout);
        dialog
                = builder.create();
        dialog.show();


    }

    public void init() {

        getzippath = getIntent().getStringExtra("getpdfpath");
//        Toast.makeText(getApplicationContext(), "" + getzippath, Toast.LENGTH_SHORT).show();

        unpackZip(getzippath);

        Log.d("array", "" + deleteCreatedziptofile);
        Log.d("array", "" + deleteCreatedPDF);

        SharedPreferences prefs1 = getSharedPreferences("mypref", MODE_PRIVATE);
        radio_StateMode1 = prefs1.getString("modeGroup1", "merge");
        Log.d("radioState", "" + radio_StateMode1);


        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                for (int i = 0; i < deleteCreatedziptofile.size(); i++) {


                    File fdelete = new File(deleteCreatedziptofile.get(i));
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            Toast.makeText(getApplicationContext(), "deletezipfile", Toast.LENGTH_SHORT).show();
                            Log.d("from", "delete");
                        } else {
                            Toast.makeText(getApplicationContext(), "not deletezipfile", Toast.LENGTH_SHORT).show();
                            Log.d("from", "not delete");
                        }
                    }

                }
                if (radio_StateMode1.equals("merge")) {
                    try {
                        downloadAndCombinePDFs();


                        new CountDownTimer(3000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                Log.d("filepathhh",""+viewpdfpath);
                                pdfView.fromFile(viewpdfpath).show();

                                progressBar.setVisibility(View.GONE);
                                for (int i = 0; i < deleteCreatedPDF.size(); i++) {


                                    File fdelete = new File(deleteCreatedPDF.get(i));
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            Toast.makeText(getApplicationContext(), "deletepdf", Toast.LENGTH_SHORT).show();
                                            Log.d("from", "deletepdf");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "not deletepdf", Toast.LENGTH_SHORT).show();
                                            Log.d("from", "not deletepdf");
                                        }
                                    }


                                }
//                                new CountDownTimer(1000, 1000) {
//
//                                    public void onTick(long millisUntilFinished) {
//
//                                        //here you can have your logic to set text to edittext
//                                    }
//
//                                    public void onFinish() {
//                                        pdfView.fromFile(filepath.toString()).show();
//                                    }
//
//                                }.start();
                            }

                        }.start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();


//        downloadAndCombinePDFs();


    }

    public void setradio() {


        modeGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.merge) {

                    editor.putString("modeGroup1", "merge");
                    editor.apply();

                }
                if (checkedId == R.id.Nmerge) {

                    editor.putString("modeGroup1", "Nmerge");
                    editor.apply();

                }

            }
        });


    }

    public void getradio() {

        SharedPreferences prefs1 = getSharedPreferences("mypref", MODE_PRIVATE);
        radio_StateMode1 = prefs1.getString("modeGroup1", "merge");


        if (radio_StateMode1.equals("merge")) {
            rbmerge.setChecked(true);
        } else {
            rbNmerge.setChecked(true);
        }
    }

    private boolean unpackZip(String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String getZipfilename;
            is = new FileInputStream(zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                getZipfilename = ze.getName();


                Log.d("getZipfilename:", "" + getZipfilename);

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if ((getZipfilename.contains(".jpg")) || (getZipfilename.contains(".png")) || (getZipfilename.contains(".jpeg"))) {

                    getzipfilepath = new File(file.getPath() + File.separator +
                            "/" + getZipfilename);
                    FileOutputStream fout = new FileOutputStream(getzipfilepath);

                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                    fout.close();
                    zis.closeEntry();
                    Log.d("getZipfilename:", "" + getzipfilepath);
                    deleteCreatedziptofile.add(getzipfilepath.toString());


                    com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
                    filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
                    //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

                    filepath = new File(file.getPath() + File.separator +
                            "/" + filename + ".pdf");
                    Log.e("filepath", "" + filepath);
                    deleteCreatedPDF.add(filepath.toString());
                    try {
                        PdfWriter.getInstance(doc, new FileOutputStream(filepath));
                        doc.open();
                        String mText = "showData.getText().toString()";
                        doc.addAuthor("Text To Pdf");
                        Log.d("filename1:", "" + getZipfilename);

                        Image image = Image.getInstance(getzipfilepath.toString());


                        float documentWidth = doc.getPageSize().getWidth()
                                - doc.leftMargin() - doc.rightMargin();
                        float documentHeight = doc.getPageSize().getHeight()
                                - doc.topMargin() - doc.bottomMargin();
                        image.scaleToFit(documentWidth, documentHeight);


                        float leftMargin = doc.getPageSize().getWidth() - image.getScaledWidth();
                        float lMargin = leftMargin / 2;

                        float topMargin = doc.getPageSize().getHeight() - image.getScaledHeight();
                        float tMargin = topMargin / 2;

                        image.setAbsolutePosition(lMargin, tMargin);

                        image.setAlignment(Image.ALIGN_CENTER);
                        doc.add(image);


                        doc.close();
                        Toast.makeText(ZipToPdf.this, filename + ".pdf\nis saved to\n" + filepath, Toast.LENGTH_SHORT).show();
                        Log.d("path", "" + file.getPath());
                        pdfpath.add(String.valueOf(filepath));

                    } catch (Exception e) {
                        Toast.makeText(ZipToPdf.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                } else if (getZipfilename.contains(".txt")) {

                    getzipfilepath = new File(file.getPath() + File.separator +
                            "/" + getZipfilename);
                    FileOutputStream fout = new FileOutputStream(getzipfilepath);

                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                    fout.close();
                    zis.closeEntry();
                    Log.d("getZipfilename:", "" + getzipfilepath);
                    deleteCreatedziptofile.add(getzipfilepath.toString());
                    ReadTextFromTextFile(getzipfilepath.toString());
                    TextToPdf();

                } else {
                    Toast.makeText(this, "No jpg or txt file found", Toast.LENGTH_SHORT).show();
                }
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void downloadAndCombinePDFs() throws IOException {
        PDFMergerUtility ut = new PDFMergerUtility();
        for (int i = 0; i < deleteCreatedPDF.size(); i++) {

            ut.addSource(new File(deleteCreatedPDF.get(i)));

        }


        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        filepath = null;
        filepath = new File(file.getPath() + File.separator +
                "/" + filename + ".pdf");
        viewpdfpath=filepath.toString();

        final FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        try {
            ut.setDestinationStream(fileOutputStream);
            ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
            Toast.makeText(this, "Done...", Toast.LENGTH_SHORT).show();

        } finally {
            fileOutputStream.close();
        }

        Log.d("data", "" + filepath);
//        SharedPreferences.Editor editor = getSharedPreferences("mypref", MODE_PRIVATE).edit();
//        editor.putString("path", mergepdfpath.toString());
//        editor.apply();

//        viewPDF.fromFile(mergepdfpath).show();


    }

    public void ReadTextFromTextFile(String txtpath) {
        File file = new File(txtpath);
        Log.d("dddddd", "" + txtpath);

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
        txtFileData = text.toString();
    }

    public void TextToPdf() {


        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        filepath = new File(file.getPath() + File.separator +
                "/" + filename + ".pdf");

        Log.e("filepath", "" + filepath);
        deleteCreatedPDF.add(filepath.toString());
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filepath));
            doc.open();
            doc.addAuthor("Text To Pdf");

            doc.add(new Paragraph(txtFileData));

            doc.close();
            Toast.makeText(ZipToPdf.this, filename + ".pdf\nis saved to\n" + filepath, Toast.LENGTH_SHORT).show();
            Log.d("path", "" + file.getPath());
            pdfpath.add(String.valueOf(filepath));

            Log.d("arr", "" + deleteCreatedPDF);
        } catch (Exception e) {
            Toast.makeText(ZipToPdf.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


}
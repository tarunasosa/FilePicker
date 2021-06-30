package com.example.filepicker.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.filepicker.AllImage.ImageDisplay;
import com.example.filepicker.AllImage.MainActivity_gallery;

import com.example.filepicker.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pdfview.PDFView;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ViewPDF extends AppCompatActivity {
    PDFView viewPDF;
    String getpdfpath, from;
    TextView changeText;
    ImageButton back_btn;
    private int FILE_CODE = 001;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    ArrayList<String> pickImage;
    File pdfpath;
    SharedPreferences pref;
    File mergepdfpath;
    ArrayList<String> pdfpathAry;
    File storeFilePath;
    String storeFilename, txtFileData, path;
    File storeFilepathNname;

    public static final int txt_REQUEST_CODE = 100;
    public static final int docx_REQUEST_CODE = 200;
    public static final int zip_REQUEST_CODE = 300;
    ProgressBar progressBar;
    ImageButton fab_add, fab_edit;
    private int TASK_ID = 1;
    private static final int PICK_PDF_FILE = 2;
     Uri document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.gray));
        }


        Log.d("Activityyy", "create");
        back_btn = findViewById(R.id.back_btn);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        pdfpathAry = MainActivity.pdfpathAry;
        viewPDF = findViewById(R.id.viewPDF);
        changeText = findViewById(R.id.changeText);
        fab_add = findViewById(R.id.addpdf_btn);
        fab_edit = findViewById(R.id.editpdf_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        File file1 = getExternalFilesDir("PDFtoANYFormat");
        String relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        storeFilePath = new File(relativePath);
        if (!storeFilePath.exists()) {
            storeFilePath.mkdirs();
            Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
        }


        getpdfpath = getIntent().getStringExtra("getpdfpath");
        from = getIntent().getStringExtra("from");


        Log.d("getpdfpath", "" + getpdfpath);
        Log.d("from", "" + from);
//        pdfpathAry.add(getpdfpath);

        if (from.equals("txt")) {


            ReadTextFromTextFile(getpdfpath);
            TextToPdf();
            pdfpathAry.add(String.valueOf(storeFilepathNname));

            if (pdfpathAry.size() > 1) {
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        try {
                            downloadAndCombinePDFs();
                            storeFilepathNname = mergepdfpath;
                            viewPDF.fromFile(mergepdfpath.toString()).show();
                            progressBar.setVisibility(View.GONE);

                            deleteExitFile();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }.start();

            } else {
                viewPDF.fromFile(storeFilepathNname).show();
                progressBar.setVisibility(View.GONE);
            }

            Log.d("pdfpathAry", "" + pdfpathAry.size());
        } else if (from.equals("edited")) {
            storeFilepathNname = new File(getpdfpath);
            viewPDF.fromFile(getpdfpath).show();
            pdfpathAry.add(String.valueOf(storeFilepathNname));

        } else if (from.equals("docx")) {


            fab_edit.setVisibility(View.GONE);

            Log.d("getpdfpath", "" + getpdfpath);
            DocxtoPdf();
            pdfpathAry.add(String.valueOf(storeFilepathNname));

            Log.d("arrrr", "" + pdfpathAry);

            if (pdfpathAry.size() > 1) {
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        try {
                            downloadAndCombinePDFs();
                            storeFilepathNname = mergepdfpath;
                            Log.d("arrrr1", "" + storeFilepathNname);
                            viewPDF.fromFile(mergepdfpath.toString()).show();
                            progressBar.setVisibility(View.GONE);

                            deleteExitFile();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }.start();

            } else {
                Log.d("arrrr2", "" + storeFilepathNname);
                viewPDF.fromFile(storeFilepathNname).show();
                progressBar.setVisibility(View.GONE);
            }

        } else if (from.equals("image")) {
            changeText.setText("Preview Image To PDF");
            fab_edit.setVisibility(View.GONE);


            Toast.makeText(this, "" + ImageDisplay.selectedImage.size(), Toast.LENGTH_SHORT).show();
            createPDF();
            pdfpathAry.add(String.valueOf(storeFilepathNname));

            if (pdfpathAry.size() > 1) {
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        try {
                            downloadAndCombinePDFs();
                            storeFilepathNname = mergepdfpath;
                            viewPDF.fromFile(mergepdfpath.toString()).show();
                            progressBar.setVisibility(View.GONE);

                            deleteExitFile();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }.start();

            } else {
                viewPDF.fromFile(storeFilepathNname).show();
                progressBar.setVisibility(View.GONE);
            }

            Log.d("array", "" + pdfpathAry);
        } else if (from.equals("mergepdf")) {
            fab_edit.setVisibility(View.GONE);
            fab_add.setVisibility(View.GONE);


            pdfpathAry = ShowAllPdfForMerge.selectedImage;
            Toast.makeText(this, "" + pdfpathAry.toString(), Toast.LENGTH_SHORT).show();
            Log.d("jndjmn ", "" + pdfpathAry.toString());


            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {

                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    try {
                        downloadAndCombinePDFs();
                        storeFilepathNname = mergepdfpath;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    viewPDF.fromFile(storeFilepathNname).show();
                    progressBar.setVisibility(View.GONE);

                }

            }.start();


        }


        Addbtnprocess();
        Editbtnprocess();


        Log.d("data", "" + getpdfpath);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from.equals("mergepdf")) {
        } else {
            MainActivity.pdfpathAry.clear();
        }

        finish();
    }

    public void createPDF() {


        Document doc = new Document();
        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        storeFilepathNname = null;
        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");
        Log.e("filepath", "" + pdfpath);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(storeFilepathNname));
            doc.open();
            String mText = "showData.getText().toString()";
            doc.addAuthor("Text To Pdf");
            for (int i = 0; i < ImageDisplay.selectedImage.size(); i++) {
                Image image = Image.getInstance(ImageDisplay.selectedImage.get(i));

                float documentWidth = doc.getPageSize().getWidth()
                        - doc.leftMargin() - doc.rightMargin();
                float documentHeight = doc.getPageSize().getHeight()
                        - doc.topMargin() - doc.bottomMargin();
                image.scaleToFit(documentWidth, documentHeight);

//                Log.e("Document - Image  = Height", document.getPageSize().getHeight()+" - "+image.getScaledHeight());

                float leftMargin = doc.getPageSize().getWidth() - image.getScaledWidth();
                float lMargin = leftMargin / 2;

                float topMargin = doc.getPageSize().getHeight() - image.getScaledHeight();
                float tMargin = topMargin / 2;

                image.setAbsolutePosition(lMargin, tMargin);

                image.setAlignment(Image.ALIGN_CENTER);
                doc.add(image);
                doc.newPage();


            }

            doc.close();


        } catch (Exception e) {
            Toast.makeText(ViewPDF.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
//        try {
//            downloadAndCombinePDFs();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Activityyy", "resume");


    }

    private void downloadAndCombinePDFs() throws IOException {
        PDFMergerUtility ut = new PDFMergerUtility();

        for (int i = 0; i < pdfpathAry.size(); i++) {

            ut.addSource(new File(pdfpathAry.get(i)));

        }


        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        mergepdfpath = null;
        mergepdfpath = new File(storeFilePath.getPath() + File.separator +
                "/" + filename + ".pdf");

        final FileOutputStream fileOutputStream = new FileOutputStream(mergepdfpath);
        try {
            ut.setDestinationStream(fileOutputStream);
            ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
            Toast.makeText(this, "Done...", Toast.LENGTH_SHORT).show();

        } finally {
            fileOutputStream.close();
        }

        Log.d("data", "" + mergepdfpath);
//        SharedPreferences.Editor editor = getSharedPreferences("mypref", MODE_PRIVATE).edit();
//        editor.putString("path", mergepdfpath.toString());
//        editor.apply();

        viewPDF.fromFile(mergepdfpath).show();


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
            Toast.makeText(this, "file not fromated", Toast.LENGTH_SHORT).show();
            //You'll need to add proper error handling here
        }

//Find the view by its id

//Set the text
        txtFileData = text.toString();
    }

    public void TextToPdf() {


        Document doc = new Document();
        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");
        Log.e("filepath", "" + storeFilepathNname);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(storeFilepathNname));
            doc.open();
//            String mText = txtData.getText().toString();
            doc.addAuthor("Text To Pdf");
            doc.add(new Paragraph(txtFileData));
            doc.close();
            Toast.makeText(ViewPDF.this, storeFilename + ".pdf\nis saved to\n" + storeFilepathNname, Toast.LENGTH_SHORT).show();
//            Log.d("path", "" + file.getPath());
        } catch (Exception e) {

            Toast.makeText(ViewPDF.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        Log.d("data", "" + storeFilepathNname.toString());
    }

    public void Addbtnprocess() {

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((from.equals("txt")) || (from.equals("edited"))) {

                    Intent i = new Intent(getApplicationContext(), ShowAllTextFile.class);
                    startActivity(i);
                    finish();

                } else if (from.equals("docx")) {
//                    Intent i = new Intent(getApplicationContext(), ShowAllDocFile.class);
//                    startActivity(i);

                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    // mime types for MS Word documents
                    String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                    // start activiy
                    startActivityForResult(intent, PICK_PDF_FILE);



                }
                if (from.equals("image")) {

                    Intent i = new Intent(getApplicationContext(), MainActivity_gallery.class);
                    startActivity(i);
                    finish();
                }


            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                document = intent.getData();
                Log.d("path2", "" + document);


                Intent i = new Intent(getApplicationContext(), ViewPDF.class);
                i.putExtra("getpdfpath", String.valueOf(document));
                i.putExtra("from", "docx");
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
//                Toast.makeText(getApplicationContext(), "" + document, Toast.LENGTH_SHORT).show();
                // open the selected document into an Input stream

            }
        }
    }

    public void Editbtnprocess() {

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfpathAry.clear();
                Intent i = new Intent(getApplicationContext(), EditPDF.class);
                i.putExtra("editPDFpath", storeFilepathNname.toString());
                startActivity(i);
                finish();

            }
        });

    }

    public void DocxtoPdf() {

        storeFilename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        storeFilepathNname = new File(storeFilePath.getPath() + File.separator +
                "/" + storeFilename + ".pdf");

         try (InputStream inputStream =
                          getContentResolver().openInputStream(Uri.parse(getpdfpath))) {

            com.aspose.words.Document doc = new com.aspose.words.Document(inputStream);

            // save DOCX as PDF
            Log.d("name", "" + storeFilepathNname);
            doc.save(String.valueOf(storeFilepathNname));
            // show PDF file location in toast as well as treeview (optional)
            Toast.makeText(ViewPDF.this, "File saved in: " + storeFilepathNname, Toast.LENGTH_LONG).show();

            // view converted PDF
//            viewPDFFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(ViewPDF.this, "File not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ViewPDF.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ViewPDF.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteExitFile() {
        for (int i = 0; i < pdfpathAry.size(); i++) {
            File fdelete = new File(pdfpathAry.get(i));
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                    Log.d("from", "delete");
                } else {
                    Toast.makeText(this, "not delete", Toast.LENGTH_SHORT).show();
                    Log.d("from", "not delete");
                }
            }
        }
    }


}








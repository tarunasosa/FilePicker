package com.example.filepicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.filepicker.library.FilePickerActivity;
import com.example.filepicker.library.Utils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;


import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton openCambtn, openStoragebtn;
    FloatingActionsMenu menuMultipleActions;
    private int FILE_CODE = 001;
    ArrayList<String> pickImage;
    String currentPhotoPath;
    public static final int CAMERA_PERM_CODE = 101;
    String pdfpath;
    public static final int CAMERA_REQUEST_CODE = 102;
    ArrayList<String> selectPath=new ArrayList<>();
     boolean Checkdocx,Checkimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        menuMultipleActions = findViewById(R.id.multiple_actions);
        openCambtn = findViewById(R.id.openCambtn);
        openStoragebtn = findViewById(R.id.openGallerybtn);

        openCambtn
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askCameraPermissions();

                    }
                });


        openStoragebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
                // This works if you defined the intent filter
                // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, FILE_CODE);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        pickImage = new ArrayList<>();
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            // Use the provided utility method to parse the result
            List<Uri> files = Utils.getSelectedFilesFromResult(intent);
            for (Uri uri : files) {
                File file = Utils.getFileForUri(uri);

                pickImage.add(file.getPath());
                Toast.makeText(this, "" + file.getPath(), Toast.LENGTH_SHORT).show();
                Log.d("path", "" + file.getPath());
                pdfpath = file.getPath();


                selectPath.add(file.getPath());
//                try {
//                    downloadAndCombinePDFs(pdfpath);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }



//                Intent i = new Intent(getApplicationContext(), PDFconverter.class);
//                i.putExtra("pdfpath", pdfpath);
//                startActivity(i);


                // Do something with the result...
            }
            for(int i =0;i<selectPath.size();i++){
                Checkdocx = selectPath.get(i).contains(".docx");
                Checkimage =  selectPath.get(i).contains(".jpg");

                if(Checkdocx==true){
                    Log.d("data:",""+selectPath.get(i));
                }
                else if(Checkimage==true){
                    Log.d("data:",""+selectPath.get(i));
                }
            }
            Log.d("tag", "ABsolute Url of Image is " + selectPath.toString());
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Log.d("image:", "" + Uri.fromFile(f));

                pickImage.add(String.valueOf(Uri.fromFile(f)));
                Log.d("tag", "ABsolute Url of Image is " + pickImage);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }

        }
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.filepicker",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void createPDF() {
        File file1 = getExternalFilesDir("CSVReader");
        String relativePath = file1.getAbsolutePath() + File.separator + "showPDF";
        File file = new File(relativePath);
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(getApplicationContext(), "create", Toast.LENGTH_SHORT).show();
        }

        Document doc = new Document();
        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        //String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        File filepath = null;
        filepath = new File(file.getPath() + File.separator +
                "/" + filename + ".pdf");
        Log.e("filepath", "" + filepath);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(filepath));
            doc.open();
            String mText = "showData.getText().toString()";
            doc.addAuthor("Text To Pdf");
            for (int i = 0; i < pickImage.size(); i++) {
                Image image = Image.getInstance(pickImage.get(i));

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
            Toast.makeText(MainActivity.this, filename + ".pdf\nis saved to\n" + filepath, Toast.LENGTH_SHORT).show();
            Log.d("path", "" + file.getPath());
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
      private File downloadAndCombinePDFs(String path1) throws IOException {

        PDFMergerUtility ut = new PDFMergerUtility();
        ut.addSource(new File(Environment.getExternalStorageDirectory() + "/a.pdf"));
        ut.addSource(new File(path1));


        final File file = new File(getApplicationContext().getExternalCacheDir(), System.currentTimeMillis() + ".pdf");

        final FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            ut.setDestinationStream(fileOutputStream);
            ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

        } finally {
            fileOutputStream.close();
        }
        return file;
    }

}
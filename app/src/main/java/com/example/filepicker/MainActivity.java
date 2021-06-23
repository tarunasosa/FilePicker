package com.example.filepicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.filepicker.Activity.ShowAllDocFile;
import com.example.filepicker.Activity.ShowAllPdfForMerge;
import com.example.filepicker.Activity.ShowAllTextFile;

import com.example.filepicker.Activity.ShowAllZipFile;
import com.example.filepicker.Activity.Show_pdf_history;
import com.example.filepicker.Activity.URltoPDF;
import com.example.filepicker.Activity.ZipToPdf;
import com.example.filepicker.AllImage.MainActivity_gallery;

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
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class
MainActivity extends AppCompatActivity {

    private int FILE_ZIP_CODE = 001;
    private int FILE_TXT_CODE = 002;
    private int FILE_DOCX_CODE = 003;
    ArrayList<String> pickImage;
    String currentPhotoPath, path;
    public static final int CAMERA_PERM_CODE = 101;
    File pdfpath;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int txt_REQUEST_CODE = 100;
    public static final int docx_REQUEST_CODE = 200;
    public static final int zip_REQUEST_CODE = 300;
    public static final int merge_REQUEST_CODE = 400;

    ImageButton pickimage, picktxtFile,pickdocFile,urlTopdf,zipTopdf;
   public static ArrayList<String> pdfpathAry ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pdfpathAry = new ArrayList<>();
        pickimage = findViewById(R.id.pickimage);
        picktxtFile = findViewById(R.id.picktxtFile);
        pickdocFile=findViewById(R.id.pickdocFile);
        urlTopdf=findViewById(R.id.urlTopdf);
        zipTopdf=findViewById(R.id.zipTopdf);




        zipTopdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), ShowAllZipFile.class);
                startActivity(i);

            }
        });

        urlTopdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), URltoPDF.class);
                startActivity(i);


            }
        });

        pickimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(), MainActivity_gallery.class);
                startActivity(i);


            }
        });
        picktxtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getApplicationContext(), ShowAllTextFile.class);
                startActivity(i);
            }
        });
        pickdocFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i=new Intent(getApplicationContext(), ShowAllDocFile.class);
                startActivity(i);
            }
        });



//        openCambtn
//                .setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        askCameraPermissions();
//
//                    }
//                });


//        openStoragebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
//                startActivity(i);
//            }
//        });


    }




        protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        pickImage = new ArrayList<>();
       if (requestCode == CAMERA_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                File f = new File(currentPhotoPath);
//                Log.d("image:", "" + Uri.fromFile(f));
//
//                pickImage.add(String.valueOf(Uri.fromFile(f)));
//                Log.d("tag", "ABsolute Url of Image is " + pickImage);
//
//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri contentUri = Uri.fromFile(f);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
//            }

        }

       else if (requestCode == txt_REQUEST_CODE) {

           if (resultCode == Activity.RESULT_OK) {
               Uri data=intent.getData();

               Toast.makeText(this, ""+getPath(getApplicationContext(),data), Toast.LENGTH_SHORT).show();

               Log.d("getpath",""+getPath(getApplicationContext(),data));

               path=getPath(getApplicationContext(),data);
               Intent i=new Intent(getApplicationContext(), ViewPDF.class);
               i.putExtra("getpdfpath",path);
               i.putExtra("from","txt");
//               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(i);



           }

       }
       else if (requestCode == docx_REQUEST_CODE) {

           if (resultCode == Activity.RESULT_OK) {
               Uri data=intent.getData();

               Toast.makeText(this, ""+getPath(getApplicationContext(),data), Toast.LENGTH_SHORT).show();


               Log.d("getpath",""+getPath(getApplicationContext(),data));

               path=getPath(getApplicationContext(),data);
               Intent i=new Intent(getApplicationContext(), ViewPDF.class);
               i.putExtra("getpdfpath",path);
               i.putExtra("from","docx");
//               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(i);
           }


       }
       else if (requestCode == zip_REQUEST_CODE) {

           if (resultCode == Activity.RESULT_OK) {
               Uri data=intent.getData();

               Toast.makeText(this, ""+getPath(getApplicationContext(),data), Toast.LENGTH_SHORT).show();


               Log.d("getpath",""+getPath(getApplicationContext(),data));

               path=getPath(getApplicationContext(),data);
               Intent i=new Intent(getApplicationContext(), ZipToPdf.class);
               i.putExtra("getpdfpath",path);
               i.putExtra("from","zip");
//               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(i);
           }

       }
       else if (requestCode == merge_REQUEST_CODE) {

           if (resultCode == Activity.RESULT_OK) {

               int count = intent.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
               for(int i = 0; i < count; i++){
                   pdfpathAry.add(getPath(getApplicationContext(),intent.getClipData().getItemAt(i).getUri()));
                   Log.d("intentArray",""+getPath(getApplicationContext(),intent.getClipData().getItemAt(i).getUri())) ;

               }

//               Uri data=intent.getData();
//
//               Toast.makeText(this, ""+getPath(getApplicationContext(),data), Toast.LENGTH_SHORT).show();
//
//
//               Log.d("getpath",""+getPath(getApplicationContext(),data));

               path=getPath(getApplicationContext(), Uri.parse("dxfcz"));
               Intent i=new Intent(getApplicationContext(), ZipToPdf.class);
               i.putExtra("getpdfpath",path);
               i.putExtra("from","mergepdf");
//               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(i);
           }

       }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pdfpathAry.clear();
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
        pdfpath = null;
        pdfpath = new File(file.getPath() + File.separator +
                "/" + filename + ".pdf");
        Log.e("filepath", "" + pdfpath);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(pdfpath));
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
                if (pickImage.size() >= 1) {
                    doc.newPage();
                }


            }

            doc.close();
            Toast.makeText(MainActivity.this, filename + ".pdf\nis saved to\n" + pdfpath, Toast.LENGTH_SHORT).show();
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        Log.d("geturi",""+uri.toString());
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri))
            {
                Uri contentUri = null;
                try {
                    String fileName = getFilePath(context, uri);
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                    }
                    String id = DocumentsContract.getDocumentId(uri);
                    if (id.startsWith("raw:")) {
                        id = id.replaceFirst("raw:", "");
                        File file = new File(id);
                        if (file.exists()) return id;
                    }
                    contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if(contentUri != null){
                    return getDataColumn(context, contentUri, null, null);
                }
                else {
                    return null;
                }
            }
            // MediaProvider

            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }else if("document".equals(type)){
                    contentUri= Uri.parse("//media/external/document/media");

                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return uri.getPath();
    }

    public String getFilePath(Context context, Uri uri) {
        Cursor cursor = null;
        String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME
        };

        try {
            if (uri == null) return null;
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                return cursor.getString(index);
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri)
    {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }





}
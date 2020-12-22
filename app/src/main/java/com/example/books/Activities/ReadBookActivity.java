package com.example.books.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.books.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.shockwave.pdfium.PdfDocument;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadBookActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    private PDFView pdfView;
    private TextView text;
    Integer pageNumber;
    InputStream pdfFileName;

    Integer currentPageNumber;
    Integer savedPage;
    DatabaseReference  classicClickRef,comicClickPostRef,fantasyClickPostRef, fictionClickRef,
            historyClickPostRef, magicalClickRef,medicalsClickPostRef, romanceClickRef, scienceClickRef;
    String postKey;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);
        pdfView = findViewById(R.id.pdfView);

        postKey = getIntent().getExtras().get("postKey").toString();
        classicClickRef = FirebaseDatabase.getInstance().getReference().child("classic").child(postKey);
        comicClickPostRef = FirebaseDatabase.getInstance().getReference().child("comic").child(postKey);
        fantasyClickPostRef = FirebaseDatabase.getInstance().getReference().child("fantasy").child(postKey);
        fictionClickRef = FirebaseDatabase.getInstance().getReference().child("fiction").child(postKey);
        historyClickPostRef = FirebaseDatabase.getInstance().getReference().child("history").child(postKey);
        magicalClickRef = FirebaseDatabase.getInstance().getReference().child("magical").child(postKey);
        medicalsClickPostRef = FirebaseDatabase.getInstance().getReference().child("medicals").child(postKey);
        romanceClickRef = FirebaseDatabase.getInstance().getReference().child("romance").child(postKey);
        scienceClickRef = FirebaseDatabase.getInstance().getReference().child("science").child(postKey);

        displayPdf(classicClickRef);
        displayPdf(comicClickPostRef);
        displayPdf(fantasyClickPostRef);
        displayPdf(fictionClickRef);
        displayPdf(historyClickPostRef);
        displayPdf(magicalClickRef);
        displayPdf(medicalsClickPostRef);
        displayPdf(romanceClickRef);
        displayPdf(scienceClickRef);

        SharedPreferences mySharedPreferences = getPreferences(Context.MODE_PRIVATE);

        savedPage = mySharedPreferences.getInt("retrievedPage",0);

        pageNumber = savedPage;

    }

    void displayPdf(DatabaseReference reference){

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@Nullable DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    String content = dataSnapshot.child("content").getValue().toString();
                    Toast.makeText(ReadBookActivity.this, "updated", Toast.LENGTH_SHORT).show();

                    Bundle bundle = getIntent().getExtras();
                    if (bundle != null) {
                    }
                    new ReadBookActivity.RetrievePDFStream().execute(content);
                }


            }
            @Override
            public void onCancelled( DatabaseError databaseError) {

                Toast.makeText(ReadBookActivity.this, "field to load", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {

        ProgressDialog progressDialog;
        protected void onPreExecute()
        {
//            progressDialog = new ProgressDialog(ReadBookActivity.this);
//            progressDialog.setTitle("getting the book content...");
//            progressDialog.setMessage("Please wait...");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();

        }
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {

                URL urlx = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) urlx.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                Toast.makeText(ReadBookActivity.this, "please check your internet connection", Toast.LENGTH_SHORT).show();
                return null;
            }
            return inputStream;

        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfFileName = inputStream;
            int currentPage = (pdfView.getCurrentPage() );
            pdfView.fromStream(inputStream)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(true)
                    .onLoad(ReadBookActivity.this)
                    .scrollHandle(new DefaultScrollHandle(getApplicationContext()))
                    .defaultPage(pageNumber)
                    .load();


//            progressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences mySharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = mySharedPreferences.edit();


        savedPage = pdfView.getCurrentPage();
        myEditor.putInt("retrievedPage",savedPage);
        myEditor.apply();


    }
    }

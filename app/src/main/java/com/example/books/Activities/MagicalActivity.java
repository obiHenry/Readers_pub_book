package com.example.books.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.example.books.models.BookModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MagicalActivity extends AppCompatActivity {
    private DatabaseReference magicalBookRef;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<BookModel, ClassicActivity.BookViewHolder> firebaseRecyclerAdapter;
    TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magical);
        toolbar = findViewById(R.id.magicalToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Magical");

        progressBar = findViewById(R.id.magicalProgressBar);
        magicalBookRef = FirebaseDatabase.getInstance().getReference().child("magical");
        recyclerView = findViewById(R.id.magicalRecyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        displayComicBooks();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void displayComicBooks() {


        Query displayBooksInDescending = magicalBookRef;
        FirebaseRecyclerOptions<BookModel> option = new FirebaseRecyclerOptions.Builder<BookModel>()
                .setQuery(displayBooksInDescending,BookModel.class)
                .build();

        FirebaseRecyclerAdapter<BookModel, ComicActivity.BookViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookModel, ComicActivity.BookViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull ComicActivity.BookViewHolder holder, int position, @NonNull BookModel model) {

                final String postKey = getRef(position).getKey();
                holder.setTitle(model.getTitle());
                holder.setBookCover(getApplicationContext(), model.getBookCover());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ClickIntent = new Intent(MagicalActivity.this, ClickBookCoverActivity.class);
                        ClickIntent.putExtra("postKey", postKey);
                        startActivity(ClickIntent);
                    }
                });

            }

            @NonNull
            @Override
            public ComicActivity.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.books_layout, parent, false);
                return new ComicActivity.BookViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder{
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
        }



        public void setTitle(String title) {
            TextView Title = itemView.findViewById(R.id.layoutBookTitle);
            Title.setText(title);

        }


        public void setBookCover(Context context, String bookCover){
            ImageView BookCover = itemView.findViewById(R.id.layoutImageView);
            Picasso.with(context).load(bookCover).placeholder(R.drawable.ic_comic).into(BookCover);
        }



    }
}

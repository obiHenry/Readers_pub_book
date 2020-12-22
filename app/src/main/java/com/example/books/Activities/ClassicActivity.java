package com.example.books.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.R;
import com.example.books.models.BookModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import java.util.Objects;

public class ClassicActivity extends AppCompatActivity {
    private DatabaseReference classicBookRef;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FirebaseRecyclerAdapter<BookModel, BookViewHolder> firebaseRecyclerAdapter;
    TextView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic);
        toolbar = findViewById(R.id.classicToolbar);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Classic");

        progressBar = findViewById(R.id.progressBar);
        classicBookRef = FirebaseDatabase.getInstance().getReference().child("classic");
        recyclerView = findViewById(R.id.classicRecyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        view = findViewById(R.id.errorMessage);
        displayClassicBooks();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void displayClassicBooks() {


        Query displayBooksInDescending = classicBookRef;
        FirebaseRecyclerOptions<BookModel> option = new FirebaseRecyclerOptions.Builder<BookModel>()
                .setQuery(displayBooksInDescending,BookModel.class)
                .build();

        progressBar.setVisibility(View.VISIBLE);

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookModel, BookViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull BookModel model) {
                final String postKey = getRef(position).getKey();
                holder.setTitle(model.getTitle());
                holder.setBookCover(getApplicationContext(), model.getBookCover());
                progressBar.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);

                if (firebaseRecyclerAdapter.onFailedToRecycleView(holder)){

                    view.setVisibility(View.VISIBLE);
                }else {
                    view.setVisibility(View.INVISIBLE);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ClickIntent = new Intent(ClassicActivity.this, ClickBookCoverActivity.class);
                        ClickIntent.putExtra("postKey", postKey);
                        startActivity(ClickIntent);
                    }
                });

            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.books_layout, parent, false);
                return new BookViewHolder(view);
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
            Picasso.with(context).load(bookCover).placeholder(R.drawable.ic_classic).into(BookCover);
        }



    }
}

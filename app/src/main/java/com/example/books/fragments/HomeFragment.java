package com.example.books.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.Activities.ClassicActivity;
import com.example.books.Activities.ClickBookCoverActivity;
import com.example.books.Activities.ComicActivity;
import com.example.books.Activities.FantasyActivity;
import com.example.books.Activities.FictionActivity;
import com.example.books.Activities.HistoryActivity;
import com.example.books.Activities.MagicalActivity;
import com.example.books.Activities.MedicalsActivity;
import com.example.books.Activities.RomanceActivity;
import com.example.books.Activities.ScienceActivity;
import com.example.books.R;
import com.example.books.models.BookModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class HomeFragment extends Fragment {
    private DatabaseReference classicBookRef,comicBookRef,fantasyBookRef,fictionBookRef,
            historyBookRef,magicalBookRef,medicalsBookRef,romanceBookRef,scienceBookRef;
    Toolbar toolbar, toolbar1, toolbar2, toolbar3,toolbar4, toolbar5,toolbar6, toolbar7,toolbar8;
    RecyclerView classicRecyclerView, comicRecyclerView,fantasyRecyclerView,fictionRecyclerView,
            historyRecyclerView,magicalRecyclerView,medicalsRecyclerView, romanceRecyclerView,scienceRecyclerView;
    ProgressBar classicProgressBar, comicProgressBar,fantasyProgressBar,fictionProgressBar,
            historyProgressBar,magicalProgressBar,medicalsProgressBar,romanceProgressBar,scienceProgressBar;
    FirebaseRecyclerAdapter<BookModel, BookViewHolder> firebaseRecyclerAdapter;
    TextView text,text1,text2,text3,text4,text5,text6,text7,text8;
    FirebaseRecyclerOptions<BookModel> option;
    String title, gottenTitle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        text = view.findViewById(R.id.classicViewAll);
        text1 = view.findViewById(R.id.comicViewAll);
        text2 = view.findViewById(R.id.fantasyViewAll);
        text3 = view.findViewById(R.id.fictionViewAll);
        text4 = view.findViewById(R.id.historyViewAll);
        text5 = view.findViewById(R.id.magicalViewAll);
        text6 = view.findViewById(R.id.medicalViewAll);
        text7 = view.findViewById(R.id.romanceViewAll);
        text8 = view.findViewById(R.id.scienceViewAll);
//        text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        setOnClickListener(text,ClassicActivity.class);
        setOnClickListener(text1, ComicActivity.class);
        setOnClickListener(text2,FantasyActivity.class);
        setOnClickListener(text3, FictionActivity.class);
        setOnClickListener(text4, HistoryActivity.class);
        setOnClickListener(text5, MagicalActivity.class);
        setOnClickListener(text6, MedicalsActivity.class);
        setOnClickListener(text7, RomanceActivity.class);
        setOnClickListener(text8, ScienceActivity.class);




        toolbar = view.findViewById(R.id.cToolbar);
        toolbar1 = view.findViewById(R.id.classicToolbar);
        toolbar2 = view.findViewById(R.id.fantasyToolbar);
        toolbar3 = view.findViewById(R.id.fictionToolbar);
        toolbar4 = view.findViewById(R.id.historyToolbar);
        toolbar5 = view.findViewById(R.id.magicalToolbar);
        toolbar6 = view.findViewById(R.id.medicalsToolbar);
        toolbar7 = view.findViewById(R.id.romanceToolbar);
        toolbar8 = view.findViewById(R.id.scienceToolbar);



        setToobarInvisible(toolbar);
        setToobarInvisible(toolbar1);
        setToobarInvisible(toolbar2);
        setToobarInvisible(toolbar3);
        setToobarInvisible(toolbar4);
        setToobarInvisible(toolbar5);
        setToobarInvisible(toolbar6);
        setToobarInvisible(toolbar7);
        setToobarInvisible(toolbar8);



        classicProgressBar = view.findViewById(R.id.progressBar);
        comicProgressBar = view.findViewById(R.id.comicProgressBar);
        fantasyProgressBar = view.findViewById(R.id.fantasyProgressBar);
        fictionProgressBar = view.findViewById(R.id.fictionProgressBar);
        historyProgressBar = view.findViewById(R.id.historyProgressBar);
        magicalProgressBar = view.findViewById(R.id.magicalProgressBar);
        medicalsProgressBar = view.findViewById(R.id.medicalsProgressBar);
        romanceProgressBar = view.findViewById(R.id.romanceProgressBar);
        scienceProgressBar = view.findViewById(R.id.scienceProgressBar);

        classicBookRef = FirebaseDatabase.getInstance().getReference().child("classic");
        comicBookRef = FirebaseDatabase.getInstance().getReference().child("comic");
        fantasyBookRef = FirebaseDatabase.getInstance().getReference().child("fantasy");
        fictionBookRef = FirebaseDatabase.getInstance().getReference().child("fiction");
        historyBookRef = FirebaseDatabase.getInstance().getReference().child("history");
        magicalBookRef = FirebaseDatabase.getInstance().getReference().child("magical");
        medicalsBookRef = FirebaseDatabase.getInstance().getReference().child("medicals");
        romanceBookRef = FirebaseDatabase.getInstance().getReference().child("romance");
        scienceBookRef = FirebaseDatabase.getInstance().getReference().child("science");

        classicRecyclerView = view.findViewById(R.id.classicRecyclerview);
        comicRecyclerView = view.findViewById(R.id.comicRecyclerview);
        fantasyRecyclerView = view.findViewById(R.id.fantasyRecyclerview);
        fictionRecyclerView = view.findViewById(R.id.fictionRecyclerview);
        historyRecyclerView = view.findViewById(R.id.historyRecyclerview);
        magicalRecyclerView = view.findViewById(R.id.magicalRecyclerview);
        medicalsRecyclerView = view.findViewById(R.id.medicalsRecyclerview);
        romanceRecyclerView = view.findViewById(R.id.romanceRecyclerview);
        scienceRecyclerView = view.findViewById(R.id.scienceRecyclerview);




        setRecyclerview(classicRecyclerView);
        setRecyclerview(comicRecyclerView);
        setRecyclerview(fantasyRecyclerView);
        setRecyclerview(fictionRecyclerView);
        setRecyclerview(historyRecyclerView);
        setRecyclerview(magicalRecyclerView);
        setRecyclerview(medicalsRecyclerView);
        setRecyclerview(romanceRecyclerView);
        setRecyclerview(scienceRecyclerView);

        displayBooks(classicBookRef,classicRecyclerView,classicProgressBar);
        displayBooks(comicBookRef,comicRecyclerView,comicProgressBar);
        displayBooks(fantasyBookRef,fantasyRecyclerView,fantasyProgressBar);
        displayBooks(fictionBookRef,fictionRecyclerView,fictionProgressBar);
        displayBooks(historyBookRef,historyRecyclerView,historyProgressBar);
        displayBooks(magicalBookRef,magicalRecyclerView,magicalProgressBar);
        displayBooks(medicalsBookRef,medicalsRecyclerView,medicalsProgressBar);
        displayBooks(romanceBookRef,romanceRecyclerView,romanceProgressBar);
        displayBooks(scienceBookRef,scienceRecyclerView,scienceProgressBar);


        SharedPreferences mySharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        title = mySharedPreferences.getString("title", "");
        gottenTitle = title;
    }

    void setOnClickListener(TextView textView, final Class context){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ClickIntent = new Intent(getContext(), context);
//                ClickIntent.putExtra("postKey", postKey);
                startActivity(ClickIntent);
            }
        });
    }
    void setToobarInvisible(Toolbar tool){
        tool.setVisibility(View.GONE);
    }

    void setRecyclerview(RecyclerView recyclerView1){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView1.setLayoutManager(linearLayoutManager);
    }

    private void displayBooks(DatabaseReference BookRef,
                                   RecyclerView recyclerView1,
                                   final ProgressBar progressBar1) {


        Query displayBooksInDescending = BookRef.limitToFirst(10);
        option = new FirebaseRecyclerOptions.Builder<BookModel>()
                .setQuery(displayBooksInDescending,BookModel.class)
                .build();

        progressBar1.setVisibility(View.VISIBLE);
//        PreferenceManager.getDefaultSharedPreferences(getContext())
//                .edit()
//                .p("option", option).apply();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookModel, BookViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull BookModel model) {

                final String postKey = getRef(position).getKey();
                holder.setTitle(model.getTitle());
                holder.setBookCover(getContext(), model.getBookCover());
                progressBar1.setVisibility(View.INVISIBLE);
//                text1.setVisibility(View.INVISIBLE);

                if (firebaseRecyclerAdapter.onFailedToRecycleView(holder)){

                }



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent ClickIntent = new Intent(getActivity(), ClickBookCoverActivity.class);
                        ClickIntent.putExtra("postKey", postKey);
                        startActivity(ClickIntent);
                    }
                });

            }

            @NonNull
            @Override
            public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_book_layout, parent, false);
                return new BookViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView1.setAdapter(firebaseRecyclerAdapter);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder{
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
        }



        public void setTitle(String title1) {
            TextView Title = itemView.findViewById(R.id.layoutBookTitle1);

            SharedPreferences mySharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor myEditor = mySharedPreferences.edit();


            title = title1;
            myEditor.putString("gottenTitle",title);
            myEditor.apply();


            Title.setText(gottenTitle);

        }


        public void setBookCover(Context context, String bookCover){
            ImageView BookCover = itemView.findViewById(R.id.layoutImageView1);
            Picasso.with(context).load(bookCover).placeholder(R.drawable.ic_comic).into(BookCover);
        }



    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        SharedPreferences mySharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor myEditor = mySharedPreferences.edit();
//
//
//        savedPage = pdfView.getCurrentPage();
//        myEditor.putInt("retrievedPage",savedPage);
//        myEditor.apply();
//    }



}

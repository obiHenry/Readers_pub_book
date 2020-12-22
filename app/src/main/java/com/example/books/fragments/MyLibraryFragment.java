package com.example.books.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.Activities.ClickBookCoverActivity;
import com.example.books.Activities.ComicActivity;
import com.example.books.Activities.MainActivity;
import com.example.books.Activities.ScienceActivity;
import com.example.books.Activities.UsersSetUpActivity;
import com.example.books.R;
import com.example.books.models.BookModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyLibraryFragment extends Fragment {


    public RecyclerView recyclerView;
    private DatabaseReference myBooksRef, userRef;
    private FirebaseAuth firebaseAuth;
    TextView textView;
    Button goToHome;
//    String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.Message);
        goToHome = view.findViewById(R.id.goToHome);
        recyclerView = view.findViewById(R.id.myLibraryRecyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        String currentUser = firebaseAuth.getCurrentUser().getUid();


        myBooksRef = FirebaseDatabase.getInstance().getReference().child("library").child(currentUser);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        displayMyBooks();
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserFragmentHome();
            }
        });
    }

    private void sendUserFragmentHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void displayMyBooks() {
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null){
            String currentUserId = firebaseAuth.getCurrentUser().getUid();
            final Query displayBooksInDescending = myBooksRef.orderByChild("uid")
                    .startAt(currentUserId).endAt(currentUserId + "\uf8ff");

            displayBooksInDescending.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        FirebaseRecyclerOptions<BookModel> option = new FirebaseRecyclerOptions.Builder<BookModel>()
                                .setQuery(displayBooksInDescending,BookModel.class)
                                .build();

                        FirebaseRecyclerAdapter<BookModel, BookViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BookModel, BookViewHolder>(option) {
                            @Override
                            protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull BookModel model) {

                                final String postKey = getRef(position).getKey();
                                holder.setTitle(model.getTitle());
                                holder.setBookCover(getContext(), model.getBookCover());


                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent ClickIntent = new Intent(getContext(), ClickBookCoverActivity.class);
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
                    }else {
                        textView.setVisibility(View.VISIBLE);
                        goToHome.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else {
            textView.setVisibility(View.VISIBLE);
            goToHome.setVisibility(View.VISIBLE);
        }


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

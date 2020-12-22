package com.example.books.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.books.R;
import com.example.books.models.BookModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ClickBookCoverActivity extends AppCompatActivity {

    private ImageView BookCover;
    private TextView Description, Title;
    private DatabaseReference comicClickPostRef, classicClickRef,fantasyClickPostRef, fictionClickRef,fantasy,
            historyClickPostRef, magicalClickRef,medicalsClickPostRef, romanceClickRef, scienceClickRef, libraryRef, usersRef,clickedLibraryBook;
    public String  description, bookCover, title, postKey, content,libraryId ;
    Toolbar toolbar;
    Button readButton, addToLibrary, addedBook;
    FirebaseAuth firebaseAuth;
    FirebaseUser User;
    ProgressDialog progressDialog;
    String currentUserId;
    String id;
    Boolean isAdded = false;
    final List<BookModel> libraries = new ArrayList<>();
    BookModel library ;
//    Map<String, Object> library;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_book_cover);
        toolbar = findViewById(R.id.clickToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();
        User = firebaseAuth.getCurrentUser();
//        currentUserId = User.getUid();
        progressDialog = new ProgressDialog(this);
         currentUserId = firebaseAuth.getCurrentUser().getUid();


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
        libraryRef = FirebaseDatabase.getInstance().getReference().child("library").child(currentUserId);
        clickedLibraryBook = libraryRef.child(postKey);
       fantasy = FirebaseDatabase.getInstance().getReference().child("fantasy");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");



        setAddToLibraryButton();
        BookCover = (ImageView) findViewById(R.id.bookCover);
        Description = (TextView) findViewById(R.id.bookDescription);
        readButton = findViewById(R.id.classicReadButton);
        addToLibrary = findViewById(R.id.addToLibrary);
        addedBook = findViewById(R.id.addedLibraryButton);


        displayClickedBook(classicClickRef);
        displayClickedBook(comicClickPostRef);
        displayClickedBook(fantasyClickPostRef);
        displayClickedBook(fictionClickRef);
        displayClickedBook(historyClickPostRef);
        displayClickedBook(magicalClickRef);
        displayClickedBook(medicalsClickPostRef);
        displayClickedBook(romanceClickRef);
        displayClickedBook(scienceClickRef);
//        displayClickedBook(clickedLibraryBook);

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent ClickIntent = new Intent(ClickBookCoverActivity.this, ReadBookActivity.class);
                ClickIntent.putExtra("postKey", postKey);
                startActivity(ClickIntent);


            }
        });
        addToLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User == null){
                    sendUserToLoginActivity();
                }else{


                    libraryRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.hasChild(currentUserId)){
                                    checkUsersExistence();

                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

    @Override
    protected void onStart() {

        setAddToLibraryButton();
        super.onStart();

    }

    private void checkUsersExistence() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (!dataSnapshot.hasChild(currentUserId)) {
                    sendToSetUpActivity();
                }else if(isAdded){
                    Toast.makeText(ClickBookCoverActivity.this, "this book is already added to library", Toast.LENGTH_SHORT).show();
                }else{




                    AddPickedBookToLibrary();

//                    if (isEqual()){
//                        addToLibrary.setVisibility(View.GONE);
//                        addedBook.setVisibility(View.VISIBLE);
//                    }else {
////                        addToLibrary.setVisibility(View.VISIBLE);
//                        addedBook.setVisibility(View.GONE);
//                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddPickedBookToLibrary() {
//        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("adding new book");
        progressDialog.setMessage("please wait, while we update your book");

        if (this.hasWindowFocus()) {progressDialog.show();
        }

        progressDialog.setProgress(0);
        progressDialog.setCanceledOnTouchOutside(true);

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat(" dd-MMM-YYYY", Locale.ENGLISH);
        String saveCurrentDate = currentDate.format(callForDate.getTime());

        Calendar callForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm",Locale.ENGLISH);
        String saveCurrentTime = currentTime.format(callForTime.getTime());

        String postRandomName = saveCurrentDate + saveCurrentTime;

        HashMap<String, Object> postMap = new HashMap<>();
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();


        postMap.put("content", content);
        postMap.put("description", description);
        postMap.put("bookCover", bookCover);
        postMap.put("title", title);
        postMap.put("uid", currentUserId);
        postMap.put("id", id);
        postMap.put("isAdded", true);

        libraryRef.child(postRandomName).updateChildren(postMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (hasWindowFocus()) {progressDialog.dismiss();
                            }
                            Toast.makeText(ClickBookCoverActivity.this, "new book is updated Successfully ", Toast.LENGTH_SHORT).show();
                            setAddToLibraryButton();
//                            sendUserToMainActivity();
                        }else {
                            String message = Objects.requireNonNull(task.getException()).getMessage();
                            if (hasWindowFocus()) {progressDialog.dismiss();
                            }
                            Toast.makeText(ClickBookCoverActivity.this, "Error occurred while updating ur book:  " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    public void setAddToLibraryButton() {

        System.out.println("this is d d set button");

        libraryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    libraries.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    library = ( HashMap<String, HashMap>) postSnapshot.getValue();


                        library = postSnapshot.getValue(BookModel.class);
                        libraryId = library.getId();
                        libraries.add(library);

                        System.out.println("this is  librayid" + libraryId);

                        if (libraryId == id ) {
                            System.out.println("this is true");
                            isAdded = true;
                        }else{
                            System.out.println("this is  libray false");
                            isAdded = false;
                        }
                        System.out.println("The library ids: " + libraryId);
//                    libraries.add(library);

                        // here you can access to name property like university.name

                    }

                } else {
                    System.out.println("this is false");
                    isAdded = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

    }

    void displayClickedBook(DatabaseReference reference){
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@Nullable DataSnapshot dataSnapshot) {

                 if (dataSnapshot.exists()){
                     title = dataSnapshot.child("title").getValue().toString();
                     description = dataSnapshot.child("description").getValue().toString();
                     bookCover = dataSnapshot.child("bookCover").getValue().toString();
                     content = dataSnapshot.child("content").getValue().toString();
                     id = dataSnapshot.child("id").getValue().toString();
                     System.out.println("this is d book id "+ id);



//                    databaseUserId = dataSnapshot.child("uid").getValue().toString();
                     getSupportActionBar().setTitle(title);
                     Description.setText(description);
                     Picasso.with(ClickBookCoverActivity.this).load(bookCover).into(BookCover);

                     if (isAdded){
                         addToLibrary.setVisibility(View.GONE);
                         addedBook.setVisibility(View.VISIBLE);

                     }else  {
                         addToLibrary.setVisibility(View.VISIBLE);
                         addedBook.setVisibility(View.GONE);

                     }
                 }
             }

             @Override
             public void onCancelled( DatabaseError databaseError) {

             }
         });

     }

    private void sendToSetUpActivity() {
        Intent intent = new Intent(this,UsersSetUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);

    }


//    boolean isEqual(){
//        if (fantasy.child("description") == libraryRef.child("description")){
//            addToLibrary.setVisibility(View.GONE);
//            return true;
//        }else {
//            addToLibrary.setVisibility(View.VISIBLE);
//
//            return false;
//        }
//    }



}
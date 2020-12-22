package com.example.books.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.books.Activities.ClassicActivity;
import com.example.books.Activities.ComicActivity;
import com.example.books.Activities.FantasyActivity;
import com.example.books.Activities.FictionActivity;
import com.example.books.Activities.HistoryActivity;
import com.example.books.Activities.MagicalActivity;
import com.example.books.Activities.MedicalsActivity;
import com.example.books.Activities.RomanceActivity;
import com.example.books.Activities.ScienceActivity;
import com.example.books.R;
import com.example.books.fragments.HomeFragment;
import com.example.books.fragments.MyLibraryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.Maintoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawerLayout);
        View navView = navigationView.inflateHeaderView(R.layout.headers_view);
        TextView navHeading = (TextView)navView.findViewById(R.id.genres);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                PickItem(menuItem);
                return false;
            }
        });


        ChipNavigationBar bottomNavigation = findViewById(R.id.bottom_navigation_view);
        bottomNavigation.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private ChipNavigationBar.OnItemSelectedListener navListener = new ChipNavigationBar.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int i) {
            Fragment selectedItem = null;
            switch (i) {
                case R.id.home:
                    selectedItem = new HomeFragment();
                    break;
                case R.id.my_library:
                    selectedItem = new MyLibraryFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedItem).commit();
        }

    };

    public void PickItem(MenuItem menuItem){
        int id = menuItem.getItemId();

        if (id == R.id.classic){
            Intent intent = new Intent(this, ClassicActivity.class);
            startActivity(intent);

        }else if (id ==R.id.comic){
            Intent intent = new Intent(this, ComicActivity.class);
            startActivity(intent);

        }else if(id == R.id.fantasy){
            Intent intent = new Intent(this, FantasyActivity.class);
            startActivity(intent);

        }else if(id == R.id.fiction){
            Intent intent = new Intent(this, FictionActivity.class);
            startActivity(intent);

        }else if (id == R.id.history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);

        }else if (id == R.id.magical){
            Intent intent = new Intent(this, MagicalActivity.class);
            startActivity(intent);

        }else if (id == R.id.medicals){
            Intent intent = new Intent(this, MedicalsActivity.class);
            startActivity(intent);

        }else if (id == R.id.romance){
            Intent intent = new Intent(this, RomanceActivity.class);
            startActivity(intent);

        }else if (id == R.id.science){
            Intent intent = new Intent(this, ScienceActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.header_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.search) {
            Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();

        } else if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
            return super.onOptionsItemSelected(item);
        }

    }

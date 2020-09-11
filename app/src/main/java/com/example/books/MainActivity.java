package com.example.books;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

        actionBargitDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
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

        if (id == R.id.menuAddNewPost){


        }else if (id == R.id.home){

        }else if (id ==R.id.findfriends){


        }else if(id == R.id.friends){



        }else if(id == R.id.message){




        }else if (id == R.id.action_settings) {


        }else if (id == R.id.menuProfile){

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
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.search) {
            Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();

        } else if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
            return super.onOptionsItemSelected(item);
        }

    }

package com.example.dealspotter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.dealspotter.ui.AboutFragment;
import com.example.dealspotter.ui.FavoriteFragment;
import com.example.dealspotter.ui.LeaderBoardFragment;
import com.example.dealspotter.ui.MapFragment;
import com.example.dealspotter.ui.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity  {

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Toolbar toolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);


        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.bottom_home){
                    openFragment(new AboutFragment());
                    return true;
                }else if(itemId == R.id.bottom_maps){
                    openFragment(new MapFragment());
                    return true;
                }else if(itemId==R.id.bottom_leaderboard){
                    openFragment(new LeaderBoardFragment());
                    return true;
                }
                else if(itemId == R.id.bottom_favorites){
                    openFragment(new FavoriteFragment());
                    return true;
                }else if (itemId == R.id.bottom_profile){
                    openFragment(new ProfileFragment());
                    return true;
                }
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();
        openFragment(new AboutFragment());

/*        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Test",Toast.LENGTH_LONG).show();
            }
        });*/
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.commit();
    }
}
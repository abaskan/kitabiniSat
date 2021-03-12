package com.example.kitabinisat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView userBottomNavigationView;
    private Fragment tempFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userBottomNavigatonMenu();
    }


    public void userBottomNavigatonMenu() {
        userBottomNavigationView = findViewById(R.id.userBottomNavigationView);
        getSupportFragmentManager().beginTransaction().add(R.id.userFragmentHolder,new FragmentMainPage()).commit();

        userBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.actionMainPage:
                        tempFragment = new FragmentMainPage();
                        break;
                    case R.id.actionFavorites:
                        tempFragment = new FragmentFavorites();
                        break;

                    case R.id.actionChats:
                        tempFragment = new FragmentChatList();
                        break;

                    case R.id.actionSettings:
                        tempFragment = new FragmentSettings();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.userFragmentHolder,tempFragment).commit();

                return true;
            }
        });
    }
}
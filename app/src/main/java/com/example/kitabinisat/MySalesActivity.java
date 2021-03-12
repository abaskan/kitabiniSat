package com.example.kitabinisat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MySalesActivity extends AppCompatActivity {
    private TabLayout mySalesTabs;
    private ViewPager mySalesViewPager;
    private Toolbar mySalesToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sales);
        toolbar();
        viewPagerAdapter();
    }

    public void toolbar(){
        mySalesToolbar = findViewById(R.id.mySalesToolbar);
        mySalesToolbar.setTitle("İlanlarım");
        mySalesToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        mySalesToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mySalesToolbar);
        mySalesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void viewPagerAdapter(){
        mySalesTabs = findViewById(R.id.mySalesTabs);
        mySalesViewPager = findViewById(R.id.mySalesViewPager);
        mySalesViewPagerAdapter adapter = new mySalesViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentSelling(),"Satılıyor");
        adapter.addFragment(new FragmentSold(),"Satıldı");

        mySalesViewPager.setAdapter(adapter);
        mySalesTabs.setupWithViewPager(mySalesViewPager);
    }

    class mySalesViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public mySalesViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment (Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }
}
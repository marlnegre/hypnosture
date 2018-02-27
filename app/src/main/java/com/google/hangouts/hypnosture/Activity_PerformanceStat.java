package com.google.hangouts.hypnosture;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class Activity_PerformanceStat extends AppCompatActivity {

    private SectionPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_statistics);

        setBackBtn();
        mSectionsPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //setupTabLayout();
    }

    public void setBackBtn() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

//    private void setupTabLayout() {
//
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_gallery);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_exercise);
//        tabLayout.getTabAt(3).setIcon(R.drawable.ic_action_name);
//    }



    private void setupViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new Week_one(), "Week 1");
        adapter.addFragment(new Week_Two(), "Week 2");
        adapter.addFragment(new Week_Three(), "Week 3");
        adapter.addFragment(new Week_Four(), "Week 4");

        viewPager.setAdapter(adapter);
    }



}



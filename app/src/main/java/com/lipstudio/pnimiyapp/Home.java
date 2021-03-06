package com.lipstudio.pnimiyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import android.content.Context;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , View.OnFocusChangeListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbarTitle = findViewById(R.id.toolbar_title);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.homePage);
        }
        AttendanceSheet attendanceSheet = new AttendanceSheet("ארוחת צהריים");
        Log.e("currentDate",attendanceSheet.toString());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof AdminPageFragment) {
            toolbarTitle.setText(getResources().getText(R.string.admin_page));
            toolbar.setBackgroundColor(getResources().getColor(R.color.primaryColorGreen));
        }
        else if(currentFragment instanceof HomeFragment){
            toolbarTitle.setText(getResources().getText(R.string.app_name));
            toolbar.setBackgroundColor(getResources().getColor(R.color.primaryColorGreen));
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        switch (menuItem.getItemId()) {
            case R.id.adminPage:
                if(currentFragment instanceof AdminPageFragment){
                    break;
                }
                transaction.replace(R.id.fragment_container, new AdminPageFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                toolbar.setBackgroundResource(R.color.primaryColorGreen);
                toolbarTitle.setText(getResources().getText(R.string.admin_page));
                break;

            case R.id.homePage:
                if(currentFragment instanceof HomeFragment){
                    break;
                }
                transaction.replace(R.id.fragment_container, new HomeFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                setTheme(R.style.AppTheme);
                toolbar.setBackgroundResource(R.color.primaryColorGreen);
                toolbarTitle.setText(getResources().getText(R.string.app_name));
                break;

            case R.id.schedulePage:
                if(currentFragment instanceof ScheduleFragment){
                    break;
                }
                transaction.replace(R.id.fragment_container, new ScheduleFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                setTheme(R.style.timePickerTheme);
                toolbar.setBackgroundResource(R.color.blue);
                toolbarTitle.setText(getResources().getText(R.string.schedule_page));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            v.setBackgroundResource(R.drawable.edit_text_back_cyan_selected);
        else
            v.setBackgroundResource(R.drawable.edit_text_background);
    }


}

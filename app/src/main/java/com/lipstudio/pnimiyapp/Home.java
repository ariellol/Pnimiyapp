package com.lipstudio.pnimiyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , View.OnFocusChangeListener, View.OnClickListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    TextView headerUserName;
    TextView headerGroup;
    LinearLayout logout;
    SharedPreferences userPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        UserHelper userHelper = new UserHelper(this);
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

        View headerView = navigationView.getHeaderView(0);
        headerUserName = headerView.findViewById(R.id.nameInNav);
        headerGroup = headerView.findViewById(R.id.groupInNav);
        logout = headerView.findViewById(R.id.log_out);
        logout.setOnClickListener(this);
        String sharedPrefName = "userSharedPreferences";
        userPreferences = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        if(!userPreferences.contains("init")){
            Intent loginIntent = new Intent(this,Login.class);
            startActivity(loginIntent);
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
        }
        headerUserName.setText(userPreferences.getString("userName","אורח"));
        if(!userPreferences.getBoolean("userEducator",false)) {
            headerGroup.setText(userPreferences.getString("group","ללא שכבה"));
            navigationView.getMenu().findItem(R.id.adminPage).setVisible(false);
        }
        else{
            Set<String> defSet = new HashSet<>();
            defSet.add("ללא שכבה");
            String groups = userPreferences.getStringSet("groups",defSet).toString();
            headerGroup.setText(groups);
        }


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if(getSupportFragmentManager().getBackStackEntryCount() <=1){
            exitApp();
        }
        else
            super.onBackPressed();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof AdminPageFragment) {
            toolbarTitle.setText(getResources().getText(R.string.admin_page));
            toolbar.setBackgroundColor(getResources().getColor(R.color.primaryColorGreen));
        }
        else if(currentFragment instanceof HomeFragment){
            toolbar.setBackgroundColor(getResources().getColor(R.color.primaryColorGreen));
        }
        else if(currentFragment instanceof InstagramFragment){
            toolbarTitle.setText(getResources().getText(R.string.content_page));
            toolbar.setBackgroundResource(R.color.orange);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
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
                toolbar.setBackgroundResource(R.color.blue);
                toolbarTitle.setText(getResources().getText(R.string.schedule_page));
                break;
            case R.id.contentPage:
                if (currentFragment instanceof InstagramFragment){
                    break;
                }
                transaction.replace(R.id.fragment_container, new InstagramFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                toolbar.setBackgroundResource(R.color.orange);
                toolbarTitle.setText(getResources().getText(R.string.content_page));
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

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.log_out){
            userPreferences.edit().clear().apply();
            Intent loginIntent = new Intent(this,Login.class);
            startActivity(loginIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                userPreferences.edit().putBoolean("permissionGranted",true);
            else
                userPreferences.edit().putBoolean("permissionGranted",false);
        }
    }

    public void exitApp(){
        Dialog dialogExitApp = new Dialog(this);
        dialogExitApp.setContentView(R.layout.dialog_exit_app);
        dialogExitApp.show();
        Button exit = dialogExitApp.findViewById(R.id.exit_app);
        Button cancelExit = dialogExitApp.findViewById(R.id.cancel_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogExitApp.dismiss();
                finish();
            }
        });
        cancelExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogExitApp.dismiss();
            }
        });
    }

}

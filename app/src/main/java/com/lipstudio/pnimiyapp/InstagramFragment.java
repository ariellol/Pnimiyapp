package com.lipstudio.pnimiyapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.state.State;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.Permission;
import java.util.ArrayList;

public class InstagramFragment extends Fragment {

    Context context;
    InstagramHelper instagramHelper;
    ListView postsListView;
    ArrayList<Post> posts;
    PostAdapter postAdapter;
    SharedPreferences userPref;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPref = getActivity().getSharedPreferences("userSharedPreferences",Context.MODE_PRIVATE);
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        context = getActivity();
        instagramHelper = new InstagramHelper(context);
        instagramHelper.open();
        posts = instagramHelper.getAllPosts();
        instagramHelper.close();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.pnimistagram_fragment,container,false);
        postsListView = parent.findViewById(R.id.postsListView);
        postAdapter = new PostAdapter(context,posts);
        postsListView.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();
        setHasOptionsMenu(true);

        return parent;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (userPref.getBoolean("editContent",false)){
            inflater.inflate(R.menu.add_post_menu,menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addPostMenuItem){
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AddPostFragment())
            .addToBackStack(null).commit();
            ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("הוספת פוסט");
        }
        return true;
    }


}

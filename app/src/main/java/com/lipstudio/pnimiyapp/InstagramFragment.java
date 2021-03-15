package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.state.State;
import androidx.fragment.app.Fragment;

public class InstagramFragment extends Fragment {

    Context context;
    Toolbar toolbar;
    Uri imageUri;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.pnimistagram_fragment,container,false);
        imageUri = Uri.parse(getArguments().getString("imageUri"));
        LinearLayout layout = parent.findViewById(R.id.layout);
        ImageView image = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        image.setLayoutParams(params);
        image.setImageURI(imageUri);
        layout.addView(image);
        setHasOptionsMenu(true);
        return parent;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_post_menu,menu);
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

package com.lipstudio.pnimiyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;

import kotlin.reflect.KParameter;

public class AddPostFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener{

    public static final int PICK_IMAGE = 1;

    EditText descriptionEt;
    LinearLayout addImageBtn;
    Button publishPostBtn;
    ImageView preview;
    Uri imageUri = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.add_post_fragment,container,false);

        descriptionEt = parent.findViewById(R.id.postDescriptionEt);
        addImageBtn = parent.findViewById(R.id.addImageButton);
        addImageBtn.setOnClickListener(this);
        publishPostBtn = parent.findViewById(R.id.publishPostBtn);
        publishPostBtn.setOnClickListener(this);
        preview = parent.findViewById(R.id.imagePreview);
        return parent;
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
        if(v.getId() == R.id.publishPostBtn){
            if(!imageUri.equals(null)) {
                Bundle imageBundle = new Bundle();
                imageBundle.putString("imageUri",imageUri.toString());
                InstagramFragment instagramFragment = new InstagramFragment();
                instagramFragment.setArguments(imageBundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, instagramFragment)
                        .addToBackStack(null).commit();
            }
        }

        else{
            File imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String imagePath = imagesDir.getPath();
            Uri imageUri = Uri.parse(imagePath);
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setDataAndType(imageUri,"image/*");
            startActivityForResult(Intent.createChooser(galleryIntent,"בחירת תמונה"), PICK_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE){
            imageUri = data.getData();
            preview.setImageURI(imageUri);
            preview.setVisibility(View.VISIBLE);
        }
    }
}
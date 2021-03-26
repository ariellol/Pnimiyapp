package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> implements View.OnClickListener{

    private UserHelper userHelper;
    private Context context;
    private ArrayList<Post> posts;

    public PostAdapter(Context context,ArrayList<Post> posts){
        super(context,0,posts);
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.post_custom_layout,parent,false);

        TextView fullName = convertView.findViewById(R.id.nameInPost);
        ImageView image = convertView.findViewById(R.id.imageInPost);
        TextView description = convertView.findViewById(R.id.descriptionInPost);
        TextView link = convertView.findViewById(R.id.linkInPost);
        ImageView options = convertView.findViewById(R.id.optionsInPost);
        options.setOnClickListener(this);

        Post post = posts.get(position);

        userHelper = new UserHelper(context);
        userHelper.open();
        fullName.setText(userHelper.getUserNameById(post.getUserId()));
        userHelper.close();
        image.setImageURI(Uri.parse(post.getImageLink()));
        description.setText(post.getDescription());
        link.setText(post.getUrlLink());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        v.setTag("options");
    }
}

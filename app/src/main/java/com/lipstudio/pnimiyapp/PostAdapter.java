package com.lipstudio.pnimiyapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private UserHelper userHelper;
    private Context context;
    private ArrayList<Post> posts;
    private SharedPreferences userSp;
    private InstagramHelper instagramHelper;
    private int positionEdit;

    Dialog dialogDeletePost;
    Button deletePost;
    Button cancelDelete;

    public PostAdapter(Context context,ArrayList<Post> posts){
        super(context,0,posts);
        this.context = context;
        this.posts = posts;
        userSp = context.getSharedPreferences("userSharedPreferences",Context.MODE_PRIVATE);

        dialogDeletePost = new Dialog(context);
        dialogDeletePost.setContentView(R.layout.dialog_delete_image);
        deletePost = dialogDeletePost.findViewById(R.id.deletePostInDialog);
        deletePost.setOnClickListener(this);
        cancelDelete = dialogDeletePost.findViewById(R.id.cancelDeletePostInDialog);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PostViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.post_custom_layout, parent, false);
            holder = createViewHolderFrom(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (PostViewHolder) convertView.getTag();
        }
            Post post = posts.get(position);

            userHelper = new UserHelper(context);
            userHelper.open();
            holder.fullName.setText(userHelper.getUserNameById(post.getUserId()));
            userHelper.close();
            Log.e("userId",post.getUserId()+"");
            holder.image.setImageURI(Uri.parse(post.getImageLink()));
            holder.description.setText(post.getDescription());
            holder.link.setText(post.getUrlLink());
            if (userSp.getBoolean("userEducator",false)){
                holder.options.setImageResource(R.drawable.ic_baseline_more_vert_24);
                holder.options.setOnClickListener(this);
                holder.options.setTag(position);
            }
            if (post.getUserId() == userSp.getLong("userId",0)){
                holder.options.setImageResource(R.drawable.ic_baseline_more_vert_24);
                holder.options.setOnClickListener(this);
                holder.options.setTag(position);
            }

        return convertView;
    }

    private PostViewHolder createViewHolderFrom(View convertView){
        TextView fullName = convertView.findViewById(R.id.nameInPost);
        ImageView image = convertView.findViewById(R.id.imageInPost);
        TextView description = convertView.findViewById(R.id.descriptionInPost);
        TextView link = convertView.findViewById(R.id.linkInPost);
        ImageView options = convertView.findViewById(R.id.imageOptions);
        return new PostViewHolder(fullName,image,description,link,options);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.deletePostInDialog){
            instagramHelper = new InstagramHelper(context);
            instagramHelper.open();
            instagramHelper.deletePost(posts.get(positionEdit).getPostId());
            instagramHelper.close();
            posts.remove(positionEdit);
            this.notifyDataSetChanged();
            dialogDeletePost.dismiss();
        }
        else {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenuInflater().inflate(R.menu.post_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            positionEdit = (int) v.getTag();
            popupMenu.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.deletePostMenuItem){
            dialogDeletePost.show();

            cancelDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeletePost.dismiss();
                }
            });

        }
        else{
            Bundle bundle = new Bundle();
            bundle.putSerializable("postToEdit",posts.get(positionEdit));
            AddPostFragment addPostFragment = new AddPostFragment();
            addPostFragment.setArguments(bundle);
            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,addPostFragment)
                    .addToBackStack(null).commit();
        }
        return false;
    }

    private static class PostViewHolder{
        final TextView fullName;
        final ImageView image;
        final TextView description;
        final TextView link;
        final ImageView options;

        public PostViewHolder(TextView fullName, ImageView image, TextView description, TextView link, ImageView options){
            this.fullName = fullName;
            this.image = image;
            this.description = description;
            this.link = link;
            this.options = options;
        }
    }

}

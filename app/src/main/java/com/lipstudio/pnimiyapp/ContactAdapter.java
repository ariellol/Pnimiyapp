package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<User> implements View.OnClickListener{

    Context context;
    ArrayList<User> users;
    TextView phoneNumber;
    public ContactAdapter(Context context, ArrayList<User> users){
        super(context,0,users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_contact_info_layout,parent,false);

        TextView fullName = convertView.findViewById(R.id.contact_full_name);
        phoneNumber = convertView.findViewById(R.id.contact_phone);
        ImageView callContact = convertView.findViewById(R.id.callContact);
        ImageView sendMessage = convertView.findViewById(R.id.sendMessage);
        ImageView addContact = convertView.findViewById(R.id.addContact);
        callContact.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        addContact.setOnClickListener(this);

        User user = this.users.get(position);
        fullName.setText(fullName.getText().toString() + user.getFirstName() + " " + user.getLastName());
        phoneNumber.setText(phoneNumber.getText() + user.getPhoneNumber());
        fullName.setTag(position);
        sendMessage.setTag(position);
        callContact.setTag(position);
        phoneNumber.setTag(position);
        addContact.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        User currentUser = users.get((int) v.getTag());

        if (v.getId() == R.id.callContact) {
            intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+currentUser.getPhoneNumber()));
        }
        else if(v.getId() == R.id.sendMessage){
            intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+currentUser.getPhoneNumber()));

        }

        else{

            intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

            intent.putExtra(ContactsContract.Intents.Insert.NAME, currentUser.getFirstName() +" "+ currentUser.getLastName());
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, currentUser.getPhoneNumber());
        }


            context.startActivity(intent);

    }

}

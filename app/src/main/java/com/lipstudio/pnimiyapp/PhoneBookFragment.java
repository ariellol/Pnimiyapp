package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class PhoneBookFragment extends Fragment {

    Context context;
    ListView phoneListView;
    ContactAdapter contactAdapter;
    ArrayList<User> users;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        UserHelper userHelper = new UserHelper(context);
        userHelper.open();
        users = userHelper.getAllUsers();
        userHelper.close();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.phone_book_fragment,container,false);

        phoneListView = parent.findViewById(R.id.phone_list_view);
        contactAdapter = new ContactAdapter(context,users);
        phoneListView.setAdapter(contactAdapter);

        return parent;
    }
}

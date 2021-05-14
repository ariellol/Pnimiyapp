package com.lipstudio.pnimiyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class IdeaBoxFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener{

    EditText ideaTitle;
    EditText ideaDescription;
    Button sendIdea;

    SharedPreferences userPref;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.idea_box_fragment,container,false);
        context = getActivity();

        ideaTitle = parent.findViewById(R.id.idea_title);
        ideaDescription = parent.findViewById(R.id.idea_description);
        sendIdea = parent.findViewById(R.id.send_idea);
        sendIdea.setOnClickListener(this);

        userPref = context.getSharedPreferences("userSharedPreferences",Context.MODE_PRIVATE);

        return parent;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_idea){
            if (ideaTitle.getText().toString().equals("") || ideaDescription.getText().toString().equals("")){
                Toast.makeText(context, "חובה למלא את כל השדות.", Toast.LENGTH_SHORT).show();
            }
            else{
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = formatter.format(Calendar.getInstance().getTime());
                Idea idea = new Idea(ideaTitle.getText().toString(), ideaDescription.getText().toString(), userPref.getLong("userId",0),currentDate);

                RequestHelper helper = new RequestHelper(context);
                helper.open();
                helper.insertIdea(idea);
                helper.close();
                Toast.makeText(context, "הצעתך נשלחה בהצלחה!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, new HomeFragment());
                transaction.addToBackStack(null);
                ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText(getResources().getText(R.string.app_name));
                getActivity().findViewById(R.id.toolbar).setBackgroundResource(R.color.primaryColorGreen);
                transaction.commit();
            }

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            v.setBackgroundResource(R.drawable.edit_text_back_cyan_selected);
        else
            v.setBackgroundResource(R.drawable.edit_text_background);
    }


}

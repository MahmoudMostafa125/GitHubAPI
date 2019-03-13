package com.example.githubapi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class CustomDialogClass extends DialogFragment implements
        android.view.View.OnClickListener {

    public Button owner, repo;
    String LinkOwner, LinkRepo;

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.custom_dialog, container, false);
        //get data from bundle
        LinkOwner = getArguments().getString("ownerURL");
        LinkRepo = getArguments().getString("repoURL");


        owner = view.findViewById(R.id.btn_yes);
        repo = view.findViewById(R.id.btn_no);
        owner.setOnClickListener(this);
        repo.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                String url = LinkOwner;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.btn_no:
                String url2 = LinkRepo;
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(url2));
                startActivity(i2);
                break;
            default:
                break;
        }
        dismiss();
    }
}

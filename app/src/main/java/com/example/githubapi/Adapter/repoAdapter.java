package com.example.githubapi.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.githubapi.CustomDialogClass;
import com.example.githubapi.R;
import com.example.githubapi.model.DefaultResponse;

import java.util.List;

public class repoAdapter extends RecyclerView.Adapter<repoAdapter.GitViewHolder> {

    private Context mCtx;
    private List<DefaultResponse> MyGitList;

    public repoAdapter(Context mCtx, List<DefaultResponse> GitList) {
        this.mCtx = mCtx;
        this.MyGitList = GitList;
    }

    @NonNull
    @Override
    public repoAdapter.GitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new repoAdapter.GitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull repoAdapter.GitViewHolder holder, final int position) {

        final DefaultResponse results = MyGitList.get(position);
        if (results.getFullName() != null) {
            holder.title.setText(results.getFullName());
        }
        if (results.getDescription() != null) {
            holder.discreption.setText(results.getDescription());
        }
        if (results.getName() != null) {
            holder.name.setText(results.getName());
        }

        if (results.getFork() || results.getFork() == null) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#7CFC00"));
        }


        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                FragmentTransaction ft = ((AppCompatActivity) mCtx).getSupportFragmentManager().beginTransaction();
                Fragment prev = ((AppCompatActivity) mCtx).getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment dialogFragment = new CustomDialogClass();
                Bundle args = new Bundle();
                args.putString("ownerURL", results.getOwner().getHtmlUrl());
                args.putString("repoURL", results.getHtmlUrl());
                dialogFragment.setArguments(args);
                dialogFragment.show(ft, "dialog");
                return false;
            }
        });

    }

    //////////
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return MyGitList.size();
    }

    public class GitViewHolder extends RecyclerView.ViewHolder {
        TextView title, discreption, name;
        CardView cardView;

        public GitViewHolder(View itemview) {
            super(itemview);
            title = itemview.findViewById(R.id.rename);
            discreption = itemview.findViewById(R.id.desc);
            name = itemview.findViewById(R.id.ownername);
            cardView = itemview.findViewById(R.id.card);
        }
    }

    //add new repo items to the list
    public void addRepo(List<DefaultResponse> GitList) {
        for (DefaultResponse DR : GitList) {
            MyGitList.add(DR);
        }
        notifyDataSetChanged();
    }
}
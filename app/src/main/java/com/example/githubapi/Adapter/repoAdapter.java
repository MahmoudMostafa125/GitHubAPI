package com.example.githubapi.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.title.setText(results.getFullName());

    }

    @Override
    public int getItemCount() {
        return MyGitList.size();
    }

    public class GitViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public GitViewHolder(View itemview) {
            super(itemview);
            title = itemview.findViewById(R.id.rename);
        }
    }


    public void addRepo(List<DefaultResponse> GitList) {
        for (DefaultResponse DR : GitList) {
            MyGitList.add(DR);
        }
        notifyDataSetChanged();
    }
}
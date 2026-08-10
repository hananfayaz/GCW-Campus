package com.example.gcwcampus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gcwcampus.R;

import java.util.ArrayList;

public class RecyclerSubjectFacultySemAdapter extends RecyclerView.Adapter<RecyclerSubjectFacultySemAdapter.ViewHolder> {

    Context context;
    ArrayList<String> sem;

    public RecyclerSubjectFacultySemAdapter(Context context, ArrayList<String> sem) {
        this.context = context;
        this.sem = sem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_faculty,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSem.setText(sem.get(position));
    }

    @Override
    public int getItemCount() {
        return sem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSem = itemView.findViewById(R.id.txt_view);
        }
    }
}

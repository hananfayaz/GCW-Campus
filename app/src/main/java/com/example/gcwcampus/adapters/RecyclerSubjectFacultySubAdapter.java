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

public class RecyclerSubjectFacultySubAdapter extends RecyclerView.Adapter<RecyclerSubjectFacultySubAdapter.ViewHolder> {

    Context context;
    ArrayList<String> subject;

    public RecyclerSubjectFacultySubAdapter(Context context, ArrayList<String> subject) {
        this.context = context;
        this.subject = subject;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_faculty,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSubject.setText(subject.get(position));
    }

    @Override
    public int getItemCount() {
        return subject.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.txt_view);
        }
    }
}

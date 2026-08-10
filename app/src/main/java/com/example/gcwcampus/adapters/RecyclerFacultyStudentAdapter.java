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

public class RecyclerFacultyStudentAdapter extends RecyclerView.Adapter<RecyclerFacultyStudentAdapter.ViewHolder> {

    Context context;
    ArrayList<String> student;

    public RecyclerFacultyStudentAdapter(Context context, ArrayList<String> student) {
        this.context = context;
        this.student = student;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_faculty,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvStudent.setText(student.get(position));
    }

    @Override
    public int getItemCount() {
        return student.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudent = itemView.findViewById(R.id.txt_view);
        }
    }
}

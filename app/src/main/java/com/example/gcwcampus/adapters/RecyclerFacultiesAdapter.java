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

public class RecyclerFacultiesAdapter extends RecyclerView.Adapter<RecyclerFacultiesAdapter.ViewHolder> {

    ArrayList<String> faculties;
    Context context;

    public RecyclerFacultiesAdapter(Context context, ArrayList<String> faculties) {
        this.context = context;
        this.faculties = faculties;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_faculty,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvFaculty.setText(faculties.get(position));
    }

    @Override
    public int getItemCount() {
        return faculties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFaculty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFaculty = itemView.findViewById(R.id.txt_view);
        }
    }
}

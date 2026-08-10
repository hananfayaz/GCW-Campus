package com.example.gcwcampus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gcwcampus.R;
import com.example.gcwcampus.models.RecyclerTimetableModel;

import java.util.ArrayList;

public class RecyclerFacultyTimetableAdapter extends RecyclerView.Adapter<RecyclerFacultyTimetableAdapter.ViewHolder> {

    Context context;
    ArrayList<RecyclerTimetableModel> timetableModel;

    public RecyclerFacultyTimetableAdapter(Context context, ArrayList<RecyclerTimetableModel> timetableModel) {
        this.context = context;
        this.timetableModel = timetableModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timetable_faculty_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSubject.setText(timetableModel.get(position).getSubject());
        holder.tvStartTime.setText(timetableModel.get(position).getStartTime());
        holder.tvEndTime.setText(timetableModel.get(position).getEndTime());
        holder.tvDepartment.setText(timetableModel.get(position).getDepartment());
        holder.tvPeriod.setText(timetableModel.get(position).getPeriod());
        holder.tvDay.setText(timetableModel.get(position).getDay());
    }

    @Override
    public int getItemCount() {
        return timetableModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvStartTime, tvEndTime, tvDepartment, tvPeriod, tvDay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.txt_subject);
            tvStartTime = itemView.findViewById(R.id.txt_start_time);
            tvEndTime = itemView.findViewById(R.id.txt_end_time);
            tvDepartment = itemView.findViewById(R.id.txt_dept);
            tvPeriod = itemView.findViewById(R.id.txt_period);
            tvDay = itemView.findViewById(R.id.txt_day_faculty);
        }
    }
}

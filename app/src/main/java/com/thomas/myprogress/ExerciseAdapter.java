package com.thomas.myprogress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder> {
    Context context;
    //ArrayList<ExerciseItem> exerciseItems;
    ArrayList<String> test;

    public ExerciseAdapter(Context context, ArrayList<String> test){
        this.context = context;
        this.test = test;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_exercise, parent, false);
        return new ExerciseAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameTV.setText(test.get(position));
        //holder.repsTV.setText(exerciseItems.get(position).getReps());
        //holder.bodyPartTV.setText(String.valueOf(exerciseItems.get(position).getBodyPart()));
        //holder.difficultyTV.setText(String.valueOf(exerciseItems.get(position).getDifficulty()));

    }

    @Override
    public int getItemCount() {
        return test.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV;
        //TextView repsTV, bodyPartTV, difficultyTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTextView);
            //repsTV = itemView.findViewById(R.id.repsTextView);
            //bodyPartTV = itemView.findViewById(R.id.bodyPartTextView);
            //difficultyTV = itemView.findViewById(R.id.difficultyTextView);

        }
    }
}

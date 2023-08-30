package com.thomas.myprogress.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.thomas.myprogress.DataBaseHelper;
import com.thomas.myprogress.R;
import com.thomas.myprogress.RVInterface;
import com.thomas.myprogress.models.Exercise;

import java.util.ArrayList;

public class ExerciseRVAdapter extends RecyclerView.Adapter<ExerciseRVAdapter.ExerciseViewHolder> {
    Context context;
    ArrayList<Exercise> exercises;
    RVInterface rvInterface;
    long workoutId;

    public ExerciseRVAdapter(Context context, ArrayList<Exercise> exercises, RVInterface rvInterface, long workoutId){
        this.context = context;
        this.exercises = exercises;
        this.rvInterface = rvInterface;
        this.workoutId = workoutId;

        // Initialize the selection status for each exercise item
        for (Exercise exercise : exercises) {
            exercise.setChecked(exercise.isChecked());
        }
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_item_exercise_layout2, parent, false);
        return new ExerciseViewHolder(view, rvInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        holder.name.setText(exercises.get(position).getName());
        holder.bodypart.setText(exercises.get(position).getBodypart());
        holder.difficulty.setText(exercises.get(position).getDifficulty());

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }


    public void setItems(ArrayList<Exercise> items) {
        this.exercises = items;
        notifyDataSetChanged();
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView name, bodypart, difficulty;
        MaterialCardView cardView;
        Button deleteExerciseButton, addExerciseButton;
        DataBaseHelper dbHelper;

        public ExerciseViewHolder(@NonNull View itemView, RVInterface rvInterface) {
            super(itemView);

            dbHelper = new DataBaseHelper(itemView.getContext());

            name = itemView.findViewById(R.id.nameTextView);
            bodypart = itemView.findViewById(R.id.bodyPartTextView);
            difficulty = itemView.findViewById(R.id.difficultyTextView);

            deleteExerciseButton = itemView.findViewById(R.id.deleteExerciseButton);
            addExerciseButton = itemView.findViewById(R.id.addExerciseButton);

            cardView = itemView.findViewById(R.id.exerciseCV);

            cardView.setOnClickListener(v -> {
                dbHelper.addExerciseDetails(workoutId, exercises.get(getAdapterPosition()).getId(), "","","");
                rvInterface.onItemClick(getAdapterPosition());
            });

            deleteExerciseButton.setOnClickListener(v -> {
                dbHelper.deleteExercise(exercises.get(getAdapterPosition()).getId());
                exercises.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());

            });

            addExerciseButton.setOnClickListener(v -> {
                rvInterface.onAddItemClick(getAdapterPosition());
            });


        }

    }

}
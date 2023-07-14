package com.thomas.myprogress.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.thomas.myprogress.DataBaseHelper;
import com.thomas.myprogress.ExerciseModel;
import com.thomas.myprogress.R;
import com.thomas.myprogress.RVInterface;
import com.thomas.myprogress.StartWorkoutPage;
import com.thomas.myprogress.models.Exercise;

import java.util.ArrayList;

public class ExerciseRVAdapter extends RecyclerView.Adapter<ExerciseRVAdapter.ExerciseViewHolder> {
    Context context;
    ArrayList<Exercise> exercises;
    RVInterface rvInterface;
    long workoutId;
    String workoutName;

    public ExerciseRVAdapter(Context context, ArrayList<Exercise> exercises, RVInterface rvInterface, long workoutId, String workoutName){
        this.context = context;
        this.exercises = exercises;
        this.rvInterface = rvInterface;
        this.workoutId = workoutId;
        this.workoutName = workoutName;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_item_exercise_layout, parent, false);
        return new ExerciseViewHolder(view, rvInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        // Set the data for each item
        // here add text from database
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

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView name, bodypart, difficulty;
        CardView cardView;
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

                rvInterface.onItemClick(getAdapterPosition());
            });

            deleteExerciseButton.setOnClickListener(v -> {
                dbHelper.deleteExercise(exercises.get(getAdapterPosition()).getId());
                exercises.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());

            });

            addExerciseButton.setOnClickListener(v -> {
                dbHelper.addExerciseDetails(workoutId, exercises.get(getAdapterPosition()).getId(), "","","");
                rvInterface.onAddItemClick(getAdapterPosition());

                /*dbHelper.addExerciseDetails(workoutId, exercises.get(getAdapterPosition()).getId(), "","","");
                Intent intent = new Intent(v.getContext(), StartWorkoutPage.class);
                intent.putExtra("ExerciseId", exercises.get(getAdapterPosition()).getId());
                intent.putExtra("WorkoutName", "workoutName");
                intent.putExtra("ExerciseName", exercises.get(getAdapterPosition()).getName());
                v.getContext().startActivity(intent);*/
            });

        }

    }

}




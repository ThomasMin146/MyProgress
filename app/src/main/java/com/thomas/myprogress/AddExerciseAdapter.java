package com.thomas.myprogress;

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

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import java.util.ArrayList;

public class AddExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {
    Context context;
    ArrayList<ExerciseModel> exerciseModels;
    RVInterface rvInterface;


    public AddExerciseAdapter(Context context, ArrayList<ExerciseModel> exerciseModels, RVInterface rvInterface){
        this.context = context;
        this.exerciseModels = exerciseModels;
        this.rvInterface = rvInterface;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_item_exercise_layout, parent, false);
        return new ExerciseViewHolder(view, rvInterface).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        // Set the data for each item
        // here add text from database
        holder.name.setText(exerciseModels.get(position).getExerciseName());
        holder.bodypart.setText(exerciseModels.get(position).getBodypart());
        holder.difficulty.setText(exerciseModels.get(position).getDifficulty());

    }

    @Override
    public int getItemCount() {
        return exerciseModels.size();
    }

    public void setItems(ArrayList<ExerciseModel> items) {
        this.exerciseModels = items;
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        exerciseModels.remove(position);
        notifyItemRemoved(position);

    }

}

class ExerciseViewHolder extends RecyclerView.ViewHolder{
    TextView name, bodypart, difficulty;
    CardView cardView;
    Button deleteExerciseButton, addExerciseButton;
    AddExerciseAdapter adapter;
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
            if(rvInterface != null){
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION){

                    rvInterface.onItemClick(position);
                }
            }
        });

        deleteExerciseButton.setOnClickListener(v -> {
            dbHelper.deleteExerciseByName(String.valueOf(name.getText()));
            adapter.deleteItem(getAdapterPosition());

        });

        addExerciseButton.setOnClickListener(v -> {
            dbHelper.createWorkout(String.valueOf(name.getText()));

            Intent intent = new Intent(v.getContext(), StartWorkoutPage.class);
            intent.putExtra("ExerciseName", getAdapterPosition());
            v.getContext().startActivity(intent);
        });


    }
    public ExerciseViewHolder linkAdapter(AddExerciseAdapter adapter){
        this.adapter = adapter;
        return this;
    }

}


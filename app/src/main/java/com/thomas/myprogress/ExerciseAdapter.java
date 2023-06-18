package com.thomas.myprogress;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder> {
    private  final RVInterface rvInterface;
    Context context;
    ArrayList<ItemExercise> itemExercises;

    public ExerciseAdapter(Context context, ArrayList<ItemExercise> itemExercises, RVInterface rvInterface){
        this.context = context;
        this.itemExercises = itemExercises;
        this.rvInterface = rvInterface;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_exercise, parent, false);
        return new ExerciseAdapter.MyViewHolder(itemView, rvInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameTV.setText(itemExercises.get(position).getName());
        holder.repsTV.setText(String.valueOf(itemExercises.get(position).getReps()));
        holder.bodyPartTV.setText(String.valueOf(itemExercises.get(position).getSets()));
        holder.difficultyTV.setText(String.valueOf(itemExercises.get(position).getWeight()));
    }
    @Override
    public int getItemCount() {
        return itemExercises.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV;
        TextView repsTV;
        TextView bodyPartTV;
        TextView difficultyTV;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView, RVInterface rvInterface) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);

            nameTV = itemView.findViewById(R.id.nameTextView);
            repsTV = itemView.findViewById(R.id.repsTextView);
            bodyPartTV = itemView.findViewById(R.id.setsTextView);
            difficultyTV = itemView.findViewById(R.id.weightTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rvInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            rvInterface.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}

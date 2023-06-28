package com.thomas.myprogress;

import android.content.Context;
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
        return new ExerciseAdapter.MyViewHolder(itemView, rvInterface, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameTV.setText(itemExercises.get(position).getName());
        holder.repsTV.setText(String.valueOf(itemExercises.get(position).getReps()));
        holder.setsTV.setText(String.valueOf(itemExercises.get(position).getSets()));
        holder.weightTV.setText(String.valueOf(itemExercises.get(position).getWeight()));
    }
    @Override
    public int getItemCount() {
        return itemExercises.size();
    }

    public void deleteItem(int position){
        itemExercises.remove(position);
        notifyItemRemoved(position);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV, repsTV, setsTV, weightTV;
        Button deleteButton;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView, RVInterface rvInterface, ExerciseAdapter adapter) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            nameTV = itemView.findViewById(R.id.nameTextView);
            repsTV = itemView.findViewById(R.id.repsTextView);
            setsTV = itemView.findViewById(R.id.setsTextView);
            weightTV = itemView.findViewById(R.id.weightTextView);

            cardView.setOnClickListener(v -> {
                if(rvInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){

                        rvInterface.onItemClick(position);
                    }
                }
            });

            deleteButton.setOnClickListener(v -> {
                DataBaseHelper dbHelper = new DataBaseHelper(v.getContext());
                dbHelper.deleteWorkoutsByExercise(String.valueOf(nameTV.getText()));
                adapter.deleteItem(getAdapterPosition());

            });

        }
    }
}

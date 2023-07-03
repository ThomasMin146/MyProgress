package com.thomas.myprogress.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.thomas.myprogress.models.ExerciseDetails;
import com.thomas.myprogress.DataBaseHelper;
import com.thomas.myprogress.R;
import com.thomas.myprogress.RVInterface;

import java.util.ArrayList;

public class WorkoutRVAdapter extends RecyclerView.Adapter<WorkoutRVAdapter.MyViewHolder> {
    RVInterface rvInterface;
    Context context;
    ArrayList<ExerciseDetails> exerciseDetails;
    DataBaseHelper dbHelper;

    public WorkoutRVAdapter(Context context, ArrayList<ExerciseDetails> exerciseDetails, RVInterface rvInterface){
        this.context = context;
        this.exerciseDetails = exerciseDetails;
        this.rvInterface = rvInterface;
        this.dbHelper = new DataBaseHelper(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_exercise, parent, false);
        return new WorkoutRVAdapter.MyViewHolder(itemView, rvInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameTV.setText(dbHelper.getExerciseName(exerciseDetails.get(position).getExerciseId()));
        holder.repsTV.setText(exerciseDetails.get(position).getReps());
        holder.setsTV.setText(exerciseDetails.get(position).getSets());
        holder.weightTV.setText(exerciseDetails.get(position).getWeight());

    }
    @Override
    public int getItemCount() {
        return exerciseDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV, repsTV, setsTV, weightTV;
        Button deleteButton;
        public CardView cardView;

        public MyViewHolder(@NonNull View itemView, RVInterface rvInterface) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            nameTV = itemView.findViewById(R.id.nameTextView);
            repsTV = itemView.findViewById(R.id.repsTextView);
            setsTV = itemView.findViewById(R.id.setsTextView);
            weightTV = itemView.findViewById(R.id.weightTextView);

            cardView.setOnClickListener(v -> {
                rvInterface.onItemClick(getAdapterPosition());

            });

            deleteButton.setOnClickListener(v -> {
                dbHelper.deleteExerciseDetails(exerciseDetails.get(getAdapterPosition()).getId());
                exerciseDetails.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });

        }
    }
}

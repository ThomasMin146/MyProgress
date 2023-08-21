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

public class ExerciseDetailsHistoryRVAdapter extends RecyclerView.Adapter<ExerciseDetailsHistoryRVAdapter.MyViewHolder> {
    Context context;
    ArrayList<ExerciseDetails> exerciseDetails;
    DataBaseHelper dbHelper;

    public ExerciseDetailsHistoryRVAdapter(Context context, ArrayList<ExerciseDetails> exerciseDetails){
        this.context = context;
        this.exerciseDetails = exerciseDetails;
        this.dbHelper = new DataBaseHelper(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_exercise_history, parent, false);
        return new ExerciseDetailsHistoryRVAdapter.MyViewHolder(itemView);
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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTextView);
            repsTV = itemView.findViewById(R.id.repsTextView);
            setsTV = itemView.findViewById(R.id.setsTextView);
            weightTV = itemView.findViewById(R.id.weightTextView);

        }
    }
}

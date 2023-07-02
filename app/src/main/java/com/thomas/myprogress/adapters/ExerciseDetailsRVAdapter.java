package com.thomas.myprogress.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.thomas.myprogress.models.ExerciseDetails;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.thomas.myprogress.R;
import java.util.ArrayList;

public class ExerciseDetailsRVAdapter extends RecyclerView.Adapter<ExerciseDetailsRVAdapter.ItemViewHolder> {
    Context context;
    ArrayList<ExerciseDetails> exerciseDetails;

    public ExerciseDetailsRVAdapter(Context context, ArrayList<ExerciseDetails> exerciseDetails){
        this.context = context;
        this.exerciseDetails = exerciseDetails;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.linear_row_layout, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Set the data for each item
        holder.sets.setText(String.valueOf(position+1)+".");

        //this.exerciseDetails.add(new ExerciseDetails());

    }

    @Override
    public int getItemCount() {
        return exerciseDetails.size();
    }

    public ArrayList<ExerciseDetails> getExerciseModels(){
        return this.exerciseDetails;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView sets;
        EditText reps, weight;
        CardView cardView;
        Button removeButton;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.linearCV);
            sets = itemView.findViewById(R.id.setsTextView);
            reps = itemView.findViewById(R.id.editTextReps);
            weight = itemView.findViewById(R.id.editTextWeight);
            removeButton = itemView.findViewById(R.id.removeRowButton);



            removeButton.setOnClickListener(v -> {
                exerciseDetails.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition()+1, getExerciseModels().size());
            });
        }

    }



}



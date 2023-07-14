package com.thomas.myprogress.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
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
import com.thomas.myprogress.models.ExerciseInfo;

import java.util.ArrayList;

public class ExerciseDetailsRVAdapter extends RecyclerView.Adapter<ExerciseDetailsRVAdapter.ItemViewHolder> {
    Context context;
    ArrayList<String> repList;
    ArrayList<String> weightList;


    public ExerciseDetailsRVAdapter(Context context, ArrayList<String> repList, ArrayList<String> weightList){
        this.context = context;
        this.repList = repList;
        this.weightList = weightList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_chosen_exercise, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Set the data for each item
        holder.sets.setText(String.valueOf(position+1)+".");
        holder.reps.setText(repList.get(position));

        holder.weight.setText(weightList.get(position));


    }

    @Override
    public int getItemCount() {
        return repList.size();
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


            reps.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //exerciseInfo.get(getAdapterPosition()).setReps(s.toString());
                    repList.set(getAdapterPosition(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            weight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    weightList.set(getAdapterPosition(), s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            removeButton.setOnClickListener(v -> {
                repList.remove(getAdapterPosition());
                weightList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                notifyItemRangeChanged(getAdapterPosition()+1, getItemCount());
            });
        }

    }



}



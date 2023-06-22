package com.thomas.myprogress;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    Context context;
    ArrayList<ExerciseModel> exerciseModels;

    public CustomAdapter(Context context, ArrayList<ExerciseModel> exerciseModels){
        this.context = context;
        this.exerciseModels = exerciseModels;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.linear_row_layout, parent, false);
        return new ItemViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Set the data for each item
        holder.sets.setText(String.valueOf(position+1)+".");
        holder.reps.getText();

    }

    @Override
    public int getItemCount() {
        return exerciseModels.size();
    }

    public ArrayList<ExerciseModel> getExerciseModels(){
        return this.exerciseModels;
    }

}

class ItemViewHolder extends RecyclerView.ViewHolder{
    TextView sets;
    EditText reps, weight;
    CardView cardView;
    Button removeButton;
    private CustomAdapter customAdapter;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.linearCV);
        sets = itemView.findViewById(R.id.setsTextView);
        reps = itemView.findViewById(R.id.editTextReps);
        weight = itemView.findViewById(R.id.editTextWeight);
        removeButton = itemView.findViewById(R.id.removeRowButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAdapter.exerciseModels.remove(getAdapterPosition());
                customAdapter.notifyItemRemoved(getAdapterPosition());
                customAdapter.notifyItemRangeChanged(getAdapterPosition()+1, customAdapter.getExerciseModels().size());
            }
        });

    }
    public ItemViewHolder linkAdapter(CustomAdapter adapter){
        this.customAdapter = adapter;
        return this;
    }
}

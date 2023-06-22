package com.thomas.myprogress;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

import com.thomas.myprogress.dbhelper.DataBaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AddExerciseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {
    Context context;
    ArrayList<ExerciseModel> exerciseModels;


    public AddExerciseAdapter(Context context, ArrayList<ExerciseModel> exerciseModels){
        this.context = context;
        this.exerciseModels = exerciseModels;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.add_item_exercise_layout, parent, false);
        return new ExerciseViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        // Set the data for each item
        // here add text from database
        holder.name.setText(exerciseModels.get(position).getExerciseName());
    }

    @Override
    public int getItemCount() {
        return exerciseModels.size();
    }

    public ArrayList<ExerciseModel> getExerciseModels(){
        return this.exerciseModels;
    }

}

class ExerciseViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    CardView cardView;
    AddExerciseAdapter adapter;

    public ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.nameTextView);

        cardView = itemView.findViewById(R.id.exerciseCV);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHelper dbHelper = new DataBaseHelper(v.getContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put("MyWorkout_name", "Workout A");
                values.put("Exercise_name", String.valueOf(name.getText()));

                db.insert("MyWorkout", null, values);

                db.close();

                Intent intent = new Intent(v.getContext(), StartWorkoutPage.class);
                intent.putExtra("ExerciseName", getAdapterPosition());
                v.getContext().startActivity(intent);
            }
        });

    }
    public ExerciseViewHolder linkAdapter(AddExerciseAdapter adapter){
        this.adapter = adapter;
        return this;
    }

}


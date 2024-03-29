package com.thomas.myprogress.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.thomas.myprogress.DataBaseHelper;
import com.thomas.myprogress.R;
import com.thomas.myprogress.RVInterface;
import com.thomas.myprogress.models.Workout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WorkoutHistoryRVAdapter extends RecyclerView.Adapter<WorkoutHistoryRVAdapter.MyViewHolder> {
    RVInterface rvInterface;
    Context context;
    ArrayList<Workout> workouts;
    DataBaseHelper dbHelper;

    public WorkoutHistoryRVAdapter(Context context, ArrayList<Workout> workouts, RVInterface rvInterface){
        this.context = context;
        this.workouts = workouts;
        this.rvInterface = rvInterface;
        this.dbHelper = new DataBaseHelper(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_workout_history, parent, false);
        return new WorkoutHistoryRVAdapter.MyViewHolder(itemView, rvInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.workoutTV.setText(workouts.get(position).getName());

        Date date = workouts.get(position).getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateString = dateFormat.format(date);

        holder.dateTV.setText(dateString);
        String formattedTime = formatTime(workouts.get(position).getWorkingTime());
        holder.timerTV.setText(formattedTime);

        String formattedTime2 = formatTime(workouts.get(position).getRestingTime());
        holder.restTimeTV.setText(formattedTime2);
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    private String formatTime(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) (milliseconds / (1000 * 60 * 60));

        return String.format(Locale.getDefault(), "%02d : %02d : %02d", hours, minutes, seconds);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView workoutTV, dateTV, timerTV, restTimeTV;
        CardView cardView;
        Button deleteButton;

        public MyViewHolder(@NonNull View itemView, RVInterface rvInterface) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            workoutTV = itemView.findViewById(R.id.workoutNameTextView);
            dateTV = itemView.findViewById(R.id.dateTextView);
            timerTV = itemView.findViewById(R.id.timerTextView);
            restTimeTV = itemView.findViewById(R.id.restTimeTextView);

            cardView.setOnClickListener(v -> {
                rvInterface.onItemClick(getAdapterPosition());

            });

            deleteButton.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Are you sure you want to delete this workout?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the item
                    dbHelper.deleteWorkout(workouts.get(getAdapterPosition()).getId());
                    dbHelper.deleteExerciseDetailsByWorkoutId(workouts.get(getAdapterPosition()).getId());
                    workouts.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                });
                builder.setNegativeButton("No", null);
                builder.show();


            });

        }
    }
}

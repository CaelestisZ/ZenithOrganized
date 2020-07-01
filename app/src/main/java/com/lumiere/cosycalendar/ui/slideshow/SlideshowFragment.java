package com.lumiere.cosycalendar.ui.slideshow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.lumiere.cosycalendar.MainActivity;
import com.lumiere.cosycalendar.R;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private final String PREFERENCE_FILE_KEY = "KEY123";
    private SlideshowViewModel slideshowViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        // tried loading using shared preferences, Can you try this part too, BRO?
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String json_content = sharedPreferences.getString("SavedJSON", " ");
        final TimetableView timetableView = root.findViewById(R.id.timetable);
        if(json_content!=" ") {
            timetableView.load(json_content);
        }

        final EditText subject_title = root.findViewById(R.id.subject_title);
        final EditText venue = root.findViewById(R.id.venue_title);
        final EditText start_time = root.findViewById(R.id.start_time);
        final EditText end_time = root.findViewById(R.id.end_time);
        final Spinner spinner = root.findViewById(R.id.spinner);
        final int[] day_number = {0};
        Button add_button = root.findViewById(R.id.add_schedule);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject_title_value = subject_title.getText().toString();
                String venue_value = venue.getText().toString();
                String start_time_hours = start_time.getText().toString().split(":")[0];
                String start_time_minutes = start_time.getText().toString().split(":")[1];
                String end_time_hours = end_time.getText().toString().split(":")[0];
                String end_time_minutes = end_time.getText().toString().split(":")[1];
                subject_title.setText("");
                venue.setText("");
                start_time.setText("");
                end_time.setText("");
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        day_number[0] = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        day_number[0] = 0;
                    }
                });
                ArrayList<Schedule> schedules = new ArrayList<Schedule>();
                final Schedule schedule = new Schedule();
                schedule.setClassTitle(subject_title_value); // sets subject
                schedule.setClassPlace(venue_value); // sets place
                schedule.setStartTime(new Time(Integer.parseInt(start_time_hours),Integer.parseInt(start_time_minutes))); // sets the beginning of class time (hour,minute)
                schedule.setEndTime(new Time(Integer.parseInt(end_time_hours),Integer.parseInt(end_time_minutes))); // sets the end of class time (hour,minute)
                schedule.setDay(day_number[0]);
                schedules.add(schedule);
                timetableView.add(schedules);
                String json = timetableView.createSaveData();
                Context context = getActivity();
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SavedJSON", json);
                editor.apply();
            }
        });

        final Button remove_button = root.findViewById(R.id.remove);
        timetableView.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void OnStickerSelected(final int idx, ArrayList<Schedule> schedules) {
                remove_button.setVisibility(View.VISIBLE);
                remove_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timetableView.remove(idx);
                        String json = timetableView.createSaveData();
                        Context context = getActivity();
                        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("SavedJSON", json);
                        editor.apply();
                        remove_button.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        Button remove_all_button = root.findViewById(R.id.remove_all);
        remove_all_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timetableView.removeAll();
                String json = timetableView.createSaveData();
                Context context = getActivity();
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SavedJSON", json);
                editor.apply();
            }
        });
        return root;
    }

}

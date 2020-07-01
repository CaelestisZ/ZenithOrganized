package com.lumiere.cosycalendar.ui.todo;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lumiere.cosycalendar.FileHelper;
import com.lumiere.cosycalendar.R;

import java.io.File;
import java.util.ArrayList;

public class TodoFragment extends Fragment {

    private TodoViewModel todoViewModel;
    private EditText editText;
    private ListView listView;
    private Button button;

    private ArrayList<String> items;
    private ArrayAdapter<String> stringArrayAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        todoViewModel =
                ViewModelProviders.of(this).get(TodoViewModel.class);
        final View root = inflater.inflate(R.layout.todo_fragment, container, false);

        editText = root.findViewById(R.id.todo_task);
        button = root.findViewById(R.id.todo_button);
        listView = root.findViewById(R.id.todo_list);

        items = FileHelper.readData(getContext());
        stringArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(stringArrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString()!="") {
                    String itemEntered = editText.getText().toString();
                    stringArrayAdapter.add(itemEntered);
                    editText.setText("");
                    FileHelper.writeData(items, getContext());
                    Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                stringArrayAdapter.notifyDataSetChanged();
                FileHelper.writeData(items, getContext());
                Toast.makeText(getContext(), "Deleted task", Toast.LENGTH_SHORT).show();
            }
        });

        return root;

    }

}

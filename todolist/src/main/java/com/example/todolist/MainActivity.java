package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    protected ArrayList<Record> list = new ArrayList<>();
    private Context context;
    private String storageFileName = "storage";
    public RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FileInputStream fis = context.openFileInput( storageFileName );
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<Record>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadPreferences();

        recordAdapter = new RecordAdapter(this);

        ListView listView = (ListView) findViewById(R.id.listv);
        listView.setAdapter(recordAdapter);

        final EditText editText = (EditText) findViewById(R.id.edit);
        final Button button = (Button) findViewById(R.id.addbutton);
        final Button settings = (Button) findViewById(R.id.setBt);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainActivity.this, PreferenceSetting.class);
                startActivity(settingsIntent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().length() == 0) {
                    Toast.makeText(context, "Please enter task!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.ENGLISH);

                list.add(new Record(editText.getText().toString(), sdf.format(cal.getTime())));
                recordAdapter.notifyDataSetChanged();
                editText.setText(null);

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                saveToStorage();
            }
        });
    }

    private void loadPreferences(){
        SharedPreferences preferenceSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.listv);
        String colourId = preferenceSettings.getString("ColourKey","1");
        switch (colourId) {
            case "1":
                listView.setBackgroundColor(Color.WHITE);
                break;
            case "2":
                listView.setBackgroundColor(Color.BLUE);
                break;
            case "3":
                listView.setBackgroundColor(Color.RED);
                break;
            case "4":
                listView.setBackgroundColor(Color.GREEN);
                break;
            case "5":
                listView.setBackgroundColor(Color.GRAY);
                break;
        }
        preferenceSettings.registerOnSharedPreferenceChangeListener(MainActivity.this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadPreferences();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newValue = data.getStringExtra("new_title");
                    String oldValue = data.getStringExtra("old_title");
                    for (Record r: list) {
                        if(r.getTaskName().equals(oldValue)){
                            r.setTaskName(newValue);
                        }
                    }
                    saveToStorage();
                    recordAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    };

    protected void saveToStorage(){
        try {
            ObjectOutputStream fileStorageStream = new ObjectOutputStream(openFileOutput(storageFileName, Context.MODE_PRIVATE));
            fileStorageStream.writeObject(list);
            fileStorageStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
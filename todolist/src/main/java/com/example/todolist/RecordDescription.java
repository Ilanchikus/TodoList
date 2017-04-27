package com.example.todolist;

/**
 * Created by Илана on 13.03.2017.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

public class RecordDescription extends Activity {
    private ToggleButton tbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_description);

        final Record r = (Record) getIntent().getSerializableExtra("Record");

        final TextView textview = (TextView) findViewById(R.id.txview);
        final EditText editTask = (EditText) findViewById(R.id.hidden_edit_view);
        tbtn = (ToggleButton) findViewById(R.id.btEdit);

        tbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbtn.isChecked()) {
                    ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
                    switcher.showNext();
                    editTask.requestFocus();
                    editTask.setSelection(editTask.getText().length());
                } else {
                    if (editTask.getText().toString().length() == 0) {
                        tbtn.setEnabled(true);
                        Toast.makeText(context, "Please enter new task name!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent data = new Intent();
                    String text = editTask.getText().toString();
                    data.putExtra("new_title", text);
                    data.putExtra("old_title", r.getTaskName());
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        textview.setText(r.getTaskName());
        editTask.setText(r.getTaskName());

        final EditText editTX = (EditText) findViewById(R.id.edtext);
        Button share = (Button) findViewById(R.id.btshare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalString = textview.getText().toString() + ":\n" + editTX.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, finalString);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
    }
}


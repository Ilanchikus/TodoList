package com.example.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;

/**
 * Created by Илана on 01.03.2017.
 */

public class RecordAdapter extends BaseAdapter {
    public Context context;
    public LayoutInflater lInflater;
    private ArrayList<Record> records;

    public RecordAdapter(Context context) {
        this.context = context;
        this.records = ((MainActivity) context).list;
        lInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RecordAdapter recordAdapter = this;
        final Record r = (Record) getItem(position);

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.line_records, parent, false);
        }

        TextView textView = (TextView) view.findViewById(R.id.tvTask);
        textView.setText(r.getTaskName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RecordDescription.class);
                myIntent.putExtra("Record", r);
                ((Activity)context).startActivityForResult(myIntent, 0);
            }
        });

        ((TextView) view.findViewById(R.id.tvDate)).setText(r.getCreatedDate());

        ImageView imageDelete = (ImageView) view.findViewById(R.id.ivImage);
        imageDelete.setImageResource(R.mipmap.delete);
        imageDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Delete record")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                records.remove(r);
                                recordAdapter.notifyDataSetChanged();
                                if(context instanceof MainActivity){
                                    ((MainActivity)context).saveToStorage();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                builder.create().show();
            }
        });

        final CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
        cbBuy.setTag(position);
        cbBuy.setChecked(r.getStatus());
        cbBuy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                r.setStatus(cbBuy.isChecked());
                if(context instanceof MainActivity){
                    ((MainActivity)context).saveToStorage();
                }
            }
        });
        return view;
    }
}
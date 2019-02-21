package com.sparsh2k19.geekhub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CodeListAdapter extends ArrayAdapter<Code> {
    private Activity activity;
    private ArrayList<Code> codeArrayList;

    CodeListAdapter(Activity activity, ArrayList<Code> codeArrayList) {
        super(activity, R.layout.code_list_item, codeArrayList);
        this.activity = activity;
        this.codeArrayList = codeArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.code_list_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.file_name);
        TextView lang = convertView.findViewById(R.id.lang);
        TextView source = convertView.findViewById(R.id.source);

        Code currentCode = codeArrayList.get(position);
        name.setText(currentCode.getFilename());
        lang.setText(currentCode.getLang());
        source.setText(currentCode.getSource());

        return convertView;
    }
}

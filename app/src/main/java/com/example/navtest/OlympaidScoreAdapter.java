package com.example.navtest;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.navtest.OlympaidPointTableHandle;
import com.example.navtest.R;

import java.util.List;

public class OlympaidScoreAdapter extends ArrayAdapter<OlympaidPointTableHandle> {
    private Context context;
    private List<OlympaidPointTableHandle> olympaidPointTableHandleList;

    public OlympaidScoreAdapter(Activity context,  List<OlympaidPointTableHandle> olympaidPointTableHandleList) {
        super(context, R.layout.sample_olympaid_score_board,  olympaidPointTableHandleList);
        this.context = context;
        this.olympaidPointTableHandleList = olympaidPointTableHandleList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.sample_olympaid_score_board, parent, false);
        TextView emailTextView = convertView.findViewById(R.id.emailTextViewId);
        TextView pointTextView = convertView.findViewById(R.id.pointTextViewid);

        emailTextView.setText(olympaidPointTableHandleList.get(position).getEmail());
        pointTextView.setText("Score: "+olympaidPointTableHandleList.get(position).getScore());

        return convertView;
    }
}

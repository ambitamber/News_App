package com.example.newsapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<News> {

    public CustomAdapter(@NonNull Activity context, ArrayList<News> news) {
        super(context, 0,news);
    }

    @NonNull
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }
        final News currentAdapter = getItem(position);
        TextView newsTitleTextView = listItemView.findViewById(R.id.newsTitle);
        assert currentAdapter != null;
        String newsTitle = currentAdapter.getNewsTitle();
        newsTitleTextView.setText(newsTitle);

        TextView newsSectionTextView = listItemView.findViewById(R.id.section_name);
        String section_name = currentAdapter.getNewsSection();
        newsSectionTextView.setText(section_name);

        TextView updatedDateTextView = listItemView.findViewById(R.id.updated_date);
        String updated_date = currentAdapter.getUpdatedDate();
        updatedDateTextView.setText("\u2022 "+ dateTime(updated_date));

        ImageView bookImageImageView = listItemView.findViewById(R.id.newsImage);
        Picasso.get().load(currentAdapter.getImageURL()).into(bookImageImageView);

        TextView descriptionTextView = listItemView.findViewById(R.id.descriptionText);
        String descriptionText = currentAdapter.getDescriptionText();
        descriptionTextView.setText(descriptionText);

        final String newsURL = currentAdapter.getNewsURL();

        return listItemView;
    }

    public String dateTime(String t){
        PrettyTime prettyTime = new PrettyTime(new Locale(getCountry()));
        String time = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", Locale.ENGLISH);
            Date date = simpleDateFormat.parse(t);
            time = prettyTime.format(date);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}

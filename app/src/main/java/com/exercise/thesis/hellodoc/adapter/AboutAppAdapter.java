package com.exercise.thesis.hellodoc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.exercise.thesis.hellodoc.R;

public class AboutAppAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public AboutAppAdapter(Context context) {
        this.context = context;
    }

    private int[] images = {
            R.drawable.consult,
            R.drawable.doctor,
            R.drawable.emergency1
    };

    private String[] headings = {
            "Health Consultation",
            "100+ Doctors",
            "Emergency Call"
    };

    private String[] headingDescription = {
            "Get health Consultation from doctors directly",
            "More than 100 doctors available for your treatment",
            "Do emergency call 24/7"
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);
        ImageView img = view.findViewById(R.id.about_app_image);
        TextView heading = view.findViewById(R.id.about_app_image_heading);
        TextView heading_text = view.findViewById(R.id.about_app_image_text);
        img.setImageResource(images[position]); // Set image based on the current page
        heading.setText(headings[position]); // // Set heading based on the current page
        heading_text.setText(headingDescription[position]); // Set heading description based on the current page
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object); // When we change the page to another, destroy or remove the view
    }
}

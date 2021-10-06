package com.exercise.thesis.hellodoc.ui.AboutApp;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.exercise.thesis.hellodoc.R;
import com.exercise.thesis.hellodoc.adapter.AboutAppAdapter;


public class AboutFragment extends Fragment {

    private ViewPager aboutAppViewPager;
    private LinearLayout dotsLayout;
    private AboutAppAdapter appAdapter;
    private TextView[] dots;
    private Button nextBtn;
    private Button prevBtn;
    private int currentPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aboutAppViewPager = view.findViewById(R.id.about_app_viewpager);
        dotsLayout = view.findViewById(R.id.about_app_dots);
        prevBtn = view.findViewById(R.id.about_app_prevBtn);
        nextBtn = view.findViewById(R.id.about_app_nextBtn);
        appAdapter = new AboutAppAdapter(getContext());
        aboutAppViewPager.setAdapter(appAdapter); // Setting our created adapter to the viewpager

        dotsCreator(0); // When the view is created we will be in the first page
        aboutAppViewPager.addOnPageChangeListener(viewListener); // Set 'viewListener' as the listener when we will move from one page to another
        // Get triggered when we press 'Next' button
        nextBtn.setOnClickListener(v -> {
            if (nextBtn.getText().toString().toLowerCase().equals("got it")) {
                Navigation.findNavController(view).navigate(R.id.action_aboutFragment_to_welcomeFragment);
//                openNewActivity(); // If it is the final page then if we click 'Got It' Button we will move to the 'MainActivity'
            } else {
                aboutAppViewPager.setCurrentItem(currentPage + 1); // Else set current page to the next page
            }
        });
        // Get triggered when we press 'Previous' button
        prevBtn.setOnClickListener(v -> {
            aboutAppViewPager.setCurrentItem(currentPage - 1); // Set current page to previous page
        });
    }

    private void dotsCreator(int position) {
        dots = new TextView[3]; // Initiate three textView for dots
        dotsLayout.removeAllViews(); // This removes all the previous views(It is important because we
        // are calling this function whenever page is changed from one to other. So we have to remove all
        // the views that we had set in previous function call & create a new view for the new page)
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;")); // This is for creating circle
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite)); // Setting color of the circle surface
            dotsLayout.addView(dots[i]); // Adding the dot to the view
        }
        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.orange)); // Changing the color of the dot to orange based on which page we are currently in.
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dotsCreator(position); // When page is changed to another call the dot creator function to create new view for the new page
            currentPage = position;
            if (position == 0) {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(false);
                prevBtn.setVisibility(View.INVISIBLE);
                nextBtn.setText("Next");
                prevBtn.setText("");
            } else if (position == dots.length - 1) {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("Got It");
                prevBtn.setText("Back");
            } else {
                nextBtn.setEnabled(true);
                prevBtn.setEnabled(true);
                prevBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("Next");
                prevBtn.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
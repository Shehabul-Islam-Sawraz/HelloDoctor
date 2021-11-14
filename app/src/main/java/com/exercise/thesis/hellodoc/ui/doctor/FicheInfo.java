package com.exercise.thesis.hellodoc.ui.doctor;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.exercise.thesis.hellodoc.R;

public class FicheInfo extends AppCompatActivity {
    TextView t1 ,t2, t3,t4,t5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_info);
        t1=findViewById(R.id.textView2);
        t2=findViewById(R.id.textView4);
        t3=findViewById(R.id.textView5);
        t4 = findViewById(R.id.textView7);
        t5 = findViewById(R.id.textView9);

        t1.setText("Date of Creation: "+getIntent().getStringExtra("dateCreated"));
        t2.setText("About Disease");
        if(!getIntent().getStringExtra("disease").equals("")){
            t3.setText("Disease: "+getIntent().getStringExtra("disease"));
        }
        if(!getIntent().getStringExtra("description").equals("")){
            t4.setText("Disease: "+getIntent().getStringExtra("description"));
        }
        if(!getIntent().getStringExtra("treatment").equals("")){
            t5.setText("Disease: "+getIntent().getStringExtra("treatment"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

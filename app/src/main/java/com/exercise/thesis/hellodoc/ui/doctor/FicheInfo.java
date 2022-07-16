package com.exercise.thesis.hellodoc.ui.doctor;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.exercise.thesis.hellodoc.R;

public class FicheInfo extends AppCompatActivity {
    TextView t1 ,t2, t3,t4,t5;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_info);
        t1=findViewById(R.id.textView2);
        t2=findViewById(R.id.textView4);
        t3=findViewById(R.id.textView5);
        t4 = findViewById(R.id.textView7);
        t5 = findViewById(R.id.textView9);
        img = findViewById(R.id.pres_img);

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
        if(getIntent().getStringExtra("prescription")!=null){
            img.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(getIntent().getStringExtra("prescription"));
            Glide.with(FicheInfo.this)
                    .load(uri)
                    .error(R.drawable.ef_image_placeholder)
                    .placeholder(R.drawable.ef_image_placeholder)
                    .into(img);
        }
        else{
            img.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

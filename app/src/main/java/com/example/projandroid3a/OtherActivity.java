package com.example.projandroid3a;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class OtherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity);
        if (getIntent().hasExtra("clicked_character")) {
            NierCharacter chara = getIntent().getParcelableExtra("clicked_character");
            displayImg(chara);
            displayBio(chara);
        }
    }

    public void displayImg(NierCharacter character){
        Picasso.get().load(character.getImgUrl()).resize(300,400).into((ImageView)findViewById(R.id.charaImg));
    }

    private void displayBio(NierCharacter character){
        String bio = getString(R.string.charaBio, character.getBio());
        TextView myTextView = (TextView)findViewById(R.id.bioView);
        myTextView.setText(bio);
    }
}

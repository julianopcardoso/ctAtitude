package br.com.ctatitude.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.com.ctatitude.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView exercios_cardview = findViewById(R.id.cardview_exercicios);
        exercios_cardview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.cardview_exercicios:
                intent = new Intent(this, ExerciciosListaActivity.class);
        }
        startActivity(intent);
    }
}
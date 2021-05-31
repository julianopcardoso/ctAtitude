package br.com.ctatitude.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.com.ctatitude.R;

/**
 * Classe MainActivitity - Tela Inicial
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * OnCreate MainActivity - Tela Inicial
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set views da tela
        CardView exercios_cardview = findViewById(R.id.cardview_exercicios);
        CardView treinos_cardview = findViewById(R.id.cardview_treinos);
        CardView timers_cardview = findViewById(R.id.cardview_timers);
        //Set listeners
        exercios_cardview.setOnClickListener(this);
        treinos_cardview.setOnClickListener(this);
        timers_cardview.setOnClickListener(this);
    }

    /**
     * Listener onClick views da tela inicial
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.cardview_exercicios: //Tela de Exercícios
                intent = new Intent(this, ExerciciosListaActivity.class);
                break;
            case R.id.cardview_treinos: //Tela de Treinos
                intent = new Intent(this, TreinosListaActivity.class);
                break;
            case R.id.cardview_timers: //Tela de Timers
                intent = new Intent(this, TimersListaActivity.class);
                break;
        }
        startActivity(intent);
    }
}
package br.com.ctatitude.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.adapter.ExercicioAdapter;
import br.com.ctatitude.dao.ExercicioDAO;
import br.com.ctatitude.model.Exercicio;

public class ExerciciosListaActivity extends AppCompatActivity {

    private ExercicioDAO dao;
    private List<Exercicio> exercicios;
    private ExercicioAdapter exercicioAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicios_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.exercicios);
        setSupportActionBar(toolbar);
        //set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        dao = new ExercicioDAO(this);
        exercicios = dao.listar();
        exercicioAdapter = new ExercicioAdapter(exercicios);
        RecyclerView rv = findViewById(R.id.recyclerview_listaexercicios);
        rv.setAdapter(exercicioAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem novo = menu.findItem(R.id.novo);
        novo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                intent = new Intent(ExerciciosListaActivity.this, ExerciciosCasdastroActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem pesquisar = menu.findItem(R.id.pesquisar);
        SearchView searchView = (SearchView) pesquisar.getActionView();

        searchView.setQueryHint(getString(R.string.pesquisar_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                exercicioAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
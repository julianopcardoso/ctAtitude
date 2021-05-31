package br.com.ctatitude.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.adapter.TimerAdapter;
import br.com.ctatitude.adapter.TreinoAdapter;
import br.com.ctatitude.dao.TreinoDAO;
import br.com.ctatitude.model.Treino;

/**
 * Classe TimersListaActivity
 */
public class TimersListaActivity extends AppCompatActivity {

    private TreinoDAO treinoDAO;
    private List<Treino> treinos;
    private TimerAdapter timerAdapter;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers_lista);
        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.timer);
        setSupportActionBar(toolbar);

        //Set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Set conexão banco de dados
        treinoDAO = new TreinoDAO(this);
        treinos = treinoDAO.listar();
        timerAdapter = new TimerAdapter(treinos);

        //Set recyclerview - lista de timers
        RecyclerView rv = findViewById(R.id.recyclerview_listatimers);
        rv.setAdapter(timerAdapter);
    }

    /**
     * onCreateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //Set menu: Novo
        MenuItem novo = menu.findItem(R.id.novo);
        novo.setVisible(false);

        //Set menu: Pesquisar
        MenuItem pesquisar = menu.findItem(R.id.pesquisar);
        SearchView searchView = (SearchView) pesquisar.getActionView();
        searchView.setQueryHint(getString(R.string.pesquisar_hint));
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                timerAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
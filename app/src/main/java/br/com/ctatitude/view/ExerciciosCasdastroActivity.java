package br.com.ctatitude.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.com.ctatitude.dao.ExercicioDAO;
import br.com.ctatitude.utils.EnumSimNao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import br.com.ctatitude.R;
import br.com.ctatitude.model.Exercicio;

public class ExerciciosCasdastroActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nome;
    private EditText descricao;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch repeticoes;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch peso;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch distancia;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch tempo;
    private ExercicioDAO dao;

    private Exercicio exercicioEditado = null;
    private boolean acaoAlteracao = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercicios_cadastro);

        nome = findViewById(R.id.editTextNomeExercicio);
        descricao = findViewById(R.id.editTextDescricao);
        repeticoes = findViewById(R.id.switchRepeticoes);
        peso = findViewById(R.id.switchPeso);
        distancia = findViewById(R.id.switchDistancia);
        tempo = findViewById(R.id.switchTempo);

        acaoAlteracao = false;

        Intent intent = getIntent();
        if(intent.hasExtra("exercicio")){
            acaoAlteracao = true;
            exercicioEditado = (Exercicio) intent.getSerializableExtra("exercicio");
            nome.setText(exercicioEditado.getNome());
            descricao.setText(exercicioEditado.getDescricao());

            if(exercicioEditado.getRepeticoes().equals(EnumSimNao.SIM.getValor())){
               repeticoes.setChecked(true);
            }else{
                repeticoes.setChecked(false);
            }

            if(exercicioEditado.getDistancia().equals(EnumSimNao.SIM.getValor())){
                distancia.setChecked(true);
            }else{
                distancia.setChecked(false);
            }

            if(exercicioEditado.getPeso().equals(EnumSimNao.SIM.getValor())){
                peso.setChecked(true);
            }else{
                peso.setChecked(false);
            }

            if(exercicioEditado.getTempo().equals(EnumSimNao.SIM.getValor())){
                tempo.setChecked(true);
            }else{
                tempo.setChecked(false);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.exercicios);
        setSupportActionBar(toolbar);
        //set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Button salvarExercicio = findViewById(R.id.buttonSalvar);
        salvarExercicio.setOnClickListener(this);
        dao = new ExercicioDAO(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //set invisible menu item
        MenuItem pesquisar = menu.findItem(R.id.pesquisar);
        MenuItem novo = menu.findItem(R.id.novo);
        pesquisar.setVisible(false);
        novo.setVisible(false);
        return true;
    }

    public void Salvar(View view){
        Exercicio exercicio = new Exercicio();
        exercicio.setNome(nome.getText().toString());
        exercicio.setDescricao(descricao.getText().toString());

        if(repeticoes.isChecked()){
            exercicio.setRepeticoes(EnumSimNao.SIM.getValor());
        }else{
            exercicio.setRepeticoes(EnumSimNao.NAO.getValor());
        }

        if(peso.isChecked()){
            exercicio.setPeso(EnumSimNao.SIM.getValor());
        }else{
            exercicio.setPeso(EnumSimNao.NAO.getValor());
        }

        if(distancia.isChecked()){
            exercicio.setDistancia(EnumSimNao.SIM.getValor());
        }else{
            exercicio.setDistancia(EnumSimNao.NAO.getValor());
        }

        if(tempo.isChecked()){
            exercicio.setTempo(EnumSimNao.SIM.getValor());
        }else{
            exercicio.setTempo(EnumSimNao.NAO.getValor());
        }

        boolean registroSalvo = false;;

        if(!acaoAlteracao) {
            registroSalvo = dao.inserir(exercicio);
        }else {
            exercicio.setId(exercicioEditado.getId());
            registroSalvo = dao.alterar(exercicio);
        }

        if(registroSalvo) {
            Toast.makeText(this, "Registro salvo!", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(this, "Erro ao salvar!", Toast.LENGTH_LONG).show();
        }

        if(acaoAlteracao) {
            Intent intent = null;
            intent = new Intent(this, ExerciciosListaActivity.class);
            startActivity(intent);
        }else{
            nome.setText("");
            descricao.setText("");
            repeticoes.setChecked(false);
            peso.setChecked(false);
            distancia.setChecked(false);
            tempo.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        //Executa botão salvar
        Salvar(v);
    }
}
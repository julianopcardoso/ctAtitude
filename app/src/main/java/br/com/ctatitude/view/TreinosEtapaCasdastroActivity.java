package br.com.ctatitude.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.adapter.ExercicioEtapaAdapter;
import br.com.ctatitude.model.Etapa;
import br.com.ctatitude.model.EtapaTreino;
import br.com.ctatitude.model.ExercicioEtapa;
import br.com.ctatitude.utils.EnumSimNao;
import br.com.ctatitude.utils.Utils;

/**
 * Classe TreinosEtapaCasdastroActivity
 */
public class TreinosEtapaCasdastroActivity extends AppCompatActivity implements View.OnClickListener {

    private EtapaTreino etapaTreino = null;
    private Etapa etapa = null;
    private boolean acaoAlteracao = false;
    private final int hash = 0;

    private List<ExercicioEtapa> novosExerciciosEtapa;

    private TextView textEtapa;
    private EditText duracao;
    private EditText descanso;
    private EditText rounds;

    private ExercicioEtapaAdapter exercicioEtapaAdapter;
    private RecyclerView rv;

    private static final int REQUEST_INSERT = 0;
    private static final int REQUEST_UPDATE = 1;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos_etapa_cadastro);

        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.etapa);
        setSupportActionBar(toolbar);

        //set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Set objeto
        etapaTreino = new EtapaTreino();
        etapa = new Etapa();
        etapaTreino.setEtapa(etapa);
        List<ExercicioEtapa> exerciciosEtapa = new ArrayList<>();
        etapaTreino.getEtapa().setExerciciosEtapa(exerciciosEtapa);
        novosExerciciosEtapa = new ArrayList<>();

        //Set botões
        Button adicionarEtapa = findViewById(R.id.buttonAdicionarEtapa);
        Button adicionarExercicio = findViewById(R.id.buttonAdicionarExercicio);
        ImageButton minusRound = findViewById(R.id.imageButtonMinus);
        ImageButton plusRound = findViewById(R.id.imageButtonPlus);

        //Set campos da tela
        textEtapa = findViewById(R.id.textEtapa);
        rounds = findViewById(R.id.editTextRoundsEtapa);
        descanso = findViewById(R.id.editTextDescansoEtapa);
        duracao = findViewById(R.id.editTextDuracaoEtapa);

        //Set listeners
        adicionarEtapa.setOnClickListener(this);
        adicionarExercicio.setOnClickListener(this);
        minusRound.setOnClickListener(this);
        plusRound.setOnClickListener(this);
        descanso.setOnClickListener(this);
        duracao.setOnClickListener(this);

        // Alteração da Etapa. Serealiza parâmetros da tela de Cadastro de Treinos
        Intent intent = getIntent();
        if (intent.hasExtra("etapatreinonovo")) {
            acaoAlteracao = false;
            textEtapa.setText(String.format("%02d", intent.getSerializableExtra("etapatreinonovo")));
        } else {
            acaoAlteracao = true;
            etapaTreino = (EtapaTreino) intent.getSerializableExtra("etapatreino");
            textEtapa.setText(String.format("%02d", etapaTreino.getOrdem()));
            duracao.setText(Utils.convertSecondsToMinutesSeconds(etapaTreino.getEtapa().getDuracao()));
            descanso.setText(Utils.convertSecondsToMinutesSeconds(etapaTreino.getEtapa().getDescanso()));
            rounds.setText(String.format("%02d", etapaTreino.getEtapa().getRound()));
        }
        exercicioEtapaAdapter = new ExercicioEtapaAdapter(etapaTreino.getEtapa().getExerciciosEtapa());
        //Set recyclerview - Lista de exercícios
        rv = findViewById(R.id.recyclerview_exercicios);
        rv.setAdapter(exercicioEtapaAdapter);
    }

    /**
     * onCreateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //Set visibilidade dos itens de menu
        MenuItem pesquisar = menu.findItem(R.id.pesquisar);
        MenuItem novo = menu.findItem(R.id.novo);
        pesquisar.setVisible(false);
        novo.setVisible(false);

        return true;
    }

    /**
     * Método atualizarTotalRounds
     * Atualiza o total de rounds
     * @param valor the valor
     */
    public void atualizarTotalRounds(Integer valor) {
        Integer valorRoundsAtual = Integer.valueOf(rounds.getText().toString());
        if ((valor + valorRoundsAtual) == 0) {
            Toast.makeText(this,
                    R.string.valor_min_rounds,
                    Toast.LENGTH_LONG).show();
            return;
        } else if ((valor + valorRoundsAtual) == 100) {
            Toast.makeText(this,
                    R.string.valor_maximo_rounds,
                    Toast.LENGTH_LONG).show();
            return;
        }
        rounds.setText(String.format("%02d", valor + valorRoundsAtual));

    }

    /**
     * onClick
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextDescansoEtapa:
                selecionaDuracao(descanso); //Seleciona a duração do descanso
                break;
            case R.id.editTextDuracaoEtapa:
                selecionaDuracao(duracao); //Seleciona a duração da etapa
                break;
            case R.id.imageButtonMinus:
                atualizarTotalRounds(-1); //Atualiza contador de rounds -1
                break;
            case R.id.imageButtonPlus:
                atualizarTotalRounds(1); //Atualiza contador de rounds +1
                break;
            case R.id.buttonAdicionarExercicio:
                adicionarNovoExercicio(); //Adiciona novo exercício
                break;
            case R.id.buttonAdicionarEtapa:
                adicionarEtapa(); //Adiciona etapa
                break;
        }
    }

    /**
     * Método adicionarNovoExercicio
     */
    private void adicionarNovoExercicio() {
        Integer novoExercicio = etapaTreino.getEtapa().getExerciciosEtapa().size() + 1;
        Intent intent = null;
        intent = new Intent(TreinosEtapaCasdastroActivity.this, TreinosEtapaExercicioCasdastroActivity.class);
        intent.putExtra("exercicioetapanovo", novoExercicio);
        startActivityForResult(intent, REQUEST_INSERT);
    }

    /**
     * onActivityResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Verifica o tipo de requisição
        if (requestCode == REQUEST_INSERT) {
            if (resultCode == RESULT_OK) { //Resultado da requisição

                ExercicioEtapa returnExercicio = (ExercicioEtapa) data.getSerializableExtra("exercicioetapa");
                etapaTreino.getEtapa().getExerciciosEtapa().add(returnExercicio);
                novosExerciciosEtapa.add(returnExercicio);
                exercicioEtapaAdapter.notifyItemRangeInserted(etapaTreino.getEtapa().getExerciciosEtapa().size(), etapaTreino.getEtapa().getExerciciosEtapa().size());
            }
        } else if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_OK) { //Resultado da requisição

                ExercicioEtapa returnExercicio = (ExercicioEtapa) data.getSerializableExtra("exercicioetapa");
                Integer index = 0;
                for (ExercicioEtapa exercicioEtapa : etapaTreino.getEtapa().getExerciciosEtapa()) {
                    if (exercicioEtapa.getOrdem().equals(returnExercicio.getOrdem())) {
                        index = etapaTreino.getEtapa().getExerciciosEtapa().indexOf(exercicioEtapa);
                    }
                }

                etapaTreino.getEtapa().getExerciciosEtapa().get(index).setExercicio(returnExercicio.getExercicio());

                if (returnExercicio.getExercicio().getRepeticoes().equals(EnumSimNao.SIM.getValor())) {
                    if(returnExercicio.getRepeticao() != null) {
                        if (!returnExercicio.getRepeticao().toString().trim().equals("")) {
                            etapaTreino.getEtapa().getExerciciosEtapa().get(index).setRepeticao(returnExercicio.getRepeticao());
                        }
                    }
                }

                if (returnExercicio.getExercicio().getDistancia().equals(EnumSimNao.SIM.getValor())) {
                    if(returnExercicio.getDistancia() != null) {
                        if (!returnExercicio.getDistancia().toString().trim().equals("")) {
                            etapaTreino.getEtapa().getExerciciosEtapa().get(index).setDistancia(returnExercicio.getDistancia());
                        }
                    }
                }
                if (returnExercicio.getExercicio().getPeso().equals(EnumSimNao.SIM.getValor())) {
                    if(returnExercicio.getPesoFem() != null) {
                        if (!returnExercicio.getPesoFem().toString().trim().equals("")) {
                            etapaTreino.getEtapa().getExerciciosEtapa().get(index).setPesoFem(returnExercicio.getPesoFem());
                        }
                    }
                }
                if (returnExercicio.getExercicio().getPeso().equals(EnumSimNao.SIM.getValor())) {
                    if(returnExercicio.getPesoMasc() != null) {
                        if (!returnExercicio.getPesoMasc().toString().trim().equals("")) {
                            etapaTreino.getEtapa().getExerciciosEtapa().get(index).setPesoMasc(returnExercicio.getPesoMasc());
                        }
                    }
                }
                if (returnExercicio.getExercicio().getTempo().equals(EnumSimNao.SIM.getValor())) {
                    if(returnExercicio.getTempo() != null) {
                        if (!returnExercicio.getTempo().toString().trim().equals("")) {
                            etapaTreino.getEtapa().getExerciciosEtapa().get(index).setTempo(returnExercicio.getTempo());
                        }
                    }
                }

                exercicioEtapaAdapter.notifyItemChanged(index);
            }
        }
    }

    /**
     * Método adicionarEtapa
     */
    private void adicionarEtapa() {

        //Valida se foi adicionado no mínimo 1 exercício
        if(etapaTreino.getEtapa().getExerciciosEtapa().size() == 0){
            Toast.makeText(this, R.string.minimo_exercicio, Toast.LENGTH_LONG).show();
            return;
        }

        //Valida se foi informada a duração da etapa
        if(Utils.convertMinutesSecondsToSeconds(duracao.getText().toString()) == 0){
            Toast.makeText(this, R.string.duracao_minima, Toast.LENGTH_LONG).show();
            return;
        }

        etapaTreino.setOrdem(Integer.valueOf(textEtapa.getText().toString()));
        etapaTreino.getEtapa().setRound(Integer.valueOf(rounds.getText().toString()));
        etapaTreino.getEtapa().setDescanso(Utils.convertMinutesSecondsToSeconds(descanso.getText().toString()));
        etapaTreino.getEtapa().setDuracao(Utils.convertMinutesSecondsToSeconds(duracao.getText().toString()));

        Intent intent = new Intent();
        intent.putExtra("etapatreino", etapaTreino);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Método selecionaDuracao
     * @param timer
     */
    private void selecionaDuracao(EditText timer) {
        final Dialog d = new Dialog(this);
        d.setContentView(R.layout.timerdialog);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);
        d.getWindow().setLayout(width, height);
        Button buttonSelecionar = d.findViewById(R.id.buttonSelecionar);
        Button buttonCancelar = d.findViewById(R.id.buttonCancelar);
        final NumberPicker npm = d.findViewById(R.id.numberPickerMinute);
        final NumberPicker nps = d.findViewById(R.id.numberPickerSeconds);

        npm.setMaxValue(59); // max value 59
        npm.setMinValue(00);   // min value 0
        npm.setWrapSelectorWheel(true);
        nps.setMaxValue(59); // max value 59
        nps.setMinValue(00);   // min value 0

        String[] timerSplit = timer.getText().toString().split(":");

        Integer min = Integer.valueOf(timerSplit[0]);
        Integer sec = Integer.valueOf(timerSplit[1]);
        npm.setValue(min);
        nps.setValue(sec);

        nps.setWrapSelectorWheel(true);

        if (timer.getId() == R.id.editTextDuracaoEtapa) {
            buttonSelecionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    duracao.setText(String.format("%02d", npm.getValue()) + ":" + String.format("%02d", nps.getValue()));
                    d.dismiss();
                }
            });
        }
        if (timer.getId() == R.id.editTextDescansoEtapa) {
            buttonSelecionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    descanso.setText(String.format("%02d", npm.getValue()) + ":" + String.format("%02d", nps.getValue()));
                    d.dismiss();
                }
            });
        }

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }
}
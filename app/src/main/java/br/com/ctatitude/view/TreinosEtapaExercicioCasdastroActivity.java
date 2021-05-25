package br.com.ctatitude.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.dao.ExercicioDAO;
import br.com.ctatitude.model.Exercicio;
import br.com.ctatitude.model.ExercicioEtapa;
import br.com.ctatitude.utils.EnumSimNao;
import br.com.ctatitude.utils.Utils;

/**
 * Classe TreinosEtapaExercicioCasdastroActivity
 */
public class TreinosEtapaExercicioCasdastroActivity extends AppCompatActivity implements View.OnClickListener {

    private ExercicioEtapa exercicioEtapa = null;
    private boolean acaoAlteracao = false;

    private AutoCompleteTextView exercicio;
    private EditText repeticao;
    private EditText pesoMasc;
    private EditText pesoFem;
    private EditText distancia;
    private EditText tempo;

    private ExercicioDAO exercicioDAO;
    private List<Exercicio> exercicios;

    private Button adicionarExercicio;

    /**
     * onCreateOptionsMenu
     * @param savedInstanceState
     */
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treino_etapa_exercicio_cadastro);

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.exercicio);
        setSupportActionBar(toolbar);

        //Set up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //Set objetos
        exercicioEtapa = new ExercicioEtapa();

        //Set campos da tela
        repeticao = findViewById(R.id.editTextQtdeRepeticoes);
        pesoMasc = findViewById(R.id.editTextPesoMasc);
        pesoFem = findViewById(R.id.editTextPesoFem);
        distancia = findViewById(R.id.editTextDistancia);
        tempo = findViewById(R.id.editTextTempoExercicio);
        adicionarExercicio = findViewById(R.id.buttonAdicionarExercicio);
        exercicio = findViewById(R.id.editTextNomeExercicioEtapa);

        //Set listeners
        tempo.setOnClickListener(this);
        adicionarExercicio.setOnClickListener(this);

        //Set visibilidade dos movimentos conforme exercício selecionado
        setVisibilityMovimentos();

        //Set conexão bando de dados
        exercicioDAO = new ExercicioDAO(this);
        exercicios = exercicioDAO.listar();

        //Set autocomplete Exercício
        ArrayAdapter<Exercicio> adapter = new ArrayAdapter<Exercicio>(
                this, android.R.layout.simple_list_item_1, exercicios);
        exercicio.setAdapter(adapter);
        exercicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                habilitaCamposExercicioSelecionado(parent, position);
            }
        });
        exercicio.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                return validaExercicioAutoComplete(text);
            }
            @Override
            public CharSequence fixText(CharSequence invalidText) {
                return null;
            }
        });
        exercicio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        // Alteração do Exercício da Etapa. Serealiza parâmetros da tela de Cadastro de Etapas
        Intent intent = getIntent();
        acaoAlteracao = false;
        if (intent.hasExtra("exercicioetapa")) {
            acaoAlteracao = true;
            exercicioEtapa = (ExercicioEtapa) intent.getSerializableExtra("exercicioetapa");
            exercicio.setText(String.valueOf(exercicioEtapa.getExercicio().getNome()));

            if (exercicioEtapa.getExercicio().getRepeticoes().equals(EnumSimNao.SIM.getValor())) {
                repeticao.setEnabled(true);
                findViewById(R.id.textQtdeRepeticoes).setEnabled(true);
                if(exercicioEtapa.getRepeticao() != null && exercicioEtapa.getRepeticao() > 0) {
                    repeticao.setText(String.valueOf(exercicioEtapa.getRepeticao()));
                }
            }
            if (exercicioEtapa.getExercicio().getPeso().equals(EnumSimNao.SIM.getValor())) {
                findViewById(R.id.textPeso).setEnabled(true);
                pesoMasc.setEnabled(true);
                pesoFem.setEnabled(true);
                findViewById(R.id.textPesoMasc).setEnabled(true);
                findViewById(R.id.textPesoFem).setEnabled(true);

                if(exercicioEtapa.getPesoMasc() != null && exercicioEtapa.getPesoFem() != null &&
                        exercicioEtapa.getPesoMasc() > 0 && exercicioEtapa.getPesoFem() > 0) {
                    pesoMasc.setText(String.valueOf(exercicioEtapa.getPesoMasc()));
                    pesoFem.setText(String.valueOf(exercicioEtapa.getPesoFem()));
                }
            }
            if (exercicioEtapa.getExercicio().getDistancia().equals(EnumSimNao.SIM.getValor())) {
                distancia.setEnabled(true);
                findViewById(R.id.textDistancia).setEnabled(true);
                if(exercicioEtapa.getDistancia() != null && exercicioEtapa.getDistancia() > 0) {
                    distancia.setText(String.valueOf(exercicioEtapa.getDistancia()));
                }
            }
            if (exercicioEtapa.getExercicio().getTempo().equals(EnumSimNao.SIM.getValor())) {
                tempo.setEnabled(true);
                findViewById(R.id.textTempoExercicio).setEnabled(true);
                if(exercicioEtapa.getTempo() != null) {
                    tempo.setText(Utils.convertSecondsToMinutesSeconds(exercicioEtapa.getTempo()));
                }
            }
            adicionarExercicio.setEnabled(true);

        }else{
            exercicioEtapa.setOrdem((Integer)intent.getSerializableExtra("exercicioetapanovo"));
        }
    }

    /**
     * Método validaExercicioAutoComplete
     * @param text
     * @return
     */
    private boolean validaExercicioAutoComplete(CharSequence text) {
        if (!text.toString().trim().equals("")) {
            ListAdapter listAdapter = exercicio.getAdapter();
            for (int i = 0; listAdapter.getCount() > i; i++) {
                Exercicio exe = (Exercicio) listAdapter.getItem(i);
                if (text.toString().toLowerCase().equals(exe.getNome().toLowerCase())) {
                    return true;
                }
            }
        }
        //Limpar campos
        repeticao.setText("");
        pesoMasc.setText("");
        pesoFem.setText("");
        distancia.setText("");
        tempo.setText(R.string.duracao_inicial);
        Toast.makeText(TreinosEtapaExercicioCasdastroActivity.this,
                getResources().getString(R.string.exercicio_nao_cadastrado).replace("{exercicio}", text),
                Toast.LENGTH_LONG).show();
        setVisibilityMovimentos();
        return false;
    }

    /**
     * Método habilitaCamposExercicioSelecionado
     * @param parent
     * @param position
     */
    private void habilitaCamposExercicioSelecionado(AdapterView<?> parent, int position) {
        Object obj = parent.getItemAtPosition(position);
        if (obj instanceof Exercicio) {
            Exercicio exercicio = (Exercicio) obj;
            exercicioEtapa.setExercicio(exercicio);
            if (exercicioEtapa.getExercicio().getRepeticoes().equals(EnumSimNao.SIM.getValor())) {
                repeticao.setEnabled(true);
                findViewById(R.id.textQtdeRepeticoes).setEnabled(true);
            }
            if (exercicioEtapa.getExercicio().getPeso().equals(EnumSimNao.SIM.getValor())) {
                findViewById(R.id.textPeso).setEnabled(true);
                pesoMasc.setEnabled(true);
                pesoFem.setEnabled(true);
                findViewById(R.id.textPesoMasc).setEnabled(true);
                findViewById(R.id.textPesoFem).setEnabled(true);
            }
            if (exercicioEtapa.getExercicio().getDistancia().equals(EnumSimNao.SIM.getValor())) {
                distancia.setEnabled(true);
                findViewById(R.id.textDistancia).setEnabled(true);
            }
            if (exercicioEtapa.getExercicio().getTempo().equals(EnumSimNao.SIM.getValor())) {
                tempo.setEnabled(true);
                findViewById(R.id.textTempoExercicio).setEnabled(true);
            }

            adicionarExercicio.setEnabled(true);
        }
    }

    /**
     * Método setVisibilityMovimentos
     */
    private void setVisibilityMovimentos() {
        repeticao.setEnabled(false);
        pesoMasc.setEnabled(false);
        pesoFem.setEnabled(false);
        distancia.setEnabled(false);
        tempo.setEnabled(false);
        adicionarExercicio.setEnabled(false);
        findViewById(R.id.textQtdeRepeticoes).setEnabled(false);
        findViewById(R.id.textPeso).setEnabled(false);
        findViewById(R.id.textPesoMasc).setEnabled(false);
        findViewById(R.id.textPesoFem).setEnabled(false);
        findViewById(R.id.textDistancia).setEnabled(false);
        findViewById(R.id.textTempoExercicio).setEnabled(false);
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
     * onClick
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextTempoExercicio:
                selecionaDuracao(tempo); //Seleciona tempo duração
                break;
            case R.id.buttonAdicionarExercicio:
                adicionarExercicio(); //Adiciona exercício
                break;
        }
    }

    /**
     * Método adicionarExercicio
     */
    private void adicionarExercicio() {
        ExercicioEtapa exeEtapa = new ExercicioEtapa();
        exeEtapa.setOrdem(exercicioEtapa.getOrdem());
        exeEtapa.setExercicio(exercicioEtapa.getExercicio());

        if (exercicioEtapa.getExercicio().getRepeticoes().equals(EnumSimNao.SIM.getValor())) {
            if (!String.valueOf(repeticao.getText()).trim().equals("") ) {
                exeEtapa.setRepeticao(Integer.valueOf(String.valueOf(repeticao.getText())));
            }
        }

        if (exercicioEtapa.getExercicio().getPeso().equals(EnumSimNao.SIM.getValor())) {
            if (!String.valueOf(pesoMasc.getText()).trim().equals("") &&  String.valueOf(pesoFem.getText()).trim().equals("")) {
                Toast.makeText(TreinosEtapaExercicioCasdastroActivity.this,
                        R.string.peso_fem_nao_informado,
                        Toast.LENGTH_LONG).show();
                return;
            }else{
                if(String.valueOf(pesoMasc.getText()).trim().equals("") && !String.valueOf(pesoFem.getText()).trim().equals("")){
                    Toast.makeText(TreinosEtapaExercicioCasdastroActivity.this,
                            R.string.peso_masc_nao_informado,
                            Toast.LENGTH_LONG).show();
                    return;
                }else{
                    if(!String.valueOf(pesoMasc.getText()).trim().equals("") && !String.valueOf(pesoFem.getText()).trim().equals("")) {
                        exeEtapa.setPesoMasc(Integer.valueOf(String.valueOf(pesoMasc.getText())));
                        exeEtapa.setPesoFem(Integer.valueOf(String.valueOf(pesoFem.getText())));
                    }
                }
            }
        }

        if (exercicioEtapa.getExercicio().getDistancia().equals(EnumSimNao.SIM.getValor())) {
            if (!String.valueOf(distancia.getText()).trim().equals("") ) {
                exeEtapa.setDistancia(Integer.valueOf(String.valueOf(distancia.getText())));
            }
        }

        if (exercicioEtapa.getExercicio().getTempo().equals(EnumSimNao.SIM.getValor())) {
            if (!String.valueOf(tempo.getText()).trim().equals("00:00") ) {
                exeEtapa.setTempo(Utils.convertMinutesSecondsToSeconds(tempo.getText().toString()));
            }else{
                exeEtapa.setTempo(0);
            }
        }

        Intent intent = new Intent();
        intent.putExtra("exercicioetapa", exeEtapa);
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

        buttonSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempo.setText(String.format("%02d", npm.getValue()) + ":" + String.format("%02d", nps.getValue()));
                d.dismiss();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }
}
package br.com.ctatitude.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.model.ExercicioEtapa;
import br.com.ctatitude.utils.EnumSimNao;
import br.com.ctatitude.utils.Utils;
import br.com.ctatitude.view.TreinosEtapaExercicioCasdastroActivity;

/**
 * Classe ExercicioTimerAdapter
 */
public class ExercicioTimerAdapter extends RecyclerView.Adapter<ExercicioTimerAdapter.ExercicioTimerViewHolder> {
    private final List<ExercicioEtapa> exercicioEtapas;

    /**
     * Construtor
     * @param exercicioEtapas
     */
    public ExercicioTimerAdapter(List<ExercicioEtapa> exercicioEtapas) {
        this.exercicioEtapas = exercicioEtapas;
    }

    /**
     * Função ExercicioTimerViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ExercicioTimerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_exercicio_timer, parent, false);

        return new ExercicioTimerViewHolder(view);
    }

    /**
     * Método onBindViewHolder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ExercicioTimerViewHolder holder, int position) {
        ExercicioEtapa exercicioEtapa = exercicioEtapas.get(position);
        holder.bind(exercicioEtapa);

    }

    /**
     * Função getItemCount
     * @return int
     */
    @Override
    public int getItemCount() {
        return exercicioEtapas.size();
    }

    /**
     * Classe {@link ExercicioTimerViewHolder}
     */
    class ExercicioTimerViewHolder extends RecyclerView.ViewHolder {

        TextView nomeExercicioLista;
        TextView descricaoExercicio;

        /**
         * Construtor
         * @param itemView
         */
        public ExercicioTimerViewHolder(@NonNull View itemView) {
            super(itemView);

            //Set views
            nomeExercicioLista = itemView.findViewById(R.id.textNomeExercicioLista);
            descricaoExercicio = itemView.findViewById(R.id.textDescricaoExercicio);
        }

        /**
         * Método bind
         * @param exercicioEtapa
         */
        public void bind(ExercicioEtapa exercicioEtapa) {
            nomeExercicioLista.setText(String.valueOf(exercicioEtapa.getExercicio().getNome()));
            descricaoExercicio.setText(formataDescricaoExercicio(exercicioEtapa));
        }

        /**
         * Função formataDescricaoExercicio
         * Formata detalhamento do exercicio na lista do Timer
         * @param exercicioEtapa
         * @return string
         */
        private String formataDescricaoExercicio(ExercicioEtapa exercicioEtapa) {
            StringBuilder descricaoExercicio = new StringBuilder();

            if (exercicioEtapa.getExercicio().getRepeticoes().equals(EnumSimNao.SIM.getValor())) {

                if(exercicioEtapa.getRepeticao() != null) {
                    if (!exercicioEtapa.getRepeticao().toString().trim().equals("0")) {
                        descricaoExercicio.append(exercicioEtapa.getRepeticao().toString() + " repetições");
                    }
                }
            }

            if (exercicioEtapa.getExercicio().getPeso().equals(EnumSimNao.SIM.getValor())) {
                if(exercicioEtapa.getPesoMasc() != null && exercicioEtapa.getPesoFem() != null) {
                    if (!exercicioEtapa.getPesoMasc().toString().trim().equals("0") && !exercicioEtapa.getPesoFem().toString().trim().equals("0")) {
                        if (!descricaoExercicio.toString().trim().equals("")) {
                            descricaoExercicio.append(" - ");
                        }
                        descricaoExercicio.append(exercicioEtapa.getPesoMasc().toString() + "/" + exercicioEtapa.getPesoFem().toString() + "kg");
                    }
                }
            }

            if (exercicioEtapa.getExercicio().getDistancia().equals(EnumSimNao.SIM.getValor())) {
                if(exercicioEtapa.getDistancia() != null) {
                    if (!exercicioEtapa.getDistancia().toString().trim().equals("0")) {
                        if (!descricaoExercicio.toString().trim().equals("")) {
                            descricaoExercicio.append(" - ");
                        }
                        descricaoExercicio.append(exercicioEtapa.getDistancia().toString() + "m");
                    }
                }
            }

            if (exercicioEtapa.getExercicio().getTempo().equals(EnumSimNao.SIM.getValor())) {
                if(exercicioEtapa.getTempo() != null) {
                    if (!exercicioEtapa.getTempo().toString().trim().equals("0")) {
                        if (!descricaoExercicio.toString().trim().equals("")) {
                            descricaoExercicio.append(" - ");
                        }
                        descricaoExercicio.append(Utils.convertSecondsToHoursMinutesSeconds(exercicioEtapa.getTempo()));
                    }
                }
            }

            return descricaoExercicio.toString();
        }
    }
}

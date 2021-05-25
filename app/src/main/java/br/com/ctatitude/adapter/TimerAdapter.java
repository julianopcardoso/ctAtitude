package br.com.ctatitude.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.ctatitude.R;
import br.com.ctatitude.dao.EtapaDAO;
import br.com.ctatitude.dao.EtapaTreinoDAO;
import br.com.ctatitude.dao.ExercicioEtapaDAO;
import br.com.ctatitude.dao.TreinoDAO;
import br.com.ctatitude.model.Treino;
import br.com.ctatitude.utils.Utils;
import br.com.ctatitude.view.ExerciciosCasdastroActivity;
import br.com.ctatitude.view.ExerciciosListaActivity;
import br.com.ctatitude.view.TimerActivity;
import br.com.ctatitude.view.TimersListaActivity;
import br.com.ctatitude.view.TreinosCasdastroActivity;
import br.com.ctatitude.view.TreinosListaActivity;

/**
 * Classe TimerAdapter
 */
public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.TreinoViewHolder> implements Filterable {
    private final List<Treino> treinos;
    private List<Treino> treinosListaTotal;

    /**
     * Construtor
     * @param treinos
     */
    public TimerAdapter(List<Treino> treinos) {
        this.treinos = treinos;
        this.treinosListaTotal = new ArrayList<>(treinos);
    }

    /**
     * Função onCreateViewHolder
     * @param parent
     * @param viewType
     * @return TreinoViewHolder
     */
    @NonNull
    @Override
    public TreinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_timer, parent, false);

        return new TreinoViewHolder(view);
    }

    /**
     * Método onBindViewHolder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull TreinoViewHolder holder, int position) {
        Treino treino = treinos.get(position);
        holder.bind(treino);

        clickBotaoPlay(holder, treino);
        /*clickBotaoExcluir(holder, treino);*/
    }

    /**
     * Método do click do botão play
     * selecionado
     * @param holder
     * @param treino
     */
    private void clickBotaoPlay(TreinoViewHolder holder, Treino treino) {
        holder.btnPlay.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                acionarPlay(v, treino);
            }
        });
    }

    /**
     * Método acionarPlay
     * Chama tela do timer e aciona contadores
     * @param v
     * @param treino
     */
    private void acionarPlay(View v, Treino treino) {
        final View view = v;
        //Exibe mensagem de confirmação
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.confirmacao)
                .setMessage(R.string.confirmacao_inicio_timer)
                .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity activity = getActivity(v);
                        Intent intent = null;
                        intent = new Intent(activity,
                                TimerActivity.class);
                        intent.putExtra("treinotimer", treino);
                        activity.startActivity(intent);

                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .create()
                .show();
    }

    /**
     * Função getItemCount
     * @return int
     */
    @Override
    public int getItemCount() {
        return treinos.size();
    }

    /**
     * Funão getActivity
     * Busca a activity corrente do contexto
     * @param view
     * @return Activity
     */
    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * Função getFilter
     * @return Filter
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * Filtra timers na lista
     */
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Treino> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(treinosListaTotal);
            } else {
                for (Treino treino : treinosListaTotal) {
                    if (treino.getNome().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(treino);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            treinos.clear();
            treinos.addAll((Collection<? extends Treino>) results.values);
            notifyDataSetChanged();
        }
    };

    /**
     * Classe {@link TreinoViewHolder}
     */
    class TreinoViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView duracao;
        ImageButton btnPlay;

        /**
         * Construtor
         * @param itemView
         */
        public TreinoViewHolder(@NonNull View itemView) {
            super(itemView);

            //Set views
            nome = itemView.findViewById(R.id.textNomeTreinoTimerLista);
            duracao = itemView.findViewById(R.id.textTimerTreinoTimerLista);
            btnPlay = itemView.findViewById(R.id.playTreinoTimerItem);
        }

        /**
         * Método bind
         * @param treino
         */
        public void bind(Treino treino) {
            nome.setText(treino.getNome());
            duracao.setText(Utils.convertSecondsToHoursMinutesSeconds((treino.getDuracao())));
        }
    }
}

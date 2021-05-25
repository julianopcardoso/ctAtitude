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
import br.com.ctatitude.view.TreinosCasdastroActivity;

/**
 * Classe TreinoAdapter
 * Adaptador para lista de Treinos
 */
public class TreinoAdapter extends RecyclerView.Adapter<TreinoAdapter.TreinoViewHolder> implements Filterable {
    private final List<Treino> treinos;
    private List<Treino> treinosListaTotal;

    /**
     * Construtor
     * @param treinos
     */
    public TreinoAdapter(List<Treino> treinos) {
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
                R.layout.item_treino, parent, false);

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
        //Set listeners para botão Alterar e Excluir
        clickBotaoAlterar(holder, treino);
        clickBotaoExcluir(holder, treino);
    }


    /**
     * Método para click do Botão excluir
     * @param holder
     * @param treino
     */
    private void clickBotaoExcluir(TreinoViewHolder holder, Treino treino) {
        holder.btnExcluir.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluirTreino(v, treino);
            }
        });
    }

    /**
     * Método para excluir treino da base e da lista
     * Realiza exclusão quando clicar o botão excluir
     * Antes realiza confirmação
     * @param v
     * @param treino
     */
    private void excluirTreino(View v, Treino treino) {
        final View view = v;
        //Exibe mensagem de confirmação
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.confirmacao)
                .setMessage(R.string.confirmacao_exclusao)
                .setPositiveButton(R.string.excluir, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TreinoDAO treinoDAO = new TreinoDAO(view.getContext());
                        ExercicioEtapaDAO exercicioEtapaDAO = new ExercicioEtapaDAO(view.getContext());
                        EtapaTreinoDAO etapaTreinoDAO = new EtapaTreinoDAO(view.getContext());
                        EtapaDAO etapaDAO = new EtapaDAO(view.getContext());

                        treino.setEtapasTreinos(etapaTreinoDAO.listar(treino));

                        for (int i = 0; i < treino.getEtapasTreinos().size(); i++) {
                            exercicioEtapaDAO.excluir(treino.getEtapasTreinos().get(i).getEtapa());
                            etapaTreinoDAO.excluir(treino.getEtapasTreinos().get(i).getEtapa());
                            etapaDAO.excluir(treino.getEtapasTreinos().get(i).getEtapa());
                        }

                        boolean sucesso = treinoDAO.excluir(treino);
                        if (sucesso) {
                            removerTreino(treino);
                            Toast.makeText(getActivity(v), R.string.registro_excluido, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(v), R.string.erro_exclusao_registro, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .create()
                .show();
    }

    /**
     * Método do click do botão alterar
     * selecionado
     * @param holder
     * @param treino
     */
    private void clickBotaoAlterar(TreinoViewHolder holder, Treino treino) {
        holder.btnEditar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarTreino(v, treino);
            }
        });
    }

    /**
     * Método alterarTreino
     * Chama tela de cadastro para editar o treino
     * @param v
     * @param treino
     */
    private void alterarTreino(View v, Treino treino) {
        Activity activity = getActivity(v);
        Intent intent = null;
        intent = new Intent(activity,
                TreinosCasdastroActivity.class);
        intent.putExtra("treino", treino);
        activity.startActivity(intent);
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
     * Método removerTreino
     * Remove o treino da recyclerview e do objeto
     * @param treino
     */
    public void removerTreino(Treino treino) {
        int position = treinos.indexOf(treino);
        treinos.remove(position);
        treinosListaTotal.clear();
        treinosListaTotal = new ArrayList<>(treinos);
        notifyItemRemoved(position);
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
     * @return
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * Filtra treino na lista
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
        ImageButton btnEditar;
        ImageButton btnExcluir;

        /**
         * Construtor
         * @param itemView
         */
        public TreinoViewHolder(@NonNull View itemView) {
            super(itemView);
            //Set views
            nome = itemView.findViewById(R.id.textNomeTreinoLista);
            duracao = itemView.findViewById(R.id.textTimerTreinoLista);
            btnEditar = itemView.findViewById(R.id.editTreinoItem);
            btnExcluir = itemView.findViewById(R.id.deleteTreinoItem);
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

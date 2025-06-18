package com.example.napbabby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoricoSonoAdapter extends RecyclerView.Adapter<HistoricoSonoAdapter.ViewHolder> {

    private List<HistoricoSonoActivity.RegistroSono> registrosSono;

    public HistoricoSonoAdapter(List<HistoricoSonoActivity.RegistroSono> registrosSono) {
        this.registrosSono = registrosSono;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historico_sono, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoricoSonoActivity.RegistroSono registro = registrosSono.get(position);

        holder.tvData.setText(registro.getData());
        holder.tvHorario.setText(registro.getHoraInicio() + " - " + registro.getHoraFim());
        holder.tvDuracao.setText(registro.getDuracao());
    }

    @Override
    public int getItemCount() {
        return registrosSono.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvData, tvHorario, tvDuracao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData = itemView.findViewById(R.id.tvData);
            tvHorario = itemView.findViewById(R.id.tvHorario);
            tvDuracao = itemView.findViewById(R.id.tvDuracao);
        }
    }
}


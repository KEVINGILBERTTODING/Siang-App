package com.example.siangapp.PenyeliaAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.R;
import com.example.siangapp.model.HistoryProjectModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;
import com.example.siangapp.util.PesertaInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenyeliaHistoryProjectAdapter extends RecyclerView.Adapter<PenyeliaHistoryProjectAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<HistoryProjectModel> historyProjectModel;
    PendaftarInterface pendaftarInterface;
    SharedPreferences sharedPreferences;
    PesertaInterface pesertaInterface;

    public PenyeliaHistoryProjectAdapter(@NonNull Context context, List<HistoryProjectModel> historyProjectModel) {
        this.context = context;
        this.historyProjectModel = historyProjectModel;
    }

    public PenyeliaHistoryProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_history_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyeliaHistoryProjectAdapter.ViewHolder holder, int position) {

        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);

        holder.tvTanggal.setText(historyProjectModel.get(holder.getAdapterPosition()).getDateCreated());

        holder.tvKegiatan.setText(historyProjectModel.get(holder.getAdapterPosition()).getIsiKegiatab());



    }

    @Override
    public int getItemCount() {
        return historyProjectModel.size();
    }


    public void filter (ArrayList<HistoryProjectModel> filteredList) {
        historyProjectModel = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTanggal, tvKegiatan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKegiatan = itemView.findViewById(R.id.tvKegiatan);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {



            Dialog dialogKegiatan = new Dialog(context);
            dialogKegiatan.setContentView(R.layout.layout_history_project);
            dialogKegiatan.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button btnSimpan, btnBatal, btnDelete;
            EditText etKegiatan, etProgress, etNote;
            btnSimpan = dialogKegiatan.findViewById(R.id.btnSimpan);
            btnBatal = dialogKegiatan.findViewById(R.id.btnBatal);
            btnDelete = dialogKegiatan.findViewById(R.id.btnDelete);
            etProgress =dialogKegiatan.findViewById(R.id.etProgress);
            etNote = dialogKegiatan.findViewById(R.id.etNote);

            etKegiatan = dialogKegiatan.findViewById(R.id.etKegiatan);
            etKegiatan.setText(historyProjectModel.get(getAdapterPosition()).getIsiKegiatab());
            etNote.setText(historyProjectModel.get(getAdapterPosition()).getNote());
            etKegiatan.setEnabled(false);
            etNote.setEnabled(false);
            btnSimpan.setVisibility(View.GONE);
            dialogKegiatan.show();
            btnBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogKegiatan.dismiss();
                }
            });





        }
    }
}

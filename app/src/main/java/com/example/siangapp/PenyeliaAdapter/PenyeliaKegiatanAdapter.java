package com.example.siangapp.PenyeliaAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.R;
import com.example.siangapp.model.KegiatanModel;
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

public class PenyeliaKegiatanAdapter extends RecyclerView.Adapter<PenyeliaKegiatanAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<KegiatanModel> kegiatanModelList;

    public PenyeliaKegiatanAdapter(@NonNull Context context, List<KegiatanModel> kegiatanModelList) {
        this.context = context;
        this.kegiatanModelList = kegiatanModelList;
    }

    public PenyeliaKegiatanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_kegiatan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyeliaKegiatanAdapter.ViewHolder holder, int position) {
        holder.tvTanggal.setText(kegiatanModelList.get(holder.getAdapterPosition()).getTanggal());
        holder.tvKegiatan.setText(kegiatanModelList.get(holder.getAdapterPosition()).getIsiKegiatan());



    }

    @Override
    public int getItemCount() {
        return kegiatanModelList.size();
    }

    public void filter (ArrayList<KegiatanModel> filteredList) {
        kegiatanModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTanggal,tvKegiatan;
        CardView cvKegiatan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKegiatan = itemView.findViewById(R.id.tvKegiatan);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            cvKegiatan = itemView.findViewById(R.id.cvKegiatan);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            Dialog dialogKegiatan = new Dialog(context);
            dialogKegiatan.setContentView(R.layout.layout_kegiatan);
            dialogKegiatan.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button btnSimpan, btnBatal, btnDelete;
            EditText etKegiatan;
            btnSimpan = dialogKegiatan.findViewById(R.id.btnSimpan);
            btnBatal = dialogKegiatan.findViewById(R.id.btnBatal);
            btnDelete = dialogKegiatan.findViewById(R.id.btnDelete);

            etKegiatan = dialogKegiatan.findViewById(R.id.etKegiatan);
            etKegiatan.setText(kegiatanModelList.get(getAdapterPosition()).getIsiKegiatan());
            etKegiatan.setEnabled(false);
            dialogKegiatan.show();

            btnDelete.setVisibility(View.GONE);
            btnSimpan.setVisibility(View.GONE);



            btnBatal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogKegiatan.dismiss();
                }
            });


            dialogKegiatan.show();




        }
    }
}

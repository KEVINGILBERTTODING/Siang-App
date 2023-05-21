package com.example.siangapp.PenyeliaAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.PenyeliaFragment.PenyeliaDetailMahasiswaFragment;
import com.example.siangapp.PenyeliaFragment.PenyeliaKegiatanFragment;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;

import java.util.ArrayList;
import java.util.List;

public class PenyeliaMahasiswaAdapter extends RecyclerView.Adapter<PenyeliaMahasiswaAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<PendaftarModel> pendaftarModelList;

    public PenyeliaMahasiswaAdapter(@NonNull Context context, List<PendaftarModel> pendaftarModelList) {
        this.context = context;
        this.pendaftarModelList = pendaftarModelList;
    }

    public PenyeliaMahasiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_mahasiswa_penyelia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyeliaMahasiswaAdapter.ViewHolder holder, int position) {
        holder.tvNama.setText(pendaftarModelList.get(holder.getAdapterPosition()).getName());
        holder.tvNim.setText(pendaftarModelList.get(holder.getAdapterPosition()).getNim());
        holder.tvSekolah.setText(pendaftarModelList.get(holder.getAdapterPosition()).getSchool());


        if (pendaftarModelList.get(holder.getAdapterPosition()).getAcc().equals("lolos")) {
            holder.viewColor.setBackgroundColor(context.getColor(R.color.green));
            holder.tvStatus.setText("Aktif");
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.main));
        } else  if (pendaftarModelList.get(holder.getAdapterPosition()).getAcc().equals("belum")) {
            holder.tvStatus.setText("Tidak aktif");
            holder.viewColor.setBackgroundColor(context.getColor(R.color.yellow));
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.red));


        }  else if (pendaftarModelList.get(holder.getAdapterPosition()).getAcc().equals("tidak_aktif")) {
            holder.viewColor.setBackgroundColor(context.getColor(R.color.light_gray));
            holder.tvStatus.setText("Tidak aktif");
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.red));


        } else if (pendaftarModelList.get(holder.getAdapterPosition()).getAcc().equals("ditolak")) {
            holder.viewColor.setBackgroundColor(context.getColor(R.color.red));
            holder.tvStatus.setText("Tidak aktif");
            holder.cvStatus.setCardBackgroundColor(context.getColor(R.color.red));


        }



    }


    @Override
    public int getItemCount() {
        return pendaftarModelList.size();
    }

    public void filter(ArrayList<PendaftarModel> filteredList) {
        pendaftarModelList = filteredList;
        notifyDataSetChanged();;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNama, tvNim, tvSekolah, tvStatus;
        View viewColor;
        CardView cvMahasiswa, cvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNim = itemView.findViewById(R.id.tvNim);
            tvSekolah = itemView.findViewById(R.id.tvSekolah);
            viewColor = itemView.findViewById(R.id.viewColor);
            cvMahasiswa = itemView.findViewById(R.id.cvMahasiswa);
            cvStatus = itemView.findViewById(R.id.cvStatus);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        @Override
        public void onClick(View v) {
            Fragment fragment = new PenyeliaKegiatanFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id", pendaftarModelList.get(getAdapterPosition()).getUserId());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.framePenyelia, fragment).addToBackStack(null).commit();





        }
    }
}

package com.example.siangapp.AdminAdapter;

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

import com.example.siangapp.AdminFragment.AdminDetailMahasiswaVerifFragment;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;

import java.util.ArrayList;
import java.util.List;

public class AdminVerifikasiMahasiswaAdapter extends RecyclerView.Adapter<AdminVerifikasiMahasiswaAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<PendaftarModel> pendaftarModelList;

    public AdminVerifikasiMahasiswaAdapter(@NonNull Context context, List<PendaftarModel> pendaftarModelList) {
        this.context = context;
        this.pendaftarModelList = pendaftarModelList;
    }

    public AdminVerifikasiMahasiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_verifikasi_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminVerifikasiMahasiswaAdapter.ViewHolder holder, int position) {
        holder.tvNama.setText(pendaftarModelList.get(holder.getAdapterPosition()).getName());
        holder.tvNim.setText(pendaftarModelList.get(holder.getAdapterPosition()).getNim());
        holder.tvSekolah.setText(pendaftarModelList.get(holder.getAdapterPosition()).getSchool());


        if (pendaftarModelList.get(holder.getAdapterPosition()).getAcc().equals("lolos")) {
            holder.viewColor.setBackgroundColor(context.getColor(R.color.green));
        } else  if (pendaftarModelList.get(holder.getAdapterPosition()).getAcc().equals("belum")) {
            holder.viewColor.setBackgroundColor(context.getColor(R.color.yellow));
        }  else if (pendaftarModelList.get(holder.getAdapterPosition()).getAcc().equals("tidak_aktif")) {
            holder.viewColor.setBackgroundColor(context.getColor(R.color.light_gray));
        } else if (pendaftarModelList.get(holder.getAdapterPosition()).getAcc().equals("ditolak")) {
            holder.viewColor.setBackgroundColor(context.getColor(R.color.red));
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
        TextView tvNama, tvNim, tvSekolah;
        View viewColor;
        CardView cvMahasiswa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNim = itemView.findViewById(R.id.tvNim);
            tvSekolah = itemView.findViewById(R.id.tvSekolah);
            viewColor = itemView.findViewById(R.id.viewColor);
            cvMahasiswa = itemView.findViewById(R.id.cvMahasiswa);
        }

        @Override
        public void onClick(View v) {

            Fragment fragment = new AdminDetailMahasiswaVerifFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user_id", pendaftarModelList.get(getAdapterPosition()).getUserId());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameAdmin, fragment).addToBackStack(null)
                    .commit();


        }
    }
}

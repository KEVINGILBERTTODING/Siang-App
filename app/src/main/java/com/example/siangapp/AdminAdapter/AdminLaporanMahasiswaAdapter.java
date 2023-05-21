package com.example.siangapp.AdminAdapter;

import android.app.Dialog;
import android.content.Context;
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
import com.example.siangapp.model.PendaftarModel;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AdminLaporanMahasiswaAdapter extends RecyclerView.Adapter<AdminLaporanMahasiswaAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<PendaftarModel> pendaftarModelList;

    public AdminLaporanMahasiswaAdapter(@NonNull Context context, List<PendaftarModel> pendaftarModelList) {
        this.context = context;
        this.pendaftarModelList = pendaftarModelList;
    }

    public AdminLaporanMahasiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_laporan_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminLaporanMahasiswaAdapter.ViewHolder holder, int position) {
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
        notifyDataSetChanged();
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
           if (pendaftarModelList.get(getAdapterPosition()).getRating() == 0) {
               Toasty.normal(context, "Belum ada nilai yang tersedia", Toasty.LENGTH_SHORT).show();
           }else {
               Dialog dialogPenilaian = new Dialog(context);
               dialogPenilaian.setContentView(R.layout.layout_admin_nilai);
               dialogPenilaian.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
               Button btnTutup = dialogPenilaian.findViewById(R.id.btnTutup);
               EditText etInovasi, etKerjaSama, etDisiplin, etRataRata;
               etInovasi = dialogPenilaian.findViewById(R.id.etInovasi);
               etKerjaSama = dialogPenilaian.findViewById(R.id.etKerjaSama);
               etDisiplin = dialogPenilaian.findViewById(R.id.etDisiplin);
               etRataRata = dialogPenilaian.findViewById(R.id.etRataRata);
               etDisiplin.setEnabled(false);
               etInovasi.setEnabled(false);
               etKerjaSama.setEnabled(false);
               etRataRata.setEnabled(false);
               dialogPenilaian.show();

               etInovasi.setText(pendaftarModelList.get(getAdapterPosition()).getInovasi());
               etKerjaSama.setText(pendaftarModelList.get(getAdapterPosition()).getKerjaSama());
               etDisiplin.setText(pendaftarModelList.get(getAdapterPosition()).getDisiplin());
               etRataRata.setText(pendaftarModelList.get(getAdapterPosition()).getRata());

               btnTutup.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialogPenilaian.dismiss();

                   }
               });
           }






        }
    }
}

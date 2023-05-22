package com.example.siangapp.PenyeliaAdapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.PenyeliaFragment.PenyeliaKegiatanFragment;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PenyeliaInterface;

import java.util.ArrayList;
import java.util.List;

public class PenyeliaHasilPenilaianAdapter extends RecyclerView.Adapter<PenyeliaHasilPenilaianAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<PendaftarModel> pendaftarModelList;
    PenyeliaInterface penyeliaInterface;

    public PenyeliaHasilPenilaianAdapter(@NonNull Context context, List<PendaftarModel> pendaftarModelList) {
        this.context = context;
        this.pendaftarModelList = pendaftarModelList;
    }

    public PenyeliaHasilPenilaianAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_hasil_penilaian, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyeliaHasilPenilaianAdapter.ViewHolder holder, int position) {
        holder.tvNama.setText(pendaftarModelList.get(holder.getAdapterPosition()).getName());
        holder.tvSekolah.setText(pendaftarModelList.get(holder.getAdapterPosition()).getSchool());






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
        Button btnPilih;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvSekolah = itemView.findViewById(R.id.tvSekolah);
            viewColor = itemView.findViewById(R.id.viewColor);
            cvMahasiswa = itemView.findViewById(R.id.cvMahasiswa);
            btnPilih = itemView.findViewById(R.id.btnPilih);
            btnPilih.setText("Lihat Nilai");
            penyeliaInterface= DataApi.getClient().create(PenyeliaInterface.class);


            btnPilih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialogNilai = new Dialog(context);
                    dialogNilai.setContentView(R.layout.layout_admin_nilai);
                    dialogNilai.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Button btnSimpan, btnBatal;
                    EditText etInovasi, etKerjaSama, etDisiplin, etRata;
                    btnSimpan = dialogNilai.findViewById(R.id.btnSimpan);
                    btnBatal = dialogNilai.findViewById(R.id.btnTutup);
                    etInovasi = dialogNilai.findViewById(R.id.etInovasi);
                    etKerjaSama = dialogNilai.findViewById(R.id.etKerjaSama);
                    etDisiplin = dialogNilai.findViewById(R.id.etDisiplin);
                    etRata = dialogNilai.findViewById(R.id.etRataRata);

                    etInovasi.setEnabled(false);
                    etRata.setEnabled(false);
                    etDisiplin.setEnabled(false);
                    etKerjaSama.setEnabled(false);

                    etInovasi.setText(pendaftarModelList.get(getAdapterPosition()).getInovasi());
                    etKerjaSama.setText(pendaftarModelList.get(getAdapterPosition()).getKerjaSama());
                    etDisiplin.setText(pendaftarModelList.get(getAdapterPosition()).getDisiplin());
                    etRata.setText(pendaftarModelList.get(getAdapterPosition()).getRata());
                    dialogNilai.show();

                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogNilai.dismiss();
                        }
                    });
                }
            });
        }

        @Override
        public void onClick(View v) {




        }
    }
}

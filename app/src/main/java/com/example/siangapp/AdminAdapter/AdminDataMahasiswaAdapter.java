package com.example.siangapp.AdminAdapter;

import static androidx.core.app.ActivityCompat.requestPermissions;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.AdminFragment.AdminDetailMahasiswaVerifFragment;
import com.example.siangapp.FileDownload;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.util.DataApi;

import java.util.ArrayList;
import java.util.List;

public class AdminDataMahasiswaAdapter extends RecyclerView.Adapter<AdminDataMahasiswaAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<PendaftarModel> pendaftarModelList;

    public AdminDataMahasiswaAdapter(@NonNull Context context, List<PendaftarModel> pendaftarModelList) {
        this.context = context;
        this.pendaftarModelList = pendaftarModelList;
    }

    public AdminDataMahasiswaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_mahasiswa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminDataMahasiswaAdapter.ViewHolder holder, int position) {
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

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = DataApi.URL_DOWNLOAD_SURAT_KETERANGAN+pendaftarModelList.get(holder.getAdapterPosition()).getUserId();
                String title = pendaftarModelList.get(holder.getAdapterPosition()).getCertificate() +".pdf";
                String description = "Downloading PDF file";
                String fileName = pendaftarModelList.get(holder.getAdapterPosition()).getCertificate()  + ".pdf";


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    } else {

                        FileDownload fileDownload = new FileDownload(context);
                        fileDownload.downloadFile(url, title, description, fileName);

                    }
                } else {

                    FileDownload fileDownload = new FileDownload(context);
                    fileDownload.downloadFile(url, title, description, fileName);
                }
            }
        });

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
        Button btnDownload;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNim = itemView.findViewById(R.id.tvNim);
            tvSekolah = itemView.findViewById(R.id.tvSekolah);
            viewColor = itemView.findViewById(R.id.viewColor);
            cvMahasiswa = itemView.findViewById(R.id.cvMahasiswa);
            btnDownload = itemView.findViewById(R.id.btnDownload);
        }

        @Override
        public void onClick(View v) {





        }
    }
}

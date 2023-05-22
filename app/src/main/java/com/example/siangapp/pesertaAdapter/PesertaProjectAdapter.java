package com.example.siangapp.pesertaAdapter;

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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.FragmentPesertaMagang.PesertaHistoryProjectFragment;
import com.example.siangapp.R;
import com.example.siangapp.model.KegiatanModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ProjectModel;
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

public class PesertaProjectAdapter extends RecyclerView.Adapter<PesertaProjectAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<ProjectModel> projectModelList;
    PendaftarInterface pendaftarInterface;
    SharedPreferences sharedPreferences;
    PesertaInterface pesertaInterface;

    public PesertaProjectAdapter(@NonNull Context context, List<ProjectModel> projectModelList) {
        this.context = context;
        this.projectModelList = projectModelList;
    }

    public PesertaProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PesertaProjectAdapter.ViewHolder holder, int position) {
        holder.tvTanggalAwal.setText(projectModelList.get(holder.getAdapterPosition()).getTglMulai());
        holder.tvTanggalSelesai.setText(projectModelList.get(holder.getAdapterPosition()).getTglSelesai());
        holder.tvKegiatan.setText(projectModelList.get(holder.getAdapterPosition()).getJudulTugas());
        holder.tvProgress.setText(projectModelList.get(holder.getAdapterPosition()).getProgress() + "%");
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);



    }

    @Override
    public int getItemCount() {
        return projectModelList.size();
    }

    public void filter (ArrayList<ProjectModel> filteredList) {
        projectModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTanggalAwal, tvTanggalSelesai, tvKegiatan, tvProgress;
        CardView cvProject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTanggalAwal = itemView.findViewById(R.id.tvTanggalAwal);
            tvTanggalSelesai = itemView.findViewById(R.id.tvTanggalSelesai);
            tvKegiatan = itemView.findViewById(R.id.tvKegiatan);
            tvProgress = itemView.findViewById(R.id.tvProgress);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framePesertaMagang, new PesertaHistoryProjectFragment())
                    .addToBackStack(null).commit();

        }
    }
}

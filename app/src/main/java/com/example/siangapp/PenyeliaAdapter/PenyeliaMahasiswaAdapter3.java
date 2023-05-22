package com.example.siangapp.PenyeliaAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PenyeliaInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenyeliaMahasiswaAdapter3 extends RecyclerView.Adapter<PenyeliaMahasiswaAdapter3.ViewHolder> {
    @NonNull
    Context context;
    List<PendaftarModel> pendaftarModelList;
    PenyeliaInterface penyeliaInterface;

    public PenyeliaMahasiswaAdapter3(@NonNull Context context, List<PendaftarModel> pendaftarModelList) {
        this.context = context;
        this.pendaftarModelList = pendaftarModelList;
    }

    public PenyeliaMahasiswaAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_mahasiswa_selector_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyeliaMahasiswaAdapter3.ViewHolder holder, int position) {
        holder.tvNama.setText(pendaftarModelList.get(holder.getAdapterPosition()).getName());
        holder.tvNim.setText(pendaftarModelList.get(holder.getAdapterPosition()).getNim());
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
            tvNim = itemView.findViewById(R.id.tvNim);
            tvSekolah = itemView.findViewById(R.id.tvSekolah);
            viewColor = itemView.findViewById(R.id.viewColor);
            cvMahasiswa = itemView.findViewById(R.id.cvMahasiswa);
            btnPilih = itemView.findViewById(R.id.btnPilih);
            btnPilih.setText("Input Penilaian");
            penyeliaInterface= DataApi.getClient().create(PenyeliaInterface.class);


            btnPilih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialogNilai = new Dialog(context);
                    dialogNilai.setContentView(R.layout.layout_penyelia_nilai);
                    dialogNilai.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Button btnSimpan, btnBatal;
                    EditText etInovasi, etKerjaSama, etDisiplin;
                    btnSimpan = dialogNilai.findViewById(R.id.btnSimpan);
                    btnBatal = dialogNilai.findViewById(R.id.btnTutup);
                    etInovasi = dialogNilai.findViewById(R.id.etInovasi);
                    etKerjaSama = dialogNilai.findViewById(R.id.etKerjaSama);
                    etDisiplin = dialogNilai.findViewById(R.id.etDisiplin);
                    dialogNilai.show();

                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogNilai.dismiss();
                        }
                    });
                    btnSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(etInovasi.getText().toString().isEmpty()) {
                                etInovasi.setError("Nilai inovasi tidak boleh kosong");
                                etInovasi.requestFocus();
                                return;
                            }else if(etKerjaSama.getText().toString().isEmpty()) {
                                etKerjaSama.setError("Nilai kerja sama tidak boleh kosong");
                                etKerjaSama.requestFocus();
                                return;
                            }else if(etDisiplin.getText().toString().isEmpty()) {
                                etDisiplin.setError("Nilai kedisiplian tidak boleh kosong");
                                etDisiplin.requestFocus();
                                return;
                            }else {

                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Loading").setMessage("Menyimpan penilaian...").setCancelable(false);
                                AlertDialog progressDialog = alert.create();
                                progressDialog.show();
                                Integer totalRata = Integer.parseInt(etInovasi.getText().toString()) + Integer.parseInt(etKerjaSama.getText().toString()) + Integer.parseInt(etDisiplin.getText().toString());
                                Integer result = totalRata / 3;

                                HashMap map = new HashMap();
                                map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), pendaftarModelList.get(getAdapterPosition()).getUserId()));
                                map.put("inovasi", RequestBody.create(MediaType.parse("text/plain"), etInovasi.getText().toString()));
                                map.put("kerja_sama", RequestBody.create(MediaType.parse("text/plain"),etKerjaSama.getText().toString()));
                                map.put("disiplin", RequestBody.create(MediaType.parse("text/plain"),etDisiplin.getText().toString()));
                                map.put("rata", RequestBody.create(MediaType.parse("text/plain"),String.valueOf(result)));

                                penyeliaInterface.insertPenilaian(map).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if (response.isSuccessful() && response.body().getCode() == 200) {
                                            progressDialog.dismiss();
                                            dialogNilai.dismiss();
                                            Toasty.success(context, "Berhasil menambahkan nilai", Toasty.LENGTH_SHORT).show();
                                            pendaftarModelList.remove(getAdapterPosition());
                                            notifyDataSetChanged();



                                        }else {
                                            progressDialog.dismiss();
                                            dialogNilai.dismiss();
                                            Toasty.error(context, "Terjadi kesalahan", Toasty.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        progressDialog.dismiss();
                                        dialogNilai.dismiss();
                                        Toasty.error(context, "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();



                                    }
                                });

                            }
                        }
                    });
                }
            });
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

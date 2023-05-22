package com.example.siangapp.PenyeliaAdapter;

import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.PenyeliaFragment.PenyeliaHistoryProjectFragment;
import com.example.siangapp.R;
import com.example.siangapp.model.ProjectModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;
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

public class PenyeliaProjectAdapter extends RecyclerView.Adapter<PenyeliaProjectAdapter.ViewHolder> {
    @NonNull
    Context context;
    List<ProjectModel> projectModelList;
    PendaftarInterface pendaftarInterface;
    PenyeliaInterface penyeliaInterface;

    public PenyeliaProjectAdapter(@NonNull Context context, List<ProjectModel> projectModelList) {
        this.context = context;
        this.projectModelList = projectModelList;
    }

    public PenyeliaProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyeliaProjectAdapter.ViewHolder holder, int position) {
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
            penyeliaInterface = DataApi.getClient().create(PenyeliaInterface.class);
            Dialog dialogOptionMenu = new Dialog(context);
            dialogOptionMenu.setContentView(R.layout.layout_option_menu_project);
            dialogOptionMenu.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button btnDetail, btnDelete, btnEdit, btnKembali;
            btnDetail = dialogOptionMenu.findViewById(R.id.btnDetail);
            btnDelete = dialogOptionMenu.findViewById(R.id.btnDelete);
            btnEdit = dialogOptionMenu.findViewById(R.id.btnEdit);
            btnKembali = dialogOptionMenu.findViewById(R.id.btnBatal);
            dialogOptionMenu.show();

            btnKembali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogOptionMenu.dismiss();
                }
            });
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogOptionMenu.dismiss();
                    Fragment fragment = new PenyeliaHistoryProjectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", projectModelList.get(getAdapterPosition()).getUserId());
                    fragment.setArguments(bundle);

                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.framePenyelia, fragment)
                            .addToBackStack(null).commit();

                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogOptionMenu.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Loading").setMessage("Menghapus project...");
                    alert.setCancelable(false);
                    AlertDialog progressBar = alert.create();
                    progressBar.show();

                    HashMap map = new HashMap();
                    map.put("user_id", RequestBody.create(MediaType.parse("text/data"), projectModelList.get(getAdapterPosition()).getUserId()));


                    penyeliaInterface.deleteProject(map).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                progressBar.dismiss();
                                dialogOptionMenu.dismiss();
                                projectModelList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                                Toasty.normal(context, "Berhasil menghapus project", Toasty.LENGTH_SHORT).show();
                            }else {
                                progressBar.dismiss();
                                Toasty.error(context, "Gagal mengahapus project", Toasty.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            progressBar.dismiss();
                            Toasty.normal(context, "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialogEdit = new Dialog(context);
                    dialogEdit.setContentView(R.layout.layout_insert_project);
                    dialogEdit.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Button btnSimpan, btnBatal;
                    EditText etJudul, etDeskripsi;
                    TextView etTanggalDari, etTanggalSelesai;
                    btnBatal = dialogEdit.findViewById(R.id.btnBatal);
                    btnSimpan = dialogEdit.findViewById(R.id.btnSimpan);
                    etJudul = dialogEdit.findViewById(R.id.etJudul);
                    etDeskripsi = dialogEdit.findViewById(R.id.etDeskripsi);
                    etTanggalDari = dialogEdit.findViewById(R.id.etTglDari);
                    etTanggalSelesai = dialogEdit.findViewById(R.id.etTglSelesai);
                    dialogEdit.show();

                    etTanggalDari.setText(projectModelList.get(getAdapterPosition()).getTglMulai());
                    etTanggalSelesai.setText(projectModelList.get(getAdapterPosition()).getTglSelesai());
                    etJudul.setText(projectModelList.get(getAdapterPosition()).getJudulTugas());
                    etDeskripsi.setText(projectModelList.get(getAdapterPosition()).getDeskripsiTugas());

                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogOptionMenu.dismiss();
                            dialogEdit.dismiss();
                        }
                    });
                    btnSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etJudul.getText().toString().isEmpty()) {
                                etJudul.setError("Judul tidak boleh kosong");
                                etJudul.requestFocus();
                                return;
                            } else  if (etDeskripsi.getText().toString().isEmpty()) {
                                etDeskripsi.setError("Deskripsi tidak boleh kosong");
                                etDeskripsi.requestFocus();
                                return;
                            }else  if (etTanggalDari.getText().toString().isEmpty()) {
                                etTanggalDari.setError("Tanggal dari tidak boleh kosong");
                                etTanggalDari.requestFocus();
                                return;
                            }else  if (etTanggalSelesai.getText().toString().isEmpty()) {
                                etTanggalSelesai.setError("Tanggal selesai tidak boleh kosong");
                                etTanggalSelesai.requestFocus();
                                return;
                            }else {
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Loading").setMessage("Meyimpan perubahan...").setCancelable(false);
                                AlertDialog progressBar = alert.create();

                                HashMap map = new HashMap();
                                map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), projectModelList.get(getAdapterPosition()).getUserId()));
                                map.put("judul_tugas", RequestBody.create(MediaType.parse("text/plain"), etJudul.getText().toString()));
                                map.put("deskripsi_tugas", RequestBody.create(MediaType.parse("text/plain"), etDeskripsi.getText().toString()));
                                map.put("tanggal_mulai", RequestBody.create(MediaType.parse("text/plain"), etTanggalDari.getText().toString()));
                                map.put("tanggal_selesai", RequestBody.create(MediaType.parse("text/plain"), etTanggalSelesai.getText().toString()));

                                penyeliaInterface.updateProject(map).enqueue(new Callback<ResponseModel>() {
                                    @Override
                                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                        if (response.isSuccessful() && response.body().getCode() == 200) {
                                            progressBar.dismiss();

                                            projectModelList.get(getAdapterPosition()).setJudulTugas(etJudul.getText().toString());
                                            projectModelList.get(getAdapterPosition()).setDeskripsiTugas(etDeskripsi.getText().toString());
                                            projectModelList.get(getAdapterPosition()).setTglMulai(etTanggalDari.getText().toString());
                                            projectModelList.get(getAdapterPosition()).setTglSelesai(etTanggalSelesai.getText().toString());
                                            notifyDataSetChanged();
                                            dialogEdit.dismiss();
                                            dialogOptionMenu.dismiss();
                                            Toasty.success(context, "berhasil mengubah project", Toasty.LENGTH_SHORT).show();
                                        }else {
                                            Toasty.error(context, "Terjadi kesalahan", Toasty.LENGTH_SHORT).show();
                                            progressBar.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                                        Toasty.error(context, "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();
                                        progressBar.dismiss();

                                    }
                                });
                            }
                        }
                    });
                }
            });


        }
    }
}

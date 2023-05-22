package com.example.siangapp.PenyeliaAdapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class PenyeliaMahasiswaAdapter2 extends RecyclerView.Adapter<PenyeliaMahasiswaAdapter2.ViewHolder> {
    @NonNull
    Context context;
    List<PendaftarModel> pendaftarModelList;
    PenyeliaInterface penyeliaInterface;
    SharedPreferences sharedPreferences;

    public PenyeliaMahasiswaAdapter2(@NonNull Context context, List<PendaftarModel> pendaftarModelList) {
        this.context = context;
        this.pendaftarModelList = pendaftarModelList;
    }

    public PenyeliaMahasiswaAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_mahasiswa_selector_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenyeliaMahasiswaAdapter2.ViewHolder holder, int position) {
        holder.tvNama.setText(pendaftarModelList.get(holder.getAdapterPosition()).getName());
        holder.tvNim.setText(pendaftarModelList.get(holder.getAdapterPosition()).getNim());
        holder.tvSekolah.setText(pendaftarModelList.get(holder.getAdapterPosition()).getSchool());

        penyeliaInterface = DataApi.getClient().create(PenyeliaInterface.class);
        sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);



        holder.btPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogProject = new Dialog(context);
                dialogProject.setContentView(R.layout.layout_insert_project);
                dialogProject.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Button btnSimpan, btnBatal;
                EditText etJudul, etDeskripsi;
                TextView etTglDari, etTglSelesai;
                btnSimpan = dialogProject.findViewById(R.id.btnSimpan);
                btnBatal = dialogProject.findViewById(R.id.btnBatal);
                etJudul = dialogProject.findViewById(R.id.etJudul);
                etDeskripsi = dialogProject.findViewById(R.id.etDeskripsi);
                etTglDari = dialogProject.findViewById(R.id.etTglDari);
                etTglSelesai = dialogProject.findViewById(R.id.etTglSelesai);
                dialogProject.show();

                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogProject.dismiss();
                    }
                });
                etTglDari.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDate(etTglDari);
                    }
                });

                etTglSelesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectDate(etTglSelesai);
                    }
                });
                btnSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etJudul.getText().toString().isEmpty()) {
                            etJudul.setError("Judul tidak boleh kosong");
                            etJudul.requestFocus();
                            return;
                        } else if (etDeskripsi.getText().toString().isEmpty()) {
                            etDeskripsi.setError("Deskripsi project tidak boleh kosong");
                            etDeskripsi.requestFocus();
                            return;
                        }else if (etTglDari.getText().toString().isEmpty()) {
                            etTglDari.setError("Tanggal mulai project tidak boleh kosong");
                            etTglDari.requestFocus();
                            return;
                        }else if (etTglDari.getText().toString().isEmpty()) {
                            etTglSelesai.setError("Tanggal selesai project tidak boleh kosong");
                            etTglSelesai.requestFocus();
                            return;
                        }else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Loading").setMessage("Menyimpan project...").setCancelable(false);
                            AlertDialog progressBar = alert.create();
                            progressBar.show();

                            HashMap map = new HashMap();
                            map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), pendaftarModelList.get(holder.getAdapterPosition()).getUserId()));
                            map.put("divisi_id", RequestBody.create(MediaType.parse("text/plain"), sharedPreferences.getString("divisi", null)));
                            map.put("judul_tugas", RequestBody.create(MediaType.parse("text/plain"), etJudul.getText().toString()));
                            map.put("deskripsi_tugas", RequestBody.create(MediaType.parse("text/plain"), etDeskripsi.getText().toString()));
                            map.put("tanggal_mulai", RequestBody.create(MediaType.parse("text/plain"), etTglDari.getText().toString()));
                            map.put("tanggal_selesai", RequestBody.create(MediaType.parse("text/plain"), etTglSelesai.getText().toString()));

                            penyeliaInterface.insertTugas(map).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        progressBar.dismiss();
                                        dialogProject.dismiss();
                                        Toasty.success(context, "Berhasil menambahkan project baru", Toasty.LENGTH_SHORT).show();
                                        ((FragmentActivity) context).onBackPressed();

                                    }else {
                                        progressBar.dismiss();
                                        Toasty.error(context, "Gagal menambahkan project baru", Toasty.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel>call, Throwable t) {
                                    progressBar.dismiss();
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
        Button btPilih;
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
            btPilih = itemView.findViewById(R.id.btnPilih);


        }

        @Override
        public void onClick(View v) {





        }
    }

    private void selectDate(TextView tvDate) {
        DatePickerDialog dp = new DatePickerDialog(context);
        dp.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d", dayOfMonth);

                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year +"/" +monthFormatted+"/"+dateFormatted);

            }
        });
        dp.show();
    }
}

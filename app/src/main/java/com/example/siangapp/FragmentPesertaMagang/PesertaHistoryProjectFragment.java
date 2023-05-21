package com.example.siangapp.FragmentPesertaMagang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.R;
import com.example.siangapp.model.HistoryProjectModel;
import com.example.siangapp.model.HistoryProjectModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.pesertaAdapter.PesertaHistoryProjectAdapter;
import com.example.siangapp.pesertaAdapter.PesertaKegiatanAdapter;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;
import com.example.siangapp.util.PesertaInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesertaHistoryProjectFragment extends Fragment {
    RecyclerView rvKegiatan;
    PesertaHistoryProjectAdapter pesertaHistoryProjectAdapter;
    List<HistoryProjectModel> historyProjectModelList;
    FloatingActionButton fabInsert;
    PendaftarInterface pendaftarInterface;
    SearchView searchView;
    TextView tvEmpty;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    String userId;
    PesertaInterface pesertaInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_peserta_history_project, container, false);
        rvKegiatan = view.findViewById(R.id.rvKegiatanPeserta);
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        searchView = view.findViewById(R.id.searchView);
        fabInsert = view.findViewById(R.id.fabInsert);
        getHistory();
        loadDataUser();

        fabInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogKegiatan = new Dialog(getContext());
                dialogKegiatan.setContentView(R.layout.layout_history_project);
                dialogKegiatan.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Button btnSimpan, btnBatal;
                EditText etKegiatan, etProgress, etNotes;
                btnSimpan = dialogKegiatan.findViewById(R.id.btnSimpan);
                btnBatal = dialogKegiatan.findViewById(R.id.btnBatal);
                etKegiatan = dialogKegiatan.findViewById(R.id.etKegiatan);
                etNotes = dialogKegiatan.findViewById(R.id.etNote);
                etProgress = dialogKegiatan.findViewById(R.id.etProgress);
                dialogKegiatan.show();

                btnBatal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogKegiatan.dismiss();
                    }
                });

                btnSimpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etKegiatan.getText().toString().isEmpty()) {
                            etKegiatan.setError("Field kegiatan tidak boleh kosong");
                            etKegiatan.requestFocus();
                            return;
                        } else  if (etProgress.getText().toString().isEmpty()) {
                            etProgress.setError("Field progress tidak boleh kosong");
                            etProgress.requestFocus();
                            return;
                        }
                        else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Loading...").setMessage("Menyimpan data...");
                            AlertDialog progressBar = alert.create();
                            progressBar.show();

                            HashMap map = new HashMap();
                            map.put("isi_kegiatan", RequestBody.create(MediaType.parse("text/plain"), etKegiatan.getText().toString()));
                            map.put("progress", RequestBody.create(MediaType.parse("text/plain"), etProgress.getText().toString()));
                            map.put("note", RequestBody.create(MediaType.parse("text/plain"), etNotes.getText().toString()));
                            map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));


                            pesertaInterface.insertHistoryProject(map).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        progressBar.dismiss();
                                        dialogKegiatan.dismiss();
                                        Toasty.success(getContext(), "Berhasil menambahkan kegiatan baru", Toasty.LENGTH_SHORT).show();
                                        rvKegiatan.setAdapter(null);
                                        pesertaHistoryProjectAdapter.notifyDataSetChanged();
                                        getHistory();

                                    }else {
                                        progressBar.dismiss();
                                        Toasty.error(getContext(), "Gagal menambahkan kegiatan baru", Toasty.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    progressBar.dismiss();
                                    Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });



            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return false;
            }
        });


        return view;

    }

    private void getHistory() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
        AlertDialog progressBar = alert.create();
        progressBar.show();

        pesertaInterface.getHistoryProjectByUserId(userId).enqueue(new Callback<List<HistoryProjectModel>>() {
            @Override
            public void onResponse(Call<List<HistoryProjectModel>> call, Response<List<HistoryProjectModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    historyProjectModelList = response.body();
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.GONE);
                    pesertaHistoryProjectAdapter = new PesertaHistoryProjectAdapter(getContext(), historyProjectModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvKegiatan.setAdapter(pesertaHistoryProjectAdapter);
                    rvKegiatan.setLayoutManager(linearLayoutManager);
                    rvKegiatan.setHasFixedSize(true);



                }else {
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<HistoryProjectModel>> call, Throwable t) {
                progressBar.dismiss();
                tvEmpty.setVisibility(View.GONE);
                Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();


            }
        });




    }

    private void loadDataUser(){
        AlertDialog.Builder dialogprogress = new AlertDialog.Builder(getContext());
        dialogprogress.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
        AlertDialog progressBar = dialogprogress.create();
        progressBar.show();


        pendaftarInterface.getUserById(userId).enqueue(new Callback<PendaftarModel>() {
            @Override
            public void onResponse(Call<PendaftarModel> call, Response<PendaftarModel> response) {
                if (response.isSuccessful() && response.body() != null) {


                    // menapilkan formulir pendaftaran jika user belum
                    // menaftar
                    if (response.body().getAcc().equals("lolos")) {
                        fabInsert.setEnabled(true);
                    }else {
                        fabInsert.setEnabled(false);
                    }
                    progressBar.dismiss();

                }else {
                    progressBar.dismiss();
                    Toasty.error(getContext(), "Gagal memuat data...", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PendaftarModel> call, Throwable t) {
                progressBar.dismiss();
                Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();

            }
        });
    }

    private void filter(String newText) {
        ArrayList<HistoryProjectModel> filteredList = new ArrayList<>();
        for (HistoryProjectModel item : historyProjectModelList) {
            if (item.getIsiKegiatab().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        pesertaHistoryProjectAdapter.filter(filteredList);
        if (filteredList.isEmpty())  {

        }else {
            pesertaHistoryProjectAdapter.filter(filteredList);
        }
    }
}
package com.example.siangapp.FragmentPesertaMagang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.siangapp.R;
import com.example.siangapp.model.KegiatanModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
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

public class PesertaKegiatanFragment extends Fragment {
    RecyclerView rvKegiatan;
    PesertaKegiatanAdapter pesertaKegiatanAdapter;
    List<KegiatanModel> kegiatanModelList;
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
        View view = inflater.inflate(R.layout.fragment_peserta_kegiatan, container, false);
        rvKegiatan = view.findViewById(R.id.rvKegiatanPeserta);
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        searchView = view.findViewById(R.id.searchView);
        fabInsert = view.findViewById(R.id.fabInsert);
        getKegiatan();
        loadDataUser();

        fabInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogKegiatan = new Dialog(getContext());
                dialogKegiatan.setContentView(R.layout.layout_kegiatan);
                dialogKegiatan.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Button btnSimpan, btnBatal;
                EditText etKegiatan;
                btnSimpan = dialogKegiatan.findViewById(R.id.btnSimpan);
                btnBatal = dialogKegiatan.findViewById(R.id.btnBatal);
                etKegiatan = dialogKegiatan.findViewById(R.id.etKegiatan);
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
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                            alert.setTitle("Loading...").setMessage("Menyimpan data...");
                            AlertDialog progressBar = alert.create();
                            progressBar.show();

                            HashMap map = new HashMap();
                            map.put("isi_kegiatan", RequestBody.create(MediaType.parse("text/plain"), etKegiatan.getText().toString()));
                            map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userId));


                            pesertaInterface.insertKegiatan(map).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful() && response.body().getCode() == 200) {
                                        progressBar.dismiss();
                                        dialogKegiatan.dismiss();
                                        Toasty.success(getContext(), "Berhasil menambahkan kegiatan baru", Toasty.LENGTH_SHORT).show();
                                        rvKegiatan.setAdapter(null);
                                        getKegiatan();

                                    }else {
                                        progressBar.dismiss();
                                        Toasty.error(getContext(), "Gagal menambahkan kegiatan baru", Toasty.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    progressBar.dismiss();
                                    Toasty.error(getContext(), "Tidak ada koneksi interner", Toasty.LENGTH_SHORT).show();

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

    private void getKegiatan() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
        AlertDialog progressBar = alert.create();
        progressBar.show();

        pesertaInterface.getKegiatanByUserId(userId).enqueue(new Callback<List<KegiatanModel>>() {
            @Override
            public void onResponse(Call<List<KegiatanModel>> call, Response<List<KegiatanModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    kegiatanModelList = response.body();
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.GONE);
                    pesertaKegiatanAdapter = new PesertaKegiatanAdapter(getContext(), kegiatanModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvKegiatan.setAdapter(pesertaKegiatanAdapter);
                    rvKegiatan.setLayoutManager(linearLayoutManager);
                    rvKegiatan.setHasFixedSize(true);



                }else {
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<KegiatanModel>> call, Throwable t) {
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
        ArrayList<KegiatanModel> filteredList = new ArrayList<>();
        for (KegiatanModel item : kegiatanModelList) {
            if (item.getIsiKegiatan().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        pesertaKegiatanAdapter.filter(filteredList);
        if (filteredList.isEmpty())  {

        }else {
            pesertaKegiatanAdapter.filter(filteredList);
        }
    }
}
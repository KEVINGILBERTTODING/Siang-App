package com.example.siangapp.AdminFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.AdminAdapter.AdminDataMahasiswaAdapter;
import com.example.siangapp.AdminAdapter.AdminVerifikasiMahasiswaAdapter;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.util.AdminInterface;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDataMahasiswaFragment extends Fragment {
    RecyclerView rvVerifPeserta;
    AdminDataMahasiswaAdapter adminDataMahasiswaAdapter;
    List<PendaftarModel> pendaftarModelList;
    String divisi;
    PendaftarInterface pendaftarInterface;
    SearchView searchView;
    TextView tvEmpty;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    String userId;
    private FloatingActionButton fabFilter;
    AdminInterface adminInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_data_mahasiswa, container, false);
        rvVerifPeserta = view.findViewById(R.id.rvMahasiswa);
        adminInterface = DataApi.getClient().create(AdminInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        fabFilter = view.findViewById(R.id.btnFilter);
        divisi = getArguments().getString("divisi");
        searchView = view.findViewById(R.id.searchView);
        getData();

        filterData();



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

    private void getData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
        AlertDialog progressBar = alert.create();
        progressBar.show();

        adminInterface.getPesertaByDivisi(divisi).enqueue(new Callback<List<PendaftarModel>>() {
            @Override
            public void onResponse(Call<List<PendaftarModel>> call, Response<List<PendaftarModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    pendaftarModelList = response.body();
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.GONE);
                    adminDataMahasiswaAdapter = new AdminDataMahasiswaAdapter(getContext(), pendaftarModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvVerifPeserta.setAdapter(adminDataMahasiswaAdapter);
                    rvVerifPeserta.setLayoutManager(linearLayoutManager);
                    rvVerifPeserta.setHasFixedSize(true);



                }else {
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<PendaftarModel>> call, Throwable t) {
                progressBar.dismiss();
                tvEmpty.setVisibility(View.GONE);
                Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();


            }
        });




    }

    private void filterData() {
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogFilter = new Dialog(getContext());
                dialogFilter.setContentView(R.layout.layout_filter);
                dialogFilter.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                TextView tvDateStart, tvDateEnd;
                Button btnDownload, btnBatal, btnFilter;
                tvDateStart = dialogFilter.findViewById(R.id.etDateStart);
                tvDateEnd = dialogFilter.findViewById(R.id.etDateEnd);
                btnDownload = dialogFilter.findViewById(R.id.btnDownload);
                btnFilter = dialogFilter.findViewById(R.id.btnFilter);
                btnBatal = dialogFilter.findViewById(R.id.btnBatal);

                dialogFilter.show();

                tvDateEnd.setOnClickListener(View -> {
                    getDatePicker(tvDateEnd);
                });

                tvDateStart.setOnClickListener(View -> {
                    getDatePicker(tvDateStart);
                });

                btnFilter.setOnClickListener(View -> {
                    if (tvDateStart.getText().toString().isEmpty()) {
                        tvDateStart.setError("Tidak boleh kosong");
                    }else if (tvDateEnd.getText().toString().isEmpty()) {
                        tvDateEnd.setError("Tidak boleh kosong");
                    }else {
                        rvVerifPeserta.setAdapter(null);
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
                        AlertDialog progressBar = alert.create();
                        progressBar.show();
                        adminInterface.filterPedaftaran(
                                divisi, tvDateStart.getText().toString(), tvDateEnd.getText().toString()
                        ).enqueue(new Callback<List<PendaftarModel>>() {
                            @Override
                            public void onResponse(Call<List<PendaftarModel>> call, Response<List<PendaftarModel>> response) {
                                progressBar.dismiss();
                                if (response.isSuccessful() && response.body() != null) {
                                    dialogFilter.dismiss();
                                    pendaftarModelList = response.body();
                                    adminDataMahasiswaAdapter = new AdminDataMahasiswaAdapter(getContext(), pendaftarModelList);
                                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                    rvVerifPeserta.setAdapter(adminDataMahasiswaAdapter);
                                    rvVerifPeserta.setLayoutManager(linearLayoutManager);
                                    rvVerifPeserta.setHasFixedSize(true);
                                }else {
                                    dialogFilter.dismiss();
                                    Toasty.normal(getContext(), "Tidak ada data", Toasty.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<PendaftarModel>> call, Throwable t) {
                                progressBar.dismiss();
                                Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();


                            }
                        });

                    }

                });



                btnDownload.setOnClickListener(View -> {
                    if (tvDateStart.getText().toString().isEmpty()) {
                        tvDateStart.setError("Tidak boleh kosong");
                    }else if (tvDateEnd.getText().toString().isEmpty()) {
                        tvDateEnd.setError("Tidak boleh kosong");
                    }else {
                       String Url = DataApi.URL_DOWNLOAD_REKAP_ANAK_MAGANG + tvDateStart.getText().toString() + "/" + tvDateEnd.getText().toString();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(Url));
                        startActivity(intent);
                    }

                });

                btnBatal.setOnClickListener(View -> {
                    dialogFilter.dismiss();
                });
            }
        });
    }

    private void getDatePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
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
                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);
            }



        });
        datePickerDialog.show();

    }




    private void filter(String newText) {
        ArrayList<PendaftarModel> filteredList = new ArrayList<>();
        for (PendaftarModel item : pendaftarModelList) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
       adminDataMahasiswaAdapter.filter(filteredList);
        if (filteredList.isEmpty())  {

        }else {
           adminDataMahasiswaAdapter.filter(filteredList);
        }
    }
}
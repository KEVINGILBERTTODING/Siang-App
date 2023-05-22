package com.example.siangapp.PenyeliaFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.PenyeliaAdapter.PenyeliaMahasiswaAdapter;
import com.example.siangapp.PenyeliaAdapter.PenyeliaMahasiswaAdapter2;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;
import com.example.siangapp.util.PenyeliaInterface;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenyeliaMahasiswaFragment2 extends Fragment {
    RecyclerView rvVerifPeserta;
    PenyeliaMahasiswaAdapter2 penyeliaMahasiswaAdapter;
    List<PendaftarModel> pendaftarModelList;
    String divisiId;
    PendaftarInterface pendaftarInterface;
    SearchView searchView;
    TextView tvEmpty;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    String userId;
    PenyeliaInterface penyeliaInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penyelia_mahasiswa, container, false);
        rvVerifPeserta = view.findViewById(R.id.rvMahasiswa);
        penyeliaInterface = DataApi.getClient().create(PenyeliaInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);

        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        divisiId = sharedPreferences.getString("divisi", null);
        searchView = view.findViewById(R.id.searchView);
        getData();



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

        penyeliaInterface.getPendaftarProject(divisiId).enqueue(new Callback<List<PendaftarModel>>() {
            @Override
            public void onResponse(Call<List<PendaftarModel>> call, Response<List<PendaftarModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    pendaftarModelList = response.body();
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.GONE);
                    penyeliaMahasiswaAdapter = new PenyeliaMahasiswaAdapter2(getContext(), pendaftarModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvVerifPeserta.setAdapter(penyeliaMahasiswaAdapter);
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


    private void filter(String newText) {
        ArrayList<PendaftarModel> filteredList = new ArrayList<>();
        for (PendaftarModel item : pendaftarModelList) {
            if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
       penyeliaMahasiswaAdapter.filter(filteredList);
        if (filteredList.isEmpty())  {

        }else {
           penyeliaMahasiswaAdapter.filter(filteredList);
        }
    }
}
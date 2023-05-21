package com.example.siangapp.AdminFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.AdminAdapter.AdminVerifikasiMahasiswaAdapter;
import com.example.siangapp.AdminAdapter.DivisiAdapter;
import com.example.siangapp.R;
import com.example.siangapp.model.DivisiModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.util.AdminInterface;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDivisiFragment extends Fragment {
    RecyclerView rvVerifPeserta;
    AdminVerifikasiMahasiswaAdapter adminVerifikasiMahasiswaAdapter;
    List<DivisiModel> divisiModelList;
    DivisiAdapter divisiAdapter;
    SearchView searchView;
    TextView tvEmpty;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    String userId;
    AdminInterface adminInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_divisi, container, false);
        rvVerifPeserta = view.findViewById(R.id.rvMahasiswa);
        adminInterface = DataApi.getClient().create(AdminInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);
        adminInterface = DataApi.getClient().create(AdminInterface.class);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        searchView = view.findViewById(R.id.searchView);
        getData();



        return view;

    }

    private void getData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
        AlertDialog progressBar = alert.create();
        progressBar.show();

        adminInterface.getAllDivisi().enqueue(new Callback<List<DivisiModel>>() {
            @Override
            public void onResponse(Call<List<DivisiModel>> call, Response<List<DivisiModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    divisiModelList = response.body();
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.GONE);
                    divisiAdapter = new DivisiAdapter(getContext(), divisiModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvVerifPeserta.setAdapter(divisiAdapter);
                    rvVerifPeserta.setLayoutManager(linearLayoutManager);
                    rvVerifPeserta.setHasFixedSize(true);



                }else {
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<DivisiModel>> call, Throwable t) {
                progressBar.dismiss();
                tvEmpty.setVisibility(View.GONE);
                Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();


            }
        });




    }


}
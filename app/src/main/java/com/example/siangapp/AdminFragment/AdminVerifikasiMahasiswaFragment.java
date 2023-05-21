package com.example.siangapp.AdminFragment;

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

import com.example.siangapp.AdminAdapter.AdminVerifikasiMahasiswaAdapter;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ProjectModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.pesertaAdapter.PesertaProjectAdapter;
import com.example.siangapp.util.AdminInterface;
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

public class AdminVerifikasiMahasiswaFragment extends Fragment {
    RecyclerView rvVerifPeserta;
    AdminVerifikasiMahasiswaAdapter adminVerifikasiMahasiswaAdapter;
    List<PendaftarModel> pendaftarModelList;
    FloatingActionButton fabInsert;
    PendaftarInterface pendaftarInterface;
    SearchView searchView;
    TextView tvEmpty;
    LinearLayoutManager linearLayoutManager;
    SharedPreferences sharedPreferences;
    String userId;
    AdminInterface adminInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_verifikasi_mahasiswa, container, false);
        rvVerifPeserta = view.findViewById(R.id.rvMahasiswa);
        adminInterface = DataApi.getClient().create(AdminInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);
        tvEmpty = view.findViewById(R.id.tvEmpty);
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

        adminInterface.getAllVerifPendaftar().enqueue(new Callback<List<PendaftarModel>>() {
            @Override
            public void onResponse(Call<List<PendaftarModel>> call, Response<List<PendaftarModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    pendaftarModelList = response.body();
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.GONE);
                    adminVerifikasiMahasiswaAdapter = new AdminVerifikasiMahasiswaAdapter(getContext(), pendaftarModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvVerifPeserta.setAdapter(adminVerifikasiMahasiswaAdapter);
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
       adminVerifikasiMahasiswaAdapter.filter(filteredList);
        if (filteredList.isEmpty())  {

        }else {
           adminVerifikasiMahasiswaAdapter.filter(filteredList);
        }
    }
}
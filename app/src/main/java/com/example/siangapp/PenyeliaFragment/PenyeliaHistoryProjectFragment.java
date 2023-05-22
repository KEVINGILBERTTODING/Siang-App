package com.example.siangapp.PenyeliaFragment;

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

import com.example.siangapp.PenyeliaAdapter.PenyeliaHistoryProjectAdapter;
import com.example.siangapp.R;
import com.example.siangapp.model.HistoryProjectModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.pesertaAdapter.PesertaHistoryProjectAdapter;
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

public class PenyeliaHistoryProjectFragment extends Fragment {
    RecyclerView rvKegiatan;
    PenyeliaHistoryProjectAdapter penyeliaHistoryProjectFragment;
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
        View view = inflater.inflate(R.layout.fragment_penyelia_history_project, container, false);
        rvKegiatan = view.findViewById(R.id.rvKegiatanPeserta);
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = getArguments().getString("user_id");
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        searchView = view.findViewById(R.id.searchView);
        fabInsert = view.findViewById(R.id.fabInsert);
        getHistory();


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
                    penyeliaHistoryProjectFragment = new PenyeliaHistoryProjectAdapter(getContext(), historyProjectModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvKegiatan.setAdapter(penyeliaHistoryProjectFragment);
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



    private void filter(String newText) {
        ArrayList<HistoryProjectModel> filteredList = new ArrayList<>();
        for (HistoryProjectModel item : historyProjectModelList) {
            if (item.getIsiKegiatab().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        penyeliaHistoryProjectFragment.filter(filteredList);
        if (filteredList.isEmpty())  {

        }else {
            penyeliaHistoryProjectFragment.filter(filteredList);
        }
    }
}
package com.example.siangapp.PenyeliaFragment;

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

import com.example.siangapp.PenyeliaAdapter.PenyeliaDataMahasiswaAdapter;
import com.example.siangapp.PenyeliaAdapter.PenyeliaProjectAdapter;
import com.example.siangapp.R;
import com.example.siangapp.model.ProjectModel;
import com.example.siangapp.model.ProjectModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;
import com.example.siangapp.util.PenyeliaInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenyeliaProjectFragment extends Fragment {
    RecyclerView rvVerifPeserta;
    PenyeliaProjectAdapter penyeliaProjectAdapter;
    List<ProjectModel> projectModelList;
    String divisiId;
    PendaftarInterface pendaftarInterface;
    SearchView searchView;
    TextView tvEmpty;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fabInsert;
    SharedPreferences sharedPreferences;
    String userId;
    PenyeliaInterface penyeliaInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penyelia_project, container, false);
        rvVerifPeserta = view.findViewById(R.id.rvMahasiswa);
        penyeliaInterface = DataApi.getClient().create(PenyeliaInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);
        fabInsert = view.findViewById(R.id.fabInsert);

        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        divisiId = sharedPreferences.getString("divisi", null);
        searchView = view.findViewById(R.id.searchView);
        getData();




        fabInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePenyelia, new PenyeliaMahasiswaFragment2())
                        .addToBackStack(null).commit();

            }
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

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
    }

    private void getData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
        AlertDialog progressBar = alert.create();
        progressBar.show();

        penyeliaInterface.getTugasByDivisi(divisiId).enqueue(new Callback<List<ProjectModel>>() {
            @Override
            public void onResponse(Call<List<ProjectModel>> call, Response<List<ProjectModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    projectModelList = response.body();
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.GONE);
                    penyeliaProjectAdapter = new PenyeliaProjectAdapter(getContext(), projectModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvVerifPeserta.setAdapter(penyeliaProjectAdapter);
                    rvVerifPeserta.setLayoutManager(linearLayoutManager);
                    rvVerifPeserta.setHasFixedSize(true);



                }else {
                    progressBar.dismiss();
                    tvEmpty.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<List<ProjectModel>> call, Throwable t) {
                progressBar.dismiss();
                tvEmpty.setVisibility(View.GONE);
                Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();


            }
        });




    }


    private void filter(String newText) {
        ArrayList<ProjectModel> filteredList = new ArrayList<>();
        for (ProjectModel item : projectModelList) {
            if (item.getJudulTugas().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
       penyeliaProjectAdapter.filter(filteredList);
        if (filteredList.isEmpty())  {

        }else {
           penyeliaProjectAdapter.filter(filteredList);
        }
    }
}
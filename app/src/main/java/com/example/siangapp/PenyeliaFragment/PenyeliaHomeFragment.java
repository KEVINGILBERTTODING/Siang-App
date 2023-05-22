package com.example.siangapp.PenyeliaFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.siangapp.AdminFragment.AdminDivisiFragment;
import com.example.siangapp.AdminFragment.AdminLaporanMahasiswaFragment;
import com.example.siangapp.AdminFragment.AdminVerifikasiMahasiswaFragment;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PenyeliaHomeFragment extends Fragment {
    TextView tvUsername;
    String userId;
    SharedPreferences sharedPreferences;

    ImageView ivProfile;
    PendaftarInterface pendaftarInterface;
    CardView cvDataPeserta, cvMenuKegiatan, cvMenuProject,  cvMenuLaporan, cvMenuHasilPenilaian ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_penyelia_home, container, false);
        tvUsername = view.findViewById(R.id.tvUsername);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        tvUsername.setText(sharedPreferences.getString("nama", null));
        userId = sharedPreferences.getString("user_id", null);
        ivProfile = view.findViewById(R.id.ivProfile);
        cvMenuLaporan = view.findViewById(R.id.cvMenuLaporan);
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);
        cvDataPeserta = view.findViewById(R.id.cvDataPeserta);
        cvMenuKegiatan = view.findViewById(R.id.cvMenuKegiatan);
        cvMenuProject = view.findViewById(R.id.cvMenuProject);
        cvMenuHasilPenilaian = view.findViewById(R.id.cvMenuHasilPenilaian);

        cvDataPeserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new PenyeliaDataMahasiswaFragment());
            }
        });

        cvMenuKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new PenyeliaMahasiswaFragment());
            }
        });


        cvMenuProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new PenyeliaProjectFragment());
            }
        });
        loadDataUser();



        return view;
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
                    Glide.with(getContext())
                            .load(response.body().getImage())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .fitCenter()
                            .centerCrop()
                            .into(ivProfile);



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

    private void replace(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePenyelia, fragment)
                .addToBackStack(null).commit();
    }

}
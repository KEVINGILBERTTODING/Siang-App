package com.example.siangapp.FragmentPesertaMagang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.siangapp.R;
import com.example.siangapp.databinding.FragmentPesertaUpdateProfileBinding;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PesertaInterface;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class PesertaUpdateProfileFragment extends Fragment {
    
    private FragmentPesertaUpdateProfileBinding binding;
    private PesertaInterface pesertaInterface;
    private AlertDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPesertaUpdateProfileBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfile();
        listener();
    }

    private void listener() {
        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        binding.btnTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        pesertaInterface.getProfile(userId).enqueue(new Callback<PendaftarModel>() {
            @Override
            public void onResponse(Call<PendaftarModel> call, Response<PendaftarModel> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body() != null) {
                    binding.etNama.setText(response.body().getName());
                    binding.etNamaPenyelia.setText(response.body().getNamaPenyelia());
                    binding.etEmail.setText(response.body().getEmail());
                            binding.etAlamat.setText(response.body().getAddress());
                            binding.etAsalSekolah.setText(response.body().getSchool());
                            binding.etJurusan.setText(response.body().getMajor());
                            binding.etNim.setText(response.body().getNim());
                            binding.etAlamatSekolah.setText(response.body().getSchoolAddress());
                            binding.etNoTelp.setText(response.body().getPhoneNumber());
                }else {
                    showToast("err", "Tidak dapat memuat data");
                }
            }

            @Override
            public void onFailure(Call<PendaftarModel> call, Throwable t) {
                showProgressBar("s", "s", false);
                showToast("err", "Tidak ada koneksi internet");



            }
        });

    }


    private void updateProfile() {
        showProgressBar("Loading", "Menyimpan data...", true);
        pesertaInterface.updateProfile(
                userId,
                binding.etNama.getText().toString(),
                binding.etAlamat.getText().toString(),
                binding.etAsalSekolah.getText().toString(),
                binding.etJurusan.getText().toString(),
                binding.etNim.getText().toString(),
                binding.etAlamatSekolah.getText().toString(),
                binding.etNoTelp.getText().toString()
        ).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                showProgressBar("sd", "d", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success", "berhasil mengubah profil");
                    getActivity().onBackPressed();
                }else {
                    showToast("err", "Terjadi kesalahan");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("sd", "d", false);
                showToast("err", "Tidak ada koneksi internet");



            }
        });
    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                progressDialog = builder.create();
            }
            progressDialog.show(); // Menampilkan progress dialog
        } else {
            // Menyembunyikan progress dialog jika ada
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    private void showToast(String jenis, String text) {
        if (jenis.equals("success")) {
            Toasty.success(getContext(), text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), text, Toasty.LENGTH_SHORT).show();
        }
    }
}
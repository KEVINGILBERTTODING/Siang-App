package com.example.siangapp.PenyeliaFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.siangapp.FileDownload;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.AdminInterface;
import com.example.siangapp.util.DataApi;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenyeliaDetailMahasiswaFragment extends Fragment {
    EditText etNamaLengkap, etAlamat, etAsalSekolah, etAlamatSekolah, etJk, etAgama, etJurusan,
    etNim, etEmail, etTelepon;
    Button btnKembali;
    TextView tvStatus;
    CardView cvStatus;
    String userId, suratKeterangan;
    AdminInterface adminInterface;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_penyelia_detail_mahasiswa, container, false);
       etNamaLengkap = view.findViewById(R.id.etNamaLengkap);
         etAlamat = view.findViewById(R.id.etAlamat);
            etAsalSekolah = view.findViewById(R.id.etAsalSekolah);
            etAlamatSekolah = view.findViewById(R.id.etAlamatSekolah);
            etJk = view.findViewById(R.id.etJk);
            etAgama = view.findViewById(R.id.etAgama);
            etJurusan = view.findViewById(R.id.etJurusan);
            etNim = view.findViewById(R.id.etNim);
            etEmail = view.findViewById(R.id.etEmail);
            cvStatus = view.findViewById(R.id.cvStatus);
            tvStatus = view.findViewById(R.id.tvStatus);
            etTelepon = view.findViewById(R.id.etTelepon);


            btnKembali = view.findViewById(R.id.btnKembali);
            userId = getArguments().getString("user_id");
            adminInterface = DataApi.getClient().create(AdminInterface.class);

            btnKembali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });



            getDetailUser();


       return view;
    }

    private void getDetailUser(){
        AlertDialog.Builder dialogprogress = new AlertDialog.Builder(getContext());
        dialogprogress.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
        AlertDialog progressBar = dialogprogress.create();
        progressBar.show();


        adminInterface.getDetailUser(userId).enqueue(new Callback<PendaftarModel>() {
            @Override
            public void onResponse(Call<PendaftarModel> call, @NonNull Response<PendaftarModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getAcc().equals("lolos")) {
                            tvStatus.setText("Lolos");
                            tvStatus.setTextColor(getContext().getColor(R.color.green));

                        }else if (response.body().getAcc().equals("tidak_aktif")) {
                            tvStatus.setTextColor(getContext().getColor(R.color.black));
                            tvStatus.setText("Selesai");
                        }else if (response.body().getAcc().equals("ditolak")) {
                            tvStatus.setTextColor(getContext().getColor(R.color.red));
                            ;
                            tvStatus.setText("Ditolak");
                        } else if (response.body().getAcc().equals("belum")) {
                            tvStatus.setTextColor(getContext().getColor(R.color.yellow));

                            tvStatus.setText("Belum");
                        }
                        suratKeterangan = response.body().getCertificate();

                        etNamaLengkap.setText(response.body().getName());
                        etAlamat.setText(response.body().getAddress());
                        etAgama.setText(response.body().getReligion());
                        etJk.setText(response.body().getGender());
                        etAsalSekolah.setText(response.body().getSchool());
                        etAlamatSekolah.setText(response.body().getSchoolAddress());
                        etJurusan.setText(response.body().getMajor());
                        etNim.setText(response.body().getNim());
                        etEmail.setText(response.body().getEmail());
                        etTelepon.setText(response.body().getPhoneNumber());
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

}
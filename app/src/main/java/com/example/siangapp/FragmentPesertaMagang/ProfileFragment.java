package com.example.siangapp.FragmentPesertaMagang;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.siangapp.LoginActivity;
import com.example.siangapp.R;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PenyeliaInterface;
import com.example.siangapp.util.PesertaInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    Button btnLogOut, btnEdit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String userId, roleId, nama, alamat, password;
    private PesertaInterface pesertaInterface;
    private AlertDialog progressDialog;
    private TextView tvUsername, tvSimpan;
    private ImageView ivProfile;
    private ImageButton btnEditPhoto;
    private PenyeliaInterface penyeliaInterface;
    private File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        pesertaInterface = DataApi.getClient().create(PesertaInterface.class);

        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", null);
        roleId = sharedPreferences.getString("role_id", null);
        penyeliaInterface = DataApi.getClient().create(PenyeliaInterface.class);

        editor = sharedPreferences.edit();
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfile = view.findViewById(R.id.ivProfile);
        btnEditPhoto = view.findViewById(R.id.btnFilePicker);
        tvSimpan = view.findViewById(R.id.tvSimpan);
        btnEdit = view.findViewById(R.id.btnEdit);

        listener();
        getProfile();

        return  view;
    }


    private void getProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        pesertaInterface.getProfile2(userId).enqueue(new Callback<PendaftarModel>() {
            @Override
            public void onResponse(Call<PendaftarModel> call, Response<PendaftarModel> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body() != null) {
                    tvUsername.setText(response.body().getName());
                    Glide.with(getContext())
                            .load(response.body().getImage())
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivProfile);
                    nama = response.body().getName();
                    alamat = response.body().getAddress();
                    password = response.body().getPassword();

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

    private void listener() {
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();

            }
        });

        btnEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                tvSimpan.setVisibility(View.VISIBLE);
            }
        });

        tvSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfile();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roleId.equals("3")) { // jika Peserta
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.framePesertaMagang, new PesertaUpdateProfileFragment()).addToBackStack(null)
                            .commit();
                }else { // Jika admin && penyelia
                    updateProfileAdmin();
                }


            }
        });
    }


    private void uploadProfile() {
        HashMap map = new HashMap();
        map.put("id", RequestBody.create(MediaType.parse("text/plain"), userId));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("photo", file.getName(), requestBody);
        showProgressBar("Loading", "Mengupload gambar...", true);
        pesertaInterface.updatePhotoProfile(map, filePart).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call <ResponseModel>call, Response<ResponseModel> response) {
                showProgressBar("s", "s", false);
                if (response.isSuccessful() && response.body().getCode() == 200) {
                    showToast("success","Berhasil mengubah foto profile");
                    tvSimpan.setVisibility(View.GONE);

                }else{
                    showToast("err",response.body().getMessage());

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("s", "s", false);
                showToast("err","Tidak ada koneksi internet");


            }
        });
    }

    private void updateProfileAdmin() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_ubah_profile);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        EditText etNamaLengkap, etAlamat, etPassword;
        etNamaLengkap = dialog.findViewById(R.id.etNama);
        etAlamat = dialog.findViewById(R.id.etAlamatPengguna);
        etPassword  = dialog.findViewById(R.id.etPassword);
        Button btnSimpan, btnBatal;
        btnSimpan = dialog.findViewById(R.id.btnSimpan);
        btnBatal = dialog.findViewById(R.id.btnBatal);

        etNamaLengkap.setText(nama);
        etAlamat.setText(alamat);
        etPassword.setText(password);
        dialog.show();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar("Loading", "Menyimpan data...", true);
                penyeliaInterface.updateProfile(userId, etNamaLengkap.getText().toString(),
                        etAlamat.getText().toString(), etPassword.getText().toString())
                        .enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                showProgressBar("s", "S", false);
                                if (response.isSuccessful() && response.body().getCode() == 200) {
                                    showToast("success", "Berhasil mengubah profil");
                                    dialog.dismiss();
                                }else {
                                    showToast("err", "Terjadi kesalahan");
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                showProgressBar("s", "S", false);
                                showToast("err", "Tidak ada koneksi internet");



                            }
                        });

            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String pdfPath = getRealPathFromUri(uri);
                file = new File(pdfPath);
                tvSimpan.setVisibility(View.VISIBLE);
                ivProfile.setImageURI(uri);


            }
        }
    }


    public String getRealPathFromUri(Uri uri) {
        String filePath = "";
        if (getContext().getContentResolver() != null) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                File file = new File(getContext().getCacheDir(), getFileName(uri));
                writeFile(inputStream, file);
                filePath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void writeFile(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
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
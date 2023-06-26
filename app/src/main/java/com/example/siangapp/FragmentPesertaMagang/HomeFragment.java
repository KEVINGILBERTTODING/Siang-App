package com.example.siangapp.FragmentPesertaMagang;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.siangapp.AllAdapter.SpinnerDivisiAdapter;
import com.example.siangapp.R;
import com.example.siangapp.model.DivisiModel;
import com.example.siangapp.model.PendaftarModel;
import com.example.siangapp.model.ResponseModel;
import com.example.siangapp.util.DataApi;
import com.example.siangapp.util.PendaftarInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    TextView tvUsername, tvTglMulai, tvTglSelesai, tvStatus, tvAlasan;
    String userId, divisi;
    SharedPreferences sharedPreferences;
    Button btnFilePicker;
    SpinnerDivisiAdapter spinnerDivisiAdapter;
    EditText etFilePath;

    ImageView ivProfile;
    PendaftarInterface pendaftarInterface;
    CardView cvProject, cvKegiatan, cvStatus;
    List<DivisiModel> divisiModelList;
    Spinner spDivisi;
    private File file;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tvUsername = view.findViewById(R.id.tvUsername);
        sharedPreferences = getContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        tvUsername.setText(sharedPreferences.getString("nama", null));
        userId = sharedPreferences.getString("user_id", null);
        ivProfile = view.findViewById(R.id.ivProfile);
        cvStatus = view.findViewById(R.id.cvStatus);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvAlasan = view.findViewById(R.id.tvAlasan);
        cvKegiatan = view.findViewById(R.id.cvMenuKegiatan);
        cvProject = view.findViewById(R.id.cvMenuProject);
        pendaftarInterface = DataApi.getClient().create(PendaftarInterface.class);

        loadDataUser();

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new ProfileFragment());
            }
        });

        cvKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new PesertaKegiatanFragment());
            }
        });

        cvProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new PesertaProjectFragment());
            }
        });



        return view;
    }

    private void loadDataUser(){
        AlertDialog.Builder dialogprogress = new AlertDialog.Builder(getContext());
        dialogprogress.setTitle("Loading").setMessage("Memuat data...").setCancelable(false);
        AlertDialog progressBar = dialogprogress.create();
        progressBar.show();


        pendaftarInterface.getProfile2(userId).enqueue(new Callback<PendaftarModel>() {
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

                    // menapilkan formulir pendaftaran jika user belum
                    // menaftar
                    if (response.body().getDivision() == null) {
                        formulirPendadaftaran();
                        getAllDivisi();
                    }else {
                        if (response.body().getAcc().equals("belum")) {
                            cvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setText("Menunggu persetujuan...");
                        }else if (response.body().getAcc().equals("tidak_aktif")) {
                            cvStatus.setVisibility(View.VISIBLE);
                            tvStatus.setText("Status magang anda tidak aktif");
                        }else if (response.body().getAcc().equals("ditolak")) {
                            cvStatus.setVisibility(View.VISIBLE);
                            cvStatus.setCardBackgroundColor(getContext().getColor(R.color.red));
                            tvStatus.setTextColor(getContext().getColor(R.color.white));
                            tvAlasan.setText(response.body().getReason());
                            tvAlasan.setVisibility(View.VISIBLE);
                            tvStatus.setText("Pendaftaran Anda ditolak!");
                        } else {
                            cvStatus.setVisibility(View.GONE);
                        }
                    }

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



    private void getAllDivisi() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setCancelable(false)
                .setTitle("Loading").setMessage("Memuat data...");
        AlertDialog progressBar = alert.create();
        progressBar.show();


       pendaftarInterface.getAllDivisi().enqueue(new Callback<List<DivisiModel>>() {
           @Override
           public void onResponse(Call<List<DivisiModel>> call, Response<List<DivisiModel>> response) {
               if (response.isSuccessful() && response.body().size() > 0) {
                   divisiModelList = response.body();
                   spinnerDivisiAdapter = new SpinnerDivisiAdapter(getContext(), divisiModelList);
                   spDivisi.setAdapter(spinnerDivisiAdapter);
                   progressBar.dismiss();


               }else {
                   progressBar.dismiss();
                   Toasty.error(getContext(), "Gagal memuat data", Toasty.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<List<DivisiModel>> call, Throwable t) {
               progressBar.dismiss();
               Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();

           }
       });
    }

    private void selectDate(TextView tvDate){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month  < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                } else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d", dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "/" + monthFormatted + "/" +dateFormatted);
            }
        });

        datePickerDialog.show();
    }


    private void formulirPendadaftaran() {
        Dialog formulirDialog = new Dialog(getContext());
        formulirDialog.setContentView(R.layout.layout_formulir_magang);
        formulirDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        formulirDialog.setCancelable(false);
        formulirDialog.setCanceledOnTouchOutside(false);
        tvTglMulai = formulirDialog.findViewById(R.id.tvTglMulai);
        tvTglSelesai = formulirDialog.findViewById(R.id.tvTglSelesai);
        spDivisi = formulirDialog.findViewById(R.id.spDivisi);
        btnFilePicker = formulirDialog.findViewById(R.id.btnFilePicker);
        etFilePath = formulirDialog.findViewById(R.id.etFilePath);
        Button btnSimpan = formulirDialog.findViewById(R.id.btnSimpan);
        formulirDialog.show();

        tvTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(tvTglMulai);
            }
        });

        tvTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(tvTglSelesai);
            }
        });



        btnFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 1);
            }
        });

        spDivisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                divisi = String.valueOf(spinnerDivisiAdapter.getDivisId(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTglMulai.getText().toString().isEmpty()) {
                    tvTglMulai.setError("Tanggal mulai tidak boleh kosong");
                    tvTglMulai.requestFocus();
                    return;
                } else  if (tvTglSelesai.getText().toString().isEmpty()) {
                    tvTglSelesai.setError("Tanggal selesai tidak boleh kosong");
                    tvTglSelesai.requestFocus();
                    return;
                } else if (etFilePath.getText().toString().isEmpty()) {
                        etFilePath.setError("Tanggal mulai tidak boleh kosong");
                        etFilePath.requestFocus();
                        return;

                }else {

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setCancelable(false)
                            .setTitle("Loading").setMessage("Memuat data...");
                    AlertDialog progressBar = alert.create();
                    progressBar.show();

                    HashMap map = new HashMap();
                    map.put("id", RequestBody.create(MediaType.parse("text/plain"), userId));
                    map.put("tgl_mulai", RequestBody.create(MediaType.parse("text/plain"), tvTglMulai.getText().toString()));
                    map.put("tgl_selesai", RequestBody.create(MediaType.parse("text/plain"), tvTglSelesai.getText().toString()));
                    map.put("divisi", RequestBody.create(MediaType.parse("text/plain"), divisi));

                    RequestBody rbFile = RequestBody.create(MediaType.parse("application/pdf"), file);
                    MultipartBody.Part filePendaftaran = MultipartBody.Part.createFormData("file_pendaftaran", file.getName(), rbFile);


                    pendaftarInterface.insertFormulirPendaftaran(map, filePendaftaran).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.isSuccessful() && response.body().getCode() == 200) {
                                progressBar.dismiss();
                                Toasty.success(getContext(), "Berhasil mendaftar", Toasty.LENGTH_SHORT).show();
                                loadDataUser();
                                formulirDialog.dismiss();

                            }else {
                                Toasty.error(getContext(), response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                progressBar.dismiss();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toasty.error(getContext(), "Tidak ada koneksi internet", Toasty.LENGTH_SHORT).show();
                            progressBar.dismiss();

                        }
                    });


                }
            }
        });
    }

    private void replace(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framePesertaMagang, fragment)
                .addToBackStack(null).commit();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String pdfPath = getRealPathFromUri(uri);
                file = new File(pdfPath);
                etFilePath.setText(file.getName());
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
}
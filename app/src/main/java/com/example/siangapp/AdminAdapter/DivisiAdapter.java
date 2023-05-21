package com.example.siangapp.AdminAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siangapp.AdminFragment.AdminDataMahasiswaFragment;
import com.example.siangapp.R;
import com.example.siangapp.model.DivisiModel;

import java.util.List;

public class DivisiAdapter extends RecyclerView.Adapter<DivisiAdapter.ViewHolder> {
    Context context;
    List<DivisiModel> divisiModelList;


    public DivisiAdapter(Context context, List<DivisiModel> divisiModelList) {
        this.context = context;
        this.divisiModelList = divisiModelList;
    }

    @NonNull
    @Override
    public DivisiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_divisi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DivisiAdapter.ViewHolder holder, int position) {
        holder.tvDivisi.setText(divisiModelList.get(holder.getAdapterPosition()).getDivisi());

    }

    @Override
    public int getItemCount() {
        return divisiModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDivisi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDivisi = itemView.findViewById(R.id.tvDivisi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Fragment fragment = new AdminDataMahasiswaFragment();
            Bundle bundle = new Bundle();
            bundle.putString("divisi", String.valueOf(divisiModelList.get(getAdapterPosition()).getId()));
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameAdmin, fragment).addToBackStack(null).commit();

        }
    }
}

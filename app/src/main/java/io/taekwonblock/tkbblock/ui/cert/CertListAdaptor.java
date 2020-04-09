package io.taekwonblock.tkbblock.ui.cert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.CertificatesQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.CertificateModel;
import io.taekwonblock.tkbblock.ui.user.UserMenuListAdaptor;

public class CertListAdaptor extends RecyclerView.Adapter<CertListAdaptor.ViewHolder> {

    List<CertificatesQuery.Certificate> certificateList;
    Context mContext;
    Fragment fragment;


    public CertListAdaptor(Context context, Fragment fragment, List<CertificatesQuery.Certificate> list) {
        this.mContext = context;
        this.certificateList = list;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cert_list, parent, false);
        return new CertListAdaptor.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CertificatesQuery.Certificate cert = certificateList.get(position);

        holder.name.setText(cert.cert_name());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CertificateModel certificateModel = new CertificateModel(cert);

                Intent intent = new Intent(mContext, ModifyCertificateActivity.class);
                intent.putExtra("certModel", certificateModel);
                fragment.startActivityForResult(intent, 499);
            }
        });

    }

    @Override
    public int getItemCount() {
        return certificateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.cert_item_name);
            layout = itemView.findViewById(R.id.cert_item_layout);

        }
    }
}

package io.taekwonblock.tkbblock.ui.cert;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.R;

public class CertificateImagesAdaptor extends RecyclerView.Adapter<CertificateImagesAdaptor.ViewHolder> {
    private static final String TAG = "CertificateImagesAdaptor";
    List<Uri> uriList;
    Context context;

    public CertificateImagesAdaptor(List<Uri> list) {
        this.uriList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cert_image, parent, false);
        return new CertificateImagesAdaptor.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Uri imageUri = uriList.get(position);
        Log.v(TAG, "imageUri"  + imageUri.toString());



        holder.image.setImageURI(imageUri);
    }

    @Override
    public int getItemCount() {
        return this.uriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            image= itemView.findViewById(R.id.add_cert_item_image);
            layout = itemView.findViewById(R.id.add_cert_item);

        }
    }
}

package io.taekwonblock.tkbblock.ui.dojo.dojoinfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.ui.dojo.DojoListAdaptor;

public class DojoInfoGalleryAdaptor extends RecyclerView.Adapter<DojoInfoGalleryAdaptor.ViewHolder> {

    private static final String TAG = "DojoInfoGalleryAdaptor";
    List<String> uriList;
    Context mContext;

    public DojoInfoGalleryAdaptor(Context context, List<String> uriList) {
        this.mContext = context;
        this.uriList = uriList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dojoinfo_gallery, parent, false);
        return new DojoInfoGalleryAdaptor.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String imageUri = uriList.get(position);

        Log.v(TAG,  "onBindViewHolder" + imageUri);

        if(imageUri != null) {

            Log.v(TAG,  "onBindViewHolder" + ApplicationClass.FILE_URL + imageUri);
            Picasso.get().load(ApplicationClass.FILE_URL + imageUri).fit().into(holder.image);

        }

    }

    @Override
    public int getItemCount() {
        return uriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.dojoinfo_image);

        }
    }

}

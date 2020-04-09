package io.taekwonblock.tkbblock.ui.cert;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.R;

public class CertDetailImagesAdaptor extends RecyclerView.Adapter<CertDetailImagesAdaptor.ViewHolder> {

    List<String> uriList;
    Context mContext;

    public CertDetailImagesAdaptor(Context context, List<String> uriList) {
        this.mContext = context;
        this.uriList = uriList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cert_detail, parent, false);
        return new CertDetailImagesAdaptor.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String imageUri = uriList.get(position);
        final ImageView imageView = holder.image;

        if(imageUri != null) {

            new Thread() {
                public void run() {
                    try {
                        URL url = new URL(ApplicationClass.FILE_URL + imageUri);
                        final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                        Log.v("TESTTEST", "" + bmp.getWidth());

                        ((Activity)mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bmp);
                            }
                        });

                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }

                }
            }.start();






////            holder.image.setImageURI(Uri.parse(ApplicationClass.FILE_URL + imageUri) );
//            Picasso.get().load(ApplicationClass.FILE_URL + imageUri).fit().into(holder.image);

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
            image= itemView.findViewById(R.id.cert_detail_image);
        }
    }
}

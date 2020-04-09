package io.taekwonblock.tkbblock.ui.dojo;

import android.content.Context;
import android.content.Intent;
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
import io.taekwonblock.tkbblock.DojosQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.DojoModel;
import io.taekwonblock.tkbblock.ui.dojo.dojoinfo.DojoInfoActivity;

public class DojoListAdaptor  extends RecyclerView.Adapter<DojoListAdaptor.ViewHolder> {

    final static String TAG = "DojoListAdaptor";

    List<DojosQuery.Dojo> dojos;
    Context mContext;

    public DojoListAdaptor(Context context, List<DojosQuery.Dojo> dojos) {
        Log.v(TAG, dojos.toString());
        this.dojos = dojos;
        this.mContext = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dojo_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DojosQuery.Dojo dojo = dojos.get(position);
        final DojoModel dojoModel = new DojoModel(dojo);

        holder.name.setText(dojo.dojo_name());
        holder.address.setText(dojo.address());
        holder.description.setText(dojo.description());
        Log.v(TAG,  dojo.images().toString() );

        if(dojo.images().size() > 0) {
            Log.v(TAG,  dojo.images().get(0));
            Picasso.get().load(ApplicationClass.FILE_URL + dojo.images().get(0)).fit().into(holder.image);

        }


        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), DojoInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                Log.v(TAG, dojoModel.getDojo_name());
                intent.putExtra("data_model", dojoModel);

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dojos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView address;
        TextView description;

        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.dojo_image);
            name = (TextView) itemView.findViewById(R.id.dojo_name);
            address = (TextView) itemView.findViewById(R.id.dojo_address);
            description = (TextView) itemView.findViewById(R.id.dojo_description);
            cardview = (CardView) itemView.findViewById(R.id.dojo_card);

        }
    }

}

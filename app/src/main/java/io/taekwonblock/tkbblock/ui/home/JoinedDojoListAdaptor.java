package io.taekwonblock.tkbblock.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.JoinedDojoQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.RegistratedDojoQuery;

public class JoinedDojoListAdaptor extends RecyclerView.Adapter<JoinedDojoListAdaptor.ViewHolder> {

    List<JoinedDojoQuery.JoinedDojo> dojoList;
    Context mContext;
    int accountType;

    public JoinedDojoListAdaptor(Context context, List<JoinedDojoQuery.JoinedDojo> dojos) {

//        this.dojoList = new ArrayList<>(dojos.size() * 2);
        this.dojoList = new ArrayList<>(dojos.size() );

        this.dojoList.addAll(dojos);
//        this.dojoList.addAll(dojos);

        this.mContext = context;
        this.accountType = 1;
    }

    public JoinedDojoListAdaptor(Context context, List<JoinedDojoQuery.JoinedDojo> dojos, int accountType) {

//        this.dojoList = new ArrayList<>(dojos.size() * 2);
        this.dojoList = new ArrayList<>(dojos.size() );

        this.dojoList.addAll(dojos);
//        this.dojoList.addAll(dojos);

        this.mContext = context;
        this.accountType = accountType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_dojos, parent, false);
        return new JoinedDojoListAdaptor.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final JoinedDojoQuery.JoinedDojo registratedDojo = dojoList.get(position);
        
        holder.dojoName.setText(registratedDojo.dojo_name());

        Log.v("DIMENSION", holder.dojoImage.getWidth() + "");

        if(registratedDojo.images().size() > 0)
            Picasso.get().load(ApplicationClass.FILE_URL + registratedDojo.images().get(0)).fit().centerCrop().into(holder.dojoImage);

        holder.courseList.setVisibility(View.GONE);
        holder.courseEmpty.setVisibility(View.GONE);
        holder.btnGoCourse.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return dojoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dojoImage;
        TextView dojoName;
        Button btnGoCourse;

        RecyclerView courseList;
        TextView courseEmpty;

        public ViewHolder(View itemView) {
            super(itemView);

            dojoImage = itemView.findViewById(R.id.home_dojo_images);
            dojoName = itemView.findViewById(R.id.home_dojo_name);

            btnGoCourse = itemView.findViewById(R.id.home_dojo_go_course);

            courseEmpty = itemView.findViewById(R.id.home_dojo_no_course);
            courseList = itemView.findViewById(R.id.home_dojo_courses);

        }
    }
}

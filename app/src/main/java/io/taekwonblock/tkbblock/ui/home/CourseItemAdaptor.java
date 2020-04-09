package io.taekwonblock.tkbblock.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.RegistratedDojoQuery;

public class CourseItemAdaptor extends RecyclerView.Adapter<CourseItemAdaptor.ViewHolder> {

    List<RegistratedDojoQuery.Course> courseList;
    Context mContext;

    public CourseItemAdaptor(Context context, List<RegistratedDojoQuery.Course> list) {
        this.mContext = context;
        this.courseList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.state.setVisibility(View.INVISIBLE);
        holder.name.setText(courseList.get(position).course_name());
        holder.arrow.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView name;
        TextView state;
        TextView arrow;

        public ViewHolder(View itemView) {
            super(itemView);


            layout = itemView.findViewById(R.id.course_list_layout);
            name = itemView.findViewById(R.id.course_list_name);
            state = itemView.findViewById(R.id.course_list_state);
            arrow = itemView.findViewById(R.id.course_list_arrow);
        }
    }
}

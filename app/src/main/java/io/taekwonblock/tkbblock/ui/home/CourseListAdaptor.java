package io.taekwonblock.tkbblock.ui.home;

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
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.CoursesQuery;
import io.taekwonblock.tkbblock.CoursesWithStateQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.CourseModel;

public class CourseListAdaptor extends RecyclerView.Adapter<CourseListAdaptor.ViewHolder> {


    List<CoursesWithStateQuery.CoursesWithState> courseList;
    Context mContext;
    String dojo_uuid;

    public CourseListAdaptor(Context context, List<CoursesWithStateQuery.CoursesWithState> list, String dojo_uuid) {
        this.mContext = context;
        this.courseList = list;
        this.dojo_uuid = dojo_uuid;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final CourseModel courseModel = new CourseModel(courseList.get(position));

        try {
            if(courseList.get(position).state() == 0 ) {
                holder.state.setVisibility(View.VISIBLE);
                holder.state.setText("신청 중");
            } else if(courseList.get(position).state() == 1 ) {
                holder.state.setVisibility(View.VISIBLE);
                holder.state.setText("수강 중");
            }
        } catch (Exception e) {
            holder.state.setVisibility(View.INVISIBLE);
        }

        holder.name.setText(courseList.get(position).course_name());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EnrollCourse.class);

                intent.putExtra("courseinfo", courseModel);
                intent.putExtra("coursestate", courseList.get(position).state());
                intent.putExtra("dojo_uuid", dojo_uuid);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView name;
        TextView state;

        public ViewHolder(View itemView) {
            super(itemView);


            layout = itemView.findViewById(R.id.course_list_layout);
            name = itemView.findViewById(R.id.course_list_name);
            state = itemView.findViewById(R.id.course_list_state);

        }
    }
}

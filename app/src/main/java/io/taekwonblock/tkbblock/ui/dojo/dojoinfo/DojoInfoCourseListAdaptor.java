package io.taekwonblock.tkbblock.ui.dojo.dojoinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.CoursesQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.CourseModel;
import io.taekwonblock.tkbblock.ui.dojo.DojoListAdaptor;

public class DojoInfoCourseListAdaptor extends RecyclerView.Adapter<DojoInfoCourseListAdaptor.ViewHolder>{


    Context mContext;
    List<CoursesQuery.Course> courseList;

    public DojoInfoCourseListAdaptor(Context context, List<CoursesQuery.Course> courseList) {
        this.mContext = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dojoinfo_course, parent, false);
        return new DojoInfoCourseListAdaptor.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CourseModel courseModel = new CourseModel(courseList.get(position));

        holder.courseName.setText(courseList.get(position).course_name());
        holder.courseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseInfoActivity.class);

                intent.putExtra("courseinfo", courseModel);

                mContext.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView courseName;
        LinearLayout courseLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.dojoinfo_course_list_name);
            courseLayout = itemView.findViewById(R.id.dojoinfo_course_list_layout);

        }
    }
}

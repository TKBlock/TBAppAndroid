package io.taekwonblock.tkbblock.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.CoursesQuery;
import io.taekwonblock.tkbblock.CoursesWithStateQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.ui.dojo.dojoinfo.DojoInfoCourseListAdaptor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    TextView tvTitle;
    RecyclerView courseListView;

    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    String uuid;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mContext = this;


        applicationClass = (ApplicationClass)getApplicationContext();
        apolloClient = applicationClass.apolloClient();

        Intent intent = getIntent();
        uuid = intent.getStringExtra("uuid");

        SharedPreferences sharedpreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String user_uuid = sharedpreferences.getString("uuid", "");


        String name = intent.getStringExtra("name");

        tvTitle = findViewById(R.id.course_list_title);
        tvTitle.setText(name);

        courseListView = findViewById(R.id.course_list_listview);

        CoursesWithStateQuery coursesQuery = CoursesWithStateQuery.builder().dojo_uuid(uuid).user_uuid(user_uuid).build();
        apolloClient.query(coursesQuery).enqueue(new ApolloCall.Callback<CoursesWithStateQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<CoursesWithStateQuery.Data> response) {
                final List<CoursesWithStateQuery.CoursesWithState> courseList = response.data().coursesWithState();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        CourseListAdaptor courseListAdaptor = new CourseListAdaptor(mContext, courseList, uuid);
                        courseListView.setLayoutManager(layoutManager);
                        courseListView.setAdapter(courseListAdaptor);

                        courseListView.setVisibility(View.VISIBLE);

                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(courseListView.getContext(),
                                layoutManager.getOrientation());
                        courseListView.addItemDecoration(dividerItemDecoration);
                    }
                });


            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });

    }
}

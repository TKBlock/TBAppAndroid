package io.taekwonblock.tkbblock.ui.dojo.dojoinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.CoursesQuery;
import io.taekwonblock.tkbblock.DojoJoinStateQuery;
import io.taekwonblock.tkbblock.DojoRegStateQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.RegistrateDojoMutation;
import io.taekwonblock.tkbblock.RequestCareerMutation;
import io.taekwonblock.tkbblock.model.DojoModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class DojoInfoActivity extends AppCompatActivity {

    final static String TAG = "DojoInfoActivity";

    Context mContext;
    TextView tvTitle;
    TextView tvManager;
    TextView tvAddress;
    TextView tvPhone;
    TextView tvDescription;

    RecyclerView imageGallery;
    DojoInfoGalleryAdaptor dojoInfoGalleryAdaptor;


    TextView tvNoCourse;
    RecyclerView courseListView;

    Button btnSubmit;


    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dojo_info);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        applicationClass = (ApplicationClass) getApplicationContext();
        apolloClient = applicationClass.apolloClient();
        mContext = this;

        SharedPreferences sharedpreferences = mContext.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        final String uuid = sharedpreferences.getString("uuid", "");
        final int accountType = sharedpreferences.getInt("type", 0);

        Intent intent = getIntent();
        final DojoModel dojoModel = (DojoModel) intent.getSerializableExtra("data_model");


        tvTitle = findViewById(R.id.dojoinfo_title);
        tvTitle.setText(dojoModel.getDojo_name());

        tvManager = findViewById(R.id.dojoinfo_manager);
        tvManager.setText(dojoModel.getManager());

        tvAddress = findViewById(R.id.dojoinfo_address);
        tvAddress.setText(dojoModel.getAddress());

        tvPhone = findViewById(R.id.dojoinfo_phone);
        tvPhone.setText(dojoModel.getPhone());

        tvDescription = findViewById(R.id.dojoinfo_description);
        tvDescription.setText(dojoModel.getDescription());
        tvDescription.setMovementMethod(new ScrollingMovementMethod());

        courseListView = findViewById(R.id.dojoinfo_course_list);

        tvNoCourse = findViewById(R.id.dojoinfo_no_course);


        imageGallery = findViewById(R.id.dojoinfo_gallery);
        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        dojoInfoGalleryAdaptor = new DojoInfoGalleryAdaptor(this, dojoModel.getImages());
        imageGallery.setLayoutManager(layoutManagerHorizontal);
        imageGallery.setAdapter(dojoInfoGalleryAdaptor);

        btnSubmit = findViewById(R.id.dojoinfo_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Clicked");

                if(accountType == 1) {
                    RegistrateDojoMutation registrateDojoMutation = RegistrateDojoMutation.builder().dojo_uuid(dojoModel.getUuid()).user_uuid(uuid).build();
                    apolloClient.mutate(registrateDojoMutation).enqueue(new ApolloCall.Callback<RegistrateDojoMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<RegistrateDojoMutation.Data> response) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "신청되었습니다", Toast.LENGTH_SHORT).show();

                                }
                            });
                            finish();
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {

                        }
                    });

                } else if (accountType == 2) {
                    RequestCareerMutation requestCareerMutation = RequestCareerMutation.builder().dojo_uuid(dojoModel.getUuid()).user_uuid(uuid).build();
                    apolloClient.mutate(requestCareerMutation).enqueue(new ApolloCall.Callback<RequestCareerMutation.Data>() {
                        @Override
                        public void onResponse(@NotNull Response<RequestCareerMutation.Data> response) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "신청되었습니다", Toast.LENGTH_SHORT).show();

                                }
                            });
                            finish();
                        }

                        @Override
                        public void onFailure(@NotNull ApolloException e) {
                            e.printStackTrace();
                        }
                    });
                }


            }
        });

        if(accountType == 1) {
            DojoRegStateQuery dojoRegStateQuery = DojoRegStateQuery.builder().dojo_uuid(dojoModel.getUuid()).user_uuid(uuid).build();
            apolloClient.query(dojoRegStateQuery).enqueue(new ApolloCall.Callback<DojoRegStateQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<DojoRegStateQuery.Data> response) {

                    Log.v(TAG, response.data().dojoRegState().toString());
                    final int state = response.data().dojoRegState().state();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (state) {
                                case 0:
                                    btnSubmit.setEnabled(true);
                                    break;
                                case 1:
                                    btnSubmit.setEnabled(false);
                                    btnSubmit.setAlpha(0.5f);
                                    btnSubmit.setText("신청 중");
                                    break;
                                case 2:
                                    btnSubmit.setEnabled(false);
                                    btnSubmit.setAlpha(0.5f);
                                    btnSubmit.setText("수강 중");
                                    break;
                            }
                        }
                    });
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {

                }
            });

        } else if(accountType == 2) {

            DojoJoinStateQuery dojoJoinStateQuery = DojoJoinStateQuery.builder().dojo_uuid(dojoModel.getUuid()).user_uuid(uuid).build();
            apolloClient.query(dojoJoinStateQuery).enqueue(new ApolloCall.Callback<DojoJoinStateQuery.Data>() {
                @Override
                public void onResponse(@NotNull Response<DojoJoinStateQuery.Data> response) {

                    Log.v(TAG, response.data().dojoJoinState().toString());
                    final int state = response.data().dojoJoinState().state();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (state) {
                                case 0:
                                    btnSubmit.setEnabled(true);
                                    break;
                                case 1:
                                    btnSubmit.setEnabled(false);
                                    btnSubmit.setAlpha(0.5f);
                                    btnSubmit.setText("신청 중");
                                    break;
                                case 2:
                                    btnSubmit.setEnabled(false);
                                    btnSubmit.setAlpha(0.5f);
                                    btnSubmit.setText("지도 중");
                                    break;
                            }
                        }
                    });
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {

                }
            });
        }

        CoursesQuery coursesQuery = CoursesQuery.builder().dojo_uuid(dojoModel.getUuid()).build();
        apolloClient.query(coursesQuery).enqueue(new ApolloCall.Callback<CoursesQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<CoursesQuery.Data> response) {

                Log.v(TAG, response.data().courses().toString());
                final List<CoursesQuery.Course> courseList = response.data().courses();

                if(courseList.size() <= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvNoCourse.setVisibility(View.VISIBLE);
                        }
                    });
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            DojoInfoCourseListAdaptor dojoInfoCourseListAdaptor = new DojoInfoCourseListAdaptor(mContext, courseList);
                            courseListView.setLayoutManager(layoutManager);
                            courseListView.setAdapter(dojoInfoCourseListAdaptor);

                            courseListView.setVisibility(View.VISIBLE);

                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(courseListView.getContext(),
                                    layoutManager.getOrientation());
                            courseListView.addItemDecoration(dividerItemDecoration);
                        }
                    });



                }

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });



    }
}

package io.taekwonblock.tkbblock.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.ApplicationClass;
import io.taekwonblock.tkbblock.EnrollCourseMutation;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.CourseModel;
import io.taekwonblock.tkbblock.ui.dojo.dojoinfo.DojoInfoGalleryAdaptor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class EnrollCourse extends AppCompatActivity {

    CourseModel courseModel;
    int courseState;

    TextView tvManager;
    TextView tvDescription;

    RecyclerView imageGallery;
    DojoInfoGalleryAdaptor dojoInfoGalleryAdaptor;

    Button btnRequest;

    String dojo_uuid;
    String user_uuid;

    private ApplicationClass applicationClass;
    private ApolloClient apolloClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_course);

        applicationClass = (ApplicationClass)getApplicationContext();
        apolloClient = applicationClass.apolloClient();


        Intent intent = getIntent();
        courseModel = (CourseModel) intent.getSerializableExtra("courseinfo");
        dojo_uuid = intent.getStringExtra("dojo_uuid");
        courseState = intent.getIntExtra("coursestate", -1);

        SharedPreferences sharedpreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        user_uuid = sharedpreferences.getString("uuid", "");


        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(courseModel.getCourse_name());
        }

        tvManager = findViewById(R.id.enroll_manager);
        tvManager.setText(courseModel.getManager());

        tvDescription = findViewById(R.id.enroll_description);
        tvDescription.setText(courseModel.getDescription());

        imageGallery = findViewById(R.id.enroll_gallery);
        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        dojoInfoGalleryAdaptor = new DojoInfoGalleryAdaptor(this, courseModel.getImages());
        imageGallery.setLayoutManager(layoutManagerHorizontal);
        imageGallery.setAdapter(dojoInfoGalleryAdaptor);

        btnRequest = findViewById(R.id.enroll_btn_request);

        switch (courseState) {
            case 0:
                btnRequest.setEnabled(false);
                btnRequest.setText("신청 중");
                btnRequest.setAlpha(0.4f);
                break;
            case 1:
                btnRequest.setEnabled(false);
                btnRequest.setText("수강 중");
                btnRequest.setAlpha(0.4f);
                break;
            default:
                btnRequest.setEnabled(true);
                btnRequest.setText("신청");
                btnRequest.setAlpha(1);
        }

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnrollCourseMutation enrollCourseMutation = EnrollCourseMutation.builder().course_idx(courseModel.getIDX()).dojo_uuid(dojo_uuid).user_uuid(user_uuid).build();
                apolloClient.mutate(enrollCourseMutation).enqueue(new ApolloCall.Callback<EnrollCourseMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<EnrollCourseMutation.Data> response) {
                        Log.v("TAG", response.data().toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EnrollCourse.this, "신청되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        });

                        finish();
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
            }
        });
    }
}

package io.taekwonblock.tkbblock.ui.dojo.dojoinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.CourseModel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CourseInfoActivity extends AppCompatActivity {

    CourseModel courseModel;

    TextView tvManager;
    TextView tvDescription;

    RecyclerView imageGallery;
    DojoInfoGalleryAdaptor dojoInfoGalleryAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        Intent intent = getIntent();
        courseModel = (CourseModel) intent.getSerializableExtra("courseinfo");

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(courseModel.getCourse_name());
        }

        tvManager = findViewById(R.id.courseinfo_manager);
        tvManager.setText(courseModel.getManager());

        tvDescription = findViewById(R.id.courseinfo_description);
        tvDescription.setText(courseModel.getDescription());

        imageGallery = findViewById(R.id.courseinfo_gallery);
        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        dojoInfoGalleryAdaptor = new DojoInfoGalleryAdaptor(this, courseModel.getImages());
        imageGallery.setLayoutManager(layoutManagerHorizontal);
        imageGallery.setAdapter(dojoInfoGalleryAdaptor);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

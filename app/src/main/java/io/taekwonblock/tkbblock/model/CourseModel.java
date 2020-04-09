package io.taekwonblock.tkbblock.model;

import java.io.Serializable;
import java.util.List;

import io.taekwonblock.tkbblock.CoursesQuery;
import io.taekwonblock.tkbblock.CoursesWithStateQuery;

public class CourseModel implements Serializable {

    String course_name;
    String manager;
    String description;
    String IDX;

    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    List<String> images;

    public CourseModel(CoursesQuery.Course course) {

        this.course_name = course.course_name();
        this.manager = course.manager();
        this.description = course.description();
        this.images = course.images();
        this.IDX = course.IDX();

    }

    public CourseModel(CoursesWithStateQuery.CoursesWithState course) {

        this.course_name = course.course_name();
        this.manager = course.manager();
        this.description = course.description();
        this.images = course.images();
        this.IDX = course.IDX();

    }


}

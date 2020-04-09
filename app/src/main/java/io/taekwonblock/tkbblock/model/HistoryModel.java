package io.taekwonblock.tkbblock.model;

import android.util.Log;

import java.io.Serializable;

import io.taekwonblock.tkbblock.HistoriesForInstructorQuery;
import io.taekwonblock.tkbblock.HistoriesForStudentQuery;

public class HistoryModel implements Serializable {
    String dojoName;
    String courseName;
    Object startDate;
    Object endDate;

    public HistoryModel(HistoriesForStudentQuery.HistoriesForStudent history) {
        this.dojoName = history.dojo_name();
        this.courseName = history.course_name();
        this.startDate = history.start_date();
        this.endDate = history.end_date();
    }



    public HistoryModel(HistoriesForInstructorQuery.HistoriesForInstructor history) {
        this.dojoName = history.dojo_name();
        this.courseName = history.course_name();
        this.startDate = history.start_date();
        this.endDate = history.end_date();

        Log.v("HISTORYMODEL", history.start_date().toString());
        Log.v("HISTORYMODEL", this.startDate.toString());
    }

    public String getDojoName() {
        return dojoName;
    }

    public void setDojoName(String dojoName) {
        this.dojoName = dojoName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Object getStartDate() {
        return startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = endDate;
    }




//    dojo_name: String
//    course_name: String
//    start_date: DateTime
//    end_date: DateTime

}

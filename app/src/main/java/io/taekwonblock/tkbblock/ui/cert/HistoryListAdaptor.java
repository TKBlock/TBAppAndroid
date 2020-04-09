package io.taekwonblock.tkbblock.ui.cert;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.taekwonblock.tkbblock.EnrollmentForMobileQuery;
import io.taekwonblock.tkbblock.HistoriesForStudentQuery;
import io.taekwonblock.tkbblock.R;
import io.taekwonblock.tkbblock.model.HistoryModel;

public class HistoryListAdaptor extends RecyclerView.Adapter<HistoryListAdaptor.ViewHolder> {

    final static String TAG = "HistoryListAdaptor";

    List<HistoryModel> enrollmentList;
    Context mContext;

    public HistoryListAdaptor (Context context, List<HistoryModel> list ) {
        this.mContext = context;
        this.enrollmentList = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if( enrollmentList.get(position).getCourseName() == null) {
            holder.courseName.setText(enrollmentList.get(position).getDojoName());
            holder.dojoName.setVisibility(View.GONE);
        } else {
            holder.courseName.setText(enrollmentList.get(position).getCourseName());
            holder.dojoName.setText(enrollmentList.get(position).getDojoName());
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            String dtStart = enrollmentList.get(position).getStartDate().toString();
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(format.parse(dtStart));


            String endString = "";
            if(enrollmentList.get(position).getEndDate() != null) {
                String dtEnd = enrollmentList.get(position).getEndDate().toString();
                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.setTime(format.parse(dtEnd));
                endString = "" + calendarEnd.get(Calendar.YEAR) + '/' + (calendarEnd.get(Calendar.MONTH) + 1 ) + '/' + calendarEnd.get(Calendar.DAY_OF_MONTH);

            }


            if(endString == "") {
                endString = "현재";
            }


            String startString = "" + calendarStart.get(Calendar.YEAR) + '/' + (calendarStart.get(Calendar.MONTH) + 1 ) + '/' + calendarStart.get(Calendar.DAY_OF_MONTH);

            holder.duration.setText( startString + " ~ " + endString );
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return enrollmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        TextView dojoName;
        TextView duration;

        public ViewHolder(View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.history_item_coursename);
            dojoName = itemView.findViewById(R.id.history_item_dojoname);
            duration = itemView.findViewById(R.id.history_item_duration);
        }
    }
}

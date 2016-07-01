package com.ericsson.project.tescheduler.Tools;

        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.ericsson.project.tescheduler.Objects.AppData;
        import com.ericsson.project.tescheduler.Objects.ExceptionSchedule;
        import com.ericsson.project.tescheduler.R;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

public class ExceptionScheduleRVA
        extends RecyclerView.Adapter<ExceptionScheduleRVA.ExceptionScheduleViewHolder> {
    private List<ExceptionSchedule> specialDays;

    public class ExceptionScheduleViewHolder extends RecyclerView.ViewHolder{
        TextView textDate;
        TextView textTime;

        ExceptionScheduleViewHolder(final View itemView){
            super(itemView);
            textDate = (TextView) itemView.findViewById(R.id.specialday_date);
            textTime = (TextView) itemView.findViewById(R.id.specialday_time);
        }
    }

    public  ExceptionScheduleRVA(ArrayList<ExceptionSchedule> specialDays){
        this.specialDays = specialDays;
    }

    @Override
    public ExceptionScheduleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_info_specialdays, viewGroup, false);
        return new ExceptionScheduleViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return specialDays.size();
    }

    @Override
    public void onBindViewHolder(final ExceptionScheduleViewHolder exSchedVH, final int i) {
        exSchedVH.textDate.setText(formatDate(specialDays.get(i).getDay()));
        exSchedVH.textTime.setText(exSchedVH.itemView.getResources().getString(R.string.info_exceptiontime,
                formatTime(specialDays.get(i).getSchedule().getOpeningTime()),
                formatTime(specialDays.get(i).getSchedule().getClosingTime())));
    }

    private String formatDate(Calendar calendar){
        SimpleDateFormat dateFormat = new SimpleDateFormat(AppData.DATE_FORMAT_EXCEPTION, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String formatTime(Date time){
        final String DATE_FORMAT = "HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(time.getTime());
    }
}


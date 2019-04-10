package com.rishabh.customizedcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class CalendarAdapter extends ArrayAdapter<Calendar> {
  private LayoutInflater inflater;
  private ArrayList<Calendar> days;
  private int month;
  private int dateTextColor;
  private long startDate, endDate;
  private CalendarView.TRAVELTYPE traveltype;

  public CalendarAdapter(Context context, ArrayList<Calendar> days, Calendar currentDate,
      int dateTextColor, long startDate, long endDate, CalendarView.TRAVELTYPE traveltype) {
    super(context, R.layout.control_calendar_day, days);
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.days = days;
    this.startDate = startDate;
    this.endDate = endDate;
    this.dateTextColor = dateTextColor;
    this.traveltype = traveltype;
    month = currentDate.get(Calendar.MONTH);
  }

  @Nullable @Override public Calendar getItem(int position) {
    return days.get(position);
  }

  @SuppressLint({ "InflateParams", "SimpleDateFormat" }) @NonNull @Override
  public View getView(int position, View view, @NonNull ViewGroup parent) {
    final Calendar calendar = getItem(position);
    ViewHolder viewHolder = null;
    if (view == null) {
      view = inflater.inflate(R.layout.control_calendar_day, null);
      viewHolder = new ViewHolder();
      viewHolder.date = view.findViewById(R.id.date);
      viewHolder.parent = view.findViewById(R.id.parent);
      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) view.getTag();
    }
    if (calendar.getTimeInMillis() < Util.getCurrentDate().getTimeInMillis()) {
      viewHolder.date.setTextColor(getContext().getResources().getColor(R.color.disable_color));
      viewHolder.parent.setEnabled(false);
    } else {
      if (startDate == calendar.getTimeInMillis() || endDate == calendar.getTimeInMillis()) {
        viewHolder.date.setTextColor(getContext().getResources().getColor(R.color.white));
      } else {
        viewHolder.date.setTextColor(dateTextColor);
      }
      viewHolder.parent.setEnabled(true);
    }
    if (month == calendar.get(Calendar.MONTH)) {
      viewHolder.date.setVisibility(VISIBLE);
      viewHolder.date.setText(
          (new SimpleDateFormat("dd")).format(new Date(calendar.getTimeInMillis())));
      if (startDate == calendar.getTimeInMillis()) {
        viewHolder.parent.setBackgroundColor(
            getContext().getResources().getColor(R.color.colorPrimary));
      } else {
        if (calendar.getTimeInMillis() > startDate && calendar.getTimeInMillis() < endDate) {
          viewHolder.parent.setBackgroundColor(
              getContext().getResources().getColor(R.color.dark_transparent));
        } else if (endDate == calendar.getTimeInMillis()) {
          viewHolder.parent.setBackgroundColor(
              getContext().getResources().getColor(R.color.colorPrimary));
        } else {
          viewHolder.parent.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        }
      }
    } else {
      viewHolder.parent.setBackgroundColor(getContext().getResources().getColor(R.color.white));
      viewHolder.date.setVisibility(INVISIBLE);
    }

    viewHolder.parent.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (traveltype == CalendarView.TRAVELTYPE.DEPARTURE) {
          endDate = 0;
          startDate = calendar.getTimeInMillis();
        } else {
          if (calendar.getTimeInMillis() <= startDate) {
            startDate = endDate = calendar.getTimeInMillis();
          } else {
            endDate = calendar.getTimeInMillis();
          }
        }
        notifyDataSetChanged();
      }
    });
    return view;
  }

  private class ViewHolder {
    TextView date;
    LinearLayout parent;
  }

  public long getStartDate() {
    return startDate;
  }

  public long getEndDate() {
    return endDate;
  }
}

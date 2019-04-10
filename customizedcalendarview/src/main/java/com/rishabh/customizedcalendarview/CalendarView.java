package com.rishabh.customizedcalendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarView extends LinearLayout {
  private static final int DAYS_COUNT = 42; // how many days to show, defaults to six weeks, 42 days

  @BindView(R2.id.parent_layout) LinearLayout parentLayout;
  @BindView(R2.id.calendar_prev_button) ImageView btnPrev;
  @BindView(R2.id.calendar_next_button) ImageView btnNext;
  @BindView(R2.id.calendar_date_display) TextView txtDate;
  @BindView(R2.id.calendar_grid) GridView grid;
  @BindView(R2.id.sunday) TextView sun;
  @BindView(R2.id.monday) TextView mon;
  @BindView(R2.id.tuesday) TextView tue;
  @BindView(R2.id.wednesday) TextView wed;
  @BindView(R2.id.thursday) TextView thu;
  @BindView(R2.id.friday) TextView fri;
  @BindView(R2.id.saturday) TextView sat;
  @BindView(R2.id.button_done) TextView buttonDone;

  private MonthChangeListener monthChangeListener;
  private StartDateEndDateListener startDateEndDateListener;
  private Context context;
  private Calendar currentDate = Calendar.getInstance();
  private int swipeCount;
  private int backgroundColor;
  private String dateFormat;
  private static final String DATE_FORMAT = "MMMM";
  private int tintColor;
  private int weekDayColor;
  private int monthTextColor;
  private float alpha;
  private int dateTextColor;
  private int doneTextColor;
  private int doneBgColor;
  private boolean futureDates = true;
  private boolean pastDates = true;
  private CalendarAdapter calendarAdapter;
  private long startDate, endDate;
  private TRAVELTYPE traveltype;

  public CalendarView(Context context) {
    super(context);
  }

  public CalendarView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    initControl(context, attrs);
  }

  public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    initControl(context, attrs);
  }

  /**
   * Load control xml layout
   */
  private void initControl(Context context, AttributeSet attrs) {
    LayoutInflater inflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.calendar_view, this);
    ButterKnife.bind(this, view);
    this.context = context;
    loadAttribute(attrs);
    assignUiElements();
    assignClickHandlers();
  }

  private void loadAttribute(AttributeSet attrs) {
    TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);
    try {
      dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
      backgroundColor = ta.getColor(R.styleable.CalendarView_background_color,
          getResources().getColor(R.color.colorPrimary));
      tintColor = ta.getColor(R.styleable.CalendarView_tint_color, Color.WHITE);
      weekDayColor = ta.getColor(R.styleable.CalendarView_week_day_color,
          getResources().getColor(R.color.color_grey_79));
      monthTextColor = ta.getColor(R.styleable.CalendarView_month_text_color, Color.WHITE);
      alpha = ta.getFloat(R.styleable.CalendarView_alpha, 0f);
      futureDates = ta.getBoolean(R.styleable.CalendarView_future_dates, true);
      pastDates = ta.getBoolean(R.styleable.CalendarView_past_dates, true);
      dateTextColor = ta.getColor(R.styleable.CalendarView_date_text_color, Color.WHITE);
      doneTextColor = ta.getColor(R.styleable.CalendarView_done_text_color, Color.WHITE);
      doneBgColor = ta.getColor(R.styleable.CalendarView_done_bg_color,
          getContext().getResources().getColor(R.color.colorPrimary));
      if (dateFormat == null) dateFormat = DATE_FORMAT;
    } finally {
      ta.recycle();
    }
  }

  private void assignUiElements() {
    parentLayout.setBackgroundColor(backgroundColor);
    txtDate.setTextColor(monthTextColor);
    btnPrev.setColorFilter(tintColor);
    btnNext.setColorFilter(tintColor);
    buttonDone.setTextColor(doneTextColor);
    //buttonDone.setBackgroundColor(doneBgColor);

    sun.setTextColor(weekDayColor);
    sun.setAlpha(alpha);

    mon.setTextColor(weekDayColor);
    mon.setAlpha(alpha);

    tue.setTextColor(weekDayColor);
    tue.setAlpha(alpha);

    wed.setTextColor(weekDayColor);
    wed.setAlpha(alpha);

    thu.setTextColor(weekDayColor);
    thu.setAlpha(alpha);

    fri.setTextColor(weekDayColor);
    fri.setAlpha(alpha);

    sat.setTextColor(weekDayColor);
    sat.setAlpha(alpha);
  }

  private void assignClickHandlers() {
    btnNext.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (futureDates || swipeCount >= 0) {
          currentDate.add(Calendar.MONTH, 1);
          swipeCount = swipeCount + 1;
          monthChangeListener.swipeCount(swipeCount);
          setUpdateCalendar(calendarAdapter.getStartDate(), calendarAdapter.getEndDate(),
              traveltype);
        }
      }
    });

    btnPrev.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        if (pastDates || swipeCount > 0) {
          currentDate.add(Calendar.MONTH, -1);
          swipeCount = swipeCount - 1;
          monthChangeListener.swipeCount(swipeCount);
          setUpdateCalendar(calendarAdapter.getStartDate(), calendarAdapter.getEndDate(),
              traveltype);
        }
      }
    });

    buttonDone.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View view) {
        if (startDateEndDateListener != null) {
          startDateEndDateListener.getDates(calendarAdapter.getStartDate(),
              calendarAdapter.getEndDate());
        }
      }
    });
  }

  public void setUpdateCalendar(long startDate, long endDate, TRAVELTYPE traveltype) {
    ArrayList<Calendar> cells = new ArrayList<>();
    this.startDate = startDate;
    this.endDate = endDate;
    this.traveltype = traveltype;
    //currentDate.setTimeInMillis(startDate);
    Calendar calendar = (Calendar) currentDate.clone();
    Util.setDayStart(calendar);
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);
    while (cells.size() < DAYS_COUNT) {
      cells.add((Calendar) calendar.clone());
      calendar.add(Calendar.DAY_OF_MONTH, 1);
    }
    calendarAdapter =
        new CalendarAdapter(getContext(), cells, currentDate, dateTextColor, startDate, endDate,
            traveltype);
    grid.setAdapter(calendarAdapter);
    txtDate.setText(Util.monthYearNameBySwipeIndex(swipeCount, dateFormat));
  }

  public void setMonthChangeListener(MonthChangeListener monthChangeListener) {
    this.monthChangeListener = monthChangeListener;
  }

  public void setStartDateEndDateListener(StartDateEndDateListener startDateEndDateListener) {
    this.startDateEndDateListener = startDateEndDateListener;
  }

  public enum TRAVELTYPE {
    DEPARTURE, RETURN
  }
}

package com.pongal.paid.seinfeld;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.pongal.paid.seinfeld.DateState.Status;
import com.pongal.paid.seinfeld.data.Constants;
import com.pongal.paid.seinfeld.data.Date;
import com.pongal.paid.seinfeld.data.Task;
import com.pongal.paid.seinfeld.db.DBManager;
import com.pongal.paid.seinfeld.homescreen.HomeScreenWidgetProvider;

public class CalendarActivity extends Activity {
    DBManager dbManager;

    Task task;
    CalendarView calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	initDBManager();
	final int taskId = getIntent().getExtras().getInt("taskId");
	Log.d(Constants.LogTag, "Refresh for id: " + taskId);
	task = dbManager.getTaskDetails(taskId);

	setContentView(R.layout.main);
	LinearLayout wrapper = (LinearLayout) findViewById(R.id.wrapper);
	wrapper.setOrientation(LinearLayout.VERTICAL);

	calendar = new CalendarView(getApplicationContext());
	calendar.setTask(task);
	calendar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	// disableDates(calendar);
	wrapper.addView(calendar);

	calendar.addSelectHandler(getCalendarSelectHandler());
	calendar.addNotesChangeListener(getNotesActionListener());
    }   
    

    @Override
    protected void onResume() {
	super.onResume();
	task = dbManager.getTaskDetails(task.getId());
	calendar.setTask(task);
    }

    private void initDBManager() {
	if (dbManager == null) {
	    dbManager = new DBManager(getApplicationContext());
	}
    }

    private OnEditorActionListener getNotesActionListener() {
	return new OnEditorActionListener() {
	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		Log.d(Constants.LogTag, (actionId == EditorInfo.IME_ACTION_DONE) + "");
		if (actionId == EditorInfo.IME_ACTION_DONE) {
		    updateNotes(calendar.getDisplayedMonth(), v.getText().toString());
		    return true;
		}
		return false;
	    }
	};
    }

    private void updateNotes(Date month, String notes) {
	dbManager.updateNotes(task.getId(), calendar.getDisplayedMonth(), notes);
	task.getNotes().put(month, notes);
    }

    public CalendarSelectHandler getCalendarSelectHandler() {
	return new CalendarSelectHandler() {
	    public void onChange(DateState e) {
		if (e.getStatus() == Status.SELECTED) {
		    task.addAccomplishedDates(e.getDate());
		    dbManager.updateTaskCalendar(task.getId(), e.getDate(), true);
		} else {
		    task.removeAccomplishedDates(e.getDate());
		    dbManager.updateTaskCalendar(task.getId(), e.getDate(), false);
		}
		sendBroadcast(Util.getBroadcast(task, HomeScreenWidgetProvider.ACTION_REFRESH));
	    }
	};
    }


    @Override
    protected void onDestroy() {
	super.onDestroy();
	dbManager.close();
    }

    @Override
    public void onBackPressed() {
	updateNotes(calendar.getDisplayedMonth(), calendar.getNotes());
	super.onBackPressed();
    }

}
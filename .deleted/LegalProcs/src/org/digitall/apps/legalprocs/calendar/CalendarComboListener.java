package org.digitall.apps.legalprocs.calendar;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.Observable;

public class CalendarComboListener extends Observable implements ItemListener {

    CalendarComboListener(CalendarFile calendar) {
	this.calendar = calendar;
    }

    public void itemStateChanged(ItemEvent e) {
	if (e.getStateChange() == 1) {
	    calendar.initializeCalendar();
	    calendar.updateUI();
	    calendar.parentFrame.findExpedientes();
	}
    }
    CalendarFile calendar;

}

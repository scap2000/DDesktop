package org.digitall.apps.legalprocs.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Calendar;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import org.digitall.lib.calendar.JDayButton;
import org.digitall.lib.calendar.JDayLabel;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.environment.Environment;

//

public class CalendarFile extends JPanel {

    /** Configuration **/
    private Color colorCalendar = Color.white;
    private Color colorHeader = new Color(231, 231, 231);
    private Color colorSunday = Color.red;
    private Color colorTextWeek = Color.black;
    private Color colorButtonWeek = new Color(231, 231, 231);
    private Color colorSaturday = Color.blue;
    private Color colorBorder = Color.red;
    private Color colorBorderToday = Color.orange;
    private Color colorTextToday = Color.black;
    int firstDay = 1;
    /** END Configuration **/
    private int startYear = 1901;
    private int endYear = 2099;
    private BasicPanel centerPanel;
    private BasicPanel daysPanel;
    int days[][];
    int week[];
    int currentDay = 1;
    int currentYear = 2007;
    int currentMonth = 1;
    private BasicCombo cbYear;
    private BasicCombo cbMonth;
    private String[] arrayDays = new String[7];
    private Vector vectorButtonDays;
    private Set hashAlarmDays;
    private Color colorAlarm;
    public FilesCalendar parentFrame;
    private String selectedMonthText;
    private int selectedMonthValue;
    private int selectedYear;
    ButtonGroup buttonWeekGroup;

    public CalendarFile(int _Year, int _Month, int _Day, int _DayOfWeekNumber, FilesCalendar _parent) {
	try {
	    parentFrame = _parent;
	    currentYear = _Year;
	    currentMonth = _Month;
	    currentDay = _Day;
	    initializeVariables(currentYear, currentMonth - 1);
	    initializeCalendar();
	    setToday(_DayOfWeekNumber);
	    JDayButton btnDayTmp = (JDayButton)vectorButtonDays.elementAt(0);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void initializeVariables(int Year, int Month) {
	cbYear = createYearCombo();
	cbYear.setFont(cbYear.getFont().deriveFont(1, 11F));
	cbYear.setSelectedItem(new Integer(Year));
	cbYear.addItemListener(new CalendarComboListener(this));
	cbMonth = createMonthCombo();
	cbMonth.setFont(cbMonth.getFont().deriveFont(1, 11F));
	cbMonth.setSelectedIndex(Month);
	cbMonth.addItemListener(new CalendarComboListener(this));
	BasicPanel datePanel = new BasicPanel();
	datePanel.setLayout(new BorderLayout());
	datePanel.add(cbYear, "East");
	datePanel.add(cbMonth, "Center");
	centerPanel = new BasicPanel();
	centerPanel.setLayout(new BorderLayout(5, 5));
	centerPanel.add(datePanel, "North");
	centerPanel.setBackground(colorCalendar);
	this.add(centerPanel);
	this.setBorder(BorderFactory.createLineBorder(colorBorder));
	this.setBackground(colorCalendar);
    }

    protected final void initializeCalendar() {
	if (daysPanel != null)
	    centerPanel.remove(daysPanel);
	selectedYear = ((Integer)cbYear.getSelectedItem()).intValue();
	selectedMonthValue = cbMonth.getSelectedIndex();
	selectedMonthText = (String)cbMonth.getSelectedItem();
	days = createCalendarDays(selectedYear, selectedMonthValue);
	createDaysPanel();
	markToday();
	String alarmDateParam = " EXTRACT(YEAR FROM alarmdate)='" + selectedYear + "' AND EXTRACT(MONTH FROM alarmdate)='" + (selectedMonthValue + 1) + "'";
	parentFrame.setAlarmDateParam(alarmDateParam);
	centerPanel.add(daysPanel, "Center");
    }

    private final int[][] createCalendarDays(int year, int month) {
	boolean calendarCompleted = false;
	days = new int[7][6];
	week = new int[6];
	Calendar calendar = Calendar.getInstance();
	calendar.set(year, month, 1);
	int number = calendar.get(7);
	int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
	int k = 1;
	for (int j = 0; j < 6; j++) {
	    int i = 0;
	    if (j == 0)
		if (firstDay == 0) {
		    i = number - 1;
		} else {
		    i = number - 2;
		    if (i < 0)
			i = 6;
		}
	    week[j] = weekNumber++;
	    for (; i < 7; i++) {
		days[i][j] = k;
		if (month <= 6) {
		    if (month % 2 == 0) {
			if (k == 31) {
			    calendarCompleted = true;
			    break;
			}
		    } else if (month == 1) {
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
			    if (k == 29) {
				calendarCompleted = true;
				break;
			    }
			} else if (k == 28) {
			    calendarCompleted = true;
			    break;
			}
		    } else if (k == 30) {
			calendarCompleted = true;
			break;
		    }
		} else if (month % 2 == 0) {
		    if (k == 30) {
			calendarCompleted = true;
			break;
		    }
		} else if (k == 31) {
		    calendarCompleted = true;
		    break;
		}
		k++;
	    }
	    if (calendarCompleted)
		break;
	}
	return days;
    }

    private final void HeaderDays() {
	JDayLabel weekNumber = new JDayLabel(Environment.lang.getProperty("shortWeek"));
	weekNumber.setForeground(colorTextWeek);
	JDayLabel sunday = new JDayLabel(Environment.lang.getProperty("shortSunday"));
	sunday.setForeground(colorSunday);
	arrayDays[0] = Environment.lang.getProperty("Sunday");
	JDayLabel monday = new JDayLabel(Environment.lang.getProperty("shortMonday"));
	arrayDays[1] = Environment.lang.getProperty("Monday");
	JDayLabel tuesday = new JDayLabel(Environment.lang.getProperty("shortTuesday"));
	arrayDays[2] = Environment.lang.getProperty("Tuesday");
	JDayLabel wednesday = new JDayLabel(Environment.lang.getProperty("shortWednesday"));
	arrayDays[3] = Environment.lang.getProperty("Wednesday");
	JDayLabel thursday = new JDayLabel(Environment.lang.getProperty("shortThursday"));
	arrayDays[4] = Environment.lang.getProperty("Thursday");
	JDayLabel friday = new JDayLabel(Environment.lang.getProperty("shortFriday"));
	arrayDays[5] = Environment.lang.getProperty("Friday");
	JDayLabel saturday = new JDayLabel(Environment.lang.getProperty("shortSaturday"));
	saturday.setForeground(colorSaturday);
	arrayDays[6] = Environment.lang.getProperty("Saturday");
	daysPanel.add(weekNumber);
	if (firstDay == 0) {
	    daysPanel.add(sunday);
	    daysPanel.add(monday);
	    daysPanel.add(tuesday);
	    daysPanel.add(wednesday);
	    daysPanel.add(thursday);
	    daysPanel.add(friday);
	    daysPanel.add(saturday);
	} else {
	    daysPanel.add(monday);
	    daysPanel.add(tuesday);
	    daysPanel.add(wednesday);
	    daysPanel.add(thursday);
	    daysPanel.add(friday);
	    daysPanel.add(saturday);
	    daysPanel.add(sunday);
	}
    }

    private final void createDaysPanel() {
	daysPanel = new BasicPanel();
	daysPanel.setBackground(colorHeader);
	daysPanel.setLayout(new GridLayout(7, 8));
	HeaderDays();
	ButtonGroup buttonGroup = new ButtonGroup();
	buttonWeekGroup = new ButtonGroup();
	vectorButtonDays = new Vector();
	for (int i = 0; i < 6; i++) {
	    if (week[i] != 0) {
		JDayButton weekButton = new JDayButton((new Integer(week[i])).toString());
		weekButton.setBackground(colorButtonWeek);
		weekButton.setForeground(colorTextWeek);
		daysPanel.add(weekButton);
		weekButton.addMouseListener(new MouseAdapter() {

				  public void mouseClicked(MouseEvent e) {
				      JDayButton btnWeek = (JDayButton)e.getSource();
				      String selectedWeek = btnWeek.getText();
				      String query = "SELECT " + " priority_tabs.idpriority_tab," + " (SELECT COUNT(*) FROM file.alarms WHERE alarms.idpriority_tab = priority_tabs.idpriority_tab AND" + " EXTRACT(WEEK FROM alarmdate)='" + selectedWeek + "') AS totalfiles " + "FROM " + " tabs.priority_tabs " + "ORDER BY" + " idpriority_tab";
				      parentFrame.setTotalFilesByPriority(query);
				      String queryTotal = "SELECT " + "   COUNT(distinct(idfile))  AS totalFiles " + "FROM " + "   file.alarms " + "WHERE " + "   EXTRACT(WEEK FROM alarmdate)='" + selectedWeek + "'";
				      parentFrame.setTotalFiles(queryTotal);
				      parentFrame.setTotalTextByWeek(selectedWeek);
				      parentFrame.setAlarmDateParam(" EXTRACT(WEEK FROM alarmdate)='" + selectedWeek + "'");
				  }

			      }
		);
		buttonWeekGroup.add(weekButton);
	    } else {
		JDayButton weekButton = new JDayButton();
		weekButton.setBackground(colorCalendar);
		daysPanel.add(weekButton);
	    }
	    for (int k = 0; k < 7; k++) {
		if (days[k][i] == 0) {
		    JDayButton dayButton = new JDayButton();
		    dayButton.setBackground(colorCalendar);
		    daysPanel.add(dayButton);
		} else {
		    JDayButton dayButton = new JDayButton("dia", "mes", "anio", (new Integer(days[k][i])).toString());
		    dayButton.setFont(new Font("Default", 0, 10));
		    dayButton.setBackground(colorCalendar);
		    vectorButtonDays.addElement(dayButton);
		    dayButton.addMouseListener(new MouseAdapter() {

				     public void mouseClicked(MouseEvent e) {
					 JDayButton btnDay = (JDayButton)e.getSource();
					 String selectedDay = btnDay.getText();
					 String selectedMonth = String.valueOf(cbMonth.getSelectedIndex() + 1);
					 String alarmDate = cbYear.getSelectedItem() + "-" + selectedMonth + "-" + selectedDay;
					 String query = "SELECT " + " priority_tabs.idpriority_tab," + " (SELECT COUNT(*) FROM file.alarms WHERE alarms.idpriority_tab = priority_tabs.idpriority_tab AND" + " alarmdate=date('" + alarmDate + "')) AS totalfiles " + "FROM " + " tabs.priority_tabs " + "ORDER BY" + " idpriority_tab";
					 System.out.println(query);
					 parentFrame.setTotalFilesByPriority(query);
					 String queryTotal = "SELECT " + "   COUNT(distinct(idfile))  AS totalFiles " + "FROM " + "   file.alarms " + "WHERE " + "   alarmdate=date('" + alarmDate + "')";
					 parentFrame.setTotalFiles(queryTotal);
					 parentFrame.setTotalTextByDay(selectedDay);
					 parentFrame.setAlarmDateParam(" alarmdate=date('" + alarmDate + "')");
				     }

				 }
		    );
		    /** Set color day: Saturday & Sunday **/
		    if (firstDay == 0) {
			if (k == 6) {
			    dayButton.setForeground(colorSaturday);
			} else if (k == 0) {
			    dayButton.setForeground(colorSunday);
			}
		    } else {
			if (k == 5) {
			    dayButton.setForeground(colorSaturday);
			} else if (k == 6) {
			    dayButton.setForeground(colorSunday);
			}
		    }
		    buttonGroup.add(dayButton);
		    daysPanel.add(dayButton);
		}
	    }
	}
    }

    private final BasicCombo createYearCombo() {
	BasicCombo combo = new BasicCombo();
	combo.setBackground(colorHeader);
	for (int i = startYear; i <= endYear; i++)
	    combo.addItem(new Integer(i));
	return combo;
    }

    private final BasicCombo createMonthCombo() {
	BasicCombo list = new BasicCombo();
	list.setBackground(colorHeader);
	list.addItem(Environment.lang.getProperty("January"));
	list.addItem(Environment.lang.getProperty("February"));
	list.addItem(Environment.lang.getProperty("March"));
	list.addItem(Environment.lang.getProperty("April"));
	list.addItem(Environment.lang.getProperty("May"));
	list.addItem(Environment.lang.getProperty("June"));
	list.addItem(Environment.lang.getProperty("July"));
	list.addItem(Environment.lang.getProperty("August"));
	list.addItem(Environment.lang.getProperty("September"));
	list.addItem(Environment.lang.getProperty("October"));
	list.addItem(Environment.lang.getProperty("November"));
	list.addItem(Environment.lang.getProperty("December"));
	return list;
    }

    private void setToday(int _dayOfWeekNumber) {
	String day = arrayDays[_dayOfWeekNumber];
	String month = (String)cbMonth.getSelectedItem();
	String dateMask = Environment.lang.getProperty("DateMask");
	dateMask = dateMask.replaceFirst("day", day);
	dateMask = dateMask.replaceFirst("month", month);
	dateMask = dateMask.replaceFirst("dd", String.valueOf(currentDay));
	dateMask = dateMask.replaceFirst("yyyy", String.valueOf(currentYear));
	String textDateToday = Environment.lang.getProperty("Today") + ": " + dateMask;
	BasicButton btnDateToday = new BasicButton(textDateToday);
	btnDateToday.setBorder(BorderFactory.createLineBorder(colorBorderToday, 0));
	btnDateToday.setBackground(colorCalendar);
	btnDateToday.setForeground(Color.BLACK);
	btnDateToday.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnDateToday.addMouseListener(new MouseAdapter() {

				   public void mouseClicked(MouseEvent e) {
				       cbMonth.setSelectedIndex(currentMonth - 1);
				       cbYear.setSelectedItem(new Integer(currentYear));
				   }

			       }
	);
	/*patch
	BasicPanel panelDateToday = new BasicPanel();
	endpatch*/
	JPanel panelDateToday = new JPanel();
	BasicLabel labelDay = new BasicLabel();
	panelDateToday.setBounds(new Rectangle(45, 315, 240, 30));
	panelDateToday.setBackground(colorCalendar);
	labelDay.setText("    ");
	labelDay.setBorder(BorderFactory.createLineBorder(colorBorderToday, 3));
	panelDateToday.add(labelDay, null);
	panelDateToday.add(btnDateToday, null);
	this.add(panelDateToday, "Center");
    }

    private void markToday() {
	if (currentMonth == cbMonth.getSelectedIndex() + 1 && currentYear == ((Integer)cbYear.getSelectedItem()).intValue()) {
	    JDayButton btnToday = (JDayButton)vectorButtonDays.elementAt(currentDay - 1);
	    btnToday.setBorder(BorderFactory.createLineBorder(colorBorderToday, 3));
	    btnToday.setFont(new Font("Default", Font.BOLD, 11));
	    btnToday.setForeground(colorTextToday);
	}
    }

    public void clearColorDay() {
	for (int i = 0; i < vectorButtonDays.size(); i++) {
	    JDayButton btnDay = (JDayButton)vectorButtonDays.elementAt(i);
	    btnDay.setBackground(colorCalendar);
	    if (!btnDay.getForeground().equals(colorSaturday) && !btnDay.getForeground().equals(colorSunday)) {
		if (i + 1 != currentDay)
		    btnDay.setForeground(Color.black);
	    }
	}
    }

    public Vector getVectorButtonDays() {
	return vectorButtonDays;
    }

    public void setToolTipANDColorDay() {
	if (hashAlarmDays != null) {
	    Object[] vector = hashAlarmDays.toArray();
	    for (int i = 0; i < vector.length; i++) {
		String monthyearSelected = (cbMonth.getSelectedIndex() + 1) + "-" + cbYear.getSelectedItem();
		String valueDay = (String)vector[i];
		if (valueDay.indexOf(monthyearSelected) != -1) {
		    String[] vectorParam = valueDay.split("##");
		    int day = Integer.parseInt(vectorParam[0].split("-")[0]);
		    String monthyear = vectorParam[0].split("-")[1] + "-" + vectorParam[0].split("-")[2];
		    if (monthyear.equals(monthyearSelected)) {
			JDayButton btnDay = (JDayButton)vectorButtonDays.elementAt(day - 1);
			String textToolTip = vectorParam[1];
			btnDay.setToolTipText(textToolTip);
			btnDay.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnDay.setBackground(colorAlarm);
			if ((colorAlarm.equals(Color.black) || colorAlarm.equals(Color.blue)) && !btnDay.getForeground().equals(colorSunday))
			    btnDay.setForeground(Color.white);
		    }
		}
	    }
	}
    }

    public void setColorAlarm(Color _colorAlarm) {
	colorAlarm = _colorAlarm;
    }

    public void setHashAlarmDays(Set _hashAlarmDays) {
	hashAlarmDays = _hashAlarmDays;
    }

    public int getSelectedMonthValue() {
	return selectedMonthValue + 1;
    }

    public int getSelectedYear() {
	return selectedYear;
    }

    public String getSelectedMonthText() {
	return selectedMonthText;
    }

}

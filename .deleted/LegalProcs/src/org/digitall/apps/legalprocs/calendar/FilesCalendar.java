package org.digitall.apps.legalprocs.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;

//

public class FilesCalendar extends JInternalFrame {

    private CalendarFile panelCalendar;
    private FindButton btnFind = new FindButton();
    private BasicContainerPanel jPanel1 = new BasicContainerPanel();
    private BasicLabel jLabel1 = new BasicLabel();
    private BasicLabel lblTotal = new BasicLabel();
    private BasicPanel jPanel2 = new BasicPanel();
    private BasicButton btnColorFilesExpired = new BasicButton();
    private BasicButton btnColorFilesOk = new BasicButton();
    private BasicButton btnColorFilesWarned = new BasicButton();
    private BasicButton btnColorFilesInDanger = new BasicButton();
    private BasicButton btnColorFilesCustom = new BasicButton();
    private BasicButton btnTotalFile = new BasicButton();
    private BasicTextField tfQtyOkDays = new BasicTextField();
    private BasicTextField tfQtyWarnedDays = new BasicTextField();
    private BasicTextField tfQtyDangerDays = new BasicTextField();
    private BasicTextField tfQtyExpiredDays = new BasicTextField();
    private BasicTextField tfQtyCustomDays = new BasicTextField();
    private BasicLabel lblWarnedDay = new BasicLabel();
    private BasicLabel lblDangerDay = new BasicLabel();
    private BasicLabel lblExpiredDay = new BasicLabel();
    private BasicLabel lblOkDay = new BasicLabel();
    private BasicLabel lblCustomDay = new BasicLabel();
    private BasicPanel jPanel3 = new BasicPanel();
    private JDesktopPane desktopParent;
    private BasicPanel jPanel4 = new BasicPanel();
    private BasicLabel jLabel3 = new BasicLabel();
    private BasicTextField tfTotal = new BasicTextField();
    private String selectedIdPriorities = "";
    private String alarmDateParam;
    private FilesAlarmList expiredFileList;
    private FilesAlarmList dangerFileList;
    private FilesAlarmList warnedFileList;
    private FilesAlarmList okFileList;
    private FilesAlarmList customFileList;
    private BasicCheckBox[] chkVector = new BasicCheckBox[6];
    private BasicCheckBox chkExpired = new BasicCheckBox();
    private BasicCheckBox chkDanger = new BasicCheckBox();
    private BasicCheckBox chkWarned = new BasicCheckBox();
    private BasicCheckBox chkOk = new BasicCheckBox();
    private BasicCheckBox chkCustom = new BasicCheckBox();
    private BasicCheckBox chkTotal = new BasicCheckBox();
    private BasicLabel jLabel2 = new BasicLabel(IconTypes.CRFileBlack_16x16);
    private BasicLabel jLabel4 = new BasicLabel(IconTypes.CRFileRed_16x16);
    private BasicLabel jLabel5 = new BasicLabel(IconTypes.CRFileYellow_16x16);
    private BasicLabel jLabel6 = new BasicLabel(IconTypes.CRFileGreen_16x16);
    private BasicLabel jLabel7 = new BasicLabel(IconTypes.CRFileBlue_16x16);
    private BasicLabel jLabel8 = new BasicLabel(IconTypes.CRFileTotal_16x16);

    public FilesCalendar() {
	try {
	    desktopParent = Environment.getDesktop("MAIN");
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(326, 677));
	this.setTitle("Calendario de Expedientes");
	this.setIconifiable(true);
	this.setClosable(true);
	this.setFrameIcon(IconTypes.calendarIcon_16x16);
	btnFind.setBounds(new Rectangle(210, 20, 80, 25));
	btnFind.setText("Buscar");
	btnFind.setMargin(new Insets(1, 1, 1, 1));
	btnFind.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnFind_actionPerformed(e);
			       }

			   }
	);
	jPanel1.setLayout(null);
	jLabel1.setText("Tipo de Alarma:");
	jLabel1.setSize(new Dimension(90, 14));
	jLabel1.setBounds(new Rectangle(20, 45, 95, 15));
	jLabel1.setFont(new Font("Default", 0, 11));
	jLabel1.setOpaque(false);
	lblTotal.setText("Tipos de Alarmas");
	lblTotal.setBounds(new Rectangle(15, 375, 124, 15));
	lblTotal.setOpaque(false);
	lblTotal.setFont(new Font("Default", 1, 12));
	lblTotal.setSize(new Dimension(124, 15));
	jPanel2.setBounds(new Rectangle(10, 385, 300, 255));
	jPanel2.setLayout(null);
	jPanel2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	btnColorFilesExpired.setOpaque(false);
	btnColorFilesExpired.setIcon(IconTypes.CRFileBlack_22x22);
	btnColorFilesExpired.setSize(new Dimension(32, 32));
	btnColorFilesExpired.setHorizontalTextPosition(SwingConstants.CENTER);
	btnColorFilesExpired.setMargin(new Insets(4, 14, 1, 14));
	btnColorFilesExpired.setBounds(new Rectangle(15, 15, 32, 32));
	btnColorFilesExpired.setEnabled(false);
	btnColorFilesExpired.addActionListener(new ActionListener() {

					    public void actionPerformed(ActionEvent e) {
						btnColorFilesExpired_actionPerformed(e);
					    }

					}
	);
	btnColorFilesOk.setBounds(new Rectangle(15, 120, 30, 30));
	btnColorFilesOk.setIcon(IconTypes.CRFileGreen_22x22);
	btnColorFilesOk.setOpaque(false);
	btnColorFilesOk.setSize(new Dimension(32, 32));
	btnColorFilesOk.setMargin(new Insets(4, 14, 1, 14));
	btnColorFilesOk.setEnabled(false);
	btnColorFilesOk.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   btnColorFilesOk_actionPerformed(e);
				       }

				   }
	);
	btnColorFilesWarned.setBounds(new Rectangle(15, 85, 30, 30));
	btnColorFilesWarned.setOpaque(false);
	btnColorFilesWarned.setIcon(IconTypes.CRFileYellow_22x22);
	btnColorFilesWarned.setSize(new Dimension(32, 32));
	btnColorFilesWarned.setMargin(new Insets(4, 14, 1, 14));
	btnColorFilesWarned.setEnabled(false);
	btnColorFilesWarned.addActionListener(new ActionListener() {

					   public void actionPerformed(ActionEvent e) {
					       btnColorFilesWarned_actionPerformed(e);
					   }

				       }
	);
	btnColorFilesInDanger.setBounds(new Rectangle(15, 50, 30, 30));
	btnColorFilesInDanger.setIcon(IconTypes.CRFileRed_22x22);
	btnColorFilesInDanger.setOpaque(false);
	btnColorFilesInDanger.setSize(new Dimension(32, 32));
	btnColorFilesInDanger.setMargin(new Insets(4, 14, 1, 14));
	btnColorFilesInDanger.setEnabled(false);
	btnColorFilesInDanger.addActionListener(new ActionListener() {

					     public void actionPerformed(ActionEvent e) {
						 btnColorFilesInDanger_actionPerformed(e);
					     }

					 }
	);
	tfQtyOkDays.setBounds(new Rectangle(245, 127, 45, 16));
	tfQtyOkDays.setText("0");
	tfQtyOkDays.setFont(new Font("Dialog", 1, 12));
	tfQtyOkDays.setHorizontalAlignment(BasicTextField.RIGHT);
	tfQtyOkDays.setEnabled(false);
	tfQtyOkDays.setDisabledTextColor(Color.black);
	tfQtyOkDays.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfQtyWarnedDays.setBounds(new Rectangle(245, 92, 45, 16));
	tfQtyWarnedDays.setText("0");
	tfQtyWarnedDays.setFont(new Font("Dialog", 1, 12));
	tfQtyWarnedDays.setHorizontalAlignment(BasicTextField.RIGHT);
	tfQtyWarnedDays.setEnabled(false);
	tfQtyWarnedDays.setDisabledTextColor(Color.black);
	tfQtyWarnedDays.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfQtyDangerDays.setBounds(new Rectangle(245, 57, 45, 16));
	tfQtyDangerDays.setText("0");
	tfQtyDangerDays.setFont(new Font("Dialog", 1, 12));
	tfQtyDangerDays.setHorizontalAlignment(BasicTextField.RIGHT);
	tfQtyDangerDays.setEnabled(false);
	tfQtyDangerDays.setDisabledTextColor(Color.black);
	tfQtyDangerDays.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfQtyExpiredDays.setBounds(new Rectangle(245, 23, 45, 16));
	tfQtyExpiredDays.setText("0");
	tfQtyExpiredDays.setFont(new Font("Dialog", 1, 12));
	tfQtyExpiredDays.setSize(new Dimension(45, 16));
	tfQtyExpiredDays.setHorizontalAlignment(BasicTextField.RIGHT);
	tfQtyExpiredDays.setEnabled(false);
	tfQtyExpiredDays.setDisabledTextColor(Color.black);
	tfQtyExpiredDays.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	lblOkDay.setText("Vencimientos iniciales:");
	lblOkDay.setBounds(new Rectangle(65, 128, 178, 14));
	lblOkDay.setFont(new Font("Default", 0, 11));
	lblOkDay.setSize(new Dimension(121, 14));
	lblWarnedDay.setText("Alerta de vencimientos:");
	lblWarnedDay.setBounds(new Rectangle(65, 93, 182, 15));
	lblWarnedDay.setFont(new Font("Default", 0, 11));
	lblWarnedDay.setSize(new Dimension(125, 15));
	lblDangerDay.setText("Pronto vencimientos:");
	lblDangerDay.setBounds(new Rectangle(65, 58, 170, 14));
	lblDangerDay.setFont(new Font("Default", 0, 11));
	lblDangerDay.setSize(new Dimension(113, 14));
	lblExpiredDay.setText("Vencidos:");
	lblExpiredDay.setBounds(new Rectangle(65, 24, 109, 15));
	lblExpiredDay.setFont(new Font("Default", 0, 11));
	lblExpiredDay.setSize(new Dimension(52, 15));
	btnColorFilesCustom.setBounds(new Rectangle(15, 155, 30, 30));
	btnColorFilesCustom.setIcon(IconTypes.CRFileBlue_22x22);
	btnColorFilesCustom.setOpaque(false);
	btnColorFilesCustom.setSize(new Dimension(32, 32));
	btnColorFilesCustom.setMargin(new Insets(4, 14, 1, 14));
	btnColorFilesCustom.setEnabled(false);
	btnColorFilesCustom.addActionListener(new ActionListener() {

					   public void actionPerformed(ActionEvent e) {
					       btnColorFilesCustom_actionPerformed(e);
					   }

				       }
	);
	lblCustomDay.setText("Vencimientos personalizados:");
	lblCustomDay.setBounds(new Rectangle(65, 163, 217, 14));
	lblCustomDay.setFont(new Font("Default", 0, 11));
	lblCustomDay.setSize(new Dimension(160, 14));
	jPanel3.setBounds(new Rectangle(10, 130, 300, 225));
	jPanel3.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jPanel3.setLayout(null);
	jPanel3.setSize(new Dimension(300, 225));
	jPanel4.setBounds(new Rectangle(10, 55, 300, 65));
	jPanel4.setLayout(null);
	jPanel4.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jLabel3.setText("Todos");
	jLabel3.setBounds(new Rectangle(65, 220, 46, 16));
	jLabel3.setFont(new Font("Default", 1, 13));
	jLabel3.setSize(new Dimension(46, 16));
	tfTotal.setBounds(new Rectangle(230, 218, 60, 20));
	tfTotal.setCaretColor(new Color(148, 0, 0));
	tfTotal.setFont(new Font("Default", 1, 13));
	tfTotal.setText("0");
	tfTotal.setForeground(new Color(148, 0, 0));
	tfTotal.setHorizontalAlignment(BasicTextField.RIGHT);
	tfTotal.setEnabled(false);
	tfTotal.setDisabledTextColor(Color.black);
	tfTotal.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	chkExpired.setBounds(new Rectangle(40, 15, 16, 15));
	chkExpired.setSize(new Dimension(16, 15));
	chkExpired.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      chkExpired_actionPerformed(e);
				  }

			      }
	);
	chkCustom.setBounds(new Rectangle(100, 40, 16, 15));
	chkCustom.setSize(new Dimension(16, 17));
	chkCustom.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     chkCustom_actionPerformed(e);
				 }

			     }
	);
	chkDanger.setBounds(new Rectangle(100, 15, 16, 15));
	chkDanger.setSize(new Dimension(16, 17));
	chkDanger.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     chkDanger_actionPerformed(e);
				 }

			     }
	);
	chkOk.setBounds(new Rectangle(40, 40, 16, 15));
	chkOk.setSize(new Dimension(16, 17));
	chkOk.addActionListener(new ActionListener() {

			     public void actionPerformed(ActionEvent e) {
				 chkOk_actionPerformed(e);
			     }

			 }
	);
	chkTotal.setBounds(new Rectangle(160, 40, 16, 15));
	chkTotal.setSize(new Dimension(16, 17));
	chkTotal.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    chkTotal_actionPerformed(e);
				}

			    }
	);
	chkWarned.setBounds(new Rectangle(160, 15, 15, 15));
	chkWarned.setSize(new Dimension(16, 17));
	chkWarned.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     chkWarned_actionPerformed(e);
				 }

			     }
	);
	jLabel2.setText("");
	jLabel2.setBounds(new Rectangle(20, 15, 18, 14));
	jLabel2.setSize(new Dimension(18, 14));
	jLabel4.setText("");
	jLabel4.setBounds(new Rectangle(80, 15, 18, 15));
	jLabel5.setText("");
	jLabel5.setBounds(new Rectangle(140, 15, 18, 15));
	jLabel6.setText("");
	jLabel6.setBounds(new Rectangle(20, 40, 18, 15));
	jLabel7.setText("");
	jLabel7.setBounds(new Rectangle(80, 40, 18, 15));
	jLabel8.setText("");
	jLabel8.setBounds(new Rectangle(140, 40, 18, 15));
	btnTotalFile.setBounds(new Rectangle(13, 210, 36, 36));
	btnTotalFile.setMargin(new Insets(4, 14, 1, 14));
	btnTotalFile.setSize(new Dimension(36, 36));
	btnTotalFile.setIcon(IconTypes.CRFileTotal);
	btnTotalFile.setEnabled(false);
	btnTotalFile.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnTotalFile_actionPerformed(e);
				    }

				}
	);
	tfQtyCustomDays.setBounds(new Rectangle(245, 162, 45, 16));
	tfQtyCustomDays.setText("0");
	tfQtyCustomDays.setFont(new Font("Dialog", 1, 12));
	tfQtyCustomDays.setHorizontalAlignment(BasicTextField.RIGHT);
	//jPanel2.add(jPanel1, null);
	tfQtyCustomDays.setEnabled(false);
	tfQtyCustomDays.setDisabledTextColor(Color.black);
	tfQtyCustomDays.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jPanel4.add(btnFind, null);
	jPanel4.add(chkExpired, null);
	jPanel4.add(jLabel2, null);
	jPanel4.add(jLabel4, null);
	jPanel4.add(jLabel5, null);
	jPanel4.add(jLabel6, null);
	jPanel4.add(jLabel7, null);
	jPanel4.add(jLabel8, null);
	jPanel4.add(chkCustom, null);
	jPanel4.add(chkDanger, null);
	jPanel4.add(chkOk, null);
	jPanel4.add(chkTotal, null);
	jPanel4.add(chkWarned, null);
	jPanel2.add(tfQtyCustomDays, null);
	jPanel2.add(lblCustomDay, null);
	jPanel2.add(btnColorFilesCustom, null);
	jPanel2.add(tfQtyExpiredDays, null);
	jPanel2.add(tfQtyDangerDays, null);
	jPanel2.add(tfQtyWarnedDays, null);
	jPanel2.add(tfQtyOkDays, null);
	jPanel2.add(btnColorFilesInDanger, null);
	jPanel2.add(btnColorFilesWarned, null);
	jPanel2.add(btnColorFilesOk, null);
	jPanel2.add(lblExpiredDay, null);
	jPanel2.add(lblDangerDay, null);
	jPanel2.add(lblWarnedDay, null);
	jPanel2.add(lblOkDay, null);
	jPanel2.add(btnColorFilesExpired, null);
	jPanel2.add(btnTotalFile, null);
	jPanel2.add(jLabel3, null);
	jPanel2.add(tfTotal, null);
	jPanel2.add(btnColorFilesOk, null);
	jPanel2.add(btnColorFilesWarned, null);
	jPanel2.add(btnColorFilesInDanger, null);
	jPanel2.add(tfQtyOkDays, null);
	jPanel2.add(tfQtyWarnedDays, null);
	jPanel2.add(tfQtyDangerDays, null);
	jPanel2.add(tfQtyExpiredDays, null);
	jPanel2.add(lblOkDay, null);
	jPanel2.add(lblWarnedDay, null);
	jPanel1.add(jLabel1, null);
	jPanel1.add(lblTotal, null);
	jPanel1.add(jPanel4, null);
	jPanel1.add(jPanel2, null);
	jPanel1.add(jPanel3, null);
	this.add(jPanel1, BorderLayout.CENTER);
	/** Listener **/
	loadCalendar();
	loadFilterPriorities();
	chkTotal.setSelected(true);
	checkAllPriorities();
	searchFunction();
	desktopParent.add(this);
    }

    private void loadCalendar() {
	int day = Integer.parseInt(Environment.currentDay);
	int month = Integer.parseInt(Environment.currentMonth);
	int year = Integer.parseInt(Environment.currentYear);
	int dayNumber = Integer.parseInt(Environment.currentDayNumber);
	panelCalendar = new CalendarFile(year, month, day, dayNumber, this);
	panelCalendar.setBounds(new Rectangle(5, 5, 290, 215));
	jPanel3.add(panelCalendar, null);
    }

    private void loadFilterPriorities() {
	chkVector[0] = chkTotal;
	chkVector[0].setName("0");
	chkVector[1] = chkExpired;
	chkVector[2] = chkDanger;
	chkVector[3] = chkWarned;
	chkVector[4] = chkOk;
	chkVector[5] = chkCustom;
	try {
	    String Query = "SELECT * FROM file.getallname_priorities () AS resultTable";
	    ResultSet rsPriorities = LibSQL.exQuery(Query);
	    int i = 1;
	    while (rsPriorities.next()) {
		chkVector[i].setName(rsPriorities.getString("label"));
		++i;
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
    }

    private Set createSetColorDay(ResultSet _rsAlarmFiles) {
	Set hashDay = new HashSet();
	try {
	    while (_rsAlarmFiles.next()) {
		String day = _rsAlarmFiles.getString("alarmday");
		String month = _rsAlarmFiles.getString("alarmmonth");
		String year = _rsAlarmFiles.getString("alarmyear");
		if (!hashDay.contains("" + day + "-" + month + "-" + year))
		    hashDay.add("" + day + "-" + month + "-" + year);
	    }
	    _rsAlarmFiles.beforeFirst();
	} catch (SQLException x) {
	    x.printStackTrace();
	}
	return hashDay;
    }

    private Set createSetDays(ResultSet _rsAlarmFiles) {
	Set hashDay = new HashSet();
	try {
	    StringBuffer textToolTip = new StringBuffer("");
	    int indexStyle = 0;
	    String day = "";
	    String month = "";
	    String year = "";
	    String Style = "style='background-color:#FFFFFF'";
	    String alarmDayDate = "";
	    while (_rsAlarmFiles.next()) {
		if (!alarmDayDate.equals(_rsAlarmFiles.getString("alarmdate"))) {
		    if (!day.equals("")) {
			textToolTip.append("</table>");
			textToolTip.append("</html>");
			if (!hashDay.contains("" + day + "-" + month + "-" + year))
			    hashDay.add("" + day + "-" + month + "-" + year + "##" + textToolTip.toString());
			//panelCalendar.setToolTipDay(Day,textToolTip.toString());
		    }
		    day = _rsAlarmFiles.getString("alarmday");
		    month = _rsAlarmFiles.getString("alarmmonth");
		    year = _rsAlarmFiles.getString("alarmyear");
		    alarmDayDate = _rsAlarmFiles.getString("alarmdate");
		    textToolTip = new StringBuffer("");
		    textToolTip.append("<html>");
		    textToolTip.append("<table border='0' cellpadding='0' cellspacing='0' width='500'>");
		    textToolTip.append("<tr style='background-color:#303030;COLOR:#FFFFFF'>");
		    textToolTip.append("<td width='10'>&nbsp;</td>");
		    textToolTip.append("<td width='100'><b>Nº Expediente</b></td>");
		    textToolTip.append("<td width='25'>&nbsp;</td>");
		    textToolTip.append("<td><b>Tramite/Tarea</b></td>");
		    textToolTip.append("<td width='10'>&nbsp;</td>");
		    textToolTip.append("</tr>");
		    indexStyle = 0;
		}
		if ((indexStyle % 2) == 0)
		    Style = "style='background-color:#d7d7d7'";
		else
		    Style = "style='background-color:#FFFFFF'";
		String fileNumber = _rsAlarmFiles.getString("filenumber");
		String fileLetter = _rsAlarmFiles.getString("fileletter");
		String fileYear = _rsAlarmFiles.getString("fileyear");
		String applicationRef = _rsAlarmFiles.getString("applicationname");
		textToolTip.append("<tr " + Style + " >");
		textToolTip.append("<td width='10'>&nbsp;</td>");
		textToolTip.append("<td>" + fileNumber + "-" + fileLetter + "-" + fileYear + " </td>");
		textToolTip.append("<td>&nbsp;</td>");
		textToolTip.append("<td>" + applicationRef + "</td>");
		textToolTip.append("<td width='10'>&nbsp;</td>");
		textToolTip.append("</tr>");
		indexStyle += 1;
	    }
	    _rsAlarmFiles.beforeFirst();
	    textToolTip.append("</table>");
	    textToolTip.append("</html>");
	    if (!hashDay.contains("" + day + "-" + month + "-" + year))
		hashDay.add("" + day + "-" + month + "-" + year + "##" + textToolTip.toString());
	} catch (SQLException x) {
	    x.printStackTrace();
	}
	return hashDay;
    }

    private void setColorDay(String _colorPriority) {
	String Red = _colorPriority.split(";")[0];
	String Green = _colorPriority.split(";")[1];
	String Blue = _colorPriority.split(";")[2];
	panelCalendar.setColorAlarm(new Color(Integer.parseInt(Red), Integer.parseInt(Green), Integer.parseInt(Blue)));
    }

    private void paintDays(String _valuePriority) {
	String[] Priority = _valuePriority.split("-");
	String idPriority = Priority[0];
	selectedIdPriorities += idPriority + ",";
	String Query = "SELECT * FROM file.getall_alarms('" + idPriority + "') AS resultTable";
	//System.out.println(Query);
	ResultSet rsAlarmFiles = LibSQL.exQuery(Query);
	setColorDay(Priority[1]);
	Set hashDay = createSetDays(rsAlarmFiles);
	panelCalendar.setHashAlarmDays(hashDay);
	panelCalendar.setToolTipANDColorDay();
    }

    public void findExpedientes() {
	String valuePriority = "";
	selectedIdPriorities = "";
	panelCalendar.clearColorDay();
	for (int i = 1; i < chkVector.length; i++) {
	    if (chkVector[i].isSelected()) {
		valuePriority = chkVector[i].getName();
		paintDays(valuePriority);
	    }
	}
	if (selectedIdPriorities.length() != 0) {
	    String queryTotalByPriority = "SELECT  " + "       priority_tabs.idpriority_tab, " + "       (SELECT COUNT(*) FROM file.alarms WHERE alarms.estado<>'*' AND priority_tabs.idpriority_tab IN (" + selectedIdPriorities.substring(0, selectedIdPriorities.length() - 1) + ")" + "       AND alarms.idpriority_tab = priority_tabs.idpriority_tab " + "       AND " + alarmDateParam + ") AS totalfiles  " + "FROM  " + "       tabs.priority_tabs  " + " WHERE " + "       estado<>'*' " + "ORDER BY " + "       idpriority_tab;";
	    setTotalFilesByPriority(queryTotalByPriority);
	    String queryTotal = "SELECT " + "   COUNT(distinct(idfile))  AS totalFiles " + "FROM " + "   file.alarms " + "WHERE " + "   estado<>'*' AND idpriority_tab IN (" + selectedIdPriorities.substring(0, selectedIdPriorities.length() - 1) + ")" + "   AND " + alarmDateParam;
	    setTotalFiles(queryTotal);
	}
	lblTotal.setText("Vtos. (" + panelCalendar.getSelectedMonthText() + " del " + panelCalendar.getSelectedYear() + ")");
	lblTotal.setSize(lblTotal.getText().length() * 7 + 10, 15);
    }

    public void setTotalTextByDay(String _day) {
	lblTotal.setText("Vtos. (" + _day + " de " + panelCalendar.getSelectedMonthText() + " del " + panelCalendar.getSelectedYear() + ")");
	lblTotal.setSize(lblTotal.getText().length() * 8, 15);
    }

    public void setTotalTextByWeek(String _week) {
	lblTotal.setText("Vtos. (Semana " + _week + " del " + panelCalendar.getSelectedYear() + ")");
	lblTotal.setSize(lblTotal.getText().length() * 7 + 10, 15);
    }

    private void searchFunction() {
	String selectedMonthValue = String.valueOf(panelCalendar.getSelectedMonthValue());
	String selectedYear = String.valueOf(panelCalendar.getSelectedYear());
	alarmDateParam = " EXTRACT(YEAR FROM alarmdate)='" + selectedYear + "' AND EXTRACT(MONTH FROM alarmdate)='" + selectedMonthValue + "'";
	findExpedientes();
    }

    private void btnFind_actionPerformed(ActionEvent e) {
	searchFunction();
    }

    public void setTotalFilesByPriority(String _query) {
	try {
	    ResultSet rsResult = LibSQL.exQuery(_query);
	    if (rsResult.next()) {
		tfQtyExpiredDays.setText(rsResult.getString("totalfiles"));
		if (rsResult.getInt("totalfiles") == 0) {
		    btnColorFilesExpired.setDisabledIcon(btnColorFilesExpired.getIcon());
		    btnColorFilesExpired.setEnabled(false);
		} else
		    btnColorFilesExpired.setEnabled(true);
	    }
	    if (rsResult.next()) {
		tfQtyDangerDays.setText(rsResult.getString("totalfiles"));
		if (rsResult.getInt("totalfiles") == 0) {
		    btnColorFilesInDanger.setDisabledIcon(btnColorFilesInDanger.getIcon());
		    btnColorFilesInDanger.setEnabled(false);
		} else
		    btnColorFilesInDanger.setEnabled(true);
	    }
	    if (rsResult.next()) {
		tfQtyWarnedDays.setText(rsResult.getString("totalfiles"));
		if (rsResult.getInt("totalfiles") == 0) {
		    btnColorFilesWarned.setDisabledIcon(btnColorFilesWarned.getIcon());
		    btnColorFilesWarned.setEnabled(false);
		} else
		    btnColorFilesWarned.setEnabled(true);
	    }
	    if (rsResult.next()) {
		tfQtyOkDays.setText(rsResult.getString("totalfiles"));
		if (rsResult.getInt("totalfiles") == 0) {
		    btnColorFilesOk.setDisabledIcon(btnColorFilesOk.getIcon());
		    btnColorFilesOk.setEnabled(false);
		} else
		    btnColorFilesOk.setEnabled(true);
	    }
	    if (rsResult.next()) {
		tfQtyCustomDays.setText(rsResult.getString("totalfiles"));
		if (rsResult.getInt("totalfiles") == 0) {
		    btnColorFilesCustom.setDisabledIcon(btnColorFilesCustom.getIcon());
		    btnColorFilesCustom.setEnabled(false);
		} else
		    btnColorFilesCustom.setEnabled(true);
	    }
	} catch (SQLException x) {
	    x.printStackTrace();
	}
    }

    public void setTotalFiles(String _queryTotalByPriority) {
	try {
	    ResultSet rsResult = LibSQL.exQuery(_queryTotalByPriority);
	    if (rsResult.next()) {
		tfTotal.setText(rsResult.getString("totalFiles"));
		if (rsResult.getString("totalFiles").equals("0"))
		    btnTotalFile.setEnabled(false);
		else
		    btnTotalFile.setEnabled(true);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {

	}
    }

    private void btnColorFilesExpired_actionPerformed(ActionEvent e) {
	if (expiredFileList != null)
	    expiredFileList.dispose();
	expiredFileList = new FilesAlarmList("1", alarmDateParam, IconTypes.CRFileBlack_22x22);
	desktopParent.add(expiredFileList);
	expiredFileList.show();
    }

    private void btnColorFilesInDanger_actionPerformed(ActionEvent e) {
	if (dangerFileList != null)
	    dangerFileList.dispose();
	dangerFileList = new FilesAlarmList("2", alarmDateParam, IconTypes.CRFileRed_22x22);
	desktopParent.add(dangerFileList);
	dangerFileList.show();
    }

    private void btnColorFilesWarned_actionPerformed(ActionEvent e) {
	if (warnedFileList != null)
	    warnedFileList.dispose();
	warnedFileList = new FilesAlarmList("3", alarmDateParam, IconTypes.CRFileYellow_22x22);
	desktopParent.add(warnedFileList);
	warnedFileList.show();
    }

    private void btnColorFilesOk_actionPerformed(ActionEvent e) {
	if (okFileList != null)
	    okFileList.dispose();
	okFileList = new FilesAlarmList("4", alarmDateParam, IconTypes.CRFileGreen_22x22);
	desktopParent.add(okFileList);
	okFileList.show();
    }

    private void btnColorFilesCustom_actionPerformed(ActionEvent e) {
	if (customFileList != null)
	    customFileList.dispose();
	customFileList = new FilesAlarmList("5", alarmDateParam, IconTypes.CRFileBlue_22x22);
	desktopParent.add(customFileList);
	customFileList.show();
    }

    private void btnTotalFile_actionPerformed(ActionEvent e) {
	FilesAlarmList fileList = new FilesAlarmList("%", alarmDateParam, IconTypes.CRFileTotal_22x22);
	desktopParent.add(fileList);
	fileList.show();
    }

    private void checkAllPriorities() {
	for (int i = 1; i < chkVector.length; i++) {
	    chkVector[i].setSelected(chkTotal.isSelected());
	}
    }

    private void chkTotal_actionPerformed(ActionEvent e) {
	checkAllPriorities();
    }

    private void chkExpired_actionPerformed(ActionEvent e) {
	if (!chkExpired.isSelected())
	    chkTotal.setSelected(false);
    }

    private void chkDanger_actionPerformed(ActionEvent e) {
	if (!chkDanger.isSelected())
	    chkTotal.setSelected(false);
    }

    private void chkWarned_actionPerformed(ActionEvent e) {
	if (!chkWarned.isSelected())
	    chkTotal.setSelected(false);
    }

    private void chkOk_actionPerformed(ActionEvent e) {
	if (!chkOk.isSelected())
	    chkTotal.setSelected(false);
    }

    private void chkCustom_actionPerformed(ActionEvent e) {
	if (!chkCustom.isSelected())
	    chkTotal.setSelected(false);
    }

    public void setAlarmDateParam(String _alarmDateParam) {
	alarmDateParam = _alarmDateParam;
    }

}

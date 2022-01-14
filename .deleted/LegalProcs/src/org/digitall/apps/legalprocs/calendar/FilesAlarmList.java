 package org.digitall.apps.legalprocs.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.ResultSet;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.JPColorLabel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.ReportButton;
import org.digitall.lib.data.Format;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;
import org.digitall.lib.xml.XMLWorkBook;

//

public class FilesAlarmList extends JInternalFrame {

    private BasicPanel jPanel1 = new BasicPanel();
    private int resultCountSelected = 30;
    private int[] tcol = { };
    private int[] sizeColumn = { 40, 235, 114, 310, 95 };
    private Grilla jpFiles = new Grilla(resultCountSelected, tcol, sizeColumn, false, false);
    private Vector headerFiles = new Vector();
    private Vector datos = new Vector();
    private String idPriority;
    private BasicPanel jPanel2 = new BasicPanel();
    private BasicLabel lblTotalExpired = new BasicLabel();
    private String alarmDateParam;
    //private BasicPanel jPanel3 = new BasicPanel();
    private ImageIcon iconAlarmType;
    private ReportButton btnReport = new ReportButton();
    private CloseButton btnClose = new CloseButton();
    private BorderLayout borderLayout1 = new BorderLayout();

    public FilesAlarmList(String _idPriority, String _alarmDate, ImageIcon _iconAlarmType) {
	try {
	    alarmDateParam = _alarmDate;
	    idPriority = _idPriority;
	    iconAlarmType = _iconAlarmType;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(850, 550));
	this.setTitle("Expedientes");
	this.setIconifiable(true);
	this.setClosable(true);
	this.setFrameIcon(iconAlarmType);
	jPanel1.setBounds(new Rectangle(25, 25, 690, 215));
	jPanel1.setLayout(null);
	jPanel2.setBounds(new Rectangle(10, 55, 825, 410));
	jPanel2.setLayout(borderLayout1);
	jPanel2.setBorder(BorderPanel.getBorderPanel("Expedientes"));
	lblTotalExpired.setText(" Total: X");
	lblTotalExpired.setBounds(new Rectangle(735, 45, 90, 22));
	lblTotalExpired.setOpaque(false);
	lblTotalExpired.setSize(new Dimension(90, 22));
	lblTotalExpired.setIconTextGap(1);
	lblTotalExpired.setFont(new Font("Default", 1, 12));
	lblTotalExpired.setHorizontalTextPosition(SwingConstants.CENTER);
	lblTotalExpired.setHorizontalAlignment(SwingConstants.CENTER);
	lblTotalExpired.setForeground(new Color(148, 0, 0));
	btnReport.setBounds(new Rectangle(10, 485, 40, 28));
	btnReport.setSize(new Dimension(40, 28));
	btnReport.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnReport_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(795, 485, 40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	jPanel1.add(btnReport, null);
	jPanel1.add(lblTotalExpired, null);
	jPanel2.add(jpFiles, BorderLayout.CENTER);
	jPanel1.add(jPanel2, null);
	jPanel1.add(btnClose, null);
	this.getContentPane().add(jPanel1, null);
	LoadFilesList();
    }

    private void LoadFilesList() {
	jpFiles.Redimensiona();
	jpFiles.getTable().addKeyListener(new KeyAdapter() {

				       public void keyPressed(KeyEvent e) {
					   jpFiles_keyTyped(e);
				       }

				       public void keyReleased(KeyEvent e) {
					   jpFiles_keyTyped(e);
				       }

				       public void keyTyped(KeyEvent e) {
					   jpFiles_keyTyped(e);
				       }

				   }
	);
	jpFiles.getTable().addMouseListener(new MouseAdapter() {

					 public void mouseClicked(MouseEvent e) {
					     if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
						 datos = jpFiles.VDatos();
					     }
					 }

				     }
	);
	jpFiles.getTable().addMouseListener(new MouseAdapter() {

					 public void mouseClicked(MouseEvent e) {
					     if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
						 datos = jpFiles.VDatos();
					     }
					 }

				     }
	);
	setHeaderFilesList();
	setTableFileList();
	jpFiles.getTable().setDefaultRenderer(JPColorLabel.class, new ExpiredTableCellRenderer());
    }

    private void setHeaderFilesList() {
	headerFiles.removeAllElements();
	headerFiles.addElement("*");
	headerFiles.addElement("Color");
	headerFiles.addElement("Nombre de Mina");
	headerFiles.addElement("Nï¿½ Expediente");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("Tramite");
	headerFiles.addElement("Fecha Vto.");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
    }

    private void setTableFileList() {
	String query = "SELECT " + "	alarms.idfile, " + "       'Color:' || priority_tabs.colorred || ',' || priority_tabs.colorgreen || ',' || priority_tabs.colorblue AS alarmexpired, " + "       files.minename, " + "       files.filenumber ||'-'|| files.fileletter ||'-'|| files.fileyear AS filefullnumber, " + "	files.filenumber, " + "	files.fileletter, " + "	files.fileyear, " + "	alarms.idapplication_tab, " + "	alarms.idapplication, " + "	application_tabs.name AS applicationname, " + "	alarms.alarmdate, " + "	date_part('day', alarms.alarmdate) AS alarmday, " + "	date_part('month', alarms.alarmdate) AS alarmmonth, " + "	date_part('year', alarms.alarmdate) AS alarmyear, " + "	alarms.customalarmdate, " + "	alarms.idpriority_tab, " + "	priority_tabs.colorred, " + "	priority_tabs.colorgreen, " + "	priority_tabs.colorblue, " + "	X(cr_ne.point) AS pointx_ne, " + "	Y(cr_ne.point) AS pointy_ne, " + "	X(cr_so.point) AS pointx_so, " + "	Y(cr_so.point) AS pointy_so " + "FROM " + "	file.alarms, file.files, tabs.application_tabs, tabs.priority_tabs, file.applicationpriorities, file.cadastralregister cr_so, file.cadastralregister cr_ne " + "WHERE " + "	alarms.estado <> '*' AND " + alarmDateParam + "       AND alarms.idpriority_tab LIKE('" + idPriority + "')" + "	AND alarms.idpriority_tab = applicationpriorities.idpriority_tab " + "	AND alarms.idapplication_tab = applicationpriorities.idapplication_tab " + "	AND applicationpriorities.idapplication_tab = application_tabs.idapplication_tab " + "	AND applicationpriorities.idpriority_tab = priority_tabs.idpriority_tab " + "	AND alarms.idfile = files.idfile " + "	AND files.idpointso = cr_so.idcadastralregister " + "	AND files.idpointso = cr_ne.idcadastralregister " + "ORDER BY " + "	alarms.alarmdate DESC";
	String queryCount = "SELECT COUNT(*) FROM (" + query + ") as resultTableCount";
	jpFiles.ActualizaTabla((JInternalFrame)this, query, queryCount, headerFiles);
	lblTotalExpired.setText("Total: " + jpFiles.getRecordsCount());
	/*LibSQL.Connected();
        String params = "'"+ alarmDateParam.replaceAll("'","''") +"','%'";
        jpFiles.setTable(null,"file.getall_filesalarms",params,headerFiles);
        lblTotalExpired.setText("Total: " + jpFiles.getResultCount());*/
    }

    private void jpFiles_keyTyped(KeyEvent e) {
	try {
	    if (datos != jpFiles.VDatos()) {
		datos = jpFiles.VDatos();
	    }
	} catch (ArrayIndexOutOfBoundsException x) {
	    e.consume();
	}
    }

    private void xmlReport() {
	//tablas = 0;
	XMLWorkBook report = new XMLWorkBook();
	StringBuffer appendStyle = new StringBuffer();
	//String SC1 = "";
	//int Cantidad = 0;
	int thisSheet = 0;
	int sheetNumbers = 1;
	if (idPriority.equals("%"))
	    sheetNumbers = Integer.parseInt(LibSQL.getCampo("SELECT COUNT(*) FROM tabs.priority_tabs WHERE estado<>'*'")) + 1;
	Vector WBQuerys[] = new Vector[sheetNumbers];
	Vector headerReport = new Vector();
	Vector headerReportAll = new Vector();
	Vector dataTypes = new Vector();
	Vector dataTypesAll = new Vector();
	Vector stylesVector = new Vector();
	Vector stylesVectorAll = new Vector();
	Vector widthColumnVector = new Vector();
	Vector widthColumnVectorAll = new Vector();
	headerReport.add("Nombre de Mina");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("200");
	headerReport.add("Nï¿½ Expediente");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("80");
	headerReport.add("Tramite");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("300");
	headerReport.add("Fecha Vto.");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("80");
	report.setNombreLibro("Listado de Expedientes");
	report.setNroHojas(sheetNumbers);
	if (sheetNumbers > 1) {
	    WBQuerys[thisSheet] = new Vector();
	    report.setTituloHoja(thisSheet, Proced.setFormatDate(Environment.currentDate, true) + " - Listado de Expedientes (Todos)");
	    report.setNombreHoja(thisSheet, "Todos");
	    headerReportAll = (Vector)headerReport.clone();
	    headerReportAll.add("Color");
	    dataTypesAll = (Vector)dataTypes.clone();
	    dataTypesAll.add("String");
	    stylesVectorAll = (Vector)stylesVector.clone();
	    stylesVectorAll.add("Normal8");
	    widthColumnVectorAll = (Vector)widthColumnVector.clone();
	    widthColumnVectorAll.add("30");
	    report.setCabecera(thisSheet, headerReportAll);
	    report.setTipoDatos(thisSheet, dataTypesAll);
	    report.setColores(thisSheet, 0, stylesVectorAll);
	    report.setWidthColumn(thisSheet, widthColumnVectorAll);
	    String styleName = "titleFileTodos";
	    appendStyle.append("<Style ss:ID=\"" + styleName + "\">");
	    appendStyle.append(" <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\"/>");
	    appendStyle.append(" <Borders>");
	    appendStyle.append("  <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
	    appendStyle.append("  <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
	    appendStyle.append("  <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
	    appendStyle.append(" </Borders>");
	    appendStyle.append(" <Font ss:FontName=\"Times New Roman\" x:Family=\"Roman\" ss:Bold=\"1\" ss:Size=\"14\"/>");
	    appendStyle.append("<NumberFormat ss:Format=\"Standard\"/>");
	    appendStyle.append(" <Interior ss:Color=\"#e7e7e7\" ss:Pattern=\"Solid\"/>");
	    appendStyle.append("</Style>");
	    report.setTitleStyle(thisSheet, styleName);
	    String query = "SELECT minename,filenumber ||'-'|| fileletter ||'-'|| fileyear,applicationname,to_char(alarmdate,'dd/mm/yyyy') as alarmdate,idpriority_tab FROM  file.vgetall_alarms WHERE " + alarmDateParam;
	    WBQuerys[thisSheet].add(query);
	    report.setQuerys(thisSheet, WBQuerys[thisSheet]);
	    report.setColorColumn(thisSheet, 5);
	    thisSheet += 1;
	}
	try {
	    String queryPriorities = "SELECT * FROM file.getall_priorities('" + idPriority + "') AS resutlTable ORDER BY idpriority_tab";
	    ResultSet rsPriority = LibSQL.exQuery(queryPriorities);
	    while (rsPriority.next()) {
		WBQuerys[thisSheet] = new Vector();
		report.setTituloHoja(thisSheet, Proced.setFormatDate(Environment.currentDate, true) + " - Listado de Expedientes (" + rsPriority.getString("name") + ")");
		report.setNombreHoja(thisSheet, rsPriority.getString("name"));
		report.setCabecera(thisSheet, headerReport);
		report.setTipoDatos(thisSheet, dataTypes);
		report.setColores(thisSheet, 0, stylesVector);
		report.setWidthColumn(thisSheet, widthColumnVector);
		String colorStyle = Format.color2Hex(new Color(rsPriority.getInt("colorred"), rsPriority.getInt("colorgreen"), rsPriority.getInt("colorblue")));
		String styleName = "titleFile" + rsPriority.getString("idpriority_tab");
		/************************************* Style Title for all Sheet ********************************************/
		appendStyle.append("<Style ss:ID=\"" + styleName + "\">");
		appendStyle.append(" <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\"/>");
		appendStyle.append(" <Borders>");
		appendStyle.append("  <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
		appendStyle.append("  <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
		appendStyle.append("  <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
		appendStyle.append(" </Borders>");
		appendStyle.append(" <Font ss:FontName=\"Times New Roman\" x:Family=\"Roman\" ss:Bold=\"1\" ss:Size=\"14\"/>");
		appendStyle.append("<NumberFormat ss:Format=\"Standard\"/>");
		appendStyle.append(" <Interior ss:Color=\"#" + colorStyle + "\" ss:Pattern=\"Solid\"/>");
		appendStyle.append("</Style>");
		/***********************************************************************************************************/
		/************************************* Style for each Priority ********************************************/
		appendStyle.append("<Style ss:ID=\"Priority" + rsPriority.getString("idpriority_tab") + "\">");
		appendStyle.append(" <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\"/>");
		appendStyle.append(" <Borders>");
		appendStyle.append("  <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		appendStyle.append("  <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		appendStyle.append("  <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>");
		appendStyle.append(" </Borders>");
		appendStyle.append(" <Font ss:FontName=\"Times New Roman\" x:Family=\"Roman\" ss:Bold=\"0\" ss:Size=\"8\"/>");
		appendStyle.append("<NumberFormat ss:Format=\"Standard\"/>");
		appendStyle.append(" <Interior ss:Color=\"#" + colorStyle + "\" ss:Pattern=\"Solid\"/>");
		appendStyle.append("</Style>");
		/***********************************************************************************************************/
		report.setTitleStyle(thisSheet, styleName);
		String query = "SELECT minename,filenumber ||'-'|| fileletter ||'-'|| fileyear,applicationname,to_char(alarmdate,'dd/mm/yyyy') as alarmdate FROM  file.vgetall_alarms WHERE " + alarmDateParam + " AND idpriority_tab=" + rsPriority.getString("idpriority_tab") + " ORDER BY idpriority_tab";
		WBQuerys[thisSheet].add(query);
		report.setQuerys(thisSheet, WBQuerys[thisSheet]);
		thisSheet++;
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {

	}
	report.setAppendStyle(appendStyle.toString());
	report.createWorkBook();
    }

    private void btnReport_actionPerformed(ActionEvent e) {
	xmlReport();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

}
class ExpiredTableCellRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable jTable, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
	JPColorLabel exp = (JPColorLabel)object;
	return exp;
    }

}

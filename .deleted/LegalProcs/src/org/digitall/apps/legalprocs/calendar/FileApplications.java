package org.digitall.apps.legalprocs.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.ResultSet;

import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.digitall.apps.legalprocs.calendar.wizard.Manager;
import org.digitall.apps.legalprocs.calendar.wizard.PanelWizard;
import org.digitall.apps.legalprocs.calendar.wizard.Step0;
import org.digitall.apps.legalprocs.calendar.wizard.Step1_ExplorationSteps;
import org.digitall.apps.legalprocs.calendar.wizard.Step2_DiscoveryType;
import org.digitall.apps.legalprocs.calendar.wizard.Step3_MineSteps;
import org.digitall.apps.legalprocs.manager.classes.FileClass;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.JPColorLabel;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CancelDataButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.ReportButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.combos.JCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.data.Format;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;
import org.digitall.lib.xml.XMLWorkBook;

public class FileApplications extends BasicInternalFrame {

    private BasicPanel jpExpteTramite = new BasicPanel();
    private TFInput tfDate = new TFInput(DataTypes.DATE, "ExpirationDate", false);
    private TFInput tfStartDate = new TFInput(DataTypes.DATE, "StartDate", false);
    private TFInput tfEndDate = new TFInput(DataTypes.DATE, "EndDate", false);
    private TFInput tfDescription = new TFInput(DataTypes.STRING, "Description", false);
    private CBInput cbApplications = new CBInput(CachedCombo.APPLICATION_TABS, "Applications", false);
    private TFInput tfApplications = new TFInput(DataTypes.STRING, "Applications", false);
    private AcceptButton btnAddApplication = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private BasicPanel jPanel1 = new BasicPanel();
    private BasicLabel jLabel2 = new BasicLabel();
    private BasicPanel jPanel2 = new BasicPanel();
    private BasicLabel jLabel4 = new BasicLabel();
    private BasicLabel lblFileFullNumber = new BasicLabel();
    private BasicLabel jLabel6 = new BasicLabel();
    private BasicLabel lblMineName = new BasicLabel();
    private JCombo cbExternal = new JCombo();
    private int resultCountSelected = 30;
    private int[] tcol = { };
    private int[] sizeColumn = { 43, 90, 518, 85, 88 };
    private Grilla jpList = new Grilla(resultCountSelected, tcol, sizeColumn, false, false);
    private Vector dataRow = new Vector();
    private Vector headerList = new Vector();
    private String idfile;
    private String fileFullNumber;
    private String mineName;
    private String dateFile;
    private String queryFilter = "";
    private SaveDataButton btnSave = new SaveDataButton();
    private CancelDataButton btnCancel = new CancelDataButton();
    private BasicLabel jLabel5 = new BasicLabel();
    private BasicLabel lblDate = new BasicLabel();
    private ReportButton btnReport = new ReportButton();
    private BasicLabel jLabel7 = new BasicLabel();
    private JCombo cbType = new JCombo();
    private BasicButton btnWizard = new BasicButton();
    private JDesktopPane parentDesktop;
    private int step = 0, qtySteps;
    private Manager mgmt;
    private String Title = "";
    private int idFile = -1;
    private FileClass fileClass;
    public static final int EXPLORATIONREQUEST = 1;
    public static final int MINEREQUEST = 3;
    private BorderLayout borderLayout1 = new BorderLayout();

    public FileApplications(String _fileFullNumber, String _mineName, String _dateFile, int _idFile) {
	try {
	    idFile = _idFile;
	    fileFullNumber = _fileFullNumber;
	    mineName = _mineName;
	    dateFile = _dateFile;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public FileApplications(String _idfile, String _fileFullNumber, String _mineName, String _dateFile, JDesktopPane _parent) {
	try {
	    System.out.println("parentDesktop");
	    parentDesktop = _parent;
	    idfile = _idfile;
	    fileFullNumber = _fileFullNumber;
	    mineName = _mineName;
	    dateFile = _dateFile;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.getContentPane().setLayout(null);
	this.setSize(new Dimension(888, 554));
	this.setIconifiable(true);
	this.setClosable(true);
	this.setFrameIcon(IconTypes.applicationIcon_16x16);
	this.setTitle("Tramites del Expediente Nro. " + fileFullNumber);
	jpExpteTramite.setBounds(new Rectangle(9, 55, 865, 40));
	jpExpteTramite.setLayout(null);
	jpExpteTramite.setBorder(BorderPanel.getBorderPanel("Expediente:"));
	btnAddApplication.setBounds(new Rectangle(65, 490, 40, 28));
	btnAddApplication.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     btnAddApplication_actionPerformed(e);
					 }

				     }
	);
	tfDate.setBounds(new Rectangle(495, 20, 90, 35));
	tfDate.autoSize();
	tfDate.setEditable(false);
	tfDescription.setBounds(new Rectangle(15, 70, 835, 35));
	tfDescription.autoSize();
	tfDescription.setEditable(true);
	jPanel1.setBounds(new Rectangle(9, 355, 865, 115));
	jPanel1.setBorder(BorderPanel.getBorderPanel("Más datos:"));
	jPanel1.setLayout(null);
	jLabel2.setBounds(new Rectangle(25, 345, 154, 15));
	jLabel2.setFont(new Font("Default", 1, 12));
	btnClose.setBounds(new Rectangle(835, 490, 40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	jPanel2.setBounds(new Rectangle(9, 115, 865, 230));
	jPanel2.setBorder(BorderPanel.getBorderPanel("Lista de trámites:"));
	jPanel2.setLayout(borderLayout1);
	jLabel4.setText("Nï¿½ Expediente:");
	jLabel4.setBounds(new Rectangle(425, 15, 80, 14));
	jLabel4.setSize(new Dimension(80, 14));
	jLabel4.setFont(new Font("Default", 0, 11));
	lblFileFullNumber.setText("000000-0000-0000");
	lblFileFullNumber.setBounds(new Rectangle(515, 15, 175, 15));
	lblFileFullNumber.setFont(new Font("Default", 1, 15));
	lblFileFullNumber.setForeground(Color.red);
	lblFileFullNumber.setSize(new Dimension(200, 15));
	lblFileFullNumber.setMaximumSize(new Dimension(200, 15));
	jLabel6.setText("Mina:");
	jLabel6.setBounds(new Rectangle(20, 15, 28, 14));
	jLabel6.setSize(new Dimension(30, 14));
	jLabel6.setFont(new Font("Default", 0, 11));
	lblMineName.setText(mineName);
	lblMineName.setBounds(new Rectangle(60, 15, 300, 14));
	lblMineName.setFont(new Font("Default", 1, 15));
	lblMineName.setSize(new Dimension(300, 14));
	lblMineName.setForeground(Color.red);
	btnSave.setBounds(new Rectangle(115, 490, 40, 28));
	btnSave.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnSave_actionPerformed(e);
			       }

			   }
	);
	jpExpteTramite.add(lblDate, null);
	jpExpteTramite.add(jLabel5, null);
	jpExpteTramite.add(lblMineName, null);
	jpExpteTramite.add(jLabel6, null);
	jpExpteTramite.add(lblFileFullNumber, null);
	jpExpteTramite.add(jLabel4, null);
	jPanel1.add(tfApplications, null);
	jPanel1.add(tfEndDate, null);
	jPanel1.add(tfDate, null);
	jPanel1.add(tfDescription, null);
	jPanel1.add(tfStartDate, null);
	btnCancel.setBounds(new Rectangle(165, 490, 40, 28));
	btnCancel.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnCancel_actionPerformed(e);
				 }

			     }
	);
	jLabel5.setText("Fecha:");
	jLabel5.setBounds(new Rectangle(720, 15, 36, 14));
	jLabel5.setSize(new Dimension(36, 14));
	jLabel5.setFont(new Font("Default", 0, 11));
	lblDate.setText("00/00/0000");
	lblDate.setBounds(new Rectangle(765, 15, 90, 14));
	lblDate.setFont(new Font("Default", 1, 12));
	lblDate.setSize(new Dimension(90, 14));
	lblDate.setForeground(new Color(0, 0, 132));
	btnReport.setBounds(new Rectangle(215, 490, 40, 28));
	btnReport.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnReport_actionPerformed(e);
				 }

			     }
	);
	jLabel7.setText(" Tipo: ");
	jLabel7.setBounds(new Rectangle(720, 100, 43, 20));
	jLabel7.setFont(new Font("Default", 1, 12));
	jLabel7.setSize(new Dimension(43, 15));
	cbType.setBounds(new Rectangle(764, 100, 110, 20));
	btnWizard.setBounds(new Rectangle(10, 490, 40, 28));
	tfApplications.setBounds(new Rectangle(15, 20, 460, 35));
	tfApplications.autoSize();
	tfApplications.setEditable(false);
	tfApplications.setVisible(false);
	tfEndDate.setBounds(new Rectangle(750, 20, 95, 35));
	tfEndDate.autoSize();
	tfEndDate.setEditable(true);
	tfStartDate.setBounds(new Rectangle(630, 20, 95, 35));
	tfStartDate.autoSize();
	tfStartDate.setEditable(true);
	cbApplications.setBounds(new Rectangle(15, 20, 460, 35));
	cbApplications.autoSize();
	jPanel1.add(cbApplications, null);
	this.getContentPane().add(btnWizard, null);
	this.getContentPane().add(cbType, null);
	this.getContentPane().add(jLabel7, null);
	this.getContentPane().add(btnReport, null);
	this.getContentPane().add(jLabel2, null);
	jPanel2.add(jpList, BorderLayout.CENTER);
	this.getContentPane().add(jPanel2, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(jpExpteTramite, null);
	this.getContentPane().add(jPanel1, null);
	this.getContentPane().add(btnSave, null);
	this.getContentPane().add(btnAddApplication, null);
	this.getContentPane().add(btnCancel, null);
	btnWizard.setIcon(IconTypes.fileWizard_Off_22x22);
	btnWizard.setToolTipText("Wizard");
	btnWizard.setMnemonic('W');
	btnWizard.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnWizard_actionPerformed(e);
				 }

			     }
	);
	loadLabel();
	loadList();
	loadComboApplications();
	loadFilterApplications();
	setAddAction(true);
	initializeManagerAndClass();
    }

    private void loadFilterApplications() {
	cbType.addItem("Todos", "0");
	cbType.addItem("En curso", "1");
	cbType.addItem("Finalizados", "2");
	cbType.addItemListener(new ItemListener() {

			    public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
				    if (cbType.getSelectedValue().equals("-1"))
					queryFilter = "";
				    else if (cbType.getSelectedValue().equals("1"))
					queryFilter = " AND enddate IS NULL ";
				    else
					queryFilter = " AND enddate IS NOT NULL ";
				    getList();
				}
			    }

			}
	);
    }

    private void loadLabel() {
	lblMineName.setText(mineName);
	lblFileFullNumber.setText(fileFullNumber);
	lblDate.setText(dateFile);
    }

    private void loadComboApplications() {
	String queryLoadApplications = "SELECT                " + "	idapplication_tab," + "	name,0 " + "FROM                " + "	tabs.application_tabs " + "WHERE                " + "       idapplication_tab<>0 AND" + "	idapplication_tab NOT IN (SELECT     " + "					tmpResult.idapplication_tab" + "				FROM        " + "					(SELECT                " + "					*,               " + "					CASE WHEN @(alarmdate-current_date) = (SELECT min(@(alarmdate-current_date)) FROM file.alarms tmp" + "											       WHERE tmp.idfile=alarms.idfile " + "												AND tmp.idapplication_tab=alarms.idapplication_tab)    " + "					     THEN alarmdate" + "					     ELSE null                " + "					END as nextalarmdate" + "					FROM                " + "						file.alarms       " + "					WHERE                " + "						idfile=" + idFile + ") AS tmpResult " + "				WHERE        " + "					nextalarmdate IS NOT NULL)";
	//System.out.println(queryLoadApplications);
	cbExternal.loadJCombo(queryLoadApplications);
	cbApplications.setCombo(cbExternal);
    }

    private void setAddAction(boolean _actionAdd) {
	btnSave.setEnabled(!_actionAdd);
	btnCancel.setEnabled(!_actionAdd);
	btnAddApplication.setEnabled(_actionAdd);
	tfApplications.setVisible(!_actionAdd);
	cbApplications.setVisible(_actionAdd);
	tfDate.setEnabled(_actionAdd);
	if (_actionAdd) {
	    jLabel2.setText(" Agregar nuevo Tramite ");
	    jLabel2.setSize(new Dimension(165, 15));
	    tfDate.setValue("");
	    tfDescription.setValue("");
	    tfStartDate.setValue("");
	    tfEndDate.setValue("");
	    cbApplications.setSelectedID("1");
	} else {
	    jLabel2.setText(" Editar Tramite ");
	    jLabel2.setSize(new Dimension(105, 15));
	}
	btnWizard.setEnabled(!_actionAdd);
    }

    private void loadList() {
	jpList.Redimensiona();
	loadListListener();
	setHeaderList();
	getList();
	jpList.getTable().setDefaultRenderer(JPColorLabel.class, new ExpiredTableCellRenderer());
    }

    private void loadListListener() {
	jpList.getTable().addKeyListener(new KeyAdapter() {

				      public void keyPressed(KeyEvent e) {
					  jpList_keyTyped(e);
				      }

				      public void keyReleased(KeyEvent e) {
					  jpList_keyTyped(e);
				      }

				      public void keyTyped(KeyEvent e) {
					  jpList_keyTyped(e);
				      }

				  }
	);
	jpList.getTable().addMouseListener(new MouseAdapter() {

					public void mouseClicked(MouseEvent e) {
					    if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
						dataRow = jpList.VDatos();
						setAddAction(false);
						tfDate.setValue(dataRow.elementAt(8).toString());
						tfApplications.setValue(dataRow.elementAt(9).toString());
						tfStartDate.setValue(dataRow.elementAt(13).toString());
						tfEndDate.setValue(dataRow.elementAt(14).toString());
						tfDescription.setValue(dataRow.elementAt(15).toString());
						findStep();
					    } else if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
						dataRow = jpList.VDatos();
					    }
					}

				    }
	);
    }

    private void setHeaderList() {
	headerList.removeAllElements();
	headerList.addElement("*");
	headerList.addElement("*");
	headerList.addElement("*");
	headerList.addElement("*");
	headerList.addElement("*");
	headerList.addElement("*");
	headerList.addElement("*");
	headerList.addElement("Color");
	headerList.addElement("Prï¿½x Vto.");
	headerList.addElement("Tramite");
	headerList.addElement("*");
	headerList.addElement("*");
	headerList.addElement("*");
	headerList.addElement("Fecha Inicio");
	headerList.addElement("Fecha Fin");
	headerList.addElement("*");
	headerList.addElement("*");
    }

    private void getList() {
	String query = "";
	boolean alarmexist = LibSQL.getBoolean("file.alarmsexist", idfile);
	if (alarmexist) {
	    query = "SELECT         " + "	tmpResult.idalarm,       " + "	tmpResult.idfile,       " + "	tmpResult.idpriority_tab,       " + "	priority_tabs.colorred,       " + "	priority_tabs.colorgreen,       " + "	priority_tabs.colorblue,       " + "	fileapplications.idapplication_tab,       " + "	'Color:' || priority_tabs.colorred || ',' || priority_tabs.colorgreen || ',' || priority_tabs.colorblue AS coloralarm,        " + "	tmpResult.alarmdate,       " + "	application_tabs.name,       " + "	fileapplications.idapplication,       " + "	tmpResult.customalarmdate,       " + "	tmpResult.nextalarmdate," + "	fileapplications.startdate," + "	fileapplications.enddate," + "	fileapplications.description,       " + "	tmpResult.estado " + "FROM        " + "	tabs.application_tabs,tabs.priority_tabs,file.fileapplications," + "	(SELECT                " + "		*,               " + "		CASE WHEN @(alarmdate-current_date) = (SELECT min(@(alarmdate-current_date)) FROM file.alarms tmp      " + "						      WHERE tmp.idfile=alarms.idfile " + "							    AND tmp.idapplication_tab=alarms.idapplication_tab)    " + "		THEN alarmdate                " + "		ELSE NULL" + "		END AS nextalarmdate       " + "	FROM                " + "		file.alarms       " + "	WHERE                " + "		idfile=" + idFile + ") AS tmpResult " + "WHERE        " + "	nextalarmdate IS NOT NULL  " + queryFilter + "	AND tmpResult.idapplication_tab = application_tabs.idapplication_tab       " + "	AND tmpResult.idpriority_tab = priority_tabs.idpriority_tab " + "	AND tmpResult.idfile = fileapplications.idfile" + "	AND tmpResult.idapplication_tab = fileapplications.idapplication_tab" + "	AND tmpResult.idapplication = fileapplications.idapplication " + "ORDER BY " + "	nextalarmdate ASC";
	} else {
	    query = "SELECT         " + "       tmpResult.idalarm,       " + "       fileapplications.idfile,       " + "       tmpResult.idpriority_tab,       " + "       priority_tabs.colorred,       " + "       priority_tabs.colorgreen,       " + "       priority_tabs.colorblue,       " + "       fileapplications.idapplication_tab,       " + "       PUBLIC.ISNULL('Color:' || priority_tabs.colorred || ',' || priority_tabs.colorgreen || ',' || priority_tabs.colorblue,'') AS coloralarm,        " + "       tmpResult.alarmdate,       " + "       application_tabs.name,       " + "       fileapplications.idapplication,       " + "       tmpResult.customalarmdate,       " + "       tmpResult.nextalarmdate," + "       fileapplications.startdate," + "       fileapplications.enddate," + "       fileapplications.description,       " + "       tmpResult.estado " + "FROM        " + "       (SELECT                " + "               *,               " + "               CASE WHEN @(alarmdate-current_date) = (SELECT min(@(alarmdate-current_date)) FROM file.alarms tmp      " + "                                                     WHERE tmp.idfile=alarms.idfile " + "                                                           AND tmp.idapplication_tab=alarms.idapplication_tab)    " + "               THEN alarmdate                " + "               ELSE NULL" + "               END AS nextalarmdate       " + "       FROM                " + "               file.alarms       " + "       ) AS tmpResult " + "RIGHT JOIN	" + "        file.fileapplications ON tmpResult.idfile = fileapplications.idfile " + "                              AND tmpResult.idapplication = fileapplications.idapplication   " + "                              AND tmpResult.idapplication_tab = fileapplications.idapplication_tab " + "LEFT JOIN " + "        tabs.application_tabs ON application_tabs.idapplication_tab = fileapplications.idapplication_tab	" + "LEFT JOIN	" + "        tabs.priority_tabs ON tmpResult.idpriority_tab = priority_tabs.idpriority_tab   " + "WHERE " + "		fileapplications.idfile = " + idFile + "ORDER BY   " + "        nextalarmdate ASC";
	}
	//System.out.println(query);
	String queryCount = "SELECT COUNT(*) FROM (" + query + ") as resultTableCount";
	jpList.ActualizaTabla((BasicInternalFrame)this, query, queryCount, headerList);
    }

    private void btnAddApplication_actionPerformed(ActionEvent e) {
	String alarmdate = Proced.setFormatDate(tfDate.getString(), false);
	String idapplication_tab = cbApplications.getSelectedValue().toString();
	String startDate = Proced.TransformaTexto_Null(Proced.setFormatDate(tfStartDate.getString(), false));
	String description = tfDescription.getString();
	if (Proced.compareDates(alarmdate, Proced.setFormatDate(lblDate.getText(), false)) == 2) {
	    if (Proced.compareDates(alarmdate, startDate.replaceAll("'", "")) == 2) {
		if (Proced.compareDates(startDate.replaceAll("'", ""), Proced.setFormatDate(lblDate.getText(), false)) == 2 || Proced.compareDates(startDate.replaceAll("'", ""), Proced.setFormatDate(lblDate.getText(), false)) == 0) {
		    String queryAdd = "SELECT file.addnewexpiration(" + idFile + "," + idapplication_tab + ",0,'" + alarmdate + "'," + startDate + ",'" + description + "')";
		    LibSQL.getCampo(queryAdd);
		    getList();
		    setAddAction(true);
		    if (cbApplications.getSelectedIndex() != -1)
			cbApplications.removeItemAt(cbApplications.getSelectedIndex());
		} else {
		    Advisor.messageBox("La fecha de inicio del tramite\ndebe ser mayor o igual a la fecha del Expediente", "Fecha de Inicio del Tramite no válida");
		}
	    } else {
		Advisor.messageBox("La fecha de Vencimiento\ndebe ser mayor a la fecha de inicio del tramite", "Fecha de Vencimiento no válida");
	    }
	} else {
	    Advisor.messageBox("La fecha de Vencimiento\ndebe ser mayor a la fecha del Expediente", "Fecha de Vencimiento no válida");
	}
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
	setAddAction(true);
    }

    private void btnSave_actionPerformed(ActionEvent e) {
	String alarmdate = Proced.setFormatDate(tfDate.getString(), false);
	String startDate = Proced.TransformaTexto_Null(Proced.setFormatDate(tfStartDate.getString(), false));
	String endDate = Proced.TransformaTexto_Null(Proced.setFormatDate(tfEndDate.getString(), false));
	String description = tfDescription.getString();
	String idapplication_tab = dataRow.elementAt(6).toString();
	String idapplication = dataRow.elementAt(10).toString();
	if (Proced.compareDates(alarmdate, startDate.replaceAll("'", "")) == 2) {
	    if (Proced.compareDates(startDate.replaceAll("'", ""), Proced.setFormatDate(lblDate.getText(), false)) == 2 || Proced.compareDates(startDate.replaceAll("'", ""), Proced.setFormatDate(lblDate.getText(), false)) == 0) {
		if (Proced.compareDates(endDate.replaceAll("'", ""), startDate.replaceAll("'", "")) == 2 || Proced.compareDates(endDate.replaceAll("'", ""), startDate.replaceAll("'", "")) == 0 || endDate.equals("null")) {
		    String querySet = "UPDATE file.fileapplications SET startdate=" + startDate + ",enddate=" + endDate + ",description='" + description + "' WHERE idfile=" + idFile + " AND idapplication_tab=" + idapplication_tab + " AND idapplication=" + idapplication;
		    if (LibSQL.exActualizar('a', querySet)) {
			setAddAction(true);
			getList();
		    }
		} else {
		    Advisor.messageBox("La fecha de fin del tramite\ndebe ser mayor o igual a la fecha de inicio", "Fecha de Fin del Tramite no válida");
		}
	    } else {
		Advisor.messageBox("La fecha de inicio del tramite\ndebe ser mayor o igual a la fecha del Expediente", "Fecha de Inicio del Tramite no válida");
	    }
	} else {
	    Advisor.messageBox("La fecha de Vencimiento\ndebe ser mayor a la fecha de inicio del tramite", "Fecha de Vencimiento no válida");
	}
    }

    private void xmlApplicationsListReport() {
	//tablas = 0;
	XMLWorkBook report = new XMLWorkBook();
	StringBuffer appendStyle = new StringBuffer();
	int thisSheet = 0;
	int sheetNumbers = 1;
	Vector WBQuerys[] = new Vector[sheetNumbers];
	Vector headerReport = new Vector();
	Vector dataTypes = new Vector();
	Vector stylesVector = new Vector();
	Vector widthColumnVector = new Vector();
	headerReport.add("Color");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("30");
	headerReport.add("Prï¿½x Vto.");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("80");
	headerReport.add("Tramite");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("300");
	headerReport.add("Fecha Inicio");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("100");
	headerReport.add("Fecha Fin");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("100");
	headerReport.add("Descripcion");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("200");
	report.setNombreLibro("Listado de Tramites");
	report.setNroHojas(sheetNumbers);
	try {
	    /************************************* Style for each Priority ********************************************/
	    ResultSet rsPriority = LibSQL.exQuery("SELECT * FROM tabs.priority_tabs WHERE estado<>'*'");
	    while (rsPriority.next()) {
		String colorStyle = Format.color2Hex(new Color(rsPriority.getInt("colorred"), rsPriority.getInt("colorgreen"), rsPriority.getInt("colorblue")));
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
	    }
	    /***********************************************************************************************************/
	    WBQuerys[thisSheet] = new Vector();
	    report.setTituloHoja(thisSheet, Proced.setFormatDate(Environment.currentDate, true) + " - Listado de Tramites del Expediente Nï¿½ " + fileFullNumber + " - Mina: '" + mineName + "'");
	    report.setNombreHoja(thisSheet, "Tramites");
	    appendStyle.append("<Style ss:ID=\"titleApplicationsList\">");
	    appendStyle.append(" <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\"/>");
	    appendStyle.append(" <Borders>");
	    appendStyle.append("  <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
	    appendStyle.append("  <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
	    appendStyle.append("  <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"2\"/>");
	    appendStyle.append(" </Borders>");
	    appendStyle.append(" <Font ss:FontName=\"Times New Roman\" x:Family=\"Roman\" ss:Bold=\"1\" ss:Size=\"13\"/>");
	    appendStyle.append("<NumberFormat ss:Format=\"Standard\"/>");
	    appendStyle.append(" <Interior ss:Color=\"#e7e7e7\" ss:Pattern=\"Solid\"/>");
	    appendStyle.append("</Style>");
	    report.setTitleStyle(thisSheet, "titleApplicationsList");
	    report.setCabecera(thisSheet, headerReport);
	    report.setTipoDatos(thisSheet, dataTypes);
	    report.setColores(thisSheet, 0, stylesVector);
	    report.setWidthColumn(thisSheet, widthColumnVector);
	    String query = "SELECT         " + "       tmpResult.idpriority_tab,        " + "       tmpResult.alarmdate,       " + "       application_tabs.name,       " + "       ISNULL(fileapplications.startdate,'') AS startdate," + "       ISNULL(fileapplications.enddate,'') AS enddate," + "       fileapplications.description " + "FROM        " + "       tabs.application_tabs,tabs.priority_tabs,file.fileapplications," + "       (SELECT                " + "               *,               " + "               CASE WHEN @(alarmdate-current_date) = (SELECT min(@(alarmdate-current_date)) FROM file.alarms tmp      " + "                                                     WHERE tmp.idfile=alarms.idfile " + "                                                           AND tmp.idapplication_tab=alarms.idapplication_tab)    " + "               THEN alarmdate                " + "               ELSE NULL" + "               END AS nextalarmdate       " + "       FROM                " + "               file.alarms       " + "       WHERE                " + "               idfile=" + idFile + ") AS tmpResult " + "WHERE        " + "       nextalarmdate IS NOT NULL       " + "       AND tmpResult.idapplication_tab = application_tabs.idapplication_tab       " + "       AND tmpResult.idpriority_tab = priority_tabs.idpriority_tab " + "       AND tmpResult.idfile = fileapplications.idfile" + "       AND tmpResult.idapplication_tab = fileapplications.idapplication_tab" + "       AND tmpResult.idapplication = fileapplications.idapplication " + "ORDER BY " + "       nextalarmdate ASC";
	    WBQuerys[thisSheet].add(query);
	    report.setColorColumn(thisSheet, 1);
	    report.setQuerys(thisSheet, WBQuerys[thisSheet]);
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {

	}
	report.setAppendStyle(appendStyle.toString());
	report.createWorkBook();
    }

    private void btnReport_actionPerformed(ActionEvent e) {
	xmlApplicationsListReport();
    }

    private void btnWizard_actionPerformed(ActionEvent e) {
	startWizard();
    }

    class ExpiredTableCellRenderer implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable jTable, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
	    JPColorLabel exp = (JPColorLabel)object;
	    return exp;
	}

    }

    private void initializeManagerAndClass() {
	mgmt = new Manager();
	qtySteps = 4;
	mgmt.setQtyStep(qtySteps);
	fileClass = new FileClass(idFile);
    }

    private void startWizard() {
	PanelWizard[] wizard = new PanelWizard[qtySteps];
	wizard[0] = new Step0("Tramites" + Title, parentDesktop, fileClass);
	wizard[1] = new Step1_ExplorationSteps("Cateo o Solictud de Permiso de Exploraciï¿½n" + Title, parentDesktop, mgmt, fileClass);
	wizard[2] = new Step2_DiscoveryType("Adquisición de Mina" + Title, parentDesktop, fileClass);
	wizard[3] = new Step3_MineSteps("Adquisicion de Mina por Descubrimiento" + Title, parentDesktop, fileClass);
	mgmt.setWizardSteps(wizard);
	mgmt.setStep(step);
	mgmt.setInit();
    }

    private void findStep() {
	int opcion = 0;
	opcion = Integer.parseInt(dataRow.elementAt(6).toString());
	switch (opcion) {
	    case 47 :
		if (fileClass.isMine()) {
		    step = Manager.EXPLORATIONREQUEST;
		} else {
		    step = Manager.MINESTEPS;
		}
		break;
	    case 1 :
		step = Manager.EXPLORATIONREQUEST;
		break;
	    case 2 :
		step = Manager.MINESTEPS;
		break;
	}
	//String consulta = "SELECT * FROM file.fileappliactions WHERE idfile = " + idfile;
	//System.out.println("consulta--> " + consulta);
	//ResultSet tupla = org.digitall.lib.sql.LibSQL.exQuery(consulta);
    }

    private void jpList_keyTyped(KeyEvent e) {
	try {
	    if (dataRow != jpList.VDatos()) {
		dataRow = jpList.VDatos();
	    }
	} catch (ArrayIndexOutOfBoundsException x) {
	    e.consume();
	}
    }

}

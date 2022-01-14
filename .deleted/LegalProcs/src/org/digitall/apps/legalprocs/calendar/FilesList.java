package org.digitall.apps.legalprocs.calendar;

import java.awt.Color;
import java.awt.Dimension;
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
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.digitall.apps.legalprocs.manager.ExplorationRequest;
import org.digitall.common.mapper.CoordinateViewer;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicRadioButton;
import org.digitall.lib.components.buttons.ApplicationButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.DeleteButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.buttons.ReportButton;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.data.Format;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.geo.coordinatesystems.CoordinateSystems;
import org.digitall.common.geo.mapping.BasicDrawEngine;
import org.digitall.lib.geo.mapping.classes.Layer;
import org.digitall.common.geo.mapping.components.LayerListPanel;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;
import org.digitall.lib.xml.XMLWorkBook;

public class FilesList extends BasicInternalFrame {

    private BasicPanel jPanel2 = new BasicPanel();
    private int CantReg = 200;
    private int[] tcol = { };
    private int[] tamcol = { 235, 140, 100, 90, 100, 100 };
    private Grilla jpFiles = new Grilla(CantReg, tcol, tamcol, true, false);
    private Vector headerFiles = new Vector();
    private Vector dataRow = new Vector();
    private CloseButton btnClose = new CloseButton();
    private ModifyButton btnExploration = new ModifyButton();
    private JDesktopPane parentDesktop;
    private ApplicationButton btnApplications = new ApplicationButton();
    private DeleteButton btnDelete = new DeleteButton();
    private ReportButton btnReport = new ReportButton();
    private BasicRadioButton rbProspections = new BasicRadioButton();
    private BasicRadioButton rbMines = new BasicRadioButton();
    private BasicRadioButton rbAll = new BasicRadioButton();
    private ButtonGroup typeGroup = new ButtonGroup();
    private CBInput cbProvince = new CBInput(CachedCombo.PROVINCE_TABS, "ProvinceState", false);
    private ItemListener provinceItemListener;
    /** Agregado el 21-09-2007 por Santiago
     * para poder mostrar el mapa con los catastros
     * */
    private BasicDrawEngine mapPanel = new BasicDrawEngine(new BasicLabel());
    private CoordinateViewer coordinateViewer = new CoordinateViewer();
    private JInternalFrame drawPanel;
    private JInternalFrame layerListFrame;
    private LayerListPanel layerListPanel = new LayerListPanel();

    public FilesList() {
	try {
	    parentDesktop = Environment.getDesktop("FILES");
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(814, 529));
	this.setTitle("Listado de Expedientes");
	this.setIconifiable(true);
	this.setClosable(true);
	this.setFrameIcon(IconTypes.filesIcon_16x16);
	this.getContentPane().setLayout(null);
	btnClose.setBounds(new Rectangle(755, 465, 40, 28));
	btnClose.setPreferredSize(new Dimension(48, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	btnExploration.setToolTipText("Nueva Solicitud de Permiso de Exploracion");
	btnExploration.setBounds(new Rectangle(60, 465, 40, 28));
	btnExploration.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  btnExploration_actionPerformed(e);
				      }

				  }
	);
	btnApplications.setBounds(new Rectangle(10, 465, 40, 28));
	btnApplications.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   btnApplications_actionPerformed(e);
				       }

				   }
	);
	btnDelete.setBounds(new Rectangle(160, 465, 40, 28));
	btnDelete.setSize(new Dimension(40, 28));
	btnDelete.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnDelete_actionPerformed(e);
				 }

			     }
	);
	btnReport.setBounds(new Rectangle(110, 465, 40, 28));
	btnReport.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnReport_actionPerformed(e);
				 }

			     }
	);
	rbProspections.setText("Cateos");
	rbProspections.setBounds(new Rectangle(175, 20, 90, 15));
	rbProspections.addMouseListener(new MouseAdapter() {

				     public void mouseClicked(MouseEvent e) {
					 rbProspections_mouseClicked(e);
				     }

				 }
	);
	rbMines.setText("Minas");
	rbMines.setBounds(new Rectangle(265, 20, 90, 15));
	rbMines.addMouseListener(new MouseAdapter() {

			      public void mouseClicked(MouseEvent e) {
				  rbMines_mouseClicked(e);
			      }

			  }
	);
	rbAll.setText("Ambos");
	rbAll.setBounds(new Rectangle(350, 20, 90, 15));
	jpFiles.setBounds(new Rectangle(10, 40, 765, 340));
	jPanel2.setBounds(new Rectangle(10, 55, 785, 385));
	jPanel2.setLayout(null);
	jPanel2.setBorder(BorderPanel.getBorderPanel("Expedientes:"));
	jPanel2.add(cbProvince, null);
	jPanel2.add(rbAll, null);
	jPanel2.add(rbMines, null);
	jPanel2.add(rbProspections, null);
	jPanel2.add(jpFiles, null);
	this.getContentPane().add(btnReport, null);
	this.getContentPane().add(btnDelete, null);
	this.getContentPane().add(btnApplications, null);
	this.getContentPane().add(btnExploration, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(jPanel2, null);
	typeGroup.add(rbProspections);
	typeGroup.add(rbMines);
	typeGroup.add(rbAll);
	rbAll.setSelected(true);
	rbAll.addMouseListener(new MouseAdapter() {

			    public void mouseClicked(MouseEvent e) {
				rbAll_mouseClicked(e);
			    }

			}
	);
	cbProvince.setBounds(new Rectangle(475, 5, 190, 35));
	cbProvince.autoSize();
	LoadProvinceListerner();
	loadProvince();
	setDefaultCloseOperation(BasicInternalFrame.HIDE_ON_CLOSE);
	/** Agregado el 21-09-2007 por Santiago
	 * para poder mostrar el mapa con los catastros
	 * */
	drawPanel = new JInternalFrame("Croquis de Ubicaciï¿½n");
	layerListFrame = new JInternalFrame("Listado de Layers");
	mapPanel.setCoordinateViewer(coordinateViewer);
	drawPanel.setClosable(true);
	drawPanel.setIconifiable(true);
	drawPanel.setResizable(true);
	coordinateViewer.setVisible(false);
	drawPanel.setVisible(false);
	drawPanel.setBounds(0, 0, 800, 600);
	drawPanel.getContentPane().add(mapPanel);
	/*patch
	parentDesktop.add(drawPanel);
	parentDesktop.add(coordinateViewer);
	endpatch*/
	coordinateViewer.setTitle("Coordenadas");
	Layer _argentina = new Layer("Argentina", "geo", "provincesb_ar", "the_geom", "(1=1/* admin_name = 'Jujuy'*/)", "gid");
	_argentina.setProjectionType(CoordinateSystems.LL);
	mapPanel.addLayer(_argentina);
	Layer _districts = new Layer("Departamentos", "geo", "districts_ar", "the_geom", "(1=1/*provincia = 'JUJUY'*/)", "gid");
	_districts.setProjectionType(CoordinateSystems.LL);
	//panel.addLayer(_districts);
	Layer _incahuasiBuildings = new Layer("Incahuasi Buildings", "public", "incahuasi_buildings", "wkb_geometry", "(1=1)", "ogc_fid");
	_incahuasiBuildings.setProjectionType(CoordinateSystems.UTM);
	//panel.addLayer(_incahuasiBuildings);
	parentDesktop.add(layerListFrame);
	layerListFrame.getContentPane().add(layerListPanel, null);
	layerListFrame.setBounds(0, 0, 600, 100);
	layerListFrame.setVisible(false);
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
						 dataRow = jpFiles.VDatos();
					     } else if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
						 viewApplications();
					     }
					     Vector selecteds = jpFiles.getSelecteds(0);
					     Object[] _selecteds = selecteds.toArray();
					     StringBuffer _condition = new StringBuffer();
					     int _polygons = 0;
					     if (_selecteds.length > 0) {
						 StringBuffer query = new StringBuffer();
						 query.append("SELECT idpolygon FROM file.explorationrequests ");
						 query.append("WHERE estado <> '*' AND file.explorationrequests.idfile = ");
						 query.append(_selecteds[0]);
						 String idPolygon = LibSQL.getCampo(query.toString());
						 if (!idPolygon.equals("")) {
						     _condition.append("idpolygon = " + idPolygon + " ");
						     _polygons++;
						 }
						 for (int i = 1; i < _selecteds.length; i++) {
						     query = new StringBuffer();
						     query.append("SELECT idpolygon FROM file.explorationrequests ");
						     query.append("WHERE estado <> '*' AND file.explorationrequests.idfile = ");
						     query.append(_selecteds[i]);
						     idPolygon = LibSQL.getCampo(query.toString());
						     if (!idPolygon.equals("")) {
							 _condition.append("OR idpolygon = " + idPolygon);
							 _polygons++;
						     }
						 }
					     }
					     if (_polygons > 0) {
						 mapPanel.removeLayer("Catastros");
						 Layer _layer = new Layer("Catastros", "file", "polygons", "points", "(" + _condition.toString() + ")", "idpolygon");
						 _layer.setProjectionType(CoordinateSystems.GK);
					         Environment.mainDesktop.add(drawPanel);
					         Environment.mainDesktop.add(coordinateViewer);
						 drawPanel.show();
						 coordinateViewer.show();
						 mapPanel.addLayer(_layer);
						 layerListPanel.setDrawPanel(mapPanel);
						 layerListFrame.show();
					     } else {
						 drawPanel.setVisible(false);
						 coordinateViewer.setVisible(false);
					     }
					 }

				     }
	);
	setHeaderFilesList();
	setTableFileList();
    }

    private void loadProvince() {
	int idcountry = 1;
	cbProvince.setFilter("" + idcountry);
	cbProvince.setSelectedValue("JUJUY");
	addComboItemListener();
    }

    private void setHeaderFilesList() {
	headerFiles.removeAllElements();
	headerFiles.addElement("*");
	headerFiles.addElement("Nombre de Mina");
	headerFiles.addElement("Nï¿½ Expediente");
	headerFiles.addElement("Provincia");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("Fecha");
	headerFiles.addElement("*");
	headerFiles.addElement("*");
	headerFiles.addElement("Solicitante");
	headerFiles.addElement("*");
	headerFiles.addElement("Representante");
	headerFiles.addElement("*");
    }

    private Vector createToolTipVector() {
	Vector vectorToolTip = new Vector();
	try {
	    ResultSet rsFiles = LibSQL.exQuery("SELECT idfile FROM file.files WHERE estado<>'*' AND idfile<>0 ORDER BY date DESC");
	    while (rsFiles.next()) {
		StringBuffer textToolTip = new StringBuffer("");
		int indexStyle = 0;
		String Style = "style='background-color:#FFFFFF'";
		textToolTip.append("<html>");
		textToolTip.append("<table border='0' cellpadding='0' cellspacing='0' width='500'>");
		textToolTip.append("<tr style='background-color:#303030;COLOR:#FFFFFF'>");
		textToolTip.append("<td width='10'>&nbsp;</td>");
		textToolTip.append("<td width='110' colspan='3' align='left'><b>Prox. Vto.</b></td>");
		textToolTip.append("<td width='25'>&nbsp;</td>");
		textToolTip.append("<td align='left'><b>Tramite</b></td>");
		textToolTip.append("<td width='10'>&nbsp;</td>");
		textToolTip.append("</tr>");
		String queryToolTip = "SELECT " + "       alarms.idfile," + "	priority_tabs.colorred," + "	priority_tabs.colorgreen," + "	priority_tabs.colorblue," + "	alarms.alarmdate," + "	application_tabs.name " + "FROM " + "	file.alarms,tabs.application_tabs,tabs.priority_tabs,file.fileapplications " + "WHERE " + "	alarms.idfile = " + rsFiles.getString("idfile") + "	AND @(alarmdate-current_date) = (SELECT min(@(alarmdate-current_date)) FROM file.alarms tmp      	" + "					WHERE tmp.idfile=" + rsFiles.getString("idfile") + ")" + "       AND alarms.idapplication_tab = fileapplications.idapplication_tab" + "       AND alarms.idfile = fileapplications.idfile" + "       AND fileapplications.idapplication_tab = application_tabs.idapplication_tab" + "       AND alarms.idpriority_tab = priority_tabs.idpriority_tab";
		//System.out.println(queryToolTip);
		ResultSet rsToolTip = LibSQL.exQuery(queryToolTip);
		while (rsToolTip.next()) {
		    if ((indexStyle % 2) == 0)
			Style = "style='background-color:#d7d7d7'";
		    else
			Style = "style='background-color:#FFFFFF'";
		    Color colorAlarm = new Color(rsToolTip.getInt("colorred"), rsToolTip.getInt("colorgreen"), rsToolTip.getInt("colorblue"));
		    String color = Format.color2Hex(colorAlarm);
		    String alarmDate = rsToolTip.getString("alarmdate");
		    String applicationName = rsToolTip.getString("name");
		    textToolTip.append("<tr " + Style + " height='22'>");
		    textToolTip.append("<td width='10'>&nbsp;</td>");
		    textToolTip.append("<td width='15' bgcolor='#" + color + "'>&nbsp;</td>");
		    textToolTip.append("<td width='5'></td>");
		    textToolTip.append("<td>" + alarmDate + "</td>");
		    textToolTip.append("<td>&nbsp;</td>");
		    textToolTip.append("<td>" + applicationName + "</td>");
		    textToolTip.append("<td width='10'>&nbsp;</td>");
		    textToolTip.append("</tr>");
		    indexStyle += 1;
		}
		textToolTip.append("</table>");
		textToolTip.append("</html>");
		if (indexStyle == 0)
		    vectorToolTip.add("");
		else
		    vectorToolTip.add(textToolTip);
	    }
	} catch (SQLException x) {
	    x.printStackTrace();
	}
	return vectorToolTip;
    }

    private void setTableFileList() {
	String filtroTipoExpte = "";
	String filtroOrderBY = "";
	String filtroProvince = "";
	String query = "SELECT " + "       files.idfile, " + "       files.minename, " + "       files.filenumber ||'-'|| files.fileletter ||'-'|| files.fileyear AS filefullnumber," + "       provinces.name as province,   " + "       files.filenumber,    " + "       files.fileletter,   " + "       files.fileyear,  " + "       files.date," + "       files.description," + "       public.ISNULL(solic.identity,0) AS idsolic," + "       public.ISNULL(CASE WHEN solic.person THEN " + "           (Select lastname ||', '|| name from org.persons where persons.idperson = solic.idreference) " + "       ELSE " + "        (Select name from org.companies where companies.idcompany = solic.idreference) " + "       END,'No Asignado')  as solicName, " + "       public.ISNULL(rep.identity,0) AS idrep, " + "       public.ISNULL(CASE WHEN rep.person THEN " + "        (Select lastname ||', '|| name from org.persons where persons.idperson = rep.idreference) " + "       ELSE " + "           (Select name from org.companies where companies.idcompany = rep.idreference) " + "       END,'No Asignado')   as repName, " + "       files.estado " + "FROM        " + "       file.files " + "       LEFT JOIN file.entitiesbyfile solicbyfile ON files.idfile = solicbyfile.idfile AND solicbyfile.solicitor = true " + "       LEFT JOIN file.entitiesbyfile repbyfile ON files.idfile = repbyfile.idfile AND repbyfile.solicitor = false " + "       LEFT JOIN file.entities solic ON solic.identity = solicbyfile.identity " + "       LEFT JOIN file.entities rep ON rep.identity = repbyfile.identity  " + "       LEFT JOIN tabs.province_tabs provinces ON files.idprovince = provinces.idprovince   " + "WHERE               " + "        files.estado <> '*' " + "        AND files.idfile <> 0 ";
	if (rbProspections.isSelected()) {
	    filtroTipoExpte = " AND mine = false";
	} else if (rbMines.isSelected()) {
	    filtroTipoExpte = " AND mine = true";
	} else {
	    filtroTipoExpte = "";
	}
	//System.out.println("idprovince: " + cbProvince.getSelectedValue());
	if (!cbProvince.getSelectedValue().equals("-1")) {
	    filtroProvince = " AND files.idprovince = " + cbProvince.getSelectedValue().toString();
	}
	filtroOrderBY = " ORDER BY date DESC";
	query = query + filtroTipoExpte + filtroProvince + filtroOrderBY;
	String queryCount = "SELECT COUNT(*) FROM (" + query + ") as tableResult";
	jpFiles.ActualizaTabla((BasicInternalFrame)this, query, queryCount, headerFiles);
	jpFiles.setColumnToolTip(1);
	jpFiles.setColumnaEditable(6);
	jpFiles.setVectorToolTip(createToolTipVector());
    }

    private void jpFiles_keyTyped(KeyEvent e) {
	try {
	    if (dataRow != jpFiles.VDatos()) {
		dataRow = jpFiles.VDatos();
	    }
	} catch (ArrayIndexOutOfBoundsException x) {
	    e.consume();
	}
    }

    private void btnExploration_actionPerformed(ActionEvent e) {
	ExplorationRequest explorationRequest = new ExplorationRequest("");
	parentDesktop.add(explorationRequest);
	explorationRequest.show();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void viewApplications() {
	dataRow = jpFiles.VDatos();
	if (!dataRow.isEmpty()) {
	    String idfile = dataRow.elementAt(0).toString();
	    int idFile = -1;
	    try {
		idFile = Integer.parseInt(idfile);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    String mineName = dataRow.elementAt(1).toString();
	    String fileFullNumber = dataRow.elementAt(2).toString();
	    String dateFile = dataRow.elementAt(6).toString();
	    //FileApplications fileApplications = new FileApplications(idfile,fileFullNumber,mineName,dateFile,idFile);
	    FileApplications fileApplications = new FileApplications(fileFullNumber, mineName, dateFile, idFile);
	    parentDesktop.add(fileApplications);
	    fileApplications.show();
	}
    }

    private void btnApplications_actionPerformed(ActionEvent e) {
	viewApplications();
    }

    private void btnDelete_actionPerformed(ActionEvent e) {
	dataRow = jpFiles.VDatos();
	if (!dataRow.isEmpty()) {
	    String idfile = dataRow.elementAt(0).toString();
	    String fileFullNumber = dataRow.elementAt(2).toString();
	    String[] deleteFile = LibSQL.getCampo("SELECT file.deletefileisposible(" + idfile + ")").split(",");
	    String errorMsg = "Imposible de borrar este expediente debido a: \n";
	    String deleteItems = "";
	    if (!deleteFile[0].equals("0"))
		deleteItems += "- Existen " + deleteFile[0] + " Solicitante/s o Representante/s relacionado/s.\n";
	    if (!deleteFile[1].equals("0"))
		deleteItems += "- Contempla " + deleteFile[1] + " Solicitud/es de permiso/s de Exploracion.\n";
	    if (!deleteFile[2].equals("0"))
		deleteItems += "- Contempla " + deleteFile[2] + " Labor Legal.\n";
	    if (!deleteFile[3].equals("0"))
		deleteItems += "- Contempla " + deleteFile[3] + " Peticion/es de Mensura.\n";
	    if (!deleteFile[4].equals("0"))
		deleteItems += "- Contempla " + deleteFile[4] + " Manifestaciï¿½n de Descubrimiento (PMD).\n";
	    int otherApplications = Integer.parseInt(deleteFile[5]) - Integer.parseInt(deleteFile[1]) - Integer.parseInt(deleteFile[2]) - Integer.parseInt(deleteFile[3]) - Integer.parseInt(deleteFile[4]);
	    if (otherApplications != 0)
		deleteItems += "- Contempla " + String.valueOf(otherApplications) + " Tramite/s (otros), con " + deleteFile[6] + " Alarma/s relacionada/s.\n";
	    if (!deleteFile[7].equals("0"))
		deleteItems += "- Contempla " + deleteFile[7] + " Intimacion/es relacionado/s.\n";
	    if (deleteItems.length() > 0)
		Advisor.messageBox(errorMsg + deleteItems, "Imposible de borrar el Expediente Nï¿½ " + fileFullNumber);
	    else
		Advisor.messageBox("Borrado", "");
	    //org.digitall.lib.sql.LibSQL.getCampo("SELECT file.deletefile("+ idfile +")");
	}
    }

    private void xmlFilesListReport() {
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
	headerReport.add("Nombre de Mina");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("200");
	headerReport.add("Nï¿½ Expediente");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("80");
	headerReport.add("Fecha");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("80");
	headerReport.add("Representante");
	dataTypes.add("String");
	stylesVector.add("Normal8");
	widthColumnVector.add("200");
	report.setNombreLibro("Listado de Expedientes");
	report.setNroHojas(sheetNumbers);
	try {
	    WBQuerys[thisSheet] = new Vector();
	    report.setTituloHoja(thisSheet, Proced.setFormatDate(Environment.currentDate, true) + " - Listado de Expedientes");
	    report.setNombreHoja(thisSheet, "Expedientes");
	    appendStyle.append("<Style ss:ID=\"titleFilesList\">");
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
	    report.setTitleStyle(thisSheet, "titleFilesList");
	    report.setCabecera(thisSheet, headerReport);
	    report.setTipoDatos(thisSheet, dataTypes);
	    report.setColores(thisSheet, 0, stylesVector);
	    report.setWidthColumn(thisSheet, widthColumnVector);
	    String query =
		//"       public.ISNULL(rep.lastname ||', '|| rep.firstname,'') AS repname " +
		"SELECT               " + "       files.minename," + "       files.filenumber ||'-'|| files.fileletter ||'-'|| files.fileyear AS filefullnumber," + "       isnull(files.date,'') AS date," + "       public.ISNULL(CASE WHEN rep.person " + "                           THEN " + "                       	(Select lastname ||', '|| name from org.persons where persons.idperson = rep.idreference) " + "                           ELSE " + "                               (Select name from org.companies where companies.idcompany = rep.idreference) " + "                           END,'No Asignado')   as repName  " + "FROM        " + "       file.files" + "       LEFT JOIN file.entitiesbyfile solicbyfile ON files.idfile = solicbyfile.idfile AND solicbyfile.solicitor = true" + "       LEFT JOIN file.entitiesbyfile repbyfile ON files.idfile = repbyfile.idfile AND repbyfile.solicitor = false" + "       LEFT JOIN file.entities solic ON solic.identity = solicbyfile.identity" + "       LEFT JOIN file.entities rep ON rep.identity = repbyfile.identity " + "WHERE               " + "       files.estado<>'*' " + "       AND files.idfile<>0 " + "ORDER BY date DESC";
	    //System.out.println("query--> " + query);
	    WBQuerys[thisSheet].add(query);
	    report.setQuerys(thisSheet, WBQuerys[thisSheet]);
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {
	}
	report.setAppendStyle(appendStyle.toString());
	report.createWorkBook();
    }

    private void btnReport_actionPerformed(ActionEvent e) {
	xmlFilesListReport();
    }

    private void rbProspections_mouseClicked(MouseEvent e) {
	setTableFileList();
    }

    private void rbMines_mouseClicked(MouseEvent e) {
	setTableFileList();
    }

    private void rbAll_mouseClicked(MouseEvent e) {
	setTableFileList();
    }

    private void LoadProvinceListerner() {
	provinceItemListener = new ItemListener() {

		public void itemStateChanged(ItemEvent evt) {
		    if (evt.getStateChange() == ItemEvent.SELECTED) {
			setTableFileList();
		    }
		}

	    }
	;
    }

    public void addComboItemListener() {
	cbProvince.setItemListener(provinceItemListener);
    }

}

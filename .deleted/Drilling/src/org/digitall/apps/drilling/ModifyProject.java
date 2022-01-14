package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.Coordinate;
import org.digitall.common.drilling.PanelHeader;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.JDecEntry;
import org.digitall.lib.components.JTFecha;
import org.digitall.lib.components.List;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;

//

public class ModifyProject extends BasicDialog {

    private BasicLabel lblDate = new BasicLabel();
    private BasicLabel lblStartUtm = new BasicLabel();
    private BasicLabel lblEndUtm = new BasicLabel();
    private BasicLabel lblZone = new BasicLabel();
    private BasicLabel lblArea = new BasicLabel();
    private BasicLabel lblDescription = new BasicLabel();
    private BasicLabel lblMin = new BasicLabel();
    private BasicLabel lblAlterations = new BasicLabel();
    private BasicLabel lblMinAll = new BasicLabel();
    private BasicLabel lblAltAll = new BasicLabel();
    private BasicLabel lblProject = new BasicLabel();
    private AcceptButton bOk = new AcceptButton();
    private CloseButton bCancel = new CloseButton();
    private BasicButton bStartUtm = new ModifyButton();
    private ModifyButton bModMin = new ModifyButton();
    private ModifyButton bModAlt = new ModifyButton();
    private ModifyButton bEndUtm = new ModifyButton();
    private ModifyButton btnQualityControl = new ModifyButton();
    private JTFecha tfDate = new JTFecha();
    private BasicTextField tfStartUtm = new BasicTextField();
    private BasicTextField tfEndUtm = new BasicTextField();
    private BasicTextField tfZone = new BasicTextField();
    private BasicTextField tfArea = new BasicTextField();
    private BasicTextField tfProject = new BasicTextField();
    private JDecEntry tfStartUtmX = new JDecEntry();
    private JDecEntry tfStartUtmY = new JDecEntry();
    private BasicScrollPane spDescription = new BasicScrollPane();
    private JTextArea taDescription = new JTextArea();
    private BasicScrollPane spMineralizations = new BasicScrollPane();
    private BasicScrollPane spAlterations = new BasicScrollPane();
    private JList listMineralizations = new JList();
    private JList listAlterations = new JList();
    List mineralizationsList = new List();
    List alterationsList = new List();
    private String idproject = "";
    final String TEXTO = "Modify Project Data";
    private PanelHeader panelHeader;
    private BasicPanel jPanel1 = new BasicPanel();
    private Vector auxListMin, auxListAlt;
    private Coordinate startUTM = new Coordinate.Double();
    private Coordinate endUTM = new Coordinate.Double();
     
    private BasicLabel lblProject5 = new BasicLabel();
    private BasicLabel lblMin1 = new BasicLabel();

    public ModifyProject(String _idproject) {
	try {
	    auxListMin = new Vector();
	    auxListAlt = new Vector();
	    idproject = _idproject;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(473, 436));
	this.setBounds(new Rectangle(10, 10, 473, 436));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblProject5, null);
	//
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(jPanel1, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bOk, null);
	this.getContentPane().setLayout(null);
	this.setTitle("Modify Project");
	jPanel1.add(lblMin1, null);
	jPanel1.add(btnQualityControl, null);
	jPanel1.add(bEndUtm, null);
	jPanel1.add(lblAltAll, null);
	jPanel1.add(lblMinAll, null);
	jPanel1.add(lblAlterations, null);
	jPanel1.add(lblMin, null);
	spAlterations.getViewport().add(listAlterations, null);
	jPanel1.add(spAlterations, null);
	spMineralizations.getViewport().add(listMineralizations, null);
	jPanel1.add(spMineralizations, null);
	spDescription.getViewport().add(taDescription, null);
	jPanel1.add(spDescription, null);
	jPanel1.add(tfStartUtm, null);
	jPanel1.add(bStartUtm, null);
	jPanel1.add(lblProject, null);
	jPanel1.add(tfProject, null);
	jPanel1.add(lblDescription, null);
	jPanel1.add(lblArea, null);
	jPanel1.add(tfArea, null);
	jPanel1.add(lblZone, null);
	jPanel1.add(tfZone, null);
	jPanel1.add(lblEndUtm, null);
	jPanel1.add(tfEndUtm, null);
	jPanel1.add(lblStartUtm, null);
	jPanel1.add(lblDate, null);
	jPanel1.add(tfDate, null);
	jPanel1.add(bModMin, null);
	jPanel1.add(bModAlt, null);
	tfProject.setBounds(new Rectangle(75, 5, 125, 20));
	tfProject.setBackground(Color.white);
	tfProject.setFont(new Font("Dialog", 1, 10));
	tfProject.setForeground(Color.red);
	bOk.setBounds(new Rectangle(415, 380, 45, 25));
	bOk.setFont(new Font("Dialog", 1, 10));
	bOk.setMnemonic('O');
	bOk.setToolTipText("Ok");
	bOk.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bOk_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(10, 380, 45, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setMnemonic('C');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	tfDate.setBounds(new Rectangle(300, 5, 85, 20));
	tfDate.setEditable(false);
	tfDate.setBackground(Color.white);
	tfDate.setFont(new Font("Dialog", 1, 10));
	tfDate.setForeground(Color.red);
	lblDate.setText("Date:");
	lblDate.setBounds(new Rectangle(265, 10, 30, 15));
	lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
	lblDate.setFont(new Font("Dialog", 1, 10));
	tfStartUtm.setBounds(new Rectangle(75, 30, 310, 20));
	tfStartUtm.setEditable(false);
	tfStartUtm.setBackground(Color.white);
	tfStartUtm.setFont(new Font("Dialog", 1, 10));
	tfStartUtm.setForeground(Color.red);
	lblStartUtm.setText("Start UTM:");
	lblStartUtm.setBounds(new Rectangle(15, 35, 55, 10));
	lblStartUtm.setHorizontalAlignment(SwingConstants.RIGHT);
	lblStartUtm.setFont(new Font("Dialog", 1, 10));
	tfEndUtm.setBounds(new Rectangle(75, 55, 310, 20));
	tfEndUtm.setEditable(false);
	tfEndUtm.setBackground(Color.white);
	tfEndUtm.setFont(new Font("Dialog", 1, 10));
	tfEndUtm.setForeground(Color.red);
	lblEndUtm.setText("End UTM:");
	lblEndUtm.setBounds(new Rectangle(20, 60, 50, 10));
	lblEndUtm.setHorizontalAlignment(SwingConstants.RIGHT);
	lblEndUtm.setFont(new Font("Dialog", 1, 10));
	tfZone.setBounds(new Rectangle(75, 80, 125, 20));
	tfZone.setBackground(Color.white);
	tfZone.setFont(new Font("Dialog", 1, 10));
	tfZone.setForeground(Color.red);
	lblZone.setText("Zone:");
	lblZone.setBounds(new Rectangle(40, 85, 30, 10));
	lblZone.setHorizontalAlignment(SwingConstants.RIGHT);
	lblZone.setFont(new Font("Dialog", 1, 10));
	tfArea.setBounds(new Rectangle(300, 80, 125, 20));
	tfArea.setBackground(Color.white);
	tfArea.setFont(new Font("Dialog", 1, 10));
	tfArea.setForeground(Color.red);
	lblArea.setText("Area:");
	lblArea.setBounds(new Rectangle(255, 85, 35, 10));
	lblArea.setHorizontalAlignment(SwingConstants.RIGHT);
	lblArea.setFont(new Font("Dialog", 1, 10));
	spDescription.setBounds(new Rectangle(75, 105, 350, 50));
	//spDescription.getViewport().setLayout(null);
	taDescription.setBounds(new Rectangle(0, 0, 350, 50));
	taDescription.setFont(new Font("Dialog", 1, 10));
	taDescription.setForeground(Color.red);
	lblDescription.setText("Description:");
	lblDescription.setBounds(new Rectangle(5, 105, 65, 10));
	lblDescription.setHorizontalAlignment(SwingConstants.RIGHT);
	lblDescription.setFont(new Font("Dialog", 1, 10));
	spMineralizations.setBounds(new Rectangle(100, 165, 105, 110));
	//spMineralizations.getViewport().setLayout(null);
	spAlterations.setBounds(new Rectangle(300, 165, 105, 110));
	//spAlterations.getViewport().setLayout(null);
	listMineralizations.setBounds(new Rectangle(0, 0, 105, 110));
	listMineralizations.setFont(new Font("Dialog", 1, 10));
	listAlterations.setBounds(new Rectangle(35, 0, 105, 110));
	listAlterations.setFont(new Font("Dialog", 1, 10));
	jPanel1.setBounds(new Rectangle(10, 45, 450, 315));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	bEndUtm.setBounds(new Rectangle(395, 55, 30, 20));
	bEndUtm.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bEndUtm_actionPerformed(e);
		    }

		});
	//new Rectangle(0, 370, 460, 5));
	lblProject5.setText(" Details");
	lblProject5.setBounds(new Rectangle(15, 35, 50, 15));
	lblProject5.setHorizontalAlignment(SwingConstants.LEFT);
	lblProject5.setFont(new Font("Default", 1, 10));
	lblProject5.setOpaque(true);
	btnQualityControl.setBounds(new Rectangle(210, 285, 30, 20));
	btnQualityControl.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnQualityControl_actionPerformed(e);
		    }

		});
	lblMin1.setText("Quality Control Procedure:");
	lblMin1.setBounds(new Rectangle(0, 290, 200, 10));
	lblMin1.setHorizontalAlignment(SwingConstants.RIGHT);
	lblMin1.setFont(new Font("Dialog", 1, 10));
	bModMin.setBounds(new Rectangle(210, 205, 30, 20));
	bModMin.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bModMin_actionPerformed(e);
		    }

		});
	bModAlt.setBounds(new Rectangle(410, 205, 30, 20));
	bModAlt.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bModAlt_actionPerformed(e);
		    }

		});
	bStartUtm.setBounds(new Rectangle(395, 30, 30, 20));
	bStartUtm.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bStartUtm_actionPerformed(e);
		    }

		});
	lblProject.setText("Project:");
	lblProject.setBounds(new Rectangle(15, 10, 55, 10));
	lblProject.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProject.setFont(new Font("Dialog", 1, 10));
	lblMin.setText("Mineralizations");
	lblMin.setBounds(new Rectangle(5, 165, 90, 10));
	lblMin.setHorizontalAlignment(SwingConstants.RIGHT);
	lblMin.setFont(new Font("Dialog", 1, 10));
	lblAlterations.setText("Alterations");
	lblAlterations.setBounds(new Rectangle(230, 165, 65, 10));
	lblAlterations.setHorizontalAlignment(SwingConstants.RIGHT);
	lblAlterations.setFont(new Font("Dialog", 1, 10));
	lblMinAll.setText("Allowed:");
	lblMinAll.setBounds(new Rectangle(5, 175, 90, 10));
	lblMinAll.setHorizontalAlignment(SwingConstants.RIGHT);
	lblMinAll.setFont(new Font("Dialog", 1, 10));
	lblAltAll.setText("allowed:");
	lblAltAll.setBounds(new Rectangle(230, 175, 65, 10));
	lblAltAll.setHorizontalAlignment(SwingConstants.RIGHT);
	lblAltAll.setFont(new Font("Dialog", 1, 10));
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	setearDatos();
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void setearDatos() {
	try {
	    String consulta = "SELECT idproject, name, description, date, zone, area, X(startutm) as startX," + "Y(startutm) as startY, X(endutm) as endX, Y(endutm) as endY" + " FROM drilling.projects WHERE idproject = '" + idproject + "' ";
	    //System.out.println("consulta: " + consulta);
	    ResultSet resul = org.digitall.lib.sql.LibSQL.exQuery(consulta);
	    if (resul.next()) {
		try {
		    if (!resul.getString("startX").equals("null")) {
			startUTM = new Coordinate.Double(resul.getDouble("startX"), resul.getDouble("startY"));
		    }
		} catch (NullPointerException ex) {
		    startUTM.setNull(true);
		}
		try {
		    if (!resul.getString("endX").equals("null")) {
			endUTM = new Coordinate.Double(resul.getDouble("endX"), resul.getDouble("endY"));
		    }
		} catch (NullPointerException ex) {
		    endUTM.setNull(true);
		}
		setStartUTM();
		setEndUTM();
		tfProject.setText(resul.getString("name"));
		tfDate.setValue(Proced.setFormatDate(resul.getString("date"), true));
		tfZone.setText(resul.getString("zone"));
		tfArea.setText(resul.getString("area"));
		taDescription.setText(resul.getString("description"));
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	String Q = "SELECT allowed_mineralizations.idmin_tab as id, tabs.mineralization_tabs.name as dato" + " FROM tabs.mineralization_tabs, drilling.allowed_mineralizations" + " WHERE allowed_mineralizations.status <> '*' " + " AND allowed_mineralizations.idmin_tab > 0" + " AND tabs.mineralization_tabs.idmin_tab = drilling.allowed_mineralizations.idmin_tab" + " AND idproject = '" + idproject + "'" + " Order By dato";
	//System.out.println("A--> " + Q);
	mineralizationsList.getListFromQuery(Q);
	listMineralizations.setListData(mineralizationsList.getNombres());
	Q = "SELECT allowed_alterations.idalt_tab as id, tabs.alteration_tabs.name as dato" + " FROM tabs.alteration_tabs, drilling.allowed_alterations" + " WHERE allowed_alterations.status <> '*' " + " AND allowed_alterations.idalt_tab > 0" + " AND tabs.alteration_tabs.idalt_tab = drilling.allowed_alterations.idalt_tab" + " AND idproject = '" + idproject + "'" + " Order By dato";
	alterationsList.getListFromQuery(Q);
	listAlterations.setListData(alterationsList.getNombres());
	auxListAlt = alterationsList.getIds();
	auxListMin = mineralizationsList.getIds();
    }

    private void bOk_actionPerformed(ActionEvent e) {
	String startUtm = "NULL";
	String endUtm = "NULL";
	if (!startUTM.isNull()) {
	    startUtm = "GeometryFromText('POINT(" + startUTM.getX() + " " + startUTM.getY() + ")',-1)";
	}
	if (!endUTM.isNull()) {
	    endUtm = "GeometryFromText('POINT(" + endUTM.getX() + " " + endUTM.getY() + ")',-1)";
	}
	String consultaUpdate = "UPDATE drilling.projects SET " + " name = '" + tfProject.getText() + "'," + " date = '" + Proced.setFormatDate(tfDate.getText(), false) + "'," + " startutm = " + startUtm + "," + " endutm = " + endUtm + "," + " zone = '" + tfZone.getText() + "'," + " area = '" + tfArea.getText() + "'," + " description = '" + taDescription.getText() + "'" + " WHERE idproject = " + idproject;
	//System.out.println("consultaUpdate--> "+consultaUpdate);
	if (LibSQL.exActualizar('m', consultaUpdate)) {
	    actualizarAltMin();
	    org.digitall.lib.components.Advisor.messageBox("Update Success!", "Message");
	    this.dispose();
	}
    }

    private void bStartUtm_actionPerformed(ActionEvent e) {
	tfStartUtmX.setText(String.valueOf(startUTM.getX()));
	tfStartUtmY.setText(String.valueOf(startUTM.getY()));
	UtmCoordinate utm = new UtmCoordinate(startUTM);
	utm.setModal(true);
	utm.setVisible(true);
	setStartUTM();
    }

    private void bEndUtm_actionPerformed(ActionEvent e) {
	UtmCoordinate utm = new UtmCoordinate(endUTM);
	utm.setModal(true);
	utm.setVisible(true);
	setEndUTM();
    }

    private void bModAlt_actionPerformed(ActionEvent e) {
	ChangeAltMin change = new ChangeAltMin(alterationsList, 2, idproject);
	change.setModal(true);
	change.setVisible(true);
	listAlterations.setListData(alterationsList.getNombres());
    }

    private void bModMin_actionPerformed(ActionEvent e) {
	ChangeAltMin change = new ChangeAltMin(mineralizationsList, 1, idproject);
	change.setModal(true);
	change.setVisible(true);
	listMineralizations.setListData(mineralizationsList.getNombres());
    }

    private void actualizarAltMin() {
	Conjunto conjunto = new Conjunto();
	Vector agregar = new Vector();
	Vector eliminar = new Vector();
	agregar = conjunto.diferencia(mineralizationsList.getIds(), auxListMin);
	eliminar = conjunto.diferencia(auxListMin, mineralizationsList.getIds());
	if (agregar.size() > 0) {
	    for (int i = 0; i < agregar.size(); i++) {
		addMineralization(agregar.elementAt(i).toString());
	    }
	}
	if (eliminar.size() > 0) {
	    for (int i = 0; i < eliminar.size(); i++) {
		removeMineralization(eliminar.elementAt(i).toString());
	    }
	}
	agregar = conjunto.diferencia(alterationsList.getIds(), auxListAlt);
	eliminar = conjunto.diferencia(auxListAlt, alterationsList.getIds());
	if (agregar.size() > 0) {
	    for (int i = 0; i < agregar.size(); i++) {
		this.addAlteration(agregar.elementAt(i).toString());
	    }
	}
	if (eliminar.size() > 0) {
	    for (int i = 0; i < eliminar.size(); i++) {
		removeAlteration(eliminar.elementAt(i).toString());
	    }
	}
    }

    private void addMineralization(String _id) {
	String consultaInsert = "";
	consultaInsert = "INSERT INTO drilling.allowed_mineralizations VALUES ((Select max(idall_min)+1 From drilling.allowed_mineralizations)" + ", " + idproject + " , " + _id + ", '')";
	LibSQL.exActualizar('a', consultaInsert);
	System.out.println("consulta = " + consultaInsert);
    }

    private void removeMineralization(String _id) {
	String consultaDelete = "UPDATE drilling.allowed_mineralizations SET status = '*' WHERE idmin_tab = " + _id + " AND idproject = " + idproject;
	LibSQL.exActualizar('a', consultaDelete);
    }

    private void addAlteration(String _id) {
	String consultaInsert = "";
	consultaInsert = "INSERT INTO drilling.allowed_alterations VALUES ((Select max(idall_alt)+1 From drilling.allowed_alterations)" + " , " + idproject + " , " + _id + ", '')";
	LibSQL.exActualizar('a', consultaInsert);
	//System.out.println("consulta = "+consultaInsert);
    }

    private void removeAlteration(String _id) {
	String consultaDelete = "UPDATE drilling.allowed_alterations SET status = '*' WHERE idalt_tab = " + _id + " AND idproject = " + idproject;
	LibSQL.exActualizar('a', consultaDelete);
    }

    private void setStartUTM() {
	if (!startUTM.isNull()) {
	    tfStartUtm.setText("Easting: " + startUTM.getX() + " - Northing: " + startUTM.getY());
	} else {
	    tfStartUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

    private void setEndUTM() {
	if (!endUTM.isNull()) {
	    tfEndUtm.setText("Easting: " + endUTM.getX() + " - Northing: " + endUTM.getY());
	} else {
	    tfEndUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

    private void btnQualityControl_actionPerformed(ActionEvent e) {
	QualityControl qualityControl = new QualityControl(idproject);
	qualityControl.setModal(true);
	qualityControl.setVisible(true);
    }

}

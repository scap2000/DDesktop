package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.List;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;

//
//public class Paso5 extends BasicDialog

public class Paso5 extends PasoAsistente {

    private BasicLabel lblProject = new BasicLabel();
    private BasicLabel lblDate = new BasicLabel();
    private BasicLabel lblProject2 = new BasicLabel();
    private BasicLabel lblEndUtm = new BasicLabel();
    private BasicLabel lblZone = new BasicLabel();
    private BasicLabel lblArea = new BasicLabel();
    private BasicLabel lblProject3 = new BasicLabel();
    private BasicLabel lblProject4 = new BasicLabel();
    private BasicLabel lblAlterations = new BasicLabel();
    private BasicLabel lblMinAll = new BasicLabel();
    private BasicLabel lblAltAll = new BasicLabel();
    private NextWizardButton bNext = new NextWizardButton();
    private PreviousWizardButton bBack = new PreviousWizardButton();
    private AcceptButton bOk = new AcceptButton();
    private CloseButton bCancel = new CloseButton();
    private BasicTextField tfDate = new BasicTextField();
    private BasicTextField tfStartUtm = new BasicTextField();
    private BasicTextField tfEndUtm = new BasicTextField();
    private BasicTextField tfZone = new BasicTextField();
    private BasicTextField tfArea = new BasicTextField();
    private BasicTextField tfProject = new BasicTextField();
    private BasicScrollPane spDescription = new BasicScrollPane();
    private JTextArea taDescription = new JTextArea();
    private BasicScrollPane spMineralizations = new BasicScrollPane();
    private BasicScrollPane spAlterations = new BasicScrollPane();
    private JList listMineralizations = new JList();
    private JList listAlterations = new JList();
    List mineralizationsList = new List();
    List alterationsList = new List();
    private String idproject = "";
     
    final String TEXTO = "Project Summary";
    private PanelHeader panelHeader;
    private BasicPanel jPanel1 = new BasicPanel();
    private BasicLabel lblProject5 = new BasicLabel();

    public Paso5() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(451, 420));
	this.setBounds(new Rectangle(10, 10, 451, 420));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	jPanel1.add(tfDate, null);
	jPanel1.add(lblDate, null);
	jPanel1.add(tfProject, null);
	jPanel1.add(lblProject, null);
	jPanel1.add(tfZone, null);
	jPanel1.add(tfArea, null);
	jPanel1.add(lblArea, null);
	jPanel1.add(lblZone, null);
	jPanel1.add(tfEndUtm, null);
	jPanel1.add(lblEndUtm, null);
	jPanel1.add(tfStartUtm, null);
	jPanel1.add(lblProject2, null);
	spDescription.getViewport().add(taDescription, null);
	jPanel1.add(spDescription, null);
	jPanel1.add(lblProject3, null);
	spMineralizations.getViewport().add(listMineralizations, null);
	jPanel1.add(spMineralizations, null);
	spAlterations.getViewport().add(listAlterations, null);
	jPanel1.add(spAlterations, null);
	jPanel1.add(lblAlterations, null);
	jPanel1.add(lblAltAll, null);
	jPanel1.add(lblProject4, null);
	jPanel1.add(lblMinAll, null);
	this.getContentPane().add(lblProject5, null);
	this.getContentPane().add(jPanel1, null);
	this.getContentPane().add(panelHeader, null);
	//
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bOk, null);
	this.getContentPane().add(bBack, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().setLayout(null);
	this.setTitle("New Project - Step 4");
	tfProject.setBounds(new Rectangle(70, 10, 160, 20));
	tfProject.setEditable(false);
	tfProject.setBackground(Color.white);
	tfProject.setFont(new Font("Dialog", 1, 10));
	tfProject.setForeground(Color.red);
	lblProject.setText("Project:");
	lblProject.setBounds(new Rectangle(0, 15, 65, 10));
	lblProject.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProject.setFont(new Font("Dialog", 1, 10));
	bNext.setBounds(new Rectangle(335, 360, 45, 25));
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setMnemonic('N');
	bNext.setEnabled(false);
	bNext.setToolTipText("Next");
	bBack.setBounds(new Rectangle(280, 360, 45, 25));
	bBack.setFont(new Font("Dialog", 1, 10));
	bBack.setMnemonic('B');
	bBack.setToolTipText("Back");
	bBack.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bBack_actionPerformed(e);
		    }

		});
	bOk.setBounds(new Rectangle(390, 360, 45, 25));
	bOk.setFont(new Font("Dialog", 1, 10));
	bOk.setMnemonic('O');
	bOk.setToolTipText("Ok");
	bOk.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bOk_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(10, 360, 45, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setMnemonic('C');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	tfDate.setBounds(new Rectangle(330, 10, 85, 20));
	tfDate.setEditable(false);
	tfDate.setBackground(Color.white);
	tfDate.setFont(new Font("Dialog", 1, 10));
	tfDate.setForeground(Color.red);
	lblDate.setText("Date:");
	lblDate.setBounds(new Rectangle(295, 10, 30, 15));
	lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
	lblDate.setFont(new Font("Dialog", 1, 10));
	tfStartUtm.setBounds(new Rectangle(70, 35, 345, 20));
	tfStartUtm.setEditable(false);
	tfStartUtm.setBackground(Color.white);
	tfStartUtm.setFont(new Font("Dialog", 1, 10));
	tfStartUtm.setForeground(Color.red);
	lblProject2.setText("Start UTM:");
	lblProject2.setBounds(new Rectangle(0, 40, 65, 10));
	lblProject2.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProject2.setFont(new Font("Dialog", 1, 10));
	tfEndUtm.setBounds(new Rectangle(70, 60, 345, 20));
	tfEndUtm.setEditable(false);
	tfEndUtm.setBackground(Color.white);
	tfEndUtm.setFont(new Font("Dialog", 1, 10));
	tfEndUtm.setForeground(Color.red);
	lblEndUtm.setText("End UTM:");
	lblEndUtm.setBounds(new Rectangle(0, 65, 65, 10));
	lblEndUtm.setHorizontalAlignment(SwingConstants.RIGHT);
	lblEndUtm.setFont(new Font("Dialog", 1, 10));
	tfZone.setBounds(new Rectangle(70, 85, 160, 20));
	tfZone.setEditable(false);
	tfZone.setBackground(Color.white);
	tfZone.setFont(new Font("Dialog", 1, 10));
	tfZone.setForeground(Color.red);
	lblZone.setText("Zone:");
	lblZone.setBounds(new Rectangle(0, 90, 65, 10));
	lblZone.setHorizontalAlignment(SwingConstants.RIGHT);
	lblZone.setFont(new Font("Dialog", 1, 10));
	tfArea.setBounds(new Rectangle(285, 85, 130, 20));
	tfArea.setEditable(false);
	tfArea.setBackground(Color.white);
	tfArea.setFont(new Font("Dialog", 1, 10));
	tfArea.setForeground(Color.red);
	lblArea.setText("Area:");
	lblArea.setBounds(new Rectangle(245, 90, 35, 10));
	lblArea.setHorizontalAlignment(SwingConstants.RIGHT);
	lblArea.setFont(new Font("Dialog", 1, 10));
	spDescription.setBounds(new Rectangle(70, 110, 350, 50));
	spDescription.getViewport().setLayout(null);
	taDescription.setBounds(new Rectangle(0, 0, 350, 50));
	taDescription.setFont(new Font("Dialog", 1, 10));
	taDescription.setEditable(false);
	taDescription.setForeground(Color.red);
	lblProject3.setText("Description:");
	lblProject3.setBounds(new Rectangle(0, 115, 65, 10));
	lblProject3.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProject3.setFont(new Font("Dialog", 1, 10));
	spMineralizations.setBounds(new Rectangle(95, 170, 105, 110));
	spMineralizations.getViewport().setLayout(null);
	spAlterations.setBounds(new Rectangle(305, 170, 105, 110));
	spAlterations.getViewport().setLayout(null);
	listMineralizations.setBounds(new Rectangle(0, 0, 105, 110));
	listMineralizations.setFont(new Font("Dialog", 1, 10));
	listMineralizations.setForeground(Color.red);
	listAlterations.setBounds(new Rectangle(35, 0, 105, 110));
	listAlterations.setFont(new Font("Dialog", 1, 10));
	listAlterations.setForeground(Color.red);
	//new Rectangle(0, 350, 445, 2));
	jPanel1.setBounds(new Rectangle(8, 45, 430, 295));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	lblProject5.setText(" Details");
	lblProject5.setBounds(new Rectangle(15, 35, 50, 15));
	lblProject5.setHorizontalAlignment(SwingConstants.LEFT);
	lblProject5.setFont(new Font("Default", 1, 10));
	lblProject5.setOpaque(true);
	lblProject4.setText("Mineralizations");
	lblProject4.setBounds(new Rectangle(5, 175, 85, 10));
	lblProject4.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProject4.setFont(new Font("Dialog", 1, 10));
	lblAlterations.setText("Alterations");
	lblAlterations.setBounds(new Rectangle(235, 175, 65, 10));
	lblAlterations.setHorizontalAlignment(SwingConstants.RIGHT);
	lblAlterations.setFont(new Font("Dialog", 1, 10));
	lblMinAll.setText("Allowed:");
	lblMinAll.setBounds(new Rectangle(5, 185, 85, 10));
	lblMinAll.setHorizontalAlignment(SwingConstants.RIGHT);
	lblMinAll.setFont(new Font("Dialog", 1, 10));
	lblAltAll.setText("allowed:");
	lblAltAll.setBounds(new Rectangle(235, 185, 65, 10));
	lblAltAll.setHorizontalAlignment(SwingConstants.RIGHT);
	lblAltAll.setFont(new Font("Dialog", 1, 10));
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void showFields() {
	setFieldStartUtm();
	setFieldEndUtm();
	tfProject.setText(c(1));
	tfDate.setText(c(3));
	tfZone.setText(c(8));
	tfArea.setText(c(9));
	taDescription.setText(c(2));
	mineralizationsList = getMineralizationsList();
	alterationsList = getAlterationsList();
	listMineralizations.setListData(mineralizationsList.getNombres());
	listAlterations.setListData(alterationsList.getNombres());
    }

    private void bOk_actionPerformed(ActionEvent e) {
	idproject = org.digitall.lib.sql.LibSQL.getCampo("Select (max(idproject)+1) From drilling.projects");
	String startUtm = "NULL";
	String endUtm = "NULL";
	if (!getStartUtm().isNull()) {
	    startUtm = "GeometryFromText('POINT(" + getStartUtm().getX() + " " + getStartUtm().getY() + ")',-1)";
	}
	if (!getEndUtm().isNull()) {
	    endUtm = "GeometryFromText('POINT(" + getEndUtm().getX() + " " + getEndUtm().getY() + ")',-1)";
	}
	String consultaInsert = " INSERT INTO drilling.projects " + " VALUES ( " + idproject + ", 100, '" + c(1) + "', '" + c(2) + "', '" + Proced.setFormatDate(c(3), false) + "', '" + c(4) + "', '" + c(5) + "'," + startUtm + "," + endUtm + ",'" + c(6) + "','')";
	//System.out.println("consultaInsert--> "+consultaInsert);
	if (LibSQL.exActualizar('a', consultaInsert)) {
	    Vector listaMins = mineralizationsList.getIds();
	    //System.out.println("listaMins.size()--> "+ listaMins.size());
	    for (int i = 0; i < listaMins.size(); i++) {
		consultaInsert = "INSERT INTO drilling.allowed_mineralizations VALUES ((Select max(idall_min)+1 From drilling.allowed_mineralizations)" + ", " + idproject + " , " + listaMins.get(i) + ", '')";
		//System.out.println("Insert Mineral.--> "+consultaInsert);
		LibSQL.exActualizar('a', consultaInsert);
	    }
	    Vector listaAlts = alterationsList.getIds();
	    for (int i = 0; i < listaAlts.size(); i++) {
		consultaInsert = "INSERT INTO drilling.allowed_alterations VALUES ((Select max(idall_alt)+1 From drilling.allowed_alterations)" + " , " + idproject + " , " + listaAlts.get(i) + ", '')";
		//System.out.println("Insert Alterac.--> "+consultaInsert);
		LibSQL.exActualizar('a', consultaInsert);
	    }
	    int result = JOptionPane.showConfirmDialog((Component)null, "The " + c(1) + " Project was registered.\nWould you like to register a Drill for this project?", "Message", JOptionPane.YES_NO_OPTION);
	    if (result == JOptionPane.YES_OPTION) {
		NewDrill nuevoDrill = new NewDrill(idproject);
		nuevoDrill.setModal(true);
		nuevoDrill.setVisible(true);
		this.dispose();
	    }
	    cancelar();
	}
    }

    private void bBack_actionPerformed(ActionEvent e) {
	previo();
    }

    public void mostrar() {
	showFields();
	this.setVisible(true);
    }

    private void setFieldStartUtm() {
	System.out.println("start: " + getStartUtm().isNull());
	System.out.println("start: " + getStartUtm().getX() + " - " + getStartUtm().getY());
	if (!getStartUtm().isNull()) {
	    tfStartUtm.setText("Easting: " + getStartUtm().getX() + " - Northing: " + getStartUtm().getY());
	} else {
	    tfStartUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

    private void setFieldEndUtm() {
	System.out.println("end: " + getEndUtm().isNull());
	System.out.println("end: " + getEndUtm().getX() + " - " + getEndUtm().getY());
	if (!getEndUtm().isNull()) {
	    tfEndUtm.setText("Easting: " + getEndUtm().getX() + " - Northing: " + getEndUtm().getY());
	} else {
	    tfEndUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

}

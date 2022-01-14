package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.Coordinate;
import org.digitall.common.drilling.PanelHeader;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.JDecEntry;
import org.digitall.lib.components.JEntry;
import org.digitall.lib.components.JTFecha;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;

//

public class NewDrill extends BasicDialog {

    private BasicPanel jpOptions = new BasicPanel();
    private BasicLabel lblDetails = new BasicLabel();
    private BasicLabel lblName = new BasicLabel();
    private BasicLabel lblName1 = new BasicLabel();
    private BasicLabel lblName3 = new BasicLabel();
    private BasicLabel lblName4 = new BasicLabel();
    private BasicLabel lblName5 = new BasicLabel();
    private BasicLabel lblName8 = new BasicLabel();
    private BasicLabel lblName9 = new BasicLabel();
    private BasicLabel lblName10 = new BasicLabel();
    private BasicLabel lblName11 = new BasicLabel();
    private BasicLabel lblName12 = new BasicLabel();
    private BasicLabel lblName13 = new BasicLabel();
    private BasicLabel lblNotes = new BasicLabel();
    private AcceptButton bOk = new AcceptButton();
    private CloseButton bCancel = new CloseButton();
    private ModifyButton bModifySection = new ModifyButton();
    private ModifyButton bModify = new ModifyButton();
    private JTFecha tfDate = new JTFecha();
    private JDecEntry tfUtm = new JDecEntry();
    private JDecEntry tfAzimuth = new JDecEntry();
    private JDecEntry tfDip = new JDecEntry();
    private JDecEntry tfSectionUtm = new JDecEntry();
    private JDecEntry tfStartUtmX = new JDecEntry();
    private JDecEntry tfStartUtmY = new JDecEntry();
    private JDecEntry tfEndUtmX = new JDecEntry();
    private JDecEntry tfEndUtmY = new JDecEntry();
    private JDecEntry tfDepth = new JDecEntry();
    private JEntry tfDdh = new JEntry();
    private JEntry tfPlan = new JEntry();
    private JEntry tfLocation = new JEntry();
    private JEntry tfSamplers = new JEntry();
    private JEntry tfGeologist = new JEntry();
    private JTextArea taNotes = new JTextArea();
    private BasicScrollPane spNotes = new BasicScrollPane();
    private String idproject = "", proyecto = "";
    private BasicCombo cbSectionH = new BasicCombo();
    final String TEXTO = "New Drill Wizard";
    private PanelHeader panelHeader;
     
    private BasicLabel lblProjectName = new BasicLabel();
    private BasicLabel lblProject = new BasicLabel();
    private BasicLabel lbldepth = new BasicLabel();
    private String utm = "", sectionUtm = "";
    private Coordinate drillUTM = new Coordinate.Double();
    private Coordinate sectionUTM = new Coordinate.Double();

    public NewDrill(String _idproject) {
	try {
	    idproject = _idproject;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setBounds(new Rectangle(10, 10, 463, 385));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblProject, null);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(lblProjectName, null);
	//
	this.getContentPane().add(lblDetails, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bOk, null);
	this.getContentPane().add(jpOptions, null);
	this.getContentPane().setLayout(null);
	this.setTitle("New Drill");
	this.setBackground(SystemColor.activeCaptionBorder);
	this.setSize(new Dimension(455, 405));
	findProjectName();
	lblProject.setText("" + proyecto);
	lblDetails.setText(" Details");
	lblDetails.setBounds(new Rectangle(15, 60, 50, 15));
	lblDetails.setFont(new Font("Default", 1, 10));
	lblDetails.setOpaque(true);
	jpOptions.setBounds(new Rectangle(10, 70, 430, 265));
	jpOptions.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	jpOptions.setLayout(null);
	tfDdh.setBounds(new Rectangle(65, 10, 120, 20));
	tfDdh.setFont(new Font("Dialog", 1, 11));
	tfDdh.setForeground(Color.red);
	tfUtm.setBounds(new Rectangle(65, 35, 305, 20));
	tfUtm.setFont(new Font("Dialog", 1, 11));
	tfUtm.setForeground(Color.red);
	tfDate.setBounds(new Rectangle(295, 10, 85, 20));
	tfDate.setForeground(Color.red);
	tfDate.setFont(new Font("Dialog", 1, 11));
	tfDate.setSize(new Dimension(75, 20));
	lblName.setText("DDH:");
	lblName.setBounds(new Rectangle(5, 15, 55, 10));
	lblName.setFont(new Font("Dialog", 1, 10));
	lblName.setHorizontalAlignment(SwingConstants.RIGHT);
	lblName1.setText("UTM:");
	lblName1.setBounds(new Rectangle(5, 40, 55, 10));
	lblName1.setFont(new Font("Dialog", 1, 10));
	lblName1.setHorizontalAlignment(SwingConstants.RIGHT);
	tfAzimuth.setBounds(new Rectangle(65, 60, 75, 20));
	tfAzimuth.setFont(new Font("Dialog", 1, 11));
	tfAzimuth.setForeground(Color.red);
	tfDip.setBounds(new Rectangle(295, 60, 75, 20));
	tfDip.setFont(new Font("Dialog", 1, 11));
	tfDip.setForeground(Color.red);
	lblName3.setText("Date:");
	lblName3.setBounds(new Rectangle(230, 15, 55, 10));
	lblName3.setFont(new Font("Dialog", 1, 10));
	lblName3.setHorizontalAlignment(SwingConstants.RIGHT);
	lblName4.setText("Dip:");
	lblName4.setBounds(new Rectangle(235, 65, 55, 15));
	lblName4.setFont(new Font("Dialog", 1, 10));
	lblName4.setHorizontalAlignment(SwingConstants.RIGHT);
	lblName5.setText("Azimuth:");
	lblName5.setBounds(new Rectangle(-10, 65, 70, 15));
	lblName5.setFont(new Font("Dialog", 1, 10));
	lblName5.setHorizontalAlignment(SwingConstants.RIGHT);
	tfLocation.setBounds(new Rectangle(295, 135, 120, 20));
	tfLocation.setFont(new Font("Dialog", 1, 11));
	tfLocation.setForeground(Color.red);
	lblName8.setText("Location:");
	lblName8.setBounds(new Rectangle(235, 140, 55, 15));
	lblName8.setFont(new Font("Dialog", 1, 10));
	lblName8.setHorizontalAlignment(SwingConstants.RIGHT);
	tfPlan.setBounds(new Rectangle(65, 135, 120, 20));
	tfPlan.setFont(new Font("Dialog", 1, 11));
	tfPlan.setForeground(Color.red);
	lblName9.setText("Plan:");
	lblName9.setBounds(new Rectangle(5, 135, 55, 15));
	lblName9.setFont(new Font("Dialog", 1, 10));
	lblName9.setHorizontalAlignment(SwingConstants.RIGHT);
	lblName10.setText("Section H.:");
	lblName10.setBounds(new Rectangle(230, 85, 55, 20));
	lblName10.setFont(new Font("Dialog", 1, 10));
	lblName10.setHorizontalAlignment(SwingConstants.RIGHT);
	tfSectionUtm.setBounds(new Rectangle(65, 110, 305, 20));
	tfSectionUtm.setFont(new Font("Dialog", 1, 11));
	tfSectionUtm.setForeground(Color.red);
	lblName11.setText("Section:");
	lblName11.setBounds(new Rectangle(5, 113, 55, 15));
	lblName11.setFont(new Font("Dialog", 1, 10));
	lblName11.setToolTipText("null");
	lblName11.setHorizontalAlignment(SwingConstants.RIGHT);
	tfSamplers.setBounds(new Rectangle(295, 160, 120, 20));
	tfSamplers.setFont(new Font("Dialog", 1, 11));
	tfSamplers.setForeground(Color.red);
	lblName12.setText("Samplers:");
	lblName12.setBounds(new Rectangle(230, 165, 55, 15));
	lblName12.setFont(new Font("Dialog", 1, 10));
	lblName12.setHorizontalAlignment(SwingConstants.RIGHT);
	tfGeologist.setBounds(new Rectangle(65, 160, 120, 20));
	tfGeologist.setFont(new Font("Dialog", 1, 11));
	tfGeologist.setForeground(Color.red);
	lblName13.setText("Geologist:");
	lblName13.setBounds(new Rectangle(5, 160, 55, 15));
	lblName13.setFont(new Font("Dialog", 1, 10));
	lblName13.setHorizontalAlignment(SwingConstants.RIGHT);
	spNotes.setBounds(new Rectangle(65, 185, 350, 70));
	//spNotes.getViewport().setLayout(null);
	taNotes.setBounds(new Rectangle(0, 0, 355, 70));
	taNotes.setWrapStyleWord(true);
	taNotes.setLineWrap(true);
	taNotes.setFont(new Font("Dialog", 1, 11));
	taNotes.setForeground(Color.red);
	lblNotes.setText("Notes:");
	lblNotes.setBounds(new Rectangle(0, 185, 55, 15));
	lblNotes.setFont(new Font("Dialog", 1, 10));
	lblNotes.setHorizontalAlignment(SwingConstants.RIGHT);
	bOk.setBounds(new Rectangle(400, 345, 40, 25));
	bOk.setMnemonic('O');
	bOk.setToolTipText("Ok");
	bOk.setSize(new Dimension(40, 25));
	bOk.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bOk_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(10, 345, 40, 25));
	bCancel.setMnemonic('C');
	bCancel.setSize(new Dimension(40, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	cbSectionH.setBounds(new Rectangle(295, 85, 75, 20));
	cbSectionH.setFont(new Font("Dialog", 1, 11));
	cbSectionH.setForeground(Color.red);
	bModifySection.setBounds(new Rectangle(380, 110, 30, 20));
	bModifySection.setToolTipText("Modify Section UTM");
	bModifySection.setMnemonic('T');
	bModifySection.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bModifySection_actionPerformed(e);
		    }

		});
	bModify.setBounds(new Rectangle(380, 35, 30, 20));
	bModify.setToolTipText("Modify UTM");
	bModify.setMnemonic('M');
	bModify.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bModify_actionPerformed(e);
		    }

		});
	//new Rectangle(0, 50, 445, 2));
	lblProjectName.setText("Project Name: ");
	lblProjectName.setBounds(new Rectangle(10, 35, 85, 15));
	lblProjectName.setFont(new Font("Dialog", 1, 11));
	lblProject.setBounds(new Rectangle(90, 35, 355, 15));
	lblProject.setFont(new Font("Dialog", 1, 11));
	lblProject.setForeground(Color.blue);
	tfDepth.setBounds(new Rectangle(65, 85, 75, 20));
	tfDepth.setForeground(Color.red);
	tfDepth.setFont(new Font("Default", 1, 11));
	lbldepth.setText("Depth:");
	lbldepth.setBounds(new Rectangle(5, 88, 55, 15));
	lbldepth.setFont(new Font("Dialog", 1, 10));
	lbldepth.setHorizontalAlignment(SwingConstants.RIGHT);
	jpOptions.add(lbldepth, null);
	jpOptions.add(tfDepth, null);
	jpOptions.add(bModify, null);
	jpOptions.add(bModifySection, null);
	jpOptions.add(cbSectionH, null);
	jpOptions.add(lblNotes, null);
	spNotes.getViewport().add(taNotes, null);
	jpOptions.add(spNotes, null);
	jpOptions.add(lblName13, null);
	jpOptions.add(tfGeologist, null);
	jpOptions.add(lblName11, null);
	jpOptions.add(tfSectionUtm, null);
	jpOptions.add(lblName10, null);
	jpOptions.add(lblName8, null);
	jpOptions.add(tfLocation, null);
	jpOptions.add(lblName5, null);
	jpOptions.add(lblName4, null);
	jpOptions.add(lblName3, null);
	jpOptions.add(tfDip, null);
	jpOptions.add(tfAzimuth, null);
	jpOptions.add(lblName1, null);
	jpOptions.add(lblName, null);
	jpOptions.add(tfDate, null);
	jpOptions.add(tfUtm, null);
	jpOptions.add(tfDdh, null);
	jpOptions.add(tfSamplers, null);
	jpOptions.add(lblName12, null);
	jpOptions.add(tfPlan, null);
	jpOptions.add(lblName9, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	tfDate.setText(Proced.FechaHora2(true, false));
	tfUtm.setEditable(false);
	tfUtm.setBackground(Color.white);
	tfSectionUtm.setEditable(false);
	tfSectionUtm.setBackground(Color.white);
	cargaCombo();
    }

    private void cargaCombo() {
	cbSectionH.addItem("E");
	cbSectionH.addItem("N");
	cbSectionH.setSelectedItem("E");
    }

    private void bOk_actionPerformed(ActionEvent e) {
	if (control()) {
	    utm = "NULL";
	    sectionUtm = "NULL";
	    if (!drillUTM.isNull()) {
		utm = "GeometryFromText('POINT(" + drillUTM.getX() + " " + drillUTM.getY() + ")',-1)";
	    }
	    if (!sectionUTM.isNull()) {
		sectionUtm = "GeometryFromText('POINT(" + sectionUTM.getX() + " " + sectionUTM.getY() + ")',-1)";
	    }
	    String iddrill = "(Select max(iddrill)+1 From drilling.drills)";
	    String consultaInsert = "INSERT INTO drilling.drills VALUES ( " + iddrill + ", " + idproject + ", '" + tfDdh.getText().trim() + "', 100, " + utm + ", " + sectionUtm + ", 0" + tfDepth.getText().trim() + ", '" + Proced.setFormatDate(tfDate.getText(), false) + "', 1,0" + tfAzimuth.getText() + ", 0" + tfDip.getText() + ", '" + cbSectionH.getSelectedItem() + "' , '" + tfPlan.getText().trim() + "' , '" + tfLocation.getText().trim() + "', '" + tfGeologist.getText().trim() + "', '" + tfSamplers.getText().trim() + "' , '" + taNotes.getText().trim() + "',NULL,'')";
	    System.out.println("Insert: " + consultaInsert);
	    if (LibSQL.exActualizar('a', consultaInsert)) {
		org.digitall.lib.components.Advisor.messageBox("New Drill Registered!", "Message");
		this.dispose();
	    }
	}
    }

    private boolean control() {
	if (!tfDdh.getText().equals("")) {
	    return true;
	} else {
	    org.digitall.lib.components.Advisor.messageBox("insert the name of the drill!", "Error");
	    return false;
	}
    }

    private boolean controlAziDip() {
	double valor = Double.parseDouble(tfAzimuth.getText());
	if ((valor >= 0) && (valor <= 360)) {
	    return true;
	} else {
	    org.digitall.lib.components.Advisor.messageBox("Azimuth (0-360)", "Error");
	    return false;
	}
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void bModifySection_actionPerformed(ActionEvent e) {
	UtmCoordinate utm = new UtmCoordinate(sectionUTM);
	utm.setModal(true);
	utm.setVisible(true);
	setSectionUtm();
    }

    private void bModify_actionPerformed(ActionEvent e) {
	UtmCoordinate utm = new UtmCoordinate(drillUTM);
	utm.setModal(true);
	utm.setVisible(true);
	setDrillUtm();
    }

    private void findProjectName() {
	proyecto = org.digitall.lib.sql.LibSQL.getCampo("SELECT name FROM drilling.projects WHERE idproject = " + idproject + " AND estado <> '*'");
    }

    private void setDrillUtm() {
	if (!drillUTM.isNull()) {
	    tfUtm.setText("Easting: " + drillUTM.getX() + " - Northing: " + drillUTM.getY());
	} else {
	    tfUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

    private void setSectionUtm() {
	if (!sectionUTM.isNull()) {
	    tfSectionUtm.setText("Easting: " + sectionUTM.getX() + " - Northing: " + sectionUTM.getY());
	} else {
	    tfSectionUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

}

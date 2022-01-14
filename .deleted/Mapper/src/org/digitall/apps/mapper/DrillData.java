package org.digitall.apps.mapper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.Coordinate;
import org.digitall.common.drilling.PanelHeader;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.JDecEntry;
import org.digitall.lib.components.JEntry;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.icons.IconTypes;

//

public class DrillData extends BasicInternalFrame {

    private BasicPanel jPanel1 = new BasicPanel();
    private BasicLabel lblOptions = new BasicLabel();
    private BasicLabel lblName = new BasicLabel();
    private BasicLabel lblName1 = new BasicLabel();
    private BasicLabel lblDepth = new BasicLabel();
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
    private BasicTextField tfDdh = new BasicTextField();
    private JDecEntry tfUtm = new JDecEntry();
    private JDecEntry tfAzimuth = new JDecEntry();
    private JEntry tfPlan = new JEntry();
    private JDecEntry tfDip = new JDecEntry();
    private JEntry tfLocation = new JEntry();
    private JDecEntry tfSectionUtm = new JDecEntry();
    private JEntry tfSamplers = new JEntry();
    private JEntry tfGeologist = new JEntry();
    private JDecEntry tfDepth = new JDecEntry();
    private PanelHeader panelHeader;
    private JEntry tfDate = new JEntry();
    private JTextArea taNotes = new JTextArea();
    private BasicScrollPane jScrollPane1 = new BasicScrollPane();
    private BasicCombo cbSectionH = new BasicCombo();
    private String projectName;
     
    private BasicLabel lblProjectName = new BasicLabel();
    private BasicLabel lblProject = new BasicLabel();
    private int idDrill;
    private int error = 0;
    private Coordinate drillUTM = new Coordinate.Double();
    private Coordinate sectionUTM = new Coordinate.Double();

    public DrillData(String _projectName) {
	try {
	    projectName = _projectName;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	//this.setSize(new Dimension(455, 398));
	this.setBounds(new Rectangle(10, 10, 455, 398));
	setClosable(true);
	setIconifiable(true);
	setDefaultCloseOperation(BasicInternalFrame.HIDE_ON_CLOSE);
	panelHeader = new PanelHeader(this.getWidth(), "DDH Details", IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblProject, null);
	this.getContentPane().add(lblProjectName, null);
	//
	this.getContentPane().add(lblOptions, null);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(jPanel1, null);
	this.getContentPane().setLayout(null);
	//this.setBackground(SystemColor.activeCaptionBorder);
	this.setSize(new Dimension(455, 364));
	lblProject.setText(projectName);
	lblOptions.setText(" Details ");
	lblOptions.setBounds(new Rectangle(10, 60, 50, 15));
	lblOptions.setFont(new Font("Dialog", 1, 10));
	lblOptions.setOpaque(true);
	jPanel1.setBounds(new Rectangle(5, 70, 440, 265));
	jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	jPanel1.setLayout(null);
	tfDdh.setBounds(new Rectangle(75, 10, 140, 20));
	tfDdh.setCaretColor(Color.red);
	tfDdh.setFont(new Font("Dialog", 1, 11));
	tfDdh.setForeground(Color.red);
	tfDdh.setSize(new Dimension(120, 20));
	tfDdh.setEditable(false);
	tfUtm.setBounds(new Rectangle(75, 35, 300, 20));
	tfUtm.setCaretColor(Color.red);
	tfUtm.setFont(new Font("Dialog", 1, 11));
	tfUtm.setForeground(Color.red);
	tfUtm.setEditable(false);
	tfUtm.setBackground(Color.white);
	tfDate.setBounds(new Rectangle(300, 10, 75, 20));
	tfDate.setCaretColor(Color.red);
	tfDate.setFont(new Font("Dialog", 1, 10));
	tfDate.setForeground(Color.red);
	lblName.setText("DDH:");
	lblName.setBounds(new Rectangle(0, 15, 70, 10));
	lblName.setFont(new Font("Dialog", 1, 10));
	lblName.setHorizontalAlignment(SwingConstants.RIGHT);
	lblName1.setText("UTM:");
	lblName1.setBounds(new Rectangle(0, 40, 70, 10));
	lblName1.setFont(new Font("Dialog", 1, 10));
	lblName1.setHorizontalAlignment(SwingConstants.RIGHT);
	tfAzimuth.setBounds(new Rectangle(75, 60, 75, 20));
	tfAzimuth.setCaretColor(Color.red);
	tfAzimuth.setFont(new Font("Dialog", 1, 11));
	tfAzimuth.setForeground(Color.red);
	tfAzimuth.setEditable(false);
	tfDip.setBounds(new Rectangle(300, 60, 75, 20));
	tfDip.setCaretColor(Color.red);
	tfDip.setFont(new Font("Dialog", 1, 11));
	tfDip.setForeground(Color.red);
	tfDip.setEditable(false);
	lblName3.setText("Date:");
	lblName3.setBounds(new Rectangle(240, 15, 55, 10));
	lblName3.setFont(new Font("Dialog", 1, 10));
	lblName3.setHorizontalAlignment(SwingConstants.RIGHT);
	lblName4.setText("Dip:");
	lblName4.setBounds(new Rectangle(240, 63, 55, 15));
	lblName4.setFont(new Font("Dialog", 1, 10));
	lblName4.setHorizontalAlignment(SwingConstants.RIGHT);
	lblName5.setText("Azimuth:");
	lblName5.setBounds(new Rectangle(0, 63, 70, 15));
	lblName5.setFont(new Font("Dialog", 1, 10));
	lblName5.setHorizontalAlignment(SwingConstants.RIGHT);
	tfLocation.setBounds(new Rectangle(300, 135, 120, 20));
	tfLocation.setCaretColor(Color.red);
	tfLocation.setForeground(Color.red);
	tfLocation.setFont(new Font("Dialog", 1, 11));
	tfLocation.setEditable(false);
	lblName8.setText("Location:");
	lblName8.setBounds(new Rectangle(240, 140, 55, 15));
	lblName8.setFont(new Font("Dialog", 1, 10));
	lblName8.setHorizontalAlignment(SwingConstants.RIGHT);
	tfPlan.setBounds(new Rectangle(75, 135, 120, 20));
	tfPlan.setCaretColor(Color.red);
	tfPlan.setForeground(Color.red);
	tfPlan.setFont(new Font("Dialog", 1, 11));
	tfPlan.setEditable(false);
	lblName9.setText("Plan:");
	lblName9.setBounds(new Rectangle(0, 140, 70, 15));
	lblName9.setFont(new Font("Dialog", 1, 10));
	lblName9.setHorizontalAlignment(SwingConstants.RIGHT);
	lblName10.setText("Section H.:");
	lblName10.setBounds(new Rectangle(240, 85, 55, 20));
	lblName10.setFont(new Font("Dialog", 1, 10));
	lblName10.setHorizontalAlignment(SwingConstants.RIGHT);
	tfSectionUtm.setBounds(new Rectangle(75, 110, 300, 20));
	tfSectionUtm.setBackground(Color.white);
	tfSectionUtm.setCaretColor(Color.red);
	tfSectionUtm.setForeground(Color.red);
	tfSectionUtm.setFont(new Font("Dialog", 1, 11));
	tfSectionUtm.setEditable(false);
	lblName11.setText("Section Utm:");
	lblName11.setBounds(new Rectangle(5, 115, 65, 15));
	lblName11.setFont(new Font("Dialog", 1, 10));
	lblName11.setToolTipText("null");
	lblName11.setHorizontalAlignment(SwingConstants.RIGHT);
	tfSamplers.setBounds(new Rectangle(300, 160, 120, 20));
	tfSamplers.setCaretColor(Color.red);
	tfSamplers.setForeground(Color.red);
	tfSamplers.setFont(new Font("Dialog", 1, 11));
	tfSamplers.setEditable(false);
	lblName12.setText("Samplers:");
	lblName12.setBounds(new Rectangle(240, 165, 55, 15));
	lblName12.setFont(new Font("Dialog", 1, 10));
	lblName12.setHorizontalAlignment(SwingConstants.RIGHT);
	tfGeologist.setBounds(new Rectangle(75, 160, 120, 20));
	tfGeologist.setCaretColor(Color.red);
	tfGeologist.setForeground(Color.red);
	tfGeologist.setFont(new Font("Dialog", 1, 11));
	tfGeologist.setEditable(false);
	lblName13.setText("Geologist:");
	lblName13.setBounds(new Rectangle(10, 165, 60, 15));
	lblName13.setFont(new Font("Dialog", 1, 10));
	lblName13.setHorizontalAlignment(SwingConstants.RIGHT);
	jScrollPane1.setBounds(new Rectangle(75, 185, 350, 70));
	//jScrollPane1.getViewport().setLayout(null);
	taNotes.setBounds(new Rectangle(0, 0, 355, 70));
	taNotes.setWrapStyleWord(true);
	taNotes.setLineWrap(true);
	taNotes.setCaretColor(Color.red);
	taNotes.setForeground(Color.red);
	taNotes.setFont(new Font("Dialog", 1, 11));
	taNotes.setEditable(false);
	lblNotes.setText("Notes:");
	lblNotes.setBounds(new Rectangle(0, 190, 70, 15));
	lblNotes.setFont(new Font("Dialog", 1, 10));
	lblNotes.setHorizontalAlignment(SwingConstants.RIGHT);
	cbSectionH.setBounds(new Rectangle(300, 85, 75, 20));
	cbSectionH.setForeground(Color.red);
	cbSectionH.setFont(new Font("Dialog", 1, 11));
	cbSectionH.setEnabled(false);
	//new Rectangle(0, 55, 450, 2));
	lblProjectName.setText("Project Name: ");
	lblProjectName.setBounds(new Rectangle(15, 40, 85, 15));
	lblProjectName.setFont(new Font("Dialog", 1, 11));
	lblProjectName.setHorizontalAlignment(SwingConstants.LEFT);
	lblProject.setBounds(new Rectangle(95, 40, 350, 15));
	lblProject.setFont(new Font("Dialog", 1, 11));
	lblProject.setHorizontalAlignment(SwingConstants.LEFT);
	lblProject.setForeground(Color.blue);
	tfDepth.setBounds(new Rectangle(75, 85, 75, 20));
	tfDepth.setForeground(Color.red);
	tfDepth.setCaretColor(Color.red);
	tfDepth.setFont(new Font("Dialog", 1, 11));
	tfDepth.setEditable(false);
	panelHeader.setBounds(new Rectangle(0, 0, 450, 32));
	lblDepth.setText("Depth:");
	lblDepth.setBounds(new Rectangle(30, 85, 38, 14));
	lblDepth.setToolTipText("null");
	lblDepth.setFont(new Font("Dialog", 1, 11));
	jPanel1.add(lblDepth, null);
	jPanel1.add(tfDepth, null);
	jPanel1.add(cbSectionH, null);
	jPanel1.add(lblNotes, null);
	jScrollPane1.getViewport().add(taNotes, null);
	jPanel1.add(jScrollPane1, null);
	jPanel1.add(lblName13, null);
	jPanel1.add(tfGeologist, null);
	jPanel1.add(lblName12, null);
	jPanel1.add(tfSamplers, null);
	jPanel1.add(lblName11, null);
	jPanel1.add(tfSectionUtm, null);
	jPanel1.add(lblName10, null);
	jPanel1.add(lblName9, null);
	jPanel1.add(tfPlan, null);
	jPanel1.add(lblName8, null);
	jPanel1.add(tfLocation, null);
	jPanel1.add(lblName5, null);
	jPanel1.add(lblName4, null);
	jPanel1.add(lblName3, null);
	jPanel1.add(tfDip, null);
	jPanel1.add(tfAzimuth, null);
	jPanel1.add(lblName1, null);
	jPanel1.add(lblName, null);
	jPanel1.add(tfDate, null);
	jPanel1.add(tfUtm, null);
	jPanel1.add(tfDdh, null);
	cargaCombo();
    }

    public void setIdDrill(int _idDrill) {
	idDrill = _idDrill;
	setearDatos();
    }

    private void cargaCombo() {
	cbSectionH.addItem("E");
	cbSectionH.addItem("N");
	cbSectionH.setSelectedItem("E");
    }

    private void setearDatos() {
	try {
	    String consulta = " SELECT iddrill, idproject, ddh, X(utm) as X, Y(utm) as Y,X(sectionutm) as sectionX, " + " Y(sectionutm) as sectionY, date, pages, azimuth, dip, depth, sectionh, plan, location, geologist," + " samplers, notes FROM drilling.drills WHERE  iddrill = " + idDrill + ";";
	    ResultSet resul = org.digitall.lib.sql.LibSQL.exQuery(consulta);
	    if (resul.next()) {
		try {
		    if (!resul.getString("X").equals("null")) {
			drillUTM = new Coordinate.Double(resul.getDouble("X"), resul.getDouble("Y"));
		    }
		} catch (NullPointerException ex) {
		    drillUTM.setNull(true);
		}
		try {
		    if (!resul.getString("sectionX").equals("null")) {
			sectionUTM = new Coordinate.Double(resul.getDouble("sectionX"), resul.getDouble("sectionY"));
		    }
		} catch (NullPointerException ex) {
		    sectionUTM.setNull(true);
		}
		setDrillUTM();
		setSectionUTM();
		cbSectionH.setSelectedItem(resul.getString("sectionh"));
		tfDdh.setText(resul.getString("ddh"));
		tfDate.setValue(Proced.setFormatDate(resul.getString("date"), true));
		tfAzimuth.setText(resul.getString("azimuth"));
		tfDip.setText(resul.getString("dip"));
		tfPlan.setText(resul.getString("plan").trim());
		tfLocation.setText(resul.getString("location").trim());
		tfGeologist.setText(resul.getString("geologist").trim());
		tfSamplers.setText(resul.getString("samplers").trim());
		taNotes.setText(resul.getString("notes").trim());
		tfDepth.setText(resul.getString("depth"));
		setTitle("DDH" + resul.getString("ddh") + " Data");
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
    }

    private void setDrillUTM() {
	if (!drillUTM.isNull()) {
	    tfUtm.setText("Easting: " + drillUTM.getX() + " - Northing: " + drillUTM.getY());
	} else {
	    tfUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

    private void setSectionUTM() {
	if (!sectionUTM.isNull()) {
	    tfSectionUtm.setText("Easting: " + sectionUTM.getX() + " - Northing: " + sectionUTM.getY());
	} else {
	    tfSectionUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

}

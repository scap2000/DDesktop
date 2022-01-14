package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.JDecEntry;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.icons.IconTypes;

//

public class Utm extends BasicDialog {

    private BasicPanel pStartUtm = new BasicPanel();
    private BasicPanel pEndUtm = new BasicPanel();
    private BasicLabel lblStartUtm = new BasicLabel();
    private BasicLabel lblEndUtm = new BasicLabel();
    private BasicLabel lblEndUtm3 = new BasicLabel();
    private BasicLabel lblEndUtm4 = new BasicLabel();
    private BasicLabel lblEndUtmN = new BasicLabel();
    private BasicLabel lblEndUtmE = new BasicLabel();
    private JDecEntry tfStartUtmE = new JDecEntry();
    private JDecEntry tfUtmStartN = new JDecEntry();
    private JDecEntry tfUtmEndN = new JDecEntry();
    private JDecEntry tfUtmEndE = new JDecEntry();
    private AcceptButton bOk = new AcceptButton();
    private CloseButton bCancel = new CloseButton();
    private String TEXTO = "Enter UTM";
    private String idproject = "", iddrill = "", cadena = "", drillName = "";
    private PanelHeader panelHeader;
    int opcion = 0;

    public Utm(JDecEntry _startUtmX, JDecEntry _startUtmY, JDecEntry _endUtmX, JDecEntry _endUtmY, String _idproject, String _iddrill) {
	try {
	    tfStartUtmE = _startUtmX;
	    tfUtmStartN = _startUtmY;
	    tfUtmEndE = _endUtmX;
	    tfUtmEndN = _endUtmY;
	    idproject = _idproject;
	    iddrill = _iddrill;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Utm(JDecEntry _startUtmX, JDecEntry _startUtmY, JDecEntry _endUtmX, JDecEntry _endUtmY, String _idproject, String _iddrill, int _opcion) {
	try {
	    tfStartUtmE = _startUtmX;
	    tfUtmStartN = _startUtmY;
	    tfUtmEndE = _endUtmX;
	    tfUtmEndN = _endUtmY;
	    idproject = _idproject;
	    iddrill = _iddrill;
	    opcion = _opcion;
	    if (opcion == 2) {
		TEXTO = "Enter Section UTM";
	    }
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Utm(JDecEntry _startUtmX, JDecEntry _startUtmY, JDecEntry _endUtmX, JDecEntry _endUtmY, String _idproject, int _opcion) {
	try {
	    tfStartUtmE = _startUtmX;
	    tfUtmStartN = _startUtmY;
	    tfUtmEndE = _endUtmX;
	    tfUtmEndN = _endUtmY;
	    idproject = _idproject;
	    opcion = _opcion;
	    if (opcion == 2) {
		TEXTO = "Enter Section UTM";
	    }
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Utm(JDecEntry _startUtmX, JDecEntry _startUtmY, JDecEntry _endUtmX, JDecEntry _endUtmY, int _opcion) {
	try {
	    tfStartUtmE = _startUtmX;
	    tfUtmStartN = _startUtmY;
	    tfUtmEndE = _endUtmX;
	    tfUtmEndN = _endUtmY;
	    opcion = _opcion;
	    if (opcion == 3) {
		TEXTO = "Enter Section UTM";
	    }
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Utm(JDecEntry _startUtmX, JDecEntry _startUtmY, JDecEntry _endUtmX, JDecEntry _endUtmY, String _idproject, String _iddrill, int _opcion, String _cadena) {
	try {
	    tfStartUtmE = _startUtmX;
	    tfUtmStartN = _startUtmY;
	    tfUtmEndE = _endUtmX;
	    tfUtmEndN = _endUtmY;
	    idproject = _idproject;
	    iddrill = _iddrill;
	    cadena = _cadena;
	    opcion = _opcion;
	    if (opcion == 2) {
		TEXTO = "Enter Section UTM";
	    }
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Utm(JDecEntry _UtmE, JDecEntry _UtmN, String _idproject, int _opcion) {
	try {
	    tfUtmEndE = _UtmE;
	    tfUtmEndN = _UtmN;
	    idproject = _idproject;
	    opcion = _opcion;
	    if (opcion == 2) {
		TEXTO = "Enter Section UTM";
	    }
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(339, 209));
	this.setBounds(new Rectangle(10, 10, 339, 209));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(bOk, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(lblEndUtm, null);
	this.getContentPane().setLayout(null);
	this.setTitle("New Project - Set UTM");
	this.getContentPane().add(lblStartUtm, null);
	this.getContentPane().add(pEndUtm, null);
	pStartUtm.setBounds(new Rectangle(5, 50, 325, 40));
	pStartUtm.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	pStartUtm.setLayout(null);
	pEndUtm.setBounds(new Rectangle(5, 105, 325, 40));
	pEndUtm.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	pEndUtm.setLayout(null);
	lblStartUtm.setText("Start UTM");
	lblStartUtm.setBounds(new Rectangle(5, 40, 325, 10));
	lblStartUtm.setFont(new Font("Dialog", 1, 10));
	lblEndUtm.setText("End UTM");
	lblEndUtm.setBounds(new Rectangle(5, 95, 325, 10));
	lblEndUtm.setFont(new Font("Dialog", 1, 10));
	lblEndUtm3.setText("E:");
	lblEndUtm3.setBounds(new Rectangle(10, 13, 10, 15));
	lblEndUtm3.setFont(new Font("Dialog", 1, 10));
	lblEndUtm4.setText("N:");
	lblEndUtm4.setBounds(new Rectangle(165, 13, 10, 15));
	lblEndUtm4.setFont(new Font("Dialog", 1, 10));
	tfStartUtmE.setBounds(new Rectangle(25, 10, 125, 20));
	tfStartUtmE.setDisabledTextColor(Color.black);
	tfUtmStartN.setBounds(new Rectangle(180, 10, 130, 20));
	tfUtmStartN.setDisabledTextColor(Color.black);
	tfUtmEndN.setBounds(new Rectangle(180, 10, 130, 20));
	lblEndUtmN.setText("N:");
	lblEndUtmN.setBounds(new Rectangle(165, 13, 10, 15));
	lblEndUtmN.setFont(new Font("Dialog", 1, 10));
	tfUtmEndE.setBounds(new Rectangle(25, 10, 125, 20));
	lblEndUtmE.setText("E:");
	lblEndUtmE.setBounds(new Rectangle(10, 13, 10, 15));
	lblEndUtmE.setFont(new Font("Dialog", 1, 10));
	bOk.setBounds(new Rectangle(290, 150, 40, 25));
	bCancel.setBounds(new Rectangle(5, 150, 40, 25));
	//bOk.setFont(new Font("Dialog", 1, 10));
	//bOk.setMnemonic('O');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	bOk.setSize(new Dimension(40, 25));
	bOk.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bOk_actionPerformed(e);
		    }

		});
	this.getContentPane().add(pStartUtm, null);
	pEndUtm.add(lblEndUtmE, null);
	pEndUtm.add(tfUtmEndE, null);
	pEndUtm.add(lblEndUtmN, null);
	pEndUtm.add(tfUtmEndN, null);
	pStartUtm.add(tfStartUtmE, null);
	pStartUtm.add(lblEndUtm4, null);
	pStartUtm.add(lblEndUtm3, null);
	pStartUtm.add(tfUtmStartN, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	setearDatos();
    }

    private void bOk_actionPerformed(ActionEvent e) {
	dispose();
    }

    private void setearDatos() {
	String consulta = "";
	if (opcion == 0) {
	    try {
		if (!iddrill.equals("n")) {
		    //En caso de que sea llamado de ModifyDrill
		    System.out.println("uno");
		    consulta = "SELECT X(utm),Y(utm) FROM drilling.drills WHERE iddrill = " + iddrill;
		    tfUtmEndE.setEditable(false);
		    tfUtmEndN.setEditable(false);
		    ResultSet resul = org.digitall.lib.sql.LibSQL.exQuery(consulta);
		    if (resul.next()) {
			tfStartUtmE.setText(resul.getString("X"));
			tfUtmStartN.setText(resul.getString("Y"));
			tfUtmEndE.setText("");
			tfUtmEndN.setText("");
		    }
		} else if (!idproject.equals("n")) {
		    //En caso de que sea llamado de ModifyProject
		    System.out.println("dos");
		    enabledFields();
		    setReturnFields();
		}
	    } catch (SQLException ex) {
		ex.printStackTrace();
	    }
	}
	if (opcion == 1) {
	    System.out.println("tres");
	    tfUtmEndE.setEditable(false);
	    tfUtmEndN.setEditable(false);
	}
	if (opcion == 2) {
	    try {
		consulta = "SELECT X(utm) as utmE,Y(utm) as utmN, X(sectionutm) as E,Y(sectionutm) as N FROM drilling.drills WHERE iddrill = " + iddrill;
		System.out.println("cinco");
		ResultSet resul = org.digitall.lib.sql.LibSQL.exQuery(consulta);
		if (resul.next()) {
		    tfStartUtmE.setText(resul.getString("utmE"));
		    tfUtmStartN.setText(resul.getString("utmN"));
		    tfStartUtmE.setEnabled(false);
		    tfUtmStartN.setEnabled(false);
		    tfUtmEndE.setText(resul.getString("E"));
		    tfUtmEndN.setText(resul.getString("N"));
		}
	    } catch (SQLException ex) {
		ex.printStackTrace();
	    }
	}
	if (opcion == 3) {
	    //Cuando es llamado desde NewDrill
	    System.out.println("cuatro");
	    tfUtmEndE.setEnabled(true);
	    tfUtmEndN.setEditable(true);
	    tfStartUtmE.setEnabled(false);
	    tfUtmStartN.setEnabled(false);
	}
	if (opcion == 5) {
	    //Cuando es llamado desde Modify project, para ingresar startUtm
	    System.out.println("posta 5");
	    tfUtmEndE.setEnabled(false);
	    tfUtmEndN.setEnabled(false);
	    tfStartUtmE.setEditable(true);
	    tfUtmStartN.setEditable(true);
	}
	if (opcion == 6) {
	    //Cuando es llamado desde Modify project, para ingresar endUtm
	    System.out.println("posta 6");
	    tfUtmEndE.setEditable(true);
	    tfUtmEndN.setEditable(true);
	    tfStartUtmE.setEnabled(false);
	    tfUtmStartN.setEnabled(false);
	}
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	dispose();
    }

    private void enabledFields() {
	try {
	    if (cadena.equals("start")) {
		tfUtmEndE.setEditable(false);
		tfUtmEndN.setEditable(false);
		tfStartUtmE.setEditable(true);
		tfUtmStartN.setEditable(true);
		System.out.println("A");
	    } else {
		tfStartUtmE.setEditable(false);
		tfUtmStartN.setEditable(false);
		tfUtmEndE.setEnabled(true);
		tfUtmEndN.setEnabled(true);
		System.out.println("B");
	    }
	    String consult = " SELECT X(startutm) as startx, Y(startutm) as starty, " + " X(endutm) as endx, Y(endutm) as endy FROM drilling.projects WHERE idproject = '" + idproject + "'";
	    System.out.println("consulta: " + consult);
	    ResultSet resul = org.digitall.lib.sql.LibSQL.exQuery(consult);
	    if (resul.next()) {
		tfStartUtmE.setText(resul.getString("startx"));
		tfUtmStartN.setText(resul.getString("starty"));
		tfUtmEndE.setText(resul.getString("endx"));
		tfUtmEndN.setText(resul.getString("endy"));
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
    }

    private void setReturnFields() {
	tfStartUtmE.getText().trim();
	tfUtmStartN.getText().trim();
	tfUtmEndE.getText().trim();
	tfUtmEndN.getText().trim();
    }

}

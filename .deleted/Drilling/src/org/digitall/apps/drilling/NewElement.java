package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;

//

public class NewElement extends BasicDialog {

    private BasicLabel lblSymbol = new BasicLabel();
    private BasicLabel lblName = new BasicLabel();
    private BasicLabel lblElement2 = new BasicLabel();
    private AcceptButton bAcept = new AcceptButton();
    private CloseButton bCancel = new CloseButton();
    private BasicTextField tfSymbol = new BasicTextField();
    private BasicTextField tfName = new BasicTextField();
    private BasicScrollPane jScrollPane1 = new BasicScrollPane();
    private JTextArea taDescription = new JTextArea();
    private String opcion = "", operacion = "", idtab = "";
    private PanelHeader panelHeader;
    private String TEXTO = "";
    private BasicPanel jPanel1 = new BasicPanel();
    private BasicTextField tfColor = new BasicTextField();
    private BasicLabel lblColor = new BasicLabel();

    public NewElement(String _opcion, String _operacion) {
	try {
	    opcion = _opcion;
	    operacion = _operacion;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public NewElement(String _opcion, String _operacion, String _idtab) {
	try {
	    opcion = _opcion;
	    operacion = _operacion;
	    idtab = _idtab;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(342, 248));
	if (operacion.equals("insert")) {
	    setearLbNew();
	} else
	    setearLbUpdate();
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	jPanel1.add(lblColor, null);
	jPanel1.add(tfColor, null);
	jPanel1.add(lblElement2, null);
	jPanel1.add(lblName, null);
	jScrollPane1.getViewport().add(taDescription, null);
	jPanel1.add(jScrollPane1, null);
	jPanel1.add(tfName, null);
	jPanel1.add(tfSymbol, null);
	jPanel1.add(lblSymbol, null);
	this.getContentPane().add(jPanel1, null);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().setLayout(null);
	lblSymbol.setText("Symbol:");
	lblSymbol.setBounds(new Rectangle(15, 5, 45, 15));
	lblSymbol.setFont(new Font("Dialog", 1, 10));
	bAcept.setBounds(new Rectangle(290, 190, 40, 25));
	bAcept.setMnemonic('O');
	bAcept.setToolTipText("OK");
	bAcept.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bAcept_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(5, 190, 40, 25));
	bCancel.setMnemonic('C');
	bCancel.setToolTipText("Cancel");
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	tfSymbol.setBounds(new Rectangle(15, 19, 55, 20));
	tfSymbol.setFont(new Font("Dialog", 1, 10));
	tfSymbol.setText(" ");
	tfName.setBounds(new Rectangle(80, 20, 170, 20));
	tfName.setFont(new Font("Dialog", 1, 10));
	jScrollPane1.setBounds(new Rectangle(15, 55, 300, 75));
	jScrollPane1.getViewport().setLayout(null);
	taDescription.setBounds(new Rectangle(0, 0, 300, 75));
	taDescription.setFont(new Font("Dialog", 1, 10));
	jPanel1.setBounds(new Rectangle(5, 40, 325, 145));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	tfColor.setBounds(new Rectangle(260, 20, 55, 20));
	lblColor.setText("Color:");
	lblColor.setBounds(new Rectangle(260, 5, 45, 15));
	lblColor.setFont(new Font("Dialog", 1, 10));
	lblName.setText("Name:");
	lblName.setBounds(new Rectangle(80, 5, 45, 15));
	lblName.setFont(new Font("Dialog", 1, 10));
	lblElement2.setText("Description:");
	lblElement2.setBounds(new Rectangle(15, 40, 70, 15));
	lblElement2.setFont(new Font("Dialog", 1, 10));
	this.getContentPane().add(bAcept, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	if (operacion.equals("insert")) {
	    //setearLbNew();
	} else {
	    //setearLbUpdate();
	    setearDatos();
	}
	//tfColor.setEditable(false);
	tfColor.setEnabled(false);
	tfColor.setBackground(Color.white);
	if (opcion.equals("qaqc")) {
	    tfColor.setToolTipText("Click to select a color");
	    tfColor.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
			    tfColor_mouseClicked(e);
			}

		    });
	}
    }

    private void newColor() {
	/** Abre el Selector de Colores */
	Color initialColor = tfColor.getBackground();
	Color newColor = JColorChooser.showDialog(this, "Seleccione color", initialColor);
	try {
	    tfColor.setBackground(newColor);
	} catch (NullPointerException x) {
	    x.printStackTrace();
	    System.out.println("canceló!");
	}
    }

    private void setearLbNew() {
	if (opcion.equals("min")) {
	    TEXTO = "Insert a New Mineralogy";
	}
	if (opcion.equals("alt")) {
	    TEXTO = "Insert a New Alteration";
	}
	if (opcion.equals("lith")) {
	    TEXTO = "Insert a New Lithology";
	}
	if (opcion.equals("struc")) {
	    TEXTO = "Insert a New Structure";
	}
	if (opcion.equals("qaqc")) {
	    TEXTO = "Insert a New QAQC";
	    tfSymbol.setEnabled(false);
	    lblSymbol.setEnabled(false);
	}
    }

    private void setearLbUpdate() {
	if (opcion.equals("min")) {
	    TEXTO = "Update Mineralogy";
	}
	if (opcion.equals("alt")) {
	    TEXTO = "Update Alteration";
	}
	if (opcion.equals("lith")) {
	    TEXTO = "Update Lithology";
	}
	if (opcion.equals("struc")) {
	    TEXTO = "Update Structure";
	}
	if (opcion.equals("qaqc")) {
	    TEXTO = "Update QAQC";
	    tfSymbol.setEnabled(false);
	    lblSymbol.setEnabled(false);
	}
    }

    private void setearDatos() {
	String consultaSelect = "";
	ResultSet result = null;
	if (opcion.equals("qaqc")) {
	    consultaSelect = " SELECT idqaqc, name, description, qaqc_tabs.redcolor as red, qaqc_tabs.greencolor as green, qaqc_tabs.bluecolor AS blue" + " FROM tabs.qaqc_tabs WHERE(idqaqc = " + idtab + ")";
	    try {
		result = org.digitall.lib.sql.LibSQL.exQuery(consultaSelect);
		if (result.next()) {
		    tfName.setText(result.getString("name"));
		    taDescription.setText(result.getString("description"));
		    tfColor.setBackground(new Color(result.getInt("red"), result.getInt("green"), result.getInt("blue")));
		}
	    } catch (SQLException ex) {
		ex.printStackTrace();
	    }
	} else {
	    if (opcion.equals("min")) {
		consultaSelect = "SELECT * FROM tabs.mineralization_tabs WHERE(idmin_tab = " + idtab + ")";
	    }
	    if (opcion.equals("alt")) {
		consultaSelect = "SELECT * FROM tabs.alteration_tabs WHERE(idalt_tab = " + idtab + ")";
	    }
	    if (opcion.equals("lith")) {
		consultaSelect = "SELECT * FROM tabs.lithology_tabs WHERE(idlit_tab = " + idtab + ")";
	    }
	    if (opcion.equals("struc")) {
		consultaSelect = "SELECT * FROM tabs.structure_tabs WHERE(idstr_tab = " + idtab + ")";
	    }
	    try {
		result = org.digitall.lib.sql.LibSQL.exQuery(consultaSelect);
		if (result.next()) {
		    tfSymbol.setText(result.getString("code"));
		    tfName.setText(result.getString("name"));
		    taDescription.setText(result.getString("description"));
		}
	    } catch (SQLException ex) {
		ex.printStackTrace();
	    }
	}
    }

    private void actualizarDatos() {
	if ((!tfSymbol.getText().equals("")) && (!tfName.getText().equals(""))) {
	    String consultaUpdate = "", finConsulta = "";
	    if (opcion.equals("qaqc")) {
		consultaUpdate = "UPDATE tabs.qaqc_tabs SET " + " name = '" + tfName.getText().trim() + "'," + " description = '" + taDescription.getText() + "'," + " redcolor = " + tfColor.getBackground().getRed() + "," + " greencolor = " + tfColor.getBackground().getGreen() + "," + " bluecolor = " + tfColor.getBackground().getBlue() + " WHERE idqaqc = " + idtab;
	    } else {
		if (opcion.equals("min")) {
		    consultaUpdate = "UPDATE tabs.mineralization_tabs SET ";
		    finConsulta = "Where idmin_tab = " + idtab;
		}
		if (opcion.equals("alt")) {
		    consultaUpdate = "UPDATE tabs.alteration_tabs SET ";
		    finConsulta = "Where idalt_tab = " + idtab;
		}
		if (opcion.equals("lith")) {
		    consultaUpdate = "UPDATE tabs.lithology_tabs SET ";
		    finConsulta = "Where idlit_tab = " + idtab;
		}
		if (opcion.equals("struc")) {
		    consultaUpdate = "UPDATE tabs.structure_tabs SET ";
		    finConsulta = "Where idstr_tab = " + idtab;
		}
		consultaUpdate = consultaUpdate + " name = '" + tfName.getText().trim() + "'," + " code = '" + tfSymbol.getText().trim() + "'," + " description = '" + taDescription.getText() + "'";
		consultaUpdate = consultaUpdate + finConsulta;
	    }
	    if (LibSQL.exActualizar('m', consultaUpdate)) {
		this.dispose();
	    } else {
	    }
	} else {
	    org.digitall.lib.components.Advisor.messageBox("Field 'Symbol' or 'Name' is empty", "Message");
	}
    }

    private void control() {
	if ((!tfSymbol.getText().equals("")) && (!tfName.getText().equals(""))) {
	    String consultaInsert = "";
	    if (opcion.equals("min")) {
		//opcion = min
		consultaInsert = "INSERT INTO tabs.mineralization_tabs VALUES ((Select max(idmin_tab) + 1 From tabs.mineralization_tabs), '" + tfSymbol.getText().trim() + "' , '" + tfName.getText().trim() + "', '" + taDescription.getText() + "', '') ";
	    }
	    if (opcion.equals("alt")) {
		//opcion = alt
		consultaInsert = "INSERT INTO tabs.alteration_tabs VALUES ((Select max(idalt_tab) + 1 From tabs.alteration_tabs), '" + tfSymbol.getText().trim() + "' , '" + tfName.getText().trim() + "', '" + taDescription.getText() + "', '') ";
	    }
	    if (opcion.equals("lith")) {
		//opcion = lith
		consultaInsert = "INSERT INTO tabs.lithology_tabs VALUES ((Select max(idlit_tab) + 1 From tabs.lithology_tabs), '" + tfSymbol.getText().trim() + "' , '" + tfName.getText().trim() + "', '" + taDescription.getText() + "', '') ";
	    }
	    if (opcion.equals("struc")) {
		//opcion = struc
		consultaInsert = "INSERT INTO tabs.structure_tabs VALUES ((Select max(idstr_tab) + 1 From tabs.structure_tabs), '" + tfSymbol.getText().trim() + "' , '" + tfName.getText().trim() + "', '" + taDescription.getText() + "', '') ";
	    }
	    if (opcion.equals("qaqc")) {
		//opcion = struc
		consultaInsert = "INSERT INTO tabs.qaqc_tabs VALUES ((Select max(idqaqc) + 1 " + " From tabs.qaqc_tabs), '" + tfName.getText().trim() + "'" + " ,'" + taDescription.getText() + "',0" + tfColor.getBackground().getRed() + ", 0" + tfColor.getBackground().getGreen() + ",0" + tfColor.getBackground().getBlue() + ",'') ";
	    }
	    if (LibSQL.exActualizar('a', consultaInsert)) {
		this.dispose();
	    } else {
		org.digitall.lib.components.Advisor.messageBox("Error en la consulta!", "Error");
	    }
	} else {
	    org.digitall.lib.components.Advisor.messageBox("Field 'Symbol' or 'Name' is empty", "Message");
	}
    }

    private void bAcept_actionPerformed(ActionEvent e) {
	if (operacion.equals("insert")) {
	    control();
	} else {
	    actualizarDatos();
	}
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void tfColor_mouseClicked(MouseEvent e) {
	newColor();
    }

}

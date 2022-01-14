package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.digitall.common.drilling.Coordinate;
import org.digitall.common.drilling.PanelHeader;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.JDecEntry;
import org.digitall.lib.components.JTFecha;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.icons.IconTypes;

//
//public class Paso0 extends BasicDialog

public class Paso0 extends PasoAsistente {

    private NextWizardButton bNext = new NextWizardButton();
    private CloseButton bCancel = new CloseButton();
    private ModifyButton btnStartUtm = new ModifyButton();
    private ModifyButton btnEndUtm = new ModifyButton();
    private BasicLabel lblName = new BasicLabel();
    private BasicLabel lblStartUtm = new BasicLabel();
    private BasicLabel lblEndUtm = new BasicLabel();
    private BasicLabel lblDescription = new BasicLabel();
    private BasicLabel lblName1 = new BasicLabel();
    private BasicLabel lblName2 = new BasicLabel();
    private BasicTextField tfName = new BasicTextField();
    private BasicTextField tfStartUtm = new BasicTextField();
    private BasicTextField tfEndUtm = new BasicTextField();
    private JTFecha tfDate = new JTFecha();
    private JDecEntry tfStartUtmX = new JDecEntry();
    private JDecEntry tfStartUtmY = new JDecEntry();
    private JDecEntry tfEndUtmX = new JDecEntry();
    private JDecEntry tfEndUtmY = new JDecEntry();
    private int error = 0;
    private BasicScrollPane jScrollPane1 = new BasicScrollPane();
    private JTextArea taDescription = new JTextArea();
    final String TEXTO = "New Project Wizard";
    private PanelHeader panelHeader;
    private Coordinate startUTM = new Coordinate.Double();
    private Coordinate endUTM = new Coordinate.Double();

    public Paso0() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(353, 278));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(btnEndUtm, null);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().setLayout(null);
	this.setTitle("New Project - Step 0");
	this.setBounds(new Rectangle(10, 10, 352, 278));
	bNext.setBounds(new Rectangle(300, 216, 35, 25));
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setMnemonic('N');
	bNext.setToolTipText("Next");
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(10, 216, 35, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setMnemonic('C');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	lblName.setText("Name:");
	lblName.setBounds(new Rectangle(10, 48, 60, 15));
	lblName.setFont(new Font("Dialog", 1, 10));
	lblName.setHorizontalAlignment(SwingConstants.RIGHT);
	lblStartUtm.setText("Start UTM:");
	lblStartUtm.setBounds(new Rectangle(5, 95, 65, 15));
	lblStartUtm.setFont(new Font("Dialog", 1, 10));
	lblStartUtm.setHorizontalAlignment(SwingConstants.RIGHT);
	lblEndUtm.setText("End UTM:");
	lblEndUtm.setBounds(new Rectangle(5, 120, 65, 15));
	lblEndUtm.setFont(new Font("Dialog", 1, 10));
	lblEndUtm.setHorizontalAlignment(SwingConstants.RIGHT);
	lblDescription.setText("Description:");
	lblDescription.setBounds(new Rectangle(5, 145, 65, 15));
	lblDescription.setFont(new Font("Dialog", 1, 10));
	lblDescription.setHorizontalAlignment(SwingConstants.RIGHT);
	tfName.setBounds(new Rectangle(80, 45, 255, 20));
	tfStartUtm.setBounds(new Rectangle(80, 95, 220, 20));
	tfStartUtm.setEditable(false);
	tfStartUtm.setBackground(Color.white);
	tfEndUtm.setBounds(new Rectangle(80, 120, 220, 20));
	tfEndUtm.setEditable(false);
	tfEndUtm.setBackground(Color.white);
	jScrollPane1.setBounds(new Rectangle(80, 145, 255, 65));
	jScrollPane1.getViewport().setLayout(null);
	taDescription.setBounds(new Rectangle(0, 0, 255, 65));
	btnStartUtm.setBounds(new Rectangle(305, 95, 30, 20));
	btnStartUtm.setFont(new Font("Dialog", 1, 10));
	btnStartUtm.setToolTipText("Enter UTM");
	btnStartUtm.setMnemonic('F');
	btnStartUtm.setSize(new Dimension(30, 20));
	btnStartUtm.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnStartUtm_actionPerformed(e);
		    }

		});
	tfDate.setBounds(new Rectangle(80, 70, 105, 20));
	lblName2.setText("(dd/mm/yyyy)");
	lblName2.setBounds(new Rectangle(195, 73, 65, 15));
	lblName2.setFont(new Font("Dialog", 1, 8));
	lblName2.setHorizontalAlignment(SwingConstants.LEFT);
	lblName2.setForeground(Color.red);
	lblName1.setText("Date:");
	lblName1.setBounds(new Rectangle(10, 73, 60, 15));
	lblName1.setFont(new Font("Dialog", 1, 10));
	lblName1.setHorizontalAlignment(SwingConstants.RIGHT);
	jScrollPane1.getViewport().add(taDescription, null);
	this.getContentPane().add(lblName2, null);
	this.getContentPane().add(lblName1, null);
	this.getContentPane().add(tfDate, null);
	this.getContentPane().add(btnStartUtm, null);
	this.getContentPane().add(jScrollPane1, null);
	this.getContentPane().add(tfEndUtm, null);
	this.getContentPane().add(tfStartUtm, null);
	this.getContentPane().add(tfName, null);
	this.getContentPane().add(lblDescription, null);
	this.getContentPane().add(lblEndUtm, null);
	this.getContentPane().add(lblStartUtm, null);
	this.getContentPane().add(lblName, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bNext, null);
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension frameSize = this.getSize();
	tfDate.setFont(new Font("Dialog", 1, 12));
	tfStartUtm.setFont(new Font("Dialog", 1, 10));
	tfEndUtm.setFont(new Font("Dialog", 1, 10));
	tfName.setFont(new Font("Dialog", 1, 12));
	taDescription.setLineWrap(true);
	taDescription.setWrapStyleWord(true);
	taDescription.setFont(new Font("Default", 1, 12));
	btnEndUtm.setBounds(new Rectangle(305, 120, 30, 20));
	btnEndUtm.setFont(new Font("Dialog", 1, 10));
	btnEndUtm.setToolTipText("Enter UTM");
	btnEndUtm.setMnemonic('F');
	btnEndUtm.setSize(new Dimension(30, 20));
	btnEndUtm.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnEndUtm_actionPerformed(e);
		    }

		});
	if (frameSize.height > screenSize.height) {
	    frameSize.height = screenSize.height;
	}
	if (frameSize.width > screenSize.width) {
	    frameSize.width = screenSize.width;
	}
	this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	cargaDatos();
    }

    private void cargaDatos() {
	tfStartUtmX.setText("");
	tfStartUtmY.setText("");
	tfEndUtmX.setText("");
	tfEndUtmY.setText("");
	tfDate.setText(Proced.FechaHora2(true, false));
	tfStartUtm.setText("");
	tfEndUtm.setText("");
    }

    private void bNext_actionPerformed(ActionEvent e) {
	if (control()) {
	    settingFields();
	    siguiente();
	} else {
	    errores();
	}
    }

    private void settingFields() {
	setArgumento(1, tfName.getText());
	setArgumento(2, taDescription.getText());
	setArgumento(3, tfDate.getText());
	setStartCoord(startUTM);
	setEndCoord(endUTM);
    }

    private void btnStartUtm_actionPerformed(ActionEvent e) {
	UtmCoordinate utm = new UtmCoordinate(startUTM);
	utm.setModal(true);
	utm.setVisible(true);
	setStartUTM();
    }

    private void btnEndUtm_actionPerformed(ActionEvent e) {
	UtmCoordinate utm = new UtmCoordinate(endUTM);
	utm.setModal(true);
	utm.setVisible(true);
	setEndtUTM();
    }

    private void bBack_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	//Aca tiene que ir un cartel que avise de que la carga se cancela y que no se guardarÃ¡n los datos
	this.dispose();
    }

    /**Esta funcion toma los valores seteados en la Clase UTM y los asigna
     * en los TextField llamados tfDireccion y tfEndUtm
     */
    private void showFields() {
	tfStartUtm.setText(tfStartUtmX.getText() + "    " + tfStartUtmY.getText());
	tfEndUtm.setText(tfEndUtmX.getText() + "    " + tfEndUtmY.getText());
    }

    private void bFinish_actionPerformed(ActionEvent e) {
	//Aca se tiene que grabar los datos en la BD y cerrar la operación de Crear un nuevo proyecto
	//settingFields();
	this.dispose();
    }

    private boolean control() {
	if (tfName.getText().equals("")) {
	    error = 1;
	    return false;
	} else
	    return true;
    }

    private void errores() {
	if (error == 1) {
	    org.digitall.lib.components.Advisor.messageBox("Please, insert the name of the Project", "Error");
	}
    }

    private void setStartUTM() {
	if (!startUTM.isNull()) {
	    tfStartUtm.setText("Easting: " + startUTM.getX() + " - Northing: " + startUTM.getY());
	} else {
	    tfStartUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

    private void setEndtUTM() {
	if (!endUTM.isNull()) {
	    tfEndUtm.setText("Easting: " + endUTM.getX() + " - Northing: " + endUTM.getY());
	} else {
	    tfEndUtm.setText("Easting: NOT SET - Northing: NOT SET");
	}
    }

}

package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.lib.icons.IconTypes;

//
//public class Paso1 extends BasicDialog

public class Paso1 extends PasoAsistente {

    private BasicLabel lblProject = new BasicLabel();
    private BasicLabel lblEndUtm = new BasicLabel();
    private BasicLabel lblStartUtm = new BasicLabel();
    private BasicTextField tfProject = new BasicTextField();
    private BasicTextField tfArea = new BasicTextField();
    private BasicTextField tfZone = new BasicTextField();
    private NextWizardButton bNext = new NextWizardButton();
    private PreviousWizardButton bBack = new PreviousWizardButton();
    private CloseButton bCancel = new CloseButton();
    final String TEXTO = "New Project Wizard";
    private PanelHeader panelHeader;

    public Paso1() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(272, 182));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().setLayout(null);
	this.setTitle("New Project - Step 1");
	this.setBounds(new Rectangle(10, 10, 275, 182));
	this.getContentPane().add(panelHeader, null);
	lblProject.setText("Project:");
	lblProject.setBounds(new Rectangle(5, 40, 50, 20));
	lblProject.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProject.setFont(new Font("Dialog", 1, 11));
	tfProject.setBounds(new Rectangle(60, 40, 195, 20));
	tfProject.setEditable(false);
	tfProject.setForeground(Color.red);
	tfArea.setBounds(new Rectangle(60, 90, 195, 20));
	tfArea.setFont(new Font("Dialog", 1, 12));
	lblEndUtm.setText("Area:");
	lblEndUtm.setBounds(new Rectangle(5, 95, 50, 15));
	lblEndUtm.setFont(new Font("Dialog", 1, 10));
	lblEndUtm.setHorizontalAlignment(SwingConstants.RIGHT);
	tfZone.setBounds(new Rectangle(60, 65, 195, 20));
	tfZone.setFont(new Font("Dialog", 1, 12));
	lblStartUtm.setText("Zone:");
	lblStartUtm.setBounds(new Rectangle(5, 70, 50, 15));
	lblStartUtm.setFont(new Font("Dialog", 1, 10));
	lblStartUtm.setHorizontalAlignment(SwingConstants.RIGHT);
	bNext.setBounds(new Rectangle(220, 120, 35, 25));
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setMnemonic('N');
	bNext.setToolTipText("Next");
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bBack.setBounds(new Rectangle(175, 120, 35, 25));
	bBack.setFont(new Font("Dialog", 1, 10));
	bBack.setMnemonic('B');
	bBack.setToolTipText("Back");
	bBack.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bBack_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(10, 120, 35, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setMnemonic('C');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bBack, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().add(lblStartUtm, null);
	this.getContentPane().add(tfZone, null);
	this.getContentPane().add(lblEndUtm, null);
	this.getContentPane().add(tfArea, null);
	this.getContentPane().add(tfProject, null);
	this.getContentPane().add(lblProject, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void bNext_actionPerformed(ActionEvent e) {
	settingFields();
	siguiente();
    }

    private void bBack_actionPerformed(ActionEvent e) {
	previo();
	this.dispose();
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	//Aca tiene que ir un cartel que avise de que la carga se cancela y que no se guardarÃ¡n los datos
	this.dispose();
    }

    private void bFinish_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void settingFields() {
	setArgumento(4, tfZone.getText());
	setArgumento(5, tfArea.getText());
    }

    public void mostrar() {
	tfProject.setText(c(1));
	this.setVisible(true);
    }

}

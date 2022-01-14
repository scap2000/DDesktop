package org.digitall.apps.drilling;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.lib.icons.IconTypes;

//
//public class Paso4 extends BasicDialog {

public class Paso4 extends PasoAsistente {

    private BasicScrollPane spQualityControl = new BasicScrollPane();
    private JTextArea taQualityControl = new JTextArea();
     
    private NextWizardButton bNext = new NextWizardButton();
    private PreviousWizardButton bBack = new PreviousWizardButton();
    private CloseButton bCancel = new CloseButton();
    final String TEXTO = "Quality Control Procedure";
    private PanelHeader panelHeader;

    public Paso4() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(404, 374));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	//
	this.getContentPane().setLayout(null);
	this.setTitle("Quality Control");
	spQualityControl.setBounds(new Rectangle(10, 45, 380, 250));
	taQualityControl.setBounds(new Rectangle(0, 0, 380, 250));
	taQualityControl.setWrapStyleWord(true);
	taQualityControl.setLineWrap(true);
	//new Rectangle(0, 305, 400, 5));
	spQualityControl.getViewport().add(taQualityControl, null);
	this.getContentPane().add(spQualityControl, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bBack, null);
	this.getContentPane().add(bNext, null);
	bNext.setBounds(new Rectangle(350, 315, 40, 25));
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setMnemonic('N');
	bNext.setToolTipText("Next");
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bBack.setBounds(new Rectangle(295, 315, 40, 25));
	bBack.setFont(new Font("Dialog", 1, 10));
	bBack.setMnemonic('B');
	bBack.setToolTipText("Back");
	bBack.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bBack_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(10, 315, 40, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setMnemonic('C');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void settingFields() {
	setArgumento(6, taQualityControl.getText());
    }

    private void bNext_actionPerformed(ActionEvent e) {
	settingFields();
	siguiente();
    }

    private void bBack_actionPerformed(ActionEvent e) {
	previo();
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

}

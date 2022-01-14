package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.apps.drilling.reports.XMLDrillSheet;
import org.digitall.apps.drilling.reports.XMLProjectReport;
import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.JIntEntry;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.data.listeners.Keyboard;
import org.digitall.lib.icons.IconTypes;

//

public class ManagementReport extends BasicDialog {

    private BasicPanel pOptions = new BasicPanel();
    private PanelHeader panelHeader;
    private NextWizardButton bNext = new NextWizardButton();
    private CloseButton bCancel = new CloseButton();
    private BasicLabel lblOptions = new BasicLabel();
    private BasicLabel lblMts = new BasicLabel();
    private BasicLabel lblDepth = new BasicLabel();
    private BasicLabel lblProjectName = new BasicLabel();
    private BasicLabel lblProject = new BasicLabel();
    private JRadioButton rbSheetEmpty = new JRadioButton();
    private JRadioButton rbProjectSummary = new JRadioButton();
    private ButtonGroup grupo = new ButtonGroup();
    private String idproject = "", proyecto = "";
    private JIntEntry tfMts = new JIntEntry();
    final String TEXTO = "Report Management";
     

    public ManagementReport(String _idproject) {
	try {
	    idproject = _idproject;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(300, 202));
	this.getContentPane().setLayout(null);
	this.setBounds(new Rectangle(10, 10, 300, 202));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblProject, null);
	this.getContentPane().add(lblProjectName, null);
	//
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(lblOptions, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().add(pOptions, null);
	this.setTitle("Report Management");
	pOptions.setBounds(new Rectangle(5, 75, 285, 60));
	pOptions.setLayout(null);
	pOptions.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	rbSheetEmpty.setText("Create template");
	rbSheetEmpty.setBounds(new Rectangle(45, 15, 140, 15));
	rbSheetEmpty.setFont(new Font("Dialog", 1, 10));
	rbProjectSummary.setText("Create project summary");
	rbProjectSummary.setBounds(new Rectangle(45, 35, 155, 15));
	rbProjectSummary.setFont(new Font("Dialog", 1, 10));
	rbProjectSummary.setSelected(true);
	tfMts.setBounds(new Rectangle(205, 15, 50, 15));
	tfMts.setFont(new Font("Dialog", 1, 11));
	tfMts.setForeground(Color.red);
	//new Rectangle(0, 55, 295, 5));
	findProjectName();
	lblProject.setText("" + proyecto);
	lblProjectName.setText("Project Name: ");
	lblProjectName.setBounds(new Rectangle(10, 40, 85, 15));
	lblProjectName.setFont(new Font("Dialog", 1, 11));
	lblProject.setBounds(new Rectangle(90, 40, 200, 15));
	lblProject.setFont(new Font("Dialog", 1, 11));
	lblProject.setForeground(Color.blue);
	lblMts.setText("mts");
	lblMts.setBounds(new Rectangle(255, 17, 25, 10));
	lblMts.setFont(new Font("Dialog", 1, 10));
	lblMts.setHorizontalAlignment(SwingConstants.RIGHT);
	lblDepth.setText("Depth:");
	lblDepth.setBounds(new Rectangle(205, 5, 40, 10));
	lblDepth.setFont(new Font("Dialog", 1, 11));
	bNext.setBounds(new Rectangle(250, 145, 40, 25));
	bNext.setMnemonic('N');
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setSize(new Dimension(40, 25));
	bNext.setToolTipText("Next");
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(5, 145, 40, 25));
	bCancel.setMnemonic('C');
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setSize(new Dimension(40, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	lblOptions.setText(" Options");
	lblOptions.setBounds(new Rectangle(10, 67, 50, 15));
	lblOptions.setFont(new Font("Dialog", 1, 10));
	lblOptions.setOpaque(true);
	pOptions.add(lblDepth, null);
	pOptions.add(lblMts, null);
	pOptions.add(tfMts, null);
	pOptions.add(rbSheetEmpty, null);
	pOptions.add(rbProjectSummary, null);
	pOptions.add(rbProjectSummary, null);
	pOptions.add(rbSheetEmpty, null);
	grupo.add(rbSheetEmpty);
	grupo.add(rbProjectSummary);
	rbSheetEmpty.setSelected(true);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	Keyboard.keyboardInit();
    }

    private void bNext_actionPerformed(ActionEvent e) {
	if (rbSheetEmpty.isSelected()) {
	    if (!tfMts.getText().equals("")) {
		int metros = Integer.parseInt((tfMts.getText()));
		if (metros > 0) {
		    XMLDrillSheet reporte = new XMLDrillSheet(idproject, metros, true);
		} else {
		    org.digitall.lib.components.Advisor.messageBox("Please, insert a value > 0", "Message");
		}
	    } else {
		org.digitall.lib.components.Advisor.messageBox("Please, insert a value for depth", "Message");
	    }
	}
	if (rbProjectSummary.isSelected()) {
	    XMLProjectReport reporte = new XMLProjectReport(idproject);
	}
    }

    private void bBack_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void findProjectName() {
	proyecto = org.digitall.lib.sql.LibSQL.getCampo("SELECT name FROM drilling.projects WHERE idproject = " + idproject);
    }

}

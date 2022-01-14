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

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.icons.IconTypes;

//

public class ProjectMan extends BasicDialog {

    private BasicLabel lblOptions = new BasicLabel();
    private BasicLabel lblProjectName = new BasicLabel();
    private NextWizardButton bNext = new NextWizardButton();
    private CloseButton bCancel = new CloseButton();
    private BasicPanel pOptions = new BasicPanel();
    private JRadioButton rbModifyProData = new JRadioButton();
    private JRadioButton rbLoadDrillData = new JRadioButton();
    private JRadioButton rbReportManagement = new JRadioButton();
    private ButtonGroup grupo = new ButtonGroup();
    private String idproject = "", proyecto = "";
    final String TEXTO = "Project Management";
    private PanelHeader panelHeader;
     
    private BasicLabel lblProject = new BasicLabel();

    public ProjectMan(String _idproject) {
	try {
	    idproject = _idproject;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(316, 206));
	this.setBounds(new Rectangle(10, 10, 316, 206));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblProject, null);
	//
	this.getContentPane().add(lblProjectName, null);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(lblOptions, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().add(pOptions, null);
	this.getContentPane().setLayout(null);
	this.setTitle("Project");
	this.setFont(new Font("Dialog", 1, 10));
	pOptions.setBounds(new Rectangle(5, 75, 300, 65));
	pOptions.setLayout(null);
	pOptions.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	rbModifyProData.setText("Modify project data");
	rbModifyProData.setBounds(new Rectangle(10, 10, 135, 15));
	rbModifyProData.setFont(new Font("Dialog", 1, 10));
	rbModifyProData.setMnemonic('M');
	rbLoadDrillData.setText("Load drill data");
	rbLoadDrillData.setBounds(new Rectangle(170, 10, 120, 15));
	rbLoadDrillData.setFont(new Font("Dialog", 1, 10));
	rbLoadDrillData.setMnemonic('L');
	rbReportManagement.setText("Report Management");
	rbReportManagement.setBounds(new Rectangle(10, 35, 135, 15));
	rbReportManagement.setFont(new Font("Dialog", 1, 10));
	rbReportManagement.setMnemonic('R');
	//new Rectangle(0, 55, 310, 5));
	lblProject.setBounds(new Rectangle(90, 40, 215, 15));
	lblProject.setFont(new Font("Dialog", 1, 11));
	lblProject.setHorizontalAlignment(SwingConstants.LEFT);
	lblProject.setForeground(Color.blue);
	findProjectName();
	lblProject.setText("" + proyecto);
	lblProjectName.setText("Project Name: ");
	lblProjectName.setBounds(new Rectangle(10, 40, 85, 15));
	lblProjectName.setFont(new Font("Dialog", 1, 11));
	lblProjectName.setHorizontalAlignment(SwingConstants.LEFT);
	bNext.setBounds(new Rectangle(265, 150, 40, 25));
	bNext.setMnemonic('N');
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setToolTipText("Next");
	bNext.setSize(new Dimension(40, 25));
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(5, 150, 40, 25));
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
	lblOptions.setBounds(new Rectangle(10, 68, 50, 15));
	lblOptions.setFont(new Font("Dialog", 1, 10));
	lblOptions.setOpaque(true);
	pOptions.add(rbReportManagement, null);
	pOptions.add(rbLoadDrillData, null);
	pOptions.add(rbModifyProData, null);
	grupo.add(rbModifyProData);
	grupo.add(rbLoadDrillData);
	grupo.add(rbReportManagement);
	rbLoadDrillData.setSelected(true);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void bNext_actionPerformed(ActionEvent e) {
	if (rbLoadDrillData.isSelected()) {
	    //this.setVisible(false);
	    FindDrills buscaDrills = new FindDrills(idproject);
	    buscaDrills.setModal(true);
	    buscaDrills.setVisible(true);
	    //this.setVisible(true);
	}
	if (rbReportManagement.isSelected()) {
	    ManagementReport managReport = new ManagementReport(idproject);
	    managReport.setModal(true);
	    managReport.setVisible(true);
	}
	if (rbModifyProData.isSelected()) {
	    ModifyProject modificarDatos = new ModifyProject(idproject);
	    modificarDatos.setModal(true);
	    modificarDatos.setVisible(true);
	}
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void bBack_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void findProjectName() {
	proyecto = org.digitall.lib.sql.LibSQL.getCampo("SELECT name FROM drilling.projects WHERE idproject = " + idproject);
    }

}

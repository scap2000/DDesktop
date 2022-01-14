package org.digitall.apps.legalprocs.calendar;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.digitall.apps.legalprocs.calendar.wizard.Manager;
import org.digitall.apps.legalprocs.calendar.wizard.PanelWizard;
import org.digitall.apps.legalprocs.calendar.wizard.Step0;
import org.digitall.apps.legalprocs.calendar.wizard.Step1_ExplorationSteps;
import org.digitall.apps.legalprocs.calendar.wizard.Step2_DiscoveryType;
import org.digitall.apps.legalprocs.calendar.wizard.Step3_MineSteps;
import org.digitall.apps.legalprocs.manager.NewFile;
import org.digitall.apps.legalprocs.manager.classes.ExplorationRequestClass;
import org.digitall.apps.legalprocs.manager.classes.FileClass;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.common.legalprocs.manager.ApplicationConfig;
import org.digitall.lib.icons.IconTypes;

public class FilesToolBar extends JToolBar {

    private BasicButton btnFiles = new BasicButton();
    private BasicButton btnFilesCalendar = new BasicButton();
    private BasicButton btnFilesConfig = new BasicButton();
    private JDesktopPane parentDesktop;
    private JFrame parent;
    private BasicButton btnFileNewFast = new BasicButton();
    private BasicLabel jLabel1 = new BasicLabel();
    private final String title = "Herramientas del Módulo Files";
    private BasicButton btnWizard = new BasicButton();
    private int step = 0, qtySteps;
    private Manager mgmt;
    private ExplorationRequestClass explorationRequestClass;
    private FileClass fileclass;
    private String Title = "";
    private int idFile = -1;

    public FilesToolBar(JDesktopPane _desktop, JFrame _parent) {
	//super(title);
	try {
	    parentDesktop = _desktop;
	    parent = _parent;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	//btnWizard.setEnabled(false);
	btnFiles.setToolTipText("Listado de Expedientes");
	btnFiles.setMaximumSize(new Dimension(35, 35));
	btnFiles.setMinimumSize(new Dimension(35, 35));
	btnFiles.setPreferredSize(new Dimension(35, 35));
	btnFiles.setIcon(IconTypes.filesIcon_22x22);
	btnFiles.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnFiles.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnFiles_actionPerformed(e);
				}

			    }
	);
	btnFilesCalendar.setToolTipText("Calendario");
	btnFilesCalendar.setMaximumSize(new Dimension(35, 35));
	btnFilesCalendar.setMinimumSize(new Dimension(35, 35));
	btnFilesCalendar.setIcon(IconTypes.calendarIcon_22x22);
	btnFilesCalendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnFilesCalendar.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
					    btnFilesCalendar_actionPerformed(e);
					}

				    }
	);
	btnFilesConfig.setToolTipText("Configuración de Tramites");
	btnFilesConfig.setIcon(IconTypes.fileConfigIcon_Off_22x22);
	btnFilesConfig.setRolloverIcon(IconTypes.fileConfigIcon_On_22x22);
	btnFilesConfig.setMaximumSize(new Dimension(35, 35));
	btnFilesConfig.setMinimumSize(new Dimension(35, 35));
	btnFilesConfig.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnFilesConfig.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  btnFilesConfig_actionPerformed(e);
				      }

				  }
	);
	btnFileNewFast.setToolTipText("Carga Rapida de Expediente");
	btnFileNewFast.setIcon(IconTypes.fileNewFast_Off_22x22);
	btnFileNewFast.setRolloverIcon(IconTypes.fileNewFast_On_22x22);
	btnFileNewFast.setMaximumSize(new Dimension(35, 35));
	btnFileNewFast.setMinimumSize(new Dimension(35, 35));
	btnFileNewFast.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  btnFileNewFast_actionPerformed(e);
				      }

				  }
	);
	btnFileNewFast.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnWizard.setToolTipText("Carga Rapida de Expediente");
	btnWizard.setIcon(IconTypes.fileWizard_Off_22x22);
	btnWizard.setRolloverIcon(IconTypes.fileWizard_On_22x22);
	btnWizard.setMaximumSize(new Dimension(35, 35));
	btnWizard.setMinimumSize(new Dimension(35, 35));
	btnWizard.setEnabled(false);
	btnWizard.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnWizard_actionPerformed(e);
				 }

			     }
	);
	btnWizard.setCursor(new Cursor(Cursor.HAND_CURSOR));
	jLabel1.setText("Files");
	jLabel1.setIcon(IconTypes.systemIcon_16x16);
	jLabel1.setMaximumSize(new Dimension(70, 25));
	jLabel1.setFont(new Font("Default", 1, 14));
	jLabel1.setForeground(new Color(0, 0, 82));
	this.setRollover(true);
	this.setToolTipText(title);
	this.add(jLabel1, null);
	this.add(btnFiles, null);
	this.add(btnFilesCalendar, null);
	this.add(btnFileNewFast, null);
	this.add(btnFilesConfig, null);
	this.add(btnWizard, null);
	initializeManagerAndClass();
    }

    private void btnFiles_actionPerformed(ActionEvent e) {
	FilesList filesList = new FilesList();
	filesList.show();
    }

    private void btnFilesCalendar_actionPerformed(ActionEvent e) {
	FilesCalendar filesCalendar = new FilesCalendar();
	filesCalendar.show();
    }

    private void btnFilesConfig_actionPerformed(ActionEvent e) {
	ApplicationConfig applicationConfig = new ApplicationConfig(parent);
	applicationConfig.setModal(true);
	applicationConfig.show();
    }

    private void btnFileNewFast_actionPerformed(ActionEvent e) {
	NewFile newFile = new NewFile();
	newFile.show();
    }

    private void btnWizard_actionPerformed(ActionEvent e) {
	startWizard();
    }

    private void initializeManagerAndClass() {
	mgmt = new Manager();
	qtySteps = 4;
	mgmt.setQtyStep(qtySteps);
    }

    private void startWizard() {
	PanelWizard[] wizard = new PanelWizard[qtySteps];
	wizard[0] = new Step0("Tramites" + Title);
	wizard[1] = new Step1_ExplorationSteps("Cateo o Solictud de Permiso de Exploracion" + Title, parentDesktop, mgmt, idFile);
	wizard[2] = new Step2_DiscoveryType("Adquisicion de Mina" + Title);
	//wizard[3] = new Step3_MineSteps("Adquisicion de Mina por Descubrimiento" + Title, parentDesktop, mgmt, idFile);
	wizard[3] = new Step3_MineSteps("Adquisicion de Mina por Descubrimiento" + Title);
	mgmt.setWizardSteps(wizard);
	mgmt.setStep(step);
	mgmt.setInit();
    }

}

/*
    private void startWizard() {
	Manager mgmt = new Manager();
	int qtySteps = 4;
	int qtyArg = 50;
	mgmt.setQtyStep(qtySteps);
	mgmt.setNewProspection(true);
	//posiblemente sea eliminada
	// nuevo
	ExplorationRequestClass_Old prospection = new ExplorationRequestClass_Old();
	prospection.setQtyArguments(qtyArg);
	prospection.setNewProspection(true);
	PanelWizard[] wizard = new PanelWizard[qtySteps];
	wizard[0] = new Step0("Tramites");
	//wizard[1] = new Step1_ExplorationSteps("Cateo o Solictud de Permiso de Exploracion",parentDesktop, mgmt);
	wizard[1] = new Step1_ExplorationSteps_Old("Cateo o Solictud de Permiso de Exploracion", parentDesktop, mgmt, prospection);
	wizard[2] = new Step2_DiscoveryType("Adquisicion de Mina");
	wizard[3] = new Step3_MineSteps("Adquisicion de Mina por Descubrimiento");
	/*wizard[2] = new Step2_ExplorationForm("Formulario de Cateo o Solictud de Permiso de Exploracion",parentDesktop);
        wizard[3] = new Step3_ExplorationInfo("Informacion Complementaria");
        wizard[4] = new Step4_ExplorationRegistro("Registro");
        wizard[5] = new Step5_ExplorationPublications("Publicaciones");
        wizard[6] = new Step6_ExplorationGrant("Otorgamiento del Permiso de Exploracion");
        wizard[7] = new Step7_ExplorationInstalation("Instalacion del Campamento de Exploracion");
        wizard[8] = new Step8_ExplorationLiberation("Liberacion de Areas de Exploracion");

	mgmt.setWizardSteps(wizard);
	mgmt.setInit();
    }*/
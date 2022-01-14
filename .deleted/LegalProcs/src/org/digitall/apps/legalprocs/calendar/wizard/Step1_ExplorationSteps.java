package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.SwingConstants;

import org.digitall.apps.legalprocs.manager.classes.FileClass;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.buttons.GoButton;
import org.digitall.lib.icons.IconTypes;

//

public class Step1_ExplorationSteps extends PanelWizard {

    private BasicLabel lblCateoOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblInfoOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblRegisterOK = new BasicLabel(IconTypes.stepIncompleteIcon_16x16);
    private BasicLabel lblPublicationsOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblGrantOK = new BasicLabel(IconTypes.stepRejectedIcon_16x16);
    private BasicLabel lblInstalationOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblLiberationOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblOpositionOk = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private GoButton btnCateo = new GoButton();
    private GoButton btnInfo = new GoButton();
    private GoButton btnRegister = new GoButton();
    private GoButton btnPublications = new GoButton();
    private GoButton btnGrant = new GoButton();
    private GoButton btnInstalation = new GoButton();
    private GoButton btnLiberation = new GoButton();
    private String title;
    private JDesktopPane parentDesktop;
    private GoButton btnOposition = new GoButton();
    private Manager mgmt;
    private int idFile = -1;
    private FileClass fileClass;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int CATEO = 1;
    private static final int INFO = 2;
    private static final int REGISTER = 3;
    private static final int PUBLICATIONS = 4;
    private static final int GRANT = 5;
    private static final int INSTALATION = 6;
    private static final int LIBERATION = 7;
    private static final int OPOSITION = 8;

    public Step1_ExplorationSteps(String _title, JDesktopPane _parentDesktop) {
	try {
	    parentDesktop = _parentDesktop;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Step1_ExplorationSteps(String _title, JDesktopPane _parentDesktop, Manager _mgmt, int _idFile) {
	try {
	    idFile = _idFile;
	    mgmt = _mgmt;
	    parentDesktop = _parentDesktop;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Step1_ExplorationSteps(String _title, JDesktopPane _parentDesktop, Manager _mgmt, FileClass _fileClass) {
	try {
	    fileClass = _fileClass;
	    mgmt = _mgmt;
	    parentDesktop = _parentDesktop;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(451, 308));
	this.setBounds(new Rectangle(10, 10, this.getWidth(), this.getHeight()));
	this.setName(title);
	lblCateoOK.setBounds(new Rectangle(415, 10, 25, 25));
	lblCateoOK.setIconTextGap(0);
	lblCateoOK.setSize(new Dimension(25, 25));
	btnCateo.setText("Formulario de Cateo o Solicitud de Permiso de Exploracion.");
	btnInfo.setText("Informacion Complementaria.");
	btnRegister.setText("Registro.");
	btnPublications.setText("Publicaciones.");
	btnOposition.setText("Oposiciones.");
	btnGrant.setText("Otorgamiento del Permiso de Exploracion.");
	btnInstalation.setText("Instalacion del Campamento de Exploracion.");
	btnLiberation.setText("Liberacion de Areas de Exploracion.");
	btnCateo.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnPublications.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnOposition.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnGrant.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnInstalation.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnLiberation.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnCateo.setBounds(new Rectangle(10, 10, 385, 25));
	btnCateo.setSize(new Dimension(385, 25));
	btnCateo.setHorizontalAlignment(SwingConstants.LEFT);
	btnCateo.setHorizontalTextPosition(SwingConstants.RIGHT);
	btnCateo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnCateo.setMargin(new Insets(1, 1, 1, 1));
	btnCateo.setIconTextGap(10);
	btnCateo.setFont(new Font("Default", 0, 11));
	btnCateo.setContentAreaFilled(false);
	btnCateo.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnCateo_actionPerformed(e);
				}

			    }
	);
	lblInfoOK.setBounds(new Rectangle(415, 48, 25, 25));
	lblInfoOK.setIconTextGap(0);
	lblInfoOK.setSize(new Dimension(25, 25));
	lblRegisterOK.setBounds(new Rectangle(415, 86, 25, 25));
	lblRegisterOK.setIconTextGap(0);
	lblRegisterOK.setSize(new Dimension(25, 25));
	lblPublicationsOK.setBounds(new Rectangle(415, 124, 25, 25));
	lblPublicationsOK.setIconTextGap(0);
	lblPublicationsOK.setSize(new Dimension(25, 25));
	lblGrantOK.setBounds(new Rectangle(415, 199, 25, 25));
	lblGrantOK.setIconTextGap(0);
	lblGrantOK.setSize(new Dimension(25, 25));
	lblInstalationOK.setBounds(new Rectangle(415, 237, 25, 25));
	lblInstalationOK.setIconTextGap(0);
	lblInstalationOK.setSize(new Dimension(25, 25));
	lblLiberationOK.setBounds(new Rectangle(415, 275, 25, 25));
	lblLiberationOK.setIconTextGap(0);
	lblLiberationOK.setSize(new Dimension(25, 25));
	btnInfo.setBounds(new Rectangle(10, 48, 385, 25));
	btnInfo.setHorizontalAlignment(SwingConstants.LEFT);
	btnInfo.setHorizontalTextPosition(SwingConstants.RIGHT);
	btnInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnInfo.setMargin(new Insets(1, 20, 1, 20));
	btnInfo.setIconTextGap(10);
	btnInfo.setFont(new Font("Default", 0, 11));
	btnInfo.setContentAreaFilled(false);
	btnInfo.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnInfo_actionPerformed(e);
			       }

			   }
	);
	btnRegister.setBounds(new Rectangle(10, 86, 385, 25));
	btnRegister.setHorizontalAlignment(SwingConstants.LEFT);
	btnRegister.setHorizontalTextPosition(SwingConstants.RIGHT);
	btnRegister.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnRegister.setMargin(new Insets(1, 20, 1, 20));
	btnRegister.setIconTextGap(10);
	btnRegister.setFont(new Font("Default", 0, 11));
	btnRegister.setContentAreaFilled(false);
	btnRegister.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       btnRegister_actionPerformed(e);
				   }

			       }
	);
	btnPublications.setBounds(new Rectangle(10, 124, 385, 25));
	btnPublications.setHorizontalAlignment(SwingConstants.LEFT);
	btnPublications.setHorizontalTextPosition(SwingConstants.RIGHT);
	btnPublications.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnPublications.setMargin(new Insets(1, 20, 1, 20));
	btnPublications.setIconTextGap(10);
	btnPublications.setFont(new Font("Default", 0, 11));
	btnPublications.setContentAreaFilled(false);
	btnPublications.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   btnPublications_actionPerformed(e);
				       }

				   }
	);
	btnGrant.setBounds(new Rectangle(10, 199, 385, 25));
	btnGrant.setHorizontalAlignment(SwingConstants.LEFT);
	btnGrant.setHorizontalTextPosition(SwingConstants.RIGHT);
	btnGrant.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnGrant.setMargin(new Insets(1, 20, 1, 20));
	btnGrant.setIconTextGap(10);
	btnGrant.setFont(new Font("Default", 0, 11));
	btnGrant.setContentAreaFilled(false);
	btnGrant.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnGrant_actionPerformed(e);
				}

			    }
	);
	btnInstalation.setBounds(new Rectangle(10, 237, 385, 25));
	btnInstalation.setHorizontalAlignment(SwingConstants.LEFT);
	btnInstalation.setHorizontalTextPosition(SwingConstants.RIGHT);
	btnInstalation.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnInstalation.setMargin(new Insets(1, 20, 1, 20));
	btnInstalation.setIconTextGap(10);
	btnInstalation.setFont(new Font("Default", 0, 11));
	btnInstalation.setContentAreaFilled(false);
	btnInstalation.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  btnInstalation_actionPerformed(e);
				      }

				  }
	);
	btnLiberation.setBounds(new Rectangle(10, 275, 385, 25));
	btnLiberation.setHorizontalAlignment(SwingConstants.LEFT);
	btnLiberation.setHorizontalTextPosition(SwingConstants.RIGHT);
	btnLiberation.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnLiberation.setMargin(new Insets(1, 20, 1, 20));
	btnLiberation.setIconTextGap(10);
	btnLiberation.setFont(new Font("Default", 0, 11));
	btnLiberation.setContentAreaFilled(false);
	btnLiberation.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnLiberation_actionPerformed(e);
				     }

				 }
	);
	btnOposition.setBounds(new Rectangle(10, 161, 385, 25));
	btnOposition.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnOposition.setIconTextGap(10);
	btnOposition.setMargin(new Insets(1, 1, 1, 1));
	btnOposition.setHorizontalAlignment(SwingConstants.LEFT);
	btnOposition.setSize(new Dimension(385, 25));
	btnOposition.setFont(new Font("Default", 0, 11));
	btnOposition.setContentAreaFilled(false);
	btnOposition.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnOposition_actionPerformed(e);
				    }

				}
	);
	lblOpositionOk.setBounds(new Rectangle(415, 161, 25, 25));
	this.add(lblOpositionOk, null);
	this.add(btnOposition, null);
	this.add(btnCateo, null);
	this.add(btnLiberation, null);
	this.add(btnInstalation, null);
	this.add(btnGrant, null);
	this.add(btnPublications, null);
	this.add(btnRegister, null);
	this.add(btnInfo, null);
	this.add(lblLiberationOK, null);
	this.add(lblInstalationOK, null);
	this.add(lblGrantOK, null);
	this.add(lblPublicationsOK, null);
	this.add(lblRegisterOK, null);
	this.add(lblInfoOK, null);
	this.add(lblCateoOK, null);
	setIcons();
    }

    private void btnCateo_actionPerformed(ActionEvent e) {
	ExplorationRequest explorationRequest = new ExplorationRequest(btnCateo.getText(), parentDesktop, false, mgmt, fileClass.getExplorationRequestClass());
	//ExplorationRequest explorationRequest = new ExplorationRequest(btnCateo.getText(),parentDesktop,false, mgmt, estado);
	org.digitall.lib.components.ComponentsManager.centerWindow(explorationRequest);
	explorationRequest.setModal(true);
	explorationRequest.setVisible(true);
	int status = fileClass.getExplorationRequestStatus();
	updateIcons(lblCateoOK, status);
    }

    private void btnInfo_actionPerformed(ActionEvent e) {
	if (Advisor.question(this, "Informacion Complementaria", "¿Adjuntar Informacion Complementaria?")) {
	    ExplorationRequest explorationRequest = new ExplorationRequest(btnCateo.getText(), parentDesktop, true);
	    explorationRequest.setModal(true);
	    org.digitall.lib.components.ComponentsManager.centerWindow(explorationRequest);
	    explorationRequest.setVisible(true);
	} else {
	    lblInfoOK.setIcon(IconTypes.stepApplyIcon_16x16);
	}
    }

    private void btnRegister_actionPerformed(ActionEvent e) {
	PanelFile panelFile = new PanelFile(btnRegister.getText());
	panelFile.setModal(true);
	//parentDesktop.add(panelFile);
	panelFile.setVisible(true);
	org.digitall.lib.components.ComponentsManager.centerWindow(panelFile);
    }

    private void btnPublications_actionPerformed(ActionEvent e) {
	ExplorationPublications explorationPublications = new ExplorationPublications(btnPublications.getText());
	explorationPublications.setModal(true);
	org.digitall.lib.components.ComponentsManager.centerWindow(explorationPublications);
	explorationPublications.setVisible(true);
    }

    private void btnGrant_actionPerformed(ActionEvent e) {
	ExplorationGrantPermision explorationGrantPermision = new ExplorationGrantPermision(btnGrant.getText());
	explorationGrantPermision.setModal(true);
	org.digitall.lib.components.ComponentsManager.centerWindow(explorationGrantPermision);
	explorationGrantPermision.setVisible(true);
    }

    private void btnInstalation_actionPerformed(ActionEvent e) {
	//goStep(7);
    }

    private void btnLiberation_actionPerformed(ActionEvent e) {
	//goStep(8);
    }

    private void btnOposition_actionPerformed(ActionEvent e) {
	ExplorationOposition explorationOposition = new ExplorationOposition(btnOposition.getText());
	explorationOposition.setModal(true);
	org.digitall.lib.components.ComponentsManager.centerWindow(explorationOposition);
	explorationOposition.setVisible(true);
    }

    private void setIcons() {
	System.out.println("");
	if (fileClass.getIdFile() == -1 || fileClass.isMine()) {
	    setNoMadeIcon();
	} else {
	    setExplorationRequestIcons(lblCateoOK, CATEO);
	    setExplorationRequestIcons(lblInfoOK, INFO);
	    setExplorationRequestIcons(lblRegisterOK, REGISTER);
	    setExplorationRequestIcons(lblPublicationsOK, PUBLICATIONS);
	    setExplorationRequestIcons(lblOpositionOk, GRANT);
	    setExplorationRequestIcons(lblGrantOK, INSTALATION);
	    setExplorationRequestIcons(lblInstalationOK, LIBERATION);
	    setExplorationRequestIcons(lblLiberationOK, OPOSITION);
	}
    }

    private void aditionalInformationControl() {
	lblInfoOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void registerControl() {
	lblRegisterOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void publicationsControl() {
	lblPublicationsOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void opositionsControl() {
	lblOpositionOk.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void grantControl() {
	lblGrantOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void instalationControl() {
	lblInstalationOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void liberationControl() {
	lblLiberationOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void setLabel() {

    }

    private void setNoMadeIcon() {
	lblCateoOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblInfoOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblRegisterOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblPublicationsOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblOpositionOk.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblGrantOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblInstalationOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblLiberationOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void setExplorationRequestIcons(BasicLabel label, int opcion) {
	int status;
	if (opcion == CATEO) {
	    status = fileClass.getExplorationRequestStatus();
	} else {
	    status = 1;
	}
	switch (status) {
	    case NO_MADE :
		label.setIcon(IconTypes.stepNoMadeIcon_16x16);
		break;
	    case INCOMPLETE :
		label.setIcon(IconTypes.stepIncompleteIcon_16x16);
		break;
	    case COMPLETE :
		label.setIcon(IconTypes.stepApplyIcon_16x16);
		break;
	    case REJECTED :
		label.setIcon(IconTypes.stepRejectedIcon_16x16);
		break;
	}
    }

    private void updateIcons(BasicLabel label, int _status) {
	switch (_status) {
	    case NO_MADE :
		label.setIcon(IconTypes.stepNoMadeIcon_16x16);
		break;
	    case INCOMPLETE :
		label.setIcon(IconTypes.stepIncompleteIcon_16x16);
		break;
	    case COMPLETE :
		label.setIcon(IconTypes.stepApplyIcon_16x16);
		break;
	    case REJECTED :
		label.setIcon(IconTypes.stepRejectedIcon_16x16);
		break;
	}
    }

    public void loadObject() {
    }

}

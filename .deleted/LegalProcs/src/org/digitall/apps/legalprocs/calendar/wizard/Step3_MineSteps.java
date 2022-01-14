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
import org.digitall.apps.legalprocs.manager.classes.MineRequestClass;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.buttons.GoButton;
import org.digitall.lib.icons.IconTypes;

//

public class Step3_MineSteps extends PanelWizard {

    private String title;
    private GoButton btnManifest = new GoButton();
    private GoButton btnContradictions = new GoButton();
    private GoButton btnInfo = new GoButton();
    private GoButton btnArea = new GoButton();
    private GoButton btnAccept = new GoButton();
    private GoButton btnRegister = new GoButton();
    private GoButton btnPublications = new GoButton();
    private GoButton btnLegalWork = new GoButton();
    private GoButton btnAmbientalInfo = new GoButton();
    private BasicLabel lblMineOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblcontradictionsOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblFreeAreaOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblComplementInfoOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblPublicationsOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblLegalWorkOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblRegisterOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblAcceptanceOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblAmbientalOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private FileClass fileClass;
    private int idFile = -1;
    private Manager mgmt;
    private JDesktopPane parentDesktop;
    private MineRequestClass mineRequestClass;
    //CONST
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private final int MINE = 50;
    private final int CONTRADICTIONS = 51;
    private final int COMPLEMENT_INFO = 52;
    private final int FREE_AREA = 53;
    private final int ACCEPTANCE = 54;
    private final int MINE_REGISTER = 55;
    private final int MINE_PUBLICATIONS = 56;
    private final int LEGAL_WORK = 57;
    private final int AMBIENTAL_REPORT = 58;

    public Step3_MineSteps(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Step3_MineSteps(String _title, JDesktopPane _parentDesktop, Manager _mgmt, int _idFile) {
	try {
	    title = _title;
	    mgmt = _mgmt;
	    parentDesktop = _parentDesktop;
	    idFile = _idFile;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Step3_MineSteps(String _title, JDesktopPane _parentDesktop, FileClass _fileClass) {
	try {
	    title = _title;
	    parentDesktop = _parentDesktop;
	    fileClass = _fileClass;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(409, 345));
	this.setBounds(new Rectangle(10, 10, this.getWidth(), this.getHeight()));
	this.setName(title);
	btnManifest.setText("Formulario de Manifestacion o Solitud de Mina.");
	btnContradictions.setText("Contradicciones.");
	btnInfo.setText("Informacion Complementaria.");
	btnArea.setText("Area Libre o Disponible.");
	btnAccept.setText("Aceptacion Expresa.");
	btnRegister.setText("Registro.");
	btnPublications.setText("Publicaciones.");
	btnLegalWork.setText("Labor Legal.");
	btnAmbientalInfo.setText("Informe de Impacto Ambiental.");
	btnManifest.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnContradictions.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnArea.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnAccept.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnPublications.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnLegalWork.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnAmbientalInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnManifest.setBounds(new Rectangle(15, 10, 77, 25));
	btnManifest.setSize(new Dimension(300, 25));
	btnManifest.setHorizontalAlignment(SwingConstants.LEFT);
	btnManifest.setMargin(new Insets(1, 1, 1, 1));
	btnManifest.setIconTextGap(10);
	btnManifest.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnManifest.setContentAreaFilled(false);
	btnManifest.setFont(new Font("Default", 0, 11));
	btnManifest.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       btnManifest_actionPerformed(e);
				   }

			       }
	);
	btnContradictions.setBounds(new Rectangle(15, 46, 310, 25));
	btnContradictions.setSize(new Dimension(300, 25));
	btnContradictions.setHorizontalAlignment(SwingConstants.LEFT);
	btnContradictions.setMargin(new Insets(1, 1, 1, 1));
	btnContradictions.setIconTextGap(10);
	btnContradictions.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnContradictions.setContentAreaFilled(false);
	btnContradictions.setFont(new Font("Default", 0, 11));
	btnInfo.setBounds(new Rectangle(15, 83, 310, 25));
	btnInfo.setSize(new Dimension(300, 25));
	btnInfo.setHorizontalAlignment(SwingConstants.LEFT);
	btnInfo.setMargin(new Insets(1, 1, 1, 1));
	btnInfo.setIconTextGap(10);
	btnInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnInfo.setContentAreaFilled(false);
	btnInfo.setFont(new Font("Default", 0, 11));
	btnArea.setBounds(new Rectangle(15, 119, 310, 25));
	btnArea.setSize(new Dimension(300, 25));
	btnArea.setHorizontalAlignment(SwingConstants.LEFT);
	btnArea.setMargin(new Insets(1, 1, 1, 1));
	btnArea.setIconTextGap(10);
	btnArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnArea.setContentAreaFilled(false);
	btnArea.setFont(new Font("Default", 0, 11));
	btnAccept.setBounds(new Rectangle(15, 155, 310, 25));
	btnAccept.setSize(new Dimension(300, 25));
	btnAccept.setHorizontalAlignment(SwingConstants.LEFT);
	btnAccept.setMargin(new Insets(1, 1, 1, 1));
	btnAccept.setIconTextGap(10);
	btnAccept.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnAccept.setContentAreaFilled(false);
	btnAccept.setFont(new Font("Default", 0, 11));
	btnRegister.setBounds(new Rectangle(15, 191, 310, 25));
	btnRegister.setSize(new Dimension(300, 25));
	btnRegister.setHorizontalAlignment(SwingConstants.LEFT);
	btnRegister.setMargin(new Insets(1, 1, 1, 1));
	btnRegister.setIconTextGap(10);
	btnRegister.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnRegister.setContentAreaFilled(false);
	btnRegister.setFont(new Font("Default", 0, 11));
	btnPublications.setBounds(new Rectangle(15, 228, 310, 25));
	btnPublications.setSize(new Dimension(300, 25));
	btnPublications.setHorizontalAlignment(SwingConstants.LEFT);
	btnPublications.setMargin(new Insets(1, 1, 1, 1));
	btnPublications.setIconTextGap(10);
	btnPublications.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnPublications.setContentAreaFilled(false);
	btnPublications.setFont(new Font("Default", 0, 11));
	btnLegalWork.setBounds(new Rectangle(15, 264, 310, 25));
	btnLegalWork.setSize(new Dimension(300, 25));
	btnLegalWork.setHorizontalAlignment(SwingConstants.LEFT);
	btnLegalWork.setMargin(new Insets(1, 1, 1, 1));
	btnLegalWork.setIconTextGap(10);
	btnLegalWork.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnLegalWork.setContentAreaFilled(false);
	btnLegalWork.setFont(new Font("Default", 0, 11));
	btnAmbientalInfo.setBounds(new Rectangle(15, 300, 310, 25));
	btnAmbientalInfo.setSize(new Dimension(300, 25));
	btnAmbientalInfo.setHorizontalAlignment(SwingConstants.LEFT);
	btnAmbientalInfo.setMargin(new Insets(1, 1, 1, 1));
	btnAmbientalInfo.setIconTextGap(10);
	btnAmbientalInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnAmbientalInfo.setContentAreaFilled(false);
	btnAmbientalInfo.setFont(new Font("Default", 0, 11));
	lblMineOK.setBounds(new Rectangle(350, 10, 25, 25));
	lblMineOK.setIconTextGap(0);
	lblMineOK.setSize(new Dimension(25, 25));
	lblcontradictionsOK.setBounds(new Rectangle(350, 45, 25, 25));
	lblcontradictionsOK.setIconTextGap(0);
	lblcontradictionsOK.setSize(new Dimension(25, 25));
	lblFreeAreaOK.setBounds(new Rectangle(350, 120, 25, 25));
	lblFreeAreaOK.setIconTextGap(0);
	lblFreeAreaOK.setSize(new Dimension(25, 25));
	lblComplementInfoOK.setBounds(new Rectangle(350, 85, 25, 25));
	lblComplementInfoOK.setIconTextGap(0);
	lblComplementInfoOK.setSize(new Dimension(25, 25));
	lblPublicationsOK.setBounds(new Rectangle(350, 230, 25, 25));
	lblPublicationsOK.setIconTextGap(0);
	lblPublicationsOK.setSize(new Dimension(25, 25));
	lblLegalWorkOK.setBounds(new Rectangle(350, 265, 25, 25));
	lblLegalWorkOK.setIconTextGap(0);
	lblLegalWorkOK.setSize(new Dimension(25, 25));
	lblRegisterOK.setBounds(new Rectangle(350, 190, 25, 25));
	lblRegisterOK.setIconTextGap(0);
	lblRegisterOK.setSize(new Dimension(25, 25));
	lblAcceptanceOK.setBounds(new Rectangle(350, 155, 25, 25));
	lblAcceptanceOK.setIconTextGap(0);
	lblAcceptanceOK.setSize(new Dimension(25, 25));
	lblAmbientalOK.setBounds(new Rectangle(350, 300, 25, 25));
	lblAmbientalOK.setIconTextGap(0);
	lblAmbientalOK.setSize(new Dimension(25, 25));
	this.add(lblAmbientalOK, null);
	this.add(lblAcceptanceOK, null);
	this.add(lblRegisterOK, null);
	this.add(lblLegalWorkOK, null);
	this.add(lblPublicationsOK, null);
	this.add(lblComplementInfoOK, null);
	this.add(lblFreeAreaOK, null);
	this.add(lblcontradictionsOK, null);
	this.add(lblMineOK, null);
	this.add(btnAmbientalInfo, null);
	this.add(btnLegalWork, null);
	this.add(btnPublications, null);
	this.add(btnRegister, null);
	this.add(btnAccept, null);
	this.add(btnArea, null);
	this.add(btnInfo, null);
	this.add(btnContradictions, null);
	this.add(btnManifest, null);
	setIcons();
    }

    private void btnManifest_actionPerformed(ActionEvent e) {
	ManifestForm manifestForm = new ManifestForm(btnManifest.getText(), parentDesktop, fileClass.getMineRequestClass());
	manifestForm.setModal(true);
	org.digitall.lib.components.ComponentsManager.centerWindow(manifestForm);
	manifestForm.setVisible(true);
    }

    private void setIcons() {
	if (fileClass.getIdFile() == -1 || !fileClass.isMine()) {
	    setNoMadeIcon();
	} else {
	    setMineRequestIcons(lblMineOK, MINE);
	    setMineRequestIcons(lblcontradictionsOK, CONTRADICTIONS);
	    setMineRequestIcons(lblComplementInfoOK, COMPLEMENT_INFO);
	    setMineRequestIcons(lblFreeAreaOK, FREE_AREA);
	    setMineRequestIcons(lblAcceptanceOK, ACCEPTANCE);
	    setMineRequestIcons(lblRegisterOK, MINE_REGISTER);
	    setMineRequestIcons(lblPublicationsOK, MINE_PUBLICATIONS);
	    setMineRequestIcons(lblLegalWorkOK, LEGAL_WORK);
	    setMineRequestIcons(lblAmbientalOK, AMBIENTAL_REPORT);
	}
    }

    private void setNoMadeIcon() {
	lblMineOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblcontradictionsOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblComplementInfoOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblFreeAreaOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblAcceptanceOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblRegisterOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblPublicationsOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblLegalWorkOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblAmbientalOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void setMineRequestIcons(BasicLabel label, int opcion) {
	int status;
	if (opcion == MINE) {
	    status = fileClass.getMineRequestStatus();
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

}

package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.SwingConstants;

import org.digitall.apps.legalprocs.manager.classes.ExplorationRequestClass;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.icons.IconTypes;

//

public class ExplorationRequest extends BasicDialog {

    //public class ExplorationRequest extends BasicInternalFrame {
    private BasicPanel panelExplorationForm = new BasicPanel();
    private JDesktopPane parentDesktop;
    private CloseButton btnClose = new CloseButton();
    private AcceptButton btnAccept = new AcceptButton();
    private boolean info = false;
    private String title;
    private BasicLabel lblInfo = new BasicLabel();
    private BasicLabel lblDeclarationOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblSolicitorOK = new BasicLabel(IconTypes.stepIncompleteIcon_16x16);
    private BasicLabel lblLegalRepOK = new BasicLabel(IconTypes.stepRejectedIcon_16x16);
    private BasicLabel lblUbicatonOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblCoordOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblTerrainOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblProgramOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicLabel lblFileOK = new BasicLabel(IconTypes.stepNoMadeIcon_16x16);
    private BasicButton btnSolicitor = new BasicButton();
    private BasicButton btnLegalRep = new BasicButton();
    private BasicButton btnUbication = new BasicButton();
    private BasicButton btnCoord = new BasicButton();
    private BasicButton btnTerrain = new BasicButton();
    private BasicButton btnProgram = new BasicButton();
    private BasicButton btnDeclaration = new BasicButton();
    private BasicButton btnFile = new BasicButton();
    private BasicLabel lblSolicitorStatus = new BasicLabel();
    private BasicLabel lblLegalRepStatus = new BasicLabel();
    private BasicLabel lblDeclarationStatus = new BasicLabel();
    private BasicLabel lblUbicationStatus = new BasicLabel();
    private BasicLabel lblProgramStatus = new BasicLabel();
    private BasicLabel lblTerrainStatus = new BasicLabel();
    private BasicLabel lblCoordStatus = new BasicLabel();
    private BasicLabel lblFileStatus = new BasicLabel();
    private Manager mgmt;
    private PanelUbication panelUbication;
    private PanelTerrainData panelTerrainData;
    private int estado;
    private ExplorationRequestClass explorationRequestClass;
    private static final String REALIZADO = "REALIZADO";
    private static final String INCOMPLETO = "INCOMPLETO";
    private int solicitorStatus = 1;
    private int legalRepStatus = 1;
    private int ubicationStatus = 1;
    private int coordinatesStatus = 1;
    private int terrainDataStatus = 1;
    private int explorationSchemeStatus = 1;
    private int swornStatementStatus = 1;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;
    private static final int SOLICITOR = 1;
    private static final int LEGALREP = 2;
    private static final int UBICATION = 3;
    private static final int COORDINATES = 4;
    private static final int TERRAIN = 5;
    private static final int PROGRAM = 6;
    private static final int DECLARATION = 7;

    public ExplorationRequest(String _title, JDesktopPane _parentDesktop, boolean _info) {
	try {
	    title = _title;
	    parentDesktop = _parentDesktop;
	    info = _info;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ExplorationRequest(String _title, JDesktopPane _parentDesktop, boolean _info, Manager _mgmt, ExplorationRequestClass _explorationRequestClass) {
	try {
	    explorationRequestClass = _explorationRequestClass;
	    //estado = _estado;
	    title = _title;
	    parentDesktop = _parentDesktop;
	    info = _info;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ExplorationRequest() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(570, 457));
	this.setTitle(title);
	this.getContentPane().setLayout(null);
	panelExplorationForm.setBounds(new Rectangle(12, 65, 540, 305));
	btnClose.setBounds(new Rectangle(515, 395, 40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	btnAccept.setBounds(new Rectangle(15, 395, 40, 25));
	btnAccept.setSize(new Dimension(40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     //btnAccept_actionPerformed(e);
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	lblInfo.setText("INFORMACION COMPLEMENTARIA");
	lblInfo.setBounds(new Rectangle(170, 40, 225, 14));
	lblInfo.setFont(new Font("Default", 1, 12));
	lblInfo.setForeground(new Color(0, 0, 132));
	lblInfo.setSize(new Dimension(225, 14));
	lblInfo.setVisible(info);
	panelExplorationForm.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	btnTerrain.setText("Datos del Terreno");
	btnProgram.setText("Programa minimo o trabajos a realizar");
	btnDeclaration.setText("Declaracion jurada sobre la inexistencia de prohibiciones");
	btnLegalRep.setText("Nombre y Domicilio del Representante Legal");
	btnUbication.setText("Ubicacion de la Solicitud");
	btnFile.setText("Nï¿½ de Expediente y Matricula Catastral");
	btnCoord.setText("Coordenadas de los Vertices del Area Solicitada");
	btnSolicitor.setText("Nombre y Domicilio del Solicitante");
	btnTerrain.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnProgram.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnDeclaration.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnLegalRep.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnUbication.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnFile.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnCoord.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnSolicitor.setCursor(new Cursor(Cursor.HAND_CURSOR));
	lblSolicitorStatus.setText("INCOMPLETO");
	lblSolicitorStatus.setBounds(new Rectangle(430, 18, 80, 25));
	lblSolicitorStatus.setFont(new Font("Dialog", 1, 11));
	lblSolicitorStatus.setForeground(new Color(148, 107, 0));
	lblSolicitorStatus.setSize(new Dimension(80, 25));
	lblLegalRepStatus.setText("RECHAZADO");
	lblLegalRepStatus.setBounds(new Rectangle(430, 53, 80, 25));
	lblLegalRepStatus.setFont(new Font("Dialog", 1, 11));
	lblLegalRepStatus.setForeground(new Color(148, 107, 0));
	lblLegalRepStatus.setSize(new Dimension(80, 25));
	//lblDeclarationStatus.setText("REALIZADO");
	lblDeclarationStatus.setBounds(new Rectangle(430, 228, 80, 25));
	lblDeclarationStatus.setFont(new Font("Dialog", 1, 11));
	lblDeclarationStatus.setForeground(new Color(148, 107, 0));
	lblDeclarationStatus.setSize(new Dimension(80, 25));
	lblUbicationStatus.setText("RECHAZADO");
	lblUbicationStatus.setBounds(new Rectangle(430, 90, 80, 25));
	lblUbicationStatus.setFont(new Font("Dialog", 1, 11));
	lblUbicationStatus.setForeground(new Color(148, 107, 0));
	lblUbicationStatus.setSize(new Dimension(80, 25));
	lblProgramStatus.setText("RECHAZADO");
	lblProgramStatus.setBounds(new Rectangle(430, 193, 80, 25));
	lblProgramStatus.setFont(new Font("Dialog", 1, 11));
	lblProgramStatus.setForeground(new Color(148, 107, 0));
	lblProgramStatus.setSize(new Dimension(80, 25));
	lblTerrainStatus.setText("RECHAZADO");
	lblTerrainStatus.setBounds(new Rectangle(430, 158, 80, 25));
	lblTerrainStatus.setFont(new Font("Dialog", 1, 11));
	lblTerrainStatus.setForeground(new Color(148, 107, 0));
	lblTerrainStatus.setSize(new Dimension(80, 25));
	lblCoordStatus.setText("INCOMPLETO");
	lblCoordStatus.setBounds(new Rectangle(430, 123, 80, 25));
	lblCoordStatus.setFont(new Font("Dialog", 1, 11));
	lblCoordStatus.setForeground(new Color(148, 107, 0));
	lblCoordStatus.setSize(new Dimension(80, 25));
	//lblFileStatus.setText("REALIZADO");
	lblFileStatus.setBounds(new Rectangle(430, 263, 80, 25));
	lblFileStatus.setFont(new Font("Dialog", 1, 11));
	lblFileStatus.setForeground(new Color(148, 107, 0));
	lblFileStatus.setSize(new Dimension(80, 25));
	lblDeclarationOK.setBounds(new Rectangle(390, 228, 25, 25));
	lblDeclarationOK.setIconTextGap(0);
	lblDeclarationOK.setSize(new Dimension(25, 25));
	lblSolicitorOK.setBounds(new Rectangle(390, 18, 25, 25));
	lblSolicitorOK.setIconTextGap(0);
	lblSolicitorOK.setSize(new Dimension(25, 25));
	lblLegalRepOK.setBounds(new Rectangle(390, 53, 25, 25));
	lblLegalRepOK.setIconTextGap(0);
	lblLegalRepOK.setSize(new Dimension(25, 25));
	lblUbicatonOK.setBounds(new Rectangle(390, 88, 25, 25));
	lblUbicatonOK.setIconTextGap(0);
	lblUbicatonOK.setSize(new Dimension(25, 25));
	lblCoordOK.setBounds(new Rectangle(390, 123, 25, 25));
	lblCoordOK.setIconTextGap(0);
	lblCoordOK.setSize(new Dimension(25, 25));
	lblTerrainOK.setBounds(new Rectangle(390, 158, 25, 25));
	lblTerrainOK.setIconTextGap(0);
	lblTerrainOK.setSize(new Dimension(25, 25));
	lblProgramOK.setBounds(new Rectangle(390, 193, 25, 25));
	lblProgramOK.setIconTextGap(0);
	lblProgramOK.setSize(new Dimension(25, 25));
	lblFileOK.setBounds(new Rectangle(390, 263, 25, 25));
	lblFileOK.setIconTextGap(0);
	lblFileOK.setSize(new Dimension(25, 25));
	btnSolicitor.setBounds(new Rectangle(20, 15, 350, 30));
	btnSolicitor.setSize(new Dimension(200, 30));
	btnSolicitor.setFont(new Font("Default", 0, 11));
	btnSolicitor.setHorizontalAlignment(SwingConstants.LEFT);
	btnSolicitor.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnSolicitor.setBorderPainted(false);
	btnSolicitor.setContentAreaFilled(false);
	btnSolicitor.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnSolicitor_actionPerformed(e);
				    }

				}
	);
	btnLegalRep.setBounds(new Rectangle(20, 50, 350, 30));
	btnLegalRep.setFont(new Font("Default", 0, 11));
	btnLegalRep.setHorizontalAlignment(SwingConstants.LEFT);
	btnLegalRep.setContentAreaFilled(false);
	btnLegalRep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnLegalRep.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       btnLegalRep_actionPerformed(e);
				   }

			       }
	);
	btnUbication.setBounds(new Rectangle(20, 85, 350, 30));
	btnUbication.setFont(new Font("Default", 0, 11));
	btnUbication.setHorizontalAlignment(SwingConstants.LEFT);
	btnUbication.setContentAreaFilled(false);
	btnUbication.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnUbication.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnUbication_actionPerformed(e);
				    }

				}
	);
	btnCoord.setBounds(new Rectangle(20, 120, 350, 30));
	btnCoord.setFont(new Font("Default", 0, 11));
	btnCoord.setHorizontalAlignment(SwingConstants.LEFT);
	btnCoord.setContentAreaFilled(false);
	btnCoord.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnCoord.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnCoord_actionPerformed(e);
				}

			    }
	);
	btnTerrain.setBounds(new Rectangle(20, 155, 350, 30));
	btnTerrain.setFont(new Font("Default", 0, 11));
	btnTerrain.setHorizontalAlignment(SwingConstants.LEFT);
	btnTerrain.setContentAreaFilled(false);
	btnTerrain.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	/*btnTerrain.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnTerrain_actionPerformed(e);
		    }

		});*/
	btnTerrain.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      btnTerrain_actionPerformed(e);
				  }

			      }
	);
	btnProgram.setBounds(new Rectangle(20, 190, 350, 30));
	btnProgram.setFont(new Font("Default", 0, 11));
	btnProgram.setHorizontalAlignment(SwingConstants.LEFT);
	btnProgram.setContentAreaFilled(false);
	btnProgram.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnProgram.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      btnProgram_actionPerformed(e);
				  }

			      }
	);
	btnDeclaration.setBounds(new Rectangle(20, 225, 350, 30));
	btnDeclaration.setFont(new Font("Default", 0, 11));
	btnDeclaration.setSize(new Dimension(350, 30));
	btnDeclaration.setHorizontalAlignment(SwingConstants.LEFT);
	btnDeclaration.setContentAreaFilled(false);
	btnDeclaration.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnDeclaration.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  btnDeclaration_actionPerformed(e);
				      }

				  }
	);
	btnFile.setBounds(new Rectangle(20, 260, 350, 30));
	btnFile.setFont(new Font("Default", 0, 11));
	btnFile.setHorizontalAlignment(SwingConstants.LEFT);
	btnFile.setContentAreaFilled(false);
	btnFile.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnFile.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnFile_actionPerformed(e);
			       }

			   }
	);
	panelExplorationForm.setLayout(null);
	panelExplorationForm.add(lblFileStatus, null);
	panelExplorationForm.add(lblCoordStatus, null);
	panelExplorationForm.add(lblTerrainStatus, null);
	panelExplorationForm.add(lblProgramStatus, null);
	panelExplorationForm.add(lblUbicationStatus, null);
	panelExplorationForm.add(lblDeclarationStatus, null);
	panelExplorationForm.add(lblLegalRepStatus, null);
	panelExplorationForm.add(btnSolicitor, null);
	panelExplorationForm.add(btnDeclaration, null);
	panelExplorationForm.add(btnProgram, null);
	panelExplorationForm.add(btnTerrain, null);
	panelExplorationForm.add(btnCoord, null);
	panelExplorationForm.add(btnUbication, null);
	panelExplorationForm.add(btnLegalRep, null);
	panelExplorationForm.add(lblSolicitorStatus, null);
	panelExplorationForm.add(lblSolicitorOK, null);
	panelExplorationForm.add(lblFileOK, null);
	panelExplorationForm.add(lblProgramOK, null);
	panelExplorationForm.add(lblTerrainOK, null);
	panelExplorationForm.add(lblCoordOK, null);
	panelExplorationForm.add(lblUbicatonOK, null);
	panelExplorationForm.add(lblLegalRepOK, null);
	panelExplorationForm.add(lblDeclarationOK, null);
	panelExplorationForm.add(btnFile, null);
	this.getContentPane().add(lblInfo, null);
	this.getContentPane().add(btnAccept, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(panelExplorationForm, null);
	if (!info) {
	    btnFile.setVisible(false);
	    lblFileOK.setVisible(false);
	    lblFileStatus.setVisible(true);
	}
	setIcons();
	lblInfo.setVisible(info);
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.setVisible(false);
    }

    private void btnSolicitor_actionPerformed(ActionEvent e) {
	PanelSolicitor panelSolicitor = new PanelSolicitor(parentDesktop, btnSolicitor.getText(), mgmt, explorationRequestClass.getSolicitorClass());
	panelSolicitor.setModal(true);
	panelSolicitor.setVisible(true);
	statusControl(lblSolicitorOK, lblSolicitorStatus, explorationRequestClass.getSolicitorStatusAux());
    }

    private void btnLegalRep_actionPerformed(ActionEvent e) {
	PanelLegalRep panelLegalRep = new PanelLegalRep(btnLegalRep.getText(), mgmt, explorationRequestClass.getLegalRepClass());
	panelLegalRep.setModal(true);
	panelLegalRep.setVisible(true);
	statusControl(lblLegalRepOK, lblLegalRepStatus, explorationRequestClass.getLegalRepStatusAux());
    }

    private void btnUbication_actionPerformed(ActionEvent e) {
	panelUbication = new PanelUbication(btnUbication.getText(), explorationRequestClass.getUbicationClass());
	panelUbication.setModal(true);
	panelUbication.setVisible(true);
	statusControl(lblUbicatonOK, lblUbicationStatus, explorationRequestClass.getUbicationStatusAux());
    }

    private void btnCoord_actionPerformed(ActionEvent e) {
	PanelCoord panelCoord = new PanelCoord(btnUbication.getText(), explorationRequestClass.getERPolygonsClass());
	panelCoord.setModal(true);
	panelCoord.setVisible(true);
	statusControl(lblCoordOK, lblCoordStatus, explorationRequestClass.getErPolygonStatus());
    }

    private void btnTerrain_actionPerformed(ActionEvent e) {
	//PanelTerrainData panelTerrainData = new PanelTerrainData(btnTerrain.getText());
	panelTerrainData = new PanelTerrainData(btnTerrain.getText(), explorationRequestClass.getTerrainDataClass());
	panelTerrainData.setModal(true);
	panelTerrainData.setVisible(true);
	statusControl(lblTerrainOK, lblTerrainStatus, explorationRequestClass.getTerrainDataStatusAux());
    }

    private void btnProgram_actionPerformed(ActionEvent e) {
	//PanelProgram panelProgram = new PanelProgram(btnProgram.getText(), mgmt);//este funciona
	PanelProgram panelProgram = new PanelProgram(btnProgram.getText(), explorationRequestClass);
	panelProgram.setModal(true);
	panelProgram.setVisible(true);
	statusControl(lblProgramOK, lblProgramStatus, explorationRequestClass.getExplorationSchemeStatus());
    }

    private void btnDeclaration_actionPerformed(ActionEvent e) {
	//PanelSwornStatement panelSwornStatement = new PanelSwornStatement(mgmt);
	PanelSwornStatement panelSwornStatement = new PanelSwornStatement(explorationRequestClass);
	panelSwornStatement.setModal(true);
	panelSwornStatement.setVisible(true);
	statusControl(lblDeclarationOK, lblDeclarationStatus, explorationRequestClass.getERDeclarationStatusAux());
    }

    private void btnFile_actionPerformed(ActionEvent e) {
	PanelFile panelFile = new PanelFile(btnFile.getText(), mgmt);
	panelFile.setModal(true);
	panelFile.setVisible(true);
    }

    public void setParentDesktop(JDesktopPane _parentDesktop) {
	parentDesktop = _parentDesktop;
    }

    private void statusControl(BasicLabel label1, BasicLabel label2, int _status) {
	switch (_status) {
	    case NO_MADE :
		label1.setIcon(IconTypes.stepNoMadeIcon_16x16);
		label2.setText("NO INICIADO");
		break;
	    case INCOMPLETE :
		label1.setIcon(IconTypes.stepIncompleteIcon_16x16);
		label2.setText("INCOMPLETO");
		break;
	    case COMPLETE :
		label1.setIcon(IconTypes.stepApplyIcon_16x16);
		label2.setText("REALIZADO");
		break;
	    case REJECTED :
		label1.setIcon(IconTypes.stepRejectedIcon_16x16);
		label2.setText("RECHAZADO");
		break;
	}
    }

    public int getEstado() {
	return estado;
    }

    private void setIcons() {
	if (explorationRequestClass.getIdFile() == -1) {
	    setNoMadeIcon();
	} else {
	    setExplorationRequestIcons(lblSolicitorOK, lblSolicitorStatus, SOLICITOR);
	    setExplorationRequestIcons(lblLegalRepOK, lblLegalRepStatus, LEGALREP);
	    setExplorationRequestIcons(lblUbicatonOK, lblUbicationStatus, UBICATION);
	    setExplorationRequestIcons(lblCoordOK, lblCoordStatus, COORDINATES);
	    setExplorationRequestIcons(lblTerrainOK, lblTerrainStatus, TERRAIN);
	    setExplorationRequestIcons(lblProgramOK, lblProgramStatus, PROGRAM);
	    setExplorationRequestIcons(lblDeclarationOK, lblDeclarationStatus, DECLARATION);
	}
    }

    private void setNoMadeIcon() {
	lblSolicitorOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblLegalRepOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblUbicatonOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblCoordOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblTerrainOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblProgramOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblDeclarationOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void setExplorationRequestIcons(BasicLabel label, BasicLabel label2, int opcion) {
	int status;
	if (opcion == SOLICITOR) {
	    status = explorationRequestClass.getSolicitorStatus();
	    solicitorStatus = status;
	} else if (opcion == LEGALREP) {
	    status = explorationRequestClass.getLegalRepStatus();
	    legalRepStatus = status;
	} else if (opcion == UBICATION) {
	    status = explorationRequestClass.getUbicationStatus();
	    ubicationStatus = status;
	} else if (opcion == COORDINATES) {
	    status = explorationRequestClass.getErPolygonStatus();
	    coordinatesStatus = status;
	} else if (opcion == TERRAIN) {
	    status = explorationRequestClass.getTerrainDataStatus();
	    terrainDataStatus = status;
	} else if (opcion == PROGRAM) {
	    status = explorationRequestClass.getExplorationSchemeStatus();
	    explorationSchemeStatus = status;
	} else if (opcion == DECLARATION) {
	    status = explorationRequestClass.getERDeclarationStatus();
	    swornStatementStatus = status;
	} else {
	    status = 1;
	}
	switch (status) {
	    case NO_MADE :
		label.setIcon(IconTypes.stepNoMadeIcon_16x16);
		label2.setText("NO INICIADO");
		break;
	    case INCOMPLETE :
		label.setIcon(IconTypes.stepIncompleteIcon_16x16);
		label2.setText("INCOMPLETO");
		break;
	    case COMPLETE :
		label.setIcon(IconTypes.stepApplyIcon_16x16);
		label2.setText("COMPLETO");
		break;
	    case REJECTED :
		label.setIcon(IconTypes.stepRejectedIcon_16x16);
		label2.setText("RECHAZADO");
		break;
	}
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	if (explorationRequestClass.recordData()) {
	    //cambiarle el estado a explorationrequest
	    //setear los datos que estan en las variables auxiliares a las variables reales
	} else {
	    //mensaje: Ocurrio un error, los cambios no se registraron!
	}
	explorationRequestClass.updateStatus();
	this.dispose();
    }

}

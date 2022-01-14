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

import org.digitall.apps.legalprocs.manager.classes.MineRequestClass;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.icons.IconTypes;

public class ManifestForm extends BasicDialog {

    private String title;
    private BasicButton btnSolicitor = new BasicButton();
    private BasicButton btnRep = new BasicButton();
    private BasicButton btnDiscovery = new BasicButton();
    private BasicButton btnUbication = new BasicButton();
    private BasicButton btnCoord = new BasicButton();
    private BasicButton btnMine = new BasicButton();
    private BasicButton btnMineral = new BasicButton();
    private BasicButton btnTerrain = new BasicButton();
    private BasicButton btnAdjacent = new BasicButton();
    private BasicButton btnPartner = new BasicButton();
    private BasicButton btnFile = new BasicButton();
    private BasicLabel lblSolicitorOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblAdjacentMinesOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblTerrainOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    //private BasicLabel lblSolicitorOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblMineralFoundOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblMineOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblCoordOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblUbicationOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblDiscoveryOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblFileOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblLegalRepOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicLabel lblPartnersDataOK = new BasicLabel(IconTypes.stepApplyIcon_16x16);
    private BasicPanel jPanel1 = new BasicPanel();
    private BasicLabel lblSolicitorStatus = new BasicLabel();
    private BasicLabel lblLegalRepStatus = new BasicLabel();
    private BasicLabel lblDiscoveryStatus = new BasicLabel();
    private BasicLabel lblUbicationStatus = new BasicLabel();
    private BasicLabel lblCoordStatus = new BasicLabel();
    private BasicLabel lblMineStatus = new BasicLabel();
    private BasicLabel lblMineralFoundStatus = new BasicLabel();
    private BasicLabel lblTerrainStatus = new BasicLabel();
    private BasicLabel lblAdjacentMineStatus = new BasicLabel();
    private BasicLabel lblPartnersDataStatus = new BasicLabel();
    private BasicLabel lblFileStatus = new BasicLabel();
    private CloseButton btnClose = new CloseButton();
    private AcceptButton btnAccept = new AcceptButton();
    private JDesktopPane parentDesktop;
    private MineRequestClass mineRequestClass;
    private PanelSolicitor panelSolicitor;
    private PanelLegalRep panelLegalRep;
    private PanelUbication panelUbication;
    private PanelCoord panelCoord;
    private PanelTerrainData panelTerrainData;
    private PanelMineName panelMineName;
    private int solicitorStatus = 1;
    private int legalRepStatus = 1;
    private int ubicationStatus = 1;
    private int coordinatesStatus = 1;
    private int terrainDataStatus = 1;
    private int mineNameStatus = 1;
    /*private int explorationSchemeStatus = 1;
    private int swornStatementStatus = 1;*/
    //***** estos valores no se pueden cambiar porque estan relacionados con la BD **********
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    //*************************************
    private static final int SOLICITOR = 1;
    private static final int LEGALREP = 2;
    private static final int UBICATION = 3;
    private static final int COORDINATES = 4;
    private static final int TERRAIN = 5;
    private static final int PROGRAM = 6;
    private static final int DECLARATION = 7;
    private static final int DISCOVERY = 8;
    private static final int MINE_NAME = 9;
    private static final int MINES_FOUND = 10;
    private static final int ADJACENT_MINES = 11;
    private static final int PARTNER_DATA = 12;
    private static final int FILES = 13;

    public ManifestForm(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ManifestForm(String _title, JDesktopPane _parentDesktop, MineRequestClass _mineRequestClass) {
	try {
	    title = _title;
	    mineRequestClass = _mineRequestClass;
	    parentDesktop = _parentDesktop;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(590, 543));
	this.setTitle(title);
	this.getContentPane().setLayout(null);
	btnSolicitor.setText("Nombre y Domicilio del Solicitante");
	btnRep.setText("Nombre y Domicilio del Representante Legal");
	btnDiscovery.setText("Antecedentes del Descubrimiento");
	btnUbication.setText("Ubicacion de la Manifestacion");
	btnCoord.setText("Coordenadas Gaus Krüger de la Zona de Reconocimiento Exclusiva");
	btnMine.setText("Nombre de la Mina");
	btnMineral.setText("Minerales Descubiertos");
	btnTerrain.setText("Datos del Terreno");
	btnAdjacent.setText("Minas Colindantes");
	btnPartner.setText("Datos de los Compañeros (Socios)");
	btnFile.setText("Nº de Expediente y Matricula Catastral");
	btnSolicitor.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnRep.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnDiscovery.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnUbication.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnCoord.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnMine.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnMineral.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnTerrain.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnAdjacent.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnPartner.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnFile.setCursor(new Cursor(Cursor.HAND_CURSOR));
	btnSolicitor.setBounds(new Rectangle(20, 15, 370, 25));
	btnSolicitor.setSize(new Dimension(370, 25));
	btnSolicitor.setHorizontalAlignment(SwingConstants.LEFT);
	btnSolicitor.setContentAreaFilled(false);
	btnSolicitor.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnSolicitor.setFont(new Font("Default", 0, 11));
	btnSolicitor.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnSolicitor_actionPerformed(e);
				    }

				}
	);
	btnRep.setBounds(new Rectangle(20, 50, 370, 25));
	btnRep.setHorizontalAlignment(SwingConstants.LEFT);
	btnRep.setContentAreaFilled(false);
	btnRep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnRep.setFont(new Font("Default", 0, 11));
	btnRep.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  btnRep_actionPerformed(e);
			      }

			  }
	);
	btnDiscovery.setBounds(new Rectangle(20, 85, 370, 25));
	btnDiscovery.setHorizontalAlignment(SwingConstants.LEFT);
	btnDiscovery.setContentAreaFilled(false);
	btnDiscovery.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnDiscovery.setFont(new Font("Default", 0, 11));
	btnDiscovery.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnDiscovery_actionPerformed(e);
				    }

				}
	);
	btnUbication.setBounds(new Rectangle(20, 120, 370, 25));
	btnUbication.setHorizontalAlignment(SwingConstants.LEFT);
	btnUbication.setContentAreaFilled(false);
	btnUbication.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnUbication.setFont(new Font("Default", 0, 11));
	btnUbication.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnUbication_actionPerformed(e);
				    }

				}
	);
	btnCoord.setBounds(new Rectangle(20, 155, 370, 25));
	btnCoord.setSize(new Dimension(370, 25));
	btnCoord.setHorizontalAlignment(SwingConstants.LEFT);
	btnCoord.setContentAreaFilled(false);
	btnCoord.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnCoord.setFont(new Font("Default", 0, 11));
	btnCoord.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnCoord_actionPerformed(e);
				}

			    }
	);
	btnMine.setBounds(new Rectangle(20, 190, 370, 25));
	btnMine.setHorizontalAlignment(SwingConstants.LEFT);
	btnMine.setContentAreaFilled(false);
	btnMine.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnMine.setFont(new Font("Default", 0, 11));
	btnMine.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnMine_actionPerformed(e);
			       }

			   }
	);
	btnMineral.setBounds(new Rectangle(20, 225, 370, 25));
	btnMineral.setHorizontalAlignment(SwingConstants.LEFT);
	btnMineral.setContentAreaFilled(false);
	btnMineral.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnMineral.setFont(new Font("Default", 0, 11));
	btnMineral.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      btnMineral_actionPerformed(e);
				  }

			      }
	);
	btnTerrain.setBounds(new Rectangle(20, 260, 370, 25));
	btnTerrain.setHorizontalAlignment(SwingConstants.LEFT);
	btnTerrain.setContentAreaFilled(false);
	btnTerrain.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnTerrain.setFont(new Font("Default", 0, 11));
	btnTerrain.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      btnTerrain_actionPerformed(e);
				  }

			      }
	);
	btnAdjacent.setBounds(new Rectangle(20, 295, 370, 25));
	btnAdjacent.setHorizontalAlignment(SwingConstants.LEFT);
	btnAdjacent.setContentAreaFilled(false);
	btnAdjacent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnAdjacent.setFont(new Font("Default", 0, 11));
	btnAdjacent.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       btnAdjacent_actionPerformed(e);
				   }

			       }
	);
	btnPartner.setBounds(new Rectangle(20, 330, 370, 25));
	btnPartner.setHorizontalAlignment(SwingConstants.LEFT);
	btnPartner.setContentAreaFilled(false);
	btnPartner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnPartner.setFont(new Font("Default", 0, 11));
	btnPartner.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      btnPartner_actionPerformed(e);
				  }

			      }
	);
	btnFile.setBounds(new Rectangle(20, 365, 370, 25));
	btnFile.setHorizontalAlignment(SwingConstants.LEFT);
	btnFile.setContentAreaFilled(false);
	btnFile.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnFile.setFont(new Font("Default", 0, 11));
	btnFile.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnFile_actionPerformed(e);
			       }

			   }
	);
	lblSolicitorOK.setBounds(new Rectangle(410, 330, 25, 25));
	lblSolicitorOK.setIconTextGap(0);
	lblSolicitorOK.setSize(new Dimension(25, 25));
	lblAdjacentMinesOK.setBounds(new Rectangle(410, 295, 25, 25));
	lblAdjacentMinesOK.setIconTextGap(0);
	lblAdjacentMinesOK.setSize(new Dimension(25, 25));
	lblTerrainOK.setBounds(new Rectangle(410, 260, 25, 25));
	lblTerrainOK.setIconTextGap(0);
	lblTerrainOK.setSize(new Dimension(25, 25));
	lblSolicitorOK.setBounds(new Rectangle(410, 15, 25, 25));
	lblSolicitorOK.setIconTextGap(0);
	lblSolicitorOK.setSize(new Dimension(25, 25));
	lblMineralFoundOK.setBounds(new Rectangle(410, 225, 25, 25));
	lblMineralFoundOK.setIconTextGap(0);
	lblMineralFoundOK.setSize(new Dimension(25, 25));
	lblMineOK.setBounds(new Rectangle(410, 190, 25, 25));
	lblMineOK.setIconTextGap(0);
	lblMineOK.setSize(new Dimension(25, 25));
	lblCoordOK.setBounds(new Rectangle(410, 155, 25, 25));
	lblCoordOK.setIconTextGap(0);
	lblCoordOK.setSize(new Dimension(25, 25));
	lblUbicationOK.setBounds(new Rectangle(410, 120, 25, 25));
	lblUbicationOK.setIconTextGap(0);
	lblUbicationOK.setSize(new Dimension(25, 25));
	lblDiscoveryOK.setBounds(new Rectangle(410, 85, 25, 25));
	lblDiscoveryOK.setIconTextGap(0);
	lblDiscoveryOK.setSize(new Dimension(25, 25));
	lblFileOK.setBounds(new Rectangle(410, 365, 25, 25));
	lblFileOK.setIconTextGap(0);
	lblFileOK.setSize(new Dimension(25, 25));
	lblLegalRepOK.setBounds(new Rectangle(410, 50, 25, 25));
	lblLegalRepOK.setIconTextGap(0);
	lblLegalRepOK.setSize(new Dimension(25, 25));
	jPanel1.setBounds(new Rectangle(12, 45, 560, 405));
	jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jPanel1.setLayout(null);
	lblSolicitorStatus.setText("INCOMPLETO");
	lblSolicitorStatus.setFont(new Font("Dialog", 1, 11));
	lblSolicitorStatus.setForeground(new Color(148, 107, 0));
	lblSolicitorStatus.setBounds(new Rectangle(460, 20, 80, 14));
	lblSolicitorStatus.setSize(new Dimension(80, 14));
	btnClose.setBounds(new Rectangle(535, 480, 40, 25));
	btnClose.setSize(new Dimension(40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	btnAccept.setBounds(new Rectangle(10, 480, 40, 25));
	btnAccept.setSize(new Dimension(40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	lblLegalRepStatus.setText("INCOMPLETO");
	lblLegalRepStatus.setFont(new Font("Dialog", 1, 11));
	lblLegalRepStatus.setForeground(new Color(148, 107, 0));
	lblLegalRepStatus.setBounds(new Rectangle(460, 55, 80, 15));
	lblLegalRepStatus.setSize(new Dimension(80, 14));
	lblDiscoveryStatus.setText("INCOMPLETO");
	lblDiscoveryStatus.setFont(new Font("Dialog", 1, 11));
	lblDiscoveryStatus.setForeground(new Color(148, 107, 0));
	lblDiscoveryStatus.setBounds(new Rectangle(460, 90, 80, 15));
	lblDiscoveryStatus.setSize(new Dimension(80, 14));
	lblUbicationStatus.setText("INCOMPLETO");
	lblUbicationStatus.setFont(new Font("Dialog", 1, 11));
	lblUbicationStatus.setForeground(new Color(148, 107, 0));
	lblUbicationStatus.setBounds(new Rectangle(460, 125, 80, 15));
	lblUbicationStatus.setSize(new Dimension(80, 14));
	lblCoordStatus.setText("INCOMPLETO");
	lblCoordStatus.setFont(new Font("Dialog", 1, 11));
	lblCoordStatus.setForeground(new Color(148, 107, 0));
	lblCoordStatus.setBounds(new Rectangle(460, 160, 80, 15));
	lblCoordStatus.setSize(new Dimension(80, 14));
	lblMineStatus.setText("INCOMPLETO");
	lblMineStatus.setFont(new Font("Dialog", 1, 11));
	lblMineStatus.setForeground(new Color(148, 107, 0));
	lblMineStatus.setBounds(new Rectangle(460, 195, 80, 15));
	lblMineStatus.setSize(new Dimension(80, 14));
	lblMineralFoundStatus.setText("INCOMPLETO");
	lblMineralFoundStatus.setFont(new Font("Dialog", 1, 11));
	lblMineralFoundStatus.setForeground(new Color(148, 107, 0));
	lblMineralFoundStatus.setBounds(new Rectangle(460, 230, 80, 15));
	lblMineralFoundStatus.setSize(new Dimension(80, 14));
	lblTerrainStatus.setText("INCOMPLETO");
	lblTerrainStatus.setFont(new Font("Dialog", 1, 11));
	lblTerrainStatus.setForeground(new Color(148, 107, 0));
	lblTerrainStatus.setBounds(new Rectangle(460, 265, 80, 15));
	lblTerrainStatus.setSize(new Dimension(80, 14));
	lblAdjacentMineStatus.setText("INCOMPLETO");
	lblAdjacentMineStatus.setFont(new Font("Dialog", 1, 11));
	lblAdjacentMineStatus.setForeground(new Color(148, 107, 0));
	lblAdjacentMineStatus.setBounds(new Rectangle(460, 300, 80, 15));
	lblAdjacentMineStatus.setSize(new Dimension(80, 14));
	lblPartnersDataStatus.setText("INCOMPLETO");
	lblPartnersDataStatus.setFont(new Font("Dialog", 1, 11));
	lblPartnersDataStatus.setForeground(new Color(148, 107, 0));
	lblPartnersDataStatus.setBounds(new Rectangle(460, 335, 80, 15));
	lblPartnersDataStatus.setSize(new Dimension(80, 14));
	lblFileStatus.setText("INCOMPLETO");
	lblFileStatus.setFont(new Font("Dialog", 1, 11));
	lblFileStatus.setForeground(new Color(148, 107, 0));
	lblFileStatus.setBounds(new Rectangle(460, 370, 80, 15));
	lblFileStatus.setSize(new Dimension(80, 14));
	lblPartnersDataOK.setBounds(new Rectangle(410, 330, 25, 25));
	lblPartnersDataOK.setIconTextGap(0);
	lblPartnersDataOK.setSize(new Dimension(25, 25));
	jPanel1.add(lblFileStatus, null);
	jPanel1.add(lblPartnersDataStatus, null);
	jPanel1.add(lblAdjacentMineStatus, null);
	jPanel1.add(lblTerrainStatus, null);
	jPanel1.add(lblMineralFoundStatus, null);
	jPanel1.add(lblMineStatus, null);
	jPanel1.add(lblCoordStatus, null);
	jPanel1.add(lblUbicationStatus, null);
	jPanel1.add(lblDiscoveryStatus, null);
	jPanel1.add(lblLegalRepStatus, null);
	jPanel1.add(lblPartnersDataOK, null);
	jPanel1.add(lblSolicitorStatus, null);
	jPanel1.add(btnSolicitor, null);
	jPanel1.add(lblLegalRepOK, null);
	jPanel1.add(lblFileOK, null);
	jPanel1.add(lblDiscoveryOK, null);
	jPanel1.add(lblUbicationOK, null);
	jPanel1.add(lblCoordOK, null);
	jPanel1.add(lblMineOK, null);
	jPanel1.add(lblMineralFoundOK, null);
	jPanel1.add(lblSolicitorOK, null);
	jPanel1.add(lblTerrainOK, null);
	jPanel1.add(lblAdjacentMinesOK, null);
	jPanel1.add(lblSolicitorOK, null);
	jPanel1.add(btnFile, null);
	jPanel1.add(btnPartner, null);
	jPanel1.add(btnAdjacent, null);
	jPanel1.add(btnTerrain, null);
	jPanel1.add(btnMineral, null);
	jPanel1.add(btnMine, null);
	jPanel1.add(btnCoord, null);
	jPanel1.add(btnUbication, null);
	jPanel1.add(btnDiscovery, null);
	jPanel1.add(btnRep, null);
	this.getContentPane().add(btnAccept, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(jPanel1, null);
	setIcons();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	retrieveOriginalData();
	this.setVisible(false);
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	if (mineRequestClass.recordData()) {
	    //cambiarle el estado a explorationrequest
	    // MENSAJE: LOS DATOS FUERON ACTUIALIZADOS EXITOSAMENTE
	} else {
	    //MENSAJE: OCURRIO UN ERROR, LOS CAMBIOS NO SE REGISTRARON!
	}
	mineRequestClass.updateStatus();
	this.dispose();
    }

    private void btnSolicitor_actionPerformed(ActionEvent e) {
	panelSolicitor = new PanelSolicitor(parentDesktop, btnSolicitor.getText(), mineRequestClass.getSolicitorClass());
	panelSolicitor.setModal(true);
	panelSolicitor.setVisible(true);
	statusControl(lblSolicitorOK, lblSolicitorStatus, mineRequestClass.getSolicitorClass().getEntityStatusAux());
    }

    private void btnRep_actionPerformed(ActionEvent e) {
	panelLegalRep = new PanelLegalRep(btnRep.getText(), mineRequestClass.getLegalRepClass());
	panelLegalRep.setModal(true);
	panelLegalRep.setVisible(true);
	statusControl(lblLegalRepOK, lblLegalRepStatus, mineRequestClass.getLegalRepClass().getEntityStatusAux());
    }

    private void btnDiscovery_actionPerformed(ActionEvent e) {
	Advisor.messageBox("Modulo en Construcción", "Mensaje");
    }

    private void btnUbication_actionPerformed(ActionEvent e) {
	panelUbication = new PanelUbication(btnUbication.getText(), mineRequestClass.getUbicationClass());
	panelUbication.setModal(true);
	panelUbication.setVisible(true);
	statusControl(lblUbicationOK, lblUbicationStatus, mineRequestClass.getUbicationClass().getUbicationStatusAux());
    }

    private void btnCoord_actionPerformed(ActionEvent e) {
	panelCoord = new PanelCoord(btnUbication.getText(), mineRequestClass.getErPolygonsClass());
	panelCoord.setModal(true);
	panelCoord.setVisible(true);
	statusControl(lblCoordOK, lblCoordStatus, mineRequestClass.getErPolygonsClass().getErPolygonStatusAux());
    }

    private void btnMine_actionPerformed(ActionEvent e) {
	//advisor.messageBox("Modulo en Construcción","Mensaje");
	panelMineName = new PanelMineName(btnMine.getText(), mineRequestClass.getMineNameClass());
	panelMineName.setModal(true);
	panelMineName.setVisible(true);
	statusControl(lblMineOK, lblMineStatus, mineRequestClass.getMineNameClass().getMineNameStatusAux());
    }

    private void btnMineral_actionPerformed(ActionEvent e) {
	Advisor.messageBox("Modulo en Construcción", "Mensaje");
    }

    private void btnTerrain_actionPerformed(ActionEvent e) {
	//advisor.messageBox("Modulo en Construcción","Mensaje");
	panelTerrainData = new PanelTerrainData(btnTerrain.getText(), mineRequestClass.getTerrainDataClass());
	panelTerrainData.setModal(true);
	panelTerrainData.setVisible(true);
	statusControl(lblTerrainOK, lblTerrainStatus, mineRequestClass.getTerrainDataClass().getTerrainDataStatusAux());
    }

    private void btnAdjacent_actionPerformed(ActionEvent e) {
	Advisor.messageBox("Modulo en Construcción", "Mensaje");
    }

    private void btnPartner_actionPerformed(ActionEvent e) {
	Advisor.messageBox("Modulo en Construcción", "Mensaje");
    }

    private void btnFile_actionPerformed(ActionEvent e) {
	Advisor.messageBox("Modulo en Construcción", "Mensaje");
    }

    private void setIcons() {
	if (mineRequestClass.getIdFile() == -1) {
	    setNoMadeIcon();
	} else {
	    setMineRequestIcons(lblSolicitorOK, lblSolicitorStatus, SOLICITOR);
	    setMineRequestIcons(lblLegalRepOK, lblLegalRepStatus, LEGALREP);
	    setMineRequestIcons(lblDiscoveryOK, lblDiscoveryStatus, DISCOVERY);
	    setMineRequestIcons(lblUbicationOK, lblUbicationStatus, UBICATION);
	    setMineRequestIcons(lblCoordOK, lblCoordStatus, COORDINATES);
	    setMineRequestIcons(lblMineOK, lblMineStatus, MINE_NAME);
	    setMineRequestIcons(lblMineralFoundOK, lblMineralFoundStatus, MINES_FOUND);
	    setMineRequestIcons(lblTerrainOK, lblTerrainStatus, TERRAIN);
	    setMineRequestIcons(lblAdjacentMinesOK, lblAdjacentMineStatus, ADJACENT_MINES);
	    setMineRequestIcons(lblPartnersDataOK, lblPartnersDataStatus, PARTNER_DATA);
	    setMineRequestIcons(lblFileOK, lblFileStatus, FILES);
	}
    }

    private void setNoMadeIcon() {
	lblSolicitorOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblLegalRepOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblDiscoveryOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblUbicationOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblCoordOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblMineOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblMineralFoundOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblTerrainOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblAdjacentMinesOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblPartnersDataOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
	lblFileOK.setIcon(IconTypes.stepNoMadeIcon_16x16);
    }

    private void setMineRequestIcons(BasicLabel label, BasicLabel label2, int opcion) {
	int status;
	if (opcion == SOLICITOR) {
	    status = mineRequestClass.getSolicitorStatus();
	    solicitorStatus = status;
	} else if (opcion == LEGALREP) {
	    status = mineRequestClass.getLegalRepStatus();
	    legalRepStatus = status;
	} else if (opcion == UBICATION) {
	    status = mineRequestClass.getUbicationStatus();
	    ubicationStatus = status;
	} else if (opcion == COORDINATES) {
	    status = mineRequestClass.getErPolygonStatus();
	    coordinatesStatus = status;
	} else if (opcion == MINE_NAME) {
	    status = mineRequestClass.getMineNameStatus();
	    mineNameStatus = status;
	} else if (opcion == TERRAIN) {
	    status = mineRequestClass.getTerrainDataStatus();
	    terrainDataStatus = status;
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

    private void retrieveOriginalData() {
	retrieveOriginalSolicitorData();
	retrieveOriginalLegalRepData();
	retrieveOriginalUbicationData();
	retrieveOriginalCoordinatesData();
	mineRequestClass.getTerrainDataClass().copyData();
	mineRequestClass.getMineNameClass().copyData();
    }

    private void retrieveOriginalSolicitorData() {
	mineRequestClass.getSolicitorClass().setIdReferencedAux(mineRequestClass.getSolicitorClass().getIdReferenced());
	mineRequestClass.getSolicitorClass().setIsPersonAux(mineRequestClass.getSolicitorClass().isPerson());
	mineRequestClass.getSolicitorClass().setIsSolicitorAux(mineRequestClass.getSolicitorClass().IsSolicitor());
	mineRequestClass.getSolicitorClass().setEntityStatusAux(mineRequestClass.getSolicitorClass().getEntityStatus());
    }

    private void retrieveOriginalLegalRepData() {
	mineRequestClass.getLegalRepClass().setIdReferencedAux(mineRequestClass.getLegalRepClass().getIdReferenced());
	mineRequestClass.getLegalRepClass().setIsPersonAux(mineRequestClass.getLegalRepClass().isPerson());
	mineRequestClass.getLegalRepClass().setIsSolicitorAux(mineRequestClass.getLegalRepClass().IsSolicitor());
	mineRequestClass.getLegalRepClass().setEntityStatusAux(mineRequestClass.getLegalRepClass().getEntityStatus());
    }

    private void retrieveOriginalUbicationData() {
	mineRequestClass.getUbicationClass().setIdLocationAddressAux(mineRequestClass.getUbicationClass().getIdLocationAddress());
	mineRequestClass.getUbicationClass().setIdCountryAux(mineRequestClass.getUbicationClass().getIdCountry());
	mineRequestClass.getUbicationClass().setIdprovinceAux(mineRequestClass.getUbicationClass().getIdprovince());
	mineRequestClass.getUbicationClass().setIdlocationAux(mineRequestClass.getUbicationClass().getIdlocation());
	mineRequestClass.getUbicationClass().setUbicationStatusAux(mineRequestClass.getUbicationClass().getUbicationStatus());
    }

    private void retrieveOriginalCoordinatesData() {
	mineRequestClass.getErPolygonsClass().setMineralcategoryAux(mineRequestClass.getErPolygonsClass().getMineralcategory());
	mineRequestClass.getErPolygonsClass().setAreaAux(mineRequestClass.getErPolygonsClass().getArea());
    }

}

package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.digitall.apps.legalprocs.calendar.wizard.ExplorationRequestClass_Old;
import org.digitall.apps.legalprocs.calendar.wizard.Manager;
import org.digitall.apps.legalprocs.manager.classes.ExplorationSchemeClass;
import org.digitall.lib.components.JDecEntry;
import org.digitall.lib.components.JEntry;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

public class ExplorationSchemePanel extends BasicContainerPanel {

    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private BasicPanel jpExpte = new BasicPanel();
    private BasicPanel jpAntecedentes = new BasicPanel();
    private BasicPanel jpTrabajos = new BasicPanel();
    private FilePanel filePanel = new FilePanel();
    private RegistralRegistlyPanel registralRegistlyPanel = new RegistralRegistlyPanel();
    private FilesCadastralRegisterPanel filesCadastralRegisterPanel = new FilesCadastralRegisterPanel();
    private UbicationPanel ubicationPanel = new UbicationPanel();
    private SolicitorAlonePanel jpSolicitor = new SolicitorAlonePanel();
    private BasicPanel jpMinerales = new BasicPanel();
    private BasicPanel jpTrabajosProspeccion = new BasicPanel();
    private BasicPanel jptrabajosExploracion = new BasicPanel();
    private BasicPanel jpequiposConstrucciones = new BasicPanel();
    private BasicPanel jpInformacion = new BasicPanel();
    private BasicPanel jPanel1 = new BasicPanel();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private BasicCheckBox chkMineralTypes1ra = new BasicCheckBox();
    private BasicCheckBox chkMineralTypes2da = new BasicCheckBox();
    //private ButtonGroup grupoChecks = new ButtonGroup();
    private TFInput tfTermDays = new TFInput(DataTypes.INTEGER, "Term", false);
    private TFInput tfWorkDays = new TFInput(DataTypes.INTEGER, "ZoneSeason", false);
    private TFInput tfProspectionTime = new TFInput(DataTypes.INTEGER, "ProspectionTime", false);
    private TFInput tfExplorationDays = new TFInput(DataTypes.INTEGER, "ExplorationTime", false, true);
    private TFInput tfCompresorCapacity = new TFInput(DataTypes.DOUBLE, "Capacity", false, true);
    private TFInput tfDrillCapacity = new TFInput(DataTypes.DOUBLE, "Capacity", false, true);
    private TFInput tfVehicleType = new TFInput(DataTypes.STRING, "Kind", false, true);
    private TFInput tfCompresorQuantity = new TFInput(DataTypes.INTEGER, "Quantity", false, true);
    private TFInput tfDrillQuantity = new TFInput(DataTypes.INTEGER, "Quantity", false, true);
    private TFInput tfVehicleQuantity = new TFInput(DataTypes.INTEGER, "Quantity", false, true);
    private TFInput tfCoveredArea = new TFInput(DataTypes.DOUBLE, "CoveredArea", false, true);
    private TFInput tfRoadKm = new TFInput(DataTypes.DOUBLE, "Km", false, true);
    private TFInput tfPathKm = new TFInput(DataTypes.DOUBLE, "Km", false, true);
    private TFInput tfMapGeologicScale = new TFInput(DataTypes.DOUBLE, "Scale", false, true);
    private TFInput tfMapTopographicScale = new TFInput(DataTypes.DOUBLE, "Scale", false, true);
    private TFInput tfMapMiningScale = new TFInput(DataTypes.DOUBLE, "Scale", false, true);
    private TFInput tfOtherMapsSacale = new TFInput(DataTypes.DOUBLE, "Scale", false, true);
    private BasicPanel jpPersonalOcupado = new BasicPanel();
    private TFInput tfOperators = new TFInput(DataTypes.DOUBLE, "Quantity", false, true);
    private TFInput tfAdministratives = new TFInput(DataTypes.DOUBLE, "Quantity", false, true);
    private TFInput tfTechnicians = new TFInput(DataTypes.DOUBLE, "Quantity", false, true);
    private TFInput tfProfessionals = new TFInput(DataTypes.DOUBLE, "Quantity", false, true);
    private JEntry tfOtherWay = new JEntry();
    private JEntry tfOtherInvestigations = new JEntry();
    private JEntry tfOtherEquipment = new JEntry();
    private BasicLabel jLabel1 = new BasicLabel();
    private final int FRAME = 1;
    private final int INTERNALFRAME = 2;
    private final int DIALOG = 3;
    private int parentType = -1;
    private Component parent;
    private BasicLabel jLabel3 = new BasicLabel();
    private BasicLabel jLabel4 = new BasicLabel();
    private BasicCheckBox chkSatelitalPicture = new BasicCheckBox();
    private BasicCheckBox chkAerialPhoto = new BasicCheckBox();
    private BasicCheckBox chkTopographic = new BasicCheckBox();
    private BasicCheckBox chkGeochemical = new BasicCheckBox();
    private BasicCheckBox chkGeophisic = new BasicCheckBox();
    private BasicCheckBox chkGeological = new BasicCheckBox();
    private BasicCheckBox chkOtherInvest = new BasicCheckBox();
    private BasicCheckBox chkSampling = new BasicCheckBox();
    private BasicCheckBox chkProspectionDrilling = new BasicCheckBox();
    private BasicCheckBox chkExplorationDrilling = new BasicCheckBox();
    private BasicCheckBox chkMining = new BasicCheckBox();
    private BasicCheckBox chkOtrosMetodos = new BasicCheckBox();
    private BasicLabel jLabel5 = new BasicLabel();
    private BasicLabel jLabel6 = new BasicLabel();
    private BasicLabel jLabel7 = new BasicLabel();
    private BasicLabel jLabel8 = new BasicLabel();
    private BasicLabel jLabel10 = new BasicLabel();
    private BasicLabel jLabel9 = new BasicLabel();
    private BasicLabel jLabel11 = new BasicLabel();
    private BasicLabel jLabel12 = new BasicLabel();
    private BasicCheckBox chkCompressorOwnYes = new BasicCheckBox();
    private BasicCheckBox chkCompressorOwnNo = new BasicCheckBox();
    private ButtonGroup compresorGroup = new ButtonGroup();
    private BasicCheckBox chkDrillOwnNo = new BasicCheckBox();
    private BasicCheckBox chkDrillOwnYes = new BasicCheckBox();
    private ButtonGroup drillGroup = new ButtonGroup();
    private BasicCheckBox chkVehicleOwnNo = new BasicCheckBox();
    private BasicCheckBox chkVehicleOwnYes = new BasicCheckBox();
    private ButtonGroup vehicleGroup = new ButtonGroup();
    private BasicCheckBox chkOtherEquipment = new BasicCheckBox();
    private BasicPanel jPanel2 = new BasicPanel();
    private BasicPanel jpInformacionTecnica = new BasicPanel();
    private BasicPanel jPanel5 = new BasicPanel();
    private BasicPanel jpMonto = new BasicPanel();
    private BasicPanel jpProtecAmbiental = new BasicPanel();
    private BasicPanel jpMontoEstimado = new BasicPanel();
    private BasicLabel lblCampamentos = new BasicLabel();
    private BasicLabel lblCaminos = new BasicLabel();
    private BasicLabel lblProvisionAgua = new BasicLabel();
    private BasicLabel lblCaminos1 = new BasicLabel();
    private BasicLabel lblMaqEquipos = new BasicLabel();
    private BasicLabel lblConstrucciones = new BasicLabel();
    private BasicLabel jLabel13 = new BasicLabel();
    private BasicLabel lblTecnicos = new BasicLabel();
    private BasicLabel lblProfesionales = new BasicLabel();
    private BasicLabel lblAdministrativos = new BasicLabel();
    private BasicLabel lblMaqEquipos1 = new BasicLabel();
    private BasicLabel lblMaqEquipos2 = new BasicLabel();
    private BasicLabel lblMaqEquipos3 = new BasicLabel();
    private BasicLabel jLabel14 = new BasicLabel();
    private BasicLabel jLabel15 = new BasicLabel();
    private BasicLabel jLabel16 = new BasicLabel();
    private BasicLabel jLabel17 = new BasicLabel();
    private BasicLabel jLabel18 = new BasicLabel();
    private BasicLabel jLabel19 = new BasicLabel();
    private BasicLabel jLabel110 = new BasicLabel();
    private BasicLabel jLabel111 = new BasicLabel();
    private BasicLabel jLabel112 = new BasicLabel();
    private BasicLabel lblMaqEquipos4 = new BasicLabel();
    private BasicLabel lblMaqEquipos5 = new BasicLabel();
    private JEntry tfOtherProvision = new JEntry();
    private BasicCheckBox chkCampMoveable = new BasicCheckBox();
    private BasicCheckBox chkCampFixed = new BasicCheckBox();
    private BasicCheckBox chkWaterProvision = new BasicCheckBox();
    private BasicCheckBox chkOtherProvision = new BasicCheckBox();
    private BasicCheckBox chkMapMining = new BasicCheckBox();
    private BasicCheckBox chkMapTopographic = new BasicCheckBox();
    private BasicCheckBox chkOtherMaps = new BasicCheckBox();
    private BasicCheckBox chkMapGeologic = new BasicCheckBox();
    private BasicCheckBox chkChemicalAnalisys = new BasicCheckBox();
    private BasicCheckBox chkMapDrilling = new BasicCheckBox();
    private BasicCheckBox chkMineralTest = new BasicCheckBox();
    private BasicCheckBox chkEasementRoads = new BasicCheckBox();
    private BasicCheckBox chkEasementWaters = new BasicCheckBox();
    private BasicCheckBox chkEasementOthers = new BasicCheckBox();
    private BasicCheckBox chkEasementLand = new BasicCheckBox();
    private BasicCheckBox chkEasementMines = new BasicCheckBox();
    private BasicCheckBox chkAmbientalReportExploration = new BasicCheckBox();
    private BasicCheckBox chkAmbientalReportProspection = new BasicCheckBox();
    private BasicCheckBox chkAmbientalStatementExploration = new BasicCheckBox();
    private BasicCheckBox chkAmbientalStatementProspection = new BasicCheckBox();
    private JEntry tfEasementOthers = new JEntry();
    private JDecEntry tfBuildingCost = new JDecEntry();
    private JDecEntry tfRoadCost = new JDecEntry();
    private JDecEntry tfOtherCost = new JDecEntry();
    private JDecEntry tfDrillingCost = new JDecEntry();
    private JDecEntry tfTotalCost = new JDecEntry();
    private JDecEntry tfGenericCost = new JDecEntry();
    private JDecEntry tfEquipmentCost = new JDecEntry();
    private String idexplorationScheme = "";
    private int opcion = 0;
    private Manager mgmt;
    private BasicLabel jLabel2 = new BasicLabel();
    private ExplorationRequestClass_Old prospection;
    private ExplorationSchemeClass explorationSchemeClass;
    //CONSTANTES
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public ExplorationSchemePanel(BasicInternalFrame _parent) {
	try {
	    parent = _parent;
	    parentType = INTERNALFRAME;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ExplorationSchemePanel(BasicDialog _parent) {
	try {
	    parent = _parent;
	    parentType = DIALOG;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /*    public ExplorationSchemePanel(JFrame _parent) {
	try {
	    parent = _parent;
	    parentType = FRAME;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ExplorationSchemePanel(BasicDialog _parent, Manager _mgmt, int _opcion) {
	try {
	    opcion = _opcion;
	    mgmt = _mgmt;
	    parent = _parent;
	    parentType = DIALOG;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ExplorationSchemePanel(BasicDialog _parent, ExplorationRequestClass_Old _prospection, int _opcion) {
	try {
	    prospection = _prospection;
	    opcion = _opcion;
	    parent = _parent;
	    parentType = DIALOG;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
*/

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(588, 437));
	jTabbedPane1.setBounds(new Rectangle(7, 5, 575, 380));
	jpExpte.setLayout(null);
	jpAntecedentes.setLayout(null);
	jpTrabajos.setLayout(null);
	btnAccept.setBounds(new Rectangle(15, 405, 40, 25));
	btnAccept.setSize(new Dimension(40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(540, 405, 40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	jLabel1.setText(" Marticula Registral");
	jLabel1.setBounds(new Rectangle(15, 80, 125, 15));
	jLabel1.setFont(new Font("Default", 1, 11));
	jLabel1.setOpaque(false);
	jLabel3.setText(" Ubicación de la Solicitud");
	jLabel3.setBounds(new Rectangle(15, 95, 160, 15));
	jLabel3.setFont(new Font("Default", 1, 11));
	jLabel3.setOpaque(false);
	jpMinerales.setBounds(new Rectangle(5, 20, 555, 55));
	jpMinerales.setLayout(null);
	jpMinerales.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfTermDays.setBounds(new Rectangle(255, 10, 135, 40));
	tfTermDays.autoSize();
	tfTermDays.setEditable(true);
	tfWorkDays.setBounds(new Rectangle(405, 10, 130, 40));
	tfWorkDays.autoSize();
	tfWorkDays.setEditable(true);
	chkMineralTypes1ra.setText("1ra");
	chkMineralTypes1ra.setBounds(new Rectangle(20, 25, 50, 20));
	chkMineralTypes2da.setText("2da");
	chkMineralTypes2da.setBounds(new Rectangle(85, 25, 50, 20));
	jLabel4.setText("Sustancias minerales a explorar");
	jLabel4.setBounds(new Rectangle(20, 10, 202, 14));
	jLabel4.setSize(new Dimension(202, 14));
	jLabel4.setFont(new Font("Default", 1, 11));
	jpTrabajosProspeccion.setBounds(new Rectangle(5, 100, 555, 135));
	jpTrabajosProspeccion.setLayout(null);
	jpTrabajosProspeccion.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	chkSatelitalPicture.setText("Imágenes satelitales");
	chkSatelitalPicture.setBounds(new Rectangle(10, 10, 155, 20));
	chkSatelitalPicture.setFont(new Font("Default", 1, 11));
	chkSatelitalPicture.setSize(new Dimension(155, 18));
	chkAerialPhoto.setText("Fotografía aérea");
	chkAerialPhoto.setBounds(new Rectangle(10, 30, 155, 18));
	chkAerialPhoto.setFont(new Font("Default", 1, 11));
	chkTopographic.setText("Relevamiento topogr.");
	chkTopographic.setBounds(new Rectangle(10, 50, 155, 20));
	chkTopographic.setFont(new Font("Default", 1, 11));
	chkTopographic.setSize(new Dimension(178, 20));
	chkGeochemical.setText("Geoquímica");
	chkGeochemical.setBounds(new Rectangle(190, 50, 160, 18));
	chkGeochemical.setFont(new Font("Default", 1, 11));
	chkGeophisic.setText("Geofísica");
	chkGeophisic.setBounds(new Rectangle(190, 30, 160, 18));
	chkGeophisic.setFont(new Font("Default", 1, 11));
	chkGeological.setText("Relevamiento geológ.");
	chkGeological.setBounds(new Rectangle(190, 10, 160, 18));
	chkGeological.setFont(new Font("Default", 1, 11));
	chkOtherInvest.setText("Otras investigaciones, especificar");
	chkOtherInvest.setBounds(new Rectangle(10, 85, 260, 20));
	chkOtherInvest.setFont(new Font("Default", 1, 11));
	chkSampling.setText("Calicatas y extr.  mtras.");
	chkSampling.setBounds(new Rectangle(370, 30, 175, 18));
	chkSampling.setFont(new Font("Default", 1, 11));
	chkProspectionDrilling.setText("Sondajes");
	chkProspectionDrilling.setBounds(new Rectangle(370, 10, 175, 18));
	chkProspectionDrilling.setFont(new Font("Default", 1, 11));
	tfOtherInvestigations.setBounds(new Rectangle(10, 105, 335, 20));
	tfOtherInvestigations.setSize(new Dimension(260, 20));
	jptrabajosExploracion.setBounds(new Rectangle(5, 260, 555, 75));
	jptrabajosExploracion.setLayout(null);
	jptrabajosExploracion.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	chkExplorationDrilling.setText("Laboreos mineros");
	chkExplorationDrilling.setBounds(new Rectangle(10, 20, 140, 20));
	chkExplorationDrilling.setFont(new Font("Default", 1, 11));
	chkMining.setText("Sondajes");
	chkMining.setBounds(new Rectangle(10, 40, 140, 20));
	chkMining.setFont(new Font("Default", 1, 11));
	chkOtrosMetodos.setText("Otros métodos, especificar");
	chkOtrosMetodos.setBounds(new Rectangle(150, 20, 245, 20));
	chkOtrosMetodos.setFont(new Font("Default", 1, 11));
	//tfOtrasEspecif1.setBounds(new Rectangle(10, 105, 260, 20));
	//tfOtrasEspecif1.setSize(new Dimension(260, 20));
	tfOtherWay.setBounds(new Rectangle(155, 40, 245, 20));
	//tfOtrosMetodos.setSize(new Dimension(260, 20));
	jLabel5.setText(" Trabajos de exploración");
	jLabel5.setBounds(new Rectangle(15, 250, 154, 14));
	jLabel5.setFont(new Font("Default", 1, 11));
	jLabel5.setOpaque(false);
	jLabel5.setSize(new Dimension(154, 14));
	jLabel6.setText(" Trabajos de prospección");
	jLabel6.setBounds(new Rectangle(15, 90, 157, 14));
	jLabel6.setFont(new Font("Default", 1, 11));
	jLabel6.setOpaque(false);
	jLabel6.setSize(new Dimension(157, 14));
	jpequiposConstrucciones.setLayout(null);
	jpInformacion.setLayout(null);
	jPanel1.setBounds(new Rectangle(5, 15, 560, 180));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfCompresorCapacity.setBounds(new Rectangle(120, 5, 110, 40));
	tfCompresorCapacity.autoSize();
	tfDrillCapacity.setBounds(new Rectangle(120, 45, 110, 40));
	tfDrillCapacity.autoSize();
	tfVehicleType.setBounds(new Rectangle(120, 85, 110, 35));
	tfVehicleType.autoSize();
	tfCompresorQuantity.setBounds(new Rectangle(270, 5, 110, 40));
	tfCompresorCapacity.autoSize();
	tfDrillQuantity.setBounds(new Rectangle(270, 45, 110, 40));
	tfDrillQuantity.autoSize();
	tfVehicleQuantity.setBounds(new Rectangle(270, 85, 110, 35));
	tfVehicleQuantity.autoSize();
	jLabel7.setText("Compresores:");
	jLabel7.setBounds(new Rectangle(10, 25, 88, 14));
	jLabel7.setFont(new Font("Default", 1, 11));
	jLabel7.setSize(new Dimension(88, 14));
	jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel8.setText("Perforadoras:");
	jLabel8.setBounds(new Rectangle(10, 65, 88, 14));
	jLabel8.setFont(new Font("Default", 1, 11));
	jLabel8.setSize(new Dimension(88, 14));
	jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel10.setText("Vehículos:");
	jLabel10.setBounds(new Rectangle(10, 105, 88, 14));
	jLabel10.setFont(new Font("Default", 1, 11));
	jLabel10.setSize(new Dimension(88, 14));
	jLabel10.setHorizontalAlignment(SwingConstants.RIGHT);
	chkCompressorOwnYes.setText("Sí");
	chkCompressorOwnYes.setBounds(new Rectangle(470, 20, 40, 20));
	chkCompressorOwnYes.setFont(new Font("Default", 1, 11));
	chkCompressorOwnNo.setText("No");
	chkCompressorOwnNo.setBounds(new Rectangle(510, 20, 45, 20));
	chkCompressorOwnNo.setFont(new Font("Default", 1, 11));
	chkCompressorOwnNo.setToolTipText("null");
	chkDrillOwnNo.setText("No");
	chkDrillOwnNo.setBounds(new Rectangle(510, 62, 45, 20));
	chkDrillOwnNo.setFont(new Font("Default", 1, 11));
	chkDrillOwnYes.setText("Sí");
	chkDrillOwnYes.setBounds(new Rectangle(470, 62, 40, 20));
	chkDrillOwnYes.setFont(new Font("Default", 1, 11));
	chkVehicleOwnNo.setText("No");
	chkVehicleOwnNo.setBounds(new Rectangle(510, 102, 45, 20));
	chkVehicleOwnNo.setFont(new Font("Default", 1, 11));
	chkVehicleOwnYes.setText("Sí");
	chkVehicleOwnYes.setBounds(new Rectangle(470, 102, 40, 20));
	chkVehicleOwnYes.setFont(new Font("Default", 1, 11));
	jLabel9.setText("Propios:");
	jLabel9.setBounds(new Rectangle(405, 25, 55, 15));
	jLabel9.setFont(new Font("Default", 1, 11));
	jLabel9.setSize(new Dimension(52, 14));
	jLabel11.setText("Propios:");
	jLabel11.setBounds(new Rectangle(405, 65, 55, 15));
	jLabel11.setFont(new Font("Default", 1, 11));
	jLabel11.setSize(new Dimension(52, 14));
	jLabel12.setText("Propios:");
	jLabel12.setBounds(new Rectangle(405, 105, 55, 15));
	jLabel12.setFont(new Font("Default", 1, 11));
	jLabel12.setSize(new Dimension(52, 14));
	chkOtherEquipment.setText("Otros equipos, especificar");
	chkOtherEquipment.setBounds(new Rectangle(10, 135, 215, 15));
	chkOtherEquipment.setFont(new Font("Default", 1, 11));
	jPanel2.setBounds(new Rectangle(5, 210, 560, 135));
	jPanel2.setLayout(null);
	jPanel2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	lblCampamentos.setText("Campamentos:");
	lblCampamentos.setBounds(new Rectangle(10, 30, 114, 14));
	lblCampamentos.setFont(new Font("Default", 1, 11));
	lblCampamentos.setSize(new Dimension(92, 14));
	lblCampamentos.setHorizontalAlignment(SwingConstants.RIGHT);
	lblCaminos.setText("Caminos:");
	lblCaminos.setBounds(new Rectangle(10, 70, 114, 15));
	lblCaminos.setFont(new Font("Default", 1, 11));
	//lblCaminos.setSize(new Dimension(54, 14));
	lblCaminos.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProvisionAgua.setText("Provisión de agua:");
	lblProvisionAgua.setBounds(new Rectangle(10, 108, 114, 14));
	lblProvisionAgua.setFont(new Font("Default", 1, 11));
	lblProvisionAgua.setSize(new Dimension(114, 14));
	lblProvisionAgua.setHorizontalAlignment(SwingConstants.RIGHT);
	chkCampMoveable.setText("Desmontable");
	chkCampMoveable.setBounds(new Rectangle(235, 25, 110, 20));
	chkCampMoveable.setFont(new Font("Default", 1, 11));
	chkCampMoveable.setToolTipText("null");
	chkCampFixed.setText("Fijo");
	chkCampFixed.setBounds(new Rectangle(140, 25, 60, 18));
	chkCampFixed.setFont(new Font("Default", 1, 11));
	chkCampFixed.setSize(new Dimension(60, 18));
	tfCoveredArea.setBounds(new Rectangle(360, 10, 130, 40));
	tfCoveredArea.autoSize();
	tfRoadKm.setBounds(new Rectangle(135, 50, 60, 40));
	tfRoadKm.autoSize();
	tfPathKm.setBounds(new Rectangle(365, 50, 60, 40));
	tfPathKm.autoSize();
	chkWaterProvision.setText("De pozo");
	chkWaterProvision.setBounds(new Rectangle(140, 105, 85, 20));
	chkWaterProvision.setFont(new Font("Default", 1, 11));
	lblCaminos1.setText("Huellas:");
	lblCaminos1.setBounds(new Rectangle(290, 70, 54, 14));
	lblCaminos1.setFont(new Font("Default", 1, 11));
	lblCaminos1.setSize(new Dimension(54, 14));
	chkOtherProvision.setText("Otras fuentes:");
	chkOtherProvision.setBounds(new Rectangle(235, 105, 115, 20));
	chkOtherProvision.setFont(new Font("Default", 1, 11));
	tfOtherProvision.setBounds(new Rectangle(350, 105, 205, 20));
	lblMaqEquipos.setText(" Maquinaria y equipo a utilizar");
	lblMaqEquipos.setBounds(new Rectangle(15, 5, 195, 15));
	lblMaqEquipos.setFont(new Font("Default", 1, 11));
	lblMaqEquipos.setOpaque(false);
	lblConstrucciones.setText(" Construcciones");
	lblConstrucciones.setBounds(new Rectangle(15, 200, 110, 15));
	lblConstrucciones.setFont(new Font("Default", 1, 11));
	lblConstrucciones.setOpaque(false);
	jpInformacionTecnica.setBounds(new Rectangle(5, 10, 560, 135));
	jpInformacionTecnica.setLayout(null);
	jpInformacionTecnica.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	chkMapMining.setText("De laboreo");
	chkMapMining.setBounds(new Rectangle(10, 60, 95, 18));
	chkMapMining.setFont(new Font("Default", 1, 11));
	chkMapMining.setToolTipText("null");
	chkMapTopographic.setText("Topográficos");
	chkMapTopographic.setBounds(new Rectangle(10, 20, 110, 18));
	chkMapTopographic.setFont(new Font("Default", 1, 11));
	chkMapTopographic.setSize(new Dimension(110, 18));
	chkOtherMaps.setText("Otros");
	chkOtherMaps.setBounds(new Rectangle(220, 60, 85, 18));
	chkOtherMaps.setFont(new Font("Default", 1, 11));
	chkOtherMaps.setToolTipText("null");
	chkMapGeologic.setText("Geológicos");
	chkMapGeologic.setBounds(new Rectangle(220, 20, 95, 18));
	chkMapGeologic.setFont(new Font("Default", 1, 11));
	chkChemicalAnalisys.setText("Muestreo y análisis quimico");
	chkChemicalAnalisys.setBounds(new Rectangle(10, 90, 285, 18));
	chkChemicalAnalisys.setFont(new Font("Default", 1, 11));
	chkChemicalAnalisys.setToolTipText("null");
	chkChemicalAnalisys.setSize(new Dimension(275, 18));
	chkMapDrilling.setText("De perforaciones");
	chkMapDrilling.setBounds(new Rectangle(420, 20, 135, 18));
	chkMapDrilling.setFont(new Font("Default", 1, 11));
	chkMineralTest.setText("Estudios y ensayos minero-metalúrgicos");
	chkMineralTest.setBounds(new Rectangle(10, 110, 285, 20));
	chkMineralTest.setFont(new Font("Default", 1, 11));
	chkMineralTest.setToolTipText("null");
	tfMapGeologicScale.setBounds(new Rectangle(315, 5, 75, 40));
	tfMapGeologicScale.autoSize();
	tfMapTopographicScale.setBounds(new Rectangle(120, 5, 75, 40));
	tfMapTopographicScale.autoSize();
	tfMapMiningScale.setBounds(new Rectangle(120, 45, 75, 40));
	tfMapMiningScale.autoSize();
	tfOtherMapsSacale.setBounds(new Rectangle(315, 45, 230, 40));
	tfOtherMapsSacale.autoSize();
	jpPersonalOcupado.setBounds(new Rectangle(5, 165, 560, 95));
	jpPersonalOcupado.setLayout(null);
	jpPersonalOcupado.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfOperators.setBounds(new Rectangle(80, 5, 135, 40));
	tfOperators.autoSize();
	tfAdministratives.setBounds(new Rectangle(370, 5, 150, 40));
	tfAdministratives.autoSize();
	tfTechnicians.setBounds(new Rectangle(80, 45, 135, 45));
	tfTechnicians.autoSize();
	tfProfessionals.setBounds(new Rectangle(370, 45, 150, 40));
	tfProfessionals.autoSize();
	jLabel13.setText("Operarios:");
	jLabel13.setBounds(new Rectangle(5, 20, 70, 20));
	jLabel13.setFont(new Font("Default", 1, 11));
	jLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
	lblTecnicos.setText("Técnicos:");
	lblTecnicos.setBounds(new Rectangle(5, 57, 70, 20));
	lblTecnicos.setFont(new Font("Default", 1, 11));
	lblTecnicos.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProfesionales.setText("Profesionales:");
	lblProfesionales.setBounds(new Rectangle(245, 55, 115, 20));
	lblProfesionales.setFont(new Font("Default", 1, 11));
	lblProfesionales.setHorizontalAlignment(SwingConstants.RIGHT);
	lblAdministrativos.setText("Administrativos:");
	lblAdministrativos.setBounds(new Rectangle(245, 20, 115, 20));
	lblAdministrativos.setFont(new Font("Default", 1, 11));
	lblAdministrativos.setHorizontalAlignment(SwingConstants.RIGHT);
	jPanel5.setBounds(new Rectangle(5, 280, 560, 65));
	jPanel5.setLayout(null);
	jPanel5.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	chkEasementRoads.setText("Caminos");
	chkEasementRoads.setBounds(new Rectangle(355, 10, 135, 18));
	chkEasementRoads.setFont(new Font("Default", 1, 11));
	chkEasementWaters.setText("Aguas");
	chkEasementWaters.setBounds(new Rectangle(205, 10, 95, 18));
	chkEasementWaters.setFont(new Font("Default", 1, 11));
	chkEasementWaters.setSize(new Dimension(95, 18));
	chkEasementOthers.setText("Otras, especificar");
	chkEasementOthers.setBounds(new Rectangle(205, 35, 145, 20));
	chkEasementOthers.setFont(new Font("Default", 1, 11));
	chkEasementOthers.setToolTipText("null");
	chkEasementLand.setText("Ocupación del suelo");
	chkEasementLand.setBounds(new Rectangle(5, 10, 195, 20));
	chkEasementLand.setFont(new Font("Default", 1, 11));
	chkEasementMines.setText("Sobre otras minas o cateos");
	chkEasementMines.setBounds(new Rectangle(5, 35, 195, 18));
	chkEasementMines.setFont(new Font("Default", 1, 11));
	chkEasementMines.setToolTipText("null");
	tfEasementOthers.setBounds(new Rectangle(355, 33, 200, 20));
	lblMaqEquipos1.setText(" Información técnica a obtener");
	lblMaqEquipos1.setBounds(new Rectangle(15, 0, 195, 15));
	lblMaqEquipos1.setFont(new Font("Default", 1, 11));
	lblMaqEquipos1.setOpaque(false);
	lblMaqEquipos2.setText(" Personal ocupado");
	lblMaqEquipos2.setBounds(new Rectangle(15, 155, 120, 15));
	lblMaqEquipos2.setFont(new Font("Default", 1, 11));
	lblMaqEquipos2.setOpaque(false);
	lblMaqEquipos3.setText(" Servidumbres a utilizar");
	lblMaqEquipos3.setBounds(new Rectangle(15, 270, 155, 15));
	lblMaqEquipos3.setFont(new Font("Default", 1, 11));
	lblMaqEquipos3.setOpaque(false);
	jpMonto.setLayout(null);
	jpProtecAmbiental.setBounds(new Rectangle(5, 20, 560, 110));
	jpProtecAmbiental.setLayout(null);
	jpProtecAmbiental.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jpMontoEstimado.setBounds(new Rectangle(5, 155, 555, 190));
	jpMontoEstimado.setLayout(null);
	jpMontoEstimado.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	chkAmbientalReportExploration.setText("Exploración");
	chkAmbientalReportExploration.setBounds(new Rectangle(410, 25, 115, 20));
	chkAmbientalReportExploration.setFont(new Font("Default", 1, 11));
	chkAmbientalReportProspection.setText("Prospección");
	chkAmbientalReportProspection.setBounds(new Rectangle(280, 25, 110, 18));
	chkAmbientalReportProspection.setFont(new Font("Default", 1, 11));
	chkAmbientalReportProspection.setSize(new Dimension(110, 18));
	jLabel14.setText("Presentó informe de impacto ambiental");
	jLabel14.setBounds(new Rectangle(5, 20, 255, 20));
	jLabel14.setFont(new Font("Default", 1, 11));
	jLabel15.setText("Posse informe de impacto ambiental");
	jLabel15.setBounds(new Rectangle(5, 65, 255, 20));
	jLabel15.setFont(new Font("Default", 1, 11));
	chkAmbientalStatementExploration.setText("Exploración");
	chkAmbientalStatementExploration.setBounds(new Rectangle(410, 65, 115, 20));
	chkAmbientalStatementExploration.setFont(new Font("Default", 1, 11));
	chkAmbientalStatementProspection.setText("Prospección");
	chkAmbientalStatementProspection.setBounds(new Rectangle(280, 66, 110, 18));
	chkAmbientalStatementProspection.setFont(new Font("Default", 1, 11));
	chkAmbientalStatementProspection.setSize(new Dimension(110, 18));
	tfBuildingCost.setBounds(new Rectangle(260, 10, 155, 20));
	tfBuildingCost.addKeyListener(new KeyAdapter() {

				   public void keyReleased(KeyEvent e) {
				       suma();
				   }

			       }
	);
	tfRoadCost.setBounds(new Rectangle(260, 35, 155, 20));
	tfRoadCost.addKeyListener(new KeyAdapter() {

			       public void keyReleased(KeyEvent e) {
				   suma();
			       }

			   }
	);
	tfOtherCost.setBounds(new Rectangle(260, 85, 155, 20));
	tfOtherCost.addKeyListener(new KeyAdapter() {

				public void keyReleased(KeyEvent e) {
				    suma();
				}

			    }
	);
	tfDrillingCost.setBounds(new Rectangle(260, 60, 155, 20));
	tfDrillingCost.addKeyListener(new KeyAdapter() {

				   public void keyReleased(KeyEvent e) {
				       suma();
				   }

			       }
	);
	tfTotalCost.setBounds(new Rectangle(260, 160, 155, 20));
	tfGenericCost.setBounds(new Rectangle(260, 135, 155, 20));
	tfGenericCost.addKeyListener(new KeyAdapter() {

				  public void keyReleased(KeyEvent e) {
				      suma();
				  }

			      }
	);
	tfEquipmentCost.setBounds(new Rectangle(260, 110, 155, 20));
	tfEquipmentCost.addKeyListener(new KeyAdapter() {

				    public void keyReleased(KeyEvent e) {
					suma();
				    }

				}
	);
	jLabel2.setText("$");
	jLabel2.setBounds(new Rectangle(435, 75, 15, 25));
	jLabel2.setFont(new Font("Dialog", 1, 16));
	jLabel16.setText("Construcciones");
	jLabel16.setBounds(new Rectangle(70, 13, 170, 14));
	jLabel16.setFont(new Font("Default", 1, 11));
	jLabel16.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel16.setSize(new Dimension(170, 14));
	jLabel17.setText("gastos generales");
	jLabel17.setBounds(new Rectangle(70, 138, 170, 14));
	jLabel17.setFont(new Font("Default", 1, 11));
	jLabel17.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel18.setText("Laboreos y perforaciones");
	jLabel18.setBounds(new Rectangle(70, 63, 170, 14));
	jLabel18.setFont(new Font("Default", 1, 11));
	jLabel18.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel19.setText("Caminos y huellas");
	jLabel19.setBounds(new Rectangle(70, 38, 170, 14));
	jLabel19.setFont(new Font("Default", 1, 11));
	jLabel19.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel110.setText("Otras");
	jLabel110.setBounds(new Rectangle(70, 88, 170, 14));
	jLabel110.setFont(new Font("Default", 1, 11));
	jLabel110.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel111.setText("Monto total");
	jLabel111.setBounds(new Rectangle(430, 163, 80, 14));
	jLabel111.setFont(new Font("Default", 1, 11));
	jLabel112.setText("Maquinaria y equipos");
	jLabel112.setBounds(new Rectangle(70, 113, 170, 14));
	jLabel112.setFont(new Font("Default", 1, 11));
	jLabel112.setHorizontalAlignment(SwingConstants.RIGHT);
	lblMaqEquipos4.setText(" Protección ambiental");
	lblMaqEquipos4.setBounds(new Rectangle(15, 10, 145, 15));
	lblMaqEquipos4.setFont(new Font("Default", 1, 11));
	lblMaqEquipos4.setOpaque(false);
	lblMaqEquipos5.setText(" Monto estimado de las inversiones");
	lblMaqEquipos5.setBounds(new Rectangle(15, 145, 225, 15));
	lblMaqEquipos5.setFont(new Font("Default", 1, 11));
	lblMaqEquipos5.setOpaque(false);
	tfOtherEquipment.setBounds(new Rectangle(10, 150, 465, 20));
	tfExplorationDays.setBounds(new Rectangle(410, 25, 135, 35));
	tfExplorationDays.autoSize();
	tfProspectionTime.setBounds(new Rectangle(365, 85, 150, 40));
	tfProspectionTime.autoSize();
	tfProspectionTime.setEditable(true);
	jpSolicitor.setBounds(new Rectangle(5, 20, 560, 52));
	jpSolicitor.setSize(new Dimension(560, 52));
	ubicationPanel.setBounds(new Rectangle(5, 105, 560, 110));
	filesCadastralRegisterPanel.setBounds(new Rectangle(5, 170, 560, 175));
	registralRegistlyPanel.setBounds(new Rectangle(5, 90, 560, 52));
	jpExpte.add(jLabel1, null);
	jpExpte.add(filesCadastralRegisterPanel, null);
	jpExpte.add(registralRegistlyPanel, null);
	filePanel.setBounds(new Rectangle(5, 10, 560, 52));
	jpExpte.add(filePanel, null);
	jpAntecedentes.add(jpSolicitor, null);
	jpAntecedentes.add(jLabel3, null);
	jpAntecedentes.add(ubicationPanel, null);
	jTabbedPane1.addTab("Expediente", jpExpte);
	jTabbedPane1.addTab("Antecedentes", jpAntecedentes);
	jpMinerales.add(jLabel4, null);
	jpMinerales.add(chkMineralTypes2da, null);
	jpMinerales.add(chkMineralTypes1ra, null);
	jpMinerales.add(tfWorkDays, null);
	jpMinerales.add(tfTermDays, null);
	jpTrabajosProspeccion.add(tfProspectionTime, null);
	jpTrabajosProspeccion.add(tfOtherInvestigations, null);
	jpTrabajosProspeccion.add(chkProspectionDrilling, null);
	jpTrabajosProspeccion.add(chkSampling, null);
	jpTrabajosProspeccion.add(chkOtherInvest, null);
	jpTrabajosProspeccion.add(chkGeological, null);
	jpTrabajosProspeccion.add(chkGeophisic, null);
	jpTrabajosProspeccion.add(chkGeochemical, null);
	jpTrabajosProspeccion.add(chkTopographic, null);
	jpTrabajosProspeccion.add(chkAerialPhoto, null);
	jpTrabajosProspeccion.add(chkSatelitalPicture, null);
	jptrabajosExploracion.add(tfExplorationDays, null);
	jptrabajosExploracion.add(tfOtherWay, null);
	jptrabajosExploracion.add(chkOtrosMetodos, null);
	jptrabajosExploracion.add(chkMining, null);
	jptrabajosExploracion.add(chkExplorationDrilling, null);
	jpTrabajos.add(jLabel6, null);
	jpTrabajos.add(jLabel5, null);
	jpTrabajos.add(jptrabajosExploracion, null);
	jpTrabajos.add(jpTrabajosProspeccion, null);
	jpTrabajos.add(jpMinerales, null);
	jTabbedPane1.addTab("Trabajos", jpTrabajos);
	jPanel1.add(tfOtherEquipment, null);
	jPanel1.add(chkOtherEquipment, null);
	jPanel1.add(jLabel12, null);
	jPanel1.add(jLabel11, null);
	jPanel1.add(jLabel9, null);
	jPanel1.add(chkVehicleOwnYes, null);
	jPanel1.add(chkVehicleOwnNo, null);
	jPanel1.add(chkDrillOwnYes, null);
	jPanel1.add(chkDrillOwnNo, null);
	jPanel1.add(chkCompressorOwnNo, null);
	jPanel1.add(chkCompressorOwnYes, null);
	jPanel1.add(jLabel10, null);
	jPanel1.add(jLabel8, null);
	jPanel1.add(jLabel7, null);
	jPanel1.add(tfVehicleQuantity, null);
	jPanel1.add(tfDrillQuantity, null);
	jPanel1.add(tfCompresorQuantity, null);
	jPanel1.add(tfVehicleType, null);
	jPanel1.add(tfDrillCapacity, null);
	jPanel1.add(tfCompresorCapacity, null);
	jPanel2.add(tfOtherProvision, null);
	jPanel2.add(chkOtherProvision, null);
	jPanel2.add(lblCaminos1, null);
	jPanel2.add(chkWaterProvision, null);
	jPanel2.add(tfPathKm, null);
	jPanel2.add(tfRoadKm, null);
	jPanel2.add(tfCoveredArea, null);
	jPanel2.add(chkCampFixed, null);
	jPanel2.add(chkCampMoveable, null);
	jPanel2.add(lblProvisionAgua, null);
	jPanel2.add(lblCaminos, null);
	jPanel2.add(lblCampamentos, null);
	jpequiposConstrucciones.add(lblConstrucciones, null);
	jpequiposConstrucciones.add(lblMaqEquipos, null);
	jpequiposConstrucciones.add(jPanel2, null);
	jpequiposConstrucciones.add(jPanel1, null);
	jTabbedPane1.addTab("Equipos & Constr.", jpequiposConstrucciones);
	jpInformacionTecnica.add(tfOtherMapsSacale, null);
	jpInformacionTecnica.add(tfMapMiningScale, null);
	jpInformacionTecnica.add(tfMapTopographicScale, null);
	jpInformacionTecnica.add(tfMapGeologicScale, null);
	jpInformacionTecnica.add(chkMineralTest, null);
	jpInformacionTecnica.add(chkMapDrilling, null);
	jpInformacionTecnica.add(chkChemicalAnalisys, null);
	jpInformacionTecnica.add(chkMapGeologic, null);
	jpInformacionTecnica.add(chkOtherMaps, null);
	jpInformacionTecnica.add(chkMapTopographic, null);
	jpInformacionTecnica.add(chkMapMining, null);
	jpPersonalOcupado.add(lblAdministrativos, null);
	jpPersonalOcupado.add(lblProfesionales, null);
	jpPersonalOcupado.add(lblTecnicos, null);
	jpPersonalOcupado.add(jLabel13, null);
	jpPersonalOcupado.add(tfProfessionals, null);
	jpPersonalOcupado.add(tfTechnicians, null);
	jpPersonalOcupado.add(tfAdministratives, null);
	jpPersonalOcupado.add(tfOperators, null);
	jPanel5.add(tfEasementOthers, null);
	jPanel5.add(chkEasementMines, null);
	jPanel5.add(chkEasementLand, null);
	jPanel5.add(chkEasementOthers, null);
	jPanel5.add(chkEasementWaters, null);
	jPanel5.add(chkEasementRoads, null);
	jpInformacion.add(lblMaqEquipos3, null);
	jpInformacion.add(lblMaqEquipos2, null);
	jpInformacion.add(lblMaqEquipos1, null);
	jpInformacion.add(jPanel5, null);
	jpInformacion.add(jpPersonalOcupado, null);
	jpInformacion.add(jpInformacionTecnica, null);
	jTabbedPane1.addTab("Información", jpInformacion);
	jpMontoEstimado.add(jLabel2, null);
	jpMontoEstimado.add(jLabel112, null);
	jpMontoEstimado.add(jLabel111, null);
	jpMontoEstimado.add(jLabel110, null);
	jpMontoEstimado.add(jLabel19, null);
	jpMontoEstimado.add(jLabel18, null);
	jpMontoEstimado.add(jLabel17, null);
	jpMontoEstimado.add(jLabel16, null);
	jpMontoEstimado.add(tfEquipmentCost, null);
	jpMontoEstimado.add(tfGenericCost, null);
	jpMontoEstimado.add(tfTotalCost, null);
	jpMontoEstimado.add(tfDrillingCost, null);
	jpMontoEstimado.add(tfOtherCost, null);
	jpMontoEstimado.add(tfRoadCost, null);
	jpMontoEstimado.add(tfBuildingCost, null);
	jpMonto.add(lblMaqEquipos5, null);
	jpMonto.add(lblMaqEquipos4, null);
	jpMonto.add(jpMontoEstimado, null);
	jpProtecAmbiental.add(chkAmbientalStatementProspection, null);
	jpProtecAmbiental.add(chkAmbientalStatementExploration, null);
	jpProtecAmbiental.add(jLabel15, null);
	jpProtecAmbiental.add(jLabel14, null);
	jpProtecAmbiental.add(chkAmbientalReportProspection, null);
	jpProtecAmbiental.add(chkAmbientalReportExploration, null);
	jpMonto.add(jpProtecAmbiental, null);
	jTabbedPane1.addTab("Monto", jpMonto);
	this.add(btnClose, null);
	this.add(btnAccept, null);
	this.add(jTabbedPane1, null);
	/*grupoChecks.add(chkMineralTypes1ra);
	grupoChecks.add(chkMineralTypes2da);*/
	chkMineralTypes1ra.setSelected(true);
	tfOtherWay.setEditable(false);
	tfOtherWay.setBackground(Color.white);
	tfOtherInvestigations.setEditable(false);
	tfOtherInvestigations.setBackground(Color.white);
	chkOtherInvest.setSelected(false);
	tfOtherInvestigations.setEditable(false);
	tfOtherInvestigations.setBackground(Color.white);
	chkOtherInvest.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  chkOtherInvest_actionPerformed(e);
				      }

				  }
	);
	chkOtrosMetodos.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   chkOtrosMetodos_actionPerformed(e);
				       }

				   }
	);
	compresorGroup.add(chkCompressorOwnYes);
	compresorGroup.add(chkCompressorOwnNo);
	chkCompressorOwnNo.setSelected(true);
	drillGroup.add(chkDrillOwnYes);
	drillGroup.add(chkDrillOwnNo);
	chkDrillOwnNo.setSelected(true);
	vehicleGroup.add(chkVehicleOwnYes);
	vehicleGroup.add(chkVehicleOwnNo);
	chkVehicleOwnNo.setSelected(true);
	tfMapTopographicScale.setEnabled(false);
	tfMapGeologicScale.setEnabled(false);
	tfMapMiningScale.setEnabled(false);
	tfOtherMapsSacale.setEnabled(false);
	chkMapTopographic.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     chkMapTopographic_actionPerformed(e);
					 }

				     }
	);
	chkMapGeologic.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  chkMapGeologic_actionPerformed(e);
				      }

				  }
	);
	chkMapMining.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkMapMining_actionPerformed(e);
				    }

				}
	);
	chkMapDrilling.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  chkMapDrilling_actionPerformed(e);
				      }

				  }
	);
	tfEasementOthers.setEditable(false);
	tfEasementOthers.setBackground(Color.white);
	chkEasementOthers.setSelected(false);
	chkEasementOthers.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     chkEasementOthers_actionPerformed(e);
					 }

				     }
	);
	chkOtherProvision.setSelected(false);
	tfOtherProvision.setEditable(false);
	tfOtherProvision.setBackground(Color.white);
	chkOtherProvision.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     chkOtherProvision_actionPerformed(e);
					 }

				     }
	);
	chkOtherEquipment.setSelected(false);
	tfOtherEquipment.setEditable(false);
	tfOtherEquipment.setBackground(Color.white);
	chkOtherEquipment.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     chkOtherEquipment_actionPerformed(e);
					 }

				     }
	);
	setJPMonto();
    }

    private void chkOtherEquipment_actionPerformed(ActionEvent e) {
	if (chkOtherEquipment.isSelected()) {
	    tfOtherEquipment.setEditable(true);
	} else {
	    tfOtherEquipment.setEditable(false);
	}
    }

    private void chkOtherProvision_actionPerformed(ActionEvent e) {
	if (chkOtherProvision.isSelected()) {
	    tfOtherProvision.setEditable(true);
	} else {
	    tfOtherProvision.setEditable(false);
	}
    }

    private void chkEasementOthers_actionPerformed(ActionEvent e) {
	if (chkEasementOthers.isSelected()) {
	    tfEasementOthers.setEditable(true);
	} else {
	    tfEasementOthers.setEditable(false);
	}
    }

    private void chkMapTopographic_actionPerformed(ActionEvent e) {
	if (chkMapTopographic.isSelected()) {
	    tfMapTopographicScale.setEnabled(true);
	} else {
	    tfMapTopographicScale.setEnabled(false);
	}
    }

    private void chkMapGeologic_actionPerformed(ActionEvent e) {
	if (chkMapGeologic.isSelected()) {
	    tfMapGeologicScale.setEnabled(true);
	} else {
	    tfMapGeologicScale.setEnabled(false);
	}
    }

    private void chkMapMining_actionPerformed(ActionEvent e) {
	if (chkMapMining.isSelected()) {
	    tfMapMiningScale.setEnabled(true);
	} else {
	    tfMapMiningScale.setEnabled(false);
	}
    }

    private void chkMapDrilling_actionPerformed(ActionEvent e) {

    }

    private void chkOtherInvest_actionPerformed(ActionEvent e) {
	if (chkOtherInvest.isSelected()) {
	    tfOtherInvestigations.setEditable(true);
	} else {
	    tfOtherInvestigations.setEditable(false);
	}
    }

    private void chkOtrosMetodos_actionPerformed(ActionEvent e) {
	if (chkOtrosMetodos.isSelected()) {
	    tfOtherWay.setEditable(true);
	} else {
	    tfOtherWay.setEditable(false);
	}
    }

    private void dispose() {
	switch (parentType) {
	    case DIALOG :
		((BasicDialog)parent).dispose();
		break;
	    case INTERNALFRAME :
		((BasicInternalFrame)parent).dispose();
		break;
	    case FRAME :
		((JFrame)parent).dispose();
		break;
	}
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	explorationSchemeClass.setExplorationSchemeRecord(NO);
	explorationSchemeClass.setExplorationSchemeStatus(COMPLETE);
	dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	// GUARDAR LAS VARIABLES
	setParams();
	explorationSchemeClass.setExplorationSchemeStatus(COMPLETE);
	explorationSchemeClass.setExplorationSchemeRecord(YES);
	dispose();
	/*String insertES = armarConsulta();
	if (LibSQL.exActualizar('a', insertES)) {
	    Advisor.messageBox("El Programa de Exploración Mínimo fué registrado exitosamente\n y será adjuntado a la Solicitud de Permiso de exploración", "Mensaje");
	    if (opcion == 0) {
		ExplorationRequestPanel.setIdExplorationScheme(idexplorationScheme);
	    } else {
		prospection.setArgument(idexplorationScheme, 31);
		prospection.setArgument("true", 32);
	    }
	    dispose();
	} else {
	    Advisor.messageBox("Ocurrio un error, los datos no se registraron", "Error");
	}
	*/
    }
    /* private String armarConsulta() {
	String insert = "", water = "", mineralType = "";
	idexplorationScheme = LibSQL.getCampo("SELECT MAX(idexplorationscheme) + 1 FROM file.explorationscheme");
	if (chkEasementWaters.isSelected()) {
	    water = "De pozo";
	} else {
	    water = tfOtherProvision.getText().trim();
	}
	if (chkMineralTypes1ra.isSelected()) {
	    mineralType = "1ra";
	} else {
	    mineralType = "2da";
	}
	//este es el valor para mapdrillingscale pero no figura en el formulario
	insert = "INSERT INTO file.explorationscheme VALUES(" + idexplorationScheme + ", 0,0" + ",'" + mineralType + "',0" + tfTermDays.getText().trim() + ",0" + tfWorkDays.getText().trim() + ",'" + tfOtherInvestigations.getText().trim() + "', " + chkSatelitalPicture.isSelected() + ", " + chkAerialPhoto.isSelected() + ", " + chkTopographic.isSelected() + ", " + chkGeological.isSelected() + ", " + chkGeophisic.isSelected() + ", " + chkGeochemical.isSelected() + ", " + chkProspectionDrilling.isSelected() + ", " + chkSampling.isSelected() + ", 0" + tfProspectionTime.getText().trim() + ", 0" + tfExplorationDays.getText().trim() + ", " + chkExplorationDrilling.isSelected() + ", " + chkMining.isSelected() + ",'" + tfOtherWay.getText().trim() + "', 0" + tfCompresorCapacity.getText().trim() + ", 0" + tfCompresorQuantity.getText().trim() + ", " + chkCompressorOwnYes.isSelected() + ", 0" + tfDrillCapacity.getText().trim() + ", 0" + tfDrillQuantity.getText().trim() + ", " + chkDrillOwnYes.isSelected() + ", '" + tfVehicleType.getText().trim() + "', 0" + tfVehicleQuantity.getText().trim() + ", " + chkVehicleOwnYes.isSelected() + ", '" + tfOtherEquipment.getText().trim() + "', " + chkCampFixed.isSelected() + ", " + chkCampMoveable.isSelected() + ", 0" + tfCoveredArea.getText().trim() + ", 0" + tfRoadKm.getText().trim() + ", 0" + tfPathKm.getText().trim() + ", '" + water + "', " + chkMapTopographic.isSelected() + ", 0" + tfMapTopographicScale.getText().trim() + ", " + chkMapGeologic.isSelected() + ", 0" + tfMapGeologicScale.getText().trim() + ", " + chkMapDrilling.isSelected() + ", 0" + ", " + chkMapMining.isSelected() + ", 0" + tfMapMiningScale.getText().trim() + ", " + chkOtherMaps.isSelected() + ", 0" + tfOtherMapsSacale.getText().trim() + ", " + chkChemicalAnalisys.isSelected() + ", " + chkMineralTest.isSelected() + ", 0" + tfOperators.getText().trim() + ", 0" + tfTechnicians.getText().trim() + ", 0" + tfAdministratives.getText().trim() + ", 0" + tfProfessionals.getText().trim() + ", " + chkEasementLand.isSelected() + ", " + chkEasementWaters.isSelected() + ", " + chkEasementRoads.isSelected() + ", " + chkEasementMines.isSelected() + ",'" + tfEasementOthers.getText().trim() + "', " + chkAmbientalReportProspection.isSelected() + ", " + chkAmbientalReportExploration.isSelected() + ", " + chkAmbientalStatementProspection.isSelected() + ", " + chkAmbientalStatementExploration.isSelected() + ", 0" + tfBuildingCost.getText().trim() + ", 0" + tfRoadCost.getText().trim() + ", 0" + tfDrillingCost.getText().trim() + ", 0" + tfOtherCost.getText().trim() + ", 0" + tfEquipmentCost.getText().trim() + ", 0" + tfGenericCost.getText().trim() + ", 0" + tfTotalCost.getText().trim() + ",'');";
	return insert;
    }*/

    public void setManager(Manager _mgmt) {
	mgmt = _mgmt;
    }

    private void setJPMonto() {
	tfBuildingCost.setValue("0");
	tfRoadCost.setValue("0");
	tfDrillingCost.setValue("0");
	tfOtherCost.setValue("0");
	tfEquipmentCost.setValue("0");
	tfGenericCost.setValue("0");
	tfTotalCost.setValue("0");
	suma();
    }

    private void suma() {
	double buildingCost = Double.parseDouble("0" + tfBuildingCost.getText().trim());
	double roadCost = Double.parseDouble("0" + tfRoadCost.getText().trim());
	double drillingCost = Double.parseDouble("0" + tfDrillingCost.getText().trim());
	double otherCost = Double.parseDouble("0" + tfOtherCost.getText().trim());
	double equipmentCost = Double.parseDouble("0" + tfEquipmentCost.getText().trim());
	double genericCost = Double.parseDouble("0" + tfGenericCost.getText().trim());
	double total = buildingCost + roadCost + drillingCost + otherCost + equipmentCost + genericCost;
	String valorTotal = String.valueOf(total);
	tfTotalCost.setValue(valorTotal);
    }

    public void setExplorationSchemeObject(ExplorationSchemeClass _explorationSchemeClass) {
	explorationSchemeClass = _explorationSchemeClass;
	if (explorationSchemeClass.getIdExplorationRequest() != -1) {
	    explorationSchemeClass.retrieveExplorationSchemeData();
	    loadData();
	}
    }

    private void loadData() {
	//pestaña 3
	if (explorationSchemeClass.getMineralType().equals("1ra&2da")) {
	    chkMineralTypes1ra.setSelected(true);
	    chkMineralTypes2da.setSelected(true);
	} else if (explorationSchemeClass.getMineralType().equals("1ra")) {
	    chkMineralTypes1ra.setSelected(true);
	    chkMineralTypes2da.setSelected(false);
	} else if (explorationSchemeClass.getMineralType().equals("2da")) {
	    chkMineralTypes1ra.setSelected(false);
	    chkMineralTypes2da.setSelected(true);
	} else {
	    chkMineralTypes1ra.setSelected(false);
	    chkMineralTypes2da.setSelected(false);
	}
	tfTermDays.setValue(String.valueOf(explorationSchemeClass.getTermDays()));
	tfWorkDays.setValue(String.valueOf(explorationSchemeClass.getWorkDays()));
	chkSatelitalPicture.setSelected(explorationSchemeClass.isSatellitalPicture());
	chkAerialPhoto.setSelected(explorationSchemeClass.isAerialPhoto());
	chkTopographic.setSelected(explorationSchemeClass.isTopoGraphic());
	chkGeological.setSelected(explorationSchemeClass.isGeoloGical());
	chkGeochemical.setSelected(explorationSchemeClass.isGeoChemical());
	chkGeophisic.setSelected(explorationSchemeClass.isGeoPhisic());
	chkProspectionDrilling.setSelected(explorationSchemeClass.isProspectionDrilling());
	chkSampling.setSelected(explorationSchemeClass.isSampling());
	if (!explorationSchemeClass.getOtherInvestigations().trim().equals("")) {
	    chkOtherInvest.setSelected(true);
	} else {
	    chkOtherInvest.setSelected(false);
	}
	tfOtherInvestigations.setValue(explorationSchemeClass.getOtherInvestigations());
	tfProspectionTime.setValue(String.valueOf(explorationSchemeClass.getProspectionDays()));
	chkExplorationDrilling.setSelected(explorationSchemeClass.isExplorationDrilling());
	chkMining.setSelected(explorationSchemeClass.isMining());
	if (!explorationSchemeClass.getOtherWay().trim().equals("")) {
	    chkOtrosMetodos.setSelected(true);
	} else {
	    chkOtrosMetodos.setSelected(false);
	}
	tfOtherWay.setValue(String.valueOf(explorationSchemeClass.getOtherWay()));
	tfExplorationDays.setValue(String.valueOf(explorationSchemeClass.getExplorationDays()));
	//pestaña 4
	tfCompresorCapacity.setValue(String.valueOf(explorationSchemeClass.getCompressorCapacity()));
	tfCompresorQuantity.setValue(String.valueOf(explorationSchemeClass.getCompressorQty()));
	if (explorationSchemeClass.isCompressorOwn()) {
	    chkCompressorOwnYes.setSelected(true);
	} else {
	    chkCompressorOwnNo.setSelected(true);
	}
	tfDrillCapacity.setValue(String.valueOf(explorationSchemeClass.getDrillCapacity()));
	tfDrillQuantity.setValue(String.valueOf(explorationSchemeClass.getDrillQty()));
	if (explorationSchemeClass.isDrillOwn()) {
	    chkDrillOwnYes.setSelected(true);
	} else {
	    chkDrillOwnNo.setSelected(true);
	}
	tfVehicleType.setValue(String.valueOf(explorationSchemeClass.getVehicleType()));
	tfVehicleQuantity.setValue(String.valueOf(explorationSchemeClass.getVehicleType()));
	if (explorationSchemeClass.isVehicleown()) {
	    chkVehicleOwnYes.setSelected(true);
	} else {
	    chkVehicleOwnNo.setSelected(true);
	}
	if (!explorationSchemeClass.getExtraEquipment().trim().equals("")) {
	    chkOtherEquipment.setSelected(true);
	} else {
	    chkOtherEquipment.setSelected(false);
	}
	tfOtherEquipment.setValue(String.valueOf(explorationSchemeClass.getExtraEquipment()));
	chkCampFixed.setSelected(explorationSchemeClass.isCampFixed());
	chkCampMoveable.setSelected(explorationSchemeClass.isCampMoveable());
	tfCoveredArea.setValue(String.valueOf(explorationSchemeClass.getCoveredArea()));
	tfRoadKm.setValue(String.valueOf(explorationSchemeClass.getRoad()));
	tfRoadKm.setValue(String.valueOf(explorationSchemeClass.getPath()));
	//AGREGAR EL CAMPO AGUA DE POZO DEL TIPO BOOLEAN
	if (!explorationSchemeClass.getWaterProvission().trim().equals("")) {
	    chkOtherProvision.setSelected(true);
	} else {
	    chkWaterProvision.setSelected(true);
	}
	tfOtherProvision.setValue(String.valueOf(explorationSchemeClass.getWaterProvission()));
	//PESTAÑA 5
	chkMapTopographic.setSelected(explorationSchemeClass.isMapTopographics());
	tfMapTopographicScale.setValue(String.valueOf(explorationSchemeClass.getMapTopographicsScale()));
	chkMapGeologic.setSelected(explorationSchemeClass.isMapGeological());
	tfMapGeologicScale.setValue(String.valueOf(explorationSchemeClass.getMapGeologicalScale()));
	chkMapDrilling.setSelected(explorationSchemeClass.isMapDrilling());
	chkMapMining.setSelected(explorationSchemeClass.isMapMining());
	tfMapMiningScale.setValue(String.valueOf(explorationSchemeClass.getMapminingscale()));
	chkOtherMaps.setSelected(explorationSchemeClass.isMapOther());
	tfOtherMapsSacale.setValue(String.valueOf(explorationSchemeClass.getMapOtherScale()));
	chkChemicalAnalisys.setSelected(explorationSchemeClass.isChemiCanalysis());
	chkMineralTest.setSelected(explorationSchemeClass.isMineralTest());
	tfOperators.setValue(String.valueOf(explorationSchemeClass.getOperators()));
	tfAdministratives.setValue(String.valueOf(explorationSchemeClass.getAdministratives()));
	tfTechnicians.setValue(String.valueOf(explorationSchemeClass.getTechnicians()));
	tfProfessionals.setValue(String.valueOf(explorationSchemeClass.getProfessionals()));
	chkEasementLand.setSelected(explorationSchemeClass.isEasementLand());
	chkEasementWaters.setSelected(explorationSchemeClass.isEasementWater());
	chkEasementRoads.setSelected(explorationSchemeClass.isEasementRoads());
	chkEasementMines.setSelected(explorationSchemeClass.isEasementMines());
	if (!explorationSchemeClass.getEasementOthers().trim().equals("")) {
	    chkEasementOthers.setSelected(true);
	} else {
	    chkEasementOthers.setSelected(false);
	}
	tfEasementOthers.setValue(String.valueOf(explorationSchemeClass.getEasementOthers()));
	// PESTAÑA 6
	chkAmbientalReportProspection.setSelected(explorationSchemeClass.isAmbientalReportProspection());
	chkAmbientalReportExploration.setSelected(explorationSchemeClass.isAmbientalReportExploration());
	chkAmbientalStatementProspection.setSelected(explorationSchemeClass.isAmbientalStatementProspection());
	chkAmbientalStatementExploration.setSelected(explorationSchemeClass.isAmbientalStatementExploration());
	tfBuildingCost.setValue(String.valueOf(explorationSchemeClass.getBuildingCost()));
	tfRoadCost.setValue(String.valueOf(explorationSchemeClass.getRoadCost()));
	tfDrillingCost.setValue(String.valueOf(explorationSchemeClass.getDrillingCost()));
	tfOtherCost.setValue(String.valueOf(explorationSchemeClass.getOtherCost()));
	tfEquipmentCost.setValue(String.valueOf(explorationSchemeClass.getEquipmentCost()));
	tfGenericCost.setValue(String.valueOf(explorationSchemeClass.getGenericCost()));
	tfTotalCost.setValue(String.valueOf(explorationSchemeClass.getTotalCost()));
    }

    private void setParams() {
	//PESTAÑA 3
	if (chkMineralTypes1ra.isSelected() && chkMineralTypes2da.isSelected()) {
	    explorationSchemeClass.setMineralType("1ra&2da");
	} else if (chkMineralTypes1ra.isSelected()) {
	    explorationSchemeClass.setMineralType("1ra");
	} else if (chkMineralTypes2da.isSelected()) {
	    explorationSchemeClass.setMineralType("2da");
	} else {
	    explorationSchemeClass.setMineralType("");
	}
	explorationSchemeClass.setTermDays(Integer.parseInt("0" + tfTermDays.getString()));
	explorationSchemeClass.setWorkDays(Integer.parseInt("0" + tfWorkDays.getString()));
	explorationSchemeClass.setSatellitalPicture(chkSatelitalPicture.isSelected());
	explorationSchemeClass.setAerialPhoto(chkAerialPhoto.isSelected());
	explorationSchemeClass.setTopoGraphic(chkTopographic.isSelected());
	explorationSchemeClass.setGeoloGical(chkGeological.isSelected());
	explorationSchemeClass.setGeoChemical(chkGeochemical.isSelected());
	explorationSchemeClass.setGeoPhisic(chkGeophisic.isSelected());
	explorationSchemeClass.setProspectionDrilling(chkProspectionDrilling.isSelected());
	explorationSchemeClass.setSampling(chkSampling.isSelected());
	explorationSchemeClass.setOtherInvestigations(tfOtherInvestigations.getText());
	explorationSchemeClass.setProspectionDays(Integer.parseInt("0" + tfProspectionTime.getString()));
	explorationSchemeClass.setExplorationDrilling(chkExplorationDrilling.isSelected());
	explorationSchemeClass.setMining(chkMining.isSelected());
	explorationSchemeClass.setOtherWay(tfProspectionTime.getString());
	explorationSchemeClass.setExplorationDays(Integer.parseInt("0" + tfExplorationDays.getString()));
	//PESTAÑA 4
	explorationSchemeClass.setCompressorCapacity(Double.parseDouble("0" + tfCompresorCapacity.getString()));
	explorationSchemeClass.setCompressorQty(Double.parseDouble("0" + tfCompresorQuantity.getString()));
	explorationSchemeClass.setCompressorOwn(chkCompressorOwnYes.isSelected());
	explorationSchemeClass.setDrillCapacity(Double.parseDouble("0" + tfDrillCapacity.getString()));
	explorationSchemeClass.setDrillQty(Double.parseDouble("0" + tfDrillQuantity.getString()));
	explorationSchemeClass.setCompressorOwn(chkCompressorOwnYes.isSelected());
	explorationSchemeClass.setVehicleType(tfVehicleType.getString());
	explorationSchemeClass.setVehicleQty(Double.parseDouble("0" + tfVehicleQuantity.getString()));
	explorationSchemeClass.setVehicleown(chkVehicleOwnYes.isSelected());
	explorationSchemeClass.setExtraEquipment(tfOtherEquipment.getText().trim());
	explorationSchemeClass.setCampFixed(chkCampFixed.isSelected());
	explorationSchemeClass.setCampMoveable(chkCampMoveable.isSelected());
	explorationSchemeClass.setCoveredArea(Double.parseDouble("0" + tfCoveredArea.getString()));
	explorationSchemeClass.setRoad(Double.parseDouble("0" + tfRoadKm.getString()));
	explorationSchemeClass.setPath(Double.parseDouble("0" + tfPathKm.getString()));
	explorationSchemeClass.setWaterProvission(tfOtherProvision.getText().trim());
	//PESTAÑA 5
	explorationSchemeClass.setMapTopographics(chkMapTopographic.isSelected());
	explorationSchemeClass.setMapTopographicsScale(Double.parseDouble("0" + tfMapTopographicScale.getString()));
	explorationSchemeClass.setMapGeological(chkMapGeologic.isSelected());
	explorationSchemeClass.setMapGeologicalScale(Double.parseDouble("0" + tfMapGeologicScale.getString()));
	explorationSchemeClass.setMapDrilling(chkMapDrilling.isSelected());
	explorationSchemeClass.setMapMining(chkMapMining.isSelected());
	explorationSchemeClass.setMapminingscale(Double.parseDouble("0" + tfMapMiningScale.getString()));
	explorationSchemeClass.setOtherWay(tfOtherWay.getText().trim());
	explorationSchemeClass.setChemiCanalysis(chkChemicalAnalisys.isSelected());
	explorationSchemeClass.setMineralTest(chkMineralTest.isSelected());
	explorationSchemeClass.setOperators(Integer.parseInt("0" + tfOperators.getString()));
	explorationSchemeClass.setAdministratives(Integer.parseInt("0" + tfAdministratives.getString()));
	explorationSchemeClass.setTechnicians(Integer.parseInt("0" + tfTechnicians.getString()));
	explorationSchemeClass.setProfessionals(Integer.parseInt("0" + tfProfessionals.getString()));
	explorationSchemeClass.setEasementLand(chkEasementLand.isSelected());
	explorationSchemeClass.setEasementWater(chkEasementWaters.isSelected());
	explorationSchemeClass.setEasementRoads(chkEasementRoads.isSelected());
	explorationSchemeClass.setEasementMines(chkEasementMines.isSelected());
	explorationSchemeClass.setEasementOthers(tfEasementOthers.getText().trim());
	//PESTAÑA 6
	explorationSchemeClass.setAmbientalReportProspection(chkAmbientalReportProspection.isSelected());
	explorationSchemeClass.setAmbientalReportExploration(chkAmbientalReportExploration.isSelected());
	explorationSchemeClass.setAmbientalStatementProspection(chkAmbientalStatementProspection.isSelected());
	explorationSchemeClass.setAmbientalStatementExploration(chkAmbientalStatementExploration.isSelected());
	explorationSchemeClass.setBuildingCost(Double.parseDouble("0" + tfBuildingCost.getText().trim()));
	explorationSchemeClass.setRoadCost(Double.parseDouble("0" + tfRoadCost.getText().trim()));
	explorationSchemeClass.setDrillingCost(Double.parseDouble("0" + tfDrillingCost.getText().trim()));
	explorationSchemeClass.setOtherCost(Double.parseDouble("0" + tfOtherCost.getText().trim()));
	explorationSchemeClass.setEquipmentCost(Double.parseDouble("0" + tfEquipmentCost.getText().trim()));
	explorationSchemeClass.setGenericCost(Double.parseDouble("0" + tfGenericCost.getText().trim()));
	explorationSchemeClass.setTotalCost(Double.parseDouble("0" + tfTotalCost.getText().trim()));
    }

}

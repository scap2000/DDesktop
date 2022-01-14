package org.digitall.projects.kratos.pintos;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import org.digitall.apps.calendar.interfaces.CoordinatorStickyNote;
import org.digitall.apps.calendar.interfaces.DCalendar;
import org.digitall.apps.calendar.interfaces.NewsList;
import org.digitall.apps.calendar.interfaces.StickyNote;
import org.digitall.apps.cashflow.interfaces.accounting.AccountsAvailableAmountList;
import org.digitall.apps.cashflow.interfaces.accounting.JournalList;
import org.digitall.apps.cashflow.interfaces.accountingentry.BookKeepingEntryByModelMgmt;
import org.digitall.apps.cashflow.interfaces.accountingentry.BookKeepingEntryList;
import org.digitall.apps.cashflow.interfaces.accountingentry.BookKeepingEntryModelsList;
import org.digitall.apps.cashflow.interfaces.accountingentry.CreditNotesAdmin;
import org.digitall.apps.cashflow.interfaces.accountingentry.DebitNotesAdmin;
import org.digitall.apps.cashflow.interfaces.paymentorder.PaymentOrderList;
import org.digitall.apps.cashflow.interfaces.paymentorder.PaymentOrderMgmt;
import org.digitall.apps.cashflow.interfaces.voucher.InvoiceTypeB;
import org.digitall.apps.cashflow.interfaces.voucher.VoucherToInvoiceMain;
import org.digitall.apps.cashflow.interfaces.voucher.VoucherToProvisionOrderMain;
import org.digitall.apps.chwy.ChWYChatPanel;
import org.digitall.apps.resourcesrequests.interfaces.resourcesmovements.DespatchNotesList;
import org.digitall.apps.resourcesrequests.interfaces.resourcesrequests.ResourcesRequestsMain;
import org.digitall.common.cashflow.interfaces.CompanyTreePanel;
import org.digitall.common.cashflow.interfaces.account.BankAccountsList;
import org.digitall.common.cashflow.interfaces.accounting.AccountsMain;
import org.digitall.common.cashflow.interfaces.budget.BudgetList;
import org.digitall.common.cashflow.interfaces.cashmovement.CashMovementTypesTree;
import org.digitall.common.cashflow.interfaces.cashmovement.CashMovementsList;
import org.digitall.common.cashflow.interfaces.costscentre.CCList;
import org.digitall.common.cashflow.interfaces.voucher.VouchersList;
import org.digitall.common.components.basic.BasicCard;
import org.digitall.common.resourcescontrol.interfaces.PersonsList;
import org.digitall.common.resourcescontrol.interfaces.ResourcesList;
import org.digitall.common.resourcescontrol.interfaces.SkillList;
import org.digitall.common.resourcescontrol.interfaces.providers.ProvidersMain;
import org.digitall.common.resourcesrequests.interfaces.provisionorder.ProvisionOrderMain;
import org.digitall.common.resourcesrequests.interfaces.purchaseorder.PurchaseOrderGenerateList;
import org.digitall.common.resourcesrequests.interfaces.purchaseorder.PurchaseOrdersList;
import org.digitall.common.resourcesrequests.interfaces.resourcesmovements.ResourcesDeliverList;
import org.digitall.common.resourcesrequests.interfaces.resourcesmovements.ResourcesReceiveMain;
import org.digitall.common.resourcesrequests.interfaces.resourcesrequests.ResourcesRequestsAuthMain;
import org.digitall.common.systemmanager.ChangePassword;
import org.digitall.common.systemmanager.interfaces.SystemInfo;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicConfig;
import org.digitall.lib.components.basic.BasicDesktop;
import org.digitall.lib.components.basic.DesktopButton;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.environment.Debug;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.html.BrowserLauncher;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;
import org.digitall.projects.gdigitall.gobiernodigitall.SQLImport;

import org.jfree.report.JFreeReportBoot;


public class Boot {

    public static final int INIT_START = 0;
    //RESERVED = -1;
    public static final int INIT_RESTART = -2;
    public static final int INIT_RESET = -3;
    public static final int INIT_HALT = -4;
    public static final int STATUS_STARTING = -5;
    public static final int STATUS_RESTARTING = -6;
    public static final int STATUS_RESETTING = -7;
    public static final int STATUS_SHUTTING_DOWN = -8;
    public static final int STATUS_RUNNING = -9;
    public static final int STATUS_STOPPED = -10;
    /**
     * Modulos
     * */
    public static int MAIN_MODULE = 0;
    public static int CASHFLOWADMIN_MODULE = 1;
    public static int RESOURCESREQUESTS_MODULE = 2;
    public static int RESOURCESCONTROL_MODULE = 3;
    public static int LOGISTICS_MODULE = 4;
    public static int TASKS_MODULE = 5;
    public static int STICKYNOTES_MODULE = 6;
    public static int COMMANDCENTER_MODULE = 7;
    public static int CASHFLOW_MODULE = -1;
    public static int CHAT_MODULE = -1;
    /**
     * Estado de los modulos
     * */
    public static int MAIN_MODULE_STATUS = -1;
    public static int LOGISTICS_MODULE_STATUS = -1;
    public static int RESOURCESCONTROL_MODULE_STATUS = -1;
    public static int RESOURCESREQUESTS_MODULE_STATUS = -1;
    public static int CASHFLOWADMIN_MODULE_STATUS = -1;
    public static int CASHFLOW_MODULE_STATUS = -1;
    public static int CHAT_MODULE_STATUS = -1;
    public static int STICKYNOTES_MODULE_STATUS = -1;
    public static int COMMANDCENTER_MODULE_STATUS = -1;
    public static int TASKS_MODULE_STATUS = -1;
    private static Color allModulesEndColor = new Color(30, 30, 30);
    private static Color allModulesStartColor[] = { new Color(17, 27, 56), new Color(44, 78, 28), new Color(66, 17, 60), new Color(90, 18, 18), new Color(180, 91, 23), new Color(131, 110, 11), new Color(81, 72, 42) };

    public static void rc3rdpartyApps(int _action, BasicDesktop _desktop) {
	/**
	 * REPORTING
	 * JFREE 0.8.2
	 *  
	 * */
	JFreeReportBoot.getInstance().start();
	/**
	 * MAPA
	 * */
	/*drawPanel = new JInternalFrame("Croquis de Ubicaciï¿½n");
	layerListFrame = new JInternalFrame("Listado de Layers");
	mapPanel.setCoordinateViewer(coordinateViewer);
	drawPanel.setClosable(true);
	drawPanel.setIconifiable(true);
	drawPanel.setResizable(true);
	coordinateViewer.setVisible(false);
	drawPanel.setVisible(false);
	drawPanel.getContentPane().setLayout(new BorderLayout());
	drawPanel.getContentPane().add(mapPanel, BorderLayout.CENTER);*/
	/*patch
	 parentDesktop.add(drawPanel);
	 parentDesktop.add(coordinateViewer);
	 endpatch*/
	//coordinateViewer.setTitle("Coordenadas");
	//Layer _argentina = new Layer("Argentina", "geo", "provincesb_ar", "the_geom", "(1=1/* admin_name = 'Jujuy'*/)", "gid");
	/*_argentina.setProjectionType(CoordinateSystems.LL);
	mapPanel.addLayer(_argentina);*/
	//Layer _districts = new Layer("Departamentos", "geo", "districts_ar", "the_geom", "(1=1/*provincia = 'JUJUY'*/)", "gid");
	/*_districts.setProjectionType(CoordinateSystems.LL);
	//mapPanel.addLayer(_districts);
	first.add(layerListFrame);
	layerListFrame.getContentPane().add(layerListPanel, null);
	layerListFrame.setBounds(0, 0, 600, 100);
	layerListFrame.setVisible(false);
	first.add(drawPanel);
	first.add(coordinateViewer);
	drawPanel.show();
	drawPanel.setBounds(0, 0, 800, 600);
	coordinateViewer.show();
	layerListPanel.setDrawPanel(mapPanel);
	layerListFrame.show();
	*/
	/**
	 * FIN MAPA
	 * */
    }

    public static void rcMainModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (MAIN_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting MAIN");
		    Environment.jpStatusBar.setAction("Loading Main");
		    MAIN_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.close_session_32x32);
		    Environment.mainTabbedPane.setOpaque(false);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    try {
			//Environment.mp3Player = new StandalonePlayer();
			//Environment.mp3Player.setDesktop(_desktop);
		    } catch (NoClassDefFoundError x) {
			x.printStackTrace();
		    }
		    /**
		     * CHAT
		     * */

		    ExtendedInternalFrame chwy = new ExtendedInternalFrame("Chat", IconTypes.inbox_32x32);
		    ChWYChatPanel chwyPanel = new ChWYChatPanel(Environment.sessionUser, Environment.jpStatusBar);
		    chwy.setClosable(false);
		    chwy.setCentralPanel(chwyPanel);
		    chwy.setDesktop(_desktop);
		    chwy.setIcon(true);

		    /**
		      * CALENDAR
		      * */
		    Environment.jpStatusBar.setAction("Loading Calendar");
		    DCalendar calendarIFrame = new DCalendar();
		    calendarIFrame.setVisible(false);
		    calendarIFrame.start();
		    _desktop.setBottomRightComponent(calendarIFrame);
		    //player.setIcon(true);
		    /*JCTermSwingFrame x = new JCTermSwingFrame("SSH TERM");
		     x.setSize(800,600);
		     x.setDesktop(_desktop);
		     x.setClosable(false);
		     x.setIconifiable(true);
		     x.setIcon(true);*/
		    /*Boot.rcCashFlowClientModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_CASHFLOW));
		     Boot.rcNewsModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_NEWS));
		     Boot.rcChatModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_CHAT));*/
		    /**BOTONES*/
		    /**
		      * BOTON SALIR
		      * */
		    DesktopButton btnExit = new DesktopButton("Salir");
		    btnExit.setRolloverEnabled(true);
		    btnExit.setIcon(IconTypes.exit_32x32);
		    btnExit.setRolloverIcon(IconTypes.exit_ro_32x32);
		    btnExit.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					closeApplication();
				    }

				}
		    );
		    _desktop.addButton(btnExit);
		    /**
		     * Boton del WebMail
		     * */
		    DesktopButton btnWebMail = new DesktopButton("WebMail");
		    btnWebMail.setRolloverEnabled(true);
		    btnWebMail.setIcon(IconTypes.webmail_32x32);
		    btnWebMail.setRolloverIcon(IconTypes.webmail_ro_32x32);
		    btnWebMail.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   BrowserLauncher.openURL("http://www.digitallsh.com.ar/");
				       }

				   }
		    );
		    _desktop.addButton(btnWebMail);
		    /**
		     * Boton TEST
		     * */
		    DesktopButton btnTest = new DesktopButton("TEST");
		    btnTest.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					// TODO
					// CAMBIAR EL CONSTRUCTOR!!!
					/*FileManTransfersPanel fileTran = new FileManTransfersPanel(Environment.sessionUser, Environment.jpStatusBar);
				        Environment.mainDesktop.add(fileTran);
				        fileTran.setVisible(true);
				        DInternalFrame _temp = new DInternalFrame();
				        Environment.mainDesktop.add(_temp);
				        _temp.setVisible(true);*/
					/*String[] m = new String[1];
				        m[0] = "santiago";
				        Mail.postMail(m, "asunto","texto", "santiago@digitallsh.com.ar", false, "");*/
					//Mail.fetchMail();
					/*StickyNote note1 = new StickyNote("prueba1", 0, 0);
				        StickyNote note2 = new StickyNote("prueba2", 20, 20);
				        StickyNote note3 = new StickyNote("prueba3", 50, 100);*/
					/*Editor editor = new Editor("Doc. nuevo", "Pag. nueva", false, "", true, "");
				        editor.setModal(true);
				        editor.setVisible(true);*/
				    }

				}
		    );
		    //_desktop.addButton(btnTest);
		    /**
		     * INFORMACION DEL SISTEMA
		     * */
		    ExtendedInternalFrame systemInfo = new ExtendedInternalFrame("Info del Sistema");
		    SystemInfo jpSystemInfo = new SystemInfo();
		    systemInfo.setCentralPanel(jpSystemInfo);
		    systemInfo.setClosable(false);
		    systemInfo.setDesktop(_desktop);
		    systemInfo.setIcon(true);
		    /**
		     * SQL IMPORTER
		     * */
		    if (Environment.sessionUser.equals("admin")) {
			ExtendedInternalFrame sqlImport = new ExtendedInternalFrame("SQLImport");
			SQLImport jpSQLImport = new SQLImport();
			sqlImport.setCentralPanel(jpSQLImport);
			sqlImport.setClosable(false);
			sqlImport.setDesktop(_desktop);
			sqlImport.setIcon(true);
		    }
		    /**
		     * BOTON CERRAR SESION
		     * */
		    DesktopButton btnCloseSession = new DesktopButton("Cerrar<br>Sesion");
		    btnCloseSession.setRolloverEnabled(true);
		    btnCloseSession.setIcon(IconTypes.close_session_32x32);
		    btnCloseSession.setRolloverIcon(IconTypes.close_session_ro_32x32);
		    btnCloseSession.addActionListener(new ActionListener() {

					    public void actionPerformed(ActionEvent e) {
						if (e.getModifiers() == 17) {
						    ChangePassword chgPasswd = new ChangePassword(Environment.sessionUser);
						    chgPasswd.setModal(true);
						    chgPasswd.setVisible(true);
						}
						/*else {
					            try {
					                if (Advisor.question(this, "Cerrar Sesion", "¿Esta seguro que desea cerrar su sesion?")) {
					                    //Close all modules and connections
					                    endDesktop();
					                    Login inicio = new Login(this, Environment.organization, Environment.developer, true);
					                    ComponentsManager.centerWindow(inicio);
					                    inicio.setModal(true);
					                    inicio.show();
					                    if (inicio.getValidado()) {
					                        startDesktop();
					                    } else {
					                        System.exit(0);
					                    }
					                }
					            } catch (Exception x) {
					                x.printStackTrace();
					            }
					        }*/
					    }

					}
		    );
		    //_desktop.addButton(btnCloseSession);
		    /**
		     * Instancio la fabulosa calculadora de coordenadas
		     * */
		    /*ExtendedInternalFrame geoCalc = new ExtendedInternalFrame("Calculadora geografica", IconTypes.geocalc_32x32);
		    CoordinateCalculator geoCalcPanel = new CoordinateCalculator();
		    geoCalc.setClosable(false);
		    geoCalc.setCentralPanel(geoCalcPanel);
		    geoCalc.setDesktop(_desktop);
		    geoCalc.setIcon(true);*/
		    /**
		     * NOVEDADES
		     * */
		    ExtendedInternalFrame news = new ExtendedInternalFrame("Novedades", IconTypes.inbox_32x32);
		    NewsList panelNewsList = new NewsList();
		    news.setClosable(false);
		    news.setCentralPanel(panelNewsList);
		    news.setDesktop(_desktop);
		    news.setIcon(true);

		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    MAIN_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Main loaded");
		    Debug.println("MAIN boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("MAIN already running!");
		}
		break;
	    case INIT_HALT :
		if (MAIN_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("MAIN halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcResourcesControlModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (RESOURCESCONTROL_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting RESOURCESCONTROL");
		    Environment.jpStatusBar.setAction("Loading ResourcesControl");
		    RESOURCESCONTROL_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*
		    ResourcesControlToolBar tbResourcesControl = new ResourcesControlToolBar(_desktop);
		    tbResourcesControl.setFloatable(false);
		    _desktop.add(tbResourcesControl);
		    */
		    ExtendedInternalFrame despatchNotes = new ExtendedInternalFrame("Listado de\nRemitos Externos", IconTypes.requestauth);
		    DespatchNotesList despatchNotesMain = new DespatchNotesList();
		    despatchNotes.setCentralPanel(despatchNotesMain);
		    despatchNotes.setClosable(false);
		    despatchNotes.setDesktop(_desktop);
		    despatchNotes.setIcon(true);
		    ExtendedInternalFrame despatchNotesMgmt = new ExtendedInternalFrame("Ingresos de Recursos\npor Compras (Externos)", IconTypes.requestauth);
		    ResourcesReceiveMain tdespatchNoteMgmt = new ResourcesReceiveMain();
		    despatchNotesMgmt.setCentralPanel(tdespatchNoteMgmt);
		    despatchNotesMgmt.setClosable(false);
		    despatchNotesMgmt.setDesktop(_desktop);
		    despatchNotesMgmt.setIcon(true);
		    ExtendedInternalFrame internalDespatchNotes = new ExtendedInternalFrame("Entrega de Recursos\n(Interno)", IconTypes.humanresources);
		    ResourcesDeliverList internalDespatchNote = new ResourcesDeliverList();
		    internalDespatchNotes.setCentralPanel(internalDespatchNote);
		    internalDespatchNotes.setClosable(false);
		    internalDespatchNotes.setDesktop(_desktop);
		    internalDespatchNotes.setIcon(true);
		    /*ExtendedInternalFrame resourcesAdmin = new ExtendedInternalFrame("Administrador\nde Recursos", IconTypes.materialresources);
		    ResourcesAdminMain resourcesAdminMain = new ResourcesAdminMain();
		    resourcesAdmin.setCentralPanel(resourcesAdminMain);
		    resourcesAdmin.setClosable(false);
		    resourcesAdmin.setDesktop(_desktop);
		    resourcesAdmin.setIcon(true);*/
		    ExtendedInternalFrame resources = new ExtendedInternalFrame("Recursos\nmateriales", IconTypes.materialresources);
		    ResourcesList resourcesList = new ResourcesList();
		    resources.setCentralPanel(resourcesList);
		    resources.setClosable(false);
		    resources.setDesktop(_desktop);
		    resources.setIcon(true);
		    ExtendedInternalFrame people = new ExtendedInternalFrame("Recursos\nhumanos", IconTypes.humanresources);
		    PersonsList personsList = new PersonsList();
		    people.setCentralPanel(personsList);
		    people.setClosable(false);
		    people.setDesktop(_desktop);
		    people.setIcon(true);
		    ExtendedInternalFrame providers = new ExtendedInternalFrame("Proveedores", IconTypes.providers);
		    ProvidersMain providerMain = new ProvidersMain();
		    providers.setCentralPanel(providerMain);
		    providers.setClosable(false);
		    providers.setDesktop(_desktop);
		    providers.setIcon(true);
		    ExtendedInternalFrame companies = new ExtendedInternalFrame("Compañias", IconTypes.providers);
		    CompanyTreePanel companyList = new CompanyTreePanel();
		    companies.setCentralPanel(companyList);
		    companies.setClosable(false);
		    companies.setDesktop(_desktop);
		    companies.setIcon(true);
		    ExtendedInternalFrame skills = new ExtendedInternalFrame("Habilidades", IconTypes.skills);
		    SkillList skillsList = new SkillList();
		    skills.setCentralPanel(skillsList);
		    skills.setClosable(false);
		    skills.setDesktop(_desktop);
		    skills.setIcon(true);
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    RESOURCESCONTROL_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Logistics loaded");
		    Debug.println("RESOURCESCONTROL boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("RESOURCESCONTROL already running!");
		}
		break;
	    case INIT_HALT :
		if (RESOURCESCONTROL_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("RESOURCESCONTROL halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcResourcesRequestsModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (RESOURCESREQUESTS_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting RESOURCESREQUESTS");
		    Environment.jpStatusBar.setAction("Loading ResourcesRequests");
		    RESOURCESREQUESTS_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*tResourcesRequestToolBar ttbResourcesRequests = new tResourcesRequestToolBar(_desktop);
		    ttbResourcesRequests.setFloatable(false);
		    _desktop.add(ttbResourcesRequests);*/
		    ExtendedInternalFrame newRequest = new ExtendedInternalFrame("Ver/crear\npedidos", IconTypes.administracion_de_partidas_presupuestarias);
		    newRequest.setCentralPanel(new ResourcesRequestsMain());
		    newRequest.setClosable(false);
		    newRequest.setDesktop(_desktop);
		    newRequest.setIcon(true);
		    ExtendedInternalFrame resourcesRequestAuth = new ExtendedInternalFrame("Autorizar\nPedidos de Recursos", IconTypes.requestauth);
		    resourcesRequestAuth.setCentralPanel(new ResourcesRequestsAuthMain());
		    resourcesRequestAuth.setClosable(false);
		    resourcesRequestAuth.setDesktop(_desktop);
		    resourcesRequestAuth.setIcon(true);
		    ExtendedInternalFrame purchasesOrderGenerateContainer = new ExtendedInternalFrame("Generar\nOrden de Compra", IconTypes.humanresources);
		    PurchaseOrderGenerateList purchaseOrderGenerateList = new PurchaseOrderGenerateList();
		    purchasesOrderGenerateContainer.setCentralPanel(purchaseOrderGenerateList);
		    purchasesOrderGenerateContainer.setClosable(false);
		    purchasesOrderGenerateContainer.setDesktop(_desktop);
		    purchasesOrderGenerateContainer.setIcon(true);
		    ExtendedInternalFrame purchasesOrders = new ExtendedInternalFrame("Órdenes de\nCompra existentes", IconTypes.humanresources);
		    PurchaseOrdersList purchaseOrdersList = new PurchaseOrdersList();
		    purchasesOrders.setCentralPanel(purchaseOrdersList);
		    purchasesOrders.setClosable(false);
		    purchasesOrders.setDesktop(_desktop);
		    purchasesOrders.setIcon(true);
		    ExtendedInternalFrame provisionOrderContainer = new ExtendedInternalFrame("Generar\nOrden de Provisión", IconTypes.humanresources);
		    ProvisionOrderMain provisionNoteMain = new ProvisionOrderMain();
		    provisionOrderContainer.setCentralPanel(provisionNoteMain);
		    provisionOrderContainer.setClosable(false);
		    provisionOrderContainer.setDesktop(_desktop);
		    provisionOrderContainer.setIcon(true);
                    /*
		    ExtendedInternalFrame simexMain = new ExtendedInternalFrame("Sistema Expedientes", IconTypes.humanresources);
		    principal_simex simex = new principal_simex();
		    simexMain.setCentralPanel(simex);
		    simexMain.setClosable(false);
		    simexMain.setDesktop(_desktop);
		    simexMain.setIcon(true);*/
                    
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    RESOURCESREQUESTS_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("ResourcesRequests loaded");
		    Debug.println("RESOURCESREQUESTS boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("RESOURCESREQUESTS already running!");
		}
		break;
	    case INIT_HALT :
		if (RESOURCESREQUESTS_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("RESOURCESCONTROL halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcCashFlowAdminModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (CASHFLOWADMIN_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting CASHFLOWADMIN");
		    Environment.jpStatusBar.setAction("Loading CashFlowAdmin");
		    CASHFLOWADMIN_MODULE_STATUS = STATUS_STARTING;
		    _desktop.setStartColor(allModulesStartColor[1]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*CashFlowToolBar tbCashFlowMgmt = new CashFlowToolBar(_desktop);
		    tbCashFlowMgmt.setFloatable(true);
		    tbCashFlowMgmt.setLocation(0, 300);
		    _desktop.add(tbCashFlowMgmt);*/
		    
		    ExtendedInternalFrame accounts = new ExtendedInternalFrame("Plan de Cuentas", IconTypes.administracion_de_partidas_presupuestarias);
		    AccountsMain accountsMain = new AccountsMain();
		    accounts.setClosable(false);
		    accounts.setCentralPanel(accountsMain);
		    accounts.setDesktop(_desktop);
		    accounts.setIcon(true);

		    /**
		    * BOTON PARA EDICIÓN DE MODELOS DE ASIENTOS CONTABLES
		    */
		    ExtendedInternalFrame modelsContainer = new ExtendedInternalFrame("Modelos de\nAsientos Contables", IconTypes.administracion_de_partidas_presupuestarias);
		    BookKeepingEntryModelsList modelsList = new BookKeepingEntryModelsList();
		    modelsContainer.setClosable(false);
		    modelsContainer.setCentralPanel(modelsList);
		    modelsContainer.setDesktop(_desktop);
		    modelsContainer.setIcon(true);
		     
		     /**
		      * BOTON PARA AGREGAR UN ASIENTO CONTABLE
		      * A TRAVÉS DE MODELOS
		      */
		    ExtendedInternalFrame BookKeepingEntryByModelContainer = new ExtendedInternalFrame("Nuevo Asiento\nmediante Modelos", IconTypes.administracion_de_partidas_presupuestarias);
		    BookKeepingEntryByModelMgmt BookKeepingEntryByModelMgmt = new BookKeepingEntryByModelMgmt();
		    BookKeepingEntryByModelContainer.setClosable(false);
		    BookKeepingEntryByModelContainer.setCentralPanel(BookKeepingEntryByModelMgmt);
		    BookKeepingEntryByModelContainer.setDesktop(_desktop);
		    BookKeepingEntryByModelContainer.setIcon(true);

		    /**
		     * BOTON PARA AGREGAR UNA NOTA DE INGRESO
		     * MODULO SOLO VALIDO PARA UN ADMINISTRADOR
		     */
		    ExtendedInternalFrame creditNotesAdminMgmtContainer = new ExtendedInternalFrame("Nueva Nota\nde Ingreso (Admin)", IconTypes.inbox_32x32);
		    CreditNotesAdmin creditNotesAdminMgmt = new CreditNotesAdmin();
		    creditNotesAdminMgmtContainer.setClosable(false);
		    creditNotesAdminMgmtContainer.setCentralPanel(creditNotesAdminMgmt);
		    creditNotesAdminMgmtContainer.setDesktop(_desktop);
		    creditNotesAdminMgmtContainer.setIcon(true);

		    /**
		    * BOTON PARA AGREGAR UNA NOTA DE EGRESO
		    * MODULO SOLO VALIDO PARA UN ADMINISTRADOR
		    */
		    ExtendedInternalFrame DebitNotesAdminContainer = new ExtendedInternalFrame("Nueva Nota\nde Egreso (Admin)", IconTypes.close_session_32x32);
		    DebitNotesAdmin DebitNotesAdmin = new DebitNotesAdmin();
		    DebitNotesAdminContainer.setClosable(false);
		    DebitNotesAdminContainer.setCentralPanel(DebitNotesAdmin);
		    DebitNotesAdminContainer.setDesktop(_desktop);
		    DebitNotesAdminContainer.setIcon(true);
                    
		    ExtendedInternalFrame cashMovements = new ExtendedInternalFrame("Notas de\nIngreso y Egreso", IconTypes.administracion_de_partidas_presupuestarias);
		    CashMovementsList cashMovementsList = new CashMovementsList();
		    cashMovements.setClosable(false);
		    cashMovements.setCentralPanel(cashMovementsList);
		    cashMovements.setDesktop(_desktop);
		    cashMovements.setIcon(true); 
                    
		    ExtendedInternalFrame bankAccounts = new ExtendedInternalFrame("Cuentas\nbancarias", IconTypes.administracion_de_partidas_presupuestarias);
		    BankAccountsList accountsList = new BankAccountsList();
		    bankAccounts.setClosable(false);
		    bankAccounts.setCentralPanel(accountsList);
		    bankAccounts.setDesktop(_desktop);
		    bankAccounts.setIcon(true);
		    ExtendedInternalFrame budgets = new ExtendedInternalFrame("Administración de\nPartidas\nPresupuestarias", IconTypes.administracion_de_partidas_presupuestarias);
		    BudgetList budgetList = new BudgetList();
		    budgets.setClosable(false);
		    budgets.setCentralPanel(budgetList);
		    budgets.setDesktop(_desktop);
		    budgets.setIcon(true);
		    ExtendedInternalFrame costsCentres = new ExtendedInternalFrame("Centros\nde Costos", IconTypes.administracion_de_partidas_presupuestarias);
		    CCList ccList = new CCList();
		    costsCentres.setClosable(false);
		    costsCentres.setCentralPanel(ccList);
		    costsCentres.setDesktop(_desktop);
		    costsCentres.setIcon(true);
		    ExtendedInternalFrame movementTypes = new ExtendedInternalFrame("Tipos de\nMovimientos de Fondos", IconTypes.administracion_de_partidas_presupuestarias);
		    CashMovementTypesTree cashMovementTypesTree = new CashMovementTypesTree();
		    movementTypes.setClosable(false);
		    movementTypes.setCentralPanel(cashMovementTypesTree);
		    movementTypes.setDesktop(_desktop);
		    movementTypes.setIcon(true);
		    /*ExtendedInternalFrame expenditures = new ExtendedInternalFrame("Tipos\nde gasto", IconTypes.administracion_de_partidas_presupuestarias);
		    ExpenditureAccountsTree expenditureAccountTree = new ExpenditureAccountsTree();
		    expenditures.setClosable(false);
		    expenditures.setCentralPanel(expenditureAccountTree);
		    expenditures.setDesktop(_desktop);
		    expenditures.setIcon(true);
		    */
		    
		    ExtendedInternalFrame vouchers = new ExtendedInternalFrame("Comprobantes", IconTypes.administracion_de_partidas_presupuestarias);
		    VouchersList voucherList = new VouchersList();
		    vouchers.setClosable(false);
		    vouchers.setCentralPanel(voucherList);
		    vouchers.setDesktop(_desktop);
		    vouchers.setIcon(true);
		    ExtendedInternalFrame invoicesB = new ExtendedInternalFrame("Registrar\nComprobante a Pagar", IconTypes.administracion_de_partidas_presupuestarias);
		    InvoiceTypeB invoiceBMgmt = new InvoiceTypeB();
		    invoicesB.setClosable(false);
		    invoicesB.setCentralPanel(invoiceBMgmt);
		    invoicesB.setDesktop(_desktop);
		    invoicesB.setIcon(true);
		    ExtendedInternalFrame vouchersToInvoice = new ExtendedInternalFrame("Facturar\nNotas de Recepción", IconTypes.administracion_de_partidas_presupuestarias);
		    VoucherToInvoiceMain voucherToInvoiceMain = new VoucherToInvoiceMain();
		    vouchersToInvoice.setClosable(false);
		    vouchersToInvoice.setCentralPanel(voucherToInvoiceMain);
		    vouchersToInvoice.setDesktop(_desktop);
		    vouchersToInvoice.setIcon(true);
		    ExtendedInternalFrame vouchersToProvisionOrder = new ExtendedInternalFrame("Facturar\nOrdenes de Provisión", IconTypes.administracion_de_partidas_presupuestarias);
		    VoucherToProvisionOrderMain voucherToProvisionOrderMain = new VoucherToProvisionOrderMain();
		    vouchersToProvisionOrder.setClosable(false);
		    vouchersToProvisionOrder.setCentralPanel(voucherToProvisionOrderMain);
		    vouchersToProvisionOrder.setDesktop(_desktop);
		    vouchersToProvisionOrder.setIcon(true);
                    /*
		    ExtendedInternalFrame bookKeepingEntryContainer = new ExtendedInternalFrame("Nuevo asiento", IconTypes.administracion_de_partidas_presupuestarias);
		    BookKeepingEntryMgmt bookKeepingEntryMgmt = new BookKeepingEntryMgmt();
		    bookKeepingEntryContainer.setClosable(false);
		    bookKeepingEntryContainer.setCentralPanel(bookKeepingEntryMgmt);
		    bookKeepingEntryContainer.setDesktop(_desktop);
		    bookKeepingEntryContainer.setIcon(true);*/
		    ExtendedInternalFrame bookKeepingEntryListContainer = new ExtendedInternalFrame("Asientos", IconTypes.administracion_de_partidas_presupuestarias);
		    BookKeepingEntryList bookKeepingEntryList = new BookKeepingEntryList();
		    bookKeepingEntryListContainer.setClosable(false);
		    bookKeepingEntryListContainer.setCentralPanel(bookKeepingEntryList);
		    bookKeepingEntryListContainer.setDesktop(_desktop);
		    bookKeepingEntryListContainer.setIcon(true);
		    ExtendedInternalFrame paymentOrderContainer = new ExtendedInternalFrame("Nueva Orden\nde Pago", IconTypes.administracion_de_partidas_presupuestarias);
		    PaymentOrderMgmt paymentsOrderMgmt = new PaymentOrderMgmt();
		    paymentOrderContainer.setClosable(false);
		    paymentOrderContainer.setCentralPanel(paymentsOrderMgmt);
		    paymentOrderContainer.setDesktop(_desktop);
		    paymentOrderContainer.setIcon(true);
		    ExtendedInternalFrame paymentOrdersContainer = new ExtendedInternalFrame("Órdenes\nde Pago", IconTypes.administracion_de_partidas_presupuestarias);
		    PaymentOrderList paymentOrderList = new PaymentOrderList();
		    paymentOrdersContainer.setClosable(false);
		    paymentOrdersContainer.setCentralPanel(paymentOrderList);
		    paymentOrdersContainer.setDesktop(_desktop);
		    paymentOrdersContainer.setIcon(true);
		    ExtendedInternalFrame journalContainer = new ExtendedInternalFrame("Libro Mayor", IconTypes.administracion_de_partidas_presupuestarias);
		    JournalList journalList = new JournalList();
		    journalContainer.setClosable(false);
		    journalContainer.setCentralPanel(journalList);
		    journalContainer.setDesktop(_desktop);
		    journalContainer.setIcon(true);
		    ExtendedInternalFrame AccountingAvailableAmountContainer = new ExtendedInternalFrame("Balance de\nsumas y saldos", IconTypes.administracion_de_partidas_presupuestarias);
		    AccountsAvailableAmountList accountingAvailableAmountList = new AccountsAvailableAmountList();
		    AccountingAvailableAmountContainer.setClosable(false);
		    AccountingAvailableAmountContainer.setCentralPanel(accountingAvailableAmountList);
		    AccountingAvailableAmountContainer.setDesktop(_desktop);
		    AccountingAvailableAmountContainer.setIcon(true);
                    
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    CASHFLOWADMIN_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("CashFlowAdmin loaded");
		    Debug.println("CASHFLOWADMIN boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("CASHFLOWADMIN already running!");
		}
		break;
	    case INIT_HALT :
		if (CASHFLOWADMIN_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("RESOURCESCONTROL halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcCashFlowClientModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (CASHFLOW_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting CASHFLOWCLIENT");
		    Environment.jpStatusBar.setAction("Loading CashFlowClient");
		    CASHFLOW_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.cashflow_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), new Color(16, 27, 58));
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*CashFlowClientToolBar tbCashFlowClient = new CashFlowClientToolBar(_desktop);
		    tbCashFlowClient.setFloatable(false);
		    _desktop.add(tbCashFlowClient);*/
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    CASHFLOW_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("CashFlowClient loaded");
		    Debug.println("CASHFLOWCLIENT boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("CASHFLOWCLIENT already running!");
		}
		break;
	    case INIT_HALT :
		if (CASHFLOW_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("RESOURCESCONTROL halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcChatModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (CHAT_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting CHAT");
		    Environment.jpStatusBar.setAction("Loading ChatWithYou");
		    CHAT_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.chat_32x32);
		    //Environment.mainTabbedPane.updateUI();
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*ChWYChatPanel chatPanel = new ChWYChatPanel(Environment.sessionUser, Environment.jpStatusBar);
		    chatPanel.setDesktop(_desktop);
		    chatPanel.show();*/
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    CHAT_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("ChatWithYou loaded");
		    Debug.println("CHAT boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("CHAT already running!");
		}
		break;
	    case INIT_HALT :
		if (CHAT_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("CHAT halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcStickyNotesModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (STICKYNOTES_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting STICKYNOTES");
		    Environment.jpStatusBar.setAction("Loading StickyNotes");
		    STICKYNOTES_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.stickynotes_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    DesktopButton btnNewStickyNote = new DesktopButton("Nueva nota");
		    btnNewStickyNote.setRolloverEnabled(true);
		    btnNewStickyNote.setIcon(IconTypes.stickynotes_32x32);
		    btnNewStickyNote.setRolloverIcon(IconTypes.stickynotes_ro_32x32);
		    btnNewStickyNote.addActionListener(new ActionListener() {

					     public void actionPerformed(ActionEvent e) {
						 StickyNote newStickyNote = new StickyNote();
						 newStickyNote.setDate(Environment.currentDate + " " + Environment.currentTime);
						 newStickyNote.setIcon(true);
						 newStickyNote.show();
					     }

					 }
		    );
		    _desktop.addButton(btnNewStickyNote);
		    CoordinatorStickyNote coordinatorStickyNote = new CoordinatorStickyNote();
		    coordinatorStickyNote.retrieveData();
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    STICKYNOTES_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("StickyNotes loaded");
		    Debug.println("STICKYNOTES boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("STICKYNOTES already running!");
		}
		break;
	    case INIT_HALT :
		if (STICKYNOTES_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("STICKYNOTES halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcCommandCenterModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (COMMANDCENTER_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting COMMAND CENTER");
		    Environment.jpStatusBar.setAction("Loading Command Center");
		    COMMANDCENTER_MODULE_STATUS = STATUS_STARTING;
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    try {
		        ResultSet cards = LibSQL.exQuery("SELECT idcard FROM org.cards ORDER BY idcard");
			while (cards.next()) {
			    BasicCard _card = new BasicCard(cards.getInt("idcard"));
			    //, PurchaseOrdersList.class.getResource("xml/purchaseOrdersListReport.xml"));
			    _card.setClosable(false);
			    _card.setDesktop(_desktop);
			    _card.show();
			    Environment.addUnfinishedTask(_card);
			}
		    } catch (Exception e) {
			Advisor.messageBox("Error fetching cards", "");
		    }
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    COMMANDCENTER_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Command Center loaded");
		    Debug.println("COMMAND CENTER boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("COMMAND CENTER already running!");
		}
		break;
	    case INIT_HALT :
		if (COMMANDCENTER_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("COMMAND CENTER halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void initUIManagerKeys() {
	/**
	 * LOOK AND FEEL
	 * */
	ToolTipManager.sharedInstance().setDismissDelay(5000);
	ToolTipManager.sharedInstance().setInitialDelay(0);
	try {
	    //Elijo el LookAndFeel segun el OS
	    /*if (System.getProperty("os.name").toLowerCase().contains("windows")) {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
	    } else {
	        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	    }*/
	    //Con este LookAndFeel en Windows no se ve como deberia ser
	    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    //Le meto siempre el MetalLookAndFeel y consigo que se vea igual en Windows y Linux :)
	    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (UnsupportedLookAndFeelException e) {
	    e.printStackTrace();
	} catch (InstantiationException e) {
	    e.printStackTrace();
	}
	/**
	 * TABBEDPANE
	 * */
	//Color del tab seleccionado
	UIManager.put("TabbedPane.selected", BasicConfig.TABBEDPANE_TAB_AREA_BACKGROUND);
	//Color del tab no seleccionado
	UIManager.put("TabbedPane.unselectedBackground", BasicConfig.TABBEDPANE_UNSELECTED_BACKGROUND);
	//Color del texto en todos los tabs, el BasicTabbedPane tiene un método
	//que le pone otro color al texto del tab seleccionado
	UIManager.put("TabbedPane.foreground", BasicConfig.TABBEDPANE_FOREGROUND);
	//Color del borde del tabbedpane, el que se dibuja entre el tab y el container, quizá
	//sea preferible poner "TabbedPane.contentOpaque", false
	UIManager.put("TabbedPane.contentOpaque", false);
	//UIManager.put("TabbedPane.contentAreaColor", BasicConfig.TABBEDPANE_TAB_AREA_BACKGROUND);
	//Este no da bola
	UIManager.put("TabbedPane.background", BasicConfig.TABBEDPANE_TAB_AREA_BACKGROUND);
	//Los tabs sobreescribirán el borde?
	//UIManager.put("TabbedPane.tabsOverlapBorder", false);
	//No se para qué sirve, pero le saco todos los espacios
	UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
	//Hago que el TAB se funda con el contenido
	UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, -2, 0, 0));
	//Este no da bola
	//UIManager.put("TabbedPane.selectedTabPadInsets", new Insets(20, 20, 20, 20));
	//Color de la luz del borde del tab seleccionado y su contenido
	//UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource(229,124,10));
	//No se para que sirve
	//UIManager.put("TabbedPane.tabAreaBackground", BasicConfig.TABBEDPANE_TAB_AREA_BACKGROUND);
	//Luz de abajo hacia arriba, solo se ve en las flechas
	//UIManager.put("TabbedPane.highlight", new ColorUIResource(0,0,0));
	//Luz de arriba hacia abajo
	//UIManager.put("TabbedPane.light", new ColorUIResource(0,0,0));
	//Sombra inferior derecha de las flechas y de sus botones
	//UIManager.put("TabbedPane.shadow", new ColorUIResource(229,124,10));
	//Color de las flechas, de los bordes de sus botonoes y de los bordes de los tabs no seleccionados
	//UIManager.put("TabbedPane.darkShadow", new ColorUIResource(229,124,10));
	//Color del foco del tab
	//UIManager.put("TabbedPane.focus", new ColorUIResource(229,124,10));
	//Color del borde superior izquierdo del tab seleccionado
	//UIManager.put("TabbedPane.selectHighlight", new ColorUIResource(229,124,10));
	/**
	 * TOOLBAR
	 * */
	//Color del fondo de la barra de herramientas y del panelcito para hacerla flotante
	UIManager.put("ToolBar.background", BasicConfig.RESALTADOR_COLOR_UIRESOURCE);
	//Color del fondo de la barra de herramientas cuando esta lista para acoplamiento
	//UIManager.put("ToolBar.dockingBackground", new ColorUIResource(229,124,10));
	//Color del fondo de la barra de herramientas cuando esta siendo arrastrada
	//UIManager.put("ToolBar.floatingBackground", new ColorUIResource(229,124,10));
	/**
	  * MENUBAR
	  * */
	//Este no da bola
	UIManager.put("Menu.background", BasicConfig.RESALTADOR_COLOR_UIRESOURCE);
	//Color de fondo del menuitem (el que está enganchado con un JMenu)
	UIManager.put("MenuItem.background", BasicConfig.RESALTADOR_COLOR_UIRESOURCE);
	//Color de fondo del menuitem cuando está seleccionado (el que está enganchado con un JMenu)
	UIManager.put("MenuItem.selectionBackground", BasicConfig.RESALTADOR_COLOR_UIRESOURCE);
	//Este no da bola
	UIManager.put("Menu.margin", new InsetsUIResource(0, 0, 0, 0));
	//Este no da bola
	UIManager.put("MenuItem.margin", new InsetsUIResource(0, 0, 0, 0));
	/**
	  * OPTIONPANE
	  * */
	/*//Fondo del JOptionPane
	UIManager.put("OptionPane.background", new ColorUIResource(new Color(229, 124, 10)));
	//Fondo del Panel por defecto
	UIManager.put("Panel.background", new ColorUIResource(new Color(229, 124, 10)));
	ArrayList<Object> gradients = new ArrayList<Object>(5);
	gradients.add(0.28f);
	gradients.add(0.00f);
	gradients.add(new Color(0x33FF00));
	gradients.add(new Color(0x33CC00));
	gradients.add(new Color(0x339900));
	UIManager.put("Button.gradient", gradients);*/
	//JOptionPane.showOptionDialog(null, "PRUEBA!", "PRUEBA!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	/**
	 * ComboBox
	 * */
	UIManager.put("ComboBox.disabledForeground", BasicConfig.LABEL_FOREGROUND_COLOR);
	//BasicConfig.LABEL_FOREGROUND_COLOR ) ;
	UIManager.put("ComboBox.disabledBackground", BasicConfig.PANEL_BACKGROUND_COLOR);
	//BasicConfig.LABEL_FOREGROUND_COLOR ) ;
	/**
	  * ToolTips
	  * */
	UIManager.put("ToolTip.background", new ColorUIResource(51, 51, 51));
	UIManager.put("ToolTip.foreground", new ColorUIResource(255, 132, 0));
	UIManager.put("ToolTip.font", new FontUIResource("Dialog", FontUIResource.BOLD, 12));
	/**
	 * SCROLL BAR
	 * */
	ArrayList<Object> gradients = new ArrayList<Object>(5);
	gradients.add(0.28f);
	gradients.add(0.00f);
	gradients.add(BasicConfig.PANEL_GRADIENT_START_COLOR);
	gradients.add(BasicConfig.PANEL_BACKGROUND_COLOR);
	gradients.add(BasicConfig.TABBEDPANE_UNSELECTED_BACKGROUND);
	UIManager.put("ScrollBar.gradient", gradients);
    }

    public static void closeApplication() {
	int answer = JOptionPane.showConfirmDialog(Environment.mainDesktop, "¿Desea cerrar el sistema?", "Cerrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	if (answer == JOptionPane.YES_OPTION) {
	    //AVISAR A LOS MODULOS QUE SE VA A CERRAR TODO!!!
	    /**
	     * GRABAR OPCIONES
	     * */
	    Environment.cfg.setProperty("SelectedTab", String.valueOf(Environment.mainTabbedPane.getSelectedIndex()));
	    System.exit(0);
	}
    }

    public static void initGraphics() {
	Environment.organization = "DIGITALL S.H.";
	BasicConfig.PANELCONTAINER_BACKGROUND_COLOR = new Color(184, 184, 184);
	BasicConfig.PANEL_GRADIENT_START_COLOR = new Color(225, 225, 225);
	BasicConfig.PANEL_GRADIENT_END_COLOR = new Color(184, 184, 184);
	BasicConfig.LABEL_BACKGROUND_COLOR = new Color(115, 115, 115);
	BasicConfig.PANEL_BACKGROUND_COLOR = new Color(115, 115, 115);
	BasicConfig.INTERNALFRAME_NORTH_PANE_GRADIENT_START_COLOR = new Color(225, 225, 225);
	BasicConfig.INTERNALFRAME_NORTH_PANE_GRADIENT_END_COLOR = new Color(184, 184, 184);
	BasicConfig.GENERALBUTTONS_CONTAINER_GRADIENT_END_COLOR = new Color(184, 184, 184);
	BasicConfig.GENERALBUTTONS_CONTAINER_GRADIENT_START_COLOR = new Color(225, 225, 225);
	BasicConfig.GENERALBUTTONS_BORDER_COLOR = new Color(96, 96, 96);
	BasicConfig.TABBEDPANE_UNSELECTED_BACKGROUND = new ColorUIResource(66, 66, 66);
	BasicConfig.TABBEDPANE_FOREGROUND = new ColorUIResource(255, 255, 255);
	BasicConfig.TABBEDPANE_TAB_AREA_BACKGROUND = new ColorUIResource(115, 115, 115);
	BasicConfig.TITLELABEL_FOREGROUND_COLOR = Color.BLACK;
	initUIManagerKeys();
    }

}

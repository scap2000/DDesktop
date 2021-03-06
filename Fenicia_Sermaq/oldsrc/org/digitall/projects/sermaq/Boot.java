package org.digitall.projects.sermaq;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.InsetsUIResource;

import org.digitall.apps.calendar.interfaces.CoordinatorStickyNote;
import org.digitall.apps.calendar.interfaces.NewsList;
import org.digitall.apps.calendar.interfaces.StickyNote;
import org.digitall.common.cashflow.classes.EntityTypes;
import org.digitall.common.cashflow.interfaces.CompanyTreePanel;
import org.digitall.common.cashflow.interfaces.accounting.AccountsList;
import org.digitall.common.cashflow.interfaces.account.PaymentsOrdersMain;
import org.digitall.common.cashflow.interfaces.budget.BudgetList;
import org.digitall.common.cashflow.interfaces.cashmovement.CashMovementTypesTree;
import org.digitall.common.cashflow.interfaces.cashmovement.CashMovementsList;
import org.digitall.common.cashflow.interfaces.costscentre.CCList;
import org.digitall.apps.cashflow.interfaces.expendituretype.ExpenditureTypeTree;
import org.digitall.apps.cashflow.interfaces.voucher.VoucherToInvoiceMain;
import org.digitall.apps.cashflow.interfaces.voucher.VouchersList;
import org.digitall.apps.digitalk.ChWYChatPanel;
import org.digitall.apps.logistics.interfaces.CostCentreBasket;
import org.digitall.apps.logistics.interfaces.DNDCoordinator;
import org.digitall.apps.logistics.interfaces.HumanResourcesBasket;
import org.digitall.apps.logistics.interfaces.ResourcesBasket;
import org.digitall.apps.mapper.CoordinateCalculator;
import org.digitall.apps.projecttask.interfaces.ProjectsTree;
import org.digitall.apps.projecttask.interfaces.UserList;
import org.digitall.common.resourcescontrol.interfaces.PersonsList;
import org.digitall.common.resourcescontrol.interfaces.ResourcesList;
import org.digitall.common.resourcescontrol.interfaces.SkillList;
import org.digitall.apps.resourcescontrol.interfaces.resourcesadmin.ResourcesAdminMain;
import org.digitall.common.resourcesrequests.interfaces.purchaseorder.PurchaseOrderGenerateMain;
import org.digitall.apps.resourcesrequests.interfaces.purchaseorder.PurchaseOrdersMain;
import org.digitall.apps.resourcesrequests.interfaces.resourcesmovements.DespatchNotesMain;
import org.digitall.apps.resourcesrequests.interfaces.resourcesmovements.ResourcesDeliverMain;
import org.digitall.common.resourcesrequests.interfaces.resourcesmovements.ResourcesReceiveMain;
import org.digitall.common.resourcesrequests.interfaces.resourcesrequests.ResourcesRequestsAuthMain;
import org.digitall.apps.resourcesrequests.interfaces.resourcesrequests.ResourcesRequestsMain;
import org.digitall.common.systemmanager.ChangePassword;
import org.digitall.lib.components.basic.BasicConfig;
import org.digitall.lib.components.basic.BasicDesktop;
import org.digitall.lib.components.basic.DesktopButton;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.UnAssignButton;
import org.digitall.common.data.PanelDictionary;
import org.digitall.lib.environment.Debug;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.html.BrowserLauncher;
import org.digitall.lib.icons.IconTypes;

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
    public static int TASKS_MODULE_STATUS = -1;
    private static Color allModulesEndColor = new Color(30, 30, 30);
    private static Color allModulesStartColor[] = { new Color(17, 27, 56), new Color(44, 78, 28), new Color(66, 17, 60), new Color(90, 18, 18), new Color(180, 91, 23), new Color(131, 110, 11), new Color(81, 72, 42) };

    public static void rcMainModule(int _action, BasicDesktop _desktop) {
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (MAIN_MODULE_STATUS != STATUS_RUNNING) {
		    /*Boot.rcCashFlowClientModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_CASHFLOW));
		    Boot.rcNewsModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_NEWS));
		    Boot.rcChatModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_CHAT));*/
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
					   BrowserLauncher.openURL("http://www.sermaqos.com.ar");
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
		    _desktop.addButton(btnTest);
		    /**
		     * DICCIONARIO DEL SISTEMA
		     * */
		    ExtendedInternalFrame dictionary = new ExtendedInternalFrame("Dictionary");
		    PanelDictionary panelDictionary = new PanelDictionary();
		    dictionary.setCentralPanel(panelDictionary);
		    dictionary.setClosable(false);
		    dictionary.setDesktop(_desktop);
		    dictionary.setIcon(true);
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
					                if (Advisor.question(this, "Cerrar Sesion", "??Esta seguro que desea cerrar su sesion?")) {
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
		    _desktop.addButton(btnCloseSession);
		    /**
		     * Instancio la fabulosa calculadora de coordenadas
		     * */
		    ExtendedInternalFrame geoCalc = new ExtendedInternalFrame("Calculadora geografica", IconTypes.geocalc_32x32);
		    CoordinateCalculator geoCalcPanel = new CoordinateCalculator();
		    geoCalc.setClosable(false);
		    geoCalc.setCentralPanel(geoCalcPanel);
		    geoCalc.setDesktop(_desktop);
		    geoCalc.setIcon(true);
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
		    /**
		     * SYSTEM MAP CONFIGURATION
		     * */
		    Environment.mainTabbedPane.setIconAt(Environment.getDesktopIndex(Environment.MODULE_SYSTEMMAP), IconTypes.system_map);
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
    }

    public static void rcLogisticsModule(int _action, BasicDesktop _desktop) {
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (LOGISTICS_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting LOGISTICS");
		    Environment.jpStatusBar.setAction("Loading Logistics");
		    LOGISTICS_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.logistics_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    UnAssignButton assignResource = new UnAssignButton(true);
		    UnAssignButton assignHumanResource = new UnAssignButton(true);
		    assignResource.setLocation(350 - (assignResource.getWidth() / 2), 350);
		    assignHumanResource.setLocation(750 - (assignHumanResource.getWidth() / 2), 350);
		    assignResource.addActionListener(new ActionListener() {

					   public void actionPerformed(ActionEvent e) {
					       DNDCoordinator.setDragSourceType(EntityTypes.RESOURCE);
					       DNDCoordinator.completeDrop(EntityTypes.COSTS_CENTRE);
					   }

				       }
		    );
		    assignHumanResource.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
						    DNDCoordinator.setDragSourceType(EntityTypes.PERSON);
						    DNDCoordinator.completeDrop(EntityTypes.COSTS_CENTRE);
						}

					    }
		    );
		    CostCentreBasket ccBasket = new CostCentreBasket();
		    ccBasket.setDesktop(_desktop);
		    ResourcesBasket rBasket = new ResourcesBasket();
		    rBasket.setDesktop(_desktop);
		    HumanResourcesBasket hrBasket = new HumanResourcesBasket();
		    hrBasket.setDesktop(_desktop);
		    _desktop.add(assignResource);
		    _desktop.add(assignHumanResource);
		    _desktop.add(ccBasket);
		    _desktop.add(rBasket);
		    _desktop.add(hrBasket);
		    ccBasket.setLocation(500, 200);
		    rBasket.setLocation(300, 400);
		    hrBasket.setLocation(700, 400);
		    ccBasket.show();
		    rBasket.show();
		    hrBasket.show();
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    LOGISTICS_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Logistics loaded");
		    Debug.println("LOGISTICS boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("LOGISTICS already running!");
		}
		break;
	    case INIT_HALT :
		if (LOGISTICS_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("LOGISTICS halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
    }

    public static void rcResourcesControlModule(int _action, BasicDesktop _desktop) {
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (RESOURCESCONTROL_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting RESOURCESCONTROL");
		    Environment.jpStatusBar.setAction("Loading ResourcesControl");
		    RESOURCESCONTROL_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.resources_admin_32x32);
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
		    ExtendedInternalFrame despatchNotes = new ExtendedInternalFrame("Listado de Remitos\nInternos y/o Externos", IconTypes.requestauth);
		    DespatchNotesMain despatchNotesMain = new DespatchNotesMain();
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
		    ResourcesDeliverMain internalDespatchNote = new ResourcesDeliverMain();
		    internalDespatchNotes.setCentralPanel(internalDespatchNote);
		    internalDespatchNotes.setClosable(false);
		    internalDespatchNotes.setDesktop(_desktop);
		    internalDespatchNotes.setIcon(true);
		    ExtendedInternalFrame resourcesAdmin = new ExtendedInternalFrame("Administrador\nde Recursos", IconTypes.materialresources);
		    ResourcesAdminMain resourcesAdminMain = new ResourcesAdminMain();
		    resourcesAdmin.setCentralPanel(resourcesAdminMain);
		    resourcesAdmin.setClosable(false);
		    resourcesAdmin.setDesktop(_desktop);
		    resourcesAdmin.setIcon(true);
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
		    CompanyTreePanel providerList = new CompanyTreePanel();
		    providers.setCentralPanel(providerList);
		    providers.setClosable(false);
		    providers.setDesktop(_desktop);
		    providers.setIcon(true);
		    ExtendedInternalFrame skills = new ExtendedInternalFrame("Gestion\nde Habilidades", IconTypes.skills);
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
    }

    public static void rcResourcesRequestsModule(int _action, BasicDesktop _desktop) {
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (RESOURCESREQUESTS_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting RESOURCESREQUESTS");
		    Environment.jpStatusBar.setAction("Loading ResourcesRequests");
		    RESOURCESREQUESTS_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.resources_requests_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*tResourcesRequestToolBar ttbResourcesRequests = new tResourcesRequestToolBar(_desktop);
		    ttbResourcesRequests.setFloatable(false);
		    _desktop.add(ttbResourcesRequests);*/
		    ExtendedInternalFrame newRequest = new ExtendedInternalFrame("Ver/crear\npedidos", IconTypes.budgets);
		    newRequest.setCentralPanel(new ResourcesRequestsMain());
		    newRequest.setClosable(false);
		    newRequest.setDesktop(_desktop);
		    newRequest.setIcon(true);
		    ExtendedInternalFrame resourcesRequestAuth = new ExtendedInternalFrame("Autorizar\npedidos", IconTypes.requestauth);
		    resourcesRequestAuth.setCentralPanel(new ResourcesRequestsAuthMain());
		    resourcesRequestAuth.setClosable(false);
		    resourcesRequestAuth.setDesktop(_desktop);
		    resourcesRequestAuth.setIcon(true);
		    ExtendedInternalFrame purchasesOrderGenerateContainer = new ExtendedInternalFrame("Generar\nOrden de Compra", IconTypes.humanresources);
		    PurchaseOrderGenerateMain purchaseOrderGenerateMain = new PurchaseOrderGenerateMain();
		    purchasesOrderGenerateContainer.setCentralPanel(purchaseOrderGenerateMain);
		    purchasesOrderGenerateContainer.setClosable(false);
		    purchasesOrderGenerateContainer.setDesktop(_desktop);
		    purchasesOrderGenerateContainer.setIcon(true);
		    ExtendedInternalFrame purchasesOrders = new ExtendedInternalFrame("Ordenes\nde Compras", IconTypes.humanresources);
		    PurchaseOrdersMain purchaseOrdersMain = new PurchaseOrdersMain();
		    purchasesOrders.setCentralPanel(purchaseOrdersMain);
		    purchasesOrders.setClosable(false);
		    purchasesOrders.setDesktop(_desktop);
		    purchasesOrders.setIcon(true);
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
    }

    public static void rcCashFlowAdminModule(int _action, BasicDesktop _desktop) {
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (CASHFLOWADMIN_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting CASHFLOWADMIN");
		    Environment.jpStatusBar.setAction("Loading CashFlowAdmin");
		    CASHFLOWADMIN_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.cashflow_admin_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*CashFlowToolBar tbCashFlowMgmt = new CashFlowToolBar(_desktop);
		    tbCashFlowMgmt.setFloatable(true);
		    tbCashFlowMgmt.setLocation(0, 300);
		    _desktop.add(tbCashFlowMgmt);*/
		    ExtendedInternalFrame bankAccounts = new ExtendedInternalFrame("Cuentas\nbancarias", IconTypes.budgets);
		    AccountsList accountsList = new AccountsList();
		    bankAccounts.setClosable(false);
		    bankAccounts.setCentralPanel(accountsList);
		    bankAccounts.setDesktop(_desktop);
		    bankAccounts.setIcon(true);
		    ExtendedInternalFrame budgets = new ExtendedInternalFrame("Administracion de\nPartidas\npresupuestarias", IconTypes.budgets);
		    BudgetList budgetList = new BudgetList();
		    budgets.setClosable(false);
		    budgets.setCentralPanel(budgetList);
		    budgets.setDesktop(_desktop);
		    budgets.setIcon(true);
		    ExtendedInternalFrame expenditures = new ExtendedInternalFrame("Tipos\nde gasto", IconTypes.budgets);
		    ExpenditureTypeTree expenditureTypeTree = new ExpenditureTypeTree();
		    expenditures.setClosable(false);
		    expenditures.setCentralPanel(expenditureTypeTree);
		    expenditures.setDesktop(_desktop);
		    expenditures.setIcon(true);
		    ExtendedInternalFrame costsCentres = new ExtendedInternalFrame("Centros\nde Costos", IconTypes.costscentres);
		    CCList ccList = new CCList();
		    costsCentres.setClosable(false);
		    costsCentres.setCentralPanel(ccList);
		    costsCentres.setDesktop(_desktop);
		    costsCentres.setIcon(true);
		    ExtendedInternalFrame movementTypes = new ExtendedInternalFrame("Tipos de\nMovimientos de Fondos", IconTypes.costscentres);
		    CashMovementTypesTree cashMovementTypesTree = new CashMovementTypesTree();
		    movementTypes.setClosable(false);
		    movementTypes.setCentralPanel(cashMovementTypesTree);
		    movementTypes.setDesktop(_desktop);
		    movementTypes.setIcon(true);
		    ExtendedInternalFrame cashMovements = new ExtendedInternalFrame("Movimientos\nde Fondos", IconTypes.costscentres);
		    CashMovementsList cashMovementsList = new CashMovementsList();
		    cashMovements.setClosable(false);
		    cashMovements.setCentralPanel(cashMovementsList);
		    cashMovements.setDesktop(_desktop);
		    cashMovements.setIcon(true);
		    ExtendedInternalFrame vouchers = new ExtendedInternalFrame("Comprobantes", IconTypes.costscentres);
		    VouchersList voucherList = new VouchersList();
		    vouchers.setClosable(false);
		    vouchers.setCentralPanel(voucherList);
		    vouchers.setDesktop(_desktop);
		    vouchers.setIcon(true);
		    ExtendedInternalFrame vouchersToInvoice = new ExtendedInternalFrame("Registrar\nFacturas Externas", IconTypes.costscentres);
		    VoucherToInvoiceMain voucherToInvoiceMain = new VoucherToInvoiceMain();
		    vouchersToInvoice.setClosable(false);
		    vouchersToInvoice.setCentralPanel(voucherToInvoiceMain);
		    vouchersToInvoice.setDesktop(_desktop);
		    vouchersToInvoice.setIcon(true);
		    ExtendedInternalFrame paymentOrders = new ExtendedInternalFrame("??rdenes\nde Pagos", IconTypes.costscentres);
		    PaymentsOrdersMain paymentsOrdersMain = new PaymentsOrdersMain();
		    paymentOrders.setClosable(false);
		    paymentOrders.setCentralPanel(paymentsOrdersMain);
		    paymentOrders.setDesktop(_desktop);
		    paymentOrders.setIcon(true);
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
    }

    public static void rcCashFlowClientModule(int _action, BasicDesktop _desktop) {
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (CASHFLOW_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting CASHFLOWCLIENT");
		    Environment.jpStatusBar.setAction("Loading CashFlowClient");
		    CASHFLOW_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.cashflow_client_32x32);
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
    }

    public static void rcChatModule(int _action, BasicDesktop _desktop) {
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
		    ChWYChatPanel chatPanel = new ChWYChatPanel(Environment.sessionUser, Environment.jpStatusBar);
		    chatPanel.setDesktop(_desktop);
		    chatPanel.show();
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
    }

    public static void rcStickyNotesModule(int _action, BasicDesktop _desktop) {
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
		    DesktopButton btnNewStickyNote = new DesktopButton("Nueva Anotaci??n");
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
    }

    public static void rcTasksModule(int _action, BasicDesktop _desktop) {
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (TASKS_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting TASKS");
		    Environment.jpStatusBar.setAction("Loading Tasks");
		    TASKS_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.tasks_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*ProjectToolBar tbProjectTask = new ProjectToolBar(_desktop);
		    tbProjectTask.setFloatable(false);
		    _desktop.add(tbProjectTask);
		    */
		    ExtendedInternalFrame projects = new ExtendedInternalFrame("Proyectos", IconTypes.providers);
		    ProjectsTree projectsTree = new ProjectsTree();
		    projects.setCentralPanel(projectsTree);
		    projects.setClosable(false);
		    projects.setDesktop(_desktop);
		    projects.setIcon(true);
		    ExtendedInternalFrame tasks = new ExtendedInternalFrame("Tareas", IconTypes.providers);
		    UserList userList = new UserList();
		    tasks.setCentralPanel(userList);
		    tasks.setClosable(false);
		    tasks.setDesktop(_desktop);
		    tasks.setIcon(true);
		    /*ExtendedInternalFrame tasksReports = new ExtendedInternalFrame("Reportes", IconTypes.providers);
		    ReportByUser reportByUser =  new ReportByUser();
		    tasksReports.setCentralPanel(reportByUser);
		    tasksReports.setClosable(false);
		    tasksReports.setDesktop(_desktop);
		    tasksReports.setIcon(true);*/
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    TASKS_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Tasks loaded");
		    Debug.println("TASKS boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("TASKS already running!");
		}
		break;
	    case INIT_HALT :
		if (TASKS_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("TASKS halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
    }

    public static void initUIManagerKeys() {
	/**
	 * LOOK AND FEEL
	 * */
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
	//UIManager.put("TabbedPane.selected", new ColorUIResource(55, 55, 55));
	//Color del tab no seleccionado
	UIManager.put("TabbedPane.unselectedBackground", BasicConfig.TABBEDPANE_UNSELECTED_BACKGROUND);
	//Color del texto en todos los tabs, el BasicTabbedPane tiene un m??todo
	//que le pone otro color al texto del tab seleccionado
	UIManager.put("TabbedPane.foreground", BasicConfig.TABBEDPANE_FOREGROUND);
	//Color del borde del tabbedpane, el que se dibuja entre el tab y el container, quiz??
	//sea preferible poner "TabbedPane.contentOpaque", false
	UIManager.put("TabbedPane.contentOpaque", false);
	//UIManager.put("TabbedPane.contentAreaColor", new ColorUIResource(229,124,10));
	//Este no da bola
	//UIManager.put("TabbedPane.background", new ColorUIResource(229,124,10));
	//Los tabs sobreescribir??n el borde?
	//UIManager.put("TabbedPane.tabsOverlapBorder", false);
	//No se para qu?? sirve, pero le saco todos los espacios
	UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
	//Hago que el TAB se funda con el contenido
	UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, -2, 0, 0));
	//Este no da bola
	//UIManager.put("TabbedPane.selectedTabPadInsets", new Insets(20, 20, 20, 20));
	//Color de la luz del borde del tab seleccionado y su contenido
	//UIManager.put("TabbedPane.borderHightlightColor", new ColorUIResource(229,124,10));
	//No se para que sirve
	UIManager.put("TabbedPane.tabAreaBackground", BasicConfig.TABBEDPANE_TAB_AREA_BACKGROUND);
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
	//Color de fondo del menuitem (el que est?? enganchado con un JMenu)
	UIManager.put("MenuItem.background", BasicConfig.RESALTADOR_COLOR_UIRESOURCE);
	//Color de fondo del menuitem cuando est?? seleccionado (el que est?? enganchado con un JMenu)
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
    }

    public static void closeApplication() {
	int answer = JOptionPane.showConfirmDialog(Environment.mainDesktop, "??Desea cerrar el sistema?", "Cerrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	if (answer == JOptionPane.YES_OPTION) {
	    //AVISAR A LOS MODULOS QUE SE VA A CERRAR TODO!!!
	    /**
	     * GRABAR OPCIONES
	     * */
	    Environment.cfg.setProperty("SelectedTab", String.valueOf(Environment.mainTabbedPane.getSelectedIndex()));
	    System.exit(0);
	}
    }


}

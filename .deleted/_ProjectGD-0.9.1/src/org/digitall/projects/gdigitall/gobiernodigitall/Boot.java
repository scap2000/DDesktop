package org.digitall.projects.gdigitall.gobiernodigitall;

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
import org.digitall.apps.calendar.interfaces.StickyNote;
import org.digitall.apps.cashflow.interfaces.accounting.AccountsAvailableAmountList;
import org.digitall.apps.cashflow.interfaces.accounting.JournalList;
import org.digitall.apps.cashflow.interfaces.accountingentry.BookKeepingEntryByModelMgmt;
import org.digitall.apps.cashflow.interfaces.accountingentry.BookKeepingEntryList;
import org.digitall.apps.cashflow.interfaces.accountingentry.BookKeepingEntryModelsList;
import org.digitall.apps.cashflow.interfaces.accountingentry.CreditNotesMgmt;
import org.digitall.apps.cashflow.interfaces.accountingentry.DebitNotesMgmt;
import org.digitall.apps.cashflow.interfaces.paymentorder.PaymentOrderList;
import org.digitall.apps.cashflow.interfaces.paymentorder.PaymentOrderMgmt;
import org.digitall.apps.cashflow.interfaces.voucher.InvoiceTypeB;
import org.digitall.apps.cashflow.interfaces.voucher.PagosDeTercerosMain;
import org.digitall.apps.cashflow.interfaces.voucher.VoucherToInvoiceMain;
import org.digitall.apps.cashflow.interfaces.voucher.VoucherToProvisionOrderMain;
import org.digitall.common.cashflow.interfaces.account.BankAccountsList;
import org.digitall.common.cashflow.interfaces.accounting.AccountsMain;
import org.digitall.common.cashflow.interfaces.budget.BudgetList;
import org.digitall.common.cashflow.interfaces.cashmovement.CashMovementTypesTree;
import org.digitall.common.cashflow.interfaces.cashmovement.CashMovementsList;
import org.digitall.common.cashflow.interfaces.costscentre.CCList;
import org.digitall.common.cashflow.interfaces.voucher.VouchersList;
import org.digitall.common.components.basic.BasicCard;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicConfig;
import org.digitall.lib.components.basic.BasicDesktop;
import org.digitall.lib.components.basic.DesktopButton;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.environment.Debug;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;

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
    public static int ASSETS_MODULE = 1;
    public static int CASHFLOW_MODULE = 2;
    public static int GAIA_MODULE = 3;
    public static int REPORTS_MODULE = 4;
    public static int RESOURCES_MODULE = 5;
    public static int STICKYNOTES_MODULE = 6;
    public static int TAXES_MODULE = 7;
    /**
     * Estado de los modulos
     * */
    public static int MAIN_MODULE_STATUS = -1;
    public static int ASSETS_MODULE_STATUS = -1;
    public static int CASHFLOW_MODULE_STATUS = -1;
    public static int GAIA_MODULE_STATUS = -1;
    public static int REPORTS_MODULE_STATUS = -1;
    public static int RESOURCES_MODULE_STATUS = -1;
    public static int STICKYNOTES_MODULE_STATUS = -1;
    public static int TAXES_MODULE_STATUS = -1;
    private static Color allModulesEndColor = new Color(30, 30, 30);
    private static Color allModulesStartColor[] = { new Color(17, 27, 56), new Color(44, 78, 28), new Color(66, 17, 60), new Color(90, 18, 18), new Color(180, 91, 23), new Color(131, 110, 11), new Color(81, 72, 42) };

    public static void rcAssets(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (ASSETS_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting ASSETS");
		    Environment.jpStatusBar.setAction("Loading Assets");
		    ASSETS_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.resources_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */

		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    ASSETS_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Assets loaded");
		    Debug.println("ASSETS boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("ASSETS already running!");
		}
		break;
	    case INIT_HALT :
		if (ASSETS_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("ASSETS halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcCashFlowModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (CASHFLOW_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting CASHFLOW");
		    Environment.jpStatusBar.setAction("Loading CashFlow");
		    CASHFLOW_MODULE_STATUS = STATUS_STARTING;
		    _desktop.setStartColor(allModulesStartColor[1]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    /*CashFlowToolBar tbCashFlowMgmt = new CashFlowToolBar(_desktop);
		    tbCashFlowMgmt.setFloatable(true);
		    tbCashFlowMgmt.setLocation(0, 300);
		    _desktop.add(tbCashFlowMgmt);*/
		    new ExtendedInternalFrame("Plan de Cuentas", IconTypes.administracion_de_partidas_presupuestarias, AccountsMain.class, true, _desktop);
		    /**
		    * BOTON PARA EDICIÓN DE MODELOS DE ASIENTOS CONTABLES
		    */
		    new ExtendedInternalFrame("Modelos de\nAsientos Contables", IconTypes.administracion_de_partidas_presupuestarias, BookKeepingEntryModelsList.class, true, _desktop);
		    /**
		      * BOTON PARA AGREGAR UN ASIENTO CONTABLE
		      * A TRAVÉS DE MODELOS
		      */
		    new ExtendedInternalFrame("Nuevo Asiento\nmediante Modelos", IconTypes.administracion_de_partidas_presupuestarias, BookKeepingEntryByModelMgmt.class, true, _desktop);
		    /**
		     * BOTON PARA AGREGAR UNA NOTA DE INGRESO
		     * MODULO SOLO VALIDO PARA UN ADMINISTRADOR
		     */
		    /*
		    ExtendedInternalFrame creditNotesAdminMgmtContainer = new ExtendedInternalFrame("Nueva Nota\nde Ingreso (Admin)", IconTypes.budgets);
		    CreditNotesAdmin creditNotesAdminMgmt = new CreditNotesAdmin();
		    creditNotesAdminMgmtContainer.setClosable(false);
		    creditNotesAdminMgmtContainer.setCentralPanel(creditNotesAdminMgmt);
		    creditNotesAdminMgmtContainer.setDesktop(_desktop);
		    creditNotesAdminMgmtContainer.setIcon(true);
		    */
		    /**
		    * BOTON PARA AGREGAR UNA NOTA DE EGRESO
		    * MODULO SOLO VALIDO PARA UN ADMINISTRADOR
		    */
		    /*
		    ExtendedInternalFrame DebitNotesAdminContainer = new ExtendedInternalFrame("Nueva Nota\nde Egreso (Admin)", IconTypes.budgets);
		    DebitNotesAdmin DebitNotesAdmin = new DebitNotesAdmin();
		    DebitNotesAdminContainer.setClosable(false);
		    DebitNotesAdminContainer.setCentralPanel(DebitNotesAdmin);
		    DebitNotesAdminContainer.setDesktop(_desktop);
		    DebitNotesAdminContainer.setIcon(true);
		    */
		    new ExtendedInternalFrame("Notas de\nIngreso y Egreso", IconTypes.administracion_de_partidas_presupuestarias, CashMovementsList.class, true, _desktop);
		    new ExtendedInternalFrame("Cuentas\nbancarias", IconTypes.administracion_de_partidas_presupuestarias, BankAccountsList.class, true, _desktop);
		    new ExtendedInternalFrame("Administración de\nPartidas\nPresupuestarias", IconTypes.administracion_de_partidas_presupuestarias, BudgetList.class, true, _desktop);
		    new ExtendedInternalFrame("Centros\nde Costos", IconTypes.administracion_de_partidas_presupuestarias, CCList.class, true, _desktop);
		    new ExtendedInternalFrame("Tipos de\nMovimientos de Fondos", IconTypes.administracion_de_partidas_presupuestarias, CashMovementTypesTree.class, true, _desktop);

		    /*ExtendedInternalFrame expenditures = new ExtendedInternalFrame("Tipos\nde gasto", IconTypes.budgets);
		    ExpenditureAccountsTree expenditureAccountTree = new ExpenditureAccountsTree();
		    expenditures.setClosable(false);
		    expenditures.setCentralPanel(expenditureAccountTree);
		    expenditures.setDesktop(_desktop);
		    expenditures.setIcon(true);
		    */
		    new ExtendedInternalFrame("Comprobantes", IconTypes.administracion_de_partidas_presupuestarias, VouchersList.class, true, _desktop);
		    new ExtendedInternalFrame("Registrar\nComprobante a Pagar", IconTypes.administracion_de_partidas_presupuestarias, InvoiceTypeB.class, true, _desktop);
		    new ExtendedInternalFrame("Facturar\nNotas de Recepción", IconTypes.administracion_de_partidas_presupuestarias, VoucherToInvoiceMain.class, true, _desktop);
		    new ExtendedInternalFrame("Facturar\nOrdenes de Provisión", IconTypes.administracion_de_partidas_presupuestarias, VoucherToProvisionOrderMain.class, true, _desktop);
		    /*
		    ExtendedInternalFrame bookKeepingEntryContainer = new ExtendedInternalFrame("Nuevo asiento", IconTypes.budgets);
		    BookKeepingEntryMgmt bookKeepingEntryMgmt = new BookKeepingEntryMgmt();
		    bookKeepingEntryContainer.setClosable(false);
		    bookKeepingEntryContainer.setCentralPanel(bookKeepingEntryMgmt);
		    bookKeepingEntryContainer.setDesktop(_desktop);
		    bookKeepingEntryContainer.setIcon(true);*/
		    new ExtendedInternalFrame("Asientos", IconTypes.administracion_de_partidas_presupuestarias, BookKeepingEntryList.class, true, _desktop);
		    new ExtendedInternalFrame("Nueva Orden\nde Pago", IconTypes.administracion_de_partidas_presupuestarias, PaymentOrderMgmt.class, true, _desktop);
		    new ExtendedInternalFrame("Órdenes\nde Pago", IconTypes.administracion_de_partidas_presupuestarias, PaymentOrderList.class, true, _desktop);
		    new ExtendedInternalFrame("Libro Mayor", IconTypes.administracion_de_partidas_presupuestarias, JournalList.class, true, _desktop);
		    new ExtendedInternalFrame("Balance de\nsumas y saldos", IconTypes.administracion_de_partidas_presupuestarias, AccountsAvailableAmountList.class, true, _desktop);

		    /**
		     * BOTON PARA AGREGAR UNA NOTA DE EGRESO
		     */
		    new ExtendedInternalFrame("Nueva Nota\nde Egreso", IconTypes.administracion_de_partidas_presupuestarias, DebitNotesMgmt.class, true, _desktop);
		    /**
		     * BOTON PARA AGREGAR UNA NOTA DE INGRESO
		     */
		    new ExtendedInternalFrame("Nueva Nota\nde Ingreso", IconTypes.administracion_de_partidas_presupuestarias, CreditNotesMgmt.class, true, _desktop);
		    
		    /**
		     * BOTON PARA VER LA VENTANA DE PAGOS DE TERCEROS
		     */
		    new ExtendedInternalFrame("Pagos de\nTerceros", IconTypes.administracion_de_partidas_presupuestarias, PagosDeTercerosMain.class, true, _desktop);
		    
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    CASHFLOW_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("CashFlow loaded");
		    Debug.println("CASHFLOW boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("CASHFLOW already running!");
		}
		break;
	    case INIT_HALT :
		if (CASHFLOW_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("CASHFLOW halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcGaia(int _action, final BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		Debug.println("Booting GAIA");
		Environment.jpStatusBar.setAction("Loading Gaia");

		/**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		/**
		  * Boton TEST
		  * */
		 final DesktopButton btnTest = new DesktopButton("Cargar version de\nPrueba del GIS");
		 btnTest.addActionListener(new ActionListener() {

		                 public void actionPerformed(ActionEvent e) {
		                     if (GAIA_MODULE_STATUS != STATUS_RUNNING) {
/*

		                         BasicDrawEngine cityMap = new BasicDrawEngine("GIS", new BasicLabel());
		                         cityMap.setMapExtents(3548172.1937, 7249881.0068, 3571654.0741, 7268573.5261);
		                         CoordinateViewer coordinateViewer = new CoordinateViewer();
		                         coordinateViewer.setClosable(false);
		                         cityMap.setCoordinateViewer(coordinateViewer);
		                         coordinateViewer.setTitle("");

		                         cityMap.setBounds(Environment.getActiveDesktop().getBounds());
		                         cityMap.setVisible(true);

		                         LayerGroup catastralGroup = new LayerGroup("Catastral");
		                         LayerGroup infraestructuraGroup = new LayerGroup("Infraestructura");
		                         LayerGroup recaudacionGroup = new LayerGroup("Recaudación");
		                         LayerGroup socialGroup = new LayerGroup("Social");
		                         LayerGroup escrutinioGroup = new LayerGroup("Escrutinio");
		                         LayerGroup vialGroup = new LayerGroup("Vial");
		                         LayerGroup municipiosGroup = new LayerGroup("Municipios");
		                         LayerGroup topografiaGroup = new LayerGroup("Topografía");
		                         LayerGroup limitesGroup = new LayerGroup("Límites");
		                         LayerGroup ordenanzasGroup = new LayerGroup("Ordenanzas");
		                         LayerGroup parcelasGroup = new LayerGroup("Parcelas");

		                         Layer secciones = new Layer("secciones", "gis_salta", "secciones", "the_geom", "1=1", "gid");
		                         secciones.setProjectionType(CoordinateSystems.GK);
		                         secciones.setColor(Color.MAGENTA);
		                         secciones.setMouseOverColor(Color.ORANGE);

		                         Layer calles = new Layer("calles", "gis_salta", "calles", "the_geom", "1=1", "gid", "nombre");
		                         calles.setProjectionType(CoordinateSystems.GK);
		                         calles.setColor(Color.lightGray);
		                         calles.setMouseOverColor(Color.CYAN);
		                         calles.setQueryable(true);
		                         GaiaEnvironment.streetsLayer = calles.getAlias();
		                         GaiaEnvironment.scheme = "gis_salta";

		                         Layer manzanas_vinculadas = new Layer("manzanas_vinculadas", "gis_salta", "manzanas_vinculadas", "the_geom", "1=1 AND st_isvalid(the_geom)", "gid");
		                         manzanas_vinculadas.setProjectionType(CoordinateSystems.GK);
		                         manzanas_vinculadas.setColor(Color.RED);
		                         manzanas_vinculadas.setMouseOverColor(Color.MAGENTA);

		                         Layer parcelas_1 = new Layer("Parcelas 1", "gis_salta", "parcelas_vinculadas", "the_geom", "1=1 AND st_isvalid(the_geom)", "idparcela");
		                         parcelas_1.setProjectionType(CoordinateSystems.GK);
		                         parcelas_1.setColor(Color.BLACK);
		                         parcelas_1.setMouseOverColor(Color.MAGENTA);

		                         Layer parcelas_2 = new Layer("Parcelas 2", "gis_salta", "parcelas_vinculadas", "the_geom", "1=1 AND st_isvalid(the_geom)", "idparcela");
		                         parcelas_2.setProjectionType(CoordinateSystems.GK);
		                         parcelas_2.setColor(Color.BLACK);
		                         parcelas_2.setMouseOverColor(Color.MAGENTA);

		                         Layer parcelas_3 = new Layer("Parcelas 3", "gis_salta", "parcelas_vinculadas", "the_geom", "1=1 AND st_isvalid(the_geom)", "idparcela");
		                         parcelas_3.setProjectionType(CoordinateSystems.GK);
		                         parcelas_3.setColor(Color.BLACK);
		                         parcelas_3.setMouseOverColor(Color.MAGENTA);

		                         Layer parcelas_catastral = new Layer("Parcelas (Catastral)", "gis_salta", "parcelas_vinculadas", "the_geom", "1=1 AND st_isvalid(the_geom)", "idparcela");
		                         parcelas_catastral.setProjectionType(CoordinateSystems.GK);
		                         parcelas_catastral.setColor(Color.BLACK);
		                         parcelas_catastral.setMouseOverColor(Color.MAGENTA);

		                         Layer parcelas_escrutinio = new Layer("Parcelas", "gis_salta", "parcelas_vinculadas", "the_geom", "1=1 AND st_isvalid(the_geom)", "idparcela");
		                         parcelas_escrutinio.setProjectionType(CoordinateSystems.GK);
		                         parcelas_escrutinio.setColor(Color.BLACK);
		                         parcelas_escrutinio.setMouseOverColor(Color.MAGENTA);

		                         Layer ayudas_parcelas_sa = new Layer("Ayudas Sociales Sección A", "gis_salta", "parcelas_sa", "the_geom", "st_isvalid(the_geom)", "idparcela");
		                         ayudas_parcelas_sa.setProjectionType(CoordinateSystems.GK);
		                         ayudas_parcelas_sa.setColor(Color.BLACK);
		                         ayudas_parcelas_sa.setMouseOverColor(Color.CYAN);
		                         ayudas_parcelas_sa.setQueryable(true);
		                         ayudas_parcelas_sa.addFilter("valorfiscal", "(valorfiscal <= 200)", Color.black, new Color(253, 253, 0), "Menos de $ 200,00");
		                         ayudas_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 200 AND valorfiscal <= 400)", Color.black, new Color(253, 0, 0), "Entre $ 200,00 y $ 400,00");
		                         ayudas_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 400 AND valorfiscal <= 600)", Color.black, new Color(252, 0, 253), "Entre $ 400,00 y $ 600,00");
		                         ayudas_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 600 AND valorfiscal <= 800)", Color.black, new Color(152, 0, 153), "Entre $ 600,00 y $ 800,00");
		                         ayudas_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 800 AND valorfiscal <= 1000)", Color.black, new Color(0, 171, 0), "Entre $ 800,00 y $ 1.000,00");

		                         ExtendedInternalFrame ayudasSocialesContainer = new ExtendedInternalFrame("Ventana de Información");
		                         GaiaAyudasSocialesPanel ayudasSocialesPanel = new GaiaAyudasSocialesPanel();
		                         ayudasSocialesContainer.setCentralPanel(ayudasSocialesPanel);
		                         ayudas_parcelas_sa.setInfoPanel(ayudasSocialesContainer);

		                         Layer tgi_parcelas_sa = new Layer("Categoría TGI Sección A", "gis_salta", "parcelas_sa", "the_geom", "AND st_isvalid(the_geom)", "idparcela", "parcela", "", "categoria");
		                         tgi_parcelas_sa.setProjectionType(CoordinateSystems.GK);
		                         tgi_parcelas_sa.setColor(Color.BLACK);
		                         tgi_parcelas_sa.setMouseOverColor(Color.CYAN);
		                         tgi_parcelas_sa.setQueryable(true);
		                         tgi_parcelas_sa.setToolTipLabel("Categoría TGI");
		                         tgi_parcelas_sa.addFilter("categoria", "(categoria = 0)", Color.black, new Color(0, 0, 0), "1");
		                         tgi_parcelas_sa.addFilter("categoria", "(categoria = 1)", Color.black, new Color(255, 0, 0), "1");
		                         tgi_parcelas_sa.addFilter("categoria", "(categoria = 2)", Color.black, new Color(0, 220, 0), "2");
		                         tgi_parcelas_sa.addFilter("categoria", "(categoria = 3)", Color.black, new Color(200, 0, 200), "3");
		                         tgi_parcelas_sa.addFilter("categoria", "(categoria = 4)", Color.black, new Color(255, 159, 128), "4");
		                         tgi_parcelas_sa.addFilter("categoria", "(categoria = 5)", Color.black, new Color(150, 150, 255), "5");
		                         tgi_parcelas_sa.addFilter("categoria", "(categoria = 6)", Color.black, new Color(150, 250, 150), "6");

		                         Layer impinm_parcelas_sa = new Layer("Impuesto Inmobiliario Sección A", "gis_salta", "parcelas_sa", "the_geom", "AND st_isvalid(the_geom)", "idparcela");
		                         impinm_parcelas_sa.setProjectionType(CoordinateSystems.GK);
		                         impinm_parcelas_sa.setColor(Color.BLACK);
		                         impinm_parcelas_sa.setMouseOverColor(Color.CYAN);
		                         impinm_parcelas_sa.setQueryable(true);
		                         impinm_parcelas_sa.addFilter("impinm", "(impinm = 0)", Color.black, new Color(0, 0, 1), "Categoría I.I. 0");
		                         impinm_parcelas_sa.addFilter("impinm", "(impinm = 10)", Color.black, new Color(255, 0, 1), "Categoría I.I. 10");
		                         impinm_parcelas_sa.addFilter("impinm", "(impinm = 11)", Color.black, new Color(253, 253, 1), "Categoría I.I. 11");
		                         impinm_parcelas_sa.addFilter("impinm", "(impinm = 2)", Color.black, new Color(0, 220, 1), "Categoría I.I. 2");
		                         impinm_parcelas_sa.addFilter("impinm", "(impinm = 3)", Color.black, new Color(0, 0, 201), "Categoría I.I. 3");
		                         
		                         Layer valfis_parcelas_sa = new Layer("Valor Fiscal Sección A", "gis_salta", "parcelas_sa", "the_geom", "AND st_isvalid(the_geom)", "idparcela");
		                         valfis_parcelas_sa.setProjectionType(CoordinateSystems.GK);
		                         valfis_parcelas_sa.setColor(Color.BLACK);
		                         valfis_parcelas_sa.setMouseOverColor(Color.CYAN);
		                         valfis_parcelas_sa.setQueryable(true);
		                         valfis_parcelas_sa.addFilter("valorfiscal", "(valorfiscal <= 4000)", Color.black, new Color(252, 252, 0), "Hasta $ 4.000,00");
		                         valfis_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 4000 AND valorfiscal <= 10000)", Color.black, new Color(252, 0, 0), "Entre $ 4.000,00 y $ 10.000,00");
		                         valfis_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 10000 AND valorfiscal <= 20000)", Color.black, new Color(0, 0, 252), "Entre $ 10.000,00 y $ 20.000,00");
		                         valfis_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 20000 AND valorfiscal <= 30000)", Color.black, new Color(0, 252, 0), "Entre $ 20.000,00 y $ 30.000,00");
		                         valfis_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 30000 AND valorfiscal <= 40000)", Color.black, new Color(0, 252, 252), "Entre $ 30.000,00 y $ 40.000,00");
		                         valfis_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 40000 AND valorfiscal <= 50000)", Color.black, new Color(252, 0, 252), "Entre $ 40.000,00 y $ 50.000,00");
		                         valfis_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 50000 AND valorfiscal <= 60000)", Color.black, new Color(152, 0, 152), "Entre $ 50.000,00 y $ 60.000,00");
		                         valfis_parcelas_sa.addFilter("valorfiscal", "(valorfiscal > 60000)", Color.black, new Color(0, 170, 0), "Más de $ 60.000,00");

		                         Layer comercios = new Layer("Comercios", "gis_salta", "comercios", "the_geom", "AND st_isvalid(the_geom)", "idparcela", "parcela", "", "valorfiscal");
		                         comercios.setProjectionType(CoordinateSystems.GK);
		                         comercios.setColor(Color.BLACK);
		                         comercios.setMouseOverColor(Color.CYAN);
		                         comercios.setQueryable(true);
		                         comercios.setToolTipLabel("Valor Fiscal:");

		                         ExtendedInternalFrame streetsPanelContainer = new ExtendedInternalFrame("Calles");
		                         GaiaStreetsPanel streetsPanel = new GaiaStreetsPanel();
		                         streetsPanelContainer.setCentralPanel(streetsPanel);
		                         calles.setConfigPanel(streetsPanelContainer);

		                         Layer pavimento = new Layer("pavimento", "gis_salta", "pavimento", "the_geom", "1=1", "gid");
		                         pavimento.setProjectionType(CoordinateSystems.GK);
		                         pavimento.setColor(Color.BLACK);
		                         pavimento.setMouseOverColor(Color.CYAN);
		                         pavimento.setQueryable(true);
		                         pavimento.addFilter("idtipo", "(idtipo = 1)", new Color(255, 0, 0), 3, "Pavimento 1");
		                         pavimento.addFilter("idtipo", "(idtipo = 2)", new Color(70, 70, 70), 3, "Pavimento 2");
		                         pavimento.addFilter("idtipo", "(idtipo = 3)", new Color(180, 100, 0), 3, "Pavimento 3");
		                         pavimento.addFilter("idtipo", "(idtipo = 4)", new Color(170, 170, 170), 3, "Pavimento 4");
		                         pavimento.addFilter("idtipo", "(idtipo = 5)", new Color(180, 100, 0), 3, "Pavimento 5");

		                         Layer redcloacal_bocasderegistro = new Layer("Red Cloacal (Bocas de Registro)", "gis_salta", "bocas", "the_geom", "1=1", "gid");
		                         redcloacal_bocasderegistro.setProjectionType(CoordinateSystems.GK);
		                         redcloacal_bocasderegistro.setColor(Color.BLACK);
		                         redcloacal_bocasderegistro.setMouseOverColor(Color.CYAN);
		                         redcloacal_bocasderegistro.setQueryable(true);

		                         Layer redcloacal_cojinetes = new Layer("Red Cloacal (Cojinetes)", "gis_salta", "cojinetes", "the_geom", "1=1", "gid");
		                         redcloacal_cojinetes.setProjectionType(CoordinateSystems.GK);
		                         redcloacal_cojinetes.setColor(Color.RED);
		                         redcloacal_cojinetes.setMouseOverColor(Color.CYAN);
		                         redcloacal_cojinetes.setQueryable(true);

		                         Layer redcloacal = new Layer("Red Cloacal (Cañerías)", "gis_salta", "cloacas", "the_geom", "1=1", "gid");
		                         redcloacal.setProjectionType(CoordinateSystems.GK);
		                         redcloacal.setColor(Color.BLACK);
		                         redcloacal.setMouseOverColor(Color.CYAN);
		                         redcloacal.setQueryable(true);
		                         redcloacal.addFilter("idtipo", "(idtipo = 0)", new Color(0, 255, 0), 2, "Tipo 0");
		                         redcloacal.addFilter("idtipo", "(idtipo = 1)", new Color(255, 220, 168), 2, "Tipo 1");
		                         redcloacal.addFilter("idtipo", "(idtipo = 2)", new Color(255, 168, 88), 2, "Tipo 2");
		                         redcloacal.addFilter("idtipo", "(idtipo = 3)", new Color(255, 128, 0), 2, "Tipo 3");
		                         redcloacal.addFilter("idtipo", "(idtipo = 4)", new Color(255, 192, 255), 2, "Tipo 4");
		                         redcloacal.addFilter("idtipo", "(idtipo = 5)", new Color(255, 0, 255), 2, "Tipo 5");
		                         redcloacal.addFilter("idtipo", "(idtipo = 6)", new Color(192, 0, 192), 2, "Tipo 6");
		                         redcloacal.addFilter("idtipo", "(idtipo = 7)", new Color(0, 255, 255), 2, "Tipo 7");
		                         redcloacal.addFilter("idtipo", "(idtipo = 8)", new Color(0, 192, 192), 2, "Tipo 8");
		                         redcloacal.addFilter("idtipo", "(idtipo = 9)", new Color(0, 128, 128), 2, "Tipo 9");
		                         redcloacal.addFilter("idtipo", "(idtipo = 10)", new Color(255, 155, 192), 2, "Tipo 10");
		                         redcloacal.addFilter("idtipo", "(idtipo = 11)", new Color(255, 255, 0), 2, "Tipo 11");
		                         redcloacal.addFilter("idtipo", "(idtipo = 12)", new Color(192, 192, 0), 2, "Tipo 12");
		                         redcloacal.addFilter("idtipo", "(idtipo = 13)", new Color(192, 192, 255), 2, "Tipo 13");
		                         redcloacal.addFilter("idtipo", "(idtipo = 14)", new Color(0, 0, 255), 2, "Tipo 14");
		                         redcloacal.addFilter("idtipo", "(idtipo = 15)", new Color(0, 0, 192), 2, "Tipo 15");
		                         redcloacal.addFilter("idtipo", "(idtipo = 16)", new Color(192, 255, 192), 2, "Tipo 16");
		                         redcloacal.addFilter("idtipo", "(idtipo = 17)", new Color(0, 192, 0), 2, "Tipo 17");
		                         redcloacal.addFilter("idtipo", "(idtipo = 18)", new Color(0, 128, 0), 2, "Tipo 18");
		                         redcloacal.addFilter("idtipo", "(idtipo = 19)", new Color(255, 192, 192), 2, "Tipo 19");
		                         redcloacal.addFilter("idtipo", "(idtipo = 20)", new Color(255, 0, 0), 2, "Tipo 20");
		                         redcloacal.addFilter("idtipo", "(idtipo = 21)", new Color(192, 0, 0), 2, "Tipo 21");
		                         redcloacal.addFilter("idtipo", "(idtipo = 22)", new Color(128, 0, 0), 2, "Tipo 22");
		                         redcloacal.addFilter("idtipo", "(idtipo = 23)", new Color(220, 220, 220), 2, "Tipo 23");
		                         redcloacal.addFilter("idtipo", "(idtipo = 24)", new Color(160, 160, 160), 2, "Tipo 24");
		                         redcloacal.addFilter("idtipo", "(idtipo = 25)", new Color(88, 88, 88), 2, "Tipo 25");
		                         redcloacal.addFilter("idtipo", "(idtipo = 26)", new Color(0, 64, 0), 2, "Tipo 26");
		                         redcloacal.addFilter("idtipo", "(idtipo = 27)", new Color(0, 0, 128), 2, "Tipo 27");
		                         redcloacal.addFilter("idtipo", "(idtipo = 28)", new Color(128, 128, 0), 2, "Tipo 28");

		                         Layer reddeaguapotable = new Layer("Red de Agua Potable", "gis_salta", "canerias", "the_geom", "1=1", "gid");
		                         reddeaguapotable.setProjectionType(CoordinateSystems.GK);
		                         reddeaguapotable.setColor(Color.BLACK);
		                         reddeaguapotable.setMouseOverColor(Color.CYAN);
		                         reddeaguapotable.setQueryable(true);
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 0)", new Color(0, 255, 0), 2, "Tipo 0");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 1)", new Color(255, 220, 168), 2, "Tipo 1");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 2)", new Color(255, 168, 88), 2, "Tipo 2");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 3)", new Color(255, 128, 0), 2, "Tipo 3");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 4)", new Color(255, 192, 255), 2, "Tipo 4");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 5)", new Color(255, 0, 255), 2, "Tipo 5");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 6)", new Color(192, 0, 192), 2, "Tipo 6");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 7)", new Color(0, 255, 255), 2, "Tipo 7");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 8)", new Color(0, 192, 192), 2, "Tipo 8");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 9)", new Color(0, 128, 128), 2, "Tipo 9");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 10)", new Color(255, 155, 192), 2, "Tipo 10");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 11)", new Color(255, 255, 0), 2, "Tipo 11");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 12)", new Color(192, 192, 0), 2, "Tipo 12");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 13)", new Color(192, 192, 255), 2, "Tipo 13");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 14)", new Color(0, 0, 255), 2, "Tipo 14");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 15)", new Color(0, 0, 192), 2, "Tipo 15");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 16)", new Color(192, 255, 192), 2, "Tipo 16");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 17)", new Color(0, 192, 0), 2, "Tipo 17");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 18)", new Color(0, 128, 0), 2, "Tipo 18");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 19)", new Color(255, 192, 192), 2, "Tipo 19");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 20)", new Color(255, 0, 0), 2, "Tipo 20");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 21)", new Color(192, 0, 0), 2, "Tipo 21");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 22)", new Color(128, 0, 0), 2, "Tipo 22");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 23)", new Color(220, 220, 220), 2, "Tipo 23");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 24)", new Color(160, 160, 160), 2, "Tipo 24");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 25)", new Color(88, 88, 88), 2, "Tipo 25");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 26)", new Color(0, 64, 0), 2, "Tipo 26");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 27)", new Color(0, 0, 128), 2, "Tipo 27");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 28)", new Color(128, 128, 0), 2, "Tipo 28");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 29)", new Color(128, 118, 0), 2, "Tipo 29");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 30)", new Color(128, 108, 0), 2, "Tipo 30");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 31)", new Color(118, 128, 0), 2, "Tipo 31");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 32)", new Color(108, 128, 0), 2, "Tipo 32");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 33)", new Color(118, 118, 0), 2, "Tipo 33");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 34)", new Color(108, 108, 0), 2, "Tipo 34");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 35)", new Color(128, 128, 108), 2, "Tipo 35");
		                         reddeaguapotable.addFilter("idtipo", "(idtipo = 36)", new Color(128, 128, 118), 2, "Tipo 36");

		                         Layer reddegasnatural = new Layer("Red de Gas Natural", "gis_salta", "gas", "the_geom", "1=1", "gid");
		                         reddegasnatural.setProjectionType(CoordinateSystems.GK);
		                         reddegasnatural.setColor(Color.BLACK);
		                         reddegasnatural.setMouseOverColor(Color.CYAN);
		                         reddegasnatural.setQueryable(true);
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 0)", new Color(0, 255, 0), 2, "Tipo 0");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 1)", new Color(255, 220, 168), 2, "Tipo 1");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 2)", new Color(255, 168, 88), 2, "Tipo 2");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 3)", new Color(255, 128, 0), 2, "Tipo 3");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 4)", new Color(255, 192, 255), 2, "Tipo 4");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 5)", new Color(255, 0, 255), 2, "Tipo 5");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 6)", new Color(192, 0, 192), 2, "Tipo 6");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 7)", new Color(0, 255, 255), 2, "Tipo 7");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 8)", new Color(0, 192, 192), 2, "Tipo 8");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 9)", new Color(0, 128, 128), 2, "Tipo 9");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 10)", new Color(255, 155, 192), 2, "Tipo 10");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 11)", new Color(255, 255, 0), 2, "Tipo 11");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 12)", new Color(192, 192, 0), 2, "Tipo 12");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 13)", new Color(192, 192, 255), 2, "Tipo 13");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 14)", new Color(0, 0, 255), 2, "Tipo 14");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 15)", new Color(0, 0, 192), 2, "Tipo 15");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 16)", new Color(192, 255, 192), 2, "Tipo 16");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 17)", new Color(0, 192, 0), 2, "Tipo 17");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 18)", new Color(0, 128, 0), 2, "Tipo 18");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 19)", new Color(255, 192, 192), 2, "Tipo 19");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 20)", new Color(255, 0, 0), 2, "Tipo 20");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 21)", new Color(192, 0, 0), 2, "Tipo 21");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 22)", new Color(128, 0, 0), 2, "Tipo 22");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 23)", new Color(220, 220, 220), 2, "Tipo 23");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 24)", new Color(160, 160, 160), 2, "Tipo 24");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 25)", new Color(88, 88, 88), 2, "Tipo 25");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 26)", new Color(0, 64, 0), 2, "Tipo 26");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 27)", new Color(0, 0, 128), 2, "Tipo 27");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 28)", new Color(128, 128, 0), 2, "Tipo 28");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 29)", new Color(128, 118, 0), 2, "Tipo 29");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 30)", new Color(128, 108, 0), 2, "Tipo 30");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 31)", new Color(118, 128, 0), 2, "Tipo 31");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 32)", new Color(108, 128, 0), 2, "Tipo 32");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 33)", new Color(118, 118, 0), 2, "Tipo 33");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 34)", new Color(108, 108, 0), 2, "Tipo 34");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 35)", new Color(128, 128, 108), 2, "Tipo 35");
		                         reddegasnatural.addFilter("idtipo", "(idtipo = 36)", new Color(128, 128, 118), 2, "Tipo 36");

		                         Layer estructuras_viales = new Layer("estructuras", "gis_salta", "estructuras", "the_geom", "1=1", "gid");
		                         estructuras_viales.setProjectionType(CoordinateSystems.GK);
		                         estructuras_viales.setColor(Color.BLACK);
		                         estructuras_viales.setMouseOverColor(Color.CYAN);
		                         estructuras_viales.setQueryable(true);
		                         estructuras_viales.setToolTipLabel("Tipo de Estructura Vial");
		                         estructuras_viales.addFilter("idtipo", "(idtipo = 1)", new Color(0, 125, 0), 2, "Ferrocarril");
		                         estructuras_viales.addFilter("idtipo", "(idtipo = 2)", new Color(207, 207, 207), 1, "Platabanda");
		                         estructuras_viales.addFilter("idtipo", "(idtipo = 3)", new Color(207, 207, 207), 1, "Calzada");
		                         estructuras_viales.addFilter("idtipo", "(idtipo = 4)", new Color(207, 207, 207), 1, "Cordón Vereda");
		                         estructuras_viales.addFilter("idtipo", "(idtipo = 5)", new Color(160, 160, 200), 3, "Canal");
		                         estructuras_viales.addFilter("idtipo", "(idtipo = 6 OR idtipo = 0)", new Color(70, 190, 230), 1, "Río");
		                         estructuras_viales.addFilter("idtipo", "(idtipo = 7)", new Color(0, 180, 90), 1, "Espacio Verde");

		                         Layer barrios = new Layer("barrios", "gis_salta", "barrios", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         barrios.setProjectionType(CoordinateSystems.GK);
		                         barrios.setColor(new Color(0, 0, 255));
		                         barrios.setMouseOverColor(Color.CYAN);
		                         barrios.setQueryable(true);

		                         Layer farolas = new Layer("farolas", "gis_salta", "farolas", "the_geom", "1=1", "gid");
		                         farolas.setProjectionType(CoordinateSystems.GK);
		                         farolas.setColor(new Color(255, 0, 0));
		                         farolas.setMouseOverColor(Color.CYAN);
		                         farolas.setPointDiameter(2);
		                         farolas.setEditable(true);

		                         ExtendedInternalFrame formsInternalFrame = new ExtendedInternalFrame("Formularios");
		                         formsInternalFrame.setDesktop(_desktop);
		                         BasicPanel formPanel = new BasicPanel();
		                         formsInternalFrame.setClosable(false);
		                         formsInternalFrame.setIconifiable(false);
		                         formPanel.setLayout(new BorderLayout());
		                         formPanel.setSize(new Dimension(300,420));
		                         GaiaEnvironment.formPanel = formPanel;
		                         formsInternalFrame.setCentralPanel(formPanel);
		                         //formsInternalFrame.show();
		                         GaiaEnvironment.formsFrame = formsInternalFrame;

		                         Layer casosEnfermedades = new Layer("Control y Prev. de Enfermedades endémicas", "gis_salta", "casosenfermedades", "the_geom", "estado != '*'", "gid", "nombre", "", "nombre");
		                         casosEnfermedades.setProjectionType(CoordinateSystems.GK);
		                         casosEnfermedades.setColor(new Color(255, 0, 0));
		                         casosEnfermedades.setMouseOverColor(Color.CYAN);
		                         casosEnfermedades.setPointDiameter(2);
		                         casosEnfermedades.setEditable(true);
		                         GaiaFormCasosEnfermedad formCasosEnfermedades = new GaiaFormCasosEnfermedad();
		                         formCasosEnfermedades.setLayer(casosEnfermedades);

		                         Layer casosDelitos = new Layer("Mapa del Delito", "gis_salta", "casosdelitos", "the_geom", "estado != '*'", "gid", "idtipodelito", "", "idtipodelito");
		                         casosDelitos.setProjectionType(CoordinateSystems.GK);
		                         casosDelitos.setColor(new Color(255, 0, 0));
		                         casosDelitos.setMouseOverColor(Color.CYAN);
		                         casosDelitos.setPointDiameter(2);
		                         casosDelitos.setEditable(true);
		                         GaiaFormDelito formCasosDelitos = new GaiaFormDelito();
		                         formCasosDelitos.setLayer(casosDelitos);

		                         Layer asentamientos_poligono_2009 = new Layer("asentamientos_poligono_2009", "gis_salta", "asentamientos_poligono_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         asentamientos_poligono_2009.setProjectionType(CoordinateSystems.GK);
		                         asentamientos_poligono_2009.setColor(new Color(255, 153, 0));
		                         asentamientos_poligono_2009.setFillColor(new Color(255, 255, 0 , 128));
		                         asentamientos_poligono_2009.setMouseOverColor(Color.CYAN);
		                         asentamientos_poligono_2009.setQueryable(true);

		                         Layer asentamientos_textos_2009 = new Layer("asentamientos_textos_2009", "gis_salta", "asentamientos_textos_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid", "maptext");
		                         asentamientos_textos_2009.getLayerConfig().getStyleConfig().setLabelFont(new Font("Arial", Font.BOLD, 50));
		                         asentamientos_textos_2009.setProjectionType(CoordinateSystems.GK);
		                         asentamientos_textos_2009.setColor(new Color(0, 0, 255));
		                         asentamientos_textos_2009.setMouseOverColor(Color.CYAN);
		                         asentamientos_textos_2009.setQueryable(true);

		                         Layer autopistas_lineas_2009 = new Layer("autopistas_lineas_2009", "gis_salta", "autopistas_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         autopistas_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         autopistas_lineas_2009.setColor(new Color(204, 0, 0));
		                         autopistas_lineas_2009.getLayerConfig().getStyleConfig().setLineWidth(3);
		                         autopistas_lineas_2009.setMouseOverColor(Color.CYAN);
		                         autopistas_lineas_2009.setQueryable(true);

		                         Layer autopistas_proyectadas_2009 = new Layer("autopistas_proyectadas_2009", "gis_salta", "autopistas_proyectadas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         autopistas_proyectadas_2009.setProjectionType(CoordinateSystems.GK);
		                         autopistas_proyectadas_2009.setColor(new Color(204,204,204, 0));
		                         autopistas_proyectadas_2009.setMouseOverColor(Color.CYAN);
		                         autopistas_proyectadas_2009.setQueryable(true);

		                         Layer canales_lineas_2009 = new Layer("canales_lineas_2009", "gis_salta", "canales_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         canales_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         canales_lineas_2009.setColor(new Color(0, 204, 255));
		                         canales_lineas_2009.getLayerConfig().getStyleConfig().setLineWidth(2);
		                         canales_lineas_2009.setMouseOverColor(Color.CYAN);
		                         canales_lineas_2009.setQueryable(true);

		                         Layer castanares_grupos_poligonos_2009 = new Layer("castanares_grupos_poligonos_2009", "gis_salta", "castanares_grupos_poligonos_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         castanares_grupos_poligonos_2009.setProjectionType(CoordinateSystems.GK);
		                         castanares_grupos_poligonos_2009.setColor(new Color(255, 153, 51));
		                         castanares_grupos_poligonos_2009.setFillColor(new Color(255, 0, 51, 154));
		                         castanares_grupos_poligonos_2009.setMouseOverColor(Color.CYAN);
		                         castanares_grupos_poligonos_2009.setQueryable(true);

		                         Layer castanares_grupos_textos_2009 = new Layer("castanares_grupos_textos_2009", "gis_salta", "castanares_grupos_textos_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         castanares_grupos_textos_2009.setProjectionType(CoordinateSystems.GK);
		                         castanares_grupos_textos_2009.setColor(new Color(0, 0, 255));
		                         castanares_grupos_textos_2009.setMouseOverColor(Color.CYAN);
		                         castanares_grupos_textos_2009.setQueryable(true);

		                         Layer cerrillos_calles_texto_2009 = new Layer("cerrillos_calles_texto_2009", "gis_salta", "cerrillos_calles_texto_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cerrillos_calles_texto_2009.setProjectionType(CoordinateSystems.GK);
		                         cerrillos_calles_texto_2009.setColor(new Color(0, 0, 255));
		                         cerrillos_calles_texto_2009.setMouseOverColor(Color.CYAN);
		                         cerrillos_calles_texto_2009.setQueryable(true);

		                         Layer cerrillos_informat_texto_2009 = new Layer("cerrillos_informat_texto_2009", "gis_salta", "cerrillos_informat_texto_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cerrillos_informat_texto_2009.setProjectionType(CoordinateSystems.GK);
		                         cerrillos_informat_texto_2009.setColor(new Color(0, 0, 255));
		                         cerrillos_informat_texto_2009.setMouseOverColor(Color.CYAN);
		                         cerrillos_informat_texto_2009.setQueryable(true);

		                         Layer cerrillos_manzanas_lineas_2009 = new Layer("cerrillos_manzanas_lineas_2009", "gis_salta", "cerrillos_manzanas_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cerrillos_manzanas_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         cerrillos_manzanas_lineas_2009.setColor(new Color(204, 204, 204));
		                         cerrillos_manzanas_lineas_2009.setMouseOverColor(Color.CYAN);
		                         cerrillos_manzanas_lineas_2009.setQueryable(true);

		                         Layer cota100_linea_2009 = new Layer("cota100_linea_2009", "gis_salta", "cota100_linea_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cota100_linea_2009.setProjectionType(CoordinateSystems.GK);
		                         cota100_linea_2009.setColor(new Color(204, 255, 0));
		                         cota100_linea_2009.getLayerConfig().getStyleConfig().setLineWidth(2);
		                         cota100_linea_2009.setMouseOverColor(Color.CYAN);
		                         cota100_linea_2009.setQueryable(true);

		                         Layer cota1225_linea_2009 = new Layer("cota1225_linea_2009", "gis_salta", "cota1225_linea_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cota1225_linea_2009.setProjectionType(CoordinateSystems.GK);
		                         cota1225_linea_2009.setColor(new Color(255, 51, 204));
		                         cota1225_linea_2009.getLayerConfig().getStyleConfig().setLineWidth(2);
		                         cota1225_linea_2009.setMouseOverColor(Color.CYAN);
		                         cota1225_linea_2009.setQueryable(true);

		                         Layer cota1225_texto_2009 = new Layer("cota1225_texto_2009", "gis_salta", "cota1225_texto_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cota1225_texto_2009.setProjectionType(CoordinateSystems.GK);
		                         cota1225_texto_2009.setColor(new Color(255, 51, 204));
		                         cota1225_texto_2009.setMouseOverColor(Color.CYAN);
		                         cota1225_texto_2009.setQueryable(true);

		                         Layer cota25_linea_2009 = new Layer("cota25_linea_2009", "gis_salta", "cota25_linea_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cota25_linea_2009.setProjectionType(CoordinateSystems.GK);
		                         cota25_linea_2009.setColor(new Color(204, 204, 0));
		                         cota25_linea_2009.setMouseOverColor(Color.CYAN);
		                         cota25_linea_2009.setQueryable(true);

		                         Layer cota50_linea_2009 = new Layer("cota50_linea_2009", "gis_salta", "cota50_linea_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cota50_linea_2009.setProjectionType(CoordinateSystems.GK);
		                         cota50_linea_2009.setColor(new Color(153, 153, 0));
		                         cota50_linea_2009.setMouseOverColor(Color.CYAN);
		                         cota50_linea_2009.setQueryable(true);

		                         Layer cota_cima_punto_2009 = new Layer("cota_cima_punto_2009", "gis_salta", "cota_cima_punto_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cota_cima_punto_2009.setProjectionType(CoordinateSystems.GK);
		                         cota_cima_punto_2009.setColor(new Color(0, 0, 255));
		                         cota_cima_punto_2009.setMouseOverColor(Color.CYAN);
		                         cota_cima_punto_2009.setQueryable(true);

		                         Layer cota_cima_texto_2009 = new Layer("cota_cima_texto_2009", "gis_salta", "cota_cima_texto_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cota_cima_texto_2009.setProjectionType(CoordinateSystems.GK);
		                         cota_cima_texto_2009.setColor(new Color(0, 0, 255));
		                         cota_cima_texto_2009.setMouseOverColor(Color.CYAN);
		                         cota_cima_texto_2009.setQueryable(true);

		                         Layer cota_numeros_texto_2009 = new Layer("cota_numeros_texto_2009", "gis_salta", "cota_numeros_texto_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         cota_numeros_texto_2009.setProjectionType(CoordinateSystems.GK);
		                         cota_numeros_texto_2009.setColor(new Color(0, 0, 255));
		                         cota_numeros_texto_2009.setMouseOverColor(Color.CYAN);
		                         cota_numeros_texto_2009.setQueryable(true);

		                         Layer escuelas_puntos_2009 = new Layer("escuelas_puntos_2009", "gis_salta", "escuelas_puntos_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         escuelas_puntos_2009.setProjectionType(CoordinateSystems.GK);
		                         escuelas_puntos_2009.setColor(new Color(0, 0, 255));
		                         escuelas_puntos_2009.setMouseOverColor(Color.CYAN);
		                         escuelas_puntos_2009.setQueryable(true);

		                         Layer escuelas_textos_2009 = new Layer("escuelas_textos_2009", "gis_salta", "escuelas_textos_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         escuelas_textos_2009.setProjectionType(CoordinateSystems.GK);
		                         escuelas_textos_2009.setColor(new Color(0, 0, 255));
		                         escuelas_textos_2009.setMouseOverColor(Color.CYAN);
		                         escuelas_textos_2009.setQueryable(true);

		                         Layer espacios_verdes_futuro_poligono_2009 = new Layer("espacios_verdes_futuro_poligono_2009", "gis_salta", "espacios_verdes_futuro_poligono_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         espacios_verdes_futuro_poligono_2009.setProjectionType(CoordinateSystems.GK);
		                         espacios_verdes_futuro_poligono_2009.setColor(new Color(0, 153, 51));
		                         espacios_verdes_futuro_poligono_2009.setFillColor(new Color(0, 153, 51));
		                         espacios_verdes_futuro_poligono_2009.setMouseOverColor(Color.CYAN);
		                         espacios_verdes_futuro_poligono_2009.setQueryable(true);

		                         Layer espacios_verdes_poligono_2009 = new Layer("espacios_verdes_poligono_2009", "gis_salta", "espacios_verdes_poligono_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         espacios_verdes_poligono_2009.setProjectionType(CoordinateSystems.GK);
		                         espacios_verdes_poligono_2009.setColor(new Color(0, 153, 51));
		                         espacios_verdes_poligono_2009.setFillColor(new Color(0, 153, 51));
		                         espacios_verdes_poligono_2009.setMouseOverColor(Color.CYAN);
		                         espacios_verdes_poligono_2009.setQueryable(true);

		                         Layer ffcc_2009 = new Layer("ffcc_2009", "gis_salta", "ffcc_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         ffcc_2009.setProjectionType(CoordinateSystems.GK);
		                         ffcc_2009.setColor(new Color(0, 204, 153));
		                         ffcc_2009.getLayerConfig().getStyleConfig().setLineWidth(2);
		                         ffcc_2009.setMouseOverColor(Color.CYAN);
		                         ffcc_2009.setQueryable(true);

		                         Layer infraestructura_textos_2009 = new Layer("infraestructura_textos_2009", "gis_salta", "infraestructura_textos_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         infraestructura_textos_2009.setProjectionType(CoordinateSystems.GK);
		                         infraestructura_textos_2009.setColor(new Color(0, 0, 255));
		                         infraestructura_textos_2009.setMouseOverColor(Color.CYAN);
		                         infraestructura_textos_2009.setQueryable(true);

		                         Layer limite_dpto_lineas_2009 = new Layer("limite_dpto_lineas_2009", "gis_salta", "limite_dpto_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         limite_dpto_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         limite_dpto_lineas_2009.setColor(new Color(0, 0, 255));
		                         limite_dpto_lineas_2009.setMouseOverColor(Color.CYAN);
		                         limite_dpto_lineas_2009.setQueryable(true);

		                         Layer limite_dpto_texto_2009 = new Layer("limite_dpto_texto_2009", "gis_salta", "limite_dpto_texto_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         limite_dpto_texto_2009.setProjectionType(CoordinateSystems.GK);
		                         limite_dpto_texto_2009.setColor(new Color(0, 0, 255));
		                         limite_dpto_texto_2009.setMouseOverColor(Color.CYAN);
		                         limite_dpto_texto_2009.setQueryable(true);

		                         Layer limite_ejido_municipal_lineas_2009 = new Layer("limite_ejido_municipal_lineas_2009", "gis_salta", "limite_ejido_municipal_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         limite_ejido_municipal_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         limite_ejido_municipal_lineas_2009.setColor(new Color(0, 0, 255));
		                         limite_ejido_municipal_lineas_2009.setMouseOverColor(Color.CYAN);
		                         limite_ejido_municipal_lineas_2009.setQueryable(true);

		                         Layer limite_ejido_municipal_texto_2009 = new Layer("limite_ejido_municipal_texto_2009", "gis_salta", "limite_ejido_municipal_texto_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         limite_ejido_municipal_texto_2009.setProjectionType(CoordinateSystems.GK);
		                         limite_ejido_municipal_texto_2009.setColor(new Color(0, 0, 255));
		                         limite_ejido_municipal_texto_2009.setMouseOverColor(Color.CYAN);
		                         limite_ejido_municipal_texto_2009.setQueryable(true);

		                         Layer lineas_alta_tension_lineas_2009 = new Layer("lineas_alta_tension_lineas_2009", "gis_salta", "lineas_alta_tension_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         lineas_alta_tension_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         lineas_alta_tension_lineas_2009.setColor(new Color(0, 0, 255));
		                         lineas_alta_tension_lineas_2009.setMouseOverColor(Color.CYAN);
		                         lineas_alta_tension_lineas_2009.setQueryable(true);

		                         Layer numeros_catastro_textos_2009 = new Layer("numeros_catastro_textos_2009", "gis_salta", "numeros_catastro_textos_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         numeros_catastro_textos_2009.setProjectionType(CoordinateSystems.GK);
		                         numeros_catastro_textos_2009.setColor(new Color(0, 0, 255));
		                         numeros_catastro_textos_2009.setMouseOverColor(Color.CYAN);
		                         numeros_catastro_textos_2009.setQueryable(true);

		                         Layer retiros150_300_poligono_2009 = new Layer("retiros150_300_poligono_2009", "gis_salta", "retiros150_300_poligono_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         retiros150_300_poligono_2009.setProjectionType(CoordinateSystems.GK);
		                         retiros150_300_poligono_2009.setColor(new Color(0, 0, 255));
		                         retiros150_300_poligono_2009.setMouseOverColor(Color.CYAN);
		                         retiros150_300_poligono_2009.setQueryable(true);

		                         Layer retiros150_poligono_2009 = new Layer("retiros150_poligono_2009", "gis_salta", "retiros150_poligono_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         retiros150_poligono_2009.setProjectionType(CoordinateSystems.GK);
		                         retiros150_poligono_2009.setColor(new Color(0, 0, 255));
		                         retiros150_poligono_2009.setMouseOverColor(Color.CYAN);
		                         retiros150_poligono_2009.setQueryable(true);

		                         Layer retiros300_poligono_2009 = new Layer("retiros300_poligono_2009", "gis_salta", "retiros300_poligono_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         retiros300_poligono_2009.setProjectionType(CoordinateSystems.GK);
		                         retiros300_poligono_2009.setColor(new Color(0, 0, 255));
		                         retiros300_poligono_2009.setMouseOverColor(Color.CYAN);
		                         retiros300_poligono_2009.setQueryable(true);

		                         Layer rutas_nacionales_lineas_2009 = new Layer("rutas_nacionales_lineas_2009", "gis_salta", "rutas_nacionales_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         rutas_nacionales_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         rutas_nacionales_lineas_2009.setColor(new Color(255, 204, 0));
		                         rutas_nacionales_lineas_2009.getLayerConfig().getStyleConfig().setLineWidth(3);
		                         rutas_nacionales_lineas_2009.setMouseOverColor(Color.CYAN);
		                         rutas_nacionales_lineas_2009.setQueryable(true);

		                         Layer rutas_provinciales_lineas_2009 = new Layer("rutas_provinciales_lineas_2009", "gis_salta", "rutas_provinciales_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         rutas_provinciales_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         rutas_provinciales_lineas_2009.setColor(new Color(51, 204, 0));
		                         rutas_provinciales_lineas_2009.getLayerConfig().getStyleConfig().setLineWidth(3);
		                         rutas_provinciales_lineas_2009.setMouseOverColor(Color.CYAN);
		                         rutas_provinciales_lineas_2009.setQueryable(true);

		                         Layer san_lorenzo_lineas_2009 = new Layer("san_lorenzo_lineas_2009", "gis_salta", "san_lorenzo_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         san_lorenzo_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         san_lorenzo_lineas_2009.setColor(new Color(204, 204, 204));
		                         san_lorenzo_lineas_2009.setMouseOverColor(Color.CYAN);
		                         san_lorenzo_lineas_2009.setQueryable(true);

		                         Layer san_lorenzo_textos_2009 = new Layer("san_lorenzo_textos_2009", "gis_salta", "san_lorenzo_textos_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         san_lorenzo_textos_2009.setProjectionType(CoordinateSystems.GK);
		                         san_lorenzo_textos_2009.setColor(new Color(0, 0, 255));
		                         san_lorenzo_textos_2009.setMouseOverColor(Color.CYAN);
		                         san_lorenzo_textos_2009.setQueryable(true);

		                         Layer vaqueros_lineas_2009 = new Layer("vaqueros_lineas_2009", "gis_salta", "vaqueros_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         vaqueros_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         vaqueros_lineas_2009.setColor(new Color(204, 204, 204));
		                         vaqueros_lineas_2009.setMouseOverColor(Color.CYAN);
		                         vaqueros_lineas_2009.setQueryable(true);

		                         Layer vias_rapidas_lineas_2009 = new Layer("vias_rapidas_lineas_2009", "gis_salta", "vias_rapidas_lineas_2009", "the_geom", "1=1 AND isvalid(the_geom)", "gid");
		                         vias_rapidas_lineas_2009.setProjectionType(CoordinateSystems.GK);
		                         vias_rapidas_lineas_2009.setColor(new Color(0, 0, 255));
		                         vias_rapidas_lineas_2009.getLayerConfig().getStyleConfig().setLineWidth(3);
		                         vias_rapidas_lineas_2009.setMouseOverColor(Color.CYAN);
		                         vias_rapidas_lineas_2009.setQueryable(true);

		                         Layer escuelas_2005 = new Layer("Escuelas_Escrutinio_2005", "escrutinio_salta", "escuelas_2005", "the_geom", "1=1 AND st_isvalid(the_geom)", "idestablecimiento", "circuito");
		                         escuelas_2005.setProjectionType(CoordinateSystems.GK);
		                         escuelas_2005.setColor(Color.BLACK);
		                         escuelas_2005.setMouseOverColor(Color.MAGENTA);

		                         Layer circuito_3 = new Layer("Circuito 3", "escrutinio_salta", "circuito_3_poligonos", "the_geom", "1=1 AND st_isvalid(the_geom)", "idsector", "");
		                         circuito_3.setProjectionType(CoordinateSystems.GK);
		                         circuito_3.setColor(Color.BLACK);
		                         circuito_3.setMouseOverColor(Color.MAGENTA);
		                         circuito_3.setQueryable(true);

		                         secciones.setOn();

		                         GaiaEnvironment.layerGroups.add(infraestructuraGroup);
		                         GaiaEnvironment.layerGroups.add(recaudacionGroup);
		                         GaiaEnvironment.layerGroups.add(socialGroup);
		                         GaiaEnvironment.layerGroups.add(vialGroup);
		                         GaiaEnvironment.layerGroups.add(municipiosGroup);
		                         GaiaEnvironment.layerGroups.add(topografiaGroup);
		                         GaiaEnvironment.layerGroups.add(limitesGroup);
		                         GaiaEnvironment.layerGroups.add(ordenanzasGroup);
		                         GaiaEnvironment.layerGroups.add(escrutinioGroup);
		                         GaiaEnvironment.layerGroups.add(parcelasGroup);
		                         
		                         catastralGroup.add(secciones);
		                         catastralGroup.add(manzanas_vinculadas);
		                         catastralGroup.add(calles);
		                         catastralGroup.add(parcelas_catastral);
		                         catastralGroup.add(castanares_grupos_poligonos_2009);
		                         catastralGroup.add(castanares_grupos_textos_2009);
		                         catastralGroup.add(numeros_catastro_textos_2009);
		                         infraestructuraGroup.add(pavimento);
		                         infraestructuraGroup.add(estructuras_viales);
		                         infraestructuraGroup.add(barrios);
		                         infraestructuraGroup.add(farolas);
		                         infraestructuraGroup.add(redcloacal_bocasderegistro);
		                         infraestructuraGroup.add(redcloacal_cojinetes);
		                         infraestructuraGroup.add(redcloacal);
		                         infraestructuraGroup.add(reddeaguapotable);
		                         infraestructuraGroup.add(reddegasnatural);
		                         infraestructuraGroup.add(canales_lineas_2009);
		                         infraestructuraGroup.add(escuelas_puntos_2009);
		                         infraestructuraGroup.add(escuelas_textos_2009);
		                         infraestructuraGroup.add(espacios_verdes_futuro_poligono_2009);
		                         infraestructuraGroup.add(espacios_verdes_poligono_2009);
		                         infraestructuraGroup.add(ffcc_2009);
		                         infraestructuraGroup.add(infraestructura_textos_2009);
		                         infraestructuraGroup.add(lineas_alta_tension_lineas_2009);
		                         recaudacionGroup.add(tgi_parcelas_sa);
		                         recaudacionGroup.add(impinm_parcelas_sa);
		                         recaudacionGroup.add(valfis_parcelas_sa);
		                         recaudacionGroup.add(comercios);
		                         socialGroup.add(ayudas_parcelas_sa);
		                         socialGroup.add(asentamientos_poligono_2009);
		                         socialGroup.add(asentamientos_textos_2009);
		                         socialGroup.add(casosEnfermedades);
		                         socialGroup.add(casosDelitos);
		                         vialGroup.add(autopistas_lineas_2009);
		                         vialGroup.add(autopistas_proyectadas_2009);
		                         vialGroup.add(rutas_nacionales_lineas_2009);
		                         vialGroup.add(rutas_provinciales_lineas_2009);
		                         vialGroup.add(vias_rapidas_lineas_2009);
		                         municipiosGroup.add(cerrillos_calles_texto_2009);
		                         municipiosGroup.add(cerrillos_informat_texto_2009);
		                         municipiosGroup.add(cerrillos_manzanas_lineas_2009);
		                         municipiosGroup.add(san_lorenzo_lineas_2009);
		                         municipiosGroup.add(san_lorenzo_textos_2009);
		                         municipiosGroup.add(vaqueros_lineas_2009);
		                         topografiaGroup.add(cota25_linea_2009);
		                         topografiaGroup.add(cota50_linea_2009);
		                         topografiaGroup.add(cota100_linea_2009);
		                         topografiaGroup.add(cota_cima_punto_2009);
		                         topografiaGroup.add(cota_cima_texto_2009);
		                         topografiaGroup.add(cota_numeros_texto_2009);
		                         topografiaGroup.add(cota1225_linea_2009);
		                         topografiaGroup.add(cota1225_texto_2009);
		                         limitesGroup.add(limite_ejido_municipal_lineas_2009);
		                         limitesGroup.add(limite_ejido_municipal_texto_2009);
		                         limitesGroup.add(limite_dpto_lineas_2009);
		                         limitesGroup.add(limite_dpto_texto_2009);
		                         ordenanzasGroup.add(retiros150_poligono_2009);
		                         ordenanzasGroup.add(retiros150_300_poligono_2009);
		                         ordenanzasGroup.add(retiros300_poligono_2009);

		                         escrutinioGroup.add(escuelas_2005);
		                         escrutinioGroup.add(circuito_3);
		                         escrutinioGroup.add(parcelas_escrutinio);
		                         
		                         parcelasGroup.add(parcelas_1);
		                         parcelasGroup.add(parcelas_2);
		                         parcelasGroup.add(parcelas_3);
		                         
		                         cityMap.addLayerGroup(catastralGroup);
		                         cityMap.addLayerGroup(infraestructuraGroup);
		                         cityMap.addLayerGroup(recaudacionGroup);
		                         cityMap.addLayerGroup(socialGroup);
		                         cityMap.addLayerGroup(vialGroup);
		                         cityMap.addLayerGroup(municipiosGroup);
		                         cityMap.addLayerGroup(topografiaGroup);
		                         cityMap.addLayerGroup(limitesGroup);
		                         cityMap.addLayerGroup(ordenanzasGroup);
		                         cityMap.addLayerGroup(escrutinioGroup);
		                         cityMap.addLayerGroup(parcelasGroup);
		                 
		                         JInternalFrame layerListFrame = new JInternalFrame("Listado de Layers");
		                         layerListFrame.setClosable(false);
		                         LayerListPanel layerListPanel = new LayerListPanel();
		                         layerListFrame.getContentPane().add(layerListPanel, null);
		                         layerListFrame.setBounds(0, 0, 350, 150);
		                         layerListFrame.setResizable(true);
		                         layerListFrame.setVisible(false);
		                         layerListPanel.setDrawPanel(cityMap);

		                         MapBasicTools mapTools = new MapBasicTools();
		                         mapTools.setVertical();
		                         mapTools.addTool(MapBasicToolsPanel.ZOOM_IN_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.ZOOM_OUT_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.ZOOM_EXTENTS_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.RULE_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.QUERY_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.ADDRESSES_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.STREETS_EDITION_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.INFRASTRUCTURES_EDITION_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.PRINT_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.MULTIQUERY_TOOL);
		                         mapTools.addTool(MapBasicToolsPanel.LAYER_EDITION_TOOL);
		                         
		                         mapTools.setClosable(false);

		                         //_desktop.setActive(true);
		                         _desktop.add(coordinateViewer);
		                         _desktop.add(cityMap);
		                         _desktop.add(layerListFrame);
		                         _desktop.add(mapTools);

		                         layerListFrame.show();
		                         mapTools.setDrawPanel(cityMap);
		                         mapTools.show();
		                         btnTest.setEnabled(false);
		                         btnTest.setVisible(false);
		                         
		                         coordinateViewer.setVisible(false);

		                         /*GaiaCadastralFinderPanel calendarIFrame = new GaiaCadastralFinderPanel();
		                         calendarIFrame.setDesktop(_desktop);
		                         calendarIFrame.setDrawEngine(cityMap);
		                         calendarIFrame.setVisible(false);
		                         calendarIFrame.start();
		                         _desktop.setBottomRightComponent(calendarIFrame);*/
		                          GAIA_MODULE_STATUS = STATUS_RUNNING;
		                     }
		                 }

		             }
		 );
		 _desktop.addButton(btnTest);
		/**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		Environment.jpStatusBar.setAction("Gaia loaded");
		Debug.println("GAIA boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		break;
	    case INIT_HALT :
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rc3rdpartyApps(int _action, BasicDesktop _desktop) {
	/** REPORTING JFREE 3.6.1 */
	ClassicEngineBoot.getInstance().start();
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

    public static void rcResourcesModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (RESOURCES_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting RESOURCES");
		    Environment.jpStatusBar.setAction("Loading Resources");
		    ASSETS_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.resources_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */

		    
		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    RESOURCES_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Resources loaded");
		    Debug.println("RESOURCES boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("RESOURCES already running!");
		}
		break;
	    case INIT_HALT :
		if (RESOURCES_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("RESOURCES halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
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

    public static void rcTaxes(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (TAXES_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting TAXES");
		    Environment.jpStatusBar.setAction("Loading Taxes");
		    TAXES_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setIconAt(_desktop.getIdDesktop(), IconTypes.resources_32x32);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setStartColor(allModulesStartColor[_desktop.getIdDesktop()]);
		    _desktop.setEndColor(allModulesEndColor);
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */

		    /**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		    TAXES_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Taxes loaded");
		    Debug.println("TAXES boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("TAXES already running!");
		}
		break;
	    case INIT_HALT :
		if (TAXES_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("TAXES halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default :
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcReportsModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START :
		if (REPORTS_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting REPORTS");
		    Environment.jpStatusBar.setAction("Loading Reports");
		    REPORTS_MODULE_STATUS = STATUS_STARTING;
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
		    REPORTS_MODULE_STATUS = STATUS_RUNNING;
		    Environment.jpStatusBar.setAction("Reports loaded");
		    Debug.println("REPORTS boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		} else {
		    Debug.println("REPORTS already running!");
		}
		break;
	    case INIT_HALT :
		if (REPORTS_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("REPORTS halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
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

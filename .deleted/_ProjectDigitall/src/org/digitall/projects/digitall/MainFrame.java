package org.digitall.projects.digitall;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

import org.digitall.apps.cashflow.interfaces.CashFlowToolBar;
import org.digitall.apps.cashflowclient.interfaces.CashFlowClientToolBar;
import org.digitall.apps.systemmanager.ControlMain;
import org.digitall.lib.common.SplashWindow;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.Login;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicDesktop;
import org.digitall.lib.components.basic.BasicMenuBar;
import org.digitall.lib.components.basic.BasicMenuItem;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicRadioButton;
import org.digitall.lib.components.basic.BasicTabbedPane;
import org.digitall.lib.components.basic.BasicToolBarButton;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;
import org.digitall.projects.digitall.maps.SystemMap;

public class MainFrame extends JFrame {

    private BorderLayout layoutMain = new BorderLayout();
    private BasicPanel jpCenterContainer = new BasicPanel(new BorderLayout());
    private BasicMenuBar menuBar = new BasicMenuBar();
    private JMenu menuFile = new JMenu();
    private BasicMenuItem menuFileExit = new BasicMenuItem();
    private JMenu menuHelp = new JMenu();
    private BasicMenuItem menuHelpAbout = new BasicMenuItem();
    private BasicDesktop mainDesktop = new BasicDesktop(-1, "MAIN DESKTOP");
    private BasicDesktop[] desktops;
    private boolean active = true;
    private CashFlowToolBar tbCashFlowMgmt;
    private CashFlowClientToolBar tbCashFlowClient;
    private MainPanel jpNorthContainer = new MainPanel(this);
    private BasicTabbedPane mainTabbedPane = new BasicTabbedPane(BasicTabbedPane.LEFT, BasicTabbedPane.SCROLL_TAB_LAYOUT) {

	    public void setSelectedIndex(int _index) {
		if (getSelectedIndex() != -1) {
		    mainDesktop.switchDesktops(Environment.desktops[getSelectedIndex()], Environment.desktops[_index]);
		} else {
		    Environment.desktops[_index].setVisible(true);
		    Environment.desktops[_index].setActive(true);
		    mainDesktop.relocateDesktops();
		}
		Environment.addDesktopIndexToHistory(_index);
		super.setSelectedIndex(_index);
	    }

	}
    ;

    public MainFrame() {
	try {
	    /*patch para mostrar una splash antes del login*/
	    /*endpatch*/
	    setUndecorated(true);
	    setIconImage(IconTypes.lock_16x16.getImage());
	    Login loginWindow = new Login(Environment.organization, Environment.developer, true, false);
	    ComponentsManager.centerWindow(loginWindow);
	    //inicio.setLocation((int)inicio.getLocation().getX(), (int)(sw.getLocation().getY() + sw.getBounds().getHeight()));
	    loginWindow.setModal(true);
	    loginWindow.setVisible(true);
	    if (loginWindow.getValidado()) {
		//LibSQL.setDateTime();
		jbInit();
		//setUndecorated(true);
		//setResizable(false);
		//sw.dispose();
	    } else {
		System.exit(0);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	startDesktop();
    }

    private void startDesktop() {
	/**
	 * Muestro la pantalla de bienvenida SplashWindow
	 * */
	setVisible(false);
	SplashWindow sw = new SplashWindow();
	ComponentsManager.centerWindow(sw);
	sw.setVisible(true);
	sw.setAlwaysOnTop(true);
	sw.setModal(true);
	//this.setJMenuBar(menuBar);
	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	this.getContentPane().setLayout(layoutMain);
	this.getContentPane().add(jpNorthContainer, BorderLayout.NORTH);
	jpNorthContainer.setPreferredSize(new Dimension(jpNorthContainer.getWidth(), 100));
	jpCenterContainer.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds());
	this.setTitle(Environment.organization + " - " + Environment.developer);
	menuFile.setText("File");
	menuFile.setMnemonic('F');
	menuFileExit.setText("Exit");
	menuFileExit.setMnemonic('E');
	menuFileExit.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent ae) {
					closeApplication();
				    }

				}
	);
	menuHelp.setText("Help");
	menuHelp.setMnemonic('H');
	menuHelpAbout.setText("About");
	menuHelpAbout.setMnemonic('A');
	menuHelpAbout.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent ae) {
					 helpAbout_ActionPerformed(ae);
				     }

				 }
	);
	menuHelp.add(new BasicToolBarButton("BasicToolBarButton!"));
	menuHelp.add(new AcceptButton());
	menuHelp.add(new BasicRadioButton("Radio", true));
	menuHelp.add(new BasicCheckBox("Check", true));
	menuFile.add(menuFileExit);
	menuBar.add(menuFile);
	menuHelp.addSeparator();
	menuHelp.add(menuHelpAbout);
	menuBar.add(menuHelp);
	//para poner un menú alineado a la derecha
	//menuBar.add(Box.createHorizontalGlue());
	//menuBar.add(subMenu);
	this.getContentPane().add(Environment.jpStatusBar, BorderLayout.SOUTH);
	//jpCenterContainer.add(mainTabbedPane, BorderLayout.CENTER);
	jpCenterContainer.add(mainDesktop, BorderLayout.CENTER);
	jpCenterContainer.add(Environment.jpStatusBar, BorderLayout.SOUTH);
	//this.getContentPane().add(tbModules, BorderLayout.WEST);
	this.getContentPane().add(jpCenterContainer, BorderLayout.CENTER);
	menuFile.setModel(new DefaultButtonModel());
	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {

		       public void windowClosing(WindowEvent e) {
			   closeApplication();
		       }

		   }
	);
	desktops = new BasicDesktop[8];
	desktops[0] = new BasicDesktop(0, Environment.MODULE_MAIN);
	desktops[1] = new BasicDesktop(1, Environment.MODULE_CASHFLOW);
	desktops[2] = new BasicDesktop(2, Environment.MODULE_RESOURCES);
	desktops[3] = new BasicDesktop(3, Environment.MODULE_RESOURCES);
	desktops[4] = new BasicDesktop(4, Environment.MODULE_REPORTS);
	desktops[5] = new BasicDesktop(5, Environment.MODULE_TAXES);
	desktops[6] = new BasicDesktop(6, Environment.MODULE_STICKYNOTES);
	desktops[7] = new BasicDesktop(7, Environment.MODULE_GAIA);
	//desktops[7] = new BasicDesktop(6, Environment.MODULE_SYSTEMMAP);
	Environment.setMainTabbedPane(mainTabbedPane);
	Environment.setDesktops(desktops);
	for (int i = 0; i < desktops.length; i++) {
	    desktops[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	    mainTabbedPane.addTab(desktops[i].getName(), null, null, "Click y selecciona el escritorio de trabajo");
	    mainDesktop.add(desktops[i]);
	    desktops[i].setOpaco(false);
	    desktops[i].setVisible(false);
	}
	try {
	    mainTabbedPane.setSelectedIndex(Integer.parseInt(Environment.cfg.getProperty("SelectedTab")));
	    if (mainTabbedPane.getSelectedIndex() < 0) {
		mainTabbedPane.setSelectedIndex(0);
	    }
	} catch (Exception x) {
	    mainTabbedPane.setSelectedIndex(0);
	}
	//startDesktop();
	/*patch para JDesktop con Scrollbars
	JScrollPane xk = new JScrollPane(desktops[0]);
	desktops[0].setPreferredSize(new Dimension(10000,10000));
	mainTabbedPane.add(xk, "KK");
	endpatch*/
	setBounds(Environment.graphicsDevice.getDefaultConfiguration().getBounds());
	validate();
	if (active) {
	    Environment.jpStatusBar.setAction("Loading environment");
	    Environment.jpStatusBar.setIndeterminate(true);
	    if (Environment.debugMode) {
		//Console.hookStandards();
		//Console.println("Debugging...");
	    }
	    //if (Environment.sessionUser.equals("admin")) {
	    Boot.rcMainModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_MAIN));
	    Boot.rc3rdpartyApps(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_MAIN));
	    Boot.rcCashFlowAdminModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_CASHFLOW));
	    Boot.rcResourcesRequestsModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_RESOURCES));
	    Boot.rcResourcesControlModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_RESOURCES));
	    //Boot.rcLogisticsModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_LOGISTICS));
	    //Boot.rcTasksModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_TASKS));
	    //Boot.rcStickyNotesModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_STICKYNOTES));
	    /*} else {
		ResultSet systems = LibSQL.exQuery("Select distinct substr(groname, 0, strpos(groname,'_')) as system From pg_group where groname like 'cr%' and (Select usesysid from pg_catalog.pg_user where usename = '" + Environment.sessionUser + "') = any(grolist)");
		try {
		    while (systems.next()) {
			if (systems.getString("system").equals(CRLOGISTIC_MODULE)) {
			    Boot.rcLogisticsModule(Boot.INIT_START, Environment.getDesktop("Logistica"));
			}
		    }
		} catch (SQLException e) {
		    e.printStackTrace()r;
		}
	    }*/
	    //Start calculator
	    Environment.jpStatusBar.setAction("All modules loaded");
	    Environment.jpStatusBar.setIndeterminate(false);
	    setVisible(true);
	    sw.dispose();
	    Advisor.messagePopupWindow("Bienvenido usuario " + Environment.sessionUser, "");
	    Environment.jpStatusBar.setUser(Environment.sessionUser);
	}
    }

    public void closeApplication() {
	int answer = JOptionPane.showConfirmDialog(this, "¿Desea cerrar el sistema?", "Cerrar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	if (answer == JOptionPane.YES_OPTION) {
	    //AVISAR A LOS MODULOS QUE SE VA A CERRAR TODO!!!
	    /**
	     * GRABAR OPCIONES
	     * */
	    Environment.cfg.setProperty("SelectedTab", String.valueOf(Environment.mainTabbedPane.getSelectedIndex()));
	    System.exit(0);
	}
    }

    public void helpAbout_ActionPerformed(ActionEvent e) {
	JOptionPane.showMessageDialog(this, new AboutBoxPanel(), "About", JOptionPane.PLAIN_MESSAGE);
    }

    private void bAdminModule_actionPerformed(ActionEvent e) {
	//org.digitall.lib.components.Advisor.messageBox("Administration Module Loaded", "Administration");
	ControlMain _temp = new ControlMain();
	ComponentsManager.centerWindow(_temp);
	_temp.show();
    }

    private void endDesktop() {
	System.exit(0);
	/*LibSQL.closeConnection();
	Environment.mainDesktop.removeAll();
	Environment.mainDesktop.updateUI();
	if (chatModuleLoaded) {
	    chatModuleLoaded = false;
	    chatPanel.disconnect();
	    chatPanel.dispose();
	}
	btnTask.setEnabled(false);
	btnLogisticsModule.setEnabled(false);*/
    }

    private void btnCashFlowModule_actionPerformed(ActionEvent e) {
	tbCashFlowMgmt.setVisible(!tbCashFlowMgmt.isVisible());
    }

    private void btnCashFlowClientModule_actionPerformed(ActionEvent e) {
	tbCashFlowClient.setVisible(!tbCashFlowClient.isVisible());
    }

    private void btnNewsList_actionPerformed(ActionEvent e) {
    }

}

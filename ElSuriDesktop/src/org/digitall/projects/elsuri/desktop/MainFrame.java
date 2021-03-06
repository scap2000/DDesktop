/**
 LIMITACIÓN DE RESPONSABILIDAD - COPYRIGHT - [Español]
 ================================================================================
 El Suri - Entorno JAVA de Trabajo y Desarrollo para Gobierno Electrónico
 ================================================================================

 Información del Proyecto:  http://elsuri.sourceforge.net

 Copyright (C) 2007-2010 por D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO.
 D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO es propiedad de
 Lic. Santiago Cassina (santiago@digitallsh.com.ar - scap2000@yahoo.com) y
 Marcelo D'Ambrosio (marcelo@digitallsh.com.ar - marcelodambrosio@gmail.com)
 http://www.digitallsh.com.ar

 Este programa es software libre: usted puede redistribuirlo y/o modificarlo
 bajo los términos de la Licencia Pública General GNU publicada
 por la Fundación para el Software Libre, ya sea la versión 3
 de la Licencia, o (a su elección) cualquier versión posterior.

 Este programa se distribuye con la esperanza de que sea útil, pero
 SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita
 MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO.
 Consulte los detalles de la Licencia Pública General GNU para obtener
 una información más detallada.

 Debería haber recibido una copia de la Licencia Pública General GNU
 junto a este programa.
 En caso contrario, consulte <http://www.gnu.org/licenses/>.

 DISCLAIMER - COPYRIGHT - [English]
 =====================================================================================
 El Suri - JAVA Management & Development Environment for Electronic Government
 =====================================================================================

 Project Info:  http://elsuri.sourceforge.net

 Copyright (C) 2007-2010 by D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO.
 D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO is owned by
 Lic. Santiago Cassina (santiago@digitallsh.com.ar - scap2000@yahoo.com) and
 Marcelo D'Ambrosio (marcelo@digitallsh.com.ar - marcelodambrosio@gmail.com)
 http://www.digitallsh.com.ar

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
/**
 * MainFrame.java
 *
 * */

package org.digitall.projects.elsuri.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;

import org.digitall.common.systemmanager.UserInfo;
import org.digitall.lib.common.OrganizationInfo;
import org.digitall.lib.common.SplashWindow;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.JArea;
import org.digitall.lib.components.Login;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicDesktop;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicMenuBar;
import org.digitall.lib.components.basic.BasicMenuItem;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicRadioButton;
import org.digitall.lib.components.basic.BasicTabbedPane;
import org.digitall.lib.components.basic.BasicToolBarButton;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.ssl.SSLConfig;

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
    private MainPanel jpNorthContainer = new MainPanel(this);
    private boolean fullScreen = false;
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
	    Environment.SYSTEM_VERSION = "ElSuriDesktop [2012-03-16] ß";
	    setUndecorated(true);
	    setIconImage(IconTypes.lock_16x16.getImage());

	    checkCerts();
	    
	    Login loginWindow = new Login(Environment.organization, Environment.developer, true, false);
	    ComponentsManager.centerWindow(loginWindow);
	    loginWindow.setModal(true);
	    loginWindow.setVisible(true);
	    if (loginWindow.getValidado()) {
		//LibSQL.setDateTime();
		jbInit();
	    } else {
		System.exit(0);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	Environment.mainFrame = this;
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
	this.getContentPane().setLayout(layoutMain);
	this.getContentPane().add(jpNorthContainer, BorderLayout.NORTH);
	jpNorthContainer.setPreferredSize(new Dimension(jpNorthContainer.getWidth(), 100));
	jpCenterContainer.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	this.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds());
	this.setTitle(Environment.organization + " - " + Environment.developer);
	menuHelp.add(new BasicToolBarButton("BasicToolBarButton!"));
	menuHelp.add(new AcceptButton());
	menuHelp.add(new BasicRadioButton("Radio", true));
	menuHelp.add(new BasicCheckBox("Check", true));
	menuFile.add(menuFileExit);
	menuBar.add(menuFile);
	menuHelp.addSeparator();
	menuHelp.add(menuHelpAbout);
	menuBar.add(menuHelp);
	this.getContentPane().add(Environment.jpStatusBar, BorderLayout.SOUTH);
	jpCenterContainer.add(mainDesktop, BorderLayout.CENTER);
	jpCenterContainer.add(Environment.jpStatusBar, BorderLayout.SOUTH);
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
	desktops[0] = new BasicDesktop(Boot.MAIN_MODULE, Environment.MODULE_MAIN);
	desktops[1] = new BasicDesktop(Boot.TAXES_MODULE, Environment.MODULE_TAXES);
	desktops[2] = new BasicDesktop(Boot.CASHFLOW_MODULE, Environment.MODULE_CASHFLOW);
	desktops[3] = new BasicDesktop(Boot.RESOURCES_MODULE, Environment.MODULE_RESOURCES);
	desktops[4] = new BasicDesktop(Boot.ASSETS_MODULE, Environment.MODULE_ASSETS);
	desktops[5] = new BasicDesktop(Boot.REPORTS_MODULE, Environment.MODULE_REPORTS);
	desktops[6] = new BasicDesktop(Boot.GAIA_MODULE, Environment.MODULE_GAIA);
	desktops[7] = new BasicDesktop(Boot.STICKYNOTES_MODULE, Environment.MODULE_STICKYNOTES);
	Environment.setMainTabbedPane(mainTabbedPane);
	Environment.setDesktops(desktops);
	
	desktops[0].setStartColor(new Color(81, 72, 42));
	desktops[0].setEndColor(new Color(30, 30, 30));
	desktops[1].setStartColor(new Color(17, 27, 58));
	desktops[1].setEndColor(new Color(30, 30, 30));
	desktops[2].setStartColor(new Color(44, 78, 28));
	desktops[2].setEndColor(new Color(30, 30, 30));
	desktops[3].setStartColor(new Color(66, 17, 60));
	desktops[3].setEndColor(new Color(30, 30, 30));
	desktops[4].setStartColor(new Color(90, 18, 18));
	desktops[4].setEndColor(new Color(30, 30, 30));
	desktops[5].setStartColor(new Color(182, 162, 94));
	desktops[5].setEndColor(new Color(30, 30, 30));
	desktops[6].setStartColor(new Color(180, 91, 23));
	desktops[6].setEndColor(new Color(30, 30, 30));
	desktops[7].setStartColor(new Color(81, 72, 42));
	desktops[7].setEndColor(new Color(30, 30, 30));
	
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
	    Boot.rc3rdpartyApps(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_MAIN));
	    Boot.rcAssets(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_ASSETS));
	    Boot.rcCashFlowModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_CASHFLOW));
	    Boot.rcGaia(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_GAIA));
	    Boot.rcMainModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_MAIN));
	    Boot.rcReportsModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_REPORTS));
	    Boot.rcResourcesModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_RESOURCES));
	    Boot.rcStickyNotesModule(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_STICKYNOTES));
	    Boot.rcTaxes(Boot.INIT_START, Environment.getDesktop(Environment.MODULE_TAXES));
	    Environment.jpStatusBar.setAction("All modules loaded");
	    Environment.jpStatusBar.setIndeterminate(false);
	    setVisible(true);
	    sw.dispose();
	    Advisor.messagePopupWindow("Bienvenido usuario " + Environment.sessionUser, "");
	    Environment.jpStatusBar.setUser(Environment.sessionUser);

	    showLicense();

	    if (Environment.cfg.getProperty("usercode").equals("usercode")) {
	        ExtendedInternalFrame _userInfo = new ExtendedInternalFrame("Información de Usuario", IconTypes.configuracion_base_de_sueldos);
	        _userInfo.setClosable(false);
	        _userInfo.setCentralPanel(new UserInfo());
	        _userInfo.setDesktop(Environment.getDesktop(Environment.MODULE_MAIN));
	        _userInfo.setIcon(true);
	        Advisor.messageBox("Por favor, tómese un momento para completar algunos datos\nEstos datos son opcionales y para fines estadísticos\nNo serán divulgados", "Datos personales");
	        Environment.mainTabbedPane.setSelectedIndex(Environment.getDesktopIndex(Environment.MODULE_MAIN));
	        _userInfo.show();
	    } else {
	        new ExtendedInternalFrame("Información de Usuario", IconTypes.configuracion_base_de_sueldos, UserInfo.class, true, Environment.getDesktop(Environment.MODULE_MAIN));
	    }
	    
	    KeyStroke fullScreenSwitch = KeyStroke.getKeyStroke( KeyEvent.VK_F11, 0);
	    JRootPane rootPane = getRootPane();
	    ActionListener fullScreenListener = new ActionListener()  {

	            public void actionPerformed(ActionEvent actionEvent) {
	                switchFullScreen();
	            }

	        };
	    rootPane.registerKeyboardAction(fullScreenListener, "fullScreenSwitch", fullScreenSwitch, JComponent.WHEN_IN_FOCUSED_WINDOW );
	}
    }

    public void switchFullScreen() {
	jpNorthContainer.setVisible(fullScreen);
	Environment.jpStatusBar.setVisible(fullScreen);
	fullScreen = !fullScreen;
    }

    public void showLicense() {
	if (!Environment.cfg.getProperty("showlicense").equals("false")) {
	    final BasicDialog _licenseDialog = new BasicDialog() {
	        public void closeBasicDialog() {
		    dispose();
	        }
	    };

	    JArea _jaLicense = new JArea();
	    _jaLicense.setContentType("text/html");
	    _jaLicense.setPage(Environment.mainClass.getResource("license.txt"));
	    _jaLicense.setEditable(false);
	    _licenseDialog.setLayout(new BorderLayout());
	    _licenseDialog.add(_jaLicense, BorderLayout.CENTER);
	    _licenseDialog.setModal(true);

	    JButton _ok = new JButton("ACEPTO");

	    _ok.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent actionEvent) {
			Environment.cfg.setProperty("showlicense", "false");
			_licenseDialog.closeBasicDialog();			
		    }
		});

	    _licenseDialog.add(_ok, BorderLayout.SOUTH);

	    _licenseDialog.setSize(new Dimension(500,300));
	    ComponentsManager.centerWindow(_licenseDialog);
	    _licenseDialog.setVisible(true);
	}
    }

    public void checkCerts() {
	if ( (SSLConfig.class.getResourceAsStream(SSLConfig.KEY_FILE_PATH) != null) &&
	     (SSLConfig.class.getResourceAsStream(SSLConfig.TRUST_FILE_PATH) != null)
	    ) {
	} else {
	    Advisor.messageBox("No se han encontrado los certificados SSL","Error");
	    System.exit(-1);
	}
    }

    public void closeApplication() {
	Advisor.closeApplication();
    }

}

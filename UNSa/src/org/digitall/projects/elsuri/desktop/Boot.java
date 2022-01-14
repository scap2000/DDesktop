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
 * Boot.java
 *
 * */
package org.digitall.projects.elsuri.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import org.digitall.apps.calendar.interfaces.CoordinatorStickyNote;
import org.digitall.apps.calendar.interfaces.DCalendar;
import org.digitall.apps.calendar.interfaces.StickyNote;
import org.digitall.apps.chwy.ChWYChatPanel;
import org.digitall.apps.jpgadmin.sqleditor.FunctionsList;
import org.digitall.apps.taskman.interfases.TaskManList;
import org.digitall.common.components.basic.BasicCard;
import org.digitall.common.data.PanelDictionary;
import org.digitall.common.geo.mapping.BasicDrawEngine;
import org.digitall.common.geo.mapping.components.LayerTree;
import org.digitall.common.geo.mapping.components.MapBasicTools;
import org.digitall.common.geo.mapping.components.MapBasicToolsPanel;
import org.digitall.common.mapper.CoordinateCalculator;
import org.digitall.common.mapper.CoordinateViewer;
import org.digitall.common.mapper.RuleViewer;
import org.digitall.common.systemmanager.interfaces.SystemInfo;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicConfig;
import org.digitall.lib.components.basic.BasicDesktop;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.DesktopButton;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.environment.Debug;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.geo.coordinatesystems.CoordinateSystems;
import org.digitall.lib.geo.gaia.GaiaEnvironment;
import org.digitall.lib.geo.mapping.classes.GeometrySet;
import org.digitall.lib.geo.mapping.classes.Layer;
import org.digitall.lib.geo.mapping.classes.LayerGroup;
import org.digitall.lib.geo.mapping.classes.LayerProfile;
import org.digitall.lib.html.BrowserLauncher;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;
import org.digitall.lib.sql.SQLImport;
import org.digitall.projects.apps.dbadmin_091.interfases.DBAdminPanel;
import org.digitall.projects.unsa.censodocpri.interfaces.Encuesta;

import org.digitall.projects.unsa.gaia.interfaces.GaiaDistrictPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;

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


    public static void rcGaia(int _action, final BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START:
		Debug.println("Booting GAIA");
		Environment.jpStatusBar.setAction("Loading Gaia");
		Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), _desktop.getStartColor());
		/**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		/**
		  * Boton TEST
		  * */
		final DesktopButton btnTest = new DesktopButton("Cargar versión de\nPrueba del GIS", IconTypes.gaia);
		btnTest.addActionListener(new ActionListener() {

			    public void actionPerformed(ActionEvent e) {
				if (GAIA_MODULE_STATUS != STATUS_RUNNING) {
				    loadGaia(_desktop);
				    btnTest.setEnabled(false);
				    btnTest.setVisible(false);
				    GAIA_MODULE_STATUS = STATUS_RUNNING;
				}
			    }

			});
		_desktop.addButton(btnTest);
		/**
		     * FIN CODIGO DE INICIO DEL MODULO
		     * */
		Environment.jpStatusBar.setAction("Gaia loaded");
		Debug.println("GAIA boot time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		break;
	    case INIT_HALT:
	    default:
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rc3rdpartyApps(int _action, BasicDesktop _desktop) {
	/** REPORTING PENTAHO 3.6.1 */
	 Thread _thread = new Thread(new Runnable() {
		public void run() {
		    ClassicEngineBoot.getInstance().start();
		}
	 });
	 _thread.start();
    }

    public static void rcMainModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START:
		if (MAIN_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting MAIN");
		    Environment.jpStatusBar.setAction("Loading Main");
		    MAIN_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setEnabledAt(_desktop.getIdDesktop(), true);
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), _desktop.getStartColor());
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
		    ExtendedInternalFrame chwy = new ExtendedInternalFrame("Chat", IconTypes.chat_32x32);
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
		     * Boton del WebMail
		     * */
		    /*DesktopButton btnWebMail = new DesktopButton("WebMail");
		    btnWebMail.setRolloverEnabled(true);
		    btnWebMail.setIcon(IconTypes.webmail_32x32);
		    btnWebMail.setRolloverIcon(IconTypes.webmail_ro_32x32);
		    btnWebMail.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   BrowserLauncher.openURL("http://www.digitallsh.com.ar/");
				       }

				   }
		    );
		    _desktop.addButton(btnWebMail);*/
		    /**
		     * INFORMACION DEL SISTEMA
		     * */
		    ExtendedInternalFrame systemInfo = new ExtendedInternalFrame("Info del Sistema", IconTypes.info_del_sistema);
		    SystemInfo jpSystemInfo = new SystemInfo();
		    systemInfo.setCentralPanel(jpSystemInfo);
		    systemInfo.setResizable(true);
		    systemInfo.setClosable(false);
		    systemInfo.setDesktop(_desktop);
		    systemInfo.setIcon(true);



		    boolean superUser = false;
		    try {
			ResultSet _result = LibSQL.exQuery("SELECT rolsuper FROM pg_roles WHERE rolname = session_user");
			if (_result.next()) {
			    superUser = _result.getBoolean("rolsuper");
			}
		    } catch (SQLException e) {
		    }
		    if (superUser) {
		        /**
		          * DICCIONARIO DEL SISTEMA
		          * */
		        new ExtendedInternalFrame("Dictionary", IconTypes.diccionario, PanelDictionary.class, true, _desktop);
		        /**
		         * DBAdmin
		         * */
		        new ExtendedInternalFrame("DBAdmin", IconTypes.info_del_sistema, DBAdminPanel.class, true, _desktop);
		        /**
		         * JPGAdmin
		         * */
		        new ExtendedInternalFrame("JPGAdmin", IconTypes.info_del_sistema, FunctionsList.class, true, _desktop);
		        /**
		         * SQL IMPORTER
		         * */
		        new ExtendedInternalFrame("SQLImport", IconTypes.info_del_sistema, SQLImport.class, true, _desktop);
		    }

		    /**
		     * TaskMan
		     * */
		    new ExtendedInternalFrame("TaskMan", IconTypes.info_del_sistema, TaskManList.class, true, _desktop);
		    
		    /**
		     * Instancio la fabulosa calculadora de coordenadas
		     * */
		    ExtendedInternalFrame geoCalc = new ExtendedInternalFrame("Calculadora geografica", IconTypes.geocalc_32x32);
		    CoordinateCalculator geoCalcPanel = new CoordinateCalculator();
		    geoCalc.setClosable(false);
		    geoCalc.setCentralPanel(geoCalcPanel);
		    geoCalc.setDesktop(_desktop);
		    geoCalc.setIcon(true);

		    DesktopButton _btnReadme = new DesktopButton("LÉAME");
		    _btnReadme.setRolloverEnabled(true);
		    _btnReadme.setIcon(IconTypes.webmail_32x32);
		    _btnReadme.setRolloverIcon(IconTypes.webmail_ro_32x32);
		    _btnReadme.addActionListener(new ActionListener() {

		                       public void actionPerformed(ActionEvent e) {

		                           BrowserLauncher.openURL(Environment.mainClass.getResource("README").toString());

		                           final BasicDialog _readmeDialog = new BasicDialog();
					   _readmeDialog.setTitle("Archivo README");

		                           JArea _jaReadme = new JArea();
		                           _jaReadme.setContentType("text/html");
		                           _jaReadme.setPage(Environment.mainClass.getResource("README"));
		                           _jaReadme.setEditable(false);
		                           _readmeDialog.setLayout(new BorderLayout());
		                           _readmeDialog.add(_jaReadme, BorderLayout.CENTER);
		                           _readmeDialog.setModal(true);

		                           JButton _ok = new JButton("CERRAR VENTANA");

		                           _ok.addActionListener(new ActionListener() {

		                                   public void actionPerformed(ActionEvent actionEvent) {
		                                       _readmeDialog.closeBasicDialog();                      
		                                   }
		                               });

		                           _readmeDialog.add(_ok, BorderLayout.SOUTH);

		                           _readmeDialog.setSize(new Dimension(550,300));
		                           ComponentsManager.centerWindow(_readmeDialog);
		                           _readmeDialog.setVisible(true);
		                       }

		                   }
		    );
		    _desktop.addButton(_btnReadme);

		    new ExtendedInternalFrame("Carga de Encuestas", IconTypes.recursos_humanos, Encuesta.class, true, _desktop);

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
	    case INIT_HALT:
		if (MAIN_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("MAIN halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default:
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcReportsModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START:
		if (REPORTS_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting REPORTS");
		    Environment.jpStatusBar.setAction("Loading Reports");
		    REPORTS_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), _desktop.getStartColor());
		    /**
		     * CODIGO DE INICIO DEL MODULO
		     * */
		    try {
			ResultSet cards = LibSQL.exQuery("SELECT idcard FROM org.cards ORDER BY idcard");
			while (cards.next()) {
			    BasicCard _card = new BasicCard(cards.getInt("idcard"));
			    _card.setClosable(false);
			    _card.setDesktop(_desktop);
			    _card.show();
			    ComponentsManager.setConfigurable(_card);
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
	    case INIT_HALT:
		if (REPORTS_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("REPORTS halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default:
	}
	LibSQL.setVerboseMode(true);
    }

    public static void rcStickyNotesModule(int _action, BasicDesktop _desktop) {
	LibSQL.setVerboseMode(false);
	long initTime = System.currentTimeMillis();
	switch (_action) {
	    case INIT_START:
		if (STICKYNOTES_MODULE_STATUS != STATUS_RUNNING) {
		    Debug.println("Booting STICKYNOTES");
		    Environment.jpStatusBar.setAction("Loading StickyNotes");
		    STICKYNOTES_MODULE_STATUS = STATUS_STARTING;
		    Environment.mainTabbedPane.setBackgroundAt(_desktop.getIdDesktop(), _desktop.getStartColor());
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

			    });
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
	    case INIT_HALT:
		if (STICKYNOTES_MODULE_STATUS != STATUS_STOPPED) {
		    //Shutdown module
		    //Debug.println("STICKYNOTES halt time: " + (System.currentTimeMillis() - initTime) + " milliseconds");
		}
	    default:
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
	//Color del texto en todos los tabs, el BasicTabbedPane tiene un metodo
	//que le pone otro color al texto del tab seleccionado
	UIManager.put("TabbedPane.foreground", BasicConfig.TABBEDPANE_FOREGROUND);
	//Color del borde del tabbedpane, el que se dibuja entre el tab y el container, quizá
	//sea preferible poner "TabbedPane.contentOpaque", false
	UIManager.put("TabbedPane.contentOpaque", false);
	//UIManager.put("TabbedPane.contentAreaColor", BasicConfig.TABBEDPANE_TAB_AREA_BACKGROUND);
	//Este no da bola
	UIManager.put("TabbedPane.background", BasicConfig.TABBEDPANE_TAB_AREA_BACKGROUND);
	//Los tabs sobreescribiron el borde?
	//UIManager.put("TabbedPane.tabsOverlapBorder", false);
	//No se para que sirve, pero le saco todos los espacios
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
	//Color de fondo del menuitem (el que esta enganchado con un JMenu)
	UIManager.put("MenuItem.background", BasicConfig.RESALTADOR_COLOR_UIRESOURCE);
	//Color de fondo del menuitem cuando esta seleccionado (el que esta enganchado con un JMenu)
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
	Advisor.closeApplication();
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

    private static void loadGaia(final BasicDesktop _desktop) {
	Thread _thread = new Thread(new Runnable() {

		public void run() {
		    try {
		        String _ayuda = "<html><p align=center><font size=5><b>Aviso sobre el GIS</b></font></p>" + 
		                        "<br><p align=center><b>Sobre los sistemas de coordenadas</b>:</p>" +
		                        "<p align=left>En este Sistema de Información Geográfica coexisten diferentes sistemas" +
		                        " de coordenadas: <i>Coordenadas Planas Gauss Krüger (sistema legal en Argentina)</i> para los planos (ciudades) y" +
		                        " <i>Coordenadas Esféricas (Latitud/Longitud gº m' ss.ssss'')</i> para lo que corresponde" +
					" a departamentos, provincias, localidades y todo aquello que abarque más de una faja de Gauss Krüger." +
					"<br>Actualmente se ha fijado (<i>por cuestiones de compatibilidad</i>), como faja principal la <b>Faja 3</b>, " +
					" lo que puede llevar a coordenadas erróneas en planos o polígonos que abarquen otras fajas." +
		                        "</p>" +
		                        "</html>";
		        Advisor.showInternalMessageDialog(_ayuda);
			BasicDrawEngine cityMap = new BasicDrawEngine("GIS SALTA (Provincia)", new BasicLabel());
		        GaiaEnvironment.initialize(); //línea agregada para cargar la configuración de GAIA
			GaiaEnvironment.forzarFaja(3);
		        cityMap.setMapExtents(3548172.1937, 7249881.0068, 3571654.0741, 7268573.5261);
		        //cityMap.setMapExtents(-73.5744, -89.9018, -26.24042, -21.7778);
			//cityMap.getEngineConfig().setProjectionType(CoordinateSystems.LL);
			CoordinateViewer coordinateViewer = new CoordinateViewer();
			coordinateViewer.setClosable(false);
			cityMap.setCoordinateViewer(coordinateViewer);
			coordinateViewer.setTitle("");
			cityMap.setBounds(_desktop.getBounds());
			cityMap.setVisible(true);

			if (!GaiaEnvironment.gaiaEngine.initialize()) { //línea agregada para cargar la configuración de GAIA
			    GaiaEnvironment.setScheme("gis_salta");

			    //Nivel país
			    GeometrySet gsetLocalidades = new GeometrySet("geo", "locations_ar", "the_geom", "1=1", "gid");
			    gsetLocalidades.getGeometrySetConfig().setProjectionType(CoordinateSystems.LL);
			    GeometrySet gsetDepartamentos = new GeometrySet("geo", "districts_ar", "the_geom", "1=1", "gid");
			    gsetDepartamentos.getGeometrySetConfig().setProjectionType(CoordinateSystems.LL);
			    GeometrySet gsetProvincias = new GeometrySet("geo", "provinces_ar", "the_geom", "1=1", "gid");
			    gsetProvincias.getGeometrySetConfig().setProjectionType(CoordinateSystems.LL);

			    Layer localidades = new Layer("Localidades", gsetLocalidades, "localidad");
			    localidades.setColor(Color.BLACK);
			    localidades.setMouseOverColor(Color.ORANGE);
			    //localidades.setQueryable(true);
			    localidades.setPointDiameter(3000);

			    Layer departamentos = new Layer("Departamentos", gsetDepartamentos, "departamto");
			    departamentos.setColor(Color.RED);
			    departamentos.setMouseOverColor(Color.ORANGE);
			    //departamentos.setQueryable(true);

			    Layer provincias = new Layer("Provincias", gsetProvincias, "admin_name");
			    provincias.setColor(Color.BLACK);
			    provincias.setMouseOverColor(Color.CYAN);
			    //provincias.setQueryable(true);

			    //Nivel localidades
			    GeometrySet gsetSaltaSecciones = new GeometrySet("gis_salta", "secciones", "the_geom", "1=1", "gid");
			    GeometrySet gsetSaltaCalles = new GeometrySet("gis_salta", "calles", "the_geom", "1=1", "idcalle");
			    GeometrySet gsetSaltaManzanas = new GeometrySet("gis_salta", "manzanas_vinculadas", "the_geom", "1=1", "gid");

			    GeometrySet gsetMosconiSecciones = new GeometrySet("gis_mosconi", "secciones", "the_geom", "1=1", "gid");
			    GeometrySet gsetMosconiCalles = new GeometrySet("gis_mosconi", "calles", "the_geom", "1=1", "gid");
			    GeometrySet gsetMosconiManzanas = new GeometrySet("gis_mosconi", "manzanas_vinculadas", "the_geom", "1=1", "idmanzana");
			    GeometrySet gsetTartagalManzanas = new GeometrySet("gis_tartagal", "manzanas", "the_geom", "isvalid(the_geom)", "gid");
			    GeometrySet gsetTartagalCalles = new GeometrySet("gis_tartagal", "nombre_calles", "the_geom", "isvalid(the_geom)", "gid");

			    Layer mosconiSecciones = new Layer("Secciones", gsetMosconiSecciones);
			    mosconiSecciones.setColor(Color.MAGENTA);
			    mosconiSecciones.setMouseOverColor(Color.ORANGE);
			    Layer mosconiCalles = new Layer("Calles", gsetMosconiCalles, "nombre");
			    mosconiCalles.setColor(Color.lightGray);
			    mosconiCalles.setMouseOverColor(Color.CYAN);
			    mosconiCalles.setQueryable(true);
			    Layer mosconiManzanas = new Layer("Manzanas", gsetMosconiManzanas);
			    mosconiManzanas.setColor(Color.RED);
			    mosconiManzanas.setMouseOverColor(Color.MAGENTA);
			    Layer tartagalManzanas = new Layer("Manzanas [Tartagal]", gsetTartagalManzanas);
			    tartagalManzanas.setColor(Color.RED);
			    tartagalManzanas.setMouseOverColor(Color.MAGENTA);
			    Layer tartagalCalles = new Layer("Calles [Tartagal]", gsetTartagalCalles, "textstring");
			    tartagalCalles.setColor(Color.MAGENTA);
			    tartagalCalles.setMouseOverColor(Color.CYAN);
			    tartagalCalles.setPointDiameter(6);
			    tartagalCalles.setDrawGeometries(false);

			    Layer saltaCalles = new Layer("Calles", gsetSaltaCalles, "nombre");
			    saltaCalles.setColor(Color.lightGray);
			    saltaCalles.setMouseOverColor(Color.CYAN);
			    saltaCalles.setQueryable(true);

			    Layer saltaManzanas = new Layer("Manzanas", gsetSaltaManzanas, "nombre");
			    saltaManzanas.setColor(new Color(153, 153, 153));
			    saltaManzanas.setFillColor(new Color(255, 255, 0));
			    saltaManzanas.getLayerConfig().getStyleConfig().setTransparency(100-70);
			    saltaManzanas.setMouseOverColor(Color.MAGENTA);

			    Layer saltaSecciones = new Layer("Secciones", gsetSaltaSecciones, "nombre");
			    saltaSecciones.setColor(Color.MAGENTA);
			    saltaSecciones.setMouseOverColor(Color.ORANGE);

			    LayerGroup saltaCiudadGroup = new LayerGroup("Ciudad de Salta");
			    LayerGroup mosconiCiudadGroup = new LayerGroup("Ciudad de Gral. Mosconi");
			    LayerGroup tartagalCiudadGroup = new LayerGroup("Ciudad de Tartagal");
			    LayerGroup oranCiudadGroup = new LayerGroup("Ciudad de Oran");
			    LayerGroup nacionalGroup = new LayerGroup("Argentina");

			    mosconiCiudadGroup.add(mosconiSecciones);
			    mosconiCiudadGroup.add(mosconiManzanas);
			    mosconiCiudadGroup.add(mosconiCalles);
			    
			    tartagalCiudadGroup.add(tartagalManzanas);
			    tartagalCiudadGroup.add(tartagalCalles);

			    GaiaEnvironment.layerGroups.add(saltaCiudadGroup);
			    GaiaEnvironment.layerGroups.add(mosconiCiudadGroup);
			    GaiaEnvironment.layerGroups.add(tartagalCiudadGroup);
			    //GaiaEnvironment.layerGroups.add(oranCiudadGroup);
			    GaiaEnvironment.layerGroups.add(nacionalGroup);

			    saltaCiudadGroup.add(saltaSecciones);
			    saltaCiudadGroup.add(saltaManzanas);
			    saltaCiudadGroup.add(saltaCalles);

			    nacionalGroup.add(localidades);
			    nacionalGroup.add(departamentos);
			    nacionalGroup.add(provincias);

			    GaiaEnvironment.gaiaEngine.setLayerGroupList(GaiaEnvironment.layerGroups);

			    Vector<GeometrySet> geometrySets = new Vector<GeometrySet>();

			    geometrySets.add(gsetMosconiSecciones);
			    geometrySets.add(gsetMosconiManzanas);
			    geometrySets.add(gsetMosconiCalles);
			    geometrySets.add(gsetTartagalManzanas);
			    geometrySets.add(gsetTartagalCalles);
			    geometrySets.add(gsetSaltaSecciones);
			    geometrySets.add(gsetSaltaManzanas);
			    geometrySets.add(gsetSaltaCalles);
			    geometrySets.add(gsetLocalidades);
			    geometrySets.add(gsetDepartamentos);
			    geometrySets.add(gsetProvincias);

			    GaiaEnvironment.geometrySets = geometrySets;

			    for (int i = 0; i < GaiaEnvironment.layerGroups.size(); i++) {
				LayerGroup _layerGroup = GaiaEnvironment.layerGroups.elementAt(i);
				for (int j = 0; j < _layerGroup.size(); j++) {
				    GaiaEnvironment.gaiaEngine.getLayerProfileList().add(new LayerProfile(_layerGroup.elementAt(j).getGeometrySetConfig().getName(), _layerGroup.getName(), _layerGroup.elementAt(j).getName()));
				}
			    }
			    //fin de líneas agregadas para cargar la configuración de GAIA
			}

			cityMap.initialize(); //línea agregada para cargar la configuración de GAIA

			Layer departamentos = cityMap.getLayer("Departamentos");
			if (departamentos != null) {
			    ExtendedInternalFrame districtPanelContainer = new ExtendedInternalFrame("Ventana de Información");
			    GaiaDistrictPanel districtPanel = new GaiaDistrictPanel();
			    districtPanelContainer.setCentralPanel(districtPanel);
			    departamentos.setQueryable(true);
			    departamentos.setInfoPanel(districtPanelContainer);
			}

			JInternalFrame layerListFrame = new JInternalFrame("Listado de Layers");
			layerListFrame.setClosable(false);
			LayerTree layerTree = new LayerTree();
			layerListFrame.getContentPane().add(layerTree, null);
			layerListFrame.setBounds(0, 0, 350, 150);
			layerListFrame.setResizable(true);
			layerListFrame.setVisible(false);
			layerTree.setDrawPanel(cityMap);

			MapBasicTools mapBasicToolsFrame = new MapBasicTools();
			mapBasicToolsFrame.setVertical();
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.ZOOM_IN_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.ZOOM_OUT_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.ZOOM_EXTENTS_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.RULE_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.QUERY_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.ADDRESSES_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.STREETS_EDITION_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.INFRASTRUCTURES_EDITION_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.LAYER_EDITION_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.PRINT_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.MULTIQUERY_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.FIXED_POLYGON_QUERY_TOOL);
			mapBasicToolsFrame.setClosable(false);
			//_desktop.setActive(true);

			RuleViewer ruleViewer = new RuleViewer();
			ruleViewer.setClosable(false);
			cityMap.setRuleViewer(ruleViewer);
			ruleViewer.setTitle("");

			_desktop.add(coordinateViewer);
			_desktop.add(ruleViewer);
			_desktop.add(cityMap);
			_desktop.add(layerListFrame);
			_desktop.add(mapBasicToolsFrame);
			layerListFrame.show();
			mapBasicToolsFrame.setDrawPanel(cityMap);
			mapBasicToolsFrame.show();
			//coordinateViewer.setVisible(false);
			//ruleViewer.setVisible(false);

			ComponentsManager.setConfigurable(layerListFrame);
			ComponentsManager.setConfigurable(coordinateViewer);
			ComponentsManager.setConfigurable(ruleViewer);
			ComponentsManager.setConfigurable(mapBasicToolsFrame);

		    } catch (Exception x) {
			Advisor.printException(x);
		    }
		    _desktop.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	    });
	try {
	    _desktop.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    Environment.jpStatusBar.setIndeterminate(true);
	    Environment.jpStatusBar.setAction("Cargando versión de\n Prueba del GIS");
	    _thread.start();
	} catch (Exception x) {
	    _desktop.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    Environment.jpStatusBar.setIndeterminate(false);
	    Environment.jpStatusBar.setAction("Listo...");
	    x.printStackTrace();
	}
	/*Thread _thread = new Thread(new Runnable() {
	       public void run() {
		   try {
		       GaiaEnvironment.initialize();//línea agregada para cargar la configuración de GAIA

		
		       BasicDrawEngine cityMap = new BasicDrawEngine("GIS DIGITALL", new BasicLabel());
		       cityMap.setMapExtents(3556114.5299, 7260975.3743, 3556551.2590, 7261336.3605);
		       CoordinateViewer coordinateViewer = new CoordinateViewer();
		       coordinateViewer.setClosable(false);
		       cityMap.setCoordinateViewer(coordinateViewer);
		       coordinateViewer.setTitle("");
		       cityMap.setBounds(_desktop.getBounds());
		       cityMap.setVisible(true);
		
		       if (!GaiaEnvironment.gaiaEngine.initialize()) {//línea agregada para cargar la configuración de GAIA
			    GaiaEnvironment.setScheme("gis_digitall");
			    GeometrySet gsetSecciones = new GeometrySet("secciones_vinculadas", "the_geom", "1=1", "gid");
			    GeometrySet gsetInfraestructura = new GeometrySet("infraestructura", "the_geom", "1=1", "gid");
			    GeometrySet gsetEspaciosVerdes = new GeometrySet("espacios_verdes", "the_geom", "1=1", "gid");
			    GeometrySet gsetManzanasVinculadas = new GeometrySet("manzanas_vinculadas", "the_geom", "idmanzana<100000", "idmanzana");
			    GeometrySet gsetParcelasVinculadas = new GeometrySet("parcelas_vinculadas", "the_geom", "1=1", "idparcela");
			    GeometrySet gsetCalles = new GeometrySet("calles_vinculadas", "the_geom", "1=1", "gid");
			    GeometrySet gsetPavimento = new GeometrySet("pavimento", "the_geom", "1=1", "gid");
			    GeometrySet gsetCensoComercial2009 = new GeometrySet("gea_salta", "censo_comercial_2009_seccion1", "the_geom", "estado != '*'", "idencuestacomercial");
			    GeometrySet gsetRelevamientoPublicidad2009 = new GeometrySet("gea_salta", "relevamientopublicidad_2009_detalle", "the_geom", "estado != '*'", "iddetallerelevamiento");
			    GeometrySet gsetCasosEnfermedades = new GeometrySet("casosenfermedades", "the_geom", "estado != '*'", "gid");

			    GeometrySet gsetAccionSocialEntregas = new GeometrySet("accionsocial", "entregas", "the_geom", "1=1", "identrega");
			    GeometrySet gsetAccionSocialPersonas = new GeometrySet("accionsocial", "personas", "the_geom", "1=1", "idpersona");

			    Layer secciones = new Layer("Secciones", gsetSecciones);
			    secciones.setColor(Color.MAGENTA);
			    secciones.setMouseOverColor(Color.ORANGE);

			    Layer calles = new Layer("Calles", gsetCalles, "nombre");
			    calles.setColor(Color.lightGray);
			    calles.setMouseOverColor(Color.CYAN);
			    calles.setQueryable(true);
			    GaiaEnvironment.setStreetsLayer(calles.getAlias());

			    Layer espacios_verdes = new Layer("Espacios verdes", gsetEspaciosVerdes);
			    espacios_verdes.setColor(Color.GREEN);
			    espacios_verdes.setFillColor(Color.GREEN.darker());
			    espacios_verdes.setMouseOverColor(Color.GREEN.darker());
			    Layer manzanas_vinculadas = new Layer("Manzanas", gsetManzanasVinculadas);
			    manzanas_vinculadas.setColor(Color.RED);
			    manzanas_vinculadas.setMouseOverColor(Color.MAGENTA);
			    Layer parcelas_vinculadas = new Layer("Parcelas", gsetParcelasVinculadas, "", "", "catastros");
			    parcelas_vinculadas.setColor(Color.BLACK);
			    parcelas_vinculadas.setMouseOverColor(Color.CYAN);
			    parcelas_vinculadas.setQueryable(true);
			    parcelas_vinculadas.setToolTipLabel("Catastros");
			
			    ExtendedInternalFrame parcelPanelContainer = new ExtendedInternalFrame("Ventana de Información");
			    GaiaParcelPanel parcelPanel = new GaiaParcelPanel();
			    parcelPanelContainer.setCentralPanel(parcelPanel);
			    parcelas_vinculadas.setInfoPanel(parcelPanelContainer);
			    GaiaEnvironment.setCadastralLayer(parcelas_vinculadas.getAlias());
			    Layer parcelas_categorias_tgs = new Layer("Categoría TGS", gsetParcelasVinculadas, "categoria");
			    parcelas_categorias_tgs.setColor(Color.BLACK);
			    parcelas_categorias_tgs.setMouseOverColor(Color.CYAN);
			    parcelas_categorias_tgs.setQueryable(true);
			    parcelas_categorias_tgs.addFilter("Categoría 1A", "categoria", "(categoria = 1)", Color.black, Color.RED, "Categoría 1A TGS");
			    parcelas_categorias_tgs.addFilter("Categoría 1B", "categoria", "(categoria = 2)", Color.black, Color.GREEN, "Categoría 1B TGS");
			    parcelas_categorias_tgs.addFilter("Categoría 2", "categoria", "(categoria = 3)", Color.black, Color.YELLOW, "Categoría 2 TGS");
			    parcelas_categorias_tgs.addFilter("Categoría 3", "categoria", "(categoria = 4)", Color.black, Color.ORANGE, "Categoría 3 TGS");
			
			    Layer parcelas_periodos_deuda_tgs = new Layer("Periodos Deuda TGS", gsetParcelasVinculadas);
			    parcelas_periodos_deuda_tgs.setColor(Color.BLACK);
			    parcelas_periodos_deuda_tgs.setMouseOverColor(Color.CYAN);
			    parcelas_periodos_deuda_tgs.setQueryable(true);
			    parcelas_periodos_deuda_tgs.addFilter("Hasta 3 anticipos", "", "(SELECT anticipostgs FROM gis_digitall.deudasxidparcela WHERE deudasxidparcela.idparcela = _id) <= 3", Color.black, Color.GREEN, "Hasta 3 anticipos");
			    parcelas_periodos_deuda_tgs.addFilter("Entre 3 y 6 anticipos", "", "(SELECT anticipostgs FROM gis_digitall.deudasxidparcela WHERE deudasxidparcela.idparcela = _id) BETWEEN 4 AND 6", Color.black, Color.YELLOW, "Entre 3 y 6 anticipos");
			    parcelas_periodos_deuda_tgs.addFilter("Entre 6 y 9 anticipos", "", "(SELECT anticipostgs FROM gis_digitall.deudasxidparcela WHERE deudasxidparcela.idparcela = _id) BETWEEN 7 AND 9", Color.black, Color.RED, "Entre 6 y 9 anticipos");
			    parcelas_periodos_deuda_tgs.addFilter("Más de 9 anticipos", "", "(SELECT anticipostgs FROM gis_digitall.deudasxidparcela WHERE deudasxidparcela.idparcela = _id) > 9", Color.black, Color.BLUE, "Más de 9 anticipos");
			
			    Layer parcelas_periodos_deuda_inmob = new Layer("Periodos Deuda Imp. Inmob.", gsetParcelasVinculadas);
			    parcelas_periodos_deuda_inmob.setColor(Color.BLACK);
			    parcelas_periodos_deuda_inmob.setMouseOverColor(Color.CYAN);
			    parcelas_periodos_deuda_inmob.setQueryable(true);
			    parcelas_periodos_deuda_inmob.addFilter("Hasta 3 anticipos", "", "(SELECT anticiposinmob FROM gis_digitall.deudasxidparcela WHERE deudasxidparcela.idparcela = _id) <= 3", Color.black, Color.GREEN, "Hasta 3 anticipos");
			    parcelas_periodos_deuda_inmob.addFilter("Entre 3 y 6 anticipos", "", "(SELECT anticiposinmob FROM gis_digitall.deudasxidparcela WHERE deudasxidparcela.idparcela = _id) BETWEEN 4 AND 6", Color.black, Color.YELLOW, "Entre 3 y 6 anticipos");
			    parcelas_periodos_deuda_inmob.addFilter("Entre 6 y 9 anticipos", "", "(SELECT anticiposinmob FROM gis_digitall.deudasxidparcela WHERE deudasxidparcela.idparcela = _id) BETWEEN 7 AND 9", Color.black, Color.RED, "Entre 6 y 9 anticipos");
			    parcelas_periodos_deuda_inmob.addFilter("Más de 9 anticipos", "", "(SELECT anticiposinmob FROM gis_digitall.deudasxidparcela WHERE deudasxidparcela.idparcela = _id) > 9", Color.black, Color.BLUE, "Más de 9 anticipos");

			    Layer parcelas_deuda_moratoria = new Layer("Estado de Cuenta y Moratoria", gsetParcelasVinculadas);
			    parcelas_deuda_moratoria.setColor(Color.BLACK);
			    parcelas_deuda_moratoria.setMouseOverColor(Color.CYAN);
			    parcelas_deuda_moratoria.setQueryable(true);
			    parcelas_deuda_moratoria.getLayerConfig().setAutoUpdateRateInSeconds(60);
			    parcelas_deuda_moratoria.addFilter("Tiene una moratoria/Se encuentra al día", "", GaiaEnvironment.getScheme() + ".getConsultaMoratoria(_id) > 0", Color.black, Color.GREEN, "Tiene una moratoria/Se encuentra al día");
			    parcelas_deuda_moratoria.addFilter("Averiguó sobre la moratoria", "", GaiaEnvironment.getScheme() + ".getConsultaMoratoria(_id) = 0", Color.black, Color.YELLOW, "Averiguó sobre la moratoria");
			    parcelas_deuda_moratoria.addFilter("No averiguó sobre la moratoria", "", GaiaEnvironment.getScheme() + ".getConsultaMoratoria(_id) < 0", Color.black, Color.RED, "No averiguó sobre la moratoria");
			
			    Layer infraestructura = new Layer("Infraestructura", gsetInfraestructura, "nombre", "tipo");
			    infraestructura.setColor(Color.BLACK);
			    infraestructura.setMouseOverColor(Color.CYAN);
			    infraestructura.setPointDiameter(4);
			    infraestructura.setQueryable(true);
			    infraestructura.setModifiable(true);
			    infraestructura.setTolerance(25);
			    //infraestructura.setSymbolsQuery("SELECT idtype, symbol FROM tabs.infrastructuretype_tabs ORDER BY idtype");
			    ExtendedInternalFrame infraestructuraPanelContainer = new ExtendedInternalFrame("Infraestructura Urbana");
			    GaiaInfrastructuresPanel infraestructuraPanel = new GaiaInfrastructuresPanel();
			    infraestructuraPanelContainer.setCentralPanel(infraestructuraPanel);
			    infraestructura.setConfigPanel(infraestructuraPanelContainer);
			    infraestructuraPanel.setLayer(infraestructura);
			    GaiaEnvironment.setInfrastructuresLayer(infraestructura.getAlias());

			    Layer pavimento = new Layer("Pavimento", gsetPavimento);
			    pavimento.setColor(Color.lightGray);
			    pavimento.setMouseOverColor(Color.CYAN);
			    pavimento.setQueryable(true);
			    pavimento.getLayerConfig().getStyleConfig().setLineWidth(6);

			    Layer casosEnfermedades = new Layer("Control y Prev. de Enfermedades endémicas", gsetCasosEnfermedades, "nombre", "", "nombre");
			    casosEnfermedades.setColor(new Color(255, 0, 0));
			    casosEnfermedades.setMouseOverColor(Color.CYAN);
			    casosEnfermedades.setPointDiameter(2);
			    casosEnfermedades.setEditable(true);
			    ExtendedInternalFrame formsInternalFrame = new ExtendedInternalFrame("Formularios");
			    formsInternalFrame.setDesktop(_desktop);
			    BasicPanel formPanel = new BasicPanel();
			    formsInternalFrame.setClosable(false);
			    formsInternalFrame.setIconifiable(false);
			    formPanel.setLayout(new BorderLayout());
			    formPanel.setSize(new Dimension(300, 420));
			    GaiaEnvironment.formPanel = formPanel;
			    formsInternalFrame.setCentralPanel(formPanel);
			    GaiaEnvironment.formsFrame = formsInternalFrame;
			    GaiaFormCasosEnfermedad formCasosEnfermedades = new GaiaFormCasosEnfermedad();
			    formCasosEnfermedades.setLayer(casosEnfermedades);
			    ExtendedInternalFrame streetsPanelContainer = new ExtendedInternalFrame("Calles");
			    GaiaStreetsPanel streetsPanel = new GaiaStreetsPanel();
			    streetsPanelContainer.setCentralPanel(streetsPanel);
			    calles.setConfigPanel(streetsPanelContainer);
			
			    Layer comercios_2009 = new Layer("Relevamiento Comercial 2009", gsetCensoComercial2009);
			    comercios_2009.setColor(Color.BLACK);
			    comercios_2009.setMouseOverColor(Color.CYAN);
			    comercios_2009.setQueryable(true);
			    comercios_2009.setEditable(true);
			    comercios_2009.setToolTipLabel("Nombre:");
			    ExtendedInternalFrame infoComercialContainer = new ExtendedInternalFrame("Ventana de Información Comercial");
			    InfoComercios infoComerciosPanel = new InfoComercios();
			    infoComercialContainer.setCentralPanel(infoComerciosPanel);
			    comercios_2009.setInfoPanel(infoComercialContainer);
			    FormCensoComercial2009 formComercios = new FormCensoComercial2009();
			    formComercios.setLayer(comercios_2009);
			
			    Layer publicidad_2009 = new Layer("Relevamiento Publicidad 2009", gsetRelevamientoPublicidad2009);
			    publicidad_2009.setColor(Color.BLACK);
			    publicidad_2009.setMouseOverColor(Color.CYAN);
			    publicidad_2009.setQueryable(true);
			    publicidad_2009.setEditable(true);
			    publicidad_2009.setToolTipLabel("Nombre:");
			    ExtendedInternalFrame infoPublicidadContainer = new ExtendedInternalFrame("Ventana de Información Publicitaria");
			    InfoPublicidad infoPublicidadPanel = new InfoPublicidad();
			    infoPublicidadContainer.setCentralPanel(infoPublicidadPanel);
			    publicidad_2009.setInfoPanel(infoPublicidadContainer);
			    FormPublicidad formPublicidad = new FormPublicidad();
			    formPublicidad.setLayer(publicidad_2009);
			
			    secciones.setOn();
			
			    Layer consultas_01 = new Layer("Consultas 01", gsetParcelasVinculadas, "", "", "catastros");
			    consultas_01.setColor(Color.MAGENTA);
			    consultas_01.setMouseOverColor(Color.MAGENTA);
			
			    Layer consultas_02 = new Layer("Consultas 02", gsetParcelasVinculadas, "", "", "catastros");
			    consultas_02.setColor(Color.MAGENTA);
			    consultas_02.setMouseOverColor(Color.MAGENTA);
			
			    Layer consultas_03 = new Layer("Consultas 03", gsetParcelasVinculadas, "", "", "catastros");
			    consultas_03.setColor(Color.MAGENTA);
			    consultas_03.setMouseOverColor(Color.MAGENTA);
			
			    Layer consultas_04 = new Layer("Consultas 04", gsetParcelasVinculadas, "", "", "catastros");
			    consultas_04.setColor(Color.MAGENTA);
			    consultas_04.setMouseOverColor(Color.MAGENTA);
			
			    Layer consultas_05 = new Layer("Consultas 05", gsetParcelasVinculadas, "", "", "catastros");
			    consultas_05.setColor(Color.MAGENTA);
			    consultas_05.setMouseOverColor(Color.MAGENTA);
			
			    LayerGroup catastralGroup = new LayerGroup("Catastral");
			    LayerGroup infraestructuraGroup = new LayerGroup("Infraestructura");
			    LayerGroup recaudacionGroup = new LayerGroup("Recaudación");
			    LayerGroup queryGroup = new LayerGroup("Consultas");
			    LayerGroup relevamientoGroup = new LayerGroup("Relevamiento");
			
			    LayerGroup accionSocialGroup = new LayerGroup("Acción Social y Comunitaria");
			
			    Layer accionSocialPersonas = new Layer("Personas", gsetAccionSocialPersonas);
			    accionSocialPersonas.setColor(Color.GREEN);
			    accionSocialPersonas.setFillColor(Color.BLUE);
			    accionSocialPersonas.setMouseOverColor(Color.MAGENTA);
			    accionSocialPersonas.setPointDiameter(8);
			    accionSocialPersonas.setQueryable(true);
			    accionSocialPersonas.setModifiable(true);
			    accionSocialPersonas.setDrawGeometries(false);
			
			    Layer accionSocialEntregas = new Layer("Entregas", gsetAccionSocialEntregas);
			    accionSocialEntregas.setColor(Color.GREEN);
			    accionSocialEntregas.setFillColor(Color.BLUE);
			    accionSocialEntregas.setMouseOverColor(Color.MAGENTA);
			    accionSocialEntregas.setPointDiameter(8);
			    accionSocialEntregas.setQueryable(true);
			    accionSocialEntregas.setModifiable(true);
			    accionSocialEntregas.setDrawGeometries(false);
			
			    GaiaEnvironment.layerGroups.add(catastralGroup);
			    GaiaEnvironment.layerGroups.add(infraestructuraGroup);
			    GaiaEnvironment.layerGroups.add(recaudacionGroup);
			    GaiaEnvironment.layerGroups.add(accionSocialGroup);
			    GaiaEnvironment.layerGroups.add(queryGroup);
			    GaiaEnvironment.layerGroups.add(relevamientoGroup);
			
			    accionSocialGroup.add(accionSocialPersonas);
			    accionSocialGroup.add(accionSocialEntregas);
			
			    queryGroup.add(consultas_01);
			    queryGroup.add(consultas_02);
			    queryGroup.add(consultas_03);
			    queryGroup.add(consultas_04);
			    queryGroup.add(consultas_05);
			
			    infraestructuraGroup.add(secciones);
			    infraestructuraGroup.add(espacios_verdes);
			    infraestructuraGroup.add(casosEnfermedades);
			    catastralGroup.add(manzanas_vinculadas);
			    catastralGroup.add(parcelas_vinculadas);
			    catastralGroup.add(calles);
			    infraestructuraGroup.add(infraestructura);
			    infraestructuraGroup.add(pavimento);
			    recaudacionGroup.add(parcelas_categorias_tgs);
			    recaudacionGroup.add(parcelas_periodos_deuda_tgs);
			    recaudacionGroup.add(parcelas_periodos_deuda_inmob);
			    recaudacionGroup.add(parcelas_deuda_moratoria);
			
			    relevamientoGroup.add(comercios_2009);
			    relevamientoGroup.add(publicidad_2009);
			
			    GaiaEnvironment.nomencladorLayers.add(manzanas_vinculadas);
			    GaiaEnvironment.nomencladorLayers.add(calles);
			    GaiaEnvironment.nomencladorLayers.add(parcelas_vinculadas);
			
			    //líneas agregadas para cargar la configuración de GAIA
			    GaiaEnvironment.gaiaEngine.setLayerGroupList(GaiaEnvironment.layerGroups);
			    Vector<GeometrySet> geometrySets = new Vector<GeometrySet>();
			
			    geometrySets.add(gsetSecciones);
			    geometrySets.add(gsetCalles);
			    geometrySets.add(gsetEspaciosVerdes);
			    geometrySets.add(gsetManzanasVinculadas);
			    geometrySets.add(gsetParcelasVinculadas);
			    geometrySets.add(gsetInfraestructura);
			    geometrySets.add(gsetPavimento);
			    geometrySets.add(gsetCasosEnfermedades);
			    geometrySets.add(gsetCensoComercial2009);
			    geometrySets.add(gsetRelevamientoPublicidad2009);

			    geometrySets.add(gsetAccionSocialEntregas);
			    geometrySets.add(gsetAccionSocialPersonas);
			
			    GaiaEnvironment.geometrySets = geometrySets;

			    for (int i = 0; i < GaiaEnvironment.layerGroups.size(); i++) {
				LayerGroup _layerGroup = GaiaEnvironment.layerGroups.elementAt(i);
				for (int j = 0; j < _layerGroup.size(); j++) {
				    GaiaEnvironment.gaiaEngine.getLayerProfileList().add(new LayerProfile(_layerGroup.elementAt(j).getGeometrySetConfig().getName(), _layerGroup.getName(), _layerGroup.elementAt(j).getName()));
				}
			    }
			    //fin de líneas agregadas para cargar la configuración de GAIA
			}
		
			cityMap.initialize();//línea agregada para cargar la configuración de GAIA
		
			JInternalFrame layerListFrame = new JInternalFrame("Listado de Layers");
			layerListFrame.setClosable(false);
			LayerTree layerTree = new LayerTree();
			layerListFrame.getContentPane().add(layerTree, null);
			layerListFrame.setBounds(0, 0, 350, 150);
			layerListFrame.setResizable(true);
			layerListFrame.setVisible(false);
			layerTree.setDrawPanel(cityMap);
			
			MapBasicTools mapBasicToolsFrame = new MapBasicTools();
			mapBasicToolsFrame.setVertical();
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.ZOOM_IN_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.ZOOM_OUT_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.ZOOM_EXTENTS_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.RULE_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.QUERY_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.ADDRESSES_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.STREETS_EDITION_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.INFRASTRUCTURES_EDITION_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.LAYER_EDITION_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.PRINT_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.MULTIQUERY_TOOL);
			mapBasicToolsFrame.addTool(MapBasicToolsPanel.FIXED_POLYGON_QUERY_TOOL);
			mapBasicToolsFrame.setClosable(false);
			//_desktop.setActive(true);
			
			RuleViewer ruleViewer = new RuleViewer();
			ruleViewer.setClosable(false);
			cityMap.setRuleViewer(ruleViewer);
			ruleViewer.setTitle("");
			
			_desktop.add(coordinateViewer);
			_desktop.add(ruleViewer);
			_desktop.add(cityMap);
			_desktop.add(layerListFrame);
			_desktop.add(mapBasicToolsFrame);
			layerListFrame.show();
			mapBasicToolsFrame.setDrawPanel(cityMap);
			mapBasicToolsFrame.show();
			//coordinateViewer.setVisible(false);
			//ruleViewer.setVisible(false);
			
			ComponentsManager.setConfigurable(layerListFrame);
			ComponentsManager.setConfigurable(coordinateViewer);
			ComponentsManager.setConfigurable(ruleViewer);
			ComponentsManager.setConfigurable(mapBasicToolsFrame);
			
			GaiaCadastralFinderPanel finderPanel = new GaiaCadastralFinderPanel();
			finderPanel.setDesktop(_desktop);
			finderPanel.setDrawEngine(cityMap);
			finderPanel.setVisible(false);
			finderPanel.start();
			_desktop.setBottomRightComponent(finderPanel);
		   } catch (Exception x) {
		       Advisor.printException(x);
		   }
		   _desktop.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	       }
	});
	try {
	    _desktop.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	    Environment.jpStatusBar.setIndeterminate(true);
	    Environment.jpStatusBar.setAction("Cargando versión de\n Prueba del GIS");
	    _thread.start();
	} catch (Exception x) {
	    _desktop.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    Environment.jpStatusBar.setIndeterminate(false);
	    Environment.jpStatusBar.setAction("Listo...");
	    x.printStackTrace();
	}*/
    }

    private static void generarPiramidePoblacional() {
	
	try {
	    DefaultKeyedValues2DDataset data = new DefaultKeyedValues2DDataset();
	    ResultSet _population = LibSQL.exFunction("accionsocial.getAllPoblacion", "''");
	    while (_population.next()) {
		//data.addValue(-_population.getDouble(3)*.18, "Analfabetos", _population.getString(1));
		//data.addValue(-_population.getDouble(3)*.82, "Hombres", _population.getString(1));
		if (_population.getBoolean(2)) {
		    data.addValue(-_population.getDouble(3), "Hombres", _population.getString(1));
		    System.out.println((-_population.getDouble(3)) + ", " + "Hombres" + ", " + _population.getString(1));
		} else {
		    data.addValue(_population.getDouble(3), "Mujeres", _population.getString(1));
		    System.out.println(_population.getDouble(3) + ", " + "Mujeres" + ", " + _population.getString(1));
		}
	    }
	    CategoryDataset dataset = data;

		   // create the chart...
		   JFreeChart chart = ChartFactory.createStackedBarChart(
		       "Pirámide Poblacional",
		       "Edades",     // domain axis label
		       "Población", // range axis label
		       dataset,         // data
		       PlotOrientation.HORIZONTAL,
		       true,            // include legend
		       true,            // tooltips
		       false            // urls
		   );

		   // add the chart to a panel...
		   ChartPanel chartPanel = new ChartPanel(chart);
		   chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		   chartPanel.setSize(new java.awt.Dimension(500, 270));
		   ExtendedInternalFrame _populationChart = new ExtendedInternalFrame("Población");
		   _populationChart.setClosable(true);
		   _populationChart.setMaximizable(true);
		   _populationChart.setCentralPanel(chartPanel);
		   _populationChart.setDesktop(Environment.getActiveDesktop());
		   _populationChart.show();
	} catch (SQLException x) {
	    Advisor.printException(x);
	}
    }
}

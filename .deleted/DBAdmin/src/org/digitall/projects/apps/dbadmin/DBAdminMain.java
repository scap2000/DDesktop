package org.digitall.projects.apps.dbadmin;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.JFrame;

import org.digitall.common.components.combos.JCombo;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicTabbedPane;
import org.digitall.lib.sql.LibSQL;

public class DBAdminMain extends JFrame {

    private BasicTabbedPane tpMain = new BasicTabbedPane();
    private JCombo cbDatabases = new JCombo();
    private BasicButton dbConnect = new BasicButton();
    private Vector dbVector = new Vector();
    private Vector schemeVector = new Vector();
    private Vector<DBRole> groupVector = new Vector<DBRole>();
    private Vector userVector = new Vector();
    private Vector tableVector = new Vector();
    private Vector functionVector = new Vector();
    private UserManager userManager = new UserManager(this);
    private GroupManager groupManager = new GroupManager(this);
    private TableManager tableManager = new TableManager(this);
    private FunctionManager functionManager = new FunctionManager(this);
    
    public DBAdminMain() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	getContentPane().setLayout(null);
	setSize(new Dimension(707, 623));
	setTitle("DataBase Administrator");
	tpMain.setBounds(new Rectangle(15, 50, 675, 540));
	tpMain.addTab("Gestión de Usuarios", userManager);
	tpMain.addTab("Gestión de Grupos", groupManager);
	tpMain.addTab("Gestión de Tablas", tableManager);
	tpMain.addTab("Gestión de Funciones", functionManager);
	this.getContentPane().add(dbConnect, null);
	this.getContentPane().add(cbDatabases, null);
	getContentPane().add(tpMain, null);
	ComponentsManager.centerWindow(this);
	cbDatabases.setBounds(new Rectangle(60, 20, 200, 20));
	dbConnect.setText("Connect");
	dbConnect.setBounds(new Rectangle(270, 20, 95, 20));
	dbConnect.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     dbConnect_actionPerformed(e);
				 }

			     }
	);
	loadStuff();
    }

    private void dbConnect_actionPerformed(ActionEvent e) {
	LibSQL.setDataBase(cbDatabases.getSelectedItem().toString());
	LibSQL.closeConnection();
	if (LibSQL.isConnected()) {
	    loadStuff();
	} else {
	    Advisor.messageBox("Error al intentar conectar a la base de datos " + cbDatabases.getSelectedItem().toString(), "Error");
	}
    }

    private void loadStuff() {
	cbDatabases.loadJCombo("SELECT datdba, datname, 0 FROM pg_database WHERE datallowconn");
	dbVector = cbDatabases.getItemsVector();
	cbDatabases.setSelectedItem(LibSQL.getDataBase().split("/")[3]);
	userManager.boot();
	groupManager.boot();
	tableManager.boot();
	functionManager.boot();
	groupManager.setDBVector(dbVector);
    }

    public void setGroupVector(Vector<DBRole> _groupVector) {
	groupVector = _groupVector;
	userManager.setGroupVector(_groupVector);
	tableManager.setGroupVector(_groupVector);
	functionManager.setGroupVector(_groupVector);
    }

    public Vector getGroupVector() {
	return groupVector;
    }

    public void setUserVector(Vector _userVector) {
	userVector = _userVector;
	groupManager.setUserVector(_userVector);
    }

    public Vector getUserVector() {
	return userVector;
    }

}


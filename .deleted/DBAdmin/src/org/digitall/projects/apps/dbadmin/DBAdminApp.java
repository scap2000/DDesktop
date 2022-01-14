package org.digitall.projects.apps.dbadmin;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.Login;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

public class DBAdminApp {

    public DBAdminApp() {
	JFrame frame = new DBAdminMain();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension frameSize = frame.getSize();
	if (frameSize.height > screenSize.height) {
	    frameSize.height = screenSize.height;
	}
	if (frameSize.width > screenSize.width) {
	    frameSize.width = screenSize.width;
	}
	frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }

    public static void main(String[] args) {
	Environment.mainClass = DBAdminApp.class;
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice[] devices = env.getScreenDevices();
	Environment.graphicsDevice = devices[0];
	Login loginWindow = new Login(Environment.organization, Environment.developer, true, false);
	ComponentsManager.centerWindow(loginWindow);
	loginWindow.setModal(true);
	loginWindow.setVisible(true);
	if (loginWindow.getValidado()) {
	    //LibSQL.setDateTime();
	    new DBAdminApp();
	} else {
	    System.exit(0);
	}
    }

}

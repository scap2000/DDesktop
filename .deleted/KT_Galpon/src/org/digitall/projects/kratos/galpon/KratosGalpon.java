package org.digitall.projects.kratos.galpon;


import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.digitall.common.systemmanager.interfaces.MonitorSelector;
import org.digitall.lib.common.OrganizationInfo;
import org.digitall.lib.environment.Environment;
import org.digitall.projects.gdigitall.gobiernodigitall.MainFrame;
import org.digitall.projects.gdigitall.gobiernodigitall.Boot;

public class KratosGalpon {

    private MainFrame frame;

    public KratosGalpon(GraphicsDevice[] devices) {
	Environment.graphicsDevice = devices[0];
	if (devices.length > 1) {
	    if (!Environment.cfg.hasProperty("Screen")) {
		MonitorSelector monitorSelector = new MonitorSelector(devices);
		monitorSelector.show();
	    } else {
		Environment.graphicsDevice = devices[Integer.parseInt(Environment.cfg.getProperty("Screen"))];
	    }
	}
	frame = new MainFrame();
    }

    public static void main(String[] args) {
	// Get all system UI Defaults
	/*
	UIDefaults defaults = UIManager.getDefaults();
	Enumeration enume = defaults.keys();
	for (; enume.hasMoreElements(); ) {
	    // Get property name
	    Object key = enume.nextElement();
	    Object value = defaults.get(key);
	    System.out.println(key + ": " + value);
	}
	*/
	// Get all system properties
	/*
	Properties props = System.getProperties();
	// Enumerate all system properties
	Enumeration enume = props.propertyNames();
	for (; enume.hasMoreElements(); ) {
	    // Get property name
	    String propName = (String)enume.nextElement();
	    // Get property value
	    String propValue = (String)props.get(propName);
            System.out.println(propName + ": " + propValue);
	}
	*/
	Environment.mainClass = KratosGalpon.class;
	Boot.initGraphics();
	Environment.organization = "Municipalidad de El Galpón	";
	

	OrganizationInfo.setOrgName("Municipalidad de El Galpón");
	OrganizationInfo.setShortName("Munic. de El Galpón");
	OrganizationInfo.setDescription("Municipalidad de El Galpón - Provincia de Salta");
	OrganizationInfo.setAddress("Avenida San Martín 211");
	OrganizationInfo.setShortAddress("Avda. San Martín 211");
	OrganizationInfo.setCuit("30-65637160-5");
	OrganizationInfo.setZipCode("4444");
	OrganizationInfo.setLocation("El Galpón");
	OrganizationInfo.setProvince("Salta");
	OrganizationInfo.setCountry("República Argentina");
	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice[] devices = env.getScreenDevices();
	new KratosGalpon(devices);
    }

}

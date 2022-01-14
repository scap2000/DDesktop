package org.digitall.projects.fenicia.sermaq;


import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.digitall.common.systemmanager.interfaces.MonitorSelector;
import org.digitall.lib.common.OrganizationInfo;
import org.digitall.lib.environment.Environment;

public class FeniciaSermaq {

    private MainFrame frame;

    public FeniciaSermaq(GraphicsDevice[] devices) {
	Environment.mainClass = FeniciaSermaq.class;
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
	Environment.mainClass = FeniciaSermaq.class;
	Boot.initGraphics();
	Environment.organization = "SERMAQ Obras y Servicios";
	OrganizationInfo.setOrgName("SERMAQ Obras y Servicios");
	OrganizationInfo.setTitle("SERMAQ OBRAS Y SERVICIOS");
	OrganizationInfo.setShortName("SERMAQ OS");
	OrganizationInfo.setDescription("SERMAQ  - Obras y Servicios petroleros - Salta");
	OrganizationInfo.setAddress("Av. Belgrano 900 - Salta - C.P. 4400"); 
	OrganizationInfo.setShortAddress("Belgrano 900");
	OrganizationInfo.setCuit("XX-XXXXXXXX-X");
	OrganizationInfo.setZipCode("4400");
	OrganizationInfo.setLocation("Salta");
	OrganizationInfo.setProvince("Salta");
	OrganizationInfo.setCountry("República Argentina");
        OrganizationInfo.setWebAddress("http://www.sermaq.com.ar");
        OrganizationInfo.setPhoneNumber1("(0387) 4XX-XXXX");
        OrganizationInfo.setPhoneNumber2("(0387) 4XX-XXXX");
	Environment.defaultLocation = "SALTA (SALTA)";
	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice[] devices = env.getScreenDevices();
	new FeniciaSermaq(devices);
    }

}

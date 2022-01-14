package org.digitall.projects.tests;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.digitall.lib.environment.Environment;
import org.digitall.projects.gdigitall.gobiernodigitall.GobiernoDigitall;

import sun.net.www.protocol.file.FileURLConnection;

public class DigitallApplication {

    private MainFrame frame;

    public DigitallApplication(GraphicsDevice[] devices) {
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
	Environment.mainClass = GobiernoDigitall.class;
	Boot.initGraphics();
	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice[] devices = env.getScreenDevices();
	new DigitallApplication(devices);
    }

}

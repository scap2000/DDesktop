package org.digitall.projects.sermaq;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.digitall.lib.environment.Environment;

public class SermaqApplication {

    private MainFrame frame;

    public SermaqApplication(GraphicsDevice[] devices) {
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
	Boot.initUIManagerKeys();
	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice[] devices = env.getScreenDevices();
	new SermaqApplication(devices);
    }

}

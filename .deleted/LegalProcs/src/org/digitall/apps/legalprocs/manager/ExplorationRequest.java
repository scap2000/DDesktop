package org.digitall.apps.legalprocs.manager;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.digitall.lib.components.basic.BasicInternalFrame;

//

public class ExplorationRequest extends BasicInternalFrame {

    private ExplorationRequestPanel explorationRequestPanel;
    private String type = "";

    public ExplorationRequest(String _type) {
	try {
	    type = _type;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	explorationRequestPanel = new ExplorationRequestPanel(this, "ER");
	this.setSize(new Dimension(640, 562));
	this.setTitle("Permiso de Exploración");
	this.getContentPane().add(explorationRequestPanel);
	this.getContentPane().setLayout(null);
	explorationRequestPanel.setBounds(new Rectangle(0, 35, 635, 500));
	explorationRequestPanel.setBounds(0, 35, 636, 499);
    }

}

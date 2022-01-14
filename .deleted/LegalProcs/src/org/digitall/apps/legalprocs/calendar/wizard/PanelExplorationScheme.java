package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;

import org.digitall.apps.legalprocs.manager.ExplorationSchemePanel;
import org.digitall.apps.legalprocs.manager.classes.ExplorationRequestClass;
import org.digitall.apps.legalprocs.manager.classes.ExplorationSchemeClass;
import org.digitall.lib.components.basic.BasicDialog;

//

public class PanelExplorationScheme extends BasicDialog {

    private ExplorationSchemePanel explorationschemePanel;
    private Manager mgmt;
    private ExplorationRequestClass_Old prospection;
    private ExplorationRequestClass explorationRequestClass;
    private ExplorationSchemeClass explorationSchemeClass;
    private int idExplorationRequestClass = -1;

    public PanelExplorationScheme() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /*public PanelExplorationScheme(Manager _mgmt) {
	try {
	    mgmt = _mgmt;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelExplorationScheme(ExplorationRequestClass_Old _prospection) {
	try {
	    prospection = _prospection;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }*/

    public PanelExplorationScheme(ExplorationSchemeClass _explorationSchemeClass) {
	try {
	    explorationSchemeClass = _explorationSchemeClass;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(592, 462));
	this.setTitle("Exploration Scheme");
	//explorationschemePanel = new ExplorationSchemePanel(this, prospection, 1);
	explorationschemePanel = new ExplorationSchemePanel(this);
	explorationschemePanel.setBounds(0, 0, 588, 437);
	this.getContentPane().add(explorationschemePanel, null);
	this.getContentPane().setLayout(null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	loadExplorationSchemeObject();
    }

    private void loadExplorationSchemeObject() {
	explorationschemePanel.setExplorationSchemeObject(explorationSchemeClass);
    }

}

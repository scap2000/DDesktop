package org.digitall.apps.legalprocs.manager;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.digitall.apps.legalprocs.calendar.wizard.Manager;
import org.digitall.lib.components.basic.BasicDialog;

//

public class ExplorationScheme extends BasicDialog {

    private ExplorationSchemePanel jpExplorationScheme;
    private Manager mgmt;

    public ExplorationScheme() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ExplorationScheme(Manager _mgmt) {
	try {
	    mgmt = _mgmt;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(593, 493));
	this.setTitle("Programa de Exploración");
	jpExplorationScheme = new ExplorationSchemePanel(this);
	this.getContentPane().add(jpExplorationScheme, null);
	this.getContentPane().setLayout(null);
	jpExplorationScheme.setBounds(new Rectangle(0, 30, 588, 437));
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

}

package org.digitall.apps.legalprocs.manager;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.digitall.lib.components.basic.BasicDialog;

//

public class FindExplorationScheme extends BasicDialog {

    private FindExplorationsSchemePanel findES;

    public FindExplorationScheme() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	findES = new FindExplorationsSchemePanel(this);
	this.setSize(new Dimension(510, 383));
	this.setTitle("Expedientes");
	this.getContentPane().add(findES, null);
	this.getContentPane().setLayout(null);
	this.setTitle("Programas Mínimos de Exploración");
	findES.setBounds(new Rectangle(0, 30, 506, 329));
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

}

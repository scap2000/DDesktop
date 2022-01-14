package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Rectangle;

public class ExplorationInfo extends PanelWizard {

    private String title;

    public ExplorationInfo(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(555, 244));
	this.setBounds(new Rectangle(10, 10, this.getWidth(), this.getHeight()));
	this.setName(title);
    }

}

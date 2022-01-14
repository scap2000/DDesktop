package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;

import org.digitall.lib.components.basic.BasicContainerPanel;

public class PanelWizard extends BasicContainerPanel {

    private Wizard wizard;

    public PanelWizard() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(453, 332));
    }

    public void setWizard(Wizard _wizard) {
	wizard = _wizard;
    }

    public void goStep(int _step) {
	wizard.goStep(_step);
    }

    public void setStep(int _step) {
	wizard.setStep(_step);
    }

}

package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInputPanel;

public class SolicitorAlonePanel extends BasicContainerPanel {

    private CBInputPanel cbSolicitante = new CBInputPanel(CachedCombo.ENTITIES_ALLPERSONLIST, "Solicitor");

    public SolicitorAlonePanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(560, 52));
	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	cbSolicitante.setBounds(new Rectangle(93, 5, 375, 40));
	this.add(cbSolicitante, null);
    }

}

package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.inputpanels.TFInputPanel;
import org.digitall.lib.data.DataTypes;

public class RegistralRegistlyPanel extends BasicContainerPanel {

    private TFInputPanel tfFolio = new TFInputPanel(DataTypes.INTEGER, "Leaf", false, false);
    private TFInputPanel tfCodDpto = new TFInputPanel(DataTypes.INTEGER, "DepartmentCode", false, false);

    public RegistralRegistlyPanel() {
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
	tfFolio.setBounds(new Rectangle(95, 5, 140, 40));
	tfCodDpto.setBounds(new Rectangle(350, 5, 150, 40));
	this.add(tfCodDpto, null);
	this.add(tfFolio, null);
    }

}

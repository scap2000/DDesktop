package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.inputpanels.TFInputPanel;
import org.digitall.lib.data.DataTypes;

public class FilePanel extends BasicContainerPanel {

    private TFInputPanel panelFileNumber = new TFInputPanel(DataTypes.INTEGER, "FileNumber", false, false);
    private TFInputPanel panelFileLetter = new TFInputPanel(DataTypes.STRING, "FileLetter", false, false);
    private TFInputPanel panelFileYear = new TFInputPanel(DataTypes.INTEGER, "FileYear", false, false);

    public FilePanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(560, 55));
	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	panelFileNumber.setBounds(new Rectangle(75, 5, 90, 40));
	panelFileLetter.setBounds(new Rectangle(255, 5, 90, 40));
	panelFileYear.setBounds(new Rectangle(425, 5, 90, 40));
	this.add(panelFileYear, null);
	this.add(panelFileLetter, null);
	this.add(panelFileNumber, null);
    }

}

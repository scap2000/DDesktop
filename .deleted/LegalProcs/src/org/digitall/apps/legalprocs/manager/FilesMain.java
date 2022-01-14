package org.digitall.apps.legalprocs.manager;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;

//

public class FilesMain extends BasicInternalFrame {

    private MainPanel mainPanel;
    final String TEXTO = "Expedientes";
    private BasicLabel jLabel1 = new BasicLabel();
    // = new MainPanel();

    public FilesMain() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	mainPanel = new MainPanel(this);
	this.setSize(new Dimension(357, 193));
	this.setTitle("Sistema de Expedientes");
	this.getContentPane().setLayout(null);
	this.getContentPane().add(jLabel1, null);
	this.getContentPane().add(mainPanel, null);
	mainPanel.setBounds(new Rectangle(5, 40, 342, 126));
	jLabel1.setBounds(new Rectangle(0, 0, 355, 35));
    }

}

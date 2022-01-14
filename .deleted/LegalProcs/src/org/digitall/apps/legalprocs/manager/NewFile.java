package org.digitall.apps.legalprocs.manager;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.digitall.lib.components.basic.BasicInternalFrame;

public class NewFile extends BasicInternalFrame {

    private NewFilePanel newFiles;

    public NewFile() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	newFiles = new NewFilePanel(this);
	setClosable(true);
	setDefaultCloseOperation(BasicInternalFrame.HIDE_ON_CLOSE);
	this.setSize(new Dimension(573, 659));
	this.setTitle("Carga rapida de Expedientes");
	this.getContentPane().setLayout(null);
	newFiles.setBounds(new Rectangle(5, 35, 560, 590));
	this.getContentPane().add(newFiles);
    }

}

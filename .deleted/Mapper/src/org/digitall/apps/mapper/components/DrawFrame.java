package org.digitall.apps.mapper.components;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;

public class DrawFrame extends BasicDialog {

    private BasicLabel statusBar = new BasicLabel();
    private DrawDesk panel;
    private int idProject;

    public DrawFrame(int _idProject) {
	try {
	    idProject = _idProject;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	panel = new DrawDesk(statusBar, idProject);
	this.setTitle("Prueba");
	this.setSize(new Dimension(840, 722));
	this.getContentPane().setLayout(null);
	panel.setBounds(new Rectangle(15, 15, 800, 600));
	statusBar.setBounds(new Rectangle(0, 670, 830, 20));
	statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	this.getContentPane().add(statusBar, null);
	this.getContentPane().add(panel, null);
    }

    public boolean hasPolygons() {
	return panel.hasPolygons();
    }

}

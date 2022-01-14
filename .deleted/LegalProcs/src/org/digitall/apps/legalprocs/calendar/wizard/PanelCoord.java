package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.digitall.apps.legalprocs.manager.CoordGKPanel;
import org.digitall.apps.legalprocs.manager.classes.ERPolygon;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;

public class PanelCoord extends BasicDialog {

    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private CoordGKPanel coordGKPanel = new CoordGKPanel();
    private String title;
    private ERPolygon erPoligonClass;
    private static final int YES = 1;

    public PanelCoord(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelCoord(String _title, ERPolygon _erPoligonClass) {
	try {
	    erPoligonClass = _erPoligonClass;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(785, 538));
	this.setTitle(title);
	btnAccept.setBounds(new Rectangle(10, 475, 40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(730, 475, 40, 28));
	btnClose.setSize(new Dimension(40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	coordGKPanel.setBounds(new Rectangle(5, 50, 760, 400));
	this.getContentPane().add(coordGKPanel, null);
	this.add(btnAccept, null);
	this.add(btnClose, null);
	ComponentsManager.centerWindow(this);
	loadERPolygonObject();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	erPoligonClass.setMineralcategoryAux(coordGKPanel.getMinCat());
	erPoligonClass.setAreaAux(Double.parseDouble(coordGKPanel.getArea()));
	erPoligonClass.setPoints(coordGKPanel.getPoints());
	erPoligonClass.setRecord(YES);
	this.dispose();
    }

    private void loadERPolygonObject() {
	coordGKPanel.setERPolygonObject(erPoligonClass);
    }

}

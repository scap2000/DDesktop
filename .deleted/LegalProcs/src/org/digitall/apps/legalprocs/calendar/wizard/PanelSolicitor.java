package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;

import org.digitall.apps.legalprocs.manager.SolicitorPanel;
import org.digitall.apps.legalprocs.manager.classes.EntitiesByFileClass;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;

//
//public class PanelSolicitor extends BasicInternalFrame {

public class PanelSolicitor extends BasicDialog {

    private SolicitorPanel jpSolicitor = new SolicitorPanel();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private String title;
    private Manager mgmt;
    private JDesktopPane parentDesktop;
    private ExplorationRequestClass_Old explorationRequestClass_Old;
    //private ExplorationRequestClass explorationRequestClass;
    private EntitiesByFileClass solicitorClass;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public PanelSolicitor(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelSolicitor(String _title, Manager _mgmt) {
	try {
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelSolicitor(String _title, Manager _mgmt, ExplorationRequestClass_Old _explorationRequestClass_Old) {
	try {
	    explorationRequestClass_Old = _explorationRequestClass_Old;
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelSolicitor(JDesktopPane _parentDesktop, String _title, Manager _mgmt, ExplorationRequestClass_Old _explorationRequestClass_Old) {
	//usado por el wizard desde cero
	try {
	    explorationRequestClass_Old = _explorationRequestClass_Old;
	    parentDesktop = _parentDesktop;
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelSolicitor(JDesktopPane _parentDesktop, String _title, Manager _mgmt, EntitiesByFileClass _solicitorClass) {
	//usado por el panel con todas las aplicaciones (fileapplications)
	try {
	    solicitorClass = _solicitorClass;
	    parentDesktop = _parentDesktop;
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelSolicitor(JDesktopPane _parentDesktop, String _title, EntitiesByFileClass _solicitorClass) {
	try {
	    solicitorClass = _solicitorClass;
	    parentDesktop = _parentDesktop;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(586, 545));
	this.setTitle(title);
	jpSolicitor.setBounds(new Rectangle(5, 40, 570, 417));
	jpSolicitor.setSize(new Dimension(570, 417));
	btnAccept.setBounds(new Rectangle(10, 485, 40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(530, 482, 40, 25));
	btnClose.setSize(new Dimension(40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	this.getContentPane().add(jpSolicitor, null);
	this.add(btnAccept, null);
	this.add(btnClose, null);
	//loadProspection();	// A ser borrado cuando todo funciones bien con los Objetos
	loadSolicitorObject();
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	//clearOjectData();
	/*if (solicitorClass.getRecord() == NO)  {
	    solicitorClass.setRecord(NO);	
	}*/
	this.dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	//explorationRequestClass_Old.setArgument(jpSolicitor.getIdSolicitor(), 1);
	solicitorClass.setIdReferencedAux(Integer.parseInt(jpSolicitor.getIdSolicitor().toString()));
	solicitorClass.setIsPersonAux(jpSolicitor.getIsPerson());
	solicitorClass.setIsSolicitorAux(true);
	solicitorClass.setRecord(YES);
	if (solicitorClass.getIdReferencedAux() == 0) {
	    solicitorClass.setEntityStatusAux(NO_MADE);
	} else {
	    solicitorClass.setEntityStatusAux(COMPLETE);
	}
	this.dispose();
    }
    // A ser borrado cuando todo funciones bien con los Objetos
    /* private void loadProspection() {
	jpSolicitor.setER(explorationRequestClass_Old);
	jpSolicitor.setParentDesktop(parentDesktop);
    }*/

    private void loadSolicitorObject() {
	jpSolicitor.setSolicitorClass(solicitorClass);
    }

    private void clearOjectData() {
	solicitorClass.clearAuxiliaryData();
    }

}

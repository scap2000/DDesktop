package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.digitall.apps.legalprocs.manager.ERProgramPanel;
import org.digitall.apps.legalprocs.manager.classes.ExplorationRequestClass;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;

//
//public class PanelProgram extends BasicInternalFrame {

public class PanelProgram extends BasicDialog {

    private ERProgramPanel erProgramPanel;
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private String title;
    private String idExplorationScheme = "";
    private int opcion = 0;
    private Manager mgmt;
    //private ExplorationRequestClass_Old prospection;
    private ExplorationRequestClass explorationRequestClass;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public PanelProgram(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelProgram(String _title, Manager _mgmt) {
	try {
	    opcion = 1;
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelProgram(String _title, ExplorationRequestClass _explorationRequestClass) {
	try {
	    explorationRequestClass = _explorationRequestClass;
	    opcion = 1;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(587, 503));
	this.setTitle(title);
	erProgramPanel = new ERProgramPanel();
	erProgramPanel.setBounds(new Rectangle(9, 35, 565, 375));
	btnAccept.setBounds(new Rectangle(10, 440, 40, 30));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(530, 440, 40, 30));
	btnClose.setSize(new Dimension(40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	this.getContentPane().add(erProgramPanel, null);
	this.add(btnAccept, null);
	this.add(btnClose, null);
	ComponentsManager.centerWindow(this);
	loadExplorationRequestObject();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	explorationRequestClass.setRecordERExpSch(NO);
	explorationRequestClass.setERDeclarationStatusAux(explorationRequestClass.getExplorationSchemeStatus());
	this.dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	explorationRequestClass.setTermDays(erProgramPanel.getPlazo());
	explorationRequestClass.setWorkCycleType(erProgramPanel.getCycleType());
	explorationRequestClass.setWorkCyclenumber(erProgramPanel.getCycleNumber());
	explorationRequestClass.setPermissionGranted(erProgramPanel.getPermisosOtorgados());
	explorationRequestClass.setMeasurementUnitsGranted(erProgramPanel.getUnidadesOtorgadas());
	explorationRequestClass.setWorkProgram(erProgramPanel.getWorkProgram());
	explorationRequestClass.setRecordERExpSch(YES);
	explorationRequestClass.setERDeclarationStatusAux(COMPLETE);
	this.dispose();
    }

    private void loadExplorationRequestObject() {
	erProgramPanel.setExplorationRequestClass(explorationRequestClass);
    }

}

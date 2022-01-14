package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.digitall.apps.legalprocs.manager.LegalRepresentativePanel;
import org.digitall.apps.legalprocs.manager.classes.EntitiesByFileClass;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;

//
//public class PanelLegalRep extends BasicInternalFrame {

public class PanelLegalRep extends BasicDialog {

    private LegalRepresentativePanel jplegalRepresentative = new LegalRepresentativePanel();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private String title;
    private Manager mgmt;
    private ExplorationRequestClass_Old prospection;
    private EntitiesByFileClass legalRepClass;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public PanelLegalRep(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelLegalRep(String _title, Manager _mgmt) {
	try {
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelLegalRep(String _title, Manager _mgmt, ExplorationRequestClass_Old _prospection) {
	//candidata a ser borrada, no se usa
	try {
	    prospection = _prospection;
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelLegalRep(String _title, Manager _mgmt, EntitiesByFileClass _legalRepClass) {
	// ANALIZAR SI NECESITA EL mgmt, por ahora se esta usando cuando se trata de una exploracion
	try {
	    legalRepClass = _legalRepClass;
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelLegalRep(String _title, EntitiesByFileClass _legalRepClass) {
	try {
	    legalRepClass = _legalRepClass;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(587, 430));
	this.setTitle(title);
	jplegalRepresentative.setBounds(new Rectangle(5, 40, 570, 295));
	btnAccept.setBounds(new Rectangle(10, 370, 40, 30));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(530, 370, 40, 30));
	btnClose.setSize(new Dimension(40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	this.getContentPane().add(jplegalRepresentative, null);
	this.add(btnAccept, null);
	this.add(btnClose, null);
	//loadProspection(); // a ser borrado cuando los objetos funcionen bien 
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	loadLegalRepObject();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	//clearOjectData();
	//legalRepClass.setEntityStatusAux(legalRepClass.getEntityStatus());
	legalRepClass.setRecord(NO);
	this.dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	// a ser borrado cuando los objetos funcionen bien 
	/*prospection.setArgument(jplegalRepresentative.getIdRepresentative(), 2);
	this.dispose();*/
	legalRepClass.setIdReferencedAux(Integer.parseInt(jplegalRepresentative.getIdRepresentative().toString()));
	legalRepClass.setIsPersonAux(true);
	legalRepClass.setIsSolicitorAux(false);
	legalRepClass.setRecord(YES);
	System.out.println("idreferenceAux: " + legalRepClass.getIdReferencedAux());
	if (legalRepClass.getIdReferencedAux() == 0) {
	    legalRepClass.setEntityStatusAux(NO_MADE);
	} else {
	    legalRepClass.setEntityStatusAux(COMPLETE);
	}
	this.dispose();
    }
    // a ser borrado cuando los objetos funcionen bien 
    /*private void loadProspection() {
	jplegalRepresentative.setProspection(prospection);
    }*/

    private void loadLegalRepObject() {
	jplegalRepresentative.setLegalRepClass(legalRepClass);
    }
    /*private void clearOjectData() {
	legalRepClass.clearAuxiliaryData();
    }*/

}

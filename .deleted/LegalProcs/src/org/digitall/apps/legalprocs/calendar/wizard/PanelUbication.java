package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;

import org.digitall.apps.legalprocs.manager.classes.UbicationClass;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInput;

//
//public class PanelUbication extends BasicInternalFrame {

public class PanelUbication extends BasicDialog {

    private CBInput cbCountry = new CBInput(CachedCombo.COUNTRY_TABS, "Country", true);
    private CBInput cbProvince = new CBInput(CachedCombo.PROVINCE_TABS, "ProvinceState", true);
    private CBInput cbLocation = new CBInput(CachedCombo.LOCATION_TABS, "Location", true);
    private BasicPanel jPanel1 = new BasicPanel();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private String title;
    private Manager mgmt;
    private String idLocationAddress = "";
    private BasicDialog explorationRequest;
    private ExplorationRequestClass_Old prospection;
    private UbicationClass ubicationClass;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public PanelUbication(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelUbication(String _title, Manager _mgmt, BasicDialog _explorationRequest) {
	try {
	    explorationRequest = _explorationRequest;
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelUbication(String _title, ExplorationRequestClass_Old _prospection) {
	try {
	    prospection = _prospection;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelUbication(String _title, UbicationClass _ubicationClass) {
	// Nueva forma de trabajo, los anteriores constructores van a desaparecer
	try {
	    ubicationClass = _ubicationClass;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(336, 352));
	this.setTitle(title);
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	btnAccept.setBounds(new Rectangle(10, 290, 40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(280, 290, 40, 25));
	btnClose.setSize(new Dimension(40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	cbCountry.setBounds(new Rectangle(15, 15, 280, 45));
	cbProvince.setBounds(new Rectangle(15, 90, 280, 40));
	cbLocation.setBounds(new Rectangle(15, 160, 280, 40));
	cbCountry.autoSize();
	cbProvince.autoSize();
	cbLocation.autoSize();
	jPanel1.add(cbCountry, null);
	jPanel1.add(cbLocation, null);
	jPanel1.add(cbProvince, null);
	this.getContentPane().add(jPanel1, null);
	this.add(btnAccept, null);
	this.add(btnClose, null);
	cbCountry.getCombo().addItemListener(new ItemListener() {

					  public void itemStateChanged(ItemEvent evt) {
					      if (evt.getStateChange() == ItemEvent.SELECTED) {
						  loadProvince(cbCountry.getSelectedValue());
					      }
					  }

				      }
	);
	cbProvince.getCombo().addItemListener(new ItemListener() {

					   public void itemStateChanged(ItemEvent evt) {
					       if (evt.getStateChange() == ItemEvent.SELECTED) {
						   loadLocation(cbProvince.getSelectedValue());
					       }
					   }

				       }
	);
	loadUbicationClassData();
	loadData();
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	this.setDefaultCloseOperation(BasicDialog.DISPOSE_ON_CLOSE);
    }

    private void loadData() {
	if (ubicationClass.getIdCountry() != -1) {
	    cbCountry.setSelectedID(String.valueOf(ubicationClass.getIdCountryAux()));
	    cbProvince.setSelectedID(String.valueOf(ubicationClass.getIdprovinceAux()));
	    cbLocation.setSelectedID(String.valueOf(ubicationClass.getIdlocationAux()));
	} else {
	    cbCountry.setSelectedID("0");
	    cbProvince.setSelectedID("0");
	    cbLocation.setSelectedID("0");
	}
    }

    private void loadProvince(Object _idCountry) {
	cbProvince.setFilter(_idCountry);
	if (cbCountry.getSelectedItem().toString().equals("Argentina")) {
	    cbProvince.setSelectedValue("SALTA");
	} else {
	    cbProvince.setSelectedValue(null);
	}
    }

    private void loadLocation(Object _idProvince) {
	if (ubicationClass.getIdlocation() != -1) {
	    cbLocation.setSelectedID(String.valueOf(ubicationClass.getIdlocationAux()));
	} else {
	    cbLocation.setFilter(_idProvince);
	    cbLocation.setSelectedID("0");
	}
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	//clearOjectData();
	//ubicationClass.setUbicationStatusAux(ubicationClass.getUbicationStatus());
	//ubicationClass.setRecord(NO);	
	this.dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	ubicationClass.setIdCountryAux(Integer.parseInt(cbCountry.getSelectedValue().toString()));
	ubicationClass.setIdprovinceAux(Integer.parseInt(cbProvince.getSelectedValue().toString()));
	ubicationClass.setIdlocationAux(Integer.parseInt(cbLocation.getSelectedValue().toString()));
	ubicationClass.setRecord(YES);
	if (ubicationClass.getIdCountryAux() == 0) {
	    ubicationClass.setUbicationStatusAux(INCOMPLETE);
	} else {
	    ubicationClass.setUbicationStatusAux(COMPLETE);
	}
	this.dispose();
    }

    public String getIdLocation() {
	return idLocationAddress;
    }

    private void loadUbicationClassData() {
	if (ubicationClass.getRecord() == NO) {
	    ubicationClass.retrieveData();
	}
    }

    private void clearOjectData() {
	ubicationClass.clearAuxiliaryData();
    }

    public void dispose() {
	this.setModal(false);
	this.setVisible(false);
    }

}

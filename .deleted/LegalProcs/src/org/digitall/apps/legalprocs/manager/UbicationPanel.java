package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInputPanel;

//

public class UbicationPanel extends BasicContainerPanel {

    private CBInputPanel cbCountry = new CBInputPanel(CachedCombo.COUNTRY_TABS, "Country", true);
    private CBInputPanel cbProvince = new CBInputPanel(CachedCombo.PROVINCE_TABS, "ProvinceState", true);
    private CBInputPanel cbLocation = new CBInputPanel(CachedCombo.LOCATION_TABS, "Location", true);
    private String idLocationAddress = "";

    public UbicationPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(560, 108));
	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	cbCountry.setBounds(new Rectangle(15, 10, 240, 45));
	cbProvince.setBounds(new Rectangle(295, 10, 250, 40));
	cbLocation.setBounds(new Rectangle(15, 60, 280, 40));
	this.add(cbLocation, null);
	this.add(cbProvince, null);
	this.add(cbCountry, null);
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
	loadData();
    }

    private void loadData() {
	cbCountry.setSelectedValue("Not Assigned");
	cbProvince.setSelectedValue("Not Assigned");
	cbLocation.setSelectedValue("Not Assigned");
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
	cbLocation.setFilter(_idProvince);
	if (cbProvince.getSelectedItem().toString().equals("SALTA")) {
	    cbLocation.setSelectedValue("SALTA");
	} else {
	    cbLocation.setSelectedValue(null);
	}
    }

    public String getIdCountry() {
	return cbCountry.getSelectedValue().toString();
    }

    public String getIdProvince() {
	return cbProvince.getSelectedValue().toString();
    }

    public String getIdLocation() {
	return cbLocation.getSelectedValue().toString();
    }

    public String getUbicationInsert() {
	idLocationAddress = org.digitall.lib.sql.LibSQL.getCampo("SELECT MAX(idaddress) + 1 FROM org.addresses");
	String ubicationInsert = "INSERT INTO org.addresses VALUES(" + idLocationAddress + "," + cbCountry.getSelectedValue().toString() + " ," + cbProvince.getSelectedValue().toString() + " , " + cbLocation.getSelectedValue().toString() + " ,0 ,0 ,0 ,'' ,'' ,0 ,'' ,0 ,0 ,0 ,0 ,'' ,'' );";
	return ubicationInsert;
    }

    public String getIdUbication() {
	return idLocationAddress;
    }

}

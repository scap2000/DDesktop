package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

//

public class SolicitorAddressPanel extends BasicContainerPanel {

    private TFInput panelProvinceReal = new TFInput(DataTypes.STRING, "ProvinceState", false);
    private TFInput panelLocationReal = new TFInput(DataTypes.STRING, "Location", false);
    private TFInput panelStreetReal = new TFInput(DataTypes.STRING, "Street", false);
    private TFInput panelStreetNumberReal = new TFInput(DataTypes.INTEGER, "FileNumber", false);

    public SolicitorAddressPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.add(panelProvinceReal, null);
	this.add(panelLocationReal, null);
	this.add(panelStreetReal, null);
	this.add(panelStreetNumberReal, null);
	this.setSize(new Dimension(560, 112));
	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	panelProvinceReal.setBounds(new Rectangle(5, 15, 230, 42));
	panelProvinceReal.autoSize();
	panelProvinceReal.setEditable(false);
	panelLocationReal.setBounds(new Rectangle(245, 15, 305, 42));
	panelLocationReal.autoSize();
	panelLocationReal.setEditable(false);
	panelStreetReal.setBounds(new Rectangle(5, 60, 330, 42));
	panelStreetReal.autoSize();
	panelStreetReal.setEditable(false);
	panelStreetNumberReal.setBounds(new Rectangle(390, 60, 75, 42));
	panelStreetNumberReal.autoSize();
	panelStreetNumberReal.setEditable(false);
    }

    public void setData(String _country, String _province, String _street, String _streetnumber) {
	panelProvinceReal.setValue(_country);
	panelLocationReal.setValue(_province);
	panelStreetReal.setValue(_street);
	panelStreetNumberReal.setValue(_streetnumber);
    }

    public void setData2(String _idprovince, String _idlocation, String _street, String _streetnumber) {
	panelProvinceReal.setValue(org.digitall.lib.sql.LibSQL.getCampo("SELECT name FROM tabs.province_tabs WHERE idprovince = " + _idprovince));
	panelLocationReal.setValue(org.digitall.lib.sql.LibSQL.getCampo("SELECT name FROM tabs.location_tabs WHERE idlocation = " + _idlocation));
	panelStreetReal.setValue(_street);
	panelStreetNumberReal.setValue(_streetnumber);
    }

}

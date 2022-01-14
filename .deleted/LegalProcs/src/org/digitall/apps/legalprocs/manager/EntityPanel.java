package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import org.digitall.common.systemmanager.PanelAddress;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

public class EntityPanel extends BasicContainerPanel {

    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private BasicPanel jpPersonalData = new BasicPanel();
    private BasicPanel jpAddresses = new BasicPanel();
    private PanelAddress jpAddress = new PanelAddress();
    private TFInput tfFirstName = new TFInput(DataTypes.STRING, "Name", false);
    private TFInput tfLastName = new TFInput(DataTypes.STRING, "LastName", false);
    private TFInput tfNumber = new TFInput(DataTypes.INTEGER, "IdentificationNumber", false);
    private TFInput tfBirthdate = new TFInput(DataTypes.DATE, "Birthdate", false);
    private CBInput cbIdentificationType = new CBInput();
    private CBInput cbCivilState = new CBInput();

    public EntityPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(563, 318));
	jTabbedPane1.setBounds(new Rectangle(5, 5, 550, 305));
	jpPersonalData.setLayout(null);
	jpPersonalData.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jpAddresses.setLayout(null);
	tfFirstName.setBounds(new Rectangle(10, 15, 230, 35));
	tfFirstName.autoSize();
	tfLastName.setBounds(new Rectangle(325, 15, 185, 35));
	tfNumber.setBounds(new Rectangle(120, 75, 155, 35));
	tfNumber.setSize(new Dimension(155, 35));
	tfNumber.autoSize();
	tfBirthdate.setValue("");
	tfBirthdate.setBounds(new Rectangle(300, 70, 165, 35));
	tfBirthdate.setSize(new Dimension(85, 35));
	cbIdentificationType.setBounds(new Rectangle(10, 75, 130, 35));
	cbIdentificationType.setSize(new Dimension(95, 35));
	cbIdentificationType.autoSize();
	cbCivilState.setBounds(new Rectangle(420, 75, 105, 35));
	cbCivilState.setSize(new Dimension(105, 35));
	cbCivilState.autoSize();
	tfLastName.autoSize();
	jpPersonalData.add(cbCivilState, null);
	jpPersonalData.add(cbIdentificationType, null);
	jpPersonalData.add(tfBirthdate, null);
	jpPersonalData.add(tfNumber, null);
	jpPersonalData.add(tfLastName, null);
	jpPersonalData.add(tfFirstName, null);
	jpAddresses.add(jpAddress);
	jpAddress.setBounds(new Rectangle(15, 29, 515, 225));
	jTabbedPane1.addTab("jpPersonalData", jpPersonalData);
	jTabbedPane1.addTab("Domicilio", jpAddresses);
	this.add(jTabbedPane1, null);
    }

}

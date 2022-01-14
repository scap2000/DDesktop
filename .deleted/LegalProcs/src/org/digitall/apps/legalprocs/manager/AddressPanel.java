package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.inputpanels.TFInputPanel;
import org.digitall.lib.data.DataTypes;

public class AddressPanel extends BasicContainerPanel {

    private TFInputPanel panelCountry = new TFInputPanel(DataTypes.STRING, "Country", true, true);
    private TFInputPanel panelProvince = new TFInputPanel(DataTypes.STRING, "ProvinceState", true, true);
    private TFInputPanel panelLocation = new TFInputPanel(DataTypes.STRING, "Location", true, true);
    private TFInputPanel panelNeighborhood = new TFInputPanel(DataTypes.STRING, "Neighborhood", true, true);
    private TFInputPanel panelPostalCode = new TFInputPanel(DataTypes.STRING, "PostalCode", true, true);
    private TFInputPanel panelStreet = new TFInputPanel(DataTypes.STRING, "Street", true, true);
    private TFInputPanel panelStreetNumber = new TFInputPanel(DataTypes.INTEGER, "FileNumber", true, true);
    private TFInputPanel panelBlock = new TFInputPanel(DataTypes.STRING, "Block", false, true);
    private TFInputPanel panelFloor = new TFInputPanel(DataTypes.STRING, "Floor", false, true);
    private TFInputPanel panelAppt = new TFInputPanel(DataTypes.STRING, "Appt", false, true);
    private TFInputPanel panelPhoneCode = new TFInputPanel(DataTypes.STRING, "PhoneCode", false, true);
    private TFInputPanel panelPhoneNumber = new TFInputPanel(DataTypes.INTEGER, "Phone", false, true);
    private TFInputPanel panelCelularCode = new TFInputPanel(DataTypes.INTEGER, "PhoneCode", false, true);
    private TFInputPanel panelCelularNumber = new TFInputPanel(DataTypes.STRING, "Celular", false, true);
    private TFInputPanel panelEmail = new TFInputPanel(DataTypes.STRING, "Email", false, true);
    private BasicButton btnAddLocation = new BasicButton();
    private BasicButton btnAddCountry = new BasicButton();
    private BasicButton btnNeighborhood = new BasicButton();
    private BasicButton btnAddProvince = new BasicButton();
    private BasicButton btnAddLocation1 = new BasicButton();

    public AddressPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(560, 209));
	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	panelCountry.setBounds(new Rectangle(10, 5, 230, 42));
	panelProvince.setBounds(new Rectangle(290, 5, 230, 40));
	panelLocation.setBounds(new Rectangle(10, 45, 230, 40));
	panelNeighborhood.setBounds(new Rectangle(290, 45, 230, 40));
	panelStreetNumber.setBounds(new Rectangle(330, 85, 75, 40));
	panelBlock.setBounds(new Rectangle(415, 85, 40, 40));
	panelFloor.setBounds(new Rectangle(460, 85, 35, 42));
	panelAppt.setBounds(new Rectangle(505, 85, 40, 42));
	panelPhoneCode.setBounds(new Rectangle(10, 125, 65, 42));
	panelPhoneNumber.setBounds(new Rectangle(75, 125, 140, 42));
	panelCelularCode.setBounds(new Rectangle(330, 125, 65, 42));
	panelCelularNumber.setBounds(new Rectangle(395, 125, 140, 42));
	panelEmail.setBounds(new Rectangle(10, 165, 350, 42));
	panelStreet.setBounds(new Rectangle(75, 85, 220, 40));
	panelPostalCode.setBounds(new Rectangle(10, 85, 65, 40));
	btnAddLocation.setBounds(new Rectangle(520, 25, 30, 25));
	btnAddLocation.setToolTipText("Agregar Localidad");
	btnAddCountry.setBounds(new Rectangle(240, 20, 25, 25));
	btnAddCountry.setToolTipText("Agregar Provincia");
	btnAddCountry.setSize(new Dimension(25, 25));
	btnNeighborhood.setBounds(new Rectangle(525, 60, 25, 25));
	btnNeighborhood.setToolTipText("Agregar Localidad");
	btnAddProvince.setBounds(new Rectangle(525, 20, 25, 25));
	btnAddProvince.setToolTipText("Agregar Provincia");
	btnAddLocation1.setBounds(new Rectangle(520, 25, 30, 25));
	btnAddLocation1.setToolTipText("Agregar Localidad");
	btnAddLocation1.setBounds(new Rectangle(295, 100, 25, 25));
	btnAddLocation1.setToolTipText("Agregar Provincia");
	btnAddLocation.setBounds(new Rectangle(240, 60, 25, 25));
	btnAddLocation.setToolTipText("Agregar Provincia");
	this.add(panelEmail, null);
	this.add(panelCelularNumber, null);
	this.add(panelCelularCode, null);
	this.add(panelPhoneNumber, null);
	this.add(panelPhoneCode, null);
	this.add(btnAddLocation1, null);
	this.add(panelAppt, null);
	this.add(panelBlock, null);
	this.add(btnAddProvince, null);
	this.add(btnAddLocation, null);
	this.add(btnNeighborhood, null);
	this.add(btnAddCountry, null);
	this.add(panelPostalCode, null);
	this.add(panelStreet, null);
	this.add(panelStreetNumber, null);
	this.add(panelFloor, null);
	this.add(panelNeighborhood, null);
	this.add(panelLocation, null);
	this.add(panelProvince, null);
	this.add(panelCountry, null);
	this.add(btnAddLocation, null);
    }

}

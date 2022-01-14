package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.digitall.apps.legalprocs.manager.classes.TerrainDataClass;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

//

public class TerrainDataPanel extends BasicContainerPanel {

    private BasicPanel jpTerrainData = new BasicPanel();
    private BasicPanel jpOwnerData = new BasicPanel();
    private BasicCheckBox chkCultivado = new BasicCheckBox();
    private BasicCheckBox chkCercado = new BasicCheckBox();
    private BasicCheckBox chkSitioPublico = new BasicCheckBox();
    private BasicCheckBox chkSitioHistorico = new BasicCheckBox();
    private BasicCheckBox chkEdificado = new BasicCheckBox();
    private BasicCheckBox chkErial = new BasicCheckBox();
    private BasicCheckBox chkReservaNatural = new BasicCheckBox();
    private BasicCheckBox chkSitioReligioso = new BasicCheckBox();
    private TFInput tfOther = new TFInput(DataTypes.STRING, "SpecificOther", false);
    private TFInput tfOwnerData = new TFInput(DataTypes.STRING, "FullName", false);
    private TFInput tfStreetNumber = new TFInput(DataTypes.INTEGER, "FileNumber", false);
    private BasicButton btnLegalStreet = new BasicButton();
    private BasicButton btnAddLegalProvince = new BasicButton();
    private BasicButton btnAddStreet = new BasicButton();
    private CBInput cbProvince = new CBInput(CachedCombo.PROVINCE_TABS, "ProvinceState", true);
    private CBInput cbLocation = new CBInput(CachedCombo.LOCATION_TABS, "Location", true);
    private CBInput cbStreet = new CBInput(CachedCombo.STREET_TABS, "Street", true);
    private String TDId = "";
    private TerrainDataClass terrainDataClass;
    //CONST
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;

    public TerrainDataPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(572, 359));
	jpTerrainData.setBounds(new Rectangle(5, 15, 560, 135));
	jpTerrainData.setLayout(null);
	jpTerrainData.setBorder(org.digitall.lib.components.BorderPanel.getBorderPanel("Estado de los Terrenos", Color.black, Color.black));
	chkCultivado.setText("Cultivado");
	chkCultivado.setBounds(new Rectangle(15, 25, 125, 25));
	chkCultivado.setFont(new Font("Default", 1, 11));
	chkCercado.setText("Cercado");
	chkCercado.setBounds(new Rectangle(155, 25, 125, 25));
	chkCercado.setFont(new Font("Default", 1, 11));
	chkSitioPublico.setText("Sitio Público");
	chkSitioPublico.setBounds(new Rectangle(15, 50, 125, 25));
	chkSitioPublico.setFont(new Font("Default", 1, 11));
	chkSitioHistorico.setText("Sitio Histórico");
	chkSitioHistorico.setBounds(new Rectangle(155, 50, 125, 25));
	chkSitioHistorico.setFont(new Font("Default", 1, 11));
	chkEdificado.setText("Edificado");
	chkEdificado.setBounds(new Rectangle(290, 25, 125, 25));
	chkEdificado.setFont(new Font("Default", 1, 11));
	chkErial.setText("Erial");
	chkErial.setBounds(new Rectangle(425, 25, 125, 25));
	chkErial.setFont(new Font("Default", 1, 11));
	chkReservaNatural.setText("Reserva Natural");
	chkReservaNatural.setBounds(new Rectangle(425, 50, 125, 25));
	chkReservaNatural.setFont(new Font("Default", 1, 11));
	chkSitioReligioso.setText("Sitio Religioso");
	chkSitioReligioso.setBounds(new Rectangle(290, 50, 125, 25));
	chkSitioReligioso.setFont(new Font("Default", 1, 11));
	tfOther.setBounds(new Rectangle(15, 80, 535, 45));
	tfOther.autoSize();
	tfOther.setEditable(true);
	jpOwnerData.setBounds(new Rectangle(5, 165, 560, 180));
	jpOwnerData.setLayout(null);
	//jpOwnerData.setBorder(BorderFactory.createTitledBorder("Datos del Propietario"));
	jpOwnerData.setBorder(org.digitall.lib.components.BorderPanel.getBorderPanel("Datos del Propietario", Color.black, Color.black));
	tfOwnerData.setBounds(new Rectangle(10, 20, 535, 45));
	tfOwnerData.autoSize();
	tfOwnerData.setEditable(true);
	btnLegalStreet.setBounds(new Rectangle(340, 200, 25, 25));
	btnLegalStreet.setToolTipText("Agregar Localidad");
	btnAddLegalProvince.setBounds(new Rectangle(245, 150, 25, 25));
	btnAddLegalProvince.setToolTipText("Agregar Localidad");
	tfStreetNumber.setBounds(new Rectangle(385, 123, 75, 45));
	tfStreetNumber.autoSize();
	tfStreetNumber.setEditable(true);
	btnAddStreet.setBounds(new Rectangle(310, 135, 25, 25));
	cbProvince.setBounds(new Rectangle(10, 75, 225, 40));
	cbProvince.autoSize();
	cbLocation.setBounds(new Rectangle(280, 75, 260, 40));
	cbLocation.autoSize();
	cbStreet.setBounds(new Rectangle(10, 125, 290, 40));
	cbStreet.autoSize();
	jpTerrainData.add(tfOther, null);
	jpTerrainData.add(chkSitioReligioso, null);
	jpTerrainData.add(chkReservaNatural, null);
	jpTerrainData.add(chkErial, null);
	jpTerrainData.add(chkEdificado, null);
	jpTerrainData.add(chkSitioHistorico, null);
	jpTerrainData.add(chkSitioPublico, null);
	jpTerrainData.add(chkCercado, null);
	jpTerrainData.add(chkCultivado, null);
	jpOwnerData.add(cbStreet, null);
	jpOwnerData.add(cbLocation, null);
	jpOwnerData.add(cbProvince, null);
	jpOwnerData.add(tfStreetNumber, null);
	jpOwnerData.add(btnAddStreet);
	jpOwnerData.add(tfOwnerData, null);
	this.add(jpOwnerData, null);
	this.add(jpTerrainData, null);
	cbProvince.getCombo().addItemListener(new ItemListener() {

					   public void itemStateChanged(ItemEvent evt) {
					       if (evt.getStateChange() == ItemEvent.SELECTED) {
						   loadLocation();
					       }
					   }

				       }
	);
	cbLocation.getCombo().addItemListener(new ItemListener() {

					   public void itemStateChanged(ItemEvent evt) {
					       if (evt.getStateChange() == ItemEvent.SELECTED) {
						   loadStreet();
					       }
					   }

				       }
	);
	btnAddStreet.setVisible(false);
	cbProvince.setSelectedID(String.valueOf(0));
	cbLocation.setSelectedID(String.valueOf(0));
	cbStreet.setSelectedID(String.valueOf(0));
    }

    private void loadLocation() {
	if (cbProvince.getSelectedItem().toString().equals("SALTA")) {
	    cbLocation.setSelectedValue("SALTA");
	} else {
	    cbLocation.setSelectedID(String.valueOf(0));
	}
    }

    private void loadStreet() {
	if (cbLocation.getSelectedItem().toString().equals("SALTA")) {
	    cbStreet.setSelectedValue("Abdo, Eusebio");
	} else {
	    cbStreet.setSelectedID(String.valueOf(0));
	}
    }
    // Este metodo se eliminara cuando funcione todo con objetos

    public String getTDInsert() {
	TDId = org.digitall.lib.sql.LibSQL.getCampo("SELECT MAX(idterraindata) + 1 FROM file.terraindata");
	String insert = "INSERT INTO file.terraindata VALUES(" + TDId + "," + chkCultivado.isSelected() + " , " + chkCercado.isSelected() + " ," + chkEdificado.isSelected() + " ," + chkErial.isSelected() + " , " + chkSitioPublico.isSelected() + " ," + chkSitioHistorico.isSelected() + " , " + chkSitioReligioso.isSelected() + "," + chkReservaNatural.isSelected() + " ,'" + tfOther.getString() + "','');";
	return insert;
    }

    public String getTDId() {
	return TDId;
    }

    public boolean getCultivated() {
	return chkCultivado.isSelected();
    }

    public boolean getSurrounded() {
	return chkCercado.isSelected();
    }

    public boolean getBuilt() {
	return chkEdificado.isSelected();
    }

    public boolean getUnplowed() {
	return chkErial.isSelected();
    }

    public boolean getPublic() {
	return chkSitioPublico.isSelected();
    }

    public boolean getHistorical() {
	return chkSitioHistorico.isSelected();
    }

    public boolean getReligious() {
	return chkSitioReligioso.isSelected();
    }

    public boolean getNaturalReserve() {
	return chkReservaNatural.isSelected();
    }

    public String getOther() {
	return tfOther.getString();
    }

    public String getOwner() {
	return tfOwnerData.getString();
    }

    public int getIdProvince() {
	return Integer.parseInt(cbProvince.getSelectedValue().toString());
    }

    public int getIdLocation() {
	return Integer.parseInt(cbLocation.getSelectedValue().toString());
    }

    public int getIdStreet() {
	return Integer.parseInt(cbStreet.getSelectedValue().toString());
    }

    public int getStreetNumber() {
	return Integer.parseInt("0" + tfStreetNumber.getString());
    }

    public void setTerrainDataClass(TerrainDataClass _terrainDataClass) {
	terrainDataClass = _terrainDataClass;
	if (terrainDataClass.getIdTerrainDataAux() == -1) {
	    terrainDataClass.retrieveTerrainData(terrainDataClass.getIdReferenced());
	}
	loadData();
    }

    private void loadData() {
	chkCultivado.setSelected(terrainDataClass.isCultivatedAux());
	chkCercado.setSelected(terrainDataClass.isSurroundedAux());
	chkEdificado.setSelected(terrainDataClass.isBuiltAux());
	chkErial.setSelected(terrainDataClass.isUnpLowedAux());
	chkSitioPublico.setSelected(terrainDataClass.isPublicPlaceAux());
	chkSitioHistorico.setSelected(terrainDataClass.isHistoricalAux());
	chkSitioReligioso.setSelected(terrainDataClass.isReligiousAux());
	chkReservaNatural.setSelected(terrainDataClass.isNaturalReserveAux());
	tfOther.setValue(terrainDataClass.getOtherAux());
	tfOwnerData.setValue(terrainDataClass.getOwnernameAux());
	tfStreetNumber.setValue(String.valueOf(terrainDataClass.getStreetnumberAux()));
	if (terrainDataClass.getIdProvinceAux() != -1) {
	    cbProvince.setSelectedID(String.valueOf(terrainDataClass.getIdProvinceAux()));
	    cbLocation.setSelectedID(String.valueOf(terrainDataClass.getIdlocationAux()));
	    cbStreet.setSelectedID(String.valueOf(terrainDataClass.getIdstreetAux()));
	}
    }

}

package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.digitall.apps.legalprocs.manager.TerrainDataPanel;
import org.digitall.apps.legalprocs.manager.classes.TerrainDataClass;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;

//
//public class PanelTerrainData extends BasicInternalFrame {

public class PanelTerrainData extends BasicDialog {

    private TerrainDataPanel terrainDataPanel = new TerrainDataPanel();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    //
    private String title;
    private Manager mgmt;
    //private ExplorationRequestClass_Old prospection;
    private TerrainDataClass terrainDataClass;
    //CONSTANTES
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public PanelTerrainData(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelTerrainData(String _title, Manager _mgmt) {
	try {
	    mgmt = _mgmt;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /*public PanelTerrainData(String _title, ExplorationRequestClass_Old _prospection) {
	try {
	    prospection = _prospection;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }*/

    public PanelTerrainData(String _title, TerrainDataClass _terrainDataClass) {
	try {
	    terrainDataClass = _terrainDataClass;
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(587, 477));
	this.setTitle(title);
	//(this.getWidth(), this.getTitle(), IconTypes.CR_IconHeaderLogo, IconTypes.CRFiles_IconHeaderLogo);
	terrainDataPanel.setBounds(new Rectangle(5, 35, 570, 350));
	btnAccept.setBounds(new Rectangle(15, 410, 40, 30));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setSize(new Dimension(40, 28));
	btnClose.setBounds(new Rectangle(530, 410, 40, 30));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	this.getContentPane().add(terrainDataPanel, null);
	//btnAccept, null);
	//btnClose, null);
	//
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	loadterrainDataObject();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	loadAuxiliaryParams();
	this.dispose();
    }

    private void loadterrainDataObject() {
	terrainDataPanel.setTerrainDataClass(terrainDataClass);
    }

    private void loadAuxiliaryParams() {
	terrainDataClass.setCultivatedAux(terrainDataPanel.getCultivated());
	terrainDataClass.setSurroundedAux(terrainDataPanel.getSurrounded());
	terrainDataClass.setBuiltAux(terrainDataPanel.getBuilt());
	terrainDataClass.setUnpLowedAux(terrainDataPanel.getUnplowed());
	terrainDataClass.setPublicPlaceAux(terrainDataPanel.getPublic());
	terrainDataClass.setHistoricalAux(terrainDataPanel.getHistorical());
	terrainDataClass.setReligiousAux(terrainDataPanel.getReligious());
	terrainDataClass.setNaturalReserveAux(terrainDataPanel.getNaturalReserve());
	terrainDataClass.setOtherAux(terrainDataPanel.getOther());
	terrainDataClass.setOwnernameAux(terrainDataPanel.getOwner());
	terrainDataClass.setIdProvinceAux(terrainDataPanel.getIdProvince());
	terrainDataClass.setIdlocationAux(terrainDataPanel.getIdLocation());
	terrainDataClass.setIdstreetAux(terrainDataPanel.getIdStreet());
	terrainDataClass.setStreetnumberAux(terrainDataPanel.getStreetNumber());
	terrainDataClass.setTerrainDataStatusAux(COMPLETE);
	terrainDataClass.setRecord(YES);
    }

}

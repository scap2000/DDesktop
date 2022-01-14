package org.digitall.apps.legalprocs.manager.classes;

import java.util.Date;

import org.digitall.lib.sql.LibSQL;

public class MineRequestClass {

    private int idMineRequest = -1;
    private int idPointpm = -1;
    private int idMine = -1;
    private int idPolygon = -1;
    private int idfile = -1;
    private Date registeredDate = null;
    private int mineRequestStatus = -1;
    private int solicitorStatus = 1;
    private int legalRepStatus = 1;
    private int ubicationStatus = 1;
    private int erPolygonStatus = 1;
    private int terrainDataStatus = 1;
    private int mineNameStatus;
    //OBJECTS
    private EntitiesByFileClass solicitorClass;
    private EntitiesByFileClass legalRepClass;
    private UbicationClass ubicationClass;
    private ERPolygon erPolygonsClass;
    private TerrainDataClass terrainDataClass;
    private MineNameClass mineNameClass;
    //CONST
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    // CONSTRUCTORS

    public MineRequestClass() {

    }

    public MineRequestClass(int _idMineRequest, int _idFile) {
	idfile = _idFile;
	idMineRequest = _idMineRequest;
	retrieveStatus();
    }
    // ACCESORS

    public void setIdMineRequest(int _idMineRequest) {
	idMineRequest = _idMineRequest;
    }

    public int getIdMineRequest() {
	return idMineRequest;
    }

    public void setIdPointpm(int _idPointpm) {
	idPointpm = _idPointpm;
    }

    public int getIdPointpm() {
	return idPointpm;
    }

    public void setIdMine(int _idMine) {
	idMine = _idMine;
    }

    public int getIdMine() {
	return idMine;
    }

    public void setIdPolygon(int _idPolygon) {
	idPolygon = _idPolygon;
    }

    public int getIdPolygon() {
	return idPolygon;
    }

    public void setIdfile(int _idfile) {
	idfile = _idfile;
    }

    public int getIdFile() {
	return idfile;
    }

    public void setRegisteredDate(Date _registeredDate) {
	registeredDate = _registeredDate;
    }

    public Date getRegisteredDate() {
	return registeredDate;
    }

    public void setMineRequestStatus(int _mineRequestStatus) {
	mineRequestStatus = _mineRequestStatus;
    }

    public int getMineRequestStatus() {
	return mineRequestStatus;
    }

    public void setSolicitorClass(EntitiesByFileClass _solicitorClass) {
	solicitorClass = _solicitorClass;
    }

    public EntitiesByFileClass getSolicitorClass() {
	return solicitorClass;
    }

    public void setLegalRepClass(EntitiesByFileClass _legalRepClass) {
	legalRepClass = _legalRepClass;
    }

    public EntitiesByFileClass getLegalRepClass() {
	return legalRepClass;
    }

    public void setSolicitorStatus(int _solicitorStatus) {
	solicitorStatus = _solicitorStatus;
    }

    public int getSolicitorStatus() {
	return solicitorStatus;
    }

    public void setLegalRepStatus(int _legalRepStatus) {
	legalRepStatus = _legalRepStatus;
    }

    public int getLegalRepStatus() {
	return legalRepStatus;
    }

    public void setUbicationStatus(int _ubicationStatus) {
	ubicationStatus = _ubicationStatus;
    }

    public int getUbicationStatus() {
	return ubicationStatus;
    }

    public void setUbicationClass(UbicationClass _ubicationClass) {
	ubicationClass = _ubicationClass;
    }

    public UbicationClass getUbicationClass() {
	return ubicationClass;
    }

    public void setErPolygonsClass(ERPolygon _erPolygonsClass) {
	erPolygonsClass = _erPolygonsClass;
    }

    public ERPolygon getErPolygonsClass() {
	return erPolygonsClass;
    }

    public void setErPolygonStatus(int _erPolygonStatus) {
	erPolygonStatus = _erPolygonStatus;
    }

    public int getErPolygonStatus() {
	return erPolygonStatus;
    }

    public void setTerrainDataClass(TerrainDataClass _terrainDataClass) {
	terrainDataClass = _terrainDataClass;
    }

    public TerrainDataClass getTerrainDataClass() {
	return terrainDataClass;
    }

    public void setTerrainDataStatus(int _terrainDataStatus) {
	terrainDataStatus = _terrainDataStatus;
    }

    public int getTerrainDataStatus() {
	return terrainDataStatus;
    }

    public void setMineNameClass(MineNameClass _mineNameClass) {
	mineNameClass = _mineNameClass;
    }

    public MineNameClass getMineNameClass() {
	return mineNameClass;
    }

    public void setMineNameStatus(int _mineNameStatus) {
	mineNameStatus = _mineNameStatus;
    }

    public int getMineNameStatus() {
	return mineNameStatus;
    }
    //METODS

    private void retrieveStatus() {
	String params = String.valueOf(idMineRequest);
	mineRequestStatus = LibSQL.getInt("file.getMineRequestStatus", params);
	// CARGO EL OBJETO SOLICITORCLASS SOLO CON EL ESTADO (SOLICITANTE)
	solicitorClass = new EntitiesByFileClass(idfile, true);
	solicitorStatus = solicitorClass.getEntityStatus();
	// CARGO EL OBJETO LEGALREPCLASS SOLO CON EL ESTADO (REPRESENTANTE LEGAL)
	legalRepClass = new EntitiesByFileClass(idfile, false);
	legalRepStatus = legalRepClass.getEntityStatus();
	/** FALTA CARGAR EL OBJETO DISCOVERY */
	// CARGO EL OBJETO UBICATIONCLASS SOLO CON EL ESTADO 
	ubicationClass = new UbicationClass(idfile);
	ubicationStatus = ubicationClass.getUbicationStatus();
	// CARGO EL OBJETO POLYGONCLASS SOLO CON EL ESTADO 
	erPolygonsClass = new ERPolygon(idMineRequest, true);
	erPolygonStatus = erPolygonsClass.getErPolygonStatus();
	// CARGO EL OBJETO MINECLASS SOLO CON EL ESTADO
	mineNameClass = new MineNameClass(idMineRequest);
	mineNameStatus = mineNameClass.getMineStatus();
	// CARGO EL OBJETO TERRAIN DATA SOLO CON EL ESTADO 
	terrainDataClass = new TerrainDataClass(idMineRequest, true);
	terrainDataStatus = terrainDataClass.getTerrainDataStatus();
    }

    public boolean recordData() {
	if (solicitorClass.recordEntityByFile() && legalRepClass.recordEntityByFile() && ubicationClass.recordUbication() && erPolygonsClass.recordPolygon() && terrainDataClass.recordTerrainData() && mineNameClass.recordMineName()) {
	    return true;
	} else {
	    return false;
	}
    }

    public void updateStatus() {
	solicitorStatus = solicitorClass.getEntityStatus();
	legalRepStatus = legalRepClass.getEntityStatus();
	ubicationStatus = ubicationClass.getUbicationStatus();
	erPolygonStatus = erPolygonsClass.getErPolygonStatus();
	terrainDataStatus = terrainDataClass.getTerrainDataStatus();
	mineNameStatus = mineNameClass.getMineNameStatus();
	String params = "";
	if (solicitorStatus == COMPLETE && legalRepStatus == COMPLETE && ubicationStatus == COMPLETE && erPolygonStatus == COMPLETE && terrainDataStatus == COMPLETE && mineNameStatus == COMPLETE) {
	    if (mineRequestStatus != COMPLETE) {
		params = String.valueOf(idfile) + "," + String.valueOf(idMineRequest) + "," + COMPLETE;
		LibSQL.getBoolean("file.updateMineRequestStatus", params);
		mineRequestStatus = COMPLETE;
	    }
	} else {
	    params = String.valueOf(idfile) + "," + String.valueOf(idMineRequest) + "," + INCOMPLETE;
	    if (mineRequestStatus != INCOMPLETE) {
		LibSQL.getBoolean("file.updateMineRequestStatus", params.toString());
		mineRequestStatus = INCOMPLETE;
	    }
	}
    }

}

package org.digitall.apps.legalprocs.manager.classes;

import java.sql.ResultSet;

import org.digitall.lib.sql.LibSQL;

public class MineNameClass {

    private int idmine = -1;
    private int idterraindata = -1;
    private int idmineral = -1;
    private String name = "";
    private boolean sample = false;
    private int idMineRequest = -1;
    private int idMineAux = -1;
    private int idterraindataAux = -1;
    private int idmineralAux = -1;
    private String nameAux = "";
    private boolean sampleAux = false;
    private int idMineRequestAux = -1;
    private int mineNameStatus = 1;
    private int mineNameStatusAux = 1;
    private int record = 2;
    //COSNTS
    private static final int YES = 1;
    private static final int NO = 2;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    //CONSTRUCTORS

    public MineNameClass() {
    }

    public MineNameClass(int _idMineRequest) {
	idMineRequest = _idMineRequest;
	retrieveStatus();
    }
    //ACCESSORS

    public void setIdMine(int _idmine) {
	idmine = _idmine;
    }

    public int getIdMine() {
	return idmine;
    }

    public void setIdTerrainData(int _idterraindata) {
	idterraindata = _idterraindata;
    }

    public int getIdTerrainData() {
	return idterraindata;
    }

    public void setIdMineral(int _idmineral) {
	idmineral = _idmineral;
    }

    public int getIdMineral() {
	return idmineral;
    }

    public void setName(String _name) {
	name = _name;
    }

    public String getName() {
	return name;
    }

    public void setSample(boolean _sample) {
	sample = _sample;
    }

    public boolean isSample() {
	return sample;
    }

    public void setMineStatus(int _mineStatus) {
	mineNameStatus = _mineStatus;
    }

    public int getMineStatus() {
	return mineNameStatus;
    }

    public void setIdMineRequest(int _idMineRequest) {
	idMineRequest = _idMineRequest;
    }

    public int getIdMineRequest() {
	return idMineRequest;
    }

    public void setMineNameStatus(int _mineNameStatus) {
	mineNameStatus = _mineNameStatus;
    }

    public int getMineNameStatus() {
	return mineNameStatus;
    }

    public void setMineNameStatusAux(int _mineNameStatusAux) {
	mineNameStatusAux = _mineNameStatusAux;
    }

    public int getMineNameStatusAux() {
	return mineNameStatusAux;
    }

    public void setRecord(int _record) {
	record = _record;
    }

    public int getRecord() {
	return record;
    }
    //AUXILIARY PARAMS

    public void setIdMineAux(int idmineAux) {
	this.idMineAux = idmineAux;
    }

    public int getIdMineAux() {
	return idMineAux;
    }

    public void setIdterraindataAux(int idterraindataAux) {
	this.idterraindataAux = idterraindataAux;
    }

    public int getIdterraindataAux() {
	return idterraindataAux;
    }

    public void setIdmineralAux(int idmineralAux) {
	this.idmineralAux = idmineralAux;
    }

    public int getIdmineralAux() {
	return idmineralAux;
    }

    public void setNameAux(String nameAux) {
	this.nameAux = nameAux;
    }

    public String getNameAux() {
	return nameAux;
    }

    public void setSampleAux(boolean sampleAux) {
	this.sampleAux = sampleAux;
    }

    public boolean isSampleAux() {
	return sampleAux;
    }

    public void setIdMineRequestAux(int idMineRequestAux) {
	this.idMineRequestAux = idMineRequestAux;
    }

    public int getIdMineRequestAux() {
	return idMineRequestAux;
    }
    //METHODS

    private void retrieveStatus() {
	String params = String.valueOf(idMineRequest);
	mineNameStatus = LibSQL.getInt("file.getMineNameStatus", params);
	mineNameStatusAux = mineNameStatus;
    }

    public void retrieveData() {
	if (idMineRequest != -1) {
	    String params = String.valueOf(idMineRequest);
	    ResultSet data = LibSQL.exFunction("file.getMineData", params);
	    try {
		if (data.next()) {
		    idmine = data.getInt("idmine");
		    idmineral = data.getInt("idmineral");
		    idterraindata = data.getInt("idterraindata");
		    name = data.getString("name");
		    sample = data.getBoolean("sample");
		    copyData();
		}
	    } catch (Exception ex) {
		ex.printStackTrace();
		// inicializar las variables en -1
	    }
	}
    }

    public void copyData() {
	idMineAux = idmine;
	idmineralAux = idmineral;
	idterraindataAux = idterraindata;
	nameAux = name;
	sampleAux = sample;
    }

    public boolean recordMineName() {
	if (record == YES) {
	    if (mineNameStatus == NO_MADE) {
		return executeInsert();
	    } else {
		return executeUpdate();
	    }
	} else {
	    return true;
	}
    }

    private boolean executeInsert() {
	String params = nameAux;
	if (LibSQL.getBoolean("file.addNewMines", params)) {
	    loadMineData();
	    return true;
	} else {
	    return false;
	    //ubicationStatus = INCOMPLETE;
	}
    }

    private boolean executeUpdate() {
	String params = String.valueOf(idmine) + ",'" + nameAux + "'";
	if (LibSQL.getBoolean("file.updateMineName", params)) {
	    loadMineData();
	    return true;
	} else {
	    return false;
	}
    }

    private void loadMineData() {
	idmine = idMineAux;
	idmineral = idmineralAux;
	idterraindata = idterraindataAux;
	name = nameAux;
	sample = sampleAux;
	mineNameStatus = COMPLETE;
    }

}

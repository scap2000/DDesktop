package org.digitall.apps.legalprocs.manager.classes;

import java.sql.ResultSet;

import org.digitall.lib.sql.LibSQL;

public class TerrainDataClass {

    //PARAMETROS
    private int idTerrainData = -1;
    private boolean cultivated = false;
    private boolean surrounded = false;
    private boolean built = false;
    private boolean unpLowed = false;
    private boolean publicPlace = false;
    private boolean historical = false;
    private boolean religious = false;
    private boolean naturalReserve = false;
    private String other = " ";
    private String ownername = "";
    private int idowneraddress = -1;
    private int idprovince = -1;
    private int idlocation = -1;
    private int idstreet = -1;
    private int streetnumber = -1;
    private boolean isMine = false;
    //PARAMETROS AUXILIARES
    private int idTerrainDataAux = -1;
    private boolean cultivatedAux = false;
    private boolean surroundedAux = false;
    private boolean builtAux = false;
    private boolean unpLowedAux = false;
    private boolean publicPlaceAux = false;
    private boolean historicalAux = false;
    private boolean religiousAux = false;
    private boolean naturalReserveAux = false;
    private String otherAux = "";
    private String ownernameAux = "";
    private int idowneraddressAux = -1;
    private int idProvinceAux = -1;
    private int idLocationAux = -1;
    private int idStreetAux = -1;
    private int streetnumberAux = -1;
    private int terrainDataStatusAux = 1;
    private int idExplorationRequest = -1;
    private int idReferenced = -1;
    private int terrainDataStatus = 1;
    //contenedor de los owenrs
    //CONSTANTES
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;
    private int record = 2;

    public TerrainDataClass() {
    }
    /*public TerrainDataClass(int _idExplorationRequest) {
	idExplorationRequest = _idExplorationRequest;
	retrieveTerrainDataStatus();
    }*/

    public TerrainDataClass(int _idReferenced, boolean _isMine) {
	idReferenced = _idReferenced;
	isMine = _isMine;
	retrieveTerrainDataStatus();
    }

    public int getIdTerrainData() {
	return idTerrainData;
    }

    public void setCultivated(boolean _cultivated) {
	cultivated = _cultivated;
    }

    public boolean isCultivated() {
	return cultivated;
    }

    public void setSurrounded(boolean _surrounded) {
	surrounded = _surrounded;
    }

    public boolean isSurrounded() {
	return surrounded;
    }

    public void setBuilt(boolean _built) {
	built = _built;
    }

    public boolean isBuilt() {
	return built;
    }

    public void setUnpLowed(boolean _unpLowed) {
	unpLowed = _unpLowed;
    }

    public boolean isUnpLowed() {
	return unpLowed;
    }

    public void setPublicPlace(boolean _publicPlace) {
	publicPlace = _publicPlace;
    }

    public boolean isPublicPlace() {
	return publicPlace;
    }

    public void setHistorical(boolean _historical) {
	historical = _historical;
    }

    public boolean isHistorical() {
	return historical;
    }

    public void setReligious(boolean _religious) {
	religious = _religious;
    }

    public boolean isReligious() {
	return religious;
    }

    public void setNaturalReserve(boolean _naturalReserve) {
	naturalReserve = _naturalReserve;
    }

    public boolean isNaturalReserve() {
	return naturalReserve;
    }

    public void setOther(String _other) {
	other = _other;
    }

    public String getOther() {
	return other;
    }

    public void setOwnername(String ownername) {
	this.ownername = ownername;
    }

    public String getOwnername() {
	return ownername;
    }

    public void setIdowneraddress(int _idowneraddress) {
	idowneraddress = _idowneraddress;
    }

    public int getIdowneraddress() {
	return idowneraddress;
    }

    public void setIdexplorationrequest(int _idExplorationRequest) {
	idExplorationRequest = _idExplorationRequest;
    }

    public int getIdExplorationRequest() {
	return idExplorationRequest;
    }

    public void setTerrainDataStatus(int _terrainDataStatus) {
	terrainDataStatus = _terrainDataStatus;
    }

    public int getTerrainDataStatus() {
	return terrainDataStatus;
    }

    public void setIdprovince(int _idprovince) {
	idprovince = _idprovince;
    }

    public int getIdprovince() {
	return idprovince;
    }

    public void setIdlocation(int _idlocation) {
	idlocation = _idlocation;
    }

    public int getIdlocation() {
	return idlocation;
    }

    public void setIdstreet(int _idstreet) {
	idstreet = _idstreet;
    }

    public int getIdstreet() {
	return idstreet;
    }

    public void setStreetnumber(int _streetnumber) {
	streetnumber = _streetnumber;
    }

    public int getStreetnumber() {
	return streetnumber;
    }

    public void setRecord(int _record) {
	record = _record;
    }

    public int getRecord() {
	return record;
    }
    //PARAMETROS AUXILIARES

    public void setIdTerrainDataAux(int _idTerrainDataAux) {
	idTerrainDataAux = _idTerrainDataAux;
    }

    public int getIdTerrainDataAux() {
	return idTerrainDataAux;
    }

    public void setCultivatedAux(boolean _cultivatedAux) {
	cultivatedAux = _cultivatedAux;
    }

    public boolean isCultivatedAux() {
	return cultivatedAux;
    }

    public void setSurroundedAux(boolean _surroundedAux) {
	surroundedAux = _surroundedAux;
    }

    public boolean isSurroundedAux() {
	return surroundedAux;
    }

    public void setBuiltAux(boolean _builtAux) {
	builtAux = _builtAux;
    }

    public boolean isBuiltAux() {
	return builtAux;
    }

    public void setUnpLowedAux(boolean _unpLowedAux) {
	unpLowedAux = _unpLowedAux;
    }

    public boolean isUnpLowedAux() {
	return unpLowedAux;
    }

    public void setPublicPlaceAux(boolean _publicPlaceAux) {
	publicPlaceAux = _publicPlaceAux;
    }

    public boolean isPublicPlaceAux() {
	return publicPlaceAux;
    }

    public void setHistoricalAux(boolean historicalAux) {
	this.historicalAux = historicalAux;
    }

    public boolean isHistoricalAux() {
	return historicalAux;
    }

    public void setReligiousAux(boolean _religiousAux) {
	religiousAux = _religiousAux;
    }

    public boolean isReligiousAux() {
	return religiousAux;
    }

    public void setNaturalReserveAux(boolean _naturalReserveAux) {
	naturalReserveAux = _naturalReserveAux;
    }

    public boolean isNaturalReserveAux() {
	return naturalReserveAux;
    }

    public void setOtherAux(String _otherAux) {
	otherAux = _otherAux;
    }

    public String getOtherAux() {
	return otherAux;
    }

    public void setOwnernameAux(String ownernameAux) {
	this.ownernameAux = ownernameAux;
    }

    public String getOwnernameAux() {
	return ownernameAux;
    }

    public void setIdowneraddressAux(int _idowneraddressAux) {
	idowneraddressAux = _idowneraddressAux;
    }

    public int getIdowneraddressAux() {
	return idowneraddressAux;
    }

    public void setIdProvinceAux(int _idProvinceAux) {
	idProvinceAux = _idProvinceAux;
    }

    public int getIdProvinceAux() {
	return idProvinceAux;
    }

    public void setIdlocationAux(int _idlocationAux) {
	idLocationAux = _idlocationAux;
    }

    public int getIdlocationAux() {
	return idLocationAux;
    }

    public void setIdstreetAux(int _idstreetAux) {
	idStreetAux = _idstreetAux;
    }

    public int getIdstreetAux() {
	return idStreetAux;
    }

    public void setStreetnumberAux(int _streetnumberAux) {
	streetnumberAux = _streetnumberAux;
    }

    public int getStreetnumberAux() {
	return streetnumberAux;
    }

    public void setTerrainDataStatusAux(int _terrainDataStatusAux) {
	terrainDataStatusAux = _terrainDataStatusAux;
    }

    public int getTerrainDataStatusAux() {
	return terrainDataStatusAux;
    }

    public void setIsMine(boolean _isMine) {
	isMine = _isMine;
    }

    public boolean isIsMine() {
	return isMine;
    }

    public void setIdReferenced(int _idReferenced) {
	idReferenced = _idReferenced;
    }

    public int getIdReferenced() {
	return idReferenced;
    }
    //METODOS

    private void retrieveTerrainDataStatus() {
	String param = String.valueOf(idReferenced) + ",'" + isMine + "'";
	System.out.println("param: " + param);
	terrainDataStatus = LibSQL.getInt("file.getTerrainDataStatus", param);
    }

    public void retrieveTerrainData(int _idReferenced) {
	String param = String.valueOf(_idReferenced) + ",'" + isMine + "'";
	if (idReferenced != -1) {
	    ResultSet terrainData = LibSQL.exFunction("file.getTerrainData", param);
	    try {
		if (terrainData.next()) {
		    idTerrainData = terrainData.getInt("idterraindata");
		    cultivated = terrainData.getBoolean("cultivated");
		    surrounded = terrainData.getBoolean("surrounded");
		    built = terrainData.getBoolean("built");
		    unpLowed = terrainData.getBoolean("unplowed");
		    publicPlace = terrainData.getBoolean("public");
		    historical = terrainData.getBoolean("historical");
		    religious = terrainData.getBoolean("religious");
		    naturalReserve = terrainData.getBoolean("naturalreserve");
		    other = terrainData.getString("other");
		    ownername = terrainData.getString("ownername");
		    idprovince = terrainData.getInt("idprovince");
		    idlocation = terrainData.getInt("idlocation");
		    idstreet = terrainData.getInt("idstreet");
		    streetnumber = terrainData.getInt("num_street");
		    //terrainDataStatus = terrainData.getInt("terraindatastatus");
		    copyData();
		} else {
		    setEmptyFields();
		}
	    } catch (Exception ex) {
		setEmptyFields();
		ex.printStackTrace();
	    }
	} else {
	    //System.out.println("CLASE VACIA");
	}
    }

    private void setEmptyFields() {
	idTerrainData = -1;
	cultivated = false;
	surrounded = false;
	built = false;
	unpLowed = false;
	publicPlace = false;
	historical = false;
	religious = false;
	naturalReserve = false;
	other = "";
	terrainDataStatus = 1;
    }

    public boolean recordTerrainData() {
	if (record == YES) {
	    if (terrainDataStatus == NO_MADE) {
		return executeInsert();
	    } else {
		return executeUpdate();
	    }
	} else {
	    return true;
	}
    }

    private boolean executeInsert() {
	String param = String.valueOf(idReferenced) + ",'" + cultivatedAux + "','" + surroundedAux + "','" + builtAux + "','" + unpLowedAux + "','" + publicPlaceAux + "','" + historicalAux + "','" + religiousAux + "','" + naturalReserveAux + "','" + otherAux + "','" + ownernameAux + "'," + String.valueOf(idProvinceAux) + "," + String.valueOf(idLocationAux) + "," + String.valueOf(idStreetAux) + "," + String.valueOf(streetnumberAux) + ",'" + isMine + "'";
	if (LibSQL.getBoolean("file.addNewTerrainData", param)) {
	    loadauxiliaryData();
	    return true;
	} else {
	    terrainDataStatus = INCOMPLETE;
	    return false;
	}
    }

    private boolean executeUpdate() {
	String param = String.valueOf(idReferenced) + ",'" + cultivatedAux + "','" + surroundedAux + "','" + builtAux + "','" + unpLowedAux + "','" + publicPlaceAux + "','" + historicalAux + "','" + religiousAux + "','" + naturalReserveAux + "','" + otherAux + "','" + ownernameAux + "'," + String.valueOf(idProvinceAux) + "," + String.valueOf(idLocationAux) + "," + String.valueOf(idStreetAux) + "," + String.valueOf(streetnumberAux) + ",'" + isMine + "'";
	if (LibSQL.getBoolean("file.updateTerrainData", param)) {
	    loadauxiliaryData();
	    //idTerrainDataAux = -1;
	    System.out.println("actualizó");
	    return true;
	} else {
	    System.out.println("NO actualizó");
	    return false;
	}
    }

    private void loadauxiliaryData() {
	cultivated = cultivatedAux;
	surrounded = surroundedAux;
	built = builtAux;
	unpLowed = unpLowedAux;
	publicPlace = publicPlaceAux;
	historical = historicalAux;
	religious = religiousAux;
	naturalReserve = naturalReserveAux;
	other = otherAux;
	ownername = ownernameAux;
	idprovince = idProvinceAux;
	idlocation = idLocationAux;
	idstreet = idStreetAux;
	streetnumber = streetnumberAux;
	terrainDataStatus = COMPLETE;
    }

    public void copyData() {
	idTerrainDataAux = idTerrainData;
	cultivatedAux = cultivated;
	surroundedAux = surrounded;
	builtAux = built;
	unpLowedAux = unpLowed;
	publicPlaceAux = publicPlace;
	historicalAux = historical;
	religiousAux = religious;
	naturalReserveAux = naturalReserve;
	otherAux = other;
	ownernameAux = ownername;
	idProvinceAux = idprovince;
	idLocationAux = idlocation;
	idStreetAux = idstreet;
	streetnumberAux = streetnumber;
    }

}

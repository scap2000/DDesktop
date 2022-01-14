package org.digitall.apps.legalprocs.manager.classes;

import java.sql.ResultSet;

import org.digitall.lib.sql.LibSQL;

public class UbicationClass {

    private int idFile = -1;
    private int idFileAux = -1;
    private int idLocationAddress = -1;
    private int idLocationAddressAux = -1;
    private int idCountry = -1;
    private int idCountryAux = -1;
    private int idprovince = -1;
    private int idprovinceAux = -1;
    private int idlocation = -1;
    private int idlocationAux = -1;
    private int ubicationStatus = 1;
    private int ubicationStatusAux = 1;
    private int record = 2;
    //CONSTANTES
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public UbicationClass() {
    }

    public UbicationClass(int _idFile) {
	idFile = _idFile;
	retrieveStatus(idFile);
    }

    public void setIdFile(int idFile) {
	this.idFile = idFile;
    }

    public int getIdFile() {
	return idFile;
    }

    public void setIdaddress(int _idLocationAddress) {
	idLocationAddress = _idLocationAddress;
    }

    public int getIdLocationAddress() {
	return idLocationAddress;
    }

    public void setIdCountry(int idCountry) {
	this.idCountry = idCountry;
    }

    public int getIdCountry() {
	return idCountry;
    }

    public void setIdprovince(int idprovince) {
	this.idprovince = idprovince;
    }

    public int getIdprovince() {
	return idprovince;
    }

    public void setIdlocation(int idlocation) {
	this.idlocation = idlocation;
    }

    public int getIdlocation() {
	return idlocation;
    }

    public void setUbicationStatus(int ubicationStatus) {
	this.ubicationStatus = ubicationStatus;
    }

    public int getUbicationStatus() {
	return ubicationStatus;
    }

    private void retrieveStatus(int idFile) {
	String params = String.valueOf(idFile);
	ubicationStatus = LibSQL.getInt("file.getUbicationStatus", params);
	ubicationStatusAux = ubicationStatus;
    }

    public void retrieveData() {
	if (idFile != -1) {
	    StringBuffer params = new StringBuffer();
	    params.append(String.valueOf(getIdFile()));
	    ResultSet data = LibSQL.exFunction("file.getUbicationData", params.toString());
	    try {
		if (data.next()) {
		    idLocationAddress = data.getInt("idlocationaddress");
		    idCountry = data.getInt("idcountry");
		    idprovince = data.getInt("idprovince");
		    idlocation = data.getInt("idlocation");
		    copyData();
		}
	    } catch (Exception ex) {
		ex.printStackTrace();
		// inicializar las variables en -1
	    }
	} else {
	    // inicializar las variables en -1
	}
    }

    public void setIdFileAux(int _idFileAux) {
	idFileAux = _idFileAux;
    }

    public int getIdFileAux() {
	return idFileAux;
    }

    public void setIdLocationAddressAux(int _idLocationAddressAux) {
	idLocationAddressAux = _idLocationAddressAux;
    }

    public int getIdLocationAddressAux() {
	return idLocationAddressAux;
    }

    public void setIdCountryAux(int _idCountryAux) {
	idCountryAux = _idCountryAux;
    }

    public int getIdCountryAux() {
	return idCountryAux;
    }

    public void setIdprovinceAux(int _idprovinceAux) {
	idprovinceAux = _idprovinceAux;
    }

    public int getIdprovinceAux() {
	return idprovinceAux;
    }

    public void setIdlocationAux(int _idlocationAux) {
	idlocationAux = _idlocationAux;
    }

    public int getIdlocationAux() {
	return idlocationAux;
    }

    public void clearAuxiliaryData() {
	idFileAux = -1;
	idLocationAddressAux = -1;
	idCountryAux = -1;
	idprovinceAux = -1;
	idlocationAux = -1;
    }

    public boolean recordUbication() {
	if (record == YES) {
	    if (ubicationStatus == NO_MADE || ubicationStatus == INCOMPLETE) {
		return executeInsert();
	    } else {
		return executeUpdate();
	    }
	} else {
	    return true;
	}
    }

    private boolean executeInsert() {
	StringBuffer params = new StringBuffer();
	params.append(String.valueOf(idFile) + "," + idCountryAux + "," + idprovinceAux + "," + idlocationAux);
	if (LibSQL.getBoolean("file.addNewLocationAddress", params.toString())) {
	    loadUbicationData();
	    return true;
	} else {
	    return false;
	    //ubicationStatus = INCOMPLETE;
	}
    }

    private boolean executeUpdate() {
	StringBuffer params = new StringBuffer();
	params.append(String.valueOf(idFile) + "," + idCountryAux + "," + idprovinceAux + "," + idlocationAux);
	if (LibSQL.getBoolean("updateLocationAddress", params.toString())) {
	    loadUbicationData();
	    return true;
	} else {
	    return false;
	}
    }

    private void loadUbicationData() {
	idCountry = idCountryAux;
	idprovince = idprovinceAux;
	idlocation = idlocationAux;
	ubicationStatus = COMPLETE;
    }

    public void setRecord(int _record) {
	record = _record;
    }

    public int getRecord() {
	return record;
    }

    public void setUbicationStatusAux(int _ubicationStatusAux) {
	ubicationStatusAux = _ubicationStatusAux;
    }

    public int getUbicationStatusAux() {
	return ubicationStatusAux;
    }

    private void copyData() {
	idLocationAddressAux = idLocationAddress;
	idCountryAux = idCountry;
	idprovinceAux = idprovince;
	idlocationAux = idlocation;
    }

}

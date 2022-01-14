package org.digitall.apps.legalprocs.manager.classes;

import java.sql.ResultSet;

import org.digitall.lib.sql.LibSQL;

public class EntitiesByFileClass {

    private int idFile = -1;
    private int idFileAux = -1;
    private boolean isSolicitor = false;
    private boolean isSolicitorAux = false;
    private boolean isPerson = false;
    private boolean isPersonAux = false;
    private int idReferenced = -1;
    private int idReferencedAux = -1;
    private int entityStatus = 1;
    private int entityStatusAux = 1;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private int record = 2;
    private static final int YES = 1;
    private static final int NO = 2;

    public EntitiesByFileClass() {

    }

    public EntitiesByFileClass(int _idFile, boolean _issolicitor) {
	idFile = _idFile;
	isSolicitor = _issolicitor;
	retrieveStatus(idFile, isSolicitor);
    }

    public void setIdFile(int _idFile) {
	idFile = _idFile;
    }

    public int getIdFile() {
	return idFile;
    }

    public void setIdReferenced(int _idReferenced) {
	idReferenced = _idReferenced;
    }

    public void setIssolicitor(boolean _issolicitor) {
	isSolicitor = _issolicitor;
    }

    public boolean IsSolicitor() {
	return isSolicitor;
    }

    public void setIsperson(boolean _isperson) {
	isPerson = _isperson;
    }

    public boolean isPerson() {
	return isPerson;
    }

    private void retrieveStatus(int _idFile, boolean _issolicitor) {
	String params = String.valueOf(_idFile) + "," + _issolicitor;
	idFile = _idFile;
	entityStatus = LibSQL.getInt("file.getEntityStatus", params);
	entityStatusAux = entityStatus;
    }

    public void setEntityStatus(int _entityStatus) {
	entityStatus = _entityStatus;
    }

    public int getEntityStatus() {
	return entityStatus;
    }

    public int getIdReferenced() {
	return idReferenced;
    }

    public void setIdFileAux(int _idFileAux) {
	idFileAux = _idFileAux;
    }

    public int getIdFileAux() {
	return idFileAux;
    }

    public void setIsSolicitorAux(boolean _isSolicitorAux) {
	isSolicitorAux = _isSolicitorAux;
    }

    public boolean isSolicitorAux() {
	return isSolicitorAux;
    }

    public void setIsPersonAux(boolean _isPersonAux) {
	isPersonAux = _isPersonAux;
    }

    public boolean isPersonAux() {
	return isPersonAux;
    }

    public void setIdReferencedAux(int _idReferencedAux) {
	idReferencedAux = _idReferencedAux;
    }

    public int getIdReferencedAux() {
	return idReferencedAux;
    }

    public void setEntityStatusAux(int _entityStatusAux) {
	entityStatusAux = _entityStatusAux;
    }

    public int getEntityStatusAux() {
	return entityStatusAux;
    }

    public void setRecord(int _record) {
	record = _record;
    }

    public int getRecord() {
	return record;
    }

    public int updateStatus() {
	return -1;
    }

    public int getStatusAux() {
	return entityStatusAux;
    }

    public void retrieveData(boolean _isSolicitor) {
	String params = String.valueOf(idFile) + "," + _isSolicitor;
	ResultSet data = LibSQL.exFunction("file.getEntityByFileData", params);
	try {
	    if (data.next()) {
		idReferenced = data.getInt("idreference");
		isSolicitor = data.getBoolean("solicitor");
		isPerson = data.getBoolean("person");
		copyData();
	    } else {
		//System.out.println("no existe ningun dato asociado a los parametros ingresados");
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    idReferenced = -1;
	    isSolicitor = false;
	    isPerson = false;
	}
    }

    public void clearAuxiliaryData() {
	idFileAux = -1;
	idReferencedAux = -1;
	isSolicitorAux = false;
	isPersonAux = false;
    }

    public boolean updateSolicitor() {
	return true;
    }

    public boolean recordEntityByFile() {
	if (record == YES) {
	    if (entityStatus == NO_MADE) {
		return executeInsert();
	    } else {
		return executeUpdate();
	    }
	} else {
	    return true;
	}
    }

    private boolean executeInsert() {
	String params = String.valueOf(idFile) + "," + isPersonAux + "," + isSolicitorAux + "," + idReferencedAux;
	if (LibSQL.getBoolean("file.addNewEntityByFile", params)) {
	    loadSolicitorData();
	    return true;
	} else {
	    return false;
	}
    }

    private boolean executeUpdate() {
	StringBuffer params = new StringBuffer();
	params.append(String.valueOf(idFile) + ", " + idReferencedAux + ", " + isPersonAux + ", " + isSolicitorAux);
	if (LibSQL.getBoolean("file.updateEntityByFile", params.toString())) {
	    loadSolicitorData();
	    return true;
	} else {
	    return false;
	}
    }

    private void loadSolicitorData() {
	isSolicitor = isSolicitorAux;
	isPerson = isPersonAux;
	idReferenced = idReferencedAux;
	entityStatus = COMPLETE;
	entityStatusAux = COMPLETE;
    }

    private void copyData() {
	idFileAux = idFile;
	isSolicitorAux = isSolicitor;
	isPersonAux = isPerson;
	idReferencedAux = idReferenced;
    }

}

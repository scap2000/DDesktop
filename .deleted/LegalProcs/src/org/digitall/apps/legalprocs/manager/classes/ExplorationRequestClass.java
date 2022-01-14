package org.digitall.apps.legalprocs.manager.classes;

import java.sql.ResultSet;

import java.util.Date;

import org.digitall.lib.sql.LibSQL;

//

public class ExplorationRequestClass {

    private int idExplorationRequest = -1;
    // Permiso de Exploración
    private String mineralCategory = "";
    //CategorÃ­a del Mineral
    private int idFile = -1;
    //Expediente
    private int idPolygon = -1;
    //Poligonos
    private int idExplorationScheme = -1;
    //Programa MÃ­nimo de Exploración
    private int idTerrainData = -1;
    //Datos del Terreno
    private int termDays = -1;
    //Plazo solicitado
    private int workCycleType = -1;
    //PerÃ­odo de trabajo anual admitido. 1: Anual, 2: Meses, 3:Dias
    private int permissionGranted = -1;
    //Cantidad de permisos otorgados
    private int measurementUnitsGranted = -1;
    //Cantidad de unidades de medida otorgadas
    private Date registeredDate = null;
    //Fecha de registración
    private int registeredNumber = -1;
    //integer
    private int workCycleNumber = -1;
    //Cantidad de Dias, meses o años, de acuerdo al tipo seleccionado en workcycletype
    private boolean workProgram = false;
    //Programa mÃ­nimo de trabajo
    private boolean swornStatement = false;
    //Declaración Jurada, True: Comprometido, False: No Comprometido
    private int explorationrequeststatus = 1;
    //Status 1: No Iniciado
    private String estado = "";
    private boolean swornStatementAux = false;
    private int recordERSwornStatement = 2;
    private int recordERExpSch = 2;
    private int solicitorStatus = 1;
    private int legalRepStatus = 1;
    private int ubicationStatus = -1;
    private int erPolygonStatus = -1;
    private int terrainDataStatus = 1;
    private int ERDeclarationStatus = 1;
    private int ERDeclarationStatusAux = 1;
    private int ERESStatus = 1;
    private int ExplorationSchemeStatus = 1;
    private int ExplorationSchemeStatusAux = 1;
    //OBJECTS
    private EntitiesByFileClass solicitorClass;
    private EntitiesByFileClass legalRepClass;
    private UbicationClass ubicationClass;
    private ERPolygon erPolygonsClass;
    private TerrainDataClass terrainDataClass;
    private ExplorationSchemeClass explorationSchemeClass;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public ExplorationRequestClass() {

    }

    public ExplorationRequestClass(int _idExplorationRequest, int _idFile) {
	idFile = _idFile;
	idExplorationRequest = _idExplorationRequest;
	retrieveStatus(idExplorationRequest);
    }

    private void retrieveData(int _idExplorationRequest) {
	//OBTENER DATOS DE LA BASE DE DATOS
	//SI EL ER NO EXISTE, SETEAR idER = -1;
	String params = String.valueOf(_idExplorationRequest);
	ResultSet data = LibSQL.exFunction("file.getExplorationRequest", params);
	try {
	    if (data.next()) {
		idExplorationRequest = data.getInt("idexplorationrequest");
		mineralCategory = data.getString("mineralcategory");
		idFile = data.getInt("idfile");
		idPolygon = data.getInt("idpolygon");
		idExplorationScheme = data.getInt("idexplorationscheme");
		idTerrainData = data.getInt("idterraindata");
		termDays = data.getInt("termdays");
		workCycleType = data.getInt("workcycletype");
		permissionGranted = data.getInt("permissiongranted");
		measurementUnitsGranted = data.getInt("measurementunitsgranted");
		registeredDate = data.getDate("registereddate");
		registeredNumber = data.getInt("registerednumber");
		workCycleNumber = data.getInt("workcyclenumber");
		workProgram = data.getBoolean("workprogram");
		swornStatement = data.getBoolean("swornstatement");
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    idExplorationRequest = -1;
	}
	//LUEGO OBTENER LOS DATOS DE LOS DEMAS OBJETOS
	if (idExplorationRequest != -1) {
	    // CARGO EL OBJETO SOLICITORCLASS SOLO CON EL ESTADO (SOLICITANTE)
	} else {
	}
    }

    protected boolean saveData() {
	boolean saved = false;
	//TRATAR D GRABAR DATOS
	//SI DA ERROR, DEVOLVER FALSE, SI SALIO BIEN DEVOLVER TRUE
	return saved;
    }

    public void setIdExplorationRequest(int _idExplorationRequest) {
	idExplorationRequest = _idExplorationRequest;
    }

    public int getIdExplorationRequest() {
	return idExplorationRequest;
    }

    public void setMineralCategory(String _mineralCategory) {
	mineralCategory = _mineralCategory;
    }

    public String getMineralCategory() {
	return mineralCategory;
    }

    public void setIdFile(int _idFile) {
	idFile = _idFile;
    }

    public int getIdFile() {
	return idFile;
    }

    public void setIdPolygon(int _idPolygon) {
	idPolygon = _idPolygon;
    }

    public int getIdPolygon() {
	return idPolygon;
    }

    public void setIdExplorationScheme(int _idExplorationScheme) {
	idExplorationScheme = _idExplorationScheme;
    }

    public int getIdExplorationScheme() {
	return idExplorationScheme;
    }

    public void setIdTerrainData(int _idTerrainData) {
	idTerrainData = _idTerrainData;
    }

    public int getIdTerrainData() {
	return idTerrainData;
    }

    public void setTermDays(int _termDays) {
	termDays = _termDays;
    }

    public int getTermDays() {
	return termDays;
    }

    public void setWorkCycleType(int _workCycleType) {
	workCycleType = _workCycleType;
    }

    public int getWorkCycleType() {
	return workCycleType;
    }

    public void setPermissionGranted(int _permissionGranted) {
	permissionGranted = _permissionGranted;
    }

    public int getPermissionGranted() {
	return permissionGranted;
    }

    public void setMeasurementUnitsGranted(int _measurementUnitsGranted) {
	measurementUnitsGranted = _measurementUnitsGranted;
    }

    public int getMeasurementUnitsGranted() {
	return measurementUnitsGranted;
    }

    public void setRegisteredDate(Date _registeredDate) {
	registeredDate = _registeredDate;
    }

    public Date getRegisteredDate() {
	return registeredDate;
    }

    public void setRegisteredNumber(int _registeredNumber) {
	registeredNumber = _registeredNumber;
    }

    public int getRegisteredNumber() {
	return registeredNumber;
    }

    public void setWorkCyclenumber(int _workCyclenumber) {
	workCycleNumber = _workCyclenumber;
    }

    public int getWorkCyclenumber() {
	return workCycleNumber;
    }

    public void setWorkProgram(boolean _workProgram) {
	workProgram = _workProgram;
    }

    public boolean isWorkProgram() {
	return workProgram;
    }

    public void setSwornstatement(boolean _swornstatement) {
	swornStatement = _swornstatement;
    }

    public boolean isSwornstatement() {
	return swornStatement;
    }

    public void setEstado(String _estado) {
	estado = _estado;
    }

    public String getEstado() {
	return estado;
    }

    public int getStatus() {
	int status = -1;
	return status;
    }

    public void setExplorationrequeststatus(int _explorationrequeststatus) {
	explorationrequeststatus = _explorationrequeststatus;
    }

    public int getExplorationrequeststatus() {
	return explorationrequeststatus;
    }

    private void retrieveStatus(int _idExplorationRequest) {
	String params = String.valueOf(_idExplorationRequest);
	explorationrequeststatus = LibSQL.getInt("file.getExplorationRequestStatus", params);
	// CARGO EL OBJETO SOLICITORCLASS SOLO CON EL ESTADO (SOLICITANTE)
	solicitorClass = new EntitiesByFileClass(idFile, true);
	solicitorStatus = solicitorClass.getEntityStatus();
	// CARGO EL OBJETO LEGALREPCLASS SOLO CON EL ESTADO (REPRESENTANTE LEGAL)
	legalRepClass = new EntitiesByFileClass(idFile, false);
	legalRepStatus = legalRepClass.getEntityStatus();
	// CARGO EL OBJETO UBICATIONCLASS SOLO CON EL ESTADO 
	ubicationClass = new UbicationClass(idFile);
	ubicationStatus = ubicationClass.getUbicationStatus();
	// CARGO EL OBJETO POLYGONCLASS SOLO CON EL ESTADO 
	erPolygonsClass = new ERPolygon(idExplorationRequest, false);
	erPolygonStatus = erPolygonsClass.getErPolygonStatus();
	// CARGO EL OBJETO TERRAIN DATA SOLO CON EL ESTADO 
	terrainDataClass = new TerrainDataClass(idExplorationRequest, false);
	terrainDataStatus = terrainDataClass.getTerrainDataStatus();
	// CARGO EL OBJETO PROGRAM SOLO CON EL ESTADO 
	explorationSchemeClass = new ExplorationSchemeClass(idExplorationRequest);
	ExplorationSchemeStatus = explorationSchemeClass.getExplorationSchemeStatus();
	// CARGO EL PARAMETRO SWORNSTATEMENT
	StringBuffer param = new StringBuffer();
	param.append(String.valueOf(_idExplorationRequest));
	swornStatement = LibSQL.getBoolean("file.getSwornStatement", params.toString());
	if (swornStatement) {
	    ERDeclarationStatus = COMPLETE;
	} else {
	    ERDeclarationStatus = INCOMPLETE;
	}
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

    public void setSolicitorClass(EntitiesByFileClass _solicitorClass) {
	this.solicitorClass = _solicitorClass;
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

    public int getSolicitorStatusAux() {
	return solicitorClass.getStatusAux();
    }

    public int getLegalRepStatusAux() {
	return legalRepClass.getStatusAux();
    }

    public int getUbicationStatusAux() {
	return ubicationClass.getUbicationStatusAux();
    }

    public int getERPolygonStatusAux() {
	return erPolygonsClass.getErPolygonStatusAux();
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

    public void setPolygonsClass(ERPolygon _erPolygonsClass) {
	erPolygonsClass = _erPolygonsClass;
    }

    public ERPolygon getERPolygonsClass() {
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
	return terrainDataClass.getTerrainDataStatus();
    }
    /*public void setTerrainDataStatusAux(int _terrainDataStatusAux) {
	terrainDataStatusAux = _terrainDataStatusAux;
    }*/

    public int getTerrainDataStatusAux() {
	return terrainDataClass.getTerrainDataStatusAux();
    }

    public void setERDeclarationStatus(int _ERDeclarationStatus) {
	ERDeclarationStatus = _ERDeclarationStatus;
    }

    public int getERDeclarationStatus() {
	return ERDeclarationStatus;
    }

    public void setSwornStatementAux(boolean _swornStatementAux) {
	swornStatementAux = _swornStatementAux;
    }

    public boolean isSwornStatementAux() {
	return swornStatementAux;
    }

    public void setRecordERSwornStatement(int _recordERSwornStatement) {
	recordERSwornStatement = _recordERSwornStatement;
    }

    public int getRecordERSwornStatement() {
	return recordERSwornStatement;
    }

    public void setERDeclarationStatusAux(int _ERDeclarationStatusAux) {
	ERDeclarationStatusAux = _ERDeclarationStatusAux;
    }

    public int getERDeclarationStatusAux() {
	return ERDeclarationStatusAux;
    }

    public void setExplorationSchemeStatusAux(int _explorationSchemeStatusAux) {
	ExplorationSchemeStatusAux = _explorationSchemeStatusAux;
    }

    public int getExplorationSchemeStatusAux() {
	return ExplorationSchemeStatusAux;
    }

    public void setExplorationSchemeClass(ExplorationSchemeClass _explorationSchemeClass) {
	explorationSchemeClass = _explorationSchemeClass;
    }

    public ExplorationSchemeClass getExplorationSchemeClass() {
	return explorationSchemeClass;
    }

    public void setERESStatus(int _eRESStatus) {
	ERESStatus = _eRESStatus;
    }

    public int getERESStatus() {
	return ERESStatus;
    }

    public void setRecordERExpSch(int _recordERExpSch) {
	recordERExpSch = _recordERExpSch;
    }

    public int getRecordERExpSch() {
	return recordERExpSch;
    }

    public void setExplorationSchemeStatus(int _explorationSchemeStatus) {
	ExplorationSchemeStatus = _explorationSchemeStatus;
    }

    public int getExplorationSchemeStatus() {
	return ExplorationSchemeStatus;
    }
    //	METODOS

    public boolean recordData() {
	if (solicitorClass.recordEntityByFile() && legalRepClass.recordEntityByFile() && ubicationClass.recordUbication() && erPolygonsClass.recordPolygon() && terrainDataClass.recordTerrainData() && recordSwornStatement() && recordERExpSch() && explorationSchemeClass.recordExplorationSchemeData()) {
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
	if (solicitorStatus == COMPLETE && legalRepStatus == COMPLETE && ubicationStatus == COMPLETE && erPolygonStatus == COMPLETE) {
	    StringBuffer params = new StringBuffer();
	    params.append(idFile + "," + idExplorationRequest + "," + INCOMPLETE);
	    if (LibSQL.getBoolean("file.updateExplorationRequestStatus", params.toString())) {
		explorationrequeststatus = INCOMPLETE;
	    } else {
		//analizar
	    }
	}
    }

    private boolean recordSwornStatement() {
	StringBuffer params = new StringBuffer();
	params.append(idExplorationRequest);
	params.append("," + swornStatementAux);
	if (LibSQL.getBoolean("file.updateSwornStatement", params.toString())) {
	    swornStatement = swornStatementAux;
	    if (swornStatement) {
		ERDeclarationStatus = COMPLETE;
	    } else {
		ERDeclarationStatus = INCOMPLETE;
	    }
	    return true;
	} else {
	    swornStatementAux = false;
	    ERDeclarationStatus = INCOMPLETE;
	    return false;
	}
    }

    public void retrieveExplorationSchemeData() {
	StringBuffer params = new StringBuffer();
	params.append(idExplorationRequest);
	ResultSet ESData = LibSQL.exFunction("file.getExpSchemeData", params.toString());
	try {
	    if (ESData.next()) {
		idExplorationScheme = ESData.getInt("idexplorationscheme");
		termDays = ESData.getInt("termdays");
		workCycleType = ESData.getInt("workcycletype");
		permissionGranted = ESData.getInt("permissiongranted");
		measurementUnitsGranted = ESData.getInt("measurementunitsgranted");
		registeredNumber = ESData.getInt("registerednumber");
		workCycleNumber = ESData.getInt("workcyclenumber");
		workProgram = ESData.getBoolean("workprogram");
	    } else {
		//analizar
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private boolean recordERExpSch() {
	StringBuffer params = new StringBuffer();
	params.append(idExplorationRequest);
	params.append("," + termDays);
	params.append("," + workCycleType);
	params.append("," + workCycleNumber);
	params.append("," + permissionGranted);
	params.append("," + measurementUnitsGranted);
	params.append("," + workProgram);
	if (LibSQL.getBoolean("file.updateERExplorationScheme", params.toString())) {
	    ERESStatus = COMPLETE;
	    return true;
	} else {
	    ERESStatus = INCOMPLETE;
	    return false;
	}
    }

}

package org.digitall.apps.legalprocs.manager.classes;

import java.sql.ResultSet;

import java.util.Date;

import org.digitall.lib.sql.LibSQL;

public class FileClass {

    private int idFile = -1;
    //# Expediente
    private int idLocationAddress = -1;
    //# Direcciones
    private int idPointSO = -1;
    //# Registro Catastral(Coord.S.O.)
    private int idPointNE = -1;
    //# Registro Catastral(Coord.N.E.)
    private int fileNumber = -1;
    //Numero de Expediente
    private String fileLetter = "";
    // Letra del Expediente
    private int fileYear = -1;
    //Aï¿½o del Expediente
    private String mineName = "";
    //Nombre de la Mina
    private Date date = null;
    //Fecha del Expediente
    private String description = "";
    //Descripcion
    private int idPointPD = -1;
    //# Registro Catastral (Coord. P.D.)
    private boolean isMine = false;
    //TRUE: MINA; FALSE: CATEO
    private int idProvince = -1;
    //# Provincia
    private int idExplorationRequest = -1;
    private int idMineRequest = -1;
    // OBJETOS
    private ExplorationRequestClass explorationRequestClass;
    private CadastralRegister cadastralRegister;
    private ERPolygon polygon;
    private MineRequestClass mineRequestClass;
    //VARIABLES
    private int explorationRequestStatus = -1;
    //CONSTANTES
    public static int NO_MADE = 1;
    public static int INCOMPLETE = 2;
    public static int COMPLETE = 3;
    public static int REJECTED = 4;

    public FileClass() {
	loadObject();
    }

    public FileClass(int _idFile) {
	idFile = _idFile;
	retrieveData(idFile);
    }

    private void retrieveData(int _idFile) {
	//OBTENER DATOS DE LA BASE DE DATOS
	String params = String.valueOf(_idFile);
	ResultSet data = LibSQL.exFunction("file.getFileData", params);
	try {
	    if (data.next()) {
		idLocationAddress = data.getInt("idlocationaddress");
		idPointSO = data.getInt("idpointso");
		idPointNE = data.getInt("idpointne");
		fileNumber = data.getInt("filenumber");
		fileLetter = data.getString("fileletter");
		fileYear = data.getInt("fileyear");
		mineName = data.getString("minename");
		date = data.getDate("date");
		description = data.getString("description");
		idPointPD = data.getInt("idpointpd");
		isMine = data.getBoolean("mine");
		idProvince = data.getInt("idprovince");
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	    idExplorationRequest = -1;
	}
	//SI EL EXPEDIENTE NO EXISTE, SETEAR idFile = -1;
	//LUEGO OBTENER LOS DATOS DE LOS DEMAS OBJETOS
	if (idFile != -1) {
	    if (isMine) {
		StringBuffer param = new StringBuffer();
		param.append(idFile);
		idMineRequest = LibSQL.getInt("file.getIdMineRequest", param.toString());
		if (idMineRequest != -1) {
		    mineRequestClass = new MineRequestClass(idMineRequest, idFile);
		} else {
		    mineRequestClass = new MineRequestClass();
		}
	    } else {
		//ES CATEO
		params = String.valueOf(idFile);
		idExplorationRequest = LibSQL.getInt("file.getIdExplorationRequest", params);
		if (idExplorationRequest != -1) {
		    explorationRequestClass = new ExplorationRequestClass(idExplorationRequest, idFile);
		} else {
		    explorationRequestClass = new ExplorationRequestClass();
		}
		explorationRequestStatus = explorationRequestClass.getExplorationrequeststatus();
	    }
	}
    }

    protected boolean saveData() {
	boolean saved = false;
	if (idFile == -1) {
	    //INSERT
	    //idFile = idFile obtenido en la inserciï¿½n
	} else {
	    //UPDATE
	}
	explorationRequestClass.saveData();
	return saved;
    }

    public void setExplorationRequest(ExplorationRequestClass _explorationRequestClass) {
	explorationRequestClass = _explorationRequestClass;
    }

    public ExplorationRequestClass getExplorationRequestClass() {
	return explorationRequestClass;
    }

    public void setCadastralRegister(CadastralRegister _cadastralRegister) {
	cadastralRegister = _cadastralRegister;
    }

    public CadastralRegister getCadastralRegister() {
	return cadastralRegister;
    }

    public void setIdFile(int _idFile) {
	idFile = _idFile;
    }

    public int getIdFile() {
	return idFile;
    }

    public void setIdLocationAddress(int _idLocationAddress) {
	idLocationAddress = _idLocationAddress;
    }

    public int getIdLocationAddress() {
	return idLocationAddress;
    }

    public void setIdPointSO(int _idPointSO) {
	idPointSO = _idPointSO;
    }

    public int getIdPointSO() {
	return idPointSO;
    }

    public void setIdPointNE(int _idPointNE) {
	idPointNE = _idPointNE;
    }

    public int getIdPointNE() {
	return idPointNE;
    }

    public void setFileLetter(String _fileLetter) {
	fileLetter = _fileLetter;
    }

    public String getFileLetter() {
	return fileLetter;
    }

    public void setFileYear(int _fileYear) {
	fileYear = _fileYear;
    }

    public int getFileYear() {
	return fileYear;
    }

    public void setMineName(String _mineName) {
	mineName = _mineName;
    }

    public String getMineName() {
	return mineName;
    }

    public void setDate(Date _date) {
	date = _date;
    }

    public Date getDate() {
	return date;
    }

    public void setDescription(String _description) {
	description = _description;
    }

    public String getDescription() {
	return description;
    }

    public void setIdPointPD(int _idPointPD) {
	idPointPD = _idPointPD;
    }

    public int getIdPointPD() {
	return idPointPD;
    }

    public void setIsMine(boolean _isMine) {
	isMine = _isMine;
    }

    public boolean isMine() {
	return isMine;
    }

    public void setIdProvince(int _idProvince) {
	idProvince = _idProvince;
    }

    public int getIdProvince() {
	return idProvince;
    }

    public void setIdExplorationRequest(int _idExplorationRequest) {
	idExplorationRequest = _idExplorationRequest;
    }

    public int getIdExplorationRequest() {
	return idExplorationRequest;
    }

    public void setFileNumber(int _fileNumber) {
	fileNumber = _fileNumber;
    }

    public int getFileNumber() {
	return fileNumber;
    }

    public int getERIdPolygon() {
	return explorationRequestClass.getIdPolygon();
    }

    public void setExplorationRequestStatus(int _explorationRequestStatus) {
	explorationRequestStatus = _explorationRequestStatus;
    }

    public int getExplorationRequestStatus() {
	//return explorationRequestStatus;
	return explorationRequestClass.getExplorationrequeststatus();
    }

    public void setIdMineRequest(int _idMineRequest) {
	idMineRequest = _idMineRequest;
    }

    public int getIdMineRequest() {
	return idMineRequest;
    }

    public void setMineRequestStatus(int _mineRequestStatus) {
	mineRequestClass.setMineRequestStatus(_mineRequestStatus);
    }

    public int getMineRequestStatus() {
	return mineRequestClass.getMineRequestStatus();
    }

    public void setMineRequestClass(MineRequestClass _mineRequestClass) {
	mineRequestClass = _mineRequestClass;
    }

    public MineRequestClass getMineRequestClass() {
	return mineRequestClass;
    }

    private void loadObject() {
	cadastralRegister = new CadastralRegister();
	explorationRequestClass = new ExplorationRequestClass();
	polygon = new ERPolygon();
    }

}

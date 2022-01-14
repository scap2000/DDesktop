package org.digitall.apps.legalprocs.manager.classes;

import java.util.Date;

import org.digitall.apps.legalprocs.manager.ExplorationScheme;

public class File {

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
    private String fileLetter = " ";
    // Letra del Expediente
    private int fileYear = -1;
    //Aï¿½o del Expediente
    private String mineName = " ";
    //Nombre de la Mina
    private Date date = null;
    //Fecha del Expediente
    private String description = " ";
    //Descripcion
    private int idPointPD = -1;
    //# Registro Catastral (Coord. P.D.)
    private boolean isMine = false;
    //TRUE: MINA; FALSE: CATEO
    private int idProvince = -1;
    //# Provincia
    private int idExplorationRequest = -1;
    private ExplorationScheme explorationScheme;
    private CadastralRegister cadastralRegister;
    private ExplorationRequestClass explorationRequest;
    private EntitiesByFileClass entitiesByFile;
    private ERPolygon polygon;
    private TerrainDataClass terrentData;

    public File() {
	loadObject();
    }

    public File(int _idFile) {
	idFile = _idFile;
	retrieveData(idFile);
    }

    private void retrieveData(int _idFile) {
	//OBTENER DATOS DE LA BASE DE DATOS
	//SI EL EXPEDIENTE NO EXISTE, SETEAR idFile = -1;
	//LUEGO OBTENER LOS DATOS DE LOS DEMAS OBJETOS
	if (idFile != -1) {
	    if (isMine) {

	    } else {
		//ES CATEO
		//TENGO QUE OBTENER EL idExplorationRequest
		if (idExplorationRequest != -1) {
		    explorationRequest = new ExplorationRequestClass(idExplorationRequest, idFile);
		} else {
		    explorationRequest = new ExplorationRequestClass();
		}
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
	explorationRequest.saveData();
	return saved;
    }

    public void setExplorationRequest(ExplorationRequestClass _explorationRequest) {
	explorationRequest = _explorationRequest;
    }

    public ExplorationRequestClass getExplorationRequest() {
	return explorationRequest;
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

    public boolean isIsMine() {
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

    private void loadObject() {
	explorationScheme = new ExplorationScheme();
	cadastralRegister = new CadastralRegister();
	explorationRequest = new ExplorationRequestClass();
	entitiesByFile = new EntitiesByFileClass();
	polygon = new ERPolygon();
	terrentData = new TerrainDataClass();
    }

    public void setFileNumber(int _fileNumber) {
	fileNumber = _fileNumber;
    }

    public int getFileNumber() {
	return fileNumber;
    }

}

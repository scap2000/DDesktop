package org.digitall.apps.legalprocs.calendar.wizard;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.digitall.lib.sql.LibSQL;

//

/** Argumentos:
 * args[0]: True: nuevo--> hacer insert, False: Update--> hacer update
 * args[1]: id del Solicitante;
 * args[2]: id del Represemtante Legal
 * args[3]: id del Pais ---------> "Ubicación de la Solicitud"
 * args[4]: id de la Provincia --> "Ubicación de la Solicitud"
 * args[5]: id de la Localidad --> "Ubicación de la Solicitud"
 * args[6]: Campo que contiene la cantidad de puntos del Polígono
 * args[7]: Campo que contiene la consulta para guardar los puntos del Polígono (coord. GK).
 * args[8]: Area del Polígono
 * args[9]: Categoría del mineral
 *
 * A partir de aca es para Terrain Data
 * args[10]: Cultivado
 * args[11]: Cercado
 * args[12]: Edificado
 * args[13]: Erial
 * args[14]: Sitio Público
 * args[15]: Sitio Histórico
 * args[16]: Sitio Religioso
 * args[17]: Reserva Natural
 * args[18]: Otros
 * args[19]: Nombre del propietario
 * args[20]: idprovincia
 * args[21]: idlocalidad
 * args[22]: idcalle
 * args[23]: numero de la calle
 * A partir de aca es para Program
 * args[24]: Plazo
 * args[25]: CycleType
 * args[26]: CycleNumber
 * args[27]: Permisos Otorgados
 * args[28]: Unidades Otorgadas
 * args[29]: Programa mínimo de Trabajo, True: adjunta PMT, FALSE: no adjuntaPMT
 *
 *para la declaracion jurada
 * args[30]: Declaracion jurada.
 * args[31]: id del Programa Mínimo de Exploración registrado (WP)
 * args[32]: Programa mínimo de Trabajo, True: adjunta PMT, FALSE: no adjuntaPMT
 * args[33]: Estado del Cateo.
 *
 * args[35]: ifFile
 * args[36]: Programa mínimo de Trabajo, True: adjunta PMT, FALSE: no adjuntaPMT
 * args[37]: Tipo: Nuevo - Cateo - etc;
 *
 * para el expediente
 * args[40]: filenumber
 * args[41]: fileletter
 * args[41]: fileyear
 */
public class ExplorationRequestClass_Old {

    private String[] args;
    private boolean ubication, solicitor, legalRep, terrainData, coordenates;
    private String idLocationAddress = "", IdFile = "", TDId = "", idterrarinowners = "", idPoly = "";
    private String idExplorationRequest = "";
    //, idpolygon = "";
    private int numPoints = 0;
    private double[] xd;
    private double[] yd;
    private boolean newPropspection;
    private String titleText;
    private boolean newER;
    private int idER = -1;
    private String catMineral = "";
    private int idpolygon = -1;
    private int idLegalRep = -1;
    private int idCountry = -1;
    private int idProvince = -1;
    private int idLocation = -1;
    private int polygonNumPoints = -1;
    private String sqlPolygon = "";
    private double polygonArea = -1;
    private int mineralCategory = -1;

    public ExplorationRequestClass_Old() {

    }

    public void setArgument(String _arg, int _index) {
	args[_index] = _arg;
    }

    public String getArgument(int _index) {
	return args[_index];
    }

    public void setQtyArguments(int _quantity) {
	args = new String[_quantity];
    }

    public String insertER() {
	/** 0º) insertar una tupla en file.addresses para relacionarla con el pedido de exploracion  */
	String ubicationInsert = "";
	if (ubication) {
	    ubicationInsert = getUbicationInsert();
	}
	/** 1º) insertar una tupla en files para relacionarla con el pedido de exploracion  */
	String fileInsert = getFileInsert();
	/** 2º) insertar una tupla en entitiesbyfile para ligarlo con el solicitante */
	String solicitorInsert = "";
	if (solicitor) {
	    solicitorInsert = getSolicitorInsert();
	}
	/** 3º) insertar una tupla en entitiesbyfile para ligarlo con el Representante Legal */
	String legalRepInsert = "";
	if (legalRep) {
	    legalRepInsert = getLegalRepInsert();
	}
	/** 4º) insertar una tupla en Terrain Data */
	String TDInsert = "";
	String TOInsert = "";
	if (terrainData) {
	    TDInsert = getterraindataInsert();
	    /** 4.1) Inserto una tupla en terrainowners */
	    TOInsert = getterrainOwners();
	}
	/** 4º) insertar una tupla la tabla polygons para relacionarla con permiso de Exploracion */
	String polyInsert = "";
	idPoly = "0";
	if (coordenates) {
	    polyInsert = getPolyInsert();
	}
	/** 5º) Insert para el Permiso de Exploración */
	String ERInsert = getERInsert();
	/** 6º Insert para la tabla FilesApplications */
	String fileapplicationsInsert = getFAInsert();
	//System.out.println("superconsulta: " + ubicationInsert + fileInsert + solicitorInsert + legalRepInsert + TDInsert + TOInsert + polyInsert + ERInsert + fileapplicationsInsert);
	String superInsert = ubicationInsert + fileInsert + solicitorInsert + legalRepInsert + TDInsert + TOInsert + polyInsert + ERInsert + fileapplicationsInsert;
	System.out.println("insert-->  " + superInsert);
	return superInsert;
    }

    public String getSolicitorStatus() {
	try {
	    if (args[1].equals("0")) {
		solicitor = true;
		return "INCOMPLETO";
	    } else {
		solicitor = true;
		return "REALIZADO";
	    }
	} catch (NullPointerException ex) {
	    solicitor = false;
	    return "NO INICIADO";
	}
    }

    public String getLegalRepStatus() {
	try {
	    if (args[2].equals("0")) {
		legalRep = true;
		return "INCOMPLETO";
	    } else {
		legalRep = true;
		return "REALIZADO";
	    }
	} catch (NullPointerException ex) {
	    legalRep = false;
	    return "NO INICIADO";
	}
    }
    //

    public String getUbicationStatus() {
	try {
	    if (args[3].equals("0")) {
		ubication = true;
		return "INCOMPLETO";
	    } else {
		ubication = true;
		return "REALIZADO";
	    }
	} catch (NullPointerException ex) {
	    //ex.printStackTrace();
	    ubication = false;
	    return "NO INICIADO";
	}
    }

    public String getCoordStatus() {
	try {
	    if (args[6].equals("0")) {
		coordenates = true;
		return "INCOMPLETO";
	    } else {
		coordenates = true;
		return "REALIZADO";
	    }
	} catch (NullPointerException ex) {
	    //ex.printStackTrace();
	    coordenates = false;
	    return "NO INICIADO";
	}
    }

    public String getTerrainDataStatus() {
	try {
	    if (completo()) {
		terrainData = true;
		return "REALIZADO";
	    } else {
		terrainData = true;
		return "INCOMPLETO";
	    }
	} catch (NullPointerException ex) {
	    terrainData = false;
	    return "NO INICIADO";
	}
    }

    private boolean completo() {
	int i = 10;
	boolean encontrado = false;
	while ((i < 18) && !encontrado) {
	    if (args[i].equals("true")) {
		encontrado = true;
	    }
	    i++;
	}
	if (encontrado) {
	    return true;
	} else {
	    if (!args[18].equals("")) {
		return true;
	    } else {
		return false;
	    }
	}
    }

    public String getProgramStatus() {
	try {
	    if (!args[24].equals("0")) {
		return "REALIZADO";
	    } else {
		return "INCOMPLETO";
	    }
	} catch (NullPointerException ex) {
	    //ex.printStackTrace();
	    return "NO INICIADO";
	}
    }

    public String getSwornStatementStatus() {
	try {
	    if (args[30].equals("true") || args[30].equals("false")) {
		return "REALIZADO";
	    } else {
		return "INCOMPLETO";
	    }
	} catch (NullPointerException ex) {
	    //ex.printStackTrace();
	    return "NO INICIADO";
	}
    }

    private String getUbicationInsert() {
	String locationInsert = "";
	if (ubication) {
	    idLocationAddress = org.digitall.lib.sql.LibSQL.getCampo("SELECT MAX(idaddress) + 1 FROM org.addresses");
	    locationInsert = " INSERT INTO org.addresses VALUES(" + idLocationAddress + "," + args[3] + " ," + args[4] + " , " + args[5] + " ,0 ,0 ,0 ,'' ,'' ,0 ,'' ,0 ,0 ,0 ,0 ,'' ,'' );";
	    //counter++;
	} else {
	    idLocationAddress = "0";
	}
	return locationInsert;
    }

    private String getFileInsert() {
	String FInsert = "";
	IdFile = org.digitall.lib.sql.LibSQL.getCampo("SELECT MAX(idfile) + 1 FROM file.files");
	FInsert = "INSERT INTO file.files VALUES(" + IdFile + ", 0" + idLocationAddress + ", 0, 0, 0 " + ", '', 1901, 'cateo', null,'', 0, false,0,'');";
	return FInsert;
	//counter++;
	//System.out.println("filesInsert: " + fileInsert);
    }

    private String getSolicitorInsert() {
	String solInsert = "INSERT INTO file.entitiesbyfile VALUES(" + IdFile + "," + args[1] + " ,true,'');";
	return solInsert;
    }

    private String getLegalRepInsert() {
	String LRInsert = "INSERT INTO file.entitiesbyfile VALUES(" + IdFile + "," + args[2] + ",false,'');";
	return LRInsert;
    }

    private String getterraindataInsert() {
	TDId = org.digitall.lib.sql.LibSQL.getCampo("SELECT MAX(idterraindata) + 1 FROM file.terraindata");
	String tdInsert = "INSERT INTO file.terraindata VALUES(" + TDId + "," + args[10] + " , " + args[11] + " ," + args[12] + " ," + args[13] + " , " + args[14] + " ," + args[15] + " , " + args[16] + "," + args[17] + " ,'" + args[18] + "','');";
	return tdInsert;
    }

    private String getterrainOwners() {
	idterrarinowners = org.digitall.lib.sql.LibSQL.getCampo("SELECT MAX(idterrainowner) + 1 FROM file.terrainowners");
	String toInsert = "INSERT INTO file.terrainowners VALUES (" + idterrarinowners + ", 0, " + TDId + ", '');";
	return toInsert;
    }

    private String getPolyInsert() {
	idPoly = org.digitall.lib.sql.LibSQL.getCampo("SELECT max(idpolygon)+1 from file.polygons");
	String coordInsert = "INSERT INTO file.polygons VALUES ( " + idPoly + ",0" + args[8] + "," + args[7] + ",'');";
	catMineral = args[9];
	return coordInsert;
    }

    private String getERInsert() {
	String erInsert = "";
	String idWP = "";
	try {
	    if (args[29].equals("false")) {
		idWP = "0";
	    } else {
		idWP = args[31];
	    }
	} catch (Exception ex) {
	    //ex.printStackTrace();
	    idWP = "0";
	}
	idExplorationRequest = org.digitall.lib.sql.LibSQL.getCampo("Select max(idexplorationrequest) + 1 From file.explorationrequests ");
	// termdays - Plazo
	// CycleType
	// permisos otorgados
	//
	//CycleNumber
	//workprogram (true or false)
	//declaracion jurada
	erInsert = " INSERT INTO file.explorationrequests VALUES( " + idExplorationRequest + " ,'" + catMineral + "', 0" + IdFile + " ,0" + idPoly + " , 0" + idWP + " ,0" + TDId + " , " + args[24] + " , " + args[25] + " , " + args[27] + " , " + args[28] + " ,null ,0 " + " , " + args[26] + " , " + args[29] + " , " + args[30] + ",'' ); ";
	return erInsert;
	//System.out.println("POLYInsert--> " + polyInsert);
	//System.out.println("idExplorationScheme: " + idExplorationScheme);
    }

    private String getFAInsert() {
	String faInsert = "INSERT INTO file.fileapplications VALUES(" + IdFile + ",1," + idExplorationRequest + ",null,null,'','');";
	return faInsert;
	//System.out.println("fileapplicationsInsert --> " + fileapplicationsInsert );
    }

    public void setNewProspection(boolean _valor) {
	newPropspection = _valor;
    }

    public boolean NewPropspection() {
	return newPropspection;
    }

    /*************************      MÉTODOS PARA LA RECUPERCIÓN DE LA INFORMACIÓN   *************************/
    public void loadProspection() {
	/** setear los id del Solicitante y de Representante Legal*/
	args[1] = org.digitall.lib.sql.LibSQL.getCampo("SELECT file.getidentity(" + IdFile + ",'true')");
	args[2] = org.digitall.lib.sql.LibSQL.getCampo("SELECT file.getidentity(" + IdFile + ",'false')");
	/** Setear los datos de la Ubicación de la Solicitud */
	String consulta1 = "SELECT idcountry, idprovince, idlocation FROM org.addresses WHERE idaddress = (SELECT idlocationaddress FROM file.files WHERE idfile = " + IdFile + ");";
	ResultSet location = org.digitall.lib.sql.LibSQL.exQuery(consulta1);
	try {
	    if (location.next()) {
		args[3] = location.getString("idcountry");
		args[4] = location.getString("idprovince");
		args[5] = location.getString("idlocation");
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	/** Recupero lod puntos del Poligono */
	torecoverPolygon();
	/** recupero los datos del Terreno */
	toRecoverTerrainData();
	ResultSet ERData = org.digitall.lib.sql.LibSQL.exQuery("SELECT * FROM file.explorationrequests WHERE idfile = " + IdFile);
	try {
	    if (ERData.next()) {
		//System.out.println("idexplorationrequest --> " + ERData.getString("idexplorationrequest"));
		//System.out.println("mineralcategory --> " + ERData.getString("mineralcategory"));
		args[9] = ERData.getString("mineralcategory");
	    } else {

	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    public void setIdFile(String _idFile) {
	IdFile = _idFile;
    }

    public String getIdFile() {
	return IdFile;
    }

    private void torecoverPolygon() {
	String poligono = "";
	String idpolygon = org.digitall.lib.sql.LibSQL.getCampo("SELECT idpolygon FROM file.explorationrequests WHERE idfile = " + IdFile);
	if (!idpolygon.equals("0")) {
	    String consulta = " SELECT NPoints(polygons.points) as cantPuntos, AsText(polygons.points) as poligono,area " + " FROM   file.polygons WHERE  idpolygon = " + idpolygon;
	    ResultSet poly = org.digitall.lib.sql.LibSQL.exQuery(consulta);
	    try {
		if (poly.next()) {
		    numPoints = poly.getInt("cantPuntos");
		    poligono = poly.getString("poligono");
		    poligono = poligono.substring(9, poligono.length() - 2);
		    //System.out.println("poligono: " + poligono);
		    xd = new double[numPoints];
		    yd = new double[numPoints];
		    String[] polig = poligono.split(",");
		    for (int j = 0; j < numPoints; j++) {
			xd[j] = Double.parseDouble(polig[j].substring(0, polig[j].indexOf(" ")).trim());
			yd[j] = Double.parseDouble(polig[j].substring(polig[j].indexOf(" "), polig[j].length()));
		    }
		    args[6] = String.valueOf(numPoints);
		    args[8] = poly.getString("area");
		}
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	} else {
	    idpolygon = "0";
	    args[8] = "0";
	}
    }

    public double getXPoint(int _index) {
	return xd[_index];
    }

    public double getYPoint(int _index) {
	return xd[_index];
    }

    public int getNumPoint() {
	return numPoints;
    }

    private void toRecoverTerrainData() {
	//System.out.println("SELECT idterraindata FROM file.explorationrequests WHERE idfile = " + IdFile);
	String tdid = org.digitall.lib.sql.LibSQL.getCampo("SELECT idterraindata FROM file.explorationrequests WHERE idfile = " + IdFile);
	String query = "SELECT * FROM file.terraindata WHERE idterraindata = " + tdid;
	//System.out.println("query: " + query);
	ResultSet terrain = org.digitall.lib.sql.LibSQL.exQuery(query);
	try {
	    if (terrain.next()) {
		args[10] = terrain.getString("cultivated");
		args[11] = terrain.getString("surrounded");
		args[12] = terrain.getString("built");
		args[13] = terrain.getString("unplowed");
		args[14] = terrain.getString("public");
		args[15] = terrain.getString("historical");
		args[16] = terrain.getString("religious");
		args[17] = terrain.getString("naturalreserve");
		args[18] = terrain.getString("other");
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    /************************************************************************************/
    public void setTitleText(String _titleText) {
	titleText = _titleText;
    }

    public String getTitleText() {
	return titleText;
    }

    public void setNewER(boolean _newER) {
	newER = _newER;
    }

    public boolean getNewER() {
	return newER;
    }

    public void setIdLegalRep(int idLegalRep) {
	this.idLegalRep = idLegalRep;
    }

    public int getIdLegalRep() {
	return idLegalRep;
    }

    public void setIdCountry(int idCountry) {
	this.idCountry = idCountry;
    }

    public int getIdCountry() {
	return idCountry;
    }

    public void setIdProvince(int idProvince) {
	this.idProvince = idProvince;
    }

    public int getIdProvince() {
	return idProvince;
    }

    public void setIdLocation(int idLocation) {
	this.idLocation = idLocation;
    }

    public int getIdLocation() {
	return idLocation;
    }

    public void setPolygonNumPoints(int polygonNumPoints) {
	this.polygonNumPoints = polygonNumPoints;
    }

    public int getPolygonNumPoints() {
	return polygonNumPoints;
    }

    public void setSqlPolygon(String sqlPolygon) {
	this.sqlPolygon = sqlPolygon;
    }

    public String getSqlPolygon() {
	return sqlPolygon;
    }

    public void setPolygonArea(double polygonArea) {
	this.polygonArea = polygonArea;
    }

    public double getPolygonArea() {
	return polygonArea;
    }

    public void setMineralCategory(int mineralCategory) {
	this.mineralCategory = mineralCategory;
    }

    public int getMineralCategory() {
	return mineralCategory;
    }

    public void loadData(String _idexplorationRequest) {
	ResultSet data = LibSQL.exFunction("getexplorationrequest", _idexplorationRequest);
	try {
	    if (data.next()) {
		idER = data.getInt("idexplorationrequest");
		catMineral = data.getString("mineralcategory");
		idpolygon = data.getInt("idpolygon");
		/*polygonNumPoints = -1;
                sqlPolygon = "";
                polygonArea = -1;
                mineralCategory = -1;*/
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

}

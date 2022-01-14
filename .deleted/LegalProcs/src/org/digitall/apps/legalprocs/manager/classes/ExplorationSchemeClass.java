package org.digitall.apps.legalprocs.manager.classes;

import java.sql.ResultSet;

import org.digitall.lib.sql.LibSQL;

public class ExplorationSchemeClass {

    private int idExplorationRequest = -1;
    //Programa de Exploración
    private int idExplorationScheme = -1;
    //Programa de Exploración
    private int realLeafNumber = -1;
    //Asiento folio real
    private String districtCode = "";
    //Departamento
    private String mineralType = "";
    //Tipo de mineral: 1ra o 2da
    private int termDays = -1;
    //DÃ­as de plazo
    private int workDays = -1;
    //DÃ­as de trabajo
    private String otherInvestigations = " ";
    //Otras investigaciones
    private boolean satellitalPicture = false;
    //Imagenes satelitales
    private boolean aerialPhoto = false;
    //fotografia aerea
    private boolean topoGraphic = false;
    //Relevamiento topográfico
    private boolean geoloGical = false;
    //Relevamiento geológico
    private boolean geoPhisic = false;
    //GeofÃ­sica
    private boolean geoChemical = false;
    //GeoquÃ­mica
    private boolean prospectionDrilling = false;
    //Sondajes
    private boolean sampling = false;
    //Calicatas y extracción de muestras
    private int prospectionDays = -1;
    //Tiempo de la prospección
    private int explorationDays = -1;
    //Tiempo de la exploración
    private boolean explorationDrilling = false;
    //Laboreos mineros
    private boolean mining = false;
    //Sondajes
    private String otherWay = " ";
    //Otros mÃ©todos
    private double compressorCapacity = -1;
    //Capacidad del compresor
    private double compressorQty = -1;
    //Cantidad de compresores
    private boolean compressorOwn = false;
    //Compresor propio
    private double drillCapacity = -1;
    //Capacidad de las Perforadoras
    private double drillQty = -1;
    //Cantidad de Perforadoras
    private boolean drillOwn = false;
    //Perforadora propia
    private String vehicleType = " ";
    //Tipo de vehÃ­culo
    private double vehicleQty = -1;
    //Cantidad de vehiculos
    private boolean vehicleOwn = false;
    //Vehiculos propios
    private String extraEquipment = " ";
    //Otros exquipos
    private boolean campFixed = false;
    //Campamentos fijos
    private boolean campMoveable = false;
    //Campamentos móviles
    private double coveredArea = -1;
    //Superficie cubierta
    private double road = -1;
    //Caminos
    private double path = -1;
    //Huellas
    private String waterProvission = " ";
    //Provision de agua
    private boolean mapTopographics = false;
    //Plano topogrÃ¡fico
    private double mapTopographicsScale = -1;
    //Escala del mapa topografico
    private boolean mapGeological = false;
    //Mapa geológico
    private double mapGeologicalScale = -1;
    //Escala del mapa geológico
    private boolean mapDrilling = false;
    //Mapa de perforaciones
    private double mapDrillingScale = -1;
    //Escala del mapa de perforaciones
    private boolean mapMining = false;
    //Mapa de laboreo
    private double mapminingscale = -1;
    //Escala del mapa de laboreo
    private boolean mapOther = false;
    //Otros mapas
    private double mapOtherScale = -1;
    //Escala de otros mapas
    private boolean chemiCanalysis = false;
    //Muestreo y anÃ¡lisis quÃ­mico
    private boolean mineralTest = false;
    //Estudios y ensayos minero-metalÃºrgicos
    private int operators = -1;
    //Cantidad de operarios
    private int technicians = -1;
    //Cantidad de TÃ©cnicos
    private int administratives = -1;
    //Cantidad de Administrativos
    private int professionals = -1;
    //Cantidad de Profesionales
    private boolean easementLand = false;
    //Ocupación del suelo
    private boolean easementWater = false;
    //Ocupación del agua
    private boolean easementRoads = false;
    //Ocupación de caminos
    private boolean easementMines = false;
    //Ocupación de minas
    private String easementOthers = " ";
    //Otras Ocupaciones
    private boolean ambientalReportProspection = false;
    //Prospección del informe de impacto ambiental
    private boolean ambientalReportExploration = false;
    //Exploración del informe de impacto ambiental
    private boolean ambientalStatementProspection = false;
    //Prospección de la declaración de impacto ambiental
    private boolean ambientalStatementExploration = false;
    //Exploración de la declaración de impacto ambiental
    private double buildingCost = -1;
    //Costo de las construcciones
    private double roadCost = -1;
    //Costos de los caminos y huellas
    private double drillingCost = -1;
    //Costo de los laboreos y perforaciones
    private double otherCost = -1;
    //Otros costos
    private double equipmentCost = -1;
    //costos de maquinarias y equipos
    private double genericCost = -1;
    //Costos generales
    private double totalCost = -1;
    //Costo total
    private int explorationSchemeStatus = 1;
    private int explorationSchemeStatusAux = 1;
    private int explorationSchemeRecord = 1;
    //CONSTANTES
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public ExplorationSchemeClass() {

    }
    /*public ExplorationSchemeClass(int _idExplorationScheme) {
	idExplorationScheme = _idExplorationScheme;
	retrieveExplorationSchemeStatus();
	//loadObject();
    }*/

    public ExplorationSchemeClass(int _idExplorationRequest) {
	idExplorationRequest = _idExplorationRequest;
	retrieveExplorationSchemeStatus();
	//loadObject();
    }

    public void setIdExplorationsCheme(int _idExplorationRequest) {
	idExplorationRequest = _idExplorationRequest;
    }

    public int getIdExplorationsCheme() {
	return idExplorationScheme;
    }

    public void setRealLeafNumber(int _realLeafNumber) {
	realLeafNumber = _realLeafNumber;
    }

    public int getRealLeafNumber() {
	return realLeafNumber;
    }

    public void setDistrictCode(String _districtCode) {
	districtCode = _districtCode;
    }

    public String getDistrictCode() {
	return districtCode;
    }

    public void setMineralType(String _mineralType) {
	mineralType = _mineralType;
    }

    public String getMineralType() {
	return mineralType;
    }

    public void setTermDays(int _termDays) {
	termDays = _termDays;
    }

    public int getTermDays() {
	return termDays;
    }

    public void setWorkDays(int _workDays) {
	workDays = _workDays;
    }

    public int getWorkDays() {
	return workDays;
    }

    public void setOtherInvestigations(String _otherInvestigations) {
	otherInvestigations = _otherInvestigations;
    }

    public String getOtherInvestigations() {
	return otherInvestigations;
    }

    public void setSatellitalPicture(boolean _satellitalPicture) {
	satellitalPicture = _satellitalPicture;
    }

    public boolean isSatellitalPicture() {
	return satellitalPicture;
    }

    public void setAerialPhoto(boolean _aerialPhoto) {
	aerialPhoto = _aerialPhoto;
    }

    public boolean isAerialPhoto() {
	return aerialPhoto;
    }

    public void setTopoGraphic(boolean _topoGraphic) {
	topoGraphic = _topoGraphic;
    }

    public boolean isTopoGraphic() {
	return topoGraphic;
    }

    public void setGeoloGical(boolean _geoloGical) {
	geoloGical = _geoloGical;
    }

    public boolean isGeoloGical() {
	return geoloGical;
    }

    public void setGeoPhisic(boolean _geoPhisic) {
	geoPhisic = _geoPhisic;
    }

    public boolean isGeoPhisic() {
	return geoPhisic;
    }

    public void setGeoChemical(boolean _geoChemical) {
	geoChemical = _geoChemical;
    }

    public boolean isGeoChemical() {
	return geoChemical;
    }

    public void setProspectionDrilling(boolean _prospectionDrilling) {
	prospectionDrilling = _prospectionDrilling;
    }

    public boolean isProspectionDrilling() {
	return prospectionDrilling;
    }

    public void setSampling(boolean _sampling) {
	sampling = _sampling;
    }

    public boolean isSampling() {
	return sampling;
    }

    public void setProspectionDays(int _prospectionDays) {
	prospectionDays = _prospectionDays;
    }

    public int getProspectionDays() {
	return prospectionDays;
    }

    public void setExplorationDays(int _explorationDays) {
	explorationDays = _explorationDays;
    }

    public int getExplorationDays() {
	return explorationDays;
    }

    public void setExplorationDrilling(boolean _explorationDrilling) {
	explorationDrilling = _explorationDrilling;
    }

    public boolean isExplorationDrilling() {
	return explorationDrilling;
    }

    public void setMining(boolean _mining) {
	mining = _mining;
    }

    public boolean isMining() {
	return mining;
    }

    public void setOtherWay(String _otherWay) {
	otherWay = _otherWay;
    }

    public String getOtherWay() {
	return otherWay;
    }

    public void setCompressorCapacity(double _compressorCapacity) {
	compressorCapacity = _compressorCapacity;
    }

    public double getCompressorCapacity() {
	return compressorCapacity;
    }

    public void setCompressorQty(double _compressorQty) {
	compressorQty = _compressorQty;
    }

    public double getCompressorQty() {
	return compressorQty;
    }

    public void setCompressorOwn(boolean _compressorOwn) {
	compressorOwn = _compressorOwn;
    }

    public boolean isCompressorOwn() {
	return compressorOwn;
    }

    public void setDrillCapacity(double _drillCapacity) {
	drillCapacity = _drillCapacity;
    }

    public double getDrillCapacity() {
	return drillCapacity;
    }

    public void setDrillQty(double _drillQty) {
	drillQty = _drillQty;
    }

    public double getDrillQty() {
	return drillQty;
    }

    public void setDrillOwn(boolean _drillOwn) {
	drillOwn = _drillOwn;
    }

    public boolean isDrillOwn() {
	return drillOwn;
    }

    public void setVehicleType(String _vehicleType) {
	vehicleType = _vehicleType;
    }

    public String getVehicleType() {
	return vehicleType;
    }

    public void setVehicleQty(double _vehicleQty) {
	vehicleQty = _vehicleQty;
    }

    public double getVehicleQty() {
	return vehicleQty;
    }

    public void setVehicleown(boolean _vehicleown) {
	vehicleOwn = _vehicleown;
    }

    public boolean isVehicleown() {
	return vehicleOwn;
    }

    public void setExtraEquipment(String _extraEquipment) {
	extraEquipment = _extraEquipment;
    }

    public String getExtraEquipment() {
	return extraEquipment;
    }

    public void setCampFixed(boolean _campFixed) {
	campFixed = _campFixed;
    }

    public boolean isCampFixed() {
	return campFixed;
    }

    public void setCampMoveable(boolean _campMoveable) {
	campMoveable = _campMoveable;
    }

    public boolean isCampMoveable() {
	return campMoveable;
    }

    public void setCoveredArea(double _coveredArea) {
	coveredArea = _coveredArea;
    }

    public double getCoveredArea() {
	return coveredArea;
    }

    public void setRoad(double _road) {
	road = _road;
    }

    public double getRoad() {
	return road;
    }

    public void setPath(double _path) {
	path = _path;
    }

    public double getPath() {
	return path;
    }

    public void setWaterProvission(String _waterProvission) {
	waterProvission = _waterProvission;
    }

    public String getWaterProvission() {
	return waterProvission;
    }

    public void setMapTopographics(boolean _mapTopographics) {
	mapTopographics = _mapTopographics;
    }

    public boolean isMapTopographics() {
	return mapTopographics;
    }

    public void setMapTopographicsScale(double _mapTopographicsScale) {
	mapTopographicsScale = _mapTopographicsScale;
    }

    public double getMapTopographicsScale() {
	return mapTopographicsScale;
    }

    public void setMapGeological(boolean _mapGeological) {
	mapGeological = _mapGeological;
    }

    public boolean isMapGeological() {
	return mapGeological;
    }

    public void setMapGeologicalScale(double _mapGeologicalScale) {
	mapGeologicalScale = _mapGeologicalScale;
    }

    public double getMapGeologicalScale() {
	return mapGeologicalScale;
    }

    public void setMapDrilling(boolean _mapDrilling) {
	mapDrilling = _mapDrilling;
    }

    public boolean isMapDrilling() {
	return mapDrilling;
    }

    public void setMapDrillingScale(double _mapDrillingScale) {
	mapDrillingScale = _mapDrillingScale;
    }

    public double getMapDrillingScale() {
	return mapDrillingScale;
    }

    public void setMapMining(boolean _mapMining) {
	mapMining = _mapMining;
    }

    public boolean isMapMining() {
	return mapMining;
    }

    public void setMapminingscale(double _mapminingscale) {
	mapminingscale = _mapminingscale;
    }

    public double getMapminingscale() {
	return mapminingscale;
    }

    public void setMapOther(boolean _mapOther) {
	mapOther = _mapOther;
    }

    public boolean isMapOther() {
	return mapOther;
    }

    public void setMapOtherScale(double _mapOtherScale) {
	mapOtherScale = _mapOtherScale;
    }

    public double getMapOtherScale() {
	return mapOtherScale;
    }

    public void setChemiCanalysis(boolean _chemiCanalysis) {
	chemiCanalysis = _chemiCanalysis;
    }

    public boolean isChemiCanalysis() {
	return chemiCanalysis;
    }

    public void setMineralTest(boolean _mineralTest) {
	mineralTest = _mineralTest;
    }

    public boolean isMineralTest() {
	return mineralTest;
    }

    public void setOperators(int _operators) {
	operators = _operators;
    }

    public int getOperators() {
	return operators;
    }

    public void setTechnicians(int _technicians) {
	technicians = _technicians;
    }

    public int getTechnicians() {
	return technicians;
    }

    public void setAdministratives(int _administratives) {
	administratives = _administratives;
    }

    public int getAdministratives() {
	return administratives;
    }

    public void setProfessionals(int _professionals) {
	professionals = _professionals;
    }

    public int getProfessionals() {
	return professionals;
    }

    public void setEasementLand(boolean _easementLand) {
	easementLand = _easementLand;
    }

    public boolean isEasementLand() {
	return easementLand;
    }

    public void setEasementWater(boolean _easementWater) {
	easementWater = _easementWater;
    }

    public boolean isEasementWater() {
	return easementWater;
    }

    public void setEasementRoads(boolean _easementRoads) {
	easementRoads = _easementRoads;
    }

    public boolean isEasementRoads() {
	return easementRoads;
    }

    public void setEasementMines(boolean _easementMines) {
	easementMines = _easementMines;
    }

    public boolean isEasementMines() {
	return easementMines;
    }

    public void setEasementOthers(String _easementOthers) {
	easementOthers = _easementOthers;
    }

    public String getEasementOthers() {
	return easementOthers;
    }

    public void setAmbientalReportProspection(boolean _ambientalReportProspection) {
	ambientalReportProspection = _ambientalReportProspection;
    }

    public boolean isAmbientalReportProspection() {
	return ambientalReportProspection;
    }

    public void setAmbientalReportExploration(boolean _ambientalReportExploration) {
	ambientalReportExploration = _ambientalReportExploration;
    }

    public boolean isAmbientalReportExploration() {
	return ambientalReportExploration;
    }

    public void setAmbientalStatementProspection(boolean _ambientalStatementProspection) {
	ambientalStatementProspection = _ambientalStatementProspection;
    }

    public boolean isAmbientalStatementProspection() {
	return ambientalStatementProspection;
    }

    public void setAmbientalStatementExploration(boolean _ambientalStatementExploration) {
	ambientalStatementExploration = _ambientalStatementExploration;
    }

    public boolean isAmbientalStatementExploration() {
	return ambientalStatementExploration;
    }

    public void setBuildingCost(double _buildingCost) {
	buildingCost = _buildingCost;
    }

    public double getBuildingCost() {
	return buildingCost;
    }

    public void setRoadCost(double _roadCost) {
	roadCost = _roadCost;
    }

    public double getRoadCost() {
	return roadCost;
    }

    public void setDrillingCost(double _drillingCost) {
	drillingCost = _drillingCost;
    }

    public double getDrillingCost() {
	return drillingCost;
    }

    public void setOtherCost(double _otherCost) {
	otherCost = _otherCost;
    }

    public double getOtherCost() {
	return otherCost;
    }

    public void setEquipmentCost(double _equipmentCost) {
	equipmentCost = _equipmentCost;
    }

    public double getEquipmentCost() {
	return equipmentCost;
    }

    public void setGenericCost(double _genericCost) {
	genericCost = _genericCost;
    }

    public double getGenericCost() {
	return genericCost;
    }

    public void setTotalCost(double _totalCost) {
	totalCost = _totalCost;
    }

    public double getTotalCost() {
	return totalCost;
    }

    public void setExplorationSchemeStatus(int _explorationSchemeStatus) {
	explorationSchemeStatus = _explorationSchemeStatus;
    }

    public int getExplorationSchemeStatus() {
	return explorationSchemeStatus;
    }

    public void setIdExplorationRequest(int _idExplorationRequest) {
	idExplorationRequest = _idExplorationRequest;
    }

    public int getIdExplorationRequest() {
	return idExplorationRequest;
    }

    public void setExplorationSchemeRecord(int _explorationSchemeRecord) {
	explorationSchemeRecord = _explorationSchemeRecord;
    }

    public int getExplorationSchemeRecord() {
	return explorationSchemeRecord;
    }

    public void retrieveExplorationSchemeData() {
	StringBuffer params = new StringBuffer();
	params.append(idExplorationRequest);
	ResultSet ESData = LibSQL.exFunction("file.getexplorationschemedata", params.toString());
	try {
	    if (ESData.next()) {
		idExplorationScheme = ESData.getInt("idexplorationscheme");
		realLeafNumber = ESData.getInt("realleafnumber");
		districtCode = ESData.getString("districtcode");
		mineralType = ESData.getString("mineraltype");
		termDays = ESData.getInt("termdays");
		workDays = ESData.getInt("workdays");
		otherInvestigations = ESData.getString("otherinvestigations");
		satellitalPicture = ESData.getBoolean("satellitalpicture");
		aerialPhoto = ESData.getBoolean("aerialphoto");
		topoGraphic = ESData.getBoolean("topographic");
		geoloGical = ESData.getBoolean("geological");
		geoPhisic = ESData.getBoolean("geophisic");
		geoChemical = ESData.getBoolean("geochemical");
		prospectionDrilling = ESData.getBoolean("prospectiondrilling");
		sampling = ESData.getBoolean("sampling");
		prospectionDays = ESData.getInt("prospectiondays");
		explorationDays = ESData.getInt("explorationdays");
		explorationDrilling = ESData.getBoolean("explorationdrilling");
		mining = ESData.getBoolean("mining");
		otherWay = ESData.getString("otherway");
		compressorCapacity = ESData.getDouble("compressorcapacity");
		compressorQty = ESData.getDouble("compressorqty");
		compressorOwn = ESData.getBoolean("compressorown");
		drillCapacity = ESData.getDouble("drillcapacity");
		drillQty = ESData.getDouble("drillqty");
		drillOwn = ESData.getBoolean("drillown");
		vehicleType = ESData.getString("vehicletype");
		vehicleQty = ESData.getDouble("vehicleqty");
		vehicleOwn = ESData.getBoolean("vehicleown");
		extraEquipment = ESData.getString("extraequipment");
		campFixed = ESData.getBoolean("campfixed");
		campMoveable = ESData.getBoolean("campmoveable");
		coveredArea = ESData.getDouble("coveredarea");
		road = ESData.getDouble("road");
		path = ESData.getDouble("path");
		waterProvission = ESData.getString("waterprovission");
		mapTopographics = ESData.getBoolean("maptopographics");
		mapTopographicsScale = ESData.getDouble("maptopographicsscale");
		mapGeological = ESData.getBoolean("mapgeological");
		mapGeologicalScale = ESData.getDouble("mapgeologicalscale");
		mapDrilling = ESData.getBoolean("mapdrilling");
		mapDrillingScale = ESData.getDouble("mapdrillingscale");
		mapMining = ESData.getBoolean("mapmining");
		mapminingscale = ESData.getDouble("mapminingscale");
		mapOther = ESData.getBoolean("mapother");
		mapOtherScale = ESData.getDouble("mapotherscale");
		chemiCanalysis = ESData.getBoolean("chemicanalysis");
		mineralTest = ESData.getBoolean("mineraltest");
		operators = ESData.getInt("operators");
		technicians = ESData.getInt("technicians");
		administratives = ESData.getInt("administratives");
		professionals = ESData.getInt("professionals");
		easementLand = ESData.getBoolean("easementland");
		easementWater = ESData.getBoolean("easementwater");
		easementRoads = ESData.getBoolean("easementroads");
		easementMines = ESData.getBoolean("easementmines");
		easementOthers = ESData.getString("easementothers");
		ambientalReportProspection = ESData.getBoolean("ambientalreportprospection");
		ambientalReportExploration = ESData.getBoolean("ambientalreportexploration");
		ambientalStatementProspection = ESData.getBoolean("ambientalstatementprospection");
		ambientalStatementExploration = ESData.getBoolean("ambientalstatementexploration");
		buildingCost = ESData.getDouble("buildingcost");
		roadCost = ESData.getDouble("roadcost");
		drillingCost = ESData.getDouble("drillingcost");
		otherCost = ESData.getDouble("othercost");
		equipmentCost = ESData.getDouble("equipmentcost");
		genericCost = ESData.getDouble("genericcost");
		totalCost = ESData.getDouble("totalcost");
		explorationSchemeStatus = ESData.getInt("explorationschemestatus");
	    } else {
		//System.out.println("no encontro nada");
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

    private void retrieveExplorationSchemeStatus() {
	StringBuffer params = new StringBuffer();
	params.append(String.valueOf(idExplorationRequest));
	explorationSchemeStatus = LibSQL.getInt("file.getExplorationSchemeStatus", params.toString());
    }

    public boolean recordExplorationSchemeData() {
	if (explorationSchemeRecord == YES) {
	    if (explorationSchemeStatus == NO_MADE) {
		return executeInsert();
	    } else {
		return executeUpdate();
	    }
	} else {
	    return true;
	}
    }

    private boolean executeInsert() {
	return true;
    }

    private boolean executeUpdate() {
	StringBuffer params = new StringBuffer();
	params.append(getWorkParams() + "," + getEquipmentParams() + "," + getInformationParams() + "," + getAmountParams());
	if (LibSQL.getBoolean("file.updateExplorationScheme", params.toString())) {
	    explorationSchemeStatus = COMPLETE;
	    return true;
	} else {
	    explorationSchemeStatus = INCOMPLETE;
	    return false;
	}
    }

    private String getWorkParams() {
	StringBuffer workParams = new StringBuffer();
	workParams.append(idExplorationScheme);
	workParams.append(",'" + mineralType + "'");
	workParams.append("," + termDays);
	workParams.append("," + workDays);
	workParams.append(",'" + otherInvestigations + "'");
	workParams.append("," + satellitalPicture);
	workParams.append("," + aerialPhoto);
	workParams.append("," + topoGraphic);
	workParams.append("," + geoloGical);
	workParams.append("," + geoPhisic);
	workParams.append("," + geoChemical);
	workParams.append("," + prospectionDrilling);
	workParams.append("," + sampling);
	workParams.append("," + prospectionDays);
	workParams.append("," + explorationDays);
	workParams.append("," + explorationDrilling);
	workParams.append("," + mining);
	workParams.append(",'" + otherWay + "'");
	return workParams.toString();
    }

    private String getEquipmentParams() {
	StringBuffer equipmentParams = new StringBuffer();
	equipmentParams.append(compressorCapacity);
	equipmentParams.append("," + compressorQty);
	equipmentParams.append("," + compressorOwn);
	equipmentParams.append("," + drillCapacity);
	equipmentParams.append("," + drillQty);
	equipmentParams.append("," + drillOwn);
	equipmentParams.append(",'" + vehicleType + "'");
	equipmentParams.append("," + vehicleQty);
	equipmentParams.append("," + vehicleOwn);
	equipmentParams.append(",'" + extraEquipment + "'");
	equipmentParams.append("," + campFixed);
	equipmentParams.append("," + campMoveable);
	equipmentParams.append("," + coveredArea);
	equipmentParams.append("," + road);
	equipmentParams.append("," + path);
	equipmentParams.append(",'" + waterProvission + "'");
	return equipmentParams.toString();
    }

    private String getInformationParams() {
	StringBuffer informationParams = new StringBuffer();
	informationParams.append(mapTopographics);
	informationParams.append("," + mapTopographicsScale);
	informationParams.append("," + mapGeological);
	informationParams.append("," + mapGeologicalScale);
	informationParams.append("," + mapDrilling);
	informationParams.append("," + mapDrillingScale);
	informationParams.append("," + mapMining);
	informationParams.append("," + mapminingscale);
	informationParams.append("," + mapOther);
	informationParams.append("," + mapOtherScale);
	informationParams.append("," + chemiCanalysis);
	informationParams.append("," + mineralTest);
	informationParams.append("," + operators);
	informationParams.append("," + technicians);
	informationParams.append("," + administratives);
	informationParams.append("," + professionals);
	informationParams.append("," + easementLand);
	informationParams.append("," + easementWater);
	informationParams.append("," + easementRoads);
	informationParams.append("," + easementMines);
	informationParams.append(",'" + easementOthers + "'");
	return informationParams.toString();
    }

    private String getAmountParams() {
	StringBuffer amountParams = new StringBuffer();
	amountParams.append(ambientalReportProspection);
	amountParams.append("," + ambientalReportExploration);
	amountParams.append("," + ambientalStatementProspection);
	amountParams.append("," + ambientalStatementExploration);
	amountParams.append("," + buildingCost);
	amountParams.append("," + roadCost);
	amountParams.append("," + drillingCost);
	amountParams.append("," + otherCost);
	amountParams.append("," + equipmentCost);
	amountParams.append("," + genericCost);
	amountParams.append("," + totalCost);
	return amountParams.toString();
    }

    public void setExplorationSchemeStatusAux(int explorationSchemeStatusAux) {
	this.explorationSchemeStatusAux = explorationSchemeStatusAux;
    }

    public int getExplorationSchemeStatusAux() {
	return explorationSchemeStatusAux;
    }

}

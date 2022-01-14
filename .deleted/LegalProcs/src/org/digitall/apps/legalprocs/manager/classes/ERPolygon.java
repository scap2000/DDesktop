package org.digitall.apps.legalprocs.manager.classes;

import java.awt.geom.Point2D;

import java.sql.ResultSet;

import org.digitall.lib.geo.esri.ESRIPolygon;
import org.digitall.lib.sql.LibSQL;

public class ERPolygon {

    private int idexplorationrequest = -1;
    private int idReference = -1;
    private int idReferenceAux = -1;
    private int idPolygon = -1;
    private int idPolygonAux = -1;
    private double area = -1;
    private double areaAux = -1;
    private String mineralcategory = "";
    private String mineralcategoryAux = "";
    private int quantityPoints = -1;
    private int quantityPointsAux = -1;
    private Point2D[] points;
    // = new Point2D[numPoints];
    private Point2D[] pointsAux;
    private double[] xd;
    private double[] yd;
    private double[] xdAux;
    private double[] ydAux;
    private int erPolygonStatus = 1;
    private int erPolygonStatusAux = 1;
    private int record = 2;
    //OBJETOS
    private ESRIPolygon polygon;
    //CONSTANTES
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;
    private static final int UPDATE = 3;
    private boolean isMine = false;

    public ERPolygon() {
    }

    public ERPolygon(int _idReference, boolean _isMine) {
	idReference = _idReference;
	idReferenceAux = idReference;
	isMine = _isMine;
	retrieveStatus();
    }

    public void setIdPolygon(int _idPolygon) {
	idPolygon = _idPolygon;
    }

    public int getIdPolygon() {
	return idPolygon;
    }

    public void setArea(double _area) {
	area = _area;
    }

    public double getArea() {
	return area;
    }

    public void setIdPolygonAux(int _idPolygonAux) {
	idPolygonAux = _idPolygonAux;
    }

    public int getIdPolygonAux() {
	return idPolygonAux;
    }

    public void setAreaAux(double _areaAux) {
	areaAux = _areaAux;
    }

    public double getAreaAux() {
	return areaAux;
    }

    public void setPolygon(ESRIPolygon _polygon) {
	polygon = _polygon;
    }

    public ESRIPolygon getPolygon() {
	return polygon;
    }

    public void setErPolygonStatus(int _erPolygonStatus) {
	erPolygonStatus = _erPolygonStatus;
    }

    public int getErPolygonStatus() {
	return erPolygonStatus;
    }

    public void setIdexplorationrequest(int _idexplorationrequest) {
	idexplorationrequest = _idexplorationrequest;
    }

    public int getIdexplorationrequest() {
	return idexplorationrequest;
    }

    public void setErPolygonStatusAux(int _erPolygonStatusAux) {
	erPolygonStatusAux = _erPolygonStatusAux;
    }

    public int getErPolygonStatusAux() {
	return erPolygonStatusAux;
    }

    public void setQuantityPoints(int _quantityPoints) {
	quantityPoints = _quantityPoints;
    }

    public int getQuantityPoints() {
	return quantityPoints;
    }

    public void setQuantityPointsAux(int _quantityPointsAux) {
	quantityPointsAux = _quantityPointsAux;
    }

    public int getQuantityPointsAux() {
	return quantityPointsAux;
    }

    public void setXd(double[] _xd) {
	xd = _xd;
    }

    public double getXd(int _index) {
	return xd[_index];
    }

    public void setYd(double[] _yd) {
	yd = _yd;
    }

    public double getYd(int _index) {
	return yd[_index];
    }

    public void setPoints(Point2D[] _points) {
	points = _points;
    }

    public Point2D[] getPoints() {
	return points;
    }

    public void setPointsAux(Point2D[] pointsAux) {
	this.pointsAux = pointsAux;
    }

    public Point2D[] getPointsAux() {
	return pointsAux;
    }

    public void setRecord(int _record) {
	record = _record;
    }

    public int getRecord() {
	return record;
    }

    public void setMineralcategory(String _mineralcategory) {
	mineralcategory = _mineralcategory;
    }

    public String getMineralcategory() {
	return mineralcategory;
    }

    public void setIdReference(int _idReference) {
	idReference = _idReference;
    }

    public int getIdReference() {
	return idReference;
    }

    public void setIdReferenceAux(int _idReferenceAux) {
	idReferenceAux = _idReferenceAux;
    }

    public int getIdReferenceAux() {
	return idReferenceAux;
    }

    public void setMineralcategoryAux(String _mineralcategoryAux) {
	mineralcategoryAux = _mineralcategoryAux;
    }

    public String getMineralcategoryAux() {
	return mineralcategoryAux;
    }

    public void setXdAux(double[] _xdAux) {
	xdAux = _xdAux;
    }

    public double getXdAux(int _index) {
	return xdAux[_index];
    }

    public void setYdAux(double[] _ydAux) {
	ydAux = _ydAux;
    }

    public double getYdAux(int _index) {
	return ydAux[_index];
    }
    //	METODOS

    private void retrieveStatus() {
	if (idReference != -1) {
	    String params = String.valueOf(idReference) + ",'" + String.valueOf(isMine) + "'";
	    erPolygonStatus = LibSQL.getInt("file.getPolygonStatus", params);
	    erPolygonStatusAux = erPolygonStatus;
	} else {
	    erPolygonStatus = NO_MADE;
	    erPolygonStatusAux = erPolygonStatus;
	}
    }

    public void retrieveData() {
	if (idReference != -1) {
	    String param = String.valueOf(idReference) + "," + String.valueOf("'" + isMine + "'");
	    idPolygon = LibSQL.getInt("file.getIdPolygon", param);
	    if (idPolygon != 0) {
		//recuperar los puntos del poligono
		param = String.valueOf(idPolygon) + ",'" + isMine + "'";
		//ResultSet polygonData = LibSQL.exFunction("file.getPolygonData",String.valueOf(idPolygon));
		ResultSet polygonData = LibSQL.exFunction("file.getPolygonData", param);
		try {
		    if (polygonData.next()) {
			String poligono = "";
			quantityPoints = polygonData.getInt("cantPuntos");
			poligono = polygonData.getString("poligono");
			poligono = poligono.substring(9, poligono.length() - 2);
			xd = new double[quantityPoints];
			yd = new double[quantityPoints];
			String[] polig = poligono.split(",");
			for (int j = 0; j < quantityPoints; j++) {
			    xd[j] = Double.parseDouble(polig[j].substring(0, polig[j].indexOf(" ")).trim());
			    yd[j] = Double.parseDouble(polig[j].substring(polig[j].indexOf(" "), polig[j].length()));
			}
			area = polygonData.getDouble("area");
			mineralcategory = polygonData.getString("mineralcategory");
			copyData();
		    } else {

		    }
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    } else {
		//setear los campos de los puntos del poligono en vacio
	    }
	} else {
	    idPolygon = -1;
	    area = -1;
	}
    }

    public boolean recordPolygon() {
	if (record == YES) {
	    if (idPolygon == 0) {
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
	params.append(String.valueOf(idReference) + "," + areaAux + "," + getSQLInsertPolygonString() + ",'" + mineralcategoryAux + "','" + isMine + "'");
	if (LibSQL.getBoolean("file.addNewPolygonOld", params.toString())) {
	    //if (LibSQL.getBoolean("file.addNewPolygon",params.toString()))  {
	    setData();
	    return true;
	} else {
	    erPolygonStatus = NO_MADE;
	    erPolygonStatusAux = erPolygonStatus;
	    return false;
	}
    }

    private boolean executeUpdate() {
	StringBuffer params = new StringBuffer();
	params.append(String.valueOf(idReference) + "," + String.valueOf(idPolygon) + "," + areaAux + "," + getSQLPolygonString() + ",'" + mineralcategoryAux + "','" + isMine + "'");
	System.out.println("params: " + params.toString());
	if (LibSQL.getBoolean("file.updatePolygonData", params.toString())) {
	    setData();
	    return true;
	} else {
	    return false;
	}
    }

    private void setData() {
	area = areaAux;
	mineralcategory = mineralcategoryAux;
	erPolygonStatus = COMPLETE;
	erPolygonStatusAux = erPolygonStatus;
	areaAux = -1;
    }

    public String getSQLInsertPolygonString() {
	try {
	    String sql = "'public.GeometryFromText(''POLYGON((";
	    for (int j = 0; j < points.length; j++) {
		sql += points[j].getX() + " " + points[j].getY() + ",";
	    }
	    sql += points[0].getX() + " " + points[0].getY();
	    sql += "))'',-1)'";
	    return sql;
	} catch (Exception e) {
	    return "null";
	}
    }

    public String getSQLPolygonString() {
	try {
	    String sql = "public.GeometryFromText('POLYGON((";
	    for (int j = 0; j < points.length; j++) {
		sql += points[j].getX() + " " + points[j].getY() + ",";
	    }
	    sql += points[0].getX() + " " + points[0].getY();
	    sql += "))',-1)";
	    return sql;
	} catch (Exception e) {
	    return "null";
	}
    }

    private void copyData() {
	areaAux = area;
	mineralcategoryAux = mineralcategory;
    }

}

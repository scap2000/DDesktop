package org.digitall.apps.legalprocs.manager.classes;

public class CadastralRegister {

    private int idCadastralRegister = -1;
    //Registro Catastral
    private int idLocation = -1;
    //Localidad
    private int idProvince = -1;
    //Provincia
    private String point = " ";
    //Punto de Coordenada
    private String rightClass = " ";
    //Clase de Derecho
    private boolean definitive = false;
    //True: Definitiva, False: Provisoria

    public CadastralRegister() {

    }

    public CadastralRegister(int _idCadastralRegister) {
	loadObject();
    }

    public void setIdCadastralRegister(int _idCadastralRegister) {
	idCadastralRegister = _idCadastralRegister;
    }

    public int getIdCadastralRegister() {
	return idCadastralRegister;
    }

    public void setIdLocation(int _idLocation) {
	idLocation = _idLocation;
    }

    public int getIdLocation() {
	return idLocation;
    }

    public void setIdProvince(int _idProvince) {
	idProvince = _idProvince;
    }

    public int getIdProvince() {
	return idProvince;
    }

    public void setPoint(String _point) {
	point = _point;
    }

    public String getPoint() {
	return point;
    }

    public void setRightClass(String _rightClass) {
	rightClass = _rightClass;
    }

    public String getRightClass() {
	return rightClass;
    }

    public void setDefinitive(boolean _definitive) {
	definitive = _definitive;
    }

    public boolean isDefinitive() {
	return definitive;
    }

    private void loadObject() {
	/*
        idCadastralRegister = ;
        idLocation = ;
        idProvince = ;
        point = ;
        rightClass = ;
        definitive = ;
        */
    }

}

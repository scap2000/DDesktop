package org.digitall.apps.legalprocs.manager.classes;

import java.util.Date;

public class Companies {

    private int idCompany = -1;
    private int idIdentificationType = -1;
    private long idEntificationNumber = -1;
    private int idTributaryCondition = -1;
    private String name = " ";
    private int idPersonCharge = -1;
    private int idPrefix = -1;
    private int idCompanyType = -1;
    private int idParent = -1;
    private Date startDate = null;
    private String photo = " ";
    //Verificar tipo de dato
    private String logo = " ";
    //Verificar tipo de dato
    private int idCommunicationType = -1;
    private String description = " ";

    public Companies() {

    }

    public Companies(int _idCompanies) {
	loadObject();
    }

    public void setIdCompany(int _idCompany) {
	idCompany = _idCompany;
    }

    public int getIdCompany() {
	return idCompany;
    }

    public void setIdIdentificationType(int _idIdentificationType) {
	idIdentificationType = _idIdentificationType;
    }

    public int getIdIdentificationType() {
	return idIdentificationType;
    }

    public void setIdEntificationNumber(long _idEntificationNumber) {
	idEntificationNumber = _idEntificationNumber;
    }

    public long getIdEntificationNumber() {
	return idEntificationNumber;
    }

    public void setIdTributaryCondition(int _idTributaryCondition) {
	idTributaryCondition = _idTributaryCondition;
    }

    public int getIdTributaryCondition() {
	return idTributaryCondition;
    }

    public void setName(String _name) {
	name = _name;
    }

    public String getName() {
	return name;
    }

    public void setIdPersonCharge(int _idPersonCharge) {
	idPersonCharge = _idPersonCharge;
    }

    public int getIdPersonCharge() {
	return idPersonCharge;
    }

    public void setIdPrefix(int _idPrefix) {
	idPrefix = _idPrefix;
    }

    public int getIdPrefix() {
	return idPrefix;
    }

    public void setIdCompanyType(int _idCompanyType) {
	idCompanyType = _idCompanyType;
    }

    public int getIdCompanyType() {
	return idCompanyType;
    }

    public void setIdParent(int _idParent) {
	idParent = _idParent;
    }

    public int getIdParent() {
	return idParent;
    }

    public void setStartdate(Date _startdate) {
	startDate = _startdate;
    }

    public Date getStartdate() {
	return startDate;
    }

    public void setPhoto(String _photo) {
	photo = _photo;
    }

    public String getPhoto() {
	return photo;
    }

    public void setLogo(String _logo) {
	logo = _logo;
    }

    public String getLogo() {
	return logo;
    }

    public void setIdCommunicationType(int _idCommunicationType) {
	idCommunicationType = _idCommunicationType;
    }

    public int getIdCommunicationType() {
	return idCommunicationType;
    }

    public void setDescription(String _description) {
	description = _description;
    }

    public String getDescription() {
	return description;
    }

    private void loadObject() {
	/*
        idCompany = ;
        idIdentificationType = ;
        idEntificationNumber = ;
        idTributaryCondition = ;
        name = ;
        idPersonCharge = ;
        idPrefix = ;
        idCompanyType = ;
        idParent = ;
        startDate = ;
        photo = ;
        logo = ;
        idCommunicationType = ;
        description = ;
        */
    }

}

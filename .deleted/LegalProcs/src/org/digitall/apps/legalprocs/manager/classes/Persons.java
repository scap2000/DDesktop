package org.digitall.apps.legalprocs.manager.classes;

import java.util.Date;

public class Persons {

    private int idPerson = -1;
    private int idIdentificationType = -1;
    private long idEntificationNumber = -1;
    private String name = " ";
    private String lastName = " ";
    private int idPrefix = -1;
    private int idSuffix = -1;
    private int idFormatView = -1;
    private int idContacTtype = -1;
    private int idProfession = -1;
    private String title = "";
    private Date birthDate = null;
    private String digitalSign = "";
    private String photo = " ";
    //Averiguar tipo dato   bytea
    private String logo = " ";
    //Averiguar tipo dato   bytea                       Modificar       Eliminar
    private int idCivilState = -1;
    private int idCommunicationType = -1;
    private String description = " ";

    public Persons() {

    }

    public Persons(int _idPerson) {
	loadObject();
    }

    public void setIdPerson(int _idPerson) {
	idPerson = _idPerson;
    }

    public int getIdPerson() {
	return idPerson;
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

    public void setName(String _name) {
	name = _name;
    }

    public String getName() {
	return name;
    }

    public void setLastName(String _lastName) {
	lastName = _lastName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setIdPrefix(int _idPrefix) {
	idPrefix = _idPrefix;
    }

    public int getIdPrefix() {
	return idPrefix;
    }

    public void setIdSuffix(int _idSuffix) {
	idSuffix = _idSuffix;
    }

    public int getIdSuffix() {
	return idSuffix;
    }

    public void setIdFormatView(int _idFormatView) {
	idFormatView = _idFormatView;
    }

    public int getIdFormatView() {
	return idFormatView;
    }

    public void setIdContacTtype(int _idContacTtype) {
	idContacTtype = _idContacTtype;
    }

    public int getIdContacTtype() {
	return idContacTtype;
    }

    public void setIdProfession(int _idProfession) {
	idProfession = _idProfession;
    }

    public int getIdProfession() {
	return idProfession;
    }

    public void setTitle(String _title) {
	title = _title;
    }

    public String getTitle() {
	return title;
    }

    public void setBirthDate(Date _birthDate) {
	birthDate = _birthDate;
    }

    public Date getBirthDate() {
	return birthDate;
    }

    public void setDigitalSign(String _digitalSign) {
	digitalSign = _digitalSign;
    }

    public String getDigitalSign() {
	return digitalSign;
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

    public void setIdCivilState(int _idCivilState) {
	idCivilState = _idCivilState;
    }

    public int getIdCivilState() {
	return idCivilState;
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
         idPerson = ;
         idIdentificationType = ;
         idEntificationNumber = ;
         name = ;
         lastName = ;
         idPrefix = ;
         idSuffix = ;
         idFormatView = ;
         idContacTtype = ;
         idProfession = ;
         title  = ;
         birthDate = ;
         digitalSign = ;
         photo = ;
         logo =  ;
         idCivilState = ;
         idCommunicationType = ;
         description = ;
         */
    }

}

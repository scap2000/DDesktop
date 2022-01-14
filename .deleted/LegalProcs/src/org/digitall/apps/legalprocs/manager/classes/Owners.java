package org.digitall.apps.legalprocs.manager.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;

import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.sql.LibSQL;

//

public class Owners {

    private int idOwner = -1;
    //Propietario
    private int idPostAddress = -1;
    //Direcciones, direccion postal del propietario
    private int idLegalAddress = -1;
    //Direcciones, direccion legal del propietario
    private String lastName = "";
    //Apellido del propietario
    private String firstName = "";
    //Nombre del propietario
    private int identificationNumber = -1;
    //NÃºmero de identificación
    private Date birthDate = null;
    //Fecha de nacimiento

    public Owners() {

    }

    public Owners(int _idFile) {
	loadObject();
    }

    public void setIdOwner(int _idOwner) {
	idOwner = _idOwner;
    }

    public int getIdOwner() {
	return idOwner;
    }

    public void setIdPostAddress(int _idPostAddress) {
	idPostAddress = _idPostAddress;
    }

    public int getIdPostAddress() {
	return idPostAddress;
    }

    public void setIdLegalAddress(int _idLegalAddress) {
	idLegalAddress = _idLegalAddress;
    }

    public int getIdLegalAddress() {
	return idLegalAddress;
    }

    public void setLastname(String _lastName) {
	lastName = _lastName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setFirstName(String _firstName) {
	firstName = _firstName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setIdentificationNumber(int _identificationNumber) {
	identificationNumber = _identificationNumber;
	//ResultSet reOwner = (ResultSet) LibSQL.exFunction("setDataOwner",identificationNumber);
    }

    public int getIdentificationNumber() {
	return identificationNumber;
    }

    public void setBirthDate(Date _birthDate) {
	String date;
	birthDate = _birthDate;
	date = Proced.setFormatDate(birthDate.toString(), false);
	/* if (LibSQL.exFunction("setDataOwner",date)){
        }
        else
         */
    }

    public Date getBirthDate() {
	return birthDate;
    }
    //devuelve false si no se pudo cargar
    //         true si cargo con exito

    public boolean loadObject() {
	boolean load = false;
	ResultSet reOwner = LibSQL.exFunction("getOwner", "0");
	try {
	    if (reOwner.next()) {
		idOwner = reOwner.getInt("idowner");
		idPostAddress = reOwner.getInt("idpostaddress");
		idLegalAddress = reOwner.getInt("idlegaladdress");
		lastName = reOwner.getString("lastname");
		firstName = reOwner.getString("firstname");
		identificationNumber = reOwner.getInt("identificationnumber");
		birthDate = reOwner.getDate("birthdate");
		load = true;
	    }
	} catch (SQLException e) {
	    // TODO
	}
	return load;
    }

    public boolean updateObjet() {
	boolean update = false;
	return update;
    }

}

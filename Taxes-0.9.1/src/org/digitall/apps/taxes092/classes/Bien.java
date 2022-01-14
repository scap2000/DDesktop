/**
 LIMITACIÓN DE RESPONSABILIDAD - COPYRIGHT - [Español]
 ================================================================================
 El Suri - Entorno JAVA de Trabajo y Desarrollo para Gobierno Electrónico
 ================================================================================

 Información del Proyecto:  http://elsuri.sourceforge.net

 Copyright (C) 2007-2010 por D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO.
 D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO es propiedad de
 Lic. Santiago Cassina (santiago@digitallsh.com.ar - scap2000@yahoo.com) y
 Marcelo D'Ambrosio (marcelo@digitallsh.com.ar - marcelodambrosio@gmail.com)
 http://www.digitallsh.com.ar

 Este programa es software libre: usted puede redistribuirlo y/o modificarlo
 bajo los términos de la Licencia Pública General GNU publicada
 por la Fundación para el Software Libre, ya sea la versión 3
 de la Licencia, o (a su elección) cualquier versión posterior.

 Este programa se distribuye con la esperanza de que sea útil, pero
 SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita
 MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO.
 Consulte los detalles de la Licencia Pública General GNU para obtener
 una información más detallada.

 Debería haber recibido una copia de la Licencia Pública General GNU
 junto a este programa.
 En caso contrario, consulte <http://www.gnu.org/licenses/>.

 DISCLAIMER - COPYRIGHT - [English]
 =====================================================================================
 El Suri - JAVA Management & Development Environment for Electronic Government
 =====================================================================================

 Project Info:  http://elsuri.sourceforge.net

 Copyright (C) 2007-2010 by D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO.
 D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO is owned by
 Lic. Santiago Cassina (santiago@digitallsh.com.ar - scap2000@yahoo.com) and
 Marcelo D'Ambrosio (marcelo@digitallsh.com.ar - marcelodambrosio@gmail.com)
 http://www.digitallsh.com.ar

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
/**
 * Bien.java
 *
 * */
package org.digitall.apps.taxes092.classes;

import org.digitall.apps.taxes.classes.Cadastral;
import org.digitall.apps.taxes.classes.Car;
import org.digitall.apps.taxes.classes.Commerce;
import org.digitall.apps.taxes.classes.PlanDePago;
import org.digitall.lib.sql.LibSQL;

public class Bien {

    public static final int TIPO_CATASTRO = 1; 
    public static final int TIPO_AUTOMOTOR = 3;
    public static final int TIPO_COMERCIO = 4;
    
    private Cadastral catastro = null;
    private Car automotor = null;
    private Commerce commerce = null;
    private PlanesPago planesPago = new PlanesPago();
    private int tipoBien = -1;
    private int idBien = -1;

    public Bien() {
	
    }
    
    public void cargarse(int _idBien, int _tipoBien, boolean _revisarEstado){
	tipoBien = _tipoBien;
	idBien = _idBien;
        switch (_tipoBien) {
            case TIPO_CATASTRO:
                catastro = new Cadastral();
                catastro.setIdCatastro(_idBien);
                catastro.retrieveData();
                catastro.retrieveApoderadoData();
                break;
            case TIPO_AUTOMOTOR:
                automotor = new Car();
                automotor.setIdAutomotor(_idBien);
                automotor.retrieveIdDominio();
                automotor.retrieveData();
                break;
            case TIPO_COMERCIO:
                commerce = new Commerce();
                commerce.setIdpadron(_idBien);
                commerce.retrieveData();
            break;
	}
	planesPago.cargarPlanesDePago(this,_revisarEstado);
    }
    
    public void recargarse(){
	cargarse(idBien,tipoBien,false);
    }
    
    public boolean esCatastro() {
	return tipoBien == TIPO_CATASTRO;
    }
    
    public boolean esAutomotor() {
        return tipoBien == TIPO_AUTOMOTOR;
    }
    
    public boolean esComercio() {
        return tipoBien == TIPO_COMERCIO;
    }
    
    public int getIdBien (){
	int resultado = -1;
        switch (tipoBien) {
            case TIPO_CATASTRO:
                resultado = catastro.getIdCatastro();
                break;
            case TIPO_AUTOMOTOR:
                resultado = automotor.getIdAutomotor();
                break;
            case TIPO_COMERCIO:
                resultado = commerce.getIdComercio();
                break;
	}
	return resultado;
    }
    
    public boolean adeudaAnticipos(int _tipoDeImpuesto){
	return LibSQL.getBoolean("taxes.adeudaAnticipos","" + getIdBien()+"," + _tipoDeImpuesto);
    }
    
    public boolean tienePlanDePagoActivo(int _tipoDeImpuesto){
	boolean resultado = false;
        switch (_tipoDeImpuesto) {
            case Impuesto.TIPO_IMPUESTO_TGS:
                resultado = tienePlanDePagoTgs();
                break;
            case Impuesto.TIPO_IMPUESTO_INMOB:
                resultado = tienePlanDePagoInmob();
                break;
            case Impuesto.TIPO_IMPUESTO_AUTOMOTOR:
                resultado = tienePlanDePagoAutomotor();
                break;
            case Impuesto.TIPO_IMPUESTO_ACTVARIAS:
                resultado = false; //REVISAR!!!;
                break;
        }
	return resultado;
    }

    public PlanesPago getPlanesPago() {
	return planesPago;
    }
    
    public boolean tienePlanDePago(){
	return (planesPago.getCantidadPlanesPago() > 0);
    }
    
    public boolean tienePlanDePagoTgs(){
	return (planesPago.tienePlanDePagoTgs());
    }
    
    public boolean tienePlanDePagoInmob(){
	return (planesPago.tienePlanDePagoInmob());
    }
    
    public boolean tienePlanDePagoAutomotor(){
	return (planesPago.tienePlanDePagoAutomotor());
    }
    
    public PlanDePago getPlanPagoTGS() {
	return planesPago.getPlanDePagoTGS();
    }
    
    public PlanDePago getPlanPagoInmob() {
	return planesPago.getPlanDePagoInmob();
    }
    
    public PlanDePago getPlanPagoAutomotor() {
        return planesPago.getPlanDePagoAutomotor();
    }
    
    public PlanDePago getPlanPagoComercio() {
        return planesPago.getPlanDePagoComercio();
    }
    
    public boolean puedeHacerPagoContado(Impuesto _impuesto){
	return LibSQL.getBoolean("taxes.puedeHacerPagoContado", getIdBien() + "," + _impuesto.getTipoImpuesto().getIdTipoImpuesto());
    }

    public boolean puedeHacerPagoAnual(Impuesto _impuesto){
	return LibSQL.getBoolean("taxes.getEstadoPagoAnual", _impuesto.getTipoImpuesto().getIdTipoImpuesto() + "," + getIdBien());
    }

    public void setCatastro(Cadastral catastro) {
	this.catastro = catastro;
    }

    public Cadastral getCatastro() {
	return catastro;
    }

    public void setAutomotor(Car automotor) {
	this.automotor = automotor;
    }

    public Car getAutomotor() {
        return automotor;
    }
    
    public Commerce getComercio() {
        return commerce;
    }

    public void setTipoBien(int tipoBien) {
	this.tipoBien = tipoBien;
    }

    public int getTipoBien() {
	return tipoBien;
    }
    
    public String getApoderado(){
        String resultado = "";
        switch (tipoBien) {
            case TIPO_CATASTRO:
                resultado = catastro.getApoderadoLastName() + ", " + catastro.getApoderadoName() ;
                break;
            case TIPO_AUTOMOTOR:
                resultado = automotor.getTitular();
                break;
            case TIPO_COMERCIO:
                resultado = commerce.getContribuyente();
                break;
            }
        return resultado;
    }
}

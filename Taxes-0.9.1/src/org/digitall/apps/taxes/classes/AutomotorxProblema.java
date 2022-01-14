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
 * CatastroxProblema.java
 *
 * */
 package org.digitall.apps.taxes.classes;

import java.sql.ResultSet;

import org.digitall.lib.sql.LibSQL;

public class AutomotorxProblema {
    
    int idautomotoresxproblemas = -1;
    int idautomotor = -1;
    String fechacarga = "";
    String problema = "";
    String usuario = "";
    String estado = "";
    boolean esproblema = false;
    
    public AutomotorxProblema() {
        super();
    }

    public int saveData() {
        int resultado = -1;
        Object params = "";
        if(idautomotoresxproblemas == -1){
            params = idautomotor +",'"+ problema +"','"+ estado +"',"+ esproblema +"";
            resultado = LibSQL.getInt("taxes.addproblemaxautomotor",params);
        } else {
            params =  idautomotoresxproblemas + "," + idautomotor + ",'"+problema + "','" + estado + "'," + esproblema + "";
            resultado = LibSQL.getInt("taxes.setproblemaxautomotor",params);    
        }
        return resultado;
    } 
    
    public void retrieveData() {
        Object params = idautomotoresxproblemas;
        ResultSet data = LibSQL.exFunction("taxes.getautomotorxproblema",params);
        try {
            if (data.next()) {
                idautomotoresxproblemas  = data.getInt("idautomotoresxproblemas");
                idautomotor = data.getInt("idautomotor");
                fechacarga = data.getString("fechacarga");
                problema = data.getString("problema");
                usuario = data.getString("usuario");
                estado  = data.getString("estado");
                esproblema = data.getBoolean("esproblema");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setIdautomotoressxproblemas(int _idautomotoresxproblemas) {
        this.idautomotoresxproblemas = _idautomotoresxproblemas;
    }

    public int getIdautomotoresxproblemas() {
        return idautomotoresxproblemas;
    }

    public void setIdautomotor(int _idautomotor) {
        this.idautomotor = _idautomotor;
    }

    public int getIdautomotor() {
        return idautomotor;
    }

    public void setFechacarga(String fechacarga) {
        this.fechacarga = fechacarga;
    }

    public String getFechacarga() {
        return fechacarga;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public String getProblema() {
        return problema;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEsproblema(boolean esproblema) {
        this.esproblema = esproblema;
    }

    public boolean esproblema() {
        return esproblema;
    }
}

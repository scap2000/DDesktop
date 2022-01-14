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
 * FormVotantes.java
 *
 * */
package org.digitall.apps.gaia.relevamientos.votantes_2011.interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;

import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.geo.esri.ESRIPoint;
import org.digitall.lib.geo.gaia.GaiaEnvironment;
import org.digitall.lib.geo.gaia.GaiaPrimitiveForm;
import org.digitall.lib.sql.LibSQL;

public class FormVotantes extends GaiaPrimitiveForm {

    private BasicPanel centralPanel = new BasicPanel();
    private ESRIPoint point = null;
    private TFInput tfDocumento = new TFInput(DataTypes.STRING, "Nº de Documento", false);
    private TFInput tfNombre = new TFInput(DataTypes.STRING, "Apellido y Nombre", false);
    private TFInput tfPartido = new TFInput(DataTypes.STRING, "Partido", false);
    private JArea tfObservaciones = new JArea();

    private TFInput tfCoordenadas = new TFInput(DataTypes.GEOMETRY, "COORDENADAS", false);

    private boolean readOnly = false;
    private int idVotante = -1;

    public FormVotantes() {
	this(-1);
    }

    public FormVotantes(int _idVotante) {
	readOnly = true;
	idVotante = _idVotante;
	GaiaEnvironment.formsFrame.setClosable(false);
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	setTitle("VOTANTES 2011");
	centralPanel.setBounds(new Rectangle(10, 10, 480, 170));
	centralPanel.setPreferredSize(new Dimension(480, 170));
	this.setSize(new Dimension(510, 340));
	this.setPreferredSize(new Dimension(510, 340));
	tfDocumento.setBounds(new Rectangle(255, 5, 220, 35));
	tfNombre.setBounds(new Rectangle(5, 45, 245, 35));
	tfPartido.setBounds(new Rectangle(255, 45, 220, 35));

	tfObservaciones.setBounds(new Rectangle(5, 85, 470, 80));
	tfCoordenadas.setBounds(new Rectangle(5, 5, 245, 35));

	centralPanel.setLayout(null);

	centralPanel.add(tfObservaciones, null);
	centralPanel.add(tfDocumento, null);
	centralPanel.add(tfNombre, null);
	centralPanel.add(tfPartido, null);
	centralPanel.add(tfCoordenadas, null);
	this.setCentralPanel(centralPanel);

	tfCoordenadas.getTextField().setHorizontalAlignment(JTextField.CENTER);
	tfCoordenadas.getTextField().setForeground(Color.red);
	tfCoordenadas.setEditable(false);
	tfCoordenadas.setValue(new ESRIPoint(0,0));
	setVisibleCloseButton(false);
	setVisiblePrintButton(false);
	tfDocumento.getTextField().requestFocus();
	loadData(idVotante);
    }

    private void clearData() {
	idVotante = -1;
	tfDocumento.setValue("");
	tfNombre.setValue("");
	tfPartido.setValue("");
	tfCoordenadas.setValue(new ESRIPoint(0,0));
	tfObservaciones.setText("");
	point = null;
	setEnabledDeleteButton(false);
    }

    private void clearForms() {
    }

    @Override
    public void setContentObject(Object _contentObject) {
	clearData();
	if (ESRIPoint.class.isInstance(_contentObject)) {
	    point = (ESRIPoint)_contentObject;
	    if (point.getIdPoint() != -1) {
		loadData(point.getIdPoint());
		setEnabledDeleteButton(true);
	    }
	    tfCoordenadas.setValue(point);
	} else {
	    point = null;
	}
    }

    @Override
    public void printReport() {
	//super.doCSVReport("Votantes 2011", "gea_salta.xmlGetVotantes_2011()");
    }

    @Override
    public void saveData() {
	if (point != null) {
	    if (tfNombre.isEmpty() && tfDocumento.isEmpty()) {
	        Advisor.messageBox("Debe completar al menos un campo (Nº de Documento o Apellido y Nombre)", "Grabar datos");
	    } else {
		String params = idVotante + "," + tfNombre.getStringForSQL() + "," + tfDocumento.getStringForSQL() + "," + tfPartido.getStringForSQL()
			    + "," + tfObservaciones.getStringForSQL() + ",'" + tfCoordenadas.getGeometryAsText() + "'";
		int _result = LibSQL.getInt("gea_salta.addVotante_2011", params);
		if (_result == -1) {
		    Advisor.messageBox("Error al intentar grabar los datos", "Grabar datos");
		} else {
		    if (idVotante == -1) {
			idVotante = _result;
			point.setIdPoint(idVotante);
			getLayer().getGeometrySet().addGeometry(point);
		    }
		    clearData();
		}
	    }
	}
    }

    @Override
    public void delete() {
	if (idVotante != -1) {
	    if (LibSQL.getBoolean("gea_salta.delVotante_2011", idVotante)) {
		getLayer().getGeometrySet().removeGeometry(point);
		clearData();
	    } else {
		Advisor.messageBox("Error al intentar borrar el votante", "Borrar votante");
	    }
	}
    }

    public void close() {
	hideForm();
    }

    private void loadData(int _idVotante) {
	if (_idVotante != -1) {
	    ResultSet _votante = LibSQL.exFunction("gea_salta.getVotante_2011", _idVotante);
	    try {
		if (_votante.next()) {
		    idVotante = _idVotante;
		    tfDocumento.setValue(_votante.getString("documento"));
		    tfNombre.setValue(_votante.getString("nombre"));
		    tfPartido.setValue(_votante.getString("partido"));
		    tfObservaciones.setText(_votante.getString("observaciones"));
		    point.setLocation(_votante.getDouble("x"), _votante.getDouble("y"));
		}
	    } catch (SQLException e) {
		Advisor.messageBox("Error al intentar obtener los datos del votante", "Cargar datos");
	    }
	}
    }
}


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
 * Coordinador.java
 *
 * */
package org.digitall.projects.unsa.censodocpri.classes;

import java.awt.BorderLayout;

import java.awt.Color;

import java.io.File;
import java.io.FileWriter;

import java.io.IOException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFileChooser;

import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;
import org.digitall.projects.unsa.censodocpri.interfaces.F1Escuela;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q1;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q10;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q11;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q12;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q13;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q14;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q2;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q3;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q4;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q5;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q6;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q7;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q8;
import org.digitall.projects.unsa.censodocpri.interfaces.F1q9;
import org.digitall.projects.unsa.censodocpri.interfaces.WizardPanel;

public class Coordinador {

    private F1 encuesta = new F1();
    private boolean writable = false;

    private F1Escuela f1escuela;
    private F1q1 f1q1;
    private F1q2 f1q2;
    private F1q3 f1q3;
    private F1q4 f1q4;
    private F1q5 f1q5;
    private F1q6 f1q6;
    private F1q7 f1q7;
    private F1q8 f1q8;
    private F1q9 f1q9;
    private F1q10 f1q10;
    private F1q11 f1q11;
    private F1q12 f1q12;
    private F1q13 f1q13;
    private F1q14 f1q14;

    private WizardPanel contentPane = new WizardPanel();
    

    public Coordinador(BasicPrimitivePanel _parent) {
	_parent.setContent(contentPane);
	f1escuela = new F1Escuela(this);
    }
    
    public void jbInit() {
	f1q1 = new F1q1(this);
	f1q2 = new F1q2(this);
	f1q3 = new F1q3(this);
	f1q4 = new F1q4(this);
	f1q5 = new F1q5(this);
	f1q6 = new F1q6(this);
	f1q7 = new F1q7(this);
	f1q8 = new F1q8(this);
	f1q9 = new F1q9(this);
	f1q10 = new F1q10(this);
	f1q11 = new F1q11(this);
	f1q12 = new F1q12(this);
	f1q13 = new F1q13(this);
	f1q14 = new F1q14(this);
	showF1Escuela();
    }

    public F1 getEncuesta() {
	return encuesta;
    }

    public void newEncuesta() {
	contentPane.setTitle("Nueva encuesta");
	encuesta = new F1();
	writable = true;
	jbInit();
    }
    
    public void setEncuesta(F1 encuesta, boolean _writable) {
	this.encuesta = encuesta;
	writable = _writable;
	if (encuesta.getIdEncuesta() != -1) {
	    encuesta.retrieveData();
	contentPane.setTitle("Encuesta Nº " + encuesta.getIdEncuesta() + (writable?" (Modificando)":" (Sólo lectura)"));
	}
	jbInit();
    }

    public void showF1Escuela() {
	contentPane.setCentralPanel(f1escuela);
    }

    public void showF1q1() {
	contentPane.setCentralPanel(f1q1);
    }

    public void showF1q2() {
	contentPane.setCentralPanel(f1q2);
    }

    public void showF1q3() {
	contentPane.setCentralPanel(f1q3);
    }

    public void showF1q4() {
	contentPane.setCentralPanel(f1q4);
    }

    public void showF1q5() {
	contentPane.setCentralPanel(f1q5);
    }

    public void showF1q6() {
	contentPane.setCentralPanel(f1q6);
    }

    public void showF1q7() {
	contentPane.setCentralPanel(f1q7);
    }

    public void showF1q8() {
	contentPane.setCentralPanel(f1q8);
    }

    public void showF1q9() {
	contentPane.setCentralPanel(f1q9);
    }

    public void showF1q10() {
	contentPane.setCentralPanel(f1q10);
    }

    public void showF1q11() {
	contentPane.setCentralPanel(f1q11);
    }

    public void showF1q12() {
	contentPane.setCentralPanel(f1q12);
    }

    public void showF1q13() {
	contentPane.setCentralPanel(f1q13);
    }

    public void showF1q14() {
	contentPane.setCentralPanel(f1q14);
    }

    public void finEncuesta() {
	if (isWritable()) {
	    int _idencuesta = -1;
	    _idencuesta = encuesta.saveData();
	    if (_idencuesta != -1) {
		Advisor.messageBox("Encuesta grabada con éxito, identificador Nº " + _idencuesta, "Encuesta Nº " + _idencuesta);
		newEncuesta();
	    } else {
		Advisor.messageBox("Ha ocurrido un error al intentar grabar la encuesta", "Encuesta Nº " + _idencuesta);
	    }
	}
    }
    
    public void setWritable(boolean writable) {
	this.writable = writable;
    }

    public boolean isWritable() {
	return writable;
    }
    
    public void setEscuela(String _escuela) {
	encuesta.setEscuela(_escuela);
	String[] _data = _escuela.split("\\|");
	if (_data.length > 2) {
	    contentPane.setEscuela(_data[2]);
        } else {
	    contentPane.setEscuela("");
	}
    }


    
    private FileWriter openFile(String _reportName) {
	String _path = Environment.cfg.getProperty(_reportName) + File.separator;
	JFileChooser chooser = new JFileChooser(_path);
	chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	int returnVal = chooser.showSaveDialog(chooser.getParent());
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    // IF File Selected
	    try {
		String path = chooser.getSelectedFile().getAbsolutePath();
		if (!path.toLowerCase().endsWith(".csv")) {
		    path += ".csv";
		}
		Environment.cfg.setProperty(_reportName, chooser.getSelectedFile().getParent());
		return new FileWriter(path, false);
	    } catch (IOException x) {
		Advisor.messageBox("Error de E/S", "Error " + _reportName);
		x.printStackTrace();
		return null;
	    }
	} else {
	    return null;
	}
    }
    
    public void doCSVReport(String _reportName, String _sqlFunction, Object _params) {
	if (_sqlFunction.length() > 0) {
	    FileWriter _reportFile = openFile(_reportName);
	    if (_reportFile != null) {
		ResultSet _resultSet = LibSQL.exFunction(_sqlFunction, _params);
		try {
		    if (_resultSet.next()) {
			ResultSetMetaData _resultSetMetaData = _resultSet.getMetaData();
			try {
			    for (int i = 0; i < _resultSetMetaData.getColumnCount(); i++) {
				_reportFile.write("\"" + _resultSetMetaData.getColumnName(i+1) + "\"\t");
			    }
			    _reportFile.write("\n");
			    do {
				for (int i = 0; i < _resultSetMetaData.getColumnCount(); i++) {
				    if (_resultSet.getString(i+1) != null) {
					String _field = _resultSet.getString(i+1);
					//_field = _field.replaceAll("\"", "\\\"");
					//_field = _field.replaceAll("\n", "\\\n");
					_field = _field.replaceAll("[\\r\\n\"\t]", "");
					//_field = _field.replaceAll("\t", "\\\t");
					_reportFile.write("\"" + _field + "\"\t");
				    } else {
					_reportFile.write("\"\"\t");
				    }
				}
				_reportFile.write("\n");
			    } while (_resultSet.next());
			    _reportFile.close();
			    Advisor.messageBox("<html><p align=center>Los datos se han exportado separados por tabulaciones</p></html>", "Exportación de datos");
			} catch (Exception e) {
			    // TODO
			    Advisor.printException(e);
			}
		    }
		} catch (SQLException x) {
		    Advisor.printException(x);
		}
	    }
	}
    }
}

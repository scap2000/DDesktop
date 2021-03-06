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
 * JArea.java
 *
 * */
package org.digitall.lib.components;

import java.awt.Color;

import java.awt.event.FocusEvent;

import java.awt.event.FocusListener;

import java.io.IOException;

import java.net.URL;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicScrollPane;

public class JArea extends BasicScrollPane {

    private JTArea jtArea = new JTArea();
    
    public final static String CONTENT_HTML = "text/html";

    public JArea() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.getViewport().add(jtArea, null);
	addFocusListener(new FocusListener() {

	    public void focusGained(FocusEvent focusEvent) {
		setBorder(BorderFactory.createLineBorder(new Color(150, 0, 0), 2));
	    }

	    public void focusLost(FocusEvent focusEvent) {
	        setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
	    }

	});
    }

    public String getText() {
	return jtArea.getText();
    }

    public String getStringForSQL() {
	return "'" + getText() + "'";
    }

    public void setText(String _text) {
	jtArea.setText(_text);
	jtArea.setCaretPosition(0);
    }
    
    public void setPage(URL uRL) {
	try {
	    jtArea.setPage(uRL);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public JTArea getTextArea() {
	return jtArea;
    }

    public void setDisabledTextColor(Color _color) {
	jtArea.setDisabledTextColor(_color);
    }

    public void setEditable(boolean _editable) {
	jtArea.setEditable(_editable);
    }

    public void setContentType(String _contenType) {
	jtArea.setContentType(_contenType);
    }

}

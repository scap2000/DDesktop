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
 * WizardPanel.java
 *
 * */
package org.digitall.projects.unsa.censodocpri.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicTextField;

public class WizardPanel extends BasicPrimitivePanel {

    private BasicPanel jpNorth = new BasicPanel();
    private BasicLabel lblTitle = new BasicLabel();
    private BasicLabel lblEscuela = new BasicLabel();
    private BasicTextField tfEscuela = new BasicTextField();
    private Component centralPanel = new BasicPanel();

    public WizardPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(693, 300));
	jpNorth.setPreferredSize(new Dimension(10, 55));
	jpNorth.setLayout(new BorderLayout());
	lblTitle.setPreferredSize(new Dimension(39, 30));
	lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
	lblTitle.setFont(new Font("Dialog", 1, 11));
	lblEscuela.setText("Escuela:");
	lblEscuela.setPreferredSize(new Dimension(60, 13));
	lblEscuela.setHorizontalAlignment(SwingConstants.CENTER);
	lblEscuela.setFont(new Font("Lucida Sans", 1, 12));
	jpNorth.add(lblTitle, BorderLayout.NORTH);
	jpNorth.add(lblEscuela, BorderLayout.WEST);
	jpNorth.add(tfEscuela, BorderLayout.CENTER);
	tfEscuela.setEditable(false);
	tfEscuela.setHorizontalAlignment(JTextField.CENTER);
	tfEscuela.setForeground(new Color(148, 0, 0));
	tfEscuela.setFont(new Font("Dialog", 1, 12));
	this.add(jpNorth, BorderLayout.NORTH);
	this.add(centralPanel, BorderLayout.CENTER);
    }
    
    public void setCentralPanel(Component _panel) {
	remove(centralPanel);
	centralPanel = _panel;
	add(centralPanel, BorderLayout.CENTER);
	updateUI();
    }

    public void setEscuela(String _escuela) {
	tfEscuela.setText(_escuela);
    }
    
    public void setTitle(String _title) {
	lblTitle.setText("Cuestionario para Docentes Primarios" + (_title.length()>0?" - " + _title:""));
    }
}

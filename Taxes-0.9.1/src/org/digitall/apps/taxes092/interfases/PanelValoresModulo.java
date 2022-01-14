package org.digitall.apps.taxes092.interfases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;

import java.util.Vector;

import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.grid.GridPanel;

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
 * PanelValoresModulo.java
 *
 * */

public class PanelValoresModulo extends BasicPrimitivePanel {

    private int[] sizeColumnList = {92,101, 89, 112, 198};
    private Vector<String> headerList = new Vector<String>();
    private Vector dataRow = new Vector();
    private GridPanel listPanel = new GridPanel(50000, sizeColumnList, "Valores de Módulo registrados", dataRow);
    private BasicPanel panelTransaction = new BasicPanel();

    public PanelValoresModulo() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(645, 425));
	panelTransaction.setLayout(null);
	panelTransaction.setBorder(BorderPanel.getBorderPanel("Nuevo valor", Color.red, Color.black));
	panelTransaction.setSize(new Dimension(635, 125));
	panelTransaction.setMinimumSize(new Dimension(1, 100));
	panelTransaction.setPreferredSize(new Dimension(1, 130));
        this.add(listPanel, BorderLayout.CENTER);
        this.add(panelTransaction, BorderLayout.NORTH);
        setHeaderList();
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
	super.setParentInternalFrame(_e);
	getParentInternalFrame().setClosable(false);
	getParentInternalFrame().setInfo("**** INFORMACIÓN ***");
    }
    
    private void setHeaderList() {
	headerList.removeAllElements();
	headerList.addElement("Nº");
	headerList.addElement("Desde");
	headerList.addElement("Hasta");
	headerList.addElement("($) Valor");
	headerList.addElement("Ordenanza");
				 
	listPanel.getTable().addMouseListener(new MouseAdapter(){
	    public void mouseClicked(MouseEvent me) {
	        
		if (me.getButton() == MouseEvent.BUTTON3 && me.getClickCount() == 2) {
		    int index = listPanel.getTable().rowAtPoint(me.getPoint());
		    listPanel.getTable().getSelectionModel().setSelectionInterval(index, index);
		    
		}
	    }
	});
	
	listPanel.setParams("taxes.getAllValoresModulo", "", headerList);
    }

    public void loadList() {
	listPanel.refresh("");
    }


}

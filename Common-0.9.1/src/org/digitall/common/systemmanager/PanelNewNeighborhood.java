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
 * PanelNewNeighborhood.java
 *
 * */
package org.digitall.common.systemmanager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.lib.components.JEntry;
import org.digitall.lib.components.JOutry;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.sql.LibSQL;

//

public class PanelNewNeighborhood extends BasicContainerPanel {

    private BasicPanel PanelCountry = new BasicPanel();
    private BasicLabel lblNeighborhood = new BasicLabel();
    private JEntry tfNeighborhood = new JEntry();
    private JOutry tfLocation = new JOutry();
    private AcceptButton bAccept = new AcceptButton();
    private CloseButton bClose = new CloseButton();
    private final int FRAME = 1;
    private final int INTERNALFRAME = 2;
    private final int DIALOG = 3;
    private int parentType = -1;
    private Component parent;
    private String insert, idlocation = "";
    private int error = 0;
    private BasicLabel lblLocation = new BasicLabel();

    public PanelNewNeighborhood(BasicInternalFrame _parent, String _idlocation) {
	try {
	    parent = _parent;
	    parentType = INTERNALFRAME;
	    idlocation = _idlocation;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelNewNeighborhood(BasicDialog _parent, String _idlocation) {
	try {
	    parent = _parent;
	    parentType = DIALOG;
	    idlocation = _idlocation;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelNewNeighborhood(JFrame _parent, String _idlocation) {
	try {
	    parent = _parent;
	    parentType = FRAME;
	    idlocation = _idlocation;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(286, 124));
	tfNeighborhood.setBounds(new Rectangle(5, 50, 265, 20));
	tfNeighborhood.setFont(new Font("Dialog", 1, 11));
	bAccept.setBounds(new Rectangle(240, 90, 40, 25));
	bAccept.setSize(new Dimension(40, 25));
	bAccept.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bAccept_actionPerformed(e);
		    }

		});
	bClose.setBounds(new Rectangle(5, 90, 40, 25));
	bClose.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bClose_actionPerformed(e);
		    }

		});
	lblLocation.setText("Location:");
	lblLocation.setBounds(new Rectangle(5, 5, 265, 10));
	lblLocation.setFont(new Font("Dialog", 1, 11));
	lblLocation.setHorizontalAlignment(SwingConstants.LEFT);
	lblLocation.setText("Location:");
	lblLocation.setBounds(new Rectangle(5, 5, 265, 10));
	lblLocation.setFont(new Font("Dialog", 1, 11));
	lblLocation.setHorizontalAlignment(SwingConstants.LEFT);
	lblNeighborhood.setText("Location:");
	lblNeighborhood.setBounds(new Rectangle(5, 5, 265, 10));
	lblNeighborhood.setFont(new Font("Dialog", 1, 11));
	lblNeighborhood.setHorizontalAlignment(SwingConstants.LEFT);
	tfLocation.setBounds(new Rectangle(5, 15, 265, 20));
	PanelCountry.setBounds(new Rectangle(5, 5, 275, 80));
	PanelCountry.setLayout(null);
	PanelCountry.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	lblNeighborhood.setText("Neighborhood:");
	lblNeighborhood.setBounds(new Rectangle(5, 40, 265, 10));
	lblNeighborhood.setFont(new Font("Dialog", 1, 11));
	lblNeighborhood.setHorizontalAlignment(SwingConstants.LEFT);
	PanelCountry.add(lblLocation, null);
	PanelCountry.add(tfLocation, null);
	PanelCountry.add(lblNeighborhood, null);
	PanelCountry.add(tfNeighborhood, null);
	PanelCountry.add(lblNeighborhood, null);
	this.add(PanelCountry, null);
	this.add(bClose, null);
	this.add(bAccept, null);
	seteaDatos();
    }

    private void seteaDatos() {
	String localidad = org.digitall.lib.sql.LibSQL.getCampo("Select name From tabs.location_tabs Where idlocation = " + idlocation);
	tfLocation.setText(localidad);
    }

    private void dispose() {
	switch (parentType) {
	    case DIALOG:
		((BasicDialog)parent).dispose();
		break;
	    case INTERNALFRAME:
		((BasicInternalFrame)parent).dispose();
		break;
	    case FRAME:
		((JFrame)parent).dispose();
		break;
	}
    }

    private void bClose_actionPerformed(ActionEvent e) {
	dispose();
    }

    private boolean control() {
	if (tfNeighborhood.getText().equals("")) {
	    error = 1;
	    return false;
	} else
	    return true;
    }

    private void bAccept_actionPerformed(ActionEvent e) {
	if (control()) {
	    insert = "INSERT INTO tabs.neighborhood_tabs VALUES ( (select max(idneighborhood) +1 From tabs.neighborhood_tabs)" + " , " + idlocation + " ,'" + tfNeighborhood.getText().toUpperCase().toLowerCase() + "' , '')";
	    //System.out.println("insert: "+ insert);
	    if (LibSQL.exActualizar('a', insert)) {
		org.digitall.lib.components.Advisor.messageBox("Insert Success!", "Message");
	    }
	} else
	    error();
    }

    private void error() {
	switch (error) {
	    case 1:
		org.digitall.lib.components.Advisor.messageBox("Field Neighborhood is empty", "Error");
		break;
	}
    }

}

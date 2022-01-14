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
 * VoucherToProvisionOrderMain.java
 *
 * */
package org.digitall.apps.cashflow.interfaces.voucher;

import java.awt.Dimension;

import org.digitall.lib.components.basic.BasicTabContainer;
import org.digitall.lib.components.basic.ExtendedInternalFrame;


public class VoucherToProvisionOrderMain extends BasicTabContainer {

    private VoucherToProvisionOrderList voucherToProvisionOrderList = new VoucherToProvisionOrderList(this);
    private VoucherToProvisionOrderMgmt voucherToProvisionOrderMgmt = new VoucherToProvisionOrderMgmt(this);

    public VoucherToProvisionOrderMain() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(900, 500));
	this.setPreferredSize(new Dimension(900, 500));
	addTab(voucherToProvisionOrderList);
	addTab(voucherToProvisionOrderMgmt);
	getTabbedPane().setEnabledAt(1, false);
    }

    

    public void changeSelectedTab() {
	if (getParentInternalFrame() != null) {
	    if (getSelectedTab() == 0) {
		getParentInternalFrame().setInfo("Seleccione las Órdenes de Provisión a facturar y presione el botón \"Facturar\"");
	    } else {
		getParentInternalFrame().setInfo("Ajuste los campos Bonificación y/o Precio en cada uno de los ítems y presione el botón \"Grabar Factura\"");
	    }
	}
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
	super.setParentInternalFrame(_e);
	changeSelectedTab();
    }

    public VoucherToProvisionOrderList getVoucherToProvisionOrderList() {
	return voucherToProvisionOrderList;
    }

    public VoucherToProvisionOrderMgmt getVoucherToProvisionOrderMgmt() {
	return voucherToProvisionOrderMgmt;
    }

    @Override
    public boolean saveData() {
	return voucherToProvisionOrderMgmt.saveData();
    }
}


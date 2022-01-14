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
 * CashierClosures.java
 *
 * */
package org.digitall.apps.taxes.interfases.cashier;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Vector;

import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;


public class CashierClosures extends BasicPrimitivePanel {

    private BasicPanel content = new BasicPanel();
    private BasicPanel northPanel = new BasicPanel();

    private BasicPanel depositoPanel = new BasicPanel();
    private BasicPanel northWestPanel = new BasicPanel();
    private BasicPanel centerPanel = new BasicPanel();

    private int[] closuresColumnSize = { 226, 242, 182, 200, 200 };
    private Vector closuresDataRow = new Vector();
    private GridPanel closuresListPanel = new GridPanel(1000, closuresColumnSize, "Cierres de caja", closuresDataRow);
    private Vector<String> closuresHeader = new Vector<String>();

    private int[] closureDetailColumnSize = {  120, 79, 232, 66, 98 };
    private Vector closureDetailDataRow = new Vector();
    private GridPanel closureDetailListPanel = new GridPanel(50000, closureDetailColumnSize, "Detalle de los pagos", closureDetailDataRow);
    private Vector<String> closureDetailHeader = new Vector<String>();


    private PrintButton btnImprimirCierreCaja = new PrintButton();
    
    private BorderLayout borderLayout1 = new BorderLayout();

    private GridLayout gridLayout1 = new GridLayout();
    private GridLayout gridLayout2 = new GridLayout();

    private int idClosure = -1;

    private BasicPanel pContenedorBusqueda = new BasicPanel();
    private BasicPanel pBusqueda = new BasicPanel();

    private TFInput tfUsuario = new TFInput(DataTypes.STRING, "Usuario", false);
    private TFInput tfFechaDesde = new TFInput(DataTypes.SIMPLEDATE, "Desde", false);
    private TFInput tfFechaHasta = new TFInput(DataTypes.SIMPLEDATE, "Hasta", false);
    private FindButton btnBusqueda = new FindButton();
    private CBInput cbCajas = new CBInput(0,"Caja", false);

    private BorderLayout borderLayout2 = new BorderLayout();

    private BorderLayout borderLayout3 = new BorderLayout();
    private String error = "";
    
    public CashierClosures() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(700, 507));
	this.setBounds(new Rectangle(10, 10, 700, 537));
	this.setTitle("Caja");
	northWestPanel.setLayout(borderLayout1);
	northWestPanel.setPreferredSize(new Dimension(700, 170));
	centerPanel.setLayout(gridLayout1);
	gridLayout1.setColumns(1);
	gridLayout2.setColumns(1);
	pContenedorBusqueda.setLayout(borderLayout2);
	pBusqueda.setLayout(null);
	pBusqueda.setBorder(BorderPanel.getBorderPanel("Búsqueda"));
	pBusqueda.setPreferredSize(new Dimension(700, 65));
	tfUsuario.setBounds(new Rectangle(15, 20, 140, 35));
	tfFechaDesde.setBounds(new Rectangle(195, 20, 140, 35));
	tfFechaDesde.setSize(new Dimension(120, 35));
	tfFechaHasta.setBounds(new Rectangle(355, 20, 120, 35));
        tfFechaDesde.getTextField().addKeyListener(new KeyAdapter() {

                                                   public void keyTyped(KeyEvent e) {
                                                       if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
                                                           buscar();
                                                       }
                                                   }

                                               }
        );
        tfFechaHasta.getTextField().addKeyListener(new KeyAdapter() {

                                                   public void keyTyped(KeyEvent e) {
                                                       if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
                                                           buscar();
                                                       }
                                                   }

                                               }
        );
	btnBusqueda.setBounds(new Rectangle(505, 35, 35, 20));
	btnBusqueda.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnBusqueda_actionPerformed(e);
		}
	    }
	);
        
        cbCajas.setBounds(new Rectangle(15, 20, 140, 35));

	northPanel.setLayout(gridLayout2);

	depositoPanel.setLayout(null);
	content.setLayout(borderLayout3);

	content.setPreferredSize(new Dimension(700, 500));
	northPanel.setPreferredSize(new Dimension(700, 170));
	//northPanel.setMinimumSize(new Dimension(170, 100));
	centerPanel.setPreferredSize(new Dimension(700, 265));

	btnImprimirCierreCaja.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnImprimirCierreCaja_actionPerformed(e);
		}

	    }
	);

        closureDetailListPanel.setPreferredSize(new Dimension(700, 265));
        centerPanel.add(closureDetailListPanel);

	this.add(content, null);
	addButton(btnImprimirCierreCaja);

	btnImprimirCierreCaja.setToolTipText("Reimprimir Planilla de Cierre de Caja");

        northWestPanel.add(closuresListPanel, BorderLayout.CENTER);
	northPanel.add(northWestPanel);
	pBusqueda.add(btnBusqueda, null);
	pBusqueda.add(tfFechaHasta, null);
	pBusqueda.add(tfFechaDesde, null);
	//pBusqueda.add(tfUsuario, null);
        pBusqueda.add(cbCajas, null);
        cbCajas.autoSize();
        cbCajas.setGeneralItem(true);

	pContenedorBusqueda.add(pBusqueda, BorderLayout.NORTH);
	pContenedorBusqueda.add(northPanel, BorderLayout.CENTER);
	content.add(pContenedorBusqueda, BorderLayout.NORTH);
	content.add(centerPanel, BorderLayout.CENTER);
	
        setClosureHeader();
        setClosuresDetailHeader();
	
        btnImprimirCierreCaja.setEnabled(false);
	tfUsuario.getTextField().setEditable(false);
	tfUsuario.setValue(Environment.sessionUser);
	tfFechaDesde.setValue(Proced.setFormatDate(Environment.currentDate,true));
        loadComboCajas();
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
	super.setParentInternalFrame(_e);
    }

    private void loadComboCajas() {
        cbCajas.loadJCombo(LibSQL.exFunction("cashier.getAllCajas",""));
    }
    
    private void setClosureHeader() {
	closuresHeader.removeAllElements();
	closuresHeader.addElement("*"); 
	closuresHeader.addElement("Fecha");
	closuresHeader.addElement("Hora"); 
	closuresHeader.addElement("*"); 
	closuresHeader.addElement("Usuario"); 
	closuresHeader.addElement("# Caja"); 
	closuresHeader.addElement("($) Monto"); 
	closuresHeader.addElement("*"); 

	closuresListPanel.getTable().addMouseListener(new MouseAdapter() {

						 public void mouseClicked(MouseEvent e) {
						     loadSelectedObject();
						     if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
						         refreshClosureDetail();
						     } else if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {

						     }
						 }

					     }
	);
	String params = cbCajas.getSelectedValueRef() + ",'"+Proced.setFormatDate(tfFechaDesde.getDate(),false)+"','"+Proced.setFormatDate(tfFechaDesde.getDate(),false)+"'"; 
	closuresListPanel.setParams("cashier.getallcashierclosures", params, closuresHeader);
    }

    private void setClosuresDetailHeader() {

        closureDetailHeader.removeAllElements();
        closureDetailHeader.addElement("*");
        closureDetailHeader.addElement("Fecha");
        closureDetailHeader.addElement("Hora");
        closureDetailHeader.addElement("*");
        closureDetailHeader.addElement("Tipo");
        closureDetailHeader.addElement("*");
        closureDetailHeader.addElement("*");
        closureDetailHeader.addElement("# Ticket");
        closureDetailHeader.addElement("($) Monto");

        closureDetailListPanel.getTable().addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    
                    if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
                        //LOAD DATA
                    } else if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                        
                    }
                }

            }
        );
        closureDetailListPanel.setParams("cashier.getCierreCaja", "-1", closureDetailHeader);
    }


    private void refreshPagos(String _params) {
	closuresListPanel.refresh(_params);
	btnImprimirCierreCaja.setEnabled(false);
        closureDetailListPanel.refresh("-1");
    }
    
    private void loadSelectedObject() {
	if (!closuresDataRow.isEmpty()) {
	    idClosure =  Integer.parseInt(closuresDataRow.elementAt(0).toString());
	    btnImprimirCierreCaja.setEnabled(true);
	} else {
            btnImprimirCierreCaja.setEnabled(false);
        }
    }
    
    private void refreshClosureDetail() {
        closureDetailListPanel.refresh(""+ idClosure);
    }
    

    private void btnImprimirCierreCaja_actionPerformed(ActionEvent e) {
	CashierPrinter.printClosure(idClosure, true);
    }

    private void btnBusqueda_actionPerformed(ActionEvent e) {
	buscar();
    }
    
    private void buscar(){
        if(control()) {
            String params = cbCajas.getSelectedValueRef().toString();
            if( (tfFechaDesde.getDate().equals("")) && (tfFechaHasta.getDate().equals("")) ) { // '' AND ''
                params += ",'',''";
            } else {
                if (tfFechaHasta.getDate().equals("")) { // 'FECHAINICIO' AND ''
                    params += ",'"+Proced.setFormatDate(tfFechaDesde.getDate(),false)+"','"+Proced.setFormatDate(tfFechaDesde.getDate(),false)+"'"; 
                } else {// 'FECHAINICIO' AND 'FECHAFIN'
                    params += ",'"+Proced.setFormatDate(tfFechaDesde.getDate(),false)+"','"+Proced.setFormatDate(tfFechaHasta.getDate(),false)+"'";
                }
            }
            refreshPagos(params);
        } else {
            Advisor.messageBox(error, "Error");
        }
    }
    
    private boolean control(){
	boolean retorno = true;
	if((tfFechaDesde.getDate().equals(""))&& (!tfFechaHasta.getDate().equals(""))){
	    error = "El campo \"Fecha Inicio\" no debe ser vacío";
	    retorno = false;
	}else{
	    if(Proced.compareDates(Proced.setFormatDate(tfFechaDesde.getDate(),false),Proced.setFormatDate(tfFechaHasta.getDate(),false)) == 2){
	        error = "La Fecha de Inicio no puede ser mayor que la Fecha de Fin";
	        retorno = false;
	    }
	}
	return retorno;
    }
}

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
 * PanelPagoAnticipos.java
 *
 * */
package org.digitall.apps.taxes092.interfases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.digitall.apps.taxes.classes.AlicuotaContribucion;
import org.digitall.apps.taxes.classes.BoletaContribucion;
import org.digitall.apps.taxes.interfases.feesadmin.PlansOfPaymentMgmt;
import org.digitall.apps.taxes092.classes.Coordinador;
import org.digitall.apps.taxes092.classes.Impuesto;
import org.digitall.apps.taxes092.classes.PagoAnticipos;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.common.reports.BasicReport;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicRadioButton;
import org.digitall.lib.components.basic.BasicTextArea;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.data.Format;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

public class PanelPagoAnticipos extends BasicPanel {

    private Coordinador coordinador;
    private BasicPanel panelAyuda = new BasicPanel();
    private BasicPanel panelCentral = new BasicPanel();
    private BasicPanel panelSeleccionImpuesto = new BasicPanel();
    private BasicPanel panelSeleccionBonificacion = new BasicPanel();
    private BasicLabel lblTitulo = new BasicLabel();
    private BasicTextArea taAyuda = new BasicTextArea();
    private BasicRadioButton rbtnTgs = new BasicRadioButton();
    private BasicRadioButton rbtnInmob = new BasicRadioButton();
    private BasicRadioButton rbtnAutomotor = new BasicRadioButton();
    private BasicRadioButton rbtnComercio = new BasicRadioButton();
    private BasicCheckBox chkVerTodosAnticipos = new BasicCheckBox();
    private TFInput tfEntrega = new TFInput(DataTypes.MONEY, "($) Entrega", false);
    private CBInput cbBonificaciones = new CBInput(0, "Bonificación", false);
    private CBInput cbCantidadAnticipos = new CBInput(0, "(#) Cantidad", false);
    private JSeparator separador1 = new JSeparator();
    private JSeparator separador2 = new JSeparator();
    private TFInput tfMonto = new TFInput(DataTypes.MONEY, "($) Monto", false);
    private TFInput tfInteres = new TFInput(DataTypes.MONEY, "($) Interés", false);
    private TFInput tfBonificacion = new TFInput(DataTypes.MONEY, "($) Bonificación", false);
    private TFInput tfSaldo = new TFInput(DataTypes.MONEY, "($) Saldo CC", false);
    private TFInput tfTotal = new TFInput(DataTypes.MONEY, "($) Total", false);
    private TFInput tfPorcentajeParcial = new TFInput(DataTypes.PERCENT, "(%) Parcial", false);
    private ButtonGroup rbGroupTiposImpuesto = new ButtonGroup();
    private BasicRadioButton rbtnRegistarBoleta = new BasicRadioButton();
    private BasicRadioButton rbtnGenerarPP = new BasicRadioButton();
    private BasicRadioButton rbtnLibreDeuda = new BasicRadioButton();
    private BasicRadioButton rbtnPagoContado = new BasicRadioButton();
    private ButtonGroup grupo2 = new ButtonGroup();
    private BasicButton btnRegistrarBoleta = new BasicButton("Registrar Boleta");
    private BasicButton btnDesbloquearPagoEdesa = new BasicButton("DESBLOQUEAR Pago EDESA");
    private BasicButton btnRegistrarPagoEdesa = new BasicButton("Registrar Pago EDESA");
    private BasicButton btnPagoContado = new BasicButton("Pago de Contado");
    private BasicButton btnPlanDePago = new BasicButton("Plan de Pago");
    private BasicButton btnLibreDeuda = new BasicButton("Libre Deuda");
    private BasicButton btnLibreDeudaConBaja = new BasicButton("Libre Deuda con Baja");
    private BasicButton btnLeyVeinteanial = new BasicButton("Ley Veinteañal");
    private BasicButton btnPagoAnual = new BasicButton("Pago Anual");
    private int[] sizeColumnList = { 23, 38, 85, 88, 52, 58, 67, 57, 70, 84, 73, 74, 74 };
    private Vector dataRow = new Vector();
    private Vector<String> headerList = new Vector<String>();
    private boolean panelIniciado = false;
    private GridPanel grillaAnticiposAdeudados = new GridPanel(50000, sizeColumnList, "Anticipos Adeudados", dataRow) {

	public void finishLoading() {
	    setComboAnticipos();
	    cbBonificaciones.setSelectedID(0);
	    calcTotalAmount();
	}
    };
    private PrintButton btnImprimirEstadoDeuda = new PrintButton();

    public PanelPagoAnticipos(Coordinador _coordinador) {
	coordinador = _coordinador;
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(845, 475));
	panelAyuda.setBounds(new Rectangle(0, 0, 215, 420));
	panelAyuda.setLayout(null);
	panelAyuda.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	panelAyuda.setSize(new Dimension(215, 475));
	lblTitulo.setText("Selección de Anticipos a Pagar");
	lblTitulo.setBounds(new Rectangle(5, 5, 205, 20));
	lblTitulo.setBackground(Color.black);
	lblTitulo.setOpaque(true);
	lblTitulo.setFont(new Font("Dialog", 1, 11));
	lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
	lblTitulo.setHorizontalTextPosition(SwingConstants.CENTER);
	taAyuda.setBounds(new Rectangle(5, 30, 205, 385));
	panelCentral.setBounds(new Rectangle(215, 0, 630, 475));
	panelCentral.setLayout(null);
	panelCentral.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	panelSeleccionImpuesto.setBounds(new Rectangle(5, 5, 405, 65));
	panelSeleccionImpuesto.setBorder(BorderPanel.getBorderPanel("Seleccionar Impuesto"));
	panelSeleccionImpuesto.setLayout(null);
	panelSeleccionBonificacion.setBounds(new Rectangle(415, 5, 210, 65));
	panelSeleccionBonificacion.setBorder(BorderPanel.getBorderPanel("Seleccionar Descuento"));
	panelSeleccionBonificacion.setLayout(null);
	cbBonificaciones.setBounds(new Rectangle(10, 20, 190, 30));
	cbBonificaciones.setSize(new Dimension(190, 35));
	cbBonificaciones.addItemListener(new ItemListener() {

		public void itemStateChanged(ItemEvent e) {
		    cbBonificaciones_itemStateChanged(e);
		}
	    });
	rbtnTgs.setText("TGS");
	rbtnTgs.setBounds(new Rectangle(10, 30, 70, 20));
	rbtnInmob.setText("Inmob.");
	rbtnInmob.setBounds(new Rectangle(90, 30, 90, 20));
        rbtnAutomotor.setText("Automotor");
        rbtnAutomotor.setBounds(new Rectangle(190, 30, 100, 20));
        rbtnComercio.setText("Comercio");
        rbtnComercio.setBounds(new Rectangle(300, 30, 90, 20));
	grillaAnticiposAdeudados.setBounds(new Rectangle(5, 75, 620, 255));
	chkVerTodosAnticipos.setText("Ver Adelantos");
	chkVerTodosAnticipos.setBounds(new Rectangle(5, 335, 120, 20));
	tfEntrega.setBounds(new Rectangle(365, 335, 95, 35));
	tfEntrega.getTextField().addKeyListener(new KeyAdapter() {

		public void keyTyped(KeyEvent e) {
		    if ((e.getKeyChar() == KeyEvent.VK_ENTER) || (e.getKeyChar() == KeyEvent.VK_TAB)) {
			calcTotalAmount();
		    }
		}

	    });
	cbCantidadAnticipos.setBounds(new Rectangle(468, 335, 80, 35));
	tfEntrega.setValue(0.0);
	cbCantidadAnticipos.addItemListener(new ItemListener() {

		public void itemStateChanged(ItemEvent evt) {
		    if (evt.getStateChange() == ItemEvent.SELECTED) {
			tfPorcentajeParcial.setValue(0.0);
			calcTotalAmount();
		    }
		}
	    });
	separador1.setBounds(new Rectangle(10, 420, 610, 5));
	separador2.setBounds(new Rectangle(5, 420, 205, 5));
	tfMonto.setBounds(new Rectangle(10, 375, 100, 35));
	tfMonto.setEditable(false);
	tfInteres.setBounds(new Rectangle(125, 375, 100, 35));
	tfInteres.setEditable(false);
	tfBonificacion.setBounds(new Rectangle(240, 375, 100, 35));
	tfBonificacion.setEditable(false);

        tfSaldo.setBounds(new Rectangle(350, 375, 80, 35));
        tfSaldo.setEditable(false);
        tfSaldo.getTextField().setBackground(new Color(41, 165, 165));

	tfTotal.setBounds(new Rectangle(520, 375, 100, 35));
	tfTotal.setEditable(false);
	tfTotal.setSize(new Dimension(100, 35));
        tfTotal.getTextField().setBackground(new Color(231, 115, 0));

	rbtnRegistarBoleta.setText("Registrar Boleta");
	rbtnRegistarBoleta.setBounds(new Rectangle(140, 335, 80, 20));
	rbtnGenerarPP.setText("Generar Plan de Pago");
	rbtnGenerarPP.setBounds(new Rectangle(135, 355, 85, 20));
	rbtnGenerarPP.setPreferredSize(new Dimension(140, 20));
	rbtnGenerarPP.setSize(new Dimension(140, 20));
        
        btnLibreDeuda.setEnabled(false);
        btnLibreDeudaConBaja.setEnabled(false);
        
	rbtnLibreDeuda.setText("Libre Deuda");
	rbtnLibreDeuda.setBounds(new Rectangle(220, 355, 75, 20));
	btnImprimirEstadoDeuda.setBounds(new Rectangle(565, 445, 55, 25));
	btnImprimirEstadoDeuda.setToolTipText("Imprimr estado de deuda");

	btnImprimirEstadoDeuda.setOpaque(true);
	btnImprimirEstadoDeuda.setFont(new Font("Dialog", 1, 10));
	btnImprimirEstadoDeuda.setForeground(Color.black);
	btnImprimirEstadoDeuda.setBackground(new Color(255, 90, 33));
	btnImprimirEstadoDeuda.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

	btnImprimirEstadoDeuda.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnImprimirEstadoDeuda_actionPerformed(e);
		}
	    });
	rbtnLibreDeuda.setPreferredSize(new Dimension(95, 20));
	rbtnPagoContado.setText("Pago de Contado");
	rbtnPagoContado.setBounds(new Rectangle(220, 335, 75, 20));
	rbtnPagoContado.setPreferredSize(new Dimension(115, 20));
	rbtnPagoContado.setSize(new Dimension(115, 20));
	tfPorcentajeParcial.setBounds(new Rectangle(555, 335, 65, 35));
	tfPorcentajeParcial.setEditable(false);
	btnRegistrarPagoEdesa.setEnabled(false);
	btnRegistrarPagoEdesa.setOpaque(true);
	btnRegistrarPagoEdesa.setFont(new Font("Dialog", 1, 10));
	btnRegistrarPagoEdesa.setForeground(Color.black);
	btnRegistrarPagoEdesa.setBackground(new Color(165, 165, 0));
	btnRegistrarPagoEdesa.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnRegistrarPagoEdesa.setBounds(new Rectangle(115, 435, 90, 35));

	btnRegistrarPagoEdesa.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnRegistrarPagoEdesa_actionPerformed(e);
		}
	    });

	btnDesbloquearPagoEdesa.setOpaque(true);
	btnDesbloquearPagoEdesa.setFont(new Font("Dialog", 1, 10));
	btnDesbloquearPagoEdesa.setForeground(Color.black);
	btnDesbloquearPagoEdesa.setBackground(new Color(165, 165, 0));
	btnDesbloquearPagoEdesa.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnDesbloquearPagoEdesa.setBounds(new Rectangle(10, 435, 90, 35));

	btnDesbloquearPagoEdesa.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnDesbloquearPagoEdesa_actionPerformed(e);
		}
	    });
	btnRegistrarBoleta.setOpaque(true);
	btnRegistrarBoleta.setFont(new Font("Dialog", 1, 10));
	btnRegistrarBoleta.setForeground(Color.black);
	btnRegistrarBoleta.setBackground(new Color(165, 165, 0));
	btnRegistrarBoleta.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnRegistrarBoleta.setBounds(new Rectangle(10, 435, 60, 35));
	btnRegistrarBoleta.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnRegistrarBoleta_actionPerformed(e);
		}
	    });
      
        btnPagoContado.setVisible(false); //Anulado hasta reparar
	btnPagoContado.setOpaque(true);
	btnPagoContado.setFont(new Font("Dialog", 1, 10));
	btnPagoContado.setForeground(Color.black);
	btnPagoContado.setBackground(new Color(41, 165, 0));
	btnPagoContado.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnPagoContado.setBounds(new Rectangle(75, 435, 65, 35));
	btnPagoContado.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnPagoContado_actionPerformed(e);
		}
	    });

	btnPagoAnual.setOpaque(true);
	btnPagoAnual.setFont(new Font("Dialog", 1, 10));
	btnPagoAnual.setBackground(new Color(92, 0, 0));
	btnPagoAnual.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnPagoAnual.setBounds(new Rectangle(435, 435, 65, 35));
	btnPagoAnual.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnPagoAnual_actionPerformed(e);
		}
	    });

        btnPlanDePago.setVisible(false); //Anulado hasta reparar
        btnPlanDePago.setOpaque(true);
	btnPlanDePago.setFont(new Font("Dialog", 1, 10));
	btnPlanDePago.setForeground(Color.white);
	btnPlanDePago.setBackground(new Color(165, 41, 0));
	btnPlanDePago.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnPlanDePago.setBounds(new Rectangle(145, 435, 65, 35));
	btnPlanDePago.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnPlanDePago_actionPerformed(e);
		}
	    });
	btnLibreDeuda.setBounds(new Rectangle(215, 435, 65, 35));
	btnLibreDeuda.setOpaque(true);
	btnLibreDeuda.setFont(new Font("Dialog", 1, 10));
	btnLibreDeuda.setForeground(Color.white);
	btnLibreDeuda.setBackground(new Color(41, 41, 165));
	btnLibreDeuda.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnLibreDeuda.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnLibreDeuda_actionPerformed(e);
		}
	    });

	btnLibreDeudaConBaja.setBounds(new Rectangle(285, 435, 75, 35));
	btnLibreDeudaConBaja.setOpaque(true);
	btnLibreDeudaConBaja.setFont(new Font("Dialog", 1, 10));
	btnLibreDeudaConBaja.setForeground(Color.black);
	btnLibreDeudaConBaja.setBackground(new Color(41, 165, 165));
	btnLibreDeudaConBaja.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnLibreDeudaConBaja.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnLibreDeudaConBaja_actionPerformed(e);
		}
	    });

	btnLeyVeinteanial.setBounds(new Rectangle(365, 435, 65, 35));
	btnLeyVeinteanial.setOpaque(true);
	btnLeyVeinteanial.setFont(new Font("Dialog", 1, 10));
	btnLeyVeinteanial.setForeground(Color.white);
	btnLeyVeinteanial.setBackground(new Color(231, 115, 0));
	btnLeyVeinteanial.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnLeyVeinteanial.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnLeyVeinteanial_actionPerformed(e);
		}
	    });

	panelAyuda.add(taAyuda, null);
	panelAyuda.add(lblTitulo, null);
	panelAyuda.add(separador2, null);
	panelAyuda.add(btnDesbloquearPagoEdesa, null);
	panelAyuda.add(btnRegistrarPagoEdesa, null);
	panelCentral.add(btnLeyVeinteanial, null);
	panelCentral.add(btnLibreDeuda, null);
	panelCentral.add(btnLibreDeudaConBaja, null);
	panelCentral.add(btnPlanDePago, null);
	panelCentral.add(btnPagoContado, null);
	panelCentral.add(btnRegistrarBoleta, null);
	panelCentral.add(btnPagoAnual, null);
	panelCentral.add(tfPorcentajeParcial, null);
	panelCentral.add(rbtnPagoContado, null);
	panelCentral.add(rbtnLibreDeuda, null);
	panelCentral.add(btnImprimirEstadoDeuda, null);
	panelCentral.add(rbtnGenerarPP, null);
	panelCentral.add(rbtnRegistarBoleta, null);
	panelCentral.add(tfTotal, null);
        panelCentral.add(tfBonificacion, null);
        panelCentral.add(tfSaldo, null);
	panelCentral.add(tfInteres, null);
	panelCentral.add(tfMonto, null);
	panelCentral.add(separador1, null);
	panelCentral.add(cbCantidadAnticipos, null);
	panelCentral.add(tfEntrega, null);
	panelCentral.add(chkVerTodosAnticipos, null);
	panelCentral.add(grillaAnticiposAdeudados, null);
	panelSeleccionBonificacion.add(cbBonificaciones, null);
	panelCentral.add(panelSeleccionBonificacion, null);
	panelCentral.add(panelSeleccionImpuesto, null);
	panelSeleccionImpuesto.add(rbtnTgs, null);
	panelSeleccionImpuesto.add(rbtnInmob, null);
        panelSeleccionImpuesto.add(rbtnAutomotor, null);
        panelSeleccionImpuesto.add(rbtnComercio, null);
	this.add(panelCentral, null);
	this.add(panelAyuda, null);
	loadComboBonificacion();
	rbGroupTiposImpuesto.add(rbtnTgs);
	rbGroupTiposImpuesto.add(rbtnInmob);
        rbGroupTiposImpuesto.add(rbtnAutomotor);
        rbGroupTiposImpuesto.add(rbtnComercio);
	grupo2.add(rbtnRegistarBoleta);
	grupo2.add(rbtnGenerarPP);
	grupo2.add(rbtnLibreDeuda);
	grupo2.add(rbtnPagoContado);
	taAyuda.setEditable(false);
	rbtnRegistarBoleta.setVisible(false);
	rbtnGenerarPP.setVisible(false);
	rbtnPagoContado.setVisible(false);
	rbtnLibreDeuda.setVisible(false);
	grillaAnticiposAdeudados.setSortable(false);
	grillaAnticiposAdeudados.getTable().setEnabled(false);
	rbtnTgs.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    rbtnTgs_actionPerformed(e);
		}
	    });
	rbtnTgs.addChangeListener(new ChangeListener() {

		public void stateChanged(ChangeEvent e) {
		    rbtnTgs_stateChanged(e);
		}
	    });
	rbtnInmob.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    rbtnInmob_actionPerformed(e);
		}
	    });
	rbtnInmob.addChangeListener(new ChangeListener() {

		public void stateChanged(ChangeEvent e) {
		    rbtnInmob_stateChanged(e);
		}
	    });
	rbtnAutomotor.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    rbtnAutomotor_actionPerformed(e);
		}
	    });
	rbtnAutomotor.addChangeListener(new ChangeListener() {

		public void stateChanged(ChangeEvent e) {
		    rbtnAutomotor_stateChanged(e);
		}
	    });
        rbtnComercio.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    rbtnComercio_actionPerformed(e);
                }
            });
        rbtnComercio.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    rbtnComercio_stateChanged(e);
                }
            });	setHeaderList();
	grillaAnticiposAdeudados.setSortable(false);
	tfMonto.setEditable(false);
	tfEntrega.setEnabled(true);
	tfPorcentajeParcial.setEnabled(false);
	taAyuda.setText("* Seleccione el Tipo de Impuesto y la Bonificación.\n \n * Para ver los adelantos debe tildar la casilla \"Ver Adelantos\".\n \n * Seleccione uno de los Tipos de Pago.\n \n * Para volver a atrás presione el botón \"Atras\" o \"Inicio\".");
	taAyuda.setEditable(false);
    }

    private void rbtnTgs_actionPerformed(ActionEvent e) {
	coordinador.setVerPlanDePagoTgs(true);
	coordinador.setPaso(1);
	coordinador.setOpcion(0);
	coordinador.siguiente();
	/*coordinador.getImpuesto().setTipoDeImpuesto(coordinador.getImpuesto().TIPO_IMPUESTO_TGS);
	coordinador.setCabecera();
	refresh();*/
    }

    private void rbtnInmob_actionPerformed(ActionEvent e) {
	coordinador.getImpuesto().setTipoDeImpuesto(Impuesto.TIPO_IMPUESTO_INMOB);
	coordinador.setCabecera();

	if (coordinador.getBien().tienePlanDePagoInmob()) {
	    //Setear la opcion al coordinador
	    //Decirle al coordinador que avance
	    coordinador.setPaso(1);
	    coordinador.setOpcion(0);
	    coordinador.setVerPlanDePagoInmob(true);
	    coordinador.siguiente();
	} else {
	    loadComboBonificacion();
	    refresh();
	}
	rbtnAutomotor.setEnabled(false);
        rbtnComercio.setEnabled(false);
    }

    private void rbtnAutomotor_actionPerformed(ActionEvent e) {
        coordinador.getImpuesto().setTipoDeImpuesto(Impuesto.TIPO_IMPUESTO_AUTOMOTOR);
        coordinador.setCabecera();
        refresh();
    }

    private void rbtnComercio_actionPerformed(ActionEvent e) {
        coordinador.getImpuesto().setTipoDeImpuesto(Impuesto.TIPO_IMPUESTO_ACTVARIAS);
        coordinador.setCabecera();
        refresh();
    }

    public void iniciarPanel() {
	panelIniciado = false;
	bloquearPagoEdesa();
	loadComboBonificacion();
        switch (coordinador.getImpuesto().getTipoDeImpuesto()) {
            case Impuesto.TIPO_IMPUESTO_TGS:
                rbtnInmob.setEnabled(true);
                rbtnTgs.setEnabled(true);
                rbtnAutomotor.setEnabled(false);
                rbtnComercio.setEnabled(false);
                rbtnTgs.setSelected(true);
                break;
            case Impuesto.TIPO_IMPUESTO_INMOB:
                rbtnInmob.setEnabled(true);
                rbtnTgs.setEnabled(true);
                rbtnInmob.setSelected(true);
                break;
            case Impuesto.TIPO_IMPUESTO_AUTOMOTOR:
                rbtnInmob.setEnabled(false);
                rbtnTgs.setEnabled(false);
                rbtnComercio.setEnabled(false);
                rbtnAutomotor.setEnabled(true);
                rbtnAutomotor.setSelected(true);
                break;
            case Impuesto.TIPO_IMPUESTO_ACTVARIAS:
                rbtnInmob.setEnabled(false);
                rbtnTgs.setEnabled(false);
                rbtnAutomotor.setEnabled(false);
                rbtnComercio.setEnabled(true);
                rbtnComercio.setSelected(true);
            break;
        }
	refresh();
	panelIniciado = true;
    }

    private void setHeaderList() {
	headerList.removeAllElements();
	headerList.removeAllElements();
	headerList.addElement("*");
	headerList.addElement("Nº");
	headerList.addElement("Año");
	headerList.addElement("Fecha");
	headerList.addElement("Vence");
	headerList.addElement("$ Valor");
	headerList.addElement("% Mora");
	headerList.addElement("$ Mora");
	headerList.addElement("% Desc.");
	headerList.addElement("$ Desc.");
	headerList.addElement("Fº Act.");
	headerList.addElement("$ SubTotal");
	headerList.addElement("% Pagado");
	headerList.addElement("$ Total");
	headerList.addElement("*");
	headerList.addElement("*");

	grillaAnticiposAdeudados.getTable().addMouseListener(new MouseListener() {

		public void mouseClicked(MouseEvent e) {
		    if (e.getButton() == e.BUTTON1) {
		    }
		}

		public void mousePressed(MouseEvent mouseEvent) {
		}

		public void mouseReleased(MouseEvent mouseEvent) {
		}

		public void mouseEntered(MouseEvent mouseEvent) {
		}

		public void mouseExited(MouseEvent mouseEvent) {
		}

	    });
	grillaAnticiposAdeudados.getTable().addKeyListener(new KeyListener() {

		public void keyReleased(KeyEvent e) {
		    e.consume();
		    checkRows();
		}

		public void keyTyped(KeyEvent e) {
		    e.consume();
		    checkRows();
		}

		public void keyPressed(KeyEvent e) {
		    e.consume();
		    checkRows();
		}

	    });
	grillaAnticiposAdeudados.setParams("taxes.getcuotas", "-1,-1,false", headerList);
	chkVerTodosAnticipos.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    refresh();
		}
	    });
	chkVerTodosAnticipos.setSelected(false);
    }

    public void refresh() {
	cbCantidadAnticipos.setEnabled(false);
	String params = "" + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien() + "," + chkVerTodosAnticipos.isSelected();
	grillaAnticiposAdeudados.refresh(params);
        tfSaldo.setValue(LibSQL.getDouble("taxes.getsaldobybien", coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien()));
	clearFields();
    }

    public void clearFields() {
	tfEntrega.setValue(0.0);
	tfInteres.setValue(0.0);
	tfMonto.setValue(0.0);
	tfPorcentajeParcial.setValue(0.0);
        tfTotal.setValue(0.0);
	cbBonificaciones.setSelectedID(0);
	coordinador.getPagoAnticipos().setBonificacion(Integer.parseInt(cbBonificaciones.getSelectedValue().toString()));
    }

    private void checkRows() {
	for (int i = 0; i < grillaAnticiposAdeudados.getTable().getRowCount(); i++) {
	    grillaAnticiposAdeudados.getTable().setValueAt(false, i, 0);
	}
    }

    private void loadComboBonificacion() {
	cbBonificaciones.removeAllItems();
	cbBonificaciones.loadJCombo(LibSQL.exFunction("taxes.getAllBonificaciones", "" + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto()));
    }

    public void setComboAnticipos() {
	String params = coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien() + "," + chkVerTodosAnticipos.isSelected();
	int cantAnticipos = LibSQL.getInt("taxes.getCantAnticiposAPagar", "" + params);
	cbCantidadAnticipos.removeAllItems();
	if (cantAnticipos > 0) {
	    for (int i = 1; i <= cantAnticipos; i++) {
		cbCantidadAnticipos.getCombo().addItem(i);
	    }
	} else {
	    cbCantidadAnticipos.getCombo().addItem(0);
	}
	//calcTotalAmount();
	cbCantidadAnticipos.setEnabled(true);
    }

    private void calcTotalAmount() {
	if (grillaAnticiposAdeudados.hasItems()) {
	    if (tfEntrega.getAmount() == 0.0) {
		calcularTotalSinEntrega();
	    } else {
		//calcularTotalConEntrega();
		//Por ahora lo mismo
		calcularTotalSinEntrega();
	    }
	    coordinador.getPagoAnticipos().setBonificacion(Integer.parseInt(cbBonificaciones.getSelectedValue().toString()));
	    coordinador.getPagoAnticipos().setMonto(tfMonto.getAmount());
	    coordinador.getPagoAnticipos().setInteres(tfInteres.getAmount());
	    coordinador.getPagoAnticipos().setDescuento(tfBonificacion.getAmount());
	    coordinador.getPagoAnticipos().setMontoTotal((new BigDecimal("" + tfTotal.getAmount())).doubleValue());
	    /*
	    if(Double.parseDouble(cbBonificaciones.getSelectedValueRef().toString()) == 0){
		coordinador.getPagoAnticipos().setTotalAnticiposAdeudados(tfTotal.getAmount());// Cambiar para usar con bigdecimal
	    }else{
		coordinador.getPagoAnticipos().setTotalAnticiposAdeudados((tfTotal.getAmount()  * 100) / (100 * Double.parseDouble(cbBonificaciones.getSelectedValueRef().toString())));// Cambiar para usar con bigdecimal
	    }
            */
	    coordinador.getPagoAnticipos().setTotalAnticiposAdeudados(new BigDecimal(tfTotal.getAmount() + tfBonificacion.getAmount()).doubleValue()); // Cambiar para usar con bigdecimal
	    int periodoDesde = Integer.parseInt(grillaAnticiposAdeudados.getTable().getModel().getValueAt(0, 2).toString());
	    int periodoHasta = Integer.parseInt(grillaAnticiposAdeudados.getTable().getModel().getValueAt(Integer.parseInt(cbCantidadAnticipos.getSelectedItem().toString()) - 1, 2).toString());
	    int anioDesde = Integer.parseInt(grillaAnticiposAdeudados.getTable().getModel().getValueAt(0, 3).toString());
	    int anioHasta = Integer.parseInt(grillaAnticiposAdeudados.getTable().getModel().getValueAt(Integer.parseInt(cbCantidadAnticipos.getSelectedItem().toString()) - 1, 3).toString());
	    int cantidadPeriodos = Integer.parseInt(cbCantidadAnticipos.getSelectedItem().toString());
	    int periodoDesdePuro = periodoDesde;
	    int periodoHastaPuro = periodoHasta;
            
            /*
	    int periodos = LibSQL.getInt("taxes.getconfigperiodosautomotor","");

	    if (coordinador.getBien().esAutomotor()) {
		periodoDesdePuro = (periodoDesdePuro * periodos);
		periodoHastaPuro = (periodoHastaPuro * periodos);
	    }*/
	    coordinador.getPagoAnticipos().setCantidadAnticipos(cantidadPeriodos);
	    coordinador.getPagoAnticipos().setPeriodoDesde(periodoDesdePuro);
	    coordinador.getPagoAnticipos().setPeriodoHasta(periodoHastaPuro);
	    coordinador.getPagoAnticipos().setAnioDesde(anioDesde);
	    coordinador.getPagoAnticipos().setAnioHasta(anioHasta);
	} else {
	    coordinador.getPagoAnticipos().clear();
	}
    }

    private void calcularTotalSinEntrega() {
	/*
	if (cbCantidadAnticipos.getSelectedIndex() >= 0) {
	    int cantidadAnticiposAPagar = Integer.parseInt(cbCantidadAnticipos.getSelectedItem().toString());
	    BigDecimal totalIntereses = new BigDecimal("0");
	    BigDecimal totalValor = new BigDecimal("0");
	    BigDecimal totalDescuento = new BigDecimal("0");
	    BigDecimal subTotalDeuda = new BigDecimal("0");
	    BigDecimal porcentajeDescuento = new BigDecimal("0");
	    BigDecimal total = new BigDecimal("0");
	    BigDecimal auxValorTotal = new BigDecimal("0");
	    grillaAnticiposAdeudados.selectAllItems(true);
	    porcentajeDescuento = porcentajeDescuento.add(Format.toDouble(cbBonificaciones.getSelectedValueRef().toString()));
	    if (grillaAnticiposAdeudados.getTable().getRowCount() > 0) {
		Vector valorCuota = grillaAnticiposAdeudados.getValuesInTableAt("Valor");
		Vector valorIntereses = grillaAnticiposAdeudados.getValuesInTableAt("$ Mora");
		Vector porcentajePagado = grillaAnticiposAdeudados.getValuesInTableAt("% Pagado");
		Vector valorTotal = grillaAnticiposAdeudados.getValuesInTableAt("$ Total");
		for (int i = 0; i < cantidadAnticiposAPagar; i++) {
		    totalValor = totalValor.add(Format.toDouble(new Double(valorCuota.elementAt(i).toString()) * (1 - new Double((porcentajePagado.elementAt(i).toString())) / 100)));
		    totalIntereses = totalIntereses.add(Format.toDouble(new Double(valorIntereses.elementAt(i).toString()) * (1 - new Double((porcentajePagado.elementAt(i).toString())) / 100)));
		    subTotalDeuda = subTotalDeuda.add(new BigDecimal(valorTotal.elementAt(i).toString()));
		}
	    }
	    tfMonto.setValue(totalValor);
	    tfInteres.setValue(totalIntereses);
	    totalDescuento = Format.toDouble(totalValor.add(totalIntereses).doubleValue() * porcentajeDescuento.doubleValue());
            totalValor = totalValor.add(totalIntereses);
            total = Format.toDouble(totalValor.doubleValue() * (1 - porcentajeDescuento.doubleValue()));
	    tfBonificacion.setValue(totalValor.subtract(total));
	    tfTotal.setValue(total);

	}
        */
	int cantidadAnticiposAPagar = 0;
	if (grillaAnticiposAdeudados.hasItems()) {
	    cantidadAnticiposAPagar = grillaAnticiposAdeudados.getItemCount();
	}

	String params = coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien() + "," + cbCantidadAnticipos.getSelectedItem() + "," + Double.parseDouble(cbBonificaciones.getSelectedValueRef().toString());
	ResultSet rs = LibSQL.exFunction("taxes.getMontosDeuda", params);
	try {
	    while (rs.next()) {
		tfMonto.setValue(rs.getDouble("montoPuro"));
		tfInteres.setValue(rs.getDouble("interesPuro"));
		tfTotal.setValue(rs.getDouble("totalDescontado"));
		tfBonificacion.setValue(rs.getDouble("Descuento"));
	    }
	} catch (SQLException e) {
	    tfMonto.setValue(0.00);
	    tfInteres.setValue(0.00);
	    tfTotal.setValue(0.00);
	    tfBonificacion.setValue(0.00);
	}
    }

    private void calcularTotalConEntrega() {

    }

    private void cbBonificaciones_itemStateChanged(ItemEvent e) {
	if (e.getStateChange() == ItemEvent.SELECTED) {
	    coordinador.getBonificacion().setIdBonificacion(Integer.parseInt(cbBonificaciones.getSelectedValue().toString()));
	    coordinador.getBonificacion().retrieveData();
	    calcTotalAmount();
	}
    }

    private void btnRegistrarBoleta_actionPerformed(ActionEvent e) {
        if (btnRegistrarPagoEdesa.isEnabled()) {
            if (grillaAnticiposAdeudados.getSelectedsVector().size() == 1) {
                //Dar pagado el anticipo seleccionado con el asiento y toda la bola
                Vector _anticipo = (Vector)grillaAnticiposAdeudados.getSelectedsVector().elementAt(0);

                PagoAnticipos pa = coordinador.getPagoAnticipos();

                pa.setAnioDesde(Integer.parseInt(_anticipo.elementAt(2).toString()));
                pa.setAnioHasta(Integer.parseInt(_anticipo.elementAt(2).toString()));
                pa.setPeriodoDesde(Integer.parseInt(_anticipo.elementAt(1).toString()));
                pa.setPeriodoHasta(Integer.parseInt(_anticipo.elementAt(1).toString()));

/*******************************/
                
                pa.setBonificacion(Integer.parseInt(cbBonificaciones.getSelectedValue().toString()));
                pa.setMonto(Double.parseDouble(_anticipo.elementAt(5).toString()));
                pa.setInteres(Double.parseDouble(_anticipo.elementAt(7).toString()));
                pa.setDescuento(pa.getBonificacion().getPorcentaje()*Double.parseDouble(_anticipo.elementAt(13).toString()));
                pa.setCantidadAnticipos(1);
                pa.setTotalAnticiposAdeudados(Double.parseDouble(_anticipo.elementAt(13).toString()));
                pa.setMontoTotal(Double.parseDouble(_anticipo.elementAt(13).toString())-pa.getDescuento());

/***********************************/
                System.out.println(_anticipo);
                registrarBoleta();
            } else {
                Advisor.messageBox("Debe seleccionar uno y sólo un anticipo", "Error");
            }
        } else {
            registrarBoleta();
        }
    }

    private void registrarBoleta() {
	//if (Double.parseDouble(tfTotal.getValue().toString()) > 0.0) {
	if (grillaAnticiposAdeudados.getTable().getRowCount() > 0) {
	    coordinador.setSubOpcion(Coordinador.SUBOPCION_REGISTRAR_BOLETA);
	    coordinador.siguiente();
	} else {
	    Advisor.messageBox("No se indicaron anticipos a pagar", "Aviso");
	}
	/*
        } else {
            Advisor.messageBox("No se puede generar una boleta por un monto total de $ "+ tfTotal.getValue(), "Error");
        }
        */
    }

    private void btnPagoContado_actionPerformed(ActionEvent e) {
	registrarPagoContado();
    }

    private void registrarPagoContado() {
	if (coordinador.getBien().puedeHacerPagoContado(coordinador.getImpuesto())) {
	    String params = "" + coordinador.getBien().getIdBien() + "," + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBonificacion().getPorcentaje() + "," + false;
	    double deudaActual = LibSQL.getDouble("taxes.getTotalDeuda", params);
	    if (deudaActual > 0.00) {
		cargarPagoAnticipo();
		if (coordinador.getPagoAnticipos().getMontoTotal() > 0.0) {
		    coordinador.setPaso(3);
		    coordinador.setOpcion(3);
		    coordinador.setSubOpcion(Coordinador.SUBOPCION_PAGO_CONTADO);
		    coordinador.siguiente();
		} else {
		    Advisor.messageBox("No se puede generar un pago de contado a una deuda de $ 0,00", "Error");
		}
	    } else {
		Advisor.messageBox("No se puede generar un pago de contado a una deuda de $ 0,00", "Error");
	    }
	    /* Ya no se hace un plan de pago por una cuota
        coordinador.getConfiguracionPlanDePago().setBonificacion(coordinador.getBonificacion());
        coordinador.getConfiguracionPlanDePago().setPagoContado(true);
        */
	} else {
	    Advisor.messageBox("No puede hacer pago de contado en esta época del año.", "Aviso");
	}
    }

    private void btnPagoAnual_actionPerformed(ActionEvent e) {
	registrarPagoAnual();
    }

    private void registrarPagoAnual() {
	if (coordinador.getBien().puedeHacerPagoAnual(coordinador.getImpuesto())) {
	    String params = "" + coordinador.getBien().getIdBien() + "," + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBonificacion().getPorcentaje() + "," + true;
	    double deudaActual = LibSQL.getDouble("taxes.getTotalDeuda", params);
	    if (deudaActual > 0.00) {
		cargarPagoAnticipo();
		if (coordinador.getPagoAnticipos().getMontoTotal() > 0.0) {
		    coordinador.setPaso(3);
		    coordinador.setOpcion(3);
		    coordinador.setSubOpcion(Coordinador.SUBOPCION_PAGO_ANUAL);
		    coordinador.siguiente();
		} else {
		    Advisor.messageBox("No se puede generar un pago anual a una deuda de $ 0,00", "Error");
		}
	    } else {
		Advisor.messageBox("No se puede generar un pago anual a una deuda de $ 0,00", "Error");
	    }
	} else {
	    Advisor.messageBox("El pago anual sólo se puede hacer en una determinada época del año\nPara ello además debe adeudar sólo anticipos del año actual", "Aviso");
	}
    }

    private void cargarPagoAnticipo() {
	//se debe cargar la configuracion de pago de contado
	//para completar
	coordinador.clearPagoAnticipo();
	coordinador.getTipoPlanDePago().setIdTipoImpuesto(coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto());
	coordinador.getTipoPlanDePago().retrieveData2();
	coordinador.getPagoContado().retrieveData(coordinador.getTipoPlanDePago(), coordinador.getBien(), coordinador.getBonificacion(), coordinador.getImpuesto().getTipoImpuesto());

	coordinador.getPagoAnticipos().setBonificacion(coordinador.getBonificacion().getIdBonificacion());
	coordinador.getPagoAnticipos().setMonto(coordinador.getPagoContado().getCapitalPuro());
	coordinador.getPagoAnticipos().setInteres(coordinador.getPagoContado().getInteresPuro());
	coordinador.getPagoAnticipos().setDescuento(coordinador.getPagoContado().getDescuento());
	coordinador.getPagoAnticipos().setMontoTotal(coordinador.getPagoContado().getTotal());

	if (coordinador.getBonificacion().getPorcentaje() == 0) {
	    coordinador.getPagoAnticipos().setTotalAnticiposAdeudados(coordinador.getPagoContado().getTotal()); // Cambiar para usar con bigdecimal
	} else {
	    coordinador.getPagoAnticipos().setTotalAnticiposAdeudados(coordinador.getPagoContado().getTotal()); // Cambiar para usar con bigdecimal
	}

	//corregir la forma en que se pasan los periodos en caso de ser automotor
	int periodoDesde = coordinador.getPagoContado().getPeriodoDesdePuro();
	int periodoHasta = coordinador.getPagoContado().getPeriodoHastaPuro();
	int anioDesde = coordinador.getPagoContado().getAnioDesde();
	int anioHasta = coordinador.getPagoContado().getAnioHasta();
	coordinador.getPagoAnticipos().setCantidadAnticipos(coordinador.getPagoContado().getCantidadPeriodos());
	coordinador.getPagoAnticipos().setPeriodoDesde(periodoDesde);
	coordinador.getPagoAnticipos().setPeriodoHasta(periodoHasta);
	coordinador.getPagoAnticipos().setAnioDesde(anioDesde);
	coordinador.getPagoAnticipos().setAnioHasta(anioHasta);
	if (coordinador.getBien().puedeHacerPagoAnual(coordinador.getImpuesto())) {
	    cargarPagoAnual();
	}
    }

    private void cargarPagoAnual() {
	coordinador.clearPagoAnticipo();
	//coordinador.getTipoPlanDePago().setIdTipoImpuesto(coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto());
	//coordinador.getTipoPlanDePago().retrieveData2();
	coordinador.getPagoAnual().retrieveData(coordinador.getBien(), coordinador.getBonificacion(), coordinador.getImpuesto().getTipoImpuesto());

	coordinador.getPagoAnticipos().setBonificacion(coordinador.getBonificacion().getIdBonificacion());
	coordinador.getPagoAnticipos().setMonto(coordinador.getPagoAnual().getCapitalPuro());
	coordinador.getPagoAnticipos().setInteres(coordinador.getPagoAnual().getInteresPuro());
	coordinador.getPagoAnticipos().setDescuento(coordinador.getPagoAnual().getDescuento());
	coordinador.getPagoAnticipos().setMontoTotal(coordinador.getPagoAnual().getTotal());

	if (coordinador.getBonificacion().getPorcentaje() == 0) {
	    coordinador.getPagoAnticipos().setTotalAnticiposAdeudados(coordinador.getPagoAnual().getTotal()); // Cambiar para usar con bigdecimal
	} else {
	    coordinador.getPagoAnticipos().setTotalAnticiposAdeudados(coordinador.getPagoAnual().getTotal()); // Cambiar para usar con bigdecimal
	}

	//corregir la forma en que se pasan los periodos en caso de ser automotor
	int periodoDesde = coordinador.getPagoAnual().getPeriodoDesdePuro();
	int periodoHasta = coordinador.getPagoAnual().getPeriodoHastaPuro();
	int anioDesde = coordinador.getPagoAnual().getAnioDesde();
	int anioHasta = coordinador.getPagoAnual().getAnioHasta();
	coordinador.getPagoAnticipos().setCantidadAnticipos(coordinador.getPagoAnual().getCantidadPeriodos());
	coordinador.getPagoAnticipos().setPeriodoDesde(periodoDesde);
	coordinador.getPagoAnticipos().setPeriodoHasta(periodoHasta);
	coordinador.getPagoAnticipos().setAnioDesde(anioDesde);
	coordinador.getPagoAnticipos().setAnioHasta(anioHasta);
    }

    private void btnPlanDePago_actionPerformed(ActionEvent e) {
        configurarPlanDePago();
    }

    private void btnLibreDeuda_actionPerformed(ActionEvent e) {
	generarLibreDeuda(false);
    }

    private void btnLibreDeudaConBaja_actionPerformed(ActionEvent e) {
	generarLibreDeuda(true);
    }

    private void generarLibreDeuda(boolean _conBaja) {
	if (LibSQL.getBoolean("taxes.getLibreDeuda", "" + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien())) {
	    if (_conBaja) {
		if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 3) {
		    if (Advisor.question("Libre Deuda CON BAJA", "¿Está seguro de generar el Certificado de Libre Deuda CON BAJA?")) {
			if (coordinador.getBien().getAutomotor().getIdTipoAutomotor() == 4) { //ES MOTOCICLETA
			    generarCertificado(132, 3, "Libre Deuda con Baja"); //AUTOMOTOR - Libre Deuda con Baja para Motos
			} else {
			    generarCertificado(131, 3, "Libre Deuda con Baja"); //AUTOMOTOR - Libre Deuda con Baja para Vehículos
			}
		    }
		} else {
		    Advisor.messageBox("No se puede generar Libre Deuda CON BAJA a un catastro", "Error");
		}
	    } else {
		if (Advisor.question("Libre Deuda", "¿Está seguro de generar el Certificado de Libre Deuda?")) {
		    switch (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto()) {
			case 1:
			    generarCertificado(3, 1, "Libre Deuda"); //CATASTRO - Tasa General de Servicios (T.G.S.)
			    break;
			case 2:
			    generarCertificado(4, 2, "Libre Deuda"); //CATASTRO - Impuesto Inmobiliario
			    break;
			case 3:
			    generarCertificado(1, 3, "Libre Deuda"); //AUTOMOTOR - Impuesto Automotor
			    break;
		    }
		}
	    }
	} else {
	    Advisor.messageBox("<html><p align=center>No se puede generar el Certificado de Libre Deuda<br>" + "Verifique que no tenga saldo pendiente de pago</p></html>", "Libre Deuda");
	}
    }

    /*private void generarLibreDeuda(int _idAlicuotaContribucion, int _idTipoImpuesto) {
	AlicuotaContribucion alicuotaContribucion = new AlicuotaContribucion();
	alicuotaContribucion.setIdalicuotacontribucion(_idAlicuotaContribucion);
	BoletaContribucion boletaContribucion = new BoletaContribucion();
	boletaContribucion.setConcepto(alicuotaContribucion.getNombre());
	boletaContribucion.setIdBien(coordinador.getBien().getIdBien());
	if (_idTipoImpuesto == 1 || _idTipoImpuesto == 2) {
	    boletaContribucion.setContribuyente(coordinador.getBien().getCatastro().getDomape());
	    boletaContribucion.setNroBien("" + coordinador.getBien().getCatastro().getCatastro());
	    boletaContribucion.setTipoBien("CATASTRO");
	} else {
	    boletaContribucion.setContribuyente(coordinador.getBien().getAutomotor().getTitular());
	    boletaContribucion.setNroBien(coordinador.getBien().getAutomotor().getDominio());
	    boletaContribucion.setTipoBien(coordinador.getBien().getAutomotor().getTipo());
	}
	boletaContribucion.setIdTipoImpuesto(_idTipoImpuesto);
	boletaContribucion.setIdAlicuotaContribucion(alicuotaContribucion.getIdalicuotacontribucion());
	if (boletaContribucion.saveData() > 0) {
	    boletaContribucion.retrieveData();
	    Advisor.messageBox("Se generó correctamente el Certificado de Libre Deuda (Boleta Nº "
			       + boletaContribucion.getAnio() + "-" + boletaContribucion.getNumeroFormateado() + ")", "Aviso");
	} else {
	    Advisor.messageBox("Ha ocurrido un error al intentar generar el Certificado de Libre Deuda", "Error");
	}
    }*/

    private void configurarPlanDePago() {
	String params = "" + coordinador.getBien().getIdBien() + "," + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBonificacion().getPorcentaje() + "," + false;
	double deudaActual = LibSQL.getDouble("taxes.getTotalDeuda", params);
	if (deudaActual > 0.00) {
	    coordinador.getTipoPlanDePago().setIdTipoImpuesto(coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto());
	    coordinador.getTipoPlanDePago().retrieveData2();
	    //if( Proced.compareDates(coordinador.getTipoPlanDePago().getFechaFinInscripcion(),Environment.currentDate) == 1){ // Ver mas adelante ? Preguntar?
	    boolean adeudaAnticipos = coordinador.getBien().adeudaAnticipos(coordinador.getImpuesto().getTipoDeImpuesto());
	    boolean tienePlanDePagoActivo = coordinador.getBien().tienePlanDePagoActivo(coordinador.getImpuesto().getTipoDeImpuesto());
	    if ((adeudaAnticipos) && (!tienePlanDePagoActivo)) {
		//ir a configurar el plan de pago
		coordinador.getConfiguracionPlanDePago().setPagoAnticipado(Double.parseDouble(tfEntrega.getValue().toString()));
		coordinador.getTipoPlanDePago().setIdTipoImpuesto(coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto());
		coordinador.getTipoPlanDePago().retrieveData2();
		coordinador.getConfiguracionPlanDePago().setBonificacion(coordinador.getBonificacion());
		coordinador.getConfiguracionPlanDePago().setPagoContado(false);
		coordinador.setOpcion(3);
		coordinador.setSubOpcion(Coordinador.SUBOPCION_PLAN_DE_PAGO);
		coordinador.siguiente();
	    } else {
		if (!adeudaAnticipos) {
		    Advisor.messageBox("No se puede configurar un plan de pago porque no hay deuda.", "Aviso");
		} else {
		    Advisor.messageBox("El bien ya posee un plan de pago para impuesto " + coordinador.getImpuesto().getTipoImpuesto().getNombre(), "Aviso");
		}
	    }
	} else {
	    Advisor.messageBox("No se puede configurar un plan de pago porque no hay deuda.", "Aviso");
	}
    }

    private void btnImprimirEstadoDeuda_actionPerformed(ActionEvent e) {
	imprimirEstadoDeuda();
    }

    private void imprimirEstadoDeuda() {
	if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() != 4) {
	    int anticipoDesde = 1;
	    int anioDesde = 2000;
	    int anticipoHasta = 12;
	    int anioHasta = 2009;
	    int cantAnticipos = 1;
	    String bonificacion = "";
	    double montoDescuento = 0.0;
	    double totalInteres = 0.0;
	    double deudaParcial = 0.0;
	    double subtotal = 0.0;
	    double totalDeuda = 0.0;

	    if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 1 || coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 2) { /** Caso Impuesto TGS o Inmobiliario */

		BasicReport report = new BasicReport(PlansOfPaymentMgmt.class.getResource("xml/EstadoDeCuentaCatastral.xml"));
		ResultSet result = LibSQL.exFunction("taxes.xmlGetEstadoDeCuenta", "" + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien() + "," + cbBonificaciones.getSelectedValue());
		try {
		    if (result.next()) {
			anticipoDesde = result.getInt("anticipodesde");
			anioDesde = result.getInt("aniodesde");
			anticipoHasta = result.getInt("anticipohasta");
			anioHasta = result.getInt("aniohasta");
			cantAnticipos = result.getInt("cantanticipos");
			bonificacion = result.getString("bonificacion");
			montoDescuento = result.getDouble("totaldescuento");
			totalInteres = result.getDouble("totalinteres");
			deudaParcial = result.getDouble("deudaparcial");
			subtotal = result.getDouble("subtotal");
			totalDeuda = result.getDouble("totaldeuda");
		    }

		    /** Datos Encabezado */
		    if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 1) {
			report.setProperty("impuesto", "TASA GENERAL DE SERVICIOS");
		    } else if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 2) {
			report.setProperty("impuesto", "IMPUESTO INMOBILIARIO");
		    } else {
			report.setProperty("impuesto", "");
		    }

		    /** Datos del Catastro */
		    coordinador.getBien().getCatastro().retrieveApoderadoData();
		    report.setProperty("cuentacorriente", coordinador.getBien().getCatastro().getNroCuenta());
		    report.setProperty("titulobien", "Catastro:");
		    report.setProperty("titulovalorfiscal", "Val. Fisc.:");
		    report.setProperty("valorfiscal", Double.parseDouble(coordinador.getBien().getCatastro().getValfis()));
		    report.setProperty("bien", coordinador.getBien().getCatastro().getCatastro());
		    report.setProperty("apoderado", coordinador.getBien().getCatastro().getApoderadoLastName() + " " + coordinador.getBien().getCatastro().getApoderadoName());
		    report.setProperty("domicilio", coordinador.getBien().getCatastro().getDcalle() + " Nº " + coordinador.getBien().getCatastro().getDnumro());

		    /** Datos del Resumen */
		    if (cantAnticipos > 0) {
			report.setProperty("anticipos", anticipoDesde + "/" + anioDesde + " - " + anticipoHasta + "/" + anioHasta);
		    } else {
			report.setProperty("anticipos", "No adeuda períodos");
		    }
		    report.setProperty("bonificacion", bonificacion);
		    report.setProperty("montoBonificacion", montoDescuento);
		    report.setProperty("totalInteres", totalInteres);
		    report.setProperty("cantAnticipos", cantAnticipos);
		    report.setProperty("deudaParcial", deudaParcial);
		    report.setProperty("totalDeuda", totalDeuda);
		    report.setProperty("totaldeudatext", "( Son Pesos " + Format.NumberToText.numberToText(totalDeuda) + " .-)");
		    report.setProperty("subtotal", subtotal);

		    /** Datos del Detalle */
		    report.setProperty("contribuyente", coordinador.getBien().getCatastro().getDomape() + " " + coordinador.getBien().getCatastro().getDomapeAux());
		    report.addTableModel("taxes.xmlGetDetalleEstadoDeCuenta", "" + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien() + "," + cbBonificaciones.getSelectedValue());
		    report.doReport();

		} catch (SQLException x) {
		    // TODO
		    x.printStackTrace();
		}

	    } else if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 3) {

		BasicReport report = new BasicReport(PlansOfPaymentMgmt.class.getResource("xml/EstadoDeCuentaAutomotor.xml"));
		ResultSet result = LibSQL.exFunction("taxes.xmlGetEstadoDeCuenta", "" + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien() + "," + cbBonificaciones.getSelectedValue());
		try {
		    if (result.next()) {
			anticipoDesde = result.getInt("anticipodesde");
			anioDesde = result.getInt("aniodesde");
			anticipoHasta = result.getInt("anticipohasta");
			anioHasta = result.getInt("aniohasta");
			cantAnticipos = result.getInt("cantanticipos");
			bonificacion = result.getString("bonificacion");
			montoDescuento = result.getDouble("totaldescuento");
			totalInteres = result.getDouble("totalinteres");
			deudaParcial = result.getDouble("deudaparcial");
			subtotal = result.getDouble("subtotal");
			totalDeuda = result.getDouble("totaldeuda");
		    }

		    /** Datos Encabezado */
		    report.setProperty("impuesto", "IMPUESTO AUTOMOTOR");

		    /** Datos del Dominio */
		    report.setProperty("cuentacorriente", "-");
		    report.setProperty("titulobien", "Dominio:");
		    report.setProperty("bien", coordinador.getBien().getAutomotor().getDominio());
		    report.setProperty("apoderado", "-");
		    report.setProperty("domicilio", coordinador.getBien().getAutomotor().getDomicilio());

		    /** Datos del Resumen */
		    if (cantAnticipos > 0) {
			report.setProperty("anticipos", anticipoDesde + "/" + anioDesde + " - " + anticipoHasta + "/" + anioHasta);
		    } else {
			report.setProperty("anticipos", "No adeuda períodos");
		    }
		    report.setProperty("bonificacion", bonificacion);
		    report.setProperty("montoBonificacion", montoDescuento);
		    report.setProperty("totalInteres", totalInteres);
		    report.setProperty("cantAnticipos", cantAnticipos);
		    report.setProperty("deudaParcial", deudaParcial);
		    report.setProperty("totalDeuda", totalDeuda);
		    report.setProperty("totaldeudatext", "( Son Pesos " + Format.NumberToText.numberToText(totalDeuda) + " .-)");
		    report.setProperty("subtotal", subtotal);
		    report.setProperty("contribuyente", coordinador.getBien().getApoderado());

		    /** Datos del Detalle */
		    report.addTableModel("taxes.xmlGetDetalleEstadoDeCuenta", "" + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien() + "," + cbBonificaciones.getSelectedValue());
		    report.doReport();
		} catch (SQLException x) {
		    // TODO
		    x.printStackTrace();
		}
	    } else {
		Advisor.messageBox("Funcionalidad no disponible", "Mensaje");
	    }
	} else {
	    Advisor.messageBox("Funcionalidad no disponible", "Mensaje");
	}
    }

    private void rbtnTgs_stateChanged(ChangeEvent e) {
	setOpciones();
    }

    private void setOpciones() {
	rbtnTgs.setEnabled(!rbtnAutomotor.isSelected() && !rbtnComercio.isSelected());
	rbtnInmob.setEnabled(!rbtnAutomotor.isSelected() && !rbtnComercio.isSelected());
        rbtnAutomotor.setEnabled(rbtnAutomotor.isSelected());
        rbtnComercio.setEnabled(rbtnComercio.isSelected());
    }

    private void rbtnInmob_stateChanged(ChangeEvent e) {
	setOpciones();
    }

    private void rbtnAutomotor_stateChanged(ChangeEvent e) {
        setOpciones();
    }
    
    private void rbtnComercio_stateChanged(ChangeEvent e) {
        setOpciones();
    }

    private void btnLeyVeinteanial_actionPerformed(ActionEvent e) {
	if (LibSQL.getBoolean("taxes.getLibreDeuda", "" + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() + "," + coordinador.getBien().getIdBien())) {
	    if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 3) {
		if (coordinador.getBien().getAutomotor().getModelo() > 1900) {
		    int _antiguedad = Integer.parseInt(Environment.currentYear) - coordinador.getBien().getAutomotor().getModelo();
		    if (_antiguedad > 19) {
			if (Advisor.question("Ley Veinteañal", "¿Está seguro de generar el Certificado de Ley Veinteañal?")) {
			    generarCertificado(133, 3, "Ley Veinteañal"); //AUTOMOTOR - Ley Veinteñal
			}
		    } else {
			Advisor.messageBox("El vehículo debe tener 20 o más años de antigüedad\nEste vehículo tiene sólo " + (_antiguedad != 1?_antiguedad + " años":_antiguedad + " año"), "Error");
		    }
		} else {
		    Advisor.messageBox("El modelo del vehículo es incorrecto (Año " + coordinador.getBien().getAutomotor().getModelo() + "), verifíquelo y vuelva a intentarlo", "Error");
		}
	    } else {
		Advisor.messageBox("No se puede generar un certificado por Ley Veinteañal a un catastro", "Error");
	    }
	} else {
	    Advisor.messageBox("<html><p align=center>No se puede generar el Certificado de Ley Veinteañal<br>" + "Verifique que no tenga saldo pendiente de pago</p></html>", "Ley Veinteañal");
	}
    }

    private void generarCertificado(int _idAlicuotaContribucion, int _idTipoImpuesto, String _tipoCertificado) {
	AlicuotaContribucion alicuotaContribucion = new AlicuotaContribucion();
	alicuotaContribucion.setIdalicuotacontribucion(_idAlicuotaContribucion);
	BoletaContribucion boletaContribucion = new BoletaContribucion();
	boletaContribucion.setConcepto(alicuotaContribucion.getNombre());
	boletaContribucion.setIdBien(coordinador.getBien().getIdBien());
	if (_idTipoImpuesto == 1 || _idTipoImpuesto == 2) {
	    boletaContribucion.setContribuyente(coordinador.getBien().getCatastro().getDomape());
	    boletaContribucion.setNroBien("" + coordinador.getBien().getCatastro().getCatastro());
	    boletaContribucion.setTipoBien("CATASTRO");
	} else {
	    boletaContribucion.setContribuyente(coordinador.getBien().getAutomotor().getTitular());
	    boletaContribucion.setNroBien(coordinador.getBien().getAutomotor().getDominio());
	    boletaContribucion.setTipoBien(coordinador.getBien().getAutomotor().getTipo());
	}
	boletaContribucion.setIdTipoImpuesto(_idTipoImpuesto);
	boletaContribucion.setIdAlicuotaContribucion(alicuotaContribucion.getIdalicuotacontribucion());
	if (boletaContribucion.saveData() > 0) {
	    boletaContribucion.retrieveData();
	    Advisor.messageBox("Se generó correctamente el Certificado por " + _tipoCertificado + " (Boleta Nº " + boletaContribucion.getAnio() + "-" + boletaContribucion.getNumeroFormateado() + ")", "Aviso");
	} else {
	    Advisor.messageBox("Ha ocurrido un error al intentar generar el Certificado por " + _tipoCertificado, "Error");
	}
    }

    private void btnRegistrarPagoEdesa_actionPerformed(ActionEvent e) {
	if (grillaAnticiposAdeudados.getSelectedsVector().size() == 1) {
	    //Dar pagado el anticipo seleccionado con el asiento y toda la bola
	    Vector _anticipo = (Vector)grillaAnticiposAdeudados.getSelectedsVector().elementAt(0);
	    if (Advisor.question("Registrar pago vía EDESA", "¿Está seguro de dar como pagado por EDESA?\nAnticipo: " + _anticipo.elementAt(1) + "/" + _anticipo.elementAt(2)
				     + "\nMonto: $ " + _anticipo.elementAt(5) + "\nCatastro: " + coordinador.getBien().getCatastro().getCatastro())) 
		{
		    bloquearPagoEdesa();
		    if (LibSQL.getBoolean("cashier.setpagoedesa", coordinador.getBien().getCatastro().getIdCatastro() + "," + _anticipo.elementAt(1) + "," + _anticipo.elementAt(2) + "," + _anticipo.elementAt(5) + "," + coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto()
			/*"idcatastro, anticipo, anio, monto, tipoimpuesto!!!"*/)) {
		        Advisor.messageBox("El pago ha sido registrado", "Pago por EDESA");
			refresh();
		    } else {
		        Advisor.messageBox("Ha ocurrido un error al intentar registrar el pago", "Error");
		    }
		}
	} else {
	    Advisor.messageBox("Debe seleccionar uno y sólo un anticipo", "Error");
	}
    }

    private void btnDesbloquearPagoEdesa_actionPerformed(ActionEvent e) {
	if (!btnRegistrarPagoEdesa.isEnabled()) { //Activo EDESA
	    desbloquearPagoEdesa();
	} else { //Desactivo EDESA
	    bloquearPagoEdesa();
	}
    }
    
    private void desbloquearPagoEdesa() {
	Advisor.messageBox("Seleccione UN anticipo de la tabla central (tilde la primer columna)", "Registrar pago por EDESA");
	btnRegistrarPagoEdesa.setEnabled(true);
	btnRegistrarPagoEdesa.setBackground(new Color(255, 0, 0));
	grillaAnticiposAdeudados.setEnabled(true);
	grillaAnticiposAdeudados.getTable().setEnabled(true);
	btnDesbloquearPagoEdesa.setText("BLOQUEAR Pago EDESA");
	btnDesbloquearPagoEdesa.setBackground(new Color(0, 200, 0));
    }

    private void bloquearPagoEdesa() {
	btnRegistrarPagoEdesa.setEnabled(false);
	btnRegistrarPagoEdesa.setBackground(new Color(165, 165, 0));
	grillaAnticiposAdeudados.setEnabled(false);
	grillaAnticiposAdeudados.getTable().setEnabled(false);
	btnDesbloquearPagoEdesa.setText("DESBLOQUEAR Pago EDESA");
	btnDesbloquearPagoEdesa.setBackground(new Color(255, 0, 0));
    }

}



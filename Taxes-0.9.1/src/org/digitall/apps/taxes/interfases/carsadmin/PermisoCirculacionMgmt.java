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
 * PermisoCirculacionMgmt.java
 *
 * */
 package org.digitall.apps.taxes.interfases.carsadmin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.digitall.apps.taxes.classes.AlicuotaContribucion;
import org.digitall.apps.taxes.classes.BoletaContribucion;
import org.digitall.apps.taxes.classes.Certificado;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.common.OrganizationInfo;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

public class PermisoCirculacionMgmt extends BasicPrimitivePanel {

    private BasicPanel content = new BasicPanel("");

    private CloseButton btnCerrar = new CloseButton();
    private SaveDataButton btnAceptar = new SaveDataButton();

    private TFInput tfDominio = new TFInput(DataTypes.STRING, "Domain", true);
    private TFInput tfContribuyente = new TFInput(DataTypes.STRING, "OwnerDomain", true);
    private TFInput tfMarca = new TFInput(DataTypes.STRING, "Brand", false);
    private TFInput tfMotor = new TFInput(DataTypes.STRING, "Nº Motor", false);
    private TFInput tfDni = new TFInput(DataTypes.STRING, "DNI/CUIT/CUIL", false);
    private TFInput tfObservaciones = new TFInput(DataTypes.STRING, "Observations", false);
    private TFInput tfDomicilio = new TFInput(DataTypes.STRING, "Domicilio Legal", false);

    private CBInput cbModelos = new CBInput(0, "Model", false);
    private CBInput cbTipoDominio = new CBInput(0, "DomainType", false);
    private CBInput cbTipoVehiculo = new CBInput(CachedCombo.AUTOMOTORES_TABS, "Tipo Automotor", false);

    private Certificado permisoCirculacion;
    private BasicPrimitivePanel parentList;

    int error = 0;
    private static final int OK = 0;
    private static final int DOMINIOVACIO = 1;
    private static final int TITULARVACIO = 2;
    private static final int FORMATODOMINIO = 3;
    private static final int EXISTENTE = 4;

    private JLabel lblDominio = new JLabel();
    private JLabel lblTituloDominio = new JLabel();

    private BasicCheckBox chkDiscapacitado = new BasicCheckBox();

    private CBInput cbTipoAutomotor = new CBInput(0, "Descripción", true);

    private static final int FORMATO_PATENTE_AUTOS_NUEVA = 1;
    private static final int FORMATO_PATENTE_AUTOS_VIEJA = 2;
    private static final int FORMATO_DNI_CUIT_CUIL = 3;
    private static final int FORMATO_PATENTE_MOTOS = 4;

    public PermisoCirculacionMgmt() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	//tfCategory.setText("0");
	this.setSize(new Dimension(565, 370));
	this.setPreferredSize(new Dimension(565, 300));
	content.setBounds(new Rectangle(5, 0, 555, 220));
	content.setLayout(null);

	lblDominio.setBounds(new Rectangle(10, 40, 185, 20));
	lblDominio.setFont(new Font("Dialog", 1, 16));
	lblDominio.setForeground(Color.red);
	lblDominio.setBackground(new Color(183, 215, 255));
	//lblDominio.setText("TLP-929");
	lblDominio.setHorizontalAlignment(SwingConstants.CENTER);
	lblDominio.setOpaque(true);
	lblDominio.setBorder(BorderFactory.createLineBorder(Color.yellow, 1));
	lblTituloDominio.setText("Dominio Registrado");
	lblTituloDominio.setBounds(new Rectangle(10, 25, 185, 15));
	lblTituloDominio.setFont(new Font("Dialog", 1, 12));
	lblTituloDominio.setHorizontalAlignment(SwingConstants.CENTER);
	lblTituloDominio.setOpaque(true);
	lblTituloDominio.setBackground(new Color(255, 132, 0));
	lblTituloDominio.setBorder(BorderFactory.createLineBorder(Color.yellow, 1));

	tfDominio.setBounds(new Rectangle(385, 25, 160, 35));
	tfContribuyente.setBounds(new Rectangle(10, 65, 190, 35));
	tfDomicilio.setBounds(new Rectangle(10, 110, 280, 35));
	tfObservaciones.setBounds(new Rectangle(300, 110, 245, 35));
	tfMarca.setBounds(new Rectangle(215, 150, 175, 35));
	tfMotor.setBounds(new Rectangle(400, 150, 145, 35));
	tfDni.setBounds(new Rectangle(210, 65, 145, 35));

	cbTipoDominio.setBounds(new Rectangle(210, 25, 145, 35));
	cbModelos.setBounds(new Rectangle(270, 195, 75, 35));
	cbTipoDominio.addItemListener(new ItemListener() {

		public void itemStateChanged(ItemEvent evt) {
		    if (evt.getStateChange() == ItemEvent.SELECTED) {
			formatDomain();
		    }
		}
	    });
	cbTipoAutomotor.setBounds(new Rectangle(10, 150, 200, 35));
	cbTipoVehiculo.setBounds(new Rectangle(10, 195, 250, 35));
	//cbTipoVehiculo.autoSize();
	cbTipoVehiculo.setVisible(true);

	btnCerrar.setBounds(new Rectangle(540, 535, 40, 25));
	btnCerrar.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnCerrar_actionPerformed(e);
		}

	    });
	btnAceptar.setBounds(new Rectangle(490, 535, 40, 25));
	btnAceptar.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnAceptar_actionPerformed(e);
		}

	    });

	chkDiscapacitado.setText("Discapacitado");
	chkDiscapacitado.setBounds(new Rectangle(385, 75, 110, 25));

	content.add(cbTipoAutomotor, null);
	content.add(chkDiscapacitado, null);
	content.add(cbTipoVehiculo, null);
	content.add(lblTituloDominio, null);
	content.add(lblDominio, null);
	content.add(tfDni, null);
	content.add(cbModelos, null);
	content.add(tfContribuyente, null);
	content.add(tfDominio, null);
	content.add(tfMotor, null);
	content.add(tfMarca, null);
	//content.add(tfObservaciones, null);
	content.add(tfDomicilio, null);
	content.add(cbTipoDominio, null);

	content.setBorder(BorderPanel.getBorderPanel("Emisión de Permiso de Circulación"));

	setContent(content);
	addButton(btnCerrar);
	addButton(btnAceptar);

	loadCombos();
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
	super.setParentInternalFrame(_e);
	getParentInternalFrame().setInfo("Complete los campos y presione el botón \"Grabar\" ");
    }

    private void loadDomainTypeCombo(boolean _valor) {
	if (_valor) {
	    cbTipoDominio.getCombo().addItem("Sin Asignar", "0");
	}
	cbTipoDominio.getCombo().addItem("Patente Nueva", FORMATO_PATENTE_AUTOS_NUEVA);
	cbTipoDominio.getCombo().addItem("Patente Vieja", FORMATO_PATENTE_AUTOS_VIEJA);
	cbTipoDominio.getCombo().addItem("DNI/CUIT/CUIL", FORMATO_DNI_CUIT_CUIL);
	cbTipoDominio.getCombo().addItem("Patente de Motos", FORMATO_PATENTE_MOTOS);
    }

    private void formatDomain() {
	remove(content);
	content.remove(tfDominio);
	int _tipoDominio = new Integer(cbTipoDominio.getSelectedValue().toString());
	switch (_tipoDominio) {
	    case FORMATO_PATENTE_AUTOS_NUEVA:
		tfDominio = new TFInput("Domain", true, DataTypes.FORMATO_PATENTE_AUTOS_LEY1995);
		break;
	    case FORMATO_PATENTE_AUTOS_VIEJA:
		tfDominio = new TFInput("Domain", true, DataTypes.FORMATO_PATENTE_AUTOS_ANTES1995);
		break;
	    case FORMATO_PATENTE_MOTOS:
		tfDominio = new TFInput("Domain", true, DataTypes.FORMATO_PATENTE_MOTOS_LEY1995);
		break;
	    case FORMATO_DNI_CUIT_CUIL:
		tfDominio = new TFInput("Domain", true, DataTypes.FORMATO_DNI_CUIT_CUIL);
		break;
	}
	tfDominio.setBounds(new Rectangle(385, 25, 160, 35));
	content.add(tfDominio, null);
	add(content);
	tfDominio.getTextField().requestFocus();
    }

    private void loadCombos() {
	// carga combo models
	int actualYear = Integer.parseInt("0" + Environment.currentYear);
	int cont = 0;
	for (int i = 1960; i <= (actualYear + 1); i++) {
	    cont++;
	    cbModelos.getCombo().addItem(i, cont);
	}
	cbModelos.setSelectedID(cont - 1);
	cont = 0;
	cbTipoAutomotor.loadJCombo(LibSQL.exFunction("taxes.getAllTiposAutomotor", ""));
    }

    private void btnCerrar_actionPerformed(ActionEvent e) {
	getParentInternalFrame().close();
    }

    public String getLeyendaPermisoCirculacion(){
	return "\t\tLa " + OrganizationInfo.getOrgName() + ", a través de la Dirección General de Tránsito (o Departamento de Tránsito)"
	    + " certifica que el automotor con dominio Nº " + (permisoCirculacion.getNroBien().length()>0?permisoCirculacion.getNroBien().toUpperCase():"--------") + ", según R.N.P.A."
	    + "; Tipo: " + (permisoCirculacion.getTipoAutomotor().length()>0?permisoCirculacion.getTipoAutomotor().toUpperCase():"-") + "; Marca: " + (permisoCirculacion.getMarca().length()>0?permisoCirculacion.getMarca().toUpperCase():"-") 
	    + "; Modelo: " + (permisoCirculacion.getModelo().length()>0?permisoCirculacion.getModelo():"-") + "; Categoría: " + (permisoCirculacion.getCategoria().length()>0?permisoCirculacion.getCategoria():"-")
	    + "; Motor Nº: " + (permisoCirculacion.getLeyenda1().length()>0?permisoCirculacion.getLeyenda1().toUpperCase():"-")
	    + ", a nombre de "+ (permisoCirculacion.getContribuyente().length()>0?permisoCirculacion.getContribuyente().toUpperCase():"-") + "; DNI/CUIT/CUIL Nº: " + (permisoCirculacion.getDni().length()>0?permisoCirculacion.getDni().toUpperCase():"-") + ""
	    + ", se encuentra AUTORIZADO A CIRCULAR POR TODO EL TERRITORIO DEPARTAMENTAL/MUNICIPAL/PROVINCIAL/NACIONAL" 
	    + " (hasta el día " + /*Proced.setFormatDate(LibSQL.getDate("???.getfechafinpermisocirculacion", permisoCirculacion.getIdCertificado()).toString(), true) +*/ ")"
	    + "."
	    + "\n\t\tSe extiende el presente a solicitud de la parte interesada para ser presentado ante las autoridades que así lo solicitan, a los " + Environment.currentDay + " días" + " del mes de " + Environment.monthsArray[(Integer.parseInt(Environment.currentMonth) - 1)] + " del año " + Environment.currentYear + ".-";
    }

    public boolean saveData() {
	boolean _return = false;
	permisoCirculacion = new Certificado();
	if (control() == OK) {
	    permisoCirculacion.setNroBien(tfDominio.getString());
	    permisoCirculacion.setContribuyente(tfContribuyente.getString());
	    permisoCirculacion.setDni(tfDni.getValue().toString());
	    permisoCirculacion.setTipoAutomotor(cbTipoAutomotor.getSelectedItem().toString());
	    permisoCirculacion.setMarca(tfMarca.getString());
	    permisoCirculacion.setLeyenda1(tfMotor.getString());
	    permisoCirculacion.setModelo(cbModelos.getSelectedItem().toString());
	    permisoCirculacion.setDomicilio(tfDomicilio.getString());
	    permisoCirculacion.setVigencia(30);
	    permisoCirculacion.setLeyenda2(getLeyendaPermisoCirculacion());

	    if (Integer.parseInt(cbTipoVehiculo.getSelectedValue().toString()) == 4) {
		//MOTOCICLETAS
	        permisoCirculacion.setIdAlicuotaContribucion(135); //PERMISO DE CIRCULACIÓN POR DÍA PARA MOTOCICLETAS (Artº 137, inciso b.2)
	    } else {
		//AUTOMÓVILES; PICK UP Y CAMIONES; ACOPLADOS-TRAILERS Y SEMI REMOLQUE
	        permisoCirculacion.setIdAlicuotaContribucion(134); //PERMISO DE CIRCULACIÓN POR DÍA AUTOMOTORES (Artº 137, inciso b.1)
	    }
	    
	    AlicuotaContribucion alicuotaContribucion = new AlicuotaContribucion();
	    alicuotaContribucion.setIdalicuotacontribucion(permisoCirculacion.getIdAlicuotaContribucion());
	    BoletaContribucion boletaContribucion = new BoletaContribucion();
	    boletaContribucion.setConcepto(alicuotaContribucion.getNombre());
	    boletaContribucion.setContribuyente(permisoCirculacion.getContribuyente());
	    boletaContribucion.setNroBien(permisoCirculacion.getNroBien());
	    boletaContribucion.setTipoBien(permisoCirculacion.getTipoAutomotor());
	    //boletaContribucion.setIdTipoImpuesto(_idTipoImpuesto);
	    boletaContribucion.setIdAlicuotaContribucion(alicuotaContribucion.getIdalicuotacontribucion());
	    if (boletaContribucion.saveData() > 0) {
	        boletaContribucion.retrieveData();
	        permisoCirculacion.setIdDetalleBoletaContribucion(boletaContribucion.getIdDetalleBoletaContribucionByIndex(1));
	        if (permisoCirculacion.saveData() >= 0) {
	            Advisor.messageBox("Se generó correctamente el Permiso de Circulación (Boleta Nº " + boletaContribucion.getAnio() + "-" + boletaContribucion.getNumeroFormateado() + ")", "Aviso");
	            _return = true;
	        } else {
		    boletaContribucion.delete();
		    Advisor.messageBox("Ha ocurrido un error al intentar generar el Permiso de Circulación", "Error");
		}
	    } else {
	        Advisor.messageBox("Ha ocurrido un error al intentar generar el Permiso de Circulación", "Error");
	    }
	} else {
	    showMessage();
	}
	return _return;
    }

    private void btnAceptar_actionPerformed(ActionEvent e) {
	if (saveData()) {
	    parentList.refresh();
	    getParentInternalFrame().close();
	}
    }

    public void setParentList(BasicPrimitivePanel _parentList) {
	parentList = _parentList;

    }

    private int control() {
	error = OK;
	if (permisoCirculacion.getIdCertificado() == -1) {
	    if (LibSQL.getBoolean("taxes.existsPermisoCirculacion", tfDominio.getStringForSQL() + "," + tfMotor.getStringForSQL())) {
		error = EXISTENTE;
	    }
	}
	if (error != EXISTENTE) {
	    if (tfDominio.getTextField().getText().trim().equals("")) {
		error = DOMINIOVACIO;
	    } else if (tfContribuyente.getString().equals("")) {
		error = TITULARVACIO;
	    } else if ((tfDominio.getTextField().getValue().toString() == "") || (tfDominio.getTextField().getValue().toString().contains("_"))) {
		error = FORMATODOMINIO;
	    }
	}
	return error;
    }

    private int getYear(String _date) {
	return (Integer.parseInt(_date.substring(0, _date.indexOf("-"))));
    }

    private void showMessage() {
	switch (error) {
	    case DOMINIOVACIO:
		Advisor.messageBox("Debe ingresar el número de Dominio", "Mensaje");
		break;
	    case TITULARVACIO:
		Advisor.messageBox("Debe ingresar el nombre del Titular", "Mensaje");
		break;
	    case FORMATODOMINIO:
		Advisor.messageBox("El formato del dominio es incorrecto", "Mensaje");
		break;
	    case EXISTENTE:
		Advisor.messageBox("Ya existe el Dominio: " + tfDominio.getValue().toString(), "Mensaje");
		break;
	}
    }

}

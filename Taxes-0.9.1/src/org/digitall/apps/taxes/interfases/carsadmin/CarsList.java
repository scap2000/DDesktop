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
 * CarsList.java
 *
 * */
package org.digitall.apps.taxes.interfases.carsadmin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.digitall.apps.taxes.classes.Car;
import org.digitall.common.reports.BasicReport;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.grid.TableTransferHandler;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

public class CarsList extends BasicPrimitivePanel {

    private BasicPanel content = new BasicPanel();
    private BasicPanel jpBuscar = new BasicPanel();

    private TFInput tfNombre = new TFInput(DataTypes.STRING, "Name",false);
    private TFInput tfDominio = new TFInput(DataTypes.STRING, "Domain",false);
    private TFInput tfDni = new TFInput(DataTypes.INTEGER, "DNI",false);
    private TFInput tfModelo = new TFInput(DataTypes.INTEGER, "Modelo",false);

    private int[] sizeColumnList = {75, 190, 75, 85, 135, 35 , 55 , 80, 80};
    private Vector dataRow = new Vector();
    private GridPanel listPanel = new GridPanel(50000, sizeColumnList, "Listado de Vehículos", dataRow) {
	public void finishLoading() {
	    controlBotones();
	}
    };
    
    private FindButton btnBuscar = new FindButton();
    private AddButton btnAgregar = new AddButton();
    private ModifyButton btnEditar = new ModifyButton();
    private ModifyButton btnVerProblemas = new ModifyButton();
    private PrintButton btnImprimir = new PrintButton();
    private NextWizardButton btnManuallyEditFee = new NextWizardButton();
    
    private Car car;

    public CarsList() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	//this.setLayout(null);
	this.setSize(new Dimension(710, 388));
	this.setPreferredSize(new Dimension(710, 515));
	tfNombre.setBounds(new Rectangle(15, 20, 170, 35));
	listPanel.setBounds(new Rectangle(5, 75, 700, 270));
	content.setBounds(new Rectangle(5, 5, 700, 500));
	content.setLayout(null);
	content.setSize(new Dimension(700, 515));
	jpBuscar.setBounds(new Rectangle(3, 0, 700, 65));
	jpBuscar.setLayout(null);
	tfDominio.setBounds(new Rectangle(380, 20, 130, 35));
	btnBuscar.setBounds(new Rectangle(660, 30, 30, 25));
	btnBuscar.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      btnFind_actionPerformed(e);
				  }

			      }
	);
	btnAgregar.setBounds(new Rectangle(560, 525, 40, 25));
	btnAgregar.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  btnAdd_actionPerformed(e);
			      }

			  }
	);
        btnAgregar.setToolTipText("Agregar nuevo Dominio");
	btnImprimir.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnPrint_actionPerformed(e);
				}

			    }
	);
	
	btnImprimir.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.getButton() == e.BUTTON3) {
		    clickPrint(e);
		}
	    }
	});
	
	tfDni.setBounds(new Rectangle(230, 20, 105, 35));
        jpBuscar.setBorder(BorderPanel.getBorderPanel("Buscar"));
        jpBuscar.add(tfModelo, null);
        jpBuscar.add(tfDni, null);
        jpBuscar.add(tfDominio, null);
        jpBuscar.add(tfNombre, null);
        jpBuscar.add(btnBuscar, null);
        btnEditar.setBounds(new Rectangle(610, 525, 40, 25));
	btnEditar.setToolTipText("Modificar el Dominio seleccionado");
	btnManuallyEditFee.setToolTipText("Modificar el valor de anticipos periódicos (mes, bimestre, trimestre, etc.) según corresponda");
	btnVerProblemas.setToolTipText("Ver problemas del Automotor seleccionado");
	btnEditar.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnEdit_actionPerformed(e);
			       }

			   }
	);

	btnManuallyEditFee.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnManuallyEditFee_actionPerformed(e);
			       }

			   }
	);
	
	btnVerProblemas.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  btnVerProblemas_actionPerformed(e);
			      }

			  }
	);
	btnVerProblemas.setEnabled(false);
	content.add(listPanel, null);
        content.add(jpBuscar, null);
        this.add(content, BorderLayout.CENTER);
	addButton(btnEditar);
	addButton(btnAgregar); 
	addButton(btnVerProblemas);
	addButton(btnImprimir);
	addButton(btnManuallyEditFee);
	
	listPanel.getTable().setDragEnabled(true);
	listPanel.getTable().setTransferHandler(new TableTransferHandler());
	tfNombre.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});
	tfDominio.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});
	tfDni.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});
	tfModelo.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});
	btnEditar.setEnabled(false);
	btnManuallyEditFee.setEnabled(false);
	tfModelo.setBounds(new Rectangle(555, 20, 70, 35));
	setCadastralHeader();
        listPanel.getTable().addMouseListener(new MouseAdapter() { 
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
                            loadSelectedObject();    
                        } else if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
                            loadMgmt(false);
                        }
                    }
       });
    }
    
    public void setParentInternalFrame(ExtendedInternalFrame _e) {
	super.setParentInternalFrame(_e);
	getParentInternalFrame().setInfo("Busqueda, Alta, Modificación e Impresión de Dominios");
    }
    

    public void refresh() {
	String params = "'"+ tfNombre.getString() +"','"+ tfDni.getString() +"','"+ tfDominio.getString() +"',"+ tfModelo.getInteger();
	listPanel.refresh(params);
	btnEditar.setEnabled(false);
	btnManuallyEditFee.setEnabled(false);
	btnImprimir.setVisible(true);
	btnVerProblemas.setEnabled(false);
    }

    private void setCadastralHeader() {
        Vector carslHeader = new Vector();
	//carslHeader.removeAllElements();
	carslHeader.addElement("*");    //iddominio
	carslHeader.addElement(Environment.lang.getProperty("Domain"));
	carslHeader.addElement(Environment.lang.getProperty("Name"));
        carslHeader.addElement(Environment.lang.getProperty("DNI"));
	carslHeader.addElement(Environment.lang.getProperty("Type"));
	carslHeader.addElement(Environment.lang.getProperty("Brand"));
	carslHeader.addElement("*");    //motor
	carslHeader.addElement("Cat.");    //categoria
	carslHeader.addElement(Environment.lang.getProperty("Model"));
        carslHeader.addElement("*");    //cuota
        carslHeader.addElement("*");    //valor anual
        carslHeader.addElement("*");    //pagadesde
	carslHeader.addElement(Environment.lang.getProperty("Eximido"));
	carslHeader.addElement("*");    //fechabaja;		-->
	carslHeader.addElement("*");    //estado
	carslHeader.addElement("*");    //idautomotor
	carslHeader.addElement("*");    //anticipoautomotor
	carslHeader.addElement("*");    //domicilio
	carslHeader.addElement("*");    //observaciones
        carslHeader.addElement("*");    //tipo dominio
        carslHeader.addElement("*");    //discapacitado
        carslHeader.addElement("*");    //Cuota depreciada
	carslHeader.addElement("$ Actual");    //Cuota actual
		
	String params = "'','','',0" ;
		listPanel.setParams("taxes.getAllCars", params, carslHeader);
    }

    
    private void loadSelectedObject(){
	if (!dataRow.isEmpty()){
	    car = new Car();
	    car.setIddominio(Integer.parseInt("" + dataRow.elementAt(0)));
	    car.setDominio("" + dataRow.elementAt(1));
	    car.retrieveData();
	    btnEditar.setEnabled(true);
	    btnManuallyEditFee.setEnabled(true);
	    btnVerProblemas.setEnabled(true);
	}
    }
    
    private void btnFind_actionPerformed(ActionEvent e) {
	refresh();
    }

    private void loadMgmt(boolean _addAction){
	if (_addAction) {
	    car = new Car();
	}
	CarsMgmt carsMgmt = new CarsMgmt();
	carsMgmt.setCar(car);
	carsMgmt.setParentList(this);
	
	ExtendedInternalFrame carsMgmtContainer = new ExtendedInternalFrame("Agregar/Modificar");
	carsMgmtContainer.setCentralPanel(carsMgmt);
	carsMgmtContainer.show();
	carsMgmt.load();
    }
    
    private void btnAdd_actionPerformed(ActionEvent e) {
	//Falta una funcion que haga controles. Por ej. la fecha del modelo no debe ser superior a la fecha de inicio de pago
	loadMgmt(true);
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
	//Falta una funcion que haga controles. Por ej. la fecha del modelo no debe ser superior a la fecha de inicio de pago
	loadSelectedObject();
	loadMgmt(false);
    }

    private void btnManuallyEditFee_actionPerformed(ActionEvent e) {
	loadSelectedObject();
	//Solicito el nuevo valor PERIÓDICO
	String _inputFee = null;
	double _newFee = -1;
	while (_inputFee == null) {
	    _inputFee = (String)JOptionPane.showInternalInputDialog(Environment.getActiveDesktop(), "Valor actual: " + "\nIngrese el nuevo valor  ", "Nuevo valor " + car.getDominio(), JOptionPane.QUESTION_MESSAGE, null, null, "");
	    if (_inputFee == null) {
	        _inputFee = "-1"; //Presionó escape o cancelar
	    }
	    try {
	        _newFee = Double.parseDouble(_inputFee.replaceAll(",", "."));
	    } catch (NumberFormatException ex) {
	        Advisor.messageBox("Debe ingresar un monto válido", "Monto erróneo");
	        _inputFee = null;
	    }
	}
		
	//Pregunto desde cuándo quiero actualizar el valor
	//Esto funciona ÚNICAMENTE para los bimestres del AÑO ACTUAL
        
        //Aquí debería obtener la cantidad de períodos a subdividir el año... es configuración
        int periodos = LibSQL.getInt("taxes.getconfigperiodosautomotor","");
	Object[] _periodos = new Object[periodos];
        for (int i = 0; i < periodos; i++) {
            _periodos[i] = i+1;
        }
        
        int _currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int _cantAnios = _currentYear + 1 - (car.getPagadesde().equals(null)?0:Integer.parseInt(car.getPagadesde().substring(0,4)));
        Object[] _anios = new Object[_cantAnios];
        for (int i = _cantAnios-1; i >= 0; i--) {
            _anios[i] = _currentYear-i;
        }
        
        int _inputAnio = JOptionPane.showInternalOptionDialog(Environment.getActiveDesktop(), "Actualizar SOLAMENTE EL AÑO: ", "Elija anio", -1, JOptionPane.QUESTION_MESSAGE, null, _anios, _anios[0]);
        
	int _inputPeriodo = JOptionPane.showInternalOptionDialog(Environment.getActiveDesktop(), "(SOLAMENTE EL AÑO " + _anios[_inputAnio] + ")\nActualizar desde el período (inclusive): ", "Elija período", -1, JOptionPane.QUESTION_MESSAGE, null, _periodos, _periodos[0]);
	if (_inputPeriodo != -1 && _newFee != -1) {
	    double _periodicalFee = LibSQL.getDouble("taxes.getperiodicalcarfeebyiddominio", car.getIddominio());
	    //Muestro valor PERIÓDICO Y ANUAL NUEVOS Y VIEJOS y pido confirmación
	    if (Advisor.question("Actualizar valores", "¿Desea cambiar estos valores?\nAntes $ " + _periodicalFee + " - Ahora $ " + _newFee + "\npara actualizar desde el período " + _periodos[_inputPeriodo] + " en adelante")) {
		if (LibSQL.getBoolean("taxes.setperiodicalcarfeebyiddominio", car.getIddominio() + "," + _newFee + "," + _anios[_inputAnio] + "," + _periodos[_inputPeriodo])) {
		    Advisor.messageBox("El nuevo valor se ha asignado con éxito", "Cambios grabados");
		    //refresh();
		} else {
		    Advisor.messageBox("Ha ocurrido un error al intentar modificar el valor", "Error");
		}
	    }
	}
    }

    private void btnPrint_actionPerformed(ActionEvent e) {
	boolean vigente = true;
	BasicReport report = new BasicReport(CarsList.class.getResource("xml/PadronAutomotor.xml"));
	report.addTableModel("taxes.xmlGetPadronAutomotor", ""+ vigente );
	if (vigente) {
	    report.setProperty("title", "Padrón Automotor Vigente");
	    report.setProperty("impuesto", "TASA GENERAL DE SERVICIOS");
	} else {
	    report.setProperty("title", "Padrón Automotor con baja");
	}
	report.doReport();
    }

    private void btnVerProblemas_actionPerformed(ActionEvent e) {
	AutomotoresConProblemasMgmt automotoresConProblemasMgmt = new AutomotoresConProblemasMgmt();
	automotoresConProblemasMgmt.setCar(car);
	automotoresConProblemasMgmt.setParentList(this);
	
	ExtendedInternalFrame automotoresConProblemasMgmtContainer = new ExtendedInternalFrame("Agregar/Modificar Problemas");
	automotoresConProblemasMgmtContainer.setCentralPanel(automotoresConProblemasMgmt);
	automotoresConProblemasMgmtContainer.show();
    }
    
    private void controlBotones(){
	btnEditar.setEnabled(false);
	btnManuallyEditFee.setEnabled(false);
	btnVerProblemas.setEnabled(false);
    }
    
    private void btnDel_actionPerformed(ActionEvent e) {
	// Funcion que podra borrar automotores, solo en modo admin
	if (Advisor.question("Advertencia","¿Está seguro de eliminar el Dominio "+ car.getDominio() +"?")) {
	    if (car.deleteCar()) {
		Advisor.messageBox("Dominio Borrado exitosamente","Mensaje");
		refresh();
	    } else {
	        Advisor.messageBox("Ocurrió un error al borrar el Dominio","Error");
	    }
	}
    }
    
    private void clickPrint(MouseEvent e){
	PopupPrinterCars popupPrint = new  PopupPrinterCars();
	int x = (int)e.getPoint().getX();
	int y = (int)e.getPoint().getY() - popupPrint.getAlto();
	popupPrint.show(btnImprimir,x,y);
    }
    
}

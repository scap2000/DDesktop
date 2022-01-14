package org.digitall.apps.taxes092.interfases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.Charset;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.buttons.GoButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.buttons.RefreshGridButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.grid.TableTransferHandler;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

public class EDESACOBAdmin extends BasicPrimitivePanel {

    private BasicPanel contentPanel = new BasicPanel();
    private BasicPanel searchPanel = new BasicPanel();
    private CBInput cbPeriodos = new CBInput(0, "Periodos COB", false);
    private CBInput cbFileNames = new CBInput(0, "Archivos importados", false);
    private CBInput cbErrorTypes = new CBInput(0, "# Error", false);
    private TFInput tfNIS = new TFInput(DataTypes.INTEGER, "NIS",false);

    private int[] sizeColumnList = {104, 74, 77, 67, 70, 62, 40, 54, 78, 35, 443 };
    private Vector pagosEdesaHeader = new Vector();
    private Vector dataRow = new Vector();
    private GridPanel edesaPaymentsPanel = new GridPanel(50000, sizeColumnList, "Listado de PAGOS ARCHIVOS COB EDESA", dataRow) {
	public void finishLoading() {
	    controlBotones();
	}
    };
    
    private AssignButton btnFilterFiles = new AssignButton();
    private FindButton btnFind = new FindButton();
    private AddButton btnAdd = new AddButton();
    private ModifyButton btnAsentarPagos = new ModifyButton();
    private GoButton btnAsentarPagosNIS = new GoButton();
    private RefreshGridButton btnUndoPagoEdesaFromNIS = new RefreshGridButton();
    private SaveDataButton btnImportarArchivos = new SaveDataButton();
   
    private Color alertColor = Color.red;
    private BasicPanel panelAlerta;
    private Timer alertTimer;

    public EDESACOBAdmin() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	panelAlerta = new BasicPanel() {
    
	    @Override
	    public void paint(Graphics _graphics) {
		_graphics.setColor(alertColor);
		_graphics.fillOval(8, 0, 30, 30);
		_graphics.setColor(Color.black);
		_graphics.drawOval(8, 0, 30, 30);
		super.paintComponents(_graphics);
	    }
	};
	alertTimer = new Timer(400, new ActionListener() {
    
	    public void actionPerformed(ActionEvent e) {
		alertColor = (alertColor==Color.red?Color.yellow:Color.red);
		panelAlerta.repaint();
		panelAlerta.getParent().repaint();
	    }
            
	});
	this.setSize(new Dimension(825, 600));
	this.setPreferredSize(new Dimension(825, 600));
	edesaPaymentsPanel.setBounds(new Rectangle(5, 70, 810, 480));
	contentPanel.setBounds(new Rectangle(5, 5, 700, 450));
	contentPanel.setLayout(null);
	contentPanel.setSize(new Dimension(700, 515));
	searchPanel.setBounds(new Rectangle(5, 5, 700, 60));
	searchPanel.setLayout(null);

	cbPeriodos.setBounds(new Rectangle(40, 15, 130, 35));
	cbPeriodos.autoSize();
	cbPeriodos.setGeneralItem(true);
	cbPeriodos.loadJCombo("taxes.getimportedperiodsfromedesacob", "");

	cbFileNames.setBounds(new Rectangle(240, 15, 130, 35));
	cbFileNames.autoSize();
	cbFileNames.setGeneralItem(true);

	cbErrorTypes.setBounds(new Rectangle(380, 15, 100, 35));
	cbErrorTypes.autoSize();
	cbErrorTypes.setGeneralItem(true);
	cbErrorTypes.loadJCombo("taxes.getedesaerrortypesfromcob", "");

	tfNIS.setBounds(new Rectangle(505, 15, 110, 35));


	btnFilterFiles.setBounds(new Rectangle(185, 20, 30, 25));

	btnFind.setBounds(new Rectangle(655, 20, 30, 25));
	btnFind.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      btnFind_actionPerformed(e);
				  }

			      }
	);
	btnAdd.setBounds(new Rectangle(560, 525, 40, 25));
	btnAdd.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  btnAdd_actionPerformed(e);
			      }

			  }
	);

	btnImportarArchivos.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  btnImportarArchivos_actionPerformed(e);
			      }

			  }
	);
	btnImportarArchivos.setToolTipText("Importar más archivos");
        
	searchPanel.add(panelAlerta, null);
	searchPanel.add(tfNIS, null);
	searchPanel.add(cbPeriodos, null);
	searchPanel.add(cbFileNames, null);
	searchPanel.add(cbErrorTypes, null);
	searchPanel.add(btnFilterFiles, null);
	searchPanel.add(btnFind, null);
	btnAsentarPagos.setBounds(new Rectangle(610, 525, 40, 25));
	btnAsentarPagosNIS.setBounds(new Rectangle(610, 525, 40, 25));
	btnUndoPagoEdesaFromNIS.setBounds(new Rectangle(610, 525, 40, 25));
	btnAsentarPagos.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnAsentarPagos_actionPerformed(e);
			       }

			   }
	);


	btnAsentarPagosNIS.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnAsentarPagosNIS_actionPerformed(e);
			       }

			   }
	);

	btnUndoPagoEdesaFromNIS.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnUndoPagoEdesaFromNIS_actionPerformed(e);
			       }

			   }
	);


	contentPanel.add(edesaPaymentsPanel, null);
	contentPanel.add(searchPanel, null);
	this.add(contentPanel, BorderLayout.CENTER);
	this.addButton(btnAsentarPagos);
	this.addButton(btnAsentarPagosNIS);
	this.addButton(btnUndoPagoEdesaFromNIS);

	if (Environment.sessionUser.equals("santiago")) this.addButton(btnImportarArchivos);

	edesaPaymentsPanel.getTable().setDragEnabled(true);
	edesaPaymentsPanel.getTable().setTransferHandler(new TableTransferHandler());

	tfNIS.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});     
	
	btnAsentarPagos.setToolTipText("Asentar pagos del periodo o archivo seleccionado");
	btnAsentarPagosNIS.setToolTipText("Asentar pagos de un NIS seleccionado");
	btnUndoPagoEdesaFromNIS.setToolTipText("Deshacer pagos de un NIS seleccionado");
	setPagosEdesaHeader();
	btnAdd.setEnabled(false);
	searchPanel.setBorder(BorderPanel.getBorderPanel("Buscar"));
	btnFilterFiles.addActionListener(new ActionListener() {

    public void actionPerformed(ActionEvent e) {
	btnFilterFiles_actionPerformed(e);
    }
});
	searchPanel.setSize(new Dimension(810, 60));
	panelAlerta.setBounds(new Rectangle(10, 20, 45, 35));
	panelAlerta.setVisible(false);
    }
    
    private void setPagosEdesaHeader() {
	
	pagosEdesaHeader.removeAllElements();
	pagosEdesaHeader.addElement("Archivo");
	pagosEdesaHeader.addElement("Importado");
	pagosEdesaHeader.addElement("Pagado"); //Dia+Mes+Año
	pagosEdesaHeader.addElement("NIS"); //NIS
	pagosEdesaHeader.addElement("Anticipo"); //Dia_nir+Mes_nir+Año_nir
	pagosEdesaHeader.addElement("$ Importe"); //Importe
	pagosEdesaHeader.addElement("Tipo"); //Tipopago
	pagosEdesaHeader.addElement("Cargado"); //Cargado
	pagosEdesaHeader.addElement("Fecha Carga"); //Fechacarga
	pagosEdesaHeader.addElement("Error"); //Errordecarga
	pagosEdesaHeader.addElement("Descripción error"); //Errordecarga
	
	 edesaPaymentsPanel.getTable().addMouseListener(new MouseAdapter() {
                                                    
						  public void mouseClicked(MouseEvent e) {
						      loadObjectSelected();
						      if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
						      } else if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
						      }
						  }

					      }
	 );
	String params =  "'" + cbPeriodos.getSelectedValueRef() + "','" + cbFileNames.getSelectedValue() + "'," + cbErrorTypes.getSelectedValue() + ",0" + tfNIS.getInteger();
	 edesaPaymentsPanel.setParams("taxes.getAllPagosEdesaCob", params, pagosEdesaHeader);
    }
    
    public void setParentInternalFrame(ExtendedInternalFrame _e) {	
	super.setParentInternalFrame(_e);
	getParentInternalFrame().setInfo("Administración de Pagos de EDESA");
    }

    
    public void refresh() {
	String params =  "'" + cbPeriodos.getSelectedValueRef() + "','" + cbFileNames.getSelectedValue() + "'," + cbErrorTypes.getSelectedValue() + ",0" + tfNIS.getInteger();
	edesaPaymentsPanel.refresh(params);
	btnAdd.setEnabled(false);
	panelAlerta.setVisible(false);
	alertTimer.stop();
    }
    
    private void loadObjectSelected(){
	if (!dataRow.isEmpty()){
	}
    }
    
    private void btnFind_actionPerformed(ActionEvent e) {
	refresh();
    }

    private void btnAsentarPagos_actionPerformed(ActionEvent e) {
	if (!cbFileNames.getSelectedValue().toString().equals("-1")) {
	    if (Advisor.question("Asentar pagos", "¿Está seguro de asentar los pagos del archivo " +  cbFileNames.getSelectedItem() + "?")) {
		//Que comience la fiesta
		if (LibSQL.getBoolean("taxes.setpagosedesafromcob", "'" + cbFileNames.getSelectedItem() + "'")) {
		    Advisor.messageBox("Los datos del archivo " + cbFileNames.getSelectedItem() + " han sido registrados todos con éxito", "Correcto");
		} else {
		    Advisor.messageBox("Han ocurrido uno o más errores al asentar los pagos del archivo " + cbFileNames.getSelectedItem() + ", revise el registro de pagos", "Error al asentar los pagos");
		}
	        refresh();
	    }
	} else if (!cbPeriodos.getSelectedValue().toString().equals("-1")) {
	    if (Advisor.question("Asentar pagos", "¿Está seguro de asentar los pagos del periodo " +  cbPeriodos.getSelectedItem() + "?")) {
	        //Que comience la fiesta
		if (LibSQL.getBoolean("taxes.setpagosedesaperiodosfromcob", "'" + cbPeriodos.getSelectedValueRef().toString().substring(0,2) + "','" + cbPeriodos.getSelectedValueRef().toString().substring(2,4) + "'")) {
	            Advisor.messageBox("Los datos del periodo " + cbPeriodos.getSelectedItem() + " han sido registrados con éxito", "Correcto");
	        } else {
	            Advisor.messageBox("Han ocurrido uno o más errores al asentar los pagos del archivo  " + cbFileNames.getSelectedItem() + ", revise el registro de pagos", "Error al asentar los pagos");
	        }
	        refresh();
	    }
	} else {
	    Advisor.messageBox("Debe seleccionar un único archivo o periodo para asentar los pagos", "Error");
	}
    }

    private void btnAsentarPagosNIS_actionPerformed(ActionEvent e) {
	if (tfNIS.getString().length()>5) {
	    if (Advisor.question("Asentar pagos", "¿Está seguro de asentar los pagos del NIS " +  tfNIS.getString()+ "?")) {
		//Que comience la fiesta
		if (LibSQL.getBoolean("taxes.setpagosedesacobfromnis", "" + tfNIS.getString() + ",''")) {
		    Advisor.messageBox("Los datos del NIS " + tfNIS.getString() + " han sido registrados todos con éxito", "Correcto");
		} else {
		    Advisor.messageBox("Han ocurrido uno o más errores al asentar los pagos del NIS " + tfNIS.getString() + ", revise el registro de pagos", "Error al asentar los pagos");
		}
		refresh();
	    }
	} else {
	    Advisor.messageBox("Debe escribir un NIS válido para asentar los pagos", "Error");
	}
    }

    private void btnUndoPagoEdesaFromNIS_actionPerformed(ActionEvent e) {
	if (tfNIS.getString().length()>5) {
	    if (Advisor.question("Asentar pagos", "¿Está seguro de deshacer los pagos del NIS " +  tfNIS.getString()+ "?")) {
		//Que comience la fiesta
		if (LibSQL.getBoolean("taxes.undopagosedesafromniscob", "" + tfNIS.getString() + ",-1")) {
		    Advisor.messageBox("Los pagos del NIS " + tfNIS.getString() + " han sido registrados con éxito", "Correcto");
		} else {
		    Advisor.messageBox("Han ocurrido uno o más errores al deshacer los pagos del NIS " + tfNIS.getString() + ", revise el registro de pagos", "Error al deshacer los pagos");
		}
		refresh();
	    }
	} else {
	    Advisor.messageBox("Debe escribir un NIS válido para deshacer los pagos", "Error");
	}
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
    }

    private void controlBotones(){
	btnAdd.setEnabled(false);
    }

    private void btnImportarArchivos_actionPerformed(ActionEvent e) {
	try {
	    JFileChooser chooser = new JFileChooser(Environment.cfg.getProperty("EDESAFilePath") + File.separator);
	    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("EDESA COB", "cob", "pagos");
	    chooser.setFileFilter(filter);
	    chooser.setMultiSelectionEnabled(true);
	    int _files = 0;
	    int _importedFiles = 0;
	    int returnVal = chooser.showSaveDialog(chooser.getParent());
	    if (returnVal == JFileChooser.APPROVE_OPTION && chooser.getSelectedFiles().length > 0) {
	        Connection _pgCon = LibSQL.CreateConnection();
		Environment.cfg.setProperty("EDESAFilePath", chooser.getSelectedFile().getParent());
	        for (File _file: chooser.getSelectedFiles()) {
		    _files++;
		    InputStream fis = new FileInputStream(_file);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		    boolean _corruptedFile = false;
		    int i = 0;
		    Vector<String>_archivo = new Vector<String>();
		    String _readLine;
		    if (LibSQL.getBoolean("taxes.edesacobfileexists", "'" + _file.getName() + "'")) {//archivo ya cargado
			//Advisor.messageBox("El archivo " + _file.getName() + " ya existe en la base de datos, no se cargará", "Archivo COB existente");
		        System.out.println("El archivo " + _file.getName() + " ya existe en la base de datos, no se cargará");
			_corruptedFile = true;
		    }
		    while ((_readLine = br.readLine()) != null && !_corruptedFile) { //if para probar con una línea, reemplazar con while para procesar todo el archivo
			i++;
			if (_readLine.length() != 46) {
			    Advisor.messageBox("El archivo " + _file.getName() + " es incorrecto, línea " + i + "\nDatos: " + _readLine , "Error en archivo COB");
			    _corruptedFile = true;
			    Advisor.messageBox("No se cargará NINGÚN DATO de este archivo", "Error en archivo COB");
			} else {
			    //System.out.println("Archivo: " + _file.getName() + " - DATA: " +_readLine + " (Chars: " + _readLine.length() + ")");
			    _archivo.add(_readLine);
			}
		    }
		    CallableStatement _statement = _pgCon.prepareCall("{call taxes.importEdesaCobFile(?, ?)}");
		    _statement.setString(1, _file.getName()); //FILENAME
		    if (!_corruptedFile && _archivo.size()>0) {
			for (String _line: _archivo) {
			    /*System.out.println("Código de Agencia: " + _line.substring(0, 10));
			    System.out.println("Código de Sucursal: " + _line.substring(10, 20));
			    System.out.println("Código de Banco: " + _line.substring(20, 23));
			    System.out.println("Día de cobro: " + _line.substring(23, 25));
			    System.out.println("Mes de cobro: " + _line.substring(25, 27));
			    System.out.println("Año de cobro: " + _line.substring(27, 31));
			    System.out.println("X: " + _line.substring(31, 32));
			    System.out.println("Recibo: " + _line.substring(32, 33));
			    System.out.println("NIS: " + _line.substring(33, 40));
			    System.out.println("Contrato: " + _line.substring(40, 42));
			    System.out.println("Día NIR: " + _line.substring(42, 44));
			    System.out.println("Mes NIR: " + _line.substring(44, 46));
			    System.out.println("Año NIR: " + _line.substring(46, 48));
			    System.out.println("IMPORTE: " + new Double(_line.substring(48, 56) + "." + _line.substring(57, 59))); //el 57 es la coma
			    System.out.println("NADA: " + _line.substring(59, 60));
			    System.out.println("Tipo de Pago: " + _line.substring(60, 61));*/ //OJO SI NO ES "P" HAY QUE REVISAR EL CASO PARTICULAR!!!
			    
			    //Habría que registrar toda la línea y aparte 
			    //la fecha 28022014 + NIS como clave de búsqueda para saber que no se haya cargado ya ese pago???
			    
			    //System.out.println(_line);
			}
			Array _data = _pgCon.createArrayOf("VARCHAR", _archivo.toArray());
			_statement.setArray(2, _data); //Contenido
			_statement.registerOutParameter(1, -7);
			_statement.execute();
			boolean _result = _statement.getBoolean(1);
			//Advisor.mesesageBox("Archivo " + _file.getName() + " - Resultado: " + (_result?"Correcto":"Con error"), "Resultado de importación");
			_importedFiles++;
			System.out.println("Archivo " + _file.getName() + " - Resultado: " + (_result?"Correcto":"Con error"));
		    }
		}
	        _pgCon.close();
	        cbPeriodos.loadJCombo("taxes.getimportedperiodsfromedesacob", "");
	        Advisor.messageBox("Fin de la importación, revise el LOG para más datos\nSe han importado " + _importedFiles + " de " + _files + " archivos seleccionados", "Resultado de importación");
	    }
	} catch (FileNotFoundException f) {
	    Advisor.messageBox("Error: archivo no encontrado", "Error");
	} catch (IOException f) {
	    Advisor.messageBox("Error: no se pudo leer el archivo", "Error");
	} catch (SQLException f) {
	    //Advisor.messageBox("Error: no se pudo conectar a la Base de Datos", "Error");
	    Advisor.printException(f);
	}
    }

/*    public void tryToAddEDESAPayment(String _line) {	
	
    }*/

    private void btnFilterFiles_actionPerformed(ActionEvent e) {
	cbFileNames.loadJCombo("taxes.getimportedfilesfromedesacob", "'" + cbPeriodos.getSelectedValueRef() + "'");
    }
}

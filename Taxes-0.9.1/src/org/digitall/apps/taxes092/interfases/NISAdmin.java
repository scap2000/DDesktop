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

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import org.digitall.apps.taxes.classes.Cadastral;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.DeleteButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.components.grid.TableTransferHandler;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

public class NISAdmin extends BasicPrimitivePanel {

    private BasicPanel contentPanel = new BasicPanel();
    private BasicPanel searchPanel = new BasicPanel();
    private TFInput tfPersonName = new TFInput(DataTypes.STRING, "Name",false);
    private TFInput tfIdentificationNumber = new TFInput(DataTypes.INTEGER, "DNI",false);
    private TFInput tfCadastral = new TFInput(DataTypes.INTEGER, "Cadastral",false);
    private TFInput tfNIS = new TFInput(DataTypes.INTEGER, "NIS",false);

    private int[] sizeColumnList = {60, 60, 230, 70, 50, 155, 50, 170,60};
    private Vector cadastralHeader = new Vector();
    private Vector dataRow = new Vector();
    private GridPanel cadastralPanel = new GridPanel(50000, sizeColumnList, "Listado de Catastros y NIS asociados", dataRow) {
	public void finishLoading() {
	    controlBotones();
	}
    };
    
    private FindButton btnFind = new FindButton();
    private AddButton btnAdd = new AddButton();
    private ModifyButton btnEdit = new ModifyButton();
    private DeleteButton btnDelete = new DeleteButton();

    private Cadastral cadastral;
    
    private Color alertColor = Color.red;
    private BasicPanel panelAlerta;
    private Timer alertTimer;

    public NISAdmin() {
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
	this.setSize(new Dimension(710, 448));
	this.setPreferredSize(new Dimension(710, 515));
	tfPersonName.setBounds(new Rectangle(65, 15, 220, 35));
	cadastralPanel.setBounds(new Rectangle(5, 70, 700, 335));
	contentPanel.setBounds(new Rectangle(5, 5, 700, 450));
	contentPanel.setLayout(null);
	contentPanel.setSize(new Dimension(700, 515));
	searchPanel.setBounds(new Rectangle(5, 5, 700, 60));
	searchPanel.setLayout(null);
	tfIdentificationNumber.setBounds(new Rectangle(330, 15, 120, 35));
	tfCadastral.setBounds(new Rectangle(505, 15, 110, 35));
	tfNIS.setBounds(new Rectangle(505, 15, 110, 35));

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

	btnDelete.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  btnDelete_actionPerformed(e);
			      }

			  }
	);
	btnDelete.setToolTipText("Desvincular NIS del Catastro seleccionado");
        
	searchPanel.add(panelAlerta, null);
	searchPanel.add(tfCadastral, null);
	searchPanel.add(tfNIS, null);
	searchPanel.add(tfIdentificationNumber, null);
	searchPanel.add(tfPersonName, null);
	searchPanel.add(btnFind, null);
	btnEdit.setBounds(new Rectangle(610, 525, 40, 25));
	btnEdit.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnEdit_actionPerformed(e);
			       }

			   }
	);
	contentPanel.add(cadastralPanel, null);
	contentPanel.add(searchPanel, null);
	this.add(contentPanel, BorderLayout.CENTER);
	this.addButton(btnEdit);
	//this.addButton(btnAdd);
	this.addButton(btnDelete);
        
	cadastralPanel.getTable().setDragEnabled(true);
	cadastralPanel.getTable().setTransferHandler(new TableTransferHandler());
	tfPersonName.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});
	
	tfIdentificationNumber.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});

	tfCadastral.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});	
	tfNIS.getTextField().addKeyListener(new KeyAdapter() {
		    public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			    refresh();
		    }
		});     
	
	btnEdit.setToolTipText("Modificar NIS del Catastro seleccionado");
	setCadastralHeader();
	btnAdd.setEnabled(false);
	btnEdit.setEnabled(false);
	btnDelete.setEnabled(false);
	searchPanel.setBorder(BorderPanel.getBorderPanel("Buscar"));
	tfPersonName.setBounds(new Rectangle(10, 15, 220, 35));
	tfIdentificationNumber.setBounds(new Rectangle(245, 15, 120, 35));
	tfCadastral.setBounds(new Rectangle(380, 15, 110, 35));
	searchPanel.setSize(new Dimension(700, 60));
	panelAlerta.setBounds(new Rectangle(10, 20, 45, 35));
	panelAlerta.setVisible(false);
    }
    
    private void setCadastralHeader() {
	
	cadastralHeader.removeAllElements();
	cadastralHeader.addElement("*"); //idcatastro
	cadastralHeader.addElement("Catastro");
	cadastralHeader.addElement(Environment.lang.getProperty("NIS"));
	cadastralHeader.addElement(Environment.lang.getProperty("Name"));
	cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");
	cadastralHeader.addElement(Environment.lang.getProperty("DNI"));
	cadastralHeader.addElement("*");
	cadastralHeader.addElement(Environment.lang.getProperty("%"));
	cadastralHeader.addElement("*");
        
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
        
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
        
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement(Environment.lang.getProperty("Street"));
	cadastralHeader.addElement(Environment.lang.getProperty("Number"));
	cadastralHeader.addElement(Environment.lang.getProperty("Neighborhood"));
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("*");cadastralHeader.addElement("*");
	cadastralHeader.addElement("Es Rural");
	 
	 cadastralPanel.getTable().addMouseListener(new MouseAdapter() {
                                                    
						  public void mouseClicked(MouseEvent e) {
						      loadObjectSelected();
						      if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
							    tryToModifyNIS();
						      } else if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
							  btnDelete.setEnabled(true);
						      }
						  }

					      }
	 );
	 String params = "'" + tfPersonName.getString() + "','" + tfIdentificationNumber.getString() + "',0" + tfCadastral.getInteger() + ",0" + tfNIS.getInteger();
	 cadastralPanel.setParams("taxes.getAllCatastros", params, cadastralHeader);
    }
    
    public void setParentInternalFrame(ExtendedInternalFrame _e) {	
	super.setParentInternalFrame(_e);
	getParentInternalFrame().setInfo("Administración de Relaciones entre Catastros y NIS");
    }

    
    public void refresh() {
	String params = "'"+ tfPersonName.getString() +"','"+ tfIdentificationNumber.getString() + "',0" + tfCadastral.getInteger() + ",0" + tfNIS.getInteger();
	cadastralPanel.refresh(params);
	btnAdd.setEnabled(false);
	btnDelete.setEnabled(false);
	btnEdit.setEnabled(false);
	panelAlerta.setVisible(false);
	alertTimer.stop();
    }
    
    private void loadObjectSelected(){
	if (!dataRow.isEmpty()){
	    cadastral = new Cadastral();
	    cadastral.setIdCatastro(Integer.parseInt(dataRow.elementAt(0).toString()));
	    cadastral.setDomnudoc(dataRow.elementAt(10).toString());
	    cadastral.retrieveData();
	    btnAdd.setEnabled(true);
	    btnEdit.setEnabled(true);
	    btnDelete.setEnabled(true);
	    panelAlerta.setVisible(false);
	    alertTimer.stop();
	    if (cadastral.isConProblema()) {
		panelAlerta.setVisible(true);
		alertTimer.start();
	    }
	}
    }
    
    private void btnFind_actionPerformed(ActionEvent e) {
	refresh();
    }

    private void btnAdd_actionPerformed(ActionEvent e) {
    }

    private void btnEdit_actionPerformed(ActionEvent e) {
	tryToModifyNIS();
    }

    /**
     *OJO!!!!
     * Al asignar el MISMO NIS AL MISMO CATASTRO hay un BUG
     * Al intentar asignar NIS 0 o borrar NIS, hay otro BUG
     *
     *
     *
     * @throws Exception
     */
  private void tryToModifyNIS() {
	String _inputNIS = null;
	int _newNIS = -1;
	while (_inputNIS == null) {
	    _inputNIS = (String)JOptionPane.showInternalInputDialog(Environment.getActiveDesktop(), "Ingrese el nuevo NIS para el catastro Nº " + cadastral.getCatastro(), "Cambio de NIS para el catastro Nº " + cadastral.getCatastro(), JOptionPane.QUESTION_MESSAGE, null, null, "");
	    if (_inputNIS == null) {
		_inputNIS = "-1"; //Presionó escape o cancelar
	    }
	    try {
		_newNIS = Integer.parseInt(_inputNIS);
	    } catch (NumberFormatException ex) {
		Advisor.messageBox("Debe ingresar un número de NIS válido", "NIS erróneo");
		_inputNIS = null;
	    }
	}
	if (_newNIS != -1) {
	    //1) Reviso si el NIS lo tiene otro catastro
	    int _catastro = LibSQL.getInt("taxes.getcatastrobynis", _newNIS);
	    if (_catastro == 0) { //Nadie lo tiene asignado
		//Pregunto si cambio el NIS mostrando todos los datos, si acepta entonces muestro el mensaje completo de qué se cambió
		    if (Advisor.question("Cambiar NIS al catastro" + cadastral.getCatastro() , "¿Desea asignar el NIS " + _newNIS + " al catastro " + cadastral.getCatastro() + "?")) {
			if (LibSQL.getBoolean("taxes.setcadastralnis", cadastral.getCatastro() + "," + _newNIS)) {
			    Advisor.messageBox("El NIS se ha asignado con éxito", "Cambios grabados");
			    refresh();
			} else {
			    Advisor.messageBox("Ha ocurrido un error al intentar modificar el NIS", "Error");
			}
		    }
		} else {
		    //Pregunto si intercambio el NIS con el otro catastro mostrando todos los datos, si acepta entonces muestro el mensaje completo de qué se cambió
		    if (Advisor.question("¡¡NIS YA ASIGNADO!!", "¿Desea cambiarlo de todas formas?\nAl catastro " + _catastro + " se le asignará el NIS 0")) {
			if (LibSQL.getBoolean("taxes.setcadastralnis", cadastral.getCatastro() + "," + _newNIS)) {
			    Advisor.messageBox("El NIS se ha asignado con éxito", "Cambios grabados");
			    refresh();
			} else {
			    Advisor.messageBox("Ha ocurrido un error al intentar modificar el NIS", "Error");
			}
		    } else {
			Advisor.messageBox("El NIS no ha sido modificado", "Cambios cancelados");
		    }
		}
	}
    }

    private void controlBotones(){
	btnAdd.setEnabled(false);
	btnEdit.setEnabled(false);
	btnDelete.setEnabled(false);
    }

    private void btnDelete_actionPerformed(ActionEvent e) {
	if (Advisor.question("Desvincular NIS de un catastro" + cadastral.getCatastro() , "¿Desea desvincular el NIS al catastro " + cadastral.getCatastro() + "?")) {
	    if (LibSQL.getBoolean("taxes.setcadastralnis", cadastral.getCatastro() + "," + 0)) {
		Advisor.messageBox("El NIS se ha desvinculado con éxito", "Cambios grabados");
		refresh();
	    } else {
		Advisor.messageBox("Ha ocurrido un error al intentar desvincular el NIS", "Error");
	    }
	}
    }
       
}

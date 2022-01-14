package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Component;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.icons.IconTypes;

//

public class FindLabResult extends BasicDialog implements KeyListener {

    private BasicLabel lblSample = new BasicLabel();
    private BasicLabel lblDate = new BasicLabel();
    private BasicLabel lblResults = new BasicLabel();
    private BasicLabel jLabel1 = new BasicLabel();
    private BasicLabel lblDrill = new BasicLabel();
    private BasicLabel lblFindResult = new BasicLabel();
    private FindButton bSearch = new FindButton();
    private AcceptButton bNext = new AcceptButton();
    private CloseButton bCancel = new CloseButton();
    private AddButton bNewImport = new AddButton();
    private BasicPanel pFound = new BasicPanel();
    private BasicTextField tfSample = new BasicTextField();
    private BasicTextField tfDrill = new BasicTextField();
    private BasicCombo cbProject = new BasicCombo();
    private BasicCombo cbDate = new BasicCombo();
    private int Liminf = 0, CantReg1 = 200;
    private int[] tcol = { };
    private int[] tamcol = { 65, 90, 74, 88, 49, 67, 100 };
    private Grilla jgFindProject = new Grilla(CantReg1, tcol, tamcol, false, false);
    private Vector cabecera = new Vector();
    private Vector datos = new Vector();
    private BasicPanel pResults = new BasicPanel();
    private String ConsultaInicial = "", Consulta = "", ConsultaCount = "";
    final String TEXTO = "Laboratory Results";
    private PanelHeader panelHeader;
    int val = 0, bandera = 0;
    boolean date = false;
    boolean proj = false;
    ItemListener dateList;
    ItemListener projList;

    public FindLabResult() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(571, 440));
	this.setBounds(new Rectangle(10, 10, 571, 440));
	jgFindProject.setSpanishLanguage(false);
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblResults, null);
	this.getContentPane().add(lblFindResult, null);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(bNewImport, null);
	pResults.add(jgFindProject, null);
	this.getContentPane().add(pResults, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().setLayout(null);
	this.setTitle("Laboratory Results");
	this.setFont(new Font("Dialog", 1, 12));
	pFound.setBounds(new Rectangle(5, 45, 555, 55));
	pFound.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	pFound.setLayout(null);
	tfSample.setBounds(new Rectangle(270, 20, 75, 20));
	tfSample.setFont(new Font("Dialog", 1, 10));
	tfSample.setForeground(Color.red);
	tfSample.addKeyListener(this);
	lblSample.setText("Sample:");
	lblSample.setBounds(new Rectangle(270, 10, 40, 10));
	lblSample.setFont(new Font("Dialog", 1, 10));
	lblDate.setText("Date:");
	lblDate.setBounds(new Rectangle(365, 10, 30, 10));
	lblDate.setFont(new Font("Dialog", 1, 10));
	bSearch.setBounds(new Rectangle(495, 18, 40, 25));
	bSearch.setFont(new Font("Dialog", 1, 10));
	bSearch.setMnemonic('S');
	bSearch.setToolTipText("Search");
	bSearch.setSize(new Dimension(40, 25));
	bSearch.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bSearch_actionPerformed(e);
		    }

		});
	jgFindProject.setBounds(new Rectangle(5, 5, 545, 240));
	jgFindProject.Redimensiona();
	lblResults.setText(" Results");
	lblResults.setBounds(new Rectangle(10, 110, 45, 10));
	lblResults.setFont(new Font("Dialog", 1, 10));
	lblResults.setOpaque(true);
	pResults.setBounds(new Rectangle(5, 115, 555, 255));
	pResults.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	pResults.setLayout(null);
	cbProject.setBounds(new Rectangle(10, 20, 140, 20));
	jLabel1.setText("Project:");
	jLabel1.setBounds(new Rectangle(10, 8, 38, 14));
	jLabel1.setFont(new Font("Dialog", 1, 10));
	tfDrill.setBounds(new Rectangle(175, 20, 75, 20));
	tfDrill.setFont(new Font("Dialog", 1, 10));
	tfDrill.setForeground(Color.red);
	tfDrill.addKeyListener(this);
	tfSample.addKeyListener(this);
	cbDate.addKeyListener(this);
	cbProject.addKeyListener(this);
	lblDrill.setText("Drill:");
	lblDrill.setBounds(new Rectangle(175, 10, 40, 10));
	lblDrill.setFont(new Font("Dialog", 1, 10));
	cbDate.setBounds(new Rectangle(365, 20, 100, 20));
	cbDate.setSize(new Dimension(100, 20));
	bNewImport.setBounds(new Rectangle(260, 380, 40, 25));
	bNewImport.setFont(new Font("Dialog", 1, 10));
	bNewImport.setMnemonic('W');
	bNewImport.setToolTipText("New Import");
	bNewImport.setSize(new Dimension(40, 25));
	bNewImport.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNewDrill_actionPerformed(e);
		    }

		});
	lblFindResult.setText(" Find Lab. result");
	lblFindResult.setBounds(new Rectangle(10, 40, 90, 10));
	lblFindResult.setFont(new Font("Dialog", 1, 10));
	lblFindResult.setOpaque(true);
	bNext.setBounds(new Rectangle(520, 380, 40, 25));
	bNext.setMnemonic('N');
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setEnabled(false);
	bNext.setToolTipText("Next");
	bNext.setSize(new Dimension(40, 25));
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(5, 380, 40, 25));
	bCancel.setMnemonic('C');
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setToolTipText("Cancel");
	bCancel.setSize(new Dimension(40, 25));
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	pFound.add(cbDate, null);
	pFound.add(lblDrill, null);
	pFound.add(tfDrill, null);
	pFound.add(jLabel1, null);
	pFound.add(cbProject, null);
	pFound.add(bSearch, null);
	pFound.add(lblDate, null);
	pFound.add(lblSample, null);
	pFound.add(tfSample, null);
	this.getContentPane().add(pFound, null);
	jgFindProject.getTable().addKeyListener(new KeyAdapter() {

		    public void keyPressed(KeyEvent e) {
			jgFindProject_keyTyped(e);
		    }

		    public void keyReleased(KeyEvent e) {
			jgFindProject_keyTyped(e);
		    }

		    public void keyTyped(KeyEvent e) {
			jgFindProject_keyTyped(e);
		    }

		});
	jgFindProject.getTable().addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			/*if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1)
              {
                datos = jgFindProject.VDatos();
                bNext.setEnabled(true);
              }*/
		    }

		});
	jgFindProject.getTable().addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			/*if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1)
              {
                datos = jgFindProject.VDatos();
                ProjectMan AdministrarProyecto = new ProjectMan(datos.elementAt(0).toString());
                AdministrarProyecto.setModal(true);
                AdministrarProyecto.setVisible(true);
              }*/
		    }

		});
	DateComboInicial(consultaDateCombo());
	ProjectComboInicial(consultaProjectCombo());
	cbProject.addItemListener(new ItemListener() {

		    public void itemStateChanged(ItemEvent e) {
			cbProject_itemStateChanged(e);
		    }

		});
	//cbDate.addItemListener();
	/*projList = new ItemListener() {
                                       public void itemStateChanged(ItemEvent e) {
                                           cbProject_itemStateChanged(e);
                                       }
                                   };
        dateList = new ItemListener() {
                                   public void itemStateChanged(ItemEvent e) {
                                       cbDate_itemStateChanged(e);
                                   }
                               };
        */
	//DateComboInicial(consultaDateCombo());
	//ProjectComboInicial(consultaProjectCombo());
	val++;
	//jgFindProject.setSpanishLenguage(false);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void bNext_actionPerformed(ActionEvent e) {
	/*datos = jgFindProject.VDatos();
        ProjectMan AdministrarProyecto = new ProjectMan(datos.elementAt(0).toString());
        AdministrarProyecto.setModal(true);
        AdministrarProyecto.setVisible(true);
        setearCabecera();
        actualizaTabla();
        DateComboInicial(consultaDateCombo());*/
    }

    private void bSearch_actionPerformed(ActionEvent e) {
	setearCabecera();
	actualizaTabla();
	//armarConsulta();
    }

    private void setearCabecera() {
	cabecera.removeAllElements();
	cabecera.addElement("DDH");
	cabecera.addElement("*");
	cabecera.addElement("Sample Number");
	cabecera.addElement("Test");
	cabecera.addElement("Mineral");
	cabecera.addElement("Unit");
	cabecera.addElement("Amount");
	cabecera.addElement("Amount original");
    }

    private void actualizaTabla() {
	String consulta = armarConsulta();
	ConsultaCount = "SELECT count(*) FROM (" + consulta + ") as mat";
	jgFindProject.ActualizaTabla((BasicDialog)this, consulta, ConsultaCount, cabecera);
	//System.out.println("Consulta Count --> "+ConsultaCount);
	//System.out.println("Consulta FrmFrenteObras --> "+consulta);
    }

    private String armarConsulta() {
	String consulta = "SELECT ddh, samples.idsample,samplenumber,test, mineral,unit,amount,amount_orig " + " FROM drilling.drills, drilling.samples, drilling.lab_results, drilling.lab_import " + " WHERE drills.iddrill = samples.iddrill " + " AND samples.idsample = lab_results.idsample " + " AND drilling.lab_import.idlab_imp = lab_results.idlab_imp" + " AND lab_results.idsample > 0";
	String filtroProject = "";
	if (cbProject.getSelectedIndex() > 0) {
	    //System.out.println("idproject: " + cbProject.getSelectedItem().toString());
	    filtroProject = " AND drills.idproject = (Select idproject from drilling.projects where name = '" + cbProject.getSelectedItem().toString() + "' )";
	}
	String filtroDrill = "";
	if (!tfDrill.getText().equals("")) {
	    filtroDrill = " AND upper(drills.ddh) LIKE upper('%" + tfDrill.getText().trim() + "%') ";
	}
	String filtroSample = "";
	if (!tfSample.getText().equals("")) {
	    filtroSample = " AND upper(samples.samplenumber) LIKE upper('%" + tfSample.getText().trim() + "%') ";
	}
	String filtroDate = "";
	if (this.cbDate.getSelectedIndex() > 0) {
	    filtroDate = " AND drilling.lab_import.date_imp = '" + cbDate.getSelectedItem().toString() + "'";
	}
	consulta = consulta + filtroProject + filtroDrill + filtroSample + filtroDate;
	//System.out.println("Consulta --> "+consulta);
	return consulta;
    }

    private void jgFindProject_keyTyped(KeyEvent e) {
	/*try{
           if (datos!=jgFindProject.VDatos()){
                datos = jgFindProject.VDatos();
           }
        } catch (ArrayIndexOutOfBoundsException x){
            e.consume();

        }*/
    }

    public void keyReleased(KeyEvent key) {
    }

    public void keyPressed(KeyEvent key) {
	if (key.getKeyCode() == 10) {
	    setearCabecera();
	    Component c = (Component)key.getSource();
	    if (key.getSource() == this.tfSample) {
		actualizaTabla();
	    }
	    if (key.getSource() == this.tfDrill) {
		actualizaTabla();
	    }
	    if (key.getSource() == this.cbProject) {
		//actualizaTabla();
	    }
	    if (key.getSource() == this.cbDate) {
		//actualizaTabla();
	    }
	}
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    private String consultaProjectCombo() {
	ConsultaInicial = "SELECT distinct name FROM drilling.projects where idproject > 0 ";
	String filtroDate = "";
	//System.out.println("******item"+cbDate.getSelectedIndex());
	if (cbDate.getSelectedIndex() > 0) {
	    //System.out.println("****** no deberia entrar aca!");
	    /*filtroDate = ", drilling.drills, drilling.depth_interval, drilling.lab_results where projects.idproject = drills.idproject and drills.iddrill = depth_interval.iddrill and " +
                        "depth_interval.idsample = lab_results.idsample and lab_results.idlab_imp in " +
                        "(select idlab_imp from drilling.lab_import where date_imp = '" +
                        cbDate.getSelectedItem().toString() +
                        "')";*/
	}
	Consulta = ConsultaInicial + filtroDate;
	//System.out.println("Consulta --> "+Consulta);
	return Consulta;
    }

    private void ProjectComboInicial(String _consulta) {
	cbProject.removeItemListener(projList);
	String consultaCombo = _consulta;
	ResultSet combo = org.digitall.lib.sql.LibSQL.exQuery(consultaCombo);
	cbProject.removeAllItems();
	cbProject.addItem("Todos");
	//Proced.CargaCombo(cbProject,consultaCombo,"");
	try {
	    while (combo.next()) {
		cbProject.addItem(combo.getString("name"));
	    }
	} catch (SQLException e) {
	    //select distinct
	    e.printStackTrace();
	}
	cbProject.addItemListener(projList);
    }

    private String consultaDateCombo() {
	ConsultaInicial = "select distinct date_imp from drilling.lab_import ";
	String filtroDate = "";
	//cbDate.addItem("Todos");
	//cbDate.setSelectedIndex(0);
	if (cbProject.getSelectedIndex() > 0) {
	    String consulta = "select idproject from drilling.projects where name = '" + cbProject.getSelectedItem().toString() + "'";
	    filtroDate = ",drilling.lab_results where lab_import.idlab_imp = lab_results.idlab_imp and idsample in " + "(select idsample from drilling.depth_interval, drilling.drills where depth_interval.iddrill = drilling.drills.iddrill and " + "drilling.drills.idproject = " + org.digitall.lib.sql.LibSQL.getCampo(consulta) + ")";
	}
	Consulta = ConsultaInicial + filtroDate;
	//System.out.println("ConsultaDate --> "+Consulta);
	return Consulta;
    }

    private void DateComboInicial(String _consulta) {
	cbDate.removeItemListener(dateList);
	String consultaCombo = _consulta;
	ResultSet combo = org.digitall.lib.sql.LibSQL.exQuery(consultaCombo);
	cbDate.removeAllItems();
	cbDate.addItem("Todos");
	try {
	    while (combo.next()) {
		cbDate.addItem(combo.getString("date_imp"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	cbDate.addItemListener(dateList);
    }

    private void cbDate_itemStateChanged(ItemEvent e) {
	ProjectComboInicial(consultaProjectCombo());
    }

    private void cbProject_itemStateChanged(ItemEvent e) {
	DateComboInicial(consultaDateCombo());
    }

    private void bNewDrill_actionPerformed(ActionEvent e) {
	ImportFiles importar = new ImportFiles();
	importar.setModal(true);
	importar.setVisible(true);
	setearCabecera();
	actualizaTabla();
    }

}

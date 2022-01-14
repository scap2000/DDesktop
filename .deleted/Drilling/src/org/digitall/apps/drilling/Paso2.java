package org.digitall.apps.drilling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Vector;

import javax.swing.JList;
import javax.swing.SwingConstants;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.List;
import org.digitall.lib.components.ListItem;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.lib.components.buttons.UnAssignButton;
import org.digitall.lib.icons.IconTypes;

//

public class Paso2 extends PasoAsistente {

    private BasicTextField tfProject = new BasicTextField();
    private BasicLabel lblProject = new BasicLabel();
    private BasicLabel lblAlterations = new BasicLabel();
    private BasicLabel lblMineralizationAllowed = new BasicLabel();
    private AssignButton bAsing = new AssignButton();
    private UnAssignButton bUnassign = new UnAssignButton();
    private NextWizardButton bNext = new NextWizardButton();
    private PreviousWizardButton bBack = new PreviousWizardButton();
    private CloseButton bCancel = new CloseButton();
    private AddButton bNewElement = new AddButton();
     
    private BasicScrollPane spMineralizations = new BasicScrollPane();
    private BasicScrollPane spMineralizationsAllowed = new BasicScrollPane();
    private JList listMineralizations = new JList();
    private JList listMineralizationsAllowed = new JList();
    List mineralizationsList = new List();
    List mineralizationsAllowedList = new List();
    ListItem item = new ListItem();
    Vector c = new Vector();
    Vector vectorOrdenados = new Vector();
    Vector vectorSeleccionados = new Vector();
    Vector auxListMin = new Vector();
    private boolean instanciado = true, insert = false;
    final String TEXTO = "Allowed Mineralizations";
    private PanelHeader panelHeader;

    public Paso2() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(360, 334));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().setLayout(null);
	this.setTitle("New Project - Step 2");
	this.setBounds(new Rectangle(10, 10, 360, 339));
	tfProject.setBounds(new Rectangle(80, 45, 225, 20));
	tfProject.setEditable(false);
	tfProject.setForeground(Color.red);
	tfProject.setFont(new Font("Dialog", 1, 11));
	lblProject.setText("Project:");
	lblProject.setBounds(new Rectangle(25, 48, 50, 15));
	lblProject.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProject.setFont(new Font("Dialog", 1, 11));
	lblProject.setForeground(Color.blue);
	//new Rectangle(0, 270, 355, 2));
	spMineralizations.setBounds(new Rectangle(10, 90, 140, 175));
	spMineralizationsAllowed.setBounds(new Rectangle(205, 90, 140, 175));
	listMineralizations.setFont(new Font("Dialog", 1, 10));
	listMineralizations.addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			listMineralizations_mouseClicked(e);
		    }

		});
	listMineralizationsAllowed.setBounds(new Rectangle(0, 0, 140, 175));
	listMineralizationsAllowed.setFont(new Font("Dialog", 1, 10));
	listMineralizationsAllowed.addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			listMineralizationsAllowed_mouseClicked(e);
		    }

		});
	bNewElement.setBounds(new Rectangle(160, 235, 35, 25));
	bNewElement.setToolTipText("Add new element");
	bNewElement.setMnemonic('n');
	bNewElement.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNewMineralization_actionPerformed(e);
		    }

		});
	lblAlterations.setText("Mineralizations");
	lblAlterations.setBounds(new Rectangle(10, 75, 80, 15));
	lblAlterations.setFont(new Font("Dialog", 1, 10));
	lblMineralizationAllowed.setText("Mineralizations allowed");
	lblMineralizationAllowed.setBounds(new Rectangle(207, 75, 125, 15));
	lblMineralizationAllowed.setFont(new Font("Dialog", 1, 10));
	bAsing.setBounds(new Rectangle(160, 135, 35, 25));
	bAsing.setFont(new Font("Dialog", 1, 10));
	bAsing.setToolTipText("Assign");
	bAsing.setMnemonic('A');
	bAsing.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bAsing_actionPerformed(e);
		    }

		});
	bUnassign.setBounds(new Rectangle(160, 170, 35, 25));
	bUnassign.setToolTipText("Remove");
	bUnassign.setMnemonic('R');
	bUnassign.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bUnassign_actionPerformed(e);
		    }

		});
	bNext.setBounds(new Rectangle(305, 275, 40, 25));
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setMnemonic('N');
	bNext.setToolTipText("Next");
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bBack.setBounds(new Rectangle(255, 275, 40, 25));
	bBack.setFont(new Font("Dialog", 1, 10));
	bBack.setMnemonic('B');
	bBack.setToolTipText("Back");
	bBack.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bBack_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(10, 275, 40, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setMnemonic('C');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	this.getContentPane().add(bNewElement, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bBack, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().add(bUnassign, null);
	this.getContentPane().add(bAsing, null);
	this.getContentPane().add(lblMineralizationAllowed, null);
	this.getContentPane().add(lblAlterations, null);
	spMineralizationsAllowed.getViewport().add(listMineralizationsAllowed, null);
	this.getContentPane().add(spMineralizationsAllowed, null);
	spMineralizations.getViewport().add(listMineralizations, BorderLayout.CENTER);
	this.getContentPane().add(spMineralizations, null);
	//
	this.getContentPane().add(lblProject, null);
	this.getContentPane().add(tfProject, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void setearDatos() {
	tfProject.setText(c(1));
	if (instanciado) {
	    String Q = "SELECT idmin_tab as id, name as dato FROM tabs.mineralization_tabs WHERE status <> '*' AND idmin_tab > 0 Order By name";
	    mineralizationsList.getListFromQuery(Q);
	    listMineralizations.setListData(mineralizationsList.getNombres());
	    instanciado = false;
	}
    }

    private void bNext_actionPerformed(ActionEvent e) {
	settingFields();
	siguiente();
    }

    private void bBack_actionPerformed(ActionEvent e) {
	previo();
	//Aca tiene que ir un cartel que avise de que la carga se cancela y que no se guardarÃ¡n los datos
	this.dispose();
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void settingFields() {
	setMineralizationAllowed(mineralizationsAllowedList);
    }

    private void bOk_actionPerformed(ActionEvent e) {
    }

    public void mostrar() {
	setearDatos();
	this.setVisible(true);
    }

    private void listMineralizations_mouseClicked(MouseEvent e) {
	if ((e.getClickCount() == 2) && (listMineralizations.getSelectedIndex() >= 0)) {
	    mineralizationsList.swapItem(mineralizationsAllowedList, mineralizationsList.getIndexFromString(listMineralizations.getSelectedValue().toString()));
	    listMineralizations.setListData(mineralizationsList.getNombres());
	    listMineralizationsAllowed.setListData(mineralizationsAllowedList.getNombres());
	}
    }

    private void listMineralizationsAllowed_mouseClicked(MouseEvent e) {
	if ((e.getClickCount() == 2) && (listMineralizationsAllowed.getSelectedIndex() >= 0)) {
	    mineralizationsAllowedList.swapItem(mineralizationsList, mineralizationsAllowedList.getIndexFromString(listMineralizationsAllowed.getSelectedValue().toString()));
	    listMineralizations.setListData(mineralizationsList.getNombres());
	    listMineralizationsAllowed.setListData(mineralizationsAllowedList.getNombres());
	}
    }

    private void bAsing_actionPerformed(ActionEvent e) {
	if (listMineralizations.getSelectedIndices().length > 0) {
	    int[] selecteds = listMineralizations.getSelectedIndices();
	    for (int i = selecteds.length - 1; i >= 0; i--) {
		mineralizationsList.swapItem(mineralizationsAllowedList, selecteds[i]);
		listMineralizations.setListData(mineralizationsList.getNombres());
		listMineralizationsAllowed.setListData(mineralizationsAllowedList.getNombres());
	    }
	}
    }

    private void bUnassign_actionPerformed(ActionEvent e) {
	if (listMineralizationsAllowed.getSelectedIndices().length > 0) {
	    int[] selecteds = listMineralizationsAllowed.getSelectedIndices();
	    for (int i = selecteds.length - 1; i >= 0; i--) {
		mineralizationsAllowedList.swapItem(mineralizationsList, selecteds[i]);
		listMineralizations.setListData(mineralizationsList.getNombres());
		listMineralizationsAllowed.setListData(mineralizationsAllowedList.getNombres());
	    }
	}
    }

    private void bNewMineralization_actionPerformed(ActionEvent e) {
	if (e.getModifiers() == 17) {
	    auxListMin = mineralizationsList.getIds();
	    System.out.println("auxListMin = " + auxListMin);
	    /*Pass pass = new Pass("min");
            pass.setModal(true);
            pass.setVisible(true);*/
	    NewElement nuevo = new NewElement("min", "insert");
	    nuevo.setModal(true);
	    nuevo.setVisible(true);
	    refreshList();
	}
    }

    private void refreshList() {
	Vector aux = new Vector();
	String Q = "SELECT idmin_tab as id, name as dato FROM tabs.mineralization_tabs WHERE status <> '*' AND idmin_tab > 0 Order By name";
	mineralizationsList.getListFromQuery(Q);
	//listMineralizations.setListData(mineralizationsList.getNombres());
	aux = this.mineralizationsAllowedList.getIds();
	int id = 0;
	for (int i = 0; i < aux.size(); i++) {
	    id = Integer.parseInt(aux.elementAt(i).toString());
	    mineralizationsList.removeItem(id);
	}
	listMineralizations.setListData(mineralizationsList.getNombres());
    }

    public void estadoConsulta(boolean _insert) {
	insert = _insert;
    }

}

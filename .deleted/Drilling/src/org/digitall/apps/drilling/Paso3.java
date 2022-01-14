package org.digitall.apps.drilling;

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
//public class Paso3 extends BasicDialog

public class Paso3 extends PasoAsistente {

    private BasicTextField tfProject = new BasicTextField();
    private BasicLabel lblProject1 = new BasicLabel();
    private BasicLabel lblAlterationsAllowed = new BasicLabel();
    private BasicLabel lblAlterations = new BasicLabel();
    private AssignButton bAssign = new AssignButton();
    private UnAssignButton bUnassign = new UnAssignButton();
    private NextWizardButton bNext = new NextWizardButton();
    private PreviousWizardButton bBack = new PreviousWizardButton();
    private CloseButton bCancel = new CloseButton();
    private AddButton bNewElement = new AddButton();
     
    private BasicScrollPane spAlterations = new BasicScrollPane();
    private BasicScrollPane spAlterationallowed = new BasicScrollPane();
    private JList listAlterations = new JList();
    private JList listAlterationsAllowed = new JList();
    private boolean instanciado = true;
    private List alterationsList = new List();
    private List alterationsAllowedList = new List();
    private ListItem item = new ListItem();
    private Vector auxListAlt = new Vector();
    private final String TEXTO = "Allowed Alterations";
    private PanelHeader panelHeader;

    public Paso3() {
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
	this.setTitle("New Project - Step 3");
	this.setBounds(new Rectangle(10, 10, 360, 339));
	tfProject.setBounds(new Rectangle(85, 45, 225, 20));
	tfProject.setEditable(false);
	tfProject.setFont(new Font("Dialog", 1, 12));
	tfProject.setForeground(Color.red);
	//new Rectangle(0, 272, 350, 2));
	spAlterations.setBounds(new Rectangle(10, 90, 140, 175));
	spAlterationallowed.setBounds(new Rectangle(205, 90, 140, 175));
	spAlterationallowed.getViewport().setLayout(null);
	listAlterations.setBounds(new Rectangle(60, 45, 140, 175));
	listAlterations.setFont(new Font("Dialog", 1, 10));
	listAlterations.addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			listAlterations_mouseClicked(e);
		    }

		});
	listAlterationsAllowed.setBounds(new Rectangle(0, 0, 140, 175));
	listAlterationsAllowed.setFont(new Font("Dialog", 1, 10));
	listAlterationsAllowed.addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			listAlterationsAllowed_mouseClicked(e);
		    }

		});
	bNewElement.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNewElement_actionPerformed(e);
		    }

		});
	bNewElement.setBounds(new Rectangle(160, 240, 35, 25));
	bNewElement.setToolTipText("Add new alteration");
	bNewElement.setMnemonic('n');
	lblProject1.setText("Project:");
	lblProject1.setBounds(new Rectangle(25, 50, 50, 15));
	lblProject1.setHorizontalAlignment(SwingConstants.RIGHT);
	lblProject1.setFont(new Font("Dialog", 1, 11));
	lblProject1.setForeground(Color.blue);
	lblAlterationsAllowed.setText("Alterations allowed");
	lblAlterationsAllowed.setBounds(new Rectangle(205, 75, 125, 15));
	lblAlterationsAllowed.setFont(new Font("Dialog", 1, 10));
	lblAlterations.setText("Alterations");
	lblAlterations.setBounds(new Rectangle(10, 75, 80, 15));
	lblAlterations.setFont(new Font("Dialog", 1, 10));
	bAssign.setBounds(new Rectangle(159, 140, 35, 25));
	bAssign.setFont(new Font("Dialog", 1, 10));
	bAssign.setToolTipText("Assign");
	bAssign.setMnemonic('A');
	bAssign.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bAssign_actionPerformed(e);
		    }

		});
	bUnassign.setBounds(new Rectangle(159, 175, 35, 25));
	bUnassign.setToolTipText("Remove");
	bUnassign.setMnemonic('R');
	bUnassign.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bUnassign_actionPerformed(e);
		    }

		});
	bNext.setBounds(new Rectangle(305, 280, 40, 25));
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setMnemonic('N');
	bNext.setToolTipText("Next");
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bBack.setBounds(new Rectangle(250, 280, 40, 25));
	bBack.setFont(new Font("Dialog", 1, 10));
	bBack.setMnemonic('B');
	bBack.setToolTipText("Back");
	bBack.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bBack_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(10, 280, 40, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setMnemonic('C');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	this.getContentPane().add(bNewElement, null);
	this.getContentPane().add(lblAlterations, null);
	this.getContentPane().add(lblAlterationsAllowed, null);
	this.getContentPane().add(lblProject1, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bBack, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().add(bUnassign, null);
	this.getContentPane().add(bAssign, null);
	spAlterationallowed.getViewport().add(listAlterationsAllowed, null);
	this.getContentPane().add(spAlterationallowed, null);
	spAlterations.getViewport().add(listAlterations, null);
	this.getContentPane().add(spAlterations, null);
	//
	this.getContentPane().add(tfProject, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void setearDatos() {
	tfProject.setText(c(1));
	if (instanciado) {
	    String Q = "SELECT idalt_tab as id, name as dato FROM tabs.alteration_tabs WHERE status <> '*' AND idalt_tab > 0 Order By name";
	    alterationsList.getListFromQuery(Q);
	    listAlterations.setListData(alterationsList.getNombres());
	    instanciado = false;
	}
    }

    private void bNext_actionPerformed(ActionEvent e) {
	settingFields();
	siguiente();
    }

    private void settingFields() {
	setAlterationsAllowed(alterationsAllowedList);
    }

    private void bBack_actionPerformed(ActionEvent e) {
	previo();
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	//Aca tiene que ir un cartel que avise de que la carga se cancela y que no se guardarÃ¡n los datos
	this.dispose();
    }

    public void mostrar() {
	setearDatos();
	this.setVisible(true);
    }

    private void listAlterations_mouseClicked(MouseEvent e) {
	if ((e.getClickCount() == 2) && (listAlterations.getSelectedIndex() >= 0)) {
	    alterationsList.swapItem(alterationsAllowedList, alterationsList.getIndexFromString(listAlterations.getSelectedValue().toString()));
	    listAlterations.setListData(alterationsList.getNombres());
	    listAlterationsAllowed.setListData(alterationsAllowedList.getNombres());
	}
    }

    private void listAlterationsAllowed_mouseClicked(MouseEvent e) {
	if ((e.getClickCount() == 2) && (listAlterationsAllowed.getSelectedIndex() >= 0)) {
	    alterationsAllowedList.swapItem(alterationsList, alterationsAllowedList.getIndexFromString(listAlterationsAllowed.getSelectedValue().toString()));
	    listAlterations.setListData(alterationsList.getNombres());
	    listAlterationsAllowed.setListData(alterationsAllowedList.getNombres());
	}
    }

    private void bAssign_actionPerformed(ActionEvent e) {
	if (listAlterations.getSelectedIndices().length > 0) {
	    int[] selecteds = listAlterations.getSelectedIndices();
	    for (int i = selecteds.length - 1; i >= 0; i--) {
		alterationsList.swapItem(alterationsAllowedList, selecteds[i]);
		listAlterations.setListData(alterationsList.getNombres());
		listAlterationsAllowed.setListData(alterationsAllowedList.getNombres());
	    }
	}
    }

    private void bUnassign_actionPerformed(ActionEvent e) {
	if (listAlterationsAllowed.getSelectedIndex() != -1) {
	    int[] selecteds = listAlterationsAllowed.getSelectedIndices();
	    for (int i = selecteds.length - 1; i >= 0; i--) {
		alterationsAllowedList.swapItem(alterationsList, selecteds[i]);
		listAlterations.setListData(alterationsList.getNombres());
		listAlterationsAllowed.setListData(alterationsAllowedList.getNombres());
	    }
	}
    }

    private void bNewElement_actionPerformed(ActionEvent e) {
	auxListAlt = alterationsList.getIds();
	NewElement nuevo = new NewElement("alt", "insert");
	nuevo.setModal(true);
	nuevo.setVisible(true);
	refreshList();
    }

    private void refreshList() {
	Vector aux = new Vector();
	String Q = "SELECT idalt_tab as id, name as dato FROM tabs.alteration_tabs WHERE status <> '*' AND idalt_tab > 0 Order By name";
	alterationsList.getListFromQuery(Q);
	aux = alterationsAllowedList.getIds();
	int id = 0;
	for (int i = 0; i < aux.size(); i++) {
	    id = Integer.parseInt(aux.elementAt(i).toString());
	    this.alterationsList.removeItem(id);
	}
	listAlterations.setListData(alterationsList.getNombres());
    }

}

package org.digitall.apps.drilling;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.List;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.UnAssignButton;
import org.digitall.lib.icons.IconTypes;

//

public class ChangeAltMin extends BasicDialog {

    private BasicLabel lblAllowedList = new BasicLabel("<html>Mineralizations<br>Allowed</html>");
    private BasicLabel lblList = new BasicLabel();
    private UnAssignButton bUnassign = new UnAssignButton();
    private AssignButton bAssign = new AssignButton();
    private CloseButton bCancel = new CloseButton();
    private AcceptButton bOk = new AcceptButton();
    private BasicPanel jPanel1 = new BasicPanel();
    String idProject = "";
    int opcion;
    private BasicScrollPane spAlterations = new BasicScrollPane();
    private BasicScrollPane spAlterationallowed = new BasicScrollPane();
    private JList list = new JList();
    private JList listAllowed = new JList();
    List List = new List();
    List AllowedList = new List();
    List listAct = new List();
    private PanelHeader panelHeader;
    final String TEXTO = "<html><p align=center></p></html>";
    Vector auxList, auxAllowedList, auxListInicial;
    Conjunto conjunto = new Conjunto();
    private AddButton bNewElement = new AddButton();

    public ChangeAltMin(List _listAct, int _opcion, String _idProject) {
	try {
	    opcion = _opcion;
	    String textoMain = "";
	    if (opcion == 1) {
		textoMain = "<html><p align=center>Alter Mineralizations</p></html>";
	    } else {
		textoMain = "<html><p align=center>Alter Alterations</p></html>";
	    }
	    idProject = _idProject;
	    AllowedList = _listAct;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(371, 282));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(bOk, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(jPanel1, null);
	this.getContentPane().setLayout(null);
	//this.getContentPane().add(LabelMain);
	auxListInicial = AllowedList.getIds();
	System.out.println("auxListInicial = " + auxListInicial);
	spAlterations.setBounds(new Rectangle(10, 30, 145, 140));
	list.setFont(new Font("Dialog", 1, 10));
	list.setBounds(new Rectangle(0, 0, 105, 125));
	list.addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			listAlterations_mouseClicked(e);
		    }

		});
	spAlterationallowed.setBounds(new Rectangle(200, 30, 145, 140));
	spAlterationallowed.setSize(new Dimension(145, 140));
	listAllowed.setFont(new Font("Dialog", 1, 10));
	listAllowed.setBounds(new Rectangle(0, 0, 100, 125));
	listAllowed.setSize(new Dimension(105, 125));
	listAllowed.addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			listAlterationsAllowed_mouseClicked(e);
		    }

		});
	lblAllowedList.setBounds(new Rectangle(200, 5, 145, 25));
	lblAllowedList.setFont(new Font("Dialog", 1, 10));
	lblAllowedList.setText("null");
	lblList.setBounds(new Rectangle(10, 5, 145, 25));
	lblList.setFont(new Font("Dialog", 1, 10));
	bUnassign.setBounds(new Rectangle(160, 95, 35, 25));
	bUnassign.setToolTipText("Remove");
	bUnassign.setMnemonic('R');
	bUnassign.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bUnassign_actionPerformed(e);
		    }

		});
	bAssign.setBounds(new Rectangle(160, 50, 35, 25));
	bAssign.setFont(new Font("Dialog", 1, 10));
	bAssign.setToolTipText("Assign");
	bAssign.setMnemonic('A');
	bAssign.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bAssign_actionPerformed(e);
		    }

		});
	bNewElement.setBounds(new Rectangle(160, 145, 35, 25));
	bNewElement.setToolTipText("Add new element");
	bNewElement.setMnemonic('n');
	bNewElement.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNewElement_actionPerformed(e);
		    }

		});
	jPanel1.setBounds(new Rectangle(5, 40, 355, 180));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	bCancel.setBounds(new Rectangle(5, 225, 40, 25));
	bCancel.setToolTipText("Cancel");
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setMnemonic('C');
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	bOk.setBounds(new Rectangle(320, 225, 40, 25));
	bOk.setFont(new Font("Dialog", 1, 10));
	bOk.setMnemonic('O');
	bOk.setToolTipText("Ok");
	bOk.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bOk_actionPerformed(e);
		    }

		});
	spAlterations.getViewport().add(list, null);
	jPanel1.add(spAlterations, null);
	spAlterationallowed.getViewport().add(listAllowed, null);
	jPanel1.add(spAlterationallowed, null);
	jPanel1.add(lblList, null);
	jPanel1.add(lblAllowedList, null);
	jPanel1.add(bAssign, null);
	jPanel1.add(bUnassign, null);
	jPanel1.add(bNewElement, null);
	auxList = new Vector();
	auxAllowedList = new Vector();
	this.setModal(true);
	this.setTitle("Change Alt-Min");
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	setearList();
    }

    private void bNewElement_actionPerformed(ActionEvent e) {
	//auxListAlt = alterationsList.getIds();
	NewElement nuevo;
	if (opcion == 1) {
	    nuevo = new NewElement("min", "insert");
	} else {
	    nuevo = new NewElement("alt", "insert");
	}
	nuevo.setModal(true);
	nuevo.setVisible(true);
	setearList();
    }

    private void setearList() {
	if (opcion == 1) {
	    lblList.setText("Mineralizations");
	    lblAllowedList.setText("<html>Mineralizations<br>Allowed</html>");
	    String Q = "SELECT idmin_tab as id, name as dato FROM tabs.mineralization_tabs " + "Where (status <> '*' And idmin_tab > 0) Order By name";
	    List.getListFromQuery(Q);
	    list.setListData(List.getNombres());
	} else {
	    lblList.setText("Alterations");
	    lblAllowedList.setText("<html>Alterations<br>Allowed</html>");
	    String Q = "SELECT idalt_tab as id, name as dato FROM tabs.alteration_tabs " + "Where (status <> '*' And idalt_tab > 0) Order By name";
	    List.getListFromQuery(Q);
	    list.setListData(List.getNombres());
	}
	actualizarItemList();
    }

    private void actualizarItemList() {
	int id = 0;
	for (int i = 0; i < auxListInicial.size(); i++) {
	    id = Integer.parseInt(auxListInicial.elementAt(i).toString());
	    List.removeItem(id);
	}
	list.setListData(List.getNombres());
	listAllowed.setListData(AllowedList.getNombres());
    }

    private void setearDatosIniciales() {
	String Q;
	if (opcion == 1) {
	    Q = "SELECT idmin_tab as id, name as dato FROM tabs.mineralization_tabs " + "Where (status <> '*' And idmin_tab > 0) Order By name";
	} else {
	    Q = "SELECT idalt_tab as id, name as dato FROM tabs.alteration_tabs " + "Where (status <> '*' And idalt_tab > 0) Order By name";
	}
	AllowedList.getListFromQuery(Q);
	listAllowed.setListData(AllowedList.getNombres());
	Vector listaEliminar = conjunto.diferencia(AllowedList.getIds(), auxListInicial);
	int id = 0;
	for (int i = 0; i < listaEliminar.size(); i++) {
	    id = Integer.parseInt(listaEliminar.elementAt(i).toString());
	    //List.swapItem(AllowedList,id);
	    AllowedList.removeItem(id);
	}
	listAllowed.setListData(AllowedList.getNombres());
    }

    private void listAlterations_mouseClicked(MouseEvent e) {
	if ((e.getClickCount() == 2) && (list.getSelectedIndex() >= 0)) {
	    List.swapItem(AllowedList, List.getIndexFromString(list.getSelectedValue().toString()));
	    list.setListData(List.getNombres());
	    listAllowed.setListData(AllowedList.getNombres());
	}
    }

    private void listAlterationsAllowed_mouseClicked(MouseEvent e) {
	if ((e.getClickCount() == 2) && (listAllowed.getSelectedIndex() >= 0)) {
	    AllowedList.swapItem(List, AllowedList.getIndexFromString(listAllowed.getSelectedValue().toString()));
	    list.setListData(List.getNombres());
	    listAllowed.setListData(AllowedList.getNombres());
	}
    }

    private void bUnassign_actionPerformed(ActionEvent e) {
	if (listAllowed.getSelectedIndices().length > 0) {
	    int[] selecteds = listAllowed.getSelectedIndices();
	    for (int i = selecteds.length - 1; i >= 0; i--) {
		AllowedList.swapItem(List, selecteds[i]);
		list.setListData(List.getNombres());
		listAllowed.setListData(AllowedList.getNombres());
	    }
	}
    }

    private void bAssign_actionPerformed(ActionEvent e) {
	if (list.getSelectedIndices().length > 0) {
	    int[] selecteds = list.getSelectedIndices();
	    for (int i = selecteds.length - 1; i >= 0; i--) {
		List.swapItem(AllowedList, selecteds[i]);
		list.setListData(List.getNombres());
		listAllowed.setListData(AllowedList.getNombres());
	    }
	}
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	setearDatosIniciales();
	this.dispose();
    }

    private void bOk_actionPerformed(ActionEvent e) {
	this.dispose();
    }

}

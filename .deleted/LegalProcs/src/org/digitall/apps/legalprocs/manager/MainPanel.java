package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;

public class MainPanel extends BasicContainerPanel {

    private BasicPanel panelOpciones = new BasicPanel();
    private BasicLabel lbnlOpciones = new BasicLabel();
    private JRadioButton rbExplorationScheme = new JRadioButton();
    private JRadioButton rbExplorationRequest = new JRadioButton();
    private JRadioButton rbCExpte = new JRadioButton();
    private ButtonGroup group = new ButtonGroup();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnCancel = new CloseButton();
    private final int FRAME = 1;
    private final int INTERNALFRAME = 2;
    private final int DIALOG = 3;
    private int parentType = -1;
    private Component parent;
    private NewFile newFile;

    public MainPanel(BasicInternalFrame _parent) {
	try {
	    parent = _parent;
	    parentType = INTERNALFRAME;
	    newFile = new NewFile();
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(342, 126));
	panelOpciones.setBounds(new Rectangle(5, 7, 330, 75));
	panelOpciones.setLayout(null);
	panelOpciones.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	rbExplorationScheme.setText("Programa Mínimo de Exploración");
	rbExplorationScheme.setBounds(new Rectangle(20, 30, 230, 15));
	rbExplorationScheme.setFont(new Font("Default", 1, 11));
	rbExplorationScheme.setHorizontalAlignment(SwingConstants.LEFT);
	rbExplorationScheme.setMnemonic('M');
	rbExplorationRequest.setText("Permiso de Exploración");
	rbExplorationRequest.setBounds(new Rectangle(20, 10, 175, 15));
	rbExplorationRequest.setFont(new Font("Default", 1, 11));
	rbExplorationRequest.setHorizontalAlignment(SwingConstants.LEFT);
	rbExplorationRequest.setMnemonic('E');
	rbCExpte.setText("Carga de Expedientes");
	rbCExpte.setBounds(new Rectangle(20, 50, 230, 15));
	rbCExpte.setFont(new Font("Default", 1, 11));
	rbCExpte.setHorizontalAlignment(SwingConstants.LEFT);
	lbnlOpciones.setText(" Opciones");
	lbnlOpciones.setBounds(new Rectangle(15, 0, 70, 15));
	lbnlOpciones.setFont(new Font("Default", 1, 11));
	lbnlOpciones.setOpaque(false);
	btnAccept.setBounds(new Rectangle(295, 90, 40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnCancel.setBounds(new Rectangle(5, 90, 40, 25));
	btnCancel.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     bCancel_actionPerformed(e);
				 }

			     }
	);
	panelOpciones.add(rbExplorationScheme, null);
	panelOpciones.add(rbExplorationRequest, null);
	panelOpciones.add(rbCExpte, null);
	panelOpciones.add(rbExplorationRequest, null);
	panelOpciones.add(rbExplorationScheme, null);
	this.add(btnCancel, null);
	this.add(btnAccept, null);
	this.add(lbnlOpciones, null);
	this.add(panelOpciones, null);
	group.add(rbExplorationRequest);
	group.add(rbExplorationScheme);
	group.add(rbCExpte);
	rbExplorationRequest.setSelected(true);
    }

    private void dispose() {
	switch (parentType) {
	    case DIALOG :
		((BasicDialog)parent).dispose();
		break;
	    case INTERNALFRAME :
		((BasicInternalFrame)parent).hide();
		break;
	    case FRAME :
		((JFrame)parent).dispose();
		break;
	}
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	if (rbExplorationRequest.isSelected()) {
	    ExplorationRequest explorationRequest = new ExplorationRequest("ER");
	    explorationRequest.show();
	}
	if (rbCExpte.isSelected()) {
	    NewFile newFile = new NewFile();
	    newFile.show();
	}
    }

}

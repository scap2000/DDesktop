package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.icons.IconTypes;

//

public class Message extends BasicDialog {

    private BasicPanel jPanel1 = new BasicPanel();
    private PanelHeader panelHeader;
    final String TEXTO = "Errors Report";
    ButtonGroup grupo = new ButtonGroup();
    String texto = "", valorRetorno = "";
    private BasicLabel lblMensaje1 = new BasicLabel();
    private BasicLabel lblOptions = new BasicLabel();
    private BasicPanel jPanel2 = new BasicPanel();
    private BasicButton btnFix = new BasicButton();
    private BasicButton btnIgnore = new BasicButton();
    private BasicButton btnCancel = new BasicButton();

    public Message(String _texto, String _valorRetorno) {
	//this(null, "", false);
	try {
	    texto = _texto;
	    valorRetorno = _valorRetorno;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Message(Frame parent, String title, boolean modal) {
	super(parent, title, modal);
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(265, 208));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	jPanel2.add(lblMensaje1, null);
	this.getContentPane().add(jPanel2, null);
	this.getContentPane().add(lblOptions, null);
	this.getContentPane().add(panelHeader, null);
	//this.getContentPane().add(bOk, null);
	this.getContentPane().add(jPanel1, null);
	//this.getContentPane().add(rbOmitir, null);
	//this.getContentPane().add(rbCancelar, null);
	//this.getContentPane().add(rbCorregir, null);
	this.getContentPane().setLayout(null);
	this.setTitle("Errors Report");
	lblMensaje1.setText("Incomplete process: " + texto + " errors");
	//lblMensaje.setText(texto);
	jPanel1.setBounds(new Rectangle(5, 85, 250, 85));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	/*rbOmitir.setText("Omit errors");
        rbOmitir.setBounds(new Rectangle(120, 210, 80, 15));
        rbOmitir.setFont(new Font("Dialog", 1, 11));
        rbOmitir.setMnemonic('t');
        rbOmitir.addActionListener(new ActionListener() {

                                       public void actionPerformed(ActionEvent e) {
                                           rbOmitir_actionPerformed(e);
                                       }
                                   }
        );
        rbCorregir.setText("Correct errors");
        rbCorregir.setBounds(new Rectangle(30, 210, 80, 15));
        rbCorregir.setFont(new Font("Dialog", 1, 11));
        rbCorregir.setSelected(true);
        rbCorregir.setMnemonic('r');
        rbCorregir.addActionListener(new ActionListener() {

                                         public void actionPerformed(ActionEvent e) {
                                             rbCorregir_actionPerformed(e);
                                         }
                                     }
        );
        rbCancelar.setText("Cancel import");
        rbCancelar.setBounds(new Rectangle(35, 230, 75, 15));
        rbCancelar.setFont(new Font("Dialog", 1, 11));
        rbCancelar.setMnemonic('l');
        rbCancelar.addActionListener(new ActionListener() {

                                         public void actionPerformed(ActionEvent e) {
                                             rbCancelar_actionPerformed(e);
                                         }
                                     }
        );
        grupo.add(rbOmitir);
        grupo.add(rbCancelar);
        grupo.add(rbCorregir);*/
	//tfCorregir.setEnabled(false);
	/*bOk.setBounds(new Rectangle(215, 175, 40, 25));
        bOk.setMnemonic('O');
        bOk.setSize(new Dimension(40, 25));
        bOk.setHorizontalTextPosition(SwingConstants.LEFT);
        bOk.setToolTipText("Ok");
        bOk.addActionListener(new ActionListener() {

                                  public void actionPerformed(ActionEvent e) {
                                      bOk_actionPerformed(e);
                                  }
                              }
        );*/
	lblMensaje1.setBounds(new Rectangle(5, 7, 240, 20));
	lblMensaje1.setFont(new Font("Dialog", 1, 11));
	lblMensaje1.setForeground(Color.red);
	lblOptions.setBounds(new Rectangle(10, 76, 55, 15));
	lblOptions.setText(" Options");
	lblOptions.setFont(new Font("Dialog", 1, 11));
	lblOptions.setOpaque(true);
	jPanel2.setBounds(new Rectangle(5, 35, 250, 35));
	jPanel2.setLayout(null);
	jPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	btnFix.setText("Fix");
	btnFix.setBounds(new Rectangle(15, 15, 100, 25));
	btnFix.setMnemonic('F');
	btnFix.setFont(new Font("Dialog", 1, 11));
	btnFix.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnFix_actionPerformed(e);
		    }

		});
	btnIgnore.setText("Ignore");
	btnIgnore.setBounds(new Rectangle(15, 50, 100, 25));
	btnIgnore.setMnemonic('I');
	btnIgnore.setFont(new Font("Dialog", 1, 11));
	btnIgnore.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnIgnore_actionPerformed(e);
		    }

		});
	btnCancel.setText("Cancel");
	btnCancel.setBounds(new Rectangle(140, 50, 100, 25));
	btnCancel.setMnemonic('C');
	btnCancel.setFont(new Font("Dialog", 1, 11));
	btnCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnCancel_actionPerformed(e);
		    }

		});
	jPanel1.add(btnCancel, null);
	jPanel1.add(btnFix, null);
	jPanel1.add(btnIgnore, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    public String getOpcion() {
	return valorRetorno;
    }

    private void rbCorregir_actionPerformed(ActionEvent e) {
    }

    private void rbOmitir_actionPerformed(ActionEvent e) {
    }

    private void rbCancelar_actionPerformed(ActionEvent e) {
    }

    private void bOk_actionPerformed(ActionEvent e) {
	/*if(rbCorregir.isSelected()){
            valorRetorno = "corregir";
        }
        if(rbOmitir.isSelected()){
            valorRetorno = "omitir";
        }
        if(rbCancelar.isSelected()){
            valorRetorno = "cancelar";
        }
        System.out.println("valor  de valorRetorno = "+valorRetorno);
        this.dispose();*/
    }

    private void btnFix_actionPerformed(ActionEvent e) {
	valorRetorno = "corregir";
	this.dispose();
    }

    private void btnIgnore_actionPerformed(ActionEvent e) {
	valorRetorno = "omitir";
	this.dispose();
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
	valorRetorno = "cancelar";
	this.dispose();
    }

}

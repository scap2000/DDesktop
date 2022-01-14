package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.icons.IconTypes;

public class MessageDialog extends BasicDialog {

    private BasicLabel lblFixingError = new BasicLabel();
    private BasicPanel panelError = new BasicPanel();
    private BasicTextField tfCorregir = new BasicTextField();
    private PanelHeader panelHeader;
    // = new PanelLabelMain();
    final String TEXTO = "Errors Report";
    ButtonGroup grupo = new ButtonGroup();
    private String texto = "", valorRetorno = "";
    private int cantError = 0, countError = 0;
    private JToggleButton tbtnFix = new JToggleButton();
    private BasicButton btnCancel = new BasicButton();
    private BasicButton btnIgnore = new BasicButton();
    private AcceptButton btnAccept = new AcceptButton();
    private BasicLabel lblTotalError = new BasicLabel();

    public MessageDialog(String _texto, String _valorRetorno, int _cantError, int _countError) {
	try {
	    texto = _texto;
	    valorRetorno = _valorRetorno;
	    cantError = _cantError;
	    countError = _countError;
	    /*System.out.println("texto--> " + texto);
             System.out.println("valorRetorno--> " + valorRetorno);
             System.out.println("xcantError--> " + cantError);
             System.out.println("countError--> " + countError);*/
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /*    public MessageDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    private void jbInit() throws Exception {
	this.setSize(new Dimension(405, 170));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblFixingError, null);
	this.getContentPane().add(lblTotalError, null);
	this.getContentPane().add(btnIgnore, null);
	this.getContentPane().add(btnCancel, null);
	this.getContentPane().add(tbtnFix, null);
	this.getContentPane().add(panelHeader, null);
	//this.getContentPane().add(bOk, null);
	this.getContentPane().add(panelError, null);
	panelError.add(tfCorregir, null);
	panelError.add(btnAccept, null);
	this.getContentPane().setLayout(null);
	/*this.getContentPane().add(rbCancelar, null);
        this.getContentPane().add(rbOmitir, null);
        this.getContentPane().add(rbCorregir, null);*/
	this.setTitle("Errors Report");
	//lblMensaje.setText(texto);
	lblFixingError.setBounds(new Rectangle(10, 50, 205, 15));
	lblFixingError.setFont(new Font("Dialog", 1, 12));
	lblFixingError.setForeground(Color.blue);
	lblFixingError.setOpaque(true);
	panelError.setBounds(new Rectangle(5, 60, 390, 45));
	panelError.setLayout(null);
	panelError.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	/*rbOmitir.setText("Ignore");
        rbOmitir.setBounds(new Rectangle(20, 210, 135, 20));
        rbOmitir.setFont(new Font("Dialog", 1, 11));
        rbOmitir.setMnemonic('t');
        rbOmitir.addActionListener(new ActionListener() {

                                       public void actionPerformed(ActionEvent e) {
                                           rbOmitir_actionPerformed(e);
                                       }
                                   }
        );
        rbCorregir.setText("Fix");
        rbCorregir.setBounds(new Rectangle(15, 165, 135, 20));
        rbCorregir.setFont(new Font("Dialog", 1, 11));
        rbCorregir.setSelected(true);
        rbCorregir.setMnemonic('r');
        rbCorregir.addActionListener(new ActionListener() {

                                         public void actionPerformed(ActionEvent e) {
                                             rbCorregir_actionPerformed(e);
                                         }
                                     }
        );
        rbCancelar.setText("Cancel");
        rbCancelar.setBounds(new Rectangle(15, 185, 135, 20));
        rbCancelar.setFont(new Font("Dialog", 1, 11));
        rbCancelar.setMnemonic('l');
        rbCancelar.addActionListener(new ActionListener() {

                                         public void actionPerformed(ActionEvent e) {
                                             rbCancelar_actionPerformed(e);
                                         }
                                     }
        );*/
	/*grupo.add(rbOmitir);
        grupo.add(rbCancelar);
        grupo.add(rbCorregir);*/
	tfCorregir.setBounds(new Rectangle(5, 12, 330, 20));
	//tfCorregir.setEnabled(false);
	tfCorregir.setText(valorRetorno);
	/* bOk.setBounds(new Rectangle(245, 180, 45, 25));
        bOk.setMnemonic('O');
        bOk.setSize(new Dimension(45, 25));
        bOk.setHorizontalTextPosition(SwingConstants.LEFT);
        bOk.setToolTipText("Ok");
        bOk.addActionListener(new ActionListener() {

                                  public void actionPerformed(ActionEvent e) {
                                      bOk_actionPerformed(e);
                                  }
                              }
        );*/
	tbtnFix.setBounds(new Rectangle(105, 110, 90, 25));
	tbtnFix.setText("Fix");
	tbtnFix.setFont(new Font("Default", 1, 11));
	tbtnFix.setMnemonic('F');
	tbtnFix.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			tbtnFix_actionPerformed(e);
		    }

		});
	btnCancel.setText("Cancel");
	btnCancel.setBounds(new Rectangle(305, 110, 90, 25));
	btnCancel.setMnemonic('C');
	btnCancel.setFont(new Font("Dialog", 1, 11));
	btnCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnCancel_actionPerformed(e);
		    }

		});
	btnIgnore.setText("Ignore");
	btnIgnore.setBounds(new Rectangle(205, 110, 90, 25));
	btnIgnore.setMnemonic('I');
	btnIgnore.setFont(new Font("Dialog", 1, 11));
	btnIgnore.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnIgnore_actionPerformed(e);
		    }

		});
	btnAccept.setBounds(new Rectangle(345, 10, 40, 25));
	btnAccept.setFont(new Font("Dialog", 1, 11));
	btnAccept.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnIgnore_actionPerformed(e);
		    }

		});
	setLblTotalError();
	lblTotalError.setBounds(new Rectangle(5, 35, 390, 15));
	lblTotalError.setFont(new Font("Default", 1, 11));
	lblTotalError.setForeground(Color.red);
	lblTotalError.setHorizontalAlignment(SwingConstants.CENTER);
	habilitarPanelFix(false);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void setLblTotalError() {
	lblTotalError.setText("TOTAL ERROR: " + cantError);
    }

    public String getOpcion() {
	return valorRetorno;
    }

    private void habilitarPanelFix(boolean _valor) {
	if (_valor) {
	    lblFixingError.setText(" Fixing error " + countError + " of " + cantError + " at line " + texto);
	}
	/*tfCorregir.setVisible(_valor);
        lblFixingError.setVisible(_valor);
        btnAccept.setVisible(_valor);
        btnIgnore.setEnabled(!_valor);
        btnCancel.setEnabled(!_valor);
        panelError.setEnabled(_valor);*/
	tfCorregir.setVisible(!_valor);
	lblFixingError.setVisible(!_valor);
	btnAccept.setVisible(!_valor);
	btnIgnore.setEnabled(_valor);
	btnCancel.setEnabled(_valor);
	panelError.setEnabled(!_valor);
    }
    /*    private void rbCorregir_actionPerformed(ActionEvent e) {
        tfCorregir.setEnabled(true);
    }

    private void rbOmitir_actionPerformed(ActionEvent e) {
        tfCorregir.setEnabled(false);
    }

    private void rbCancelar_actionPerformed(ActionEvent e) {
        tfCorregir.setEnabled(false);
    }

    private void bOk_actionPerformed(ActionEvent e) {
        if(rbCorregir.isSelected()){
            valorRetorno = tfCorregir.getText().trim();
        }
        if(rbOmitir.isSelected()){
            valorRetorno = "omitir";
        }
        if(rbCancelar.isSelected()){
            valorRetorno = "cancelar";
        }
        System.out.println("valor  de valorRetorno = "+valorRetorno);
        this.dispose();
    }
*/

    private void btnCancel_actionPerformed(ActionEvent e) {
	valorRetorno = "cancelar";
	this.dispose();
    }

    private void btnIgnore_actionPerformed(ActionEvent e) {
	valorRetorno = "omitir";
	this.dispose();
    }

    private void btnFix_actionPerformed(ActionEvent e) {
	valorRetorno = tfCorregir.getText().trim();
	this.dispose();
    }

    private void tbtnFix_actionPerformed(ActionEvent e) {
	habilitarPanelFix(tbtnFix.isSelected());
    }

}

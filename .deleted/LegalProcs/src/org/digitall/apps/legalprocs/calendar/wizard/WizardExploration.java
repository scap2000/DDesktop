package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.lib.icons.IconTypes;

//

public class WizardExploration extends BasicDialog {

    private BasicPanel panelHelp = new BasicPanel();
    private NextWizardButton btnNext = new NextWizardButton();
    private PreviousWizardButton btnPrevious = new PreviousWizardButton();
    private CloseButton btnClose = new CloseButton();
    private Manager mgmt;
    private BasicLabel lblIcon = new BasicLabel(IconTypes.getIcon("iconos/96x96/solicitor.png"));
    private BasicLabel lblHelp = new BasicLabel();
    private BasicPanel panel = new BasicPanel();
    private BasicPanel panelMap = new BasicPanel();
    private BasicButton[] lblVector = new BasicButton[10];
    private BasicButton btnIndexStep0 = new BasicButton();
    private BasicButton btnIndexStep1 = new BasicButton();
    private BasicButton btnIndexStep2 = new BasicButton();
    private BasicButton btnIndexStep3 = new BasicButton();
    private BasicButton btnIndexStep4 = new BasicButton();
    private BasicButton btnIndexStep5 = new BasicButton();
    private BasicButton btnIndexStep6 = new BasicButton();
    private BasicButton btnIndexStep7 = new BasicButton();
    private BasicButton btnIndexStep8 = new BasicButton();
    private BasicButton btnIndexStep9 = new BasicButton();
    private BasicLabel jLabel2 = new BasicLabel();

    public WizardExploration(Manager _mgmt) {
	try {
	    setManager(_mgmt);
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public WizardExploration() {
	this(null, "", false);
    }

    public WizardExploration(Frame parent, String title, boolean modal) {
	super(parent, title, modal);
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(730, 570));
	this.getContentPane().setLayout(null);
	this.setTitle("Asistente: ");
	panelHelp.setBounds(new Rectangle(5, 40, 145, 495));
	panelHelp.setSize(new Dimension(145, 495));
	panelHelp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	panelHelp.setLayout(null);
	btnNext.setBounds(new Rectangle(440, 510, 40, 28));
	btnNext.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnNext_actionPerformed(e);
			       }

			   }
	);
	btnPrevious.setBounds(new Rectangle(390, 510, 40, 28));
	btnPrevious.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       btnPrevious_actionPerformed(e);
				   }

			       }
	);
	btnClose.setBounds(new Rectangle(670, 510, 40, 28));
	btnClose.setSize(new Dimension(40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	lblIcon.setBounds(new Rectangle(15, 5, 105, 120));
	lblHelp.setText("<html>Texto de ayuda basica referido al paso actual del asistente, esto sera de gran ayuda para el usuario.</html>");
	lblHelp.setBounds(new Rectangle(10, 135, 125, 350));
	lblHelp.setFont(new Font("Default", 0, 12));
	lblHelp.setVerticalAlignment(SwingConstants.BOTTOM);
	panel.setLayout(null);
	panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	panel.setBounds(new Rectangle(170, 110, 540, 365));
	panelMap.setBounds(new Rectangle(170, 40, 540, 50));
	panelMap.setLayout(null);
	panelMap.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	btnIndexStep0.setText("0");
	btnIndexStep0.setBounds(new Rectangle(25, 10, 32, 32));
	btnIndexStep0.setSize(new Dimension(32, 32));
	btnIndexStep0.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep0.setFont(new Font("Default", 1, 20));
	btnIndexStep0.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep0.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep0_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep1.setText("0");
	btnIndexStep1.setBounds(new Rectangle(77, 10, 32, 32));
	btnIndexStep1.setSize(new Dimension(32, 32));
	btnIndexStep1.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep1.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep1_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep2.setText("0");
	btnIndexStep2.setBounds(new Rectangle(128, 10, 32, 32));
	btnIndexStep2.setSize(new Dimension(32, 32));
	btnIndexStep2.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep2.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep2_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep3.setText("0");
	btnIndexStep3.setBounds(new Rectangle(180, 10, 32, 32));
	btnIndexStep3.setSize(new Dimension(32, 32));
	btnIndexStep3.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep3.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep3.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep3_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep4.setText("0");
	btnIndexStep4.setBounds(new Rectangle(232, 10, 32, 32));
	btnIndexStep4.setSize(new Dimension(32, 32));
	btnIndexStep4.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep4.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep4.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep4_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep5.setText("0");
	btnIndexStep5.setBounds(new Rectangle(283, 10, 32, 32));
	btnIndexStep5.setSize(new Dimension(32, 32));
	btnIndexStep5.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep5.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep5.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep5_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep6.setText("0");
	btnIndexStep6.setBounds(new Rectangle(335, 10, 32, 32));
	btnIndexStep6.setSize(new Dimension(32, 32));
	btnIndexStep6.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep6.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep6.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep6_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep7.setText("0");
	btnIndexStep7.setBounds(new Rectangle(387, 10, 32, 32));
	btnIndexStep7.setSize(new Dimension(32, 32));
	btnIndexStep7.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep7.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep7.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep8_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep8.setText("0");
	btnIndexStep8.setBounds(new Rectangle(438, 10, 32, 32));
	btnIndexStep8.setSize(new Dimension(32, 32));
	btnIndexStep8.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep8.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep8.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep9_actionPerformed(e);
				     }

				 }
	);
	btnIndexStep9.setText("0");
	btnIndexStep9.setBounds(new Rectangle(490, 10, 32, 32));
	btnIndexStep9.setSize(new Dimension(32, 32));
	btnIndexStep9.setMargin(new Insets(1, 1, 1, 1));
	btnIndexStep9.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
	btnIndexStep9.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnIndexStep9_actionPerformed(e);
				     }

				 }
	);
	jLabel2.setText("MODULO EN DESARROLLO");
	jLabel2.setBounds(new Rectangle(320, 90, 215, 20));
	jLabel2.setFont(new Font("Default", 3, 14));
	jLabel2.setForeground(new Color(255, 66, 0));
	jLabel2.setSize(new Dimension(200, 20));
	jLabel2.setBackground(Color.yellow);
	jLabel2.setOpaque(false);
	jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
	panelHelp.add(lblIcon, null);
	panelHelp.add(lblHelp, null);
	panelMap.add(btnIndexStep9, null);
	panelMap.add(btnIndexStep8, null);
	panelMap.add(btnIndexStep7, null);
	panelMap.add(btnIndexStep6, null);
	panelMap.add(btnIndexStep5, null);
	panelMap.add(btnIndexStep4, null);
	panelMap.add(btnIndexStep3, null);
	panelMap.add(btnIndexStep2, null);
	panelMap.add(btnIndexStep1, null);
	panelMap.add(btnIndexStep0, null);
	this.getContentPane().add(jLabel2, null);
	this.getContentPane().add(panelMap, null);
	this.getContentPane().add(panel, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(btnPrevious, null);
	this.getContentPane().add(btnNext, null);
	this.getContentPane().add(panelHelp, null);
	lblVector[0] = btnIndexStep0;
	lblVector[1] = btnIndexStep1;
	lblVector[2] = btnIndexStep2;
	lblVector[3] = btnIndexStep3;
	lblVector[4] = btnIndexStep4;
	lblVector[5] = btnIndexStep5;
	lblVector[6] = btnIndexStep6;
	lblVector[7] = btnIndexStep7;
	lblVector[8] = btnIndexStep8;
	lblVector[9] = btnIndexStep9;
    }

    private void paintStep(int _step) {
	int factor = 0;
	if (_step > 9)
	    factor = 10;
	int indexStep = _step - factor;
	for (int i = indexStep - 1; i >= 0; i--) {
	    lblVector[i].setText(String.valueOf(i + factor));
	    lblVector[i].setFont(new Font("Default", 1, 14));
	    lblVector[i].setForeground(new Color(20, 185, 200));
	}
	for (int i = indexStep + 1; i < lblVector.length; i++) {
	    lblVector[i].setText(String.valueOf(i + factor));
	    lblVector[i].setFont(new Font("Default", 0, 14));
	    lblVector[i].setForeground(Color.white);
	}
	lblVector[indexStep].setForeground(Color.black);
	lblVector[indexStep].setText(String.valueOf(_step));
	lblVector[indexStep].setFont(new Font("Default", 1, 20));
    }

    public void setWizardTitle(String _title) {
	String initialTitle = "Asistente: ";
	this.setTitle(initialTitle + _title);
    }

    public void setManager(Manager _mgmt) {
	mgmt = _mgmt;
    }

    public void setStep(int _step) {
	mgmt.setStep(_step);
    }

    public void goStep(int _step) {
	panel.removeAll();
	panel.add(mgmt.getPanelStep(_step), null);
	panel.updateUI();
	setWizardTitle(mgmt.getPanelStep(_step).getName());
	paintStep(_step);
    }

    private void btnNext_actionPerformed(ActionEvent e) {
	goStep(mgmt.getNextStep());
    }

    private void btnPrevious_actionPerformed(ActionEvent e) {
	goStep(mgmt.getPreviousStep());
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void btnIndexStep1_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep2_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep3_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep4_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep5_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep6_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep7_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep8_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep9_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

    private void btnIndexStep0_actionPerformed(ActionEvent e) {
	goStep(Integer.parseInt(((BasicButton)e.getSource()).getText()));
    }

}

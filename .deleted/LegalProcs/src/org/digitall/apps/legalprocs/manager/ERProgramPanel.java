package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import org.digitall.apps.legalprocs.calendar.wizard.ExplorationRequestClass_Old;
import org.digitall.apps.legalprocs.calendar.wizard.Manager;
import org.digitall.apps.legalprocs.calendar.wizard.PanelExplorationScheme;
import org.digitall.apps.legalprocs.manager.classes.ExplorationRequestClass;
import org.digitall.lib.components.JIntEntry;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;

public class ERProgramPanel extends BasicContainerPanel {

    private BasicPanel jPanel1 = new BasicPanel();
    private BasicPanel jPanel3 = new BasicPanel();
    private BasicPanel jPanel4 = new BasicPanel();
    private JIntEntry tfPlazoSoicitado = new JIntEntry();
    private JIntEntry tfMeses = new JIntEntry();
    private JIntEntry tfDias = new JIntEntry();
    private JIntEntry tfUnidOtorgadas = new JIntEntry();
    private JIntEntry tfPermOtorgados = new JIntEntry();
    private BasicLabel lblPlazoSolicitado = new BasicLabel();
    private BasicLabel lblDias = new BasicLabel();
    private BasicPanel jPanel2 = new BasicPanel();
    private BasicLabel lblPlazoSolicitado2 = new BasicLabel();
    private BasicLabel lblPlazoSolicitado3 = new BasicLabel();
    private BasicLabel lblSeAdjunta = new BasicLabel();
    private BasicLabel lblPlazoSolicitado4 = new BasicLabel();
    private BasicLabel lblPermOtorgados = new BasicLabel();
    private BasicLabel lblInidOtorgadas = new BasicLabel();
    private BasicLabel lblPlazoSolicitado7 = new BasicLabel();
    private BasicCheckBox chkAnio = new BasicCheckBox();
    private BasicCheckBox chkMeses = new BasicCheckBox();
    private BasicCheckBox chkDias = new BasicCheckBox();
    private BasicCheckBox chkNo = new BasicCheckBox();
    private BasicCheckBox chkSi = new BasicCheckBox();
    private ButtonGroup grupoPeriodo = new ButtonGroup();
    private ButtonGroup grupoadjuntado = new ButtonGroup();
    private String idExplorationScheme = "";
    private int opcion = 0;
    private Manager mgmt;
    private ExplorationRequestClass_Old prospection;
    private ExplorationRequestClass explorationRequestClass;
    private static int YEARS = 1;
    private static int MONTHS = 2;
    private static int DAYS = 3;
    private BasicButton btnShow = new BasicButton();

    public ERProgramPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    /* public ERProgramPanel(int _opcion, Manager _mgmt) {
	try {
	    mgmt = _mgmt;
	    opcion = _opcion;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ERProgramPanel(int _opcion, ExplorationRequestClass_Old _prospection) {
	try {
	    prospection = _prospection;
	    opcion = _opcion;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }*/

    public ERProgramPanel(ExplorationRequestClass _explorationRequestClass) {
	try {
	    explorationRequestClass = _explorationRequestClass;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(572, 380));
	jPanel1.setBounds(new Rectangle(5, 25, 555, 345));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfPlazoSoicitado.setBounds(new Rectangle(20, 30, 40, 15));
	tfPlazoSoicitado.setSize(new Dimension(40, 20));
	tfPlazoSoicitado.setText("0");
	tfMeses.setBounds(new Rectangle(215, 35, 35, 20));
	tfDias.setBounds(new Rectangle(400, 35, 35, 20));
	lblPlazoSolicitado.setText("Plazo Solicitado (días)");
	lblPlazoSolicitado.setBounds(new Rectangle(20, 15, 105, 15));
	lblPlazoSolicitado.setFont(new Font("Default", 1, 11));
	lblPlazoSolicitado.setSize(new Dimension(137, 14));
	lblDias.setText("(*)");
	lblDias.setBounds(new Rectangle(70, 30, 27, 14));
	lblDias.setFont(new Font("Default", 0, 11));
	lblDias.setSize(new Dimension(13, 14));
	lblDias.setForeground(Color.red);
	jPanel2.setBounds(new Rectangle(10, 70, 535, 65));
	jPanel2.setLayout(null);
	jPanel2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	lblPlazoSolicitado2.setText(" Período de trabajo anual permitido");
	lblPlazoSolicitado2.setBounds(new Rectangle(15, 65, 218, 15));
	lblPlazoSolicitado2.setFont(new Font("Default", 1, 11));
	lblPlazoSolicitado2.setSize(new Dimension(218, 15));
	lblPlazoSolicitado2.setOpaque(false);
	chkAnio.setText("Todo el año");
	chkAnio.setBounds(new Rectangle(40, 15, 100, 20));
	chkAnio.setFont(new Font("Default", 1, 11));
	chkAnio.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   chkAnio_actionPerformed(e);
			       }

			   }
	);
	chkMeses.setText("Meses");
	chkMeses.setBounds(new Rectangle(217, 15, 100, 20));
	chkMeses.setFont(new Font("Default", 1, 11));
	chkMeses.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    chkMeses_actionPerformed(e);
				}

			    }
	);
	chkDias.setText("Días");
	chkDias.setBounds(new Rectangle(400, 15, 100, 20));
	chkDias.setFont(new Font("Default", 1, 11));
	chkDias.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   chkDias_actionPerformed(e);
			       }

			   }
	);
	jPanel3.setBounds(new Rectangle(10, 155, 530, 40));
	jPanel3.setLayout(null);
	jPanel3.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	lblPlazoSolicitado3.setText(" Programa mínimo de trabajos");
	lblPlazoSolicitado3.setBounds(new Rectangle(15, 145, 185, 14));
	lblPlazoSolicitado3.setFont(new Font("Default", 1, 11));
	lblPlazoSolicitado3.setSize(new Dimension(185, 14));
	lblPlazoSolicitado3.setOpaque(false);
	chkNo.setText("NO");
	chkNo.setBounds(new Rectangle(405, 10, 45, 20));
	chkNo.setFont(new Font("Default", 1, 11));
	chkSi.setText("SI");
	chkSi.setBounds(new Rectangle(220, 10, 50, 20));
	chkSi.setFont(new Font("Default", 1, 11));
	lblSeAdjunta.setText("Se Adjunta:");
	lblSeAdjunta.setBounds(new Rectangle(70, 13, 70, 14));
	lblSeAdjunta.setFont(new Font("Default", 1, 11));
	lblSeAdjunta.setSize(new Dimension(70, 14));
	jPanel4.setBounds(new Rectangle(10, 230, 530, 100));
	jPanel4.setLayout(null);
	jPanel4.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	lblPlazoSolicitado4.setText("<html>Cantidad de permisos otorgados al peticionante, sus socios o por <br> interposita persona en la provincia</html>");
	lblPlazoSolicitado4.setBounds(new Rectangle(15, 215, 410, 28));
	lblPlazoSolicitado4.setFont(new Font("Default", 1, 11));
	lblPlazoSolicitado4.setSize(new Dimension(410, 28));
	lblPlazoSolicitado4.setOpaque(false);
	tfUnidOtorgadas.setBounds(new Rectangle(330, 55, 55, 20));
	tfUnidOtorgadas.setText("0");
	tfPermOtorgados.setBounds(new Rectangle(110, 55, 55, 20));
	tfPermOtorgados.setText("0");
	lblPermOtorgados.setText("<html><p align=Center>Cantidad de permisos<br> otorgados</p></html>");
	lblPermOtorgados.setBounds(new Rectangle(75, 25, 205, 25));
	lblPermOtorgados.setFont(new Font("Default", 1, 11));
	lblPermOtorgados.setSize(new Dimension(138, 28));
	lblInidOtorgadas.setText("<html><p align=Center>Cantidad de unidades de <br> medidas otorgadas</p></html>");
	lblInidOtorgadas.setBounds(new Rectangle(280, 18, 155, 42));
	lblInidOtorgadas.setFont(new Font("Default", 1, 11));
	lblInidOtorgadas.setSize(new Dimension(155, 42));
	lblPlazoSolicitado7.setText(" Programa");
	lblPlazoSolicitado7.setBounds(new Rectangle(15, 15, 63, 14));
	lblPlazoSolicitado7.setFont(new Font("Default", 1, 11));
	lblPlazoSolicitado7.setSize(new Dimension(63, 14));
	lblPlazoSolicitado7.setOpaque(false);
	jPanel4.add(lblInidOtorgadas, null);
	jPanel4.add(lblPermOtorgados, null);
	jPanel4.add(tfPermOtorgados, null);
	jPanel4.add(tfUnidOtorgadas, null);
	jPanel1.add(lblPlazoSolicitado4, null);
	jPanel1.add(jPanel4, null);
	jPanel1.add(lblPlazoSolicitado3, null);
	jPanel1.add(jPanel3, null);
	jPanel1.add(lblPlazoSolicitado2, null);
	jPanel1.add(jPanel2, null);
	jPanel1.add(lblDias, null);
	jPanel1.add(lblPlazoSolicitado, null);
	jPanel1.add(tfPlazoSoicitado, null);
	jPanel3.add(btnShow, null);
	jPanel3.add(chkSi, null);
	jPanel3.add(chkNo, null);
	jPanel3.add(lblSeAdjunta, null);
	jPanel2.add(chkDias, null);
	jPanel2.add(chkMeses, null);
	jPanel2.add(chkAnio, null);
	jPanel2.add(tfMeses, null);
	jPanel2.add(tfDias, null);
	this.add(lblPlazoSolicitado7, null);
	this.add(jPanel1, null);
	grupoPeriodo.add(chkAnio);
	grupoPeriodo.add(chkMeses);
	grupoPeriodo.add(chkDias);
	chkAnio.setSelected(true);
	grupoadjuntado.add(chkSi);
	grupoadjuntado.add(chkNo);
	chkNo.setSelected(true);
	configComponents();
	chkSi.addActionListener(new ActionListener() {

			     public void actionPerformed(ActionEvent e) {
				 chkSi_actionPerformed(e);
			     }

			 }
	);
	btnShow.setText("ver");
	btnShow.setBounds(new Rectangle(300, 10, 55, 20));
	btnShow.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   btnShow_actionPerformed(e);
			       }

			   }
	);
    }

    private void configComponents() {
	tfMeses.setEnabled(false);
	tfDias.setEnabled(false);
    }

    private void chkMeses_actionPerformed(ActionEvent e) {
	tfMeses.setEnabled(true);
	tfDias.setEnabled(false);
    }

    private void chkDias_actionPerformed(ActionEvent e) {
	tfDias.setEnabled(true);
	tfMeses.setEnabled(false);
    }

    private void chkAnio_actionPerformed(ActionEvent e) {
	tfMeses.setEnabled(false);
	tfDias.setEnabled(false);
    }

    public int getPlazo() {
	return Integer.parseInt("0" + tfPlazoSoicitado.getText().trim());
    }

    public int getCycleType() {
	int cycleType = 0;
	if (chkAnio.isSelected()) {
	    cycleType = 1;
	} else if (chkMeses.isSelected()) {
	    cycleType = 2;
	} else if (chkDias.isSelected()) {
	    cycleType = 3;
	}
	return cycleType;
    }

    public int getCycleNumber() {
	int cycleNumber = 0;
	if (chkMeses.isSelected()) {
	    cycleNumber = Integer.parseInt("0" + tfMeses.getText().trim());
	} else {
	    cycleNumber = Integer.parseInt("0" + tfDias.getText().trim());
	}
	return cycleNumber;
    }

    public boolean getWorkProgram() {
	boolean workProgram = false;
	if (chkSi.isSelected()) {
	    workProgram = true;
	} else {
	    workProgram = false;
	}
	return workProgram;
    }

    public String getWP() {
	String workProgram = "false";
	if (chkSi.isSelected()) {
	    workProgram = "true";
	} else {
	    workProgram = "false";
	}
	return workProgram;
    }

    public int getPermisosOtorgados() {
	int PermOtorg = Integer.parseInt("0" + tfPermOtorgados.getText().trim());
	return PermOtorg;
    }

    public int getUnidadesOtorgadas() {
	int unidOtorgadas = Integer.parseInt("0" + tfUnidOtorgadas.getText().trim());
	return unidOtorgadas;
    }

    public void chkSi_actionPerformed(ActionEvent e) {
	/*if (opcion == 1) {
	    PanelExplorationScheme explorationScheme = new PanelExplorationScheme(prospection);
	    explorationScheme.setModal(true);
	    explorationScheme.setVisible(true);
	    try {
		if (prospection.getArgument(32).equals("true")) {
		    chkSi.setSelected(true);
		} else {
		    chkNo.setSelected(true);
		}
	    } catch (Exception ex) {
		//ex.printStackTrace();
		chkNo.setSelected(true);
	    }
	} else {
	    ExplorationScheme explorationScheme = new ExplorationScheme();
	    explorationScheme.setModal(true);
	    explorationScheme.setVisible(true);
	}*/
	/*if (explorationRequestClass.getIdExplorationRequest() != -1) {
	    PanelExplorationScheme explorationScheme = new PanelExplorationScheme(explorationRequestClass.getExplorationSchemeClass());
	    explorationScheme.setModal(true);
	    explorationScheme.setVisible(true);
	    try {
		if (prospection.getArgument(32).equals("true")) {
		    chkSi.setSelected(true);
		} else {
		    chkNo.setSelected(true);
		}
	    } catch (Exception ex) {
		//ex.printStackTrace();
		chkNo.setSelected(true);
	    }
	} else {
	    ExplorationScheme explorationScheme = new ExplorationScheme();
	    explorationScheme.setModal(true);
	    explorationScheme.setVisible(true);
	}*/
    }

    public void setIdExplorationScheme(String _idExplorationScheme) {
	idExplorationScheme = _idExplorationScheme;
    }

    public void setChk(boolean _valor) {
	chkSi.setSelected(_valor);
    }

    public void setExplorationRequestClass(ExplorationRequestClass _explorationRequestClass) {
	explorationRequestClass = _explorationRequestClass;
	if (explorationRequestClass.getIdExplorationRequest() != -1) {
	    explorationRequestClass.retrieveExplorationSchemeData();
	    loadData();
	} else {
	    //analizar
	}
    }

    private void loadData() {
	tfPlazoSoicitado.setText(String.valueOf(explorationRequestClass.getTermDays()));
	tfPermOtorgados.setText(String.valueOf(explorationRequestClass.getPermissionGranted()));
	tfUnidOtorgadas.setText(String.valueOf(explorationRequestClass.getMeasurementUnitsGranted()));
	chkSi.setSelected(explorationRequestClass.isWorkProgram());
	if (explorationRequestClass.getWorkCycleType() == MONTHS) {
	    chkMeses.setSelected(true);
	    tfMeses.setText(String.valueOf(explorationRequestClass.getWorkCyclenumber()));
	} else if (explorationRequestClass.getWorkCycleType() == DAYS) {
	    chkDias.setSelected(true);
	    tfDias.setText(String.valueOf(explorationRequestClass.getWorkCyclenumber()));
	} else {
	    chkAnio.setSelected(true);
	}
    }

    private void btnShow_actionPerformed(ActionEvent e) {
	if (explorationRequestClass.getIdExplorationRequest() != -1) {
	    PanelExplorationScheme explorationScheme = new PanelExplorationScheme(explorationRequestClass.getExplorationSchemeClass());
	    explorationScheme.setModal(true);
	    explorationScheme.setVisible(true);
	    chkSi.setSelected(true);
	    /*try {
		if (prospection.getArgument(32).equals("true")) {
		    chkSi.setSelected(true);
		} else {
		    chkNo.setSelected(true);
		}
	    } catch (Exception ex) {
		//ex.printStackTrace();
		chkNo.setSelected(true);
	    }*/
	} else {
	    ExplorationScheme explorationScheme = new ExplorationScheme();
	    explorationScheme.setModal(true);
	    explorationScheme.setVisible(true);
	}
    }

}

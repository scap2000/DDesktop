package org.digitall.apps.mapper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import org.digitall.common.mapper.CoordinateViewer;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;

//

public class ProjectViewerOld extends BasicInternalFrame {
    //Constantes de operacion
    private final int MALLA = 1;
    private final int POLIGONO = 2;
    private final int ELIMINAR = 3;
    private final int PARCELA = 4;
    private final int NUEVO = 5;
    //Graba en la Base de Datos
    private final int PARALELA = 6;
    private final int TRAPECIO = 7;
    private final int INFO = 8;
    private final int NUMERAR = 9;
    private BasicLabel statusBar = new BasicLabel();
    private DrawPanel panel;
    private boolean firstLoad = true;
    private int idProject;
    private BorderLayout borderLayout1 = new BorderLayout();
    private JToolBar tbTools = new JToolBar();
    private BasicButton bmalla = new BasicButton();
    private BasicButton bpoligono = new BasicButton();//IconTypes.getIcon("iconos/other/polygon.png"));
    private BasicButton belimina = new BasicButton();
    private BasicButton bparcela = new BasicButton();
    private BasicButton bnuevo = new BasicButton();
    private BasicButton bparalela = new BasicButton();
    private BasicButton btrapeciorect = new BasicButton();
    private BasicButton bInfo = new BasicButton();//IconTypes.getIcon("iconos/other/info.png"));
    private BasicButton bNumera = new BasicButton();
    private BasicButton Boton1 = new BasicButton();
    private JDesktopPane desktop;
    private String projectName;
    private CoordinateViewer coordinateViewer;

    public ProjectViewerOld(JDesktopPane _desktop, int _idProject, String _projectName) {
	try {
	    desktop = _desktop;
	    idProject = _idProject;
	    projectName = _projectName;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	setClosable(true);
	setDefaultCloseOperation(BasicInternalFrame.HIDE_ON_CLOSE);
	setIconifiable(true);
	setResizable(true);
	this.setTitle("Project: " + projectName);
	this.setSize(new Dimension(566, 547));
	this.getContentPane().setLayout(borderLayout1);
	statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	tbTools.setOrientation(1);
	bmalla.setText("Malla");
	bmalla.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bmalla_actionPerformed(e);
		    }

		});
	//bpoligono.setText("Poligono");
	bpoligono.setToolTipText("Calculates distance and areas");
	bpoligono.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bpoligono_actionPerformed(e);
		    }

		});
	belimina.setText("Eliminar");
	belimina.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			belimina_actionPerformed(e);
		    }

		});
	bparcela.setText("Parcela");
	bparcela.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bparcela_actionPerformed(e);
		    }

		});
	bnuevo.setText("nuevo");
	bnuevo.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bnuevo_actionPerformed(e);
		    }

		});
	bparalela.setText("paralela");
	bparalela.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bparalela_actionPerformed(e);
		    }

		});
	btrapeciorect.setText("trapeciorect");
	btrapeciorect.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btrapeciorect_actionPerformed(e);
		    }

		});
	//bInfo.setText("Info");
	bInfo.setToolTipText("Drill logging information");
	bInfo.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bInfo_actionPerformed(e);
		    }

		});
	bNumera.setText("Numerar");
	bNumera.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNumera_actionPerformed(e);
		    }

		});
	Boton1.setText("_");
	this.getContentPane().add(statusBar, BorderLayout.NORTH);
	this.getContentPane().add(tbTools, BorderLayout.EAST);
	//tbTools.add(bmalla, null);
	tbTools.add(bpoligono, null);
	//tbTools.add(belimina, null);
	//tbTools.add(bparcela, null);
	//tbTools.add(bnuevo, null);
	//tbTools.add(bparalela, null);
	//tbTools.add(btrapeciorect, null);
	tbTools.add(bInfo, null);
	//tbTools.add(bNumera, null);
	//tbTools.add(Boton1, null);
    }

    public boolean hasPolygons() {
	return panel.hasPolygons();
    }

    public void mostrar() {
	if (idProject != -1) {
	    if (firstLoad) {
		panel = new DrawPanel(desktop, statusBar, idProject, projectName);
		if (coordinateViewer != null) {
		    panel.setCoordinateViewer(coordinateViewer);
		}
		panel.setBounds(new Rectangle(15, 15, 800, 600));
		this.getContentPane().add(panel, BorderLayout.CENTER);
		firstLoad = false;
	    }
	}
	setVisible(true);
    }

    private void bmalla_actionPerformed(ActionEvent e) {
	panel.setOperation(MALLA);
    }

    private void bpoligono_actionPerformed(ActionEvent e) {
	panel.setOperation(POLIGONO);
    }

    private void belimina_actionPerformed(ActionEvent e) {
	panel.setOperation(ELIMINAR);
    }

    private void bparcela_actionPerformed(ActionEvent e) {
	panel.setOperation(PARCELA);
    }

    private void bnuevo_actionPerformed(ActionEvent e) {
	panel.setOperation(NUEVO);
    }

    private void bInfo_actionPerformed(ActionEvent e) {
	panel.setOperation(INFO);
    }

    private void bNumera_actionPerformed(ActionEvent e) {
	panel.setOperation(NUMERAR);
    }

    private void bparalela_actionPerformed(ActionEvent e) {
	panel.setOperation(6);
    }

    private void btrapeciorect_actionPerformed(ActionEvent e) {
	panel.setOperation(7);
    }

    public void setCoordinateViewer(CoordinateViewer _coordViewer) {
	coordinateViewer = _coordViewer;
	coordinateViewer.setVisible(true);
	if (panel != null) {
	    panel.setCoordinateViewer(coordinateViewer);
	}
    }

}

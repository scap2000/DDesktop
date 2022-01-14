package org.digitall.apps.mapper;

import java.awt.BorderLayout;
import java.awt.Color;
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
import org.digitall.lib.geo.coordinatesystems.CoordinateSystems;
import org.digitall.common.geo.mapping.BasicDrawEngine;
import org.digitall.lib.geo.mapping.classes.BasicDrawEngineConfig;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.geo.esri.ESRIPolygon;
import org.digitall.lib.geo.mapping.classes.Layer;

//

public class CityMap extends BasicPrimitivePanel {

    //Constantes de operacion
    private BasicLabel statusBar = new BasicLabel();
    private BasicDrawEngine panel;
    private boolean firstLoad = true;
    private BorderLayout borderLayout1 = new BorderLayout();
    private JToolBar tbTools = new JToolBar();
    private BasicButton bpoligono = new BasicButton();
    private BasicButton bInfo = new BasicButton();
    private BasicButton bZoom = new BasicButton();
    private CoordinateViewer coordinateViewer;

    public CityMap() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	/*setClosable(true);
	setDefaultCloseOperation(BasicInternalFrame.HIDE_ON_CLOSE);
	setIconifiable(true);
	setResizable(true);*/
	this.setTitle("Project: ");
	this.setSize(new Dimension(566, 547));
	this.setLayout(borderLayout1);
	statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	tbTools.setOrientation(1);
	bpoligono.setText("Regla");
	bpoligono.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     bpoligono_actionPerformed(e);
				 }

			     }
	);
	bInfo.setText("Info");
	bInfo.addActionListener(new ActionListener() {

			     public void actionPerformed(ActionEvent e) {
				 bInfo_actionPerformed(e);
			     }

			 }
	);
	bZoom.setText("Zoom");
	bZoom.addActionListener(new ActionListener() {

			     public void actionPerformed(ActionEvent e) {
				 bZoom_actionPerformed(e);
			     }

			 }
	);
	this.add(statusBar, BorderLayout.NORTH);
	this.add(tbTools, BorderLayout.EAST);
	tbTools.add(bpoligono, null);
	tbTools.add(bInfo, null);
	tbTools.add(bZoom, null);
    }

    public void mostrar() {
	if (firstLoad) {
	    panel = new BasicDrawEngine(statusBar);
	    if (coordinateViewer != null) {
		panel.setCoordinateViewer(coordinateViewer);
		coordinateViewer.setTitle("");
		//Layer _layer = new Layer("drilling","drills","utm","idproject = " + idProject);
		Layer manzanas = new Layer("manzanas", "gis_mosconi", "manzanas", "the_geom", "idmanzana<100000", "idmanzana");
		manzanas.setProjectionType(CoordinateSystems.GK);
		manzanas.setColor(Color.RED);
	        manzanas.setMouseOverColor(Color.MAGENTA);
		panel.addLayer(manzanas);
		
	        Layer parcelas = new Layer("parcelas", "gis_mosconi", "parcelas", "the_geom", "idparcela<100000", "idparcela");
	        parcelas.setProjectionType(CoordinateSystems.GK);
	        parcelas.setColor(Color.BLACK);
	        parcelas.setMouseOverColor(Color.CYAN);
	        panel.addLayer(parcelas);

	        Layer manzanas_desvinculadas = new Layer("manzanas_desvinculadas", "gis_mosconi", "manzanas_desvinculadas", "the_geom", "1=1", "gid");
	        manzanas_desvinculadas.setProjectionType(CoordinateSystems.GK);
	        manzanas_desvinculadas.setColor(Color.RED);
	        manzanas_desvinculadas.setMouseOverColor(Color.MAGENTA);
	        panel.addLayer(manzanas_desvinculadas);
	        
		Layer parcelas_desvinculadas = new Layer("parcelas_desvinculadas", "gis_mosconi", "parcelas_desvinculadas", "the_geom", "1=1", "gid");
	        parcelas_desvinculadas.setProjectionType(CoordinateSystems.GK);
	        parcelas_desvinculadas.setColor(Color.BLACK);
	        parcelas_desvinculadas.setMouseOverColor(Color.CYAN);
	        panel.addLayer(parcelas_desvinculadas);

	        Layer fracciones_desvinculadas = new Layer("fracciones_desvinculadas", "gis_mosconi", "fracciones_desvinculadas", "the_geom", "gid!=171", "gid");
	        fracciones_desvinculadas.setProjectionType(CoordinateSystems.GK);
	        fracciones_desvinculadas.setColor(Color.GRAY);
	        fracciones_desvinculadas.setMouseOverColor(Color.GRAY.darker());
	        panel.addLayer(fracciones_desvinculadas);

	        Layer canales = new Layer("canales", "gis_mosconi", "canales", "the_geom", "1=1", "gid");
	        canales.setProjectionType(CoordinateSystems.GK);
	        canales.setColor(Color.BLUE);
	        canales.setMouseOverColor(Color.CYAN);
	        panel.addLayer(canales);

	        Layer agua = new Layer("agua", "gis_mosconi", "agua", "the_geom", "1=1", "gid");
	        agua.setProjectionType(CoordinateSystems.GK);
	        agua.setColor(Color.CYAN);
	        agua.setMouseOverColor(Color.CYAN);
	        panel.addLayer(agua);

	        Layer espacios_verdes = new Layer("espacios_verdes", "gis_mosconi", "espacios_verdes", "the_geom", "1=1", "gid");
	        espacios_verdes.setProjectionType(CoordinateSystems.GK);
	        espacios_verdes.setColor(Color.GREEN.darker());
	        espacios_verdes.setMouseOverColor(Color.GREEN.darker());
	        panel.addLayer(espacios_verdes);

	        Layer platabandas = new Layer("platabandas", "gis_mosconi", "platabandas", "the_geom", "1=1", "gid");
	        platabandas.setProjectionType(CoordinateSystems.GK);
	        platabandas.setColor(Color.GREEN.brighter());
	        platabandas.setMouseOverColor(Color.GREEN.brighter());
	        panel.addLayer(platabandas);

	        Layer rutas = new Layer("rutas", "gis_mosconi", "rutas", "the_geom", "1=1", "gid");
	        rutas.setProjectionType(CoordinateSystems.GK);
	        rutas.setColor(Color.ORANGE);
	        rutas.setMouseOverColor(Color.ORANGE);
	        panel.addLayer(rutas);

	        Layer secciones = new Layer("secciones", "gis_mosconi", "secciones", "the_geom", "1=1", "gid");
	        secciones.setProjectionType(CoordinateSystems.GK);
	        secciones.setColor(Color.MAGENTA);
	        secciones.setMouseOverColor(Color.MAGENTA);
	        panel.addLayer(secciones);

	        Layer veredas = new Layer("veredas", "gis_mosconi", "veredas", "the_geom", "1=1", "gid");
	        veredas.setProjectionType(CoordinateSystems.GK);
	        veredas.setColor(Color.PINK);
	        veredas.setMouseOverColor(Color.PINK);
	        panel.addLayer(veredas);

	        Layer nros_manzanas = new Layer("nros_manzanas", "gis_mosconi", "nros_manzanas", "the_geom", "1=1", "gid", "maptext");
	        nros_manzanas.setProjectionType(CoordinateSystems.GK);
	        nros_manzanas.setColor(Color.RED);
	        nros_manzanas.setMouseOverColor(Color.CYAN);
	        nros_manzanas.setPointDiameter(4);
	        panel.addLayer(nros_manzanas);

	        Layer nros_parcelas = new Layer("nros_parcelas", "gis_mosconi", "nros_parcelas", "the_geom", "1=1", "gid", "maptext");
	        nros_parcelas.setProjectionType(CoordinateSystems.GK);
	        nros_parcelas.setColor(Color.BLACK);
	        nros_parcelas.setMouseOverColor(Color.CYAN);
	        nros_parcelas.setPointDiameter(4);
	        panel.addLayer(nros_parcelas);

	        Layer nros_fracciones = new Layer("nros_fracciones", "gis_mosconi", "nros_fracciones", "the_geom", "1=1", "gid", "maptext");
	        nros_fracciones.setProjectionType(CoordinateSystems.GK);
	        nros_fracciones.setColor(Color.BLACK);
	        nros_fracciones.setMouseOverColor(Color.CYAN);
	        nros_fracciones.setPointDiameter(16);
	        panel.addLayer(nros_fracciones);

		coordinateViewer.setVisible(true);
	    }
	    panel.setBounds(new Rectangle(15, 15, 800, 600));
	    this.add(panel, BorderLayout.CENTER);
	    firstLoad = false;
	}
	//setDesktop(Environment.getActiveDesktop());
	//setIcon(true);
	//setVisible(true);
    }

    private void bpoligono_actionPerformed(ActionEvent e) {
	panel.setOperation(BasicDrawEngineConfig.OPERATION_DISTANCE_AREA);
    }

    private void bInfo_actionPerformed(ActionEvent e) {
	panel.setOperation(BasicDrawEngineConfig.OPERATION_QUERY);
    }

    private void bZoom_actionPerformed(ActionEvent e) {
	panel.setOperation(BasicDrawEngineConfig.OPERATION_ZOOM_IN);
    }

    public void setCoordinateViewer(CoordinateViewer _coordViewer) {
	coordinateViewer = _coordViewer;
	coordinateViewer.setVisible(true);
	if (panel != null) {
	    panel.setCoordinateViewer(coordinateViewer);
	}
    }

}

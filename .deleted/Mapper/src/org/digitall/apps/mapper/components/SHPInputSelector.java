package org.digitall.apps.mapper.components;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.lib.common.ConfigFile;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.geo.coordinatesystems.CoordinateSystems;
import org.digitall.lib.geo.coordinatesystems.GKCoord;
import org.digitall.lib.geo.coordinatesystems.LatLongCoord;
import org.digitall.lib.geo.coordinatesystems.UTMCoord;
import org.digitall.lib.geo.shapefile.SHPFilter;
import org.digitall.lib.geo.shapefile.SHPPoint;
import org.digitall.lib.geo.shapefile.SHPPolygon;
import org.digitall.lib.geo.shapefile.SHPReader;
import org.digitall.lib.geo.shapefile.ShapeTypes;
import org.digitall.lib.sql.LibSQL;

public class SHPInputSelector extends BasicDialog {

    private BasicButton bAbrir = new BasicButton();
    private Vector shapes = new Vector();
    private int idProject = 0;
    private int shapeType = -1;
    private BasicLabel jlFileProps = new BasicLabel();
    private BasicButton bSelectProject = new BasicButton();
    private BasicLabel jlProjectProps = new BasicLabel();
    private BasicButton bInsertDB = new BasicButton();
    private BasicButton bEmptyTable = new BasicButton();
    private BasicButton bDrawDesk = new BasicButton();
    private BasicTextField utm_x = new BasicTextField();
    private BasicTextField utm_y = new BasicTextField();
    private BasicButton bConvertTUTM2GK = new BasicButton();
    private BasicTextField gk_y = new BasicTextField();
    private BasicTextField gk_x = new BasicTextField();
    private BasicButton bConvertGK2UTM = new BasicButton();
    private BasicCombo fromCoord = new BasicCombo();
    private BasicCombo toCoord = new BasicCombo();
    private BasicCombo utm_zone = new BasicCombo();
    private BasicLabel jLabel1 = new BasicLabel();
    private BasicLabel jLabel2 = new BasicLabel();
    private BasicLabel jLabel3 = new BasicLabel();
    private BasicLabel jLabel4 = new BasicLabel();
    private BasicCombo gk_faja = new BasicCombo();
    private BasicLabel jLabel5 = new BasicLabel();
    private BasicLabel jLabel6 = new BasicLabel();
    private ConfigFile cfg = new ConfigFile();

    public SHPInputSelector() {
	this(null, "", false);
    }

    public SHPInputSelector(Frame parent, String title, boolean modal) {
	super(parent, title, modal);
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(400, 563));
	this.getContentPane().setLayout(null);
	bAbrir.setText("Seleccionar Archivo");
	bAbrir.setBounds(new Rectangle(5, 15, 185, 20));
	bAbrir.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	bAbrir.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  bAbrir_actionPerformed(e);
			      }

			  }
	);
	jlFileProps.setText("File: None");
	jlFileProps.setBounds(new Rectangle(5, 45, 385, 25));
	jlFileProps.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	bSelectProject.setText("Seleccionar Proyecto");
	bSelectProject.setBounds(new Rectangle(5, 80, 385, 20));
	bSelectProject.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	bSelectProject.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  bSelectProject_actionPerformed(e);
				      }

				  }
	);
	jlProjectProps.setText("Project: None");
	jlProjectProps.setBounds(new Rectangle(5, 105, 385, 25));
	jlProjectProps.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	bInsertDB.setText("Introducir en la Base de Datos");
	bInsertDB.setBounds(new Rectangle(5, 170, 385, 22));
	bInsertDB.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	bInsertDB.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     bInsertDB_actionPerformed(e);
				 }

			     }
	);
	bEmptyTable.setText("Vaciar Tabla");
	bEmptyTable.setBounds(new Rectangle(5, 140, 385, 20));
	bEmptyTable.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	bEmptyTable.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       bEmptyTable_actionPerformed(e);
				   }

			       }
	);
	bDrawDesk.setText("Ver Dibujo");
	bDrawDesk.setBounds(new Rectangle(5, 200, 385, 20));
	bDrawDesk.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     bDrawDesk_actionPerformed(e);
				 }

			     }
	);
	this.getContentPane().add(jLabel6, null);
	this.getContentPane().add(jLabel5, null);
	this.getContentPane().add(gk_faja, null);
	this.getContentPane().add(jLabel4, null);
	this.getContentPane().add(jLabel3, null);
	this.getContentPane().add(jLabel2, null);
	this.getContentPane().add(jLabel1, null);
	this.getContentPane().add(utm_zone, null);
	this.getContentPane().add(toCoord, null);
	this.getContentPane().add(fromCoord, null);
	this.getContentPane().add(bConvertGK2UTM, null);
	this.getContentPane().add(gk_x, null);
	this.getContentPane().add(gk_y, null);
	this.getContentPane().add(bConvertTUTM2GK, null);
	this.getContentPane().add(utm_y, null);
	this.getContentPane().add(utm_x, null);
	this.getContentPane().add(bDrawDesk, null);
	this.getContentPane().add(bEmptyTable, null);
	this.getContentPane().add(bInsertDB, null);
	this.getContentPane().add(jlProjectProps, null);
	this.getContentPane().add(bSelectProject, null);
	this.getContentPane().add(jlFileProps, null);
	this.getContentPane().add(bAbrir, null);
	bDrawDesk.setEnabled(false);
	utm_x.setBounds(new Rectangle(75, 405, 165, 20));
	utm_x.setText("680764");
	utm_y.setBounds(new Rectangle(75, 430, 165, 20));
	utm_y.setText("7184023");
	bConvertTUTM2GK.setText("UTM ---> GK");
	bConvertTUTM2GK.setBounds(new Rectangle(275, 405, 105, 45));
	bConvertTUTM2GK.setMnemonic('U');
	bConvertTUTM2GK.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   bConvertUTM2GK_actionPerformed(e);
				       }

				   }
	);
	gk_y.setBounds(new Rectangle(75, 480, 165, 20));
	gk_y.setText("7185536.043121210");
	gk_x.setBounds(new Rectangle(75, 455, 165, 20));
	gk_x.setText("3379073.509311160");
	bConvertGK2UTM.setText("GK ---> UTM");
	bConvertGK2UTM.setBounds(new Rectangle(275, 455, 105, 45));
	bConvertGK2UTM.setMnemonic('G');
	bConvertGK2UTM.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  bConvertGK2UTM_actionPerformed(e);
				      }

				  }
	);
	fromCoord.setBounds(new Rectangle(210, 15, 65, 20));
	toCoord.setBounds(new Rectangle(285, 15, 65, 20));
	utm_zone.setBounds(new Rectangle(5, 430, 65, 20));
	jLabel1.setText("Zone:");
	jLabel1.setBounds(new Rectangle(18, 408, 38, 14));
	jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
	jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
	jLabel2.setText("E");
	jLabel2.setBounds(new Rectangle(245, 408, 15, 15));
	jLabel3.setText("N");
	jLabel3.setBounds(new Rectangle(245, 433, 15, 15));
	jLabel4.setText("Faja:");
	jLabel4.setBounds(new Rectangle(20, 460, 35, 10));
	jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
	jLabel4.setHorizontalTextPosition(SwingConstants.CENTER);
	gk_faja.setBounds(new Rectangle(5, 480, 65, 20));
	jLabel5.setText("X");
	jLabel5.setBounds(new Rectangle(245, 458, 15, 15));
	jLabel6.setText("Y");
	jLabel6.setBounds(new Rectangle(245, 483, 15, 15));
	fromCoord.addItem("From");
	fromCoord.addItem("LL");
	fromCoord.addItem("UTM");
	fromCoord.addItem("GK");
	toCoord.addItem("To");
	toCoord.addItem("LL");
	toCoord.addItem("UTM");
	toCoord.addItem("GK");
	for (int i = 1; i <= 60; i++) {
	    utm_zone.addItem(String.valueOf(i) + "N");
	    utm_zone.addItem(String.valueOf(i) + "S");
	}
	for (int i = 1; i <= 7; i++) {
	    gk_faja.addItem(String.valueOf(i));
	}
    }

    private void bAbrir_actionPerformed(ActionEvent e) {
	String fileName = loadShapesFromSHPFile();
	jlFileProps.setText(fileName + ": " + shapes.size() + " shape(s)");
    }

    private String loadShapesFromSHPFile() {
	File file = selectInputFile();
	String fileName;
	if (file != null) {
	    SHPReader reader = new SHPReader();
	    String path = file.getAbsolutePath();
	    fileName = path.substring(0, path.indexOf(".shp"));
	    if (fromCoord.getItemAt(fromCoord.getSelectedIndex()).equals(toCoord.getItemAt(toCoord.getSelectedIndex()))) {
		System.out.println("Sistemas de coordenadas identicos, no se realizara conversion");
		shapes = reader.read(fileName);
	    } else if (fromCoord.getItemAt(fromCoord.getSelectedIndex()).equals("From") || (toCoord.getItemAt(toCoord.getSelectedIndex()).equals("To"))) {
		System.out.println("Sistemas de coordenadas identicos, no se realizara conversion");
		shapes = reader.read(fileName);
	    } else {
		shapes = reader.read(fileName, fromCoord.getSelectedIndex() - 1, toCoord.getSelectedIndex() - 1);
	    }
	    //IMPORTAR EL ARCHIVO
	    shapeType = reader.getShapeType();
	    return path;
	} else {
	    return "Invalid input file";
	}
    }

    private File selectInputFile() {
	File file;
	String lastpath = cfg.getProperty("lastpath");
	//String filename = File.separator+"shp";
	JFileChooser fc = new JFileChooser(lastpath);
	fc.setFileFilter(new SHPFilter());
	fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	fc.setAcceptAllFileFilterUsed(false);
	// Show open dialog; this method does not return until the dialog is closed
	fc.showOpenDialog(this);
	try {
	    file = fc.getSelectedFile();
	    cfg.setProperty("lastpath", file.getPath());
	} catch (NullPointerException ex) {
	    file = null;
	}
	return file;
    }

    private void bSelectProject_actionPerformed(ActionEvent e) {
	//SELECCIONAR PROYECTO!!!
	idProject = 1;
	jlProjectProps.setText("Project: " + "Incahuasi - ID: " + idProject);
    }

    private void bInsertDB_actionPerformed(ActionEvent e) {
	String idColumn = "idproject";
	crearTabla("projects", "idproject");
	insertarDB(shapes);
	bDrawDesk.setEnabled(true);
    }

    private void crearTabla(String _table, String _idColumn) {
	String _scheme = "public";
	ShapeTypes shapeTypes = new ShapeTypes(shapeType);
	System.out.println(shapeTypes.getSQLCreateTable(_scheme, _table, _idColumn));
	LibSQL.exActualizar('a', shapeTypes.getSQLCreateTable(_scheme, _table, _idColumn));
    }

    private void bEmptyTable_actionPerformed(ActionEvent e) {
	vaciarTabla("public", "projects", "idproject", String.valueOf(idProject));
    }

    private void vaciarTabla(String _scheme, String _table) {
	String SQLEmptyTable = "DELETE FROM \"" + _scheme + "\".\"" + _table + "\";";
	LibSQL.exActualizar('b', SQLEmptyTable);
    }

    private void vaciarTabla(String _scheme, String _table, String _idColumn, String _ID) {
	String SQLEmptyTable = "DELETE FROM \"" + _scheme + "\".\"" + _table + "\" where " + _idColumn + " = " + _ID + ";";
	LibSQL.exActualizar('b', SQLEmptyTable);
    }

    private void bDrawDesk_actionPerformed(ActionEvent e) {
	verPoligonos(idProject);
    }

    private void verPoligonos(int _idProject) {
	DrawFrame frame = new DrawFrame(_idProject);
	if (frame.hasPolygons()) {
	    //frame.setModal(true);
	    frame.show();
	} else {
	    frame.dispose();
	}
    }

    private void insertarDB(Vector _shapes) {
	switch (shapeType) {
	    case ShapeTypes.POLYGON :
		for (int i = 0; i < _shapes.size(); i++) {
		    SHPPolygon poly = (SHPPolygon)shapes.elementAt(i);
		    //Proc.exActualizar('a', "delete from projects_polygons where gid>100");
		    LibSQL.exActualizar('a', poly.getSQLString("public", "projects_polygons", "idproject", 1));
		}
		break;
	    case ShapeTypes.POINT :
		for (int i = 0; i < _shapes.size(); i++) {
		    SHPPoint point = (SHPPoint)shapes.elementAt(i);
		    LibSQL.exActualizar('a', point.getSQLString("public", "projects_points", "idproject", "1"));
		}
		break;
	    default :
		break;
	}
    }

    private void bConvertGK2UTM_actionPerformed(ActionEvent e) {
	double gkx = Double.parseDouble(gk_x.getText());
	double gky = Double.parseDouble(gk_y.getText());
	LatLongCoord geod = CoordinateSystems.gk2geo(gkx, gky, 0);
	UTMCoord utm = CoordinateSystems.geo2utm(geod.getLatitude(), geod.getLongitude());
	utm_x.setText(String.valueOf(utm.getEasting()));
	utm_y.setText(String.valueOf(utm.getNorthing()));
	String zone = String.valueOf(utm.getZone()) + ((utm.getBand() < 0) ? "S" : "N");
	utm_zone.setSelectedItem(zone);
    }

    private void bConvertUTM2GK_actionPerformed(ActionEvent e) {
	double utmeast = Double.parseDouble(utm_x.getText());
	double utmnorth = Double.parseDouble(utm_y.getText());
	double utmzone = Double.parseDouble(utm_zone.getSelectedItem().toString().replaceAll("N", "").replaceAll("S", ""));
	LatLongCoord geod = CoordinateSystems.utm2geo(utmeast, utmnorth, utmzone);
	GKCoord gk = CoordinateSystems.geo2gk(geod.getLatitude(), geod.getLongitude());
	gk_x.setText(String.valueOf(gk.getX()));
	gk_y.setText(String.valueOf(gk.getY()));
	gk_faja.setSelectedItem(String.valueOf(gk.getFaja()));
    }

}

package org.digitall.apps.legalprocs.manager;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.sql.LibSQL;

//

public class ExplorationRequestPanel extends BasicContainerPanel {

    private JTabbedPane jtabExplorationRequest = new JTabbedPane();
    private ERFilePanel filePanel;
    private SolicitorPanel jpSolicitor = new SolicitorPanel();
    private LegalRepresentativePanel jplegalRepresentative = new LegalRepresentativePanel();
    private CoordGKPanel coordGKPanel = new CoordGKPanel();
    private TerrainDataPanel terrainDataPanel = new TerrainDataPanel();
    private ERProgramPanel erProgramPanel = new ERProgramPanel();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnCancel = new CloseButton();
    private static String type = "";
    private final int FRAME = 1;
    private final int INTERNALFRAME = 2;
    private final int DIALOG = 3;
    private int parentType = -1;
    private Component parent;
    private static String idExplorationScheme = "";
    private String IdFile = "";

    public ExplorationRequestPanel(BasicInternalFrame _parent, String _type) {
	try {
	    type = _type;
	    parent = _parent;
	    parentType = INTERNALFRAME;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ExplorationRequestPanel(BasicDialog _parent, String _type) {
	try {
	    type = _type;
	    parent = _parent;
	    parentType = DIALOG;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public ExplorationRequestPanel(JFrame _parent, String _type) {
	try {
	    type = _type;
	    parent = _parent;
	    parentType = FRAME;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(603, 499));
	if (type.equals("ER")) {
	    filePanel = new ERFilePanel("ER");
	} else {

	}
	jtabExplorationRequest.setBounds(new Rectangle(5, 0, 590, 445));
	jpSolicitor.setSize(new Dimension(580, 418));
	filePanel.setSize(580, 117);
	jtabExplorationRequest.addTab("Expediente", filePanel);
	jtabExplorationRequest.addTab("Solicitante", jpSolicitor);
	jtabExplorationRequest.addTab("Rep. Legal", jplegalRepresentative);
	jtabExplorationRequest.addTab("Coordenadas G.K.", coordGKPanel);
	jtabExplorationRequest.addTab("Terrenos", terrainDataPanel);
	jtabExplorationRequest.addTab("Programa", erProgramPanel);
	this.add(jtabExplorationRequest, null);
	this.add(btnCancel, null);
	this.add(btnAccept, null);
	btnAccept.setBounds(new Rectangle(550, 465, 45, 25));
	btnAccept.setMnemonic('A');
	btnAccept.setToolTipText("Aceptar");
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnCancel.setBounds(new Rectangle(5, 465, 45, 25));
	btnCancel.setMnemonic('C');
	btnCancel.setToolTipText("Cancelar");
	btnCancel.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnCancel_actionPerformed(e);
				 }

			     }
	);
    }

    private void dispose() {
	switch (parentType) {
	    case DIALOG :
		((BasicDialog)parent).dispose();
		break;
	    case INTERNALFRAME :
		((BasicInternalFrame)parent).dispose();
		break;
	    case FRAME :
		((JFrame)parent).dispose();
		break;
	}
    }

    private void btnCancel_actionPerformed(ActionEvent e) {
	dispose();
    }

    public static String getType() {
	return type;
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	/** 0º) insertar una tupla en file.addresses para relacionarla con el pedido de exploracion  */
	String locationInsert = filePanel.getLocationInsert();
	String idlocation = filePanel.getIdUbicationInsert();
	//System.out.println("--> " + locationInsert + idlocation);
	/** 1º) insertar una tupla en files para relacionarla con el pedido de exploracion  */
	String year = filePanel.getFileYear();
	String expirationDate = "";
	if (year.equals("")) {
	    year = "null";
	} else {
	    year = "'" + year + "'";
	}
	if (filePanel.getExpirationDate().equals("")) {
	    expirationDate = "null";
	} else {
	    expirationDate = "'" + filePanel.getExpirationDate() + "'";
	}
	IdFile = org.digitall.lib.sql.LibSQL.getCampo("SELECT MAX(idfile) + 1 FROM file.files");
	String fileInsert = "INSERT INTO file.files VALUES(" + IdFile + ",0" + idlocation + ",0,0,0" + filePanel.getFileNumber() + ",'" + filePanel.getFileLetter() + "'," + year + ",'cateo'," + expirationDate + ",'',0,false,0,'');";
	//System.out.println("filesInsert: " + fileInsert);
	/** 2º) insertar una tupla en entitiesbyfile para ligarlos con los solicitantes y representantes legales */
	String idSolicitor = jpSolicitor.getIdSolicitor();
	String idRepresentative = jplegalRepresentative.getIdRepresentative();
	String solRepresInsert = "INSERT INTO file.entitiesbyfile VALUES(" + IdFile + "," + idSolicitor + " ,'');";
	solRepresInsert += "INSERT INTO file.entitiesbyfile VALUES(" + IdFile + "," + idRepresentative + ",'');";
	//System.out.println("solRepres--> " + fileInsert +  "*** " + solRepresInsert);
	/** 3º) insertar una tupla en Terrain Data */
	String TDInsert = terrainDataPanel.getTDInsert();
	String TDId = terrainDataPanel.getTDId();
	//System.out.println("TDInsert: " + TDInsert);
	/** 4º) insertar una tupla la tabla polygons para relacionarla con permiso de Exploracion */
	String idPoly = org.digitall.lib.sql.LibSQL.getCampo("SELECT max(idpolygon)+1 from file.polygons");
	String polyInsert = "INSERT INTO file.polygons VALUES ( " + idPoly + ",0" + coordGKPanel.getArea() + "," + getSQLPolygonString(coordGKPanel.getPoints()) + ",'');";
	/** 5º) Insert para el Permiso de Exploración */
	String mineralCategory = coordGKPanel.getMinCat();
	String idExplorationRequest = org.digitall.lib.sql.LibSQL.getCampo("Select max(idexplorationrequest) + 1 From file.explorationrequests ");
	String ERInsert = " INSERT INTO file.explorationrequests VALUES( " + idExplorationRequest + " ,'" + mineralCategory + "'," + IdFile + " ," + idPoly + " ,0" + idExplorationScheme + " ," + TDId + " , " + erProgramPanel.getPlazo() + " , " + erProgramPanel.getCycleType() + " , " + erProgramPanel.getPermisosOtorgados() + " , " + erProgramPanel.getUnidadesOtorgadas() + " ,null ,0 " + " , " + erProgramPanel.getCycleNumber() + " , " + erProgramPanel.getWorkProgram() + ", '' ); ";
	//System.out.println("POLYInsert--> " + polyInsert);
	//System.out.println("idExplorationScheme: " + idExplorationScheme);
	/** 6º Insert para la tabla FilesApplications */
	String startDate = "";
	if (filePanel.getStartDate().equals("")) {
	    startDate = "null";
	} else {
	    startDate = "'" + filePanel.getStartDate() + "'";
	}
	String fileapplicationsInsert = "INSERT INTO file.fileapplications VALUES(" + IdFile + ",1," + idExplorationRequest + "," + startDate + "," + expirationDate + ",'','');";
	//System.out.println("fileapplicationsInsert --> " + fileapplicationsInsert );
	if (coordGKPanel.getPointsSize() == 0) {
	    if (Advisor.question(this, "Message", "No está guardando las coordenadas del area a explorar. ¿Deséa continuar?")) {
		//System.out.println("superInsert--> " + fileInsert + solRepresInsert + TDInsert + polyInsert + ERInsert);
		if (LibSQL.exActualizar('a', locationInsert + fileInsert + solRepresInsert + TDInsert + polyInsert + ERInsert + fileapplicationsInsert)) {
		    org.digitall.lib.components.Advisor.messageBox("operation success!", "Message!");
		    dispose();
		} else {
		    org.digitall.lib.components.Advisor.messageBox("Ocurrió un error, los datos no se registraron.", "Error!");
		}
	    } else {
		org.digitall.lib.components.Advisor.messageBox("Operación cancelada, los datos no se registraron.", "Aviso");
	    }
	} else if (coordGKPanel.getPermimtir()) {
	    System.out.println("superInsert--> " + locationInsert + fileInsert + solRepresInsert + TDInsert + polyInsert + ERInsert + fileapplicationsInsert);
	    if (LibSQL.exActualizar('a', locationInsert + fileInsert + solRepresInsert + TDInsert + polyInsert + ERInsert + fileapplicationsInsert)) {
		org.digitall.lib.components.Advisor.messageBox("operation success!", "Message!");
		dispose();
	    } else {
		org.digitall.lib.components.Advisor.messageBox("Ocurrió un error, los datos no se registraron.", "Error!");
	    }
	} else {
	    org.digitall.lib.components.Advisor.messageBox("Existe un punto al cual no se le asigno un valor", "Error");
	}
    }

    public String getSQLPolygonString(Point2D[] points) {
	try {
	    //String sql = "GeometryFromText('MULTIPOLYGON(((";
	    String sql = "GeometryFromText('POLYGON(((";
	    for (int j = 0; j < points.length; j++) {
		sql += points[j].getX() + " " + points[j].getY() + ",";
	    }
	    sql += points[0].getX() + " " + points[0].getY();
	    sql += ")))',-1)";
	    return sql;
	} catch (Exception e) {
	    return "null";
	}
    }

    public static void setIdExplorationScheme(String _idExplorationScheme) {
	idExplorationScheme = _idExplorationScheme;
    }

}

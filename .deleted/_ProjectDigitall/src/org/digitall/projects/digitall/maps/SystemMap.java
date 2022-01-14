package org.digitall.projects.digitall.maps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DecimalFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.digitall.apps.resourcescontrol.interfaces.resourcesadmin.ResourcesAdminMain;
import org.digitall.apps.resourcesrequests.interfaces.resourcesrequests.ResourcesRequestsMain;
import org.digitall.common.cashflow.interfaces.CompanyTreePanel;
import org.digitall.common.cashflow.interfaces.account.BankAccountsList;
import org.digitall.common.cashflow.interfaces.account.PaymentsOrdersMain;
import org.digitall.common.cashflow.interfaces.accounting.AccountsMain;
import org.digitall.common.cashflow.interfaces.budget.BudgetList;
import org.digitall.common.cashflow.interfaces.cashmovement.CashMovementTypesTree;
import org.digitall.common.cashflow.interfaces.cashmovement.CashMovementsList;
import org.digitall.common.cashflow.interfaces.costscentre.CCList;
import org.digitall.common.components.combos.JCombo;
import org.digitall.common.resourcescontrol.interfaces.PersonsList;
import org.digitall.common.resourcescontrol.interfaces.ResourcesList;
import org.digitall.common.resourcescontrol.interfaces.SkillList;
import org.digitall.common.resourcesrequests.interfaces.purchaseorder.PurchaseOrderGenerateList;
import org.digitall.common.resourcesrequests.interfaces.resourcesmovements.ResourcesDeliverList;
import org.digitall.common.resourcesrequests.interfaces.resourcesmovements.ResourcesReceiveMain;
import org.digitall.common.resourcesrequests.interfaces.resourcesrequests.ResourcesRequestsAuthMain;
import org.digitall.common.resourcesrequests.reports.resourcesrequests.ResourcesRequestForAuthListReport;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicDesktop;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.basic.MapButton;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.geom.Polygon2D;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;

public class SystemMap extends BasicDesktop {

    private String operacionStatus;
    private BasicLabel labelxy = new BasicLabel();
    private BasicLabel labelinfo = new BasicLabel();
    //Graba en la Base de Datos
    private final int INFO = 8;
    //Variables de Posicion
    private Point mousePosition = new Point();
    private Point2D.Double currentPosition = new Point2D.Double();
    private Point2D.Double puntoOrigen;
    private Point startDragPosition = null;
    //Variables de Dibujo
    private Vector<MapEntity> entities = new Vector<MapEntity>();
    private Vector<MapButton> mapButtons = new Vector<MapButton>();
    private Vector annotations = new Vector();
    private Vector<MapConnector> connectors = new Vector<MapConnector>();
    private Vector parcelas = new Vector();
    private Vector parcelasTemporales = new Vector();
    private Vector distancePoints = new Vector();
    private Vector osnapPoints = new Vector();
    //Servira de algo?
    private RectangularShape osnapRectangle;
    private int containedParcela = -1;
    private int containedEntity = -1;
    private int containedPoint = -1;
    private Vector badLines = new Vector();
    protected Line2D panLine = new Line2D.Float();
    //Variables del Universo
    private double xMin = 0;
    private double xMax = 0;
    private double yMin = 0;
    private double yMax = 0;
    private double xExtents = 0;
    private double yExtents = 0;
    private double drawFactor = 0;
    private double drawFactorOriginal = 0;
    private double xOffset = 0;
    private double yOffset = 0;
    private double xOffsetOriginal = 0;
    private double yOffsetOriginal = 0;
    private double xOffsetPosta = 0;
    private double yOffsetPosta = 0;
    private double fWidth = 800;
    private double fHeight = 500;
    private double drawScale = 1;
    //Variables de trabajo
    private MouseListener eraseListener;
    private MouseMotionListener eraseMotionListener;
    private boolean osnapActive = true;
    private int osnapRectSize = 8;
    //Variables para una parcela
    private Point2D x0Parcela;
    private Point2D x1Parcela;
    //Variables para un trapecio
    boolean aTrapecio = false;
    Point2D.Double a0Trapecio = null;
    Point2D.Double a1Trapecio = null;
    boolean hTrapecio = false;
    Point2D.Double h0Trapecio = null;
    Point2D.Double h1Trapecio = null;
    private JButton btnReload = new JButton();
    private JPanel drawLabel;
    private JCombo fontNamesCombo = new JCombo();
    private JCombo fontSizesCombo = new JCombo();
    public static String predefinedFontName = "Lucida Sans";
    public static int predefinedFontSize = 12;
    private Font gFont = new Font(predefinedFontName, Font.PLAIN, predefinedFontSize);

    public SystemMap(int _id, String _name) {
	super(_id, _name);
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	fWidth = getBounds().getMaxX();
	fHeight = getBounds().getMaxY();
	labelxy.setText("labelxy");
	labelinfo.setText("labelinfo");
	btnReload.setText("Reload");
	btnReload.setBounds(new Rectangle(5, 30, 80, 20));
	btnReload.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnRefresh_actionPerformed(e);
				 }

			     }
	);
	addMouseWheelListener(CommonWheelListener);
	addMouseListener(CommonMouseListener);
	addMouseMotionListener(CommonMotionListener);
	btnReload.setVisible(false);
	add(btnReload, null);
	//add(fontNamesCombo, null);
	//add(fontSizesCombo, null);
	setOperation(INFO);
	doEntities();
	doLines();
	doAnnotations();
	String[] names = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	for (int i = 0; i < names.length; i++) {
	    fontNamesCombo.addItem(names[i]);
	    fontSizesCombo.addItem(String.valueOf(i));
	}
	fontNamesCombo.setBounds(new Rectangle(5, 5, 145, 20));
	fontSizesCombo.setBounds(new Rectangle(155, 5, 45, 20));
	fontNamesCombo.addItemListener(new ItemListener() {

				    public void itemStateChanged(ItemEvent e) {
					setFonts();
				    }

				}
	);
	fontSizesCombo.addItemListener(new ItemListener() {

				    public void itemStateChanged(ItemEvent e) {
					setFonts();
				    }

				}
	);
	String fontName = Environment.cfg.getProperty("MapFontName");
	String fontSize = Environment.cfg.getProperty("MapFontSize");
	fontNamesCombo.setSelectedItem(fontName.equals(Environment.cfgNullProperty) ? predefinedFontName : fontName);
	fontSizesCombo.setSelectedItem(fontSize.equals(Environment.cfgNullProperty) ? String.valueOf(predefinedFontSize) : fontSize);
	drawLabel = new JPanel() {

		public void paint(Graphics g) {
		    super.paint(g);
		    Graphics2D g2 = (Graphics2D)g;
		    //Recorro el vector Poligonos para dibujar
		    //TODOS los poligonos del SQL que carga doPolys();
		    /*for (int minas = 0; minas < entities.size(); minas++) {
			MapEntity _entity = entities.elementAt(minas);
			Polygon2D _entityPoly = getFakePolyFromPoly(_entity.getPolygon());
			if (_entity.getFillColor() != null) {
			    g2.setColor(_entity.getFillColor());
			    g2.fill(_entityPoly);
			}
			if (containedEntity != -1) {
			    g2.setColor(Color.magenta);
			    if (minas == containedEntity) {
				g2.fill(_entityPoly);
			    }
			}
			g2.setFont(gFont);
			g2.setColor(_entity.getColor());
			g2.drawString(String.valueOf(_entity.getGid()), xtoPixel(_entity.getInitialPoint().getX()), ytoPixel(_entity.getInitialPoint().getY()));
			String[] clines = _entity.getContent().split("\n");
			for (int i = 0; i < clines.length; i++) {
			    g2.drawString(clines[i], xtoPixel(_entity.getInitialPoint().getX()), ytoPixel(_entity.getInitialPoint().getY()) + g2.getFontMetrics().getHeight() * (i + 1));
			}
			g2.draw(_entityPoly);
		    }*/
		    for (int lines = 0; lines < connectors.size(); lines++) {
			MapConnector _connector = connectors.elementAt(lines);
			Polygon2D _connectorPoly = getFakePolyFromPoly(_connector.getPolygon());
			g2.setStroke(_connector.getStroke());
			g2.setColor(_connector.getColor());
			//g2.drawString(String.valueOf(_connector.getGid()), (int)_connectorPoly.getX(0), (int)_connectorPoly.getY(0));
			for (int i = 0; i < _connectorPoly.getVertexCount() - 1; i++) {
			    g2.drawLine((int)_connectorPoly.getX(i), (int)_connectorPoly.getY(i), (int)_connectorPoly.getX(i + 1), (int)_connectorPoly.getY(i + 1));
			}
		    }
		    g2.setStroke(new BasicStroke());
		    for (int puntos = 0; puntos < annotations.size(); puntos++) {
			Annotation2D p = (Annotation2D)annotations.elementAt(puntos);
			/*Shape _annotation = new Ellipse2D.Double(xtoPixel(p.getX()) - annotationSize / 2, ytoPixel(p.getY()) - annotationSize / 2, annotationSize, annotationSize);
			if (containedPoint != -1) {
			    g2.setColor(Color.magenta);
			    if (puntos == containedPoint) {
				g2.fill(_annotation);
			    }
			}
			g2.setColor(Color.black);
			g2.draw(_annotation);*/
			g2.drawString(p.getName(), xtoPixel(p.getX()), ytoPixel(p.getY()));
			/*
			Image imagen = Toolkit.getDefaultToolkit().getImage(Proced.getResource(IconTypes.FILES_LOGO_ON));
			int width = (int)(imagen.getWidth(this)*drawFactor);
			int height = (int)(imagen.getHeight(this)*drawFactor);
			int centX = xtoPixel(p.getX())-(width/2);
			int centY = ytoPixel(p.getY())-(height/2);
			g2.drawImage(imagen, centX, centY, width, height, this);
			*/
			/*for (int i = 0; i < loadedDrillsID.size(); i++) {
			    if (Integer.parseInt(loadedDrillsID.elementAt(i).toString()) == p.getIdPoint()) {
				if (((INFOFRAME)loadedDrills.elementAt(i)).isVisible()) {
				    g2.setColor(Color.red);
				    g2.draw(drill);
				}
			    }
			}*/
		    }
		    g2.setColor(Color.red);
		    for (int lines = 0; lines < badLines.size(); lines++) {
			Line2D.Double _line = (Line2D.Double)badLines.elementAt(lines);
			int x0 = xtoPixel(_line.getX1());
			int x1 = xtoPixel(_line.getX2());
			int y0 = ytoPixel(_line.getY1());
			int y1 = ytoPixel(_line.getY2());
			g2.drawLine(x0, y0, x1, y1);
		    }
		    g2.setColor(Color.lightGray);
		    g2.draw(panLine);
		    //Dibujo el texto del puntero del mouse
		    /*if (currentPosition != null) {
			g2.setColor(Color.black);
			g2.drawString("(" + decimalFormat(currentPosition.getX(), 4) + " " + decimalFormat(currentPosition.getY(), 4) + ")", (int)mousePosition.getX(), (int)mousePosition.getY());
		    }*/
		    g2.setColor(Color.magenta);
		    for (int i = 0; i < parcelas.size(); i++) {
			Polygon2D _parcela = (Polygon2D)parcelas.elementAt(i);
			if (i == containedParcela) {
			    g2.fill(getFakePolyFromPoly(_parcela));
			    if (_parcela.getId() != -1) {
				//Dibujo el ID de la mina
				//System.out.println("ID MINA: " + _parcela.getId());
				g2.setColor(Color.black);
				g2.drawString(String.valueOf(_parcela.getId()), (int)mousePosition.getX(), (int)mousePosition.getY());
				g2.setColor(Color.magenta);
			    }
			}
			g2.draw(getFakePolyFromPoly(_parcela));
		    }
		    for (int i = 0; i < parcelasTemporales.size(); i++) {
			g2.setColor(Color.lightGray);
			g2.draw(getFakePolyFromPoly((Polygon2D)parcelasTemporales.elementAt(i)));
		    }
		    g2.setColor(Color.lightGray);
		    for (int buttons = 0; buttons < mapButtons.size(); buttons++) {
			MapButton _mapButton = mapButtons.elementAt(buttons);
			Point _centroid = new Point(xtoPixel(_mapButton.getCentroid().getX()), ytoPixel(_mapButton.getCentroid().getY()));
			int x = _centroid.x - _mapButton.getWidth() / 2;
			int y = _centroid.y - _mapButton.getHeight() / 2;
			_mapButton.setVisible(true);
			_mapButton.setLocation(x, y);
		    }
		}

	    }
	;
	add(drawLabel, 0);
	drawLabel.setOpaque(false);
	drawLabel.setBounds(0, 0, getBounds().width, getBounds().height);
    }

    public void setFonts() {
	gFont = new Font(fontNamesCombo.getSelectedItem().toString(), Font.PLAIN, Integer.parseInt(fontSizesCombo.getSelectedItem().toString()));
	Environment.cfg.setProperty("MapFontName", fontNamesCombo.getSelectedItem().toString());
	Environment.cfg.setProperty("MapFontSize", fontSizesCombo.getSelectedItem().toString());
	repaint();
    }

    public void paint(Graphics g) {
	calculaEntorno();
	super.paint(g);
    }

    private void doEntities() {
	try {
	    String Data = "the_geom from (select gid, the_geom as the_geom, complete, completetext, extra1, extra1text, extra2, extra2text, extra3, extra3text from maps.entidades) as foo";
	    Data = Data.trim();
	    String geometry = Data.substring(0, Data.indexOf(" ")).trim();
	    Data = Data.substring(Data.indexOf(" ")).trim();
	    String LabelItem = "complete, completetext, extra1, extra1text, extra2, extra2text, extra3, extra3text, ";
	    String Qwhere = "";
	    String Query = "select " + LabelItem + " AsText(" + geometry + ") as the_geom, npoints(" + geometry + "), xmin(" + geometry + "), ymax(" + geometry + "), x(centroid(" + geometry + ")) as xcenter, y(centroid(" + geometry + ")) as ycenter, gid " + Data + Qwhere;
	    ResultSet count = LibSQL.exQuery("select count(*) from (" + Query + ") as foo");
	    count.next();
	    ResultSet Polygons = LibSQL.exQuery(Query);
	    if (Qwhere == Qwhere) {
		ResultSet Extent = LibSQL.exQuery("select " + "xmin(extent(" + geometry + ")), " + "xmax(extent(" + geometry + ")), " + "ymin(extent(" + geometry + ")), " + "ymax(extent(" + geometry + ")) " + Data);
		Extent.next();
		xMin = Extent.getDouble("xmin");
		xMax = Extent.getDouble("xmax");
		yMin = Extent.getDouble("ymin");
		yMax = Extent.getDouble("ymax");
		xExtents = xMax - xMin;
		yExtents = yMax - yMin;
	    }
	    while (Polygons.next()) {
		/**Poligono*/
		int fNumPoints = Polygons.getInt("npoints");
		String Poly = Polygons.getString("the_geom");
		//Si es polÃ­gono cerrado, por ejemplo Manzana o Parcela
		Poly = Poly.substring(Poly.indexOf("(((") + 3, Poly.length() - 3);
		double[] xd = new double[fNumPoints];
		double[] yd = new double[fNumPoints];
		for (int i = 0; i < fNumPoints - 1; i++) {
		    xd[i] = Double.parseDouble(Poly.substring(0, Poly.indexOf(" ")).trim());
		    yd[i] = Double.parseDouble(Poly.substring(Poly.indexOf(" "), Poly.indexOf(",")).trim());
		    Poly = Poly.substring(Poly.indexOf(",") + 1, Poly.length());
		    addOsnapPoint(new Point2D.Double(xd[i], yd[i]));
		}
		xd[fNumPoints - 1] = Double.parseDouble(Poly.substring(0, Poly.indexOf(" ")).trim());
		yd[fNumPoints - 1] = Double.parseDouble(Poly.substring(Poly.indexOf(" "), Poly.length()).trim());
		addOsnapPoint(new Point2D.Double(xd[fNumPoints - 1], yd[fNumPoints - 1]));
		MapEntity _entity = new MapEntity(Polygons.getInt("gid"));
		_entity.retrieveData();
		_entity.setPolygon(getTruePolyFromPoints(xd, yd));
		_entity.setInitialPoint(new Point2D.Double(Polygons.getDouble("xmin"), Polygons.getDouble("ymax")));
		entities.addElement(_entity);
		Map<String, String> _extraFunctions = new HashMap();
		if (Polygons.getBoolean("complete")) {
		    _extraFunctions.put("complete", Polygons.getString("completetext"));
		}
		if (Polygons.getBoolean("extra1")) {
		    _extraFunctions.put("extra1", Polygons.getString("extra1text"));
		}
		if (Polygons.getBoolean("extra2")) {
		    _extraFunctions.put("extra2", Polygons.getString("extra2text"));
		}
		if (Polygons.getBoolean("extra3")) {
		    _extraFunctions.put("extra3", Polygons.getString("extra3text"));
		}
		addMapButton(Polygons.getInt("gid"), new Point2D.Double(Polygons.getDouble("xcenter"), Polygons.getDouble("ycenter")), _extraFunctions);
	    }
	} catch (SQLException x) {
	    Advisor.messageBox(x.getErrorCode() + ": " + x.getMessage(), "Error en la consulta SQL");
	}
    }

    private void doLines() {
	try {
	    String Data = "the_geom from (select gid, the_geom as the_geom from maps.lineas) as foo";
	    Data = Data.trim();
	    String geometry = Data.substring(0, Data.indexOf(" ")).trim();
	    Data = Data.substring(Data.indexOf(" ")).trim();
	    String LabelItem = "";
	    String Qwhere = "";
	    String Query = "select " + LabelItem + " AsText(" + geometry + ") as the_geom, npoints(" + geometry + "), gid " + Data + Qwhere;
	    ResultSet count = LibSQL.exQuery("select count(*) from (" + Query + ") as foo");
	    count.next();
	    //polys = new Polygon2D[count.getInt(1)];
	    //      selectedPath = new int[count.getInt(1)];
	    ResultSet Polygons = LibSQL.exQuery(Query);
	    if (Qwhere == Qwhere) {
		ResultSet Extent = LibSQL.exQuery("select " + "xmin(extent(" + geometry + ")), " + "xmax(extent(" + geometry + ")), " + "ymin(extent(" + geometry + ")), " + "ymax(extent(" + geometry + ")) " + Data);
		Extent.next();
		xMin = Extent.getDouble("xmin");
		xMax = Extent.getDouble("xmax");
		yMin = Extent.getDouble("ymin");
		yMax = Extent.getDouble("ymax");
		xExtents = xMax - xMin;
		yExtents = yMax - yMin;
	    }
	    while (Polygons.next()) {
		/**Poligono*/
		int fNumPoints = Polygons.getInt("npoints");
		String Poly = Polygons.getString("the_geom");
		//Si es linea o polilinea
		Poly = Poly.substring(Poly.indexOf("((") + 2, Poly.length() - 2);
		double[] xd = new double[fNumPoints];
		double[] yd = new double[fNumPoints];
		for (int i = 0; i < fNumPoints - 1; i++) {
		    xd[i] = Double.parseDouble(Poly.substring(0, Poly.indexOf(" ")).trim());
		    yd[i] = Double.parseDouble(Poly.substring(Poly.indexOf(" "), Poly.indexOf(",")).trim());
		    Poly = Poly.substring(Poly.indexOf(",") + 1, Poly.length());
		    addOsnapPoint(new Point2D.Double(xd[i], yd[i]));
		}
		xd[fNumPoints - 1] = Double.parseDouble(Poly.substring(0, Poly.indexOf(" ")).trim());
		yd[fNumPoints - 1] = Double.parseDouble(Poly.substring(Poly.indexOf(" "), Poly.length()).trim());
		addOsnapPoint(new Point2D.Double(xd[fNumPoints - 1], yd[fNumPoints - 1]));
		MapConnector _connector = new MapConnector(Polygons.getInt("gid"));
		_connector.retrieveData();
		_connector.setPolygon(getTruePolyFromPoints(xd, yd));
		connectors.addElement(_connector);
	    }
	} catch (SQLException x) {
	    Advisor.messageBox(x.getErrorCode() + ": " + x.getMessage(), "Error en la consulta SQL");
	}
    }

    private void doAnnotations() {
	try {
	    String Data = " the_geom from (select centroid(the_geom) as the_geom, gid as gid from maps.notas) as foo";
	    Data = Data.trim();
	    String geometry = " the_geom ";
	    //Data.substring(0, Data.indexOf(" ")).trim();
	    Data = Data.substring(Data.indexOf(" ")).trim();
	    String LabelItem = "gid,";
	    String Qwhere = "";
	    String Query = "select " + LabelItem + geometry + ", X(" + geometry + "), Y(" + geometry + ") " + Data + Qwhere;
	    //System.out.println(Query);
	    ResultSet count = LibSQL.exQuery("select count(*) from (" + Query + ") as foo");
	    count.next();
	    ResultSet Points = LibSQL.exQuery(Query);
	    if (Qwhere == Qwhere) {
		Query = "select " + "xmin(extent(" + geometry + ")), " + "xmax(extent(" + geometry + ")), " + "ymin(extent(" + geometry + ")), " + "ymax(extent(" + geometry + ")) " + Data;
		//System.out.println(Query);
		ResultSet Extent = LibSQL.exQuery(Query);
		Extent.next();
		xMin = Extent.getDouble("xmin");
		xMax = Extent.getDouble("xmax");
		yMin = Extent.getDouble("ymin");
		yMax = Extent.getDouble("ymax");
		xExtents = xMax - xMin;
		yExtents = yMax - yMin;
		//ARREGLO POR SI HAY UN SOLO PUNTO PARA QUE SE PUEDA NAVEGAR Y HACER ZOOM
		if (xExtents < 10 && yExtents < 10) {
		    xExtents = 400;
		    yExtents = 400;
		}
	    }
	    while (Points.next()) {
		/**Punto*/
		Annotation2D punto = new Annotation2D.Double(Points.getDouble("X"), Points.getDouble("Y"));
		addOsnapPoint(new Point2D.Double(punto.getX(), punto.getY()));
		punto.setName(Points.getString("gid"));
		annotations.addElement(punto);
		//System.out.println("Punto: " + punto.getX());
	    }
	} catch (SQLException x) {
	    Advisor.messageBox(x.getErrorCode() + ": " + x.getMessage(), "Error en la consulta SQL");
	}
    }

    public Polygon2D getTruePolyFromPoints(double[] xp, double[] yp) {
	int cantidad = xp.length;
	if (cantidad > 0) {
	    double[] xy = new double[cantidad * 2];
	    for (int i = 0; i < cantidad * 2; i += 2) {
		xy[i] = xp[i / 2];
		xy[i + 1] = yp[i / 2];
	    }
	    Polygon2D poly = new Polygon2D.Double(xy);
	    return poly;
	} else {
	    return new Polygon2D.Double(0, 0);
	}
    }

    public Polygon2D getTruePolyFromPoints(Vector _points) {
	if (_points.size() > 0) {
	    double[] xy = new double[_points.size() * 2];
	    for (int i = 0; i < _points.size() * 2; i += 2) {
		Point2D _punto = (Point2D.Double)_points.elementAt(i / 2);
		xy[i] = _punto.getX();
		xy[i + 1] = _punto.getY();
	    }
	    Polygon2D poly = new Polygon2D.Double(xy);
	    return poly;
	} else {
	    return new Polygon2D.Double(0, 0);
	}
    }

    public Polygon2D getTruePolyFromPoints(Point2D.Double[] _xy) {
	if (_xy.length > 0) {
	    double[] xy = new double[_xy.length * 2];
	    for (int i = 0; i < _xy.length * 2; i += 2) {
		xy[i] = _xy[i / 2].getX();
		xy[i + 1] = _xy[i / 2].getY();
	    }
	    Polygon2D poly = new Polygon2D.Double(xy);
	    return poly;
	} else {
	    return new Polygon2D.Double(0, 0);
	}
    }

    public Polygon2D getFakePolyFromPoly(Polygon2D _poly) {
	double[] xy = new double[_poly.getVertexCount() * 2];
	for (int i = 0; i < _poly.getVertexCount() * 2; i += 2) {
	    /*drawScale **/xy[i] = ((_poly.getX(i / 2) - xOffset) * drawFactor);
	    /*drawScale **/xy[i + 1] = (this.getHeight() - ((_poly.getY(i / 2) - yOffset) * drawFactor));
	}
	return new Polygon2D.Double(xy);
    }

    public Polygon2D getFakePolyFromPoints(double[] xp, double[] yp) {
	int cantidad = xp.length;
	if (cantidad > 0) {
	    double[] xy = new double[cantidad * 2];
	    for (int i = 0; i < cantidad * 2; i += 2) {
		/*drawScale **/xy[i] = ((xp[i / 2] - xOffset) * drawFactor);
		/*drawScale **/xy[i + 1] = (this.getHeight() - ((yp[i / 2] - yOffset) * drawFactor));
	    }
	    Polygon2D poly = new Polygon2D.Double(xy);
	    return poly;
	} else {
	    return new Polygon2D.Double(0, 0);
	}
    }

    private Point toPixel(double xd, double yd) {
	int x = /*drawScale * */(int)(((xd - xOffset) * drawFactor));
	int y = /*drawScale * */(int)((this.getHeight() - ((yd - yOffset) * drawFactor)));
	Point xy = new Point(x, y);
	return xy;
    }

    private Point toPixel(Point2D.Double xy) {
	int x = /*drawScale * */(int)(((xy.getX() - xOffset) * drawFactor));
	int y = /*drawScale * */(int)((this.getHeight() - ((xy.getY() - yOffset) * drawFactor)));
	return new Point(x, y);
    }

    private int xtoPixel(double x) {
	return /*drawScale * */(int)(((x - xOffset) * drawFactor));
    }

    private int ytoPixel(double y) {
	return /*drawScale * */(int)((this.getHeight() - ((y - yOffset) * drawFactor)));
    }

    private Point2D.Double toSpace(int x, int y) {
	double xd = x / drawFactor + xOffset;
	double yd = (this.getHeight() - y) / drawFactor + yOffset;
	return new Point2D.Double(xd, yd);
    }

    private Point2D.Double toSpace(Point _point) {
	double xd = _point.getX() / drawFactor + xOffset;
	double yd = (this.getHeight() - _point.getY()) / drawFactor + yOffset;
	return new Point2D.Double(xd, yd);
    }

    private Point2D.Double toSpace(Point2D _point) {
	double xd = _point.getX() / drawFactor + xOffset;
	double yd = (this.getHeight() - _point.getY()) / drawFactor + yOffset;
	return new Point2D.Double(xd, yd);
    }

    private double xtoSpace(int x) {
	return (x / drawFactor + xOffset);
    }

    private double ytoSpace(int y) {
	return ((this.getHeight() - y) / drawFactor + yOffset);
    }

    public void setOperation(int _operacion) {
	emptyData();
	removeMouseListener(eraseListener);
	removeMouseMotionListener(eraseMotionListener);
	switch (_operacion) {
	    case INFO :
		eraseListener = InfoListener;
		eraseMotionListener = InfoMotionListener;
		operacionStatus = "Show Info: ";
		break;
	}
	addMouseListener(eraseListener);
	addMouseMotionListener(eraseMotionListener);
    }
    /**REVISAR EL WHEELLISTENER*/
    protected MouseWheelListener CommonWheelListener = new MouseWheelListener() {

	    public void mouseWheelMoved(MouseWheelEvent me) {
		//drawScale += -.1 * me.getWheelRotation();
		if (me.getWheelRotation() < 0) {
		    drawScale = drawScale * (-1.05 * me.getWheelRotation());
		} else {
		    drawScale = drawScale / (1.05 * me.getWheelRotation());
		}
		drawFactor = drawFactorOriginal * drawScale;
		repaint();
	    }

	}
    ;
    protected MouseListener CommonMouseListener = new MouseListener() {

	    public void mousePressed(MouseEvent me) {
		if (me.getButton() == me.BUTTON2) {
		    drawLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		    startDragPosition = mousePosition;
		}
	    }

	    public void mouseReleased(MouseEvent me) {
		drawLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (startDragPosition != null) {
		    xOffset = xOffsetOriginal + (xtoSpace(startDragPosition.x) - xtoSpace(mousePosition.x));
		    yOffset = yOffsetOriginal - (xtoSpace(startDragPosition.y) - xtoSpace(mousePosition.y));
		    xOffsetOriginal = xOffset;
		    yOffsetOriginal = yOffset;
		    panLine = new Line2D.Float();
		    startDragPosition = null;
		    repaint();
		}
	    }

	    public void mouseClicked(MouseEvent me) {
		if ((me.getButton() == 2) && (me.getClickCount() == 2)) {
		    xOffsetOriginal = xOffsetPosta;
		    yOffsetOriginal = yOffsetPosta;
		    xOffset = xOffsetPosta;
		    yOffset = yOffsetPosta;
		    /*
                    drawScale = 1;
                    drawFactor = drawFactorOriginal * drawScale;
                    repaint();
                    */
		    Thread th = new Thread();
		    if (drawScale < 1) {
			th = new Thread(new Runnable() {

		       public void run() {
			   while (drawScale < 1) {
			       int sleep = 0;
			       try {
				   if ((drawScale - 1) < 0.5) {
				       drawScale = drawScale * (1.10);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 25;
				   } else if ((drawScale - 1) < 0.5) {
				       drawScale = drawScale * (1.05);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 12;
				   } else if ((drawScale - 1) < 0.5) {
				       drawScale = drawScale * (1.02);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 7;
				   } else if ((drawScale - 1) < 0.5) {
				       drawScale = drawScale * (1.01);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 2;
				   } else {
				       drawScale = drawScale * (1.005);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 0;
				   }
				   Thread.currentThread().sleep(sleep);
			       } catch (InterruptedException e) {
				   System.out.println(e);
			       }
			   }
			   drawScale = 1;
			   drawFactor = drawFactorOriginal * drawScale;
			   repaint();
		       }

		   }
			    );
		    } else {
			th = new Thread(new Runnable() {

		       public void run() {
			   while (drawScale > 1) {
			       int sleep = 0;
			       try {
				   if ((drawScale - 1) > 0.5) {
				       drawScale = drawScale / (1.10);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 25;
				   } else if ((drawScale - 1) > 0.5) {
				       drawScale = drawScale / (1.05);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 12;
				   } else if ((drawScale - 1) > 0.5) {
				       drawScale = drawScale / (1.02);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 7;
				   } else if ((drawScale - 1) > 0.5) {
				       drawScale = drawScale / (1.01);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 2;
				   } else {
				       drawScale = drawScale / (1.005);
				       drawFactor = drawFactorOriginal * drawScale;
				       repaint();
				       sleep = 0;
				   }
				   Thread.currentThread().sleep(sleep);
			       } catch (InterruptedException e) {
				   System.out.println(e);
			       }
			   }
			   drawScale = 1;
			   drawFactor = drawFactorOriginal * drawScale;
			   repaint();
		       }

		   }
			    );
		    }
		    th.start();
		}
	    }

	    public void mouseEntered(MouseEvent me) {

	    }

	    public void mouseExited(MouseEvent me) {

	    }

	}
    ;
    protected MouseMotionListener CommonMotionListener = new MouseMotionListener() {

	    public void mouseMoved(MouseEvent me) {
		mousePosition = me.getPoint();
		osnap(mousePosition);
		repaint();
	    }

	    public void mouseDragged(MouseEvent me) {
		if (startDragPosition != null) {
		    mousePosition = me.getPoint();
		    currentPosition = toSpace(mousePosition);
		    xOffset = xOffsetOriginal + (xtoSpace(startDragPosition.x) - xtoSpace(mousePosition.x));
		    yOffset = yOffsetOriginal - (xtoSpace(startDragPosition.y) - xtoSpace(mousePosition.y));
		    panLine.setLine(startDragPosition, mousePosition);
		    repaint();
		}
	    }

	}
    ;

    private void emptyData() {
	puntoOrigen = null;
	parcelasTemporales.removeAllElements();
	distancePoints.removeAllElements();
	x0Parcela = null;
	x1Parcela = null;
	repaint();
	aTrapecio = false;
	hTrapecio = false;
	a0Trapecio = null;
	a1Trapecio = null;
	h0Trapecio = null;
	h1Trapecio = null;
    }

    private String decimalFormat(double _numero, int _decimales) {
	String df = "0.";
	for (int i = 0; i < _decimales; i++) {
	    df += "0";
	}
	return (new DecimalFormat(df)).format(_numero);
    }

    private void addParcela(Polygon2D.Double _parcela) {
	parcelas.add(_parcela);
	addOsnapPoints(_parcela);
    }

    private void addParcela(Object _poly) {
	Polygon2D.Double _parcela = (Polygon2D.Double)_poly;
	parcelas.add(_parcela);
	addOsnapPoints(_parcela);
    }

    private void addOsnapPoint(Point2D.Double _point) {
	boolean found = false;
	int i = 0;
	while ((i < osnapPoints.size()) && (!found)) {
	    Point2D.Double point = (Point2D.Double)osnapPoints.elementAt(i);
	    if ((_point.getX() == point.getX()) && (_point.getY() == point.getY())) {
		found = true;
	    }
	    i++;
	}
	if (!found) {
	    osnapPoints.add(_point);
	}
    }

    private void addOsnapPoints(Polygon2D.Double _parcela) {
	for (int i = 0; i < _parcela.getVertexCount(); i++) {
	    addOsnapPoint(new Point2D.Double(_parcela.getX(i), _parcela.getY(i)));
	}
    }

    private void osnap(Point _p) {
	if (osnapActive) {
	    if (distancePoints.size() > 0) {
		distancePoints.setElementAt(toSpace(_p), distancePoints.size() - 1);
		osnapRectangle = null;
		for (int i = 0; i < osnapPoints.size(); i++) {
		    Point2D.Double xy = (Point2D.Double)osnapPoints.elementAt(i);
		    if (Math.abs(_p.getX() - xtoPixel(xy.getX())) < 5 && Math.abs(_p.getY() - ytoPixel(xy.getY())) < 5) {
			distancePoints.setElementAt(new Point2D.Double(xy.getX(), xy.getY()), distancePoints.size() - 1);
			osnapRectangle = new Rectangle2D.Double(xtoPixel(xy.getX()) - osnapRectSize / 2, ytoPixel(xy.getY()) - osnapRectSize / 2, osnapRectSize, osnapRectSize);
		    } else {

		    }
		}
		/*                if (me.getModifiers() == 1)
                {
                  Point2D xy = dameInterseccion(pp0, pp1, polypoints[0], polypoints[cantdistpoints]);
                  polypoints[cantdistpoints] = new Point2D.Double(xy.getX(), xy.getY());
                  distance(polypoints[cantdistpoints].getX(), polypoints[cantdistpoints].getY(), false);
                  osnapRectangle = new Rectangle2D.Double(xtoPixel(xy.getX()) - size / 2, ytoPixel(xy.getY()) - size / 2, size, size);
                } */
		currentPosition = (Point2D.Double)distancePoints.elementAt(distancePoints.size() - 1);
	    }
	}
    }

    public boolean hasPolygons() {
	if (entities.size() == 0) {
	    return false;
	} else {
	    return true;
	}
    }
    protected MouseListener InfoListener = new MouseAdapter() {

	    //REVISAR
	    ExtendedInternalFrame container;

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON1 && me.getClickCount() == 1) {
		    if (containedEntity != -1) {
			switch (entities.elementAt(containedEntity).getGid()) {
			    case 1 :
				container = new ExtendedInternalFrame("Ingresos");
				container.setCentralPanel(new CashMovementsList(1));
				container.show();
				break;
			    case 2 :
				container = new ExtendedInternalFrame("Egresos");
				container.setCentralPanel(new CashMovementsList(2));
				container.show();
				break;
			    case 3 :
				container = new ExtendedInternalFrame("Movimientos de Fondos");
				container.setCentralPanel(new CashMovementsList());
				container.show();
				break;
			    case 4 :
				/*container = new ExtendedInternalFrame("Pedido de Presupuestos");
			        container.setCentralPanel(new CCList());
			        container.show();*/
				Advisor.messageBox("Módulo en Construcción", "Módulo en Construcción");
				break;
			    case 5 :
				container = new ExtendedInternalFrame("Tipos de Movimientos de Fondos");
				container.setCentralPanel(new CashMovementTypesTree());
				container.show();
				break;
			    case 6 :
				container = new ExtendedInternalFrame("Cuentas Bancarias");
				container.setCentralPanel(new BankAccountsList());
				container.show();
				break;
			    case 7 :
				container = new ExtendedInternalFrame("Plan de Cuentas");
				container.setCentralPanel(new AccountsMain());
				container.show();
				break;
			    case 8 :
				container = new ExtendedInternalFrame("Autorización de Pedidos de Recursos");
				container.setCentralPanel(new ResourcesRequestsAuthMain());
				container.show();
				break;
			    case 9 :
				container = new ExtendedInternalFrame("Pedidos de Recursos");
				container.setCentralPanel(new ResourcesRequestsMain());
				container.show();
				break;
			    case 10 :
				container = new ExtendedInternalFrame("Centros de Costos");
				container.setCentralPanel(new CCList());
				container.show();
				break;
			    case 11 :
				container = new ExtendedInternalFrame("Generar Orden de Compra");
				container.setCentralPanel(new PurchaseOrderGenerateList());
				container.show();
				break;
			    case 12 :
				container = new ExtendedInternalFrame("Órdenes de Pago");
				container.setCentralPanel(new PaymentsOrdersMain());
				container.show();
				break;
			    case 13 :
				container = new ExtendedInternalFrame("Administración de Recursos");
				container.setCentralPanel(new ResourcesAdminMain());
				container.show();
				break;
			    case 14 :
				container = new ExtendedInternalFrame("Entrega de Recursos (Interno)");
				container.setCentralPanel(new ResourcesDeliverList());
				container.show();
				break;
			    case 15 :
				container = new ExtendedInternalFrame("Ingresos de Recursos (Externo)");
				container.setCentralPanel(new ResourcesReceiveMain());
				container.show();
				break;
			    case 16 :
				container = new ExtendedInternalFrame("Calificación de Habilidades");
				container.setCentralPanel(new SkillList());
				container.show();
				break;
			    case 17 :
				container = new ExtendedInternalFrame("Recursos Materiales");
				container.setCentralPanel(new ResourcesList());
				container.show();
				break;
			    case 18 :
				container = new ExtendedInternalFrame("Recursos Humanos");
				container.setCentralPanel(new PersonsList());
				container.show();
				break;
			    case 19 :
				container = new ExtendedInternalFrame("Listado de Proveedores");
				container.setCentralPanel(new CompanyTreePanel());
				container.show();
				break;
			    case 20 :
				container = new ExtendedInternalFrame("Administración de Partidas Presupuestarias");
				container.setCentralPanel(new BudgetList());
				container.show();
				break;
			    default :
				System.out.println(entities.elementAt(containedEntity).getGid());
				break;
			}
			containedEntity = -1;
			repaint();
		    }
		}
	    }
	    //REVISAR

	}
    ;
    protected MouseMotionListener InfoMotionListener = new MouseMotionListener() {

	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		labelxy.setText("X: " + me.getX() + ", Y: " + me.getY());
		labelinfo.setText(String.valueOf(drawScale));
		mousePosition = me.getPoint();
		containedEntity = -1;
		containedPoint = -1;
		for (int i = 0; i < entities.size(); i++) {
		    Polygon2D _entity = entities.elementAt(i).getPolygon();
		    if (getFakePolyFromPoly(_entity).contains(me.getPoint())) {
			containedEntity = i;
		    }
		}
		repaint();
	    }

	    public void mouseDragged(MouseEvent e) {

	    }

	}
    ;

    private void calculaEntorno() {
	if ((fWidth != getBounds().getMaxX()) || (fHeight != getBounds().getMaxY())) {
	    fWidth = getBounds().getMaxX();
	    fHeight = getBounds().getMaxY();
	    drawLabel.setBounds(0, 0, (int)fWidth, (int)fHeight);
	    System.out.println(drawLabel.getBounds());
	    if (xExtents > yExtents) {
		drawFactor = (fWidth - 20) / xExtents;
	    } else {
		drawFactor = (fHeight - 20) / yExtents;
	    }
	    drawFactorOriginal = drawFactor;
	    drawFactor = drawFactorOriginal * drawScale;
	    xOffset = xMin;
	    // - 1000;
	    xOffsetOriginal = xOffset;
	    xOffsetPosta = xOffsetOriginal;
	    //Por quÃ©?
	    yOffset = yMin;
	    // + 1000;
	    yOffsetOriginal = yOffset;
	    yOffsetPosta = yOffsetOriginal;
	    //Por quÃ©?
	    System.out.println("ExtX: " + xExtents + " ExtY: " + yExtents);
	    System.out.println("drawFactor: " + drawFactor);
	    System.out.println("xOffset: " + xOffset + " yOffset: " + yOffset);
	}
    }

    private Polygon2D pointToRect(Point2D punto) {
	double[] x = new double[4];
	double[] y = new double[4];
	x[0] = xtoPixel(punto.getX()) - osnapRectSize;
	y[0] = ytoPixel(punto.getY()) - osnapRectSize;
	x[1] = xtoPixel(punto.getX()) + osnapRectSize;
	y[1] = ytoPixel(punto.getY()) - osnapRectSize;
	x[2] = xtoPixel(punto.getX()) + osnapRectSize;
	y[2] = ytoPixel(punto.getY()) + osnapRectSize;
	x[3] = xtoPixel(punto.getX()) - osnapRectSize;
	y[3] = ytoPixel(punto.getY()) + osnapRectSize;
	return getTruePolyFromPoints(x, y);
    }

    private void btnRefresh_actionPerformed(ActionEvent e) {
	entities.removeAllElements();
	connectors.removeAllElements();
	annotations.removeAllElements();
	doEntities();
	doLines();
	doAnnotations();
	repaint();
    }

    private void addMapButton(int _gid, Point2D.Double _centroid, Map<String, String> _extraFunctions) {
	Component _panel = null;
	ImageIcon _icon = null;
	MapButton _button = null;
	String _title = "";
	switch (_gid) {
	    case 1 :
		_title = "Ingresos";
		_icon = IconTypes.centros_de_costos;
		_panel = new CashMovementsList(1);
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 2 :
		_title = "Egresos";
		_icon = IconTypes.centros_de_costos;
		_panel = new CashMovementsList(2);
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 3 :
		_title = "Movimientos\nde Fondos";
		_icon = IconTypes.centros_de_costos;
		_panel = new CashMovementsList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 4 :
		_title = "Pedido de\nPresupuestos\n(Módulo en\nConstrucción)";
		//Advisor.messageBox("Módulo en Construcción", "Módulo en Construcción");
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 5 :
		_title = "Tipos de\nMovimientos\nde Fondos";
		_icon = IconTypes.centros_de_costos;
		_panel = new CashMovementTypesTree();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 6 :
		_title = "Cuentas\nBancarias";
		_icon = IconTypes.centros_de_costos;
		_panel = new BankAccountsList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 7 :
		_title = "Plan de Cuentas";
		_icon = IconTypes.centros_de_costos;
		_panel = new AccountsMain();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 8 :
		_title = "Autorización\nde Pedidos\nde Recursos";
		_icon = IconTypes.centros_de_costos;
		_panel = new ResourcesRequestsAuthMain();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			       ExtendedInternalFrame _rListIframe = new ExtendedInternalFrame("Listado de recursos");
			       _rListIframe.setCentralPanel(new ResourcesList());
			       _rListIframe.show();
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			       new ResourcesRequestForAuthListReport("-1,'','','',2,-1,-1", -1, "Todos", -1, "Todos", -1, "Todas");
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 9 :
		_title = "Pedidos\nde Recursos";
		_icon = IconTypes.centros_de_costos;
		_panel = new ResourcesRequestsMain();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 10 :
		_title = "Centros\nde Costos";
		_icon = IconTypes.centros_de_costos;
		_panel = new CCList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 11 :
		_title = "Generar Orden\nde Compra";
		_icon = IconTypes.centros_de_costos;
		_panel = new PurchaseOrderGenerateList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 12 :
		_title = "Órdenes\nde Pago";
		_icon = IconTypes.centros_de_costos;
		_panel = new PaymentsOrdersMain();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 13 :
		_title = "Administración\nde Recursos";
		_icon = IconTypes.centros_de_costos;
		_panel = new ResourcesAdminMain();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 14 :
		_title = "Entrega\nde Recursos\n(Interno)";
		_icon = IconTypes.centros_de_costos;
		_panel = new ResourcesDeliverList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 15 :
		_title = "Ingreso\nde Recursos\n(Externo)";
		_icon = IconTypes.centros_de_costos;
		_panel = new ResourcesReceiveMain();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 16 :
		_title = "Calificación\nde Habilidades";
		_icon = IconTypes.centros_de_costos;
		_panel = new SkillList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 17 :
		_title = "Recursos\nMateriales";
		_icon = IconTypes.centros_de_costos;
		_panel = new ResourcesList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 18 :
		_title = "Recursos\nHumanos";
		_icon = IconTypes.centros_de_costos;
		_panel = new PersonsList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 19 :
		_title = "Listado de\nProveedores";
		_icon = IconTypes.centros_de_costos;
		_panel = new CompanyTreePanel();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    case 20 :
		_title = "Administración\nde Partidas\nPresupuestarias";
		_icon = IconTypes.centros_de_costos;
		_panel = new BudgetList();
		_button = new MapButton(_title, _icon);
		if (_extraFunctions.containsKey("complete")) {
		    _button.setExtra(MapButton.WARNING, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("complete"));
		}
		if (_extraFunctions.containsKey("extra1")) {
		    _button.setExtra(MapButton.EXTRA1, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra1"));
		}
		if (_extraFunctions.containsKey("extra2")) {
		    _button.setExtra(MapButton.EXTRA2, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra2"));
		}
		if (_extraFunctions.containsKey("extra3")) {
		    _button.setExtra(MapButton.EXTRA3, new ActionListener() {

			   public void actionPerformed(ActionEvent e) {
			   }

		       }
		       , _extraFunctions.get("extra3"));
		}
		break;
	    default :
		System.out.println(entities.elementAt(containedEntity).getGid());
		break;
	}
	if (_panel != null) {
	    ExtendedInternalFrame _iFrame = (_icon != null) ? new ExtendedInternalFrame(_title, _icon) : new ExtendedInternalFrame(_title);
	    _iFrame.setCentralPanel(_panel);
	    _iFrame.setClosable(false);
	    _iFrame.setDesktop(this);
	    if (_button == null) {
		_button = _iFrame.getMapButton();
	    } else {
		_iFrame.setMapButton(_button);
	    }
	    _button.setID(_gid);
	    _button.setCentroid(_centroid);
	    _iFrame.setMapped(true);
	    _iFrame.setIcon(true);
	    mapButtons.add(_button);
	}
    }

}

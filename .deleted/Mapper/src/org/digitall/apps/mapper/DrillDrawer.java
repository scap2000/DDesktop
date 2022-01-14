package org.digitall.apps.mapper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
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

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.geo.coordinatesystems.CoordinateSystems;
import org.digitall.lib.geo.coordinatesystems.LatLongCoord;
import org.digitall.lib.geo.coordinatesystems.UTMCoord;
import org.digitall.lib.geom.Polygon2D;
import org.digitall.lib.sql.LibSQL;

public class DrillDrawer extends BasicContainerPanel {

    private JToolBar tbStatus = new JToolBar();
    private BorderLayout borderLayout1 = new BorderLayout();

    public DrillDrawer(BasicLabel _statusBar, int _idProject) {
	try {
	    statusBar = _statusBar;
	    idProject = _idProject;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    private BasicLabel statusBar;
    private String operacionStatus;
    private BasicLabel labelxy = new BasicLabel();
    private BasicLabel labelinfo = new BasicLabel();
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
    //Variables de Posicion
    private Point mousePosition = new Point();
    private Point2D.Double currentPosition = new Point2D.Double();
    private Point2D.Double puntoOrigen;
    private Point startDragPosition = null;
    private String testString = "";
    //Variables de Dibujo
    private Vector Poligonos = new Vector();
    private Vector parcelas = new Vector();
    private Vector parcelasTemporales = new Vector();
    private Vector distancePoints = new Vector();
    private Vector osnapPoints = new Vector();
    private Vector osnapPointsTemporales = new Vector();
    //Servira de algo?
    private RectangularShape osnapRectangle;
    private int containedParcela = -1;
    private int containedPolygon = -1;
    private Vector badLines = new Vector();
    protected Line2D panLine = new Line2D.Float();
    private int idParcela = -1;
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
    private int fWidth = 800;
    private int fHeight = 500;
    private double drawScale = 1;
    private boolean UTM = false;
    //Variables del momento
    private double base = 1000.0;
    //1000 metros
    private double altura = 1000.0;
    //1000 metros
    private double area = base * altura;
    //100 has ??? :=0 !!!
    private double areaTotal = 0;
    private double distanciaTotal = 0;
    private double areaParcial = area;
    private double areaRestante = area - -areaParcial;
    //Variables de trabajo
    private MouseListener eraseListener;
    private MouseMotionListener eraseMotionListener;
    private MouseWheelListener eraseWheelListener;
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
    private int idProject;

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	/**mPoints = new Point2D[2];*/
	//        mousePosition.setLocation(0, 0);
	//        currentPosition.setLocation(0, 0);
	setLayout(borderLayout1);
	this.setSize(new Dimension(800, 600));
	fWidth = this.getWidth();
	fHeight = this.getHeight();
	labelxy.setText("labelxy");
	labelinfo.setText("labelinfo");
	tbStatus.add(labelxy, null);
	tbStatus.add(labelinfo, null);
	this.add(tbStatus, BorderLayout.NORTH);
	addMouseWheelListener(CommonWheelListener);
	addMouseListener(CommonMouseListener);
	addMouseMotionListener(CommonMotionListener);
	doPolys();
	//    setPuntos();
    }

    public void paint(Graphics g) {
	super.paint(g);
	Graphics2D g2 = (Graphics2D)g;
	calculaEntorno();
	//Recorro el vector Poligonos para dibujar
	//TODOS los poligonos del SQL que carga doPolys();
	for (int minas = 0; minas < Poligonos.size(); minas++) {
	    Polygon2D mina = getFakePolyFromPoly((Polygon2D)(Poligonos.elementAt(minas)));
	    if (containedPolygon != -1) {
		g2.setColor(Color.magenta);
		if (minas == containedPolygon) {
		    g2.fill(mina);
		}
	    }
	    g2.setColor(Color.black);
	    g2.draw(mina);
	}
	setBadLines(Poligonos);
	g2.setColor(Color.red);
	for (int lines = 0; lines < badLines.size(); lines++) {
	    Line2D.Double _line = (Line2D.Double)badLines.elementAt(lines);
	    int x0 = xtoPixel(_line.getX1());
	    int x1 = xtoPixel(_line.getX2());
	    int y0 = ytoPixel(_line.getY1());
	    int y1 = ytoPixel(_line.getY2());
	    g2.drawLine(x0, y0, x1, y1);
	}
	g2.setColor(Color.yellow);
	g2.draw(panLine);
	//Dibujo el texto del puntero del mouse
	int faja = 0;
	if (currentPosition != null) {
	    LatLongCoord latlon = CoordinateSystems.gk2geo(currentPosition.getX(), currentPosition.getY(), faja);
	    String latlong = CoordinateSystems.dec2gms(latlon.getLatitude(), 4) + " lat " + CoordinateSystems.dec2gms(latlon.getLongitude(), 4) + " long";
	    if (UTM) {
		UTMCoord pos = CoordinateSystems.gk2utm(currentPosition.getX(), currentPosition.getY(), faja);
		g2.setColor(Color.black);
		g2.drawString("(" + decimalFormat(pos.getEasting(), 4) + " " + decimalFormat(pos.getNorthing(), 4) + " Zone: " + pos.getZone() + ((pos.getBand() < 0) ? "S" : "N") + ")::(" + latlong + ")", (int)mousePosition.getX(), (int)mousePosition.getY());
	    } else {
		//GK
		//GKCoord pos =
		g2.setColor(Color.black);
		g2.drawString("(" + decimalFormat(currentPosition.getX(), 4) + " " + decimalFormat(currentPosition.getY(), 4) + ")::(" + latlong + ")", (int)mousePosition.getX(), (int)mousePosition.getY());
	    }
	}
	//Dibujo el texto del puntero del mouse
	//if (startDragPosition != null)
	{
	    g2.setColor(Color.blue);
	    g2.drawString(testString, 200, 200);
	}
	//Dibujo el RectÃ¡ngulo del osnap
	if (osnapRectangle != null) {
	    g2.setColor(Color.cyan);
	    g2.fill(osnapRectangle);
	}
	/* for (int i=0; i<cantparc; i++)
    {
      g2.setColor(Color.magenta);
      int cont = 0;
      double[] x = new double[5];
      double[] y = new double[5];
      for (int j = 0; j<4; j++)
      {
        x[j] = parcelas[j][i].getX();
        y[j] = parcelas[j][i].getY();
        if (mina.contains(xtoPixel(x[j]), ytoPixel(y[j])) || mina.intersects(xtoPixel(x[j]), ytoPixel(y[j]),0,0))
        {
          cont++;
        }
      }
      if (i == contained) g2.fill(getFakePolyFromPoints(x, y, 4));
      if (cont>=2) g2.draw(getFakePolyFromPoints(x, y, 4));
    }
    */
	g2.setColor(Color.magenta);
	for (int i = 0; i < parcelas.size(); i++) {
	    Polygon2D _parcela = (Polygon2D)parcelas.elementAt(i);
	    if (i == containedParcela) {
		g2.fill(getFakePolyFromPoly(_parcela));
		if (_parcela.getId() != -1) {
		    //Dibujo el ID de la mina
		    System.out.println("ID MINA: " + _parcela.getId());
		    g2.setColor(Color.black);
		    g2.drawString(String.valueOf(_parcela.getId()), (int)mousePosition.getX(), (int)mousePosition.getY());
		    g2.setColor(Color.magenta);
		}
	    }
	    g2.draw(getFakePolyFromPoly(_parcela));
	}
	for (int i = 0; i < parcelasTemporales.size(); i++) {
	    g2.setColor(Color.yellow);
	    g2.draw(getFakePolyFromPoly((Polygon2D)parcelasTemporales.elementAt(i)));
	}
	/**g2.draw(getFakePolyFromPoints(xp, yp));*/
	/*if (out)
    {
      for (int j = 0; j<xp.length-1; j++)
      {
        for (int i = 0; i<mpuntos.size()-1; i++)
        {
          Point2D p0 = (Point2D.Double)mpuntos.elementAt(i);
          Point2D p1 = (Point2D.Double)mpuntos.elementAt(i+1);
          Point2D xy = dameInterseccion(p0, p1, new Point2D.Double(xp[j], yp[j]), new Point2D.Double(xp[j+1], yp[j+1]));
          g2.drawString("X", xtoPixel(xy.getX()), ytoPixel(xy.getY()));
        }
        Point2D p0 = (Point2D.Double)mpuntos.elementAt(mpuntos.size()-1);
        Point2D p1 = (Point2D.Double)mpuntos.elementAt(0);
        Point2D xy = dameInterseccion(p0, p1, new Point2D.Double(xp[j], yp[j]), new Point2D.Double(xp[j+1], yp[j+1]));
        g2.drawString("X", xtoPixel(xy.getX()), ytoPixel(xy.getY()));
      }
      Point2D p0 = (Point2D.Double)mpuntos.elementAt(mpuntos.size()-1);
      Point2D p1 = (Point2D.Double)mpuntos.elementAt(0);
      Point2D xy = dameInterseccion(p0, p1, new Point2D.Double(xp[xp.length-1], yp[yp.length-1]), new Point2D.Double(xp[0], yp[0]));
      g2.drawString("X", xtoPixel(xy.getX()), ytoPixel(xy.getY()));
    }*/
	g2.setColor(Color.yellow);
	if (distancePoints.size() > 0) {
	    try {
		for (int i = 0; i < distancePoints.size() - 1; i++) //Fijarse si es size()-1
		{
		    Point2D x0y0 = (Point2D.Double)distancePoints.elementAt(i);
		    Point2D x1y1 = (Point2D.Double)distancePoints.elementAt(i + 1);
		    g2.drawLine(xtoPixel(x0y0.getX()), ytoPixel(x0y0.getY()), xtoPixel(x1y1.getX()), ytoPixel(x1y1.getY()));
		}
		Point2D x0y0 = (Point2D.Double)distancePoints.elementAt(distancePoints.size() - 1);
		Point2D x1y1 = (Point2D.Double)distancePoints.elementAt(0);
		g2.drawLine(xtoPixel(x0y0.getX()), ytoPixel(x0y0.getY()), xtoPixel(x1y1.getX()), ytoPixel(x1y1.getY()));
	    } catch (NullPointerException x) {
		x.printStackTrace();
	    }
	} else {
	    distancePoints.add(new Point2D.Double());
	}
    }

    public void doPolys() {
	try {
	    String Data = "the_geom from (select the_geom from public.projects_polygons " + " where idproject = " + idProject + ") as foo";
	    Data = Data.trim();
	    String geometry = Data.substring(0, Data.indexOf(" ")).trim();
	    Data = Data.substring(Data.indexOf(" ")).trim();
	    String LabelItem = "";
	    String Qwhere = "";
	    String Query = "select " + LabelItem + " AsText(centroid(" + geometry + ")) as centroid, AsText(" + geometry + ") as the_geom,npoints(" + geometry + "), area(" + geometry + ") as area " + Data + Qwhere;
	    ResultSet count = org.digitall.lib.sql.LibSQL.exQuery("select count(*) from (" + Query + ") as foo");
	    count.next();
	    //polys = new Polygon2D[count.getInt(1)];
	    //      selectedPath = new int[count.getInt(1)];
	    ResultSet Polygons = org.digitall.lib.sql.LibSQL.exQuery(Query);
	    if (Qwhere == Qwhere) {
		ResultSet Extent = org.digitall.lib.sql.LibSQL.exQuery("select " + "xmin(extent(" + geometry + ")), " + "xmax(extent(" + geometry + ")), " + "ymin(extent(" + geometry + ")), " + "ymax(extent(" + geometry + ")) " + Data);
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
		areaTotal += Polygons.getDouble("area");
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
		Poligonos.addElement(getTruePolyFromPoints(xd, yd));
	    }
	    if (Poligonos.size() == 0) {
		org.digitall.lib.components.Advisor.messageBox("No hay polÃ­gonos para ver", "Tabla vacÃ­a");
	    }
	} catch (SQLException x) {
	    org.digitall.lib.components.Advisor.messageBox(x.getErrorCode() + ": " + x.getMessage(), "Error en la consulta SQL");
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

    private void Boton1_actionPerformed(ActionEvent e) {
	UTM = !UTM;
	repaint();
    }

    private void setPuntos() {
	//Selecciona 2 puntos consecutivos para trazar una recta paralela a ellos
	/**    for (int i = 0; i<numPolys; i++)  //todavia no es necesario pues tengo un solo poligono
    {
      int cantidad = polys[i].getVertexCount()-1;

      if ((ppunto == -1) || (ppunto == cantidad-1))
      {
        ppunto = 0;
      } else
      {
        ppunto++;
      }

      if (spunto <= cantidad-2)
      {
        spunto = ppunto+1;
      } else
      {
        spunto = 0;
      }
    }
    mPoints[0] = new Point2D.Double(xtoPixel(xd[ppunto]), ytoPixel(yd[ppunto]));
    mPoints[1] = new Point2D.Double(xtoPixel(xd[spunto]), ytoPixel(yd[spunto]));
    l.setLine(mPoints[0], mPoints[1]);
    triangularPuntos();
    repaint();*/
    }
    //REVISAR

    public void distance(boolean nuevo) {
	double distancia = 0;
	double area = 0;
	double angulo = 0;
	if (distancePoints.size() < 99) //Fijarse si hay que mantener la restricción o no
	{
	    if (nuevo) {
		//distancePoints.removeAllElements();
		distancePoints.add(new Point2D.Double(currentPosition.getX(), currentPosition.getY()));
	    }
	    if (distancePoints.size() == 2) {
		if (!nuevo) {
		    distancia = distance((Point2D.Double)distancePoints.elementAt(distancePoints.size() - 2), (Point2D.Double)distancePoints.elementAt(distancePoints.size() - 1));
		}
	    } else if (distancePoints.size() > 2) {
		if (nuevo) {
		    distancia = distance((Point2D.Double)distancePoints.elementAt(distancePoints.size() - 3), (Point2D.Double)distancePoints.elementAt(distancePoints.size() - 2));
		    distanciaTotal += distancia;
		} else {
		    distancia = distance((Point2D.Double)distancePoints.elementAt(distancePoints.size() - 2), (Point2D.Double)distancePoints.elementAt(distancePoints.size() - 1));
		}
		area = calcArea(distancePoints, nuevo);
		/**angulo = calcAngulo(polypoints);*/
	    }
	    labelinfo.setText("Distancia parcial: " + decimalFormat(distancia, 4) + "m - Distancia total: " + decimalFormat(distanciaTotal, 4) + "m - Area: " + decimalFormat(area, 4) + "m2 - Angulo: " + decimalFormat(angulo, 4));
	} else {
	    org.digitall.lib.components.Advisor.messageBox("Maxima cantidad de puntos alcanzada, \nno debe superar los 100 puntos", "Error");
	}
    }

    public double distance(Point2D.Double x0, Point2D.Double x1) {
	double catetox = x1.getX() - x0.getX();
	double catetoy = x1.getY() - x0.getY();
	double cats = catetox * catetox + catetoy * catetoy;
	double dist = Math.sqrt(cats);
	return dist;
    }

    public double distance(Point2D x0, Point2D x1) {
	double catetox = x1.getX() - x0.getX();
	double catetoy = x1.getY() - x0.getY();
	double cats = catetox * catetox + catetoy * catetoy;
	double dist = Math.sqrt(cats);
	return dist;
    }

    public void setBadLines(Vector _polys) {
	badLines.removeAllElements();
	for (int j = 0; j < _polys.size(); j++) {
	    Polygon2D.Double _poly = (Polygon2D.Double)(Poligonos.elementAt(j));
	    for (int i = 0; i < _poly.getVertexCount() - 1; i++) {
		//La primer pendiente es entre el primer punto con el segundo
		//La segunda pendiente es entre el segundo punto con el tercero
		//Hago mod para ejecutar un ciclo en el poligono y
		//evitar un ArrayIndexOutOfBoundsException
		double x0 = _poly.getX((i) % _poly.getVertexCount());
		double x1 = _poly.getX((i + 1) % _poly.getVertexCount());
		double y0 = _poly.getY((i) % _poly.getVertexCount());
		double y1 = _poly.getY((i + 1) % _poly.getVertexCount());
		double rx = x1 - x0;
		double ry = y1 - y0;
		if ((rx != 0) && (ry != 0)) {
		    badLines.add(new Line2D.Double(x0, y0, x1, y1));
		}
	    }
	}
    }

    /**public double calcArea(Point2D.Double[] poly, boolean nuevo)
     * {
     * double area = 0;
     * int dif = 0;
     * if (nuevo) dif = 1;
     * for (int i = 0; i<cantdistpoints-dif; i++)
     * {
     * area += (poly[i+1].minX * poly[i].minY) - (poly[i].minX * poly[i+1].minY);
     * }
     * area += (poly[0].minX * poly[cantdistpoints-dif].minY) - (poly[cantdistpoints-dif].minX * poly[0].minY);
     * return Math.abs(area/2);
     * }
     */
    public double calcArea(Vector _poly, boolean nuevo) {
	double area = 0;
	int dif = 1;
	if (nuevo)
	    dif = 2;
	for (int i = 0; i < _poly.size() - dif; i++) {
	    area += ((((Point2D.Double)_poly.elementAt(i + 1)).getX() * ((Point2D.Double)_poly.elementAt(i)).getY()) - (((Point2D.Double)_poly.elementAt(i)).getX() * ((Point2D.Double)_poly.elementAt(i + 1)).getY()));
	}
	area += ((((Point2D.Double)_poly.elementAt(0)).getX() * ((Point2D.Double)_poly.elementAt(_poly.size() - dif)).getY()) - (((Point2D.Double)_poly.elementAt(_poly.size() - dif)).getX() * ((Point2D.Double)_poly.elementAt(0)).getY()));
	return Math.abs(area / 2);
    }

    public double calcArea(Vector _puntos) {
	double area = 0;
	if (_puntos.size() > 1) {
	    for (int i = 0; i < _puntos.size() - 1; i++) {
		Point2D p0 = (Point2D.Double)_puntos.elementAt(i);
		Point2D p1 = (Point2D.Double)_puntos.elementAt(i + 1);
		area += (p1.getX() * p0.getY()) - (p0.getX() * p1.getY());
	    }
	    Point2D pn = (Point2D.Double)_puntos.elementAt(_puntos.size() - 1);
	    Point2D p0 = (Point2D.Double)_puntos.elementAt(0);
	    area += (p0.getX() * pn.getY()) - (pn.getX() * p0.getY());
	    return Math.abs(area / 2);
	} else {
	    return area;
	}
    }

    public void triangularPuntos() {
	/**  double tx[] = new double[4];
    double ty[] = new double[4];

    tx[0] = xd[ppunto];
    ty[0] = yd[ppunto];

    if (polys[0].contains(xd[ppunto], yd[spunto]))
    {
      tx[1] = xd[ppunto];
      ty[1] = yd[spunto];
    } else
    {
      tx[1] = xd[spunto];
      ty[1] = yd[ppunto];
    }

    tx[2] = xd[spunto];
    ty[2] = yd[spunto];

    tx[3] = xd[ppunto];
    ty[3] = yd[ppunto];

    triangle = getFakePolyFromPoints(tx, ty);*/
    }

    /**private Shape createShape()
     * {
     * //chequear los puntos internos, primero lo primero
     * l.setLine(0,0,0,0);
     * //Rectangle2D nuevo = new Rectangle2D.Double(curpos.getX(), curpos.getY(), base, altura);
     * Rectangle2D nuevo = new Rectangle2D.Double(mousePosition.minX, mousePosition.minY, base*Factor, altura*Factor);
     * for (int i=0; i<fNumPoints-1; i++)
     * {
     * //Line2D linea = new Line2D.Double(xd[i],yd[i], xd[i+1], yd[i+1]);
     * Line2D linea = new Line2D.Double(toPixel(xd[i],yd[i]), toPixel(xd[i+1], yd[i+1]));
     * if (linea.intersects(nuevo))
     * {
     * l.setLine(toPixel(xd[i],yd[i]), toPixel(xd[i+1], yd[i+1]));
     * //Recalcular!!!
     * //return new Rectangle2D.Double(xtoPixel(curpos.getX()), ytoPixel(curpos.getY()), (int)(base*Factor)-50, (int)(altura*Factor)-50);
     * } else
     * {
     * //return new Rectangle2D.Double(xtoPixel(curpos.getX()), ytoPixel(curpos.getY()), (int)(base*Factor), (int)(altura*Factor));
     * }
     * }
     * return new Rectangle2D.Double(xtoPixel(currentPosition.getX()), ytoPixel(currentPosition.getY()), base*drawFactor, altura*drawFactor);
     * //return new Rectangle2D.Double(xtoPixel(curpos.getX()), ytoPixel(curpos.getY()), base*Factor, altura*Factor);
     * //if (
     * //xtoPixel(curpos.getX()), ytoPixel(curpos.getY()), (int)(base*Factor), (int)(altura*Factor));
     * // return null;
     * }
     */
    private void createGrid() {
	/**    double[] pends = new double[fNumPoints];
    for (int i=0; i<fNumPoints-1; i++)
    {
      pends[i] = (yd[i+1] - yd[i])/(xd[i+1] - xd[i]);
//      angulototal += angle(xd[i], yd[i], xd[i+1], yd[i+1]);
    }
    for (int i=0; i<fNumPoints-1; i++)
    {
      double angle = Math.atan(Math.abs((pends[i+1]-pends[i])/(1+(pends[i]*pends[i+1]))));
      angle = (angle * 180)/Math.PI;
      System.out.println("Angulo: " + angle);
      angulototal += angle;
    }
    System.out.println("Angulototal: " + angulototal);*/
    }

    private double angle(double x0, double y0, double x1, double y1) {
	double angle = Math.abs((Math.atan((y1 - y0) / (x1 - x0)) * 180) / Math.PI);
	return angle;
    }

    /**  private Point2D[] calcgrid()
  {
    Point2D minimos = new Point2D.Double(xd[0], yd[0]);
    Point2D maximos = new Point2D.Double(xd[0], yd[0]);
    for (int i=1; i<fNumPoints; i++)
    {
      if (xd[i]<minimos.getX())
      {
        minimos.setLocation(xd[i],minimos.getY());
      } else if (xd[i]>maximos.getX())
      {
        maximos.setLocation(xd[i], maximos.getY());
      }
      if (yd[i]<minimos.getY())
      {
        minimos.setLocation(minimos.getX(), yd[i]);
      } else if (yd[i]>maximos.getY())
      {
        maximos.setLocation(maximos.getX(), yd[i]);
      }
    }
    Point2D[] r = new Point2D[2];
    r[0] = minimos;
    r[1] = maximos;
    return r;
  }*/
    private void makeShapes(Point2D origen, Point2D destino) {
	int dirx = 1, diry = 1;
	double ho = destino.getX() - origen.getX();
	double ve = destino.getY() - origen.getY();
	System.out.println("origen: " + origen.getX() + "," + origen.getY());
	System.out.println("destino: " + destino.getX() + "," + destino.getY());
	int horiz = Math.abs((int)(ho / base)) + 1;
	int vert = Math.abs((int)(ve / altura)) + 1;
	if (ho < 0) {
	    dirx = -1;
	}
	if (ve < 0) {
	    diry = -1;
	}
	System.out.println(parcelasTemporales.size() + " parcelas Temporales");
	System.out.println("Vert: " + vert);
	System.out.println("Horiz: " + horiz);
	parcelasTemporales.removeAllElements();
	for (int v = 0; v < vert; v++) {
	    for (int h = 0; h < horiz; h++) {
		Point2D.Double xy[] = new Point2D.Double[5];
		xy[0] = new Point2D.Double(origen.getX() + dirx * (h * base), origen.getY() + diry * (v * altura));
		xy[1] = new Point2D.Double(origen.getX() + dirx * ((h * base) + base), origen.getY() + diry * (v * altura));
		xy[2] = new Point2D.Double(origen.getX() + dirx * ((h * base) + base), origen.getY() + diry * ((v * altura) + altura));
		xy[3] = new Point2D.Double(origen.getX() + dirx * (h * base), origen.getY() + diry * ((v * altura) + altura));
		xy[4] = new Point2D.Double(origen.getX() + dirx * (h * base), origen.getY() + diry * (v * altura));
		parcelasTemporales.add(getTruePolyFromPoints(xy));
	    }
	}
	//HAY QUE PODER AGREGAR osnapPoints desde un Poligono =)
    }

    public void setOperation(int _operacion) {
	emptyData();
	removeMouseListener(eraseListener);
	removeMouseMotionListener(eraseMotionListener);
	switch (_operacion) {
	    case MALLA:
		eraseListener = MallaListener;
		eraseMotionListener = MallaMotionListener;
		operacionStatus = "Malla: ";
		break;
	    case POLIGONO:
		eraseListener = PoligonoListener;
		eraseMotionListener = PoligonoMotionListener;
		operacionStatus = "Poligono: ";
		break;
	    case ELIMINAR:
		eraseListener = EliminaListener;
		eraseMotionListener = EliminaMotionListener;
		operacionStatus = "Eliminar: ";
		break;
	    case PARCELA:
		eraseListener = ParcelaListener;
		eraseMotionListener = ParcelaMotionListener;
		operacionStatus = "Parcela: ";
		break;
	    case NUEVO:
		eraseListener = NuevoListener;
		eraseMotionListener = NuevoMotionListener;
		operacionStatus = "DataBase: ";
		break;
	    case PARALELA:
		eraseListener = ParalelaListener;
		eraseMotionListener = ParalelaMotionListener;
		operacionStatus = "Paralela: ";
		break;
	    case TRAPECIO:
		eraseListener = TrapecioRectListener;
		eraseMotionListener = TrapecioRectMotionListener;
		operacionStatus = "Trapecio: ";
		break;
	    case INFO:
		eraseListener = InfoListener;
		eraseMotionListener = InfoMotionListener;
		operacionStatus = "Show Info: ";
		break;
	    case NUMERAR:
		eraseListener = NumeraListener;
		eraseMotionListener = NumeraMotionListener;
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

	};
    protected MouseListener CommonMouseListener = new MouseListener() {

	    public void mousePressed(MouseEvent me) {
		if (me.getButton() == me.BUTTON2) {
		    startDragPosition = mousePosition;
		}
	    }

	    public void mouseReleased(MouseEvent me) {
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
		    drawScale = 1;
		    drawFactor = drawFactorOriginal * drawScale;
		    repaint();
		}
	    }

	    public void mouseEntered(MouseEvent me) {
	    }

	    public void mouseExited(MouseEvent me) {
	    }

	};
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

	};
    protected MouseListener MallaListener = new MouseListener() {
	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON3) {
		    //Acepto el mallado actual
		    for (int i = 0; i < parcelasTemporales.size(); i++) {
			addParcela((Polygon2D.Double)parcelasTemporales.elementAt(i));
		    }
		    //Vacio el vector de Parcelas Temporales
		    parcelasTemporales.removeAllElements();
		    //Vacio los datos de la operación actual
		    /**REVISAR LOS DATOS A BORRAR*/
		    emptyData();
		} else if (me.getButton() == me.BUTTON1) {
		    //Cambio el origen del mallado
		    puntoOrigen = new Point2D.Double(currentPosition.getX(), currentPosition.getY());
		    statusBar.setText(operacionStatus + "Origen " + puntoOrigen.getX() + "," + puntoOrigen.getY());
		}
	    }

	    public void mousePressed(MouseEvent me) {
	    }

	    public void mouseReleased(MouseEvent me) {
	    }

	    public void mouseEntered(MouseEvent me) {
	    }

	    public void mouseExited(MouseEvent me) {
	    }

	};
    protected MouseMotionListener MallaMotionListener = new MouseMotionListener() {
	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		double areamallada = area * parcelasTemporales.size();
		double arearestante = areaTotal - areamallada;
		labelinfo.setText("Area mallada: " + String.valueOf(decimalFormat(areamallada, 4)) + " m2, Area restante: " + String.valueOf(decimalFormat(arearestante, 4)));
		/**inside = getFakePolyFromPoints(xd,yd).contains(me.getPoint());*/
		if (puntoOrigen != null) {
		    makeShapes(puntoOrigen, currentPosition);
		}
		//                currentPosition = (Point2D.Double)distancePoints.elementAt(distancePoints.size()-1);
		repaint();
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
	    }

	};
    protected MouseListener PoligonoListener = new MouseListener() {
	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON1) {
		    distance(true);
		}
	    }

	    public void mousePressed(MouseEvent me) {
	    }

	    public void mouseReleased(MouseEvent me) {
	    }

	    public void mouseEntered(MouseEvent me) {
	    }

	    public void mouseExited(MouseEvent me) {
	    }

	};
    protected MouseMotionListener PoligonoMotionListener = new MouseMotionListener() {
	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		labelxy.setText("X: " + me.getX() + ", Y: " + me.getY());
		//labelinfo.setText("Dist x*y: " + String.valueOf(l.ptSegDist(me.getPoint())));
		/**      inside = getFakePolyFromPoints(xd,yd).contains(me.getPoint());*/
		distance(false);
		//currentPosition = (Point2D.Double)distancePoints.elementAt(distancePoints.size()-1);
		repaint();
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
	    }

	};
    protected MouseListener EliminaListener = new MouseListener() {
	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON1) {
		    //Eliminar poligono contenido!
		    if (containedParcela != -1) {
			parcelas.remove(containedParcela);
			double areamallada = area * parcelas.size();
			double arearestante = areaTotal - areamallada;
			labelinfo.setText("Area mallada: " + String.valueOf(decimalFormat(areamallada, 4)) + " m2, Area restante: " + String.valueOf(decimalFormat(arearestante, 4)));
			containedParcela = -1;
			repaint();
		    }
		}
	    }
	    //REVISAR

	    public void mousePressed(MouseEvent me) {
	    }
	    //REVISAR

	    public void mouseReleased(MouseEvent me) {
	    }

	    public void mouseEntered(MouseEvent me) {
	    }

	    public void mouseExited(MouseEvent me) {
	    }

	};
    protected MouseMotionListener EliminaMotionListener = new MouseMotionListener() {
	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		labelxy.setText("X: " + me.getX() + ", Y: " + me.getY());
		mousePosition = me.getPoint();
		containedParcela = -1;
		for (int i = 0; i < parcelas.size(); i++) {
		    Polygon2D _parcela = (Polygon2D)parcelas.elementAt(i);
		    if (getFakePolyFromPoly(_parcela).contains(me.getPoint()))
			containedParcela = i;
		}
		//currentPosition = toSpace(mousePosition);
		repaint();
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
	    }

	};
    //************************************************************
    protected MouseListener ParcelaListener = new MouseListener() {
	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON1) {
		    //parcelasTemporales.removeAllElements();
		    if (x0Parcela == null) {
			x0Parcela = currentPosition;
		    } else {
			x1Parcela = currentPosition;
		    }
		}
		if (me.getButton() == me.BUTTON3) {
		    addParcela(parcelasTemporales.lastElement());
		    parcelasTemporales.removeAllElements();
		    System.out.println("Ar: " + area + " Pr: " + areaParcial + " Rr: " + areaRestante);
		    if (areaParcial < area) {
			/**parte = true;*/
			areaRestante += areaParcial;
			System.out.println("At: " + area + " Pt: " + areaParcial + " Rt: " + areaRestante);
		    } else {
			/**parte = false;*/
			areaRestante = 0;
			System.out.println("Af: " + area + " Pf: " + areaParcial + " Rf: " + areaRestante);
		    }
		    if (areaRestante >= area) {
			areaRestante = 0;
			areaParcial = area;
		    } else {
			//arearestante = area - areaparcial;
		    }
		    System.out.println("Ar: " + area + " Pr: " + areaParcial + " Rr: " + areaRestante);
		    x0Parcela = null;
		    x1Parcela = null;
		    repaint();
		}
	    }
	    //REVISAR

	    public void mousePressed(MouseEvent me) {
	    }
	    //REVISAR

	    public void mouseReleased(MouseEvent me) {
	    }

	    public void mouseEntered(MouseEvent me) {
	    }

	    public void mouseExited(MouseEvent me) {
	    }

	};
    protected MouseMotionListener ParcelaMotionListener = new MouseMotionListener() {
	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		mousePosition = me.getPoint();
		osnap(mousePosition);
		//currentPosition = (Point2D.Double)distancePoints.elementAt(distancePoints.size()-1);
		if ((x0Parcela != null) && (x1Parcela != null)) {
		    makeParcela(x0Parcela, x1Parcela, currentPosition, me.getModifiers());
		}
		repaint();
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
	    }

	};

    private void makeParcela(Point2D x0, Point2D x1, Point2D destino, int modif) {
	int dirx = 1, diry = 1;
	double ho = x1.getX() - x0.getX();
	double ve = x1.getY() - x0.getY();
	double distancia = distance(x0, x1);
	Point2D destino0;
	Point2D destino1;
	/*out = false;
    for (int j = 0; j<xp.length; j++)
    {
      //if (!mina.contains(xtoPixel(xp[j]), ytoPixel(yp[j])) || !mina.intersects(xtoPixel(xp[j]), ytoPixel(yp[j]),0,0))
      if (minareal.contains(xp[j], yp[j]) || minareal.intersects(xp[j], yp[j],.0001,.0001))
      {
      } else
      {
        out = true;
        //for (int i=0; i<
      }
    }*/
	double _ap = area;
	if (areaRestante + 1 < area) {
	    _ap = area - areaRestante;
	} else {
	    areaRestante = 0;
	}
	if (modif == 1) //SHIFT
	{
	    destino0 = destino;
	    destino1 = destino;
	    _ap = _ap - areaParcial;
	} else {
	    destino0 = x0;
	    destino1 = x1;
	}
	Vector puntos = new Vector();
	if (ho == 0) //vertical
	{
	    double naltura = distancia;
	    double nbase = _ap / naltura;
	    if (destino.getX() < x1.getX()) {
		dirx = -1;
	    }
	    if (modif == 1) {
		nbase = 0;
	    }
	    puntos.add(x0);
	    puntos.add(x1);
	    puntos.add(new Point2D.Double(destino1.getX() + dirx * nbase, x1.getY()));
	    puntos.add(new Point2D.Double(destino0.getX() + dirx * nbase, x0.getY()));
	} else if (ve == 0) //horizontal
	{
	    double nbase = distancia;
	    double naltura = _ap / nbase;
	    if (destino.getY() < x1.getY()) {
		diry = -1;
	    }
	    if (modif == 1) {
		naltura = 0;
	    }
	    puntos.add(x0);
	    puntos.add(x1);
	    puntos.add(new Point2D.Double(x1.getX(), destino1.getY() + diry * naltura));
	    puntos.add(new Point2D.Double(x0.getX(), destino0.getY() + diry * naltura));
	} else {
	    double nbase = distancia;
	    double naltura = area / nbase;
	    puntos.add(x0);
	    puntos.add(x1);
	    puntos.add(new Point2D.Double(x1.getX(), x1.getY() + diry * naltura));
	    puntos.add(new Point2D.Double(x0.getX(), x1.getY() + diry * naltura));
	}
	parcelasTemporales.removeAllElements();
	parcelasTemporales.add(getTruePolyFromPoints(puntos));
	areaParcial = calcArea(puntos);
	labelinfo.setText("Area:= " + String.valueOf(areaParcial));
    }
    //***********************************************************

    private void makeTrapecio(Point2D.Double destino) {
	int dirx = 1, diry = 1;
	double ho = a1Trapecio.getX() - a0Trapecio.getX();
	double ve = a1Trapecio.getY() - a0Trapecio.getY();
	double distancia = distance(a0Trapecio, a1Trapecio);
	double _ap = area;
	double delta = 1.0;
	if (areaRestante + 1 < area) {
	    _ap = area - areaRestante;
	} else {
	    areaRestante = 0;
	}
	Point2D.Double destino0;
	Point2D.Double destino1;
	destino0 = a0Trapecio;
	destino1 = a1Trapecio;
	double ladoa = distancia;
	//es el tamaï¿½o del lado a
	double ladob = 0;
	double ladoc = 0;
	Vector puntos = new Vector();
	if (ho == 0) //vertical --> El lado a es vertical
	{
	    System.out.println("Lado a vertical");
	} else //if (ve == 0) //horizontal --> el lado a es horizontal
	{
	    System.out.println("Lado a horizontal");
	    if (destino.getY() < a1Trapecio.getY()) {
		diry = -1;
	    }
	    Point2D.Double cp0 = new Point2D.Double(a0Trapecio.getX(), a0Trapecio.getY());
	    double temp = 0.0;
	    Point2D.Double hp2;
	    double dist = 0.0;
	    while (temp < _ap) {
		puntos.removeAllElements();
		dist = dist + delta;
		Point2D.Double cp1 = new Point2D.Double(a0Trapecio.getX(), a0Trapecio.getY() + (dist * diry));
		Point2D.Double cp2 = new Point2D.Double(h0Trapecio.getX(), cp1.getY());
		ladoc = distance(cp0, cp1);
		hp2 = dameInterseccion(h0Trapecio, h1Trapecio, cp1, cp2);
		ladob = distance(cp1, hp2);
		System.out.println("ladoa1: " + ladoa);
		System.out.println("ladob1: " + ladob);
		System.out.println("ladoc1: " + ladoc);
		puntos.add(a0Trapecio);
		puntos.add(a1Trapecio);
		puntos.add(hp2);
		puntos.add(cp1);
		temp = calcArea(puntos);
		System.out.println("area1: " + temp);
	    }
	    temp = 0.0;
	    dist = dist - delta;
	    delta = delta / 10.0;
	    while (temp < _ap) {
		puntos.removeAllElements();
		dist = dist + delta;
		Point2D.Double cp1 = new Point2D.Double(a0Trapecio.getX(), a0Trapecio.getY() + (dist * diry));
		Point2D.Double cp2 = new Point2D.Double(h0Trapecio.getX(), cp1.getY());
		ladoc = distance(cp0, cp1);
		hp2 = dameInterseccion(h0Trapecio, h1Trapecio, cp1, cp2);
		ladob = distance(cp1, hp2);
		System.out.println("ladoa2: " + ladoa);
		System.out.println("ladob2: " + ladob);
		System.out.println("ladoc2: " + ladoc);
		puntos.add(a0Trapecio);
		puntos.add(a1Trapecio);
		puntos.add(hp2);
		puntos.add(cp1);
		temp = calcArea(puntos);
		System.out.println("area2: " + temp);
	    }
	    temp = 0.0;
	    dist = dist - delta;
	    delta = delta / 100.0;
	    while (temp < _ap) {
		puntos.removeAllElements();
		dist = dist + delta;
		Point2D.Double cp1 = new Point2D.Double(a0Trapecio.getX(), a0Trapecio.getY() + (dist * diry));
		Point2D.Double cp2 = new Point2D.Double(h0Trapecio.getX(), cp1.getY());
		ladoc = distance(cp0, cp1);
		hp2 = dameInterseccion(h0Trapecio, h1Trapecio, cp1, cp2);
		ladob = distance(cp1, hp2);
		System.out.println("ladoa2: " + ladoa);
		System.out.println("ladob2: " + ladob);
		System.out.println("ladoc2: " + ladoc);
		puntos.add(a0Trapecio);
		puntos.add(a1Trapecio);
		puntos.add(hp2);
		puntos.add(cp1);
		temp = calcArea(puntos);
		System.out.println("area2: " + temp);
	    }
	    temp = 0.0;
	    dist = dist - delta;
	    delta = delta / 100.0;
	    while (temp < _ap) {
		puntos.removeAllElements();
		dist = dist + delta;
		Point2D.Double cp1 = new Point2D.Double(a0Trapecio.getX(), a0Trapecio.getY() + (dist * diry));
		Point2D.Double cp2 = new Point2D.Double(h0Trapecio.getX(), cp1.getY());
		ladoc = distance(cp0, cp1);
		hp2 = dameInterseccion(h0Trapecio, h1Trapecio, cp1, cp2);
		ladob = distance(cp1, hp2);
		System.out.println("ladoa2: " + ladoa);
		System.out.println("ladob2: " + ladob);
		System.out.println("ladoc2: " + ladoc);
		puntos.add(a0Trapecio);
		puntos.add(a1Trapecio);
		puntos.add(hp2);
		puntos.add(cp1);
		temp = calcArea(puntos);
		System.out.println("area2: " + temp);
	    }
	}
	parcelasTemporales.removeAllElements();
	parcelasTemporales.addElement(getTruePolyFromPoints(puntos));
	areaParcial = calcArea(puntos);
	labelinfo.setText("Area:= " + String.valueOf(areaParcial));
	double kk = ((ladoa + ladob) * ladoc) / 2;
	System.out.println("Area:= " + kk);
    }
    protected MouseListener NuevoListener = new MouseListener() {
	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON3) {
		    for (int i = 0; i < parcelas.size(); i++) {
			double[] x = new double[4];
			double[] y = new double[4];
			Point2D[] _parcela = (Point2D[])parcelas.elementAt(i);
			//GRABAR EN LA BASE DE DATOS
			String Query = "Insert into minas (MAPKEY,the_geom) values('" + (i + 10) + "',GeometryFromText('POLYGON(((";
			for (int j = 0; j < 4; j++) {
			    Query += String.valueOf(_parcela[j].getX()) + " " + String.valueOf(_parcela[j].getY()) + ",";
			}
			Query += String.valueOf(_parcela[0].getX()) + " " + String.valueOf(_parcela[0].getY()) + "))',-1) )";
			LibSQL.exActualizar('a', Query);
			System.out.println("insert: " + (i + 10));
		    }
		}
	    }

	    public void mousePressed(MouseEvent me) {
	    }

	    public void mouseReleased(MouseEvent me) {
	    }

	    public void mouseEntered(MouseEvent me) {
	    }

	    public void mouseExited(MouseEvent me) {
	    }

	};
    protected MouseMotionListener NuevoMotionListener = new MouseMotionListener() {
	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
	    }

	};
    //******************************************************************************

    private Point2D.Double dameInterseccion(Point2D.Double p0, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
	double a = (p1.getY() - p0.getY()) / (p1.getX() - p0.getX());
	double b = -1.0;
	double c = a * p0.getX() - p0.getY();
	double d = (p3.getY() - p2.getY()) / (p3.getX() - p2.getX());
	double e = -1.0;
	double f = d * p2.getX() - p2.getY();
	double D = a * e - b * d;
	double x = (c * e - f * b) / D;
	double y = (a * f - d * c) / D;
	System.out.println("Int: " + x + ", " + y);
	return new Point2D.Double(x, y);
    }
    protected MouseListener ParalelaListener = new MouseListener() {

	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		/**      if (me.getButton() == me.BUTTON1)
      {
        if (pp0 == null)
        {
          pp0 = new Point2D.Double(curpos.getX(), curpos.getY());
          System.out.println("pp0 set");
        } else
        {
          pp1 = new Point2D.Double(curpos.getX(), curpos.getY());
          System.out.println("pp1 set");
          l.setLine(pp0,pp1);
        }
      }*/
	    }

	    public void mousePressed(MouseEvent me) {

	    }

	    public void mouseReleased(MouseEvent me) {

	    }

	    public void mouseEntered(MouseEvent me) {

	    }

	    public void mouseExited(MouseEvent me) {

	    }

	}
    ;
    protected MouseMotionListener ParalelaMotionListener = new MouseMotionListener() {

	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		/**      labelxy.setText("X: " + me.getX() + ", Y: " + me.getY());
      //labelinfo.setText("Dist x*y: " + String.valueOf(l.ptSegDist(me.getPoint())));
      inside = getFakePolyFromPoints(xd,yd).contains(me.getPoint());
      mouse = me.getPoint();
      polypoints[cantdistpoints] = new Point2D.Double(xtoSpace(me.getX()), ytoSpace(me.getY()));
      osnapRectangle = null;
      osnap = false;
      int m = 0;
      while ((m<mpuntos.size()) && (!osnap))
      {
        l.setLine(0,0,0,0);
        double x = ((Point2D.Double)mpuntos.elementAt(m)).getX();
        double y = ((Point2D.Double)mpuntos.elementAt(m)).getY();
        if (Math.abs(me.getX()-xtoPixel(x))<5)
        {
          if (Math.abs(me.getY()-ytoPixel(y))<5)
          {
            polypoints[cantdistpoints] = new Point2D.Double(x, y);
            osnapRectangle = new Rectangle2D.Double(xtoPixel(x) - size / 2, ytoPixel(y) - size / 2, size, size);
            labelinfo.setText("O:Interseccion");
            osnap = true;
          }
        }
        m++;
      }
      m = 0;

      while ((m<mpuntos.size()) && (!osnap))
      {
        double x = ((Point2D.Double)mpuntos.elementAt(m)).getX();
        double y = ((Point2D.Double)mpuntos.elementAt(m)).getY();
        if (Math.abs(me.getX()-xtoPixel(x))<5)
        {
          polypoints[cantdistpoints] = new Point2D.Double(x, ytoSpace(me.getY()));
          osnapRectangle = new Rectangle2D.Double(xtoPixel(x) - size / 2, me.getY() - size / 2, size, size);
          labelinfo.setText("O:Y");
          l.setLine(xtoPixel(x), 0, xtoPixel(x), 800);
          osnap = true;
        } else if (Math.abs(me.getY()-ytoPixel(y))<5)
        {
          polypoints[cantdistpoints] = new Point2D.Double(xtoSpace(me.getX()), y);
          osnapRectangle = new Rectangle2D.Double(me.getX() - size / 2, ytoPixel(y) - size / 2, size, size);
          labelinfo.setText("O:X");
          l.setLine(0, ytoPixel(y), 800, ytoPixel(y));
          osnap = true;
        }
        m++;
      }

      if (cantdistpoints>0)
      {
        distance(polypoints[cantdistpoints].getX(), polypoints[cantdistpoints].getY(), false);
        for (int i=0; i<cantdistpoints; i++)
        {
          if (Math.abs(me.getX()-xtoPixel(polypoints[i].getX()))<5 && Math.abs(me.getY()-ytoPixel(polypoints[i].getY()))<5)
          {
            polypoints[cantdistpoints] = new Point2D.Double(polypoints[i].getX(), polypoints[i].getY());
            distance(polypoints[i].getX(), polypoints[i].getY(), false);
            osnapRectangle = new Rectangle2D.Double(xtoPixel(polypoints[i].getX()) - size / 2, ytoPixel(polypoints[i].getY()) - size / 2, size, size);
            osnap = true;
          }
        }
      }

      if (xp.length>0)
      {
        distance(polypoints[cantdistpoints].getX(), polypoints[cantdistpoints].getY(), false);
        for (int i=0; i<xp.length; i++)
        {
          if (Math.abs(me.getX()-xtoPixel(xp[i]))<5 && Math.abs(me.getY()-ytoPixel(yp[i]))<5)
          {
            polypoints[cantdistpoints] = new Point2D.Double(xp[i], yp[i]);
            distance(xp[i], yp[i], false);
            osnapRectangle = new Rectangle2D.Double(xtoPixel(xp[i]) - size / 2, ytoPixel(yp[i]) - size / 2, size, size);
            osnap = true;
          }
        }
      }

      curpos = polypoints[cantdistpoints];
      repaint(); */
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
		/** if (mSelectedPoint != null)
      {
        double deltax = mSelectedPoint.getX() - me.getPoint().getX();
        double deltay = mSelectedPoint.getY() - me.getPoint().getY();
        Point2D punto0 = new Point2D.Double(mPoints[0].getX() - deltax, mPoints[0].getY() - deltay);
        Point2D punto1 = new Point2D.Double(mPoints[1].getX() - deltax, mPoints[1].getY() - deltay);
        labelxy.setText(String.valueOf(punto0.getX()) + ", " + String.valueOf(punto1.getX()));
        labelinfo.setText(String.valueOf(punto0.getY()) + ", " + String.valueOf(punto1.getY()));
        l.setLine(punto0, punto1);
      }
      curpos = toSpace(me.getX(), me.getY());
      repaint(); */
	    }

	}
    ;
    protected MouseListener TrapecioRectListener = new MouseListener() {

	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON1) {
		    if (!aTrapecio) {
			//pido lado a
			if (a0Trapecio == null) {
			    a0Trapecio = new Point2D.Double(currentPosition.getX(), currentPosition.getY());
			    System.out.println("set: ladoap0");
			} else {
			    a1Trapecio = new Point2D.Double(currentPosition.getX(), currentPosition.getY());
			    System.out.println("set: ladoap1");
			    aTrapecio = true;
			}
		    } else if (!hTrapecio) {
			//pido lado h
			if (h0Trapecio == null) {
			    h0Trapecio = new Point2D.Double(currentPosition.getX(), currentPosition.getY());
			    System.out.println("set: ladohp0");
			} else {
			    h1Trapecio = new Point2D.Double(currentPosition.getX(), currentPosition.getY());
			    System.out.println("set: ladohp1");
			    hTrapecio = true;
			    if (aTrapecio && hTrapecio) {
				makeTrapecio(currentPosition);
				repaint();
			    }
			}
		    }
		}
		if (me.getButton() == me.BUTTON3) {
		    Point2D[] xparcelas;
		    for (int i = 0; i < parcelasTemporales.size(); i++) {
			addParcela((Polygon2D.Double)parcelasTemporales.elementAt(i));
		    }
		    System.out.println("Ar: " + area + " Pr: " + areaParcial + " Rr: " + areaRestante);
		    if (areaParcial < area) {
			/**parte = true;*/
			areaRestante += areaParcial;
			System.out.println("At: " + area + " Pt: " + areaParcial + " Rt: " + areaRestante);
		    } else {
			/**parte = false;*/
			areaRestante = 0;
			System.out.println("Af: " + area + " Pf: " + areaParcial + " Rf: " + areaRestante);
		    }
		    if (areaRestante >= area) {
			areaRestante = 0;
			areaParcial = area;
		    } else {
			//arearestante = area - areaparcial;
		    }
		    System.out.println("Ar: " + area + " Pr: " + areaParcial + " Rr: " + areaRestante);
		    emptyData();
		}
	    }

	    public void mousePressed(MouseEvent me) {

	    }

	    public void mouseReleased(MouseEvent me) {

	    }

	    public void mouseEntered(MouseEvent me) {

	    }

	    public void mouseExited(MouseEvent me) {

	    }

	}
    ;
    protected MouseMotionListener TrapecioRectMotionListener = new MouseMotionListener() {

	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		/*                if (aTrapecio && hTrapecio)
                {
                    makeTrapecio(currentPosition);
                    repaint();
                }
                */
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
		/** if (mSelectedPoint != null)
      {
        double deltax = mSelectedPoint.getX() - me.getPoint().getX();
        double deltay = mSelectedPoint.getY() - me.getPoint().getY();
        Point2D punto0 = new Point2D.Double(mPoints[0].getX() - deltax, mPoints[0].getY() - deltay);
        Point2D punto1 = new Point2D.Double(mPoints[1].getX() - deltax, mPoints[1].getY() - deltay);
        labelxy.setText(String.valueOf(punto0.getX()) + ", " + String.valueOf(punto1.getX()));
        labelinfo.setText(String.valueOf(punto0.getY()) + ", " + String.valueOf(punto1.getY()));
        l.setLine(punto0, punto1);
      }
      curpos = toSpace(me.getX(), me.getY());
      repaint(); */
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

    /** private File selectFile()
  {
    File file;
    String filename = File.separator+"shp";
    JFileChooser fc = new JFileChooser(lastpath);

    fc.setFileFilter(new SHPFilter());
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setAcceptAllFileFilterUsed(false);

      // Show open dialog; this method does not return until the dialog is closed
    fc.showOpenDialog(this);
    try
    {
      file = fc.getSelectedFile();
    } catch (NullPointerException ex)
    {
      file = null;
    }
    return file;
  }*/
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
	if (Poligonos.size() == 0) {
	    return false;
	} else {
	    return true;
	}
    }
    protected MouseListener InfoListener = new MouseListener() {
	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON1) {
		    if (containedPolygon != -1) {
			containedPolygon = -1;
			repaint();
		    }
		}
	    }
	    //REVISAR

	    public void mousePressed(MouseEvent me) {
	    }
	    //REVISAR

	    public void mouseReleased(MouseEvent me) {
	    }

	    public void mouseEntered(MouseEvent me) {
	    }

	    public void mouseExited(MouseEvent me) {
	    }

	};
    protected MouseMotionListener InfoMotionListener = new MouseMotionListener() {
	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		labelxy.setText("X: " + me.getX() + ", Y: " + me.getY());
		labelinfo.setText(String.valueOf(drawScale));
		mousePosition = me.getPoint();
		containedPolygon = -1;
		/*for (int i = 0; i < parcelas.size(); i++) {
                    Polygon2D _parcela = (Polygon2D)parcelas.elementAt(i);
                    if (getFakePolyFromPoly(_parcela).contains(me.getPoint()))
                        containedPolygon = i;
                }*/
		for (int i = 0; i < Poligonos.size(); i++) {
		    Polygon2D _parcela = (Polygon2D)Poligonos.elementAt(i);
		    if (getFakePolyFromPoly(_parcela).contains(me.getPoint()))
			containedPolygon = i;
		}
		repaint();
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
	    }

	};
    protected MouseListener NumeraListener = new MouseListener() {
	    //REVISAR

	    public void mouseClicked(MouseEvent me) {
		if (me.getButton() == me.BUTTON1) {
		    if (containedParcela != -1) {
			Polygon2D _poly = (Polygon2D)parcelas.elementAt(containedParcela);
			if (_poly.getId() == -1) {
			    idParcela++;
			    _poly.setId(idParcela);
			}
			repaint();
			containedParcela = -1;
		    }
		}
	    }
	    //REVISAR

	    public void mousePressed(MouseEvent me) {
	    }
	    //REVISAR

	    public void mouseReleased(MouseEvent me) {
	    }

	    public void mouseEntered(MouseEvent me) {
	    }

	    public void mouseExited(MouseEvent me) {
	    }

	};
    protected MouseMotionListener NumeraMotionListener = new MouseMotionListener() {
	    //REVISAR

	    public void mouseMoved(MouseEvent me) {
		labelxy.setText("X: " + me.getX() + ", Y: " + me.getY());
		mousePosition = me.getPoint();
		containedParcela = -1;
		for (int i = 0; i < parcelas.size(); i++) {
		    Polygon2D _parcela = (Polygon2D)parcelas.elementAt(i);
		    if (getFakePolyFromPoly(_parcela).contains(me.getPoint()))
			containedParcela = i;
		}
		//currentPosition = toSpace(mousePosition);
		repaint();
	    }
	    //REVISAR

	    public void mouseDragged(MouseEvent me) {
	    }

	};

    private void calculaEntorno() {
	if ((fWidth != this.getWidth()) || (fHeight != this.getHeight())) {
	    fWidth = this.getWidth();
	    fHeight = this.getHeight();
	    if (xExtents > yExtents) {
		drawFactor = (this.getWidth() - 20) / xExtents;
	    } else {
		drawFactor = (this.getHeight() - 20) / yExtents;
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

}

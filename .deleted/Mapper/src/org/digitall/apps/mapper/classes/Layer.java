package org.digitall.apps.mapper.classes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.util.Vector;

import org.digitall.lib.geo.shapefile.ShapeTypes;
import org.digitall.lib.geom.Polygon2D;

public class Layer {

    private int shapeType = -1;
    // -1 = Not Set
    private Color color;
    private int symbol = -1;
    //For points,  -1 = Not Set
    private int lineType = -1;
    //For polylines and polygons, -1 = Not Set
    private int fillPatern = -1;
    //For polygons, -1 = Not Set
    private Color fillColor;
    //For polygons
    private int projectionType = -1;
    //UTM, GK, LL, -1 = Not Set
    //Variables de Posicion
    //Variables de Dibujo
    private Vector badLines = new Vector();
    //Variables del Universo
    private double drawFactor = 0;
    private double xOffset = 0;
    private double yOffset = 0;
    private int fHeight = 500;

    public Layer() {

    }

    public void drawLayer(Graphics2D g2, Vector Shapes) {
	switch (shapeType) {
	    case ShapeTypes.POINT :
		drawPoints(g2, Shapes);
		break;
	    case ShapeTypes.POLYGON :
		drawPolys(g2, Shapes);
		break;
	    default :
		break;
	}
    }

    private void drawPolys(Graphics2D g2, Vector Poligonos) {
	//Recorro el vector Poligonos para dibujar 
	//TODOS los poligonos del Layer
	for (int i = 0; i < Poligonos.size(); i++) {
	    Polygon2D poly = getFakePolyFromPoly((Polygon2D)(Poligonos.elementAt(i)));
	    g2.setColor(color);
	    g2.draw(poly);
	}
    }

    private void drawPoints(Graphics2D g2, Vector Puntos) {
	//Recorro el vector Puntos para dibujar 
	//TODOS los puntos del Layer
	for (int i = 0; i < Puntos.size(); i++) {
	    Rectangle2D.Double point = getFakeRectangleFromPoint((Point2D.Double)(Puntos.elementAt(i)));
	    g2.setColor(color);
	    g2.draw(point);
	}
    }

    public Polygon2D getFakePolyFromPoly(Polygon2D _poly) {
	double[] xy = new double[_poly.getVertexCount() * 2];
	for (int i = 0; i < _poly.getVertexCount() * 2; i += 2) {
	    xy[i] = ((_poly.getX(i / 2) - xOffset) * drawFactor);
	    xy[i + 1] = (fHeight - ((_poly.getY(i / 2) - yOffset) * drawFactor));
	}
	return new Polygon2D.Double(xy);
    }

    public Rectangle2D.Double getFakeRectangleFromPoint(Point2D xy) {
	double osnapRectSize = 8;
	return new Rectangle2D.Double(xtoPixel(xy.getX()) - osnapRectSize / 2, ytoPixel(xy.getY()) - osnapRectSize / 2, osnapRectSize, osnapRectSize);
    }

    public void setBadLines(Vector _polys) {
	badLines.removeAllElements();
	for (int j = 0; j < _polys.size(); j++) {
	    Polygon2D.Double _poly = (Polygon2D.Double)(_polys.elementAt(j));
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

    private int xtoPixel(double x) {
	return (int)(((x - xOffset) * drawFactor));
    }

    private int ytoPixel(double y) {
	return (int)((fHeight - ((y - yOffset) * drawFactor)));
    }

    public void setShapeType(int _shapeType) {
	shapeType = _shapeType;
    }

    public int getShapeType() {
	return shapeType;
    }

    public void setColor(Color _color) {
	color = _color;
    }

    public Color getColor() {
	return color;
    }

    public void setSymbol(int _symbol) {
	symbol = _symbol;
    }

    public int getSymbol() {
	return symbol;
    }

    public void setLineType(int _lineType) {
	lineType = _lineType;
    }

    public int getLineType() {
	return lineType;
    }

    public void setFillPatern(int _fillPatern) {
	fillPatern = _fillPatern;
    }

    public int getFillPatern() {
	return fillPatern;
    }

    public void setFillColor(Color _fillColor) {
	fillColor = _fillColor;
    }

    public Color getFillColor() {
	return fillColor;
    }

    public void setProjectionType(int _projectionType) {
	projectionType = _projectionType;
    }

    public int getProjectionType() {
	return projectionType;
    }

}

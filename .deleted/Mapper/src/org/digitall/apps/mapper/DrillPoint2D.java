package org.digitall.apps.mapper;

import java.awt.geom.Point2D;

public abstract class DrillPoint2D extends Point2D {

    private int idPoint;
    private String name;

    public void setName(String _name) {
	name = _name;
    }

    public String getName() {
	return name;
    }

    public static class Double extends DrillPoint2D {

	public double x;
	public double y;

	public Double(double _x, double _y) {
	    x = _x;
	    y = _y;
	}

	public double getX() {
	    return x;
	}

	public double getY() {
	    return y;
	}

	public void setLocation(double _x, double _y) {
	    x = _x;
	    y = _y;
	}

    }

    public void setIdPoint(int _idPoint) {
	idPoint = _idPoint;
    }

    public int getIdPoint() {
	return idPoint;
    }
    
}

package org.digitall.projects.sermaq.maps;

import java.awt.BasicStroke;
import java.awt.Color;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.digitall.lib.data.Format;
import org.digitall.lib.geom.Polygon2D;
import org.digitall.lib.sql.LibSQL;

public class MapConnector {

    private int gid = -1;
    private Polygon2D polygon;
    private Color color;
    //Stroke's default values
    private BasicStroke stroke = new BasicStroke(1, 2, 0, 10, null, 0);

    public MapConnector() {
	this(-1);
    }

    public MapConnector(int _gid) {
	gid = _gid;
    }

    public void setPolygon(Polygon2D _polygon) {
	polygon = _polygon;
    }

    public Polygon2D getPolygon() {
	return polygon;
    }

    public void setColor(Color _color) {
	color = _color;
    }

    public Color getColor() {
	return color;
    }

    public void setStroke(BasicStroke _stroke) {
	stroke = _stroke;
    }

    public BasicStroke getStroke() {
	return stroke;
    }

    public void retrieveData() {
	if (gid != -1) {
	    ResultSet data = LibSQL.exQuery("SELECT * FROM maps.lineas WHERE gid = " + gid);
	    try {
		if (data.next()) {
		    //fetch data from database
		    float width = data.getFloat("width");
		    int cap = data.getInt("endstyle");
		    int join = data.getInt("joinstyle");
		    float miterlimit = data.getFloat("miterlimit");
		    String dashStr = data.getString("dash");
		    float[] dash = null;
		    if (dashStr != null) {
		        String dashes[] = dashStr.replace('{', ' ').replace('}',' ').split(",");
			dash = new float[dashes.length];
			for (int i = 0; i < dash.length; i++) {
			    dash[i] = Float.parseFloat(dashes[i]);
			}
		    }
		    float dash_phase = data.getFloat("dash_phase");
		    stroke = new BasicStroke(width, cap, join, miterlimit, dash, dash_phase);
		    color = Format.hex2Color(data.getString("color"));
		}
	    } catch (SQLException x) {
		x.printStackTrace();
	    }
	}
    }

    public boolean saveData() {
	boolean returns = false;
	if (gid == -1) {
	    gid = LibSQL.getInt("", "");
	    returns = (gid != -1);
	} else {
	    returns = LibSQL.getBoolean("", "");
	}
	return returns;
    }

    public int getGid() {
	return gid;
    }

}

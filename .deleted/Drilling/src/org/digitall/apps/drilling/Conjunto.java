package org.digitall.apps.drilling;

import java.util.Vector;

import org.digitall.lib.components.ListItem;

public class Conjunto {

    public Conjunto() {
    }
    /*public Vector diferencia(Vector vec1,Vector vec2){
        System.out.println("vec1 = "+vec1);
        System.out.println("vec2 = "+vec2);
        int tamanioMin,tamanioMax;
        tamanioMin = vec1.size();
        tamanioMax = vec2.size();
        Vector minuendo = new Vector(),sustraendo = new Vector();
        Vector dif = new Vector();
        if (vec2.size()<tamanioMin){
            tamanioMin = vec2.size();
            tamanioMax = vec1.size();
            minuendo = vec1;
            sustraendo = vec2;
        }
        else{
            minuendo = vec2;
            sustraendo = vec1;
        }
        System.out.println("tamanioMin = "+tamanioMin);
        System.out.println("tamanioMax = "+tamanioMax);
        System.out.println("minuendo = "+minuendo);
        System.out.println("sustraendo = "+sustraendo);
        for(int i=0;i<tamanioMax;i++){
            boolean encontro = false;
            int j = 0;
            while((!encontro)&&(j<tamanioMin)){
                if(minuendo.elementAt(i).toString().equals(sustraendo.elementAt(j).toString())){
                    encontro = true;
                }
                j++;
            }
            if(!encontro){
                dif.add(minuendo.elementAt(i));
            }
        }
        return dif;
    }*/

    public Vector diferencia(Vector _minuendo, Vector _sustraendo) {
	Vector dif = new Vector();
	for (int i = 0; i < _minuendo.size(); i++) {
	    boolean encontro = false;
	    int j = 0;
	    while ((!encontro) && (j < _sustraendo.size())) {
		if (_minuendo.elementAt(i).toString().equals(_sustraendo.elementAt(j).toString())) {
		    encontro = true;
		}
		j++;
	    }
	    if (!encontro) {
		dif.add(_minuendo.elementAt(i));
	    }
	}
	return dif;
    }

    public Vector diferenciaItems(Vector _minuendo, Vector _sustraendo) {
	Vector dif = new Vector();
	for (int i = 0; i < _minuendo.size(); i++) {
	    boolean encontro = false;
	    int j = 0;
	    ListItem item1 = (ListItem)_minuendo.elementAt(i);
	    ListItem item2 = (ListItem)_sustraendo.elementAt(j);
	    while ((!encontro) && (j < _sustraendo.size())) {
		if (item1.getId() == item2.getId()) {
		    encontro = true;
		} else {
		    item2 = (ListItem)_sustraendo.elementAt(j);
		}
		j++;
	    }
	    if (!encontro) {
		dif.add(new Integer(item1.getId()));
	    }
	}
	return dif;
    }
    /*public static void main(String args[]){
        Conjunto conjunto = new Conjunto();
        Vector vec1 = new Vector();
        Vector vec2 = new Vector();
        vec1.add("7");
        vec1.add("8");
        vec1.add("6");
        vec2.add("7");
        vec2.add("8");
        vec2.add("15");
        System.out.println(conjunto.diferencia(vec2,vec1));
    }*/

}

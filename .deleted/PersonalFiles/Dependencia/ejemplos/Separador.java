package org.digitall.apps.personalfiles.Dependencia.ejemplos;

//  
//  Separador.java
//  Copyright (c) 1996, Agustin Froufe
//  Todos los derechos reservados.
//  
//  No se asume ninguna  responsabilidad por el  uso o  alteracion  de este
//  software.  Este software se proporciona COMO ES, sin garantia de ningun
//  tipo de su funcionamiento y en ningun caso sera el autor responsable de
//  daños o perjuicios que se deriven del mal uso del software,  aun cuando
//  este haya sido notificado de la posibilidad de dicho daño.
// 
//   Compilador: javac 1.0
//        Autor: Agustin Froufe
//     Creacion: 22-Ago-1996  23:00:56
// 
//--------------------------------------------------------------------------
//  Esta informacion no es necesariamente definitiva y esta sujeta a cambios
//  que pueden ser incorporados en cualquier momento, sin avisar.
//--------------------------------------------------------------------------

import java.awt.*;

// En esta clase creamos un nuevo componente que presentará en pantalla 
// una línea de separación con sobra, para dar apariencia de tres
// dimensiones
public class Separador extends Canvas {
    public final static int HORIZONTAL = 0;
    public final static int VERTICAL = 1;

    int orientacion;
    Dimension sepTama,sepDim;

    // En el constructor debemos indicar la longitud, en ancho y la
    // orientación con que vamos a crear el separador
    public Separador( int lon,int thick,int orient ) {
        orientacion = orient;
        if( orient == HORIZONTAL ) 
            sepTama = new Dimension( lon,thick );
        else
            sepTama = new Dimension( thick,lon );
        }


    // Este método devuelve la orientación con que se ha creado
    // el separador
    public int getOrientacion() {
        return( orientacion );
        }


    // Permitimos cambiar la orientación del separador
    public void setOrientacion( int orient ) {
        // Si en el parámetro no llega una orientación válida generamos
        // una excepción de argumento ilegal
        if( orient > VERTICAL || orient < HORIZONTAL )
            throw new IllegalArgumentException( "Orientación ilegal" );

        if( orientacion != orient ) 
            {
            orientacion = orient;
            sepDim = new Dimension( sepDim.height,sepDim.width );
            invalidate();
            }
        }


    public Dimension preferredSize() {
        return( sepDim );
        }


    public Dimension minimumSize() {
        return( sepDim );
        }



    public void paint( Graphics g ) {
        int x1,y1,x2,y2;
        Rectangle bbox = bounds();
        Color c = getBackground();
        Color brillo = c.brighter();
        Color oscuro = c.darker();

        // Hacemos aparecer la caja que formará el separador
        if( orientacion == HORIZONTAL )
            {
            x1 = 0;
            x2 = bbox.width - 1;
            y1 = y2 = bbox.height/2 - 1;
            }
        else
            {
            x1 = x2 = bbox.width/2 - 1;
            y1 = 0;
            y2 = bbox.height - 1;
            }

        // Pintamos una línea oscura
        g.setColor( oscuro );
        g.drawLine( x1,y1,x2,y2 );

        // Pintamos una línea brillante para completar la sensación de 
        // tres dimensiones
        g.setColor( brillo );
        if( orientacion == HORIZONTAL )
            g.drawLine( x1,y1+1,x2,y2+1 );
        else
            g.drawLine( x1+1,y1,x2+1,y2 );
        }
    }

//---------------------------------------- Final del fichero Separador.java

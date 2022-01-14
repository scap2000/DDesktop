package org.digitall.apps.personalfiles.Dependencia.ejemplos;

/*==============================================================================
*
* $RCSfile: cvsmkhd,v $
*
* $Revision: 1.2 $ $Date: 2003/09/22 07:05:39 $
*
* $State: Exp $
*
* AUTOR: paquito
*
==============================================================================*/



import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListSelectionModel;



public class MainDrag
{
   //~ Metodos -----------------------------------------------------------------

   /**
    * Metodo main
    */
   public static void main( String[] args )
   {
      //Creamos una lista con cadenas de caracteres que será el origen del arrastre
      DefaultListModel defaultListModel = new DefaultListModel(  );
      JList jList = new JList( defaultListModel );
      jList.getSelectionModel(  )
           .setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

      defaultListModel.addElement( "Uno" );
      defaultListModel.addElement( "Dos" );
      defaultListModel.addElement( "Tres" );
      defaultListModel.addElement( "Cuatro" );
      defaultListModel.addElement( "Cinco" );

      JFrame frame = new JFrame( "Ejemplo de Origen de Arrastrar" );
      frame.add( jList );

      frame.setVisible( true );
      frame.setSize( new Dimension( 
            400,
            200 ) );
      frame.setLocation( 
         200,
         200 );
      //Creamos nuestro listener para conocer que  esta teniendo
      //lugar durante el proceso de arrastre
      DragSourceListenerEjemplo dragSourceListenerEjemplo =
         new DragSourceListenerEjemplo(  );

      //Obtenemos una fuente de arrastre: lo habital es obtener la asociada por defecto a la plataforma.
      DragSource dragSource = DragSource.getDefaultDragSource(  );

      //Creamos un listener de "gestos" de arrastre, para ser informados
      //de cuando, la actividad del usuario se interpreta como un intento de arrastre
      DragGestureListener dragGestureListener =
         new DragGestureListener( jList,dragSourceListenerEjemplo );

      //Se anhade dicho listener de "gestos" a la fuente de arrastre
      dragSource.createDefaultDragGestureRecognizer( 
         jList,
         //Aqui indicamos los tipos de acciones posibles que queremos: descritas en la clase DndConstants
      DnDConstants.ACTION_COPY | DnDConstants.ACTION_MOVE |
         DnDConstants.ACTION_LINK,
         dragGestureListener );
   }
}
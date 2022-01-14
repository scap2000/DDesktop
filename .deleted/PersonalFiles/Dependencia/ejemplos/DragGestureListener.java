package org.digitall.apps.personalfiles.Dependencia.ejemplos;

import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JList;


/**
 *  Clase encargada de observar los "gestos" del usuario para decidir si ha iniciado
 *  una operacion de arrastre. 
 */
public class DragGestureListener
        implements java.awt.dnd.DragGestureListener
{
   //~ Variables de instancia --------------------------------------------------

   private DragSourceListener dragSourceListener;

   private JList jList;

   //~ Constructores -----------------------------------------------------------

   /**
    * Crea  un nuevo objeto de la clase DragGestureListener.<br>
    */
   public DragGestureListener( 
      JList jList,
      DragSourceListener dragSourceListener )
   {
      this.jList = jList;
      this.dragSourceListener = dragSourceListener;
   }

   //~ Metodos -----------------------------------------------------------------

   /**
    * Este listener tiene un unico metodo, invocado cuando AWT considera
    * que lo  que está haciendo el usuario es una operación de arrastre.<br>
    */
   public void dragGestureRecognized( DragGestureEvent arg0 )
   {
      System.out.println("ELemento seleccionado: "+ this.jList.getSelectedValue(  ).toString(  ));
      //La clase que implementa Transferable, para el caso de una cadena
      //ya la proporciona java: StringSelection
      StringSelection seleccion =
         new StringSelection( this.jList.getSelectedValue(  ).toString(  ) );

      //Con este metodo del evento comenzamos "oficialmente" la operacion de arrastre
      arg0.startDrag( 
         //Aqui indicamos el tipo de cursor que queremos durante el arrastre, null si nos vale el proporcionado por defecto
      null,
         //Aqui va el Transferable que queremos enviar 
      seleccion,
         //Aqui va el listener del proceso de arrastre
      this.dragSourceListener );
   }
}
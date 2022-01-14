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



import java.awt.Color;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;


/**
 * Este es el listener que nos va a tener informados en todo momento de
 * como evoluciona el proceso de arrastre
 */
public class DragSourceListenerEjemplo
        implements DragSourceListener
{
   //~ Metodos -----------------------------------------------------------------

   /**
    * Este es el metodo mas importante: nos indica que el proceso de
    * arrastar y soltar ha finalizado, es decir el usuario ha pulsado el
    * raton, lo ha arrastrado con el pulsado y  lo ha soltado
    */
   public void dragDropEnd( DragSourceDropEvent arg0 )
   {
      //En primer lugar necesitamos saber si se ha completado con exito el proceso:
      //puede que el destino no fuese valido, no aceptase el tipo de arrastre 
      //(copiar, mover, enlazar) que el usuario ha intentado... 
      if( arg0.getDropSuccess(  ) )
      {
         if( arg0.getDropAction(  ) == DnDConstants.ACTION_COPY )
         {
            //En este caso no hacemos nada porque la lista sigue como estaba
         }
         else if( arg0.getDropAction(  ) == DnDConstants.ACTION_MOVE )
         {
            //Aqui eliminamos los elementos movidos de la lista
            JList jList = (JList)arg0.getDragSourceContext(  ).getComponent(  );
            DefaultListModel defaultListModel =
               (DefaultListModel)jList.getModel(  );

            ListSelectionModel listSelectionModel = jList.getSelectionModel(  );

            defaultListModel.removeElementAt( 
               listSelectionModel.getMinSelectionIndex(  ) );
         }
         else if( arg0.getDropAction(  ) == DnDConstants.ACTION_LINK )
         {
            //Mismo comentario que en copiar: en otra circunstancia podríamos pasar 
            //una referencia al elemento arrastrado o algo así...
         }

         arg0.getDragSourceContext(  ).getComponent(  )
             .setBackground( Color.WHITE );
      }
   }

   /**
    * Este método se invoca cuando el ratón ha entrado en un posible
    * destino de soltar. Cambiamos el color de fondo de nuestra lista a verde
    */
   public void dragEnter( DragSourceDragEvent arg0 )
   {
      arg0.getDragSourceContext(  ).getComponent(  ).setBackground( 
         Color.GREEN );
   }

   /**
    * Este método se invoca cuando el ratón ha salido de un posible
    * destino de soltar. Cambiamos el color de fondo de nuestra lista al
    * habitual
    */
   public void dragExit( DragSourceEvent arg0 )
   {
      arg0.getDragSourceContext(  ).getComponent(  ).setBackground( 
         Color.WHITE );
   }

   /**
    * Este método se invoca cuando el ratón se está moviendo sobre el
    * destino soltar
    */
   public void dragOver( DragSourceDragEvent arg0 )
   {
   }

   /**
    * Este método se invoca cuando el usuario ha cambiado el tipo de
    * accion (mover, copiar, enlazar), soltando o pulsando alguna de las
    * teclas SHIFT o CONTROL
    */
   public void dropActionChanged( DragSourceDragEvent arg0 )
   {
   }
}
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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTextField;


public class DropTargetListenerEjemplo
        implements DropTargetListener
{
   //~ Variables de instancia --------------------------------------------------


   protected JTextField jTextField;

   //~ Constructores -----------------------------------------------------------

    /**
    * Crea  un nuevo objeto de la clase DropTargetListenerEjemplo.<br>
    */
   public DropTargetListenerEjemplo( JTextField jTextField )
   {
      this.jTextField = jTextField;
   }

   //~ Metodos -----------------------------------------------------------------

   /**
    * Este método se invoca cuando el ratón ha entrado en el destino de
    * soltar. Cambiamos el color de fondo de nuestra lista a verde
    */
   public void dragEnter( DropTargetDragEvent arg0 )
   {
      this.jTextField.setBackground( Color.GREEN );
   }

   /**
    * Este método se invoca cuando el ratón ha salido en el destino de
    * soltar. Cambiamos el color de fondo de nuestra lista a verde
    */
   public void dragExit( DropTargetEvent arg0 )
   {
      this.jTextField.setBackground( Color.WHITE );
   }

   /**
    * Este método se invoca cuando el ratón se ha movido dentro del
    * destino de soltar. Cambiamos el color de fondo de nuestra lista a verde
    */
   public void dragOver( DropTargetDragEvent evento )
   {
   }

   /**
    * Este método se invoca cuando el usuario ha soltado el boton del
    * raton dentro del destino de arrastre, es decir se ha completado la
    * accion de arrastre
    */
   public void drop( DropTargetDropEvent evento )
   {
      //Se comprueba que el tipo de arrastre ocurrido es de los que acepta este objetivo
      if( ( evento.getDropAction(  ) == DnDConstants.ACTION_MOVE ) ||
         ( evento.getDropAction(  ) == DnDConstants.ACTION_COPY ) ||
         ( evento.getDropAction(  ) == DnDConstants.ACTION_LINK ) )
      {
         //Se acepta el soltar
         evento.acceptDrop( evento.getDropAction(  ) );

         //Se obtiene el Transferable que contiene el objeto arrastrado
         Transferable transferable = evento.getTransferable(  );

         //Se comprueba si el tipo de datos que contiene el transferable se
         //puede extraer como un tipo valido para nosotros, en este caso
         //se trata de extraer un String
         if( transferable.isDataFlavorSupported( DataFlavor.stringFlavor ) )
         {
            try
            {
               //Se obtiene el dato como String
               String texto =
                  (String)transferable.getTransferData( 
                     DataFlavor.stringFlavor );

               this.jTextField.setText( texto );
            }
            catch( Exception excepcion )
            {
               System.out.println( excepcion );
            }
         }

         //Se informa de que la operacion se ha completado
         evento.dropComplete( true );
         evento.getDropTargetContext(  ).getComponent(  )
               .setBackground( Color.WHITE );
      }
   }

   /**
    * Este método se invoca cuando el usuario ha cambiado el tipo de
    * accion (mover, copiar, enlazar), soltando o pulsando alguna de las
    * teclas SHIFT o CONTROL
    */
   public void dropActionChanged( DropTargetDragEvent arg0 )
   {
     
   }
}
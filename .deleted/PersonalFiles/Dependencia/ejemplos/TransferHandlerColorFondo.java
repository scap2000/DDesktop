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




import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.TransferHandler;


/**
 * .<br>
 */
public class TransferHandlerColorFondo
{
   //~ Metodos -----------------------------------------------------------------

   /**
    * Ejemplo de uso de un TransferHandler con la propiedad "background"
    * en un JLabel. JColorChooser mete en el Transferable un Color. El
    * TrasnferHandler de JLabel intenta invocar un
    * setBackground(Color)/getBackground()
    */
   public static void main( String[] argumentos )
   {
      JColorChooser colorChooser = new JColorChooser(  );
      //Se habilita el componente como fuente de arrastre
      colorChooser.setDragEnabled( true );

      JLabel jLabel =
         new JLabel( 
            "Arrastra el color  desde la vista previa del JColoChooser hasta aqui para cambiar el fondo" );

      jLabel.setOpaque( true );

      TransferHandler transferHandler = new TransferHandler( "background" );
      jLabel.setTransferHandler( transferHandler );

      JFrame jFrame = new JFrame(  );
      jFrame.add( 
         colorChooser,
         BorderLayout.NORTH );
      jFrame.add( jLabel );
      jFrame.setVisible( true );
      jFrame.setSize( new Dimension( 
            600,
            600 ) );	    
   }
}
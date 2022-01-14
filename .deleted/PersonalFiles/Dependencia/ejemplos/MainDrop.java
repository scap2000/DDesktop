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
 import java.awt.dnd.DropTarget;

 import javax.swing.JFrame;
 import javax.swing.JTextField;



 public class MainDrop
 {
    //~ Metodos -----------------------------------------------------------------

    /**
     * Metodo main
     */
    public static void main( String[] args )
    {
       //Creamos un campo de texto  destino del arrastre
       JTextField jTextField = new JTextField(  );
       JFrame frame = new JFrame( "Ejemplo de destino de Soltar" );
       frame.getContentPane(  ).add( jTextField );
       frame.setVisible( true );
       frame.setSize( new Dimension( 
	     400,
	     200 ) );
       frame.setLocation( 
	  400,
	  400 );

       //Creamos un listener al que se le informará de como va evolucionando el proceso de arrastre
       DropTargetListenerEjemplo dropTargetListenerEjemplo =
	  new DropTargetListenerEjemplo( jTextField );

       //Se crea el el objetivo del arrastre y se configura
       DropTarget dropTarget =
	  new DropTarget( jTextField,dropTargetListenerEjemplo );
  
       //Se le dice que es un destino activo de arrastre (que puede recibir objetos)
       dropTarget.setActive( true );
  
       //Se establecen los tipos de arrastre que admite
       dropTarget.setDefaultActions( 
	  DnDConstants.ACTION_COPY | DnDConstants.ACTION_MOVE |
	  DnDConstants.ACTION_LINK );
    }
 }
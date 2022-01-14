package org.pentaho.reportdesigner.crm.report.components;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Arc2D;

/**
 * User: Martin
 * Date: 20.01.2007
 * Time: 10:53:46
 */
public class PaletteColorChooser
{
    private PaletteColorChooser()
    {
    }


    public static void main(@NotNull String[] args)
    {
        final JFrame frame = new JFrame();

        JButton button = new JButton("...");
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Color color = showDialog(frame,
                                         "Select a Color",//NON-NLS
                                         Color.RED,
                                         true);
                //noinspection UseOfSystemOutOrSystemErr
                System.out.println("color = " + color);//NON-NLS
            }
        });

        frame.getContentPane().add(button);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 300, 200);
        frame.setVisible(true);
    }


    private static final int OFFSET = 3;

    @NotNull
    private static final Color[] PALETTE_COLORS = new Color[]
            {
                    new Color(0x772200),
                    new Color(0xAA6611),
                    new Color(0x1E8AD3),
                    new Color(0xFFAA00),
                    new Color(0x445500),
                    new Color(0x4A0866),
                    new Color(0x123D82),
                    new Color(0xDDCC88),
                    new Color(0x467AA9),
                    new Color(0xFCCF12),
                    new Color(0xBF0000),
                    new Color(0x9EAA36),
                    new Color(0xEE7733),
            };


    @Nullable
    public static Color showDialog(@NotNull Component parent, @NotNull String title, @Nullable Color initalColor)
    {
        return showDialog(parent, title, initalColor, true);
    }


    @Nullable
    public static Color showDialog(@NotNull Component parent, @NotNull String title, @Nullable Color initalColor, boolean showPalette)
    {
        final JColorChooser chooser = new JColorChooser(initalColor != null ? initalColor : Color.WHITE);
        if (showPalette)
        {
            AbstractColorChooserPanel chooserPanel = new AbstractColorChooserPanel()
            {

                protected void paintComponent(@NotNull Graphics g)
                {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;

                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    double ang = 360. / PALETTE_COLORS.length;
                    for (int i = 0; i < PALETTE_COLORS.length; i++)
                    {
                        double start = (i + OFFSET) * ang;
                        Arc2D.Double arc = new Arc2D.Double(5, 5, 190, 190, start, ang + 0.2, Arc2D.PIE);
                        g2d.setColor(PALETTE_COLORS[i]);
                        g2d.fill(arc);
                    }

                    for (int i = 0; i < PALETTE_COLORS.length; i++)
                    {
                        double start = (i + OFFSET) * ang;
                        Arc2D.Double arc = new Arc2D.Double(5, 5, 190, 190, start, ang + 0.2, Arc2D.PIE);
                        if (PALETTE_COLORS[i].equals(getColorSelectionModel().getSelectedColor()))
                        {
                            g2d.setColor(Color.BLACK);
                            g2d.draw(arc);
                        }
                    }
                }


                public void updateChooser()
                {
                    repaint();
                }


                protected void buildChooser()
                {
                    addMouseMotionListener(new MouseMotionAdapter()
                    {
                        public void mouseDragged(@NotNull MouseEvent e)
                        {
                            selectColor(e);
                        }
                    });

                    addMouseListener(new MouseAdapter()
                    {

                        public void mouseClicked(@NotNull MouseEvent e)
                        {
                            selectColor(e);
                        }


                        public void mousePressed(@NotNull MouseEvent e)
                        {
                            selectColor(e);
                        }


                        public void mouseReleased(@NotNull MouseEvent e)
                        {
                            selectColor(e);
                        }


                        public void mouseDragged(@NotNull MouseEvent e)
                        {
                            selectColor(e);
                        }

                    });
                }


                private void selectColor(@NotNull MouseEvent e)
                {
                    double ang = 360. / PALETTE_COLORS.length;
                    for (int i = 0; i < PALETTE_COLORS.length; i++)
                    {
                        double start = (i + OFFSET) * ang;
                        Arc2D.Double arc = new Arc2D.Double(5, 5, 190, 190, start, ang + 0.2, Arc2D.PIE);
                        if (arc.contains(e.getPoint()))
                        {
                            getColorSelectionModel().setSelectedColor(PALETTE_COLORS[i]);
                            repaint();
                        }
                    }
                }


                @NotNull
                public String getDisplayName()
                {
                    return TranslationManager.getInstance().getTranslation("R", "PentahoColorChooser.PaletteTitle");
                }


                @Nullable
                public Icon getSmallDisplayIcon()
                {
                    return null;
                }


                @Nullable
                public Icon getLargeDisplayIcon()
                {
                    return null;
                }
            };
            chooserPanel.setPreferredSize(new Dimension(200, 200));
            chooser.addChooserPanel(chooserPanel);
        }
        final Color[] result = new Color[1];
        JDialog jDialog = JColorChooser.createDialog(parent, title, true, chooser, new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                result[0] = chooser.getColor();
            }
        }, null);

        WindowUtils.setLocationRelativeTo(jDialog, parent);
        jDialog.setVisible(true);
        return result[0];
    }
}

/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A splashscreen component that is used to show progress of the application loading.
 * <p/>
 * This component is based completely on awt, ssince this prevents the Swing defaults to be
 * initialized and makes the SplashScreen displayed much faster.
 *
 * @author schmm7
 */
public class SplashScreen extends Window
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(SplashScreen.class.getName());

    @NotNull
    private Image image;

    @NotNull
    private String buildString;


    /**
     * Creates a new SplashScreen.
     * <p/>
     * The size of the SplashScreen depends on the imagesize.
     */
    public SplashScreen(@Nullable URL resource)
    {
        super(new Frame());
        MediaTracker mediaTracker = new MediaTracker(this);

        int iw;
        int ih;
        if (resource == null)
        {
            iw = 100;
            ih = 100;

            image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        else
        {
            image = Toolkit.getDefaultToolkit().getImage(resource);
            mediaTracker.addImage(image, 1);
            try
            {
                mediaTracker.waitForAll();
            }
            catch (InterruptedException e)
            {
                //ok
            }

            iw = image.getWidth(this);
            ih = image.getHeight(this);
        }

        setSize(iw, ih);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - iw) / 2, (screen.height - ih) / 2);

        VersionHelper version = VersionHelper.getInstance();
        try
        {
            buildString = MessageFormat.format("{0}.{1}.{2} build {3}", version.getMajorRelease(), version.getMinorRelease(), version.getMilestoneRelease(), version.getBuildNumber());//NON-NLS
        }
        catch (Exception ex)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SplashScreen.SplashScreen ", ex);
            buildString = "";
        }
    }


    /**
     * Overridden to avoid the parent component to paint its background, this prevents flickering.
     *
     * @param g the GraphicsContext to paint on
     */
    public void update(@NotNull Graphics g)
    {
        paint(g);
    }


    /**
     * Paints this SplashScreen.
     * <p/>
     * Draws the image on the top and a descriptive progress at the bottom.
     *
     * @param g the GraphicsContext to paint on
     */
    public void paint(@NotNull Graphics g)
    {
        g.drawImage(image, 0, 0, image.getWidth(this), image.getHeight(this), this);

        g.setColor(Color.BLACK);

        Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(buildString, g);
        g.drawString(buildString, (int) (stringBounds.getX() + 5), getHeight() - (int) (stringBounds.getHeight() + stringBounds.getY()) - 5);
    }

}

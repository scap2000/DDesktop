package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 11.02.2007
 * Time: 13:56:46
 */
@SuppressWarnings({"ALL"})
public class NullTest
{
    @NotNull
    private A a;
    @NotNull
    private D d;


    @NotNull
    private Rectangle2D.Double sr;

    @Nullable
    private Rectangle2D.Double c1;
    @Nullable
    private Rectangle2D.Double c2;
    @Nullable
    private Rectangle2D.Double c3;
    @Nullable
    private Rectangle2D.Double c4;

    @Nullable
    private Rectangle2D.Double e1;
    @Nullable
    private Rectangle2D.Double e2;
    @Nullable
    private Rectangle2D.Double e3;
    @Nullable
    private Rectangle2D.Double e4;


    public void updateSelectionRects()
    {
        ArrayList<C> sel = d.x();
        ArrayList<C> sel2 = new ArrayList<C>();

        for (C c : sel)
        {
            if (c.e(a.g()))
            {
                sel2.add(c);
            }
        }

        if (!sel2.isEmpty())
        {
            sr = null;

            boolean hl = false;
            boolean vl = false;

            //if 1
            if (sel2.size() == 1)
            {
                C c = null;
                if (c instanceof F)
                {
                    F f = (F) c;
                    if (null == null)
                    {
                    }
                    else if (f.h() == LD.A && f.getS().getWidth() == 0)
                    {
                    }
                }
            }

            //if 2
            if (null == null)
            {
            }

            double sf = 0;

            if (!hl && !vl)
            {
                c1 = new Rectangle2D.Double(sr.x - 2 / sf, sr.y - 2 / sf, 5 / sf, 5 / sf);
                c2 = new Rectangle2D.Double(sr.x + sr.width - 2 / sf, sr.y - 2 / sf, 5 / sf, 5 / sf);
                c3 = new Rectangle2D.Double(sr.x + sr.width - 2 / sf, sr.y + sr.height - 2 / sf, 5 / sf, 5 / sf);
                c4 = new Rectangle2D.Double(sr.x - 2 / sf, sr.y + sr.height - 2 / sf, 5 / sf, 5 / sf);
            }

            if (!hl)
            {
                e1 = new Rectangle2D.Double(sr.x + sr.width / 2 - 2 / sf, sr.y - 2 / sf, 5 / sf, 5 / sf);
                e3 = new Rectangle2D.Double(sr.x + sr.width / 2 - 2 / sf, sr.y + sr.height - 2 / sf, 5 / sf, 5 / sf);
            }

            if (!vl)
            {
                e2 = new Rectangle2D.Double(sr.x + sr.width - 2 / sf, sr.y + sr.height / 2 - 2 / sf, 5 / sf, 5 / sf);
                e4 = new Rectangle2D.Double(sr.x - 2 / sf, sr.y + sr.height / 2 - 2 / sf, 5 / sf, 5 / sf);
            }
        }
    }


    private static class A
    {

        public B g()
        {
            return null;
        }
    }

    private static class B
    {

    }

    private static class C
    {

        public boolean e(B b)
        {
            return false;
        }
    }

    private static class F extends C
    {

        public LD h()
        {
            return null;
        }


        public Rectangle2D.Double getS()
        {
            return null;
        }
    }

    private static class D
    {

        public ArrayList<C> x()
        {
            return null;
        }
    }

    private enum LD
    {
        A
    }
}
    
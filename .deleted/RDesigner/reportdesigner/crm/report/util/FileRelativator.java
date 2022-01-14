package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 28.09.2007
 * Time: 07:56:10
 */
public class FileRelativator
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(FileRelativator.class.getName());


    private FileRelativator()
    {
    }


    @NotNull
    private static String getRelativePathFromURL(@Nullable File contextFile, @NotNull URL url)
    {
        try
        {
            if (contextFile == null)
            {
                return url.toURI().toASCIIString();
            }

            String s1 = contextFile.toURI().toASCIIString();
            String s2 = url.toURI().toASCIIString();

            if (s2.startsWith(s1))
            {
                return s2.substring(s1.length());
            }
        }
        catch (URISyntaxException e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "FileRelativator.getRelativeFile ", e);
        }

        return url.toString();
    }


    @NotNull
    private static String getRelativePathFromFile(@Nullable File contextFile, @NotNull File file)
    {
        String fp = file.getAbsolutePath();
        if (contextFile != null)
        {
            String cp = contextFile.getAbsolutePath();
            if (!cp.endsWith(File.separator))
            {
                cp = cp + File.separator;
            }

            if (fp.startsWith(cp))
            {
                return fp.substring(cp.length());
            }
        }
        return fp;
    }


    @NotNull
    private static String getRelativePathFromFile(@Nullable XMLContext xmlContext, @NotNull File file)
    {
        File contextFile = XMLContextKeys.CONTEXT_PATH.getObject(xmlContext);
        return getRelativePathFromFile(contextFile, file);
    }


    @NotNull
    public static String getRelativePathFromFile(@Nullable XMLContext xmlContext, @NotNull String file)
    {
        if (file.trim().length() == 0)
        {
            return "";
        }
        return getRelativePathFromFile(xmlContext, new File(file));
    }


    @NotNull
    public static String getRelativePathFromFile(@Nullable File contextFile, @NotNull String fileString)
    {
        if (fileString.trim().length() == 0)
        {
            return fileString;
        }
        return getRelativePathFromFile(contextFile, new File(fileString));
    }


    @NotNull
    public static String getRelativePathFromURL(@Nullable XMLContext xmlContext, @NotNull String urlString)
    {
        try
        {
            File contextFile = XMLContextKeys.CONTEXT_PATH.getObject(xmlContext);
            URL url = new URL(urlString);
            return getRelativePathFromURL(contextFile, url);
        }
        catch (MalformedURLException e)
        {
            if (LOG.isLoggable(Level.WARNING)) LOG.log(Level.WARNING, "FileRelativator.getRelativePathFromFile ", e);
            return urlString;
        }
    }


    @NotNull
    public static URL getAbsoluteURL(@Nullable XMLContext xmlContext, @NotNull String spec) throws MalformedURLException
    {
        File contextFile = XMLContextKeys.CONTEXT_PATH.getObject(xmlContext);
        if (contextFile == null)
        {
            return new URL(spec);
        }
        else
        {
            return new URL(contextFile.toURI().toURL(), spec);
        }
    }


    @NotNull
    public static String getAbsoluteFile(@Nullable XMLContext xmlContext, @NotNull String relativePath)
    {
        if (relativePath.trim().length() == 0)
        {
            return "";
        }
        File rf = new File(relativePath);
        if (rf.isAbsolute())
        {
            return rf.getAbsolutePath();
        }
        else
        {
            File contextFile = XMLContextKeys.CONTEXT_PATH.getObject(xmlContext);
            return new File(contextFile, relativePath).getAbsolutePath();
        }
    }
}

package org.pentaho.reportdesigner.lib.common.xml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.model.FilePath;
import org.pentaho.reportdesigner.crm.report.util.FileRelativator;

import java.lang.reflect.Field;

/**
 * User: Martin
 * Date: 06.02.2006
 * Time: 13:30:43
 */
public class FilePathConverter implements ObjectConverter
{
    @Nullable
    private XMLContext xmlContext;


    @NotNull
    public FilePath getObject(@NotNull String s)
    {
        try
        {
            //noinspection ConstantConditions
            if (s == null)
            {
                throw new IllegalArgumentException("s must not be null");
            }

            return new FilePath(FileRelativator.getAbsoluteFile(xmlContext, s));
        }
        finally
        {
            xmlContext = null;
        }
    }


    @NotNull
    public String getString(@NotNull Object obj)
    {
        try
        {
            return FileRelativator.getRelativePathFromFile(xmlContext, obj.toString());
        }
        finally
        {
            xmlContext = null;
        }
    }


    public void configure(@Nullable XMLContext xmlContext, @Nullable Field field)
    {
        this.xmlContext = xmlContext;
    }

}

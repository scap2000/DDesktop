package org.pentaho.reportdesigner.crm.report.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: Martin
 * Date: 17.02.2007
 * Time: 16:08:25
 */
public class FilePath
{
    @NotNull
    private String path;


    public FilePath(@NonNls @NotNull String path)
    {
        this.path = path;
    }


    @NotNull
    public String getPath()
    {
        return path;
    }


    @NotNull
    public String toString()
    {
        return path;
    }


    public boolean equals(@Nullable Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilePath filePath = (FilePath) o;

        return path.equals(filePath.path);
    }


    public int hashCode()
    {
        return path.hashCode();
    }
}

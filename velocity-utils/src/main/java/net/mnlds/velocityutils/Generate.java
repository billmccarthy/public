package net.mnlds.velocityutils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @author bill
 */
public class Generate
{
    public static void main( String[] args ) throws Exception
    {
        if( args.length != 3 )
        {
            throw new IllegalArgumentException( "Usage: Generate <contextClass> <templateRoot> <outputRoot>" );
        }
        else
        {
            generateAll( args[0], args[1], args[2] );
        }
    }

    private static void generateAll( String ctxClassName, String templateRoot, String outputRoot ) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException
    {
        CtxIfc ctx = createCtx( ctxClassName );

        File outputDir = createOutputDir( outputRoot, ctx );

        File[] templateFiles = getTemplateFiles( templateRoot );

        for( File templateFile : templateFiles )
        {
            generateSingle( ctx, templateRoot, templateFile, createOutputFile( outputDir, templateFile ) );
        }
    }

    private static void generateSingle( CtxIfc ctx, String templateRoot, File templateFile, File outputFile ) throws FileNotFoundException, UnsupportedEncodingException
    {
        Properties p = new Properties();
        p.setProperty( "file.resource.loader.path", templateRoot );
        VelocityEngine ve = new VelocityEngine( p );
        ve.init();

        Template t = ve.getTemplate( templateFile.getName() );

        VelocityContext vc = createVelocityContext( ctx );

        PrintWriter w = new PrintWriter( outputFile, "UTF-8" );
        t.merge( vc, w );
        w.close();
    }

    private static CtxIfc createCtx( String ctxClassName ) throws IllegalAccessException, InstantiationException, ClassNotFoundException
    {
        Class c = Class.forName( ctxClassName );
        return (CtxIfc) c.newInstance();
    }

    private static File[] getTemplateFiles( String templateRoot )
    {
        File templRoot = new File( templateRoot );
        File[] ret = templRoot.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File file, String s)
            {
                return s.endsWith(".vm");
            }
        });
        return ret == null ? new File[0] : ret;
    }

    private static String getPackageDir( CtxIfc ctx )
    {
        return ctx.getPackage().replace('.', '/');
    }

    private static File createOutputDir( String outputRoot, CtxIfc ctx )
    {
        File outputDir = new File(outputRoot+"/"+getPackageDir(ctx));
        outputDir.mkdirs();
        return outputDir;
    }

    private static File createOutputFile( File outputDir, File templateFile ) throws IOException
    {
        String templateFileName = templateFile.getName();
        String outputFileName = templateFileName.substring(0, templateFileName.lastIndexOf(".vm"));
        File outputFile = new File( outputDir, outputFileName );
        outputFile.createNewFile();
        return outputFile;
    }

    private static VelocityContext createVelocityContext( CtxIfc ctx )
    {
        VelocityContext ret = new VelocityContext();
        ret.put("Utils", Utils.class);
        ret.put("Ctx", ctx);
        return ret;
    }
}

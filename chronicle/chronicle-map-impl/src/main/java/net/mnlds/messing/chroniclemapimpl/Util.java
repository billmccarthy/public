package net.mnlds.messing.chroniclemapimpl;

import net.openhft.lang.io.Bytes;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * @author bill
 */
public class Util
{
    public static void repeat( int times, IntConsumer action )
    {
        IntStream.range( 0, times ).forEach( action );
    }

    public static void printTime( String prefix, Consumer< Void > c )
    {
        System.out.println(prefix + " time: " + time(c) + "ms");
    }

    public static long time( Consumer<Void> c )
    {
        long t0 = System.currentTimeMillis();
        c.accept( null );
        long t1 = System.currentTimeMillis();
        return t1 - t0;
    }

    public static long maxIters( IntConsumer action )
    {
        for( int i = 0; i < 1000000000; i++ )
        {
            try
            {
                action.accept( i );
            }
            catch( Exception e )
            {
                return i - 1;
            }
        }
        throw new RuntimeException("Never ends");
    }
    public static String readString( Bytes in )
    {
        int len = in.readInt();
        char[] chars = new char[ len ];
        for( int i = 0; i < len; i++ )
        {
            chars[i] = in.readChar();
        }
        return new String( chars ).intern();
    }

    public static void writeString( String str, Bytes out )
    {
        char[] chars = str.toCharArray();
        out.writeInt( chars.length );
        for( char c : chars )
        {
            out.writeChar( c );
        }
    }
}

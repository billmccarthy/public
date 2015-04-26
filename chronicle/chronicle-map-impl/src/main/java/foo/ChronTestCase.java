package foo;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import net.openhft.lang.io.Bytes;
import net.openhft.lang.io.serialization.BytesMarshallable;
import net.openhft.lang.model.constraints.MaxSize;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class ChronTestCase
{
    private static final int s_putCount = 1;
    private static final int s_getCount = 1;
    private static final int s_keyCount = 1000;

    public interface RecA
    {
        int getIntAt0();
        void setIntAt0(int v);
        String getStringAt1();
        void setStringAt1(@MaxSize(16) String v);
    }

    public interface RecB
    {
        int getIntAt(int i);
        void setIntAt(/* @MaxSize( 300 ) */ int i, int v);
        String getStringAt(int i);
        void setStringAt(/* @MaxSize( 300 ) */ int i, @MaxSize(16) String v);
    }

    public static class RecAImpl implements RecA, BytesMarshallable
    {
        private int m_int0;
        private String m_string1;

        public RecAImpl(int i)
        {
            m_int0 = i;
            m_string1 = "pfx"+i;
        }

        public int getIntAt0() { return m_int0; }
        public void setIntAt0(int v) { m_int0 = v; }
        public String getStringAt1() { return m_string1; }
        public void setStringAt1(@MaxSize(16) String v) { m_string1 = v; }

        public void readMarshallable(Bytes in) throws IllegalStateException
        {
            m_int0 = in.readInt();
            m_string1 = readStringFrom( in );
        }
        public void writeMarshallable(Bytes out)
        {
            out.writeInt( m_int0 );
            writeStringTo( out, m_string1 );
        }
    }

    public static class RecBImpl implements RecB, BytesMarshallable
    {
        private int m_int0;
        private String m_string1;

        public RecBImpl( int i )
        {
            m_int0 = i;
            m_string1 = "pfx"+i;
        }

        public int getIntAt(int i)
        {
            switch(i)
            {
                case 0: return m_int0;
                default: throw new RuntimeException("Bad idx: "+i);
            }
        }
        public void setIntAt(int i, int v)
        {
            switch(i)
            {
                case 0: m_int0 = v;
                default: throw new RuntimeException("Bad idx: "+i);
            }
        }
        public String getStringAt(int i)
        {
            switch(i)
            {
                case 1: return m_string1;
                default: throw new RuntimeException("Bad idx: "+i);
            }
        }
        public void setStringAt(int i, @MaxSize(16) String v)
        {
            switch(i)
            {
                case 1: m_string1 = v;
                default: throw new RuntimeException("Bad idx: "+i);
            }
        }

        public void readMarshallable(Bytes in) throws IllegalStateException
        {
            m_int0 = in.readInt();
            m_string1 = readStringFrom( in );
        }
        public void writeMarshallable(Bytes out)
        {
            out.writeInt( m_int0 );
            writeStringTo( out, m_string1 );
        }
    }

    public static void main( String[] args ) throws IOException
    {
        doRun("RecA", RecA.class, RecAImpl::new, ChronTestCase::exerciseRecAGetters);
        doRun("RecB", RecB.class, RecBImpl::new, ChronTestCase::exerciseRecBGetters);
    }

    private static <VAL> void doRun( String name, Class<VAL> valueClass, IntFunction<VAL> create, Consumer<VAL> getterExerciser ) throws IOException
    {
        File file = new File(name + ".dat");
        try
        {
            ChronicleMap<Integer, VAL> map = ChronicleMapBuilder.of(Integer.class, valueClass).createPersistedTo(file);

            printTime("Put", ignored -> repeat(s_putCount, i -> map.put(i % s_keyCount, create.apply(i))));
            printTime("Get", ignored -> repeat(s_getCount, i -> getterExerciser.accept(map.get(i % s_keyCount))));
            printTime("GetUsing", ignored -> repeat(s_getCount, i -> getterExerciser.accept(map.getUsing(i % s_keyCount, map.newValueInstance()))));

            map.close();
        }
        finally
        {
            file.delete();
        }
    }

    private static void printTime( String prefix, Consumer< Void > c )
    {
        System.out.println(prefix + " time: " + time(c) + "ms");
    }
    private static void repeat( int times, IntConsumer action )
    {
        IntStream.range(0, times).forEach(action);
    }
    private static long time( Consumer<Void> c )
    {
        long t0 = System.currentTimeMillis();
        c.accept( null );
        long t1 = System.currentTimeMillis();
        return t1 - t0;
    }

    private static void exerciseRecAGetters(RecA rec)
    {
        rec.getIntAt0();
        rec.getStringAt1();
    }

    private static void exerciseRecBGetters(RecB rec)
    {
        rec.getIntAt(0);
        rec.getStringAt(1);
    }

    private static String readStringFrom( Bytes in )
    {
        int len = in.readInt();
        char[] chars = new char[ len ];
        for( int i = 0; i < len; i++ )
        {
            chars[ i ] = in.readChar();
        }
        return new String( chars ).intern();
    }

    private static void writeStringTo( Bytes out, String s )
    {
        char[] chars = s.toCharArray();
        out.writeInt(chars.length);
        for( char c : chars )
        {
            out.writeChar( c );
        }
    }
}

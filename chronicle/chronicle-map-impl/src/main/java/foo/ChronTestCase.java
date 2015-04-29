package foo;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import net.openhft.lang.model.constraints.MaxSize;

import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
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
        int getIntAt1();
        void setIntAt1(int v);
        String getStringAt0();
        void setStringAt0(@MaxSize(16) String v);
        String getStringAt1();
        void setStringAt1(@MaxSize(16) String v);
    }

    public interface RecB
    {
        int getIntAt(int i);
        void setIntAt( @MaxSize( 2 ) int i, int v);
        String getStringAt(int i);
        void setStringAt( @MaxSize( 2 ) int i, @MaxSize(16) String v);
    }

    public static void main( String[] args ) throws IOException
    {
        System.setProperty("dvg.dumpCode", "false");
        doRun(
                "RecA",
                RecA.class,
                (map, i) -> {
                    RecA rec = map.newValueInstance();
                    rec.setIntAt0(0);
                    rec.setIntAt1(1);
                    rec.setStringAt0("pfx" + 0);
                    rec.setStringAt1("pfx" + 1);
                    return rec;
                },
                rec -> {
                    rec.getIntAt0();
                    rec.getIntAt1();
                    rec.getStringAt0();
                    rec.getStringAt1();
                } );
        doRun(
                "RecB",
                RecB.class,
                (map, i) -> {
                    RecB rec = map.newValueInstance();
                    rec.setIntAt( 0, 0 );
                    rec.setIntAt( 1, 1 );
                    rec.setStringAt( 0, "pfx"+0 );
                    rec.setStringAt( 1, "pfx"+1 );
                    return rec;
                },
                rec -> {
                    rec.getIntAt( 0 );
                    rec.getIntAt( 1 );
                    rec.getStringAt( 0 );
                    rec.getStringAt( 1 );
                });
    }

    private static <REC> void doRun( String name, Class<REC> valueClass, BiFunction<ChronicleMap<Integer, REC>, Integer, REC> create, Consumer<REC> getterExerciser ) throws IOException
    {
        File file = new File(name + ".dat");
        try
        {
            ChronicleMap<Integer, REC> map = ChronicleMapBuilder.of(Integer.class, valueClass).createPersistedTo(file);

            printTime("Put", ignored -> repeat(s_putCount, i -> map.put(i % s_keyCount, create.apply(map, i))));
            printTime("Get", ignored -> repeat(s_getCount, i -> getterExerciser.accept(map.get(i % s_keyCount))));
            REC usingRec = create.apply( map, 0 );
            printTime("GetUsing", ignored -> repeat(s_getCount, i -> getterExerciser.accept(map.getUsing(i % s_keyCount, usingRec))));

            map.close();
        }
        finally
        {
            //noinspection ResultOfMethodCallIgnored
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
        c.accept(null);
        long t1 = System.currentTimeMillis();
        return t1 - t0;
    }
}

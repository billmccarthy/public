package net.mnlds.messing.chroniclemapimpl;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ChronMain
{
    private static final int s_iterCount = 100000;
    private static final int s_keyCount = 1000;

    public static void main( String[] args ) throws IOException
    {
        doRun( "IntegerReadRecBytesMarshallable", ReadRec.class, ReadRecImpl::new, m -> new ReadRecImpl(), ReadRec::getI0 );
        doRun("IntegerReadWriteRecBytesMarshallable", ReadWriteRec.class, ReadWriteRecImpl::new, ChronicleMap::newValueInstance, ReadWriteRec::getI0);
    }

    private static <VAL> void doRun( String name, Class<VAL> valueClass, IntFunction<VAL> create, Function<ChronicleMap<Integer, VAL>, VAL> createUsing, Function<VAL, Integer> getFromRec ) throws IOException
    {
        File file = new File("target/"+name+".dat");
        ChronicleMap<Integer, VAL> map = ChronicleMapBuilder.of( Integer.class, valueClass ).createPersistedTo( file );
        System.out.println(valueClass.getSimpleName() + " starting size: " + map.size());

//        System.out.println("Max iters: "+Util.maxIters( i -> map.put( i, create.apply( i ))));
        Util.printTime("Put", ignored -> Util.repeat( s_iterCount, i -> map.put(i % s_keyCount, create.apply(i))));
        Util.printTime("Get", ignored -> Util.repeat( s_iterCount, i -> getFromRec.apply( map.get( i % s_keyCount ))));
        VAL usingRec = createUsing.apply( map );
        Util.printTime("GetUsing", ignored -> Util.repeat( s_iterCount, i -> getFromRec.apply( map.getUsing( i % s_keyCount, usingRec ))));
    }
}

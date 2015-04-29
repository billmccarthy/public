package net.mnlds.messing.chroniclemapimpl;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ChronMain
{
    private static final int s_putCount = 1000000;
    private static final int s_getCount = 10000000;
    private static final int s_keyCount = 1000000;

    public static void main( String[] args ) throws IOException
    {
        System.setProperty("dvg.dumpCode", "false");
        doRun(
                "ReadWriteRec",
                ReadWriteRec.class,
                (map, i) -> {
                    ReadWriteRec rec = map.newValueInstance();
                    rec.setI0(0);
                    rec.setD0(0);
                    rec.setD49(49);
                    rec.setS0("pfx" + 0);
                    rec.setS49("pfx"+49);
                    return rec;
                },
                rec -> {
                    rec.getD0();
                    rec.getD49();
                    rec.getS0();
                    rec.getS49();
                    return rec.getI0();
                });
        doRun(
                "ReadWriteRecPos",
                ReadWriteRecPos.class,
                (map, i) -> {
                    ReadWriteRecPos rec = map.newValueInstance();
                    rec.setIntAt(0, 0);
                    rec.setDoubleAt(0, 0);
                    rec.setDoubleAt(49, 49);
                    rec.setStringAt(0, "pfx" + 0);
                    rec.setStringAt(49, "pfx" + 49);
                    return rec;
                },
                rec -> {
                    rec.getDoubleAt(0);
                    rec.getDoubleAt(49);
                    rec.getStringAt(0);
                    rec.getStringAt(49);
                    return rec.getIntAt(0);
                });
    }

    private static <REC> void doRun( String name, Class<REC> valueClass, BiFunction<ChronicleMap<Integer, REC>, Integer, REC> create, Function<REC, Integer> get ) throws IOException
    {
        File file = new File(name+".dat");
        try
        {
            ChronicleMap<Integer, REC> map = ChronicleMapBuilder.of(Integer.class, valueClass).createPersistedTo(file);
            System.out.println(valueClass.getSimpleName() + " starting size: " + map.size());

            Util.printTime("Put", ignored -> Util.repeat(s_putCount, i -> map.put(i % s_keyCount, create.apply(map, i))));
            Util.printTime("Get", ignored -> Util.repeat(s_getCount, i -> get.apply(map.get(i % s_keyCount))));
            REC usingRec = create.apply(map, 0);
            Util.printTime("GetUsing", ignored -> Util.repeat(s_getCount, i -> get.apply(map.getUsing(i % s_keyCount, usingRec))));
        }
        finally
        {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }
}

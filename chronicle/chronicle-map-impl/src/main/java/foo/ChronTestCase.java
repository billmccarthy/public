package foo;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import net.openhft.lang.model.constraints.MaxSize;

import java.io.File;
import java.io.IOException;

public class ChronTestCase
{
    public interface RecB
    {
        int getIntAt(int i);
        void setIntAt(@MaxSize( 1 ) int i, int v);
        String getStringAt(int i);
        void setStringAt(@MaxSize( 2 ) int i, @MaxSize(16) String v);
    }

    public static void main( String[] args ) throws IOException
    {
        File file = new File("RecB" + ".dat");
        try
        {
            ChronicleMap<Integer, RecB> map = ChronicleMapBuilder.of(Integer.class, RecB.class).createPersistedTo(file);
            RecB rec = map.newValueInstance();
            System.out.println( rec );
            map.close();
        }
        finally
        {
            file.delete();
        }
    }
}

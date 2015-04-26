package net.mnlds.messing.chroniclectx;

import net.mnlds.velocityutils.CtxIfc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author bill
 */
public class Ctx implements CtxIfc
{
    @Override
    public String getPackage()
    {
        return "net.mnlds.messing.chroniclemapimpl";
    }

    public List<Attr> getAttrs()
    {
        return streamToList( Stream.concat(stringCols(), Stream.concat( doubleCols(), intCols() ) ) );
    }

    public static class Attr
    {
        private final String m_type;
        private final String m_name;
        private final String m_annotation;
        private final Function<String, String> m_paramMapper;

        Attr( String type, String name ){ this( type, name, "", p -> p ); }
        Attr( String type, String name, String annotation, Function<String, String> paramMapper)
        {
            m_type = type;
            m_name = name;
            m_annotation = annotation;
            m_paramMapper = paramMapper;
        }

        public String getType() { return m_type; }
        public String getName() { return m_name; }
        public String getAnnotation() { return m_annotation; }
        public String fromInt( String intParam ){ return m_paramMapper.apply( intParam ); }

        @Override
        public String toString()
        {
            return m_name;
        }
    }

    private Stream< Attr > doubleCols()
    {
        return IntStream.range( 0, 100 ).mapToObj( i -> new Attr( "double", "d"+i ) );
    }

    private Stream< Attr > intCols()
    {
        return IntStream.range( 0, 1 ).mapToObj( i -> new Attr( "int", "i"+i ) );
    }

    private Stream< Attr > stringCols()
    {
        return IntStream.range( 0, 100 ).mapToObj( i -> new Attr( "String", "s"+i, "@MaxSize( 16 )", p -> "\"pfx\" + "+p ) );
    }

    private static <T> List<T> streamToList( Stream<T> stream )
    {
        return stream.collect( Collectors.toCollection(ArrayList::new) );
    }

}

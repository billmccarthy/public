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

    public List<Type> getTypes()
    {
        return streamToList( types() );
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

    public static class Type
    {
        private final String m_name;
        private final int m_count;
        private final String m_valueAnnotation;
        Type( String name, int count, String valueAnnotation )
        {
            m_name = name;
            m_count = count;
            m_valueAnnotation = valueAnnotation;
        }

        public String getName()
        {
            return m_name;
        }

        public String getKeyAnnotation()
        {
            return "@MaxSize("+m_count+")";
        }

        public String getValueAnnotation()
        {
            return m_valueAnnotation;
        }

    }

    private Stream< Attr > doubleCols()
    {
        return IntStream.range( 0, 50 ).mapToObj( i -> new Attr( "double", "d"+i ) );
    }

    private Stream< Attr > intCols()
    {
        return IntStream.range( 0, 1 ).mapToObj( i -> new Attr( "int", "i"+i ) );
    }

    private Stream< Attr > stringCols()
    {
        return IntStream.range( 0, 50 ).mapToObj( i -> new Attr( "String", "s"+i, "@MaxSize( 16 )", p -> "\"pfx\" + "+p ) );
    }

    private Stream< Type > types()
    {
        return Stream.of(
                new Type( "boolean", 0, "" ),
                new Type( "byte", 0, "" ),
                new Type( "short", 0, "" ),
                new Type( "int", 1, "" ),
                new Type( "char", 0, "" ),
                new Type( "long", 0, "" ),
                new Type( "float", 0, "" ),
                new Type( "double", 50, "" ),
                new Type( "String", 50, "@MaxSize(16)" )
                );
    }

    private static <T> List<T> streamToList( Stream<T> stream )
    {
        return stream.collect( Collectors.toCollection(ArrayList::new) );
    }

}

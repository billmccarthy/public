package net.mnlds.velocityutils;

/**
 * @author bill
 */
public class Utils
{
    public static String toUpperFirstLetter(String s)
    {
        String head = s.substring( 0, 1 );
        String tail = s.substring( 1 );
        return head.toUpperCase()+tail;
    }
}

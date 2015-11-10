/**
 *
 */
package com.minethurn.logicworld.clausal;

import java.io.IOException;
import java.io.Writer;

/**
 *
 */
public class LogicalWorldPrinter
{
   /**
    * write the clause to the given output
    *
    * @param out
    *           the location to receive the clause
    * @param c
    *           the clause to write
    * @throws IOException
    *            if there is an error
    */
   public static void print(final Writer out, final LogicalClause c) throws IOException
   {
      out.write(c.toString());
   }

   /**
    * write the logical unit to the given output
    *
    * @param out
    *           the output to receive the unit
    * @param u
    *           the unit to write
    * @throws IOException
    *            on error
    */
   public static void print(final Writer out, final LogicalUnit u) throws IOException
   {
      out.write(u.toString());
   }

   /**
    * write the world to the given output location
    *
    * @param out
    *           the output to receive the world
    * @param world
    *           the world to print
    * @throws IOException
    *            if we cannot write the world out
    */
   public static void print(final Writer out, final LogicalWorld world) throws IOException
   {
      for (final LogicalClause c : world)
      {
         out.write(c.toString());
         out.write("\n");
      }
   }
}

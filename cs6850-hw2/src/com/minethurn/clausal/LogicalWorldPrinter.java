/**
 *
 */
package com.minethurn.clausal;

import java.io.IOException;
import java.io.OutputStreamWriter;

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
   public static void print(final OutputStreamWriter out, final LogicalClause c) throws IOException
   {
      boolean first = true;
      out.write("{ ");
      for (final LogicalUnit u : c)
      {
         if (!first)
         {
            out.write(", ");
         }
         first = false;

         out.write(u.toString());
      }
      out.write(" }");
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
   public static void print(final OutputStreamWriter out, final LogicalUnit u) throws IOException
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
   public static void print(final OutputStreamWriter out, final LogicalWorld world) throws IOException
   {
      for (final LogicalClause c : world)
      {
         print(out, c);
      }
   }
}

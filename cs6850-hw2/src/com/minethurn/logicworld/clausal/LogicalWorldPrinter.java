/**
 *
 */
package com.minethurn.logicworld.clausal;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.minethurn.logicworld.processor.DerivationLine;

/**
 *
 */
public class LogicalWorldPrinter
{
   /**
    * The output format for a premise line (with world for gamma)
    */
   private static final String LINE_FORMAT = "%d\t%s\t%s\n";
   /**
    * The output format for a premise line (with world for gamma)
    */
   private static final String DERIVED_LINE_FORMAT = "%d\t%s\t(%d, %d) %s\n";

   /** a line counter */
   private static int lineNumber;

   /**
    * write the world to the given output location
    *
    * @param out
    *           the output to receive the world
    * @param derivation
    *           the derivation to print
    * @throws IOException
    *            if we cannot write the world out
    */
   public static void print(final Writer out, final List<DerivationLine> derivation) throws IOException
   {
      lineNumber = 1;
      for (final DerivationLine line : derivation)
      {
         for (final LogicalClause c : line.getClauses())
         {
            final Integer num = Integer.valueOf(lineNumber++);
            if (c.getType() == LogicalClauseType.DERIVED)
            {
               final Integer left = Integer.valueOf(line.getLeftIndex());
               final Integer right = Integer.valueOf(line.getRightIndex());
               out.write(String.format(DERIVED_LINE_FORMAT, num, c.toString(), left, right,
                     line.getMapping() == null ? "" : line.getMapping().toString()));
            }
            else
            {
               out.write(String.format(LINE_FORMAT, num, c.toString(), c.getType()));
            }
         } // for each clause in derivation line
      } // for each derivation line
   }

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

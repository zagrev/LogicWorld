/**
 *
 */
package com.minethurn.logicworld.processor;

import java.util.ArrayList;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;

/**
 *
 */
public class TautologyRemoval
{
   /**
    * @param world
    */
   public static void removeTautologies(final LogicalWorld world)
   {
      final ArrayList<LogicalClause> clauses = new ArrayList<>();
      clauses.addAll(world.getClauses());

      for (final LogicalClause clause : clauses)
      {
         boolean hasTautology = false;
         final ArrayList<LogicalUnit> units = new ArrayList<>();
         units.addAll(clause.getUnits());

         for (final LogicalUnit u : units)
         {
            if (!hasTautology)
            {
               for (final LogicalUnit u2 : clause)
               {
                  if (u.complement(u2))
                  {
                     hasTautology = true;
                  }
               }
            }
         } // for u in units
         if (hasTautology)
         {
            world.getClauses().remove(clause);
         }
      }
   }
}

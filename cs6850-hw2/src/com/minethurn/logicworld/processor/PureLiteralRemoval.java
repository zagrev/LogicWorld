/**
 *
 */
package com.minethurn.logicworld.processor;

import java.util.ArrayList;
import java.util.HashSet;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;

/**
 *
 */
public class PureLiteralRemoval
{
   /**
    * given a logical world, remove all the clauses that contain pure literals, because pure literals cannot be resolved
    * away.
    *
    * @param world
    *           the world to update
    */
   public static void removePureLiterals(final LogicalWorld world)
   {
      final HashSet<LogicalUnit> units = new HashSet<>();

      // remove duplicates
      for (final LogicalClause clause : world)
      {
         for (final LogicalUnit u : clause)
         {
            units.add(u);
         }
      }

      final ArrayList<LogicalUnit> unitsToRemove = new ArrayList<>();

      // now find a complement for each unit, or add it to the list to remove
      for (final LogicalUnit u : units)
      {
         boolean hasComplement = false;
         for (final LogicalUnit u2 : units)
         {
            if (u.complement(u2))
            {
               hasComplement = true;
               break;
            }
         }
         if (!hasComplement)
         {
            unitsToRemove.add(u);
         }
      }

      // now remove anything that can be removed
      for (final LogicalUnit u : unitsToRemove)
      {
         for (final LogicalClause clause : world.getClauses().toArray(new LogicalClause[world.getClauses().size()]))
         {
            for (final LogicalUnit u2 : clause)
            {
               if (u.equals(u2))
               {
                  world.getClauses().remove(clause);
               }
            }
         }
      }
   }
}

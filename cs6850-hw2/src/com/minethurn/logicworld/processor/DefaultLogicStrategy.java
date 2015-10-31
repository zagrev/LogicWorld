/**
 *
 */
package com.minethurn.logicworld.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;

/**
 *
 */
public class DefaultLogicStrategy implements ILogicStrategy
{
   /** the source clause to manipulate */
   int sourceClause;
   /** the destination clause to manipulate */
   int destClause;

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#finalize(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void finalize(final LogicalWorld world)
   {
      // nothing to do
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#initialize(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld world)
   {
      sourceClause = 0;
      destClause = 0;
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#step(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public List<LogicalClause> step(final LogicalWorld world)
   {
      final ArrayList<LogicalClause> results = new ArrayList<>();

      // if the dest is larger than the world, increment the source and reset the dest
      if (destClause >= world.size())
      {
         sourceClause += 1;
         destClause = 0;
      }
      if (sourceClause < world.size())
      {
         // get which clauses to try to combine
         final LogicalClause src = world.getClause(sourceClause);
         final LogicalClause dest = world.getClause(destClause);

         // this should be the combination of the clauses
         final LogicalClause newClause = new LogicalClause();

         // first we remove duplicates
         final HashSet<LogicalUnit> map = new HashSet<>();
         for (final LogicalUnit u : src)
         {
            map.add(u);
         }
         for (final LogicalUnit u : dest)
         {
            map.add(u);
         }

         // now sort by name
         final LogicalUnit[] units = map.toArray(new LogicalUnit[map.size()]);
         Arrays.sort(units, new Comparator<LogicalUnit>()
         {

            @Override
            public int compare(final LogicalUnit o1, final LogicalUnit o2)
            {
               return o1.getName().compareTo(o2.getName());
            }
         });

         // if we have more than one clause
         if (units.length > 1)
         {
            int i = 0;

            while (i < units.length)
            {
               final LogicalUnit curUnit = units[i];
               boolean same = true;
               boolean complementFound = false;

               // while the current unit is equal or complementary to next, go to next
               while (i < units.length && same)
               {
                  final LogicalUnit nextUnit = units[i + 1];

                  if (curUnit.equals(nextUnit))
                  {
                     // skip current unit, because next unit duplicates it
                  }
                  else if (curUnit.complement(nextUnit))
                  {
                     // drop both curUnit and next unit (and all additional units that are the same or complementary)
                     complementFound = true;
                  }
                  else
                  {
                     same = false;
                  }

                  // if no complements to curUnit found, add curUnit to new clause
                  if (!complementFound)
                  {
                     newClause.add(curUnit);
                  }

                  i += 1;
               }

               // go on to the next unit
               i += 1;
            }
         }

         // there is only one unit so we're done
         else
         {
            newClause.add(units[0]);
         }
         if (!newClause.equals(src) && !newClause.equals(dest))
         {
            results.add(newClause);
         }
      }
      return results;
   }

}

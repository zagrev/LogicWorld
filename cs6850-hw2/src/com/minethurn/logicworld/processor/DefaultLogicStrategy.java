/**
 *
 */
package com.minethurn.logicworld.processor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;

/**
 *
 */
public class DefaultLogicStrategy extends LogicStrategyAdapter
{
   /** the source clause to manipulate */
   int deltaIndex;
   /** the destination clause to manipulate */
   int gammaIndex;

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#initialize(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld delta, final LogicalWorld gamma)
   {
      super.initialize(delta, gamma);
      deltaIndex = 0;
      gammaIndex = 0;
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#step(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public DerivationLine step()
   {
      final DerivationLine line = new DerivationLine();

      // if the dest is larger than the world, increment the source and reset the dest
      if (gammaIndex >= getGamma().size())
      {
         deltaIndex += 1;
         gammaIndex = 0;
      }
      if (deltaIndex < getDelta().size())
      {
         // get which clauses to try to combine
         final LogicalClause src = getDelta().getClause(deltaIndex);
         final LogicalClause dest = getGamma().getClause(gammaIndex);

         if (src.isEmpty() || dest.isEmpty())
         {
            return null;
         }

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

            // compare every unit against every other unit
            while (i < units.length - 1)
            {
               final LogicalUnit curUnit = units[i];

               // while the current unit is equal or complementary to next, go to next
               final LogicalUnit nextUnit = units[i + 1];

               if (curUnit.equals(nextUnit))
               {
                  // skip current unit, because next unit duplicates it. But this should never happen
                  System.out.println("duplicate unit found during clause scan - "
                        + "this should not happen as duplicates should already have been removed");
               }
               else if (curUnit.complement(nextUnit))
               {
                  // drop both curUnit and next unit (and all additional units that are the same or complementary)
                  i += 1;
               }

               // the next unit is not a duplicate nor a complement, so keep this one and move on
               else
               {
                  newClause.add(curUnit);
               }

               // go on to the next unit
               i += 1;
            }
            // now copy the last unit over
            while (i < units.length)
            {
               newClause.add(units[i++]);
            }
         }

         // there is only one unit so we're done
         else
         {
            newClause.add(units[0]);
         }
         if (!newClause.equals(src) && !newClause.equals(dest))
         {
            line.getClauses().add(newClause);
            line.setLeftIndex(deltaIndex);
            line.setRightIndex(gammaIndex);
         }
         gammaIndex += 1;
      }
      return line;
   }

}

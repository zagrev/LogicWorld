/**
 *
 */
package com.minethurn.logicworld.strategy;

import java.util.PriorityQueue;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalMapping;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.LogicalUnitNameComparator;

/**
 *
 */
public class OrderedResolutionStrategy extends LogicStrategyAdapter
{
   /** the sorting queue */
   private final PriorityQueue<LogicalUnit> queue = new PriorityQueue<>(10, new LogicalUnitNameComparator());

   /*
    * (non-Javadoc)
    * @see
    * com.minethurn.logicworld.strategy.LogicStrategyAdapter#combine(com.minethurn.logicworld.clausal.LogicalClause,
    * com.minethurn.logicworld.clausal.LogicalClause, com.minethurn.logicworld.clausal.LogicalMapping)
    */
   @Override
   protected LogicalClause combine(final LogicalClause currentClause, final LogicalClause otherClause,
         final LogicalMapping mapping)
   {
      return sortUnits(super.combine(currentClause, otherClause, mapping));
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.strategy.LogicStrategyAdapter#getNextSecondaryClause()
    */
   @Override
   protected LogicalClause getNextSecondaryClause()
   {
      final LogicalWorld curWorld = getWorld();
      final int size = curWorld.size();
      while (getOtherClauseIndex() < size)
      {
         final LogicalClause primaryClause = curWorld.getClause(getCurrentClauseIndex());
         final LogicalClause testClause = curWorld.getClause(getOtherClauseIndex());

         if (primaryClause.size() > 0 && testClause.size() > 0)
         {
            final LogicalUnit toMap = primaryClause.get(0);
            final LogicalMapping mapping = findMapping(primaryClause, testClause, toMap);

            final LogicalClause mappedPrimary = primaryClause.map(mapping);
            final LogicalClause mappedSecondary = testClause.map(mapping);

            final LogicalUnit mappedPrimaryUnit = mappedPrimary.get(0);
            final LogicalUnit mappedSecondaryUnit = mappedSecondary.get(0);

            if (mappedPrimaryUnit.complement(mappedSecondaryUnit))
            {
               return testClause;
            }
         }
         incrementSecondaryClauseIndex();
      } // while more primary clauses candidates available

      resetSecondaryClauseIndex();
      return null;
   }

   /*
    * (non-Javadoc)
    * @see
    * com.minethurn.logicworld.strategy.LogicStrategyAdapter#initialize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld base, final LogicalWorld refutation)
   {
      final LogicalWorld d = sortUnits(base);
      final LogicalWorld g = sortUnits(refutation);

      super.initialize(d, g);

      base.setClauses(d.getClauses());
      refutation.setClauses(g.getClauses());
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.strategy.LogicStrategyAdapter#resetSecondaryClauseIndex()
    */
   @Override
   protected void resetSecondaryClauseIndex()
   {
      setOtherClauseIndex(0);
   }

   /**
    * @param c
    * @return a new clause with the units sorted
    */
   private LogicalClause sortUnits(final LogicalClause c)
   {
      queue.clear();

      for (final LogicalUnit u : c)
      {
         queue.add(u);
      }
      final LogicalClause newclause = new LogicalClause();
      newclause.setType(c.getType());

      while (!queue.isEmpty())
      {
         newclause.add(queue.remove());
      }
      return newclause;
   }

   /**
    * sort the units in all the clauses in the given world. Return a new world with the units sorted.
    *
    * @param oldWorld
    *           The world to sort
    * @return a new world with the clauses containing sorted terms
    */
   private LogicalWorld sortUnits(final LogicalWorld oldWorld)
   {

      // sort all the terms in each clause before we process them
      final LogicalWorld world = new LogicalWorld();
      for (final LogicalClause c : oldWorld)
      {
         final LogicalClause newclause = sortUnits(c);
         world.add(newclause);
      }
      return world;
   }

}

/**
 *
 */
package com.minethurn.logicworld.strategy;

import java.util.PriorityQueue;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.DerivationLine;
import com.minethurn.logicworld.processor.LogicalUnitNameComparator;

/**
 *
 */
public class OrderedResolutionStrategy extends LogicStrategyAdapter
{
   /** the current clause */
   private int currentClauseIndex;
   /** the other clause */
   private int otherClauseindex;
   /** the sorting queue */
   private final PriorityQueue<LogicalUnit> queue = new PriorityQueue<>(10, new LogicalUnitNameComparator());

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

      // have to fix base and refutation :(
      base.setClauses(d.getClauses());
      refutation.setClauses(g.getClauses());

      getDelta().addAll(getGamma().getClauses());

      currentClauseIndex = 0;
      otherClauseindex = 1;
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

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.strategy.ILogicStrategy#step()
    */
   @Override
   public DerivationLine step()
   {
      final LogicalWorld world = getDelta();
      while (currentClauseIndex < world.size())
      {
         final LogicalClause currentClause = world.getClause(currentClauseIndex);
         if (currentClause.size() > 0)
         {
            final LogicalUnit currentUnit = currentClause.get(0);

            while (otherClauseindex < world.size())
            {
               final LogicalClause otherClause = world.getClause(otherClauseindex);
               otherClauseindex++;

               if (otherClause.size() > 0)
               {
                  final LogicalUnit otherUnit = otherClause.get(0);

                  if (currentUnit.complement(otherUnit))
                  {
                     final LogicalClause newClause = sortUnits(combine(currentClause, otherClause));

                     if (isUnique(world, newClause))
                     {
                        final DerivationLine line = new DerivationLine(newClause, currentClauseIndex,
                              otherClauseindex - 1);
                        world.add(newClause);
                        return line;
                     }
                  }
               }
            }
         }
         currentClauseIndex++;
         otherClauseindex = 0;
      }
      return null;
   }

}

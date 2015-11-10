/**
 *
 */
package com.minethurn.logicworld.strategy;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.DerivationLine;

/**
 * Unit resolution strategy say only use single unit rules to compare
 */
public class UnitResolutionStrategy extends LogicStrategyAdapter
{
   /** the current unit clause we are working with */
   private int currentUnitIndex = 0;
   /** the current other clause we are trying to combine with */
   private int currentOtherIndex = 0;

   /**
    * get the next logical clause that has the complement of the given logical unit in it.
    *
    * @param logicalUnit
    *           the logical unit we are trying to remove
    * @return the next clause we can operate on, or {@code null} if no clause exists
    */
   private LogicalClause getNextClauseWithComplement(final LogicalUnit logicalUnit)
   {
      final LogicalWorld world = getDelta();
      final int worldSize = world.size();

      // if there were any multiple unit clauses we left behind while working down through the single unit items, then
      // we should check if we can work on them now.
      while (currentOtherIndex < currentUnitIndex)
      {
         final LogicalClause c = world.getClause(currentOtherIndex++);
         if (c.size() > 1)
         {
            for (final LogicalUnit u : c)
            {
               if (logicalUnit.complement(u))
               {
                  return c;
               }
            }

         }
      }

      // if there were no multiple unit clauses, then look ahead to any clause with a complementary unit
      currentOtherIndex = Math.max(currentOtherIndex, currentUnitIndex + 1);
      while (currentOtherIndex < worldSize)
      {
         final LogicalClause c = world.getClause(currentOtherIndex++);
         for (final LogicalUnit u : c)
         {
            if (logicalUnit.complement(u))
            {
               return c;
            }
         }
      }

      // if we didn't find anything we could use, then make sure we try the next line
      currentUnitIndex += 1;
      currentOtherIndex = 0;

      return null;
   }

   /**
    * get the next clause in the world that has only a single logical unit in it
    *
    * @return the next clause found or {@code null} if no more clauses available
    */
   private LogicalClause getNextUnitClause()
   {
      final LogicalWorld world = getDelta();
      final int worldSize = world.size();

      // find the next row that has just 1 unit clause in it
      for (; currentUnitIndex < worldSize; currentUnitIndex++)
      {
         final LogicalClause curClause = world.getClause(currentUnitIndex);
         if (curClause.size() == 1)
         {
            return curClause;
         }
      }
      return null;
   }

   /*
    * (non-Javadoc)
    * @see
    * com.minethurn.logicworld.processor.LogicStrategyAdapter#initialize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld base, final LogicalWorld refutation)
   {
      super.initialize(base, refutation);
      // TODO add in pure literal removal and tautology removal
      getDelta().addAll(getGamma().getClauses());
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#step()
    */
   @Override
   public DerivationLine step()
   {
      final LogicalWorld world = getDelta();
      LogicalClause unitClause = null;
      final int worldSize = world.size();

      while (currentUnitIndex < worldSize)
      {
         unitClause = getNextUnitClause();
         if (unitClause != null)
         {
            final LogicalUnit firstUnit = unitClause.get(0);
            final LogicalClause otherClause = getNextClauseWithComplement(firstUnit);

            if (otherClause != null)
            {
               final LogicalClause newClause = new LogicalClause();
               final DerivationLine line = new DerivationLine(newClause, currentUnitIndex, currentOtherIndex - 1);

               // ok, now we can combine the two clauses
               for (final LogicalUnit u : otherClause)
               {
                  if (!firstUnit.equals(u) && !firstUnit.complement(u))
                  {
                     newClause.add(u);
                  }
               }

               if (isUnique(world, newClause))
               {
                  world.add(newClause);
                  return line;
               }

            }
         }
      }
      return null;
   }
}

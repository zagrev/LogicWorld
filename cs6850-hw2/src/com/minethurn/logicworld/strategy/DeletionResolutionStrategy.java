/**
 *
 */
package com.minethurn.logicworld.strategy;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.DerivationLine;

/**
 *
 */
public class DeletionResolutionStrategy extends LogicStrategyAdapter
{
   /** the other clause we are comparing it with */
   private int currentClauseIndex;
   /** the unit we are processing */
   private int currentUnitIndex;
   /** the index of the clause that has a complement to the current unit */
   private int otherClauseIndex;

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#finalize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void finalize(final LogicalWorld delta, final LogicalWorld gamma)
   {
      // TODO Auto-generated method stub

   }

   /**
    * work down the world and find another clause that contains a complementary unit
    *
    * @param world
    *           the world to scan
    * @param currentUnit
    *           the unit to find a complement
    * @return the clause that has a complementary unit, or {@code null} if no matching clause exists
    */
   private LogicalClause findNextComplementaryClause(final LogicalWorld world, final LogicalUnit currentUnit)
   {
      // continue on from where we were before
      while (otherClauseIndex < world.size())
      {
         final LogicalClause otherClause = world.getClause(otherClauseIndex);

         // regardless of outcome, this line is done
         otherClauseIndex++;

         for (int otherUnitIndex = 0; otherUnitIndex < otherClause.size(); otherUnitIndex++)
         {
            final LogicalUnit otherUnit = otherClause.get(otherUnitIndex);
            // check if the unit is a complement other the unit we are checking
            if (currentUnit.complement(otherUnit))
            {
               return otherClause;
            }
         } // for each unit in the other clause
      } // for each other clause

      return null;
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#initialize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld delta, final LogicalWorld gamma)
   {
      super.initialize(delta, gamma);
      // TODO add in pure literal removal and tautology removal
      getDelta().addAll(getGamma().getClauses());

      currentClauseIndex = 0;
      currentUnitIndex = 0;
      otherClauseIndex = 1;
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#step()
    */
   @Override
   public DerivationLine step()
   {
      // this is going to iterate over every unit of every clause and for each following clause
      // it will iterate over the units and find any later clause that has the complement and combine them
      final LogicalWorld world = getDelta();
      while (currentClauseIndex < world.size())
      {
         final LogicalClause currentClause = world.getClause(currentClauseIndex);

         while (currentUnitIndex < currentClause.size())
         {
            final LogicalUnit currentUnit = currentClause.get(currentUnitIndex);
            final LogicalClause otherClause = findNextComplementaryClause(world, currentUnit);

            // if we didn't find a complementary term, then we are done with this unit
            if (otherClause == null)
            {
               currentUnitIndex++;
               otherClauseIndex = currentClauseIndex + 1;
            }

            // if we did find a complementary clause, that other line is done
            else
            {
               final LogicalClause newClause = combine(currentClause, otherClause);
               final DerivationLine newLine = new DerivationLine(newClause, currentClauseIndex, otherClauseIndex);

               // ok, this unit is done
               if (isUnique(world, newClause))
               {
                  world.add(newClause);
                  return newLine;
               }
            } // if other clause found
         } // for each unit in the current clause

         currentClauseIndex++;
         currentUnitIndex = 0;
      } // for each clause in the world
      return null;
   }
}

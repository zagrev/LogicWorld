/**
 *
 */
package com.minethurn.logicworld.strategy;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalClauseType;
import com.minethurn.logicworld.clausal.LogicalFunction;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalVariable;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.DerivationLine;

/**
 * The Input Resotuion strategy requires that any resolvent have at least one parent from the initial (input) database.
 * This strategy cannot resolve any database where there is not at least 1 clause with only a single literal in it.
 */
public class InputResolutionStrategy extends LogicStrategyAdapter
{
   /** the current clause we are checking */
   private int currentClauseIndex;
   /** the other clause we compare to the current clause */
   private int otherClauseIndex;
   /** the current unit we are comparing */
   private int currentUnitIndex;

   /**
    * find the next complementary clause to the given unit
    *
    * @param unitToMatch
    *           the unit for which to find the complement, or a mapped complement
    * @return the clause which represents the complement to the given unit, or {@code null} if there are no matches
    */
   private LogicalClause findNextComplementaryClause(final LogicalUnit unitToMatch)
   {
      final LogicalWorld world = getDelta();
      while (otherClauseIndex < world.size())
      {
         LogicalClause otherClause = world.getClause(otherClauseIndex);
         otherClauseIndex++;

         if (otherClause.size() > 0)
         {
            for (final LogicalUnit otherUnit : otherClause)
            {
               // if we found an exact match, we are done here
               if (unitToMatch.complement(otherUnit))
               {
                  return otherClause;
               }
            }

            // no exact matches, can we find a mapping?
            if (unitToMatch instanceof LogicalFunction
                  || unitToMatch instanceof LogicalVariable && !((LogicalVariable) unitToMatch).isEntity())
            {
               currentMapping = findMapping(world.getClause(currentClauseIndex), otherClause, unitToMatch);
               if (currentMapping != null && currentMapping.size() > 0)
               {
                  otherClause = otherClause.map(currentMapping);
                  for (final LogicalUnit otherUnit : otherClause)
                  {
                     // if we found an exact match, we are done here
                     if (unitToMatch.complement(otherUnit))
                     {
                        return otherClause;
                     }
                  } // for each logical unit in mapped clause
               } // if there is a mapping
            } // if the clause is not empty
         }
      } // while there are more clauses to try

      otherClauseIndex = 0;
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
      super.initialize(base, refutation);

      // combine the base sets
      getDelta().getClauses().addAll(getGamma().getClauses());

      currentClauseIndex = 0;
      otherClauseIndex = 0;
      currentUnitIndex = 0;
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
         if (currentClause.getType() == LogicalClauseType.DELTA)
         {
            while (currentUnitIndex < currentClause.size())
            {
// System.out.println("current clause = " + currentClause);
               final LogicalUnit currentUnit = currentClause.get(currentUnitIndex);
// System.out.println("current unit = " + currentUnit);

               final LogicalClause otherClause = findNextComplementaryClause(currentUnit);
// System.out.println("other = " + otherClause);

               if (otherClause != null)
               {
                  final LogicalClause derived = combine(currentClause, otherClause, currentMapping);
                  final DerivationLine line = new DerivationLine(derived, currentClauseIndex, otherClauseIndex - 1);

                  world.add(derived);
                  return line;
               }
               currentUnitIndex++;
            }
         }
         currentClauseIndex++;
         currentUnitIndex = 0;
      }
      return null;
   }

}

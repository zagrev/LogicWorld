/**
 *
 */
package com.minethurn.logicworld.strategy;

import java.util.HashSet;

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

   /**
    * @param newClause
    *           destination clause for all the units from the pair of clauses we are combining
    * @param currentClause
    *           the current clause
    * @param otherClause
    *           the other clause
    */
   private void combine(final LogicalClause newClause, final LogicalClause currentClause,
         final LogicalClause otherClause)
   {
      final HashSet<LogicalUnit> alreadySeen = new HashSet<>();

      for (final LogicalUnit u : currentClause)
      {
         // if we haven't already processed a duplicate term
         if (!alreadySeen.contains(u))
         {
            // remember we looked at it
            alreadySeen.add(u);
            boolean found = false;

            // now compare it to every unit in the other clause
            for (final LogicalUnit u2 : otherClause)
            {
               // if there's a complement in the other clause, then remember we have seen it but don't copy it to the
               // output clause
               if (u.complement(u2))
               {
                  alreadySeen.add(u2);
                  found = true;
               }
            }

            // if we never saw a complement of this unit, then we can add this unit to the output clause
            if (!found)
            {
               newClause.add(u);
            }
         } // not already seen
      }

      // now copy everything in the second clause that we have not already processed
      for (final LogicalUnit u : otherClause)
      {
         if (!alreadySeen.contains(u))
         {
            newClause.add(u);
         }
      }

   }

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
               final LogicalClause newClause = new LogicalClause();
               final DerivationLine newLine = new DerivationLine(newClause, currentClauseIndex, otherClauseIndex);

               // ok, now we can combine the two clauses
               combine(newClause, currentClause, otherClause);

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

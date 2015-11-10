/**
 *
 */
package com.minethurn.logicworld.strategy;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalWorld;

/**
 *
 */
public abstract class LogicStrategyAdapter implements ILogicStrategy
{
   /** the base clauses of the logical world */
   private LogicalWorld delta;
   /** the refutation clauses of the logical world */
   private LogicalWorld gamma;

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#finalize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void finalize(final LogicalWorld d, final LogicalWorld g)
   {
      // do nothing
   }

   /**
    * @return the delta
    */
   public LogicalWorld getDelta()
   {
      return delta;
   }

   /**
    * @return the gamma
    */
   public LogicalWorld getGamma()
   {
      return gamma;
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#initialize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld base, final LogicalWorld refutation)
   {
      delta = new LogicalWorld();
      delta.addAll(base.getClauses());

      gamma = new LogicalWorld();
      gamma.addAll(refutation.getClauses());
   }

   /**
    * @param delta
    *           the delta to set
    */
   public void setDelta(final LogicalWorld delta)
   {
      this.delta = delta;
   }

   /**
    * @param gamma
    *           the gamma to set
    */
   public void setGamma(final LogicalWorld gamma)
   {
      this.gamma = gamma;
   }

   /**
    * check the world for any clauses that are identical to the given clause
    *
    * @param world
    *           the set of clauses to check
    * @param clauseToCheck
    *           the clause to check
    * @return {@code true} if the clause doesn't already exist in the given world, or {@code false} otherwise
    */
   protected boolean isUnique(final LogicalWorld world, final LogicalClause clauseToCheck)
   {
      // only add units to the derivation if they don't already exist
      for (final LogicalClause derived : world)
      {
         if (clauseToCheck.equals(derived))
         {
            return false;
         }
      }
      return true;
   }
}

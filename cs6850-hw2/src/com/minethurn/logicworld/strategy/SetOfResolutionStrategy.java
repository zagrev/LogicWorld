/**
 *
 */
package com.minethurn.logicworld.strategy;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalClauseType;

/**
 * The set of resolution strategy requires that one of the two clauses to combine comes from the set of resolution
 * (gamma) clauses, or a resolvent of the set of resolution clauses.
 */
public class SetOfResolutionStrategy extends LogicStrategyAdapter
{
   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.strategy.LogicStrategyAdapter#getNextPrimaryClause()
    */
   @Override
   protected LogicalClause getNextPrimaryClause()
   {
      while (getCurrentClauseIndex() < getWorld().size())
      {
         // only return clauses from the Gamma or Derived clauses
         final LogicalClause clause = getWorld().getClause(getCurrentClauseIndex());
         if (clause.getType() != LogicalClauseType.DELTA)
         {
            return clause;
         }

         incrementPrimaryClauseIndex();
      }

      // no reset for primary clause index. We are done at this point.
      return null;
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
}

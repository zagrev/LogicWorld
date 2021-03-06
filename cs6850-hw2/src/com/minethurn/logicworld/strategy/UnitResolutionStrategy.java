/**
 *
 */
package com.minethurn.logicworld.strategy;

import com.minethurn.logicworld.clausal.LogicalClause;

/**
 * Unit resolution strategy say only use single unit rules to compare
 */
public class UnitResolutionStrategy extends LogicStrategyAdapter
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
         final LogicalClause clause = getWorld().getClause(getCurrentClauseIndex());
         if (clause.size() == 1)
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

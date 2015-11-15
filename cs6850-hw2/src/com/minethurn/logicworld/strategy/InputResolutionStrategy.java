/**
 *
 */
package com.minethurn.logicworld.strategy;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalClauseType;

/**
 * The Input Resotuion strategy requires that any resolvent have at least one parent from the initial (input) database.
 * This strategy cannot resolve any database where there is not at least 1 clause with only a single literal in it.
 */
public class InputResolutionStrategy extends LogicStrategyAdapter
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
         if (getWorld().getClause(getCurrentClauseIndex()).getType() == LogicalClauseType.DELTA)
         {
            return getWorld().getClause(getCurrentClauseIndex());
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
      setOtherClauseIndex(getCurrentClauseIndex() + 1);
   }

}

/**
 *
 */
package com.minethurn.logicworld.strategy;

/**
 *
 */
public class DeletionResolutionStrategy extends LogicStrategyAdapter
{

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

/**
 *
 */
package com.minethurn.logicworld.strategy;

/**
 *
 */
public class DefaultLogicStrategy extends LogicStrategyAdapter
{

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

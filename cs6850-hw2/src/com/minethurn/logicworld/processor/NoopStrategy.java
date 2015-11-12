/**
 *
 */
package com.minethurn.logicworld.processor;

import com.minethurn.logicworld.strategy.LogicStrategyAdapter;

/**
 *
 */
public class NoopStrategy extends LogicStrategyAdapter
{

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#step(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public DerivationLine step()
   {
      return null;
   }

}

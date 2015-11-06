/**
 *
 */
package com.minethurn.logicworld.processor;

import java.util.List;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalWorld;

/**
 *
 */
public class NoopStrategy implements ILogicStrategy
{

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#finalize(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void finalize(final LogicalWorld delta, final LogicalWorld gamma)
   {
      // do nothing
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#initialize(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld delta, final LogicalWorld gamma)
   {
      // do nothing
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#step(com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public List<LogicalClause> step()
   {
      return null;
   }

}

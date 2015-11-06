/**
 *
 */
package com.minethurn.logicworld.processor;

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
      this.delta = new LogicalWorld();
      this.delta.addAll(base.getClauses());

      this.gamma = new LogicalWorld();
      this.gamma.addAll(refutation.getClauses());
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
}

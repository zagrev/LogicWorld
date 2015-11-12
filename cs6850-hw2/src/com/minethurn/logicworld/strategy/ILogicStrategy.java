/**
 *
 */
package com.minethurn.logicworld.strategy;

import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.DerivationLine;

/**
 *
 */
public interface ILogicStrategy
{
   /**
    * called by the processor after all processing on the world is complete to allow the strategy to clean up any
    * internal state that it created.
    *
    * @param delta
    *           the base set of clauses
    * @param gamma
    *           the refutation set of clauses
    */
   void finalize(LogicalWorld delta, LogicalWorld gamma);

   /**
    * get the result after processing
    * 
    * @return the world containing all the derived clauses
    */
   LogicalWorld getResult();

   /**
    * Called to allow the strategy to initialize any internal state that it needs
    *
    * @param delta
    *           the base set of clauses that will be processed
    * @param gamma
    *           the refutation set of clauses
    */
   void initialize(LogicalWorld delta, LogicalWorld gamma);

   /**
    * repeatedly called until the method returns {@code null}. Each call should do the minimum amount of processing to
    * generate the next new conclusion.
    *
    * @return any new clauses that were created by this processing step
    */
   DerivationLine step();
}

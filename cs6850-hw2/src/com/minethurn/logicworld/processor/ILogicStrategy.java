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
   List<LogicalClause> step();
}

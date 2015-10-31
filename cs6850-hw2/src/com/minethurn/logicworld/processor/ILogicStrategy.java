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
    * @param world
    */
   void finalize(LogicalWorld world);

   /**
    * Called to allow the strategy to initialize any internal state that it needs
    *
    * @param world
    *           the world that will be processed
    */
   void initialize(LogicalWorld world);

   /**
    * repeatedly called until the method returns {@code null}. Each call should do the minimum amount of processing to
    * generate the next new conclusion.
    *
    * @param world
    *           the world upon which to work
    * @return any new clauses that were created by this processing
    */
   List<LogicalClause> step(LogicalWorld world);
}

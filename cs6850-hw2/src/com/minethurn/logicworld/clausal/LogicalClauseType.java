/**
 * 
 */
package com.minethurn.logicworld.clausal;

/**
 * whether the clause is from the original set of statements (delta), the query statements (gamma), or derived by the
 * strategy
 */
public enum LogicalClauseType
{
   /**
    * from the original data set
    */
   DELTA,
   /**
    * from the query data set
    */
   GAMMA,
   /**
    * derived by the strategy
    */
   DERIVED
}
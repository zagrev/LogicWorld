/**
 *
 */
package com.minethurn.logicworld.processor;

import java.util.ArrayList;

import com.minethurn.logicworld.clausal.LogicalClause;

/**
 * how the line was derived and the order it was derived
 */
public class DerivationLine
{
   /** the clauses derived from this step */
   private ArrayList<LogicalClause> clauses;

   /** the index of a clause used to create this clause */
   private int leftIndex;

   /** the index of the other clause used to create this clause */
   private int rightIndex;

   /**
    * create an empty derived line
    */
   public DerivationLine()
   {
      super();
      this.clauses = new ArrayList<>();
   }

   /**
    * @param clause
    */
   public DerivationLine(final LogicalClause clause)
   {
      this();
      this.clauses.add(clause);
   }

   /**
    * @param newClause
    * @param index1
    * @param index2
    */
   public DerivationLine(final LogicalClause newClause, final int index1, final int index2)
   {
      this(newClause);
      leftIndex = index1;
      rightIndex = index2;
   }

   /**
    * @return the clauses
    */
   public ArrayList<LogicalClause> getClauses()
   {
      return clauses;
   }

   /**
    * @return the leftIndex
    */
   public int getLeftIndex()
   {
      return leftIndex;
   }

   /**
    * @return the rightIndex
    */
   public int getRightIndex()
   {
      return rightIndex;
   }

   /**
    * @param clauses
    *           the clauses to set
    */
   public void setClauses(final ArrayList<LogicalClause> clauses)
   {
      this.clauses = clauses;
   }

   /**
    * @param leftIndex
    *           the leftIndex to set
    */
   public void setLeftIndex(final int leftIndex)
   {
      this.leftIndex = leftIndex;
   }

   /**
    * @param rightIndex
    *           the rightIndex to set
    */
   public void setRightIndex(final int rightIndex)
   {
      this.rightIndex = rightIndex;
   }
}

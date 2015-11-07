/**
 *
 */
package com.minethurn.logicworld.processor;

import java.util.ArrayList;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.processor.LogicProcessor.DerivationLineType;

/**
 * how the line was derived and the order it was derived
 */
public class DerivationLine
{
   /** the clauses derived from this step */
   private ArrayList<LogicalClause> clauses;

   /** where the clauses came from */
   private final DerivationLineType type;

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
      this.type = DerivationLineType.DERIVED;
   }

   /**
    * @param clause
    * @param type
    */
   public DerivationLine(final LogicalClause clause, final DerivationLineType type)
   {
      super();
      this.clauses = new ArrayList<>();

      this.clauses.add(clause);
      this.type = type;
   }

   /**
    * @param newClause
    * @param index1
    * @param index2
    */
   public DerivationLine(final LogicalClause newClause, final int index1, final int index2)
   {
      super();
      clauses = new ArrayList<>();
      clauses.add(newClause);
      leftIndex = index1;
      rightIndex = index2;
      type = DerivationLineType.DERIVED;
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
    * @return the type
    */
   public DerivationLineType getType()
   {
      return type;
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

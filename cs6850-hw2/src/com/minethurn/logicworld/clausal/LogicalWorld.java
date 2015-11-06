/**
 *
 */
package com.minethurn.logicworld.clausal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The logical world for manipulation
 */
public class LogicalWorld implements Iterable<LogicalClause>
{
   /** the truth clauses in this logical world */
   private ArrayList<LogicalClause> clauses = new ArrayList<>();

   /**
    * @param newClause
    * @return the world
    */
   public LogicalWorld add(final LogicalClause newClause)
   {
      clauses.add(newClause);
      return this;
   }

   /**
    * @param newClauses
    */
   public void addAll(final List<LogicalClause> newClauses)
   {
      clauses.addAll(newClauses);
   }

   /**
    * @param index
    * @return the clause at the given index
    */
   public LogicalClause getClause(final int index)
   {
      return clauses.get(index);
   }

   /**
    * @return the clauses
    */
   public ArrayList<LogicalClause> getClauses()
   {
      return clauses;
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Iterable#iterator()
    */
   @Override
   public Iterator<LogicalClause> iterator()
   {
      return clauses.iterator();
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
    * returns the number of clauses in the world
    *
    * @return the number of clauses in the world
    */
   public int size()
   {
      return clauses.size();
   }
}

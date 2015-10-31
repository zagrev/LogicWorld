/**
 *
 */
package com.minethurn.clausal;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The logical world for manipulation
 */
public class LogicalWorld implements Iterable<LogicalClause>
{
   /** the truth clauses in this logical world */
   private final ArrayList<LogicalClause> clauses = new ArrayList<>();

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
    * @param index
    * @return the clause at the given index
    */
   public LogicalClause getClause(final int index)
   {
      return clauses.get(index);
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
    * returns the number of clauses in the world
    *
    * @return the number of clauses in the world
    */
   public int size()
   {
      return clauses.size();
   }
}

/**
 *
 */
package com.minethurn.logicworld.clausal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 */
public class LogicalClause implements Iterable<LogicalUnit>
{
   /** the functions in the logical clause */
   private ArrayList<LogicalUnit> units;
   /** the type of clause - delta, gamma, or derived */
   private LogicalClauseType type;

   /**
    * @param functions
    *           All of the functions that are contained in the current clause
    */
   public LogicalClause(final LogicalUnit... functions)
   {
      units = new ArrayList<>();
      if (functions != null && functions.length > 0)
      {
         for (final LogicalUnit f : functions)
         {
            units.add(f);
         }
      }
      type = LogicalClauseType.DERIVED;
   }

   /**
    * add the function to this clause
    *
    * @param newFunction
    *           the function to add
    */
   public void add(final LogicalUnit newFunction)
   {
      units.add(newFunction);
   }

   /**
    * @param src
    */
   public void addAll(final Collection<LogicalUnit> src)
   {
      units.addAll(src);
   }

   /**
    * @param src
    */
   public void addAll(final LogicalClause src)
   {
      units.addAll(src.units);
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final LogicalClause other = (LogicalClause) obj;
      if (units == null)
      {
         if (other.units != null)
         {
            return false;
         }
      }
      else if (!units.equals(other.units))
      {
         return false;
      }
      return true;
   }

   /**
    * get the logical unit at the given index
    *
    * @param i
    * @return the i'th logical unit
    */
   public LogicalUnit get(final int i)
   {
      return units.get(i);
   }

   /**
    * @return the type
    */
   public LogicalClauseType getType()
   {
      return type;
   }

   /**
    * @return the units
    */
   public ArrayList<LogicalUnit> getUnits()
   {
      return units;
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + (units == null ? 0 : units.hashCode());
      return result;
   }

   /**
    * @return if this clause is empty
    */
   public boolean isEmpty()
   {
      return units == null || units.size() == 0;
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Iterable#iterator()
    */
   @Override
   public Iterator<LogicalUnit> iterator()
   {
      return units.iterator();
   }

   /**
    * apply the mapping to all the units in this clause and return a new clause with the units mapped
    *
    * @param mapping
    * @return a duplicate clause with the units mapped
    */
   public LogicalClause map(final HashMap<String, String> mapping)
   {
      final LogicalClause newClause = new LogicalClause();
      for (final LogicalUnit u : this)
      {
         final LogicalUnit newUnit = u.map(mapping);
         newClause.add(newUnit);
      }
      return newClause;
   }

   /**
    * @param type
    *           the type to set
    */
   public void setType(final LogicalClauseType type)
   {
      this.type = type;
   }

   /**
    * @param units
    *           the units to set
    */
   public void setUnits(final ArrayList<LogicalUnit> units)
   {
      this.units = units;
   }

   /**
    * return the number of functions and variables in this clause
    *
    * @return the size of this clause
    */
   public int size()
   {
      return units.size();
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      final StringBuilder b = new StringBuilder();

      b.append("{ ");
      if (units != null && units.size() > 0)
      {
         for (final LogicalUnit f : units)
         {
            b.append(f.toString());
            b.append(", ");
         }
         b.setLength(b.length() - 2);
         b.append(" }");
      }
      else
      {
         b.append("}");
      }

      return b.toString();
   }
}

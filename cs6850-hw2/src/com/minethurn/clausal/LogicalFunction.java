/**
 *
 */
package com.minethurn.clausal;

import java.util.ArrayList;

/**
 *
 */
public class LogicalFunction extends LogicalUnit
{
   /** the variables, in order */
   private ArrayList<LogicalVariable> variables;

   /**
    * Create a function with the given name that uses the given variables
    *
    * @param name
    *           the name of the new function
    * @param positive
    *           Whether this function is negated, or is positive
    * @param variables
    *           the variables used by this function
    */
   public LogicalFunction(final String name, final boolean positive, final LogicalVariable... variables)
   {
      this.name = name;
      this.variables = new ArrayList<>();
      this.positive = positive;

      if (variables != null && variables.length > 0)
      {
         for (final LogicalVariable v : variables)
         {
            this.variables.add(v);
         }
      }
   }

   /**
    * Create a function with the given name that uses the given variables
    *
    * @param name
    *           the name of the new function
    * @param variables
    *           the variables used by this function
    */
   public LogicalFunction(final String name, final LogicalVariable... variables)
   {
      this(name, true, variables);
   }

   /**
    * @param v
    */
   public void add(final LogicalVariable v)
   {
      variables.add(v);
   }

   @Override
   public boolean complement(final Object obj)
   {
      if (this == obj)
      {
         return false;
      }
      if (!super.complement(obj))
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final LogicalFunction other = (LogicalFunction) obj;
      if (variables == null)
      {
         if (other.variables != null)
         {
            return false;
         }
      }
      else if (!variables.equals(other.variables))
      {
         return false;
      }
      return true;
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
      if (!super.equals(obj))
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final LogicalFunction other = (LogicalFunction) obj;
      if (variables == null)
      {
         if (other.variables != null)
         {
            return false;
         }
      }
      else if (!variables.equals(other.variables))
      {
         return false;
      }
      return true;
   }

   /**
    * get the ith variable
    *
    * @param i
    *           the index of the variable to return
    * @return the variable at index i
    * @throws IndexOutOfBoundsException
    *            if the index is out of range (index < 0 || index >= size())
    */
   public LogicalVariable get(final int i)
   {
      return variables.get(i);
   }

   /**
    * @return the variables
    */
   public ArrayList<LogicalVariable> getVariables()
   {
      return variables;
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + (variables == null ? 0 : variables.hashCode());
      return result;
   }

   /**
    * @param variables
    *           the variables to set
    */
   public void setVariables(final ArrayList<LogicalVariable> variables)
   {
      this.variables = variables;
   }

   /**
    * get the number of variables in this function
    *
    * @return the number of variables in this function
    */
   public int size()

   {
      return variables.size();
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      final StringBuilder b = new StringBuilder();

      if (!isPositive())
      {
         b.append("¬");
      }
      b.append(name);
      b.append("(");
      if (this.variables != null && this.variables.size() > 0)
      {
         for (final LogicalUnit v : variables)
         {
            b.append(v);
            b.append(",");
         }
         b.setLength(b.length() - 1);
      }
      b.append(")");

      return b.toString();
   }

}

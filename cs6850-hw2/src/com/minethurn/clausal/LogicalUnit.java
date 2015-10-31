/**
 *
 */
package com.minethurn.clausal;

/**
 *
 */
public class LogicalUnit
{

   /** the name of this logical variable */
   protected String name;
   /** if this is a positive or negative logical function */
   protected boolean positive;

   /**
    *
    */
   public LogicalUnit()
   {
      super();
      positive = true;
   }

   /**
    * check if this object is the complement of ourself. This means we are equal but with the other value for positive.
    *
    * @param obj
    *           the object to compare
    * @return {@code true} if the other object is the complement of this object, or {@code false} otherwise
    */
   public boolean complement(final Object obj)
   {
      if (this == obj)
      {
         return false;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      final LogicalUnit other = (LogicalUnit) obj;
      if (name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!name.equals(other.name))
      {
         return false;
      }
      if (positive != other.positive)
      {
         return true;
      }
      return false;
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
      final LogicalUnit other = (LogicalUnit) obj;
      if (name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!name.equals(other.name))
      {
         return false;
      }
      if (positive != other.positive)
      {
         return false;
      }
      return true;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
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
      result = prime * result + (name == null ? 0 : name.hashCode());
      result = prime * result + (positive ? 1231 : 1237);
      return result;
   }

   /**
    * @return the positive
    */
   public boolean isPositive()
   {
      return positive;
   }

   /**
    * @param name
    *           the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * @param positive
    *           the positive to set
    */
   public void setPositive(final boolean positive)
   {
      this.positive = positive;
   }

}

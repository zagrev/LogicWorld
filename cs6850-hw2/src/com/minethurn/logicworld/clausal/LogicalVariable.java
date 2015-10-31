/**
 *
 */
package com.minethurn.logicworld.clausal;

/**
 *
 */
public class LogicalVariable extends LogicalUnit
{
   /** if this is an entity or not */
   private boolean entity;

   /**
    * create a new variable
    *
    * @param name
    */
   public LogicalVariable(final String name)
   {
      this.name = name;
   }

   /**
    * @param string
    * @param b
    */
   public LogicalVariable(final String string, final boolean b)
   {
      setEntity(b);
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
      final LogicalVariable other = (LogicalVariable) obj;
      if (entity != other.entity)
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
      final LogicalVariable other = (LogicalVariable) obj;
      if (entity != other.entity)
      {
         return false;
      }
      return true;
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
      result = prime * result + (entity ? 1231 : 1237);
      return result;
   }

   /**
    * @return the entity
    */
   public boolean isEntity()
   {
      return entity;
   }

   /**
    * @param entity
    *           the entity to set
    */
   public void setEntity(final boolean entity)
   {
      this.entity = entity;
   }

   /*
    * (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      if (!isPositive())
      {
         return "¬" + name;
      }
      return name;
   }

}

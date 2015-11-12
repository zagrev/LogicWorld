/**
 *
 */
package com.minethurn.logicworld.processor;

import java.util.Comparator;

import com.minethurn.logicworld.clausal.LogicalUnit;

/**
 *
 */
public final class LogicalUnitNameComparator implements Comparator<LogicalUnit>
{
   @Override
   public int compare(final LogicalUnit o1, final LogicalUnit o2)
   {
      int rc = o1.getName().compareTo(o2.getName());
      if (rc == 0)
      {
         if (o1.isPositive() && !o2.isPositive())
         {
            rc = -1;
         }
         else if (!o1.isPositive() && o2.isPositive())
         {
            rc = 1;
         }
      }
      return rc;
   }
}

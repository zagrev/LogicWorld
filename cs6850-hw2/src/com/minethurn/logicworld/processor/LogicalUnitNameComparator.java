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
      return o1.getName().compareTo(o2.getName());
   }
}

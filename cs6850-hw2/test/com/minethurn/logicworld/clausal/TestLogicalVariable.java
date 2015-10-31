package com.minethurn.logicworld.clausal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalVariable;

/**
 *
 */

/**
 *
 */
public class TestLogicalVariable
{
   /**
    * test that we can create variables and they properly equal each other
    */
   @Test
   public void testComplement()
   {
      final LogicalVariable a = new LogicalVariable("A");
      final LogicalVariable notA = new LogicalVariable("A");
      notA.setPositive(false);
      final LogicalVariable aa = new LogicalVariable("A");

      assertTrue("A should be complementary to ¬A", a.complement(notA));
      assertFalse("A should not be complementary to A", a.complement(a));
      assertFalse("A should not be complementary to AA", a.complement(aa));
   }

   /**
    * test that we can create variables and they properly equal each other
    */
   @Test
   public void testCreateLv()
   {
      final LogicalVariable a = new LogicalVariable("A");
      final LogicalVariable b = new LogicalVariable("B");
      final LogicalVariable aa = new LogicalVariable("A");

      assertEquals("A should be equal to A", a, aa);
      assertNotEquals("A should not be equal to B", a, b);
   }

   /**
    *
    */
   @Test
   public void testEqualsNotEntity()
   {
      final LogicalVariable a = new LogicalVariable("A");
      a.setEntity(false);
      final LogicalVariable aa = new LogicalVariable("A");
      aa.setEntity(true);

      assertNotEquals("A should NOT be equal to A", a, aa);
   }

   /**
    *
    */
   @Test
   public void testEqualsNotPositive()
   {
      final LogicalVariable a = new LogicalVariable("A");
      a.setPositive(true);
      final LogicalVariable aa = new LogicalVariable("A");
      aa.setPositive(false);

      assertNotEquals("A should NOT be equal to A", a, aa);
   }

}

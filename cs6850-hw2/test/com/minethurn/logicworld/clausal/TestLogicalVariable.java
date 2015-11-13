package com.minethurn.logicworld.clausal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

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
   @SuppressWarnings("boxing")
   @Test
   public void testEntity()
   {
      final LogicalVariable x = new LogicalVariable("x");
      final LogicalVariable a = new LogicalVariable("A");

      assertEquals(false, x.isEntity());
      assertEquals(true, a.isEntity());
   }

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testEntityLongNames()
   {
      final LogicalVariable x = new LogicalVariable("x");
      final LogicalVariable a = new LogicalVariable("A");
      final LogicalVariable xc = new LogicalVariable("xCase");
      final LogicalVariable alpha = new LogicalVariable("Alpha");

      assertEquals(false, x.isEntity());
      assertEquals(true, a.isEntity());
      assertEquals(false, xc.isEntity());
      assertEquals(true, alpha.isEntity());
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

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testMapping()
   {
      final HashMap<String, String> mapping = new HashMap<>();
      mapping.put("A", "B");

      final LogicalVariable a = new LogicalVariable("A");
      final LogicalFunction func = new LogicalFunction("Func", a);

      final LogicalUnit u = func.map(mapping);
      assertNotNull(u);
      assertEquals(func.getName(), u.getName());
      assertTrue(u instanceof LogicalFunction);

      final LogicalFunction mappedFunc = (LogicalFunction) u;

      final LogicalVariable b = mappedFunc.get(0);
      assertNotNull(u);
      assertEquals("B", b.getName());
      assertNotEquals("A should NOT be equal to B", a, b);
      assertEquals("Mapped variable should be positive like the original varable", a.isPositive(), b.isPositive());
   }

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testMappingMissing()
   {
      final HashMap<String, String> mapping = new HashMap<>();

      final LogicalVariable a = new LogicalVariable("A");
      final LogicalFunction func = new LogicalFunction("Func", a);

      final LogicalUnit u = func.map(mapping);
      assertNotNull(u);
      assertEquals(func.getName(), u.getName());
      assertTrue(u instanceof LogicalFunction);

      final LogicalFunction mappedFunc = (LogicalFunction) u;

      final LogicalVariable b = mappedFunc.get(0);
      assertNotNull(u);
      assertEquals(a.getName(), b.getName());
      assertEquals("A should be equal to B", a, b);
      assertEquals("Mapped variable should be positive like the original variable", a.isPositive(), b.isPositive());
   }

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testMappingMissingMultiVariable()
   {
      final HashMap<String, String> mapping = new HashMap<>();

      final LogicalVariable x = new LogicalVariable("x");
      final LogicalVariable y = new LogicalVariable("y");
      final LogicalVariable z = new LogicalVariable("z");
      final LogicalFunction func = new LogicalFunction("Func", x, y, z);

      final LogicalUnit u = func.map(mapping);
      assertNotNull(u);
      assertEquals(func.getName(), u.getName());
      assertTrue(u instanceof LogicalFunction);

      final LogicalFunction mappedFunc = (LogicalFunction) u;

      LogicalVariable v = mappedFunc.get(0);
      assertNotNull(u);
      assertEquals("x", v.getName());
      assertEquals("A should be equal to B", x, v);
      assertEquals("Mapped variable should be positive like the original varable", x.isPositive(), v.isPositive());

      v = mappedFunc.get(1);
      assertNotNull(u);
      assertEquals("y", v.getName());
      assertEquals("A should be equal to B", y, v);
      assertEquals("Mapped variable should be positive like the original varable", y.isPositive(), v.isPositive());

      v = mappedFunc.get(2);
      assertNotNull(u);
      assertEquals("z", v.getName());
      assertEquals("A should be equal to B", z, v);
      assertEquals("Mapped variable should be positive like the original varable", z.isPositive(), v.isPositive());
   }

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testMappingMissingNegative()
   {
      final HashMap<String, String> mapping = new HashMap<>();
      mapping.put("A", "B");

      final LogicalVariable a = new LogicalVariable("A");
      final LogicalFunction func = new LogicalFunction("Func", a);
      func.setPositive(false);

      final LogicalUnit u = func.map(mapping);
      assertNotNull(u);
      assertEquals(func.getName(), u.getName());
      assertTrue(u instanceof LogicalFunction);
      assertEquals("Mapped variable should be positive like the original varable", func.isPositive(), u.isPositive());

      final LogicalFunction mappedFunc = (LogicalFunction) u;

      final LogicalVariable b = mappedFunc.get(0);
      assertNotNull(u);
      assertEquals("B", b.getName());
      assertNotEquals("A should NOT be equal to B", a, b);
      assertEquals("Mapped variable should be positive like the original varable", a.isPositive(), b.isPositive());
   }

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testMappingMissingNegativeMultiVariable()
   {
      final HashMap<String, String> mapping = new HashMap<>();

      final LogicalVariable x = new LogicalVariable("x");
      final LogicalVariable y = new LogicalVariable("y");
      final LogicalVariable z = new LogicalVariable("z");
      final LogicalFunction func = new LogicalFunction("Func", x, y, z);
      func.setPositive(false);

      final LogicalUnit u = func.map(mapping);
      assertNotNull(u);
      assertEquals(func.getName(), u.getName());
      assertTrue(u instanceof LogicalFunction);

      final LogicalFunction mappedFunc = (LogicalFunction) u;

      LogicalVariable v = mappedFunc.get(0);
      assertNotNull(u);
      assertEquals("x", v.getName());
      assertEquals("A should be equal to B", x, v);
      assertEquals("Mapped variable should be positive like the original varable", x.isPositive(), v.isPositive());

      v = mappedFunc.get(1);
      assertNotNull(u);
      assertEquals("y", v.getName());
      assertEquals("A should be equal to B", y, v);
      assertEquals("Mapped variable should be positive like the original varable", y.isPositive(), v.isPositive());

      v = mappedFunc.get(2);
      assertNotNull(u);
      assertEquals("z", v.getName());
      assertEquals("A should be equal to B", z, v);
      assertEquals("Mapped variable should be positive like the original varable", z.isPositive(), v.isPositive());
   }

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testMappingMultiVariable()
   {
      final HashMap<String, String> mapping = new HashMap<>();
      mapping.put("x", "A");
      mapping.put("y", "B");
      mapping.put("z", "C");

      final LogicalVariable x = new LogicalVariable("x");
      final LogicalVariable y = new LogicalVariable("y");
      final LogicalVariable z = new LogicalVariable("z");
      final LogicalFunction func = new LogicalFunction("Func", x, y, z);

      final LogicalUnit u = func.map(mapping);
      assertNotNull(u);
      assertEquals(func.getName(), u.getName());
      assertTrue(u instanceof LogicalFunction);

      final LogicalFunction mappedFunc = (LogicalFunction) u;

      LogicalVariable v = mappedFunc.get(0);
      assertNotNull(u);
      assertEquals("A", v.getName());
      assertNotEquals("A should NOT be equal to B", x, v);
      assertEquals("Mapped variable should be positive like the original varable", x.isPositive(), v.isPositive());

      v = mappedFunc.get(1);
      assertNotNull(u);
      assertEquals("B", v.getName());
      assertNotEquals("A should NOT be equal to B", x, v);
      assertEquals("Mapped variable should be positive like the original varable", y.isPositive(), v.isPositive());

      v = mappedFunc.get(2);
      assertNotNull(u);
      assertEquals("C", v.getName());
      assertNotEquals("A should NOT be equal to B", x, v);
      assertEquals("Mapped variable should be positive like the original varable", z.isPositive(), v.isPositive());
   }

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testMappingNegative()
   {
      final HashMap<String, String> mapping = new HashMap<>();
      mapping.put("A", "B");

      final LogicalVariable a = new LogicalVariable("A");
      a.setPositive(false);

      final LogicalUnit u = a.map(mapping);
      assertNotNull(u);
      assertEquals("B", u.getName());
      assertTrue(u instanceof LogicalVariable);

      final LogicalVariable b = (LogicalVariable) u;

      assertNotEquals("A should NOT be equal to B", a, b);
      assertEquals("Mapped variable should be positive like the original varable", a.isPositive(), b.isPositive());
   }

   /**
    *
    */
   @SuppressWarnings("boxing")
   @Test
   public void testMappingNegativeMultiVariable()
   {
      final HashMap<String, String> mapping = new HashMap<>();
      mapping.put("x", "A");
      mapping.put("y", "B");
      mapping.put("z", "C");

      final LogicalVariable x = new LogicalVariable("x");
      final LogicalVariable y = new LogicalVariable("y");
      final LogicalVariable z = new LogicalVariable("z");
      final LogicalFunction func = new LogicalFunction("Func", x, y, z);
      func.setPositive(false);

      final LogicalUnit u = func.map(mapping);
      assertNotNull(u);
      assertEquals(func.getName(), u.getName());
      assertTrue(u instanceof LogicalFunction);
      assertEquals(func.isPositive(), u.isPositive());

      final LogicalFunction mappedFunc = (LogicalFunction) u;

      LogicalVariable v = mappedFunc.get(0);
      assertNotNull(u);
      assertEquals("A", v.getName());
      assertNotEquals("A should NOT be equal to B", x, v);
      assertEquals("Mapped variable should be positive like the original varable", x.isPositive(), v.isPositive());

      v = mappedFunc.get(1);
      assertNotNull(u);
      assertEquals("B", v.getName());
      assertNotEquals("A should NOT be equal to B", x, v);
      assertEquals("Mapped variable should be positive like the original varable", y.isPositive(), v.isPositive());

      v = mappedFunc.get(2);
      assertNotNull(u);
      assertEquals("C", v.getName());
      assertNotEquals("A should NOT be equal to B", x, v);
      assertEquals("Mapped variable should be positive like the original varable", z.isPositive(), v.isPositive());
   }
}

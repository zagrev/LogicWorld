/**
 *
 */
package com.minethurn.clausal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 */
public class TestLogicalFunction
{
   /**
    * test the basic create and display of a function
    */
   @Test
   public void testComplementary()
   {
      final LogicalUnit f1 = new LogicalFunction("Func");
      final LogicalUnit f2 = new LogicalFunction("Func");
      f2.setPositive(false);
      final LogicalUnit f3 = new LogicalFunction("Func");

      assertTrue("F1 should be equal to F2", f1.complement(f2));
      assertFalse("F1 should be equal to F1", f1.complement(f1));
      assertFalse("F1 should be equal to F1", f1.complement(f3));
   }

   /**
    *
    */
   public void testComplementarySameVariable()
   {
      final LogicalVariable var = new LogicalVariable("V");
      final LogicalUnit f1 = new LogicalFunction("Func", var);
      final LogicalUnit f2 = new LogicalFunction("Func", var);
      f2.setPositive(false);

      assertTrue("F1 should be complementary to F2", f1.complement(f2));
      assertFalse("F1 should not be complementary to F1", f1.complement(f1));
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testCreate()
   {
      final LogicalUnit f1 = new LogicalFunction("Func");
      final LogicalUnit f2 = new LogicalFunction("Func", new LogicalVariable("A"));
      final LogicalUnit f3 = new LogicalFunction("Func", new LogicalVariable("A"), new LogicalVariable("B"));

      assertNotNull("F1 should exist", f1);

      assertEquals("F1 should print as \"Func()\"", "Func()", f1.toString());
      assertEquals("F2 should print as \"Func(A)\"", "Func(A)", f2.toString());
      assertEquals("F3 should print as \"Func(A,B)\"", "Func(A,B)", f3.toString());
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testEqualDifferentCountVariable()
   {
      final LogicalUnit f1 = new LogicalFunction("Func", new LogicalVariable("V"));
      final LogicalUnit f2 = new LogicalFunction("Func", new LogicalVariable("V"), new LogicalVariable("W"));

      assertNotEquals("F1 should NOT be equal to F2", f1, f2);
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testEqualDifferentName()
   {
      final LogicalUnit f1 = new LogicalFunction("Func");
      final LogicalUnit f2 = new LogicalFunction("Func2");

      assertNotEquals("F1 should NOT be equal to F2", f1, f2);
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testEqualDifferentVariable()
   {
      final LogicalUnit f1 = new LogicalFunction("Func", new LogicalVariable("V"));
      final LogicalUnit f2 = new LogicalFunction("Func", new LogicalVariable("W"));

      assertNotEquals("F1 should NOT be equal to F2", f1, f2);
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testEqualSameVariable()
   {
      final LogicalVariable var = new LogicalVariable("V");
      final LogicalUnit f1 = new LogicalFunction("Func", var);
      final LogicalUnit f2 = new LogicalFunction("Func", var);

      assertEquals("F1 should be equal to F2", f1, f2);
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testEqualSameVariableNotPositive()
   {
      final LogicalVariable var = new LogicalVariable("V");
      final LogicalUnit f1 = new LogicalFunction("Func", false, var);
      final LogicalUnit f2 = new LogicalFunction("Func", var);

      assertNotEquals("F1 should be equal to F2", f1, f2);
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testEqualSimilarVariable()
   {
      final LogicalUnit f1 = new LogicalFunction("Func", new LogicalVariable("V"));
      final LogicalUnit f2 = new LogicalFunction("Func", new LogicalVariable("V"));

      assertEquals("F1 should be equal to F2", f1, f2);
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testEqualSimilarVariableEntity()
   {
      final LogicalUnit f1 = new LogicalFunction("Func", new LogicalVariable("V"));
      final LogicalUnit f2 = new LogicalFunction("Func", new LogicalVariable("V", false));

      assertNotEquals("F1 should be equal to F2", f1, f2);
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testEqualsNoVariable()
   {
      final LogicalUnit f1 = new LogicalFunction("Func");
      final LogicalUnit f2 = new LogicalFunction("Func");

      assertEquals("F1 should be equal to F2", f1, f2);
   }

   /**
    * test the basic create and display of a function
    */
   @Test
   public void testNegate()
   {
      final LogicalUnit f3 = new LogicalFunction("Func", false, new LogicalVariable("A"), new LogicalVariable("B"));

      assertEquals("F3 should print as \"¬Func(A,B)\"", "¬Func(A,B)", f3.toString());
   }
}

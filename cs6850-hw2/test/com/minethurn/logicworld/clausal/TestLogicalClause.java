/**
 *
 */
package com.minethurn.logicworld.clausal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalFunction;
import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalVariable;
import com.minethurn.logicworld.clausal.LogicalWorld;

/**
 *
 */
public class TestLogicalClause
{
   /**
    * test that we can create and display a logical clause
    */
   @Test
   public void testCreate()
   {
      final LogicalVariable a = new LogicalVariable("A");
      final LogicalVariable b = new LogicalVariable("B");

      final LogicalUnit f1 = new LogicalFunction("Friend", false, a, b);
      final LogicalUnit f2 = new LogicalFunction("Friend", a, b);

      final LogicalClause clause = new LogicalClause(f1, f2);

      assertNotNull("Clause really cannot be null", clause);

      final String expect = "{ ¬Friend(A,B), Friend(A,B) }";
      assertEquals(expect, clause.toString());
      assertFalse("Clause should not be empty", expect.isEmpty());
   }

   /**
    * test that we can create and display a logical clause
    */
   @Test
   public void testCreateEmpty()
   {
      final LogicalClause clause = new LogicalClause();

      assertNotNull("Clause really cannot be null", clause);

      final String expect = "{ }";
      assertEquals(expect, clause.toString());
      assertTrue(clause.isEmpty());
   }

   /**
    * test that we can create and display a logical clause
    *
    * @throws IOException
    *            on error
    */
   @Test
   public void testEqualsDifferentClauseCounts() throws IOException
   {
      final String clause1 = "{ Func(A,B), Func(A,B) }";
      final String clause2 = "{ Func(A,B) } { Func2(A,B) }";
      final LogicalWorld world1 = LogicalParser.parse(clause1);
      final LogicalWorld world2 = LogicalParser.parse(clause2);

      assertEquals(1, world1.size());
      assertEquals(2, world2.size());

      assertNotEquals("Clause1 should equal Clause2", world1.getClause(0), world2.getClause(0));
   }

   /**
    * test that we can create and display a logical clause
    *
    * @throws IOException
    *            on error
    */
   @Test
   public void testEqualsDifferentClauses() throws IOException
   {
      final String clause1 = "{ Func(A,B), Func(A,B) }";
      final String clause2 = "{ Func(A,B), Func2(A,B) }";
      final LogicalWorld world1 = LogicalParser.parse(clause1);
      final LogicalWorld world2 = LogicalParser.parse(clause2);

      assertNotEquals("Clause1 should equal Clause2", world1.getClause(0), world2.getClause(0));
   }

   /**
    * test that we can create and display a logical clause
    *
    * @throws IOException
    *            on error
    */
   @Test
   public void testEqualsDifferentVariable() throws IOException
   {
      final String clause = "{ Func(A,B), Func(A,B) }";
      final LogicalWorld world1 = LogicalParser.parse(clause);
      final LogicalWorld world2 = LogicalParser.parse(clause);

      assertEquals("Clause1 should equal Clause2", world1.getClause(0), world2.getClause(0));
   }

   /**
    * test that we can create and display a logical clause
    */
   @Test
   public void testEqualsEmpty()
   {
      final LogicalClause clause1 = new LogicalClause();
      final LogicalClause clause2 = new LogicalClause();

      assertEquals("Clause1 should equals Clause2", clause1, clause2);
   }

   /**
    * test that we can create and display a logical clause
    */
   @Test
   public void testEqualsOneVariable()
   {
      final LogicalVariable a = new LogicalVariable("A");

      final LogicalUnit f1 = new LogicalFunction("Friend", a);
      final LogicalUnit f2 = new LogicalFunction("Friend", a);

      final LogicalClause clause1 = new LogicalClause(f1, f2);
      final LogicalClause clause2 = new LogicalClause(f1, f2);

      assertEquals("Clause1 should equals Clause2", clause1, clause2);
   }
}

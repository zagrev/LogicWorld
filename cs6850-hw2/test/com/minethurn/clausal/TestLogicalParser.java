/**
 *
 */
package com.minethurn.clausal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

/**
 *
 */
public class TestLogicalParser
{
   /**
    * @throws IOException
    */
   @Test
   public void testParserComments() throws IOException
   {
      final String input1 = "# this is a comment";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have been empty", 0, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserCommentsWithSpecialChars() throws IOException
   {
      final String input1 = "# { dont, parse, this } ";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have been empty", 0, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserEmptyClause() throws IOException
   {
      final String input1 = "{  }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());
      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should be empty", 0, clause.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserFunctionClause() throws IOException
   {
      final String input1 = "{ Func(A) }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should be empty", 1, clause.size());

      final LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalFunction);
      assertEquals("Function should be name \"Func\"", "Func", unit.getName());

      final LogicalVariable var = ((LogicalFunction) unit).get(0);
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"A\"", "A", var.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserFunctionMultipleFunctionsClause() throws IOException
   {
      final String input1 = "{ Func(A), Func2(B) }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 2 functions", 2, clause.size());

      LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalFunction);
      assertEquals("Function should be name \"Func\"", "Func", unit.getName());

      LogicalFunction function = (LogicalFunction) unit;
      assertEquals("Function should have 1 variables", 1, function.size());

      LogicalVariable var = function.get(0);
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"A\"", "A", var.getName());

      unit = clause.get(1);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalFunction);
      assertEquals("Function should be name \"Func2\"", "Func2", unit.getName());

      function = (LogicalFunction) unit;
      assertEquals("Function should have 1 variables", 1, function.size());

      var = function.get(0);
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"B\"", "B", var.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserFunctionMultipleVariablesClause() throws IOException
   {
      final String input1 = "{ Func(A,B,C) }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should be empty", 1, clause.size());

      final LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalFunction);
      assertEquals("Function should be name \"Func\"", "Func", unit.getName());

      final LogicalFunction function = (LogicalFunction) unit;
      assertEquals("Function should have 3 variables", 3, function.size());

      LogicalVariable var = function.get(0);
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"A\"", "A", var.getName());

      var = function.get(1);
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"B\"", "B", var.getName());

      var = function.get(2);
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"C\"", "C", var.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserFunctionNegativeClause() throws IOException
   {
      final String input1 = "{ ¬Func(A) }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should be empty", 1, clause.size());

      final LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalFunction);
      assertEquals("Function should be name \"Func\"", "Func", unit.getName());

      final LogicalFunction f = (LogicalFunction) unit;
      final LogicalVariable var = f.get(0);
      assertFalse("Function should be negative", f.isPositive());
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"A\"", "A", var.getName());
   }

   /**
    * * @throws IOException
    */
   @Test
   public void testParserFunctionNegativeVariableClause() throws IOException
   {
      final String input1 = "{ Func(¬A) }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should be empty", 1, clause.size());

      final LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalFunction);
      assertEquals("Function should be name \"Func\"", "Func", unit.getName());

      final LogicalFunction f = (LogicalFunction) unit;
      assertTrue("Function should not be negative", f.isPositive());

      final LogicalVariable var = f.get(0);
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"A\"", "A", var.getName());
      assertFalse("Variable should be negative", var.isPositive());
   }

   /**
    * * @throws IOException
    */
   @Test
   public void testParserFunctionNegativeVariableNegativeClause() throws IOException
   {
      final String input1 = "{ ¬Func(¬A) }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should be empty", 1, clause.size());

      final LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalFunction);
      assertEquals("Function should be name \"Func\"", "Func", unit.getName());

      final LogicalFunction f = (LogicalFunction) unit;
      assertFalse("Function should be negative", f.isPositive());

      final LogicalVariable var = f.get(0);
      assertNotNull("Variable in function shoud exist", var);
      assertEquals("Variable should be named \"A\"", "A", var.getName());
      assertFalse("Variable should be negative", var.isPositive());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserMultipleVariableClause() throws IOException
   {
      final String input1 = "{ A } { B } { C }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 3 clause", 3, world.size());

      LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 1 variable", 1, clause.size());

      LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"A\"", "A", unit.getName());

      clause = world.getClause(1);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 1 variable", 1, clause.size());

      unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"B\"", "B", unit.getName());

      clause = world.getClause(2);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 1 variable", 1, clause.size());

      unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"C\"", "C", unit.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserMultipleVariableClauseWithNewline() throws IOException
   {
      final String input1 = "{ A }\n{ B }\n{ C }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 3 clause", 3, world.size());

      LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 1 variable", 1, clause.size());

      LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"A\"", "A", unit.getName());

      clause = world.getClause(1);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 1 variable", 1, clause.size());

      unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"B\"", "B", unit.getName());

      clause = world.getClause(2);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 1 variable", 1, clause.size());

      unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"C\"", "C", unit.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserVariableClause() throws IOException
   {
      final String input1 = "{ A }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 1 variable", 1, clause.size());

      final LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"A\"", "A", unit.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserVariableMultipleClause() throws IOException
   {
      final String input1 = "{ A,B }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 2 variables", 2, clause.size());

      LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"A\"", "A", unit.getName());

      unit = clause.get(1);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"B\"", "B", unit.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserVariableMultipleNegativeClause() throws IOException
   {
      final String input1 = "{ A,¬B }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 2 variables", 2, clause.size());

      LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"A\"", "A", unit.getName());

      unit = clause.get(1);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"B\"", "B", unit.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserVariableMultipleNegativeWithSpaceClause() throws IOException
   {
      final String input1 = "{ A, ¬B }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 2 variables", 2, clause.size());

      LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"A\"", "A", unit.getName());

      unit = clause.get(1);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"B\"", "B", unit.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserVariableMultipleWithSpaceClause() throws IOException
   {
      final String input1 = "{ A, B }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 2 variables", 2, clause.size());

      LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"A\"", "A", unit.getName());

      unit = clause.get(1);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"B\"", "B", unit.getName());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testParserVariableNegativeClause() throws IOException
   {
      final String input1 = "{ ¬A }";
      final LogicalWorld world = LogicalParser.parse(new ByteArrayInputStream(input1.getBytes()));

      assertEquals("World should have have 1 clause", 1, world.size());

      final LogicalClause clause = world.getClause(0);
      assertNotNull("Clause 1 should exist", clause);
      assertEquals("Clause should have 1 variable", 1, clause.size());

      final LogicalUnit unit = clause.get(0);
      assertNotNull("Unit should exist", unit);
      assertTrue("Unit should be a variable", unit instanceof LogicalVariable);
      assertEquals("Variable should be name \"A\"", "A", unit.getName());
      assertFalse("Variable should be negative", unit.isPositive());
   }
}

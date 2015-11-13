/**
 *
 */
package com.minethurn.logicworld.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalMapping;
import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;

/**
 *
 */
public class TestLogicStrategyAdapter
{
   /**
    * make sure mapping still works if we send in the entities instead of the variables
    *
    * @throws IOException
    */
   @Test
   public void testFindMappingCombined() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{ ¬Ans(x), Town(x) }{ Ans(A), T(A) }");
      final LogicalClause clause1 = world.getClause(0);
      final LogicalClause clause2 = world.getClause(1);

      final DefaultLogicStrategy strategy = new DefaultLogicStrategy();
      final LogicalUnit toMap = clause1.get(0);
      assertEquals("¬Ans(x)", toMap.toString());

      final LogicalMapping mapping = strategy.findMapping(clause1, clause2, toMap);
      final LogicalClause result = strategy.combine(clause1, clause2, mapping);
      System.out.println("result = " + result.toString());

      assertNotNull(result);
      assertEquals(1, mapping.size());
      assertEquals("Town(A)", result.get(0).toString());
      assertEquals("T(A)", result.get(1).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testFindMappingEmpty() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{}");
      final LogicalClause clause = world.getClause(0);

      final DefaultLogicStrategy strategy = new DefaultLogicStrategy();
      final LogicalMapping mapping = strategy.findMapping(clause, clause, null);

      assertNotNull(mapping);
      assertEquals(0, mapping.size());
   }

   /**
    * make sure mapping works when we send in the variables instead of the entities
    *
    * @throws IOException
    */
   @Test
   public void testFindMappingSimple() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{Func(x,y)}{Func(A,B)}");
      final LogicalClause clause1 = world.getClause(0);
      final LogicalClause clause2 = world.getClause(1);

      final DefaultLogicStrategy strategy = new DefaultLogicStrategy();
      final LogicalUnit toMap = clause1.get(0);
      assertEquals("Func(x,y)", toMap.toString());

      final LogicalMapping mapping = strategy.findMapping(clause1, clause2, toMap);

      assertNotNull(mapping);
      assertEquals(2, mapping.size());
      assertEquals("A", mapping.get("x"));
      assertEquals("B", mapping.get("y"));
   }

   /**
    * make sure mapping still works if we send in the entities instead of the variables
    *
    * @throws IOException
    */
   @Test
   public void testFindMappingSimpleEntity() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{Func(x,y)}{Func(A,B)}");
      final LogicalClause clause1 = world.getClause(0);
      final LogicalClause clause2 = world.getClause(1);

      final DefaultLogicStrategy strategy = new DefaultLogicStrategy();
      final LogicalUnit toMap = clause2.get(0);
      assertEquals("Func(A,B)", toMap.toString());

      final LogicalMapping mapping = strategy.findMapping(clause1, clause2, toMap);

      assertNotNull(mapping);
      assertEquals(2, mapping.size());
      assertEquals("A", mapping.get("x"));
      assertEquals("B", mapping.get("y"));
   }

   /**
    * make sure mapping still works if we send in the entities instead of the variables
    *
    * @throws IOException
    */
   @Test
   public void testFindMappingUnbalanced() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{Func(x,y,z)}{Func(A,B)}");
      final LogicalClause clause1 = world.getClause(0);
      final LogicalClause clause2 = world.getClause(1);

      final DefaultLogicStrategy strategy = new DefaultLogicStrategy();
      final LogicalUnit toMap = clause1.get(0);
      assertEquals("Func(x,y,z)", toMap.toString());

      final LogicalMapping mapping = strategy.findMapping(clause1, clause2, toMap);

      assertNotNull(mapping);
      assertEquals(2, mapping.size());
      assertEquals("A", mapping.get("x"));
      assertEquals("B", mapping.get("y"));
      assertNull(mapping.get("z"));
   }
}

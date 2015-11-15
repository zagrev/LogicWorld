/**
 *
 */
package com.minethurn.logicworld.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalClauseType;
import com.minethurn.logicworld.clausal.LogicalMapping;
import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.clausal.LogicalWorldPrinter;
import com.minethurn.logicworld.processor.LogicProcessor;

/**
 *
 */
public class TestLogicStrategyAdapter
{
   /** the logger */
   private final Logger logger = LogManager.getLogger(getClass());

   /**
    * make sure mapping still works if we send in the entities instead of the variables
    *
    * @throws IOException
    */
   @Test
   public void testFindMappingCombined() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{ 括ns(x), Town(x) }{ Ans(A), T(A) }");
      final LogicalClause clause1 = world.getClause(0);
      final LogicalClause clause2 = world.getClause(1);

      final LogicStrategyAdapter strategy = new DefaultLogicStrategy();
      final LogicalUnit toMap = clause1.get(0);
      assertEquals("括ns(x)", toMap.toString());

      final LogicalMapping mapping = strategy.findMapping(clause1, clause2, toMap);
      final LogicalClause result = strategy.combine(clause1, clause2, mapping);
      logger.debug("result = " + result.toString());

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

      final LogicStrategyAdapter strategy = new DefaultLogicStrategy();
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

      final LogicStrategyAdapter strategy = new DefaultLogicStrategy();
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

      final LogicStrategyAdapter strategy = new DefaultLogicStrategy();
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

      final LogicStrategyAdapter strategy = new DefaultLogicStrategy();
      final LogicalUnit toMap = clause1.get(0);
      assertEquals("Func(x,y,z)", toMap.toString());

      final LogicalMapping mapping = strategy.findMapping(clause1, clause2, toMap);

      assertNotNull(mapping);
      assertEquals(2, mapping.size());
      assertEquals("A", mapping.get("x"));
      assertEquals("B", mapping.get("y"));
      assertNull(mapping.get("z"));
   }

   /**
    * @throws IOException
    */
   @Test
   public void testVariableOnlyMapping() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ 政unc(x,y), x }{ C, D }", LogicalClauseType.DELTA);
      final LogicalWorld g = LogicalParser.parse("{ 挑 }{ 拾 }{ Func(x,y), 流 }{ Func(x,y), 洪 }",
            LogicalClauseType.GAMMA);

      final LogicProcessor processor = new LogicProcessor(d, g);

      final LogicStrategyAdapter strategy = new DefaultLogicStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      LogicalWorldPrinter.print(new OutputStreamWriter(System.out), processor.getDerivation());
      logger.debug("result = " + result);

      int i = 0;
      // delta
      assertEquals("{ 政unc(x,y), x }", result.getClause(i++).toString());
      assertEquals("{ C, D }", result.getClause(i++).toString());

      // gamma
      assertEquals("{ 挑 }", result.getClause(i++).toString());
      assertEquals("{ 拾 }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y), 流 }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y), 洪 }", result.getClause(i++).toString());

      // derived
      assertEquals("{ x, 流 }", result.getClause(i++).toString());
      assertEquals("{ }", result.getClause(i++).toString());
      assertEquals("{ 政unc(D,y) }", result.getClause(i++).toString());
      assertEquals("{ 政unc(B,y) }", result.getClause(i++).toString());
      assertEquals("{ D, Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ C }", result.getClause(i++).toString());
      assertEquals("{ C, Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ 流 }", result.getClause(i++).toString());

      assertEquals("{ Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ 流, x }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), C }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), D }", result.getClause(i++).toString());
      assertEquals("{ Func(D,C), Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,y) }", result.getClause(i++).toString());
      assertEquals("{ 流, Func(x,y) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), Func(D,C) }", result.getClause(i++).toString());
      assertEquals("{ x }", result.getClause(i++).toString());
      assertEquals("{ Func(x,C) }", result.getClause(i++).toString());
      assertEquals("{ D }", result.getClause(i++).toString());
      assertEquals("{ D, C }", result.getClause(i++).toString());
      assertEquals("{ Func(D,C), D }", result.getClause(i++).toString());
      assertEquals("{ Func(x,D) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,C), C }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,y) }", result.getClause(i++).toString());
      assertEquals("{ C, Func(D,C) }", result.getClause(i++).toString());
      assertEquals("{ D, Func(D,C) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,C) }", result.getClause(i++).toString());

      assertEquals(34, result.size());
   }

}

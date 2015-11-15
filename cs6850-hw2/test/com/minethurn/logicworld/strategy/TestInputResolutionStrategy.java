/**
 *
 */
package com.minethurn.logicworld.strategy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalClauseType;
import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.LogicProcessor;

/**
 * test the input resolution strategy
 */
public class TestInputResolutionStrategy
{
   /**
    *
    */
   @Test
   public void testEmpty()
   {
      final LogicalWorld w = new LogicalWorld();
      final LogicProcessor processor = new LogicProcessor(w, w);

      final InputResolutionStrategy strategy = new InputResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();

      assertEquals(0, result.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testMutipleMapping() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ 政unc(A,C), B }{ C, D }", LogicalClauseType.DELTA);
      final LogicalWorld g = LogicalParser.parse("{ 挑 }{ 拾 }{ Func(x,y), 流 }{ Func(x,y), 洪 }",
            LogicalClauseType.GAMMA);

      final LogicProcessor processor = new LogicProcessor(d, g);

      final InputResolutionStrategy strategy = new InputResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      System.out.println("result = " + result);

// assertEquals(7, result.size());
      int i = 0;
      assertEquals("{ 政unc(A,C), B }", result.getClause(i++).toString());
      assertEquals("{ C, D }", result.getClause(i++).toString());
      assertEquals("{ 挑 }", result.getClause(i++).toString());
      assertEquals("{ 拾 }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y), 流 }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y), 洪 }", result.getClause(i++).toString());

      assertEquals("{ B, 拴 }", result.getClause(i++).toString());
      assertEquals("{ B, 括 }", result.getClause(i++).toString());
      assertEquals("{ 政unc(A,C) }", result.getClause(i++).toString());
      assertEquals("{ D, B }", result.getClause(i++).toString());
      assertEquals("{ C }", result.getClause(i++).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testSimple() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ A, B }{ C, D }");
      final LogicalWorld g = LogicalParser.parse("{ E }");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final InputResolutionStrategy strategy = new InputResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();

      assertEquals(3, result.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testSimpleCombine() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ A, B }{ C, D }", LogicalClauseType.DELTA);
      final LogicalWorld g = LogicalParser.parse("{ 挑 }{ 拾 }{ D, B }", LogicalClauseType.GAMMA);

      final LogicProcessor processor = new LogicProcessor(d, g);

      final InputResolutionStrategy strategy = new InputResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      System.out.println("result = " + result);

      assertEquals(7, result.size());
      assertEquals("{ A, B }", result.getClause(0).toString());
      assertEquals("{ C, D }", result.getClause(1).toString());
      assertEquals("{ 挑 }", result.getClause(2).toString());
      assertEquals("{ 拾 }", result.getClause(3).toString());
      assertEquals("{ D, B }", result.getClause(4).toString());

      assertEquals("{ A }", result.getClause(5).toString());
      assertEquals("{ C }", result.getClause(6).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testSimpleMapping() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ 政unc(A,C), B }{ C, D }", LogicalClauseType.DELTA);
      final LogicalWorld g = LogicalParser.parse("{ 挑 }{ 拾 }{ Func(x,y), 流 }", LogicalClauseType.GAMMA);

      final LogicProcessor processor = new LogicProcessor(d, g);

      final InputResolutionStrategy strategy = new InputResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      System.out.println("result = " + result);

// assertEquals(7, result.size());
      int i = 0;
      assertEquals("{ 政unc(A,C), B }", result.getClause(i++).toString());
      assertEquals("{ C, D }", result.getClause(i++).toString());
      assertEquals("{ 挑 }", result.getClause(i++).toString());
      assertEquals("{ 拾 }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y), 流 }", result.getClause(i++).toString());

      assertEquals("{ B, 拴 }", result.getClause(i++).toString());
      assertEquals("{ 政unc(A,C) }", result.getClause(i++).toString());
      assertEquals("{ D, B }", result.getClause(i++).toString());
      assertEquals("{ C }", result.getClause(i++).toString());
   }
}

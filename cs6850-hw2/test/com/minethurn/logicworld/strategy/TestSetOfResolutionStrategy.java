/**
 *
 */
package com.minethurn.logicworld.strategy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalClauseType;
import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.LogicProcessor;

/**
 *
 */
public class TestSetOfResolutionStrategy
{
   /** the logger */
   private final Logger logger = LogManager.getLogger(getClass());

   /**
   *
   */
   @Test
   public void testEmpty()
   {
      final LogicalWorld w = new LogicalWorld();
      final LogicProcessor processor = new LogicProcessor(w, w);

      final SetOfResolutionStrategy strategy = new SetOfResolutionStrategy();
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

      final SetOfResolutionStrategy strategy = new SetOfResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      logger.debug("testMutipleMapping = " + result);

      int i = 0;
      assertEquals("{ 政unc(A,C), B }", result.getClause(i++).toString());
      assertEquals("{ C, D }", result.getClause(i++).toString());
      assertEquals("{ 挑 }", result.getClause(i++).toString());
      assertEquals("{ 拾 }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y), 流 }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y), 洪 }", result.getClause(i++).toString());
      assertEquals("{ C }", result.getClause(i++).toString());
      assertEquals("{ 政unc(A,C) }", result.getClause(i++).toString());
      assertEquals("{ 拴, B }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), C }", result.getClause(i++).toString());
      assertEquals("{ Func(C,B), 拴 }", result.getClause(i++).toString());
      assertEquals("{ 括, B }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), D }", result.getClause(i++).toString());
      assertEquals("{ Func(C,y) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ B }", result.getClause(i++).toString());
      assertEquals("{ Func(C,B) }", result.getClause(i++).toString());
      assertEquals("{ 拴 }", result.getClause(i++).toString());
      assertEquals("{ 括 }", result.getClause(i++).toString());
      assertEquals("{ B, D }", result.getClause(i++).toString());
      assertEquals("{ B, Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ 拴, Func(C,B) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), B }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), Func(C,B) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,B), D }", result.getClause(i++).toString());
      assertEquals("{ Func(C,B), Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ 括, Func(A,B) }", result.getClause(i++).toString());
      assertEquals("{ }", result.getClause(i++).toString());
      assertEquals("{ Func(B,y) }", result.getClause(i++).toString());
      assertEquals("{ D }", result.getClause(i++).toString());
      assertEquals("{ D, Func(B,D) }", result.getClause(i++).toString());
      assertEquals("{ B, Func(B,D) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), Func(B,C) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,y) }", result.getClause(i++).toString());
      assertEquals("{ Func(B,D) }", result.getClause(i++).toString());
      assertEquals("{ Func(B,D), Func(D,B) }", result.getClause(i++).toString());

      assertEquals(36, result.size());
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

      final SetOfResolutionStrategy strategy = new SetOfResolutionStrategy();
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

      final SetOfResolutionStrategy strategy = new SetOfResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      logger.debug("result = " + result);

      int i = 0;

      // the delta clauses
      assertEquals("{ A, B }", result.getClause(i++).toString()); // 1
      assertEquals("{ C, D }", result.getClause(i++).toString()); // 2

      // the gamma clauses (set of resolution)
      assertEquals("{ 挑 }", result.getClause(i++).toString()); // 3
      assertEquals("{ 拾 }", result.getClause(i++).toString()); // 4
      assertEquals("{ D, B }", result.getClause(i++).toString()); // 5

      // the derived clauses
      assertEquals("{ C }", result.getClause(i++).toString()); // 6 - 3 ,2
      assertEquals("{ B }", result.getClause(i++).toString()); // 7 - 3, 5
      assertEquals("{ A }", result.getClause(i++).toString()); // 8 - 4,1
      assertEquals("{ D }", result.getClause(i++).toString()); // 9 - 4, 5
      assertEquals("{ }", result.getClause(i++).toString()); // 10 - 4, 7

      assertEquals(10, result.size());
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

      final SetOfResolutionStrategy strategy = new SetOfResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      logger.debug("result = " + result);

      int i = 0;
      // delta
      assertEquals("{ 政unc(A,C), B }", result.getClause(i++).toString());
      assertEquals("{ C, D }", result.getClause(i++).toString());

      // gamma
      assertEquals("{ 挑 }", result.getClause(i++).toString());
      assertEquals("{ 拾 }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y), 流 }", result.getClause(i++).toString());

      // derived
      assertEquals("{ C }", result.getClause(i++).toString());
      assertEquals("{ 政unc(A,C) }", result.getClause(i++).toString());
      assertEquals("{ 拴, B }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), C }", result.getClause(i++).toString());
      assertEquals("{ Func(C,B), 拴 }", result.getClause(i++).toString());
      assertEquals("{ B }", result.getClause(i++).toString());
      assertEquals("{ Func(C,B) }", result.getClause(i++).toString());
      assertEquals("{ 拴 }", result.getClause(i++).toString());
      assertEquals("{ B, D }", result.getClause(i++).toString());
      assertEquals("{ B, Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ 拴, Func(C,B) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), B }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), Func(C,B) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,B), D }", result.getClause(i++).toString());
      assertEquals("{ Func(C,B), Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ }", result.getClause(i++).toString());
      assertEquals("{ D }", result.getClause(i++).toString());
      assertEquals("{ B, Func(B,D) }", result.getClause(i++).toString());
      assertEquals("{ Func(B,D) }", result.getClause(i++).toString());

      assertEquals(25, result.size());
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

      final SetOfResolutionStrategy strategy = new SetOfResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      logger.debug("testVariableOnlyMapping = " + result);

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
      assertEquals("{ 政unc(D,y) }", result.getClause(i++).toString());
      assertEquals("{ C }", result.getClause(i++).toString());
      assertEquals("{ 政unc(B,y) }", result.getClause(i++).toString());
      assertEquals("{ 流, x }", result.getClause(i++).toString());
      assertEquals("{ 流 }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), C }", result.getClause(i++).toString());
      assertEquals("{ }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), D }", result.getClause(i++).toString());
      assertEquals("{ Func(C,y) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ x }", result.getClause(i++).toString());
      assertEquals("{ D }", result.getClause(i++).toString());
      assertEquals("{ D, Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ C, Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ 流, Func(x,y) }", result.getClause(i++).toString());
      assertEquals("{ Func(C,D), Func(D,C) }", result.getClause(i++).toString());
      assertEquals("{ D, C }", result.getClause(i++).toString());
      assertEquals("{ Func(x,y) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,y) }", result.getClause(i++).toString());
      assertEquals("{ Func(x,D) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,C), D }", result.getClause(i++).toString());
      assertEquals("{ Func(x,C) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,C), Func(C,D) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,C), C }", result.getClause(i++).toString());
      assertEquals("{ C, Func(D,C) }", result.getClause(i++).toString());
      assertEquals("{ D, Func(D,C) }", result.getClause(i++).toString());
      assertEquals("{ Func(D,C) }", result.getClause(i++).toString());

      assertEquals(33, result.size());
   }
}

/**
 *
 */
package com.minethurn.logicworld.strategy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.LogicProcessor;

/**
 *
 */
public class TestUnitResolutionStrategy
{
   /** the logger */
   private final Logger logger = LogManager.getLogger(getClass());

   /**
    * @throws IOException
    */
   @Test
   public void testDoubleUnit() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{A}");
      final LogicalWorld x = LogicalParser.parse("{¬A}");
      final LogicProcessor processor = new LogicProcessor(w, x);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(3, processor.getResult().size());
      assertEquals("{ A }{ ¬A }{ }", processor.getResult().toString());
   }

   /**
    *
    */
   @Test
   public void testEmpty()
   {
      final LogicalWorld w = new LogicalWorld();
      final LogicProcessor processor = new LogicProcessor(w, w);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(0, processor.getResult().size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testEmpty2() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{}");
      final LogicProcessor processor = new LogicProcessor(w, w);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(2, processor.getResult().size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testLeave2Unit1() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{A,P}{¬B}{P}{Q}");
      final LogicalWorld g = LogicalParser.parse("{¬A}{B, ¬P}");
      final LogicProcessor processor = new LogicProcessor(d, g);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();
      final LogicalWorld result = processor.getResult();
      logger.debug("result = " + result);

      int i = 0;
      // delta
      assertEquals("{ A, P }", result.getClause(i++).toString());
      assertEquals("{ ¬B }", result.getClause(i++).toString());
      assertEquals("{ P }", result.getClause(i++).toString());
      assertEquals("{ Q }", result.getClause(i++).toString());
      // gamma
      assertEquals("{ ¬A }", result.getClause(i++).toString());
      assertEquals("{ B, ¬P }", result.getClause(i++).toString());

      // derived
      assertEquals("{ ¬P }", result.getClause(i++).toString());
      assertEquals("{ B }", result.getClause(i++).toString());
      assertEquals("{ }", result.getClause(i++).toString());

      assertEquals(10, processor.getResult().size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testLeaveUnit() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{A}");
      final LogicalWorld g = LogicalParser.parse("{¬A}{B}");
      final LogicProcessor processor = new LogicProcessor(d, g);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(4, processor.getResult().size());
      assertEquals("{ A }{ ¬A }{ B }{ }", processor.getResult().toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testLeaveUnit2() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{A}");
      final LogicalWorld g = LogicalParser.parse("{¬A}{B}{¬A}");
      final LogicProcessor processor = new LogicProcessor(d, g);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(5, processor.getResult().size());
      assertEquals("{ A }{ ¬A }{ B }{ ¬A }{ }", processor.getResult().toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testLeaveUnit3() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{A}{¬B}{P}");
      final LogicalWorld g = LogicalParser.parse("{¬A}{B}{¬A}");
      final LogicProcessor processor = new LogicProcessor(d, g);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(7, processor.getResult().size());
      assertEquals("{ A }{ ¬B }{ P }{ ¬A }{ B }{ ¬A }{ }", processor.getResult().toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testLeaveUnit4() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{A}{¬B}{P}{Q}");
      final LogicalWorld g = LogicalParser.parse("{¬A}{B, ¬P}{¬A}");
      final LogicProcessor processor = new LogicProcessor(d, g);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals("{ A }{ ¬B }{ P }{ Q }{ ¬A }{ B, ¬P }{ ¬A }" + "{ }{ ¬P }{ B }", processor.getResult().toString());
      assertEquals(10, processor.getResult().size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testSingleUnit() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{A}");
      final LogicProcessor processor = new LogicProcessor(w, w);

      final LogicStrategyAdapter strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(2, processor.getResult().size());
   }
}

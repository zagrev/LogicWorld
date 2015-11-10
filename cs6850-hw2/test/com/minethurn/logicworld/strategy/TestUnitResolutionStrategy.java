/**
 *
 */
package com.minethurn.logicworld.strategy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.LogicProcessor;

/**
 *
 */
public class TestUnitResolutionStrategy
{
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

      assertEquals(3, processor.getDerivedWorld().size());
      assertEquals("{ A }{ ¬A }{ }", processor.getDerivedWorld().toString());
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

      assertEquals(0, processor.getDerivedWorld().size());
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

      assertEquals(2, processor.getDerivedWorld().size());
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

      assertEquals(10, processor.getDerivedWorld().size());
      assertEquals("{ A, P }{ ¬B }{ P }{ Q }{ ¬A }{ B, ¬P }{ ¬P }" + "{ B }{ }{ A }",
            processor.getDerivedWorld().toString());
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

      assertEquals(4, processor.getDerivedWorld().size());
      assertEquals("{ A }{ ¬A }{ B }{ }", processor.getDerivedWorld().toString());
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

      assertEquals(5, processor.getDerivedWorld().size());
      assertEquals("{ A }{ ¬A }{ B }{ ¬A }{ }", processor.getDerivedWorld().toString());
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

      assertEquals(7, processor.getDerivedWorld().size());
      assertEquals("{ A }{ ¬B }{ P }{ ¬A }{ B }{ ¬A }{ }", processor.getDerivedWorld().toString());
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

      assertEquals("{ A }{ ¬B }{ P }{ Q }{ ¬A }{ B, ¬P }{ ¬A }" + "{ }{ ¬P }{ B }",
            processor.getDerivedWorld().toString());
      assertEquals(10, processor.getDerivedWorld().size());
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

      assertEquals(2, processor.getDerivedWorld().size());
   }
}

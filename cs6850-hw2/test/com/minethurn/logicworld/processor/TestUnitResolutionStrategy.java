/**
 *
 */
package com.minethurn.logicworld.processor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalWorld;

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

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(3, processor.derivedWorld.size());
      assertEquals("{ A }{ ¬A }{ }", processor.derivedWorld.toString());
   }

   /**
    *
    */
   @Test
   public void testEmpty()
   {
      final LogicalWorld w = new LogicalWorld();
      final LogicProcessor processor = new LogicProcessor(w, w);

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(0, processor.derivedWorld.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testEmpty2() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{}");
      final LogicProcessor processor = new LogicProcessor(w, w);

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(2, processor.derivedWorld.size());
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

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(10, processor.derivedWorld.size());
      assertEquals("{ A, P }{ ¬B }{ P }{ Q }{ ¬A }{ B, ¬P }{ ¬P }" + "{ B }{ }{ A }",
            processor.derivedWorld.toString());
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

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(4, processor.derivedWorld.size());
      assertEquals("{ A }{ ¬A }{ B }{ }", processor.derivedWorld.toString());
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

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(5, processor.derivedWorld.size());
      assertEquals("{ A }{ ¬A }{ B }{ ¬A }{ }", processor.derivedWorld.toString());
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

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(7, processor.derivedWorld.size());
      assertEquals("{ A }{ ¬B }{ P }{ ¬A }{ B }{ ¬A }{ }", processor.derivedWorld.toString());
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

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals("{ A }{ ¬B }{ P }{ Q }{ ¬A }{ B, ¬P }{ ¬A }" + "{ }{ ¬P }{ B }", processor.derivedWorld.toString());
      assertEquals(10, processor.derivedWorld.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testSingleUnit() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{A}");
      final LogicProcessor processor = new LogicProcessor(w, w);

      final UnitResolutionStrategy strategy = new UnitResolutionStrategy();
      processor.setStrategy(strategy);

      processor.process();

      assertEquals(2, processor.derivedWorld.size());
   }
}

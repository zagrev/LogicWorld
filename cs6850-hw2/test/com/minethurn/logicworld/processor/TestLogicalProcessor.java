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
public class TestLogicalProcessor
{
   /**
    * @throws IOException
    */
   @Test
   public void testDefaultLogicVariableComplementProcessor() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ A }");
      final LogicalWorld gamma = LogicalParser.parse("{ ¬A }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());
      processor.process();

      assertEquals("should have a single clause", 3, processor.derivedWorld.size());
      assertEquals("world should be \"{  }\"", "{ }", processor.derivedWorld.getClause(2).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testDefaultLogicVariablePQ() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ P, Q }");
      final LogicalWorld gamma = LogicalParser.parse("{ ¬P }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());
      processor.process();

      assertEquals("should have a single clause", 3, processor.derivedWorld.size());
      assertEquals("world should be \"{  }\"", "{ Q }", processor.derivedWorld.getClause(2).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testDefaultLogicVariablePQ2() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ P, Q }");
      final LogicalWorld gamma = LogicalParser.parse("{ ¬Q }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());
      processor.process();

      assertEquals("should have a single clause", 3, processor.derivedWorld.size());
      assertEquals("world should be \"{  }\"", "{ P }", processor.derivedWorld.getClause(2).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testDefaultLogicVariableProcessor() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ A }");
      final LogicalWorld gamma = LogicalParser.parse("{ A }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());
      processor.process();

      assertEquals("should have a single clause", 2, processor.derivedWorld.size());
      assertEquals("world should be \"{ A }\"", "{ A }", processor.derivedWorld.getClause(0).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testGetStrategry() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ A }");
      final LogicalWorld gamma = LogicalParser.parse("{ A }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      final ILogicStrategy s = new NoopStrategy();
      processor.setStrategy(s);

      assertEquals("Should have gotten the same processor back", s, processor.getStrategy());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testNoopProcessor() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ A }");
      final LogicalWorld gamma = LogicalParser.parse("{ A }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new NoopStrategy());
      processor.process();

      assertEquals("should have the original clauses", 2, processor.derivation.size());
      assertEquals("world size should be 2", 2, processor.derivedWorld.size());
   }

   /**
    *
    */
   @Test(expected = IllegalStateException.class)
   public void testNullDeltaWorld()
   {
      final LogicalWorld w = new LogicalWorld();
      final LogicProcessor procesor = new LogicProcessor(null, w);

      final ILogicStrategy s = new DefaultLogicStrategy();
      procesor.setStrategy(s);

      procesor.process();
   }

   /**
    *
    */
   @Test(expected = IllegalStateException.class)
   public void testNullGammaWorld()
   {
      final LogicalWorld w = new LogicalWorld();
      final LogicProcessor procesor = new LogicProcessor(w, null);

      final ILogicStrategy s = new DefaultLogicStrategy();
      procesor.setStrategy(s);

      procesor.process();
   }

   /**
    *
    */
   @Test(expected = IllegalStateException.class)
   public void testNullStrategy()
   {
      final LogicProcessor procesor = new LogicProcessor(null, null);
      procesor.process();
   }
}

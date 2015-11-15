/**
 *
 */
package com.minethurn.logicworld.processor;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.strategy.DefaultLogicStrategy;
import com.minethurn.logicworld.strategy.ILogicStrategy;

/**
 *
 */
public class TestLogicalProcessor
{
   /** the logger */
   private final Logger logger = LogManager.getLogger(getClass());

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

      final LogicalWorld world = processor.getResult();
      int i = 0;
      assertEquals("{ A }", world.getClause(i++).toString());
      assertEquals("{ ¬A }", world.getClause(i++).toString());
      assertEquals("{ }", world.getClause(i++).toString());

      assertEquals("should have a single clause", 3, world.size());
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

      int i = 0;
      final LogicalWorld world = processor.getResult();
      logger.debug("World = " + world);

      assertEquals("{ P, Q }", world.getClause(i++).toString());
      assertEquals("{ ¬P }", world.getClause(i++).toString());
      assertEquals("{ Q }", world.getClause(i++).toString());
      assertEquals("should have a single clause", 3, world.size());
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

      int i = 0;
      final LogicalWorld world = processor.getResult();
      logger.debug("result = " + world);

      assertEquals("{ P, Q }", world.getClause(i++).toString());
      assertEquals("{ ¬Q }", world.getClause(i++).toString());
      assertEquals("{ P }", world.getClause(i++).toString());

      assertEquals(3, world.size());
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

      assertEquals("should have a single clause", 2, processor.getResult().size());
      assertEquals("world should be \"{ A }\"", "{ A }", processor.getResult().getClause(0).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testDefaultStrategyMappingPrimary() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ Func(x,y), ¬Bad(x,y) }");
      final LogicalWorld gamma = LogicalParser.parse("{ ¬Func(A,B), C }{ Bad(A,B) }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());
      processor.process();

      int i = 0;
      assertEquals("{ Func(x,y), ¬Bad(x,y) }", processor.getResult().getClause(i++).toString());
      assertEquals("{ ¬Func(A,B), C }", processor.getResult().getClause(i++).toString());
      assertEquals("{ Bad(A,B) }", processor.getResult().getClause(i++).toString());

      assertEquals("{ ¬Bad(A,B), C }", processor.getResult().getClause(i++).toString());
      assertEquals("{ Func(A,B) }", processor.getResult().getClause(i++).toString());
      assertEquals("{ C, ¬Bad(A,B) }", processor.getResult().getClause(i++).toString());
      assertEquals("{ C }", processor.getResult().getClause(i++).toString());

      assertEquals(7, processor.getResult().size());
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

      assertEquals("should have the original clauses", 2, processor.getResult().size());
      assertEquals("world size should be 2", 2, processor.getResult().size());
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

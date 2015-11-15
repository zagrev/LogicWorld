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
public class TestDefaultLogicStrategy extends LogicStrategyAdapter
{
   /** the logger */
   private final Logger logger = LogManager.getLogger(getClass());

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
   public void testEmpty() throws IOException
   {

      final LogicalWorld delta = LogicalParser.parse("{}");
      final LogicalWorld gamma = LogicalParser.parse("{}");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());

      processor.process();

      final LogicalWorld world = processor.getResult();

      int i = 0;
      assertEquals("{ }", world.getClause(i++).toString());
      assertEquals("{ }", world.getClause(i++).toString());

      assertEquals(2, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testSingle() throws IOException
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

      assertEquals(3, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testThree() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ ¬A, B, C }");
      final LogicalWorld gamma = LogicalParser.parse("{ A, C }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());

      processor.process();

      final LogicalWorld world = processor.getResult();
      logger.info("result = " + world);

      int i = 0;
      assertEquals("{ ¬A, B, C }", world.getClause(i++).toString());
      assertEquals("{ A, C }", world.getClause(i++).toString());
      assertEquals("{ B, C }", world.getClause(i++).toString());
      assertEquals("{ C, B }", world.getClause(i++).toString());

      assertEquals(4, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testTwo() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ A, C }");
      final LogicalWorld gamma = LogicalParser.parse("{ ¬A }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());

      processor.process();

      final LogicalWorld world = processor.getResult();
      logger.debug(world);

      int i = 0;
      assertEquals("{ A, C }", world.getClause(i++).toString());
      assertEquals("{ ¬A }", world.getClause(i++).toString());
      assertEquals("{ C }", world.getClause(i++).toString());

      assertEquals(3, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testTwoReverse() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ ¬A }");
      final LogicalWorld gamma = LogicalParser.parse("{ A, C }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());

      processor.process();

      final LogicalWorld world = processor.getResult();
      logger.debug(world);

      int i = 0;
      assertEquals("{ ¬A }", world.getClause(i++).toString());
      assertEquals("{ A, C }", world.getClause(i++).toString());
      assertEquals("{ C }", world.getClause(i++).toString());

      assertEquals(3, world.size());
   }

}

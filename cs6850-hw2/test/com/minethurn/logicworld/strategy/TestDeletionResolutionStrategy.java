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
public class TestDeletionResolutionStrategy
{
   /**
    * @throws IOException
    */
   @Test
   public void testCrazyMatching() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ A(x), B(x), ¬A(y), C(x), D(x) }");
      final LogicalWorld g = LogicalParser.parse("{ ¬B(x), D(x), ¬A(x), C(y) }");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final DeletionResolutionStrategy deletionStrategy = new DeletionResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld world = processor.getDerivedWorld();
      assertEquals(3, world.size());
      assertEquals("{ A(x), B(x), ¬A(y), C(x), D(x) }", world.getClause(0).toString());
      assertEquals("{ ¬B(x), D(x), ¬A(x), C(y) }", world.getClause(1).toString());
      assertEquals("{ ¬A(y), C(x), D(x), C(y) }", world.getClause(2).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testCrazyMatchingMoreLines() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ A(x), B(x), ¬A(y), C(x), D(x) }{A(x), D(x)}");
      final LogicalWorld g = LogicalParser.parse("{ ¬B(x), D(x), ¬A(x), C(y) }{¬A(x), D(x)}");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final DeletionResolutionStrategy deletionStrategy = new DeletionResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld world = processor.getDerivedWorld();

      assertEquals(9, world.size());
      assertEquals("{ A(x), B(x), ¬A(y), C(x), D(x) }", world.getClause(0).toString());
      assertEquals("{ A(x), D(x) }", world.getClause(1).toString());
      assertEquals("{ ¬B(x), D(x), ¬A(x), C(y) }", world.getClause(2).toString());
      assertEquals("{ ¬A(x), D(x) }", world.getClause(3).toString());
      assertEquals("{ ¬A(y), C(x), D(x), C(y) }", world.getClause(4).toString());
      assertEquals("{ B(x), ¬A(y), C(x), D(x) }", world.getClause(5).toString());
      assertEquals("{ D(x), ¬B(x), C(y) }", world.getClause(6).toString());
      assertEquals("{ D(x) }", world.getClause(7).toString());
      assertEquals("{ D(x), ¬A(x), C(y), ¬A(y), C(x) }", world.getClause(8).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testEmpty() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{}");
      final LogicalWorld g = LogicalParser.parse("{}");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final DeletionResolutionStrategy deletionStrategy = new DeletionResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      assertEquals(2, processor.getDerivedWorld().size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testMultipleLines() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ A(x), ¬B(x) }");
      final LogicalWorld g = LogicalParser.parse("{ B(x) }{¬A(x)}");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final DeletionResolutionStrategy deletionStrategy = new DeletionResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld world = processor.getDerivedWorld();
// assertEquals(9, world.size());
      assertEquals("{ A(x), ¬B(x) }", world.getClause(0).toString());
      assertEquals("{ B(x) }", world.getClause(1).toString());
      assertEquals("{ ¬A(x) }", world.getClause(2).toString());
      assertEquals("{ ¬B(x) }", world.getClause(3).toString());
      assertEquals("{ A(x) }", world.getClause(4).toString());
      assertEquals("{ }", world.getClause(5).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testNoMatching() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{Func(A)}");
      final LogicalWorld g = LogicalParser.parse("{}");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final DeletionResolutionStrategy deletionStrategy = new DeletionResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      assertEquals(2, processor.getDerivedWorld().size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testOneMatching() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{Func(A)}");
      final LogicalWorld g = LogicalParser.parse("{Func(X,Y), ¬Func(A)}");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final DeletionResolutionStrategy deletionStrategy = new DeletionResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld world = processor.getDerivedWorld();
      assertEquals(3, world.size());
      assertEquals("{ Func(A) }", world.getClause(0).toString());
      assertEquals("{ Func(X,Y), ¬Func(A) }", world.getClause(1).toString());
      assertEquals("{ Func(X,Y) }", world.getClause(2).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testOneMatchingReverse() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{¬Func(X,Y)}");
      final LogicalWorld g = LogicalParser.parse("{Func(X,Y), ¬Func(A)}");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final DeletionResolutionStrategy deletionStrategy = new DeletionResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld world = processor.getDerivedWorld();
      assertEquals(3, world.size());
      assertEquals("{ ¬Func(X,Y) }", world.getClause(0).toString());
      assertEquals("{ Func(X,Y), ¬Func(A) }", world.getClause(1).toString());
      assertEquals("{ ¬Func(A) }", world.getClause(2).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testTwoMatching() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{¬Func(X,Y), Func(D,G)}");
      final LogicalWorld g = LogicalParser.parse("{Func(X,Y), ¬Func(A)}");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final DeletionResolutionStrategy deletionStrategy = new DeletionResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld world = processor.getDerivedWorld();
      assertEquals(3, world.size());
      assertEquals("{ ¬Func(X,Y), Func(D,G) }", world.getClause(0).toString());
      assertEquals("{ Func(X,Y), ¬Func(A) }", world.getClause(1).toString());
      assertEquals("{ Func(D,G), ¬Func(A) }", world.getClause(2).toString());
   }
}

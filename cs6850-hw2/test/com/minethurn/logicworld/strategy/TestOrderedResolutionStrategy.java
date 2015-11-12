/**
 *
 */
package com.minethurn.logicworld.strategy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.LogicProcessor;

/**
 *
 */
public class TestOrderedResolutionStrategy
{
   /**
    * @throws IOException
    */
   @Test
   public void testAdvanced() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ ¬A(x), B(x), D(x) }{ ¬B(x), ¬C(x) }");
      final LogicalWorld g = LogicalParser.parse("{ C(x), B(x), A(x) }{ B(x), C(x) }");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final OrderedResolutionStrategy deletionStrategy = new OrderedResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld result = processor.getResult();
// assertEquals(9, result.size());

      assertEquals("{ ¬A(x), B(x), D(x) }", result.getClause(0).toString());
      assertEquals("{ ¬B(x), ¬C(x) }", result.getClause(1).toString());
      assertEquals("{ A(x), B(x), C(x) }", result.getClause(2).toString());
      assertEquals("{ B(x), C(x) }", result.getClause(3).toString());

      assertEquals("{ B(x), C(x), D(x) }", result.getClause(4).toString());
      assertEquals("{ }", result.getClause(5).toString());
      assertEquals("{ D(x) }", result.getClause(6).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testAdvanced2() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ ¬A(x), B(x), D(x) }{ B(x), C(x) }");
      final LogicalWorld g = LogicalParser.parse("{ C(x), B(x), A(x) }{ ¬B(x), ¬C(x) }");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final OrderedResolutionStrategy deletionStrategy = new OrderedResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld result = processor.getResult();
      assertEquals(7, result.size());

      assertEquals("{ ¬A(x), B(x), D(x) }", result.getClause(0).toString());
      assertEquals("{ B(x), C(x) }", result.getClause(1).toString());
      assertEquals("{ A(x), B(x), C(x) }", result.getClause(2).toString());
      assertEquals("{ ¬B(x), ¬C(x) }", result.getClause(3).toString());
      assertEquals("{ B(x), C(x), D(x) }", result.getClause(4).toString());
      assertEquals("{ }", result.getClause(5).toString());
      assertEquals("{ D(x) }", result.getClause(6).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testBasic() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ A(x), B(x), D(x) }");
      final LogicalWorld g = LogicalParser.parse("{ C(x), B(x), ¬A(x) }");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final OrderedResolutionStrategy deletionStrategy = new OrderedResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld result = processor.getResult();
      System.out.println("testBasic result: " + result);
      assertEquals(3, result.size());

      LogicalClause clause = result.getClause(1);
      assertEquals("¬A(x)", clause.get(0).toString());
      assertEquals("B(x)", clause.get(1).toString());
      assertEquals("C(x)", clause.get(2).toString());

      clause = result.getClause(2);
      assertEquals("B(x)", clause.get(0).toString());
      assertEquals("C(x)", clause.get(1).toString());
      assertEquals("D(x)", clause.get(2).toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testBasic2() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ ¬A(x), B(x), D(x) }");
      final LogicalWorld g = LogicalParser.parse("{ C(x), B(x), A(x) }");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final OrderedResolutionStrategy deletionStrategy = new OrderedResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld result = processor.getResult();
      assertEquals(3, result.size());

      LogicalClause clause = result.getClause(1);
      assertEquals("A(x)", clause.get(0).toString());
      assertEquals("B(x)", clause.get(1).toString());
      assertEquals("C(x)", clause.get(2).toString());

      clause = result.getClause(2);
      assertEquals("B(x)", clause.get(0).toString());
      assertEquals("C(x)", clause.get(1).toString());
      assertEquals("D(x)", clause.get(2).toString());
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

      final OrderedResolutionStrategy deletionStrategy = new OrderedResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      assertEquals(2, processor.getResult().size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testSorting() throws IOException
   {
      final LogicalWorld d = LogicalParser.parse("{ A(x), B(x), D(x) }");
      final LogicalWorld g = LogicalParser.parse("{ C(x), B(x), A(x) }");

      final LogicProcessor processor = new LogicProcessor(d, g);

      final OrderedResolutionStrategy deletionStrategy = new OrderedResolutionStrategy();
      processor.setStrategy(deletionStrategy);

      processor.process();

      final LogicalWorld result = processor.getResult();
      assertEquals(2, result.size());

      final LogicalClause clause = result.getClause(1);
      assertEquals("A(x)", clause.get(0).toString());
      assertEquals("B(x)", clause.get(1).toString());
      assertEquals("C(x)", clause.get(2).toString());
   }
}

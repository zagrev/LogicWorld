/**
 *
 */
package com.minethurn.logicworld.strategy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
   /** the logger */
   private final Logger logger = LogManager.getLogger(getClass());

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
      logger.debug("result = " + result);

      int i = 0;

      // delta
      assertEquals("{ ¬A(x), B(x), D(x) }", result.getClause(i++).toString());
      assertEquals("{ ¬B(x), ¬C(x) }", result.getClause(i++).toString());

      // gamma
      assertEquals("{ A(x), B(x), C(x) }", result.getClause(i++).toString());
      assertEquals("{ B(x), C(x) }", result.getClause(i++).toString());

      // derived
      assertEquals("{ B(x), C(x), D(x) }", result.getClause(i++).toString());
      assertEquals("{ }", result.getClause(i++).toString());
      assertEquals("{ D(x) }", result.getClause(i++).toString());

      assertEquals(7, result.size());
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
      logger.debug("result = " + result);

      int i = 0;
      // delta
      assertEquals("{ ¬A(x), B(x), D(x) }", result.getClause(i++).toString());
      assertEquals("{ B(x), C(x) }", result.getClause(i++).toString());

      // gamma
      assertEquals("{ A(x), B(x), C(x) }", result.getClause(i++).toString());
      assertEquals("{ ¬B(x), ¬C(x) }", result.getClause(i++).toString());

      // dervied
      assertEquals("{ B(x), C(x), D(x) }", result.getClause(i++).toString());
      assertEquals("{ }", result.getClause(i++).toString());
      assertEquals("{ D(x) }", result.getClause(i++).toString());

      assertEquals(7, result.size());
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
      logger.debug("testBasic result: " + result);

      LogicalClause clause = result.getClause(1);
      assertEquals("¬A(x)", clause.get(0).toString());
      assertEquals("B(x)", clause.get(1).toString());
      assertEquals("C(x)", clause.get(2).toString());

      clause = result.getClause(2);
      assertEquals("B(x)", clause.get(0).toString());
      assertEquals("C(x)", clause.get(1).toString());
      assertEquals("D(x)", clause.get(2).toString());

      assertEquals(3, result.size());
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

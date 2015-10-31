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
   public void testNoopProcessor() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ A }");
      final LogicalWorld gamma = LogicalParser.parse("{ A }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());
      processor.process();

      assertEquals("should have a original clauses", 2, processor.derivation.size());
      assertEquals("world size should be 2", 2, processor.derivedWorld.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testProcessor() throws IOException
   {
      final LogicalWorld delta = LogicalParser.parse("{ A }");
      final LogicalWorld gamma = LogicalParser.parse("{ A }");

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(new DefaultLogicStrategy());
      processor.process();

      assertEquals("should have a single clause", 2, processor.derivedWorld.size());
      assertEquals("world should be \"{ A }\"", "{ A }", processor.derivedWorld.getClause(0).toString());
   }
}

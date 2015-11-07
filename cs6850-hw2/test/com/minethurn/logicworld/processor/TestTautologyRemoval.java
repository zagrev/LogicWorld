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
public class TestTautologyRemoval
{
   /**
    * @throws IOException
    */
   @Test
   public void testEmpty() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{}");
      TautologyRemoval.removeTautologies(world);

      assertEquals(1, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testFunction() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{Func(A)}");
      TautologyRemoval.removeTautologies(world);

      assertEquals(1, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testFunctionTautology() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{Func(A),�Func(A)}");
      TautologyRemoval.removeTautologies(world);

      assertEquals(0, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testVariable() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{A}");
      TautologyRemoval.removeTautologies(world);

      assertEquals(1, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testVariableFunction() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{A, �Func(A)}");
      TautologyRemoval.removeTautologies(world);

      assertEquals(1, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testVariableFunctionMutlipleLines() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{A, �Func(A)}{�A, �Func(A)}");
      TautologyRemoval.removeTautologies(world);

      assertEquals(2, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testVariableFunctionTautology() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{A, �Func(A)}{�A, �Func(A)}{A,�A}");
      TautologyRemoval.removeTautologies(world);

      assertEquals(2, world.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testVariableTautology() throws IOException
   {
      final LogicalWorld world = LogicalParser.parse("{A, �A}");
      TautologyRemoval.removeTautologies(world);

      assertEquals(0, world.size());
   }
}

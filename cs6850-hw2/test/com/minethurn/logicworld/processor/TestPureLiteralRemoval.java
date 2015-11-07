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
public class TestPureLiteralRemoval
{
   /**
    * @throws IOException
    */
   @Test
   public void testDuplicateLiteral() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{A, A}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(0, w.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testEmpty() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(1, w.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testFunction() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{Func(A),Func(A)}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(0, w.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testFunctionHasComplement() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{Func(A),�Func(A)}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(1, w.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testS() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{�P,�Q, R}" + "{ �P, S}" + "{�Q, S}" + "{P}" + "{Q}" + "{�R}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(4, w.size());
      assertEquals("{ �P, �Q, R }" + "{ P }" + "{ Q }" + "{ �R }", w.toString());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testSingleLiteral() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{A}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(0, w.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testVariable() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{A,A}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(0, w.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testVariableHasComplement() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{A,�A}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(1, w.size());
   }
}

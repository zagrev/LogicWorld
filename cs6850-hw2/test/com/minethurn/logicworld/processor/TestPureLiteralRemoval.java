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
      final LogicalWorld w = LogicalParser.parse("{Func(A),政unc(A)}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(1, w.size());
   }

   /**
    * @throws IOException
    */
   @Test
   public void testS() throws IOException
   {
      final LogicalWorld w = LogicalParser.parse("{星,昨, R}" + "{ 星, S}" + "{昨, S}" + "{P}" + "{Q}" + "{昱}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(4, w.size());
      assertEquals("{ 星, 昨, R }" + "{ P }" + "{ Q }" + "{ 昱 }", w.toString());
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
      final LogicalWorld w = LogicalParser.parse("{A,括}");
      PureLiteralRemoval.removePureLiterals(w);

      assertEquals(1, w.size());
   }
}

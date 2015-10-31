/**
 *
 */
package com.minethurn.clausal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.junit.Test;

/**
 *
 */
public class TestLogicalWorldPrinter
{
   /**
    * @param input
    * @throws IOException
    */
   private void test(final String input) throws IOException
   {
      final LogicalWorld w = LogicalParser.parse(new ByteArrayInputStream(input.getBytes()));
      assertNotNull("World should have parsed ok", w);

      try (ByteArrayOutputStream out = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out))
      {
         LogicalWorldPrinter.print(writer, w);
         writer.close();
         out.close();
         assertEquals("output should match input", input, out.toString());
      }
   }

   /**
    * @throws IOException
    */
   @Test
   public void testPrinter() throws IOException
   {
      final String input = "{ A }";
      test(input);
   }

   /**
    * @throws IOException
    */
   @Test
   public void testPrinterFancy() throws IOException
   {
      final String input = "{ ¬T(A), ¬Town(A) }" + "{ ¬T(A), ¬Key(A) }" + "{ ¬T(B), Key(C) }" + "{ ¬T(C), Key(C) }"
            + "{ ¬T(D), Key(B) }" + "{ Friend(A,D) }" + "{ ¬Like(B,C) }" + "{ ¬Friend(x,y), Friend(y,x) }"
            + "{ ¬Friend(x,y), Like(x,y) }" + "{ Like(x,y), ¬Friend(x,y) }";
      test(input);
   }

   /**
    * @throws IOException
    */
   @Test
   public void testPrinterTwoClauses() throws IOException
   {
      final String input = "{ A }{ B }";
      test(input);
   }

   /**
    * @throws IOException
    */
   @Test
   public void testPrinterTwoClausesOneNegative() throws IOException
   {
      final String input = "{ A }{ ¬B }";
      test(input);
   }

}

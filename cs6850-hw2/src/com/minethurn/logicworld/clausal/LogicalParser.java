/**
 *
 */
package com.minethurn.logicworld.clausal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class LogicalParser
{
   /**
    * parse the given input into a logical world and returns that world
    *
    * @param in
    *           the input to read
    * @return the created world. This is never null.
    * @throws IOException
    *            if the input cannot be read
    */
   public static LogicalWorld parse(final InputStream in) throws IOException
   {
      int c;
      final LogicalParser context = new LogicalParser();

      while ((c = in.read()) != -1)
      {
         switch (c)
         {
         // start a new clause. Should not be in a clause right now
         case '{':
            context.startClause();
            break;

         // done with logical clause. Add it to the world
         case '}':
            context.endClause();
            break;

         // rest of the line is a comment
         case '#':
            context.startComment();
            break;

         // this next function will be negated (this handles double negatives too)
         case '¬':
            context.not = true;
            break;

         // whitespace ends names and comments
         case '\n':
            context.endWord();
            break;

         // start a function (no longer just a variable name
         case '(':
            context.startFunction();
            break;

         case ')':
            context.endFunction();
            break;

         // just ignore spaces
         case ' ':
            break;

         // commas separate variables in a function
         case ',':
            context.endWord();
            break;

         // add the current character to the accumulating name
         default:
            context.addChar(c);
         }
      }
      context.endWord();

      return context.world;

   }

   /**
    * @param world
    * @return the world defined in the given string
    * @throws IOException
    */
   public static LogicalWorld parse(final String world) throws IOException
   {
      try (InputStream in = new ByteArrayInputStream(world.getBytes()))
      {
         return parse(in);
      }
   }

   /** the world we are creating with the parser */
   public final LogicalWorld world = new LogicalWorld();
   /** the last clause added to the world. Used to add additional functions and variables to the clause */
   public LogicalClause clause = null;
   /** the last function added to the current clause. used to add variables to the function */
   public LogicalFunction function = null;
   /** if we are currently in a comment */
   public boolean inComment = false;

   /** the accumulator for the next name to use */
   public final StringBuilder name = new StringBuilder();

   /** true if the next function or variable is negated */
   public boolean not = false;

   /**
    * @param c
    */
   public void addChar(final int c)
   {
      if (!inComment)
      {
         name.append((char) c);
      }
   }

   /**
    *
    */
   public void endClause()
   {
      if (!inComment && name.length() > 0)
      {
         final LogicalVariable v = new LogicalVariable(name.toString());
         name.setLength(0);

         clause.add(v);

         if (not)
         {
            v.setPositive(false);
         }
         not = false;
      }
   }

   /**
    *
    */
   public void endFunction()
   {
      if (!inComment && name.length() > 0)
      {
         final LogicalVariable v = new LogicalVariable(name.toString());
         function.add(v);
         if (not)
         {
            v.setPositive(false);
         }
         not = false;
         name.setLength(0);
         function = null;
      }
   }

   /**
    *
    */
   public void endWord()
   {
      if (!inComment && name.length() > 0)
      {
         final LogicalVariable v = new LogicalVariable(name.toString());
         name.setLength(0);

         if (not)
         {
            v.setPositive(false);
         }
         not = false;

         if (function != null)
         {
            function.add(v);
         }
         else
         {
            clause.add(v);
         }
      }

   }

   /**
    *
    */
   public void startClause()
   {
      if (!inComment)
      {
         clause = new LogicalClause();
         world.add(clause);
      }
   }

   /**
    *
    */
   public void startComment()
   {
      inComment = true;
   }

   /**
    * start a function and add it to the current clause.
    */
   public void startFunction()
   {
      function = new LogicalFunction(name.toString());
      name.setLength(0);

      if (not)
      {
         function.setPositive(false);
      }
      not = false;

      clause.add(function);
   }
}

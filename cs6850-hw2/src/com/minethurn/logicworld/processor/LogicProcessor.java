/**
 *
 */
package com.minethurn.logicworld.processor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalParser;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.clausal.LogicalWorldPrinter;

/**
 *
 */
public class LogicProcessor
{
   /**
    * how the line was derived and the order it was derived
    */
   public class DerivationLine
   {
      /** the clauses derived from this step */
      private ArrayList<LogicalClause> clauses;
      /** where the clauses came from */
      private DerivationLineType type;

      /**
       * @param newClauses
       * @param derived
       */
      public DerivationLine(final List<LogicalClause> newClauses, final DerivationLineType derived)
      {
         super();
         this.clauses = new ArrayList<>(newClauses);
         this.type = derived;
      }

      /**
       * @param clause
       * @param type
       */
      public DerivationLine(final LogicalClause clause, final DerivationLineType type)
      {
         super();
         this.clauses = new ArrayList<>();

         this.clauses.add(clause);
         this.type = type;
      }

      /**
       * @return the clauses
       */
      public ArrayList<LogicalClause> getClauses()
      {
         return clauses;
      }

      /**
       * @return the type
       */
      public DerivationLineType getType()
      {
         return type;
      }

      /**
       * @param clauses
       *           the clauses to set
       */
      public void setClauses(final ArrayList<LogicalClause> clauses)
      {
         this.clauses = clauses;
      }

      /**
       * @param type
       *           the type to set
       */
      public void setType(final DerivationLineType type)
      {
         this.type = type;
      }
   }

   /**
    * whether the clause is from the original set of statements (delta), the query statements (gamma), or derived by the
    * strategy
    */
   public enum DerivationLineType
   {
      /**
       * from the original data set
       */
      DELTA, /**
              * from the query data set
              */
      GAMMA, /**
              * derived by the strategy
              */
      DERIVED
   }

   /**
    * start up a logical derivedWorld
    *
    * @param args
    *           the command line arguments
    * @throws IOException
    */
   public static void main(final String[] args) throws IOException
   {
      String deltaFile = "delta.world";
      String gammaFile = "gamma.world";
      String strategyClass = DefaultLogicStrategy.class.getName();
      ILogicStrategy strategy = null;

      final Options options = new Options();
      options.addOption("d", "delta", true, "The file with the original database of statements");
      options.addOption("g", "gamma", true, "The file with the query clauses");

      try
      {
         final CommandLineParser parser = new DefaultParser();
         final CommandLine cmdLine = parser.parse(options, args);

         if (cmdLine.hasOption("delta"))
         {
            deltaFile = cmdLine.getOptionValue("delta");
         }
         if (cmdLine.hasOption("gamma"))
         {
            gammaFile = cmdLine.getOptionValue("gamma");
         }
         if (cmdLine.hasOption("strategy"))
         {
            strategyClass = cmdLine.getOptionValue("strategy");
         }
         final Class<?> cls = Class.forName(strategyClass);
         strategy = (ILogicStrategy) cls.newInstance();

      }
      catch (final ParseException e)
      {
         System.err.println("Cannot parse command line");
         System.err.println(e.getMessage());

         final HelpFormatter formatter = new HelpFormatter();
         formatter.printHelp("process", options);
         return;
      }
      catch (final ClassCastException e)
      {
         System.err.println("Strategy class does not implemnt ILogicStrategy");
         return;
      }
      catch (final ClassNotFoundException | IllegalAccessException | InstantiationException e)
      {
         System.err.println("Strategy class not found: " + strategyClass);
         return;
      }

      LogicalWorld delta = null;
      LogicalWorld gamma = null;

      try (InputStream in = new FileInputStream(deltaFile))
      {
         delta = LogicalParser.parse(in);
      }
      try (InputStream in = new FileInputStream(gammaFile))
      {
         gamma = LogicalParser.parse(in);
      }

      System.out.println("Delta world:");
      LogicalWorldPrinter.print(new OutputStreamWriter(System.out), delta);
      System.out.println("Gamma world:");
      LogicalWorldPrinter.print(new OutputStreamWriter(System.out), gamma);

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(strategy);
      processor.process();
   }

   /** the base set of rules in the problem space */
   LogicalWorld delta;
   /** the question we are trying to answer */
   LogicalWorld gamma;

   /** the current state of the derivation */
   LogicalWorld derivedWorld;
   /** the steps created during the derivation */
   ArrayList<DerivationLine> derivation;

   /** the strategy to use while processing */
   private ILogicStrategy strategy;

   /**
    * @param delta
    * @param gamma
    */
   public LogicProcessor(final LogicalWorld delta, final LogicalWorld gamma)
   {
      this.delta = delta;
      this.gamma = gamma;
      derivation = new ArrayList<>();
   }

   /**
    * @return the strategy
    */
   public ILogicStrategy getStrategy()
   {
      return strategy;
   }

   /**
    *
    */
   public void process()
   {
      if (strategy == null)
      {
         throw new IllegalStateException("No strategy defined");
      }
      if (delta == null || gamma == null)
      {
         throw new IllegalStateException("Both delta and gamma must be defined");
      }

      derivedWorld = new LogicalWorld();

      // combine the worlds
      for (final LogicalClause d : delta)
      {
         derivedWorld.add(d);
         derivation.add(new DerivationLine(d, DerivationLineType.DELTA));
      }
      for (final LogicalClause g : gamma)
      {
         derivedWorld.add(g);
         derivation.add(new DerivationLine(g, DerivationLineType.GAMMA));
      }

      strategy.initialize(derivedWorld);

      List<LogicalClause> newClauses = strategy.step(derivedWorld);
      while (newClauses != null && !newClauses.isEmpty())
      {
         derivedWorld.addAll(newClauses);
         derivation.add(new DerivationLine(newClauses, DerivationLineType.DERIVED));

         newClauses = strategy.step(derivedWorld);
      }
      strategy.finalize(derivedWorld);
   }

   /**
    * @param strategy
    *           the strategy to set
    */
   public void setStrategy(final ILogicStrategy strategy)
   {
      this.strategy = strategy;
   }
}
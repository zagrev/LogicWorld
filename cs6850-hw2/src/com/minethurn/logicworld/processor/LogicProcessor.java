/**
 *
 */
package com.minethurn.logicworld.processor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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
import com.minethurn.logicworld.strategy.DefaultLogicStrategy;
import com.minethurn.logicworld.strategy.ILogicStrategy;

/**
 *
 */
public class LogicProcessor
{
   /**
    * whether the clause is from the original set of statements (delta), the query statements (gamma), or derived by the
    * strategy
    */
   public enum DerivationLineType
   {
      /**
       * from the original data set
       */
      DELTA,
      /**
       * from the query data set
       */
      GAMMA,
      /**
       * derived by the strategy
       */
      DERIVED
   }

   /**
    * the output format for a derived line (with line number pairs)
    */
   private static final String DERIVED_FORMAT = "%-4d %-40s %d, %d";

   /**
    *
    */
   private static final int MAX_COUNT = 100;

   /**
    * start up a logical derivedWorld
    *
    * @param args
    *           the command line arguments
    * @throws IOException
    */
   public static void main(final String[] args) throws IOException
   {
      String deltaFile = "hw3delta.txt";
      String gammaFile = "hw3gamma.txt";
      String strategyClass = DefaultLogicStrategy.class.getName();
      ILogicStrategy strategy = null;
      boolean showUsage = false;
      boolean removeTautologies = false;
      boolean removePureLiterals = false;

      final Options options = new Options();
      options.addOption("h", "help", false, "Show the usage help");
      options.addOption("d", "delta", true, "The file with the original database of statements");
      options.addOption("g", "gamma", true, "The file with the query clauses");
      options.addOption("s", "strategy", true,
            "The strategy to use to find resolution.  Can be one of: \n"
                  + "com.minethurn.logicworld.strategy.DefaultLogicStrategy, \n"
                  + "com.minethurn.logicworld.strategy.DeletionResolutionStrategy, \n"
                  + "com.minethurn.logicworld.strategy.UnitResolutionStrategy");
      options.addOption("t", "rmTautology", false, "Remove tautologies before processing");
      options.addOption("l", "rmLiterals", false, "Remove pure literals before processing");

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
         if (cmdLine.hasOption("help"))
         {
            showUsage = true;
         }
         if (cmdLine.hasOption("rmTautology"))
         {
            removeTautologies = true;
         }
         if (cmdLine.hasOption("rmLiterals"))
         {
            removePureLiterals = true;
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
         System.err.println("Strategy class does not implement ILogicStrategy");
         return;
      }
      catch (final ClassNotFoundException | IllegalAccessException | InstantiationException e)
      {
         System.err.println("Strategy class not found: " + strategyClass);
         return;
      }
      if (showUsage)
      {
         final HelpFormatter formatter = new HelpFormatter();
         formatter.printHelp("process", options);
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

      final OutputStreamWriter output = new OutputStreamWriter(System.out);
      output.write("Delta world:");
      LogicalWorldPrinter.print(output, delta);
      output.write("Gamma world:");
      LogicalWorldPrinter.print(output, gamma);

      final LogicProcessor processor = new LogicProcessor(delta, gamma);
      processor.setStrategy(strategy);
      processor.setRemovePureLiterals(removePureLiterals);
      processor.setRemoveTautologies(removeTautologies);

      processor.process();
   }

   /** the base set of rules in the problem space */
   LogicalWorld delta;

   /** the question we are trying to answer */
   LogicalWorld gamma;

   /** the steps created during the derivation */
   ArrayList<DerivationLine> derivation;

   /** the strategy to use while processing */
   private ILogicStrategy strategy;

   /** apply pure literal removal before processing */
   private boolean removePureLiterals;

   /** remove tautologies before processing */
   private boolean removeTautologies;

   /**
    * @param delta
    * @param gamma
    */
   public LogicProcessor(final LogicalWorld delta, final LogicalWorld gamma)
   {
      this.delta = delta;
      this.gamma = gamma;
      derivation = new ArrayList<>();
      setRemovePureLiterals(false);
   }

   /**
    * @return the delta
    */
   public LogicalWorld getDelta()
   {
      return delta;
   }

   /**
    * @return the derivation
    */
   public ArrayList<DerivationLine> getDerivation()
   {
      return derivation;
   }

   /**
    * @return the gamma
    */
   public LogicalWorld getGamma()
   {
      return gamma;
   }

   /**
    * return the world as it exists after it was processed
    *
    * @return the processed world
    */
   public LogicalWorld getResult()
   {
      final LogicalWorld w = new LogicalWorld();
      for (final DerivationLine line : getDerivation())
      {
         w.getClauses().addAll(line.getClauses());
      }
      return w;
   }

   /**
    * @return the strategy
    */
   public ILogicStrategy getStrategy()
   {
      return strategy;
   }

   /**
    * @return the removePureLiterals
    */
   public boolean isRemovePureLiterals()
   {
      return removePureLiterals;
   }

   /**
    * @return the removeTautologies
    */
   public boolean isRemoveTautologies()
   {
      return removeTautologies;
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

      // combine the worlds
      strategy.initialize(delta, gamma);
      for (final LogicalClause d : delta)
      {
         derivation.add(new DerivationLine(d, DerivationLineType.DELTA));
      }
      for (final LogicalClause d : gamma)
      {
         derivation.add(new DerivationLine(d, DerivationLineType.GAMMA));
      }

      DerivationLine newLine = strategy.step();
      int count = delta.size() + gamma.size() + 1;
      while (newLine != null && !newLine.getClauses().isEmpty() && count < MAX_COUNT)
      {
         for (final LogicalClause c : newLine.getClauses())
         {
            System.out.println(String.format(DERIVED_FORMAT, Integer.valueOf(count++), c.toString(),
                  Integer.valueOf(newLine.getLeftIndex() + 1), Integer.valueOf(newLine.getRightIndex() + 1)));
         }
         derivation.add(newLine);

         newLine = strategy.step();
      }

      strategy.finalize(delta, gamma);
   }

   /**
    * @param derivation
    *           the derivation to set
    */
   public void setDerivation(final ArrayList<DerivationLine> derivation)
   {
      this.derivation = derivation;
   }

   /**
    * @param removePureLiterals
    *           the removePureLiterals to set
    */
   public void setRemovePureLiterals(final boolean removePureLiterals)
   {
      this.removePureLiterals = removePureLiterals;
   }

   /**
    * @param removeTautologies
    *           the removeTautologies to set
    */
   public void setRemoveTautologies(final boolean removeTautologies)
   {
      this.removeTautologies = removeTautologies;
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

/**
 *
 */
package com.minethurn.logicworld.strategy;

import java.util.HashSet;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.PureLiteralRemoval;
import com.minethurn.logicworld.processor.TautologyRemoval;

/**
 *
 */
public abstract class LogicStrategyAdapter implements ILogicStrategy
{
   /**
    * The output format for a premise line (with delta for gamma)
    */
   private static final String LINE_FORMAT = "%-4d %-40s %s";
   /** the base clauses of the logical world */
   private LogicalWorld delta;
   /** the refutation clauses of the logical world */
   private LogicalWorld gamma;

   /** whether we should remove pure literals before processing */
   private boolean removePureLiterals = false;

   /** whether we should remove tautologies before processing */
   private boolean removeTautologies = false;

   /**
    * @param currentClause
    *           the current clause
    * @param otherClause
    *           the other clause
    * @return a new clause representing the combination of the two given clauses
    */
   protected LogicalClause combine(final LogicalClause currentClause, final LogicalClause otherClause)
   {
      final LogicalClause newClause = new LogicalClause();
      final HashSet<LogicalUnit> alreadySeen = new HashSet<>();

      for (final LogicalUnit u : currentClause)
      {
         // if we haven't already processed a duplicate term
         if (!alreadySeen.contains(u))
         {
            // remember we looked at it
            alreadySeen.add(u);
            boolean found = false;

            // now compare it to every unit in the other clause
            for (final LogicalUnit u2 : otherClause)
            {
               // if there's a complement in the other clause, then remember we have seen it but don't copy it to the
               // output clause
               if (u.complement(u2))
               {
                  alreadySeen.add(u2);
                  found = true;
               }
            }

            // if we never saw a complement of this unit, then we can add this unit to the output clause
            if (!found)
            {
               newClause.add(u);
            }
         } // not already seen
      }

      // now copy everything in the second clause that we have not already processed
      for (final LogicalUnit u : otherClause)
      {
         if (!alreadySeen.contains(u))
         {
            newClause.add(u);
         }
      }

      return newClause;
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#finalize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void finalize(final LogicalWorld d, final LogicalWorld g)
   {
      // do nothing
   }

   /**
    * @return the delta
    */
   public LogicalWorld getDelta()
   {
      return delta;
   }

   /**
    * @return the gamma
    */
   public LogicalWorld getGamma()
   {
      return gamma;
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.strategy.ILogicStrategy#getResult()
    */
   @Override
   public LogicalWorld getResult()
   {
      final LogicalWorld w = new LogicalWorld();
      w.getClauses().addAll(getDelta().getClauses());
      w.getClauses().addAll(getGamma().getClauses());
      return getDelta();
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#initialize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld base, final LogicalWorld refutation)
   {
      delta = new LogicalWorld();
      delta.addAll(base.getClauses());

      gamma = new LogicalWorld();
      gamma.addAll(refutation.getClauses());

      if (removePureLiterals)
      {
         PureLiteralRemoval.removePureLiterals(delta);
      }
      if (removeTautologies)
      {
         TautologyRemoval.removeTautologies(delta);
      }

      int count = 1;
      for (final LogicalClause d : delta)
      {
         System.out.println(String.format(LINE_FORMAT, Integer.valueOf(count++), d.toString(), "D"));
      }
      for (final LogicalClause d : gamma)
      {
         System.out.println(String.format(LINE_FORMAT, Integer.valueOf(count++), d.toString(), "G"));
      }
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
    * check the world for any clauses that are identical to the given clause
    *
    * @param world
    *           the set of clauses to check
    * @param clauseToCheck
    *           the clause to check
    * @return {@code true} if the clause doesn't already exist in the given world, or {@code false} otherwise
    */
   protected boolean isUnique(final LogicalWorld world, final LogicalClause clauseToCheck)
   {
      // only add units to the derivation if they don't already exist
      for (final LogicalClause derived : world)
      {
         if (clauseToCheck.equals(derived))
         {
            return false;
         }
      }
      return true;
   }

   /**
    * @param delta
    *           the delta to set
    */
   public void setDelta(final LogicalWorld delta)
   {
      this.delta = delta;
   }

   /**
    * @param gamma
    *           the gamma to set
    */
   public void setGamma(final LogicalWorld gamma)
   {
      this.gamma = gamma;
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

}

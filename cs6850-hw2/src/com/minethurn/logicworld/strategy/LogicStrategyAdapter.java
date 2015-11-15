/**
 *
 */
package com.minethurn.logicworld.strategy;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minethurn.logicworld.clausal.LogicalClause;
import com.minethurn.logicworld.clausal.LogicalFunction;
import com.minethurn.logicworld.clausal.LogicalMapping;
import com.minethurn.logicworld.clausal.LogicalUnit;
import com.minethurn.logicworld.clausal.LogicalVariable;
import com.minethurn.logicworld.clausal.LogicalWorld;
import com.minethurn.logicworld.processor.DerivationLine;
import com.minethurn.logicworld.processor.PureLiteralRemoval;
import com.minethurn.logicworld.processor.TautologyRemoval;

/**
 *
 */
public abstract class LogicStrategyAdapter implements ILogicStrategy
{

   /** the logger */
   private final Logger logger = LogManager.getLogger(getClass());

   /** the base clauses of the logical world */
   private LogicalWorld world;

   /** whether we should remove pure literals before processing */
   private boolean removePureLiterals = false;

   /** whether we should remove tautologies before processing */
   private boolean removeTautologies = false;

   /** the mapping to use between the current clause and the other clause */
   protected LogicalMapping currentMapping;

   /** the current clause we are checking */
   private int currentClauseIndex;

   /** the other clause we compare to the current clause */
   private int otherClauseIndex;

   /** the current unit we are comparing */
   private int currentUnitIndex;

   /**
    * @param currentClause
    *           the current clause
    * @param otherClause
    *           the other clause
    * @param mapping
    *           the mapping to use between the variables and the entities, if any
    * @return a new clause representing the combination of the two given clauses
    */
   protected LogicalClause combine(final LogicalClause currentClause, final LogicalClause otherClause,
         final LogicalMapping mapping)
   {
      final LogicalClause newClause = new LogicalClause();
      final HashSet<LogicalUnit> alreadySeen = new HashSet<>();
      final LogicalClause mappedCurrent = currentClause.map(mapping);
      final LogicalClause mappedOther = otherClause.map(mapping);

      for (final LogicalUnit u : mappedCurrent)
      {
         // if we haven't already processed a duplicate term
         if (!alreadySeen.contains(u))
         {
            // remember we looked at it
            alreadySeen.add(u);
            boolean found = false;

            // now compare it to every unit in the other clause
            for (final LogicalUnit u2 : mappedOther)
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
      for (final LogicalUnit u : mappedOther)
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
    * find a mapping between two clauses. This will map the first variable to the first entity without regard for order.
    *
    * @param a
    *           the first clause in the mapping
    * @param b
    *           the second clause in the mapping
    * @param toMap
    *           the logical unit that we must map first
    * @return a logical mapping between the two clauses, if one exists. This may return an empty mapping, in which case
    *         no mapping is available.
    */
   protected LogicalMapping findMapping(final LogicalClause a, final LogicalClause b, final LogicalUnit toMap)
   {
      final LogicalMapping mapping = new LogicalMapping();
      final ArrayList<String> variables = new ArrayList<>();
      final ArrayList<String> entities = new ArrayList<>();

      // TODO worry about sub-functions and sub-variables
      splitVariables(a, variables, entities);
      splitVariables(b, variables, entities);

      // now start mapping with the important unit
      if (toMap != null && toMap instanceof LogicalFunction)
      {
         for (final LogicalUnit u : ((LogicalFunction) toMap).getVariables())
         {
            if (u instanceof LogicalVariable && !((LogicalVariable) u).isEntity() && entities.size() > 0)
            {
               mapping.put(u.getName(), entities.remove(0));
               variables.remove(u.getName());
            }
         }
      }
      for (final String v : variables)
      {
         if (entities.size() > 0)
         {
            mapping.put(v, entities.remove(0));
         }
      }
      return mapping;
   }

   /**
    * find the next complementary clause to the given unit
    *
    * @param unitToMatch
    *           the unit for which to find the complement, or a mapped complement
    * @return the clause which represents the complement to the given unit, or {@code null} if there are no matches
    */
   protected LogicalClause findNextComplementaryClause(final LogicalUnit unitToMatch)
   {
      final LogicalWorld curWorld = getWorld();
      while (otherClauseIndex < curWorld.size())
      {
         LogicalClause otherClause = curWorld.getClause(otherClauseIndex);
         otherClauseIndex++;

         if (otherClause.size() > 0)
         {
            for (final LogicalUnit otherUnit : otherClause)
            {
               // if we found an exact match, we are done here
               if (unitToMatch.complement(otherUnit))
               {
                  return otherClause;
               }
            }

            // no exact matches, can we find a mapping?
            if (unitToMatch instanceof LogicalFunction
                  || unitToMatch instanceof LogicalVariable && !((LogicalVariable) unitToMatch).isEntity())
            {
               currentMapping = findMapping(curWorld.getClause(currentClauseIndex), otherClause, unitToMatch);
               if (currentMapping != null && currentMapping.size() > 0)
               {
                  otherClause = otherClause.map(currentMapping);
                  for (final LogicalUnit otherUnit : otherClause)
                  {
                     // if we found an exact match, we are done here
                     if (unitToMatch.complement(otherUnit))
                     {
                        return otherClause;
                     }
                  } // for each logical unit in mapped clause
               } // if there is a mapping
            } // if the clause is not empty
         }
      } // while there are more clauses to try

      otherClauseIndex = 0;
      return null;
   }

   /**
    * @return the currentClauseIndex
    */
   public int getCurrentClauseIndex()
   {
      return currentClauseIndex;
   }

   /**
    * @return the currentMapping
    */
   public LogicalMapping getCurrentMapping()
   {
      return currentMapping;
   }

   /**
    * @return the currentUnitIndex
    */
   public int getCurrentUnitIndex()
   {
      return currentUnitIndex;
   }

   /**
    * return the next primary clause we should compare
    *
    * @return the next clause to compare, or {@code null} if no clauses left to process
    */
   protected LogicalClause getNextPrimaryClause()
   {
      if (getCurrentClauseIndex() < getWorld().size())
      {
         return getWorld().getClause(getCurrentClauseIndex());
      }

      // no reset for primary clause index. We are done at this point.
      return null;
   }

   /**
    * return the next term we should check against all the other clauses
    *
    * @param primaryClause
    * @return the next primary clause to return
    */
   protected LogicalUnit getNextPrimaryTerm(final LogicalClause primaryClause)
   {
      if (getCurrentUnitIndex() < primaryClause.size())
      {
         return primaryClause.get(getCurrentUnitIndex());
      }

      setCurrentUnitIndex(0);
      resetSecondaryClauseIndex();
      return null;
   }

   /**
    * @return the next secondary clause to check
    */
   protected LogicalClause getNextSecondaryClause()
   {
      if (getOtherClauseIndex() < getWorld().size())
      {
         return getWorld().getClause(getOtherClauseIndex());
      }

      resetSecondaryClauseIndex();
      return null;
   }

   /**
    * @return the otherClauseIndex
    */
   public int getOtherClauseIndex()
   {
      return otherClauseIndex;
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.strategy.ILogicStrategy#getResult()
    */
   @Override
   public LogicalWorld getResult()
   {
      final LogicalWorld w = new LogicalWorld();
      w.getClauses().addAll(getWorld().getClauses());
      return w;
   }

   /**
    * @return the world
    */
   public LogicalWorld getWorld()
   {
      return world;
   }

   /**
    *
    */
   protected void incrementPrimaryClauseIndex()
   {
      setCurrentClauseIndex(getCurrentClauseIndex() + 1);
   }

   /**
    *
    */
   protected void incrementPrimaryUnitIndex()
   {
      setCurrentUnitIndex(getCurrentUnitIndex() + 1);
   }

   /**
    *
    */
   protected void incrementSecondaryClauseIndex()
   {
      setOtherClauseIndex(getOtherClauseIndex() + 1);
   }

   /*
    * (non-Javadoc)
    * @see com.minethurn.logicworld.processor.ILogicStrategy#initialize(com.minethurn.logicworld.clausal.LogicalWorld,
    * com.minethurn.logicworld.clausal.LogicalWorld)
    */
   @Override
   public void initialize(final LogicalWorld base, final LogicalWorld refutation)
   {
      currentClauseIndex = 0;
      otherClauseIndex = 0;
      currentUnitIndex = 0;

      world = new LogicalWorld();
      world.addAll(base.getClauses());
      world.addAll(refutation.getClauses());

      if (removePureLiterals)
      {
         PureLiteralRemoval.removePureLiterals(world);
      }
      if (removeTautologies)
      {
         TautologyRemoval.removeTautologies(world);
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
    * @param clauseToCheck
    *           the clause to check
    * @return {@code true} if the clause doesn't already exist in the given world, or {@code false} otherwise
    */
   protected boolean isUnique(final LogicalClause clauseToCheck)
   {
      // only add units to the derivation if they don't already exist
      for (final LogicalClause derived : getWorld())
      {
         if (clauseToCheck.equals(derived))
         {
            return false;
         }
      }
      return true;
   }

   /**
    * override this method to change what the default initial value of the secondary clause index
    */
   protected void resetSecondaryClauseIndex()
   {
      setOtherClauseIndex(getCurrentClauseIndex() + 1);
   }

   /**
    * @param currentClauseIndex
    *           the currentClauseIndex to set
    */
   public void setCurrentClauseIndex(final int currentClauseIndex)
   {
      this.currentClauseIndex = currentClauseIndex;
   }

   /**
    * @param currentMapping
    *           the currentMapping to set
    */
   public void setCurrentMapping(final LogicalMapping currentMapping)
   {
      this.currentMapping = currentMapping;
   }

   /**
    * @param currentUnitIndex
    *           the currentUnitIndex to set
    */
   public void setCurrentUnitIndex(final int currentUnitIndex)
   {
      this.currentUnitIndex = currentUnitIndex;
   }

   /**
    * @param otherClauseIndex
    *           the otherClauseIndex to set
    */
   public void setOtherClauseIndex(final int otherClauseIndex)
   {
      this.otherClauseIndex = otherClauseIndex;
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
    * @param initial
    *           the world to set
    */
   public void setWorld(final LogicalWorld initial)
   {
      this.world = initial;
   }

   /**
    * @param a
    * @param variables
    * @param entities
    */
   protected void splitVariables(final LogicalClause a, final ArrayList<String> variables,
         final ArrayList<String> entities)
   {
      for (final LogicalUnit u : a)
      {
         if (u instanceof LogicalVariable)
         {
            final String name = u.getName();
            if (((LogicalVariable) u).isEntity())
            {
               if (!entities.contains(name))
               {
                  entities.add(name);
               }
            }
            else if (!variables.contains(name))
            {
               variables.add(name);
            }
         }
         else if (u instanceof LogicalFunction)
         {
            for (final LogicalVariable v : ((LogicalFunction) u).getVariables())
            {
               final String name = v.getName();
               if (v.isEntity())
               {
                  if (!entities.contains(name))
                  {
                     entities.add(name);
                  }
               }
               else if (!variables.contains(name))
               {
                  variables.add(name);
               }
            }
         }
      }
   }

   /**
    * The adapter iterates over the every unit in every clause, finds a complementary unit, and then combines them.
    * Derived classes can override {@link #findNextComplementaryClause(LogicalUnit)} to change how it finds the
    * complementary term.
    */
   @Override
   public DerivationLine step()
   {
      LogicalClause primaryClause;
      LogicalClause secondaryClause;
      LogicalUnit primaryUnit;

      while ((primaryClause = getNextPrimaryClause()) != null)
      {
         while ((primaryUnit = getNextPrimaryTerm(primaryClause)) != null)
         {
            logger.printf(Level.INFO, "primary unit = (%d) %s => %s", Integer.valueOf(getCurrentClauseIndex() + 1),
                  primaryClause, primaryUnit);
            while ((secondaryClause = getNextSecondaryClause()) != null)
            {
               logger.printf(Level.DEBUG, "  secondaryClause = (%d) %s", Integer.valueOf(getOtherClauseIndex() + 1),
                     secondaryClause);
               incrementSecondaryClauseIndex();

               currentMapping = findMapping(primaryClause, secondaryClause, primaryUnit);

               final LogicalClause mappedPrimary = primaryClause.map(currentMapping);
               final LogicalClause mappedSecondary = secondaryClause.map(currentMapping);

               primaryUnit = getNextPrimaryTerm(mappedPrimary);
               logger.printf(Level.DEBUG, "  mapped primary unit = (%d) %s => %s",
                     Integer.valueOf(getCurrentClauseIndex() + 1), mappedPrimary, primaryUnit);

               for (final LogicalUnit secondaryUnit : mappedSecondary)
               {
                  logger.printf(Level.DEBUG, "  mapped secondary unit = (%d) %s => %s",
                        Integer.valueOf(getOtherClauseIndex()), mappedSecondary, secondaryUnit);

                  if (primaryUnit.complement(secondaryUnit))
                  {
                     final LogicalClause derived = combine(mappedPrimary, mappedSecondary, currentMapping);
                     if (isUnique(derived))
                     {
                        final DerivationLine derivedLine = new DerivationLine(derived, getCurrentClauseIndex(),
                              getOtherClauseIndex() - 1);

                        getWorld().add(derived);
                        return derivedLine;
                     }
                     logger.debug("    duplicate");
                  }
               } // for each unit in the secondary clause
            }
            incrementPrimaryUnitIndex();
         } // while there are more secondary clauses to check
         incrementPrimaryClauseIndex();
      } // while more primary clauses to check

      return null;
   }

}

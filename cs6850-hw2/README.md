The program will run a given logic strategy on a set of logical clauses. Hopefully, this will give you an answer 
to the proposition the clauses were intending to solve.

To run the program, run the class 
    
    com.minethurn.logicworld.processor.LogicProcessor
    
    Usage: 
    
     -d,--delta <arg>      The file with the original database of statements
     -g,--gamma <arg>      The file with the query or resolution clauses
     -h,--help             Show the usage help
     -l,--rmLiterals       Remove pure literals before processing
     -t,--rmTautology      Remove tautologies before processing
     -s,--strategy <arg>   The strategy to use to find resolution.  Can be one of:
                           com.minethurn.logicworld.strategy.DefaultLogicStrategy,
                           com.minethurn.logicworld.strategy.DeletionResolutionStrategy,
                           com.minethurn.logicworld.strategy.InputResolutionStrategy
                           com.minethurn.logicworld.strategy.UnitResolutionStrategy
     
 The input files should be of the form:
 
    { A(x), ¬B(x) }
    { B(x) }{¬A(x) }

The parser assumes that lower case names are variables and upper case names are entities. So when a mapping occurs, it will map lowers case variables into upper case names.

So I have this lingering suspicion that I will have to combine two clauses, where one has both entities and variables. The entities will already form a complement, so these two clauses can be combined. But this clause will conceivably have a mapping where a term becomes another complement, but That second term wil not be matched because the mapping will not be applied because the previous term matched.

    { Func(A), ¬F2(B) }
    { F2(B), ¬Func(x), C }
    
So with a mapping of x/A would cause Func(A) to match ¬Func(x), but this won't happen because the mapping only occurs after the terms are first scanned for an exact match.


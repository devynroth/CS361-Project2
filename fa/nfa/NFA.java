package fa.nfa;

import fa.State;
import fa.dfa.DFA;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class NFA implements NFAInterface {
    // start state
    private NFAState startState;
    // HashMap to store transitions
    private final HashMap<String, String> transitions;
    // LinkedHashSet to store states
    private final LinkedHashSet<NFAState> states;
    // contains the alphabet in the language
    private final LinkedHashSet<Character> alphabet;

    public NFA() {
        states = new LinkedHashSet<>();
        transitions = new HashMap<>();
        alphabet = new LinkedHashSet<>();
    }

    /**
     * Check if start state is also a final state. Then create start state.
     *
     * @param name is the label of the start state
     */
    @Override
    public void addStartState(String name) {
        for (Object state : states.toArray()) {
            if (((NFAState) state).getName().equals(name)) {
                startState = (NFAState) state;
                return;
            }
        }
        startState = new NFAState(name, false);
        states.add(new NFAState(name, false));
    }

    /**
     * Adds a new state to the states set.
     *
     * @param name is the label of the state
     */
    @Override
    public void addState(String name) {
        states.add(new NFAState(name, false));
    }

    /**
     * Add final state to states set.
     *
     * @param name is the label of the state
     */
    @Override
    public void addFinalState(String name) {
        states.add(new NFAState(name, true));
    }

    /**
     * Adds a transition to transitions hashmap.
     *
     * @param fromState is the label of the state where the transition starts
     * @param onSymb    is the symbol from the NFA's alphabet.
     * @param toState   is the label of the state where the transition ends
     */
    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
        // if onSymb is not in alphabet, add it
        alphabet.add(onSymb);
        // check if start state and character have been used already
        if (transitions.get(fromState + onSymb) == null) {
            // redo addition to hashmap and append new state to end of string
            transitions.put(fromState + onSymb, transitions.get(fromState + onSymb) + " " + toState);
        } else {
            transitions.put(fromState + onSymb, toState);
        }
    }

    /**
     * Returns the set of all states.
     *
     * @return collection of all states
     */
    @Override
    public Set<? extends State> getStates() {
        return states;
    }

    /**
     * Iterate through all states and return all final states.
     *
     * @return set of final states
     */
    @Override
    public Set<? extends State> getFinalStates() {
        LinkedHashSet<NFAState> temp = new LinkedHashSet<>();
        for (Object state : states.toArray())
            if (((NFAState) state).isFinalState())
                temp.add((NFAState) state);
        return temp;
    }

    /**
     * Returns the start state.
     *
     * @return start state
     */
    @Override
    public State getStartState() {
        return startState;
    }

    /**
     * Returns a set of the alphabet.
     *
     * @return the alphabet
     */
    @Override
    public Set<Character> getABC() {
        return alphabet;
    }

    /**
     * Converts the NFA to a DFA and returns said DFA.
     *
     * @return equivalent DFA
     */
    @Override
    public DFA getDFA() {
        return null;
    }

    /**
     * Gets all possible next states given a start position and a symbol.
     *
     * @param from   - the source state
     * @param onSymb - the label of the transition
     * @return set of next possible states
     */
    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        // initialize hashset to return possible transition states
        LinkedHashSet<NFAState> possibleStates = new LinkedHashSet<>();
        // get next transition(s) and create tokenizer to iterate through next state(s)
        String next = transitions.get(from.getName() + onSymb);
        StringTokenizer tk = new StringTokenizer(next, " ");
        String nextToken;
        // iterate through all possible next states and find their respective NFAState object
        while (tk.hasMoreTokens()) {
            nextToken = tk.nextToken();
            if (from.getName().equals(nextToken))
                possibleStates.add(from);
            if (startState.getName().equals(nextToken))
                possibleStates.add(startState);
            else
                for (Object state : states.toArray())
                    if (((NFAState) state).getName().equals(nextToken))
                        possibleStates.add((NFAState) state);
        }
        // return all possible next states
        return possibleStates;
    }

    /**
     * Return a set of which states can be reached from the given state on only empty transitions.
     *
     * @param s state to start at
     * @return set of states which can be reached from s on empty transitions
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        return null;
    }
}

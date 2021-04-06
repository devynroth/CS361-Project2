package fa.nfa;

import fa.State;
import fa.dfa.DFA;

import java.util.*;

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
     * TODO: look into error from p1 on start states not working as final states
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
            transitions.put(fromState + onSymb, toState);
        } else {
            // redo addition to hashmap and append new state to end of string
            transitions.put(fromState + onSymb, transitions.get(fromState + onSymb) + " " + toState);
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
     * TODO: Implement getDFA method
     *
     * @return equivalent DFA
     */
    @Override
    public DFA getDFA() {
        // String storing start state to add to DFA later
        String dfaStartState = "";
        // set to store a list of states in the DFA
        Set<String> dfaStates = new LinkedHashSet<>();
        // hashmap to store dfa state transitions (i.e. "ABC1", "ABD")
        HashMap<String, String> dfaTransitions = new HashMap<>();
        // queue to store next DFA states to explore
        Queue<String> stateQueue = new ArrayDeque<>();
        // add start state to begin
        stateQueue.add(startState.getName());
        boolean isStartState = true;
        while (!stateQueue.isEmpty()) {
            // remove the next state from the queue
            String currentState = stateQueue.remove();
            // ENSURING CURRENT STATE IS ACCURATE AND FULL
            // COMPLETE: get eclosure of currentState
            Set<NFAState> eClosure = eClosureOfStates(currentState);
            // COMPLETE: reassign current state to output of nfaSetToAlphabetizedString
            currentState = nfaSetToAlphabetizedString(eClosure);
            // COMPLETE: if currentState is not the start state, add it to dfaStates. if it is the start state, assign it to dfaStartState
            if (isStartState) {
                isStartState = false;
                dfaStartState = currentState;
            } else {
                dfaStates.add(currentState);
            }
            // ADDING TRANSITIONS TO HASHMAP
            // iterate through each letter of the alphabet
            for (Object character : alphabet.toArray()) {
                String nextState = "";
                // for each NFA state in currentState
                for (int i = 0; i < currentState.length(); i++) {
                    // TODO: use getToState to find all possible next states from the current states
                    // TODO: if the next state(s) are not already in nextState, add them IN ALPHABETICAL ORDER
                }
                // TODO: if nextState is not already in stateQueue or dfaStates, add nextState to stateQueue
                // TODO: add transition to dfaTransitions in form of {<currentState><transition character>, <nextState> (i.e. {"ABC0", "BDE"})
            }
        }
        // once stateQueue is empty, convert dfaStates to single-character values rather than multi character values
        // TODO: map each state in dfaStates to a single-character string to comply with DFA naming conventions
        // nextDFAState can be incremented with nextDFAState++ when naming the states in dfaStates
        char nextDFAState = 'a';
        // TODO: adapt dfaTransitions to comply with the new naming scheme for state names
        // NOTE: these transition names can be changed when they are added to the dfa, or before
        DFA dfa = new DFA();
        // TODO: add states and transitions to dfa object using new state names
        // NOTE: WHEN ADDING STATES, CHECK IF THE STATE CONTAINS A FINAL STATE. IF SO, USE dfa.addFinalState
        return dfa;
    }

    /**
     * Given a set of NFA states, returns the names of each state in a string in alphabetical order
     * TODO: implement nfaSetToAlphabetizedString
     *
     * @param states - Set of states to be alphabetized and converted to a string
     * @return - String of states in alphabetical order
     */
    private String nfaSetToAlphabetizedString(Set<NFAState> states) {
        String output = "";
        return output;
    }

    /**
     * Method to find and return a target NFAState
     *
     * @param target - Name of the target state
     * @return - Target NFAState object, or null if the given state does not exist
     */
    private NFAState getState(String target) {
        for (NFAState nfaState : (NFAState[]) states.toArray())
            if (nfaState.getName().equals(target))
                return nfaState;
        return null;
    }

    /**
     * Gets all possible next states given a start position and a symbol.
     * TODO: there may be an infinite loop between getToState and eClosure calling each other
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
        // account for empty transitions
        LinkedHashSet<NFAState> allPossibleStates = new LinkedHashSet<>();
        for (Object state : allPossibleStates.toArray())
            for (Object eclosureState : eClosure((NFAState) state).toArray())
                allPossibleStates.add((NFAState) eclosureState);
        // return all possible next states
        return allPossibleStates;
    }

    /**
     * Returns the eClosure of the given NFA states as a Set of NFAStates
     *
     * @param states - String containing names of all states to get the eclosure of
     * @return - Set of NFAStates
     */
    private Set<NFAState> eClosureOfStates(String states) {
        LinkedHashSet<NFAState> output = new LinkedHashSet<>();
        // for each state in states, if the character is not a space
        for (int i = 0; i < states.length(); i++) {
            if (states.charAt(i) != ' ') {
                // get the NFAState of the current state
                NFAState currentState = getState(Character.toString(states.charAt(i)));
                // if we have not alreaedy visited the current state
                if (!output.contains(currentState)) {
                    // get its eclosure and add all states to output
                    Set<NFAState> eClosureOfCurrentState = eClosure(currentState);
                    for (Object state : eClosureOfCurrentState.toArray())
                        output.add((NFAState) state);
                }
            }
        }
        return output;
    }

    /**
     * Return a set of which states can be reached from the given state on only empty transitions.
     *
     * @param s state to start at
     * @return set of states which can be reached from s on empty transitions
     */
    @Override
    public Set<NFAState> eClosure(NFAState s) {
        // get all next states which can be reaches on an empty transition
        Set<NFAState> nextStates = getToState(s, 'e');
        // for each next possible state, see if there are further states which can be reached
        for (Object state : nextStates.toArray())
            // for each state returned by eClosureRecursive, add it to nextStates
            for (Object level2State : eClosureRecursive((NFAState) state, nextStates))
                nextStates.add((NFAState) level2State);
        return nextStates;
    }

    /**
     * Recursively finds any potential states which can be reached on
     * an empty transition.
     *
     * @param s              starting state
     * @param previousStates set of all previously explored states
     * @return set of all possible states
     */
    private Set<NFAState> eClosureRecursive(NFAState s, Set<NFAState> previousStates) {
        // get all next states which can be reaches on an empty transition
        Set<NFAState> nextStates = getToState(s, 'e');
        Set<NFAState> output = previousStates;
        // for each next possible state, see if there are further states which can be reached
        for (Object state : nextStates.toArray())
            // if the next state has already been explored, ignore it
            if (!output.contains((NFAState) state)) {
                // otherwise, add it to output
                output.add((NFAState) state);
                // for each state returned by eClosureRecursive, add it to nextStates
                for (Object level2State : eClosureRecursive((NFAState) state, output))
                    output.add((NFAState) level2State);
            }
        return output;
    }
}

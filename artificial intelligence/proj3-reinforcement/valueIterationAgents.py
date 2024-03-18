# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


import mdp, util

from learningAgents import ValueEstimationAgent
import collections

class ValueIterationAgent(ValueEstimationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A ValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100):
        """
          Your value iteration agent should take an mdp on
          construction, run the indicated number of iterations
          and then act according to the resulting policy.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state, action, nextState)
              mdp.isTerminal(state)
        """
        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        self.values = util.Counter() # A Counter is a dict with default 0
        self.runValueIteration()

    def runValueIteration(self):
        # Write value iteration code here
        "*** YOUR CODE HERE ***"
        states = self.mdp.getStates()

        for i in range(self.iterations):
            new_values = util.Counter()
            for state in states:
                best_value = -10e10
                for action in self.mdp.getPossibleActions(state):
                    value = self.getQValue(state, action)
                    best_value = max(best_value, value)
                new_values[state] = 0 if not self.mdp.getPossibleActions(state) else best_value
            self.values = new_values



    def getValue(self, state):
        """
          Return the value of the state (computed in __init__).
        """
        return self.values[state]


    def computeQValueFromValues(self, state, action):
        """
          Compute the Q-value of action in state from the
          value function stored in self.values.
        """
        "*** YOUR CODE HERE ***"
        tuples = self.mdp.getTransitionStatesAndProbs(state, action)
        result = 0
        for (next_state, probability) in tuples:
            value = self.discount * self.values[next_state]
            value += self.mdp.getReward(state, action, next_state)
            value *= probability
            result += value
        return result

    def computeActionFromValues(self, state):
        """
          The policy is the best action in the given state
          according to the values currently stored in self.values.

          You may break ties any way you see fit.  Note that if
          there are no legal actions, which is the case at the
          terminal state, you should return None.
        """
        "*** YOUR CODE HERE ***"

        best_value = -10e10
        best_action = None
        for action in self.mdp.getPossibleActions(state):
            value = self.getQValue(state,action)
            if best_value < value:
                best_value = value
                best_action = action
        return best_action


    def getPolicy(self, state):
        return self.computeActionFromValues(state)

    def getAction(self, state):
        "Returns the policy at the state (no exploration)."
        return self.computeActionFromValues(state)

    def getQValue(self, state, action):
        return self.computeQValueFromValues(state, action)

class AsynchronousValueIterationAgent(ValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        An AsynchronousValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs cyclic value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 1000):
        """
          Your cyclic value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy. Each iteration
          updates the value of only one state, which cycles through
          the states list. If the chosen state is terminal, nothing
          happens in that iteration.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state)
              mdp.isTerminal(state)
        """
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        "*** YOUR CODE HERE ***"
        states = self.mdp.getStates()

        for i in range(self.iterations):
            state = states[i%len(states)]
            best_value = -10e10
            for action in self.mdp.getPossibleActions(state):
                value = self.getQValue(state, action)
                best_value = max(best_value, value)
            self.values[state] = 0 if not self.mdp.getPossibleActions(state) else best_value

class PrioritizedSweepingValueIterationAgent(AsynchronousValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A PrioritizedSweepingValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs prioritized sweeping value iteration
        for a given number of iterations using the supplied parameters.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100, theta = 1e-5):
        """
          Your prioritized sweeping value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy.
        """
        self.theta = theta
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        "*** YOUR CODE HERE ***"
        predecessors = self.getPredecessors()
        pqueue = self.fillPriorityQueue()

        for i in range(self.iterations):
            if not pqueue.isEmpty():
                state = pqueue.pop()
                if state != 'TERMINAL_STATE':
                    self.values[state] = self.getBestQValue(state)
                    for predecessor in predecessors[state]:
                        best_q = self.getBestQValue(predecessor)
                        diff = abs(self.values[predecessor] - best_q)
                        if diff > self.theta:
                            pqueue.update(predecessor, -diff)

    def getPredecessors(self):
        predecessors = dict()
        for state in self.mdp.getStates():
            predecessors[state] = set()
        for state in self.mdp.getStates():
            for action in self.mdp.getPossibleActions(state):
                for t_state, probability in self.mdp.getTransitionStatesAndProbs(state, action):
                    if probability != 0:
                        predecessors[t_state].add(state)
        return predecessors

    def fillPriorityQueue(self):
        pqueue = util.PriorityQueue()
        for state in self.mdp.getStates():
            if state != 'TERMINAL_STATE':
                best_q = self.getBestQValue(state)
                diff = abs(best_q - self.values[state])
                pqueue.push(state, -diff)
        return pqueue

    def getBestQValue(self, state):
        best_q = -10e10
        for action in self.mdp.getPossibleActions(state):
            q = self.getQValue(state, action)
            best_q = max(best_q, q)
        if best_q == -10e10:
            best_q = self.values[state]
        return best_q

# multiAgents.py
# --------------
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


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent


class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """

    def getAction(self, gameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices)  # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        evaluation = successorGameState.getScore() * 50

        food_distances = [manhattanDistance(newPos, food) for food in newFood.asList()]
        min_food_distance = -min(food_distances) if food_distances else 0

        ghost_distances = [manhattanDistance(newPos, ghostState.getPosition()) for ghostState in newGhostStates]
        min_ghost_distance = min(ghost_distances) if ghost_distances else 0

        action_score = 0
        if action == "Stop":
            action_score = -1

        evaluation += 100 * -len(newFood.asList()) + 100 * action_score + 15 * min_food_distance + 10 * min_ghost_distance

        return evaluation
        # return successorGameState.getScore()


def scoreEvaluationFunction(currentGameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()


class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn='scoreEvaluationFunction', depth='2'):
        self.index = 0  # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)


class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"

        actions = gameState.getLegalActions(0)  # Pacman's legal actions
        best_action = None
        best_value = -10e10

        for action in actions:
            successor = gameState.generateSuccessor(0, action)
            value = self.minimax(successor, self.depth, 1)
            if value > best_value:
                best_value = value
                best_action = action

        return best_action

    def minimax(self, state, depth, agent_index) -> float:
        if depth == 0 or state.isWin() or state.isLose():
            return self.evaluationFunction(state)

        next_agent = (agent_index + 1) % state.getNumAgents()
        next_depth = depth - 1 if next_agent == 0 else depth
        best_value = -10e10 if agent_index == 0 else 10e10
        func = max if agent_index == 0 else min

        actions = state.getLegalActions(agent_index)
        for action in actions:
            successor = state.generateSuccessor(agent_index, action)
            value = self.minimax(successor, next_depth, next_agent)
            best_value = func(best_value, value)
        return best_value


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        actions = gameState.getLegalActions(0)  # Pacman's legal actions
        best_action = None
        best_value = -10e10
        a = -10e10
        b = 10e10

        for action in actions:
            successor = gameState.generateSuccessor(0, action)
            value = self.minimax(successor, self.depth, 1, a, b)
            if value > best_value:
                best_value = value
                best_action = action
            a = max(a, best_value)

        return best_action

    def minimax(self, state, depth, agent_index, a, b) -> float:
        if depth == 0 or state.isWin() or state.isLose():
            return self.evaluationFunction(state)

        next_agent = (agent_index + 1) % state.getNumAgents()
        next_depth = depth - 1 if next_agent == 0 else depth
        best_value = -10e10 if agent_index == 0 else 10e10
        func = max if agent_index == 0 else min

        actions = state.getLegalActions(agent_index)
        for action in actions:
            successor = state.generateSuccessor(agent_index, action)
            value = self.minimax(successor, next_depth, next_agent, a, b)
            best_value = func(best_value, value)
            if agent_index == 0:
                if best_value > b:
                    return best_value
                a = func(a, best_value)
            else:
                if best_value < a:
                    return best_value
                b = func(b, best_value)

        return best_value


class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        actions = gameState.getLegalActions(0)  # Pacman's legal actions
        best_action = None
        best_value = -10e10

        for action in actions:
            successor = gameState.generateSuccessor(0, action)
            value = self.minimax(successor, self.depth, 1)
            if value > best_value:
                best_value = value
                best_action = action

        return best_action

    def minimax(self, state, depth, agent_index) -> float:
        if depth == 0 or state.isWin() or state.isLose():
            return self.evaluationFunction(state)

        next_agent = (agent_index + 1) % state.getNumAgents()
        next_depth = depth - 1 if next_agent == 0 else depth
        best_value = -10e10 if agent_index == 0 else 0

        actions = state.getLegalActions(agent_index)
        for action in actions:
            successor = state.generateSuccessor(agent_index, action)
            value = self.minimax(successor, next_depth, next_agent)
            if agent_index == 0:
                best_value = max(best_value, value)
            else:
                probability = 1 / len(actions)
                best_value += probability * value

        return best_value


def betterEvaluationFunction(currentGameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    newPos = currentGameState.getPacmanPosition()
    newFood = currentGameState.getFood()
    newGhostStates = currentGameState.getGhostStates()
    newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

    "*** YOUR CODE HERE ***"
    evaluation = currentGameState.getScore() * 50

    food_distances = [manhattanDistance(newPos, food) for food in newFood.asList()]
    min_food_distance = -min(food_distances) if food_distances else 0

    ghost_distances = [manhattanDistance(newPos, ghostState.getPosition()) for ghostState in newGhostStates]
    min_ghost_distance = min(ghost_distances) if ghost_distances else 0

    evaluation += 100 * -len(newFood.asList()) + 15 * min_food_distance + 10 * min_ghost_distance

    return evaluation


# Abbreviation
better = betterEvaluationFunction

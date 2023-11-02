from bisect import bisect
from curses.ascii import isalnum
from dataclasses import dataclass
from typing import List, Set, Dict

EPSILON = 'E'


@dataclass
class NFA:
    states: List[Dict[str, Set[int]]]
    accept_states: Set[int]

    def union(self, other):
        start = {EPSILON: {1, len(self.states) + 1}}
        accept = {}

        for state in self.states:
            for c in state:
                transitions = set()
                for other_state in state[c]:
                    transitions.add(other_state + 1)
                state[c] = transitions

        self.states.insert(0, start)

        state = self.accept_states.copy().pop()
        self.accept_states.remove(state)
        self.states[state + 1] = {EPSILON: {len(self.states) + len(other.states)}}

        i = len(self.states)
        for state in other.states:
            for key_symbol in state:
                new_transitions = set()
                for other_state in state[key_symbol]:
                    new_transitions.add(other_state + i)
                state[key_symbol] = new_transitions

        self.states += other.states
        self.states.append(accept)

        for other_accept in other.accept_states:
            self.states[other_accept + i] = {EPSILON: {len(self.states) - 1}}
        self.accept_states = {len(self.states) - 1}

        return self

    def kleene(self):
        start = {EPSILON: {1, len(self.states) + 1}}
        accept = {}
        for state in self.states:
            transitions = set()
            for symbol in state:
                for to_state in state[symbol]:
                    transitions.add(to_state + 1)
                state[symbol] = transitions
        self.states.insert(0, start)

        state = list(self.accept_states)[0]
        self.accept_states.remove(state)
        self.states[state + 1] = {EPSILON: {1, len(self.states)}}

        self.states.append(accept)
        self.accept_states = {len(self.states) - 1}

        return self

    def concat(self, other):
        if len(other.states) == 0:
            return self
        if len(self.states) == 0:
            return other

        for from_state in other.states:
            for symbol in from_state:
                transitions = set()
                for to_state in from_state[symbol]:
                    transitions.add(to_state + len(self.states) - 1)
                from_state[symbol] = transitions

        self.accept_states = {other.accept_states.pop() + len(self.states) - 1}

        self.states.pop()
        self.states += other.states

        return self

    def delete_epsilon_transitions(self):
        count = -1
        while count != len(self.accept_states):
            count = len(self.accept_states)
            for i, to_state in enumerate(self.states):
                if EPSILON in to_state:
                    for from_state in to_state[EPSILON]:
                        if from_state in self.accept_states:
                            self.accept_states.add(i)
                            break

        walked = [set() for i in range(len(self.states))]
        while True:
            if sum([len(state[EPSILON]) for state in self.states if EPSILON in state]) == 0:
                break

            for i, state in enumerate(self.states):
                if EPSILON not in state:
                    continue
                if i in state[EPSILON]:
                    state[EPSILON].remove(i)
                epsilon = state[EPSILON].copy()

                walked[i].update(epsilon)

                for to_state in epsilon:
                    for symbol in self.states[to_state]:
                        if symbol == EPSILON:
                            for transition in self.states[to_state][EPSILON]:
                                if transition not in walked[i]:
                                    state[EPSILON].add(transition)
                        elif symbol in state:
                            state[symbol].update(self.states[to_state][symbol])
                        else:
                            state[symbol] = set(self.states[to_state][symbol])

                state[EPSILON].difference_update(epsilon)

        for state in self.states:
            if EPSILON in state:
                state.pop(EPSILON)

        return self

    def delete_invalid_states(self):
        reachable = set()
        for state in self.states:
            for symbol in state:
                for to_state in state[symbol]:
                    reachable.add(to_state)
        unreachable = set()
        for i in range(1, len(self.states)):
            if i not in reachable:
                unreachable.add(i)

        states = []
        for i, state in enumerate(self.states):
            if i not in unreachable:
                states.append(state)
            else:
                self.accept_states.discard(i)
        self.states = states

        for state in self.states:
            for symbol in state:
                transitions = set()
                for to_state in state[symbol]:
                    transitions.add(to_state - bisect(list(unreachable), to_state))
                state[symbol] = transitions

        states = set()
        for state in self.accept_states:
            states.add(state - bisect(list(unreachable), state))
        self.accept_states = states

        return self

    def next(self, state, symbol):
        if symbol in self.states[state]:
            return self.states[state][symbol]
        return set()

    def __str__(self):
        nfa = len(self.states).__str__() + ' ' + len(self.accept_states).__str__() + ' '
        transitions = 0
        for state in self.states:
            for symbol in state:
                transitions += len(state[symbol])
        nfa += transitions.__str__() + '\n'

        for state in self.accept_states:
            nfa += state.__str__() + ' '
        nfa += '\n'

        for state in self.states:
            count = 0
            for symbol in state:
                count += len(state[symbol])
            nfa += count.__str__() + ' '

            for c in state:
                for to in state[c]:
                    nfa += c + ' ' + to.__str__() + ' '
            nfa += '\n'

        return nfa


def is_unit(regex):
    return len(regex) == 1 and isinstance(regex, str) and isalnum(regex)

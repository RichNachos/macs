from typing import Dict, List, Set

from nfa import NFA

ACCEPT = "Y"
REJECT = "N"


def simulate(nfa: NFA, nfa_input: str):
    answer = ""
    states = {0}

    for symbol in nfa_input:
        new = set()
        for s in [nfa.next(state, symbol) for state in states]:
            new.update(s)
        states = new

        if len([state for state in states if state in nfa.accept_states]):
            answer += ACCEPT
        else:
            answer += REJECT
    return answer


def read_nfa() -> NFA:
    [n, _, _] = [int(x) for x in input().split(" ")]
    accepts = set([int(x) for x in input().split(" ")])
    states: List[Dict[str, Set[int]]] = []

    for i in range(n):
        states += [{}]
    nfa = NFA(states, accepts)

    for i in range(n):
        transitions = input().split(" ")
        index = 1
        for j in range(int(transitions[0])):
            symbol = transitions[index]
            state = int(transitions[index + 1])
            if symbol not in nfa.states[i]:
                nfa.states[i][symbol] = set()
            nfa.states[i][symbol].add(state)
            index += 2

    return nfa


def main():
    nfa_input = input()
    nfa = read_nfa()
    print(simulate(nfa, nfa_input))


if __name__ == "__main__":
    main()

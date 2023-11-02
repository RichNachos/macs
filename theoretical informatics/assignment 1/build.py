from nfa import EPSILON, NFA, is_unit


def singletons(regex):
    for i, c in enumerate(regex):
        if isinstance(c, NFA):
            break
        if is_unit(c):
            regex[i] = build_nfa(c)
    return regex


def brackets(regex):
    while regex.__contains__("("):
        open_index = regex.index("(")
        count = 0
        close_index = 0
        for i in range(open_index + 1, len(regex)):
            if isinstance(regex[i], str):
                if regex[i] == ")":
                    if count == 0:
                        close_index = i
                        break
                    else:
                        count -= 1
                if regex[i] == "(":
                    count += 1

        regex = (
            regex[:open_index]
            + [build_nfa(regex[open_index + 1 : close_index])]
            + regex[close_index + 1 :]
        )
    return regex


def concat(regex):
    i = 0
    while i + 1 < len(regex):
        i = 0
        if regex[i + 1] == "|":
            i += 2
            continue
        regex = regex[:i] + [regex[i].concat(regex[i + 1])] + regex[i + 2 :]
    return regex


def kleene(regex):
    while True:
        if not regex.__contains__("*"):
            break
        i = regex.index("*")
        regex = regex[: i - 1] + [regex[i - 1].kleene()] + regex[i + 1 :]
    return regex


def union(regex):
    for i in range(2, len(regex), 2):
        regex[0] = regex[0].union(regex[i])
    return regex


# Build nfa
def build_nfa(regex) -> NFA:
    if isinstance(regex, str):
        regex = regex.replace("()", EPSILON)

    if is_unit(regex):
        return NFA([{regex: {1}}, {}], {1})

    regex = list(regex)
    regex = singletons(regex)
    regex = brackets(regex)
    regex = kleene(regex)
    regex = concat(regex)
    regex = union(regex)
    regex = regex[0]

    return regex


def main():
    regex = input()
    nfa = build_nfa(regex).delete_epsilon_transitions().delete_invalid_states()

    print(nfa)


if __name__ == "__main__":
    main()

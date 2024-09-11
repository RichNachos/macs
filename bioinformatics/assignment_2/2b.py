import numpy as np

prob_N = {
    0: 0.1,
    1: 0.35,
    2: 0.25,
    3: 0.2,
    6: 0.1
}
prob_C = {
    0: 0.05,
    1: 0.15,
    2: 0.2,
    3: 0.3,
    6: 0.3
}

scores = [0, 1, 2, 3, 6]

probabilities_N = [prob_N[score] for score in scores]
probabilities_C = [prob_C[score] for score in scores]
print(probabilities_N)
print(probabilities_C)


def generate_sequence_from(probabilities):
    return np.random.choice(scores, size=10, p=probabilities)


def compute_probability(sequence, prob_model):
    return np.prod([prob_model[score] for score in sequence])


num_simulations = 10_000


def simulate(num_simulations, probabilities, compare):
    count = 0

    for _ in range(num_simulations):
        sequence = generate_sequence_from(probabilities)
        P_S_given_N = compute_probability(sequence, prob_N)
        P_S_given_C = compute_probability(sequence, prob_C)

        if compare(P_S_given_C, P_S_given_N):
            count += 1

    proportion = count / num_simulations

    print(f"P(S | C) > P(S | N) in {100 * proportion:.2f}% of the sequences.")


simulate(num_simulations, probabilities_N, lambda x, y: x > y)
simulate(num_simulations, probabilities_C, lambda x, y: x < y)

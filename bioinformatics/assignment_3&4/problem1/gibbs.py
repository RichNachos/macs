#!/usr/bin/env python
import sys
import string
import random

#### INSTRUCTIONS FOR USE:
# call program as follows: ./gibbs.py <Motif Length> <Data File>
# make sure the gibbs.py is marked as executable: chmod +x gibbs.py

alphabet = ['A', 'G', 'C', 'T']


#### GibbsSampler:
#### 	INPUTS:	S - list of sequences
####		L - length of motif
####	OUTPUT:	PWM - 4xL list with frequencies of each base at each position
####                  Order of bases should be consistent with alphabet variable
def GibbsSampler(S, L):
    # Initialize the motif positions randomly
    t = len(S)
    positions = [random.randint(0, len(S[i]) - L) for i in range(t)]
    best_motif = [S[i][positions[i]:positions[i] + L] for i in range(t)]

    def create_pwm(exclude_idx):
        pwm = [[1] * L for _ in range(4)]
        for i in range(t):
            if i == exclude_idx:
                continue
            motif = S[i][positions[i]:positions[i] + L]
            for j in range(L):
                if motif[j] == 'A':
                    pwm[0][j] += 1
                elif motif[j] == 'G':
                    pwm[1][j] += 1
                elif motif[j] == 'C':
                    pwm[2][j] += 1
                elif motif[j] == 'T':
                    pwm[3][j] += 1

        for j in range(L):
            col_sum = sum(pwm[i][j] for i in range(4))
            for i in range(4):
                pwm[i][j] /= col_sum

        return pwm

    # Motif scoring
    def score_motif(pwm, kmer):
        score = 1.0
        for j in range(L):
            if kmer[j] == 'A':
                score *= pwm[0][j]
            elif kmer[j] == 'G':
                score *= pwm[1][j]
            elif kmer[j] == 'C':
                score *= pwm[2][j]
            elif kmer[j] == 'T':
                score *= pwm[3][j]

        return score

    # Sampling
    def sample_new_position(seq, pwm):
        n = len(seq)
        scores = []

        for i in range(n - L + 1):
            kmer = seq[i:i + L]
            scores.append(score_motif(pwm, kmer))

        total_score = sum(scores)
        scores = [s / total_score for s in scores]
        return random.choices(range(n - L + 1), weights=scores, k=1)[0]

    # Main convergence loop
    converged = False
    it = 0
    while not converged and it < 500:
        it += 1
        for i in range(t):
            pwm = create_pwm(i)
            positions[i] = sample_new_position(S[i], pwm)

        new_motif = [S[i][positions[i]:positions[i] + L] for i in range(t)]
        if new_motif == best_motif:
            converged = True
        else:
            best_motif = new_motif

    final_pwm = create_pwm(-1)
    return final_pwm


###### YOUR OWN FUNCTIONS HERE
# optional -- feel free to add your own functions if you want to


###### END OF YOUR FUNCTIONS

def main():
    if len(sys.argv) != 3:
        print("Usage: ./gibbs.py <Motif Length> <Data File>")
        return

    L = int(sys.argv[1])
    datafile = sys.argv[2]
    S = readdata(datafile)

    P = GibbsSampler(S, L)

    print("     ", end="")
    for i in range(L):
        print("%-5d " % (i + 1), end="")
    print("")

    for j in range(len(alphabet)):
        print(" %s " % alphabet[j], end="")
        for i in range(L):
            print(" %5.3f" % P[j][i], end="")
        print("")


def readdata(file):
    data = []
    for line in open(file, 'r'):
        data.append(line[0:-1])
    return data


main()

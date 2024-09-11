def load_known_motifs(filename):
    with open(filename, 'r') as f:
        motifs = [line.strip().split(" ")[1] for line in f]
    return motifs[1:]


def compare_kmers_with_known_motifs(kmers, known_motifs):
    matched_motifs = [kmer for kmer in kmers if kmer in known_motifs]
    return matched_motifs


def load_kmers(filename):
    with open(filename, 'r') as f:
        kmers = [line.split(':')[0].strip() for line in f]
    return kmers


known_motifs_file = 'yeast_motifs.txt'
frequent_kmers_file = 'a1.txt'
conserved_kmers_file = 'a2.txt'

known_motifs = load_known_motifs(known_motifs_file)
print(known_motifs)

frequent_kmers = load_kmers(frequent_kmers_file)
conserved_kmers = load_kmers(conserved_kmers_file)

matched_frequent_kmers = compare_kmers_with_known_motifs(frequent_kmers, known_motifs)
matched_conserved_kmers = compare_kmers_with_known_motifs(conserved_kmers, known_motifs)

print("Matched Frequent K-mers with Known Motifs:")
print(matched_frequent_kmers)

print("\nMatched Conserved K-mers with Known Motifs:")
print(matched_conserved_kmers)

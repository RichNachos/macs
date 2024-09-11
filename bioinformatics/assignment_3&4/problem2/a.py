from collections import Counter


def read_file(filename):
    with open(filename, 'r') as f:
        return f.read().strip().split('\n')


def count_kmers_and_conserved_kmers(sequences, conservation, k=6):
    kmer_counts = Counter()
    conserved_kmer_counts = Counter()

    for seq, cons in zip(sequences, conservation):
        for i in range(len(seq) - k + 1):
            kmer = seq[i:i + k]
            conserved = cons[i:i + k]
            kmer_counts[kmer] += 1
            if all(c == '*' for c in conserved):
                conserved_kmer_counts[kmer] += 1

    return kmer_counts, conserved_kmer_counts


def calculate_conservation_proportion(kmer_counts, conserved_kmer_counts):
    conservation_proportion = {}

    for kmer in kmer_counts:
        total_count = kmer_counts[kmer]
        conserved_count = conserved_kmer_counts.get(kmer, 0)
        conservation_proportion[kmer] = conserved_count / total_count

    return conservation_proportion


def write_top_kmers_to_file(kmer_counts, conservation_proportion, top_n=50):
    with open('a1.txt', 'w') as f:
        for kmer, count in kmer_counts.most_common(top_n):
            f.write(f"{kmer}: {count}\n")

    with open('a2.txt', 'w') as f:
        sorted_conserved_kmers = sorted(conservation_proportion.items(), key=lambda item: item[1], reverse=True)
        for kmer, proportion in sorted_conserved_kmers[:top_n]:
            f.write(f"{kmer}: {proportion:.4f}\n")


sequences = read_file('allinter')
conservation = read_file('allintercons')

kmer_counts, conserved_kmer_counts = count_kmers_and_conserved_kmers(sequences, conservation)
conservation_proportion = calculate_conservation_proportion(kmer_counts, conserved_kmer_counts)

write_top_kmers_to_file(kmer_counts, conservation_proportion)

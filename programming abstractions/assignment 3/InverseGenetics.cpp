/*
 * File: InverseGenetics.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Inverse Genetics problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include <fstream>
#include "set.h"
#include "map.h"
#include "console.h"
#include "simpio.h"
#include "foreach.h"

using namespace std;

/* Function: listAllRNAStrandsFor(string protein,
 *                                Map<char, Set<string> >& codons);
 * Usage: listAllRNAStrandsFor("PARTY", codons);
 * ==================================================================
 * Given a protein and a map from amino acid codes to the codons for
 * that code, lists all possible RNA strands that could generate
 * that protein
 */
void goOverRNAStrands(string before, string protein, Map<char, Set<string> >& codons) {
	if (protein.length() == 0) {
		cout << before << endl;
		return;
	}
	
	char aminoAcid = protein[0];
	protein = protein.substr(1, protein.length() - 1);

	Set<string> rnaStrands = codons.get(aminoAcid);

	foreach (string rna in rnaStrands) {
		goOverRNAStrands(before + rna, protein, codons);
	}
}

void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons) {
	goOverRNAStrands("", protein, codons);
}

/* Function: loadCodonMap();
 * Usage: Map<char, Lexicon> codonMap = loadCodonMap();
 * ==================================================================
 * Loads the codon mapping table from a file.
 */
Map<char, Set<string> > loadCodonMap();

int main() {
    /* Load the codon map. */
    Map<char, Set<string> > codons = loadCodonMap();

	string protein = getLine("Enter protein string: ");

	listAllRNAStrandsFor(protein, codons);

    return 0;
}

/* You do not need to change this function. */
Map<char, Set<string> > loadCodonMap() {
    ifstream input("codons.txt");
    Map<char, Set<string> > result;

    /* The current codon / protein combination. */
    string codon;
    char protein;

    /* Continuously pull data from the file until all data has been
     * read.
     */
    while (input >> codon >> protein) {
        result[protein] += codon;
    }

    return result;
}

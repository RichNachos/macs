use std::fmt::format;

pub use sha256::digest;
// merkle tree should contain 1024 elements 
const CHILDREN_LENGTH: usize = 1024;
const DATA_ITEM_PREFIX: &str = "data item ";

#[derive(Debug)]
pub struct MerkleProof {
    pub root: String,
    pub proof_hashes: Vec<String>
}

impl MerkleProof {
    pub fn generate_tree_components(initial_position: usize) -> MerkleProof {
        let mut merkle_tree: Vec<Vec<String>> = Vec::new();
        let mut leafs: Vec<String> = Vec::with_capacity(CHILDREN_LENGTH);


        for i in 0..CHILDREN_LENGTH {
            let value = format!("{}{}", DATA_ITEM_PREFIX, i);
            let hash = digest(value);
            leafs.push(hash);
        }
        merkle_tree.push(leafs);

        let mut k = 1;
        let mut i = 0;
        while k < CHILDREN_LENGTH {
            let mut j = 0;
            let mut hashes = Vec::new();

            while j < (CHILDREN_LENGTH / k) {
                let hash1 = merkle_tree[i][j].clone();
                let hash2: String = merkle_tree[i][j+1].clone();
                let value = format!("{}{}", hash1, hash2);
                let hash = digest(value);
                hashes.push(hash);
                j += 2;
            }

            merkle_tree.push(hashes);
            i += 1;
            k *= 2;
        }

        let mut proof_hashes = Vec::new();
        let mut pos = initial_position;
        i = 0;
        while merkle_tree[i].len() > 1 {
            if pos % 2 == 0 {
                proof_hashes.push(merkle_tree[i][pos+1].clone())
            } else {
                proof_hashes.push(merkle_tree[i][pos-1].clone())
            }
            pos /= 2;
            i += 1;
        }
        

        MerkleProof { root: merkle_tree[i][0].clone(), proof_hashes: proof_hashes }
    }
}
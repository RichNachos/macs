use std::str::FromStr;

use bitcoin::PrivateKey;
use bitcoin::PublicKey;

use ripemd::{Ripemd160, Digest};
use secp256k1::Message;
use secp256k1::SecretKey;
use sha256::digest;

use secp256k1::Secp256k1;

use bs58;
use base64::{engine::general_purpose, Engine as _};
use serde_json::Value;

const PRIVATE_KEY: &str = "your private key here"; // wif format private key
const RECEIVER_PUBLIC_KEY: &str = "receiver p2pkh address here";
const FEE_SAT: u64 = 400; // fee in satoshis
const NETWORK: u8 = 0x6f; //testnet

// RPC user and pass
const USER: &str = "blank";
const PASS: &str = "blank";

struct Utxo {
    txid: String, // transaction id
    vout: u64, // output num
    amount: u64, // satoshis
    script_pub_key: String // scriptPubKey for signing
}

#[tokio::main]
async fn main() {
    send(RECEIVER_PUBLIC_KEY, 1000).await;
}

// Sends amount_satoshis to receiver_address pubkey
async fn send(receiver_address: &str, amount_satoshis: u64) {
    let utxos = get_utxos().await;
    println!("Performing various checks before sending satoshis...");
    if !balance_enough(&utxos, amount_satoshis).await {
        panic!("Not enough balance!")
    }
    println!("Balance is enough! Continuing...");
    if !valid_pubkey(receiver_address).await {
        panic!("Bitcoin address not valid!")
    }
    println!("Bitcoin addres is valid! Continuing...");
    println!("----------ALL CHECKS COMPLETE-----------");
    // CHECKS COMPLETE
    println!("BUILDING RAW TRANSACTION");
    let hex_tx = build_raw_transaction(&utxos, receiver_address, amount_satoshis).await;

    let (valid, txid) = is_valid(&hex_tx).await;
    if !valid {
        panic!("Generated transaction was not valid for some reason!");
    }
    
    let result = send_call(&hex_tx).await;
    if result {
        println!("Transaction has been broadcast");
        println!("TXID:");
        println!("{}", txid);
    } else {
        panic!("Transaction has failed to be broadcast");
    }
}

// Send send call rpc to node
async fn send_call(hex_tx: &str) -> bool {
    let user_pass = format!("{}:{}", USER, PASS);
    let user_pass = user_pass.as_bytes();
    let encoded: String = general_purpose::STANDARD.encode(user_pass);
    let token = format!("Basic {encoded}");
    
    let client = reqwest::Client::new();
    let body = format!("{{\"jsonrpc\":\"1.0\",\"method\":\"sendrawtransaction\",\"params\":[\"{}\"]}}", hex_tx);
    client
        .post("http://127.0.0.1:18332")
        .body(body)
        .header("content-type", "application/json")
        .header("Authorization", token).send().await.unwrap();
    
    return true
}

// Send rpc to check if transaction is valid
// Return tuple of validity and txid string
async fn is_valid(hex_tx: &str) -> (bool, String) {
    let user_pass = format!("{}:{}", USER, PASS);
    let user_pass = user_pass.as_bytes();
    let encoded: String = general_purpose::STANDARD.encode(user_pass);
    let token = format!("Basic {encoded}");
    
    let client = reqwest::Client::new();
    let body = format!("{{\"jsonrpc\":\"1.0\",\"method\":\"testmempoolaccept\",\"params\":[[\"{}\"]]}}", hex_tx);
    let res = client
        .post("http://127.0.0.1:18332")
        .body(body)
        .header("content-type", "application/json")
        .header("Authorization", token).send().await.unwrap();
        
    let json_result: String = res.text().await.unwrap();
    let parsed: Value = serde_json::from_str(&json_result).unwrap();
    let valid = parsed["result"][0]["allowed"].as_bool().unwrap();
    let txid = parsed["result"][0]["txid"].as_str().unwrap();
    return (valid, txid.to_owned());
}

// Build raw transaction from our data
async fn build_raw_transaction(utxos: &Vec<Utxo>, receiver_address: &str, amount_satoshis: u64) -> String {
    // Our transaction bytes
    let secp = Secp256k1::new();
    let mut tx: Vec<u8> = Vec::new();
    
    // Version bytes (4 bytes)
    tx.extend([0x02, 0x00, 0x00, 0x00]);
    
    // Input Count bytes (varint) (unknown bytes)
    let input_count = get_input_count(utxos, amount_satoshis).await as usize;
    let input_count_varint = int_to_varint(input_count as u64).await;
    tx.extend(input_count_varint);
    

    let mut script_sig_indexes: Vec<usize> = Vec::new();
    let mut balance: u64 = 0;    
    // Inputs
    for i in 0..input_count {
        let utxo = &utxos[i];
        balance += utxo.amount;
        
        // TXID bytes (32 bytes)
        let mut little_endian_txid: Vec<u8> = hex::decode(&utxo.txid).unwrap();
        little_endian_txid.reverse();
        tx.extend(little_endian_txid);

        // Transaction Output Id bytes (4 bytes)
        let vout = utxo.vout as u32;
        let vout_bytes  = vout.to_le_bytes();
        tx.extend(vout_bytes);


        // Push the index of future scriptSig index
        script_sig_indexes.push(tx.len());

        // locktime bytes (no locktime) (4 bytes)
        tx.extend([0xff, 0xff, 0xff, 0xff]);
    }

    // 2 Outputs varint (1 byte)
    tx.extend([0x02]);

    // Output 1 = Receiver (amount_satoshis)
    // Satoshis amount (8 bytes)
    let amount_bytes = amount_satoshis.to_le_bytes();
    tx.extend(amount_bytes);

    // scriptPubKey size (1 byte)
    tx.extend([0x19]);
    // scriptPubKey bytes (25 bytes)
    let mut script_pub_key: Vec<u8> = Vec::new();
    script_pub_key.extend([0x76, 0xa9, 0x14]);
    script_pub_key.extend(hex::decode(extract_pubkey_hash(receiver_address).await).unwrap());
    script_pub_key.extend([0x88, 0xac]);
    tx.extend(&script_pub_key);

    // Output 2 = Sender (leftover satoshis)
    // Satoshis amount (8 bytes)
    let amount_bytes = (balance - amount_satoshis - FEE_SAT).to_le_bytes();
    tx.extend(amount_bytes);

    // scriptPubKey size (1 byte)
    tx.extend([0x19]);
    // scriptPubKey bytes (25 bytes)
    let mut script_pub_key: Vec<u8> = Vec::new();
    script_pub_key.extend([0x76, 0xa9, 0x14]);
    script_pub_key.extend(hex::decode(extract_pubkey_hash(&get_p2pkh().await).await).unwrap());
    script_pub_key.extend([0x88, 0xac]);
    tx.extend(script_pub_key);
    
    
    // locking time (4 bytes)
    tx.extend([0x00,0x00,0x00,0x00]);
    
    // TODO: sign transaction
    // Transactions video 39:52
    // sign with scriptpubkey and then replace
    let private_key_hex = extract_private_key(PRIVATE_KEY).await;
    let public_key_hex = extract_public_key(PRIVATE_KEY).await;
    
    // Go over all inputs and sign each one of them seperately
    // Save those signatures into a vector to later add them in correct order to not ruin indexes
    
    let mut script_sigs: Vec<Vec<u8>> = Vec::new();
    for i in 0..input_count {
        let mut unsigned_tx = tx.clone();
        let mut script_pub_key = hex::decode(&utxos[i].script_pub_key).unwrap(); // get script
        script_pub_key.insert(0, 0x19); // add script size (0x19)
        let index = script_sig_indexes[i]; // index where to put script

        // ADD BYTES IN REVERSE ORDER SO INDEXES DO NOT GET RUINED
        // add 0 bytes to all inputs which are after unsigned transaction
        for j in (i+1..input_count).rev() {
            unsigned_tx.insert(script_sig_indexes[j], 0x00); // varint 0 for scriptSig
        }
        // add scriptPubKey bytes
        unsigned_tx.splice(index..index, script_pub_key);

        // add 0 bytes to all inputs which are before unsigned transaction
        for j in (0..i).rev() {
            unsigned_tx.insert(script_sig_indexes[j], 0x00); // varint 0 for scriptSig
        }
        
        // Hashtype bytes (4 bytes little endian)
        unsigned_tx.extend([0x01, 0x00, 0x00, 0x00]);

        // After this point we have made our transaction bytes ready for signing
        // Signing is done for every single input seperately
        // Currently our unsigned_tx contains bytes for the i-th input
        // Now we have to just hash it, sign it and save the bytes somewhere for later inserting into finalized transaction

        let hash = digest(&unsigned_tx);                            // digest our transaction using SHA256
        let doublehash = digest(hex::decode(hash).unwrap());
        let doublehash_as_vec = hex::decode(doublehash).unwrap();               // get hash as vector of bytes
        let doublehash_as_array: [u8; 32] = doublehash_as_vec.as_slice().try_into().unwrap(); // turn it into fixed 32 byte array
        let msg = Message::from_digest(doublehash_as_array);                   // get message object from our hash
        let sk = SecretKey::from_str(&private_key_hex).unwrap();       // get secret key object from our WIF private key
        let sig = secp.sign_ecdsa(&msg, &sk);                          // sign our msg
        let mut sig_bytes = hex::decode(sig.to_string()).unwrap(); // turn hex encoded DER encoding signature into bytes
        sig_bytes.extend([0x01]);                                           // Append sighash byte to our signature
        // Signature is ready
        
        let sig_script_size = sig_bytes.len() + 1 + 33 + 1;

        // scriptSig bytes vector
        let mut script_sig: Vec<u8> = Vec::new();
        // scriptSig size varint (1 byte)
        script_sig.extend(int_to_varint(sig_script_size as u64).await);
        // signature size varint (1 byte)
        script_sig.extend(int_to_varint(sig_bytes.len() as u64).await);
        // signature bytes
        script_sig.extend(sig_bytes);
        // public key size varint (1 byte) // Only P2PKH Addresses so we know its a fixed length public address
        script_sig.extend([0x21]);
        // public key bytes // THIS IS OUR PUBLIC KEY NOT THE RECEIVERS!
        script_sig.extend(hex::decode(&public_key_hex).unwrap());

        // scriptSig is finished, add it to our script_sigs vector for future inserting
        // we push it into the array to not mix the ordering
        script_sigs.push(script_sig.clone());
    }


    // Start adding scriptSig-s to finally, FINALLY finish the transaction building
    // Doing it in reverse order to not ruin our saved indexes

    for i in (0..input_count).rev() {
        let index = script_sig_indexes[i];
        let script_sig = script_sigs[i].clone();
        tx.splice(index..index, script_sig);
    }
    let hex_tx = hex::encode(tx);

    return hex_tx;
}


async fn extract_pubkey_hash(address: &str) -> String {
    let pubkey_bytes = bs58::decode(address).into_vec().unwrap();

    let pubkey_hash = &pubkey_bytes[1..pubkey_bytes.len() - 4];
    let pubkey_hash_hex = hex::encode(pubkey_hash);
    return pubkey_hash_hex;
}

async fn extract_private_key(address: &str) -> String {
    let private_bytes = bs58::decode(address).into_vec().unwrap();

    let private_key = &private_bytes[1..33];
    let private_key_hex = hex::encode(private_key);
    return private_key_hex;
}

async fn extract_public_key(address: &str) -> String {
    let secp = Secp256k1::new();
    let private_key = PrivateKey::from_wif(address).unwrap();
    let public_key = private_key.public_key(&secp);

    return public_key.to_string();
}

async fn get_input_count(utxos: &Vec<Utxo>, amount_satoshis: u64) -> u64 {
    let mut total = 0;
    for i in 0..utxos.len() {
        total += utxos[i].amount;
        if total >=  amount_satoshis + FEE_SAT {
            return (i + 1) as u64;
        }
    }
    panic!("Not enough balance! Previous transactions have yet to be confirmed, rerun in a few minutes.");
}

async fn int_to_varint(value: u64) -> Vec<u8> {
    let mut varint = Vec::new();
    let mut value = value;
    loop {
        let mut byte = (value & 0x7F) as u8;
        value >>= 7;
        if value != 0 {
            byte |= 0x80;
        }
        varint.push(byte);
        if value == 0 {
            break;
        }
    }
    return varint;
}

// Returns false if balance isn't enough and true if it is
// Checks if we can pay fee too
// Easily changeable if we decide to not include fee in the calculation
async fn balance_enough(utxos: &Vec<Utxo>, amount_satoshis: u64) -> bool {
    let mut total = 0;
    for i in 0..utxos.len() {
        total += utxos[i].amount;
    }
    total >= amount_satoshis + FEE_SAT
}

// Get Base58Check format P2PKH from our private key in WIF format in constant variable PRIVATE_KEY
async fn get_p2pkh() -> String {
    let secp = Secp256k1::new();
    let private_key = PrivateKey::from_wif(PRIVATE_KEY).unwrap();
    let public_key = private_key.public_key(&secp);

    return encrypt_public_key(&public_key).await;
}

// Turn public key (which is tied to its private key) into bitcoin usable
// Base58Check format
async fn encrypt_public_key(public_key: &PublicKey) -> String{
    let mut ripemd = Ripemd160::new();

    let sha = digest(public_key.to_bytes());
    let sha_bytes = hex::decode(sha).unwrap();
    ripemd.update(sha_bytes);
    let mut hashed: Vec<u8> = ripemd.finalize().to_vec();
    
    // add network byte
    hashed.insert(0, NETWORK);

    // add checksum
    let doublehash = hex::decode(digest(hex::decode(digest(&hashed)).unwrap())).unwrap();
    // let d = doublehash.clone();
    let checksum = doublehash.iter().take(4).cloned();
    // let doublehash_str = hex::encode(d);
    hashed.extend(checksum);
    // let hex_p2pkh = hex::encode(hashed);

    let base58_p2pkh = bs58::encode(hashed).into_string();

    base58_p2pkh
}


// // Create a private key encoded in hex
// // Private key is just random 256 bits = 32 bytes = 64 hex encoded string
// async fn generate_private_key() -> String {
//     let secp = Secp256k1::new();
//     let (secret_key, _) = secp.generate_keypair(&mut OsRng);
//     let hex_private_key = secret_key.display_secret().to_string();
//      // Print the private and public keys

//      return hex_private_key
// }

// Get all unspent utxo's
async fn get_utxos() -> Vec<Utxo> {
    let user_pass = format!("{}:{}", USER, PASS);
    let user_pass = user_pass.as_bytes();
    let encoded: String = general_purpose::STANDARD.encode(user_pass);
    let token = format!("Basic {encoded}");
    
    let client = reqwest::Client::new();
    let body = format!("{{\"jsonrpc\":\"1.0\",\"method\":\"listunspent\",\"params\":[2,999999,[\"{}\"]]}}", get_p2pkh().await);
    let res = client
        .post("http://127.0.0.1:18332")
        .body(body)
        .header("content-type", "application/json")
        .header("Authorization", token).send().await.unwrap();
        
    let json_result: String = res.text().await.unwrap();
    
    let parsed: Value = serde_json::from_str(&json_result).unwrap();
    let parsed_tx_array = parsed["result"].as_array().unwrap();
    let mut utxos: Vec<Utxo> = Vec::new();
    for i in 0..parsed_tx_array.len() {
        utxos.push(Utxo {
            txid: parsed_tx_array[i]["txid"].to_string().trim_matches(|c| c == '"').to_owned(),
            vout: parsed_tx_array[i]["vout"].as_u64().unwrap(),
            amount: (parsed_tx_array[i]["amount"].as_f64().unwrap() * 10e7) as u64,
            script_pub_key:  parsed_tx_array[i]["scriptPubKey"].to_string().trim_matches(|c| c == '"').to_owned(),
        });
    }

    return utxos
}


// Validate public key we're sending to
async fn valid_pubkey(pubkey: &str) -> bool {
    let pubkey_bytes = bs58::decode(pubkey).into_vec().unwrap();
    
    // SIZE
    if pubkey_bytes.len() != 25 {
        println!("P2PKH Address size is wrong!");
        return false;
    }
    
    // CHECKSUM
    let checksum_bytes = &pubkey_bytes[pubkey_bytes.len() - 4..];
    let checksum_str = hex::encode(checksum_bytes);
    let without_checksum = &pubkey_bytes[0..pubkey_bytes.len()-4];
    let doublehash = hex::decode(digest(hex::decode(digest(without_checksum)).unwrap())).unwrap();
    // let d = doublehash.clone();
    let our_checksum_bytes = &doublehash[0..4];
    let our_checksum_str = hex::encode(our_checksum_bytes);
    if checksum_str != our_checksum_str {
        println!("Checksum is wrong!");
        return false
    }

    // NETWORK BYTE
    let network_byte = pubkey_bytes[0];
    if network_byte != NETWORK {
        println!("Network byte is wrong!");
        return false;
    }



    return true
}

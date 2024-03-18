use core::panic;

use num_bigint::{BigInt, ToBigInt};

pub struct MyPublicKey {
    data: BigInt,
}

impl From<&str> for MyPublicKey {
    fn from(value: &str) -> Self {
        if value.len() == 0 {
            panic!("Invalid length!");
        }


        let mut result = BigInt::from(0);
        let mut i = 0;
        for ch in value.chars().rev() {
            let mut curr = BigInt::from(-1);
            for (i, c)  in "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".chars().enumerate() {
                if c == ch {
                    curr = BigInt::from(i);
                    break;
                }
            }
            if curr == -1.to_bigint().unwrap() {
                panic!("Encoded string not base58");
            }
            
            result += curr * 58.to_bigint().unwrap().pow(i);
            i += 1;
            // println!("Result: {}", result)
        }
        MyPublicKey { data: result }
    }
}

impl From<String> for MyPublicKey {
    fn from(value: String) -> Self {
        let value_str: &str = &value;
        MyPublicKey::from(value_str)
    }
}

impl From<[u8; 32]> for MyPublicKey {
    fn from(bytes: [u8; 32]) -> Self {
        let arr = &bytes;
        MyPublicKey { data: BigInt::from_bytes_be(num_bigint::Sign::Plus, arr) }
    }
}

impl PartialEq for MyPublicKey {
    fn eq(&self, other: &Self) -> bool {
        // Compare the data field of both MyPublicKey instances.
        self.data == other.data
    }
}

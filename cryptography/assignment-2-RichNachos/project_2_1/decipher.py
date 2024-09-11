import sys
from oracle import *

ENCRYPTION_KEY_SIZE = 128
BLOCK_SIZE = 16

def decrypt_aes_cbc(ciphertext: str) -> str:



def main():
    if len(sys.argv) < 2:
        print("Usage: python decipher.py <filename>")
        sys.exit(-1)

    ctext_path: str = sys.argv[1]
    file = open(ctext_path)
    ciphertext: str = file.read().strip()
    plaintext: str = decrypt_aes_cbc(ciphertext)
    print(plaintext)

if __name__ == '__main__':
    main()

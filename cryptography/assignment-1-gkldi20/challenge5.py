
def repeating_xor_encryption(message: bytes, key: bytes) -> bytes:
    cipher = b''
    for i, byte in enumerate(message):
        cipher += chr(byte ^ key[i % len(key)]).encode()
    return cipher


def main() -> None:
    key = input().strip().encode()
    plaintext = input().strip().encode()
    cipher = repeating_xor_encryption(plaintext, key)
    print(cipher.hex())


if __name__ == '__main__':
    main()

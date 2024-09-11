import string
from binascii import unhexlify
from collections import Counter

CHAR_FREQUENCY = {
    'a': 0.0651738,
    'b': 0.0124248,
    'c': 0.0217339,
    'd': 0.0349835,
    'e': 0.1041442,
    'f': 0.0197881,
    'g': 0.0158610,
    'h': 0.0492888,
    'i': 0.0558094,
    'j': 0.0009033,
    'k': 0.0050529,
    'l': 0.0331490,
    'm': 0.0202124,
    'n': 0.0564513,
    'o': 0.0596302,
    'p': 0.0137645,
    'q': 0.0008606,
    'r': 0.0497563,
    's': 0.0515760,
    't': 0.0729357,
    'u': 0.0225134,
    'v': 0.0082903,
    'w': 0.0171272,
    'x': 0.0013692,
    'y': 0.0145984,
    'z': 0.0007836,
    ' ': 0.1918182
}


def get_score(candidate):
    score = 0
    for byte in candidate:
        score += CHAR_FREQUENCY.get(chr(byte).lower(), 0)
    return score


def xor_data(data: bytes, key: int) -> bytes:
    xored_data = b''
    for byte in data:
        xored_data += chr(byte ^ key).encode()
    return xored_data


def bruteforce_xor_char(cipher: str) -> str:
    cipher_bytes: bytes = unhexlify(cipher)

    best_candidate = b''
    best_score = -1
    for i in range(256):
        key: int = i
        candidate = xor_data(cipher_bytes, key)
        score = get_score(candidate)
        if score > best_score:
            best_score = score
            best_candidate = candidate

    return best_candidate.decode()


def main() -> None:
    cipher: str = input()
    decipher: str = bruteforce_xor_char(cipher)
    print(decipher)


if __name__ == '__main__':
    main()

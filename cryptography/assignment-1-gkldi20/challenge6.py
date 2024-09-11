import base64
import string

from challenge3 import get_score

letterFrequency = {
    'E': 12.0,
    'T': 9.10,
    'A': 8.12,
    'O': 7.68,
    'I': 7.31,
    'N': 6.95,
    'S': 6.28,
    'R': 6.02,
    'H': 5.92,
    'D': 4.32,
    'L': 3.98,
    'U': 2.88,
    'C': 2.71,
    'M': 2.61,
    'F': 2.30,
    'Y': 2.11,
    'W': 2.09,
    'G': 2.03,
    'P': 1.82,
    'B': 1.49,
    'V': 1.11,
    'K': 0.69,
    'X': 0.17,
    'Q': 0.11,
    'J': 0.10,
    'Z': 0.07
}


def hamming_distance(s1, s2):
    xored = xor_sequences(s1, s2)
    return count_bits(xored)


def xor_sequences(seq_1, seq_2):
    seq = ''
    for i in range(len(seq_1)):
        seq += chr(seq_1[i] ^ seq_2[i])
    return seq.encode()


def count_bits(sequence) -> int:
    return bin(int.from_bytes(sequence, 'little')).count('1')


def find_hamming_distance(decoded_inp) -> int:
    key_size = 1
    normalized_distance = 1000000
    for i in range(2, 41):
        seq_1 = decoded_inp[0:i]
        seq_2 = decoded_inp[i:i + i]
        new_dist = hamming_distance(seq_1, seq_2)
        new_norm_dist = new_dist / i
        if normalized_distance > new_norm_dist:
            normalized_distance = new_norm_dist
            key_size = i
    return key_size


def transpose_blocks(decoded_inp, key_size):
    blocks = []
    for i in range(key_size):
        j = 0
        block = ''
        while i + (j * key_size) < len(decoded_inp):
            block += chr(decoded_inp[i + (j * key_size)])
            j += 1
        blocks.append(block)
    return blocks


def is_text(inp):
    nb_letters = sum([x in string.ascii_lowercase + ' .' for x in inp])
    return nb_letters / len(inp)


def find_msg(decrypted_messages):
    for msg in decrypted_messages:
        if is_text(msg):
            return msg
    return "NONE"


def read_block(block):
    inp = block
    best_c = ''
    best_score = 0
    for c in string.ascii_letters:
        decrypted = ''
        for byte in inp:
            decrypted += chr(ord(byte) ^ ord(c))
        curr_score = get_score(decrypted.encode())

        if curr_score > best_score:
            best_c = c
            best_score = curr_score
    return best_c


def xor_input_with_key(inp, key) -> str:
    decrypted = ''
    for i in range(len(inp)):
        decrypted += chr(inp[i] ^ ord(key[i % len(key)]))
    return decrypted


def solve(inp):
    decoded_inp = base64.b64decode(inp)
    key_size = find_hamming_distance(decoded_inp)
    blocks = transpose_blocks(decoded_inp, key_size)
    key = ''
    for block in blocks:
        key += read_block(block)
    decrypted = xor_input_with_key(decoded_inp, key)
    return decrypted


inp = input()
ans = solve(inp)
print(ans)

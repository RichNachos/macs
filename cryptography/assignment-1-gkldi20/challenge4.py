from challenge3 import bruteforce_xor_char, get_score


def main() -> None:
    num_ciphers = int(input())
    best_score = -1
    best_decipher = ''
    for i in range(num_ciphers):
        cipher: str = input().strip()
        decipher: str = bruteforce_xor_char(cipher)
        score = get_score(decipher.encode())
        if best_score < score:
            best_score = score
            best_decipher = decipher

    print(best_decipher)


if __name__ == '__main__':
    main()

from typing import Callable, List

MAX_INT = 2**16


class Cpu:
    def __init__(self, instructions: List[str]) -> None:
        self.instructions = instructions
        self.a = 0
        self.d = 0
        self.pc = 0
        self.ram: dict[str, int] = {}
        self.comp: dict[str, Callable[[], int]] = {
            "0101010": lambda: 0,
            "0111111": lambda: 1,
            "0111010": lambda: MAX_INT - 1,
            "0001100": lambda: self.d,
            "0110000": lambda: self.a,
            "0001101": lambda: MAX_INT - (1 + self.d),
            "0110001": lambda: MAX_INT - (1 + self.a),
            "0001111": lambda: MAX_INT - self.d,
            "0110011": lambda: MAX_INT - self.a,
            "0011111": lambda: (self.d + 1) % MAX_INT,
            "0110111": lambda: (self.a + 1) % MAX_INT,
            "0001110": lambda: (self.d - 1) % MAX_INT,
            "0110010": lambda: (self.a - 1) % MAX_INT,
            "0000010": lambda: (self.d + self.a) % MAX_INT,
            "0010011": lambda: (self.d - self.a) % MAX_INT,
            "0000111": lambda: (self.a - self.d) % MAX_INT,
            "0000000": lambda: (self.d & self.a),
            "0010101": lambda: (self.d | self.a),
            "1110000": lambda: self.ram[str(self.a)],
            "1110001": lambda: MAX_INT - (1 + self.ram[str(self.a)]),
            "1110011": lambda: MAX_INT - self.ram[str(self.a)],
            "1110111": lambda: (self.ram[str(self.a)] + 1) % MAX_INT,
            "1110010": lambda: (self.ram[str(self.a)] - 1) % MAX_INT,
            "1000010": lambda: (self.d + self.ram[str(self.a)]) % MAX_INT,
            "1010011": lambda: (self.d - self.ram[str(self.a)]) % MAX_INT,
            "1000111": lambda: (self.ram[str(self.a)] - self.d) % MAX_INT,
            "1000000": lambda: self.d & self.ram[str(self.a)],
            "1010101": lambda: self.d | self.ram[str(self.a)],
        }

    def execute_next_instruction(self) -> None:
        if self.instructions[self.pc][0] == "0":
            self.execute_a_instruction()
        else:
            self.execute_c_instruction()
        self.pc += 1

    def has_instructions(self) -> bool:
        return self.pc < len(self.instructions)

    def execute_a_instruction(self) -> None:
        binary = self.instructions[self.pc][1:16]
        int_val = int(binary, 2)
        self.a = int_val

    def execute_c_instruction(self) -> None:
        curr = self.instructions[self.pc]
        comp = curr[3:10]
        dest = curr[10:13]
        jump = curr[13:16]
        try:
            value = self.comp[comp]()
        except KeyError as e:  # Exception: CPU uses RAM address where it did not store
            self.ram[str(e).split("'")[1]] = 0
            value = self.comp[comp]()

        self.store_in_dest(value, dest)
        self.execute_jump(value, jump)

    def store_in_dest(self, value: int, dest: str) -> None:
        if dest[2] == "1":  # M
            self.ram[str(self.a)] = value
        if dest[1] == "1":  # D
            self.d = value
        if dest[0] == "1":  # A
            self.a = value

    def execute_jump(self, value: int, jump: str) -> None:
        if jump == "000":
            return
        elif (
            (jump == "001" and 0 < value < 2**15)
            or (jump == "010" and value == 0)
            or (jump == "011" and 0 <= value < 2**15)
            or (jump == "100" and 2**15 <= value < 2**16)
            or (jump == "101" and value != 0)
            or (jump == "110" and (2**15 <= value < 2**16 or value == 0))
            or (jump == "111")
        ):
            self.pc = self.a - 1

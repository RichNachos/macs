from __future__ import annotations

from dataclasses import dataclass

SYMBOL_TABLE = {
    "SP": 0,
    "LCL": 1,
    "ARG": 2,
    "THIS": 3,
    "THAT": 4,
    "R0": 0,
    "R1": 1,
    "R2": 2,
    "R3": 3,
    "R4": 4,
    "R5": 5,
    "R6": 6,
    "R7": 7,
    "R8": 8,
    "R9": 9,
    "R10": 10,
    "R11": 11,
    "R12": 12,
    "R13": 13,
    "R14": 14,
    "R15": 15,
    "SCREEN": 16384,
    "KBD": 24576,
}


@dataclass
class SymbolTable:
    dict: dict[str, int]

    @classmethod
    def create(cls) -> SymbolTable:
        return cls(SYMBOL_TABLE.copy())

    def add_entry(self, symbol: str, address: int) -> None:
        self.dict[symbol] = address

    def __contains__(self, symbol: str) -> bool:
        return self.dict.keys().__contains__(symbol)

    def get_address(self, symbol: str) -> int:
        return self.dict[symbol]

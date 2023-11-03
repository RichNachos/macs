from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable

from n2t.core.assembler.code import Code
from n2t.core.assembler.parser import CommandType, Parser
from n2t.core.assembler.symbol_table import SymbolTable


@dataclass
class Assembler:
    @classmethod
    def create(cls) -> Assembler:
        return cls()

    def assemble(self, assembly: Iterable[str]) -> Iterable[str]:
        parser = Parser.create(assembly)
        code = Code.create()
        symbol_table = SymbolTable.create()
        rom = 0
        ram = 16
        binary = []

        while parser.has_more_commands():
            if parser.command_type() == CommandType.L_COMMAND:
                symbol_table.add_entry(parser.symbol(), rom)
            else:
                rom += 1
            parser.advance()

        parser.reset_rip()

        while parser.has_more_commands():
            if parser.command_type() == CommandType.A_COMMAND:
                symbol = parser.symbol()
                address = ram
                if not symbol.isnumeric():
                    if symbol_table.__contains__(parser.symbol()):
                        address = symbol_table.get_address(parser.symbol())
                    else:
                        symbol_table.add_entry(parser.symbol(), address)
                        ram += 1
                else:
                    address = int(symbol)
                add = bin(address)
                add = add.split("b")[1]
                instruction = "0" * (16 - len(add)) + add
                binary.append(instruction)

            if parser.command_type() == CommandType.C_COMMAND:
                instruction = (
                    "111"
                    + code.comp(parser.comp())
                    + code.dest(parser.dest())
                    + code.jump(parser.jump())
                )
                binary.append(instruction)

            if parser.command_type() == CommandType.L_COMMAND:
                pass

            parser.advance()
        return binary.__iter__()

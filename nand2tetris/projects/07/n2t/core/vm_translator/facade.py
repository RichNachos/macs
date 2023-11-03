from __future__ import annotations

from dataclasses import dataclass
from typing import Iterable, List

from n2t.core.vm_translator.code_writer import CodeWriter
from n2t.core.vm_translator.parser import CommandType, Parser

REGISTER_TABLE = {
    "SP": 0,
    "LCL": 1,
    "ARG": 2,
    "THIS": 3,
    "THAT": 4,
    "STATIC": 16,
}


@dataclass
class VmTranslator:
    class_name: str

    @classmethod
    def create(cls, class_name: str) -> VmTranslator:
        return cls(class_name)

    def translate(self, vm: Iterable[str]) -> Iterable[str]:
        asm_output: List[str] = []
        parser = Parser.create(vm)
        code_writer = CodeWriter.create(asm_output, self.class_name)

        parser.advance()
        while parser.has_more_lines():
            if (
                parser.command_type() == CommandType.C_PUSH
                or parser.command_type() == CommandType.C_POP
            ):
                code_writer.write_push_pop(
                    parser.command_type(), parser.arg1(), parser.arg2()
                )

            if parser.command_type() == CommandType.C_ARITHMETIC:
                code_writer.write_arithmetic(parser.arg1())
            parser.advance()

        return asm_output

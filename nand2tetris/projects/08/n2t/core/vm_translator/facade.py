from __future__ import annotations

import itertools
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

    def translate(self, vm: Iterable[str], starter: str) -> List[str]:
        if starter != "":
            vm = itertools.chain([starter], iter(vm))
        asm_output: List[str] = []
        parser = Parser.create(vm)
        code_writer = CodeWriter.create(asm_output, self.class_name)

        parser.advance()
        while parser.has_more_lines():
            type = parser.command_type()
            if type == CommandType.C_PUSH or type == CommandType.C_POP:
                code_writer.write_push_pop(type, parser.arg1(), parser.arg2())
            if type == CommandType.C_ARITHMETIC:
                code_writer.write_arithmetic(parser.arg1())
            if type == CommandType.C_LABEL:
                code_writer.write_label(parser.arg1())
            if type == CommandType.C_GOTO:
                code_writer.write_goto(parser.arg1())
            if type == CommandType.C_IF:
                code_writer.write_if(parser.arg1())
            if type == CommandType.C_FUNCTION:
                code_writer.write_function(parser.arg1(), parser.arg2())
            if type == CommandType.C_CALL:
                code_writer.write_call(parser.arg1(), parser.arg2())
            if type == CommandType.C_RETURN:
                code_writer.write_return()
            parser.advance()

        return asm_output

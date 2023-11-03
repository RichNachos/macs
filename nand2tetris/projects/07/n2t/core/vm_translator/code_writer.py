from __future__ import annotations

from dataclasses import dataclass
from typing import List

from n2t.core.vm_translator.parser import CommandType


@dataclass
class Shortcuts:
    PUSH_SUFFIX = ["@SP", "A=M", "M=D", "@SP", "M=M+1"]
    POP_SUFFIX = ["@SP", "AM=M-1", "D=M", "@13", "A=M", "M=D"]
    CMP_BOILERPLATE = [
        "@SP",
        "A=M-1",
        "D=M",
        "A=A-1",
        "D=M-D",
        "------",
        "------",
        "@SP",
        "A=M-1",
        "A=A-1",
        "M=0",
        "------",
        "0;JMP",
        "------",
        "@SP",
        "A=M-1",
        "A=A-1",
        "M=-1",
        "------",
        "@SP",
        "M=M-1",
    ]


@dataclass
class CodeWriter:
    asm_output: List[str]
    class_name: str
    label_index: int = 0

    @classmethod
    def create(cls, asm_output: List[str], class_name: str) -> CodeWriter:
        return cls(asm_output, class_name)

    def write_arithmetic(self, command: str) -> None:
        asm = []
        asm += self.arithmetic(command)
        self.write(asm)

    def write_push_pop(self, command: CommandType, segment: str, value: int) -> None:
        asm = []
        if command == CommandType.C_PUSH:
            asm += self.push(segment, value)
        if command == CommandType.C_POP:
            asm += self.pop(segment, value)
        self.write(asm)

    def close(self) -> None:
        pass

    def push(self, segment: str, value: int) -> List[str]:
        if segment == "constant":
            return [f"@{value}", "D=A"] + Shortcuts.PUSH_SUFFIX
        if segment == "local":
            return [f"@{value}", "D=A", "@LCL", "A=M+D", "D=M"] + Shortcuts.PUSH_SUFFIX
        if segment == "argument":
            return [f"@{value}", "D=A", "@ARG", "A=M+D", "D=M"] + Shortcuts.PUSH_SUFFIX
        if segment == "this":
            return [f"@{value}", "D=A", "@THIS", "A=M+D", "D=M"] + Shortcuts.PUSH_SUFFIX
        if segment == "that":
            return [f"@{value}", "D=A", "@THAT", "A=M+D", "D=M"] + Shortcuts.PUSH_SUFFIX
        if segment == "static":
            return [f"@{self.class_name}.{value}", "D=M"] + Shortcuts.PUSH_SUFFIX
        if segment == "temp":
            return [f"@{value}", "D=A", "@5", "A=A+D", "D=M"] + Shortcuts.PUSH_SUFFIX
        if segment == "pointer":
            return [f"@{value}", "D=A", "@3", "A=A+D", "D=M"] + Shortcuts.PUSH_SUFFIX
        return []

    def pop(self, segment: str, value: int) -> List[str]:
        if segment == "local":
            return [
                f"@{value}",
                "D=A",
                "@LCL",
                "D=M+D",
                "@13",
                "M=D",
            ] + Shortcuts.POP_SUFFIX
        if segment == "argument":
            return [
                f"@{value}",
                "D=A",
                "@ARG",
                "D=M+D",
                "@13",
                "M=D",
            ] + Shortcuts.POP_SUFFIX
        if segment == "this":
            return [
                f"@{value}",
                "D=A",
                "@THIS",
                "D=M+D",
                "@13",
                "M=D",
            ] + Shortcuts.POP_SUFFIX
        if segment == "that":
            return [
                f"@{value}",
                "D=A",
                "@THAT",
                "D=M+D",
                "@13",
                "M=D",
            ] + Shortcuts.POP_SUFFIX
        if segment == "static":
            return ["@SP", "AM=M-1", "D=M", f"@{self.class_name}.{value}", "M=D"]
        if segment == "temp":
            return [
                f"@{value}",
                "D=A",
                "@5",
                "D=A+D",
                "@13",
                "M=D",
            ] + Shortcuts.POP_SUFFIX
        if segment == "pointer":
            return [
                f"@{value}",
                "D=A",
                "@3",
                "D=A+D",
                "@13",
                "M=D",
            ] + Shortcuts.POP_SUFFIX
        return []

    def arithmetic(self, operation: str) -> List[str]:
        if operation == "add":
            return ["@SP", "A=M-1", "D=M", "A=A-1", "D=D+M", "M=D", "@SP", "M=M-1"]
        if operation == "sub":
            return ["@SP", "A=M-1", "D=M", "A=A-1", "D=M-D", "M=D", "@SP", "M=M-1"]
        if operation == "neg":
            return ["@SP", "A=M-1", "M=-M"]
        if operation == "not":
            return ["@SP", "A=M-1", "M=!M"]
        if operation == "or":
            return ["@SP", "A=M-1", "D=M", "A=A-1", "M=D|M", "@SP", "M=M-1"]
        if operation == "and":
            return ["@SP", "A=M-1", "D=M", "A=A-1", "M=D&M", "@SP", "M=M-1"]
        if operation == "eq" or operation == "gt" or operation == "lt":
            return self.handle_cmp(operation)
        return []

    def handle_cmp(self, cmp: str) -> List[str]:
        asm = Shortcuts.CMP_BOILERPLATE.copy()
        self.label_index += 1
        asm[5] = f"@LABEL_TRUE_{self.label_index}"
        asm[11] = f"@LABEL_SKIP_{self.label_index}"
        asm[13] = f"(LABEL_TRUE_{self.label_index})"
        asm[18] = f"(LABEL_SKIP_{self.label_index})"
        if cmp == "eq":
            asm[6] = "D;JEQ"
        if cmp == "gt":
            asm[6] = "D;JGT"
        if cmp == "lt":
            asm[6] = "D;JLT"
        return asm

    def write(self, asm: List[str]) -> None:
        self.asm_output += asm

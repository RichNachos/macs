from __future__ import annotations

from dataclasses import dataclass
from enum import Enum
from typing import Iterable, List


class CommandType(Enum):
    NO_COMMAND = 0
    C_ARITHMETIC = 1
    C_PUSH = 2
    C_POP = 3
    C_LABEL = 4
    C_GOTO = 5
    C_IF = 6
    C_FUNCTION = 7
    C_RETURN = 8
    C_CALL = 9


@dataclass
class Parser:
    vm: List[str]
    ip: int
    current_command: str

    @classmethod
    def create(cls, vm_file: Iterable[str]) -> Parser:
        vm: List[str] = []
        vm_file = list(vm_file)
        for i in range(len(vm_file)):
            if vm_file[i].__contains__("//"):
                continue
            if vm_file[i] == "":
                continue
            vm.append(vm_file[i].strip())
        return cls(vm, -1, "")

    def has_more_lines(self) -> bool:
        if self.ip >= len(self.vm):
            return False
        return True

    def advance(self) -> None:
        self.ip += 1
        if self.has_more_lines():
            self.current_command = self.vm[self.ip]
        else:
            self.current_command = ""

    def command_type(self) -> CommandType:
        command = self.current_command.split(" ")[0]
        if command == "push":
            return CommandType.C_PUSH
        if command == "pop":
            return CommandType.C_POP
        if ["add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not"].__contains__(
            command
        ):
            return CommandType.C_ARITHMETIC

        return CommandType.NO_COMMAND

    def arg1(self) -> str:
        if self.command_type() == CommandType.C_ARITHMETIC:
            return self.current_command
        return self.current_command.split(" ")[1]

    def arg2(self) -> int:
        type = self.command_type()
        if (
            type == CommandType.C_PUSH
            or type == CommandType.C_POP
            or type == CommandType.C_FUNCTION
            or type == CommandType.C_CALL
        ):
            return int(self.current_command.split(" ")[2])
        return -1

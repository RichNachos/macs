from __future__ import annotations

from dataclasses import dataclass
from enum import Enum
from typing import Iterable, List


class CommandType(Enum):
    A_COMMAND = 1
    C_COMMAND = 2
    L_COMMAND = 3


@dataclass
class Parser:
    assembly: List[str]
    rip: int

    @classmethod
    def create(cls, assembly: Iterable[str]) -> Parser:
        assembly = list(assembly)
        for i in range(len(assembly)):
            line = assembly[i]
            line = line.replace(" ", "")
            split_list = line.split("//")
            line = split_list[0]
            assembly[i] = line
        assembly = [x for x in assembly if x != ""]
        rip = 0
        return cls(list(assembly), rip)

    def reset_rip(self) -> None:
        self.rip = 0

    def debug(self) -> None:
        for s in self.assembly:
            print(s)

    def has_more_commands(self) -> bool:
        if self.rip >= len(self.assembly):
            return False
        return True

    def advance(self) -> None:
        self.rip += 1

    def command_type(self) -> CommandType:
        line = self.assembly[self.rip]
        if line[0] == "@":
            return CommandType.A_COMMAND
        if line[0] == "(":
            return CommandType.L_COMMAND
        return CommandType.C_COMMAND

    def symbol(self) -> str:
        line = self.assembly[self.rip]
        line = line.replace("(", "@")
        line = line.replace(")", "@")
        return line.split("@")[1]

    def dest(self) -> str:
        line = self.assembly[self.rip]
        if not line.__contains__("="):
            return ""
        return line.split("=")[0]

    def comp(self) -> str:
        line = self.assembly[self.rip]
        if line.__contains__("="):
            return line.split("=")[1]
        return line.split(";")[0]

    def jump(self) -> str:
        line = self.assembly[self.rip]
        if not line.__contains__(";"):
            return ""
        return line.split(";")[1]

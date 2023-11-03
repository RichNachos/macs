from __future__ import annotations

from dataclasses import dataclass


@dataclass
class JackProgram:  # TODO: your work for Projects 10 and 11 starts here
    @classmethod
    def load_from(cls, file_or_directory_name: str) -> JackProgram:
        return cls()

    def compile(self) -> None:
        pass

from __future__ import annotations

from dataclasses import dataclass


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        return cls()

    def translate(self) -> None:
        pass

from __future__ import annotations

import os
from dataclasses import dataclass, field
from pathlib import Path
from typing import Iterable, Iterator, Protocol

from n2t.core import Executor as DefaultExecutor
from n2t.infra import AsmProgram
from n2t.infra.io import File, FileFormat


@dataclass
class ExecProgram:
    path: Path
    cycles: int
    hack_path: Path = Path()
    executor: Executor = field(default_factory=DefaultExecutor.create)

    @classmethod
    def load_from(cls, file_name: str, cycles: int) -> ExecProgram:
        return cls(Path(file_name), cycles)

    def __post_init__(self) -> None:
        if self.path.suffix == ".asm":
            AsmProgram.load_from(self.path.__str__()).assemble()
            self.hack_path = FileFormat.hack.convert(self.path)
        else:
            self.hack_path = self.path
        FileFormat.hack.validate(self.hack_path)

    def execute(self) -> None:
        json_file = File(FileFormat.json.convert(self.hack_path))
        json_file.save(self.executor.execute(self, self.cycles))
        self.cleanup()

    def cleanup(self) -> None:
        if self.hack_path != self.path:
            os.remove(self.hack_path)

    def __iter__(self) -> Iterator[str]:
        yield from File(self.hack_path).load()


class Executor(Protocol):  # pragma: no cover
    def execute(self, hack: Iterable[str], cycles: int) -> Iterable[str]:
        pass

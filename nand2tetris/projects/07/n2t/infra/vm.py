from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path
from typing import Iterable, Iterator, Protocol

from n2t.core import VmTranslator as DefaultVmTranslator
from n2t.infra.io import File, FileFormat


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    path: Path
    vm_translator: VmTranslator

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        translator = DefaultVmTranslator(file_or_directory_name.split("/").pop())
        return cls(Path(file_or_directory_name), translator)

    def __post_init__(self) -> None:
        FileFormat.vm.validate(self.path)

    def translate(self) -> None:
        vm_file = File(FileFormat.asm.convert(self.path))
        vm_file.save(self.vm_translator.translate(self))

    def __iter__(self) -> Iterator[str]:
        yield from File(self.path).load()


class VmTranslator(Protocol):  # pragma: no cover
    def translate(self, assembly: Iterable[str]) -> Iterable[str]:
        pass

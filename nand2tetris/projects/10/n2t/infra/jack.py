from __future__ import annotations

import os
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable, Iterator, List, Protocol

from n2t.core.compiler.facade import Compiler as DefaultCompiler
from n2t.infra.io import File, FileFormat


@dataclass
class JackProgram:  # TODO: your work for Projects 10 and 11 starts here
    path: str
    file_paths: List[Path]

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> JackProgram:
        paths = []
        for root, dirs, files in os.walk(file_or_directory_name):
            for file in files:
                if file.__contains__(".jack"):
                    path = Path(os.path.join(root, file))
                    paths.append(path)
        if file_or_directory_name.__contains__(".jack"):
            paths.append(Path(file_or_directory_name))
        return cls(file_or_directory_name, paths)

    def compile(self) -> None:
        for path in self.file_paths:
            self.path = path.__str__()
            inp = self

            tokenized = path.__str__().replace(".jack", "T.jack")
            jack_file_tokens = File(FileFormat.xml.convert(Path(tokenized)))
            compiler = DefaultCompiler()
            jack_file_tokens.save(compiler.tokenize(inp))

            jack_file = File(FileFormat.xml.convert(path))
            compiler = DefaultCompiler()
            jack_file.save(compiler.compile(inp))

    def __iter__(self) -> Iterator[str]:
        yield from File(Path(self.path)).load()


class Compiler(Protocol):  # pragma: no cover
    def translate(self, vm: Iterable[str], starter: str) -> List[str]:
        pass

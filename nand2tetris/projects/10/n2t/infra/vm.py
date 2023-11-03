from __future__ import annotations

import os
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable, Iterator, List, Protocol

from n2t.core.vm_translator.facade import VmTranslator as DefaultVmTranslator
from n2t.infra.io import File, FileFormat


@dataclass
class VmProgram:  # TODO: your work for Projects 7 and 8 starts here
    path: str
    class_names: List[str]
    file_paths: List[Path]

    @classmethod
    def load_from(cls, file_or_directory_name: str) -> VmProgram:
        # classes = []
        # paths = []
        # for root, dirs, files in os.walk(file_or_directory_name):
        #     for file in files:
        #         if file.__contains__(".vm"):
        #             class_name = file.split(".vm")[0]
        #             classes.append(class_name)
        #             path = Path(os.path.join(root, file))
        #             paths.append(path)
        #
        # translator = DefaultVmTranslator(classes, paths, file_or_directory_name)
        # return cls(Path(file_or_directory_name), translator)
        classes = []
        paths = []
        for root, dirs, files in os.walk(file_or_directory_name):
            for file in files:
                if file.__contains__(".vm"):
                    class_name = file.split(".vm")[0]
                    classes.append(class_name)
                    path = Path(os.path.join(root, file))
                    paths.append(path)

        return cls(file_or_directory_name, classes, paths)

    def translate(self) -> None:
        if self.path.__contains__(".vm"):
            vm_file = File(FileFormat.asm.convert(Path(self.path)))
            translator = DefaultVmTranslator(self.path.split("/")[-1].split(".vm")[0])
            vm_file.save(translator.translate(self, ""))
            return

        lines: List[str] = []

        if len(self.file_paths) > 1:
            lines += ["@256", "D=A", "@SP", "M=D"]

        for i, path in enumerate(self.file_paths):
            vm = File(path).load()
            translator = DefaultVmTranslator(self.class_names[i])
            starter = ""
            if len(self.file_paths) > 1:
                starter = "call Sys.init 0"
            lines += translator.translate(vm, starter)
            i += 1
        output_file_path = self.path + "/" + self.path.split("/")[-1] + ".asm"
        File(Path(output_file_path)).save(lines)

    def __iter__(self) -> Iterator[str]:
        yield from File(Path(self.path)).load()


class VmTranslator(Protocol):  # pragma: no cover
    def translate(self, vm: Iterable[str], starter: str) -> List[str]:
        pass

from __future__ import annotations

import json
from dataclasses import dataclass
from typing import Iterable

from n2t.core.executor.cpu import Cpu


@dataclass
class Executor:
    @classmethod
    def create(cls) -> Executor:
        return cls()

    def execute(self, hack: Iterable[str], cycles: int) -> Iterable[str]:
        cpu = Cpu(list(hack))
        i = 0

        while cpu.has_instructions() and (cycles == -1 or i < cycles):
            cpu.execute_next_instruction()
            i += 1

        return json.dumps(
            {"RAM": dict(sorted(cpu.ram.items(), key=lambda x: int(x[0])))}, indent=2
        ).split("\n")

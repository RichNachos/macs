import filecmp
from pathlib import Path

import pytest

from n2t.runner.cli import run_assembler

_TEST_PROGRAMS = ["empty", "addL", "maxL", "rectL", "pongL", "max", "rect", "pong"]


@pytest.mark.parametrize("program", _TEST_PROGRAMS)
def test_should_assemble(program: str, asm_directory: Path) -> None:
    asm_file = str(asm_directory.joinpath(f"{program}.asm"))

    run_assembler(asm_file)

    assert filecmp.cmp(
        shallow=False,
        f1=str(asm_directory.joinpath(f"{program}.cmp")),
        f2=str(asm_directory.joinpath(f"{program}.hack")),
    )

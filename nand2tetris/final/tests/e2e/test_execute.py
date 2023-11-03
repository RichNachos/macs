import filecmp
from pathlib import Path

import pytest

from n2t.runner.cli import run_executor

_TEST_PROGRAMS = ["addL", "sum", "pong", "overflow"]


@pytest.mark.parametrize("program", _TEST_PROGRAMS)
def test_should_execute(program: str, exec_directory: Path) -> None:
    # We don't test .hack files, just their respective .asm files
    asm_file = str(exec_directory.joinpath(f"{program}.asm"))

    run_executor(asm_file, cycles=40000)  # Needs exactly 40k cycles for pong test!

    assert filecmp.cmp(
        shallow=False,
        f1=str(exec_directory.joinpath(f"{program}.cmp")),
        f2=str(exec_directory.joinpath(f"{program}.json")),
    )

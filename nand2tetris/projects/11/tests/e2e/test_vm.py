import os
import subprocess
from pathlib import Path

import pytest

from n2t.runner.cli import run_vm_translator

_TEST_PROGRAMS = ["SimpleAdd", "StackTest", "BasicTest", "PointerTest", "StaticTest"]
_TEST_DIRECTORY_PROGRAMS = [
    "BasicLoop",
    "FibonacciElement",
    "FibonacciSeries",
    "NestedCall",
    "SimpleFunction",
    "StaticsTest",
]

# TODO: Change this to your own CPUEmulator path!
_CPU_EMU_PATH = "/home/georgi/Desktop/university/nand2tetris/tools/CPUEmulator.sh"


@pytest.mark.parametrize("program", _TEST_PROGRAMS)
def test_should_translate(program: str, vm_directory: Path) -> None:
    vm_file = str(vm_directory.joinpath(f"{program}.vm"))

    run_vm_translator(vm_file)

    vm_test_file = str(vm_directory.joinpath(f"{program}.tst"))
    checker = subprocess.run(args=[_CPU_EMU_PATH, vm_test_file], capture_output=True)
    result = checker.stdout.decode() + checker.stderr.decode()

    assert result == "End of script - Comparison ended successfully\n"


@pytest.mark.parametrize("directory_program", _TEST_DIRECTORY_PROGRAMS)
def test_should_translate_directory(directory_program: str, vm_directory: Path) -> None:
    vm_file = str(vm_directory.joinpath(f"{directory_program}"))

    run_vm_translator(vm_file)

    vm_test_file = str(
        vm_directory.joinpath(f"{directory_program}/{directory_program}.tst")
    )
    checker = subprocess.run(args=[_CPU_EMU_PATH, vm_test_file], capture_output=True)
    result = checker.stdout.decode() + checker.stderr.decode()

    assert result == "End of script - Comparison ended successfully\n"

    os.remove(vm_test_file.split(".tst")[0] + ".asm")
    os.remove(vm_test_file.split(".tst")[0] + ".out")

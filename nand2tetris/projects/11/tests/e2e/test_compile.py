import os
import subprocess
from pathlib import Path

import pytest

from n2t.runner.cli import run_compiler

_TEST_DIRECTORY_PROGRAMS = [
    "ArrayTest",
    "ExpressionLessSquare",
    "Square",
]

# TODO: Change this to your own TextComparer path!
_TEXT_COMPARER_PATH = (
    "/home/georgi/Desktop/university/nand2tetris/tools/TextComparer.sh"
)


@pytest.mark.parametrize("directory_program", _TEST_DIRECTORY_PROGRAMS)
def test_should_compile(directory_program: str, jack_directory: Path) -> None:
    jack_dir = str(jack_directory.joinpath(f"{directory_program}"))

    run_compiler(jack_dir)

    for root, dirs, files in os.walk(jack_dir):
        for file in files:
            if file.__contains__(".xml"):
                path = Path(os.path.join(root, file))
                compare = Path(os.path.join(root, file)).with_suffix(".tst")

                checker = subprocess.run(
                    args=[_TEXT_COMPARER_PATH, path, compare],
                    capture_output=True,
                )
                result = checker.stdout.decode() + checker.stderr.decode()

                assert result == "Comparison ended successfully\n"


@pytest.mark.parametrize("directory_program", _TEST_DIRECTORY_PROGRAMS)
def test_should_tokenize(directory_program: str, jack_directory: Path) -> None:
    jack_dir = str(jack_directory.joinpath(f"{directory_program}"))

    run_compiler(jack_dir)

    for root, dirs, files in os.walk(jack_dir):
        for file in files:
            if file.__contains__("T.xml"):
                path = Path(os.path.join(root, file))
                compare = Path(os.path.join(root, file).replace("T.xml", "T.tst"))

                checker = subprocess.run(
                    args=[_TEXT_COMPARER_PATH, path, compare],
                    capture_output=True,
                )
                result = checker.stdout.decode() + checker.stderr.decode()

                assert result == "Comparison ended successfully\n"

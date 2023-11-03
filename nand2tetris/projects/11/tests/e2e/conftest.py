from pathlib import Path
from typing import Iterable

import pytest

from n2t.infra.io import remove_files
from tests.e2e.test_compile import _TEST_DIRECTORY_PROGRAMS


@pytest.fixture(scope="module")
def hack_directory(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath("tests", "e2e", "hack")

    yield name

    remove_files(pattern=str(name.joinpath("*.asm")))


@pytest.fixture(scope="module")
def asm_directory(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath("tests", "e2e", "asm")

    yield name

    remove_files(pattern=str(name.joinpath("*.hack")))


@pytest.fixture(scope="module")
def vm_directory(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath("tests", "e2e", "vm")

    yield name

    remove_files(pattern=str(name.joinpath("*.asm")))
    remove_files(pattern=str(name.joinpath("*.out")))


@pytest.fixture(scope="module")
def jack_directory(pytestconfig: pytest.Config) -> Iterable[Path]:
    name = pytestconfig.rootpath.joinpath("tests", "e2e", "jack")

    yield name

    for directory in _TEST_DIRECTORY_PROGRAMS:
        remove_files(pattern=str(name.joinpath(directory, "*.xml")))

from pathlib import Path
from typing import Iterable

import pytest

from n2t.infra.io import remove_files


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

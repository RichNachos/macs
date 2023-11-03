# Nand2Tetris Python Starter

The repository contains (Python) starter project for Nand2Tetris spin-off course read in
[Free University of Tbilisi](https://www.freeuni.edu.ge/en), for the School of Math and Computer Science.

The original [Nand2Tetris](https://www.nand2tetris.org/) course was created by
[Noam Nisan](https://www.cs.huji.ac.il/~noam/) and [Shimon Schocken](https://www.shimonschocken.com/).
In a spirit to teach how to build a general-purpose computer system and a modern software hierarchy from the ground up.

## Coding Conventions

Keep python conventions in mind:

- variable_name
- function_name
- ClassName
- CONSTANT_NAME
- file_name.py

## Linters/Formatters

Use tools described below to avoid endless arguments related to coding style and formatting.
They help you to catch errors, and make code more readable for your peers.

- [black](https://github.com/psf/black) auto formatter
- [isort](https://github.com/PyCQA/isort) to order imports
- [mypy](https://github.com/python/mypy) to check your static types
- [flake8](https://github.com/PyCQA/flake8) to catch common style issues

Configuration for all the tools mentioned above is provided with the project.
You can use `make` to run each of these tools or see how to run them manually
inside the `Makefile`.

## Requirements

Use following command to install needed requirements `pip install -r requirements.txt`
you can extend requirements.txt with more packages if you need to.

## Usage

Use following command to see usage instructions `python -m n2t --help`

## Licence

This project is licensed under the terms of the `MIT license`.

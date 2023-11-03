from typer import Typer, echo

from n2t.infra import AsmProgram, ExecProgram

cli = Typer(
    name="Nand 2 Tetris Software",
    no_args_is_help=True,
    add_completion=False,
)


@cli.command("assemble", no_args_is_help=True)
def run_assembler(assembly_file: str) -> None:
    echo(f"Assembling {assembly_file}")
    AsmProgram.load_from(assembly_file).assemble()
    echo("Done!")


@cli.command("execute", no_args_is_help=True)
def run_executor(file: str, cycles: int = -1) -> None:
    if cycles == -1:
        echo(f"Executing {file} until program halts")
    else:
        echo(f"Executing {file} for {cycles} cycles")
    ExecProgram.load_from(file, cycles).execute()
    echo("Done!")

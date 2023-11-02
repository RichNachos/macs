from app.classes.logger import ConsoleLogger


def test_stdoutlogger() -> None:
    logger = ConsoleLogger()
    logger.log("testing stdout log")

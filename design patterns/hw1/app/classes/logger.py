from abc import abstractmethod
from dataclasses import dataclass


@dataclass
class SimulationLogger:
    # Interface for a generic simulation logger
    @abstractmethod
    def log(self, text: str) -> None:
        pass


@dataclass
class ConsoleLogger(SimulationLogger):
    # Concrete stdout logger
    def log(self, text: str) -> None:
        print(text)

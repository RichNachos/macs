from abc import abstractmethod
from dataclasses import dataclass, field

from app import constants as c
from app.classes.creature import Creature
from app.classes.logger import ConsoleLogger, SimulationLogger


@dataclass
class FightHandlerInterface:
    @abstractmethod
    # Returns fight results
    def fight(self, predator: Creature, prey: Creature) -> str:
        pass


@dataclass
class BasicFightHandler(FightHandlerInterface):
    def fight(self, predator: Creature, prey: Creature) -> str:
        while predator.get_health() > 0 and prey.get_health() > 0:
            prey.take_damage(predator.get_damage())
            if prey.get_health() == 0:
                return str(c.PREDATOR_WON)
            predator.take_damage(prey.get_damage())
            if predator.get_health() == 0:
                return str(c.PREY_WON)
        return "ERROR"


@dataclass
class SimulationInterface:
    predator: Creature
    prey: Creature
    fight_handler: FightHandlerInterface = field(default_factory=BasicFightHandler)
    logger: SimulationLogger = field(default_factory=ConsoleLogger)

    @abstractmethod
    def simulate(self) -> None:
        pass


@dataclass
class BasicSimulation(SimulationInterface):
    def simulate(self) -> None:
        self.logger.log("PREDATOR CHARACTERISTICS")
        self.logger.log(self.predator.characteristics())
        self.logger.log("********************************")
        self.logger.log("PREY CHARACTERISTICS")
        self.logger.log(self.prey.characteristics())
        self.logger.log("STARTING SIMULATION\n")
        while True:
            self.predator.move()
            if self.predator.get_location() >= self.prey.get_location():
                self.logger.log(self.fight_handler.fight(self.predator, self.prey))
                break
            if self.predator.exhausted():
                self.logger.log(c.PREY_WON)
                break
            self.prey.move()

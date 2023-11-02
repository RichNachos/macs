import app.constants as constants
from app.classes.creature_builder import RandomCreatureBuilder
from app.classes.logger import ConsoleLogger
from app.classes.simulation import BasicSimulation


def main() -> None:
    # Run simulations
    print("Getting ready to run " + str(constants.N_SIMULATIONS) + " simulation(s)..")
    creature_generator = RandomCreatureBuilder()
    logger = ConsoleLogger()

    for i in range(0, constants.N_SIMULATIONS):
        predator = creature_generator.create_predator_creature()
        prey = creature_generator.create_prey_creature()

        sim = BasicSimulation(predator, prey, logger=logger)
        sim.simulate()
        logger.log("---------------------------------")


if __name__ == "__main__":
    main()

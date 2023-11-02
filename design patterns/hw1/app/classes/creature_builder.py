from random import choice, randint
from typing import List

from app import constants as c
from app.classes.creature import Creature
from app.classes.creature_handlers import (
    BasicDamageHandler,
    BasicMovementHandler,
    DamageHandlerInterface,
    MovementHandlerInterface,
)
from app.classes.creature_traits import DAMAGE_TRAITS_ALL, DamageTrait, MovementTrait


class CreatureBuilder:
    info: List[int]
    movement_traits: List[MovementTrait]
    damage_traits: List[DamageTrait]
    movement_handler: MovementHandlerInterface
    damage_handler: DamageHandlerInterface

    def __init__(self) -> None:
        self.info = [0] * c.LIST_SIZE
        self.movement_traits = []
        self.damage_traits = []
        self.movement_handler = BasicMovementHandler()
        self.damage_handler = BasicDamageHandler()

    # Setters

    def set_health(self, health: int) -> None:
        self.info[c.HEALTH_IND] = health

    def set_stamina(self, stamina: int) -> None:
        self.info[c.STAMINA_IND] = stamina

    def set_power(self, power: int) -> None:
        self.info[c.POWER_IND] = power

    def set_location(self, location: int) -> None:
        self.info[c.LOCATION_IND] = location

    def set_legs(self, legs: int) -> None:
        self.info[c.LEGS_IND] = legs

    def set_wings(self, wings: int) -> None:
        self.info[c.WINGS_IND] = wings

    def set_movement_traits(self, movement_traits: List[MovementTrait]) -> None:
        self.movement_traits = movement_traits

    def set_damage_traits(self, damage_traits: List[DamageTrait]) -> None:
        self.damage_traits = damage_traits

    def add_movement_trait(self, movement_trait: MovementTrait) -> None:
        self.movement_traits.append(movement_trait)

    def add_damage_trait(self, damage_trait: DamageTrait) -> None:
        self.damage_traits.append(damage_trait)

    def set_damage_handler(self, damage_handler: DamageHandlerInterface) -> None:
        self.damage_handler = damage_handler

    def set_movement_handler(self, movement_handler: MovementHandlerInterface) -> None:
        self.movement_handler = movement_handler

    def build(self) -> Creature:
        return Creature(
            info=self.info,
            damage_traits=self.damage_traits,
            movement_handler=self.movement_handler,
            damage_handler=self.damage_handler,
        )


class RandomCreatureBuilder:
    builder: CreatureBuilder

    def create_basis(self) -> None:
        self.builder.set_health(randint(c.HEALTH_MIN, c.HEALTH_MAX))
        self.builder.set_stamina(randint(c.STAMINA_MIN, c.STAMINA_MAX))
        self.builder.set_power(randint(c.POWER_MIN, c.POWER_MAX))
        self.builder.set_legs(choice(c.LEGS))
        self.builder.set_wings(choice(c.WINGS))

        # Damage Traits
        for damage_trait_class in DAMAGE_TRAITS_ALL:
            if choice([True, False]):
                self.builder.add_damage_trait(damage_trait_class())
        # CONTINUE FOR DAMAGE TRAITS BRUV!
        # AND FIX THE BUG WHY DOES RANDOM GENERATE SAME SHIT FOR BOTH PRED AND PREY???

    def create_predator_creature(self) -> Creature:
        self.builder = CreatureBuilder()
        self.create_basis()
        self.builder.set_location(
            randint(c.PREDATOR_LOCATION_MIN, c.PREDATOR_LOCATION_MAX)
        )
        return self.builder.build()
        pass

    def create_prey_creature(self) -> Creature:
        self.builder = CreatureBuilder()
        self.create_basis()
        self.builder.set_location(randint(c.PREY_LOCATION_MIN, c.PREY_LOCATION_MAX))
        return self.builder.build()
        pass

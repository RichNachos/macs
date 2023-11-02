from dataclasses import dataclass
from typing import List

import app.constants as c
from app.classes.creature_handlers import (
    DamageHandlerInterface,
    MovementHandlerInterface,
)
from app.classes.creature_traits import DamageTrait, MovementTrait, movement_traits_all


@dataclass
class Creature:
    info: List[int]
    movement_traits: List[MovementTrait]
    damage_traits: List[DamageTrait]
    movement_handler: MovementHandlerInterface
    damage_handler: DamageHandlerInterface

    def __init__(
        self,
        info: List[int],
        damage_traits: List[DamageTrait],
        movement_handler: MovementHandlerInterface,
        damage_handler: DamageHandlerInterface,
    ):
        self.info = info
        self.damage_traits = damage_traits
        self.movement_handler = movement_handler
        self.damage_handler = damage_handler
        self.movement_traits = []
        for movement_trait in movement_traits_all:
            if movement_trait.trait_requirement.trait_allowed(self.info):
                self.movement_traits.append(movement_trait)

    def get_damage(self) -> int:
        return int(self.damage_handler.get_damage(self.info, self.damage_traits))

    def move(self) -> None:
        if not self.exhausted():
            self.info = self.movement_handler.next_position(
                self.info, self.movement_traits
            )

    def exhausted(self) -> bool:
        if self.get_stamina() <= 0:
            return True
        else:
            return False

    def take_damage(self, damage: int) -> None:
        self.info[c.HEALTH_IND] = self.get_health() - damage
        if self.get_health() < 0:
            self.info[c.HEALTH_IND] = 0

    # Getters
    def get_health(self) -> int:
        return int(self.info[c.HEALTH_IND])

    def get_stamina(self) -> int:
        return int(self.info[c.STAMINA_IND])

    def get_power(self) -> int:
        return int(self.info[c.POWER_IND])

    def get_location(self) -> int:
        return int(self.info[c.LOCATION_IND])

    def get_legs(self) -> int:
        return int(self.info[c.LEGS_IND])

    def get_wings(self) -> int:
        return int(self.info[c.WINGS_IND])

    def characteristics(self) -> str:
        string = (
            "Health: {0}\n"
            "Stamina: {1}\n"
            "Power: {2}\n"
            "Location: {3}\n"
            "Legs: {4}\n"
            "Wings: {5}\n\n"
            "{6}\n"
            "{7}\n"
        )
        movement_traits = "Movement Traits: "
        for movement_trait in self.movement_traits:
            movement_traits += movement_trait.description() + ", "
        damage_traits = "Damage Traits: "
        for damage_trait in self.damage_traits:
            damage_traits += damage_trait.description() + ", "
        return string.format(
            self.get_health(),
            self.get_stamina(),
            self.get_power(),
            self.get_location(),
            self.get_legs(),
            self.get_wings(),
            movement_traits,
            damage_traits,
        )

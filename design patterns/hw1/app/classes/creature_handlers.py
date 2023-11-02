from abc import abstractmethod
from dataclasses import dataclass
from typing import List

from app import constants as c
from app.classes.creature_traits import DamageTrait, MovementTrait


@dataclass
class MovementHandlerInterface:
    @abstractmethod
    def next_position(
        self, info: List[int], movement_traits: List[MovementTrait]
    ) -> List[int]:
        pass


@dataclass
class BasicMovementHandler(MovementHandlerInterface):
    def next_position(
        self, info: List[int], movement_traits: List[MovementTrait]
    ) -> List[int]:  # creature info List[int]
        if info[c.STAMINA_IND] <= 0:
            return info
        new_info = info.copy()
        delta = 0
        usage = 0
        for movement_trait in movement_traits:
            if (
                delta < movement_trait.speed
                and movement_trait.stamina_min <= info[c.STAMINA_IND]
            ):
                delta = movement_trait.speed
                usage = movement_trait.stamina_usage

        new_info[c.LOCATION_IND] = info[c.LOCATION_IND] + delta
        new_info[c.STAMINA_IND] = info[c.STAMINA_IND] - usage

        return new_info


@dataclass
class DamageHandlerInterface:
    @abstractmethod
    def get_damage(self, info: List[int], damage_traits: List[DamageTrait]) -> int:
        pass


@dataclass
class BasicDamageHandler(DamageHandlerInterface):
    def get_damage(self, info: List[int], damage_traits: List[DamageTrait]) -> int:
        damage = info[c.POWER_IND]
        for damage_trait in damage_traits:
            if damage_trait.trait_requirement.trait_allowed(info):
                damage = damage + damage_trait.calculate_damage(info)
        return int(damage)

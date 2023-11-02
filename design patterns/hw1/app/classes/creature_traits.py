from abc import abstractmethod
from dataclasses import dataclass
from random import choice
from typing import List

import app.constants as c


# Creature Trait requirement structure
class TraitRequirementInterface:
    @abstractmethod
    def trait_allowed(self, info: List[int]) -> bool:
        pass

    def __init__(self) -> None:
        pass


# Creature Trait structure
@dataclass
class TraitInterface:
    trait_name: str
    trait_requirement: TraitRequirementInterface

    def description(self) -> str:
        return self.trait_name


@dataclass
class MovementTrait(TraitInterface):
    speed: int
    stamina_usage: int
    stamina_min: int


@dataclass
class DamageTrait(TraitInterface):
    # Returns additional damage
    @abstractmethod
    def calculate_damage(self, info: List[int]) -> int:
        pass


# Concrete Creature Trait requirements


class NoRequirements(TraitRequirementInterface):
    def trait_allowed(self, info: List[int]) -> bool:
        return True


class FlyRequirement(TraitRequirementInterface):
    def trait_allowed(self, info: List[int]) -> bool:
        if info[c.WINGS_IND] >= c.FLY_MIN_WINGS:
            return True
        else:
            return False


class RunRequirement(TraitRequirementInterface):
    def trait_allowed(self, info: List[int]) -> bool:
        if info[c.LEGS_IND] >= c.RUN_MIN_LEGS:
            return True
        else:
            return False


class WalkRequirement(TraitRequirementInterface):
    def trait_allowed(self, info: List[int]) -> bool:
        if info[c.LEGS_IND] >= c.WALK_MIN_LEGS:
            return True
        else:
            return False


class HopRequirement(TraitRequirementInterface):
    def trait_allowed(self, info: List[int]) -> bool:
        if info[c.LEGS_IND] >= c.HOP_MIN_LEGS:
            return True
        else:
            return False


class CrawlRequirement(NoRequirements):
    def trait_allowed(self, info: List[int]) -> bool:
        if info[c.LEGS_IND] >= c.CRAWL_MIN_LEGS:
            return True
        else:
            return False


# Concrete Creature Traits

# Damage Traits


class TeethTrait(DamageTrait):
    trait_name = "Teeth"
    trait_requirement = NoRequirements()
    sharpness: int

    def calculate_damage(self, info: List[int]) -> int:
        return self.sharpness

    def description(self) -> str:
        return "Teeth with +" + str(self.sharpness) + " Sharpness"

    def __init__(self, sharpness: int = choice(c.TEETH)):
        self.sharpness = sharpness


class ClawsTrait(DamageTrait):
    trait_name = "Claws"
    trait_requirement = NoRequirements()
    multiplier: int

    def calculate_damage(self, info: List[int]) -> int:
        return int(info[c.POWER_IND] * self.multiplier - info[c.POWER_IND])

    def description(self) -> str:
        if self.multiplier == c.CLAWS[0]:
            return "Small Claws"
        if self.multiplier == c.CLAWS[1]:
            return "Medium Claws"
        if self.multiplier == c.CLAWS[2]:
            return "Big Claws"
        return self.trait_name

    def __init__(self, multiplier: int = choice(c.CLAWS)) -> None:
        self.multiplier = multiplier


# Movement Traits

movement_traits_all = [
    MovementTrait(
        trait_name="Crawl",
        trait_requirement=CrawlRequirement(),
        speed=c.CRAWL_SPEED,
        stamina_min=c.CRAWL_MIN_STAMINA,
        stamina_usage=c.CRAWL_STAMINA_USAGE,
    ),
    MovementTrait(
        trait_name="Hop",
        trait_requirement=HopRequirement(),
        speed=c.HOP_SPEED,
        stamina_min=c.HOP_MIN_STAMINA,
        stamina_usage=c.HOP_STAMINA_USAGE,
    ),
    MovementTrait(
        trait_name="Walk",
        trait_requirement=WalkRequirement(),
        speed=c.WALK_SPEED,
        stamina_min=c.WALK_MIN_STAMINA,
        stamina_usage=c.WALK_STAMINA_USAGE,
    ),
    MovementTrait(
        trait_name="Run",
        trait_requirement=RunRequirement(),
        speed=c.RUN_SPEED,
        stamina_min=c.RUN_MIN_STAMINA,
        stamina_usage=c.RUN_STAMINA_USAGE,
    ),
    MovementTrait(
        trait_name="Fly",
        trait_requirement=FlyRequirement(),
        speed=c.FLY_SPEED,
        stamina_min=c.FLY_MIN_STAMINA,
        stamina_usage=c.FLY_STAMINA_USAGE,
    ),
]

DAMAGE_TRAITS_ALL = [
    TeethTrait,
    ClawsTrait,
]

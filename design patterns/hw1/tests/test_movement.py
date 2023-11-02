from typing import List

from app import constants as c
from app.classes.creature import Creature
from app.classes.creature_handlers import BasicDamageHandler, BasicMovementHandler
from app.classes.creature_traits import DamageTrait


def test_movement() -> None:
    health = 100
    stamina = c.FLY_STAMINA_USAGE * 5 + c.FLY_MIN_STAMINA
    power = 25
    location = 0
    legs = 4
    wings = 2
    info: List[int] = [health, stamina, power, location, legs, wings]
    damage_traits: List[DamageTrait] = []
    creature = Creature(
        info,
        damage_traits,
        BasicMovementHandler(),
        BasicDamageHandler(),
    )

    assert creature.get_location() == location
    for i in range(6):
        creature.move()  # Creature uses fly
        print(creature.info)
        assert creature.get_location() == location + c.FLY_SPEED
        assert creature.get_stamina() == stamina - c.FLY_STAMINA_USAGE
        location = location + c.FLY_SPEED
        stamina = stamina - c.FLY_STAMINA_USAGE

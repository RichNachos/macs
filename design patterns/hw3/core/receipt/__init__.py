from dataclasses import dataclass
from typing import List

from core.item import Item


@dataclass
class Receipt:
    items: List[Item]
    grand_total: float

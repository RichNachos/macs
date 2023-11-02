from dataclasses import dataclass
from typing import List


@dataclass
class ReportRow:
    item_name: str
    units: int


@dataclass
class Report:
    revenue: float
    table: List[ReportRow]
    closed_receipts: int

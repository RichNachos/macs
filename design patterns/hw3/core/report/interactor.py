from dataclasses import dataclass

from core.report import Report
from infra.dao import Dao


@dataclass
class ReportRequest:
    pass


@dataclass
class ReportResponse:
    status: int
    report: Report


@dataclass
class ReportInteractor:
    dao: Dao

    def get_report(self, request: ReportRequest) -> ReportResponse:
        report = self.dao.report()
        response = ReportResponse(200, report)
        return response

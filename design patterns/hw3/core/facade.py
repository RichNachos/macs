from dataclasses import dataclass

from core.receipt.interactor import (
    AddItemReceiptRequest,
    AddItemReceiptResponse,
    CloseReceiptRequest,
    CloseReceiptResponse,
    OpenReceiptRequest,
    OpenReceiptResponse,
    ReceiptInteractor,
    ShowReceiptRequest,
    ShowReceiptResponse,
)
from core.report.interactor import ReportInteractor, ReportRequest, ReportResponse
from infra.dao import Dao


@dataclass
class AwesomePosService:
    receipt_interactor: ReceiptInteractor
    report_interactor: ReportInteractor

    def open_receipt(self, request: OpenReceiptRequest) -> OpenReceiptResponse:
        return self.receipt_interactor.open_receipt(request)

    def close_receipt(self, request: CloseReceiptRequest) -> CloseReceiptResponse:
        return self.receipt_interactor.close_receipt(request)

    def add_item_to_receipt(
        self, request: AddItemReceiptRequest
    ) -> AddItemReceiptResponse:
        return self.receipt_interactor.register_item(request)

    def show_receipt(self, request: ShowReceiptRequest) -> ShowReceiptResponse:
        return self.receipt_interactor.show_receipt(request)

    def x_report(self, request: ReportRequest) -> ReportResponse:
        return self.report_interactor.get_report(request)

    @classmethod
    def create(cls, dao: Dao) -> "AwesomePosService":
        return cls(
            receipt_interactor=ReceiptInteractor(dao=dao),
            report_interactor=ReportInteractor(dao=dao),
        )

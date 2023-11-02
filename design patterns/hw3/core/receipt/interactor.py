from dataclasses import dataclass

from core.receipt import Receipt
from infra.dao import Dao


@dataclass
class OpenReceiptRequest:
    pass


@dataclass
class OpenReceiptResponse:
    status: int
    id: int


@dataclass
class CloseReceiptRequest:
    id: int


@dataclass
class CloseReceiptResponse:
    status: int


@dataclass
class AddItemReceiptRequest:
    receipt_id: int
    item_id: int


@dataclass
class AddItemReceiptResponse:
    status: int


@dataclass
class ShowReceiptRequest:
    id: int


@dataclass
class ShowReceiptResponse:
    status: int
    receipt: Receipt


@dataclass
class ReceiptInteractor:
    dao: Dao

    def open_receipt(self, request: OpenReceiptRequest) -> OpenReceiptResponse:
        id = self.dao.open_receipt()
        response = OpenReceiptResponse(201, id)
        return response

    def close_receipt(self, request: CloseReceiptRequest) -> CloseReceiptResponse:
        if not self.dao.receipt_exists(request.id) or not self.dao.receipt_status(
            request.id
        ):
            return CloseReceiptResponse(404)

        self.dao.close_receipt(request.id)
        return CloseReceiptResponse(200)

    def register_item(self, request: AddItemReceiptRequest) -> AddItemReceiptResponse:
        if not self.dao.receipt_exists(request.receipt_id):
            return AddItemReceiptResponse(404)
        if not self.dao.receipt_status(request.receipt_id):
            return AddItemReceiptResponse(403)
        if not self.dao.item_exists(request.item_id):
            return AddItemReceiptResponse(404)

        if self.dao.register_item(
            receipt_id=request.receipt_id, item_id=request.item_id
        ):
            return AddItemReceiptResponse(200)
        return AddItemReceiptResponse(400)

    def show_receipt(self, request: ShowReceiptRequest) -> ShowReceiptResponse:
        if not self.dao.receipt_exists(request.id):
            return ShowReceiptResponse(404, Receipt([], 0))
        return ShowReceiptResponse(200, self.dao.get_receipt(request.id))

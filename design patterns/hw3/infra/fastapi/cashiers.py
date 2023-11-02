from fastapi import APIRouter, Depends, HTTPException

from core.facade import AwesomePosService
from core.receipt.interactor import (
    AddItemReceiptRequest,
    AddItemReceiptResponse,
    CloseReceiptRequest,
    CloseReceiptResponse,
    OpenReceiptRequest,
    OpenReceiptResponse,
)
from infra.fastapi.dependables import get_core

cashier_api = APIRouter()


@cashier_api.get("/cashier/open_receipt")
async def open_receipt(
    core: AwesomePosService = Depends(get_core),
) -> OpenReceiptResponse:
    response = core.open_receipt(request=OpenReceiptRequest())
    if response.status >= 300:
        raise HTTPException(status_code=response.status)
    return response


@cashier_api.get("/cashier/close_receipt/{receipt_id}")
async def close_receipt(
    receipt_id: int, core: AwesomePosService = Depends(get_core)
) -> CloseReceiptResponse:
    response = core.close_receipt(request=CloseReceiptRequest(receipt_id))
    if response.status >= 300:
        raise HTTPException(status_code=response.status)
    return response


@cashier_api.get("/cashier/add_item/{receipt_id}_{item_id}")
async def add_item(
    receipt_id: int, item_id: int, core: AwesomePosService = Depends(get_core)
) -> AddItemReceiptResponse:
    response = core.add_item_to_receipt(
        request=AddItemReceiptRequest(receipt_id, item_id)
    )
    if response.status >= 300:
        raise HTTPException(status_code=response.status)
    return response

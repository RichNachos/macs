from fastapi import APIRouter, Depends, HTTPException

from core.facade import AwesomePosService
from core.receipt.interactor import ShowReceiptRequest, ShowReceiptResponse
from infra.fastapi.dependables import get_core

customer_api = APIRouter()


@customer_api.get("/receipts/{receipt_id}")
async def show_receipt(
    receipt_id: int, core: AwesomePosService = Depends(get_core)
) -> ShowReceiptResponse:
    response = core.show_receipt(request=ShowReceiptRequest(receipt_id))
    if response.status >= 300:
        raise HTTPException(status_code=response.status)
    return response

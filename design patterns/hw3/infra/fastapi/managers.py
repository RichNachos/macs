from fastapi import APIRouter, Depends, HTTPException

from core.facade import AwesomePosService
from core.report.interactor import ReportRequest, ReportResponse
from infra.fastapi.dependables import get_core

manager_api = APIRouter()


@manager_api.get("/manager/x_report")
async def fetch_x_report(core: AwesomePosService = Depends(get_core)) -> ReportResponse:
    response = core.x_report(request=ReportRequest())
    if response.status >= 300:
        raise HTTPException(status_code=response.status)
    return response

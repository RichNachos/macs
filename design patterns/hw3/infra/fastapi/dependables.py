from starlette.requests import Request

from core.facade import AwesomePosService


def get_core(request: Request) -> AwesomePosService:
    assert isinstance(request.app.state.core, AwesomePosService)
    return request.app.state.core

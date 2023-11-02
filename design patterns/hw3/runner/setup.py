import sqlite3

from fastapi import FastAPI

from core.facade import AwesomePosService
from infra.dao import build_database
from infra.dao.sqlite_dao import SqliteDao
from infra.fastapi.cashiers import cashier_api
from infra.fastapi.customers import customer_api
from infra.fastapi.managers import manager_api

DB_NAME = "pos_db"


def include_routers(app: FastAPI) -> None:
    app.include_router(manager_api)
    app.include_router(customer_api)
    app.include_router(cashier_api)


def setup() -> FastAPI:
    app = FastAPI()
    include_routers(app)
    con = sqlite3.connect(DB_NAME)

    # Will build database on first start up
    build_database(DB_NAME)

    app.state.core = AwesomePosService.create(SqliteDao(con))

    return app

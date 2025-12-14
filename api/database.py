import os
from contextlib import contextmanager
from typing import Iterator, Optional

from sqlalchemy.engine import Engine
from sqlmodel import Session, SQLModel, create_engine

DEFAULT_SQLITE_URL = "sqlite:///./data.db"
DATABASE_URL = os.getenv("DATABASE_URL", DEFAULT_SQLITE_URL)
ENGINE: Engine = create_engine(
    DATABASE_URL,
    echo=False,
    connect_args={"check_same_thread": False} if DATABASE_URL.startswith("sqlite") else {},
)


def get_engine() -> Engine:
    return ENGINE


def init_db(engine: Optional[Engine] = None) -> None:
    engine = engine or get_engine()
    SQLModel.metadata.create_all(engine)


@contextmanager
def session_scope(engine: Optional[Engine] = None) -> Iterator[Session]:
    engine = engine or get_engine()
    with Session(engine) as session:
        yield session

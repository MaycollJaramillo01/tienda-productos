from fastapi import Depends, FastAPI, HTTPException, status
from fastapi.responses import JSONResponse
from sqlmodel import Session, select

from .database import get_engine, init_db, session_scope
from .models import Product, ProductCreate, ProductRead, ProductUpdate

app = FastAPI(title="Tienda de Productos API", version="1.0.0")


@app.on_event("startup")
def on_startup() -> None:
    init_db(get_engine())


def get_session() -> Session:
    with session_scope(get_engine()) as session:
        yield session


@app.get("/health")
def health_check() -> JSONResponse:
    return JSONResponse({"status": "ok"})


@app.post(
    "/products",
    response_model=ProductRead,
    status_code=status.HTTP_201_CREATED,
)
def create_product(payload: ProductCreate, session: Session = Depends(get_session)) -> Product:
    product = Product(**payload.dict())
    session.add(product)
    session.commit()
    session.refresh(product)
    return product


@app.get("/products", response_model=list[ProductRead])
def list_products(session: Session = Depends(get_session)) -> list[Product]:
    results = session.exec(select(Product)).all()
    return results


@app.get("/products/{product_id}", response_model=ProductRead)
def get_product(product_id: int, session: Session = Depends(get_session)) -> Product:
    product = session.get(Product, product_id)
    if not product:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Product not found")
    return product


@app.put("/products/{product_id}", response_model=ProductRead)
def update_product(
    product_id: int, payload: ProductUpdate, session: Session = Depends(get_session)
) -> Product:
    product = session.get(Product, product_id)
    if not product:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Product not found")

    update_data = payload.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(product, key, value)

    session.add(product)
    session.commit()
    session.refresh(product)
    return product


@app.delete(
    "/products/{product_id}",
    status_code=status.HTTP_204_NO_CONTENT,
)
def delete_product(product_id: int, session: Session = Depends(get_session)) -> None:
    product = session.get(Product, product_id)
    if not product:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Product not found")

    session.delete(product)
    session.commit()
    return None


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)

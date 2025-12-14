 Proyecto Personal: Tienda de Productos

Descripción general
Tienda de Productos: Es una aplicación móvil que permite manejar un inventario de productos mostrando su imagen, nombre, precio y descripción.
El sistema implementa las operaciones CRUD (Crear, Leer, Actualizar y Eliminar) para administrar los productos de forma completa y sencilla.
La idea principal de esto es ofrecer una pequeña plataforma tipo tienda virtual en la que el usuario pueda agregar productos desde su celular, subir imágenes desde la galería o bien desde su cámara, y consultar la información de manera ordenada y visual.

Objetivo del proyecto
Desarrollar una aplicación que permita practicar los conceptos de desarrollo móvil, integración de imágenes, CRUD, y el uso de APIs y mapas para agregar más funcionalidades como la ubicación de la tienda o del producto.

Funcionalidades principales
- Crear (Insertar): Permite agregar un nuevo producto ingresando su nombre, precio, descripción e imagen desde la galería o cámara del dispositivo. 
- Leer (Listar): Muestra una lista con todos los productos almacenados, mostrando imagen, nombre y precio. 
- Actualizar (Editar): Permite modificar los datos de un producto existente. 
- Eliminar (Borrar): Elimina un producto de la lista. 
- Imágenes: Cada producto puede incluir una fotografía seleccionada desde la cámara o galería. 
- (Futuro)Mapas: Se planea incluir un mapa para ubicar el lugar donde se vende el producto o la tienda. 
- (Futuro)Consumo de API: Se integrarán APIs externas para mejorar la funcionalidad (por ejemplo, conversión de precios, ubicación o inventarios). 


Estructura del proyecto
1. Lista de productos
   - Muestra todos los productos registrados con imagen, nombre y precio.  
   - Botón para agregar un nuevo producto.

2.  Agregar producto
   - Formulario para insertar un nuevo producto con los siguientes campos:  
     - Nombre del producto  
     - Precio  
     - Descripción  
     - Botón para seleccionar imagen desde cámara o galería  
     - Botón (Guardar).

3. Editar producto
   - Permite modificar los datos de un producto ya existente.  
   - Botón (Actualizar).

4. Detalle del producto
   - Muestra la información completa del producto seleccionado.  
   - Incluye botones (Editar y Eliminar).



Autor
Nombre: Yendry Morera  
Profesor: Ever Barahona  
Fecha: 16 de octubre del 2025  


<img width="725" height="1037" alt="image" src="https://github.com/user-attachments/assets/adff88bb-7e69-4c20-ae8a-10b9262c7862" />

<img width="796" height="1081" alt="image" src="https://github.com/user-attachments/assets/1c8e19f8-4075-4f6e-aaa4-911f1ac4f6ad" />

<img width="804" height="1106" alt="image" src="https://github.com/user-attachments/assets/64a2e091-d5de-45b8-a73a-a9ca854a8b92" />

<img width="713" height="1194" alt="image" src="https://github.com/user-attachments/assets/fd0cf629-f381-49f1-ba31-800d7679f15a" />

## API REST para la app móvil
Para soportar las operaciones CRUD desde el cliente móvil se añadió un backend propio construido con **FastAPI**. El servicio expone un conjunto de endpoints REST y puede ejecutarse localmente con SQLite o desplegarse en la nube usando un contenedor.

### Requisitos
- Python 3.11+.
- Las dependencias listadas en `requirements.txt`.

### Ejecución local
1. Crear y activar un entorno virtual (opcional pero recomendado):
   ```bash
   python -m venv .venv
   source .venv/bin/activate
   ```
2. Instalar dependencias:
   ```bash
   pip install -r requirements.txt
   ```
3. Iniciar el servidor de desarrollo en http://localhost:8000:
   ```bash
   uvicorn api.main:app --reload
   ```

El servicio crea automáticamente una base de datos SQLite (`data.db`) en la raíz del repositorio si no existe.

### Configuración de base de datos
- `DATABASE_URL`: cadena de conexión opcional para usar un motor diferente (por ejemplo, PostgreSQL en la nube). Si no se define, se usa SQLite local.

### Despliegue en la nube (ejemplo con contenedor)
1. Construir la imagen:
   ```bash
   docker build -t tienda-productos-api .
   ```
2. Ejecutarla indicando el puerto de escucha y la base de datos deseada:
   ```bash
   docker run -e DATABASE_URL=postgresql://usuario:clave@host:5432/tienda -p 8000:8000 tienda-productos-api
   ```
3. Llevar la imagen a tu proveedor favorito (Render, Railway, Fly.io, etc.) configurando la variable `DATABASE_URL` y mapeando el puerto 8000.

### Endpoints principales
- `GET /health`: verificación rápida de estado.
- `POST /products`: crea un producto.
- `GET /products`: lista todos los productos.
- `GET /products/{id}`: devuelve el detalle de un producto.
- `PUT /products/{id}`: actualiza campos enviados del producto.
- `DELETE /products/{id}`: elimina el producto.

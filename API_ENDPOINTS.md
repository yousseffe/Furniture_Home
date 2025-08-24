# Furniture Home API Endpoints

## Base URL
`http://localhost:8080`

## Authentication
Currently, all endpoints are open (no authentication required) as per SecurityConfig.

**Note**: The `userId` parameter is currently passed in the URL path. In the future, this will be replaced with JWT token authentication where the user ID will be extracted from the token.

## User Endpoints
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## Product Endpoints
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

## Category Endpoints
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

## Cart Endpoints
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart/{userId}` - Add item to cart (body: productId, quantity)
- `PATCH /api/cart/{userId}` - Update cart item quantity (body: productId, quantity)
- `DELETE /api/cart/{userId}` - Remove item from cart (body: productId)
- `DELETE /api/cart/{userId}/clear` - Clear entire cart

## Favorites Endpoints
- `GET /api/favorites/{userId}` - Get user's favorite products
- `POST /api/favorites/{userId}` - Add product to favorites (body: productId)
- `DELETE /api/favorites/{userId}` - Remove product from favorites (body: productId)
- `GET /api/favorites/{userId}/check?productId={productId}` - Check if product is in favorites

## Database Schema
The application uses MySQL with the following main entities:
- **User**: Basic user information (id, name, email, phone, password, role)
- **Product**: Product details (id, name, description, price, category)
- **Category**: Product categories (id, name, description)
- **ProductImage**: Product images (id, imgUrl, product)
- **Cart**: User shopping cart (id, user, items, timestamps)
- **CartItem**: Individual items in cart (id, cart, product, quantity, timestamps)
- **Favorite**: User's favorite products (id, user, product, timestamp)

## Features
- **Cart Management**: Add, update, remove items, clear cart
- **Favorites**: Add/remove products to/from favorites
- **Product Management**: Full CRUD operations
- **User Management**: Full CRUD operations
- **Category Management**: Full CRUD operations
- **Image Management**: Product images with URLs

## Sample Usage

### Add item to cart:
```
POST /api/cart/1
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

### Update cart item quantity:
```
PATCH /api/cart/1
Content-Type: application/json

{
  "productId": 1,
  "quantity": 3
}
```

### Remove item from cart:
```
DELETE /api/cart/1
Content-Type: application/json

{
  "productId": 1
}
```

### Add product to favorites:
```
POST /api/favorites/1
Content-Type: application/json

{
  "productId": 1
}
```

### Remove product from favorites:
```
DELETE /api/favorites/1
Content-Type: application/json

{
  "productId": 1
}
```

### Get user's cart:
```
GET /api/cart/1
```

### Get user's favorites:
```
GET /api/favorites/1
```

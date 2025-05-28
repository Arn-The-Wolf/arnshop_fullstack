-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_vip BOOLEAN DEFAULT FALSE,
    is_enabled BOOLEAN DEFAULT TRUE
);

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(255),
    featured BOOLEAN DEFAULT FALSE
);

-- Create brands table
CREATE TABLE IF NOT EXISTS brands (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    logo_url VARCHAR(255),
    featured BOOLEAN DEFAULT FALSE
);

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    category_id BIGINT REFERENCES categories(id),
    brand_id BIGINT REFERENCES brands(id),
    featured BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    stock_quantity INTEGER DEFAULT 0,
    rating DECIMAL(3,2) DEFAULT 0.00
);

-- Create reviews table
CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    product_id BIGINT REFERENCES products(id),
    comment TEXT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved BOOLEAN DEFAULT FALSE
); 
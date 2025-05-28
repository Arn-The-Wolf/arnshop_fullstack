-- Insert default admin user
INSERT INTO users (first_name, last_name, email, password, role, is_vip, is_enabled)
VALUES ('Admin', 'User', 'admin@arnshop.com', '$2a$10$rDkPvvAFV6GgJjXpYWxqUOQZx5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5Z5', 'ADMIN', true, true)
ON CONFLICT (email) DO NOTHING;

-- Insert some default categories
INSERT INTO categories (name, description, featured)
VALUES 
    ('Electronics', 'Latest electronic gadgets and devices', true),
    ('Clothing', 'Fashion and apparel for all seasons', true),
    ('Home & Living', 'Everything for your home', false)
ON CONFLICT (name) DO NOTHING;

-- Insert some default brands
INSERT INTO brands (name, description, featured)
VALUES 
    ('Apple', 'Think Different', true),
    ('Nike', 'Just Do It', true),
    ('IKEA', 'Creating a better everyday life', false)
ON CONFLICT (name) DO NOTHING; 
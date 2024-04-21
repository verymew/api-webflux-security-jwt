CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    cpf VARCHAR(14),
    email VARCHAR(255)
);
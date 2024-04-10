CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255),
    senha VARCHAR(255),
    cpf VARCHAR(14),
    email VARCHAR(255)
);
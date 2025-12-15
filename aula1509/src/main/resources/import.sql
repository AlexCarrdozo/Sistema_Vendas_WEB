-- 1. Inserir Pessoas (Clientes)
INSERT INTO tb_pessoa (tipo, email, telefone, nome, cpf) VALUES ('F', 'ana.souza@email.com', '(63)98888-7777', 'Ana Souza', '111.222.333-44');
INSERT INTO tb_pessoa (tipo, email, telefone, razao_social, cnpj) VALUES ('J', 'compras@techcorp.com', '(62)3333-4444', 'TechCorp Soluções S.A.', '11.222.333/0001-55');

-- 2. Inserir os produtos
INSERT INTO produto (descricao, valor) VALUES ('Notebook Gamer', 7500.00);
INSERT INTO produto (descricao, valor) VALUES ('Mouse sem fio', 150.00);
INSERT INTO produto (descricao, valor) VALUES ( 'Celular', 3500.00);
INSERT INTO produto (descricao, valor) VALUES ( 'Mouse-Pad', 50.00);

-- 3. Inserir as vendas
-- Venda 1 para Ana Souza
INSERT INTO venda (data, pessoa_id) VALUES ('2025-10-04', 1);
-- Venda 2 para TechCorp
INSERT INTO venda (data, pessoa_id) VALUES ('2025-10-05', 2);
INSERT INTO venda (data, pessoa_id) VALUES ('2025-10-06', 2);

-- 4. Inserir os itens (AGORA COM O CAMPO valor_unitario)
-- Item: Notebook (7500.00) na Venda 1
INSERT INTO item_venda (quantidade, valor_unitario, produto_id, venda_id) VALUES (1.0, 7500.00, 1, 1);

-- Item: Mouse (150.00) na Venda 1
INSERT INTO item_venda (quantidade, valor_unitario, produto_id, venda_id) VALUES (2.0, 150.00, 2, 1);

-- Item: Celular (3500.00) na Venda 2
INSERT INTO item_venda (quantidade, valor_unitario, produto_id, venda_id) VALUES (2.0, 3500.00, 3, 2);

-- Item: Mouse (150.00) na Venda 3
INSERT INTO item_venda (quantidade, valor_unitario, produto_id, venda_id) VALUES (1.0, 150.00, 2, 3);
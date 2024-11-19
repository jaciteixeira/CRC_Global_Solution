INSERT INTO T_OP_CRC_AUTH (
    ID_AUTH,
    EMAIL,
    HASH_SENHA,
    ROLE
) VALUES (
     1,
     'teste@opengroup.com',
     '$2a$12$MgdYocTnUP3YCZTOzVDl1ekKAr.ozbbObcoojqaCTwFLwpNS0f8we',
     'ADMIN'
 );

INSERT INTO T_OP_CRC_morador (
    ID_morador,
    cpf,
    pontos,
    qtd_moradores,
    status,
    ID_AUTH,
    nome
) VALUES (
     1,
     00000000000,
     0,
     1,
     'ATIVO',
     1,
     'Fulano'
 );

-- Inserir um novo endereço com os novos campos
INSERT INTO T_OP_CRC_ENDERECO (ID_ENDERECO,LOGRADOURO, NUMERO, BAIRRO, CIDADE, UF, CEP)
VALUES (1,'Av Paulista', '3100', 'Centro', 'São Paulo', 'SP', '01310-930');

-- Inserir o condomínio com o ID do endereço obtido
INSERT INTO T_OP_CRC_CONDOMINIO (ID_CONDOMINIO, NOME, ID_ENDERECO)
VALUES (1, 'Condomínio Alfa', (SELECT ID_ENDERECO FROM T_OP_CRC_ENDERECO WHERE LOGRADOURO = 'Av Paulista' AND CEP = '01310-930'));
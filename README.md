# CRC - Condominio Reduzindo Custos

## INTEGRANTES

| INTEGRANTES                    | RM      |
|--------------------------------|---------|
| Cauã Alencar Rojas Romero      | RM98638 | 
| Jaci Teixeira Santos           | RM99627 | 
| Leonardo dos Santos Guerra     | RM99738 | 
| Maria Eduarda Ferreira da Mata | RM99004 | 


## LINK DO REPOSITORIO NO GITHUB
[REPOSITORIO GITHUB](https://github.com/jaciteixeira/CRC_Global_Solution)
## LINK DO VIDEO PITCH
[VIDEO PITCH](https://youtu.be/zMBhPeODCXY?si=T-pDe19KDKk6KyFn)
## LINK DO VIDEO DE DEMOSTRAÇÃO
[VIDEO DE DEMOSTRAÇÃO](https://youtu.be/HLk9cWlZ-tc)

## INSTRUÇÕES PARA RODAR E TESTAR A APLICAÇÃO

### Configuração de ambiente
1. Adicionar a `api key` da OpenIA (fornecida pelo professor), no arquivo [application.yaml](src/main/resources/application.yaml) no campo `api-key:`
   ```
   api-key : ...
   ```
2. Rodar comando para subir RabbitMQ no docker localmente
    ``` bash
    docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
    ``` 

## EMAILS E SENHAS PARA TESTES

### Acesso com permissão:
1. #### USER

   **email**: joao@email.com ou maria@email.com

   **senha**: Senha@123

2. #### ADMIN
   **email**: condominioalfa@opengroup.com
   
3. **senha**: senha

3. #### MANAGER

   **email**: condominioalfa@opengroup.com
   
4. **senha**: senha
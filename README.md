# Rota de Viagem #

Um turista deseja viajar pelo mundo pagando o menor preço possível independentemente do número de conexões necessárias.
Vamos construir um programa que facilite ao nosso turista, escolher a melhor rota para sua viagem.
Para isso precisamos inserir as rotas através de um arquivo de entrada.

## Input Example ##
```csv
GRU,BRC,10
BRC,SCL,5
GRU,CDG,75
GRU,SCL,20
GRU,ORL,56
ORL,CDG,5
SCL,ORL,20
```

## Explicando ## 
Caso desejemos viajar de **GRU** para **CDG** existem as seguintes rotas:

1. GRU - BRC - SCL - ORL - CDG ao custo de **$40**
2. GRU - ORL - CGD ao custo de **$64**
3. GRU - CDG ao custo de **$75**
4. GRU - SCL - ORL - CDG ao custo de **$48**
5. GRU - BRC - CDG ao custo de **$45**

O melhor preço é da rota **4** logo, o output da consulta deve ser **CDG - SCL - ORL - CDG**.

### Como executar a aplicação ###

A aplicação "short-route" foi dividida entre dois módulos:
* short-route-service
* short-route-standalone

Para executar a aplicação será necessário primeiramente executar o comando "mvn clean package" no diretório raiz do projeto "short-route", fazendo com que o projeto seja compilado para execução.

Feito isso, precisamos subir a aplicação de serviço REST, através dos seguintes comandos no diretório raiz do módulo "short-route-service". Note que será passado um argumento, indicando a localição do arquivo de carga inicial das rotas. Conforme exemplo abaixo:

Linha de comando comum:
```shell
mvn spring-boot:run -Dspring-boot.run.arguments="C:\input-file.txt"
```
Power Shell:
```shell
mvn spring-boot:run '-Dspring-boot.run.arguments=C:\input-file.txt'
```

Feito isso, temos agora 2 opções para consulta/criação de novas rotas, através do módulo "standalone" ou através de requisições REST.

##### Requisições REST: #####

Obter rotas atuais cadastradas:

```
GET http://localhost:8080/routes
```

Exemplo:
```
curl -H "Content-Type: application/json" -X GET http://localhost:8080/routes
```

Output:
```json
[
    {"origin":"BRC","destination":"SCL","price":5},
    {"origin":"GRU","destination":"CDG","price":75},
    {"origin":"GRU","destination":"SCL","price":20},
    {"origin":"GRU","destination":"ORL","price":56},
    {"origin":"ORL","destination":"CDG","price":5},
    {"origin":"SCL","destination":"ORL","price":20}
]
```

Cadastrar novas rotas:

```
POST http://localhost:8080/route/{origin}/{destination}/{price}
```

Exemplo:
```
curl -H "Content-Type: application/json" -X POST http://localhost:8080/route/ABC/CDE/10
```

Output:
```json
{
    "origin":"ABC",
    "destination":"CDE",
    "price":10
}
```

Obter rota mais barata:

```
GET http://localhost:8080/route/{origin}/{destination}/
```

Exemplo:
```
curl -H "Content-Type: application/json" -X GET http://localhost:8080/route/GRU/CDG/
```

Output:
```json
{
    "origin":"GRU",
    "destination":"CDG",
    "fullShortRoute":"GRU - SCL - ORL - CDG",
    "priceBetween":45
}
```

##### Requisições através de console: #####

Para execução através da console, é preciso ter o serviço REST em execução, pois toda consulta fará acesso a API através do serviço REST implementado no módulo "short-route-service".

Tendo o serviço REST deployado, basta acessar a aplicação através do comando:

```shell
$ java -jar short-route-standalone-jar-with-dependencies.jar
```

Feito isso, será exibido um menu e retorno de acordo com cada pesquisa, conforme exemplos abaixo.
  
```shell
please enter the route: 
GRU-CGD
best route: GRU - BRC - SCL - ORL - CDG > $40
please enter the route: 
BRC-CDG
best route: BRC - ORL > $30
```

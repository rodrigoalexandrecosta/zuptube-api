# Zup Bootcamp - Segundo Desafio
*Rodrigo Alexandre Costa*\
*rodrigoalexandrecosta@gmail.com*

## Zuptube API
A Zuptube API é o back-end construído para simular o Youtube no segundo desafio do Bootcamp da Zup. Nossa API roda
localmente em `localhost:8080`.

Para instalar o projeto e gerar o .jar:
```
cd zuptube-api
chmod +x ./mvnw
./mvnw clean install
```
Para rodar o projeto (aplicação e banco) através de um Docker container:
```
cd zuptube-api
docker-compose up -d
```
_**Atenção**_: as builds geram dois diretórios na raiz do projeto [ `.sample/video-storage` ], no qual são criados
diretórios únicos para cada canal, cada dir (cada canal) com sua coleção de vídeos. Esses diretórios tendem a crescer
vertiginosamente conforme as builds se acumulam, e o diretório parent `.sample` pode ser apagado sem comprometer builds
subsequentes.

### Features
A Zuptube API conta, no momento, com seis features desenvolvidas: _account_, _channel_, _video_,
_video-engagement_, _subscription_ e _search-engine_.

#### _Account_
A feature de conta de usuário fornece endpoints para criação, edição e resgate de usuários.
Nesse momento, ela está presente aqui para conciliar as informações dos canais e dos usuários, sendo que nenhuma
implementação de autenticação foi feita.

#### _Channel_
A feature dos canais é associada à conta de usuário, de modo que o canal é criado automaticamente a partir da criação
de uma nova, e removido automaticamente a partir da remoção de uma conta.

#### _Video_
A feature de vídeos é um sub-recurso da feature _Channel_ e é responsável por manejar o upload dos vídeos e posterior
armazenamento local no diretório correspondente [ `.sample/video-storage/{channelId}/{videoName}` ]. Existe um problema 
com o endpoint de criação: seria mais efetivo construir um endpoint para upload do video, e outro endpoint para receber 
as informações. No entanto, tanto o `MultipartFile` do video quanto as infos da request estão batendo no mesmo endpoint, 
e por esse motivo a request construída precisa ser consistente, do contrário o controller não a aceitará. No estado 
atual da API, não há solução para esse problema, e portanto a criação de novos vídeos depende de outros passos 
citados abaixo na sessão de Collections do Postman. Ademais, fica aqui registrada a decepção de quem passou a semana 
toda codando e só encontrou problemas graves como esse no domingo a noite.

#### _Video-Engagement_
Feature responsável por intermediar as relações entre usuários e vídeos, como 'like' e comentários. Nossa API possui
um erro crasso nesse ponto: a implementação atual restringe a quantidade de comentários que um usuário pode fazer
em um vídeo à um, o que obviamente não faria sentido numa implementação real. Esse erro ocorreu a partir do design
incorreto da tabela `video_engagement`, que acumula as features de 'like' e comentários, que, na verdade, deveriam
estar separadas em tabelas distintas.

#### _Subscription_
É a feature responsável por fornecer endpoints para inscrição em canais.

#### _Search-Engine_
A feature `search engine` possui apenas um `RestController` e utiliza o serviço de vídeo para acessar o banco. É
responsável por trazer a lista de resultados de buscas por vídeos e de buscas por vídeos em um determinado canal.
A implementação atual da API utiliza busca nos campos `title` e `description` dos videos usando `ILIKE`, o que, em
termos de performance, é longe do ideal, e está inserida aqui apenas no contexto de exemplo e simulação de uma API real.

### _Postman Collections_
Na raiz do projeto encontra-se o arquivo `zuptube-api-collections.json`, que contém a coleção de requests
utilizadas nos endpoints disponível na API. Dados os problemas citados anteriormente, em especial com o
endpoint de upload e criação de vídeos, sugerimos o seguinte fluxo:
1. subir o banco manualmente com o comando `docker run -p 5432:5432 zupbootcamp/zuptube-api-postgres-it`;
2. rodar a aplicação pelo Intellij ou via `docker-compose up -d`;
3. rodar manualmente o teste de criação de novos vídeos, presente no arquivo `VideoServiceIT.groovy`;
4. conectar-se manualmente ao banco para recuperar o `video id` criado;
5. utilizar o endpoint de criação de contas para criar uma nova conta de usuário e recuperar o id retornado no `header`
no campo `Location`;
6. modificar a variável `account-id` presente na Collection, inserindo o `account id` recuperado;
7. usar o endpoint de busca de canais por `account id`, para recuperar o id do canal criado automaticamente;
8. modificar a variável `channel-id` na Collection;
9. daí em diante, os endpoints são auto-explicativos, e em apenas um ou outro caso será necessário fazer requisições
adicionais para encontrar as infos necessárias.

### _Agradecimento_
Agradeço sinceramente a Zup e a banca avaliadora pela oportunidade de passar por esse processo. Sem dúvidas, foi
extremamente enriquecedor e proveitoso. Também agradeço a pessoa que vai ler esse código, pelo tempo, disposição e
atenção. Muito obrigado!
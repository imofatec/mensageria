# IMO - Mensageria

## O que é
[Mensageria](https://en.wikipedia.org/wiki/Messaging_pattern) é uma estrutura usada para trocar mensagens de forma assíncrona entre partes de um sistema. Ela é essencial quando queremos desacoplar partes do sistema e manter tudo funcionando com mais resiliência.

## Message Broker
Um dos componente da mensageria, recebe as mensagens, guarda e entrega quando possível, utiliza o protocolo AMQP
Nessa aplicação estamos utilizando o [RabbitMQ](https://www.rabbitmq.com/) como message broker
![Diagrama mensageria com RabbitMQ](./assets/rabbitmq.png)

## Como funciona o fluxo
Estamos utilizando um fluxo simples de mensageria, com algumas adaptações no producer para se adequar à nossa arquitetura atual.

Como [nossa API](https://github.com/imofatec/imo) segue um modelo **monolítico**, desmontá-la completamente para aplicar uma abordagem baseada em microserviços exigiria um grande esforço. Para contornar essa limitação, adotamos uma adaptação do padrão [Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html).

Essa solução consiste adicionar em uma coleção separada eventos que precisam ser processados. Cada registro contém um payload com os dados necessários e o tipo do evento. A coleção é **escutada** por meio de uma **rotina** pelo nosso producer. Assim, conseguimos garantir a consistência e integridade dos dados, além de manter o sistema desacoplado, mesmo em um contexto monolítico.

Exemplo:

![Diagrama outbox pattern](./assets/outbox-pattern.png)

### Producer
Nosso produtor (producer) escuta uma coleção no banco de dados da [IMO](https://github.com/imofatec/imo) para coletar um documenteo com o tipo do evento e payload que será enviado ao message broker
Exemplo: Evento do usuário se cadastrar, enviamos os dados do usuário para lá

### RabbitMQ
Recebe a mensagem e coloca em uma [exchange](https://www.rabbitmq.com/docs/exchanges) ou fila
Exemplo: A fila "notificacao-email" recebe os dados do usuário para o evento da notificação acontecer depois

### Consumer
Serviço separado que fica ouvindo a sua fila, quando pega a mensagem excuta a lógica que ele é responsável
Exmeplo: Após pegar os dados de um usuário cadastrado, envia um email de confirmação

## Tecnologias utilizadas
- **Linguagem**
    - [Java 21](https://www.java.com/pt-BR/)
- **Inicar aplicação Spring**
    - [Spring Boot](https://spring.io/projects/spring-boot)
- **Message Broker**
    - [Spring AMQP](https://spring.io/projects/spring-amqp)
- **Envio de email**
    - [Java Mail Sender](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-mail)
    - [Thymeleaf Spring](https://docs.spring.io/spring-framework/reference/web/webmvc-view/mvc-thymeleaf.html)
- **Banco de dados**
    - [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)

## Requisitos
> Rodar o projeto na sua máquina
- Java 21
- MongoDB
- Navegador internet

## Setup
Rodar o projeto na sua máquina

### Envs
#### Producer service
> Crie um arquivo em  chamado `.env.properties` `mensageria/producer_service/src/main/resources` e adicione as envs
```
EXCHANGE_NAME=imo.user
ROUTING_KEY=imo.user.confirmation_email
MONGO_URI=mongodb://seu_ip:porta/nome_banco
RABBITMQ_ADDRESS=endereco_rabbitmq

```

#### Confirmation Email Service
> Crie um arquivo em  chamado `.env.properties` `mensageria/confirmation_email_service/src/main/resources` e adicione as envs
```
QUEUE_NAME=imo.user.confirmation_email.notification
EXCHANGE_NAME=imo.user
ROUTING_KEY=imo.user.confirmation_email
MONGO_URI=mongodb://seu_ip:porta/nome_banco
RABBITMQ_ADDRESS=endereco_rabbitmq
EMAIL_HOST=smtp.gmail.com
EMAIL_PORT=587
EMAIL_USERNAME=email_remetente
EMAIL_PASSWORD=senha_de_app
EMAIL_CONFIRMATION_URL=url q redireciona dps q clicar para confirmar email

```

### Run
```
./mvnw spring-boot:run
```

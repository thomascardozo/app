# Desafio Técnico - Gerenciador de Sessões de Votação

Tecnologias utilizadas:
- Java versão 11;
- Spring 2.6.4;
- Postgres 14;
- RabbitMQ;

* Detalhes gerais de configuração do projeto, por favor observar os comentários no arquivo application.properties;
* Para rodar deve-se clonar e importar na IDE de preferência (foi utilizado o IDE InteliJ Idea para o desenvolvimento). A seguir basta dar o start através da classe main, e as tabelas serão geradas automativcamente, desde que o banco de dados ms-associates tenha sido criado no postgres e a configuração de conexão adequadamente realizada.

Operacionalidade:

1) Cadastro de uma nova pauta - exemplo de requisição (passar um objeto PautaDTO no body):

Usar a rota POST -> localhost:8080/pautas/ -> payload exemplo:

        `{
            "assunto": "Compra de uma mesa",
            "isOpen": false,
            "associadosVotantes":[
            {
                "id": 3,
                "name": "Bruce Wayne",
                "cpf": "20540506010"
            },
            {
                "id": 2,
                "name": "Frank Junior",
                "cpf": "69088835071"
            }
            ]
        }`

- Rota para adicionar associado a uma pauta - exemplo:
  Usar a rota POST -> localhost:8080/pautas/addAssociado/{idPauta}

      `{
          "id": 4,
          "name": "Tony Starky",
          "cpf": "33974478019"
      }`
---------------------------------------------------------------------------------------------

2) Abrir sessão de votação em uma pauta (passar um objeto PautaDTO no body):
  Utilizar as rotas POST: localhost:8080/manage-voting/processaSessao/{tempo de duração em segundos} ou localhost:8080/manage-voting/processaSessao 
    
    Observação => É possível abrir a sessão passando ou não o parâmetro de tempo em segundos da sessão/pauta, conforme as chamadas acima exemplificadas.

      `{
          "id": 4,
          "assunto": "Compra de uma mesa",
          "associadosVotantes": [
          {
              "id": 3,
              "name": "Bruce Wayne",
              "cpf": "20540506010"
          },
          {
              "id": 2,
              "name": "Frank Junior",
              "cpf": "69088835071"
          },
          {
              "id": 4,
              "name": "Tony Starky",
              "cpf": "33974478019"
          }
          ]
      }`

------------------------------------------------------------------------------------------------------------------------
3) Receber os votos dos associados, enquanto a pauta/sessão estiver com o campo de status 'isOpen' registrado como true:

  Utilizar a rota POST: localhost:8080/manage-voting/realizaVoto
  
    - Exemplo de payload:
      `{
          "cpf": "33974478019",
          "escolha": "SIM",
          "pautaId": 4
      }`
------------------------------------------------------------------------------------------------------------------------
4) Contabilizar os votos e dar o resultado da votação na pauta (usando mensageria com RabbitMQ via email):
  Rota GET: localhost:8080/manage-voting/resultadoPautaSessao/4
------------------------------------------------------------------------------------------------------------------------
Bônus:

1) Integração com sistema de consulta de cpfs -> Classe services.ValidaCpfService;
2) Mensageria / Filas -> Envio de emails de notificação de resultado da votação com auxilio da ferramenta RabbitMQ;
3) Não realizada;
4) Versionamento utilizando o padrão simples 'v1' na url base, tendo em vista facilitar o desenvolvimento e dispor de simplicidade neste aspecto.

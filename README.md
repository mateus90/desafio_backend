# desafio_backend
  - Para executar o projeto basta iniciar a classe principal da aplicação (DesafioBackendGuiabolsoApplication.java)
  - Seguem os serviços RESTFul disponíveís para serem consumidos:
    POST /books - Espera um JSON com esse formato de exemplo: {"title": "Book title example","description": "Book description example","ISBN": "9781617293290","language": "BR"}
    GET books/{id} - Recebe um id como parâmetro e retorna o registro correspondente nesse formato: {"id": "1234","title": "Book title example","description": "Book description example","ISBN": "9781617293290","language": "BR"}
    GET books - Retorna todos os registro da aplicação nesse formato: {"numberBooks":24,"books"[{"id": "1234","title": "Book title example","description": "Book description example","ISBN": "9781617293290","language": "BR"},{"..."},{"..."}]}

# Como rodar o projeto (Cadastro de Jogadores)

## 1. O que precisa ter instalado
- **JDK 25** (o `pom.xml` pede Java 25). No IntelliJ: `File > Project Structure > SDK > Add SDK > Download JDK` e escolha a versão 25.
- **PostgreSQL + pgAdmin** (você já tem).
- **Maven não precisa instalar**: o projeto já traz o `mvnw` e o IntelliJ baixa tudo sozinho.

## 2. Criar o banco no pgAdmin
1. Abra o pgAdmin e conecte no servidor PostgreSQL.
2. Botão direito em **Databases > Create > Database...**
3. Nome: `cadastro_jogador` > **Save**.

(Ou rode no Query Tool: `CREATE DATABASE cadastro_jogador;`)

A tabela `jogador` **não precisa criar**: o Spring cria sozinho ao iniciar.

## 3. Conferir a senha
Abra `src/main/resources/application.properties` e coloque em
`spring.datasource.password` a senha do seu usuário `postgres`.

## 4. Abrir no IntelliJ
1. `File > Open` e escolha a pasta que contém o `pom.xml` (a pasta `Jogadores-main`).
2. Espere o Maven baixar as dependências (barra de progresso embaixo).
3. Se `getNome()`/`@Getter` ficarem vermelhos: `Settings > Build, Execution, Deployment > Compiler > Annotation Processors > Enable annotation processing`.

## 5. Rodar
- Abra `CadastroJogadoresApplication.java` e clique no ▶ ao lado do `main`.
- Deve aparecer no console: `Started CadastroJogadoresApplication`. A API fica em `http://localhost:8080/jogador`.

## 6. Abrir o frontend
- No IntelliJ, abra `frontend/index.html` e clique no ícone de navegador que aparece no canto superior direito do editor.
  (Isso abre em `localhost:63342`, que já está liberado no CORS.)
- **Não** dê duplo clique no arquivo (`file://`): o navegador bloqueia a comunicação com a API.

## Problemas comuns
| Erro | Causa / solução |
|---|---|
| `database "cadastro_jogador" does not exist` | Faltou criar o banco (passo 2) |
| `password authentication failed` | Senha errada no `application.properties` (passo 3) |
| `Connection refused` | PostgreSQL não está rodando (inicie o serviço) |
| `Port 8080 was already in use` | Outro programa usa a 8080. Feche-o ou adicione `server.port=8081` no properties (e mude `API_URL` no `script.js`) |
| `invalid source release: 25` | O IntelliJ está usando um JDK menor que 25 (passo 1) |
| Frontend mostra "API desconectada" | Backend não está rodando, ou o frontend foi aberto por `file://` |

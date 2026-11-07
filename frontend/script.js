const API_URL = "http://localhost:8080/jogador";

const tabela = document.getElementById("tabelaJogadores");
const contador = document.getElementById("contador");
const mensagem = document.getElementById("mensagem");
const statusApi = document.getElementById("statusApi");
const painelFormulario = document.getElementById("painelFormulario");
const formJogador = document.getElementById("formJogador");

let jogadoresExibidos = [];

function mostrarMensagem(texto, tipo = "sucesso") {
  mensagem.textContent = texto;
  mensagem.className = `mensagem visivel ${tipo}`;
  window.clearTimeout(mostrarMensagem.timer);
  mostrarMensagem.timer = window.setTimeout(() => mensagem.className = "mensagem", 4500);
}

// Troca caracteres especiais (<, >, &...) por códigos seguros.
// Evita que um nome como "<script>" seja executado na página (ataque XSS)
function escaparHtml(valor) {
  return String(valor ?? "").replace(/[&<>"']/g, caractere => ({
    "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#039;"
  })[caractere]);
}

// Função única para falar com a API (GET, POST, PUT, DELETE)
async function requisicao(url, opcoes = {}) {
  // Só avisa que é JSON quando existe corpo (POST/PUT). Em GET/DELETE isso gerava uma requisição extra de verificação do CROSS sem necessidade.
  const headers = opcoes.body ? { "Content-Type": "application/json" } : {};
  const resposta = await fetch(url, {
    ...opcoes,
    headers: { ...headers, ...(opcoes.headers || {}) }
  });

  if (!resposta.ok) {
    // O Spring devolve o erro em JSON ({"message": "..."}); pegamos só o texto da mensagem
    const detalhe = await resposta.text();
    let texto = detalhe;
    try { texto = JSON.parse(detalhe).message || detalhe; } catch { /* não era JSON, usa o texto puro */ }
    throw new Error(texto || `Erro HTTP ${resposta.status}`);
  }

  if (resposta.status === 204) return null;
  // Algumas respostas (POST/PUT/DELETE) vêm vazias, então não dá para chamar .json() direto
  const texto = await resposta.text();
  return texto ? JSON.parse(texto) : null;
}

// Monta as linhas da tabela a partir da lista de jogadores
function desenharTabela(lista) {
  jogadoresExibidos = lista;
  contador.textContent = `${lista.length} jogador(es) encontrado(s)`;
  if (!lista.length) {
    tabela.innerHTML = '<tr><td colspan="6" class="vazio">Nenhum jogador encontrado.</td></tr>';
    return;
  }

  tabela.innerHTML = lista.map(j => `
    <tr>
      <td>${escaparHtml(j.id)}</td>
      <td>${escaparHtml(j.nome)}</td>
      <td>${escaparHtml(j.idade)}</td>
      <td>${escaparHtml(j.nacionalidade)}</td>
      <td>${escaparHtml(j.gols)}</td>
      <td>
        <div class="acoes-linha">
          <button class="botao-menor botao-editar" data-acao="editar" data-id="${escaparHtml(j.id)}">Editar</button>
          <button class="botao-menor botao-excluir" data-acao="excluir" data-id="${escaparHtml(j.id)}">Excluir</button>
        </div>
      </td>
    </tr>
  `).join("");
}

// Busca todos os jogadores na API e mostra na tabela
async function carregarJogadores() {
  statusApi.textContent = "Conectando...";
  tabela.innerHTML = '<tr><td colspan="6" class="vazio">Carregando...</td></tr>';
  try {
    const dados = await requisicao(API_URL);
    if (!Array.isArray(dados)) throw new Error("A API não retornou uma lista de jogadores.");
    desenharTabela(dados);
    statusApi.textContent = "API conectada";
  } catch (erro) {
    tabela.innerHTML = '<tr><td colspan="6" class="vazio">Não foi possível carregar os jogadores.</td></tr>';
    contador.textContent = "Falha ao carregar";
    statusApi.textContent = "API desconectada";
    mostrarMensagem(`Erro ao carregar: ${erro.message}`, "erro");
  }
}

// Abre o formulário. Sem argumento = novo jogador; com jogador = edição (já preenchido)
function abrirFormulario(jogador = null) {
  formJogador.reset();
  document.getElementById("jogadorId").value = jogador?.id ?? "";
  document.getElementById("nome").value = jogador?.nome ?? "";
  document.getElementById("idade").value = jogador?.idade ?? "";
  document.getElementById("nacionalidade").value = jogador?.nacionalidade ?? "";
  document.getElementById("gols").value = jogador?.gols ?? "";
  document.getElementById("tituloFormulario").textContent = jogador ? "Editar jogador" : "Novo jogador";
  document.getElementById("btnSalvar").textContent = jogador ? "Salvar alterações" : "Cadastrar jogador";
  painelFormulario.hidden = false;
  painelFormulario.scrollIntoView({ behavior: "smooth", block: "start" });
}

function fecharFormulario() {
  painelFormulario.hidden = true;
  formJogador.reset();
}

document.getElementById("btnNovo").addEventListener("click", () => abrirFormulario());
document.getElementById("btnCancelar").addEventListener("click", fecharFormulario);
document.getElementById("btnMostrarTodos").addEventListener("click", () => {
  document.getElementById("pesquisaNome").value = "";
  carregarJogadores();
});

// Pesquisa por nome (a busca é exata: precisa digitar o nome completo)
document.getElementById("formPesquisa").addEventListener("submit", async evento => {
  evento.preventDefault();
  const nome = document.getElementById("pesquisaNome").value.trim();
  if (!nome) return carregarJogadores();

  try {
    const jogador = await requisicao(`${API_URL}?nome=${encodeURIComponent(nome)}`);
    desenharTabela(jogador ? [jogador] : []);
  } catch (erro) {
    desenharTabela([]);
    mostrarMensagem(`Jogador não encontrado ou erro na pesquisa: ${erro.message}`, "erro");
  }
});

// Salvar: se o campo escondido "jogadorId" tem valor -> edição (PUT); senão -> cadastro (POST)
formJogador.addEventListener("submit", async evento => {
  evento.preventDefault();
  const id = document.getElementById("jogadorId").value;
  const jogador = {
    nome: document.getElementById("nome").value.trim(),
    idade: Number(document.getElementById("idade").value),
    nacionalidade: document.getElementById("nacionalidade").value.trim(),
    gols: Number(document.getElementById("gols").value)
  };

  if (!jogador.nome || !jogador.nacionalidade ||
      !Number.isInteger(jogador.idade) || jogador.idade < 0 ||
      !Number.isInteger(jogador.gols) || jogador.gols < 0) {
    mostrarMensagem("Confira os campos. Idade e gols devem ser números inteiros não negativos.", "erro");
    return;
  }

  const botao = document.getElementById("btnSalvar");
  botao.disabled = true;
  try {
    if (id) {
      await requisicao(`${API_URL}?id=${encodeURIComponent(id)}`, {
        method: "PUT", body: JSON.stringify(jogador)
      });
      mostrarMensagem("Jogador atualizado com sucesso.");
    } else {
      await requisicao(API_URL, { method: "POST", body: JSON.stringify(jogador) });
      mostrarMensagem("Jogador cadastrado com sucesso.");
    }
    fecharFormulario();
    await carregarJogadores();
  } catch (erro) {
    mostrarMensagem(`Não foi possível salvar: ${erro.message}`, "erro");
  } finally {
    botao.disabled = false;
  }
});

// Um único "ouvinte" na tabela cuida dos botões Editar/Excluir de todas as linhas
// (as linhas são criadas dinamicamente, então não dá para ligar o clique em cada botão antes)
tabela.addEventListener("click", async evento => {
  const botao = evento.target.closest("button[data-acao]");
  if (!botao) return;
  const jogador = jogadoresExibidos.find(j => String(j.id) === botao.dataset.id);
  if (!jogador) return;

  if (botao.dataset.acao === "editar") {
    abrirFormulario(jogador);
    return;
  }

  if (!confirm(`Tem certeza que deseja excluir ${jogador.nome}?`)) return;
  try {
    await requisicao(`${API_URL}?nome=${encodeURIComponent(jogador.nome)}`, { method: "DELETE" });
    mostrarMensagem("Jogador excluído com sucesso.");
    await carregarJogadores();
  } catch (erro) {
    mostrarMensagem(`Não foi possível excluir: ${erro.message}`, "erro");
  }
});

carregarJogadores();

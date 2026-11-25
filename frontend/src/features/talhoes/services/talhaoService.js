import { apiFetch } from '../../../shared/services/api.js';
import { ERRO_DE_REDE, extrairErro } from '../../../shared/services/erros.js';

/**
 * Todas as funções devolvem:
 *   { ok: true, dados }                          em caso de sucesso
 *   { ok: false, status, mensagem, campos }      em caso de erro
 */
async function chamar(url, opcoes) {
  let resposta;
  try {
    resposta = await apiFetch(url, opcoes);
  } catch {
    return { ok: false, ...ERRO_DE_REDE };
  }
  if (!resposta.ok) {
    return { ok: false, ...(await extrairErro(resposta)) };
  }
  // DELETE devolve 204 (sem corpo)
  const dados = resposta.status === 204 ? null : await resposta.json();
  return { ok: true, dados };
}

export function listarTalhoes() {
  return chamar('/talhoes');
}

export function criarTalhao(talhao) {
  return chamar('/talhoes', { method: 'POST', body: JSON.stringify(talhao) });
}

export function atualizarTalhao(id, talhao) {
  return chamar(`/talhoes/${id}`, { method: 'PUT', body: JSON.stringify(talhao) });
}

export function removerTalhao(id) {
  return chamar(`/talhoes/${id}`, { method: 'DELETE' });
}

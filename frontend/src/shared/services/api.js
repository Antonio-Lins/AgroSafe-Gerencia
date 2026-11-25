/** Evento disparado quando a API responde 401 (sessão expirada ou inexistente). */
export const EVENTO_NAO_AUTENTICADO = 'agrosafe:nao-autenticado';

/**
 * Use este fetch nas próximas telas (talhões, defensivos, aplicações...).
 * Ele já envia o cookie de sessão e, se a API responder 401, avisa o AuthContext,
 * que limpa o usuário e as rotas protegidas mandam para o /login.
 *
 * Exemplo: const resposta = await apiFetch('/api/talhoes');
 */
export async function apiFetch(url, opcoes = {}) {
  const resposta = await fetch(url, {
    credentials: 'same-origin',
    ...opcoes,
    headers: { 'Content-Type': 'application/json', ...opcoes.headers },
  });
  if (resposta.status === 401) {
    window.dispatchEvent(new Event(EVENTO_NAO_AUTENTICADO));
  }
  return resposta;
}

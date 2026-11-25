/** Resultado do login: { ok: true, usuario } ou { ok: false, motivo: 'credenciais' | 'rede' | 'erro' } */
export async function login(email, senha) {
  let resposta;
  try {
    resposta = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'same-origin',
      body: JSON.stringify({ email, senha }),
    });
  } catch {
    return { ok: false, motivo: 'rede' };
  }

  if (resposta.ok) {
    return { ok: true, usuario: await resposta.json() };
  }
  if (resposta.status === 401) {
    return { ok: false, motivo: 'credenciais' };
  }
  return { ok: false, motivo: 'erro' };
}

/** Pergunta ao backend quem está logado. Devolve o usuário ou null (sem sessão / sem rede). */
export async function buscarUsuarioLogado() {
  try {
    const resposta = await fetch('/api/auth/me', { credentials: 'same-origin' });
    return resposta.ok ? await resposta.json() : null;
  } catch {
    return null;
  }
}

/** Encerra a sessão no backend. Mesmo que falhe a rede, o front limpa o usuário. */
export async function logout() {
  try {
    await fetch('/api/auth/logout', { method: 'POST', credentials: 'same-origin' });
  } catch {
    // sem rede: segue em frente
  }
}

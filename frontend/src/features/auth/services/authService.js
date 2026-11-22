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

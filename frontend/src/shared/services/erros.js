/**
 * Lê o erro devolvido pela API (formato ProblemDetail do Spring) e devolve:
 * { status, mensagem, campos }
 *  - mensagem: texto pronto para mostrar à pessoa
 *  - campos:   erros por campo, ex.: { codigo: 'O código é obrigatório' } (vem do @Valid)
 */
export async function extrairErro(resposta) {
  let corpo = null;
  try {
    corpo = await resposta.json();
  } catch {
    // resposta sem corpo JSON
  }

  let mensagem = corpo?.detail;
  if (!mensagem) {
    if (resposta.status === 403) mensagem = 'Você não tem permissão para fazer isso.';
    else if (resposta.status === 401) mensagem = 'Sua sessão expirou. Entre novamente.';
    else mensagem = 'Não foi possível concluir a operação. Tente novamente.';
  }

  return { status: resposta.status, mensagem, campos: corpo?.campos ?? {} };
}

export const ERRO_DE_REDE = {
  status: 0,
  mensagem: 'Não foi possível conectar ao servidor. Tente novamente.',
  campos: {},
};

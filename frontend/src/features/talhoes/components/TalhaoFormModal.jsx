import { useState } from 'react';
import Modal from '../../../shared/components/Modal.jsx';

/**
 * Cadastro e edição de talhão.
 * - talhao: o talhão que está sendo editado, ou null para cadastrar um novo
 * - aoSalvar(dados): faz a chamada à API e devolve { ok, mensagem, campos }
 */
export default function TalhaoFormModal({ talhao, aoSalvar, aoFechar }) {
  const [codigo, setCodigo] = useState(talhao?.codigo ?? '');
  const [hectares, setHectares] = useState(talhao ? String(talhao.hectares) : '');
  const [cultura, setCultura] = useState(talhao?.cultura ?? '');
  const [erros, setErros] = useState({});
  const [erroGeral, setErroGeral] = useState('');
  const [salvando, setSalvando] = useState(false);

  // Mesmas regras do TalhaoRequest do backend
  function validar() {
    const novos = {};
    if (!codigo.trim()) novos.codigo = 'O código é obrigatório';
    if (hectares.trim() === '') novos.hectares = 'Os hectares são obrigatórios';
    else if (!(Number(hectares) > 0)) novos.hectares = 'Os hectares devem ser maiores que zero';
    if (!cultura.trim()) novos.cultura = 'A cultura é obrigatória';
    return novos;
  }

  async function enviar(evento) {
    evento.preventDefault();
    const novos = validar();
    setErros(novos);
    setErroGeral('');
    if (Object.keys(novos).length > 0) return;

    setSalvando(true);
    const resultado = await aoSalvar({
      codigo: codigo.trim(),
      hectares: Number(hectares),
      cultura: cultura.trim(),
    });
    setSalvando(false);

    if (!resultado.ok) {
      setErros(resultado.campos ?? {});
      setErroGeral(resultado.mensagem);
    }
  }

  return (
    <Modal titulo={talhao ? 'Editar talhão' : 'Novo talhão'} aoFechar={aoFechar}>
      <form onSubmit={enviar} noValidate>
        <label htmlFor="talhao-codigo">Código</label>
        <input
          id="talhao-codigo"
          type="text"
          autoFocus
          className={erros.codigo ? 'bad' : ''}
          value={codigo}
          onChange={(e) => setCodigo(e.target.value)}
        />
        <div className="err">{erros.codigo}</div>

        <label htmlFor="talhao-hectares">Hectares</label>
        <input
          id="talhao-hectares"
          type="number"
          step="0.01"
          inputMode="decimal"
          className={erros.hectares ? 'bad' : ''}
          value={hectares}
          onChange={(e) => setHectares(e.target.value)}
        />
        <div className="err">{erros.hectares}</div>

        <label htmlFor="talhao-cultura">Cultura</label>
        <input
          id="talhao-cultura"
          type="text"
          className={erros.cultura ? 'bad' : ''}
          value={cultura}
          onChange={(e) => setCultura(e.target.value)}
        />
        <div className="err">{erros.cultura}</div>

        {erroGeral && (
          <div className="form-err" role="alert">
            {erroGeral}
          </div>
        )}

        <div className="modal-acoes">
          <button type="button" className="btn ghost" onClick={aoFechar} disabled={salvando}>
            Cancelar
          </button>
          <button type="submit" className="btn" disabled={salvando}>
            {salvando ? 'Salvando...' : 'Salvar'}
          </button>
        </div>
      </form>
    </Modal>
  );
}

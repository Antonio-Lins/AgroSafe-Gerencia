import { useCallback, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../auth/AuthContext.jsx';
import Aviso from '../../../shared/components/Aviso.jsx';
import TalhaoFormModal from '../components/TalhaoFormModal.jsx';
import ConfirmarExclusaoModal from '../components/ConfirmarExclusaoModal.jsx';
import { atualizarTalhao, criarTalhao, listarTalhoes, removerTalhao } from '../services/talhaoService.js';
import '../../../styles/componentes.css';

const formatarHectares = (n) =>
  Number(n).toLocaleString('pt-BR', { minimumFractionDigits: 1, maximumFractionDigits: 2 });

// Tela de registrar colheita (UI-03, SCRUM-70). Se o caminho dela for outro, ajuste só aqui.
const rotaRegistrarColheita = (talhaoId) => `/colheitas/nova?talhaoId=${talhaoId}`;

// A API manda "2026-11-26"; invertemos o texto para evitar problema de fuso horário.
const formatarData = (iso) => iso.split('-').reverse().join('/');

export default function TalhoesLista() {
  const { ehGestor } = useAuth();
  const navigate = useNavigate();

  const [talhoes, setTalhoes] = useState(null); // null = ainda carregando
  const [erroCarga, setErroCarga] = useState('');
  // formulario: null = fechado | { talhao: null } = novo | { talhao } = editando
  const [formulario, setFormulario] = useState(null);
  const [excluindo, setExcluindo] = useState(null);
  const [aviso, setAviso] = useState(null);

  const mostrarAviso = (texto, erro = false) => setAviso({ id: Date.now(), texto, erro });
  const fecharAviso = useCallback(() => setAviso(null), []);

  const carregar = useCallback(async () => {
    const resultado = await listarTalhoes();
    if (resultado.ok) {
      setTalhoes(resultado.dados);
      setErroCarga('');
    } else {
      setErroCarga(resultado.mensagem);
    }
  }, []);

  useEffect(() => {
    carregar();
  }, [carregar]);

  async function salvar(dados) {
    const editando = formulario?.talhao;
    const resultado = editando ? await atualizarTalhao(editando.id, dados) : await criarTalhao(dados);
    if (resultado.ok) {
      setFormulario(null);
      mostrarAviso('Talhão salvo.');
      await carregar();
    }
    return resultado;
  }

  async function excluir() {
    const resultado = await removerTalhao(excluindo.id);
    setExcluindo(null);
    if (resultado.ok) {
      mostrarAviso('Talhão excluído.');
      await carregar();
    } else {
      // Ex.: talhão com aplicações registradas (o histórico deve ser preservado)
      mostrarAviso(resultado.mensagem, true);
    }
  }

  const total = talhoes?.length ?? 0;
  const hectaresTotais = talhoes?.reduce((soma, t) => soma + Number(t.hectares), 0) ?? 0;
  const emQuarentena = talhoes?.filter((t) => t.estado === 'EM_QUARENTENA').length ?? 0;

  return (
    <section>
      <div className="head">
        <div>
          <h1>Talhões</h1>
          {talhoes && (
            <p>
              {total} {total === 1 ? 'talhão' : 'talhões'} · {formatarHectares(hectaresTotais)} ha no total ·{' '}
              {emQuarentena} em quarentena
            </p>
          )}
        </div>
        {ehGestor && (
          <button type="button" className="btn" onClick={() => setFormulario({ talhao: null })}>
            Novo talhão
          </button>
        )}
      </div>

      {!ehGestor && (
        <div className="ro">Você está como Operador: pode consultar, mas só o Gestor cadastra e edita.</div>
      )}

      {erroCarga && (
        <div className="form-err" role="alert">
          {erroCarga}{' '}
          <button type="button" className="link" onClick={carregar}>
            Tentar de novo
          </button>
        </div>
      )}

      {!talhoes && !erroCarga && (
        <p className="sub" role="status">
          Carregando talhões...
        </p>
      )}

      {talhoes && (
        <div className="card">
          {total === 0 ? (
            <div className="vazio">
              {ehGestor
                ? 'Nenhum talhão cadastrado. Clique em “Novo talhão” para começar.'
                : 'Nenhum talhão cadastrado ainda.'}
            </div>
          ) : (
            <table className="tabela">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Cultura</th>
                  <th className="num">Hectares</th>
                  <th>Estado</th>
                  <th>Colheita</th>
                  {ehGestor && <th></th>}
                </tr>
              </thead>
              <tbody>
                {talhoes.map((t) => {
                  const quarentena = t.estado === 'EM_QUARENTENA';
                  return (
                    <tr key={t.id}>
                      <td data-label="Código">
                        <strong>{t.codigo}</strong>
                      </td>
                      <td data-label="Cultura">{t.cultura}</td>
                      <td data-label="Hectares" className="num">
                        {formatarHectares(t.hectares)}
                      </td>
                      <td data-label="Estado">
                        <span className={quarentena ? 'badge q' : 'badge'}>
                          {quarentena ? 'Em quarentena' : 'Livre'}
                        </span>
                      </td>
                      <td data-label="Colheita">
                        <div className="colher">
                          <button
                            type="button"
                            className="btn sm"
                            disabled={quarentena}
                            aria-describedby={quarentena ? `libera-${t.id}` : undefined}
                            aria-label={`Colher talhão ${t.codigo}`}
                            onClick={() => navigate(rotaRegistrarColheita(t.id))}
                          >
                            Colher
                          </button>
                          {quarentena && t.dataLiberacao && (
                            <span id={`libera-${t.id}`} className="ate">
                              libera em {formatarData(t.dataLiberacao)}
                            </span>
                          )}
                        </div>
                      </td>
                      {ehGestor && (
                        <td className="acoes">
                          <button
                            type="button"
                            className="btn ghost sm"
                            aria-label={`Editar talhão ${t.codigo}`}
                            onClick={() => setFormulario({ talhao: t })}
                          >
                            Editar
                          </button>
                          <button
                            type="button"
                            className="btn ghost sm"
                            aria-label={`Excluir talhão ${t.codigo}`}
                            onClick={() => setExcluindo(t)}
                          >
                            Excluir
                          </button>
                        </td>
                      )}
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      )}

      {formulario && (
        <TalhaoFormModal talhao={formulario.talhao} aoSalvar={salvar} aoFechar={() => setFormulario(null)} />
      )}
      {excluindo && (
        <ConfirmarExclusaoModal talhao={excluindo} aoConfirmar={excluir} aoFechar={() => setExcluindo(null)} />
      )}

      <Aviso aviso={aviso} aoFechar={fecharAviso} />
    </section>
  );
}

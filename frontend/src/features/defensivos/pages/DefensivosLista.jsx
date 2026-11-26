
import { useCallback, useEffect, useState } from 'react';

import { useAuth } from '../../auth/AuthContext.jsx';

import Aviso from '../../../shared/components/Aviso.jsx';

import DefensivoFormModal from '../components/DefensivoFormModal.jsx';
import ConfirmarExclusaoModal from '../components/ConfirmarExclusaoModal.jsx';

import {
    listarDefensivos,
    criarDefensivo,
    atualizarDefensivo,
    removerDefensivo,
} from '../services/defensivoService.js';

import '../../../styles/componentes.css';

export default function DefensivosLista() {
    const { ehGestor } = useAuth();

    const [defensivos, setDefensivos] = useState(null);
    const [carregando, setCarregando] = useState(true);
    const [erroCarga, setErroCarga] = useState('');

    const [formulario, setFormulario] = useState(null);
    const [excluindo, setExcluindo] = useState(null);

    const [aviso, setAviso] = useState(null);

    const mostrarAviso = (texto, erro = false) => {
        setAviso({ id: Date.now(), texto, erro });
    };

    const fecharAviso = useCallback(() => {
        setAviso(null);
    }, []);

    const carregar = useCallback(async () => {
        setCarregando(true);

        const resultado = await listarDefensivos();

        if (resultado.ok) {
            setDefensivos(resultado.dados);
            setErroCarga('');
        } else {
            setErroCarga(resultado.mensagem);
        }

        setCarregando(false);
    }, []);

    useEffect(() => {
        carregar();
    }, [carregar]);

    async function salvar(dados) {
        if (!ehGestor) {
            return {
                ok: false,
                mensagem: 'Você não tem permissão para alterar defensivos.',
                campos: {},
            };
        }

        const editando = formulario?.defensivo;

        const resultado = editando
            ? await atualizarDefensivo(editando.id, dados)
            : await criarDefensivo(dados);

        if (resultado.ok) {
            setFormulario(null);

            mostrarAviso(
                editando
                    ? 'Defensivo atualizado com sucesso.'
                    : 'Defensivo cadastrado com sucesso.'
            );

            await carregar();
        }

        return resultado;
    }

    async function excluir() {
        if (!ehGestor || !excluindo) {
            return;
        }

        const resultado = await removerDefensivo(excluindo.id);

        setExcluindo(null);

        if (resultado.ok) {
            mostrarAviso('Defensivo excluído com sucesso.');
            await carregar();
        } else {
            mostrarAviso(resultado.mensagem, true);
        }
    }

    const total = defensivos?.length ?? 0;

    return (
        <section>
            <div className="head">
                <div>
                    <h1>Defensivos</h1>

                    {defensivos && (
                        <p>
                            {total} {total === 1
                            ? 'defensivo cadastrado'
                            : 'defensivos cadastrados'}
                        </p>
                    )}
                </div>

                {ehGestor && (
                    <button
                        type="button"
                        className="btn"
                        onClick={() => setFormulario({ defensivo: null })}
                    >
                        Novo defensivo
                    </button>
                )}
            </div>

            {!ehGestor && (
                <div className="ro">
                    Você está como Operador: pode consultar os
                    defensivos, mas somente o Gestor pode
                    cadastrar, editar ou excluir.
                </div>
            )}

            {erroCarga && (
                <div className="form-err" role="alert">
                    {erroCarga}{' '}

                    <button
                        type="button"
                        className="link"
                        onClick={carregar}
                    >
                        Tentar novamente
                    </button>
                </div>
            )}

            {carregando && (
                <p className="sub" role="status">
                    Carregando defensivos...
                </p>
            )}

            {!carregando && defensivos && (
                <div className="card">
                    {total === 0 ? (
                        <div className="vazio">
                            {ehGestor
                                ? 'Nenhum defensivo cadastrado. Clique em “Novo defensivo” para começar.'
                                : 'Nenhum defensivo cadastrado.'}
                        </div>
                    ) : (
                        <table className="tabela">
                            <thead>
                            <tr>
                                <th>Nome comercial</th>
                                <th>Princípio ativo</th>
                                <th className="num">Carência</th>
                                {ehGestor && <th>Ações</th>}
                            </tr>
                            </thead>

                            <tbody>
                            {defensivos.map((defensivo) => (
                                <tr key={defensivo.id}>
                                    <td data-label="Nome comercial">
                                        <strong>
                                            {defensivo.nomeComercial}
                                        </strong>
                                    </td>

                                    <td data-label="Princípio ativo">
                                        {defensivo.principioAtivo}
                                    </td>

                                    <td data-label="Carência" className="num">
                                        {defensivo.diasCarencia}{' '}
                                        {defensivo.diasCarencia === 1 ? 'dia' : 'dias'}
                                    </td>

                                    {ehGestor && (
                                        <td className="acoes">
                                            <button
                                                type="button"
                                                className="btn ghost sm"
                                                onClick={() =>
                                                    setFormulario({ defensivo })
                                                }
                                            >
                                                Editar
                                            </button>

                                            <button
                                                type="button"
                                                className="btn ghost sm"
                                                onClick={() => setExcluindo(defensivo)}
                                            >
                                                Excluir
                                            </button>
                                        </td>
                                    )}
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}

            {formulario && (
                <DefensivoFormModal
                    defensivo={formulario.defensivo}
                    aoSalvar={salvar}
                    aoFechar={() => setFormulario(null)}
                />
            )}

            {excluindo && (
                <ConfirmarExclusaoModal
                    defensivo={excluindo}
                    aoConfirmar={excluir}
                    aoFechar={() => setExcluindo(null)}
                />
            )}

            <Aviso
                aviso={aviso}
                aoFechar={fecharAviso}
            />
        </section>
    );
}


import { useCallback, useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';

import { listarTalhoes } from '../../talhoes/services/talhaoService.js';
import { listarDefensivos } from '../../defensivos/services/defensivoService.js';
import { registrarAplicacao } from '../services/aplicacaoService.js';

import '../../../styles/componentes.css';
import '../../../styles/aplicacoes.css';

function dataLocalHoje() {
    const hoje = new Date();

    const ano = hoje.getFullYear();
    const mes = String(hoje.getMonth() + 1).padStart(2, '0');
    const dia = String(hoje.getDate()).padStart(2, '0');

    return `${ano}-${mes}-${dia}`;
}

function formatarData(data) {
    if (!data) return 'Não definida';

    return data.split('-').reverse().join('/');
}

export default function AplicacaoNova() {
    const [searchParams] = useSearchParams();

    const talhaoInicial = searchParams.get('talhaoId') ?? '';

    const [talhoes, setTalhoes] = useState([]);
    const [defensivos, setDefensivos] = useState([]);

    const [talhaoId, setTalhaoId] = useState(talhaoInicial);
    const [defensivoId, setDefensivoId] = useState('');
    const [dataAplicacao, setDataAplicacao] = useState(dataLocalHoje);

    const [carregando, setCarregando] = useState(true);
    const [erroCarga, setErroCarga] = useState('');
    const [erros, setErros] = useState({});
    const [erroGeral, setErroGeral] = useState('');
    const [salvando, setSalvando] = useState(false);
    const [resultadoRegistro, setResultadoRegistro] = useState(null);

    const carregarDados = useCallback(async () => {
        setCarregando(true);
        setErroCarga('');

        const [resultadoTalhoes, resultadoDefensivos] =
            await Promise.all([
                listarTalhoes(),
                listarDefensivos(),
            ]);

        if (!resultadoTalhoes.ok || !resultadoDefensivos.ok) {
            setErroCarga(
                !resultadoTalhoes.ok
                    ? resultadoTalhoes.mensagem
                    : resultadoDefensivos.mensagem
            );

            setCarregando(false);
            return;
        }

        setTalhoes(resultadoTalhoes.dados);
        setDefensivos(resultadoDefensivos.dados);

        setCarregando(false);
    }, []);

    useEffect(() => {
        carregarDados();
    }, [carregarDados]);

    useEffect(() => {
        setTalhaoId(talhaoInicial);
        setResultadoRegistro(null);
    }, [talhaoInicial]);

    function validar() {
        const novosErros = {};

        if (!talhaoId) {
            novosErros.talhaoId = 'Selecione um talhão';
        } else if (!talhoes.some((t) => String(t.id) === talhaoId)) {
            novosErros.talhaoId = 'Selecione um talhão válido';
        }

        if (!defensivoId) {
            novosErros.defensivoId = 'Selecione um defensivo';
        } else if (!defensivos.some((d) => String(d.id) === defensivoId)) {
            novosErros.defensivoId = 'Selecione um defensivo válido';
        }

        if (!dataAplicacao) {
            novosErros.dataAplicacao = 'Informe a data da aplicação';
        } else if (dataAplicacao > dataLocalHoje()) {
            novosErros.dataAplicacao =
                'A data da aplicação não pode ser futura';
        }

        return novosErros;
    }

    async function enviar(evento) {
        evento.preventDefault();

        const novosErros = validar();

        setErros(novosErros);
        setErroGeral('');
        setResultadoRegistro(null);

        if (Object.keys(novosErros).length > 0) {
            return;
        }

        setSalvando(true);

        const resultado = await registrarAplicacao({
            talhaoId: Number(talhaoId),
            defensivoId: Number(defensivoId),
            dataAplicacao,
        });

        setSalvando(false);

        if (!resultado.ok) {
            setErros(resultado.campos ?? {});
            setErroGeral(resultado.mensagem);
            return;
        }

        setResultadoRegistro(resultado.dados);

        // Atualiza os dados para refletir o novo estado do talhão.
        await carregarDados();

        // Preserva o talhão para facilitar novas aplicações.
        setDefensivoId('');
        setDataAplicacao(dataLocalHoje());
    }

    const talhaoSelecionado = talhoes.find(
        (t) => String(t.id) === talhaoId
    );

    const defensivoSelecionado = defensivos.find(
        (d) => String(d.id) === defensivoId
    );

    const semCadastros =
        talhoes.length === 0 || defensivos.length === 0;

    return (
        <section className="pagina-aplicacao">
            <div className="head">
                <div>
                    <h1>Registrar aplicação</h1>
                    <p>
                        Registre a aplicação de um defensivo em um talhão.
                    </p>
                </div>
            </div>

            {erroCarga && (
                <div className="form-err" role="alert">
                    {erroCarga}{' '}

                    <button
                        type="button"
                        className="link"
                        onClick={carregarDados}
                    >
                        Tentar novamente
                    </button>
                </div>
            )}

            {carregando && (
                <p className="sub" role="status">
                    Carregando dados...
                </p>
            )}

            {!carregando && !erroCarga && (
                <>
                    {semCadastros && (
                        <div className="ro">
                            Para registrar uma aplicação, é necessário ter
                            pelo menos um talhão e um defensivo cadastrados.
                        </div>
                    )}

                    <div className="card aplicacao-card">
                        <form onSubmit={enviar} noValidate>
                            <label htmlFor="aplicacao-talhao">
                                Talhão
                            </label>

                            <select
                                id="aplicacao-talhao"
                                value={talhaoId}
                                className={erros.talhaoId ? 'bad' : ''}
                                onChange={(e) => {
                                    setTalhaoId(e.target.value);
                                    setResultadoRegistro(null);
                                }}
                            >
                                <option value="">Selecione um talhão</option>

                                {talhoes.map((talhao) => (
                                    <option key={talhao.id} value={talhao.id}>
                                        {talhao.codigo} — {talhao.cultura}
                                    </option>
                                ))}
                            </select>

                            <div className="err">
                                {erros.talhaoId}
                            </div>

                            {talhaoSelecionado && (
                                <p className="campo-ajuda">
                                    Estado atual:{' '}
                                    {talhaoSelecionado.estado === 'EM_QUARENTENA'
                                        ? 'Em quarentena'
                                        : 'Livre'}
                                </p>
                            )}

                            <label htmlFor="aplicacao-defensivo">
                                Defensivo
                            </label>

                            <select
                                id="aplicacao-defensivo"
                                value={defensivoId}
                                className={erros.defensivoId ? 'bad' : ''}
                                onChange={(e) => {
                                    setDefensivoId(e.target.value);
                                    setResultadoRegistro(null);
                                }}
                            >
                                <option value="">Selecione um defensivo</option>

                                {defensivos.map((defensivo) => (
                                    <option key={defensivo.id} value={defensivo.id}>
                                        {defensivo.nomeComercial}
                                        {' '}({defensivo.diasCarencia} dias)
                                    </option>
                                ))}
                            </select>

                            <div className="err">
                                {erros.defensivoId}
                            </div>

                            {defensivoSelecionado && (
                                <p className="campo-ajuda">
                                    Princípio ativo:{' '}
                                    {defensivoSelecionado.principioAtivo}
                                    {' '}· Carência de{' '}
                                    {defensivoSelecionado.diasCarencia} dias
                                </p>
                            )}

                            <label htmlFor="aplicacao-data">
                                Data da aplicação
                            </label>

                            <input
                                id="aplicacao-data"
                                type="date"
                                max={dataLocalHoje()}
                                value={dataAplicacao}
                                className={erros.dataAplicacao ? 'bad' : ''}
                                onChange={(e) => {
                                    setDataAplicacao(e.target.value);
                                    setResultadoRegistro(null);
                                }}
                            />

                            <div className="err">
                                {erros.dataAplicacao}
                            </div>

                            <p className="campo-ajuda">
                                A aplicação pode ser registrada na data de hoje
                                ou em uma data anterior.
                            </p>

                            {erroGeral && (
                                <div className="form-err" role="alert">
                                    {erroGeral}
                                </div>
                            )}

                            <div className="aplicacao-acoes">
                                <button
                                    type="submit"
                                    className="btn"
                                    disabled={salvando || semCadastros}
                                >
                                    {salvando
                                        ? 'Registrando...'
                                        : 'Registrar aplicação'}
                                </button>
                            </div>
                        </form>
                    </div>

                    {resultadoRegistro && (
                        <div className="aplicacao-resultado" role="status">
                            <h2>Aplicação registrada com sucesso!</h2>

                            <p>
                                <strong>Talhão:</strong>{' '}
                                {resultadoRegistro.talhaoCodigo}
                            </p>

                            <p>
                                <strong>Defensivo:</strong>{' '}
                                {resultadoRegistro.defensivoNome}
                            </p>

                            <p>
                                <strong>Data da aplicação:</strong>{' '}
                                {formatarData(resultadoRegistro.dataAplicacao)}
                            </p>

                            <p>
                                <strong>Liberação desta aplicação:</strong>{' '}
                                {formatarData(
                                    resultadoRegistro.dataLiberacaoCalculada
                                )}
                            </p>

                            <p>
                                <strong>Liberação atual do talhão:</strong>{' '}
                                {formatarData(
                                    resultadoRegistro.dataLiberacaoTalhao
                                )}
                            </p>
                        </div>
                    )}
                </>
            )}
        </section>
    );
}

import { useEffect, useState } from 'react';

import {
    useNavigate,
    useSearchParams,
} from 'react-router-dom';

import {
    listarTalhoes,
} from '../../talhoes/services/talhaoService.js';

import {
    registrarColheita,
} from '../services/colheitaService.js';

import '../../../styles/colheita.css';

export default function RegistrarColheita() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    const [talhoes, setTalhoes] = useState([]);

    const [talhaoId, setTalhaoId] = useState(
        searchParams.get('talhaoId') || ''
    );

    const [dataColheita, setDataColheita] = useState('');
    const [quantidade, setQuantidade] = useState('');

    const [carregando, setCarregando] = useState(true);
    const [enviando, setEnviando] = useState(false);

    const [erroCarga, setErroCarga] = useState('');
    const [erro, setErro] = useState('');
    const [sucesso, setSucesso] = useState('');

    const [errosCampos, setErrosCampos] = useState({});
    const [tentativa, setTentativa] = useState(0);

    useEffect(() => {
        let ativo = true;

        async function carregarTalhoes() {
            setCarregando(true);
            setErroCarga('');

            try {
                const resultado = await listarTalhoes();

                if (!ativo) return;

                if (resultado.ok) {
                    setTalhoes(resultado.dados);

                    setTalhaoId((atual) =>
                        resultado.dados.some(
                            (t) => String(t.id) === atual
                        )
                            ? atual
                            : ''
                    );
                } else {
                    setErroCarga(
                        resultado.mensagem ||
                        'Não foi possível carregar os talhões.'
                    );
                }
            } catch {
                if (ativo) {
                    setErroCarga(
                        'Não foi possível carregar os talhões.'
                    );
                }
            } finally {
                if (ativo) {
                    setCarregando(false);
                }
            }
        }

        carregarTalhoes();

        return () => {
            ativo = false;
        };
    }, [tentativa]);

    const talhaoSelecionado = talhoes.find(
        (talhao) => String(talhao.id) === talhaoId
    );

    const emQuarentena =
        talhaoSelecionado?.estado === 'EM_QUARENTENA';

    function formatarData(data) {
        return data ? data.split('-').reverse().join('/') : '';
    }

    function validar() {
        const campos = {};

        if (!talhaoSelecionado) {
            campos.talhaoId = 'Selecione um talhão.';
        }

        if (!dataColheita) {
            campos.dataColheita =
                'A data da colheita é obrigatória.';
        }

        if (quantidade.trim() === '') {
            campos.quantidade = 'A quantidade é obrigatória.';
        } else if (
            !Number.isFinite(Number(quantidade)) ||
            Number(quantidade) <= 0
        ) {
            campos.quantidade =
                'A quantidade deve ser maior que zero.';
        }

        setErrosCampos(campos);

        return Object.keys(campos).length === 0;
    }

    async function confirmar(evento) {
        evento.preventDefault();

        if (enviando || carregando || sucesso || erroCarga) {
            return;
        }

        setErro('');
        setSucesso('');

        if (!validar()) {
            return;
        }

        setEnviando(true);

        try {
            const resultado = await registrarColheita({
                talhaoId: Number(talhaoId),
                dataColheita,
                quantidade: Number(quantidade),
            });

            if (resultado.ok) {
                setSucesso('Colheita registrada com sucesso!');
                setQuantidade('');
                setDataColheita('');
                setErrosCampos({});
            } else {
                setErro(
                    resultado.mensagem ||
                    'Não foi possível registrar a colheita.'
                );

                setErrosCampos(resultado.campos || {});
            }
        } catch {
            setErro(
                'Não foi possível conectar ao servidor. ' +
                'A colheita não foi confirmada.'
            );
        } finally {
            setEnviando(false);
        }
    }

    return (
        <section className="colheita">
            <header>
                <h1>Registrar colheita</h1>

                <p className="sub">
                    Informe os dados da colheita do talhão.
                </p>
            </header>

            {carregando && (
                <p role="status">
                    Carregando talhões...
                </p>
            )}

            {erroCarga && (
                <div className="form-err" role="alert">
                    <p>{erroCarga}</p>

                    <button
                        type="button"
                        className="btn"
                        onClick={() =>
                            setTentativa((atual) => atual + 1)
                        }
                    >
                        Tentar novamente
                    </button>
                </div>
            )}

            {!carregando && !erroCarga && (
                <form
                    className="colheita-form"
                    onSubmit={confirmar}
                    noValidate
                >
                    <label htmlFor="talhaoId">
                        Talhão
                    </label>

                    <select
                        id="talhaoId"
                        value={talhaoId}
                        onChange={(e) => setTalhaoId(e.target.value)}
                        disabled={enviando || Boolean(sucesso)}
                        required
                        aria-invalid={Boolean(errosCampos.talhaoId)}
                        aria-describedby={
                            errosCampos.talhaoId
                                ? 'erro-talhao'
                                : undefined
                        }
                    >
                        <option value="">
                            Selecione um talhão
                        </option>

                        {talhoes.map((talhao) => (
                            <option
                                key={talhao.id}
                                value={talhao.id}
                            >
                                {talhao.codigo} — {talhao.cultura}
                            </option>
                        ))}
                    </select>

                    {errosCampos.talhaoId && (
                        <p className="err" id="erro-talhao">
                            {errosCampos.talhaoId}
                        </p>
                    )}

                    {talhoes.length === 0 && (
                        <p className="sub">
                            Nenhum talhão cadastrado.
                        </p>
                    )}

                    {emQuarentena && (
                        <div className="colheita-quarentena">
                            <strong>Talhão em quarentena.</strong>

                            {talhaoSelecionado.dataLiberacao && (
                                <p>
                                    Liberação prevista para{' '}
                                    {formatarData(
                                        talhaoSelecionado.dataLiberacao
                                    )}.
                                </p>
                            )}
                        </div>
                    )}

                    <label htmlFor="dataColheita">
                        Data da colheita
                    </label>

                    <input
                        id="dataColheita"
                        type="date"
                        value={dataColheita}
                        onChange={(e) =>
                            setDataColheita(e.target.value)
                        }
                        disabled={enviando || Boolean(sucesso)}
                        required
                        aria-invalid={
                            Boolean(errosCampos.dataColheita)
                        }
                        aria-describedby={
                            errosCampos.dataColheita
                                ? 'erro-data'
                                : undefined
                        }
                    />

                    {errosCampos.dataColheita && (
                        <p className="err" id="erro-data">
                            {errosCampos.dataColheita}
                        </p>
                    )}

                    <label htmlFor="quantidade">
                        Quantidade
                    </label>

                    <input
                        id="quantidade"
                        type="number"
                        inputMode="decimal"
                        step="any"
                        value={quantidade}
                        onChange={(e) =>
                            setQuantidade(e.target.value)
                        }
                        disabled={enviando || Boolean(sucesso)}
                        placeholder="Ex.: 500"
                        required
                        aria-invalid={
                            Boolean(errosCampos.quantidade)
                        }
                        aria-describedby={
                            errosCampos.quantidade
                                ? 'erro-quantidade'
                                : undefined
                        }
                    />

                    {errosCampos.quantidade && (
                        <p className="err" id="erro-quantidade">
                            {errosCampos.quantidade}
                        </p>
                    )}

                    {erro && (
                        <div className="form-err" role="alert">
                            {erro}
                        </div>
                    )}

                    {sucesso && (
                        <div className="form-ok" role="status">
                            {sucesso}
                        </div>
                    )}

                    <div className="colheita-acoes">
                        <button
                            type="submit"
                            className="btn"
                            disabled={
                                enviando ||
                                Boolean(sucesso) ||
                                talhoes.length === 0
                            }
                        >
                            {enviando
                                ? 'Registrando...'
                                : 'Registrar colheita'}
                        </button>

                        <button
                            type="button"
                            className="colheita-voltar"
                            disabled={enviando}
                            onClick={() => navigate('/')}
                        >
                            Voltar ao painel
                        </button>
                    </div>
                </form>
            )}
        </section>
    );
}
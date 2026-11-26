import { Link } from 'react-router-dom';

export default function TalhaoCard({ talhao }) {
    const emQuarentena = talhao.estado === 'EM_QUARENTENA';
    const livre = talhao.estado === 'LIVRE';

    const formatarData = (data) =>
        data.split('-').reverse().join('/');

    const formatarHectares = (hectares) =>
        Number(hectares).toLocaleString('pt-BR', {
            maximumFractionDigits: 2,
        });

    const rotaAplicacao =
        `/aplicacoes/nova?talhaoId=${talhao.id}`;

    const rotaColheita =
        `/colheitas/nova?talhaoId=${talhao.id}`;

    return (
        <article
            className={`painel-talhao ${
                emQuarentena
                    ? 'painel-talhao--quarentena'
                    : 'painel-talhao--livre'
            }`}
        >
            <div className="painel-talhao-cabecalho">
                <h2>Talhão {talhao.codigo}</h2>

                <span className="painel-talhao-estado">
          {emQuarentena ? 'Em quarentena' : 'Livre'}
        </span>
            </div>

            <div className="painel-talhao-informacoes">
                <p>
                    <strong>Cultura:</strong> {talhao.cultura}
                </p>

                <p>
                    <strong>Área:</strong>{' '}
                    {formatarHectares(talhao.hectares)} ha
                </p>

                {emQuarentena && talhao.dataLiberacao && (
                    <p className="painel-talhao-liberacao">
                        <strong>Liberação:</strong>{' '}
                        {formatarData(talhao.dataLiberacao)}
                    </p>
                )}
            </div>

            <div className="painel-talhao-acoes">
                <Link
                    to={rotaAplicacao}
                    className="painel-atalho painel-atalho--aplicacao"
                >
                    Registrar aplicação
                </Link>

                {livre ? (
                    <Link
                        to={rotaColheita}
                        className="painel-atalho painel-atalho--colheita"
                    >
                        Registrar colheita
                    </Link>
                ) : (
                    <button
                        type="button"
                        className="painel-atalho painel-atalho--colheita"
                        disabled
                    >
                        Registrar colheita
                    </button>
                )}
            </div>

            {emQuarentena && talhao.dataLiberacao && (
                <p className="painel-talhao-bloqueio">
                    Colheita bloqueada até{' '}
                    {formatarData(talhao.dataLiberacao)}.
                </p>
            )}
        </article>
    );
}
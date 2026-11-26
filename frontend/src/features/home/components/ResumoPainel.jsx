export default function ResumoPainel({ talhoes = [] }) {
    const total = talhoes.length;

    const livres = talhoes.filter(
        (talhao) => talhao.estado === 'LIVRE'
    ).length;

    const emQuarentena = talhoes.filter(
        (talhao) => talhao.estado === 'EM_QUARENTENA'
    ).length;

    function calcularPercentual(quantidade) {
        if (total === 0) {
            return 0;
        }

        return Math.round((quantidade / total) * 100);
    }

    return (
        <section
            className="painel-resumo"
            aria-label="Resumo dos talhões"
        >
            <article
                className="painel-contador"
                aria-label="Contador total de talhões"
            >
                <h2>Total de talhões</h2>

                <strong className="painel-contador-valor">
                    {total}
                </strong>

                <span>Cadastrados</span>
            </article>

            <article
                className="painel-contador painel-contador--livre"
                aria-label="Contador de talhões livres"
            >
                <h2>Livres</h2>

                <strong className="painel-contador-valor">
                    {livres}
                </strong>

                <span className="painel-contador-percentual">
          {calcularPercentual(livres)}%
        </span>
            </article>

            <article
                className="painel-contador painel-contador--quarentena"
                aria-label="Contador de talhões em quarentena"
            >
                <h2>Em quarentena</h2>

                <strong className="painel-contador-valor">
                    {emQuarentena}
                </strong>

                <span className="painel-contador-percentual">
          {calcularPercentual(emQuarentena)}%
        </span>
            </article>
        </section>
    );
}
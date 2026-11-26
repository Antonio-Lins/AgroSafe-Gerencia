import { useEffect, useState } from 'react';
import { useAuth } from '../../auth/AuthContext.jsx';
import { listarTalhoes } from '../../talhoes/services/talhaoService.js';
import TalhaoCard from '../components/TalhaoCard.jsx';
import '../../../styles/painel.css';

export default function Inicio() {
    const { usuario } = useAuth();

    const [talhoes, setTalhoes] = useState([]);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState('');
    const [tentativa, setTentativa] = useState(0);

    useEffect(() => {
        let ativo = true;

        async function carregarTalhoes() {
            setCarregando(true);
            setErro('');

            try {
                const resultado = await listarTalhoes();

                if (!ativo) return;

                if (resultado.ok) {
                    setTalhoes(resultado.dados);
                } else {
                    setErro(
                        resultado.mensagem ||
                        'Não foi possível carregar os talhões.'
                    );
                }
            } catch {
                if (ativo) {
                    setErro('Não foi possível carregar os talhões.');
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

    return (
        <section className="painel">
            <header className="painel-cabecalho">
                <h1>Painel de talhões</h1>

                <p className="sub">
                    Olá, {usuario.nome}. Acompanhe a situação
                    dos talhões da fazenda.
                </p>
            </header>

            {carregando && (
                <p className="sub" role="status">
                    Carregando talhões...
                </p>
            )}

            {!carregando && erro && (
                <div className="form-err" role="alert">
                    <p>{erro}</p>

                    <button
                        type="button"
                        className="link"
                        onClick={() => setTentativa((atual) => atual + 1)}
                    >
                        Tentar novamente
                    </button>
                </div>
            )}

            {!carregando && !erro && talhoes.length === 0 && (
                <div className="painel-vazio">
                    <p>Nenhum talhão cadastrado.</p>
                </div>
            )}

            {!carregando && !erro && talhoes.length > 0 && (
                <div className="painel-grid">
                    {talhoes.map((talhao) => (
                        <TalhaoCard
                            key={talhao.id}
                            talhao={talhao}
                        />
                    ))}
                </div>
            )}
        </section>
    );
}
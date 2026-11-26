
import { useState } from 'react';
import Modal from '../../../shared/components/Modal.jsx';

export default function DefensivoFormModal({
                                               defensivo,
                                               aoSalvar,
                                               aoFechar,
                                           }) {
    const [nomeComercial, setNomeComercial] = useState(
        defensivo?.nomeComercial ?? ''
    );

    const [principioAtivo, setPrincipioAtivo] = useState(
        defensivo?.principioAtivo ?? ''
    );

    const [diasCarencia, setDiasCarencia] = useState(
        defensivo ? String(defensivo.diasCarencia) : ''
    );

    const [erros, setErros] = useState({});
    const [erroGeral, setErroGeral] = useState('');
    const [salvando, setSalvando] = useState(false);

    function validar() {
        const novosErros = {};

        if (!nomeComercial.trim()) {
            novosErros.nomeComercial =
                'O nome comercial é obrigatório';
        }

        if (!principioAtivo.trim()) {
            novosErros.principioAtivo =
                'O princípio ativo é obrigatório';
        }

        if (!diasCarencia.trim()) {
            novosErros.diasCarencia =
                'O período de carência é obrigatório';
        } else if (
            !Number.isInteger(Number(diasCarencia)) ||
            Number(diasCarencia) <= 0
        ) {
            novosErros.diasCarencia =
                'A carência deve ser um inteiro maior que zero';
        }

        return novosErros;
    }

    async function enviar(evento) {
        evento.preventDefault();

        const novosErros = validar();
        setErros(novosErros);
        setErroGeral('');

        if (Object.keys(novosErros).length > 0) {
            return;
        }

        const dados = {
            nomeComercial: nomeComercial.trim(),
            principioAtivo: principioAtivo.trim(),
            diasCarencia: Number(diasCarencia),
        };

        setSalvando(true);

        try {
            const resultado = await aoSalvar(dados);

            if (!resultado.ok) {
                setErros(resultado.campos ?? {});
                setErroGeral(resultado.mensagem);
            }
        } catch {
            setErroGeral('Não foi possível salvar o defensivo.');
        } finally {
            setSalvando(false);
        }
    }

    return (
        <Modal
            titulo={defensivo ? 'Editar defensivo' : 'Novo defensivo'}
            aoFechar={aoFechar}
        >
            <form onSubmit={enviar} noValidate>
                <label htmlFor="defensivo-nome">
                    Nome comercial
                </label>

                <input
                    id="defensivo-nome"
                    type="text"
                    autoFocus
                    maxLength={150}
                    value={nomeComercial}
                    className={erros.nomeComercial ? 'bad' : ''}
                    onChange={(e) => setNomeComercial(e.target.value)}
                />

                <div className="err">
                    {erros.nomeComercial}
                </div>

                <label htmlFor="defensivo-principio">
                    Princípio ativo
                </label>

                <input
                    id="defensivo-principio"
                    type="text"
                    maxLength={150}
                    value={principioAtivo}
                    className={erros.principioAtivo ? 'bad' : ''}
                    onChange={(e) => setPrincipioAtivo(e.target.value)}
                />

                <div className="err">
                    {erros.principioAtivo}
                </div>

                <label htmlFor="defensivo-carencia">
                    Período de carência (dias)
                </label>

                <input
                    id="defensivo-carencia"
                    type="number"
                    min="1"
                    step="1"
                    value={diasCarencia}
                    className={erros.diasCarencia ? 'bad' : ''}
                    onChange={(e) => setDiasCarencia(e.target.value)}
                />

                <div className="err">
                    {erros.diasCarencia}
                </div>

                {erroGeral && (
                    <div className="form-err" role="alert">
                        {erroGeral}
                    </div>
                )}

                <div className="modal-acoes">
                    <button
                        type="button"
                        className="btn ghost"
                        onClick={aoFechar}
                        disabled={salvando}
                    >
                        Cancelar
                    </button>

                    <button
                        type="submit"
                        className="btn"
                        disabled={salvando}
                    >
                        {salvando ? 'Salvando...' : 'Salvar'}
                    </button>
                </div>
            </form>
        </Modal>
    );
}

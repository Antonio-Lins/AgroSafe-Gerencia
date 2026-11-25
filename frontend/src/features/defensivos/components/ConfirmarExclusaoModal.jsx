
import { useState } from 'react';
import Modal from '../../../shared/components/Modal.jsx';

export default function ConfirmarExclusaoModal({
                                                   defensivo,
                                                   aoConfirmar,
                                                   aoFechar,
                                               }) {
    const [excluindo, setExcluindo] = useState(false);

    async function confirmar() {
        setExcluindo(true);

        try {
            await aoConfirmar();
        } finally {
            setExcluindo(false);
        }
    }

    return (
        <Modal
            titulo="Excluir defensivo?"
            aoFechar={aoFechar}
        >
            <p className="sub modal-texto">
                O defensivo <strong>{defensivo.nomeComercial}</strong>
                {' '}será removido do cadastro.
            </p>

            <p className="sub modal-texto">
                Defensivos associados a aplicações registradas
                não podem ser excluídos.
            </p>

            <div className="modal-acoes">
                <button
                    type="button"
                    className="btn ghost"
                    onClick={aoFechar}
                    disabled={excluindo}
                >
                    Cancelar
                </button>

                <button
                    type="button"
                    className="btn danger"
                    onClick={confirmar}
                    disabled={excluindo}
                >
                    {excluindo ? 'Excluindo...' : 'Excluir'}
                </button>
            </div>
        </Modal>
    );
}

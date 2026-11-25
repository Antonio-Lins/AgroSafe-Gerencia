import { useState } from 'react';
import Modal from '../../../shared/components/Modal.jsx';

/** Pergunta antes de excluir. aoConfirmar() faz a chamada à API e fecha a janela. */
export default function ConfirmarExclusaoModal({ talhao, aoConfirmar, aoFechar }) {
  const [excluindo, setExcluindo] = useState(false);

  async function confirmar() {
    setExcluindo(true);
    await aoConfirmar();
    setExcluindo(false);
  }

  return (
    <Modal titulo="Excluir talhão?" aoFechar={aoFechar}>
      <p className="sub modal-texto">“{talhao.codigo}” será removido da lista.</p>
      <div className="modal-acoes">
        <button type="button" className="btn ghost" onClick={aoFechar} disabled={excluindo}>
          Cancelar
        </button>
        <button type="button" className="btn danger" onClick={confirmar} disabled={excluindo}>
          {excluindo ? 'Excluindo...' : 'Excluir'}
        </button>
      </div>
    </Modal>
  );
}

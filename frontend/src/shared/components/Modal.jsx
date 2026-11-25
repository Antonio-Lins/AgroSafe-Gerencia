import { useEffect, useRef } from 'react';

/**
 * Janela modal usando o <dialog> nativo (já trata foco, Esc e fundo escurecido).
 * Renderize só quando for abrir: {aberto && <Modal ...>}
 */
export default function Modal({ titulo, aoFechar, children }) {
  const ref = useRef(null);

  useEffect(() => {
    const dialogo = ref.current;
    if (dialogo && !dialogo.open) dialogo.showModal();
  }, []);

  return (
    <dialog ref={ref} className="modal" onClose={aoFechar} aria-labelledby="modal-titulo">
      <h2 id="modal-titulo">{titulo}</h2>
      {children}
    </dialog>
  );
}

import { useEffect } from 'react';

/**
 * Mensagem rápida no canto da tela (some sozinha em 5 segundos).
 * aviso = { id, texto, erro } ou null. Troque o "id" a cada novo aviso.
 */
export default function Aviso({ aviso, aoFechar }) {
  useEffect(() => {
    if (!aviso) return undefined;
    const tempo = setTimeout(aoFechar, 5000);
    return () => clearTimeout(tempo);
  }, [aviso, aoFechar]);

  return (
    <div className="avisos" aria-live="polite">
      {aviso && (
        <div key={aviso.id} className={aviso.erro ? 'aviso aviso-erro' : 'aviso'} role={aviso.erro ? 'alert' : 'status'}>
          {aviso.texto}
        </div>
      )}
    </div>
  );
}

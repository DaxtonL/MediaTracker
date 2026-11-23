import { useState } from 'react'
import Modal from './Modal.jsx'

function ConfirmModal({ title, message, confirmLabel = 'Delete', onConfirm, onClose }) {
  const [error, setError] = useState(null)
  const [busy, setBusy] = useState(false)

  async function handleConfirm() {
    setBusy(true)
    try {
      await onConfirm()
    } catch (e) {
      setError(e.message)
      setBusy(false)
    }
  }

  return (
    <Modal title={title} onClose={onClose}>
      <p>{message}</p>
      {error && <p className="form__error">{error}</p>}
      <footer className="form__actions">
        <button type="button" className="btn btn--ghost" onClick={onClose}>
          Cancel
        </button>
        <button type="button" className="btn btn--danger" onClick={handleConfirm} disabled={busy}>
          {confirmLabel}
        </button>
      </footer>
    </Modal>
  )
}

export default ConfirmModal

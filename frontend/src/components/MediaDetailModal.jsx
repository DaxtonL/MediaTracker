import { useCallback, useEffect, useState } from 'react'
import Modal from './Modal.jsx'
import { ProgressBar, Rating, StatusBadge, TypeIcon } from './MediaBits.jsx'
import { api } from '../api.js'
import { formatDate, todayIso, typeInfo } from '../media.js'

// Viewing history + logging, like the Swing LogViewingPanel:
// "New Log" (date + amount) and "Delete last log".
function MediaDetailModal({ mediaId, onChanged, onEdit, onClose }) {
  const [media, setMedia] = useState(null)
  const [error, setError] = useState(null)
  const [viewedOn, setViewedOn] = useState(todayIso())
  const [amount, setAmount] = useState('')
  const [busy, setBusy] = useState(false)

  const load = useCallback(
    () =>
      api
        .getMedia(mediaId)
        .then(setMedia)
        .catch((e) => setError(e.message)),
    [mediaId],
  )

  useEffect(() => {
    load()
  }, [load])

  async function run(action) {
    setBusy(true)
    setError(null)
    try {
      await action()
      await load()
      onChanged()
      return true
    } catch (e) {
      setError(e.message)
      return false
    } finally {
      setBusy(false)
    }
  }

  async function handleLog(e) {
    e.preventDefault()
    const n = Number(amount)
    if (!Number.isInteger(n) || n < 1) {
      setError('Amount must be a whole number of at least 1')
      return
    }
    if (await run(() => api.logViewing(mediaId, viewedOn, n))) setAmount('')
  }

  if (!media) {
    return (
      <Modal title="Loading…" onClose={onClose}>
        {error ? <p className="form__error">{error}</p> : <div className="spinner" />}
      </Modal>
    )
  }

  const info = typeInfo(media.type)
  const logs = [...media.logs].reverse() // newest first

  return (
    <Modal title={media.name} onClose={onClose} wide>
      <div className="detail">
        <div className="detail__summary">
          <TypeIcon type={media.type} />
          <div className="detail__meta">
            <div className="detail__tags">
              <span className="muted">{info.label}</span>
              <StatusBadge media={media} />
              {media.priority != null && <span className="chip">Priority {media.priority}</span>}
              <Rating value={media.rating} />
            </div>
            <ProgressBar media={media} />
          </div>
          <button type="button" className="btn btn--ghost" onClick={() => onEdit(media)}>
            Edit
          </button>
        </div>

        <form className="log-form" onSubmit={handleLog}>
          <h3>Log progress</h3>
          <div className="log-form__row">
            <label className="field">
              <span>Date</span>
              <input
                type="date"
                value={viewedOn}
                max={todayIso()}
                onChange={(e) => setViewedOn(e.target.value)}
                required
              />
            </label>
            <label className="field">
              <span>Amount ({media.unit})</span>
              <input
                type="number"
                min="1"
                step="1"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                placeholder="e.g. 3"
                required
              />
            </label>
            <button type="submit" className="btn btn--primary" disabled={busy}>
              + Log
            </button>
          </div>
          {error && <p className="form__error">{error}</p>}
        </form>

        <section className="history">
          <header className="history__header">
            <h3>History</h3>
            <button
              type="button"
              className="btn btn--danger-ghost btn--sm"
              disabled={busy || logs.length === 0}
              onClick={() => run(() => api.removeLatestLog(mediaId))}
            >
              Undo latest log
            </button>
          </header>
          {logs.length === 0 ? (
            <p className="empty-inline">No viewings logged yet.</p>
          ) : (
            <ol className="history__list">
              {logs.map((log, i) => (
                <li key={log.id} className={i === 0 ? 'latest' : ''}>
                  <span className="history__date">{formatDate(log.viewedOn)}</span>
                  <span className="history__amount">
                    +{log.viewProgress} {media.unit}
                  </span>
                </li>
              ))}
            </ol>
          )}
        </section>
      </div>
    </Modal>
  )
}

export default MediaDetailModal

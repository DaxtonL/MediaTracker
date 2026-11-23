import { useState } from 'react'
import Modal from './Modal.jsx'
import { MEDIA_TYPES, STATUSES, statusLabel } from '../media.js'

const toField = (n) => (n == null ? '' : String(n))

// Add mode: name, type, length, priority (like AddMediaPanel).
// Edit mode: additionally status and rating (like EditMediaPanel).
function MediaFormModal({ media, onSubmit, onClose }) {
  const editing = media != null
  const [form, setForm] = useState({
    name: media?.name ?? '',
    type: media?.type ?? 'MOVIE',
    length: toField(media?.length),
    priority: toField(media?.priority),
    status: media?.status ?? 'WAITLIST',
    rating: toField(media?.rating),
  })
  const [error, setError] = useState(null)
  const [saving, setSaving] = useState(false)

  const set = (key) => (e) => setForm((f) => ({ ...f, [key]: e.target.value }))
  const unit = MEDIA_TYPES[form.type].unit

  function parsePositive(value, label) {
    if (value === '') return null
    const n = Number(value)
    if (!Number.isInteger(n) || n < 1) throw new Error(`${label} must be a whole number of at least 1`)
    return n
  }

  function buildPayload() {
    const name = form.name.trim()
    if (!name) throw new Error('Name is required')
    const length = parsePositive(form.length, 'Length')
    const priority = parsePositive(form.priority, 'Priority')

    if (!editing) return { name, type: form.type, length, priority }

    const rating = form.rating === '' ? null : Number(form.rating)
    // The PATCH endpoint treats null as "leave unchanged", so a value can't be cleared.
    for (const [key, value] of [['length', length], ['priority', priority], ['rating', rating]]) {
      if (value == null && media[key] != null) {
        throw new Error(`${key[0].toUpperCase() + key.slice(1)} can't be cleared once set`)
      }
    }

    const changes = {}
    if (name !== media.name) changes.name = name
    if (form.type !== media.type) changes.type = form.type
    if (form.status !== media.status) changes.status = form.status
    if (length !== media.length) changes.length = length
    if (priority !== media.priority) changes.priority = priority
    if (rating !== media.rating) changes.rating = rating
    return changes
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError(null)
    let payload
    try {
      payload = buildPayload()
    } catch (err) {
      setError(err.message)
      return
    }
    if (editing && Object.keys(payload).length === 0) {
      onClose()
      return
    }
    setSaving(true)
    try {
      await onSubmit(payload)
    } catch (err) {
      setError(err.message)
      setSaving(false)
    }
  }

  return (
    <Modal title={editing ? `Edit “${media.name}”` : 'Add media'} onClose={onClose}>
      <form className="form" onSubmit={handleSubmit}>
        <label className="field">
          <span>Name</span>
          <input value={form.name} onChange={set('name')} autoFocus required />
        </label>

        <div className="field">
          <span>Type</span>
          <div className="segmented">
            {Object.entries(MEDIA_TYPES).map(([key, info]) => (
              <button
                key={key}
                type="button"
                className={form.type === key ? 'active' : ''}
                onClick={() => setForm((f) => ({ ...f, type: key }))}
              >
                <span aria-hidden>{info.icon}</span> {info.label}
              </button>
            ))}
          </div>
        </div>

        <div className="field-row">
          <label className="field">
            <span>Length <em className="muted">({unit}, optional)</em></span>
            <input type="number" min="1" step="1" value={form.length} onChange={set('length')} placeholder="Unknown" />
          </label>
          <label className="field">
            <span>Priority <em className="muted">(optional)</em></span>
            <input type="number" min="1" step="1" value={form.priority} onChange={set('priority')} placeholder="None" />
          </label>
        </div>

        {editing && (
          <div className="field-row">
            <label className="field">
              <span>Status</span>
              <select value={form.status} onChange={set('status')}>
                {STATUSES.map((s) => (
                  <option key={s} value={s}>
                    {s === 'VIEWING' ? MEDIA_TYPES[form.type].verb : statusLabel(s)}
                  </option>
                ))}
              </select>
            </label>
            <label className="field">
              <span>Rating</span>
              <select value={form.rating} onChange={set('rating')}>
                <option value="">Unrated</option>
                {Array.from({ length: 11 }, (_, i) => 10 - i).map((n) => (
                  <option key={n} value={n}>
                    {n} / 10
                  </option>
                ))}
              </select>
            </label>
          </div>
        )}

        {error && <p className="form__error">{error}</p>}

        <footer className="form__actions">
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cancel
          </button>
          <button type="submit" className="btn btn--primary" disabled={saving}>
            {saving ? 'Saving…' : editing ? 'Save changes' : 'Add media'}
          </button>
        </footer>
      </form>
    </Modal>
  )
}

export default MediaFormModal

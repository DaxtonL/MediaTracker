import { useCallback, useEffect, useMemo, useState } from 'react'
import { Link, useParams } from 'react-router'
import { api } from '../api.js'
import { MEDIA_TYPES, STATUSES, statusLabel, todayIso, typeInfo } from '../media.js'
import { ProgressBar, Rating, StatusBadge, TypeIcon } from '../components/MediaBits.jsx'
import MediaFormModal from '../components/MediaFormModal.jsx'
import MediaDetailModal from '../components/MediaDetailModal.jsx'
import ConfirmModal from '../components/ConfirmModal.jsx'

const EMPTY_FILTERS = { status: '', type: '', minRating: '', maxRating: '' }

const nullsLast = (a, b, dir = 1) => {
  if (a == null && b == null) return 0
  if (a == null) return 1
  if (b == null) return -1
  return (a - b) * dir
}

const SORTS = {
  added: { label: 'Date added', fn: (a, b) => a.id - b.id },
  name: { label: 'Name', fn: (a, b) => a.name.localeCompare(b.name) },
  priority: { label: 'Priority', fn: (a, b) => nullsLast(a.priority, b.priority) },
  rating: { label: 'Rating', fn: (a, b) => nullsLast(a.rating, b.rating, -1) },
  progress: { label: 'Progress', fn: (a, b) => b.totalProgress - a.totalProgress },
}

function TrackerPage() {
  const { id } = useParams()
  const [tracker, setTracker] = useState(null)
  const [trackerError, setTrackerError] = useState(null)
  const [media, setMedia] = useState(null)
  const [listError, setListError] = useState(null)
  const [filters, setFilters] = useState(EMPTY_FILTERS)
  const [search, setSearch] = useState('')
  const [sort, setSort] = useState('added')
  // { kind: 'add' } | { kind: 'edit', media, back? } | { kind: 'detail', id } | { kind: 'delete', media }
  const [modal, setModal] = useState(null)

  const ratingRangeInvalid =
    filters.minRating !== '' &&
    filters.maxRating !== '' &&
    Number(filters.minRating) > Number(filters.maxRating)

  const refresh = useCallback(() => {
    api.getTracker(id).then(setTracker).catch((e) => setTrackerError(e.message))
    if (ratingRangeInvalid) return
    api
      .listMedia(id, filters)
      .then((list) => {
        setMedia(list)
        setListError(null)
      })
      .catch((e) => setListError(e.message))
  }, [id, filters, ratingRangeInvalid])

  useEffect(() => {
    refresh()
  }, [refresh])

  const visible = useMemo(() => {
    if (!media) return []
    const q = search.trim().toLowerCase()
    return media.filter((m) => !q || m.name.toLowerCase().includes(q)).sort(SORTS[sort].fn)
  }, [media, search, sort])

  const setFilter = (key, value) => setFilters((f) => ({ ...f, [key]: value }))
  const filtersActive = Object.values(filters).some((v) => v !== '') || search !== ''

  async function quickLog(m) {
    try {
      await api.logViewing(m.id, todayIso(), 1)
      refresh()
    } catch (e) {
      setListError(e.message)
    }
  }

  if (trackerError) {
    return (
      <main className="page">
        <Link to="/" className="back-link">← All trackers</Link>
        <p className="banner banner--error">{trackerError}</p>
      </main>
    )
  }
  if (!tracker) {
    return (
      <main className="page">
        <div className="spinner" />
      </main>
    )
  }

  return (
    <main className="page">
      <Link to="/" className="back-link">← All trackers</Link>

      <header className="page-header">
        <div>
          <h1>{tracker.name}</h1>
          <p className="muted">
            {tracker.mediaCount} {tracker.mediaCount === 1 ? 'item' : 'items'} tracked
          </p>
        </div>
        <button type="button" className="btn btn--primary btn--lg" onClick={() => setModal({ kind: 'add' })}>
          + Add media
        </button>
      </header>

      <section className="toolbar">
        <div className="tabs" role="tablist" aria-label="Filter by status">
          {['', ...STATUSES].map((s) => (
            <button
              key={s || 'all'}
              type="button"
              role="tab"
              aria-selected={filters.status === s}
              className={filters.status === s ? 'active' : ''}
              onClick={() => setFilter('status', s)}
            >
              {s ? statusLabel(s) : 'All'}
            </button>
          ))}
        </div>

        <div className="toolbar__row">
          <div className="chips" aria-label="Filter by type">
            {Object.entries(MEDIA_TYPES).map(([key, info]) => (
              <button
                key={key}
                type="button"
                className={`chip chip--toggle${filters.type === key ? ' active' : ''}`}
                aria-pressed={filters.type === key}
                onClick={() => setFilter('type', filters.type === key ? '' : key)}
              >
                <span aria-hidden>{info.icon}</span> {info.label}
              </button>
            ))}
          </div>

          <div className="toolbar__controls">
            <input
              type="search"
              className="search"
              placeholder="Search…"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              aria-label="Search by name"
            />
            <label className="inline-field">
              <span>Rating</span>
              <select value={filters.minRating} onChange={(e) => setFilter('minRating', e.target.value)} aria-label="Minimum rating">
                <option value="">min</option>
                {Array.from({ length: 11 }, (_, i) => (
                  <option key={i} value={i}>≥ {i}</option>
                ))}
              </select>
              <select value={filters.maxRating} onChange={(e) => setFilter('maxRating', e.target.value)} aria-label="Maximum rating">
                <option value="">max</option>
                {Array.from({ length: 11 }, (_, i) => (
                  <option key={i} value={i}>≤ {i}</option>
                ))}
              </select>
            </label>
            <label className="inline-field">
              <span>Sort</span>
              <select value={sort} onChange={(e) => setSort(e.target.value)}>
                {Object.entries(SORTS).map(([key, s]) => (
                  <option key={key} value={key}>{s.label}</option>
                ))}
              </select>
            </label>
            {filtersActive && (
              <button
                type="button"
                className="btn btn--ghost btn--sm"
                onClick={() => {
                  setFilters(EMPTY_FILTERS)
                  setSearch('')
                }}
              >
                Clear
              </button>
            )}
          </div>
        </div>
      </section>

      {ratingRangeInvalid && (
        <p className="banner banner--error">Minimum rating can’t be greater than maximum rating.</p>
      )}
      {listError && <p className="banner banner--error">{listError}</p>}

      {!media ? (
        <div className="spinner" />
      ) : visible.length === 0 ? (
        <div className="empty">
          {tracker.mediaCount === 0 ? (
            <>
              <p className="empty__title">Nothing tracked yet</p>
              <p className="muted">Add a movie, show, game, book or manga to get started.</p>
              <button type="button" className="btn btn--primary" onClick={() => setModal({ kind: 'add' })}>
                + Add your first item
              </button>
            </>
          ) : (
            <>
              <p className="empty__title">No matches</p>
              <p className="muted">Try a different filter.</p>
            </>
          )}
        </div>
      ) : (
        <ul className="media-list">
          {visible.map((m) => (
            <li key={m.id} className={`media-card media-card--${m.status.toLowerCase()}`}>
              <button
                type="button"
                className="media-card__main"
                onClick={() => setModal({ kind: 'detail', id: m.id })}
                title="View history & log progress"
              >
                <TypeIcon type={m.type} />
                <div className="media-card__body">
                  <div className="media-card__title">
                    <span className="media-card__name">{m.name}</span>
                    <StatusBadge media={m} />
                  </div>
                  <div className="media-card__meta">
                    <span>{typeInfo(m.type).label}</span>
                    {m.priority != null && <span className="chip chip--sm">Priority {m.priority}</span>}
                    <Rating value={m.rating} />
                  </div>
                </div>
                <div className="media-card__progress">
                  <ProgressBar media={m} />
                </div>
              </button>
              <div className="media-card__actions">
                <button
                  type="button"
                  className="btn btn--soft btn--sm"
                  onClick={() => quickLog(m)}
                  title={`Log 1 ${m.unit} today`}
                >
                  +1
                </button>
                <button
                  type="button"
                  className="icon-btn"
                  onClick={() => setModal({ kind: 'edit', media: m })}
                  aria-label={`Edit ${m.name}`}
                  title="Edit"
                >
                  ✎
                </button>
                <button
                  type="button"
                  className="icon-btn icon-btn--danger"
                  onClick={() => setModal({ kind: 'delete', media: m })}
                  aria-label={`Delete ${m.name}`}
                  title="Delete"
                >
                  🗑
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}

      {modal?.kind === 'add' && (
        <MediaFormModal
          onClose={() => setModal(null)}
          onSubmit={async (payload) => {
            await api.createMedia(id, payload)
            setModal(null)
            refresh()
          }}
        />
      )}

      {modal?.kind === 'edit' && (
        <MediaFormModal
          media={modal.media}
          onClose={() => setModal(modal.back ?? null)}
          onSubmit={async (changes) => {
            await api.updateMedia(modal.media.id, changes)
            setModal(modal.back ?? null)
            refresh()
          }}
        />
      )}

      {modal?.kind === 'detail' && (
        <MediaDetailModal
          key={modal.id}
          mediaId={modal.id}
          onChanged={refresh}
          onEdit={(m) => setModal({ kind: 'edit', media: m, back: modal })}
          onClose={() => setModal(null)}
        />
      )}

      {modal?.kind === 'delete' && (
        <ConfirmModal
          title="Remove media?"
          message={`“${modal.media.name}” and its viewing history will be permanently removed from ${tracker.name}.`}
          confirmLabel="Remove"
          onClose={() => setModal(null)}
          onConfirm={async () => {
            await api.deleteMedia(modal.media.id)
            setModal(null)
            refresh()
          }}
        />
      )}
    </main>
  )
}

export default TrackerPage

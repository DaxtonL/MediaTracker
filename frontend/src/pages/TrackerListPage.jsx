import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router'
import { api } from '../api.js'
import { MEDIA_TYPES } from '../media.js'

function TrackerListPage() {
  const navigate = useNavigate()
  const [trackers, setTrackers] = useState(null)
  const [loadError, setLoadError] = useState(null)
  const [name, setName] = useState('')
  const [createError, setCreateError] = useState(null)
  const [creating, setCreating] = useState(false)

  useEffect(() => {
    api
      .listTrackers()
      .then(setTrackers)
      .catch((e) => setLoadError(e.message))
  }, [])

  async function handleCreate(e) {
    e.preventDefault()
    const trimmed = name.trim()
    if (!trimmed) return
    setCreating(true)
    setCreateError(null)
    try {
      const tracker = await api.createTracker(trimmed)
      navigate(`/trackers/${tracker.id}`)
    } catch (err) {
      setCreateError(err.message)
      setCreating(false)
    }
  }

  return (
    <main className="page">
      <header className="hero">
        <div className="hero__icons" aria-hidden>
          {Object.values(MEDIA_TYPES).map((t) => (
            <span key={t.label}>{t.icon}</span>
          ))}
        </div>
        <h1>Media Tracker</h1>
        <p className="hero__tagline">Movies, shows, games, books and manga, all in one place.</p>
      </header>

      <section>
        <h2 className="section-title">Your trackers</h2>

        {loadError && <p className="banner banner--error">Couldn’t load trackers: {loadError}</p>}
        {!trackers && !loadError && <div className="spinner" />}

        {trackers && (
          <div className="tracker-grid">
            {trackers.map((t) => (
              <Link key={t.id} to={`/trackers/${t.id}`} className="tracker-card">
                <span className="tracker-card__initial" aria-hidden>
                  {t.name.trim()[0]?.toUpperCase() ?? '?'}
                </span>
                <span className="tracker-card__name">{t.name}</span>
                <span className="tracker-card__count">
                  {t.mediaCount} {t.mediaCount === 1 ? 'item' : 'items'}
                </span>
              </Link>
            ))}

            <form className="tracker-card tracker-card--new" onSubmit={handleCreate}>
              <span className="tracker-card__name">New tracker</span>
              <input
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Tracker name"
                aria-label="New tracker name"
              />
              {createError && <p className="form__error">{createError}</p>}
              <button type="submit" className="btn btn--primary" disabled={creating || !name.trim()}>
                {creating ? 'Creating…' : 'Create'}
              </button>
            </form>
          </div>
        )}
      </section>
    </main>
  )
}

export default TrackerListPage

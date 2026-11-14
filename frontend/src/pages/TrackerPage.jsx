import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router'

function TrackerPage() {
  const { id } = useParams()            // the "3" from /trackers/3
  const [tracker, setTracker] = useState(null)
  const [media, setMedia] = useState([])
  const [error, setError] = useState(null)

  useEffect(() => {
    fetch(`/api/trackers/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error(`Tracker ${id} not found`)
        return res.json()
      })
      .then(setTracker)
      .catch((e) => setError(e.message))

    fetch(`/api/trackers/${id}/media`)
      .then((res) => res.json())
      .then(setMedia)
  }, [id])                              // re-fetch if the id in the URL changes

  if (error) return <main><p>{error}</p><Link to="/">Back</Link></main>
  if (!tracker) return <main><p>Loading…</p></main>

  return (
    <main>
      <Link to="/">← All trackers</Link>
      <h1>{tracker.name}</h1>
      <table>
        <thead>
          <tr>
            <th>Name</th><th>Type</th><th>Status</th>
            <th>Progress</th><th>Priority</th><th>Rating</th>
          </tr>
        </thead>
        <tbody>
          {media.map((m) => (
            <tr key={m.id}>
              <td>{m.name}</td>
              <td>{m.type}</td>
              <td>{m.status}</td>
              <td>{m.totalProgress}{m.length != null ? ` / ${m.length}` : ''} {m.unit}</td>
              <td>{m.priority ?? '–'}</td>
              <td>{m.rating ?? '–'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </main>
  )
}

export default TrackerPage
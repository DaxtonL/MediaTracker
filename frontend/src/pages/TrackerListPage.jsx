import { useEffect, useState } from 'react'
import { Link } from 'react-router'

function TrackerListPage() {
  const [trackers, setTrackers] = useState([])

  useEffect(() => {
    fetch('/api/trackers')
      .then((res) => res.json())
      .then(setTrackers)
  }, [])

  return (
    <main>
      <h1>Media Tracker</h1>
      <ul>
        {trackers.map((t) => (
          <li key={t.id}>
            <Link to={`/trackers/${t.id}`}>{t.name}</Link> ({t.mediaCount} media)
          </li>
        ))}
      </ul>
    </main>
  )
}

export default TrackerListPage
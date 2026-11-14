import { useEffect, useState } from 'react'

function App() {
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
            {t.name} ({t.mediaCount} media)
          </li>
        ))}
      </ul>
    </main>
  )
}

export default App
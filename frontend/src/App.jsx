import { Routes, Route } from 'react-router'
import TrackerListPage from './pages/TrackerListPage.jsx'
import TrackerPage from './pages/TrackerPage.jsx'

function App() {
  return (
    <Routes>
      <Route path="/" element={<TrackerListPage />} />
      <Route path="/trackers/:id" element={<TrackerPage />} />
    </Routes>
  )
}

export default App
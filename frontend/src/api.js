// Used when the backend doesn't send a `message` in its error body
const FALLBACK_MESSAGES = {
  400: 'Some of the values entered are invalid',
  404: 'Not found. It may have been removed',
  409: 'That name is already in use',
}

// Thin wrapper around fetch that turns non-2xx responses into Errors
// carrying the backend's message (server.error.include-message=always).
async function request(path, { method = 'GET', body } = {}) {
  const res = await fetch(`/api${path}`, {
    method,
    headers: body !== undefined ? { 'Content-Type': 'application/json' } : undefined,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  if (!res.ok) {
    let message = FALLBACK_MESSAGES[res.status] ?? `Request failed (${res.status})`
    try {
      const data = await res.json()
      if (data?.message) message = data.message
    } catch {
      // response had no JSON body; keep the generic message
    }
    throw new Error(message)
  }

  if (res.status === 204) return null
  return res.json()
}

export const api = {
  listTrackers: () => request('/trackers'),
  getTracker: (id) => request(`/trackers/${id}`),
  createTracker: (name) => request('/trackers', { method: 'POST', body: { name } }),

  listMedia: (trackerId, filters = {}) => {
    const params = new URLSearchParams()
    for (const [key, value] of Object.entries(filters)) {
      if (value !== '' && value != null) params.set(key, value)
    }
    const qs = params.toString()
    return request(`/trackers/${trackerId}/media${qs ? `?${qs}` : ''}`)
  },
  getMedia: (id) => request(`/media/${id}`),
  createMedia: (trackerId, media) =>
    request(`/trackers/${trackerId}/media`, { method: 'POST', body: media }),
  updateMedia: (id, changes) => request(`/media/${id}`, { method: 'PATCH', body: changes }),
  deleteMedia: (id) => request(`/media/${id}`, { method: 'DELETE' }),

  logViewing: (id, viewedOn, viewProgress) =>
    request(`/media/${id}/logs`, { method: 'POST', body: { viewedOn, viewProgress } }),
  removeLatestLog: (id) => request(`/media/${id}/logs/latest`, { method: 'DELETE' }),
}

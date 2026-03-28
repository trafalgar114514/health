const STORAGE_KEY = 'health_records'

export function getHealthRecords() {
  return uni.getStorageSync(STORAGE_KEY) || []
}

export function addHealthRecord(record) {
  const records = getHealthRecords()
  records.unshift({
    id: `${Date.now()}_${Math.random().toString(36).slice(2, 8)}`,
    ...record
  })
  uni.setStorageSync(STORAGE_KEY, records)
  return records
}

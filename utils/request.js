const BASE_URL = 'http://127.0.0.1:8080/api'

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('token') || ''

    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {})
      },
      success: (res) => {
        const result = res.data

        // 1. HTTP 状态码不是 200，直接失败
        if (res.statusCode !== 200) {
          reject(result)
          return
        }

        // 2. 后端直接返回数组，例如 /health/list -> [...]
        if (Array.isArray(result)) {
          resolve(result)
          return
        }

        // 3. 后端统一返回对象，例如 { code: 200, data: ... }
        if (result && (result.code === 200 || result.code === 0)) {
          resolve(result)
          return
        }

        // 4. 其他情况都按失败处理
        reject(result)
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}

export default request
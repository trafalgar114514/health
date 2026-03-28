const BASE_URL = 'http://127.0.0.1:8080/api'

export function request(options) {
  return new Promise((resolve, reject) => {
    const token = uni.getStorageSync('HEALTH_TOKEN') || ''

    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {})
      },
      success: (res) => {
        const data = res.data
        if (res.statusCode === 200) {
          resolve(data)
        } else {
          uni.showToast({
            title: data?.message || '请求失败',
            icon: 'none'
          })
          reject(data)
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '连接服务器失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

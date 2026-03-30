import { getToken } from '@/utils/auth'
import request from '@/utils/request'

const BASE_URL = 'http://localhost:8080/api'

export const getTrendApi = (userId, type) => {
  const token = getToken ? getToken() : uni.getStorageSync('token')

  return new Promise((resolve, reject) => {
    uni.request({
      url: `${BASE_URL}/health/trend`,
      method: 'GET',
      data: {
        userId,
        type
      },
      header: {
        ...(token ? { Authorization: 'Bearer ' + token } : {})
      },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data)
        } else {
          reject(res)
        }
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}

export const getHealthListApi = (userId) => {
  return request({
    url: '/health/list',
    method: 'GET',
    data: {
      userId
    }
  })
}

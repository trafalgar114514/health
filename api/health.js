import request from '@/utils/request'

export const getTrendApi = (userId, type) => {
  return request({
    url: '/health/trend',
    method: 'GET',
    data: {
      userId,
      type
    }
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
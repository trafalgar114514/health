import { request } from './request'

export function addHealthRecordApi(data) {
  return request({
    url: '/health/add',
    method: 'POST',
    data
  })
}

export function getTrendApi(userId, type) {
  return request({
    url: `/health/trend?userId=${userId}&type=${type}`,
    method: 'GET'
  })
}

export function getHealthListApi(userId) {
  return request({
    url: `/health/list?userId=${userId}`,
    method: 'GET'
  })
}
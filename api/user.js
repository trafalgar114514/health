import { request } from './request'

export function loginApi(data) {
  return request({
    url: '/user/login',
    method: 'POST',
    data
  })
}

export function getProfileApi() {
  return request({
    url: '/user/profile',
    method: 'GET'
  })
}

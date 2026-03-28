import request from '@/utils/request'

export const registerApi = (data) => {
  return request({
    url: '/user/register',
    method: 'POST',
    data
  })
}

export const loginApi = (data) => {
  return request({
    url: '/user/login',
    method: 'POST',
    data
  })
}

export const getProfileApi = () => {
  return request({
    url: '/user/profile',
    method: 'GET'
  })
}
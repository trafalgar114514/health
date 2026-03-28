import request from '@/utils/request'

export const riskApi = (data) => {
  return request({
    url: '/risk/evaluate',
    method: 'POST',
    data
  })
}
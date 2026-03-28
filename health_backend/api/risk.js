import { request } from './request'

export function riskApi(data){
  return request({
    url: '/risk/evaluate',
    method: 'POST',
    data
  })
}
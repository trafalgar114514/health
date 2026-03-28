import { request } from './request'

export function predictRiskApi(data) {
  return request({
    url: '/risk/predict',
    method: 'POST',
    data
  })
}

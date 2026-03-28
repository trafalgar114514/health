const TOKEN_KEY = 'HEALTH_TOKEN'
const USER_KEY = 'HEALTH_USER'

export function setToken(token) {
  uni.setStorageSync(TOKEN_KEY, token)
}

export function getToken() {
  return uni.getStorageSync(TOKEN_KEY)
}

export function clearToken() {
  uni.removeStorageSync(TOKEN_KEY)
  uni.removeStorageSync(USER_KEY)
}

export function setUserInfo(userInfo) {
  uni.setStorageSync(USER_KEY, userInfo)
}

export function getUserInfo() {
  return uni.getStorageSync(USER_KEY)
}
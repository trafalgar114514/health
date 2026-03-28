<template>
  <view class="container">
    <view class="login-box">
      <view class="title">登录</view>

      <input
        class="input"
        v-model="form.username"
        placeholder="请输入用户名"
      />

      <input
        class="input"
        v-model="form.password"
        password
        placeholder="请输入密码"
      />

      <button class="btn" @click="handleLogin">登录</button>
      <button class="btn gray" @click="goRegister">去注册</button>
    </view>
  </view>
</template>

<script setup>
import { reactive } from 'vue'
import { loginApi } from '@/api/user'

const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!form.username || !form.password) {
    uni.showToast({
      title: '请输入用户名和密码',
      icon: 'none'
    })
    return
  }

  try {
    const res = await loginApi(form)

    if (res.code === 200) {
      uni.setStorageSync('token', res.data.token)
      uni.setStorageSync('userInfo', res.data.user)

      uni.showToast({
        title: '登录成功',
        icon: 'success'
      })

      setTimeout(() => {
        uni.switchTab({
          url: '/pages/index/index'
        })
      }, 500)
    }
  } catch (e) {
    console.log('登录失败', e)
  }
}

const goRegister = () => {
  uni.navigateTo({
    url: '/pages/register/register'
  })
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 40rpx;
  box-sizing: border-box;
}

.login-box {
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
  margin-top: 80rpx;
}

.title {
  font-size: 40rpx;
  font-weight: bold;
  text-align: center;
  margin-bottom: 40rpx;
}

.input {
  height: 80rpx;
  border: 1px solid #ddd;
  border-radius: 12rpx;
  padding: 0 20rpx;
  margin-bottom: 20rpx;
  box-sizing: border-box;
  background: #fff;
}

.btn {
  margin-top: 20rpx;
  background: #007aff;
  color: #fff;
}

.gray {
  background: #999;
}
</style>
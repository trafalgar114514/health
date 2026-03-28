<template>
  <view class="container">
    <view class="login-box">
      <view class="title">个人健康管理</view>
      <view class="subtitle">登录后开始记录你的健康数据</view>

      <input
        class="input"
        v-model="form.username"
        placeholder="请输入用户名"
      />

      <input
        class="input"
        v-model="form.password"
        type="password"
        placeholder="请输入密码"
      />

      <button class="btn" @click="handleLogin">登录</button>
    </view>
  </view>
</template>

<script setup>
import { reactive } from 'vue'
import { loginApi, getProfileApi } from '@/api/user'
import { setToken, setUserInfo } from '@/utils/auth'

const form = reactive({
  username: 'test',
  password: '123456'
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
      setToken(res.data.token)

      const profileRes = await getProfileApi()
      if (profileRes.code === 200) {
        setUserInfo(profileRes.data)
      }

      uni.showToast({
        title: '登录成功',
        icon: 'success'
      })

      setTimeout(() => {
        uni.reLaunch({
          url: '/pages/index/index'
        })
      }, 300)
    } else {
      uni.showToast({
        title: res.message || '登录失败',
        icon: 'none'
      })
    }
  } catch (e) {
    console.error('登录异常：', e)
    uni.showToast({
      title: '登录异常',
      icon: 'none'
    })
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 40rpx;
  box-sizing: border-box;
}

.login-box {
  margin-top: 160rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 40rpx 30rpx;
  box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.05);
}

.title {
  font-size: 42rpx;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 20rpx;
}

.subtitle {
  font-size: 26rpx;
  color: #999;
  text-align: center;
  margin-bottom: 50rpx;
}

.input {
  height: 88rpx;
  border: 1rpx solid #e5e6eb;
  border-radius: 12rpx;
  padding: 0 24rpx;
  margin-bottom: 24rpx;
  background: #fafafa;
}

.btn {
  margin-top: 20rpx;
  background: #3c9cff;
  color: #fff;
  border-radius: 12rpx;
}
</style>